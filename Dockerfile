FROM eclipse-temurin:17-jdk-jammy

WORKDIR Portfolio-Manager/

COPY .mvn/ .mvn/
COPY src/ src/
COPY mvnw pom.xml ./

RUN ./mvnw clean install -DskipTests

CMD ["./mvnw", "spring-boot:run"]
