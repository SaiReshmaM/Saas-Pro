# # Stage 1: Build the React frontend
# FROM node:18-alpine AS frontend-build
# WORKDIR /app/frontend
# COPY frontend/package*.json ./
# RUN npm install
# COPY frontend/ ./
# RUN npm run build

# # Stage 2: Build the Spring Boot backend
# FROM maven:3.9.6-eclipse-temurin-17 AS backend-build
# WORKDIR /app
# COPY backend/pom.xml ./backend/
# # Use a wildcard to copy the backend source
# COPY backend/src ./backend/src
# # Copy the built frontend into the backend's static resources
# COPY --from=frontend-build /app/frontend/dist ./backend/src/main/resources/static/
# WORKDIR /app/backend
# RUN mvn clean package -DskipTests

# # Stage 3: Run the application
# FROM eclipse-temurin:17-jre-alpine
# WORKDIR /app
# COPY --from=backend-build /app/backend/target/*.jar app.jar
# # Create the data directory for H2 persistence
# RUN mkdir /data
# EXPOSE 8080
# ENTRYPOINT ["java", "-jar", "app.jar"]

FROM maven:3.8.4-openjdk-17-slim AS build

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/app.jar"]