# Testes - Algoritmo do Banqueiro

Esta pasta contém a suite de testes automatizados para validar a implementação do Algoritmo do Banqueiro.

## Estrutura dos Testes

### Arquivos Principais
- `BankersAlgorithmTest.java` - Classe principal de testes unitários
- `run_tests.bat` - Script para executar todos os testes

### Tipos de Testes Planejados

#### 1. Testes Unitários
- Validação do algoritmo de segurança `isSafe()`
- Testes de `requestResources()` e `releaseResources()`
- Verificação de estados seguros vs inseguros

#### 2. Testes de Integração
- Cenários completos de simulação
- Interação entre múltiplos clientes
- Validação de sincronização

#### 3. Testes de Concorrência
- Comportamento multithread
- Prevenção de race conditions
- Validação de `ReentrantLock`

#### 4. Testes de Performance
- Carga com muitos threads
- Medição de throughput
- Escalabilidade

#### 5. Testes de Cenários Extremos
- Deadlock inevitável
- Starvation de recursos
- Recursos insuficientes
- Edge cases de validação

## Como Executar

### Compilar
```bash
javac -cp ../src BankersAlgorithmTest.java
```

### Executar
```bash
java -cp "../src;." BankersAlgorithmTest
```

### Usar Script
```bash
run_tests.bat
```

## Dados de Teste

Para garantir reprodutibilidade, usamos dados fixos de teste:

- **Available**: [10, 5, 7] (3 tipos de recursos)
- **Maximum** (5 clientes):
  - Cliente 0: [2, 1, 2]
  - Cliente 1: [9, 6, 5]
  - Cliente 2: [6, 3, 2]
  - Cliente 3: [10, 8, 3]
  - Cliente 4: [1, 7, 3]

## Relatórios de Teste

Cada execução gera um relatório com:
- Número total de testes
- Testes aprovados/reprovados
- Taxa de sucesso
- Detalhes de falhas

## Status Atual

✅ **Mini Task 3.1**: Estrutura básica criada
🔄 **Próxima**: Mini Task 3.2 - Testes do algoritmo de segurança</content>
<parameter name="filePath">c:\Users\Mi-Mi\Downloads\trabalho-SO\tests\README.md