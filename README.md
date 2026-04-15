# Invoice Management System 

Welcome to **Invoice Management System**, a fully functional multi-tenant Software as a Service (SaaS) application. This project features a robust Spring Boot backend paired with a highly responsive, premium-styled React frontend.

## Key Features

* **Secure Authentication:** JWT-based login and registration system.
* **Role-Based Access Control (RBAC):** Strict isolation between `ADMIN` and `USER` capabilities.
* **Multi-Tenancy:** Data isolation ensures users only see invoices and customers belonging to their specific tenant.
* **Premium UI/UX:** A state-of-the-art Glassmorphism design system supporting dynamic Dark and Light modes.
* **Automated PDF Export:** Native, browser-based invoice PDF generation.
* **Smart Dashboard:** Dynamic revenue calculation and quick-glance statistics.

## Technology Stack

### Frontend
- **React.js (Vite)**
- **React Router DOM** for instantaneous SPA navigation
- **Axios** with automated Request Interceptors for seamless token handling
- **Vanilla CSS3** featuring CSS Variables, Grid/Flexbox, and advanced backdrop-filters

### Backend
- **Java 17 & Spring Boot 3.x**
- **Spring Security** & **JJWT** (JSON Web Tokens)
- **Spring Data JPA** for robust database operations
- **H2 Database** (Configurable to PostgreSQL/MySQL)

---

## Getting Started

### 1. Run the Backend (Spring Boot)
1. Open your terminal and navigate to the `/backend` directory.
2. If using Maven, run the following command to start the server:
   ```bash
   mvn spring-boot:run
   ```
3. The server will start securely on `http://localhost:8080`.

### 2. Run the Frontend (React + Vite)
1. Open a separate terminal and navigate to the `/frontend` directory.
2. Install the necessary JavaScript dependencies:
   ```bash
   npm install
   ```
3. Start the Vite development server:
   ```bash
   npm run dev
   ```
4. Open your browser and navigate to `http://localhost:5173`.

---

## User Roles & Permissions

### `ADMIN` Role
* **Full Access:** Complete read/write capabilities across the platform.
* **Dashboard:** Views total active invoices alongside calculated **Total Revenue**.
* **Invoices:** Can View, Create, Edit, Delete, and Download PDFs.
* **Customers:** Full CRUD capabilities for the tenant CRM.

### `USER` Role
* **Restricted Access:** Designed for staff or clients needing read-only insights.
* **Dashboard:** Views basic invoice summaries (no financial revenue exposed).
* **Invoices:** View-only permissions. Can Download PDFs but cannot Create, Edit, or Delete.
* **Customers:** Strictly blocked entirely from viewing CRM data.
