import java.net.*;
import java.io.*;
public class ServerController {
    ServerModel model;
    try{
        ServerSocket serverSocket = new ServerSocket(8080);
    }catch(IOException exception){
        exception.printStackTrace();
    }
    try{
        Socket clientSocket = serverSocket.accept();
    }catch(IOException exception){
        exception.printStackTrace();
    }
    ServerController(ServerModel model){
        this.model = model;

    }
    public void start(){
        try {
            OutputStream out = new FileOutputStream("C:\\Users\\evanh\\OneDrive\\" +
                    "Documents\\GitHub\\FileSocketFun\\" +
                    "Server\\files\\file.jpg");
        }catch(IOException exception){
            exception.printStackTrace();
        }
        try {
            InputStream in = clientSocket.getInputStream();
        }catch(IOException exception){
            exception.printStackTrace();
        }
        int count;
        model.receiveData(in, out);
        in.close();
        out.close();
        clientSocket.close();
    }
    public static void main(String[] args) throws IOException {
       new Thread(){
           public void run(){
               try{
                   ServerController controller =  new ServerController(new ServerModel);
                   controller.start();
               }catch(IOException e){
                   e.printStackTrace();
               }
           }
       }.start();


    }
}
