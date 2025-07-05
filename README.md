# SzyszkoApka

## Paradygmaty pracy z branchami

- `main` branch → produkcja (`prod`)
- `dev` branch → środowisko developerskie (`dev`)

Zmiany:
- Nowa wersja aplikacji powstaje przez zmergowanie `dev` do `main`.
- W trakcie tego procesu wykonujemy **podniesienie wersji** (version bump) wraz z commitem.

## Versioning

Projekt stosuje wersjonowanie zgodne z [Semantic Versioning (SemVer)](https://semver.org/).

Przykładowa konwencja wersji: `MAJOR.MINOR.PATCH`

- **MAJOR** — zmiany niezgodne wstecz (breaking changes)
- **MINOR** — dodanie funkcjonalności w sposób kompatybilny
- **PATCH** — poprawki błędów i drobne usprawnienia

---