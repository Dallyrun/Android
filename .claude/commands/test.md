---
description: 유닛 테스트 실행. 인자로 모듈 경로 지정 가능 (예 /test :feature:run)
argument-hint: "[모듈경로]"
---

$ARGUMENTS 가 주어졌으면 `./gradlew $ARGUMENTS:test` 를 실행하고,
비어있으면 `./gradlew test` 로 전체 유닛 테스트를 실행한다.

- 실패 시 어떤 테스트 케이스가 깨졌는지 파일 경로와 assertion 위치를 짚어준다.
- 성공 시 통과한 모듈 리스트만 간결히 보고.
