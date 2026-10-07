# Trabalho-fase-3-dia-25-fiap

## Testes automatizados com Docker (PowerShell)

Na raiz do projeto, execute:

```powershell
.\scripts\test.ps1
```

O script usa `compose.test.yml` com o projeto Compose `smartcollector-tests`.
Ele inicia outro Oracle, sem portas publicadas, espera o healthcheck e executa
`mvn -B -ntp clean verify` em um container Maven com Java 21.
Somente `pom.xml` e `src` sao montados para leitura; o build acontece numa copia
interna do container. O `.env` local nao e montado no container de testes.
As credenciais fixas desse Compose pertencem exclusivamente ao banco de testes.

O banco e a rede de testes sao separados do ambiente local. Ao terminar, os
containers de testes sao parados. O volume de testes e o cache Maven ficam
preservados; o banco da aplicacao local permanece em execucao.

O codigo de saida e diferente de zero se a execucao falhar. Os relatorios
Surefire sao copiados para `test-results/surefire-reports`, ignorado pelo Git.
O teste existente `contextLoads()` verifica se o contexto Spring inicia;
ele nao verifica os fluxos completos da API.
