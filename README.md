# ARTINUS Backend Developer (1~3년) 과제

--- 
## 개발 환경
### Language
- Java 21
### Framework
- Spring Boot 3.4.3
### Database
- Maria DB 
### Build
- Gradle

---
## 과제내용
- 회원 관리 시스템에서 구독 서비스의 가입 및 해지 기능을 구현합니다.
- 구독 및 해지 시, 외부 API를 호출하여 응답에 따른 트랜잭션 처리를 포함합니다.
- 구독 서비스의 가입 및 해지 채널은 여러 개가 될 수 있습니다.
- 채널이란 구독 및 해지를 할 수 있는 창구를 의미합니다.
- 회원의 채널별 구독일, 해지일을 관리해야 합니다.

---
## 요구사항
### 구현해야 할 API
- 구독하기 API
- 구독해지 API
- 구독 이력 조회 API
### 채널 타입
- 구독, 해지 모두 가능한 채널
- 구독만 가능한 채널
- 해지만 가능한 채널
### 회원의 구독 상태
- 구독 안함
- 일반 구독
- 프리미엄 구독
### 채널 예시
- 구독 가능 채널: **홈페이지, 모바일앱, 네이버, SKT, KT, LGU+**
- 해지 가능 채널: **홈페이지, 모바일앱, 콜센터, 채팅상담, 이메일**
### 외부 API 호출 및 트랜잭션 처리
- 구독하기 API 와 구독 해지 API 에는 외부 API 를 호출하고, 응답에 따라 트랜잭션 처리를 합니다.
- 외부 API
    ``` shell 
    curl -X GET https://csrng.net/csrng/csrng.php?min=0&max=1 
    ```
    - 해당 API 는 `random` 필드를 0 과 1로 랜덤으로 응답합니다.
- 응답 예시
    ``` json
    [ { "status": "success", "min": 0, "max": 1, "random": 1 } ]
    ```
- 응답 JSON의 random 값에 따른 처리
    - `random = 1` → 정상적으로 트랜잭션을 커밋
    - `random = 0` → 예외 발생 및 트랜잭션 롤백

### 기타
- 회원은 1개의 구독 상태를 가지며, 구독 및 해지를 여러 번 수행할 수 있습니다.
- 구현한 API 에 대한 명세서를 작성해야 합니다.
- 요구사항에 명시되지 않은 부분은 일반적인 구독 서비스의 동작을 참고하여 자유롭게 구현해주세요.

---

## API 상세
### 구독하기 API
- 휴대폰번호, 채널ID, 구독 상태를 입력 받습니다.
- 최초 회원의 구독 상태는 구독 안함, 일반 구독, 프리미엄 구독 모두 허용됩니다.
- 외부 API 호출 및 응답에 따른 처리를 포함합니다.
- 구독 상태 변경 가능 규칙
    - 구독 안함 → 일반 구독
    - 구독 안함 → 프리미엄 구독
    - 일반 구독 → 프리미엄 구독

### 구독해지 API
- 휴대폰번호, 채널ID, 구독 상태를 입력 받습니다.
- 외부 API 호출 및 응답에 따른 처리를 포함합니다.
- 구독 해지 상태 변경 가능 규칙
    - 프리미엄 구독 → 일반 구독
    - 프리미엄 구독 → 구독 안함
    - 일반 구독 → 구독 안함

### 구독 이력 조회 API
- 휴대폰번호를 입력 받습니다.
- 휴대폰번호의 채널ID, 구독일자, 해지일자를 포함한 이력을 응답합니다.

---

## 제약사항
- 개발 언어는 Java 를 사용해주세요.
- 데이터베이스는 자유롭게 선택하세요.
- 외부 라이브러리 사용에 제약이 없습니다.
- 분석 및 구현 내용을 `readme.md` 파일에 정리해주세요.
- 결과물은 GitHub public repository에 업로드 후 URL을 공유해주세요.

---

## 평가항목
- 아키텍처 설계 및 프로젝트 구성
- 요구사항 이해 및 구현
- 유지보수 및 확장 용이성
- 테스트코드 작성
- 오류 및 예외 처리****