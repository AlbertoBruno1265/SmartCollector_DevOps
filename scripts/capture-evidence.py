"""Captura a pagina de evidencias gerada a partir das respostas reais da API."""
from pathlib import Path
import sys
from playwright.sync_api import sync_playwright

environment = sys.argv[1]
if environment not in ("staging", "production"):
    raise SystemExit("Ambiente invalido")
source = Path("evidencias", environment + ".html").resolve()
with sync_playwright() as browser_api:
    browser = browser_api.chromium.launch()
    page = browser.new_page(viewport={"width": 1280, "height": 900}, device_scale_factor=1)
    page.goto(source.as_uri())
    page.screenshot(path=str(Path("evidencias", environment + ".png")), full_page=True)
    browser.close()
