\# Medical Laboratory Management System (MLMS) - Vibecoding Specification



\## 1. Project Overview

Act as an expert Java developer and software architect. Please build a Medical Laboratory Management System. The goal is to create a clean, robust, and maintainable desktop application that manages patient data, lab tests, sample tracking, and result reporting.



\## 2. Tech Stack \& Architecture

\* \*\*Language:\*\* Java (JDK 17+)

\* \*\*Build Tool:\*\* Maven 

\* \*\*UI Framework:\*\* JavaFX (use FXML for layout separation)

\* \*\*Database:\*\* SQLite (for zero-config local setup) or MySQL/PostgreSQL via JDBC/Hibernate

\* \*\*Architecture:\*\* Strict MVC (Model-View-Controller) pattern to separate business logic from UI.

\* \*\*Styling:\*\* CSS. The vibe should be "modern clinical"—clean white backgrounds, subtle gray borders, and soft blue/teal accents for primary actions.



\## 3. Core Features to Generate



\### A. Dashboard (Main View)

\* A high-level summary showing daily statistics: Total Patients, Pending Tests, Completed Tests.

\* A quick-action sidebar for navigating between modules.



\### B. Patient Management Module

\* \*\*Fields:\*\* Patient ID, Name, Age, Gender, Contact Info, Blood Group, Date of Registration.

\* \*\*Actions:\*\* Add, Edit, Search, and Delete patient profiles.



\### C. Test \& Catalog Module

\* \*\*Fields:\*\* Test Code, Test Name (e.g., Complete Blood Count, Lipid Profile), Normal Range, Cost.

\* \*\*Actions:\*\* CRUD operations for the test catalog.



\### D. Sample \& Result Tracking

\* \*\*Workflow:\*\* 

&#x20; 1. Create a "Lab Order" linking a Patient to specific Tests.

&#x20; 2. Mark sample status (e.g., Pending, Collected, Analyzing, Completed).

&#x20; 3. Input results against the specific tests.

\* \*\*Validation:\*\* Highlight results in red if they fall outside the "Normal Range" defined in the catalog.



\## 4. Database Schema Guidelines

Please generate the initial database setup script or JPA entities for the following core tables:

1\. `patients` (id, name, age, gender, phone, registered\_date)

2\. `tests` (id, code, name, normal\_range\_min, normal\_range\_max, unit, cost)

3\. `orders` (id, patient\_id, order\_date, status, total\_amount)

4\. `order\_results` (id, order\_id, test\_id, result\_value, is\_abnormal)



\## 5. Step-by-Step Execution Plan

Please execute this project in the following phases. Wait for my confirmation after each phase before proceeding:

1\. \*\*Phase 1: Scaffolding.\*\* Set up the Maven project structure, pom.xml dependencies (JavaFX, JDBC driver), and the MVC folder structure.

2\. \*\*Phase 2: Database.\*\* Create the database connection utility and entity models.

3\. \*\*Phase 3: UI Shell.\*\* Build the main Dashboard layout with the navigation sidebar.

4\. \*\*Phase 4: Core Modules.\*\* Implement the Patient and Test catalog views and controllers.

5\. \*\*Phase 5: Result Entry.\*\* Build the order creation and result entry logic with out-of-range highlighting.

