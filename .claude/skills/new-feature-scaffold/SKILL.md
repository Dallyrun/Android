---
name: new-feature-scaffold
description: Use this skill when the user requests scaffolding a new feature module in the Dallyrun Android project. Triggers include "새 feature 만들어줘", "feature 모듈 추가", "<name> 화면 만들어줘", or invocation of the /new-feature slash command. Generates the full MVI scaffold (Contract, ViewModel, Route, Screen, Navigation, tests) following Dallyrun conventions.
---

# New Feature Scaffold

달리런 안드 프로젝트에 새 feature 모듈을 MVI 패턴으로 생성한다.

## 참조 문서 (자동 로드되는 CLAUDE.md)

- 패턴 상세: `feature/CLAUDE.md`
- Git/PR 규칙: 루트 `CLAUDE.md` 의 **Git Convention** 섹션
- 기존 예시: `feature/run/`, `feature/history/`, `feature/login/`

## 입력

- `<name>`: 소문자 단일 단어 (예: `profile`, `settings`). snake_case/하이픈 금지.
- 사용자가 이름을 안 줬으면 먼저 묻는다.

## 절차

### 1. 사전 검증
- `feature/<name>/` 이미 존재하면 중단.
- 현재 브랜치가 `main`인지 확인. 아니면 사용자에게 알리고 동의 받음.

### 2. 브랜치 생성
```bash
git checkout -b feat/<name>-screen
```

### 3. 디렉터리 + 파일 생성

```
feature/<name>/
├── build.gradle.kts
└── src/
    ├── main/
    │   ├── res/values/strings.xml
    │   └── kotlin/com/inseong/dallyrun/feature/<name>/
    │       ├── <Name>Contract.kt
    │       ├── <Name>ViewModel.kt
    │       ├── <Name>Route.kt
    │       ├── <Name>Screen.kt
    │       └── navigation/
    │           └── <Name>Navigation.kt
    └── test/kotlin/com/inseong/dallyrun/feature/<name>/
        └── <Name>ViewModelTest.kt
```

`<Name>` 은 첫 글자 대문자 PascalCase (`profile` → `Profile`).

각 파일 템플릿은 `feature/CLAUDE.md` 의 코드 샘플을 그대로 사용한다.

### 4. 모듈 등록

- `settings.gradle.kts` — `include(":feature:<name>")` 추가
- `app/build.gradle.kts` — `implementation(projects.feature.<name>)` 추가
- `app/src/main/java/com/inseong/dallyrun/navigation/DallyrunNavHost.kt`
  - `import com.inseong.dallyrun.feature.<name>.navigation.<name>Screen`
  - NavHost 내부에 `<name>Screen()` 추가
  - **startDestination 변경은 사용자 명시 요청 없으면 하지 않는다.**

### 5. 문서 업데이트

루트 `CLAUDE.md` Module Structure 에 한 줄 추가:
```
- **`feature:<name>`** — <한 줄 설명>
```

### 6. 검증
```bash
./gradlew :feature:<name>:test :app:assembleDebug
```

### 7. PR 생성
`/pr` 슬래시 커맨드를 활용하거나 동일한 절차로 직접 생성.

PR title 예시: `feat(<name>): scaffold <name> feature module`

## 빈 ViewModel 처리

이번 PR은 스캐폴드만이므로 `<Name>UiEvent` 와 `<Name>SideEffect` 는 빈 sealed interface로 둔다.
`handleEvent` 는 `// TODO: 이벤트 추가 예정` 주석만.
`<Name>ViewModelTest` 는 초기 상태 검증 1케이스만 작성.

이렇게 하면 후속 PR에서 실제 이벤트/UseCase 를 추가할 때 모듈 구조는 그대로 유지된 채 내용만 채우면 된다.
