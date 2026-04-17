
import java.util.Arrays;
import java.util.Random;

public class BankersAlgorithm {
    static final int NUMBER_OF_CUSTOMERS = 5;
    static final int NUMBER_OF_RESOURCES = 3;

    static int[] available = new int[NUMBER_OF_RESOURCES];
    static int[][] maximum = new int[NUMBER_OF_CUSTOMERS][NUMBER_OF_RESOURCES];
    static int[][] allocation = new int[NUMBER_OF_CUSTOMERS][NUMBER_OF_RESOURCES];
    static int[][] need = new int[NUMBER_OF_CUSTOMERS][NUMBER_OF_RESOURCES];

    public static void main(String[] args) {
        initData(args);
        printInitialState();

        boolean safe = isSafe();
        System.out.println("Estado seguro? " + safe);
    }

    static void initData(String[] args) {
        if (args.length != NUMBER_OF_RESOURCES) {
            System.out.println("Uso: java BankersAlgorithm <rec1> <rec2> <rec3>");
            System.exit(1);
        }

        for (int i = 0; i < NUMBER_OF_RESOURCES; i++) {
            available[i] = Integer.parseInt(args[i]);
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
