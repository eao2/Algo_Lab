
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


public class Admin {
    public static JFrame mframe;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;
    private static boolean showFrame = false;
    // Declare BufferedReader globally
    private static BufferedReader reader;
    private static BufferedWriter writer;
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
        mframe.setTitle("Algo.Lab - Admin");
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

        writer.write("ADMIN_CONNECT\n");
        System.out.println("Admin: ADMIN_CONNECT");
        writer.flush(); // Flush the writer to ensure the message is sent immediately

        // Create a JComboBox with theme options
        JComboBox<String> themeComboBox = new JComboBox<>(new String[]{"FlatGitHubDarkIJTheme", "FlatGitHubIJTheme"});
        themeComboBox.setSelectedItem("FlatGitHubDarkIJTheme"); // Set default theme

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
                        }
                    });
                } catch (UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }

            }
        });

        // Create a JPanel for main content
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel descPanel = new JPanel(new BorderLayout());
        JPanel codePanel = new JPanel(new BorderLayout());

        themeComboBox.setPreferredSize(new Dimension(200, 36));

        // Create JTextArea and JButtons
        JLabel titleText = new JLabel(" Гарчиг:");
        titleText.setPreferredSize(new Dimension(200, 36));
        JTextField titleField = new JTextField();
        titleField.setPreferredSize(new Dimension(500, 36));
        JLabel descAreaText = new JLabel("Тайлбар:");
        descAreaText.setPreferredSize(new Dimension(600, 36));
        JTextArea descArea = new JTextArea();
        descArea.setPreferredSize(new Dimension(600, 200));
        JLabel codeAreaText = new JLabel("Код:");
        codeAreaText.setPreferredSize(new Dimension(600, 36));
        JTextArea codeArea = new JTextArea();
        JButton button = new JButton("Бодлого нэмэх");



        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                String description = descArea.getText();
                String code = codeArea.getText();
                // Create and send object to server
                ObjectProblem objectProblem = new ObjectProblem(title, description, code);
                try {
                    out.writeObject(objectProblem);
                    String status = reader.readLine();
                    if(status.equals("success")){
                        JOptionPane.showMessageDialog(null, "Амжилттай Хадгалагдлаа", "Амжилттай", JOptionPane.PLAIN_MESSAGE);
                    }else{
                        JOptionPane.showMessageDialog(null, "Амжилтгүй Хадгалагдлаа", "Амжилтгүй", JOptionPane.PLAIN_MESSAGE);
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // Customize the preferred size of the buttons
        Dimension buttonSize = new Dimension(120, 36);
        button.setPreferredSize(buttonSize);

        // Add components to the side panel
        topPanel.add(themeComboBox, BorderLayout.WEST);
        topPanel.add(titleText, BorderLayout.CENTER);
        topPanel.add(titleField, BorderLayout.EAST);

        descPanel.add(descAreaText, BorderLayout.NORTH);
        descPanel.add(descArea, BorderLayout.CENTER);

        codePanel.add(codeAreaText, BorderLayout.NORTH);
        codePanel.add(codeArea, BorderLayout.CENTER);

        // Add components to the main panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Panel to hold buttons
        buttonPanel.add(button);
        mainPanel.add(descPanel, BorderLayout.NORTH);
        mainPanel.add(codePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add panels to the frame
        mframe.add(topPanel, BorderLayout.NORTH);
        mframe.add(mainPanel, BorderLayout.CENTER);
        // Make the frame visible
        mframe.setVisible(true);
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
