# Executa os testes existentes e devolve o codigo de saida ao chamador.
$ErrorActionPreference = 'Stop'
$testProjectRoot = Split-Path -Parent $PSScriptRoot
Push-Location -LiteralPath $testProjectRoot
try {
    docker compose -p smartcollector-tests -f compose.test.yml config --quiet
    if ($LASTEXITCODE -ne 0) { throw 'Configuracao de testes invalida.' }
    docker compose -p smartcollector-tests -f compose.test.yml up --abort-on-container-exit --exit-code-from tests --attach tests
    $testExitCode = $LASTEXITCODE
    New-Item -ItemType Directory -Path test-results -Force | Out-Null
    docker compose -p smartcollector-tests -f compose.test.yml cp tests:/workspace/target/surefire-reports ./test-results/
    $reportExitCode = $LASTEXITCODE
    if ($reportExitCode -ne 0) { Write-Warning 'Nao foi possivel copiar os relatorios; confira os logs do container tests.' }
    if (($testExitCode -eq 0) -and ($reportExitCode -ne 0)) { $testExitCode = 1 }
} finally {
    docker compose -p smartcollector-tests -f compose.test.yml stop
    Pop-Location
}
exit $testExitCode
