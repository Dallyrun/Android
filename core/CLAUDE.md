# Core Modules Guide

## Module Dependency Rules

```
model ← (의존성 없음, 순수 Kotlin)
domain ← model
common ← (Android 의존, ViewModel)
network ← model (Retrofit, DTO↔Domain 매핑)
data ← model, domain, network (Repository 구현, DataStore 토큰 관리)
designsystem ← (Compose, Material3 theme)
ui ← model, designsystem (공유 UI 컴포넌트)
testing ← model (테스트 인프라, TestData)
```

**금지:** core 모듈 간 순환 의존.

**문서 동기화:** core 모듈이 추가/변경되면 이 파일의 의존성 규칙과 루트 `CLAUDE.md`의 Module Structure도 함께 업데이트한다.

> 로컬 캐시(`core:database` — Room)와 GPS 추적(`core:location` — FusedLocationProviderClient, Foreground Service) 모듈은 추후 구현 시 이 문서에 다시 추가한다.

## core:network 사용법

- API 인터페이스: `DallyrunApi.kt` — Retrofit suspend 함수
- DTO: `model/Network*.kt` — `@Serializable`, snake_case 필드는 `@SerialName` 사용
- 매핑 함수: `Network*.toDomain()` 확장 함수로 Domain 모델 변환
- DI: `NetworkModule.kt` — OkHttp, Retrofit, Json, API 인스턴스 Hilt 제공
- BASE_URL: `BuildConfig.BASE_URL`로 관리 (debug/release 분리 가능)

새 API 엔드포인트 추가 시:
1. `DallyrunApi`에 suspend 함수 추가
2. 필요하면 `model/`에 DTO + `toDomain()` 확장 함수 작성
3. `core:data`의 Repository에서 호출

### Auth API 패턴

- **`AuthApi.kt`** — 인증 전용 Retrofit 인터페이스 (토큰 갱신, 로그아웃 등)
- **`TokenProvider`** — 토큰 읽기/쓰기 인터페이스 (`core:network`에 정의, `core:data`에서 구현)
- **`AuthInterceptor`** — 일반 API 요청에 Authorization 헤더 자동 주입 (`api/auth/` 경로 제외)
- **`TokenAuthenticator`** — 401 응답 시 토큰 자동 갱신 + 요청 재시도
- **`@AuthClient`** — Auth 전용 OkHttpClient/Retrofit qualifier (인터셉터 순환 방지)
- **`ApiResponse<T>`** — 백엔드 공통 응답 래퍼

Auth 관련 변경 시:
1. `core:network`의 `AuthApi`에 엔드포인트 추가
2. `model/`에 요청/응답 DTO 작성
3. `core:data`의 `AuthRepositoryImpl`에서 호출
4. `core:domain`의 `AuthRepository` 인터페이스에 메서드 추가

## core:testing 사용법

테스트 작성 시 `testImplementation(projects.core.testing)` 의존성 추가.

제공 유틸리티:
- **`MainDispatcherRule`** — Coroutine 테스트에서 `Dispatchers.Main` 교체
- **`TestData`** — 샘플 Run, Location 등 테스트 픽스처
- **MockK, Turbine, Coroutines Test** — API로 재export

```kotlin
class RunViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `should update state when start run`() = runTest {
        val viewModel = RunViewModel()
        viewModel.onEvent(RunUiEvent.StartRun)
        assertThat(viewModel.uiState.value.isRunning).isTrue()
    }
}
```

## 테스트 요구사항

- 새 기능 구현 시 반드시 테스트 코드 작성
- ViewModel: 각 이벤트 핸들링 + 상태 변경 검증
- Repository: 네트워크/DB 호출 결과 매핑 검증
- UseCase: 비즈니스 로직 검증
- Compose: stateless Screen의 렌더링/인터랙션 검증
