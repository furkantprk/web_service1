# Bir Java geliştirme kiti (JDK) içeren temel bir OpenJDK imajı kullanın
FROM openjdk:17-jdk-slim

# JAR dosyasının kopyalanacağı dizini belirleyin
WORKDIR /app

# Maven veya Gradle ile oluşturulan JAR dosyasını kopyalayın
# Target klasöründeki JAR dosyanızın adını ve versiyonunu buraya doğru yazın!
# Örnek: my-spring-app-0.0.1-SNAPSHOT.jar
COPY demo-0.0.1-SNAPSHOT.jar

# Uygulama başladıktan sonra 8080 portunu dışarıya açın (Spring Boot varsayılanı)
# Render, PORT ortam değişkenini sağlayacağı için Spring uygulamanız bunu kullanacaktır.
EXPOSE 8080

# Uygulamayı başlatma komutu
# server.port=${PORT:8080} ayarı olduğu için Render'ın sağladığı PORT değişkeni kullanılacaktır.
ENTRYPOINT ["java", "-jar", "app.jar"]