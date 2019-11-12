import java.io.BufferedReader;
import java.io.PrintWriter;

public class ServerModel {
    String[] imageList;
    String[][] userList;
    public void sendData(String Msg, BufferedReader in, PrintWriter out){
        out.println(Msg);
    }
    public void receiveData(String clientMsg, BufferedReader in, PrintWriter out){
        if (clientMsg == "stop"){
            String Msg = "goodbye";
            sendData(Msg, in, out);
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
