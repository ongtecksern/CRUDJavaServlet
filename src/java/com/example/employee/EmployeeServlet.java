package com.example.employee;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "EmployeeServlet", urlPatterns = {"/EmployeeServlet"})
public class EmployeeServlet extends HttpServlet {
//    private static final String DB_URL = "jdbc:sqlserver://10.1.1.191:1434;databaseName=MyDatabase";
//    private static final String USER = "sa";
//    private static final String PASS = "password";
// Replace the following variables with your own values
        private static final String serverName = "10.1.1.191";    // Replace with remote server IP or hostname
        private static final String portNumber = "1434";          // Ensure this is the correct port
        private static final String databaseName = "MyDatabase";  // Replace with your database name
        private static final String username = "sa";              // SQL Server username
        private static final String password = "password";        // SQL Server password

        // Correct JDBC URL format
        private static final String connectionUrl = "jdbc:sqlserver://" + serverName + ":" + portNumber + ";databaseName=" + databaseName + ";encrypt=true;trustServerCertificate=true;";

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing action parameter.");
            return; // Exit if there's no action specified
        }
        switch (action) {
            case "add":
                addEmployee(request, response);
                break;
            case "delete":
                deleteEmployee(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "update":
                updateEmployee(request, response);
                break;
            case "view":
                viewEmployee(request, response);
                break;
            case "view2":
                viewEmployeev2(request, response);
                break;
            default:
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action: " + action);

        }
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void addEmployee(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String name = request.getParameter("name");
    String position = request.getParameter("position");
    double salary;

    try {
        salary = Double.parseDouble(request.getParameter("salary"));
    } catch (NumberFormatException e) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid salary format.");
        return;
    }

    try {
        // Load JDBC driver
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        
        // Establish connection with username and password
        try (Connection conn = DriverManager.getConnection(connectionUrl, username, password)) {
            String sql = "INSERT INTO Employee (name, position, salary) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, position);
            stmt.setDouble(3, salary);
            stmt.executeUpdate();
        }

    } catch (SQLException | ClassNotFoundException e) {
        e.printStackTrace();
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        return;
    }

    response.setContentType("text/html;charset=UTF-8");
    try (PrintWriter out = response.getWriter()) {
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Employee Added</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Employee added successfully</h1>");
        out.println("<a href='EmployeeServlet?action=view'>View Employees</a>");
        out.println("</body>");
        out.println("</html>");
    }
}

    private void viewEmployee(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    List<Employee> employeeList = new ArrayList<>();
    
    try {
        // Load JDBC driver
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        
        // Establish connection with username and password
        try (Connection conn = DriverManager.getConnection(connectionUrl, username, password)) {
            String sql = "SELECT * FROM Employee";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Employee emp = new Employee(rs.getInt("id"), rs.getString("name"), rs.getString("position"), rs.getDouble("salary"));
                employeeList.add(emp);
            }
        }

    } catch (SQLException | ClassNotFoundException e) {
        e.printStackTrace();
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        return;
    }

    request.setAttribute("employeeList", employeeList);
    request.getRequestDispatcher("viewEmployees.jsp").forward(request, response);
}

    
    private void deleteEmployee(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        try (Connection conn = DriverManager.getConnection(connectionUrl, username, password)) {
            String sql = "DELETE FROM Employee WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        response.sendRedirect("EmployeeServlet?action=view");
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        Employee employee = null;

        try {
            // Load JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Establish connection
            try (Connection conn = DriverManager.getConnection(connectionUrl, username, password)) {
                String sql = "SELECT * FROM Employee WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    employee = new Employee(rs.getInt("id"), rs.getString("name"), rs.getString("position"), rs.getDouble("salary"));
                } else{
                    try (PrintWriter out = response.getWriter()) {
                        out.println("<!DOCTYPE html>");
                        out.println("<html>");
                        out.println("<head>");
                        out.println("<title>Employee Added</title>");
                        out.println("</head>");
                        out.println("<body>");
                        out.println("<h1>No Result found</h1>");
                        out.println("<a href='EmployeeServlet?action=view'>View Employees</a>");
                        out.println("</body>");
                        out.println("</html>");
                    }
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
            return;
        }

        request.setAttribute("employee", employee);
        request.getRequestDispatcher("editEmployee.jsp").forward(request, response);
    }

    private void updateEmployee(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String position = request.getParameter("position");
        double salary;

        try {
            salary = Double.parseDouble(request.getParameter("salary"));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid salary format");
            return;
        }

        try {
            // Load JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Establish connection
            try (Connection conn = DriverManager.getConnection(connectionUrl, username, password)) {
                String sql = "UPDATE Employee SET name = ?, position = ?, salary = ? WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, name);
                stmt.setString(2, position);
                stmt.setDouble(3, salary);
                stmt.setInt(4, id);
                stmt.executeUpdate();
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
            return;
        }

        response.sendRedirect("EmployeeServlet?action=view");
    }

    private void viewEmployeev2(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Employee> employeeList = new ArrayList<>();

        // Get sorting and filtering parameters from the request
        String sortBy = request.getParameter("sortBy");
        String filterPosition = request.getParameter("filterPosition");

        // Default SQL query
        String sql = "SELECT * FROM Employee";

        // Apply filtering if provided
        if (filterPosition != null && !filterPosition.isEmpty()) {
            sql += " WHERE position = ?";
        }

        // Apply sorting if provided
        if (sortBy != null) {
            sql += " ORDER BY " + sortBy;
        } else {
            sql += " ORDER BY id";  // Default sorting by ID
        }

        try (Connection conn = DriverManager.getConnection(connectionUrl, username, password)) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            // Set filter value if applicable
            if (filterPosition != null && !filterPosition.isEmpty()) {
                stmt.setString(1, filterPosition);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Employee emp = new Employee(rs.getInt("id"), rs.getString("name"), rs.getString("position"), rs.getDouble("salary"));
                employeeList.add(emp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        

        // Pass employee list and sorting/filtering options to JSP
        request.setAttribute("employeeList", employeeList);
        request.setAttribute("sortBy", sortBy);
        request.setAttribute("filterPosition", filterPosition);

        // Forward to the JSP page to display the employees
        request.getRequestDispatcher("viewEmployeesv2.jsp").forward(request, response);
    }        
}