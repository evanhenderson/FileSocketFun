/**
 * Handles the sockets + connection for the client end. Currently, can send an image via port 12345 to a user
 * inputted IP address.
 *
 * @authors Nora El Naby & Evan Henderson
 * @version 1.5
 */

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientModel {
    ClientController controller;
    Socket socket;
    InputStream stdIn;
    FileInputStream fileIn;
    OutputStream out;
    File clearingCache;
    public ClientModel(){

    }

    public void beginConnection(String hostIP) {
        try {
            socket = new Socket(hostIP, 12345);
            stdIn = socket.getInputStream();
            out = socket.getOutputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendData(String imageName, String hostIP) throws IOException {
        beginConnection(hostIP);
        String filePath = System.getProperty("user.dir")+ System.getProperty("file.separator") + "Client"
                + System.getProperty("file.separator") +
                "Cache" + System.getProperty("file.separator") + imageName;

        fileIn = new FileInputStream(filePath);

        int confirmation = ensureConnection(out, stdIn);
        if(confirmation == 1) {
            System.out.println("THIS WORKED");
            sendImageName(imageName, out);
            System.out.println("Done sending file name");
            sendImage(fileIn, out);
        }
        clearingCache = new File(filePath);
        closeConnection(out, stdIn, fileIn);
    }

    public int ensureConnection(OutputStream out, InputStream stdIn) throws IOException {
        byte[] code = new byte[1];
        stdIn.read(code);
        return Integer.parseInt(String.valueOf(code[0]));
    }

    public void closeConnection(OutputStream out, InputStream stdIn, InputStream fileIn)
            throws IOException {
        out.write("closing".getBytes());
        clearingCache.deleteOnExit();
        out.close();
        stdIn.close();
        fileIn.close();
        socket.close();
    }

    public void sendImage(InputStream in, OutputStream out) throws IOException {
        System.out.println("Starting to send the image");
        out.write(2);
        byte[] buf = new byte[8192];
        int len = 0;
        while((len = in.read(buf)) != -1) {
            out.write(buf, 0, len);
        }
        System.out.println("Finished sending the image");
    }

    public void sendImageName(String imageName, OutputStream out) throws IOException {
        out.write(1);
        byte[] buf = new byte[1];
        int len = 0;
        OutputStream imageNameOut = new FileOutputStream(System.getProperty("user.dir") + controller.fileSeparator +
                "Client" + controller.fileSeparator + "Cache" + controller.fileSeparator + "imageName");
        imageNameOut.write(imageName.getBytes());
        imageNameOut.close();
        InputStream in = new FileInputStream(System.getProperty("user.dir") + controller.fileSeparator +
                "Client" + controller.fileSeparator + "Cache" + controller.fileSeparator + "imageName");
        while((len = in.read(buf)) != -1) {
            out.write(buf, 0, len);
        }
        out.write(-1);
        in.close();
    }

    public void receiveData(String serverMsg) {
    //server protocol codes: 'login', 'imageList', 'download'
        if (serverMsg == "login"){
            controller.setUser();
        }
        if (serverMsg == "imageList"){
            controller.imageListSet();
        }
        if (serverMsg == "download"){
            download();
        }
    }
    public void download(){

    }

    public void setController(ClientController controller) {
        this.controller = controller;
    }
}
