# Troubleshooting Log

작업 중 만난 에러·트러블 슈팅·성능 개선을 기록한다. PR 본문에 묻혀 사라지는 "왜 이 코드인가" 와 "같은 문제 재발 시 어떻게 풀었나" 를 보존하기 위함.

## 기록 시점

다음 중 하나에 해당하면 항상 한 항목 추가:
- **Troubleshooting** — 빌드/런타임 에러, 비직관적 버그, 환경 이슈를 해결한 경우
- **Error fix** — 의미 있는 결함을 수정한 경우 (단순 typo 제외)
- **Performance** — 측정 가능한 성능 개선을 적용한 경우 (전/후 수치 포함)

매 턴 종료 시 Stop 훅이 서브에이전트를 띄워 세션을 훑고, 위 조건에 해당하는 일이 있었다면 이 파일 상단에 자동으로 항목을 append 한다.

## 형식

새 항목은 파일 상단(가장 최신이 위) 에 추가. 각 항목은 다음 템플릿을 따름:

```markdown
## YYYY-MM-DD · <Troubleshooting | Error fix | Performance> · <한줄 제목>

**Symptom:** 어떤 에러/현상이 보였는가 (스택트레이스나 메시지 핵심)

**Root cause:** 진짜 원인은 무엇이었는가

**Fix:** 어떻게 해결했는가

**Reference:** PR/commit/파일 링크
```

---

## Entries

## 2026-04-27 · Troubleshooting · ColumnScope 밖에서 Modifier.weight 호출 시 컴파일 실패

**Symptom:**
`feature:run` 빌드 시 `Unresolved reference 'weight'` 3건. `RunScreen.kt:54`, `:64`, `:106` 에서 `Spacer(modifier = Modifier.weight(1f))` 라인.

**Root cause:**
`Modifier.weight` 는 `ColumnScope`/`RowScope` 의 멤버 확장 함수. helper composable 을 `private fun RunReadyContent()` 등으로 분리하면서 `ColumnScope` receiver 가 사라져 호출이 안 됨.

**Fix:**
helper 함수를 `private fun ColumnScope.RunReadyContent()` 로 receiver 명시. import 도 `androidx.compose.foundation.layout.ColumnScope` 추가.

**Reference:** [#28](https://github.com/Dallyrun/Android/pull/28)
