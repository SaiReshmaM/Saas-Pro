# Invoice Management System Design Document

## 1. High-Level Architecture

The application follows a standard **Client-Server Architecture**, loosely coupled via RESTful APIs.

* **Client Tier:** React Single Page Application (SPA), responsible for all routing, rendering, state management, and user interaction.
* **Server Tier:** Spring Boot Java application serving as the API Gateway and Business Logic handler.
* **Data Tier:** Relational database accessed via Spring Data JPA handling entity persistence.

---

## 2. Authentication & Security Flow

The system employs **Stateless Authentication** using JSON Web Tokens (JWT).

### 2.1 Login / Registration
1. User submits credentials to `/auth/login` or `/auth/register`.
2. Backend authenticates and returns a JSON payload containing:
   * `token`: The signed JWT.
   * `role`: The user role (`ADMIN` or `USER`).
3. Frontend stores these inside `localStorage`.

### 2.2 Authorized Requests
1. The Vite Frontend utilizes an **Axios Interceptor** configured in `api.js`.
2. Every outgoing request to the backend automatically has the `Authorization: Bearer <token>` header attached.
3. If the token is manually corrupted or deleted, the Interceptor instantly routes the user back to `/login`.

### 2.3 Backend Validation (`JwtFilter`)
1. The `JwtFilter` intercepts incoming HTTP requests.
2. It parses the incoming Bearer token using `JwtUtil`.
3. If the token is valid, it retrieves the `User` from the database, extracts the `ROLE`, and injects a `UsernamePasswordAuthenticationToken` into the `SecurityContextHolder`.

---

## 3. Role-Based Access Control (RBAC)

Access control is enforced stringently on both the Frontend and Backend to ensure zero trust.

### Frontend Strategy
* **Conditional Rendering:** Views are wrapped in `{role === 'ADMIN' && (...)}` blocks (e.g., hiding the Delete button on invoices).
* **Navigation Pruning:** The `Sidebar.jsx` maps over an array of routes filtering out objects where the current user role is not authorized.

### Backend Strategy
* **Controller-Level Enforcement:** Controllers manually extract the User object from the `HttpServletRequest` pipeline via the JWT Context.
* Hard blocks are implemented: `if (!"ADMIN".equalsIgnoreCase(user.getRole())) throw new RuntimeException("Access Denied");`

---

## 4. Multi-Tenancy Architecture

The application handles multi-tenancy at the **Row-Level (Shared Database, Shared Schema)**.

* Every User belongs to a specific Tenant (`tenantId`).
* Every Entity (like `Invoice` or `Customer`) requires a `tenantId`.
* When an `ADMIN` creates an invoice, the system binds the creator's `tenantId` to that invoice.
* During read operations, the backend forcibly enforces `repo.findByTenantId(user.getTenantId())` rather than `findAll()`, ensuring clients cannot breach data perimeters.

---

## 5. UI/UX Paradigm

The frontend employs a deeply customized, variable-driven vanilla CSS structure abandoning external utility frameworks like Tailwind for precise custom control.

### Aesthetic Design: Glassmorphism
* Backgrounds utilize `backdrop-filter: blur(16px)` overlaid on transparent RGBA panels.
* Complex radial gradients provide organic background illumination.
* Elements feature subtle `transform: translateY()` hover micro-animations to feel tactile.

### Advanced State: Settings Persistence
* Settings page manipulates state variables representing user preferences.
* Saving injects structural flags like `.light-theme` dynamically onto the DOM body, allowing pure CSS inheritance variables (`var(--bg-color)`) to globally mutate the site's palette instantly.
