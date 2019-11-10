import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class ClientDemoView extends ClientView {

    private ClientController controller;
    protected JTextField serverIP;
    protected JButton fileSearch;
    protected JButton sendFile;
    protected JLabel imageName;
    protected JLabel welcomeLabel;
    protected JLabel imagePreview;

    public ClientDemoView(ClientController controller){
        this.controller = controller;
        setPreferredSize(new Dimension(500, 400));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setupUI();
        pack();
    }

    private void setupUI() {
        serverIP = new JTextField("Host IP");
        serverIP.setPreferredSize(new Dimension(300, 30));
        fileSearch = new JButton("Find Image");
        fileSearch.setHorizontalAlignment(JButton.RIGHT);
        sendFile = new JButton("Send");
        imageName = new JLabel("Image Preview: ");
        imageName.setHorizontalAlignment(JLabel.LEFT);
        welcomeLabel = new JLabel("Welcome to FileSocketFun!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        imagePreview = new JLabel();

        getContentPane().add(welcomeLabel, BorderLayout.NORTH);
        getContentPane().add(sendFile, BorderLayout.SOUTH);
        JPanel centerPanel = new JPanel();
        GridBagLayout grid = new GridBagLayout();
        centerPanel.setLayout(grid);
        centerPanel.add(serverIP);
        centerPanel.add(imageName);
        centerPanel.add(imagePreview);
        centerPanel.add(fileSearch);
        getContentPane().add(centerPanel, BorderLayout.CENTER);

    }
}
