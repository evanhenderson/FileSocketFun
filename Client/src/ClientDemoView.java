import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

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
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
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
        imageName.setHorizontalAlignment(JLabel.CENTER);
        welcomeLabel = new JLabel("Welcome to FileSocketFun!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        imagePreview = new JLabel();
        imagePreview.setIcon(new ImageIcon(System.getProperty("user.dir") + controller.fileSeparator + "Client"
                + controller.fileSeparator + "Cache" + controller.fileSeparator + "placeholder.jpg"));


        getContentPane().add(welcomeLabel, BorderLayout.NORTH);
        getContentPane().add(sendFile, BorderLayout.SOUTH);
        JPanel outerCenter = new JPanel(new BorderLayout());
        outerCenter.add(serverIP, BorderLayout.NORTH);
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        outerCenter.add(centerPanel, BorderLayout.CENTER);
        centerPanel.add(imageName);
        JPanel innerCenterPanel = new JPanel();
        innerCenterPanel.setLayout(new BoxLayout(innerCenterPanel, BoxLayout.X_AXIS));
        innerCenterPanel.add(imagePreview);
        innerCenterPanel.add(fileSearch);
        centerPanel.add(innerCenterPanel);
        getContentPane().add(outerCenter, BorderLayout.CENTER);

    }
}
