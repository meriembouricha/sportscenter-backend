# Use JDK base image
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy Maven wrapper and build files
COPY . .

# Make sure Maven wrapper is executable
RUN chmod +x mvnw

# Build the app
RUN ./mvnw clean package -DskipTests

# Copy the built JAR
# Replace with the actual JAR file name if known
RUN cp target/*.jar app.jar

# Expose app port
EXPOSE 8080

# Run the app
CMD ["java", "-jar", "app.jar"]
