# Git Convention

> 달리런 프로젝트의 브랜치/커밋/PR 규칙. 모든 자동 도구(skills, commands)와 수동 작업은 이 문서를 따른다.

## Workflow

- 작업 시작 시 반드시 **플랜 모드**로 계획을 세운 뒤 승인받고 구현한다.
- **`main`에 직접 커밋/푸시 금지.** 브랜치 생성 → PR → 머지.
- 기능 구현 시 반드시 해당 기능의 **테스트 코드도 함께 작성**한다.

## 브랜치 네이밍

| Prefix | 용도 | 예시 |
|---|---|---|
| `feat/` | 새 기능 | `feat/login-screen` |
| `fix/` | 버그 수정 | `fix/run-pause-crash` |
| `refactor/` | 리팩터링 | `refactor/rename-mvi-contract` |
| `chore/` | 잡일 (의존성, 설정) | `chore/remove-kakao-login` |
| `docs/` | 문서만 변경 | `docs/update-claude-md` |
| `test/` | 테스트만 변경 | `test/add-history-vm` |

## Commit Message — Conventional Commits

- 형식: `<type>(<scope>): <subject>`
- type: `feat`, `fix`, `refactor`, `chore`, `docs`, `test`, `style`, `perf`, `build`, `ci`
- scope: 모듈 또는 기능 (`auth`, `login`, `run`, `core`, `designsystem` 등)
- subject: 한글 OK, 명령형 권장
- body: 왜(Why)를 설명. 무엇을(What)는 코드가 보여줌

예시:
```
feat(login): add Kakao login entry screen as start destination

기존 RunRoute가 startDestination이었으나 인증 도입을 위해
LoginRoute를 첫 화면으로 두고 ...
```

## PR 본문 템플릿

```markdown
## Summary
- (1~3개 핵심 변경 불릿)

## Test plan
- [x] ./gradlew :feature:xxx:test 통과
- [x] ./gradlew :app:assembleDebug 통과
- [ ] 디바이스 수동 확인 (필요 시)
```

## 절대 금지

- 커밋 메시지, PR 본문, 코드 주석 등 **git 기록 어디에도 Claude/AI 관련 언급 금지**:
  - `Co-Authored-By: Claude ...`
  - `🤖 Generated with Claude Code`
  - "AI assistant", "with help of Claude" 류
- `--no-verify` (pre-commit hook 우회) 사용 금지 (사용자가 명시 요청 시 예외)
- `main` 브랜치에 직접 push, force push 금지

## 머지 후 정리

머지 후 다음을 항상 실행:
```bash
git checkout main
git pull
git remote prune origin
git branch -D <머지된-브랜치>
```
