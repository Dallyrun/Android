# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Workflow

- 작업 시작 시 반드시 **플랜 모드**로 계획을 세운 뒤 승인받고 구현한다.
- 작업 시 **브랜치를 생성**하여 작업하고, 완료 후 **PR을 생성**한다.
  - 브랜치명: `feat/<feature-name>`, `fix/<bug-name>`, `refactor/<scope>` 등
- 기능 구현 시 반드시 해당 기능의 **테스트 코드**도 함께 작성한다.
  - 모든 테스트는 `core:testing` 모듈에서 작성한다.
- 기능 작업 완료 시 관련 **문서(CLAUDE.md)도 함께 업데이트**한다:
  - 새 모듈 추가 → 루트 `CLAUDE.md` Module Structure 업데이트
  - 새 feature 추가 → `feature/CLAUDE.md` 또는 루트 Module Structure 반영
  - 새 core 모듈/API 추가 → `core/CLAUDE.md` 의존성 규칙 및 사용법 업데이트
  - Convention plugin 변경 → 루트 `CLAUDE.md` Convention Plugins 테이블 업데이트
  - 새 라이브러리 도입 → `gradle/libs.versions.toml` 반영 확인

## Git Convention

- **Conventional Commits** 형식 사용: `feat:`, `fix:`, `refactor:`, `docs:`, `test:`, `chore:` 등
- 커밋 메시지, PR 본문 등 git 기록에 Claude/AI 관련 언급(Co-Authored-By 포함)을 절대 포함하지 않는다.

## Build Commands

```bash
./gradlew build                  # Full build
./gradlew :app:assembleDebug     # Build debug APK
./gradlew test                   # Run all unit tests
./gradlew :feature:run:test      # Run single module tests
./gradlew connectedAndroidTest   # Instrumented tests (requires device/emulator)
./gradlew lint                   # Lint check
./gradlew clean build            # Clean build
```

## CI (GitHub Actions)

`.github/workflows/ci.yml` — `main` 브랜치 push 및 PR 시 자동 실행

| Job | Gradle 태스크 | 목적 |
|---|---|---|
| `build` | `assembleDebug` | 컴파일, KSP, Hilt 와이어링 검증 |
| `check` | `test`, `lint` | 유닛 테스트 + 린트 |

- JDK 17 (Zulu), `gradle/actions/setup-gradle@v4` 사용
- 같은 브랜치 중복 실행 시 이전 실행 자동 취소 (`concurrency`)

## Architecture

**Multi-module** Android app using **Jetpack Compose + MVI** pattern, structured after Now in Android. 별도의 달리런 백엔드 서버가 존재하며 `core:network` 모듈(Retrofit)을 통해 통신한다.

### Module Structure

- **`app`** — Entry point, NavHost, Hilt Application
- **`build-logic`** — Convention plugins
- **`core:common`** — MVI base classes (`DallyrunViewModel`, `UiState`, `UiEvent`, `SideEffect`)
- **`core:model`** — Pure Kotlin domain models (JVM library)
- **`core:domain`** — Use cases, Repository 인터페이스 (JVM library)
- **`core:data`** — Repository implementations, TokenManager (DataStore), Auth 토큰 관리
- **`core:database`** — Room database (local cache, 추후 구현 예정)
- **`core:network`** — Retrofit + OkHttp, 백엔드 API 통신, DTO ↔ Domain 매핑, Auth 인터셉터/인증기
- **`core:designsystem`** — Material 3 theme, shared design components
- **`core:ui`** — Reusable Compose UI components
- **`core:testing`** — 공유 테스트 인프라 (MainDispatcherRule, TestData, MockK, Turbine)
- **`feature:login`** — 카카오 로그인 진입 화면 (앱 startDestination)
- **`feature:*`** — 각 피처 모듈 (MVI 패턴)

### Convention Plugins (build-logic)

| Plugin ID | Purpose |
|---|---|
| `dallyrun.android.application` | App module base config |
| `dallyrun.android.application.compose` | App + Compose |
| `dallyrun.android.library` | Library module base config |
| `dallyrun.android.library.compose` | Library + Compose |
| `dallyrun.android.feature` | Feature module (Compose + Hilt + Navigation + serialization) |
| `dallyrun.android.hilt` | Hilt DI with KSP |
| `dallyrun.jvm.library` | Pure Kotlin/JVM library |

### Key Decisions

- Kotlin 2.0.21, AGP 9.0.1, Gradle 9.1.0, Java 11
- Min SDK 24, Target/Compile SDK 36
- Type-safe Navigation Compose with kotlinx.serialization
- Type-safe project accessors (`projects.core.model`)
- Hilt DI with KSP (not kapt)
- Version catalog: `gradle/libs.versions.toml`

## Coding Conventions (Effective Kotlin 기반)

### Safety

- `val` 우선 사용, `var`는 꼭 필요한 경우만
- 불변 컬렉션(`List`, `Set`, `Map`) 기본 사용, `MutableList`는 좁은 범위에서만
- `data class`로 상태 표현, 변경 시 `copy()` 사용
- `!!` 금지 — `?.let {}`, `?:`, `requireNotNull()`, `checkNotNull()` 사용
- `require()`(인자 검증), `check()`(상태 검증), `error()`(도달 불가 분기) 활용
- 하위 타입에 데이터가 있으면 `sealed class/interface` 사용 (`when` 완전성 보장)
- 타입 안전 래퍼: `@JvmInline value class UserId(val value: String)`

### Code Design

- 상속보다 합성 — `by` 위임 또는 DI 활용
- 동일 타입 파라미터 2개 이상이면 **named arguments** 필수
- 오버로드 대신 **default parameter values** 사용
- `private` 기본, `internal`은 모듈 범위, `public`은 진정한 API만
- 인터페이스로 추상화 경계 정의 (Repository, DataSource, UseCase)
- `object`에 mutable state 금지 — DI 스코프 싱글톤 사용

### Collections

- 3개 이상 중간 연산 체인 + 대용량 → `Sequence` 사용
- `mapNotNull` > `map` + `filterNotNull`
- `buildList` / `buildMap` / `buildSet`으로 조건부 컬렉션 생성

### Coroutines

- ViewModel에서 `viewModelScope`, UI에서 `lifecycleScope` 사용. `GlobalScope` 금지
- suspend 함수는 **main-safe**: 내부에서 `withContext(Dispatchers.IO)` 전환
- `StateFlow` / `SharedFlow` 사용, `collectAsStateWithLifecycle()`로 수집
- `MutableStateFlow.update { }` 로 thread-safe 업데이트
- `CancellationException`은 삼키지 않고 재던짐
- 테스트를 위해 Dispatcher를 생성자 주입

### Compose

- State hoisting: Composable은 상태를 파라미터로 받고 이벤트는 `(Event) -> Unit` 람다
- `modifier` 파라미터: 항상 첫 번째 optional, 기본값 `Modifier`
- `LazyColumn`/`LazyRow` 아이템에 `key` 필수
- `LaunchedEffect`, `SideEffect`, `DisposableEffect`로 사이드이펙트 처리
- 모든 Screen에 `@Preview` 함수 필수 작성 (`Preview` prefix)
- **하드코딩 텍스트 금지** — 모든 사용자 노출 문자열은 `strings.xml`에 정의하고 `stringResource()`로 참조

### File Organization

- 각 파일은 **하나의 명확한 목적**만 가진다 (단일 책임)
- 유틸 함수, 확장 함수, 매퍼 등은 목적별로 별도 파일로 분리
- 하나의 파일에 관련 없는 함수를 모아두지 않는다

### Naming

- 패키지: 전부 소문자 `com.inseong.dallyrun.feature.run`
- 클래스: `PascalCase`, 함수: `camelCase`
- 상수: `SCREAMING_SNAKE_CASE`
- Unit 반환 Composable: `PascalCase` / 값 반환 Composable: `camelCase`
- Backing property: `_uiState` → `uiState`
- Boolean: `is`, `has`, `can`, `should` prefix
- 테스트 함수: 백틱 + 설명적 이름 `` `should emit loading then success`() ``

### Formatting

- 들여쓰기: 4 spaces
- 최대 줄 길이: 120자
- 멀티라인에 trailing comma 사용
- wildcard import(`*`) 금지
