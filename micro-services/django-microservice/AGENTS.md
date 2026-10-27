# AGENTS.md — django-microservice (pdf_parser)

Part of the CampusTrack monorepo (`micro-services/django-microservice/`). Sibling: Spring Boot backend (`com.jabai.campustrack`) and Next.js frontend.

## Purpose

Generates incident-report PDFs (Playwright, HTML template -> PDF) and uploads them to Supabase Storage.

- Caller: Spring backend via `RestClient`.
- Endpoint: `POST http://localhost:8001/api/pdf-parser/create/`
- Health check: `GET /api/health/` (public, no auth)

## Stack

- Python >=3.14 (`.python-version` = 3.14)
- Django >=6.1.1. Plain Django views + Pydantic. **No DRF, no Django Ninja.** Do not introduce them.
- Package manager: `uv`. Lockfile: `uv.lock`. Never use `pip install` directly.
- PDF: Playwright
- Storage: Supabase Storage (client in `src/core/`)
- Local DB: SQLite (`src/db.sqlite3`) — minimal domain data only
- Main CampusTrack DB is MySQL, shared with Spring. This service does not own it.

## Layout

```
django-microservice/
├── .env / .env.example
├── .python-version
├── pyproject.toml
├── uv.lock
├── README.md
└── src/
    ├── manage.py
    ├── db.sqlite3
    ├── core/          # settings, urls, supabase client
    ├── health/        # /api/health/
    └── pdf_parser/    # views, services, middleware, schemas, templates
```

Conventions:
- Views: thin. Parse + validate request (Pydantic in `schemas`), call `services`, return `JsonResponse`.
- Business logic (render, PDF, upload): `pdf_parser/services`.
- Request/response shapes: Pydantic models in `pdf_parser/schemas`.
- HTML templates for PDFs: `pdf_parser/templates`.
- JWT check: `pdf_parser/middleware`.

## Code principles

- **Consistent.** Match existing naming, structure, imports, error shape, response shape. New code looks like old code.
- **No over-engineering.** Simplest thing that works. No base classes, factories, registries, abstractions, or config layers for one use case. Add abstraction only after 3rd real repeat.
- **No over-commenting.** Code explains itself via names and types. Comment only *why* (non-obvious constraint, workaround). Never comment *what*. No docstring that restates the signature. No commented-out code.
- **STRICT: do not modify existing code structure or logic.** Only touch what the task needs. No drive-by refactor, rename, reformat, reorder, import cleanup, or "improvement" on code outside the task. Found bug or smell elsewhere? Report it, do not fix it unasked.

## Examples

### Code structure

Bad: logic in view, mixed concerns.

```python
def create(request):
    data = json.loads(request.body)
    html = render_to_string("report.html", data)
    with sync_playwright() as p:
        b = p.chromium.launch()
        pg = b.new_page()
        pg.set_content(html)
        pdf = pg.pdf()
        b.close()
    supabase.storage.from_("reports").upload("x.pdf", pdf)
    return JsonResponse({"ok": True})
```

Good: view thin, logic in `services`, input in `schemas`.

```python
# views.py
@require_POST
def create(request):
    payload = ReportRequest.model_validate_json(request.body)
    url = create_report(payload)
    return JsonResponse({"url": url})

# services.py
def create_report(payload: ReportRequest) -> str:
    html = render_to_string("report.html", payload.model_dump())
    pdf = html_to_pdf(html)
    return upload_pdf(pdf, payload.report_id)
```

### Django practices

Bad: no validation, no method guard, leaks internals.

```python
@csrf_exempt
def create(request):
    data = json.loads(request.body)
    try:
        return JsonResponse({"url": make(data["id"], data["title"])})
    except Exception as e:
        return JsonResponse({"error": str(e)}, status=500)
```

Good: method guard, Pydantic validation, explicit errors, no internal detail leaked.

```python
@require_POST
def create(request):
    try:
        payload = ReportRequest.model_validate_json(request.body)
    except ValidationError as e:
        return JsonResponse({"error": "invalid_request", "details": e.errors(include_url=False)}, status=400)

    try:
        url = create_report(payload)
    except PdfGenerationError:
        logger.exception("pdf generation failed")
        return JsonResponse({"error": "pdf_failed"}, status=500)

    return JsonResponse({"url": url})
```

Also:
- Read config from `settings`, never `os.environ` inside views/services.
- Autoescape templates. No `|safe` on request data.
- No bare `except:` / `except Exception:` that swallows errors.
- `logger`, not `print`.

### Control flow

Bad: deep nesting, flag variable.

```python
def check(claims):
    ok = False
    if claims:
        if "exp" in claims:
            if "role" in claims:
                if claims["role"] in ALLOWED_ROLES:
                    ok = True
    return ok
```

Good: guard clauses, early return, flat.

```python
def check(claims: dict) -> bool:
    if not claims:
        return False
    if "exp" not in claims or "role" not in claims:
        return False
    return claims["role"] in ALLOWED_ROLES
```

### Over-engineering

Bad: abstraction for a single use.

```python
class BasePdfStrategy(ABC):
    @abstractmethod
    def render(self, html: str) -> bytes: ...

class PlaywrightPdfStrategy(BasePdfStrategy):
    def render(self, html: str) -> bytes: ...

class PdfStrategyFactory:
    @staticmethod
    def get() -> BasePdfStrategy:
        return PlaywrightPdfStrategy()
```

Good: one function.

```python
def html_to_pdf(html: str) -> bytes: ...
```

### Over-commenting

Bad:

```python
# Increment counter by 1
count += 1

# Get the user id from the token
user_id = claims["sub"]
```

Good:

```python
count += 1
user_id = claims["sub"]

# Spring sends role uppercase; normalize to match ALLOWED_ROLES
role = claims["role"].upper()
```

## Commands

Run from `django-microservice/`:

```bash
uv sync
uv run src/manage.py migrate
uv run src/manage.py runserver 8001
```

Port **8001** is hardcoded in Spring config. Do not change without updating Spring side.

Playwright browsers must be installed once:

```bash
uv run playwright install chromium
```

## Auth

- Shared JWT, HS256, same secret as Spring.
- Header: `Authorization: Bearer <token>`
- Required claims: `exp`, `iss`, `sub`, `role`
- `/api/health/` is public. All other routes require valid JWT.
- No API gateway. No separate service-to-service tokens.

Rules:
- JWT shape is a contract with Spring. Do not rename, remove, or retype claims here. Any change must be matched in Spring backend (`AGENTS.md` there says keep JWT shape stable).
- Always validate `exp` and `iss`. Reject on missing claim.
- Do not log tokens or the signing secret.

## Inter-service

- REST only. No gRPC, Celery, RabbitMQ, Kafka. Do not add a broker without discussion.
- Keep response JSON shape stable; Spring `RestClient` deserializes it.
- Error responses: JSON with a consistent shape and correct HTTP status (400 validation, 401 auth, 403 role, 500 upstream/Playwright/Supabase failure). Never return HTML error pages from API routes.

## Security

- No secrets in code. Config via env (`.env`, never committed). Keep `.env.example` in sync when adding a var — keys only, no real values.
- Supabase: use service key only server-side. Never return it in a response.
- Treat all request payload content as untrusted. It is rendered into HTML for Playwright: **escape / autoescape on**, never mark user input `|safe`, never build HTML by string concat. Block Playwright from loading arbitrary external URLs where possible.
- Generated PDFs contain incident data: use private bucket or signed URLs. Do not make public by default.
- Do not set `DEBUG=True` or wildcard `ALLOWED_HOSTS` outside local dev.

## Database / migrations

- SQLite is local and low-stakes. Do not store durable CampusTrack domain data here — that belongs in the shared MySQL, owned by Spring.
- Never hand-edit migrations. Generate with `uv run src/manage.py makemigrations`.
- Do not commit `src/db.sqlite3` changes unintentionally.

## Testing / lint

None configured yet. No pytest, Django tests, ruff, black, mypy.

If adding tooling, propose first, then:
- `pytest` + `pytest-django`, or Django test runner — pick one
- `ruff` (lint + format)
- Add as dev deps via `uv add --dev <pkg>`

Until then, verify manually:
1. `uv run src/manage.py check`
2. `curl localhost:8001/api/health/`
3. POST sample payload with valid JWT, confirm PDF in Supabase

## Infra / deploy

- No Docker for this service. Only `database/Dockerfile` (MySQL) exists at repo level.
- No compose, no K8s.
- No CI workflows (`.github` has modernize hooks only).
- Deploy: none yet. If adding, note Playwright needs browser binaries + system libs in the image.

## Agent rules

1. Ask before adding any new dependency (`uv add`). Prefer stdlib / existing deps.
2. Do not add DRF, Ninja, Celery, or any broker.
3. Keep changes minimal and match existing structure (`views` -> `services` -> `schemas`). Be consistent with existing code. No over-engineering. No over-commenting.
3a. STRICT: never modify structure or logic of existing code beyond what the task requires. Report unrelated issues, do not fix them.
4. Never commit secrets or `.env`.
5. Do not change port 8001, URL path `/api/pdf-parser/create/`, health path, or JWT claims without updating Spring side.
6. Update `README.md` when commands or env vars change (README currently omits `runserver`).
7. Use type hints and Pydantic for all external input.


## Type Safety and Debuggability

- **STRICT: do not use `Any` or `typing.Any` in new code or while refactoring existing code.** Avoid untyped parameters, return values, variables, and collections that effectively bypass type checking.
- Prefer precise type hints for function parameters, return values, variables, collections, and class attributes. Use existing domain types and Pydantic schemas where they fit.
- When a value's type is uncertain, use `object` and validate or narrow its type explicitly before use. Do not silence type errors with `Any`, broad casts, `type: ignore`, or equivalent workarounds.
- For structured data, define or reuse explicit types such as Pydantic models, `TypedDict`, enums, or dataclasses as appropriate. Keep types small and aligned with actual data contracts; do not introduce abstractions without need.
- Handle optional values and failure cases explicitly. Validate untrusted input at system boundaries, and raise or return specific, meaningful errors rather than allowing unclear failures downstream.
- Keep control flow easy to trace: use descriptive names, small focused functions, guard clauses, and explicit branches. Avoid dense one-liners or hidden side effects that make debugging harder.
- Preserve useful error context when handling exceptions. Catch specific exceptions where possible; log failures with `logger.exception(...)` when traceback is needed. Do not swallow exceptions or replace them with vague errors.
- When modifying existing code, add or improve types only within the requested scope. Do not perform unrelated refactors; report adjacent type-safety issues instead.
