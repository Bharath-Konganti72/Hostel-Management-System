# 🏨 Hostel Management System (HMS)

A desktop application built with **Java Swing/AWT** for managing hostel operations: rooms, beds, residents, room requests, bed allocations, billing, payments and audit logs. The whole project is a **single Java file** with a custom, modern UI (custom-painted components) and **file-based persistence** (no external database or libraries).

---

## Table of Contents

1. [Features](#features)
2. [Tech Stack](#tech-stack)
3. [Requirements](#requirements)
4. [Getting Started](#getting-started)
5. [Default Credentials](#default-credentials)
6. [Roles & Permissions](#roles--permissions)
7. [Application Pages](#application-pages)
8. [Business Rules & Validations](#business-rules--validations)
9. [Architecture](#architecture)
10. [Class Reference](#class-reference)
11. [Data Persistence](#data-persistence)
12. [OOP Concepts Demonstrated](#oop-concepts-demonstrated)
13. [Demo Data](#demo-data)
14. [Known Limitations](#known-limitations)
15. [Future Improvements](#future-improvements)

---

## Features

- 🔐 Login and self-registration for residents
- 👥 Role-based access: **Admin**, **Receptionist**, **Resident**
- 🏠 Room and 🛏️ bed management
- 📋 Room request submission and review (approve/reject)
- 🔑 Bed allocation and check-out
- 💰 Monthly billing and 💳 payment recording (partial payments supported)
- 📊 Dashboard with live statistics
- 📝 Audit logging of important actions
- 👤 Staff account management (Admin only)
- 📦 One-click demo data loader
- 🎨 Modern UI: rounded cards, hover effects, placeholder text fields, toast notifications, sidebar navigation, styled tables
- 💾 Automatic save to a local file after each action

---

## Tech Stack

| Item | Details |
|---|---|
| Language | Java |
| GUI | Swing, AWT, Java2D (custom painting) |
| Persistence | Java Serialization (`hostel_data.dat`) |
| Build tool | None required (plain `javac`) |
| Dependencies | None (JDK only) |

---

## Requirements

- **JDK 14 or higher** (the code uses switch expressions with `->` and `String.isBlank()`). JDK 17 or 21 is recommended.
- A desktop environment (GUI application).
- Best visual results on **Windows** (uses the *Segoe UI* and *Segoe UI Emoji* fonts; other systems fall back to default fonts and emoji rendering may vary).

Check your version:

```bash
java -version
javac -version
```

---

## Getting Started

### 1. Save the file

Save the source as `HostelManagementSystem.java`. The file name must match the public class name.

### 2. Compile

```bash
javac HostelManagementSystem.java
```

### 3. Run

```bash
java HostelManagementSystem
```

On first launch the app creates `hostel_data.dat` in the working directory and seeds a default admin account.

### Running from an IDE

Open the file in IntelliJ IDEA, Eclipse, NetBeans or VS Code, and run the `main` method of `HostelManagementSystem`.

---

## Default Credentials

| Role | Email | Password |
|---|---|---|
| Admin | `admin@hostel.com` | `admin123` |

Demo residents (created via **Load Demo Data**) use the password `pass123`:

| Name | Email |
|---|---|
| Rahul Kumar | `rahul@test.com` |
| Priya Sharma | `priya@test.com` |
| Arjun Patel | `arjun@test.com` |

---

## Roles & Permissions

| Feature | Admin | Receptionist | Resident |
|---|:---:|:---:|:---:|
| Dashboard | ✅ | ✅ | ❌ |
| Rooms | ✅ | ✅ | ❌ |
| Beds | ✅ | ❌ | ❌ |
| Residents list / register | ✅ | ✅ | ❌ |
| Review room requests | ✅ | ✅ | ❌ |
| Allocations / check-out | ✅ | ✅ | ❌ |
| Create bills | ✅ | ✅ | ❌ |
| Record payments | ✅ | ✅ | ❌ |
| Staff management | ✅ | ❌ | ❌ |
| Audit logs | ✅ | ❌ | ❌ |
| Load demo data | ✅ | ❌ | ❌ |
| Own profile | ❌ | ❌ | ✅ |
| Submit / view own requests | ❌ | ❌ | ✅ |
| View own allocation | ❌ | ❌ | ✅ |
| View own bills & payments | ❌ | ❌ | ✅ |

---

## Application Pages

### Authentication
- **Login screen**: email and password with Enter-key support.
- **Registration screen**: residents self-register (name, email, phone, college, emergency contact, password).

### Admin / Receptionist
| Page | Purpose |
|---|---|
| **Dashboard** | Cards for total rooms, beds, occupied, available, residents, pending requests, unpaid bills, outstanding amount, maintenance beds; table of the 10 most recent allocations |
| **Rooms** | Add rooms (number, floor, type, monthly fee) and view all rooms with bed count |
| **Beds** | Add beds to a room; view all beds with status and occupancy |
| **Residents** | View all residents; register a new resident through a dialog |
| **Requests** | View all room requests; approve or reject by Request ID with remarks |
| **Allocations** | Allocate an available bed to a resident; check-out by Allocation ID |
| **Bills** | Create a monthly bill; table shows total, paid, outstanding, due date, status |
| **Payments** | Record a payment against an unpaid bill; view all payments |
| **Staff** *(Admin)* | Create Admin/Receptionist accounts; list all users |
| **Audit Logs** *(Admin)* | View system activity, newest first |
| **Load Demo Data** *(Admin)* | Populate sample rooms, beds and residents |

### Resident
| Page | Purpose |
|---|---|
| **My Profile** | Personal details and outstanding balance |
| **My Requests** | Submit a room request and view its status/remarks |
| **My Allocation** | Current room/bed assignment |
| **My Bills** | Outstanding and total paid cards plus bill table |
| **My Payments** | Payment history |

---

## Business Rules & Validations

**Users**
- Name required; email must contain `@` and be unique (case-insensitive).
- Password must be at least 4 characters.
- Inactive accounts cannot log in.

**Rooms & Beds**
- Room number is required, unique, floor and fee cannot be negative.
- Beds cannot be added to rooms under maintenance.
- Bed number must be unique within a room.
- An occupied bed cannot be set to maintenance.

**Requests**
- A resident can have only **one pending** request at a time.
- Only **pending** requests can be reviewed.

**Allocations**
- A bed under maintenance or already occupied cannot be allocated.
- A resident can have only **one active** allocation.
- Vacating date cannot be before the allocation date; an allocation can only be vacated once.

**Billing & Payments**
- One bill per resident per month; amount cannot be negative.
- Due date defaults to the **end of the billing month**.
- Payment must be greater than zero and cannot exceed the outstanding amount.
- Transaction references must be unique (if provided).
- Bill status updates automatically: `UNPAID` → `PARTIALLY_PAID` → `PAID`.

---

## Architecture

The application follows a layered structure inside one file:

```
HostelManagementSystem
├── Theme                  → colors, fonts, sizes (design system)
├── Enums                  → Role, AccountStatus, BedStatus, RoomStatus, RequestStatus,
│                            AllocationStatus, BillStatus, PaymentStatus, PaymentMethod
├── Exceptions             → HostelException → NotFound / Conflict / Validation
├── Model layer            → Person (abstract) → User, Resident
│                            Room, Bed, RoomRequest, Allocation, Bill, Payment, AuditLog
├── DataStore              → serializable container of all maps and ID counters
├── Repository<T>          → generic CRUD interface → MapRepository<T>
├── HostelService          → business logic, validation, statistics
├── FileDatabase           → save/load via serialization
├── Custom UI components   → RoundedPanel, ModernButton, ModernTextField,
│                            ModernPasswordField, RoundedBorder, SidebarButton,
│                            StatCard, createModernTable(), showToast()
└── MainApp (JFrame)       → login, registration, sidebar, role-based pages (Controller/View)
```

**Pattern summary:** Model (entities) – Service (logic) – View/Controller (`MainApp`), with a repository abstraction over in-memory maps.

---

## Class Reference

### Model

| Class | Description |
|---|---|
| `Person` (abstract) | Base class with `id`, `fullName`, `phone`; abstract `getPersonType()` |
| `User` | Login account with email, password, role, status |
| `Resident` | Hostel resident linked to a `User` via `userId`; college, emergency contact, registration date |
| `Room` | Room number, floor, type, monthly fee, status |
| `Bed` | Belongs to a room; has bed number and status |
| `RoomRequest` | Resident's room request with `review()` method |
| `Allocation` | Resident ↔ bed assignment with `vacate()` method |
| `Bill` | Monthly bill with `updateStatus(totalPaid)` |
| `Payment` | Payment against a bill with method and reference |
| `AuditLog` | Record of who did what and when |

### Enums

| Enum | Values |
|---|---|
| `Role` | ADMIN, RECEPTIONIST, RESIDENT |
| `AccountStatus` | ACTIVE, INACTIVE |
| `BedStatus` / `RoomStatus` | AVAILABLE, MAINTENANCE |
| `RequestStatus` | PENDING, APPROVED, REJECTED |
| `AllocationStatus` | ACTIVE, VACATED |
| `BillStatus` | UNPAID, PARTIALLY_PAID, PAID |
| `PaymentStatus` | SUCCESS, PENDING, FAILED |
| `PaymentMethod` | CASH, UPI, CARD, BANK_TRANSFER |

### Service (`HostelService`) – key methods

| Area | Methods |
|---|---|
| Users | `createUser`, `login` |
| Residents | `registerResident`, `getResident`, `getResidentByUserId` |
| Rooms/Beds | `addRoom`, `setRoomMaintenance`, `addBed`, `setBedMaintenance`, `isBedOccupied`, `getAvailableBeds`, `getBedLocation` |
| Requests | `createRoomRequest`, `reviewRequest` |
| Allocations | `allocateBed`, `checkout`, `getActiveAllocation` |
| Billing | `createBill`, `recordPayment`, `getPaidAmount`, `getOutstandingAmount`, `getResidentBills`, `getResidentPayments` |
| Stats | `totalBeds`, `occupiedBeds`, `availableBeds`, `maintenanceBeds`, `totalOutstanding` |
| Audit | `audit` |

### Custom UI Components

| Component | Role |
|---|---|
| `RoundedPanel` | Panel with rounded corners and optional soft shadow |
| `ModernButton` | Rounded button with hover and pressed colors |
| `ModernTextField` / `ModernPasswordField` | Inputs with placeholder text and focus-highlight border |
| `RoundedBorder` | Custom `AbstractBorder` |
| `SidebarButton` | Navigation item with icon, hover and selected state |
| `StatCard` | Dashboard metric card |
| `createModernTable()` | Styled `JTable` with alternating rows and slim scrollbar |
| `showToast()` | Auto-dismissing success/error notification |

---

## Data Persistence

- Data is stored in **`hostel_data.dat`** in the directory where the app is launched.
- Saved through `ObjectOutputStream` after every create/update action and on logout.
- Loaded at startup; if the file is missing or unreadable, a fresh database is created (warning printed to the console).
- To **reset all data**, close the app and delete `hostel_data.dat`.
- If you change the model classes, delete the old file to avoid deserialization errors.

---

## OOP Concepts Demonstrated

| # | Concept | Where |
|---|---|---|
| 1 | Class & Object | All model classes |
| 2 | Encapsulation | Private fields with getters/setters |
| 3 | Abstraction | `Person`, `Repository<T>` |
| 4 | Inheritance | `User`/`Resident` extend `Person`; custom exceptions; custom UI components |
| 5 | Polymorphism | `getPersonType()`, `Repository` references |
| 6 | Method Overriding | `getId()`, `toString()`, `paintComponent()` |
| 7 | Interfaces | `Identifiable`, `Repository<T>`, `Serializable` |
| 8 | Enums | Roles and statuses |
| 9 | Composition / Association | `HostelService` ↔ `DataStore` ↔ entities |
| 10 | Exception Handling | Custom exception hierarchy, try/catch in UI |
| 11 | Collections & Generics | `LinkedHashMap`, `List`, `Optional`, `MapRepository<T>` |
| 12 | File Handling | `FileDatabase` |
| 13 | Static & Final Members | `Theme` constants, `serialVersionUID` |
| 14 | Lambdas & Streams | Filtering, sorting, summing throughout the service and UI |
| 15 | Inner / Anonymous Classes | Static nested classes; anonymous `MouseAdapter`, `BasicScrollBarUI` |
| 16 | GUI & Event Handling | Swing components, `ActionListener`, mouse and focus listeners |
| 17 | Custom Painting | `Graphics2D` rendering in custom components |

---

## Demo Data

Sidebar → **Load Demo Data** (Admin only) creates, if the tables are empty:

- **5 rooms:** 101 (Single, ₹5000), 102 (Double, ₹4000), 201 (Triple, ₹3000), 202 (Single, ₹5500), 301 (Double, ₹4500)
- **9 beds** distributed across those rooms
- **3 residents:** Rahul Kumar, Priya Sharma, Arjun Patel (password `pass123`)

---

## Known Limitations

- Passwords are stored in **plain text** (suitable for learning/demo only).
- Persistence uses Java serialization, not a real database; not safe for concurrent multi-user use.
- Approving a room request does **not** auto-allocate a bed; allocation is a separate manual step.
- Bills are created manually (no automatic monthly generation).
- Bed/room maintenance and account activation logic exist in the service layer but have **no UI controls** yet.
- No edit/delete screens for existing records; tables show resident IDs in some views.
- Receptionists see the *Rooms* page but have no Beds page.
- Requests and check-outs are processed by typing an ID rather than selecting a table row.
- Tested look-and-feel targets Windows fonts; layout may differ on macOS/Linux.

---

## Future Improvements

- Password hashing (e.g., BCrypt/PBKDF2)
- Migration to a database (SQLite/MySQL via JDBC)
- Row-click actions, search, filter and pagination in tables
- Automatic monthly billing and late-fee calculation
- Auto-allocation on request approval
- Export reports to PDF/CSV and printable receipts
- Edit/delete and maintenance toggles in the UI
- Dark mode and responsive layouts
- Unit tests for the service layer

---

## License

Free to use for educational purposes.
