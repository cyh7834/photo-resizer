# photo-resizer
## 정방형 이미지 변환 웹 서비스

기존의 정방형 이미지 변환 애플리케이션을 사용하다가 광고 스킵이 귀찮아서 내가 만든 웹 서비스.

## Example

<img width="1226" alt="example" src="https://user-images.githubusercontent.com/48938462/182008871-de37a020-630b-4cdd-bed6-96df2d65aa45.png">

## Features

- Drag and drop 또는 브라우저를 통해 이미지를 선택하여 업로드.
- 업로드 후 자동변환 및 간편한 다운로드.
- 업로드 된 이미지는 2시간 이내에 삭제되고, 어떠한 개인정보도 저장하지 않음.

## Tech

웹 서비스 구현을 위해 사용된 기술들:

- [Spring Boot] - 스프링 부트 기반의 웹 애플리케이션 서버.
- [Spring Data JPA] - 스프링의 JPA 모듈.
- [Querydsl] - 자바 코드 기반 동적 쿼리 생성 프레임워크.
- [imgscalr-lib] - 순수 자바로 구현된 이미지 프로세싱 라이브러리.
- [Thymeleaf] - 서버 사이드 자바 템플릿 엔진.
- [Bootstrap] - HTML, CSS, JS 라이브러리.
- [Maven] - 자바 프로젝트 의존성 관리 도구.

## Installation

maven을 사용하여 프로젝트를 빌드하고 command line으로 실행.
```sh
git clone https://github.com/cyh7834/photo-resizer.git
cd photo-resizer
./mvnw package
java -jar target/*.jar --spring.config.location=file:///your/properties/file/path
```

프로젝트 구동 또는 배포를 위한 properties. <br/>
```sh
spring.datasource.url=jdbc:postgresql://your/postgresql/url
spring.datasource.driverClassName=org.postgresql.Driver
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.servlet.multipart.location=your/upload/directory/path
spring.servlet.multipart.max-file-size=20MB
spring.servlet.multipart.max-request-size=20MB
resize.file.path=your/resize/directory/path
```
