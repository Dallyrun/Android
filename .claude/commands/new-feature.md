---
description: 새 feature 모듈 스캐폴딩 (예 /new-feature profile)
argument-hint: "<feature-name>"
---

$ARGUMENTS 를 이름으로 받아 새 feature 모듈을 스캐폴딩한다.

@.claude/skills/new-feature-scaffold/SKILL.md 의 절차를 그대로 따른다.

## 인자 검증

- $ARGUMENTS 가 비어있으면 사용자에게 이름을 되묻는다.
- 이름은 전부 소문자여야 한다 (`profile`, `settings`). `snake_case` 나 하이픈 금지.
- 이미 `feature/<이름>/` 이 존재하면 중단하고 알린다.
