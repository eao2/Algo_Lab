package algo.algo_lab;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.nio.file.*;

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
                            // Fetch data from MySQL table "problems"
                            System.out.println("fetchDataFromDatabase()");
                            ArrayList<String> arrayList = fetchDataFromDatabase();
                            out.writeObject(arrayList);
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
                            int case11 = objectproblem.getValue3();
                            int case12 = objectproblem.getValue4();
                            int case21 = objectproblem.getValue5();
                            int case22 = objectproblem.getValue6();
                            int case31 = objectproblem.getValue7();
                            int case32 = objectproblem.getValue8();

                            String addUserQuery = "INSERT INTO problem (title, description, case11, case12, case21, case22, case31, case32) VALUES ('" + titleToAdd + "', '" + descriptionToAdd + "', '" + case11 + "', '" + case12 + "', '" + case21 + "', '" + case22 + "', '" + case31 + "', '" + case32 + "')";

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
                        }

                        else{
                            System.out.println("Invalid object");
                            writer.write("invalid\n");
                            writer.flush(); // Flush the writer to ensure the message is sent immediately
                        }
                    }
                }
                else if ("PROBLEM_CONNECT".equals(messageType)) {
                    System.out.println("Server: PROBLEM connected");

                    // Read the initial message to identify the connection type
                    String message1 = reader.readLine();
                    System.out.println("server: " + message1);

//                            String addUserQuery = "INSERT INTO problem (title, description, case11, case12, case21, case22, case31, case32) VALUES ('" + titleToAdd + "', '" + descriptionToAdd + "', '" + case11 + "', '" + case12 + "', '" + case21 + "', '" + case22 + "', '" + case31 + "', '" + case32 + "')";


                    try (Connection conn = DriverManager.getConnection(url, username, password);
                         Statement statement = conn.createStatement()) {
                        ResultSet resultSet = statement.executeQuery("SELECT * FROM problem WHERE title = '" + message1 + "'");


                        while (resultSet.next()) {
                            int id = resultSet.getInt("id");
                            String title = resultSet.getString("title");
                            String description = resultSet.getString("description");
                            int Case11 = resultSet.getInt("case11");
                            int Case12 = resultSet.getInt("case12");
                            int Case21 = resultSet.getInt("case21");
                            int Case22 = resultSet.getInt("case22");
                            int Case31 = resultSet.getInt("case31");
                            int Case32 = resultSet.getInt("case32");
                            java.sql.Timestamp createdAt = resultSet.getTimestamp("created_at");

                            // Process the data here. For example, print it out:
                            System.out.println("ID: " + id);
                            System.out.println("Title: " + title);
                            System.out.println("Description: " + description);
                            System.out.println("Case 11: " + Case11);
                            System.out.println("Case 12: " + Case12);
                            System.out.println("Case 21: " + Case21);
                            System.out.println("Case 22: " + Case22);
                            System.out.println("Case 31: " + Case31);
                            System.out.println("Case 32: " + Case32);
                            System.out.println("Created At: " + createdAt);
                            System.out.println("---------------");


                            ObjectProblem objectProblem = new ObjectProblem(title, description, Case11, Case12, Case21, Case22, Case31, Case32);
                            try {
                                out.writeObject(objectProblem);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }

                            Object algotrithm = in.readObject();
                            if (algotrithm instanceof ObjectProblemClient) {
                                // Process the received object (optional)
                                ObjectProblemClient receivedAlgotrithm = (ObjectProblemClient) algotrithm;
                                System.out.println("Received object from client: " + receivedAlgotrithm);

                                String username = receivedAlgotrithm.getValue1();
                                String problemName = receivedAlgotrithm.getValue2();
                                String algotrithmCode = receivedAlgotrithm.getValue3();


//                                        String cppCode = algotrithmCode;
                                String outputDir = "D:\\Code\\Java HW\\2.2\\algo_lab\\src\\main\\resources";
                                String cppFileName = "temp.cpp";
                                String executableName = "temp.exe";

                                try {
                                    String cppCode = algotrithmCode;

                                    // Create the output directory if it doesn't exist
                                    File outputDirFile = new File(outputDir);
                                    if (!outputDirFile.exists()) {
                                        outputDirFile.mkdirs();
                                    }

                                    // Write the C++ code to a temporary file in the output directory
                                    File cppFile = new File(outputDir, cppFileName);
                                    try (FileWriter writerr = new FileWriter(cppFile)) {
                                        writerr.write(cppCode);
                                    }

                                    // Compile the C++ code with the output executable in the specified directory
                                    ProcessBuilder compileBuilder = new ProcessBuilder("g++", "-o", new File(outputDir, executableName).getPath(), cppFile.getAbsolutePath());
                                    compileBuilder.redirectErrorStream(true); // Redirect error stream to output stream
                                    Process compileProcess = compileBuilder.start();

                                    // Capture and print the output of the compilation process
                                    try (BufferedReader compileReader = new BufferedReader(new InputStreamReader(compileProcess.getInputStream()))) {
                                        String line;
                                        while ((line = compileReader.readLine()) != null) {
                                            System.out.println(line);
                                        }
                                    }

                                    int compileExitCode = compileProcess.waitFor();
                                    if (compileExitCode == 0) {
                                        System.out.println("Compilation successful!");

                                        // Execute the compiled program
                                        ProcessBuilder executeBuilder = new ProcessBuilder(new File(outputDir, executableName).getAbsolutePath());
                                        executeBuilder.redirectErrorStream(true);
                                        Process executeProcess = executeBuilder.start();

                                        // Provide input to the executed process
                                        BufferedWriter inputWriter = new BufferedWriter(new OutputStreamWriter(executeProcess.getOutputStream()));
                                        inputWriter.write(Integer.toString(Case11)); // Convert integer to string
                                        inputWriter.newLine(); // Add newline to indicate end of input
                                        inputWriter.flush(); // Flush the writer to ensure the input is sent immediately

                                        // Capture and print the output of the execution process
                                        try (BufferedReader executeReader = new BufferedReader(new InputStreamReader(executeProcess.getInputStream()))) {
                                            String line;
                                            while ((line = executeReader.readLine()) != null) {
                                                System.out.println(line);
                                                // Attempt to parse the line as an integer
                                                try {
                                                    int outputValue = Integer.parseInt(line.trim());
                                                    // Compare the output value with the expected value
                                                    if (outputValue == Case12) {
                                                        // Output matches expected value
                                                        System.out.println("Output matches expected value.");
                                                        writer.write("success\n");
                                                        writer.flush(); // Flush the writer to ensure the message is sent immediately

                                                    } else {
                                                        // Output doesn't match expected value
                                                        System.out.println("Output doesn't match expected value.");
                                                        writer.write("invalid\n");
                                                        writer.flush(); // Flush the writer to ensure the message is sent immediately
                                                    }
                                                } catch (NumberFormatException e) {
                                                    // Line doesn't contain a valid integer
                                                    // Skip or handle this case as needed
                                                }
                                            }
                                        }


                                        int executeExitCode = executeProcess.waitFor();
                                        if (executeExitCode == 0) {
                                            System.out.println("Execution successful!");
                                        } else {
                                            System.out.println("Execution failed!");
                                        }

                                        // Delete temporary files
                                        cppFile.delete();
                                        new File(outputDir, executableName).delete();
                                    } else {
                                        System.out.println("Compilation failed!");
                                        // Optionally capture and print error messages if needed
                                        printProcessErrors(compileProcess);
                                    }
                                } catch (IOException | InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }


                    Object receivedObject1 = in.readObject();
                    if (receivedObject1 instanceof ObjectProblem) {
                        // Process the received object (optional)
                        ObjectProblem receivedObjectProblem = (ObjectProblem) receivedObject1;
                        System.out.println("Received object from server: " + receivedObjectProblem);
                    }
                }
                else {
                    System.out.println("Server: Unknown connection type");
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private static void printProcessErrors(Process process) throws IOException {
        try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            String line;
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }
        }
    }
    private static ArrayList<String> fetchDataFromDatabase() throws SQLException {
        ArrayList<String> problemsList = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT title FROM problem");

            while (resultSet.next()) {
                String problemName = resultSet.getString("title");
                problemsList.add(problemName);
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }

        return problemsList;
    }
}