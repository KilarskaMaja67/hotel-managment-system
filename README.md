# Hotel Management System (ERP)

A complete desktop Enterprise Resource Planning (ERP) platform for hotel operations built in Java. The system provides state-synchronized dashboards for **Managers, Receptionists, Housekeeping, Spa Staff, Security, and Guests** running on an asynchronous, data-driven architecture.

---

## Dynamic UI Theme Engine

The application features a custom, live theme manager that switches between Light and Dark modes instantly across all active windows without needing a restart.

| Dark Theme | Light Theme |
|---|---|
| ![Login Dark](assets/loginDark.png) | ![Login Light](assets/loginLight.png) |

---

## Role-Based Dashboards & Workflows

Users log in through a central gateway and are automatically directed to a custom workspace based on their role and access level.

### 1. Guest Self-Service Portal
Guests can manage their own stay, review bills, and handle digital keys autonomously.

* **Dashboard Home:** Quick view of active stays, checkout times, and profile data.
  ![Guest Welcome Screen](assets/guestWelcome.png)

* **Room Booking:** A filterable reservation system that searches by room size, premium status, and dates.
  ![Guest Reservation Flow](assets/guestReservation.png)
  ![Reservation Success](assets/reservationSuccess.png)

* **Digital Wallet & Keys:** A secure wallet module to transfer funds and top up digital room keycards.
  ![Wallet Management](assets/wallet.png)

### 2. Administrative & Corporate Workspace
Management tools offer full control over hotel staff, roster tracking, and secure hiring workflows.

* **Staff Auditing:** Real-time tracking of employee metrics, active shifts, and salaries.
  ![Employee List Management](assets/employeeList.png)

* **Employee Onboarding:** A secure gateway to validate inputs and register new staff members into the system.
  ![New Employee Creation](assets/newEmployee.png)

---

## Architecture & Design Patterns

The system emphasizes clean code, separation of concerns, and robust error handling.

### 1. The Observer Pattern
Decouples core business logic from the user interface. UI modules register as `DataObserver` targets, meaning any backend state change automatically triggers specific graphical updates without a full page refresh.
* **Live System Logger:** Includes an automated audit tool (`HotelLogger`) that tracks backend tasks, reservation changes, and security events in real-time.
![System Logs Viewer](assets/systemLogs.png)

### 2. Transactional Data Persistence (Serialization)
To keep the app self-contained without requiring an external database setup, the storage layer relies on **Java Object Serialization (`.ser`)**.
* Object graphs for users, rooms, queues, and bookings are loaded into memory on startup and saved atomically whenever a change occurs.
* Built with safety boundaries to protect financial and booking states against unexpected application shutdowns.

### 3. The Strategy Pattern
Rooms use the **Strategy Pattern** to separate room data from pricing algorithms.
* Decouples the core room object from dynamic pricing rules (like seasonal peaks, corporate discounts, or long-stay rate drops).
* Allows the system to swap pricing formulas at runtime without altering the underlying room structures.

### 4. Defensive Programming & Validation
* **Input Validation:** Enforces strict security protocols, including a custom `WeakPasswordException` layer during user registration.
* **Financial Integrity:** All accounting processes use structural logic guards to prevent data anomalies like negative deposits or account overdrafts.

---

## Getting Started

### 1. Initialize the Database (Data Seeding)
Before running the main application for the first time, run the automated seeding tool to generate the initial serialized data files:

```bash
javac DataGenerator.java
java DataGenerator
