/**
 * Handles the simplified GUI for the Demo. Currently, only has image sending capabilities.
 *
 * @author Nora El Naby
 *
 * @version 1.0.1
 */

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ClientDemoView extends ClientView {

    private ClientController controller;
    protected JTextField serverIP;
    protected JButton fileSearch = new JButton();
    protected JButton sendFile = new JButton();
    protected JLabel imageName;
    protected JLabel welcomeLabel;
    protected JLabel imagePreview;
    protected JLabel warningMessage;
    private ArrayList<String> imageList;
    protected JList<String> imageOptions;
    protected String[] listOptions;
    protected JButton receiveFile = new JButton();

    public ClientDemoView(ClientController controller){
        this.controller = controller;
        setPreferredSize(new Dimension(500, 400));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Object[] options = {"Send Files", "Receive Files"};
        int sendOrReceive = JOptionPane.showOptionDialog(null, "Welcome to FileSocketFun! " +
                "Would you like to send or receive a file?", "Send or Receive", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if(sendOrReceive == JOptionPane.YES_OPTION) {
            setupSendUI();
        } else if(sendOrReceive == JOptionPane.NO_OPTION){
            String hostIP = JOptionPane.showInputDialog("What's the IP of your server?");
            if(hostIP != null) {
                imageList = controller.getImageList(hostIP);
                listOptions = new String[imageList.size()];
                for(int i = 0; i < imageList.size(); i++) {
                    listOptions[i] = imageList.get(i);
                }
                setupReceiveUI();
            }
        } else {
            dispose();
        }
        pack();
    }

    private void setupSendUI() {
        setVisible(true);

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

    public void setupReceiveUI() {
        imageOptions = new JList<>(listOptions);
        JScrollPane listScroller = new JScrollPane(imageOptions);
        receiveFile = new JButton("Receive File");

        JPanel submitButton = new JPanel();
        JPanel list = new JPanel();

        submitButton.add(receiveFile);
        list.add(listScroller);

        getContentPane().add(list, BorderLayout.CENTER);
        getContentPane().add(submitButton, BorderLayout.SOUTH);
        setVisible(true);
    }

    public String saveFile() {
        JFileChooser whereToSave = new JFileChooser();
        int saved = whereToSave.showSaveDialog(null);
        if(saved == JFileChooser.APPROVE_OPTION) {
            return whereToSave.getSelectedFile().getAbsolutePath();
        }
        else return "BIG ERROR";
    }

    public void fileSentPopup() {
        int option = JOptionPane.showConfirmDialog(null, "Your file has been sent. Would you " +
                "like to send another?");
        if(option == JOptionPane.YES_OPTION) {
            setVisible(false);
            controller.view = new ClientDemoView(controller);
            dispose();
        } else {
            dispose();
        }
    }

    public void errorMessage() {
        JOptionPane.showMessageDialog(null, "There was an error processing your request." +
                " Please close the window and try again. \n Error Code: NOSAVELOCATION");
    }

}
