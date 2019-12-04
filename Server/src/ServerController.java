import java.awt.desktop.OpenURIEvent;
import java.net.*;
import java.io.*;
public class ServerController {
    ServerModel model;
    ServerSocket serverSocket = new ServerSocket(12345);
    Socket clientSocket = serverSocket.accept();
    boolean closeClient = false;
    ServerController(ServerModel model) throws IOException {
        this.model = model;

    }
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
        model.receiveData(in, fileNameOut);
        if(closeClient) {
            clientSocket.close();
        }
    }
    public void closeClientSocket() throws IOException {
        closeClient = true;
    }
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
