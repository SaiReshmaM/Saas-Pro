# Stage 1: Build the React frontend
FROM node:22-alpine AS frontend-build
WORKDIR /app/frontend
COPY frontend/package*.json ./
RUN npm install
COPY frontend/ ./
RUN npm run build

# Stage 2: Build the Spring Boot backend
FROM maven:3.9.6-eclipse-temurin-17 AS backend-build
WORKDIR /app
COPY backend/pom.xml ./backend/
COPY backend/src ./backend/src

# CRITICAL: Copy built frontend directly into Spring Boot static resources folder
# This ensures the website is bundled INSIDE the JAR file.
COPY --from=frontend-build /app/frontend/dist /app/backend/src/main/resources/static/

WORKDIR /app/backend
RUN mvn clean package -DskipTests

# Stage 3: Run the application
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# FinalName in pom.xml is APP
COPY --from=backend-build /app/backend/target/APP.jar app.jar
# Create the data directory for H2 persistence
RUN mkdir /data
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]