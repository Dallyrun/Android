---
description: CI check job과 동일하게 test + lint 실행
---

`./gradlew test lint` 를 실행한다 — GitHub Actions의 `check` job과 동일.

- 실패 시 test 실패와 lint 위반을 분리해서 보고한다.
- lint 위반은 어떤 모듈/파일/라인에서 발생했는지 명시.
