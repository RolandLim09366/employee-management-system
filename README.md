# Employee Management System

## HOW TO RUN THIS PROJECT?

### FROM THE IDE (IntelliJ IDEA):

1. Clone the repository by opening IntelliJ IDEA, selecting "New Project from Version Control," and pasting the GitHub repository URL.
2. Ensure MySQL is running as the database will be automatically created using JPA and Hibernate.
3. Update database credentials in application.properties by modifying the URL, username, and password to match your MySQL settings.
4. Run the project by right-clicking the Application class and selecting "Run 'Application'" in IntelliJ IDEA.
5. Verify the server is running by ensuring the application starts without errors on http://localhost:8080.
6. Open Postman client.
7. Test the following URLs:
    - `http://localhost:8080/employees`
    - `http://localhost:8080/departments`
    - `http://localhost:8080/projects`
8. Use the appropriate HTTP request method for each endpoint:

### Department:

- `GET` - `http://localhost:8080/departments` - Retrieve all departments
- `GET` - `http://localhost:8080/departments/{id}` - Retrieve department by ID
- `POST` - `http://localhost:8080/departments` - Add a new department
- `PUT` - `http://localhost:8080/departments/{id}` - Update department by ID
- `DELETE` - `http://localhost:8080/departments/{id}` - Delete department by ID

### Employee:

- `GET` - `http://localhost:8080/employees` - Retrieve all employees
- `GET` - `http://localhost:8080/employees/{id}` - Retrieve employee by ID
- `POST` - `http://localhost:8080/employees` - Add a new employee
- `PUT` - `http://localhost:8080/employees/{id}` - Update employee by ID
- `DELETE` - `http://localhost:8080/employees/{id}` - Delete employee by ID

### Project:

- `GET` - `http://localhost:8080/projects` - Retrieve all projects
- `GET` - `http://localhost:8080/projects/{id}` - Retrieve project by ID
- `POST` - `http://localhost:8080/projects` - Add a new project
- `PUT` - `http://localhost:8080/projects/{id}` - Update project by ID
- `DELETE` - `http://localhost:8080/projects/{id}` - Delete project by ID

## BASIC AUTHENTICATION

The API is secured with basic authentication.

### Default Credentials:

- **Username:** `user`
- **Password:** `password`

To access the endpoints, include the credentials in the request header.

**Example Request with Authentication (Postman):**

- Go to **Authorization** → Select **Basic Auth**.
- Enter the username and password.
- Click **Send**.

## ASSUMPTIONS:

1. The database and tables are automatically created using JPA and Hibernate.
2. `departmentID` is a foreign key in the `employee` table.
3. Ensure that the `department` table contains values before inserting employees.
4. `project` table exists and employees can be assigned to projects.
5. When inserting an employee via Postman, provide a valid `departmentID`.

## TECHNOLOGY STACK:

1. Java
2. IntelliJ IDEA
3. MySQL Workbench
4. Postman for API testing
5. Spring Security for authentication

## DESIGN DISCUSSION:

1. The `employee` table references `department` via a foreign key.
2. The `project` table has a many-to-many relationship with `employee`.
3. The `department` table must contain records before adding employees.
4. HTTP Methods:
   - `GET`: Retrieve records
   - `POST`: Insert records
   - `PUT`: Update records
   - `DELETE`: Remove records
5. Database creation is required. Modify `application.properties` if necessary.
