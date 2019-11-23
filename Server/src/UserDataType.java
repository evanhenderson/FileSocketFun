public class UserDataType {
    private int id;
    private String username;
    private String password;

    public UserDataType(){
        id = -1;
        username = "null";
        password = "null";
    }
    public UserDataType(String username, String password){
        this();
        this.username = username;
        this.password = password;
    }
    public UserDataType(int id, String username, String password){
        this.id = id;
        this.username = username;
        this.password = password;
    }
    @Override
    public String toString() {
        return id + " " + username;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
