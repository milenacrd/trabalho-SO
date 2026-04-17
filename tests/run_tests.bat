@echo off
REM Script de testes para o Trabalho Prático 1 - Algoritmo do Banqueiro
cd /d %~dp0\..

REM Compilar o código Java
javac src\BankersAlgorithm.java
if %ERRORLEVEL% neq 0 (
    echo Erro de compilação. Verifique o código e tente novamente.
    pause
    exit /b 1
)

echo.
echo -------- Teste 1: Available = 10 5 7 --------
java -cp src BankersAlgorithm 10 5 7

echo.
echo -------- Teste 2: Available = 12 8 7 --------
java -cp src BankersAlgorithm 12 8 7

echo.
echo -------- Teste 3: Available = 5 5 5 --------
java -cp src BankersAlgorithm 5 5 5

echo.
echo -------- Teste 4: Available = 3 2 2 --------
java -cp src BankersAlgorithm 3 2 2

echo.
echo -------- Teste 5: requestResources cliente 0 [1,1,1] --------
java -cp src BankersAlgorithm 12 8 7 request 0 1 1 1

echo.
echo Testes concluídos.
pause