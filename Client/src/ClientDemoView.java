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
    protected JLabel warningMessage;

    public ClientDemoView(ClientController controller){
        this.controller = controller;
        setPreferredSize(new Dimension(500, 400));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setupUI();
        pack();
    }

    private void setupUI() {
        serverIP = new JTextField("");
        serverIP.setPreferredSize(new Dimension(300, 30));
        fileSearch = new JButton("Find Image");
        fileSearch.setHorizontalAlignment(JButton.RIGHT);
        sendFile = new JButton("Send");
        imageName = new JLabel("Image Preview: ");
        welcomeLabel = new JLabel("Welcome to FileSocketFun!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        imagePreview = new JLabel();
        imagePreview.setIcon(new ImageIcon(System.getProperty("user.dir") + controller.fileSeparator + "Client"
                + controller.fileSeparator + "Cache" + controller.fileSeparator + "placeholder.jpg"));
        warningMessage = new JLabel("Enter the Host IP address");

        JPanel center = new JPanel(); //this is everything in the center
        JPanel imageCenter = new JPanel(); //this is the image stuff + the fileSearch button
        JPanel image = new JPanel (); //this is the image preview and the image name
        JPanel centerNorth = new JPanel(); //this is the warning Label and server IP
        JPanel searchPanel = new JPanel(); //this is just for the search button, to stop it from being huge
        JPanel sendButton = new JPanel(); //this is just for the send button, to stop if from being huge

        center.setLayout(new BorderLayout());
        image.setLayout(new BoxLayout(image, BoxLayout.Y_AXIS));
        centerNorth.setLayout(new BoxLayout(centerNorth, BoxLayout.Y_AXIS));
        searchPanel.setSize(new Dimension(60, 30));
        sendButton.setSize(new Dimension(60, 30));

        //components filled from the outside in
        searchPanel.add(fileSearch);
        sendButton.add(sendFile);

        image.add(imageName);
        image.add(imagePreview);

        imageCenter.add(image);
        imageCenter.add(searchPanel);

        centerNorth.add(warningMessage);
        centerNorth.add(serverIP);

        //adding all of the JPanels together
        center.add(centerNorth, BorderLayout.NORTH);
        center.add(imageCenter, BorderLayout.CENTER);
        center.add(sendButton, BorderLayout.SOUTH);
        getContentPane().add(welcomeLabel, BorderLayout.NORTH);
        getContentPane().add(center, BorderLayout.CENTER);








    }
}
