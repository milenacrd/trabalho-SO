# Trabalho Prático 1: Algoritmo do Banqueiro

## Descrição
Implementação multithreaded em Java do Algoritmo do Banqueiro para prevenção de deadlocks. O programa simula 5 clientes solicitando e liberando recursos de 3 tipos, controlando `available`, `maximum`, `allocation` e `need`.

**Status da Implementação: Fase 2 Concluída ✅ | Fase 3: Testes e Validação (Iniciada)**
- ✅ Algoritmo de segurança `isSafe()` implementado
- ✅ Método `requestResources()` com validação e rollback
- ✅ Método `releaseResources()` com validação
- ✅ Threads clientes com comportamento de loop
- ✅ Sincronização completa com `ReentrantLock`
- ✅ Logs detalhados e tratamento de erros
- ✅ Testes de validação realizados
- 🔄 **Fase 3**: Suite de testes automatizados (estrutura criada)

## Estrutura do Projeto
- `src/BankersAlgorithm.java`: implementação principal
- `docs/`: documentação e planejamento
- `tests/`: scripts de teste (futuro)

## Compilação
```bash
javac src/BankersAlgorithm.java
```

## Modos de Execução

### 1. Verificar estado inicial
```bash
java -cp src BankersAlgorithm 10 5 7
```
Exibe configuração inicial e se o estado é seguro.

### 2. Modo simulação (threads)
```bash
java -cp src BankersAlgorithm 20 15 10 simulate
```
Executa 5 threads clientes fazendo requests/releases aleatórios por 10 iterações cada.

### 3. Testar request específico
```bash
java -cp src BankersAlgorithm 10 5 7 request 0 1 0 0
```
Testa uma solicitação específica: cliente 0 solicita [1,0,0].

### 4. Testar release específico
```bash
java -cp src BankersAlgorithm 10 5 7 release 0 1 0 0
```
Testa uma liberação específica: cliente 0 libera [1,0,0].

## Funcionalidades Implementadas

### Algoritmo de Segurança
- Implementa o algoritmo clássico do banqueiro
- Calcula sequência segura quando possível
- Protegido por `ReentrantLock` para acesso thread-safe

### Gerenciamento de Recursos
- **Request**: Valida necessidade, disponibilidade e segurança
- **Release**: Valida alocação atual e atualiza estado
- Rollback automático em caso de request inseguro

### Sincronização
- `ReentrantLock` protege todas as operações compartilhadas
- Previne condições de corrida entre threads
- Garante consistência dos arrays `available`, `allocation`, `need`

### Logs e Tratamento de Erros
- Mensagens detalhadas para cada operação
- Indicação clara de motivo de negação (recursos insuficientes vs estado inseguro)
- Tratamento de argumentos inválidos
- Validação de entrada com mensagens de erro

## Exemplos de Saída

### Simulação com recursos suficientes:
```
Cliente 0 - Request [1, 0, 0] APROVADO. Estado seguro mantido.
Sequência segura: 0 -> 1 -> 2 -> 3 -> 4
Cliente 2 - Release [3, 2, 1] efetuado. Available agora: [9, 9, 4]
```

### Request negado por estado inseguro:
```
Cliente 0 - Request [1, 0, 0] negado: causaria estado inseguro.
```

### Request negado por recursos insuficientes:
```
Cliente 3 - Request [7, 8, 2] negado: recursos insuficientes. Available: [5, 3, 2]
```

## Tratamento de Erros
- Cliente inválido: "ERRO: Cliente 10 inválido. Deve estar entre 0 e 4"
- Recursos negativos: "ERRO: Recursos disponíveis não podem ser negativos"
- Argumentos não numéricos: "ERRO: Argumentos numéricos inválidos"

O script fará:
- compilação do código
- execução de várias entradas de `available`
- um teste de `requestResources`

