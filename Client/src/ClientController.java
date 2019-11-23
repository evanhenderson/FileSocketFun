import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.nio.file.*;

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
                if(serverIP.equals("")) {
                    view.warningMessage.setText("Please enter a valid server IP!");
                } else if (imageName.equals("Image Preview: ")) {
                    view.warningMessage.setText("Please select an image to send!");
                } else {
                    model.sendData(imageName, serverIP);
                    view.warningMessage.setText("Sent! Please select another file to send.");
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

}

class Main {

    public static void main(String[] args) {
        ClientController controller = new ClientController(new ClientModel());
        String ipAddress = controller.view.serverIP.getText();
        int portNumber = 8080;
        try{
            Socket clientSocket = new Socket(ipAddress, portNumber);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            while (true){
                    String serverResponse = in.readLine();
                    controller.model.receiveData(serverResponse);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        }
    }

