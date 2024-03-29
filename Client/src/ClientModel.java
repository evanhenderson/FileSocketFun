/**
 * Handles the sockets + connection for the client end. Currently, can send an image via port 12345 to a user
 * inputted IP address.
 *
 * @authors Nora El Naby & Evan Henderson
 * @version 1.7.1
 */

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
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
    protected FileOutputStream fileOut;

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
            if(socket == null) {
                socket = new Socket(hostIP, 12345);
                stdIn = socket.getInputStream();
                out = socket.getOutputStream();
            }
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
        String filePath = System.getProperty("user.dir")+ System.getProperty("file.separator") + "Client"
                + System.getProperty("file.separator") +
                "Cache" + System.getProperty("file.separator") + imageName;

        fileIn = new FileInputStream(filePath);

        int confirmation = ensureConnection();
        if(confirmation == 1) {
            sendImageName(imageName);
            sendImage();
        }
        controller.fileSentMessage();
        fileIn.close();
        out.write(-1);
        closeConnection();
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
        suspendConnection();
        if(cachedVersion != null) {
            cachedVersion.deleteOnExit();
        }
        out.close();
        stdIn.close();
        socket.close();
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
            out.write(buf, 0, len);
        }
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
        out.write(4);
        out.write(fileName.getBytes());
        out.write(-1);
        download();
    }

    /**
     * Downloads a given file from the server
     */
    public void download() {
        String path = controller.chooseFileSaveLocation();
        if(path == "BIG ERROR") {
            controller.invalidSaveLocationMessage();
        } else {
            try {
                socket.setSoTimeout(500);
                fileOut = new FileOutputStream(path);
                byte[] buf = new byte[8192];
                int next;
                while((next = stdIn.read(buf)) != -1) {
                    fileOut.write(buf, 0 , next);
                }
                fileOut.close();
            } catch (SocketTimeoutException e) {
                controller.fileReceivedMessage();
                controller.endProgram();
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
        availableFileNames.close();
        Scanner availableIn = new Scanner(new File(System.getProperty("user.dir") + controller.fileSeparator +
                "Client" + controller.fileSeparator + "Cache" + controller.fileSeparator + "availableFiles.txt"));
        while(availableIn.hasNext()) {
            String available = availableIn.nextLine();
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
    }

    /**
     * sets the controller
     * @param controller is the new controller
     */
    public void setController(ClientController controller) {
        this.controller = controller;
    }

    /**
     * Tells the server to suspend the connection by writing it a -1
     * @throws IOException if the output stream to the socket has not yet been initialized
     * (ie. if this is called before beginConnection).
     */
    public void suspendConnection() throws IOException {
        out.write("-1".getBytes());
    }


}
