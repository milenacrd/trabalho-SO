
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class BankersAlgorithm {
    static final int NUMBER_OF_CUSTOMERS = 5;
    static final int NUMBER_OF_RESOURCES = 3;

    static final ReentrantLock lock = new ReentrantLock();

    static int[] available = new int[NUMBER_OF_RESOURCES];
    static int[][] maximum = new int[NUMBER_OF_CUSTOMERS][NUMBER_OF_RESOURCES];
    static int[][] allocation = new int[NUMBER_OF_CUSTOMERS][NUMBER_OF_RESOURCES];
    static int[][] need = new int[NUMBER_OF_CUSTOMERS][NUMBER_OF_RESOURCES];

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }

        if (args.length == NUMBER_OF_RESOURCES + 1 && args[NUMBER_OF_RESOURCES].equalsIgnoreCase("simulate")) {
            initData(Arrays.copyOf(args, NUMBER_OF_RESOURCES));
            printInitialState();
            runSimulation();
            return;
        }

        if (args.length == NUMBER_OF_RESOURCES) {
            initData(Arrays.copyOf(args, NUMBER_OF_RESOURCES));
            printInitialState();

            boolean safe = isSafe();
            System.out.println("Estado seguro? " + safe);
            return;
        }

        if (args.length == NUMBER_OF_RESOURCES + 2 + NUMBER_OF_RESOURCES
                && args[NUMBER_OF_RESOURCES].equalsIgnoreCase("request")) {
            String[] availableArgs = Arrays.copyOf(args, NUMBER_OF_RESOURCES);
            initData(availableArgs);
            printInitialState();

            try {
                int customerNum = Integer.parseInt(args[NUMBER_OF_RESOURCES + 1]);
                int[] request = new int[NUMBER_OF_RESOURCES];
                for (int i = 0; i < NUMBER_OF_RESOURCES; i++) {
                    request[i] = Integer.parseInt(args[NUMBER_OF_RESOURCES + 2 + i]);
                }

                System.out.println("\nTeste de requestResources para cliente " + customerNum + ", pedido " + Arrays.toString(request));
                int result = requestResources(customerNum, request);
                System.out.println("Resultado: " + (result == 0 ? "Aprovado" : "Negado"));
                printInitialState();
                boolean safe = isSafe();
                System.out.println("Estado seguro após request? " + safe);
            } catch (NumberFormatException e) {
                System.err.println("ERRO: Argumentos numéricos inválidos para request. Use apenas números inteiros.");
                printUsage();
            }
            return;
        }

        if (args.length == NUMBER_OF_RESOURCES + 2 + NUMBER_OF_RESOURCES
                && args[NUMBER_OF_RESOURCES].equalsIgnoreCase("release")) {
            String[] availableArgs = Arrays.copyOf(args, NUMBER_OF_RESOURCES);
            initData(availableArgs);
            printInitialState();

            try {
                int customerNum = Integer.parseInt(args[NUMBER_OF_RESOURCES + 1]);
                int[] release = new int[NUMBER_OF_RESOURCES];
                for (int i = 0; i < NUMBER_OF_RESOURCES; i++) {
                    release[i] = Integer.parseInt(args[NUMBER_OF_RESOURCES + 2 + i]);
                }

                System.out.println("\nTeste de releaseResources para cliente " + customerNum + ", liberando " + Arrays.toString(release));
                int result = releaseResources(customerNum, release);
                System.out.println("Resultado: " + (result == 0 ? "Liberado" : "Inválido"));
                printInitialState();
                boolean safe = isSafe();
                System.out.println("Estado seguro após release? " + safe);
            } catch (NumberFormatException e) {
                System.err.println("ERRO: Argumentos numéricos inválidos para release. Use apenas números inteiros.");
                printUsage();
            }
            return;
        }

        printUsage();
    }

    static void printUsage() {
        System.out.println("Uso:");
        System.out.println("  java -cp src BankersAlgorithm <r1> <r2> <r3>");
        System.out.println("  java -cp src BankersAlgorithm <r1> <r2> <r3> simulate");
        System.out.println("  java -cp src BankersAlgorithm <r1> <r2> <r3> request <customer> <q1> <q2> <q3>");
        System.out.println("  java -cp src BankersAlgorithm <r1> <r2> <r3> release <customer> <q1> <q2> <q3>");
    }

    static void initData(String[] args) {
        if (args.length != NUMBER_OF_RESOURCES) {
            printUsage();
            System.exit(1);
        }

        try {
            for (int i = 0; i < NUMBER_OF_RESOURCES; i++) {
                available[i] = Integer.parseInt(args[i]);
                if (available[i] < 0) {
                    System.err.println("ERRO: Recursos disponíveis não podem ser negativos. Valor fornecido: " + available[i]);
                    System.exit(1);
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("ERRO: Recursos disponíveis devem ser números inteiros não-negativos.");
            printUsage();
            System.exit(1);
        }

        initMaximum();
        initAllocation();
        calculateNeed();
    }

    static void printInitialState() {
        System.out.println("=== Configuração Inicial ===");
        System.out.println("Available: " + Arrays.toString(available));
        System.out.println("Maximum:");
        printMatrix(maximum);
        System.out.println("Allocation:");
        printMatrix(allocation);
        System.out.println("Need:");
        printMatrix(need);
    }

    static void initMaximum() {
        Random random = new Random(12345);
        for (int i = 0; i < NUMBER_OF_CUSTOMERS; i++) {
            for (int j = 0; j < NUMBER_OF_RESOURCES; j++) {
                maximum[i][j] = 1 + random.nextInt(10);
            }
        }
    }

    static void initAllocation() {
        for (int i = 0; i < NUMBER_OF_CUSTOMERS; i++) {
            Arrays.fill(allocation[i], 0);
        }
    }

    static void calculateNeed() {
        for (int i = 0; i < NUMBER_OF_CUSTOMERS; i++) {
            for (int j = 0; j < NUMBER_OF_RESOURCES; j++) {
                need[i][j] = maximum[i][j] - allocation[i][j];
            }
        }
    }

    static boolean isSafe() {
        lock.lock();
        try {
            int[] work = Arrays.copyOf(available, available.length);
            boolean[] finish = new boolean[NUMBER_OF_CUSTOMERS];
            int[] safeSequence = new int[NUMBER_OF_CUSTOMERS];
            int count = 0;

            while (count < NUMBER_OF_CUSTOMERS) {
                boolean found = false;
                for (int i = 0; i < NUMBER_OF_CUSTOMERS; i++) {
                    if (!finish[i] && canSatisfy(need[i], work)) {
                        for (int j = 0; j < NUMBER_OF_RESOURCES; j++) {
                            work[j] += allocation[i][j];
                        }
                        safeSequence[count++] = i;
                        finish[i] = true;
                        found = true;
                    }
                }

                if (!found) {
                    break;
                }
            }

            boolean safe = count == NUMBER_OF_CUSTOMERS;
            if (safe) {
                System.out.print("Sequência segura: ");
                for (int i = 0; i < count; i++) {
                    System.out.print(safeSequence[i]);
                    if (i < count - 1) {
                        System.out.print(" -> ");
                    }
                }
                System.out.println();
            }
            return safe;
        } finally {
            lock.unlock();
        }
    }

    static int requestResources(int customerNum, int[] request) {
        lock.lock();
        try {
            if (customerNum < 0 || customerNum >= NUMBER_OF_CUSTOMERS) {
                System.err.println("ERRO: Cliente " + customerNum + " inválido. Deve estar entre 0 e " + (NUMBER_OF_CUSTOMERS - 1));
                return -1;
            }

            if (request == null || request.length != NUMBER_OF_RESOURCES) {
                System.err.println("ERRO: Request inválido para cliente " + customerNum + ". Deve ter " + NUMBER_OF_RESOURCES + " recursos");
                return -1;
            }

            if (!canSatisfy(request, need[customerNum])) {
                System.out.println("Cliente " + customerNum + " - Request " + Arrays.toString(request) + " negado: excede necessidade " + Arrays.toString(need[customerNum]));
                return -1;
            }

            if (!canSatisfy(request, available)) {
                System.out.println("Cliente " + customerNum + " - Request " + Arrays.toString(request) + " negado: recursos insuficientes. Available: " + Arrays.toString(available));
                return -1;
            }

            // Simular alocação
            for (int j = 0; j < NUMBER_OF_RESOURCES; j++) {
                available[j] -= request[j];
                allocation[customerNum][j] += request[j];
                need[customerNum][j] -= request[j];
            }

            if (isSafe()) {
                System.out.println("Cliente " + customerNum + " - Request " + Arrays.toString(request) + " APROVADO. Estado seguro mantido.");
                return 0;
            }

            // Reverter alocação se não for seguro
            for (int j = 0; j < NUMBER_OF_RESOURCES; j++) {
                available[j] += request[j];
                allocation[customerNum][j] -= request[j];
                need[customerNum][j] += request[j];
            }

            System.out.println("Cliente " + customerNum + " - Request " + Arrays.toString(request) + " negado: causaria estado inseguro.");
            return -1;
        } finally {
            lock.unlock();
        }
    }

    static int releaseResources(int customerNum, int[] release) {
        lock.lock();
        try {
            if (customerNum < 0 || customerNum >= NUMBER_OF_CUSTOMERS) {
                System.err.println("ERRO: Cliente " + customerNum + " inválido. Deve estar entre 0 e " + (NUMBER_OF_CUSTOMERS - 1));
                return -1;
            }

            if (release == null || release.length != NUMBER_OF_RESOURCES) {
                System.err.println("ERRO: Release inválido para cliente " + customerNum + ". Deve ter " + NUMBER_OF_RESOURCES + " recursos");
                return -1;
            }

            for (int j = 0; j < NUMBER_OF_RESOURCES; j++) {
                if (release[j] < 0 || release[j] > allocation[customerNum][j]) {
                    System.err.println("ERRO: Cliente " + customerNum + " - Release " + Arrays.toString(release) + " inválido: release[" + j + "]=" + release[j] + " > allocation[" + j + "]=" + allocation[customerNum][j]);
                    return -1;
                }
            }

            for (int j = 0; j < NUMBER_OF_RESOURCES; j++) {
                available[j] += release[j];
                allocation[customerNum][j] -= release[j];
                need[customerNum][j] += release[j];
            }

            System.out.println("Cliente " + customerNum + " - Release " + Arrays.toString(release) + " efetuado. Available agora: " + Arrays.toString(available));
            return 0;
        } finally {
            lock.unlock();
        }
    }

    static void runSimulation() {
        Thread[] threads = new Thread[NUMBER_OF_CUSTOMERS];

        for (int i = 0; i < NUMBER_OF_CUSTOMERS; i++) {
            threads[i] = new Thread(new CustomerThread(i));
            threads[i].start();
        }

        for (int i = 0; i < NUMBER_OF_CUSTOMERS; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("\nSimulação finalizada.");
        printInitialState();
        System.out.println("Estado seguro? " + isSafe());
    }

    static class CustomerThread implements Runnable {
        private final int customerNum;
        private final Random random = new Random();

        CustomerThread(int customerNum) {
            this.customerNum = customerNum;
        }

        @Override
        public void run() {
            for (int iteration = 0; iteration < 10; iteration++) {
                int[] request = generateRequest();
                if (request != null) {
                    int result = requestResources(customerNum, request);
                    if (result == 0) {
                        System.out.println("[Cliente " + customerNum + "] request aprovado: " + Arrays.toString(request));
                        sleepRandom(200, 500);
                        int[] release = generateRelease();
                        if (release != null) {
                            int releaseResult = releaseResources(customerNum, release);
                            if (releaseResult == 0) {
                                System.out.println("[Cliente " + customerNum + "] release efetuado: " + Arrays.toString(release));
                            }
                        }
                    } else {
                        System.out.println("[Cliente " + customerNum + "] request negado: " + Arrays.toString(request));
                    }
                }
                sleepRandom(100, 300);
            }
        }

        private int[] generateRequest() {
            int[] request = new int[NUMBER_OF_RESOURCES];
            int total = 0;
            for (int i = 0; i < NUMBER_OF_RESOURCES; i++) {
                int max = need[customerNum][i];
                request[i] = max > 0 ? random.nextInt(max + 1) : 0;
                total += request[i];
            }
            return total > 0 ? request : null;
        }

        private int[] generateRelease() {
            int[] release = new int[NUMBER_OF_RESOURCES];
            int total = 0;
            for (int i = 0; i < NUMBER_OF_RESOURCES; i++) {
                int max = allocation[customerNum][i];
                release[i] = max > 0 ? random.nextInt(max + 1) : 0;
                total += release[i];
            }
            return total > 0 ? release : null;
        }

        private void sleepRandom(int minMillis, int maxMillis) {
            try {
                Thread.sleep(minMillis + random.nextInt(maxMillis - minMillis + 1));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static boolean canSatisfy(int[] request, int[] available) {
        for (int i = 0; i < request.length; i++) {
            if (request[i] > available[i]) {
                return false;
            }
        }
        return true;
    }

    static void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            System.out.println("Cliente " + i + ": " + Arrays.toString(matrix[i]));
        }
    }
}
