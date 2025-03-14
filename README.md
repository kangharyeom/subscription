# ARTINUS Backend Developer (1~3년) 과제

---
## [프로젝트 개요](https://github.com/kangharyeom/subscription/wiki)
이 과제는 Java 기반 회원 관리 시스템에서 구독 서비스 가입/해지 기능을 구현하는 것입니다. 

다양한 채널을 통해 구독 상태를 변경하고, 외부 API 호출 결과에 따라 트랜잭션을 처리해야 합니다. 

구독 이력 조회 API를 통해 사용자별 구독 이력을 제공하며, 구현한 API에 대한 명세서 및 테스트 코드를 작성해야 합니다.

## [개발 환경](https://github.com/kangharyeom/subscription/wiki/%EA%B0%9C%EB%B0%9C-%ED%99%98%EA%B2%BD#framework)
### Language
- Java 21
### Framework
- Spring Boot 3.4.3
### Database
- Maria DB
### Build
- Gradle

## [요구사항 분석](https://github.com/kangharyeom/subscription/wiki/%EC%9A%94%EA%B5%AC%EC%82%AC%ED%95%AD-%EB%B6%84%EC%84%9D)
### ✅ 구독 관련 요구사항
- 회원은 하나의 구독 상태를 가짐
- 구독 및 해지는 여러 채널을 통해 가능
- 구독 및 해지 시 외부 API를 호출하여 트랜잭션 처리

### ✅ 구독 변경 가능 규칙
구독
- 구독 안함 → 일반 구독
- 구독 안함 → 프리미엄 구독
- 일반 구독 → 프리미엄 구독

구독 해지
- 프리미엄 구독 → 일반 구독
- 프리미엄 구독 → 구독 안함
- 일반 구독 → 구독 안함

### ✅ 채널 관련 요구사항
채널은 3가지 타입이 있음
- 구독 & 해지 가능 (홈페이지, 모바일앱 등)
- 구독만 가능 (네이버, SKT 등)
- 해지만 가능 (콜센터, 이메일 등)

### 요구사항 정리
회원별 단일 구독 상태를 관리하며, 다양한 채널을 통한 구독 및 해지 이력을 기록하는 애플리케이션을 구현해야 합니다. 

사용자는 여러 채널(웹, 앱, 제휴사 등)을 통해 구독 또는 해지를 요청할 수 있으며, 이 애플리케이션은 각 요청을 처리하고 관련 이력을 저장합니다. 

특히, 구독 및 해지 요청 처리 시 외부 API를 호출하여 트랜잭션의 성공 여부를 결정하고, 이에 따라 데이터베이스 트랜잭션을 커밋 또는 롤백합니다. 

또한, 사용자가 어떤 채널을 통해 구독했는지 기록하여 회원 전화번호별 구독 현황 및 이력을 추적하고 분석할 수 있도록 구현해야 합니다.


## [테이블 설계](https://github.com/kangharyeom/subscription/wiki/%ED%85%8C%EC%9D%B4%EB%B8%94-%EC%84%A4%EA%B3%84)
요구사항을 충족하기 위해 **4개의 테이블**을 설계했습니다.

| 테이블명 | 설명 |
|---------|------|
| `Member` | 회원 정보 저장 |
| `Subscription` | **회원의 현재 구독 상태** 저장 |
| `SubscriptionHistory` | **구독 변경 이력** 저장 |
| `Channel` | 구독 및 해지 가능한 **채널 목록** 저장 |

### ERD
![Subscription](https://github.com/user-attachments/assets/d9359f2e-1fe7-4190-8049-819c96c3de35)

### 설계 이유
#### 1) 확장성 고려
- 구독 상태는 Subscription 테이블에서 관리하고, 변경 이력을 SubscriptionHistory 테이블에서 별도로 관리하여 데이터 정합성을 유지할 수 있습니다.
- Channel 테이블을 분리하여 새로운 채널 추가 시 확장 가능합니다.

#### 2) 유지보수성 향상
- 구독과 해지 기능을 명확히 분리하여 API 설계 가능합니다.

#### 3) 트랜잭션 처리:
- 외부 API 응답에 따라 commit 또는 rollback 처리하여 데이터 무결성 보장합니다.


--- 

## [테이블 정의서](https://github.com/kangharyeom/subscription/wiki/%ED%85%8C%EC%9D%B4%EB%B8%94-%EB%AA%85%EC%84%B8%EC%84%9C)

### 1. 회원 (members)

#### 테이블 설명
회원 정보를 저장하는 테이블

#### 컬럼 정보

| 컬럼 이름 | 데이터 타입 | NULL 허용 | 기본값 | 제약 조건 | 컬럼 설명 |
|---|---|---|---|---|---|
| id | BIGINT | No | 자동 증가 | Primary Key | 회원 고유 ID |
| phone_number | VARCHAR | No |  | Unique Key | 전화번호 |
| created_at | DATETIME | No | 자동 생성 |  | 생성 일시 |
| last_modified_at | DATETIME | Yes | 자동 생성/수정 |  | 수정 일시 |

### 2. 채널 (channels)

#### 테이블 설명
채널 정보를 저장하는 테이블

#### 컬럼 정보

| 컬럼 이름 | 데이터 타입   | NULL 허용 | 기본값 | 제약 조건 | 컬럼 설명 |
|---|----------|---------|---|---|---|
| id | BIGINT   | No      | 자동 증가 | Primary Key | 채널 고유 ID |
| name | VARCHAR  | No      |  |  | 채널 이름 |
| channel_type | ENUM     | No      |  |  | 채널 타입 (예: SUBSCRIBE_ONLY, UNSUBSCRIBE_ONLY) |
| created_at | DATETIME | No      | 자동 생성 |  | 생성 일시 |
| last_modified_at | DATETIME | Yes     | 자동 생성/수정 |  | 수정 일시 |

### 3. 구독 (subscriptions)

#### 테이블 설명
회원의 채널 구독 정보를 저장하는 테이블

#### 컬럼 정보

| 컬럼 이름 | 데이터 타입   | NULL 허용 | 기본값 | 제약 조건 | 컬럼 설명 |
|---|----------|---|---|---|---|
| id | BIGINT   | No | 자동 증가 | Primary Key | 구독 고유 ID |
| member_id | BIGINT   | No |  | Foreign Key (members.id) | 회원 ID (외래 키) |
| subscription_status | ENUM     | No |  |  | 구독 상태 (예: NONE, BASIC, PREMIUM) |
| channel_id | BIGINT   | No |  | Foreign Key (channels.id) | 채널 ID (외래 키) |
| created_at | DATETIME | No | 자동 생성 |  | 생성 일시 |
| last_modified_at | DATETIME | Yes | 자동 생성/수정 |  | 수정 일시 |

### 4. 구독 이력 (subscription_histories)

#### 테이블 설명
회원의 채널 구독 이력 정보를 저장하는 테이블

#### 컬럼 정보

| 컬럼 이름 | 데이터 타입 | NULL 허용 | 기본값 | 제약 조건 | 컬럼 설명 |
|---|---|---|---|---|---|
| id | BIGINT | No | 자동 증가 | Primary Key | 구독 이력 고유 ID |
| member_id | BIGINT | No |  | Foreign Key (members.id) | 회원 ID (외래 키) |
| channel_id | BIGINT | No |  | Foreign Key (channels.id) | 채널 ID (외래 키) |
| previous_subscription_status | ENUM | Yes |  |  | 이전 구독 상태 |
| new_subscription_status | ENUM | Yes |  |  | 변경된 구독 상태 |
| cancelled_at | DATETIME | Yes |  |  | 구독 해지 일시 (해지 시에만 업데이트) |
| created_at | DATETIME | No | 자동 생성 |  | 생성 일시 |
| last_modified_at | DATETIME | Yes | 자동 생성/수정 |  | 수정 일시 |

## [API 명세서](https://kangharyeom.github.io/subscriptionAPI/)
클릭하여 API 명세서 페이지로 이동할 수 있습니다.
