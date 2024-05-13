// Problem.java
package algo.algo_lab;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicLabelUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatGitHubDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatGitHubIJTheme;


public class Problem {
    private final JFrame frame;
    private final JComboBox<String> themeComboBox;
    public Problem(JFrame mainFrame, JComboBox<String> themeComboBox, String problemName) {
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
//        JPanel sidePanel = new JPanel(new BorderLayout());


         String descriptionText = """
                this is test of multiple line test aaaaaaaaasafsfsdfdsfd
                haha
                idk
                gue
                """;

        // Create JTextArea and JButtons
        JTextArea codeArea = new JTextArea();
        codeArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel description = new JLabel("<html>" + descriptionText.replaceAll("\n", "<br>") + "</html>");
        description.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        description.setPreferredSize(new Dimension(200, 100));
        description.setVerticalAlignment(SwingConstants.TOP); // Align text to the top
        description.setHorizontalAlignment(SwingConstants.LEFT); // Align text to the left
        JButton button = new JButton("Click me");
        JButton button3 = new JButton("Click me");

        // Customize the preferred size of the buttons
        Dimension buttonSize = new Dimension(150, 50);
        button.setPreferredSize(buttonSize);
        button3.setPreferredSize(buttonSize);

        // Add components to the main panel
        mainPanel.add(codeArea, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Panel to hold buttons
        buttonPanel.add(button);
        buttonPanel.add(button3);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(description, BorderLayout.WEST);
        // Add panels to the frame
//        frame.add(sidePanel, BorderLayout.WEST);
        frame.add(mainPanel, BorderLayout.CENTER);

        // Add ActionListener to button3
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String codeText = codeArea.getText();
                description.setText("<html>" + codeText.replaceAll("\n", "<br>") + "</html>");
            }
        });
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
}
