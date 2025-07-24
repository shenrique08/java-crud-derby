# Java CRUD with Apache Derby

This is a CRUD (Create, Read, Update, Delete) system project for managing a product inventory. The application was developed in Java using a graphical interface with Swing and the Apache Derby database for data persistence.

## 🚀 Overview

The system allows users to perform the following operations:
- Add new products to the inventory
- View the list of registered products
- Update product information and stock quantity (simulating purchases and sales)
- Delete products from the database

The application was designed to be simple and to demonstrate fundamental software development concepts using Java, such as separation of concerns (DAO, Model, UI), database handling with JDBC, and a desktop graphical user interface.

## ✨ Features

- **Simple GUI:** Built with Java Swing for direct user interaction
- **Complete CRUD Operations:**
  - **Create:** Add new products via a form
  - **Read:** Display and refresh product listings in a table
  - **Update:** Modify stock with "Buy" and "Sell" operations
  - **Delete:** Remove selected products from the database
- **Data Persistence:** Uses the Apache Derby relational database to store product data
- **Containerization:** The database can be easily launched in an isolated environment using Docker
- **Logging:** Important actions such as database interactions and user events are logged using Logback

## 🛠️ Technologies Used

- **Language:** Java 17  
- **Build & Dependency Management:** Apache Maven  
- **Database:** Apache Derby (via Docker)  
- **GUI:** Java Swing  
- **Containerization:** Docker & Docker Compose  
- **Logging:** SLF4J & Logback  
- **Utilities:** Lombok  

## 📋 Prerequisites

Make sure you have the following installed:
- Java Development Kit (JDK) 17 or higher  
- Apache Maven  
- Docker and Docker Compose  

## ▶️ How to Run

Follow these steps to run the application locally:

### 1. Start the Database with Docker

Apache Derby is set up to run inside a Docker container. To start it, run the following command in the project root directory (where `compose.yml` is located):

```bash
docker-compose up -d
```

This command will build the image, create, and start the container in detached mode (-d). The database will be available on port 1527.

### 2. Run the Java Application

Once the database is running, you can start the Java application.

Open the project in your preferred IDE (IntelliJ, Eclipse, etc.) and run the main class:

```
src/main/java/org/api/App.java
```

On startup, the application will:
- Connect to the Derby database
- Create the PRODUTO table if it doesn't already exist
- Populate the database with sample data on the first run
- Launch the "Inventory Control" window for interaction

## 📂 Project Structure

```
.
├── .idea/                  # IDE config files (IntelliJ)
├── logs/                   # Application log files
├── src/main
│   ├── java/org/api
│   │   ├── App.java                # Main entry point
│   │   ├── dao/ProdutoDAO.java    # Data Access Object (CRUD logic)
│   │   ├── model/Produto.java     # Product model
│   │   ├── ui/EstoqueFrame.java   # GUI class
│   │   └── util/DatabaseUtil.java # DB connection utility
│   └── resources
│       ├── init.sql               # SQL script to create the table
│       └── logback.xml            # Logging configuration
├── .gitignore              # Git ignored files
├── compose.yml             # Docker Compose service definition
├── Dockerfile              # Docker image setup for Derby
└── pom.xml                 # Maven dependencies and config
```

## 📝 Author

**Henrique** — [shenrique08](https://github.com/shenrique08) on GitHub
