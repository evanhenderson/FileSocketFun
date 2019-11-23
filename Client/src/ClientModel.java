import java.io.IOException;
import java.net.Socket;

public class ClientModel {
    ClientController controller;
    Socket socket;
    public ClientModel(){

    }
    public void sendData(String imageName, String hostIP) {
        try {
            socket = new Socket(hostIP, 12345);

        } catch (IOException e) {
            e.printStackTrace();
        }

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
