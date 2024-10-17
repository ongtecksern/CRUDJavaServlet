<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>View Employees</title>
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
        }
        table, th, td {
            border: 1px solid black;
        }
        th, td {
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
        .filter-form {
            margin-bottom: 20px;
        }
    </style>
</head>
<body>

    <h1>Employee List</h1>

    <!-- Filter and Sort Form -->
    <form method="post" action="EmployeeServlet?action=view2" class="filter-form">
        <!--<input type="hidden" name="action" value="view2" />-->
        <label for="filterPosition">Filter by Position:</label>
        <input type="text" name="filterPosition" id="filterPosition" value="${filterPosition}" />
        
        <label for="sortBy">Sort by:</label>
        <select name="sortBy" id="sortBy">
            <option value="id" ${sortBy == 'id' ? 'selected' : ''}>ID</option>
            <option value="name" ${sortBy == 'name' ? 'selected' : ''}>Name</option>
            <option value="position" ${sortBy == 'position' ? 'selected' : ''}>Position</option>
            <option value="salary" ${sortBy == 'salary' ? 'selected' : ''}>Salary</option>
        </select>
        
        <input type="submit" value="Filter and Sort" /><br/>
        
    </form>
        

    <!-- Employee Table -->
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Position</th>
                <th>Salary</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <%--<c:forEach var="employee" items="${employeeList}">
                <tr>
                    <td>${employee.id}</td>
                    <td>${employee.name}</td>
                    <td>${employee.position}</td>
                    <td>${employee.salary}</td>
                    <td>
                        <a href="EmployeeServlet?action=edit&id=${employee.id}">Edit</a> |
                        <a href="EmployeeServlet?action=delete&id=${employee.id}" onclick="return confirm('Are you sure?');">Delete</a>
                    </td>
                </tr>
            </c:forEach>--%>
           <c:if test="${not empty employeeList}">
                <c:forEach var="employee" items="${employeeList}">
                    <tr>
                        <td>${employee.id}</td>
                        <td>${employee.name}</td>
                        <td>${employee.position}</td>
                        <td>${employee.salary}</td>
                        <td>
                            <a href="EmployeeServlet?action=edit&id=${employee.id}">Edit</a> |
                            <a href="EmployeeServlet?action=delete&id=${employee.id}" onclick="return confirm('Are you sure?');">Delete</a>
                        </td>
                    </tr>
                </c:forEach>                
            </c:if>
            <c:if test="${empty employeeList}">
                <tr>
                    <td colspan="5">No employees found.</td>
                </tr>
            </c:if>
        </tbody>
    </table>

    <!-- Link to Add Employee -->
    <p><a href="addEmployee.jsp">Add New Employee</a></p>

</body>
</html>
