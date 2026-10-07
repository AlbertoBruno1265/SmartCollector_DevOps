"""Verifica a API real e gera evidencias sem registrar senhas ou tokens."""
import html
import json
import os
from pathlib import Path
import secrets
import sys
import time
from datetime import datetime, timezone
from urllib.error import HTTPError, URLError
from urllib.request import Request, urlopen

environment, base_url = sys.argv[1:3]
if environment not in ("staging", "production"):
    raise SystemExit("Ambiente invalido")
output = Path("evidencias")
output.mkdir(exist_ok=True)
rows = []

def request(method, path, body=None, token=None):
    headers = {"Content-Type": "application/json"}
    if token:
        headers["Authorization"] = "Bearer " + token
    data = json.dumps(body).encode("utf-8") if body is not None else None
    req = Request(base_url + path, data=data, headers=headers, method=method)
    try:
        with urlopen(req, timeout=10) as response:
            return response.status, response.read().decode("utf-8")
    except HTTPError as error:
        return error.code, error.read().decode("utf-8")

deadline = time.monotonic() + 180
while True:
    try:
        ready_status, _ = request("GET", "/itens")
        if ready_status in (200, 401, 403):
            break
    except (URLError, TimeoutError, ConnectionError):
        pass
    if time.monotonic() >= deadline:
        raise SystemExit("API nao iniciou em 180 segundos")
    time.sleep(2)

email = "evidence-" + secrets.token_hex(8) + "@example.test"
password = secrets.token_urlsafe(24)
status, body = request("POST", "/auth/registro",
                       {"nome": "Demonstracao " + environment,
                        "email": email, "senha": password, "funcao": "USER"})
rows.append({"operacao": "Cadastrar usuario", "metodo": "POST",
             "endpoint": "/auth/registro", "http": status, "resposta": body})
if status != 200:
    raise SystemExit("Cadastro falhou: HTTP " + str(status))

status, body = request("POST", "/auth/login", {"email": email, "senha": password})
token = json.loads(body).get("token") if status == 200 else None
rows.append({"operacao": "Autenticar", "metodo": "POST", "endpoint": "/auth/login",
             "http": status, "resposta": "Token recebido (ocultado)" if token else "Login falhou"})
if not token:
    raise SystemExit("Login falhou")

status, body = request("POST", "/itens",
                       {"nome": "Papel reciclavel - " + environment, "volume": 1.0}, token)
rows.append({"operacao": "Criar item", "metodo": "POST", "endpoint": "/itens",
             "http": status, "resposta": body})
if status != 200:
    raise SystemExit("Criacao do item falhou: HTTP " + str(status))
created = json.loads(body)

status, body = request("GET", "/itens", token=token)
rows.append({"operacao": "Consultar itens", "metodo": "GET", "endpoint": "/itens",
             "http": status, "resposta": body})
if status != 200 or not any(item["id"] == created["id"] for item in json.loads(body)):
    raise SystemExit("Consulta nao retornou o item criado")

result = {"ambiente": environment, "url": base_url,
          "imagem": os.environ.get("APP_IMAGE", ""),
          "registradoEmUTC": datetime.now(timezone.utc).isoformat(),
          "resultado": "APROVADO", "operacoes": rows}
(output / (environment + ".json")).write_text(
    json.dumps(result, ensure_ascii=False, indent=2), encoding="utf-8")
cards = "".join(
    '<section><h2>' + html.escape(row["operacao"]) + ' — HTTP ' + str(row["http"])
    + '</h2><p>' + html.escape(row["metodo"] + " " + base_url + row["endpoint"])
    + '</p><pre>' + html.escape(row["resposta"] or "(resposta sem corpo)") + '</pre></section>'
    for row in rows)
page = """<!doctype html><html lang="pt-BR"><meta charset="utf-8">
<title>Evidencia de deploy</title><style>
body{font-family:Arial,sans-serif;background:#f1f5f9;color:#172033;margin:36px}
main{max-width:1100px;margin:auto}h1{font-size:30px}p{line-height:1.5}
section{background:white;padding:18px 24px;margin:16px 0;border-left:5px solid #16a34a}
h2{font-size:20px;margin:0 0 12px}pre{white-space:pre-wrap;overflow-wrap:anywhere}
.badge{color:#166534;font-weight:bold}.meta{overflow-wrap:anywhere}
</style><main><h1>SmartCollector — """ + html.escape(environment) + """</h1>
<p class="badge">DEPLOY VERIFICADO: API e Oracle em funcionamento</p>
<p>Registro de chamadas reais a API durante o pipeline. Esta pagina e um relatorio
de evidencias, nao a interface da aplicacao. Ambiente temporario do GitHub Actions.</p>
<p class="meta">Imagem: """ + html.escape(result["imagem"]) + """<br>UTC: """ + result["registradoEmUTC"] + """</p>""" + cards + "</main></html>"
(output / (environment + ".html")).write_text(page, encoding="utf-8")
(output / (environment + ".md")).write_text(
    "### Deploy " + environment + "\n\nAPI verificada em " + base_url
    + "\n\n" + "\n".join("- " + r["operacao"] + ": HTTP " + str(r["http"]) for r in rows)
    + "\n", encoding="utf-8")
print(json.dumps(result, ensure_ascii=False, indent=2))
