# Trabalho Prático 1: Algoritmo do Banqueiro

## Descrição
Implementação multithreaded em Java do Algoritmo do Banqueiro para prevenção de deadlocks. O programa simula 5 clientes solicitando e liberando recursos de 3 tipos, controlando `available`, `maximum`, `allocation` e `need`.

## Estrutura do Projeto
- `src/`: código fonte Java
- `tests/`: scripts de teste
- `.gitignore`: ignora arquivos de build e IDE

## Compilação
No terminal, dentro da pasta do projeto:

```bash
javac src\BankersAlgorithm.java
```

## Execução normal
Para executar com os recursos iniciais passados por linha de comando:

```bash
java -cp src BankersAlgorithm 10 5 7
```

Substitua `10 5 7` pelos valores de `available` que deseja testar.

## Modo de teste `requestResources`
O programa também tem um modo especial para testar `requestResources` diretamente:

```bash
java -cp src BankersAlgorithm 12 8 7 request 0 1 1 1
```

Nesse exemplo:
- `12 8 7` são os recursos `available`
- `request` ativa o modo de teste
- `0` é o índice do cliente
- `1 1 1` é o vetor de recursos solicitados

O programa exibirá se a solicitação foi aprovada ou negada e se o sistema permaneceu em estado seguro.

## Script de teste automático
Há um script de teste dentro de `tests/run_tests.bat` para rodar várias execuções automaticamente.

### Uso do script de teste
Abra o terminal na pasta do projeto e execute:

```bash
tests\run_tests.bat
```

O script fará:
- compilação do código
- execução de várias entradas de `available`
- um teste de `requestResources`

