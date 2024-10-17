<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Employee</title>
</head>
<body>
    <h2>Add Employee</h2>
    <form action="EmployeeServlet?action=add" method="post">
        <label for="name">Name:</label>
        <input type="text" name="name" id="name"><br>
        <label for="position">Position:</label>
        <input type="text" name="position" id="position"><br>
        <label for="salary">Salary:</label>
        <input type="number" name="salary" id="salary"><br>
        <button type="submit">Add Employee</button>
    </form>
</body
</html>
