# Remedium Labs - Medical Laboratory Management System (MLMS)

A modern, robust desktop Medical Laboratory Management System built with **JavaFX** and **SQLite**, designed to streamline patient records, lab test orders, and diagnostic report generation.

---

## 🔬 Features

- **Dashboard**: High-level summary displaying daily statistics (Total Patients, Pending Tests, Completed Tests).
- **Patient Management**: Register, search, update, and manage patient profiles with auto-generated patient numbers.
- **Test Catalog**: Manage medical tests, categories, reference ranges, and test fees.
- **Results & Diagnostics**: Record test observations, enter diagnostic values, and track order completion status.
- **Laboratory Reports**: Printable, structured lab reports styled for clinical delivery.
- **Authentication & Settings**: Simple PIN/password protection for sensitive configurations.

---

## 🛠️ Tech Stack

- **Language:** Java 17+
- **UI Framework:** JavaFX (FXML & CSS)
- **Database:** SQLite (embedded via JDBC)
- **Build Tool:** Apache Maven
- **Icons & Assets:** FontAwesomeFX / Custom SVG & ICO

---

## 🚀 Getting Started

### Prerequisites

- [Java Development Kit (JDK) 17+](https://adoptium.net/)
- [Apache Maven 3.8+](https://maven.apache.org/)

### Build and Run

1. **Clone the repository:**
   ```bash
   git clone https://github.com/NoCodingNoLife1/remedy.git
   cd remedy
   ```

2. **Compile and package:**
   ```bash
   mvn clean package
   ```

3. **Run the application:**
   ```bash
   mvn javafx:run
   ```

---

## 📁 Project Structure

```text
mlms/
├── src/
│   └── main/
│       ├── java/com/mlms/
│       │   ├── controller/    # JavaFX UI Controllers
│       │   ├── dao/           # Data Access Objects (SQLite)
│       │   ├── model/         # Domain Data Models
│       │   └── util/          # Utilities & Database Helpers
│       └── resources/com/mlms/
│           ├── css/           # Modern clinical UI stylesheets
│           ├── fxml/          # FXML layout views
│           └── images/        # Logos and graphics
├── pom.xml                    # Maven configuration
└── README.md
```

---

## 📄 License

This project is licensed under the MIT License.

