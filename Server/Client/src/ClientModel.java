/**
 * Handles the sockets + connection for the client end. Currently, can send an image via port 12345 to a user
 * inputted IP address.
 *
 * @authors Nora El Naby & Evan Henderson
 * @version 1.7.1
 */

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientModel {

    /**
     * This is the controller the model uses
     */
    private ClientController controller;

    /**
     * This is the socket used to communicate to the server
     */
    private Socket socket;

    /**
     * This is the input received from the server
     */
    protected InputStream stdIn;

    /**
     * This is the input read from any files being sent
     */
    protected FileInputStream fileIn;

    /**
     * This is the output written to files that have been sent
     */
    protected FileWriter fileOut;

    /**
     * This is the output sent to the server
     */
    protected OutputStream out;

    /**
     * This is the cached version of the file that is to be sent
     */
    protected File cachedVersion;

    /**
     * This is a list of available files on the server
     */
    protected ArrayList<String> availableFiles;


    /**
     * This opens communication with the server, given a hostIP
     * @param hostIP is the IP address of the server
     */
    public void beginConnection(String hostIP) {
        try {
            System.out.println("Attempting to connect");
            socket = new Socket(hostIP, 12345);
            stdIn = socket.getInputStream();
            out = socket.getOutputStream();
            System.out.println("Connected to socket");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This sends the image name, followed by confirmation the server has received the name, and then the image itself.
     * It will also begin the connection itself
     * @param imageName the name of the image being sent
     * @param hostIP the IP address of the server
     * @throws IOException is thrown if there is an error connecting to the server
     */
    public void sendData(String imageName, String hostIP) throws IOException {
        beginConnection(hostIP);
        System.out.println("Made it back to send data");
        String filePath = System.getProperty("user.dir")+ System.getProperty("file.separator") + "Client"
                + System.getProperty("file.separator") +
                "Cache" + System.getProperty("file.separator") + imageName;

        fileIn = new FileInputStream(filePath);

        int confirmation = ensureConnection();
        if(confirmation == 1) {
            System.out.println("Sending image name");
            sendImageName(imageName);
            System.out.println("Sending image");
            sendImage();
            System.out.println("Sent image");
        }
        cachedVersion = new File(filePath);
        fileIn.close();
        controller.promptToContinue();
    }

    /**
     * This reads a code from the server, and returns it, in order to ensure the connection
     * @return the code sent by the server
     * @throws IOException is thrown if it cannot connect to the server, or if stdIn has yet to be initialized
     */
    public int ensureConnection() throws IOException {
        byte[] code = new byte[1];
        stdIn.read(code);
        return Integer.parseInt(String.valueOf(code[0]));
    }

    /**
     * Closes the various input/output streams, deletes the cachedVersion of the file, and suspends the connection
     * @throws IOException if the server is already closed
     */
    public void closeConnection() throws IOException {
        System.out.println("Closing the connection");
        suspendConnection();
        System.out.println("Disconnected from server");
        if(cachedVersion != null) {
            cachedVersion.deleteOnExit();
        }
        out.close();
        stdIn.close();
        socket.close();
        System.out.println("Closed all streams");
    }

    /**
     * Sends the image in packages of 8192 bytes to the server
     * @throws IOException if stdIn or out are not initialized
     */
    public void sendImage() throws IOException {
        out.write(2);
        byte[] buf = new byte[8192];
        int len = 0;
        while((len = fileIn.read(buf)) != -1) {
            System.out.println("sending parcel");
            out.write(buf, 0, len);
        }
        System.out.println("sent");
    }

    /**
     * Sends the name of the image it is about to send to the server
     * @param imageName the name of the image being sent
     * @throws IOException if the file was not saved in the cache correctly, or if the output stream hasn't been
     * initialized yet.
     */
    public void sendImageName(String imageName) throws IOException {
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

    /**
     * Requests the names of all available files on the server, and begins the server connection to receive files
     * @param hostIP the IP address of the host
     */
    public void requestFileNames(String hostIP) {
        availableFiles = new ArrayList<>();
        try {
            beginConnection(hostIP);
            out.write(3);
            System.out.println("Receiving file names");
            receiveFileNames();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Requests a given file
     * @param fileName the name of the file being requested
     * @throws IOException
     */
    public void requestFile(String fileName) throws IOException {
        System.out.println("Requesting the file");
        out.write(4);
        out.write(fileName.getBytes());
        download();
        controller.promptToContinue();
    }

    /**
     * Downloads a given file from the server
     */
    public void download() {
        System.out.println("Downloading the file");
        String path = controller.chooseFileSaveLocation();
        if(path == "BIG ERROR") {
            controller.askTheUserWhy();
        } else {
            try {
                fileOut = new FileWriter(path);
                String next = stdIn.readAllBytes().toString();
                while(!next.equals(null)) {
                    fileOut.write(next);
                    next = stdIn.readAllBytes().toString();
                }
                fileOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Receives the list of file names from the server, and reads it into availableFiles
     * @throws IOException if stIn hasn't been initialized yet
     */
    public void receiveFileNames() throws IOException {
        System.out.println("reading file names");
        FileOutputStream availableFileNames = new FileOutputStream(System.getProperty("user.dir") + controller.fileSeparator +
                "Client" + controller.fileSeparator + "Cache" + controller.fileSeparator + "availableFiles.txt");
        int buf = 1;
         byte[] next = stdIn.readNBytes(buf);
         next = stdIn.readNBytes(buf);
         while(!"*".equals(new String (next))) {
             if (next != "%".getBytes()) {
                 availableFileNames.write(next);
             }
             next = stdIn.readNBytes(buf);
         }
        System.out.println("Starting to read into available Files");
        availableFileNames.close();
        Scanner availableIn = new Scanner(new File(System.getProperty("user.dir") + controller.fileSeparator +
                "Client" + controller.fileSeparator + "Cache" + controller.fileSeparator + "availableFiles.txt"));
        while(availableIn.hasNext()) {
            String available = availableIn.nextLine();
            System.out.println(available);
            String fileName = "";
            for(int i = 0; i < available.length(); i++) {
                if(available.charAt(i) == '%') {
                    availableFiles.add(fileName);
                    fileName = "";
                } else {
                    fileName += available.charAt(i);
                }
            }
        }
        System.out.println(availableFiles);
        System.out.println("Done");
    }

    /**
     * sets the controller
     * @param controller is the new controller
     */
    public void setController(ClientController controller) {
        this.controller = controller;
    }

    /**
     * Tells the server that it is done sending/receiving files.
     * @throws IOException if out has not yet been initialized
     */
    public void suspendConnection() throws IOException {
        out.write(-1);
    }

    /**
     * Tells the server that the user is not done sending/receiving images yet.
     * @throws IOException throws the exception if out hasn't been initialized yet.
     */
    public void resetConnection() throws IOException {
        out.write(5);
    }

}
