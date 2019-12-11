import java.awt.desktop.OpenURIEvent;
import java.net.*;
import java.io.*;
public class ServerController {
    ServerModel model;
    ServerSocket serverSocket = new ServerSocket(12345);
    Socket clientSocket;

    ServerController(ServerModel model) throws IOException {
        this.model = model;
        clientSocket = serverSocket.accept();

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
        OutputStream out3 = clientSocket.getOutputStream();
        System.out.println("goes into receive data");
        model.receiveData(in, fileNameOut, out3);
        System.out.println("leaves receive data");
        clientSocket.close();
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
