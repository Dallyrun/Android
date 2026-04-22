---
description: 현재 브랜치 push + 컨벤션 맞춰 PR 생성
---

@.claude/rules/git-convention.md 를 따라 PR을 생성한다.

## 절차

1. `git status` / `git diff main...HEAD` / `git log main..HEAD --oneline` 으로 변경을 분석한다.
2. Conventional Commits prefix (`feat` / `fix` / `refactor` / `chore` / `docs` / `test`) 중 적절한 것을 고른다.
3. PR title: `<prefix>(<scope>): <요약>` (한글 OK, 70자 이내).
4. PR body 구조:
   ```
   ## Summary
   - (1~3개 불릿)

   ## Test plan
   - [x] ./gradlew <...> 통과
   - [ ] 수동 확인 항목
   ```
5. 브랜치가 원격에 없으면 `git push -u origin <branch>` 로 push.
6. `gh pr create --base main --head <branch> --title "..." --body "$(cat <<'EOF' ... EOF)"` 로 PR 생성.
7. 생성된 PR URL을 사용자에게 반환.

## 금지사항

- PR 본문/커밋 메시지에 Claude, AI, Co-Authored-By 같은 문구 **절대 넣지 않는다.**
- `main` 브랜치에 직접 커밋/푸시 금지.
