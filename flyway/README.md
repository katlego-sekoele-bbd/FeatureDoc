# Flyway Database Migrations

This project uses **Flyway** for managing database migrations. Follow the instructions below to configure and run the migrations on your local or production environment.

## Prerequisites

Make sure you have the following files set up and configured:

1. **.env** or **.env.local** file:  
   These files contain your database connection variables. The `.env.local` file can be used to override the values in `.env` for local development.

2. **migrate.sh script**:  
   The script to run Flyway migrations using the environment variables specified in `.env` and `.env.local`.

---

## Environment Variables

Create a `.env` file in the root of the project (or a `.env.local` file for local overrides) and add the following environment variables:

```ini
# Database variables
POSTGRES_HOST=your_database_host
POSTGRES_PORT=5432
POSTGRES_USER=your_database_user
POSTGRES_PASSWORD=your_database_password
POSTGRES_DB=your_database_name
POSTGRES_SCHEMA=your_database_schema
```

Replace the placeholders with your actual database connection details.

- **POSTGRES_HOST**: The hostname or IP address of the PostgreSQL server.
- **POSTGRES_PORT**: The port PostgreSQL is running on (default is `5432`).
- **POSTGRES_USER**: The username to connect to the database.
- **POSTGRES_PASSWORD**: The password for the PostgreSQL user.
- **POSTGRES_DB**: The name of the database.
- **POSTGRES_SCHEMA**: The PostgreSQL schema where migrations will run.

You can create and configure the `.env` or `.env.local` file manually or use tools like [dotenv](https://www.npmjs.com/package/dotenv) to load them automatically.

---

## Migration Filename Conventions

Flyway uses specific naming conventions for migration files. This helps to maintain a clear and consistent versioning system, and ensures that the migrations are applied in the correct order.

### **1. Versioned Migrations**

- **Location**: `sql/migrations/`
- **Format**: `Vyyyy.MM.dd.HH.mm__Description.sql`
- **Description**: These migrations represent changes that are applied in a specific order, based on the version number (`yyyy.MM.dd.HH.mm`). The version number should be unique and follow the format `yyyy.MM.dd.HH.mm` (year, month, day, hour, minute).
- **Example**: 
  - `V2025.03.11.09.30__create_users_table.sql`
  - `V2025.03.11.12.00__add_email_column_to_users.sql`
  
  These migrations will be executed in chronological order based on the timestamp in the filename.

### **2. Repeatable Migrations**

- **Location**: `sql/repeatable_scripts/`
- **Format**: `R__Description.sql`
- **Description**: These migrations are applied every time Flyway is run and they are not versioned. Repeatable migrations are useful for schema changes that should be reapplied whenever the migration script changes.
- **Example**: 
  - `R__create_indexes.sql`
  - `R__update_user_data.sql`

  These migrations will be re-executed if their content has changed since the last execution, regardless of when they were applied.

### **3. Data Migrations**

- **Location**: `sql/data/`
- **Format**: `R__Description.sql`
- **Description**: These scripts are used for data-related migrations, such as inserting or updating data. Like repeatable migrations, they are executed in order but are not versioned.
- **Example**:
  - `R__insert_initial_data.sql`
  - `R__update_user_email_data.sql`

  These migrations are typically used to seed or modify data in the database after schema changes.

---

## Running Migrations

To run the database migrations, use the provided `migrate.sh` script. The script will automatically source the environment variables from the `.env` and `.env.local` files and execute Flyway migrations.

### **Steps to Run Migrations:**

1. Ensure that the `.env` and/or `.env.local` files are set up with the correct database variables as shown above.

2. Make the `migrate.sh` script executable:

   ```bash
   chmod +x migrate.sh
   ```

3. Run the migration script:

   ```bash
   ./migrate.sh
   ```

This will source the environment variables, set up the connection string, and run Flyway migrations on your PostgreSQL database.

### **Steps to insert seed data:**

1. Ensure that the `.env` and/or `.env.local` files are set up with the correct database variables as shown above.

2. Make the `migrate.sh` script executable:

   ```bash
   chmod +x seed-db.sh
   ```

3. Run the migration script:

   ```bash
   ./seed-db.sh
   ```

---

## Troubleshooting

- **Missing `.env` or `.env.local` file**: Ensure you have the required environment files with the correct database details.
- **Flyway command not found**: Ensure Flyway is installed and available in your system's `PATH`. You can install Flyway by following the official installation instructions: [Flyway Installation](https://flywaydb.org/documentation/getstarted/).

---

## Additional Notes

- **Local Development**: You can create a `local.env` or `.env.local` file to override the values in `.env` for development-specific configurations.
- **Version Control**: Ensure that sensitive information like database passwords is not checked into version control. Use `.gitignore` to exclude `.env` files from version control if necessary.
