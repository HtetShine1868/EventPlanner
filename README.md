# 🎯 Event Management System (EMS) - Backend

This is the **backend API** for the Event Management System (EMS), built with **Spring Boot** and **PostgreSQL**. It handles user authentication, event creation, admin approvals, and user registrations through secure RESTful endpoints.

## 📌 Features

- ✅ **JWT Authentication** (Login / Register)
- 👥 **Role-Based Access Control** (User / Organizer / Admin)
- 📅 **Event Management** (CRUD for organizers)
- ✅ **Admin Approval System**
- 📥 **User Event Registration**
- 🔍 **Search, Filter & Trending Events**
- 🤖 **Chatbot-ready Endpoints**

## 🧱 Tech Stack

| Layer        | Technology        |
|--------------|-------------------|
| Language     | Java 21+          |
| Framework    | Spring Boot       |
| Database     | PostgreSQL        |
| ORM          | Hibernate (JPA)   |
| Security     | Spring Security + JWT |
| Build Tool   | Maven             |

## 🗂️ Project Structure

src/
├── main/
│ ├── java/
│ │ └── com.project.EventPlanner/
│ │ ├── common/
│ │ ├── config/
│ │ │ ├── AppConfig.java
│ │ │ └── WebSecurityConfig.java
│ │ ├── exception/
│ │ ├── features/
│ │ │ ├── admin/
│ │ │ ├── auth/
│ │ │ ├── chatbot/
│ │ │ ├── event/
│ │ │ ├── feedback/
│ │ │ ├── registration/
│ │ │ └── user/
│ │ ├── security/
│ │ │ ├── CustomUserDetail.java
│ │ │ ├── CustomUserDetailService.java
│ │ │ └── SecurityConfig.java
│ │ └── EventPlannerApplication.java
│ └── resources/
└── test/

