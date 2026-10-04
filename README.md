# Matchuri Backend

## 1. 기술스택

- Java 21
- Spring Boot 4.0.3
- Spring Modulith 2.0.3 (모듈 선언과 구조 검증)
- Gradle Kotlin DSL, Gradle Wrapper 9.3.1
- Spring Web MVC
- Spring Security, OAuth2 Client
- Spring Data JPA
- Bean Validation
- MySQL 8.0
- H2 Database
- Spring Actuator
- Spring Mail, Thymeleaf
- springdoc-openapi 3.x
- AWS SDK for Java v2, S3 compatible storage
- Lombok
- JUnit 5
- Docker Compose

## 2. 프로젝트 구조

```text
backend
├─ .github
│  ├─ ISSUE_TEMPLATE
│  └─ workflows
├─ gradle
│  └─ wrapper
├─ backend-app          # Boot application, HTTP API, 설정, resources/seed
├─ identity             # 인증, 회원, 취향, Security
├─ catalog              # 메뉴·속성·재료
├─ recommendation       # 추천 계산, 개인 추천, 행동 기록
├─ group-decision       # 그룹, 초대, 추천 진행, 투표, 확정
├─ media                # 이미지, 프리셋, 저장소 연동
├─ realtime             # 커밋 후 이벤트와 SSE
├─ shared-kernel        # 공통 응답, 예외, 영속성, 트랜잭션 지원
├─ src/test             # 전체 모듈 회귀 테스트와 테스트 설정
├─ build.gradle.kts
├─ docker-compose.yml
├─ gradlew
├─ gradlew.bat
└─ settings.gradle.kts
```

## 3. 실행환경

각 모듈은 `src/main/java`와 자체 Gradle 의존성을 가집니다. 배포는 단일 실행 JAR과 단일 DB를 유지합니다. 모듈 경계와 트랜잭션 정책은 [아키텍처](../../docs/backend/architecture.md), 구현 규칙은 [가이드](../../docs/backend/guide.md)를 봅니다.

- JDK 21
- Docker Desktop 또는 Docker Engine
- MySQL 8.0
- 기본 Spring profile: `local`
- 기본 서버 포트: `8080`
- 로컬 DB 기본 접속 정보
  - host: `localhost`
  - port: `3331`
  - database: `matchuri`
  - username: `matchuri`
  - password: `matchuri`
- 주요 로컬 엔드포인트
  - Swagger UI: `http://localhost:8080/docs/swagger-ui.html`
  - OpenAPI JSON: `http://localhost:8080/docs/openapi`
  - Health API: `http://localhost:8080/api/v1/health`

로컬 Docker Compose 실행에는 저장소 루트의 `.env`가 필요합니다. 최소 예시는 아래와 같습니다.

```env
MATCHURI_DB_PORT=3331
MATCHURI_DB_ROOT_PW=root
MATCHURI_DB_NAME=matchuri
MATCHURI_DB_USER=matchuri
MATCHURI_DB_PW=matchuri
MATCHURI_SPRING_PROFILE=local
MATCHURI_FRONTEND_ORIGIN=http://localhost:3000
MATCHURI_GOOGLE_EMAIL_APP_PW=dummy
```

애플리케이션 기동 시 JPA가 스키마를 갱신한 뒤 기준 데이터를 멱등하게 생성합니다.
로컬 샘플 데이터는 `local` 프로필에서만 생성하며 메뉴 대표 이미지는 시드하지 않습니다.

## 4. 로컬 실행 방법

```bash
# 워크스페이스 루트에서 backend 저장소로 이동
cd app/backend

# 로컬 MySQL 실행
docker compose up -d db

# 애플리케이션 실행
./gradlew bootRun

# 테스트 실행
./gradlew test --quiet
```

모듈 경계만 검사하려면 `./gradlew fastTest --tests 'matchuri.backend.architecture.ModuleStructureTest' --quiet`를 실행합니다. 실행 인자를 넘길 때는 `./gradlew :backend-app:bootRun --args='--spring.profiles.active=local'`을 사용합니다. `./gradlew bootJar`의 배포 결과물은 `build/libs/backend-<version>.jar`입니다.

Windows PowerShell에서는 아래 명령어를 사용할 수 있습니다.

```powershell
cd app/backend
docker compose up -d db
.\gradlew.bat bootRun
.\gradlew.bat test --quiet
```
