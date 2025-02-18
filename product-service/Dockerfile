FROM openjdk:21-slim

# Uygulama dizini oluştur
WORKDIR /app

# JAR dosyasını kopyala
COPY target/*.jar app.jar

# Port aç
EXPOSE 8080

# Uygulamayı başlat
ENTRYPOINT ["java", "-jar", "app.jar"] 