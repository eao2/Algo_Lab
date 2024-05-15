package algo.algo_lab;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatGitHubDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatGitHubIJTheme;

public class Problem {
    private final JFrame frame;
    private final JComboBox<String> themeComboBox;

    private static ObjectOutputStream out;
    private static ObjectInputStream in;
    private static BufferedReader reader;
    private static BufferedWriter writer;

    private static String IP;

    public Problem(JFrame mainFrame, String Username, JComboBox<String> themeComboBox, String IPv, String problemName) throws IOException, ClassNotFoundException {
        this.themeComboBox = themeComboBox;
        frame = new JFrame(mainFrame.getGraphicsConfiguration());
        frame.setTitle(problemName);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(mainFrame); // Center the frame relative to the main frame
        ImageIcon icon = new ImageIcon("src/main/resources/algo/algo_lab/icon.png");
        frame.setIconImage(icon.getImage());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this frame
        frame.setLayout(new BorderLayout());
        // Create a JPanel for main content
        JPanel mainPanel = new JPanel(new BorderLayout());
        IP = IPv;

        establishConnection();

        writer.write("PROBLEM_CONNECT\n");
        System.out.println("PROBLEM: PROBLEM_CONNECT");
        writer.flush(); // Flush the writer to ensure the message is sent immediately

        writer.write(problemName + "\n");
        System.out.println(problemName + "\n");
        writer.flush(); // Flush the writer to ensure the message is sent immediately

        Object receivedObject = in.readObject();
        // Process the received object (optional)
        ObjectProblem receivedObjectProblem = (ObjectProblem) receivedObject;
        System.out.println("Received object from server: " + receivedObjectProblem);

        String title = receivedObjectProblem.getValue1();
        String desc = receivedObjectProblem.getValue2();

        String descriptionText = desc;

        // Create JTextArea and JButtons
        JTextArea codeArea = new JTextArea();
        codeArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel description = new JLabel("<html>" + descriptionText.replaceAll("\n", "<br>") + "</html>");
        description.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        description.setPreferredSize(new Dimension(200, 100));
        description.setVerticalAlignment(SwingConstants.TOP); // Align text to the top
        description.setHorizontalAlignment(SwingConstants.LEFT); // Align text to the left
        JButton button = new JButton("Submit");

        // Customize the preferred size of the buttons
        Dimension buttonSize = new Dimension(150, 50);
        button.setPreferredSize(buttonSize);

        // Add components to the main panel
        mainPanel.add(codeArea, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Panel to hold buttons
        buttonPanel.add(button);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(description, BorderLayout.WEST);
        // Add panels to the frame
        frame.add(mainPanel, BorderLayout.CENTER);

        // Add ActionListener to button
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String codeText = codeArea.getText();
                ObjectProblemClient objectProblemSolve = new ObjectProblemClient(title, desc, codeText);
                try {
                    out.writeObject(objectProblemSolve);
                    out.flush();

                    new SwingWorker<String, Void>() {
                        @Override
                        protected String doInBackground() throws Exception {
                            return reader.readLine();
                        }

                        @Override
                        protected void done() {
                            try {
                                String status = get();
                                if (status.equals("success")) {
                                    JOptionPane.showMessageDialog(null, "Зөв хариуллаа!", "Амжилттай", JOptionPane.PLAIN_MESSAGE);
                                } else {
                                    JOptionPane.showMessageDialog(null, "Буруу хариуллаа!", "Амжилтгүй", JOptionPane.PLAIN_MESSAGE);
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }.execute();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        frame.setVisible(true);
    }

    public JFrame getFrame() {
        return frame;
    }

    public void updateTheme() {
        try {
            if (themeComboBox.equals("FlatGitHubDarkIJTheme")) {
                UIManager.setLookAndFeel(new FlatGitHubDarkIJTheme());
            } else if (themeComboBox.equals("FlatGitHubIJTheme")) {
                UIManager.setLookAndFeel(new FlatGitHubIJTheme());
            }
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    SwingUtilities.updateComponentTreeUI(frame); // Update the UI of the frame
                    frame.repaint(); // Trigger a repaint of the frame
                }
            });
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
    }

    private static void establishConnection() {
        try {
            Socket socket = new Socket(IP, 10101);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
