# Factory Management System (Fabrika ERP) - Full Technical Manifest

## Strategic Vision & "The Vibe"
This project is a high-integrity ERP system for factory management. 
- **Aesthetic:** Minimalist, professional UI. (Beige, Cream, Burgundy).
- **Core Principle:** Progressive development following a strict 14-week roadmap.
- **Goal:** Moving from a console-based logic to a fully integrated JavaFX Desktop application with Web-View interaction.

## Detailed System Architecture (N-Tier)
The project is strictly divided into layers to ensure **Loose Coupling** and **Single Responsibility**:

1. **Presentation Layer (UI):** 
   - Uses JavaFX & WebView for desktop distribution.
   - Frontend is built with HTML5, Tailwind CSS, and JS for a modern web-vibe.
   - Integrated `MainFrame.java` with secure `LoginDialog`.
2. **Service Layer (Business Logic):**
   - The "Brain" of the system.
   - `ErisimKontrolServisi`: Centralized RBAC and IT Audit logging.
   - `EgitimServis`: Automated pedagogical tracking (video triggers, reminders).
   - `SatinAlmaService`: Handles interactive supplier and material management.
3. **Data Access Layer (DAL/Repository):**
   - Implements the Repository Pattern using `JpaRepository`.
   - `BaseRepository` provides generic CRUD operations to reduce boilerplate code[cite: 1].
4. **Domain/Model Layer:**
   - **Entities:** `Personel`, `Bordro`, `Malzeme`, `Rol` (Enum), `Departman`.
   - **Financials:** Strictly uses `BigDecimal` for all monetary fields[cite: 1].

## Advanced Security & RBAC Matrix
The system manages 14 distinct roles mapped via `Rol.java`[cite: 1].
- **Security Protocols:**
  - Password Handling: Fetched as `char[]`, processed via `JPasswordField`, and wiped from memory using `Arrays.fill(sifre, '\0')`[cite: 1].
  - Permission Checks: `dogrulaErisim()` is mandatory for all sensitive service calls[cite: 1].
  - IT Audit: Every IT-level access is logged to `logs/islem-kaydi.txt` with UTF-8 encoding[cite: 1].

## Database Management (MySQL)
- **Schema Name:** `fabrika_erp`[cite: 1].
- **Integrity:** Normalized to 3rd Normal Form (3NF).
- **Automation:** 
  - `v_satin_alma_ozet`: Unified view for purchase orders.
  - `v_kritik_stok`: Real-time report for materials below critical limits[cite: 1].

## Engineering Standards (The "Vibe Coder" Rules)
- **Scanner Leak Fix:** Always use the static `KONSOL_OKUYUCU` defined in `Main.java`[cite: 1].
- **Environment Safety:** DB credentials are never hard-coded; they must be read from environment variables (`DB_URL`, `DB_USER`, `DB_PASS`)[cite: 1].
- **Code Style:** Follow SOLID principles. Keep methods concise. Use Bean Validation API for entity integrity.

## 14-Week Roadmap Progress
- **Weeks 1-7:** Analysis, ER Diagrams, and UI skeleton completed[cite: 1].
- **Weeks 8-10:** Backend coding, RBAC implementation, and Financial module integration[cite: 1].
- **Current Focus:** Fixing the Java-to-JS bridge in WebView and finalizing MySQL integration.