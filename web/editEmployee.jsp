<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <title>Edit Employee</title>
    </head>
    <body>
        <h1>Edit Employee</h1>

        <form action="EmployeeServlet?action=update" method="POST">            
            <input type="hidden" name="id" value="${employee.id}">

            <label for="name">Name:</label>
            <input type="text" name="name" value="${employee.name}" required><br>

            <label for="position">Position:</label>
            <input type="text" name="position" value="${employee.position}" required><br>

            <label for="salary">Salary:</label>
            <input type="text" name="salary" value="${employee.salary}" required><br>

            <input type="submit" value="Update">
        </form>

        <a href="EmployeeServlet?action=view">Cancel</a>
    </body>
</html>
