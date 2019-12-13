/**
 * @author Evan Henderson
 */

import java.net.*;
import java.io.*;
public class ServerController {
    /**
     * @field has a model object
     */
    ServerModel model;
    /**
     * @field this is the server socket
     */
    ServerSocket serverSocket = new ServerSocket(12345);
    /**
     * @field socket for the client
     */
    Socket clientSocket;

    /**
     * This is the constructor for the controller
     * @param model
     * @throws IOException
     */
    ServerController(ServerModel model) throws IOException {
        this.model = model;
        clientSocket = serverSocket.accept();

    }

    /**
     * starts server by initializing out/in streams and calling recieveData from model,
     * then closing the clientSocket at the end
     * @throws IOException
     */
    public void start() throws IOException {
        OutputStream out = null;
        InputStream in = null;

        try {
            in = clientSocket.getInputStream();
        }catch(IOException exception){
            exception.printStackTrace();
        }
        OutputStream out2 = clientSocket.getOutputStream();
        out2.write(Integer.parseInt("1"));
        OutputStream fileNameOut = new FileOutputStream(System.getProperty("user.dir") + System.getProperty("file.separator") +
                 "files" + System.getProperty("file.separator")
                + "fileName.txt");
        OutputStream out3 = clientSocket.getOutputStream();
        System.out.println("goes into receive data");
        model.receiveData(in, fileNameOut, out3);
        System.out.println("leaves receive data");
        clientSocket.close();
    }

    /**
     * The main that contains the Thread for the client
     * Source for Thread code: https://stackoverflow.com/questions/6099636/sending-files-through-sockets
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
       new Thread(){
           public void run(){
               try{
                   ServerController controller =  new ServerController(new ServerModel());
                   controller.start();
               }catch(IOException e){
                   e.printStackTrace();
               }
           }
       }.start();


    }
}
