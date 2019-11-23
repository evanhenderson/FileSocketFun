import java.net.*;
import java.io.*;
public class ServerController {
    ServerModel model;
    ServerSocket serverSocket = new ServerSocket(12345);
    Socket clientSocket = serverSocket.accept();
    ServerController(ServerModel model) throws IOException {
        this.model = model;

    }
    public void start() throws IOException {
        OutputStream out = null;
        InputStream in = null;
        try {
            String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") +
                    System.getProperty("file.separator") + "files" + System.getProperty("file.separator")
                    + "file.jpg";
            System.out.println(filePath);
            out = new FileOutputStream(filePath);
        }catch(IOException exception){
            exception.printStackTrace();
        }
        try {
            in = clientSocket.getInputStream();
        }catch(IOException exception){
            exception.printStackTrace();
        }
        OutputStream out2 = clientSocket.getOutputStream();
        out2.write(Integer.parseInt("1"));
        model.receiveData(in, out);

        in.close();
        out.close();
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
