import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientController {

    protected ClientModel model;
    protected ClientDemoView view;

    public ClientController(ClientModel model) {
        this.model = model;
        this.view = new ClientDemoView(this);
    }
    public void setUser() {

    }
    public void imageListSet() {

    }
    public static void main(String[] args) {
        ClientController controller = new ClientController(new ClientModel());
        String ipAddress = controller.view.serverIP.getText();
        int portNumber = 12345;
        try{
            Socket clientSocket = new Socket(ipAddress, portNumber);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            while (true){
                // read from stdIn and send whatever the user types to the server
                if (stdIn.ready()) { // has data to  read (kb.hasNextLine())
                    String userInput = stdIn.readLine();
                    out.println(userInput);
                }
                if (in.ready()){
                    // the server has sent something to us (client)
                    String serverResponse = in.readLine();
                    controller.model.receiveData(serverResponse);
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        }
    }

