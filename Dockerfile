# docker build -t universe-loan:latest .
# docker run -p 8080:8080 --env-file=.env universe-loan:latest

# Stage 1: Build stage (멀티 스테이지 빌드를 사용해 불필요한 파일 제거)
FROM maven:3.9.4-eclipse-temurin-21 AS build

# 의존성 캐싱을 위한 Maven 설정 복사
WORKDIR /app
COPY pom.xml ./

# 프로젝트 소스 복사 후 패키징
COPY src ./src
RUN mvn clean package -DskipTests # 빌드 시 테스트를 생략하면 빌드 속도와 이미지 용량이 줄어듭니다.

# Stage 2: Slim Run stage (최소한의 런타임 환경 사용)
FROM amazoncorretto:21-alpine
LABEL maintainer="fisaai"

# 비루트 유저 생성
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# 애플리케이션 포트 노출
EXPOSE 8080

# 애플리케이션 실행 명령
CMD ["java", "-jar", "app.jar"]