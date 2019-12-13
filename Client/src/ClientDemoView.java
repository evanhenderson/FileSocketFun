/**
 * Handles the simplified GUI for the Demo. Currently, only has image sending capabilities.
 *
 * @author Nora El Naby
 *
 * @version 1.0.1
 * @see no borrowed code
 */

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ClientDemoView extends ClientView {

    /**
     * The controller the view communicates to
     */
    private ClientController controller;

    /**
     * The IP address of the server
     */
    protected JTextField serverIP;

    /**
     * Searches for a file to send to the sever
     */
    protected JButton fileSearch = new JButton();

    /**
     * Sends the chosen file to hte server
     */
    protected JButton sendFile = new JButton();

    /**
     * The name of the image in the preview
     */
    protected JLabel imageName;

    /**
     * Welcomes the user, and gives instructions on how to use the program
     */
    protected JLabel welcomeLabel;

    /**
     * Shows a preview of the image
     */
    protected JLabel imagePreview;

    /**
     * Lets the user know if they have not chosen either an image or an IP address
     */
    protected JLabel warningMessage;

    /**
     * The list of available images, sent from the server
     */
    private ArrayList<String> imageList;

    /**
     * The list of images, listed in a JScrollPane
     */
    protected JList<String> imageOptions;

    /**
     * availableFiles transcribed into a String[], so that it can be fed into imageOptions
     */
    protected String[] listOptions;

    /**
     * Receives the chosen file from the server
     */
    protected JButton receiveFile = new JButton();

    /**
     * The constructor for a ClientDemoView.
     * Sets up the JFrame, and asks the user if they would like to send or receive an image
     * @param controller The controller that the view communicates with
     */
    public ClientDemoView(ClientController controller){
        this.controller = controller;
        setPreferredSize(new Dimension(500, 400));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

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
            } else {
                System.exit(0);
            }
        } else {
            System.exit(0);
        }
        pack();
    }

    /**
     * Sets up the UI to send an image
     */
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

    /**
     * Sets up the UI to receive an image
     */
    private void setupReceiveUI() {
        imageOptions = new JList<>(listOptions);
        JScrollPane listScroller = new JScrollPane(imageOptions);
        receiveFile = new JButton("Receive File");

        JPanel submitButton = new JPanel();

        submitButton.add(receiveFile);

        getContentPane().add(listScroller, BorderLayout.CENTER);
        getContentPane().add(submitButton, BorderLayout.SOUTH);
        setVisible(true);
    }

    /**
     * Prompts the user to select a save location for the image they are about to download
     * @return the path to the chosen save location
     */
    public String saveFile() {
        JFileChooser whereToSave = new JFileChooser();
        int saved = whereToSave.showSaveDialog(null);
        if(saved == JFileChooser.APPROVE_OPTION) {
            return whereToSave.getSelectedFile().getAbsolutePath();
        }
        else return "BIG ERROR";
    }

    /**
     * Sends the user an error message, if the location they chose was invalid.
     */
    public void errorMessage() {
        JOptionPane.showMessageDialog(null, "There was an error processing your request." +
                " Please close the window and try again. \n Error Code: NOSAVELOCATION");
    }

    /**
     * Sends the user a pop up to let them know their file has been received and saved on their machine.
     */
    public void fileReceivedPopUp() {
        JOptionPane.showMessageDialog(null, "Your file has been received. Thank you.");
    }

    /**
     * Sends the user a pop up to let them know their file has been successfully sent to the server.
     */
    public void fileSentPopUp() {
        JOptionPane.showMessageDialog(null, "Your file has been sent! Thank you for using" +
                " FileSocketFun");
    }

}
