$ErrorActionPreference = "Stop"

# Load/performance benchmark for BankersAlgorithm simulate mode.
# Each simulate run uses internal multithreading (5 customers x 10 iterations).
# Parallel process levels emulate larger effective client populations.

$levels = @(1,2,4,8)
$rounds = 3
$all = @()

foreach ($lvl in $levels) {
    for ($r = 1; $r -le $rounds; $r++) {
        $sw = [System.Diagnostics.Stopwatch]::StartNew()
        $jobs = @()

        for ($i = 1; $i -le $lvl; $i++) {
            $jobs += Start-Job -ScriptBlock {
                param($wd)
                Set-Location $wd
                $o = java -cp src BankersAlgorithm 10 5 7 simulate 2>&1 | Out-String
                [pscustomobject]@{
                    HasSafe      = ($o -match 'Estado seguro\?')
                    HasException = ($o -match 'Exception|NullPointer|OutOfMemoryError|StackOverflowError')
                    HasFinalize  = ($o -match 'Sim')
                }
            } -ArgumentList (Get-Location).Path
        }

        $res = $jobs | Wait-Job | Receive-Job
        $jobs | Remove-Job -Force
        $sw.Stop()

        $okSafe = (($res | Where-Object { -not $_.HasSafe }).Count -eq 0)
        $okFinalize = (($res | Where-Object { -not $_.HasFinalize }).Count -eq 0)
        $anyExc = (($res | Where-Object { $_.HasException }).Count -gt 0)

        $seconds = [math]::Max($sw.Elapsed.TotalSeconds, 0.001)
        $reqAttempts = $lvl * 50
        $throughput = [math]::Round($reqAttempts / $seconds, 2)

        $all += [pscustomobject]@{
            Level        = $lvl
            Round        = $r
            TotalMs      = $sw.ElapsedMilliseconds
            ReqAttempts  = $reqAttempts
            ReqPerSec    = $throughput
            AllSafe      = $okSafe
            AllFinalize  = $okFinalize
            AnyException = $anyExc
        }
    }
}

$all | Sort-Object Level,Round | Format-Table -AutoSize
""
"--- AGGREGATE ---"

$all | Group-Object Level | ForEach-Object {
    $avgMs = [math]::Round((($_.Group | Measure-Object TotalMs -Average).Average), 2)
    $avgRps = [math]::Round((($_.Group | Measure-Object ReqPerSec -Average).Average), 2)
    $minRps = [math]::Round((($_.Group | Measure-Object ReqPerSec -Minimum).Minimum), 2)
    $maxRps = [math]::Round((($_.Group | Measure-Object ReqPerSec -Maximum).Maximum), 2)
    $allSafe = ((($_.Group | Where-Object { -not $_.AllSafe }).Count) -eq 0)
    $allFinalize = ((($_.Group | Where-Object { -not $_.AllFinalize }).Count) -eq 0)
    $anyExc = ((($_.Group | Where-Object { $_.AnyException }).Count) -gt 0)

    "LEVEL=$($_.Name) AVG_MS=$avgMs AVG_REQ_S=$avgRps MIN_REQ_S=$minRps MAX_REQ_S=$maxRps ALL_SAFE=$allSafe ALL_FINALIZE=$allFinalize ANY_EXCEPTION=$anyExc"
}
