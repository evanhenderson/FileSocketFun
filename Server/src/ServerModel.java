import java.io.*;
import java.nio.*;
public class ServerModel {
    String[] imageList;
    String[][] userList;
    public void sendData(String Msg, BufferedReader in, PrintWriter out){
        out.println(Msg);
    }
    public void receiveData(InputStream in, OutputStream out) throws IOException {
        byte[] bytes = new byte[16*1024];
        int count;
        byte[] code = new byte[4];
        for(int i = 0; i < 4; i++){
            in.read(bytes);

            code[i] = bytes[i];
        }
        ByteBuffer wrapped = ByteBuffer.wrap(code);
        int num = wrapped.getInt();
        if(num == 2) {
            while ((count = in.read(bytes)) != -1) {
                out.write(bytes, 0, count);
            }
        }
    }
    public boolean authenticate(String username, String password){

    }
    public void storeImage(){

    }
    public void retrieve(){

    }
    public void stop(){
        client.stopConnection();
    }
    public String[] getImageList(){
        return imageList;
    }
    public String[][] getUserList() {
        return userList;
    }
    public void setImageList(){

    }
}
