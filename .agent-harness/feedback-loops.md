# Feedback Loops

검증 하네스는 한 번의 체크리스트가 아니라 반복해서 좋아지는 루프다.

## Loop

```text
Local validation
  -> PR CI
  -> review feedback
  -> troubleshooting record
  -> harness rule or script update
```

## When To Update The Harness

다음 중 하나가 발생하면 `.agent-harness/`, 루트 guidance 문서, 또는 `scripts/verify-change.sh` 업데이트를 검토한다.

- 같은 CI 실패가 두 번 이상 반복된다.
- 새 모듈, 새 convention plugin, 새 라이브러리가 추가된다.
- 기존 검증 명령이 더 이상 충분하지 않거나 너무 느리다.
- 리뷰에서 매번 같은 누락이 지적된다.
- 수동 확인 절차를 명령이나 문서 규칙으로 표준화할 수 있다.

## What To Record

Troubleshooting 문서에는 아래 중 하나에 해당하는 내용만 남긴다.

- TROUBLE: 빌드/런타임 에러 또는 비직관적 버그를 진단하고 해결했다.
- TRADEOFF: 두 개 이상의 합리적 선택지 사이에서 장단점을 비교하고 결정했다.
- PERF: 측정 가능한 성능 개선이 있었다.

단순 구현, 문서 추가, 의존성 정리, 미사용 import 제거, CI 통과 기록은 남기지 않는다.
