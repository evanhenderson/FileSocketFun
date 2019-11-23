import java.awt.desktop.OpenURIEvent;
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

    }

    public void beginConnection(String hostIP) {
        try {
            socket = new Socket(hostIP, 12345);
            stdIn = socket.getInputStream();
            out = socket.getOutputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendData(String imageName, String hostIP) throws IOException {
        beginConnection(hostIP);


        String filePath = System.getProperty("user.dir")+ System.getProperty("file.separator") + "Client"
                + System.getProperty("file.separator") +
                "Cache" + System.getProperty("file.separator") + imageName;

        fileIn = new FileInputStream(filePath);

        int confirmation = ensureConnection(out, stdIn);
        if(confirmation == 1) {
            System.out.println("THIS WORKED");
            sendImageName(imageName, out);
            copy(fileIn, out);
        }
        File clearingCache = new File(filePath);
        closeConnection(out, stdIn, fileIn, clearingCache);
    }

    public int ensureConnection(OutputStream out, InputStream stdIn) throws IOException {
        out.write(Byte.parseByte("2"));
        byte[] code = new byte[1];
        stdIn.read(code);
        return Integer.parseInt(String.valueOf(code[0]));
    }

    public void closeConnection(OutputStream out, InputStream stdIn, InputStream fileIn, File clearingCache)
            throws IOException {
        out.write("closing".getBytes());
        out.close();
        stdIn.close();
        clearingCache.delete();
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

    public void sendImageName(String imageName, OutputStream out) throws IOException {
        out.write(Byte.parseByte(imageName));
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
