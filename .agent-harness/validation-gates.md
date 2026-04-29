# Validation Gates

변경 유형에 맞는 가장 좁은 게이트를 먼저 실행한다. 변경 범위가 여러 유형에 걸치면 더 넓은 게이트를 선택한다.

## Docs Only

Use when only Markdown, comments in guidance files, PR templates, or local process docs changed.

```bash
./scripts/verify-change.sh docs
```

Checks:
- tracked documentation paths exist
- no Gradle build required

Report:
- "문서 변경만 포함되어 Gradle 빌드 생략"

## Feature Module

Use when a `feature:<name>` module changes.

```bash
./scripts/verify-change.sh feature <name>
```

Runs:
- `./gradlew :feature:<name>:test :app:assembleDebug --no-daemon`

## Core Module

Use when `core:*` APIs, repositories, network/data/database behavior, or shared test utilities change.

```bash
./scripts/verify-change.sh core
```

Runs:
- `./gradlew test :app:assembleDebug --no-daemon`

## UI Or Compose

Use when Compose screens, reusable UI, navigation surfaces, themes, or resources change.

```bash
./scripts/verify-change.sh ui
```

Runs:
- `./gradlew test :app:assembleDebug --no-daemon`

Manual check:
- run the app or inspect the affected screen when behavior is visually user-facing

## Build Logic Or Gradle

Use when `build-logic`, Gradle convention plugins, version catalog, CI, or settings files change.

```bash
./scripts/verify-change.sh build-logic
```

Runs:
- `./gradlew build lint --no-daemon --continue`

## Network, Data, Or Auth

Use when API contracts, DTO mapping, token handling, interceptors, authenticators, repositories, or persistence change.

```bash
./scripts/verify-change.sh data
```

Runs:
- `./gradlew test :app:assembleDebug --no-daemon`

## Full Check

Use before release-like changes or when the impact is unclear.

```bash
./scripts/verify-change.sh all
```

Runs:
- `./gradlew build --no-daemon`
