
---
# 개인 개발 리포트_서경원

> 개인 개발 리포트 작성
### 개요  
제목: HR Bank  
부제: Batch로 데이터를 관리하는 Open EMS  
기간: 2025.10.20(월) ~ 2025.10.28(화)  
저장소: https://github.com/codeit-spring-6-part2-team2/sb06-hrbank-team02  
담당 기능: 파일 기능 관리

## 기술 스택

- 언어: Java 17
- 프레임워크: Spring Boot 3
- 빌드 도구: Gradle
- 데이터베이스: H2 Database (개발 및 테스트), PostgreSQL (운영)
- ORM: Spring Data JPA, Querydsl
- 버전 관리: Git, GitHub
- 협업 도구: Discord
- API 문서화: Swagger
- 테스트: JUnit 5, Mockito
- Null Safety: Jspecify, Spring Validation
- Mapping: MapStruct

  jspecify를 사용해 package-info.java를 등록하고, nullMark로 안전하게 프로젝트를 진행할 수 있도록 했음

---
## 깃허브 중심 개발 프로세스
깃을 사용할떄마다 헷갈렸었으며,
깃 명령어에도 익숙치 않았습니다.
팀 프로젝트 레포지토리에서 fork를 해서 내 작업 레포지토리로 와서 작업을 진행하고, 원본 레포지토리의 최신 반영사항을 얻어오기위해 upstream도 처음 사용해보았습니다
issue 발행 및 discussion 기능들이 있는지 잘몰랐으며, 이번기회에 잘 학습한것 같습니다.

---

## 파일 관리 기능 소개


## 1. 개요

* 프로젝트 내에서 **파일 업로드, 다운로드, 메타정보 관리**를 담당하는 기능
* 관리 대상: 직원(Employee)프로필 이미지, 백업(Backup) 파일 등
* 파일의 **메타 정보**와 **실제 파일**을 분리하여 관리

    * 메타 정보: DB에 저장 (FileEntity)
    * 실제 파일: 로컬 디렉토리에 저장
    * 향후 확장성: 파일 타입 추가 용이

---

## 2. 파일 저장 디렉토리 구조

```
./file-data-map/
│
├─ image/
├─ log/
├─ csv/
│
```

* 루트 디렉토리: `./file-data-map`
  * application.yaml에서 커스텀 경로로 설정
* fileType(image, csv, log 등) → 루트 디렉토리 하위에 생성
  * 파일 타입별 폴더 자동 생성

---

## 3. Entity 구조 (FileEntity)


* DB에는 파일의 메타 정보가 저장 
  * 파일명, 타입, 크기, 저장경로, 그리고 파일의 소유자 정보가 들어갑니다.

### ownerType과 ownerId
* 직원(Employee) 파일인지, 백업 파일인지 구분
* 여러 도메인에서 하나의 파일 관리 테이블을 공통으로 사용
* ownerType, ownerId로 다형성 연관관계 표현 및 모듈 간 직접 참조 방지

---

## 4. DTO 구조

DTO는 컨트롤러와 서비스 간 데이터 전달에 사용

* 파일 업로드나 다운로드 시, 파일의 메타정보를 주고받기 위함
* 사용자가 웹 화면에서 파일을 업로드하면

```
Spring이 MultipartFile 형태로 전달 ->  서비스 계층에서 DTO로 변환해 관리
```

---

## 5. Service 구조

### FileService
FileService 인터페이스를 중심으로 동

* 서비스 구현체
  * **saveFile**
      * MultipartFile 수신 → 디렉토리 생성 → UUID 기반 파일명으로 실제 파일 저장 → DB 저장
  * **deleteFile**

      * 파일 Id로 DB 조회 후 DB에서 삭제
  * **fileDownload**

      * DB에서 경로 조회 후 실제 파일 읽어 byte[] 반환
  * **getFileMetadata**

      * DB 조회 후 Mapper를 통해 DTO 반환

---

## 6. Mapper 구조

* Entity ↔ DTO 변환 책임 분리
* Service는 Mapper를 통해 DTO 반환
  * 서비스는 **파일 처리 로직**에만 집중 할 수 있도록 함

---

## 7. Config

* 파일이 저장될 루트 경로 설정 - file-data-map으로 기본값
* 운영 환경에서 application.yaml로 경로 커스텀 가능
* Bean으로 관리 Service에서 주입받아 하위 폴더 구성 및 저장

---

## 8. 예외 처리

* 파일 업로드 용량 초과, 파일 미존재 등 예외 처리
* 전용 예외 핸들러 작성

---

## 9. 파일 저장 흐름 다이어그램

```
Controller에서 멀티파트 파일 받아옴
   ↓
서비스의 파일 저장 메서드
   ↓
하위 폴더 생성 (예: image, log 등)
   ↓
UUID + 파일명으로 파일 저장
   ↓
DB에 메타 정보 기록

```

---

## 10. 다운로드 흐름

```
서비스의 파일 다운로드 메서드
   ↓
DB에서 파일 Id에 따른 파일 경로 조회
   ↓
파일 읽기
   ↓
byte 배열로 반환
```

---

## 11. 특징 및 향후 개선 계획

* **파일 덮어쓰기 방지**: UUID와 파일명 결합해서 사용
* **메타정보와 실제 파일 분리**
* **확장성**

    * fileType 별 저장 용이
    * Employee 이미지, Backup 파일 등 구분
* **향후 개선**

    * 외부 클라우드 스토리지 연동해 파일 보관
    * 컨트롤러 기능 구현
    * 예외처리 기능 추가
    * 좀 더 모델이 리치 도메인 모델에 가깝게 설계 하고자 함
  
---

## 12. 마무리

* 파일 관리 기능은 **확장성**, **안정성**, **명확한 책임 분리**를 중심으로 설계

* 각 도메인과 연계 가능한 범용적인 구조를 갖음

**느낀점**

부족한 저에게 많은것을 보고 들을 수있는 시간이였습니다. 처음 들어보는 방식도 많았고 이를 따라가는데 시간이 
많이 들었던것 같습니다.

특히, 멀티 파트 파일이 무엇인지, 어떻게 관리가 되어야 하는지는 앞으로 안 까먹을것 같습니다.

많이 부족했지만, 잘 도와주신 팀원분들께 감사하다는 말씀 전하고 싶습니다.

---
