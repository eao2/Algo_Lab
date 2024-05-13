package algo.algo_lab;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Server {
    static String url = "jdbc:mysql://localhost:3306/algolab";
    static String username = "admin";
    static String password = "admin";
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(10101)) {
            System.out.println("Server started. Waiting for clients...");

            Connection connection = null;
            try {
                // Load the MySQL JDBC driver
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Connect to the database
//                String url = "jdbc:mysql://localhost:3306/algolab";
//                String username = "admin";
//                String password = "admin";
                connection = DriverManager.getConnection(url, username, password);

                // Connection successful
                System.out.println("Connected to the database!");
            } catch (ClassNotFoundException e) {
                System.err.println("Could not load JDBC driver: " + e.getMessage());
            } catch (SQLException e) {
                System.err.println("Error connecting to the database: " + e.getMessage());
            } finally {
                // Close the connection
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        System.err.println("Error closing connection: " + e.getMessage());
                    }
                }
            }

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("User connected from: " + clientSocket.getInetAddress());

                // Start a new thread to handle client connection
                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        String usernameFromDB = null;
        String passwordFromDB = null;
        int id = 0;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                 ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                 BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {


                // Read the initial message to identify the connection type
                String messageType = reader.readLine();
                System.out.println("server: " + messageType);

                // Determine whether it's a client or admin
                if ("CLIENT_CONNECT".equals(messageType)) {
                    System.out.println("Server: Client connected");

                    while (true) {
                        // Receive object from client
                        Object receivedObject = in.readObject();
                        if (receivedObject instanceof ObjectLogin) {
                            // Process the received object
                            ObjectLogin objectLogin = (ObjectLogin) receivedObject;

                            switch (objectLogin.getValue1()) {
                                case 0:
                                    // Extract and print individual values
                                    System.out.println("Received value1 from client: " + objectLogin.getValue1());
                                    System.out.println("Received value2 from client: " + objectLogin.getValue2());
                                    System.out.println("Received value3 from client: " + objectLogin.getValue3());

                                    // Define the SQL query to check username and password
                                    String usernameToCheck = objectLogin.getValue2(); // username you want to check
                                    String passwordToCheck = objectLogin.getValue3(); // password you want to check

                                    String loginQuery = "SELECT * FROM user WHERE username = '" + usernameToCheck + "' AND password = '" + passwordToCheck + "'";


                                    // Send back the same object
                                    out.writeObject("teg" + objectLogin);

                                    // Execute the query and get the result set
                                    try (Connection conn = DriverManager.getConnection(url, username, password);
                                         Statement statement = conn.createStatement();
                                         ResultSet resultSet = statement.executeQuery(loginQuery)) {

                                        // Check if the result set is empty (no matching username and password)
                                        if (!resultSet.next()) {
                                            System.out.println("Invalid username or password");
                                            writer.write("invalid\n");
                                            writer.flush(); // Flush the writer to ensure the message is sent immediately
                                        } else {
                                            System.out.println("Login successful");
                                            writer.write("valid\n");
                                            writer.flush(); // Flush the writer to ensure the message is sent immediately
                                        }
                                    } catch (SQLException e) {
                                        System.err.println("Error executing SQL query: " + e.getMessage());
                                    }

                                    break;
                                case 1:
                                    // Extract and print individual values
                                    System.out.println("Received value1 from client: " + objectLogin.getValue1());
                                    System.out.println("Received value2 from client: " + objectLogin.getValue2());
                                    System.out.println("Received value3 from client: " + objectLogin.getValue3());
                                    System.out.println("Received value4 from client: " + objectLogin.getValue4());

                                    // Define the SQL query to add a new user
                                    String usernameToAdd = objectLogin.getValue2(); // Change this to the username you want to add
                                    String passwordToAdd = objectLogin.getValue3(); // Change this to the password you want to add
                                    String emailToAdd = objectLogin.getValue4(); // Change this to the email you want to add

                                    String addUserQuery = "INSERT INTO user (username, password, email) VALUES ('" + usernameToAdd + "', '" + passwordToAdd + "', '" + emailToAdd + "')";

                                    // Send back the same object
                                    out.writeObject("neg" + objectLogin);

                                    // Execute the query to add the new user
                                    try (Connection conn = DriverManager.getConnection(url, username, password);
                                         Statement statement = conn.createStatement()) {
                                        int rowsAffected = statement.executeUpdate(addUserQuery);
                                        if (rowsAffected > 0) {
                                            System.out.println("User added successfully");
                                            writer.write("success\n");
                                            writer.flush(); // Flush the writer to ensure the message is sent immediately
                                        } else {
                                            System.out.println("Failed to add user");
                                            writer.write("failed\n");
                                            writer.flush(); // Flush the writer to ensure the message is sent immediately
                                        }
                                    } catch (SQLException e) {
                                        System.err.println("Error executing SQL query: " + e.getMessage());
                                    }

                                    break;

                                case 2:
                                    // Extract and print individual values
    //                                System.out.println("Received value1 from client: " + objectLogin.getValue1());
    //                                System.out.println("Received value2 from client: " + objectLogin.getValue2());
                                    System.out.println("Received value3 from client: " + objectLogin.getValue3());
                                    System.out.println("Received value4 from client: " + objectLogin.getValue4());

                                    // Define the SQL query to add a new user
                                    String passwordToUpd = objectLogin.getValue3(); // Change this to the password you want to add
                                    String email = objectLogin.getValue4(); // Change this to the email you want to add

                                    String updUserQuery = "UPDATE user SET password = '" + passwordToUpd + "' WHERE email =  '" + email + "'";

                                    // Send back the same object
                                    out.writeObject("hoyr" + objectLogin);

                                    // Execute the query to add the new user
                                    try (Connection conn = DriverManager.getConnection(url, username, password);
                                         Statement statement = conn.createStatement()) {
                                        int rowsAffected = statement.executeUpdate(updUserQuery);
                                        if (rowsAffected > 0) {
                                            System.out.println("User password updated successfully");
                                            writer.write("success\n");
                                            writer.flush(); // Flush the writer to ensure the message is sent immediately
                                        } else {
                                            System.out.println("Failed to update user password");
                                            writer.write("failed\n");
                                            writer.flush(); // Flush the writer to ensure the message is sent immediately
                                        }
                                    } catch (SQLException e) {
                                        System.err.println("Error executing SQL query: " + e.getMessage());
                                    }

                                    break;
                            }
                        }
//                        else if (receivedObject instanceof ObjectSolve) {
//                            // Process the received object
//                            ObjectSolve objectSolve = (ObjectSolve) receivedObject;
//
//                            // Extract and print individual values
//                            System.out.println("Received value1 from client: " + objectSolve.getValue1());
//                            System.out.println("Received value2 from client: " + objectSolve.getValue2());
//                            System.out.println("Received value3 from client: " + objectSolve.getValue3());
//
//                            // Send back the same object
//                            out.writeObject(objectSolve);
//                        }
                    }
                } else if ("ADMIN_CONNECT".equals(messageType)) {
                    System.out.println("Server: Admin connected");

                    while (true) {
                        // Receive object from client
                        Object receivedObject = in.readObject();
                        if (receivedObject instanceof ObjectProblem) {
                            // Process the received object
                            ObjectProblem objectproblem = (ObjectProblem) receivedObject;

                            // Extract and print individual values
                            System.out.println("Received value1 from client: " + objectproblem.getValue1());
                            System.out.println("Received value2 from client: " + objectproblem.getValue2());
                            System.out.println("Received value3 from client: " + objectproblem.getValue3());

                            // Define the SQL query to add a new user
                            String titleToAdd = objectproblem.getValue1();
                            String descriptionToAdd = objectproblem.getValue2();
                            String codeToAdd = objectproblem.getValue3();

                            String addUserQuery = "INSERT INTO problem (title, description, code) VALUES ('" + titleToAdd + "', '" + descriptionToAdd + "', '" + codeToAdd + "')";

                            // Execute the query to add the new user
                            try (Connection conn = DriverManager.getConnection(url, username, password);
                                 Statement statement = conn.createStatement()) {
                                int rowsAffected = statement.executeUpdate(addUserQuery);
                                if (rowsAffected > 0) {
                                    System.out.println("User added successfully");
                                    writer.write("success\n");
                                    writer.flush(); // Flush the writer to ensure the message is sent immediately
                                } else {
                                    System.out.println("Failed to add user");
                                    writer.write("failed\n");
                                    writer.flush(); // Flush the writer to ensure the message is sent immediately
                                }
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }else{
                            System.out.println("Invalid object");
                            writer.write("invalid\n");
                            writer.flush(); // Flush the writer to ensure the message is sent immediately
                        }
                    }
                } else {
                    System.out.println("Server: Unknown connection type");
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}