package org.example.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/tasksServlet")
public class tasksServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Process the first pending task
        processFirstPendingTask();

        // Redirect back to the main web page
        response.sendRedirect("main.html");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String taskDescription = request.getParameter("task_description");

        // Insert the submitted task into the database
        insertTask(taskDescription);

        // Redirect back to the main web page
        response.sendRedirect("main.html");
    }

    private void processFirstPendingTask() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/your_database", "username", "password");

            // Select the first pending task
            String selectSQL = "SELECT * FROM tasks WHERE status = 'pending' ORDER BY id LIMIT 1";
            PreparedStatement selectStatement = connection.prepareStatement(selectSQL);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                int taskId = resultSet.getInt("id");
                String taskDescription = resultSet.getString("task_description");

                // Update the task status to "processing" (or "in progress")
                String updateSQL = "UPDATE tasks SET status = 'processing' WHERE id = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateSQL);
                updateStatement.setInt(1, taskId);
                updateStatement.executeUpdate();

                // Simulate task processing (you should replace this with your actual processing logic)
                simulateTaskProcessing(taskId, taskDescription);

                // Set the task status to "completed" (or delete the row if you want)
                String completeSQL = "UPDATE tasks SET status = 'completed' WHERE id = ?";
                PreparedStatement completeStatement = connection.prepareStatement(completeSQL);
                completeStatement.setInt(1, taskId);
                completeStatement.executeUpdate();
            }

            resultSet.close();
            selectStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertTask(String taskDescription) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/your_database", "username", "password");

            String insertSQL = "INSERT INTO tasks (task_description) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(insertSQL);
            statement.setString(1, taskDescription);
            statement.executeUpdate();

            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void simulateTaskProcessing(int taskId, String taskDescription) {
        // Simulate task processing here.
        // You should replace this with your actual processing logic.
        // For demonstration purposes, we'll simply print the task information.
        System.out.println("Processing Task ID: " + taskId);
        System.out.println("Task Description: " + taskDescription);
    }
}

