# Stage 1: Build the React frontend
FROM node:22 AS frontend-build 
WORKDIR /app/frontend
COPY frontend/package*.json ./
RUN npm install
COPY frontend/ ./
RUN npm run build

# Stage 2: Build the Spring Boot backend
FROM maven:3.9.6-eclipse-temurin-17 AS backend-build
WORKDIR /app/backend
COPY backend/ ./
RUN mvn clean package -DskipTests

# Stage 3: Run the application
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy backend jar
COPY --from=backend-build /app/backend/target/APP.jar app.jar

# 🔥 IMPORTANT: Copy frontend build to static folder
COPY --from=frontend-build /app/frontend/dist /app/static

# Optional (for H2)
RUN mkdir /data

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]