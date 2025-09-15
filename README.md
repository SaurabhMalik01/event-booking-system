# Event Booking System 🎟️

A full-stack **Event Booking System** built with **Spring Boot (Backend)** and **HTML, CSS, JavaScript (Frontend)**. This project allows users to view events, book tickets, and manage reservations efficiently.

## 🚀 Features

- **User-friendly interface** built with HTML, CSS, JS
- **Backend powered by Spring Boot** with RESTful APIs
- **Event creation and booking functionality**
- **Real-time seat availability** tracking
- **User registration and authentication**
- **Booking confirmation and management**
- **Admin panel** for event management
- **Database integration** with MySQL
- **Secure configuration** with environment-based properties
- **Responsive design** for mobile and desktop

## 🛠️ Tech Stack

**Frontend**
- HTML5
- CSS3
- JavaScript (Vanilla JS)

**Backend**
- Spring Boot
- Spring Data JPA / Hibernate
- Spring Security (for authentication)
- REST API
- Maven (Build tool)

**Database**
- MySQL

**Tools & Others**
- IntelliJ IDEA / Eclipse
- Postman (for API testing)
- Git & GitHub

## ⚙️ Setup & Run

### Prerequisites
- Java 17 or higher
- MySQL Server
- Maven 3.6+
- Git

### 1. Clone the repository
```bash
git clone https://github.com/SaurabhMalik01/event-booking-system.git
cd event-booking-system
```

### 2. Database Setup
```sql
-- Create database
CREATE DATABASE eventdb;

-- Create user (optional)
CREATE USER 'eventuser'@'localhost' IDENTIFIED BY 'eventpass123';
GRANT ALL PRIVILEGES ON eventdb.* TO 'eventuser'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Configure Database
Create `src/main/resources/application.properties`:
```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/eventdb
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.properties.hibernate.format_sql=true

# Server Configuration
server.port=8080

# Logging
logging.level.com.yourpackage=DEBUG
logging.level.org.springframework.security=DEBUG
```

### 4. Open the project in IntelliJ IDEA
- Open **IntelliJ IDEA**
- Select **File → Open**
- Choose the `event-booking-system` folder you cloned
- Wait for Maven dependencies to download

### 5. Build & Run the Backend
**Using Maven wrapper** (from terminal in project root):
```bash
# For Linux/Mac
./mvnw spring-boot:run

# For Windows
mvnw.cmd spring-boot:run
```

**Using IDE:**
- Right-click on the main application class
- Select "Run Application"

**Using Maven directly:**
```bash
mvn clean install
mvn spring-boot:run
```

### 6. Access the Application
- **Frontend**: Open `index.html` in your browser or serve it via a local server
- **Backend API**: http://localhost:8080


### Manual Testing
1. Start the application
2. Open Postman or use curl
3. Test the API endpoints
4. Verify database operations

## 🔒 Security Features

- **JWT Authentication** for secure API access
- **Password encryption** using BCrypt
- **Role-based access control** (User/Admin)
- **SQL injection prevention** with JPA
- **CORS configuration** for cross-origin requests


## 👨‍💻 Author

**Saurabh Malik**
- GitHub: [@SaurabhMalik01](https://github.com/SaurabhMalik01)


⭐ **If you found this project helpful, please give it a star!** ⭐
