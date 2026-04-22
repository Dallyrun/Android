# MVI Pattern (Feature Modules)

> 달리런 모든 feature 모듈이 따르는 MVI 구조. 더 자세한 가이드/예시는 [`feature/CLAUDE.md`](../../feature/CLAUDE.md) 참조.

## 파일 구조

```
feature/<name>/
├── build.gradle.kts                 — id("dallyrun.android.feature")
└── src/
    ├── main/
    │   ├── res/values/strings.xml
    │   └── kotlin/com/inseong/dallyrun/feature/<name>/
    │       ├── <Name>Contract.kt    — UiState, UiEvent, SideEffect
    │       ├── <Name>ViewModel.kt   — DallyrunViewModel 상속
    │       ├── <Name>Route.kt       — Stateful (ViewModel + 상태 수집 + SideEffect 처리)
    │       ├── <Name>Screen.kt      — Stateless + @Preview
    │       └── navigation/
    │           └── <Name>Navigation.kt  — Route 객체 + navigate 함수 + NavGraphBuilder 확장
    └── test/kotlin/com/inseong/dallyrun/feature/<name>/
        └── <Name>ViewModelTest.kt
```

## Contract

```kotlin
data class XxxUiState(val isLoading: Boolean = false) : UiState

sealed interface XxxUiEvent : UiEvent {
    data object SomeAction : XxxUiEvent
}

sealed interface XxxSideEffect : SideEffect {
    data class ShowError(val message: String) : XxxSideEffect
}
```

`UiState` / `UiEvent` / `SideEffect`는 [`core:common`의 `DallyrunContract.kt`](../../core/common/src/main/kotlin/com/inseong/dallyrun/core/common/mvi/DallyrunContract.kt)에 선언돼 있다.

## ViewModel

```kotlin
@HiltViewModel
class XxxViewModel @Inject constructor(
    private val someUseCase: SomeUseCase,
) : DallyrunViewModel<XxxUiState, XxxUiEvent, XxxSideEffect>() {

    override fun createInitialState() = XxxUiState()

    override fun handleEvent(event: XxxUiEvent) {
        when (event) {
            XxxUiEvent.SomeAction -> updateState { copy(isLoading = true) }
        }
    }
}
```

## Route (Stateful)

```kotlin
@Composable
internal fun XxxRoute(
    modifier: Modifier = Modifier,
    viewModel: XxxViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is XxxSideEffect.ShowError -> /* ... */
            }
        }
    }

    XxxScreen(uiState = uiState, onEvent = viewModel::onEvent, modifier = modifier)
}
```

## Screen (Stateless + Preview)

```kotlin
@Composable
internal fun XxxScreen(
    uiState: XxxUiState,
    onEvent: (XxxUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) { /* UI */ }

@Preview
@Composable
private fun PreviewXxxScreen() {
    DallyrunTheme {
        XxxScreen(uiState = XxxUiState(), onEvent = {})
    }
}
```

## Navigation

```kotlin
@Serializable
data object XxxRoute

fun NavController.navigateToXxx(navOptions: NavOptions? = null) {
    navigate(XxxRoute, navOptions)
}

fun NavGraphBuilder.xxxScreen() {
    composable<XxxRoute> { XxxRoute() }
}
```

## 체크리스트 (새 feature 추가 시)

1. `feature/<name>/build.gradle.kts` (`id("dallyrun.android.feature")`)
2. `settings.gradle.kts` 에 `include(":feature:<name>")`
3. Contract → ViewModel → Route → Screen → Navigation 순서로 작성
4. `app/src/main/java/com/inseong/dallyrun/navigation/DallyrunNavHost.kt` 에 `<name>Screen()` 등록
5. `app/build.gradle.kts` 에 `implementation(projects.feature.<name>)` 추가
6. **테스트 작성** (`<Name>ViewModelTest`)
7. 루트 `CLAUDE.md` Module Structure 에 한 줄 추가
8. 빌드/테스트 검증 후 PR 생성
