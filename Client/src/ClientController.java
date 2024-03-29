/**
 * Serves as the middle ground between the ClientView (GUI) and the ClientModel (Socket)
 *
 * @author Nora El Naby
 * @version 1.0.3
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;

public class ClientController {

    /**
     * This is the model the controller uses to interface with the socket
     */
    protected ClientModel model;

    /**
     * This is the view the controller uses to interface with the GUIs
     */
    protected ClientDemoView view;

    /**
     * This is the IP address of the server
     */
    protected String serverIP;

    /**
     * This is a preview of the image that is about to be sent
     */
    protected ImageIcon image;

    /**
     * This is the name of the image that is about to be sent
     */
    protected String imageName;

    /**
     * This is the file separator of the OS being used
     */
    protected String fileSeparator = System.getProperty("file.separator");

    /**
     * The constructor for the client. This sets up everything
     * @param model This is the model the client uses
     */
    public ClientController(ClientModel model) {
        this.model = model;
        model.setController(this);
        this.view = new ClientDemoView(this);
        view.fileSearch.addActionListener(new ActionListener() {

            /**
             * Resizes the image to fit within the given set of dimensions
             * @param image The image being resized
             * @return the resized ImageIcon
             */
            private ImageIcon resizeImage(ImageIcon image) {
                //resizing the image so that it fits within the range
                Image unscaledImage = image.getImage();
                int imageHeight = image.getIconHeight();
                int imageWidth = image.getIconWidth();
                if(imageWidth > 350) {
                    imageWidth = 350;
                    imageHeight = (imageWidth * image.getIconHeight()) / image.getIconWidth();
                } if(imageHeight > 240) {
                    imageHeight = 240;
                    imageWidth = (imageHeight * image.getIconWidth()) / image.getIconHeight();
                }
                Image scaledImage = unscaledImage.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            }

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                 FileDialog file = null;
                 file = new FileDialog(view, "What photo would you like to send?", FileDialog.LOAD);
                     file.setVisible(true);
                     File theFile = null;
                     String findTheFile = file.getFile();
                     if(findTheFile != null) {
                     theFile = new File(findTheFile);
                     String theFileName = theFile.getName();
                     String pathToCachedFile = System.getProperty("user.dir") + fileSeparator + "Client"
                             + fileSeparator + "Cache" + fileSeparator;
                     try {
                         Files.copy(Paths.get(file.getDirectory() + file.getFile()), Paths.get(pathToCachedFile + theFileName),
                                 StandardCopyOption.REPLACE_EXISTING);
                     } catch (IOException e) {
                         e.printStackTrace();
                     }

                     image = new ImageIcon(pathToCachedFile + theFileName);
                     image = resizeImage(image);
                     view.imagePreview.setIcon(image);
                     view.imagePreview.setText(" ");
                     view.imageName.setText(theFileName);
                     }


            }
        });
        view.sendFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                serverIP = view.serverIP.getText();
                imageName = view.imageName.getText();
                model.cachedVersion = new File(System.getProperty("user.dir") + fileSeparator + "Client" +
                        fileSeparator + "Cache" + imageName);
                if(serverIP.equals("")) {
                    view.warningMessage.setText("Please enter a valid server IP!");
                } else if (imageName.equals("Image Preview: ")) {
                    view.warningMessage.setText("Please select an image to send!");
                } else {
                    try {
                        model.sendData(imageName, serverIP);
                    } catch (IOException e) {
                        view.warningMessage.setText("Something went wrong while sending you image. Please double check" +
                                " your server's IP address and try again!");
                    }
                    view.dispose();
                }
            }
        });
        view.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent windowEvent) {
            }

            @Override
            public void windowClosing(WindowEvent windowEvent) {
                try {
                        if(model.stdIn != null) {
                            model.closeConnection();
                        }
                        view.dispose();
                }catch(IOException e) {

                }
            }

            @Override
            public void windowClosed(WindowEvent windowEvent) {

            }

            @Override
            public void windowIconified(WindowEvent windowEvent) {

            }

            @Override
            public void windowDeiconified(WindowEvent windowEvent) {

            }

            @Override
            public void windowActivated(WindowEvent windowEvent) {

            }

            @Override
            public void windowDeactivated(WindowEvent windowEvent) {

            }
        });
        view.receiveFile.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    model.requestFile(view.imageOptions.getSelectedValue());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Retrieves the list of available images from the server
     * @param hostIP the IP address of the server
     * @return An ArrayList containing the names of all the available images
     */
    public ArrayList<String> getImageList(String hostIP) {
        model.requestFileNames(hostIP);
        return model.availableFiles;
    }

    /**
     * Chooses the location to save a received file
     * @return the path to the save location
     */
    public String chooseFileSaveLocation() {
        return view.saveFile();
    }

    /**
     * Sends the user a pop up that lets them know their save location is invalid
     */
    public void invalidSaveLocationMessage() {
        view.errorMessage();
    }

    /**
     * Disposes of the view, and ends the program
     */
    public void endProgram() {
        view.dispose();
    }

    /**
     * Lets the user know their file has been received and saved
     */
    public void fileReceivedMessage() {
        view.fileReceivedPopUp();
    }

    /**
     * Sends the user a popup, letting them know their file has successfully been sent to the server
     */
    public void fileSentMessage() {
        view.fileSentPopUp();
    }

}

/**
 * Private inner class. It just creates a new controller with a new model.
 */
class Main {

    public static void main(String[] args) {
        ClientController controller = new ClientController(new ClientModel());
        }
    }

