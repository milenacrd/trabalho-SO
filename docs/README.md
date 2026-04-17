# Trabalho Prático 1: Algoritmo do Banqueiro

## Descrição
Implementação multithreaded do Algoritmo do Banqueiro para prevenção de deadlocks em Sistemas Operacionais. O programa simula 5 clientes solicitando e liberando recursos de 3 tipos, garantindo que o sistema permaneça em estado seguro.

## Requisitos
- **Linguagem**: Java
- **Estruturas**:
  - `available[3]`: Recursos disponíveis (passados por linha de comando)
  - `maximum[5][3]`: Demanda máxima de cada cliente
  - `allocation[5][3]`: Recursos alocados atualmente
  - `need[5][3]`: Recursos remanescentes necessários
- **Funções**:
  - `request_resources(int customer_num, int[] request)`: Solicita recursos se seguro
  - `release_resources(int customer_num, int[] release)`: Libera recursos
- **Threads**: 5 threads clientes em loop infinito, usando mutex para sincronização
- **Entrada**: `./java BankersAlgorithm <rec1> <rec2> <rec3>` (ex.: 10 5 7)

## Algoritmo de Segurança (Pseudocódigo)
```
isSafe():
  work = copy of available
  finish[5] = false
  while true:
    found = false
    for each customer i:
      if !finish[i] and need[i] <= work:
        work += allocation[i]
        finish[i] = true
        found = true
    if !found: break
  return all finish[i] == true
```

## Fluxo do Programa
1. Inicializar available, maximum, allocation, need
2. Criar 5 threads clientes
3. Cada cliente: request -> (se aprovado) trabalho -> release -> loop
4. Usar synchronized ou ReentrantLock para proteger acesso aos arrays

## Referências
- Silberschatz, A.; Galvin, P. B.; Gagne, G. Fundamentos de sistemas operacionais. 9. ed. Rio de Janeiro: LTC, 2015. (Seções 7.5.3 e 5.9.4)

## Compilação e Execução
```bash
javac src/BankersAlgorithm.java
java -cp src BankersAlgorithm 10 5 7
```

## Estrutura do Projeto
- `/src`: Código fonte
- `/docs`: Documentação
- `/tests`: Casos de teste
- `README.md`: Este arquivo
- `TODO.md`: Milestones do projeto

