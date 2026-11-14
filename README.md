# Departmental Newsletter Content Manager

A Java Swing + MySQL (JDBC) desktop application built for the **Minor Project (NSP)**
course — Java Based Individual Project using **IntelliJ IDEA, Java, MySQL**.

It lets a department maintain a newsletter: content managers log in, write and manage
**Articles**, organise them by **Category** and **Author**, and group them into
**Issues** (newsletter editions) that can be marked Draft or Published.

---

## 1. Features (mapped to the 14-week plan)

| Feature | Where |
|---|---|
| Login / Registration (Java Swing forms) | `ui/LoginForm.java`, `ui/RegisterForm.java` |
| Main dashboard with sidebar navigation | `ui/DashboardForm.java` |
| GUI built with JFrame, JLabel, JTextField, JButton, JTable, JComboBox, GridBagLayout | `ui/panels/*`, `ui/dialogs/*` |
| MySQL database design (5 related tables) | `database/newsletter_db.sql` |
| JDBC connectivity | `db/DBConnection.java` |
| Full CRUD (Create, Read, Update, Delete) | `dao/*DAO.java` |
| Search / filter (by title, category, status) | `dao/ArticleDAO.java` -> `search()`, used in `ui/panels/ArticlePanel.java` |
| Input validation & exception handling | every dialog + DAO layer (SQLException handled, try-with-resources) |

### Modules

* **Articles** — the core content-manager entity: title, content, category, author,
  linked issue, status (`DRAFT` / `SUBMITTED` / `PUBLISHED`), created date.
* **Categories** — e.g. Achievements, Events, Placements, Research.
* **Authors** — faculty/student contributors.
* **Issues** — a newsletter edition/date that groups several articles together.
* **Users** — login accounts (passwords stored as SHA-256 hashes, never plain text).

---

## 2. Project Structure

```
NewsletterCMS/
├── src/com/newsletter/
│   ├── Main.java                  <- run this
│   ├── db/DBConnection.java
│   ├── model/                     <- User, Category, Author, Issue, Article
│   ├── dao/                       <- UserDAO, CategoryDAO, AuthorDAO, IssueDAO, ArticleDAO
│   └── ui/
│       ├── LoginForm.java
│       ├── RegisterForm.java
│       ├── DashboardForm.java
│       ├── panels/                <- ArticlePanel, CategoryPanel, AuthorPanel, IssuePanel
│       └── dialogs/                <- ArticleDialog, CategoryDialog, AuthorDialog, IssueDialog
├── database/newsletter_db.sql     <- run this in MySQL first
├── lib/                            <- put mysql-connector-j-x.x.x.jar here
└── README.md
```

---

## 3. Setup Instructions (IntelliJ IDEA)

### Step 1 — Install prerequisites
1. JDK 17+ (IntelliJ can prompt you to download one).
2. MySQL Server + MySQL Workbench.
3. **MySQL Connector/J** (the JDBC driver). Download the "Platform Independent" ZIP/JAR from:
   `https://dev.mysql.com/downloads/connector/j/`
   Extract it and copy `mysql-connector-j-x.x.x.jar` into the project's `lib/` folder.

### Step 2 — Create the database
Open MySQL Workbench (or the `mysql` CLI) and run the provided script:
```sql
SOURCE /path/to/NewsletterCMS/database/newsletter_db.sql;
```
This creates the `newsletter_db` database, all 5 tables, and seeds:
- an admin login → **username:** `admin` **password:** `admin123`
- sample categories, authors, and one sample issue.

### Step 3 — Open the project in IntelliJ
1. `File -> Open` and select the `NewsletterCMS` folder.
2. If IntelliJ doesn't detect it as a Java project, right-click `src` -> **Mark Directory as -> Sources Root**.
3. Add the JDBC driver as a library:
   `File -> Project Structure -> Modules -> Dependencies -> + -> JARs or Directories`
   -> select `lib/mysql-connector-j-x.x.x.jar` -> OK.

### Step 4 — Configure your database credentials
Open `src/com/newsletter/db/DBConnection.java` and update if needed:
```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/newsletter_db?useSSL=false&serverTimezone=UTC";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "root"; // <-- your MySQL password
```

### Step 5 — Run
Right-click `src/com/newsletter/Main.java` -> **Run 'Main.main()'**.
Log in with `admin` / `admin123`, or register a new account.

---

## 4. Suggested Weekly Usage (matches the 14-week plan)

- **Weeks 1–5**: Study `ui/LoginForm.java` and `ui/RegisterForm.java` to see basic
  Swing components (JLabel, JTextField, JButton) and `ArticleDialog`'s
  `saveBtn.addActionListener` for event handling.
- **Week 6–7**: `DashboardForm.java` shows how a sidebar + `CardLayout` structures a
  multi-screen desktop app — this is your GUI prototype milestone.
- **Week 8**: Walk through `database/newsletter_db.sql` for table design, primary/foreign keys.
- **Week 9**: `db/DBConnection.java` is the single JDBC connectivity point.
- **Week 10**: Each `dao/*DAO.java` class implements `add`, `getAll`/`search`, `update`, `delete` — your CRUD milestone.
- **Week 11–12**: `ui/panels/ArticlePanel.java` integrates the JTable with the database and adds search/filter + validation.
- **Week 13**: Review the exception handling (`try { ... } catch (SQLException ex)`) and validation
  (empty-field checks, email regex, date parsing) throughout the dialogs.
- **Week 14**: Use this README + the SQL script + screenshots of each screen for your final report.

---

## 5. Notes on Security / Design Choices

- Passwords are hashed with SHA-256 (`util/PasswordUtil.java`) before being stored —
  never stored as plain text.
- All SQL is executed through `PreparedStatement` (prevents SQL injection).
- DAOs use try-with-resources so connections/statements/result sets are closed automatically.
- Foreign keys use `ON DELETE SET NULL` so deleting a category/author/issue doesn't
  destroy existing articles — it just unlinks them.

## 6. Possible Extensions (for extra marks)
- Export articles/issue to PDF using a library like iText.
- Add a "Preview Issue" screen that lists all articles linked to one issue.
- Add role-based access (ADMIN can delete, EDITOR cannot).
- Add pagination to the articles table for large datasets.
