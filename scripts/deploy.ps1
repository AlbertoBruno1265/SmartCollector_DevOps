param(
    [Parameter(Mandatory=$true)]
    [ValidateSet('staging', 'production')]
    [string]$Environment,
    [string]$Image = 'smartcollector:local'
)
$ErrorActionPreference = 'Stop'
$deploymentRoot = Split-Path -Parent $PSScriptRoot
$previousImage = $env:APP_IMAGE
$previousPort = $env:APP_PORT
Push-Location -LiteralPath $deploymentRoot
try {
    if (-not (Test-Path -LiteralPath '.env')) {
        throw 'Copie .env.example para .env e preencha os valores primeiro.'
    }
    $env:APP_IMAGE = $Image
    $env:APP_PORT = if ($Environment -eq 'staging') { '8081' } else { '8082' }
    docker compose -p "smartcollector-$Environment" -f compose.deploy.yml up -d --wait --wait-timeout 360
    if ($LASTEXITCODE -ne 0) { throw 'Falha no deploy. Confira os logs do Compose.' }
    Write-Host "Containers iniciados. API: http://localhost:$($env:APP_PORT)"
    Write-Host 'O inicio dos containers nao substitui a verificacao da API descrita no README.'
} finally {
    $env:APP_IMAGE = $previousImage
    $env:APP_PORT = $previousPort
    Pop-Location
}
