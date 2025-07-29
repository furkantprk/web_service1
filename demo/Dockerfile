# Aşama 1: Uygulamayı derlemek için bir build ortamı oluştur
FROM openjdk:17-jdk-slim AS builder

# Çalışma dizinini belirle
WORKDIR /app

# Maven Wrapper dosyalarını ve pom.xml'i kopyala
# Bu, bağımlılıkların her derlemede yeniden indirilmesini engeller
COPY .mvn .mvn
COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .

# mvnw betiğine çalıştırma izni ver
RUN chmod +x mvnw

# Bağımlılıkları indir (sadece pom.xml değiştiğinde yeniden çalışır)
RUN ./mvnw dependency:go-offline -B

# Projenin kalan kaynak kodunu kopyala
COPY src src

# Uygulamayı derle ve JAR dosyasını oluştur
RUN ./mvnw clean package -DskipTests -Dproject.build.sourceEncoding=UTF-8 -Dfile.encoding=UTF-8

# Aşama 2: Nihai çalışma imajını oluştur (daha küçük ve sadece JRE içerir)
FROM openjdk:17-jdk-slim

# Çalışma dizinini belirle
WORKDIR /app

# İlk aşamada oluşturulan JAR dosyasını kopyala
COPY --from=builder /app/target/demo-0.0.1-SNAPSHOT.jar app.jar

# Uygulama başladıktan sonra 8080 portunu dışarıya aç
EXPOSE 8080

# Uygulamayı başlatma komutu
ENTRYPOINT ["java", "-jar", "app.jar"]
