# Online Quiz Application

A simple **Java Swing** based quiz application with **SQLite database** integration.  
It supports user authentication, quiz creation, and result tracking.

---

## 🚀 Features
- User login & signup with password hashing (BCrypt)
- Admin can add, edit, and delete quizzes/questions
- Users can take quizzes and see results
- SQLite database for persistence
- Built with **Java 17** and **Maven**

---

## 🛠️ Technologies Used
- **Java 17**
- **Swing (GUI)**
- **SQLite (Database)**
- **Maven** (Build tool)
- **JBCrypt** (Password hashing)

---

## 📂 Project Structure
online-quiz-app/
├── src/
│ ├── main/
│ │ ├── java/com/quizapp/...
│ │ └── resources/
│ └── test/
├── pom.xml
└── README.md

---

## ⚡ Prerequisites
- Install [Java 17](https://adoptium.net/)
- Install [Maven](https://maven.apache.org/install.html)

---

## ▶️ Run the Project

1. Clone the repository:
   ```sh
   git clone https://github.com/your-username/online-quiz-app.git
   cd online-quiz-app

2.Build the project:

mvn clean package -DskipTests


3.Run the application:

mvn exec:java