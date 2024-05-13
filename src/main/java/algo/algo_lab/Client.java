
package algo.algo_lab;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.jar.JarFile;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatGitHubDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatGitHubIJTheme;


public class Client {
    public static JFrame mframe;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;
    private static Problem newFrame;
    private static boolean showFrame = false;
    // Declare BufferedReader globally
    private static BufferedReader reader;
    private static BufferedWriter writer;
    private static String Username;
    private static String IP;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // Set the default theme
        try {
            UIManager.setLookAndFeel(new FlatGitHubDarkIJTheme());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Create a JFrame
        mframe = new JFrame();
        mframe.setTitle("Algo.Lab");
        mframe.setSize(800, 600);
        mframe.setLocationRelativeTo(null);
        ImageIcon icon = new ImageIcon("src/main/resources/algo/algo_lab/icon.png");
        mframe.setIconImage(icon.getImage());
        mframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mframe.setLayout(new BorderLayout()); // Set BorderLayout

        IP = JOptionPane.showInputDialog("Ip хаяг?","127.0.0.1");
        System.out.println(IP);
        // Establish the socket connection
        establishConnection();

        writer.write("CLIENT_CONNECT\n");
        System.out.println("Client: CLIENT_CONNECT");
        writer.flush(); // Flush the writer to ensure the message is sent immediately

        // Create a JComboBox with theme options
        JComboBox<String> themeComboBox = new JComboBox<>(new String[]{"FlatGitHubDarkIJTheme", "FlatGitHubIJTheme"});
        themeComboBox.setSelectedItem("FlatGitHubDarkIJTheme"); // Set default theme

        // Create an instance of the Problem class
        newFrame = new Problem(mframe, themeComboBox, "abb");

        // Create an ActionListener for the themeComboBox
        themeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedTheme = (String) themeComboBox.getSelectedItem();
                try {
                    if (selectedTheme.equals("FlatGitHubDarkIJTheme")) {
                        UIManager.setLookAndFeel(new FlatGitHubDarkIJTheme());
                    } else if (selectedTheme.equals("FlatGitHubIJTheme")) {
                        UIManager.setLookAndFeel(new FlatGitHubIJTheme());
                    }
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            SwingUtilities.updateComponentTreeUI(mframe); // Update the UI
                            newFrame.updateTheme();
                        }
                    });
                } catch (UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }

            }
        });

        // Create a JPanel for main content
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel sidePanel = new JPanel(new BorderLayout());

        // Create JTextArea and JButtons
        JTextArea codeArea = new JTextArea();
        JButton button = new JButton("Click me");
        JButton button3 = new JButton("Click me");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create and show the new frame
                newFrame.getFrame().setVisible(true);
            }
        });
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    showInputDialog(null);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        // Customize the preferred size of the buttons
        Dimension buttonSize = new Dimension(150, 50);
        button.setPreferredSize(buttonSize);
        button3.setPreferredSize(buttonSize);

        // Add components to the side panel
        sidePanel.add(themeComboBox, BorderLayout.NORTH);

        // Add components to the main panel
        mainPanel.add(codeArea, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Panel to hold buttons
        buttonPanel.add(button);
        buttonPanel.add(button3);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add panels to the frame
        mframe.add(sidePanel, BorderLayout.WEST);
        mframe.add(mainPanel, BorderLayout.CENTER);
        showInputDialog(null);
        System.out.println("showInputDialog");
        // Make the frame visible
        mframe.setVisible(showFrame);
    }
    private static void showInputDialog(JFrame parent) throws IOException, ClassNotFoundException {

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JTextField usernameLogin = new JTextField(10);
        JTextField passwordLogin = new JPasswordField(10);
        inputPanel.add(new JLabel("Нэвтрэх нэр:"));
        inputPanel.add(usernameLogin);
        inputPanel.add(new JLabel("Нууц үг:"));
        inputPanel.add(passwordLogin);

        JPanel inputRegPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        JTextField usernameRegister = new JTextField(10);
        JTextField emailRegister = new JTextField(10);
        JTextField passwordRegister = new JPasswordField(10);
        inputRegPanel.add(new JLabel("Нэр:"));
        inputRegPanel.add(usernameRegister);
        inputRegPanel.add(new JLabel("Имэйл:"));
        inputRegPanel.add(emailRegister);
        inputRegPanel.add(new JLabel("Нууц үг:"));
        inputRegPanel.add(passwordRegister);

        JPanel inputRecPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JTextField emailRec = new JTextField(10);
        JTextField passwordRec = new JPasswordField(10);
        inputRecPanel.add(new JLabel("Имэйл:"));
        inputRecPanel.add(emailRec);
        inputRecPanel.add(new JLabel("Нууц үг:"));
        inputRecPanel.add(passwordRec);

        // Nevtreh dialogiin tovchnuud
        Object[] options = {"Нэвтрэх", "Бүртгүүлэх", "Нууц үг шинэчлэх"};
        Object[] optionsR = {"Бүртгүүлэх", "Буцах"};
        Object[] optionsU = {"Нууц үг шинэчлэх", "Буцах"};

        int result = JOptionPane.showOptionDialog(
            parent, // Etseg component
            inputPanel, // Input компонентуудыг агуулсан Panel
            "Нэвтрэх", // Dialogiin garchig
            JOptionPane.DEFAULT_OPTION, // Default option
            JOptionPane.PLAIN_MESSAGE, // Hooson message (icon)
            null, // Custom icon
            options, // Tovchnuudiig aguulsan array
            options[0] // Default tovch
        );

        if (result == 0) {
            Username = usernameLogin.getText();
            String password = passwordLogin.getText();

            // Create and send object to server
            ObjectLogin objectLogin = new ObjectLogin(0, Username, password, "");
            out.writeObject(objectLogin);
            System.out.println("Object sent to server: " + objectLogin);

            // Receive object from server
            Object receivedObject = in.readObject();
            if (receivedObject instanceof ObjectLogin) {
                // Process the received object (optional)
                ObjectLogin receivedObjectLogin = (ObjectLogin) receivedObject;
                System.out.println("Received object from server: " + receivedObjectLogin);
            }

            String nevtreh = reader.readLine();
            System.out.println("nevtreh " + nevtreh);
            if(nevtreh.equals("valid")){
                showFrame = true;
            }else{
                System.out.println("Хэрэглэгчийн нэр нууц үг таарахгүй байна!");
                JOptionPane.showMessageDialog(parent, "Хэрэглэгчийн нэр нууц үг таарахгүй байна!", "Алдаа гарлаа!", JOptionPane.WARNING_MESSAGE);
                showInputDialog(parent);
            }

        } else if (result == 1) {
            // Burtguuleh optionii dialog
            int registerResult = JOptionPane.showOptionDialog(
                    parent,
                    inputRegPanel,
                    "Бүртгүүлэх",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    optionsR,
                    optionsR[0]);
            if (registerResult == 0) {
                String username = usernameRegister.getText();
                String email = emailRegister.getText();
                String password = passwordRegister.getText();

                // Create and send object to server
                ObjectLogin objectLogin = new ObjectLogin(1, username, password, email);
                out.writeObject(objectLogin);
                System.out.println("Object sent to server: " + objectLogin);

                // Receive object from server
                Object receivedObject = in.readObject();
                if (receivedObject instanceof ObjectLogin) {
                    // Process the received object (optional)
                    ObjectLogin receivedObjectLogin = (ObjectLogin) receivedObject;
                    System.out.println("Received object from server: " + receivedObjectLogin);
                }

                String register = reader.readLine();
                System.out.println("register " + register);
                if(register.equals("success")){
                    JOptionPane.showMessageDialog(null, username + " Таний бүртгэл амжилттай бүртгэгдлээ!", "Амжилттай бүртгэгдлээ!", JOptionPane.INFORMATION_MESSAGE);
                    showInputDialog(parent);
                }else{
                    JOptionPane.showMessageDialog(parent, "Та нэр нууц үгээ зөв оруулна уу!", "Бүртгүүлэх", JOptionPane.INFORMATION_MESSAGE);
                    showInputDialog(parent);
                }

            } else {
                showInputDialog(parent);
            }
        } else if (result == 2) {
            // Nuuts ug solih option
            int updateResult = JOptionPane.showOptionDialog(
                parent,
                inputRecPanel,
                "Нууц үг шинэчлэх",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                optionsU,
                optionsU[0]);

            String email = emailRec.getText();
            String password = passwordRec.getText();
            if (updateResult == 0) {
                // Ner nuuts ug hooson bish baigaag shalgah
                if (!email.equals("") && !password.equals("")) {
                    ObjectLogin objectLogin = new ObjectLogin(2, "", password, email);
                    out.writeObject(objectLogin);
                    System.out.println("Object sent to server: " + objectLogin);

                    // Receive object from server
                    Object receivedObject = in.readObject();
                    if (receivedObject instanceof ObjectLogin) {
                        // Process the received object (optional)
                        ObjectLogin receivedObjectLogin = (ObjectLogin) receivedObject;
                        System.out.println("Received object from server: " + receivedObjectLogin);
                    }

                    String update = reader.readLine();
                    System.out.println("update: " + update);
                    if(update.equals("success")){
                        JOptionPane.showMessageDialog(parent, "Нууц үг амжилттай солигдлоо!", "Амжилттай!", JOptionPane.INFORMATION_MESSAGE);
                        showInputDialog(parent);
                    }else {
                        JOptionPane.showMessageDialog(parent, "Хэрэглэгчийн мэдээлэл буруу байна!", "Алдаа гарлаа!", JOptionPane.WARNING_MESSAGE);
                        showInputDialog(parent);
                    }

                } else {
                    JOptionPane.showMessageDialog(parent, "Хэрэглэгчийн мэдээлэл буруу байна!", "Алдаа гарлаа!", JOptionPane.WARNING_MESSAGE);
                    showInputDialog(parent);
                }
            } else {
                showInputDialog(parent);
            }

        }

    }

    private static void establishConnection() {
        try {
            Socket socket = new Socket(IP, 10101);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
//            in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
//            out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
