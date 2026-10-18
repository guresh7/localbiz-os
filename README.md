# LocalBiz OS

A terminal-based product management system built with **Java**, **JDBC**, and **SQLite**.

LocalBiz OS helps users manage products, search inventory, update stock, receive low-stock alerts, and generate bills. It stores product information in a local SQLite database, so you do not need to install or run a separate database server.

## Features

- Add new products
- View all products
- Search for products
- Update product details
- Delete products
- Manage stock by adding or removing quantities
- Receive low-stock alerts when stock falls below 5 units
- Identify out-of-stock products
- Generate bills for multiple products
- Calculate bill totals
- Automatically update inventory after billing
- Store product information in an SQLite database
- Keep product data between program runs

Each product contains a product ID, product name, category, price, quantity, and supplier.

## Requirements

### Required

- **Java Development Kit (JDK) 21 or newer**
  - The JDK includes `java` and `javac`.
  - The project was developed using Java 21.

- **SQLite JDBC driver**
  - The project uses the JDBC library located at `lib/sql.jar`.
  - Keep the JAR file in the `lib` directory, or update the classpath commands to match its location.

### Optional

- Visual Studio Code for editing and running the project.
- Git for version control.
- GitHub for hosting the source code.

### No separate database server required

SQLite is an embedded database. The application connects to a local database file named `localbiz.db`.

The database stores product information between program runs. Back up the database if you need to preserve its data.

**Important:** The database path is relative to the application's working directory. Run the program from the project directory to use the expected database file.

## Project Files

The repository has the following structure:

```text
localbiz-os/
├── Main.java
├── DatabaseSetup.java
├── DatabaseTest.java
├── ProductManager.java
├── ViewProducts.java
├── SearchProduct.java
├── UpdateProduct.java
├── DeleteProduct.java
├── StockManagement.java
├── BillingSystem.java
├── lib/
│   └── sql.jar
├── localbiz.db
├── .gitignore
└── README.md