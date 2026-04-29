# Agent Harness

이 디렉터리는 자동화 도구와 사람이 같은 검증 게이트와 피드백 루프를 공유하기 위한 공용 기준이다.
루트 `AGENTS.md`와 `CLAUDE.md`는 이 문서를 함께 참조한다.

## Operating Loop

모든 변경은 아래 루프를 따른다.

1. Scope
   - 변경 목적, 영향 모듈, 사용자 노출 여부를 먼저 식별한다.
   - 관련 없는 파일은 건드리지 않는다.
2. Implement
   - 기존 아키텍처, 모듈 경계, 로컬 convention을 우선한다.
   - 기능 변경은 테스트와 문서 업데이트를 함께 포함한다.
3. Validate
   - `.agent-harness/validation-gates.md`에서 변경 유형에 맞는 게이트를 선택한다.
   - 가능하면 `scripts/verify-change.sh`로 같은 명령을 실행한다.
4. Report
   - PR 본문에 Summary, Validation gate, Test plan을 남긴다.
   - 반복 가능한 실패나 결정은 troubleshooting 문서 또는 하네스 규칙으로 환류한다.

## Shared Rules

- 문서만 변경한 경우에도 어떤 게이트를 선택했고 왜 빌드가 생략 가능한지 적는다.
- 기능, core API, build logic 변경은 로컬 검증 또는 CI 결과 중 하나로 반드시 확인한다.
- 검증 실패를 수정했다면 실패 원인, 수정 내용, 재검증 명령을 작업 결과에 포함한다.
- 같은 문제가 반복되면 코드만 고치지 말고 하네스 문서나 검증 스크립트도 업데이트한다.

## Files

| Path | Purpose |
|---|---|
| `.agent-harness/README.md` | 공용 작업 루프와 하네스 원칙 |
| `.agent-harness/validation-gates.md` | 변경 유형별 검증 게이트 |
| `.agent-harness/feedback-loops.md` | 실패/리뷰/CI 결과를 규칙으로 환류하는 기준 |
| `scripts/verify-change.sh` | 로컬 검증 게이트 실행 진입점 |
