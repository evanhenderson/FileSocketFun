import java.io.*;
import java.nio.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServerModel {
    //databases need a name
    static final String DATABASE_NAME = "serverdatabase.db";
    //need a connection url (like a command to open a file
    static final String CONNECTION_URL = "jdbc:sqlite:database\\" + DATABASE_NAME;
    static final String TABLE_USERS = "tableUsers";
    static final String ID = "id";
    static final String USERNAME = "username";
    static final String PASSWORD = "password";
    static final String TABLE_FILES = "tableFiles";
    static final String TITLE = "title";
    static final String PATH = "path";

    Connection connection;
    public ServerModel() {
        getConnection();
        createUsersTable();
        createFilesTable();

    }
    public void getConnection(){
        // have a field for a Connection reference
        try{
            connection = DriverManager.getConnection(CONNECTION_URL);
            System.out.println("Successfully connected to the database");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void createUsersTable(){
        //to interact with a SQLite database
        //we construct SQL statements
        //these are strings that we try to get SQLite to execute
        // CREATE TABLE tableContacts(id INTEGER PRIMARY KEY AUTOINCREMENT,
        // name TEXT,
        // phoneNumber TEXT,
        // imagePath TEXT)
        String sqlCreate = "CREATE TABLE " + TABLE_USERS + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + USERNAME +
                " TEXT," + PASSWORD + " TEXT)";
        System.out.println(sqlCreate);
        //primary key uniquely identifies records
        // autoincrement means let sqlite assign unique ids
        // start at 1 and go up by 1
        if (connection != null){
            try{
                Statement statement = connection.createStatement();
                statement.execute(sqlCreate);
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }
    public void createFilesTable(){
        //to interact with a SQLite database
        //we construct SQL statements
        //these are strings that we try to get SQLite to execute
        // CREATE TABLE tableContacts(id INTEGER PRIMARY KEY AUTOINCREMENT,
        // name TEXT,
        // phoneNumber TEXT,
        // imagePath TEXT)
        String sqlCreate = "CREATE TABLE " + TABLE_FILES + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TITLE +
                " TEXT," + PATH + ")";
        System.out.println(sqlCreate);
        //primary key uniquely identifies records
        // autoincrement means let sqlite assign unique ids
        // start at 1 and go up by 1
        if (connection != null){
            try{
                Statement statement = connection.createStatement();
                statement.execute(sqlCreate);
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }
    public void insertFile(FileDataType file){

        String sqlInsert = "INSERT INTO " + TABLE_FILES + " VALUES(null, '" +
                file.getTitle() + "', '" +
                file.getPath() + "')";
        System.out.println(sqlInsert);
        if (connection != null){
            try{
                Statement statement = connection.createStatement();
                statement.execute(sqlInsert);
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }
    public void insertUser(UserDataType user){

        String sqlInsert = "INSERT INTO " + TABLE_USERS + " VALUES(null, '" +
                user.getUsername() + "', '" +
                user.getPassword() + "')";
        System.out.println(sqlInsert);
        if (connection != null){
            try{
                Statement statement = connection.createStatement();
                statement.execute(sqlInsert);
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }
    public List<FileDataType> getAllFilesList(){
        List<FileDataType> filesList = new ArrayList<>();
        //iterate through each record in our table
        //extract the column values
        //create a contract
        //add the contact to the list
        //much like I/O while(inFile.nextLine()) {}
        //while (inFile.eof() {}
        //SELECT * FROM tableContacts
        String sqlSelect = "SELECT * FROM " + TABLE_FILES;
        System.out.println(sqlSelect);
        if (connection != null){
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlSelect);
                //need to advance to the first record (if there is one)
                while(resultSet.next()) { //returns false when there are no more records
                    int id = resultSet.getInt(ID);
                    String title = resultSet.getString(TITLE);
                    String path = resultSet.getString(PATH);
                    FileDataType fileDataType = new FileDataType(id, title, path);
                    filesList.add(fileDataType);
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return filesList;
    }
    public List<UserDataType> getAllUsersList(){
        List<UserDataType> usersList = new ArrayList<>();
        //iterate through each record in our table
        //extract the column values
        //create a contract
        //add the contact to the list
        //much like I/O while(inFile.nextLine()) {}
        //while (inFile.eof() {}
        //SELECT * FROM tableContacts
        String sqlSelect = "SELECT * FROM " + TABLE_USERS;
        System.out.println(sqlSelect);
        if (connection != null){
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlSelect);
                //need to advance to the first record (if there is one)
                while(resultSet.next()) { //returns false when there are no more records
                    int id = resultSet.getInt(ID);
                    String username = resultSet.getString(USERNAME);
                    String password = resultSet.getString(PASSWORD);
                    UserDataType usersDataType = new UserDataType(id, username, password);
                    usersList.add(usersDataType);
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return usersList;
    }
    public void closeConnection(){
        //close the connection (just like a file we've opened)
        if (connection != null){
            try{
                connection.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }



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
        return false;
    }
    public void storeImage(){

    }
    public void retrieve(){

    }
    public void stop(){
        //client.stopConnection();
    }



}
