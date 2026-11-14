-- ==========================================================
-- Departmental Newsletter Content Manager
-- Database Schema (MySQL)
-- ==========================================================

DROP DATABASE IF EXISTS newsletter_db;
CREATE DATABASE newsletter_db;
USE newsletter_db;

-- ---------------------------------------------------------
-- Users table: login accounts for content managers/editors
-- ---------------------------------------------------------
CREATE TABLE users (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(50)  NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name     VARCHAR(100) NOT NULL,
    role          VARCHAR(20)  NOT NULL DEFAULT 'EDITOR',   -- ADMIN / EDITOR
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ---------------------------------------------------------
-- Categories: e.g. Achievements, Events, Placements, Research
-- ---------------------------------------------------------
CREATE TABLE categories (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255)
);

-- ---------------------------------------------------------
-- Authors / Contributors: faculty & students writing content
-- ---------------------------------------------------------
CREATE TABLE authors (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    department VARCHAR(100),
    email      VARCHAR(100)
);

-- ---------------------------------------------------------
-- Issues: a newsletter edition that groups several articles
-- ---------------------------------------------------------
CREATE TABLE issues (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    title      VARCHAR(150) NOT NULL,
    issue_date DATE,
    status     VARCHAR(20) NOT NULL DEFAULT 'DRAFT'   -- DRAFT / PUBLISHED
);

-- ---------------------------------------------------------
-- Articles: the actual newsletter content
-- ---------------------------------------------------------
CREATE TABLE articles (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    title        VARCHAR(200) NOT NULL,
    content      TEXT NOT NULL,
    category_id  INT,
    author_id    INT,
    issue_id     INT,
    status       VARCHAR(20) NOT NULL DEFAULT 'DRAFT', -- DRAFT / SUBMITTED / PUBLISHED
    created_date DATE NOT NULL,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL,
    FOREIGN KEY (author_id)   REFERENCES authors(id)    ON DELETE SET NULL,
    FOREIGN KEY (issue_id)    REFERENCES issues(id)     ON DELETE SET NULL
);

-- ---------------------------------------------------------
-- Seed data
-- ---------------------------------------------------------

-- Default login -> username: admin , password: admin123
-- (password_hash below is SHA-256 hex digest of "admin123")
INSERT INTO users (username, password_hash, full_name, role) VALUES
('admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'Administrator', 'ADMIN');

INSERT INTO categories (name, description) VALUES
('Achievements', 'Student and faculty achievements'),
('Events', 'Workshops, seminars and department events'),
('Placements', 'Placement and internship updates'),
('Research', 'Research papers and publications');

INSERT INTO authors (name, department, email) VALUES
('Dr. R. Sharma', 'Computer Science', 'r.sharma@example.edu'),
('Ms. Priya Verma', 'Computer Science', 'priya.verma@example.edu');

INSERT INTO issues (title, issue_date, status) VALUES
('Newsletter - Semester 3, Issue 1', CURDATE(), 'DRAFT');
