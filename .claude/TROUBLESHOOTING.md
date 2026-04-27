# Troubleshooting Log

작업 중 만난 트러블슈팅·트레이드오프·성능 개선을 기록한다. PR 본문에 묻혀 사라지는 "왜 이 코드인가" 와 "재발 시 어떻게 풀었나" 를 보존하기 위함.

## 카테고리

- **TROUBLE** — 빌드/런타임 에러, 비직관적 버그를 진단·해결한 경우
- **TRADEOFF** — 두 개 이상의 합리적 선택지 사이에서 장단점 비교 후 결정한 경우
- **PERF** — 측정 가능한 성능 개선을 적용한 경우 (전/후 수치 필수)

## 자동 기록

매 턴 종료 시 `.claude/settings.json` 의 Stop 훅이 `troubleshooting-recorder` 에이전트(`.claude/agents/troubleshooting-recorder.md`) 를 띄워 세션을 훑고, 조건 충족 시 이 파일 상단에 항목을 prepend 한다. 사용자는 대기하지 않음. 보수적으로 판단 — 잡무·정리·CI 통과 같은 건 기록 X.

수동으로 추가해도 됨 — 자동 기록이 못 잡은 사례가 있으면 그 자리에서 직접 항목을 prepend 하고 같은 PR 에 머지.

## 형식

새 항목은 파일 상단(가장 최신이 위) 에 추가. 각 항목은 다음 템플릿을 따름:

```markdown
## YYYY-MM-DD · <TROUBLE | TRADEOFF | PERF> · <한줄 제목>

**Symptom:** (TROUBLE) 어떤 에러/현상이 보였는가
**Context:** (TRADEOFF) 어떤 상황에서 결정해야 했는가
**Baseline:** (PERF) 변경 전 수치

**Root cause:** (TROUBLE) 진짜 원인은 무엇이었는가
**Considered:** (TRADEOFF) 어떤 대안들을 비교했는가
**Change:** (PERF) 무엇을 어떻게 바꿨는가

**Fix / Decision / Outcome:** 어떻게 해결/결정했는가 (PERF 는 전/후 비교 수치 필수)

**Reference:** PR/commit/파일 링크
```

---

## Entries

## 2026-04-27 · TROUBLE · ColumnScope 밖에서 Modifier.weight 호출 시 컴파일 실패

**Symptom:**
`feature:run` 빌드 시 `Unresolved reference 'weight'` 3건. `RunScreen.kt:54`, `:64`, `:106` 에서 `Spacer(modifier = Modifier.weight(1f))` 라인.

**Root cause:**
`Modifier.weight` 는 `ColumnScope`/`RowScope` 의 멤버 확장 함수. helper composable 을 `private fun RunReadyContent()` 등으로 분리하면서 `ColumnScope` receiver 가 사라져 호출이 안 됨.

**Fix / Decision / Outcome:**
helper 함수를 `private fun ColumnScope.RunReadyContent()` 로 receiver 명시. import 도 `androidx.compose.foundation.layout.ColumnScope` 추가.

**Reference:** [#28](https://github.com/Dallyrun/Android/pull/28)

## 2026-04-27 · TRADEOFF · GPS 위치 권한 요청 타이밍

**Context:**
달리기 트래킹 화면 진입 시 위치/알림 권한을 언제·어떻게 요청할지 결정.

**Considered:**
- 앱 시작 / 스플래시: 컨텍스트 부재로 거절률 ↑, Android 권한 가이드라인의 "in-context" 원칙 위반
- 홈 화면 진입: 사용자가 단순 둘러보는 중일 수 있어 의도 불명확
- "달리기 시작" 버튼 탭 직후 (채택): 의도가 명확한 시점, 거절 시에도 다시 시도 가능

**Fix / Decision / Outcome:**
홈의 "달리기 시작" 버튼 → RunScreen 진입 → rationale 카드 표시 → 사용자가 "위치 권한 허용" 명시 탭 시 시스템 다이얼로그. 거부 시 "설정 열기" fallback 노출. 설정에서 돌아올 때 `Lifecycle.Event.ON_RESUME` 으로 권한 상태 자동 재동기화. `ACCESS_BACKGROUND_LOCATION` 은 의도적으로 요청 제외 — foreground service + "while in use" 만으로 화면 꺼진 상태도 커버 가능.

**Reference:** [#28](https://github.com/Dallyrun/Android/pull/28)
