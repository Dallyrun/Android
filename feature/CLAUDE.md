# Feature Module Guide

## MVI 패턴

각 feature는 파일을 목적별로 분리하여 구성:

```
feature/<name>/
├── <Name>Contract.kt        ← UiState, UiEvent, SideEffect 정의
├── <Name>ViewModel.kt       ← DallyrunViewModel 상속, 이벤트 처리
├── <Name>Screen.kt           ← Stateless Composable + @Preview
├── <Name>Route.kt            ← Stateful Composable (ViewModel 주입, 상태 수집, SideEffect 처리)
├── <Name>Formatter.kt        ← 포맷팅/변환 유틸 함수 (필요 시)
└── navigation/
    └── <Name>Navigation.kt   ← Route 객체, navigate 함수, NavGraphBuilder 확장
```

### 1. Contract (UiState / UiEvent / SideEffect)

```kotlin
data class RunUiState(
    val isRunning: Boolean = false,
    val distanceMeters: Double = 0.0,
) : UiState

sealed interface RunUiEvent : UiEvent {
    data object StartRun : RunUiEvent
}

sealed interface RunSideEffect : SideEffect {
    data class ShowError(val message: String) : RunSideEffect
}
```

### 2. ViewModel

```kotlin
@HiltViewModel
class RunViewModel @Inject constructor(
    private val startRunUseCase: StartRunUseCase,
) : DallyrunViewModel<RunUiState, RunUiEvent, RunSideEffect>() {

    override fun createInitialState() = RunUiState()

    override fun handleEvent(event: RunUiEvent) {
        when (event) {
            RunUiEvent.StartRun -> updateState { copy(isRunning = true) }
        }
    }
}
```

### 3. Route (Stateful — 별도 파일 `<Name>Route.kt`)

```kotlin
// Stateful — ViewModel 주입, 상태 수집, SideEffect 처리
@Composable
internal fun RunRoute(
    modifier: Modifier = Modifier,
    viewModel: RunViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    RunScreen(uiState = uiState, onEvent = viewModel::onEvent, modifier = modifier)
}
```

### 4. Screen (Stateless — 별도 파일 `<Name>Screen.kt`)

```kotlin
// Stateless — Preview 가능, 테스트 용이
@Composable
internal fun RunScreen(
    uiState: RunUiState,
    onEvent: (RunUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) { /* UI 구현 */ }

@Preview
@Composable
private fun PreviewRunScreen() {
    DallyrunTheme {
        RunScreen(uiState = RunUiState(), onEvent = {})
    }
}
```

### 5. 시스템바 인셋 처리 (edge-to-edge)

`MainActivity` 가 `enableEdgeToEdge()` 를 사용하므로 콘텐츠가 시스템바 아래까지 확장된다.

- **MainContainer 내부 화면** (Home / History / Community / MY): 자체 Scaffold 의 `innerPadding` 으로 자동 처리됨 → 추가 작업 불필요.
- **MainContainer 외부 화면** (Login / Signup / Run 등 풀스크린 destination): 화면 root에 `Modifier.safeDrawingPadding()` **명시 적용 필수**. 빠뜨리면 하단 버튼이 navigation bar에 가려진다.

```kotlin
Column(
    modifier = modifier
        .fillMaxSize()
        .safeDrawingPadding()        // 시스템바/IME/cutout 인셋
        .padding(horizontal = 24.dp), // 콘텐츠 패딩
    ...
)
```

### 6. 텍스트 리소스

모든 사용자 노출 문자열은 `res/values/strings.xml`에 정의:

```xml
<string name="run_start">시작</string>
<string name="run_stop">정지</string>
```

```kotlin
Text(text = stringResource(R.string.run_start))
```

### 7. Navigation

```kotlin
@Serializable
data object RunRoute

fun NavController.navigateToRun(navOptions: NavOptions? = null) {
    navigate(RunRoute, navOptions)
}

fun NavGraphBuilder.runScreen() {
    composable<RunRoute> { RunRoute() }  // Stateful Route 진입점
}
```

## 새 Feature 모듈 추가 체크리스트

1. `feature/<name>/build.gradle.kts` 생성 — `id("dallyrun.android.feature")` 플러그인
2. `settings.gradle.kts`에 `include(":feature:<name>")` 추가
3. Contract → ViewModel → Screen → Navigation 순서로 작성
4. `app/navigation/DallyrunNavHost.kt`에 `<name>Screen()` 등록
5. `app/build.gradle.kts`에 `implementation(projects.feature.<name>)` 추가
6. **테스트 작성** (아래 참고)
7. **문서 업데이트** — 루트 `CLAUDE.md`의 Module Structure에 새 feature 추가

## 테스트 요구사항

기능 구현 시 반드시 아래 테스트를 함께 작성한다.
**모든 테스트는 `core:testing` 모듈에서 작성한다.**

### ViewModel 테스트 (필수)

```kotlin
// core/testing/src/test/kotlin/.../feature/run/RunViewModelTest.kt
class RunViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `should set isRunning true when StartRun event`() = runTest {
        val viewModel = RunViewModel()
        viewModel.onEvent(RunUiEvent.StartRun)
        assertEquals(true, viewModel.uiState.value.isRunning)
    }

    @Test
    fun `should emit NavigateToHistory side effect when StopRun`() = runTest {
        val viewModel = RunViewModel()
        viewModel.sideEffect.test {
            viewModel.onEvent(RunUiEvent.StopRun)
            assertEquals(RunSideEffect.NavigateToHistory, awaitItem())
        }
    }
}
```

### Compose UI 테스트 (권장)

```kotlin
class RunScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `should display distance when running`() {
        composeTestRule.setContent {
            RunScreen(
                uiState = RunUiState(isRunning = true, distanceMeters = 5000.0),
                onEvent = {},
            )
        }
        composeTestRule.onNodeWithText("5.0 km").assertIsDisplayed()
    }
}
```
