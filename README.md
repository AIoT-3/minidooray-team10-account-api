### 프로젝트 소개
2026.05.14 ~ 2026.05.25

프로젝트를 관리하는 mini Dooray 만들기

- Front : 사용자 UI, 요청 받기
- Gateway : 요청 라우팅
- Account : 사용자 정보 관리
- Tack : 프로젝트 관리

#### account
Gateway를 통해 전달된 사용자 요청을 처리하는 서비스입니다.

- Gateway에서 인증된 사용자 정보(X-USER-ID 헤더)를 기반으로 요청 처리
- 사용자 정보 조회 / 수정 / 상태 변경 기능 제공
- 인증 로직은 Gateway에서 처리하므로 Account는 비즈니스 로직에 집중

### Tech Stack

- Java 21
- Spring Boot 4.0.6
- Spring MVC
- Spring Data JPA
- Spring Security
- H2 Database (test)
- MySQL (production)
- Maven
- Lombok
- Jacoco (test coverage)

### API
localhost:8081/api/account/
요청 처리

헤더의 X-USER-ID로 id요청 받아 처리

| URL | Method | Request | Response | Description |
|-----|--------|---------|----------|--------------|
| /signup | POST | {email, password, name} | 201 CREATED | 회원가입 |
| /members/by-email?email= | GET | - | 200 {id, email, password, status} | 회원 로그인 조회 |
| /members/batch | POST | {ids: [id]} | 200 { data: [ {id, name} ] } | 회원 리스트 반환 |
| /members/id | POST | {email} | 200 {id} | 회원 ID 반환 |
| /members/me | PATCH | - | 204 | 로그인 성공 처리 |
| /members/me | GET | - | 200 {email, password, name, status} | 회원 정보 조회 |
| /members/me | PUT | {password, name} | 204 | 회원 정보 수정 |
| /members/{member-id}/active | PUT | - | 204 | 휴면 해제 |
| /members/me/withdraw | DELETE | - | 204 | 회원 탈퇴 |
| /members/me/name | GET | - | 200 {name} | 회원 이름 반환 |
