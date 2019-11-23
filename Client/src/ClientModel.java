import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;

public class ClientModel {
    ClientController controller;
    Socket socket;
    InputStream stdIn;
    FileInputStream fileIn;
    OutputStream out;
    public ClientModel(){
        try {
            socket = new Socket("147.222.236.132", 12345);
            stdIn = socket.getInputStream();
            out = socket.getOutputStream();
            out.write(Byte.parseByte("2"));
            System.out.println("Successfully connected to the server");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sendData(String imageName, String hostIP) throws IOException {
        System.out.println("Made it to sendData");
        String filePath = System.getProperty("user.dir")+ System.getProperty("file.separator") + "Client"
                + System.getProperty("file.separator") +
                "Cache" + System.getProperty("file.separator") + imageName;
        System.out.println(filePath);
        fileIn = new FileInputStream(filePath);
        byte[] code = new byte[1];
        System.out.println("Before Reading code");
        stdIn.read(code);
        System.out.println("Just read code");
        int num = Integer.parseInt(String.valueOf(code[0]));
        System.out.println(num);
        if(num == 1) {
            System.out.println("THIS WORKED");
            copy(fileIn, out);
        }
            out.write("closing".getBytes());
            out.close();
            stdIn.close();
            fileIn.close();
            socket.close();
    }

    public void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[8192];
        int len = 0;
        while((len = in.read(buf)) != -1) {
            out.write(buf, 0, len);
        }
        out.write(3);
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
