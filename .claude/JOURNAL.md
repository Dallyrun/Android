# Engineering Journal

작업 중 내린 트레이드 오프, 트러블 슈팅, 성능 개선을 기록한다. PR 본문에 묻혀 사라지기 쉬운 "왜" 와 "어떤 대안을 고려했나" 를 보존하기 위함.

## 기록 시점

다음 중 하나에 해당하면 항상 한 항목 추가:
- **Tradeoff** — 두 개 이상의 합리적 선택지 사이에서 결정한 경우 (어떤 라이브러리, 어떤 패턴, 어떤 타이밍 등)
- **Troubleshooting** — 빌드/런타임 에러 또는 비직관적 버그를 해결한 경우. 같은 문제 재발 시 빠르게 답을 찾기 위함
- **Performance** — 측정 가능한 성능 개선을 적용한 경우 (전/후 수치 포함)

## 형식

새 항목은 파일 상단(가장 최신이 위) 에 추가. 각 항목은 다음 템플릿을 따름:

```markdown
## YYYY-MM-DD · <Tradeoff | Troubleshooting | Performance> · <한줄 제목>

**Context:** 어떤 상황/문제였는가

**Considered:** (Tradeoff 만) 어떤 대안들을 검토했는가
- A: 장단점
- B: 장단점

**Decision / Resolution:** 무엇을 어떻게 결정/해결했는가

**Outcome:** 결과. 측정값이 있으면 전/후 수치

**Reference:** PR/commit/파일 링크
```

---

## Entries

## 2026-04-27 · Tradeoff · GPS 위치 권한 요청 타이밍

**Context:**
달리기 트래킹 화면 진입 시 위치/알림 권한을 언제·어떻게 요청할지 결정.

**Considered:**
- 앱 시작 / 스플래시: 컨텍스트 부재로 거절률 ↑, Android 권한 가이드라인의 "in-context" 원칙 위반
- 홈 화면 진입: 사용자가 단순 둘러보는 중일 수 있어 의도 불명확
- "달리기 시작" 버튼 탭 직후 (채택): 의도가 명확한 시점, 거절 시에도 다시 시도 가능

**Decision / Resolution:**
홈의 "달리기 시작" 버튼 → RunScreen 진입 → rationale 카드 표시 → 사용자가 "위치 권한 허용" 명시 탭 시 시스템 다이얼로그. 거부 시 "설정 열기" fallback 노출. 설정에서 돌아올 때 `Lifecycle.Event.ON_RESUME` 으로 권한 상태 자동 재동기화.

**Outcome:**
승인율 ↑ 기대 (실측은 출시 후). `ACCESS_BACKGROUND_LOCATION` 은 의도적으로 요청 제외 — foreground service + "while in use" 만으로 화면 꺼진 상태도 커버 가능하기 때문.

**Reference:** [#28](https://github.com/Dallyrun/Android/pull/28)
