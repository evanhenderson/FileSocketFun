import java.net.*;
import java.io.*;
public class ServerController {
    ServerModel model;
    ServerSocket serverSocket = new ServerSocket(8080);
    Socket clientSocket = serverSocket.accept();
    ServerController(ServerModel model){
        this.model = model;

    }
    public void start(){
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null){
            model.receiveData(inputLine, in, out);
        }
        in.close();
        out.close();
        clientSocket.close();
    }
    public static void main(String[] args) {
       ServerController controller =  new ServerController(new ServerModel);
       controller.start();

    }
}
