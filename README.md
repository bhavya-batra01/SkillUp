# SkillUp - Peer-to-Peer Skill Sharing Platform

## Overview

SkillUp is a console-based Java application that allows users to learn and teach skills within a collaborative environment. Users can contribute educational resources, search for learning materials, earn points by teaching, and spend points to learn new skills.

The project is built using Core Java and JDBC with MySQL for persistent data storage.

---

## Features

### User Authentication
- User Registration
- Secure Login
- Password Hashing
- Duplicate Username Validation

### Skill Learning
- Add skills to learn
- Point deduction system (-15 points)
- Prevent duplicate learning requests
- Checks whether requested skill exists

### Skill Contribution
- Teach new skills
- Upload learning resources
- Supports:
  - PDF
  - Video
  - External Links
- Link validation
- Reward system (+30 points)

### Resource Management
- View all learning resources
- Search resources by skill
- Store resources permanently in MySQL

### Skill Matching
- Matches learners with users teaching the same skill
- Displays available learning resources

### Leaderboard
- Displays users ranked by earned points
- Gamification encourages participation

### User Profile
- View:
  - Username
  - Current Points
  - Learning Skills
  - Teaching Skills

### Data Persistence
All application data is stored in MySQL using JDBC.

---

## Technologies Used

- Java
- Core Java
- JDBC
- MySQL
- Collections Framework
- Object-Oriented Programming
- Multithreading
- SQL

---

## Database

Database Name

```
skillup
```

Tables

```
users
learnskills
teachskills
resources
```

---

## Project Structure

```
SkillUp/
│
├── SkillUp.java
│
├── User
├── Resource
├── Loader (Thread)
│
└── MySQL Database
```

---

## Database Schema

### users

| Column | Type |
|---------|------|
| username | VARCHAR(50) |
| password | VARCHAR(100) |
| points | INT |

---

### learnskills

| Column | Type |
|---------|------|
| username | VARCHAR(50) |
| skill | VARCHAR(50) |

---

### teachskills

| Column | Type |
|---------|------|
| username | VARCHAR(50) |
| skill | VARCHAR(50) |

---

### resources

| Column | Type |
|---------|------|
| id | INT |
| username | VARCHAR(50) |
| skill | VARCHAR(50) |
| type | VARCHAR(30) |
| content | TEXT |

---

## How It Works

### Register
- Create a new account.
- Password is hashed before storage.

### Login
- Authenticate using username and password.

### Learn Skill
- Select a skill to learn.
- 15 points are deducted.
- Skill is added to the learning list.

### Teach Skill
- Upload a skill.
- Add a learning resource.
- Earn 30 reward points.

### Match Skills
- Finds users teaching skills you want to learn.
- Displays their resources.

### Search
- Search available resources by skill.

### Leaderboard
- Displays users ranked according to points.

---

## Point System

| Action | Points |
|---------|---------|
| Learn a Skill | -15 |
| Teach a Skill | +30 |

---

## Security Features

- Password Hashing
- Duplicate Username Prevention
- Minimum Password Validation
- URL Validation for Links
- Login Authentication

---

## OOP Concepts Used

- Classes & Objects
- Encapsulation
- Constructors
- ArrayList Collections
- Static Methods
- Multithreading
- Serializable Objects

---

## JDBC Concepts Used

- DriverManager
- Connection
- Statement
- PreparedStatement
- ResultSet
- SQL CRUD Operations

---

## SQL Operations

- CREATE TABLE
- INSERT
- SELECT
- DELETE

---

## Future Enhancements

- JavaFX/Swing GUI
- Spring Boot REST API
- Email Verification
- Profile Pictures
- Skill Categories
- Ratings & Reviews
- Chat System
- Admin Dashboard
- Password Encryption using BCrypt
- Resource File Upload
- Search Filters
- Notifications
- Certificate Generation

---

## Learning Outcomes

Through this project, the following concepts were implemented:

- Core Java Programming
- JDBC Connectivity
- MySQL Database Design
- Object-Oriented Programming
- Collections Framework
- Multithreading
- User Authentication
- SQL Queries
- Data Persistence
- Console-based Application Development

---

## Author

Bhavya Batra

MCA Student

Developed as an academic project to demonstrate Java programming, JDBC integration, database management, and object-oriented software development.
