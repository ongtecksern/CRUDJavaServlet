<%@page import="java.util.List"%>
<%@page import="com.example.employee.Employee"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>View Employees</title>
</head>
<body>
    <h2>Employee List</h2>
    <table border="1">
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Position</th>
            <th>Salary</th>
            <th>Actions</th>
        </tr>
        <%
            List<Employee> employees = (List<Employee>) request.getAttribute("employeeList");
            if (employees != null) {
                for (Employee emp : employees) {
        %>
        <tr>
            <td><%= emp.getId() %></td>
            <td><%= emp.getName() %></td>
            <td><%= emp.getPosition() %></td>
            <td><%= emp.getSalary() %></td>
            <td>
                <a href="EmployeeServlet?action=edit&id=<%= emp.getId() %>">Edit</a> |
                <a href="EmployeeServlet?action=delete&id=<%= emp.getId() %>">Delete</a>
            </td>
        </tr>
        <%      }
            } else { %>
        <tr>
            <td colspan="5">No Employees Found</td>
        </tr>
        <% } %>
    </table>
    
    <a href="addEmployee.jsp">Add New Employee</a>
</body>
</html>
