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

    protected ClientModel model;
    protected ClientDemoView view;
    protected String serverIP;
    protected ImageIcon image;
    protected String imageName;
    protected String fileSeparator = System.getProperty("file.separator");

    public ClientController(ClientModel model) {
        this.model = model;
        model.setController(this);
        this.view = new ClientDemoView(this);
        view.fileSearch.addActionListener(new ActionListener() {

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
                model.clearingCache = new File(System.getProperty("user.dir") + fileSeparator + "Client" +
                        fileSeparator + "Cache" + imageName);
                if(serverIP.equals("")) {
                    view.warningMessage.setText("Please enter a valid server IP!");
                } else if (imageName.equals("Image Preview: ")) {
                    view.warningMessage.setText("Please select an image to send!");
                } else {
                    try {
                        System.out.println("Send button was hit.");
                        model.sendData(imageName, serverIP);
                    } catch (IOException e) {
                        view.warningMessage.setText("Something went wrong while sending you image. Please double check" +
                                " your server's IP address and try again!");
                    }
                    view.warningMessage.setText("Sent! Please select another file to send.");
                    view.fileSentPopup();
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
                    view.dispose();
                    model.closeConnection(model.out, model.stdIn, model.fileIn);
                }catch(Exception e) {

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
    public void setUser() {
        //sets the user
    }
    public void imageListSet() {
        //when it receives the list back of what to download, it takes that, and puts it into an array for
    }

    public ArrayList<String> getImageList() {
        model.requestFileNames(view.serverIP.getText());
        return model.availableFiles;
    }

}

class Main {

    public static void main(String[] args) {
        ClientController controller = new ClientController(new ClientModel());
        }
    }

