---
description: 디버그 APK 빌드 (CI build job과 동일)
---

`./gradlew :app:assembleDebug` 를 실행하고 결과를 보고한다.

- 실패 시 컴파일/Hilt/KSP 에러를 분석해 어떤 모듈에서 깨졌는지 짚는다.
- 성공 시 BUILD SUCCESSFUL 한 줄과 소요 시간만 간결히 보고.
