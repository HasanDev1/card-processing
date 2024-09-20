FROM openjdk:17-jdk-slim

# 2. Ishchi katalogni o'rnating
WORKDIR /app

# 3. Maven yoki Gradle yordamida jar faylini nusxalash
COPY target/*.jar app.jar

# 4. Portni ochish
EXPOSE 8080

# 5. Ilovani ishga tushirish
ENTRYPOINT ["java", "-jar", "app.jar"]