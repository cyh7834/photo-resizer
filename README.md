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
- [imgscalr-lib] - 순수 자바로 구현된 이미지 프로세싱 라이브러리.
- [Thymeleaf] - 서버 사이드 자바 템플릿 엔진.
- [Bootstrap] - HTML, CSS, JS 라이브러리.
- [Maven] - 자바 프로젝트 의존성 관리 도구.

## Installation

jar 파일을 빌드하고 cmd로 실행할 수 있음.
```sh
git clone https://github.com/cyh7834/photo-resizer.git
cd photo-resizer
./mvnw package
java -jar target/*.jar
```

배포를 위한 환경 변수 설정의 예. <br/>
working.directory.path에 설정된 경로의 파일들은 스케쥴러를 통해 삭제되기 때문에 주의해야 함.
```sh
spring.servlet.multipart.location=D:/photo-resizer/upload/
spring.servlet.multipart.max-file-size=20MB
spring.servlet.multipart.max-request-size=20MB
working.directory.path=D:/photo-resizer/
resize.file.path=D:/photo-resizer/resize/
```
