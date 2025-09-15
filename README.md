# Event Booking System 🎟️
A full-stack **Event Booking System** built with **Spring Boot (Backend)** and **HTML, CSS, JavaScript (Frontend)**. This project allows users to view events, book tickets, and manage reservations efficiently.

## 🚀 Features
- User-friendly interface built with **HTML, CSS, JS**
- Backend powered by **Spring Boot**
- Event creation and booking functionality
- REST APIs for booking management
- Database integration (MySQL / PostgreSQL / H2)
- Secure configuration (with `application.properties` ignored in Git)

## 🛠️ Tech Stack
**Frontend**  
- HTML5  
- CSS3  
- JavaScript (Vanilla JS)  

**Backend**  
- Spring Boot  
- Spring Data JPA / Hibernate  
- REST API  

**Database**  
- MySQL (or your DB of choice)  

## ⚙️ Setup & Run
### 1. Clone the repository
```bash
git clone https://github.com/SaurabhMalik01/event-booking-system/
cd event-booking-springboot

### 2. Open the project in IntelliJ IDEA
- Open IntelliJ IDEA  
- Select **File → Open**  
- Choose the `event-booking-system` folder you cloned  

### 3. Configure Database
Edit `src/main/resources/application.properties`) 

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/eventdb
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update

### 4. Build & Run the Backend

Using Maven wrapper (from terminal in project root):
```bash
./mvnw spring-boot:run


