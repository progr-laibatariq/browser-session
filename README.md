# Browser Session Management System (DSA-Based) — Spring Boot + MySQL + UI

A complete browser-session simulation project built for demonstrating **Data Structures & Algorithms** in a real backend system.  
The system manages **sessions** and **tabs**, supports **LRU eviction**, **undo/redo**, **expiration scheduling (min-heap)**, and **Top-K analytics** using custom data structures (no Java Collections).

---

## Table of Contents
- [Project Summary](#project-summary)
- [Key Functionalities](#key-functionalities)
- [DSA & Algorithms Used](#dsa--algorithms-used)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Database Setup (XAMPP/phpMyAdmin)](#database-setup-xamppphpmyadmin)
- [Run the Project](#run-the-project)
- [Frontend UI (Design B - Light Mode)](#frontend-ui-design-b---light-mode)
- [API Endpoints](#api-endpoints)
- [Testing Without Postman (Windows CMD + curl)](#testing-without-postman-windows-cmd--curl)
- [How to Demonstrate (Milestone Demo Script)](#how-to-demonstrate-milestone-demo-script)
- [Big-O Complexity Summary](#bigo-complexity-summary)
- [Notes](#notes)

---

## Project Summary
This project simulates a browser system where:
- A **Session** represents a browser window.
- A **Tab** represents a webpage (URL) opened inside a session.

### Important Course Constraint
✅ **Java built-in Collections and built-in algorithms are NOT used.**  
All required structures (stack, heap, deque, hash map, linked list, dynamic array) are implemented manually under the project’s `dsa/` package.

---

## Key Functionalities

### 1) Session Management
- Create a new session with a configurable **max tab capacity**
- List sessions (for UI dropdown)

### 2) Tab Management
- Open a tab (URL)
- Access (visit/focus) a tab
- Close a tab manually
- List tabs in a session (shows active tab, expiry time, last access time)

### 3) LRU Capacity Eviction
When the number of tabs exceeds session capacity:
- The system evicts the **Least Recently Used** tab(s)
- Eviction is logged into `eviction_log`
- Activity is recorded in `activity_log`

### 4) Full Undo / Redo System
- Every major operation is a **Command** with `execute()` and `undo()`
- Two custom stacks:
  - `undoStack`
  - `redoStack`
- Undo/Redo also restores/reapplies evictions when required

### 5) Session/Tab Expiration (Min-Heap Scheduler)
- Sessions/tabs can expire automatically
- A **MinHeap** scheduler processes expiry events by time order

### 6) Analytics Top-K (Sliding Window + Heaps)
- Tracks tab access events
- Returns **Top-K most accessed tabs** in the last `windowSeconds`
- Uses:
  - Deque (sliding window)
  - Hash map (frequency counts)
  - Max heap (Top-K extraction)

### 7) Persistent Logging (Database Proof)
- `tab_access_log` records access events
- `activity_log` records all operations (open/access/close/undo/redo/expire/evict)
- `eviction_log` records evictions with timestamp (`evicted_at`)

---

## DSA & Algorithms Used

### LRU Eviction
- **Doubly Linked List (DLL)** maintains recency order:
  - Head = most recently used
  - Tail = least recently used
- **CustomHashMap(tabId → DLL node)** enables O(1) access and updates

**Result:** Access/Open updates order in **O(1)**, eviction removes tail in **O(1)** (excluding DB I/O).

### Undo/Redo
- Command Pattern:
  - `execute()` applies change
  - `undo()` reverses change
- Two custom stacks:
  - undoStack
  - redoStack

**Result:** Undo/Redo stack operations are **O(1)**.

### Expiration Scheduling
- **MinHeap** of expiry events `(expiresAtMillis, entityType, entityId)`

**Result:** Scheduling and processing expiry events is **O(log n)**.

### Analytics Top-K
- **CustomDeque** holds access events within window
- **CustomHashMap** stores counts per tabId
- **MaxHeap** ranks counts to return top K

**Result:** Update per access is amortized **O(1)**; query is **O(n + k log n)**.

---

## Tech Stack
- **Backend:** Java, Spring Boot
- **Database:** MySQL (XAMPP/phpMyAdmin)
- **Frontend:** HTML + CSS + JavaScript (served by Spring Boot static resources)
- **Build Tool:** Maven
- **Testing Tool:** Windows CMD + `curl` (Postman not required)

---

## Project Structure


- `src/main/java/.../controller/`  
  REST controllers (sessions, tabs, analytics)

- `src/main/java/.../service/`  
  Core business logic, runtime state, managers

- `src/main/java/.../dao/`  
  JDBC DAOs for MySQL tables

- `src/main/java/.../command/`  
  Command Pattern for undo/redo (Open, Access, Close, etc.)

- `src/main/java/.../dsa/`  
  Custom data structures (no Java Collections)

- `src/main/java/.../domain/`  
  DTOs/records for API output (TabRecord, SessionSummary, EvictionEntry, ActivityEntry, etc.)

- `src/main/resources/static/`  
  Frontend UI (Design B Light Mode)
  - `index.html`
  - `styles.css`
  - `app.js`

---

## Database Setup (XAMPP/phpMyAdmin)

### Database Name

### Steps
1. Start **MySQL** from XAMPP Control Panel
2. Open **phpMyAdmin**
3. Create database:
   - `browser_session_db`
4. Import/run your SQL schema (`tables.sql`) into this database

✅ Confirm tables exist (examples):
- `sessions`
- `tabs`
- `tab_access_log`
- `activity_log`
- `eviction_log` (timestamp column: `evicted_at`)

---

## Run the Project

### 1) Start MySQL
- XAMPP → Start **MySQL**

### 2) Run Spring Boot
In IntelliJ:
- Run `BrowserSessionApplication.java`

The app runs at:
http://localhost:8080

UI includes:
- Session selector + Create session
- Undo / Redo buttons
- Browser-like tab bar
- Active tab panel
- Tabs list with actions (Access / Close)
- Right drawer:
  - Analytics Top-K
  - Evictions
  - Activity logs

---

## API Endpoints

### Sessions
- `POST /api/sessions`  
  Body:
  ```json
  { "maxCapacity": 3 }
