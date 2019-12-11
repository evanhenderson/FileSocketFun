import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ServerModel {
    //databases need a name
    static final String DATABASE_NAME = "serverdatabase.db";
    //need a connection url (like a command to open a file
    static final String CONNECTION_URL = "jdbc:sqlite:database\\"  + DATABASE_NAME;
    static final String TABLE_USERS = "tableUsers";
    static final String ID = "id";
    static final String USERNAME = "username";
    static final String PASSWORD = "password";
    static final String TABLE_FILES = "tableFiles";
    static final String TITLE = "title";

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

        //primary key uniquely identifies records
        // autoincrement means let sqlite assign unique ids
        // start at 1 and go up by 1
        if (connection != null){
            try{
                Statement statement = connection.createStatement();
                statement.execute(sqlCreate);
            }catch(SQLException e){

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
                " TEXT)";

        //primary key uniquely identifies records
        // autoincrement means let sqlite assign unique ids
        // start at 1 and go up by 1
        if (connection != null){
            try{
                Statement statement = connection.createStatement();
                statement.execute(sqlCreate);
            }catch(SQLException e){

            }
        }
    }
    public void insertFile(FileDataType file){

        String sqlInsert = "INSERT INTO " + TABLE_FILES + " VALUES(null, '" +
                file.getTitle() + "')";
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
                    FileDataType fileDataType = new FileDataType(id, title);
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



    public void sendFileName(String Msg, OutputStream out) throws IOException {
        out.write(Msg.getBytes());
    }
    public void receiveData(InputStream in, OutputStream fileNameOut, OutputStream out) throws IOException {
        byte[] bytes = new byte[8192];
        String fileName = null;
        OutputStream fileout = null;
        int count;
        byte[] code = new byte[1];
        int num = readCode(in, code);
        System.out.println("goes up to while loop");
        while(num != -1){
            System.out.println("goes to 1");
        if(num == 1){
            System.out.println("read file name");
            fileName = readFileName(in, fileNameOut);
        }

            System.out.println("goes to 2");
        if(num == 2) {

            String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") +
                    "files" + System.getProperty("file.separator")
                    + fileName;
            System.out.println(filePath);
            fileout = new FileOutputStream(filePath);
            System.out.println("calls readFile");
            readFile(in, fileout, bytes);
        }
        if(num == 3){
            System.out.println("goes into send file list");
            List<FileDataType> filesList = getAllFilesList();
            FileDataType file;
            String fileNameEndChar = "%";
            for(int i = 0; i < filesList.size(); i++){
                file = filesList.get(i);
                sendFileName(file.getTitle(), out);
                out.write(fileNameEndChar.getBytes());
            }
            String endChar = "*";
            out.write(endChar.getBytes());

        }
        if(num == 4){
            fileName = readSelection(in, fileNameOut);
            String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") +
                    "files" + System.getProperty("file.separator")
                    + fileName;
            System.out.println(filePath);
            InputStream fileIn = new FileInputStream(filePath);
            byte[] buf = new byte[8192];
            int len = 0;
            while((len = fileIn.read(buf)) != -1) {
                System.out.println("sending file");
                out.write(buf, 0, len);
            }
        }
        if(num == 5){
         //continue condition

        }
        if(num == -1){
           break;
        }
        num = readCode(in, code);
        }
        out.close();
        in.close();
    }
    public int readCode(InputStream in, byte[] code)throws IOException{
        in.read(code);
        int num = Integer.parseInt(String.valueOf(code[0]));
        System.out.println(num);
        return num;
    }
    public String readSelection(InputStream in, OutputStream fileNameOut)throws IOException {
        int count;
        String fileName = null;
        byte[] nameBytes = new byte[1];
        while ((count = in.read(nameBytes)) != -1){
            if(Integer.parseInt(String.valueOf(nameBytes[0])) == -1){
                break;
            }

            fileNameOut.write(nameBytes, 0, count);
        }
        fileNameOut.close();
        File readName = new File(System.getProperty("user.dir") + System.getProperty("file.separator") +
                "files" + System.getProperty("file.separator")
                + "fileName.txt");
        Scanner sc = new Scanner(readName);

        while(sc.hasNext()){
            fileName = sc.nextLine();

        }
        sc.close();
        return fileName;
    }

    public String readFileName(InputStream in, OutputStream fileNameOut)throws IOException{
        System.out.println("read file name");
        int count;
        String fileName = null;
        byte[] nameBytes = new byte[1];
        while ((count = in.read(nameBytes)) != -1){
            if(Integer.parseInt(String.valueOf(nameBytes[0])) == -1){
                break;
            }

            fileNameOut.write(nameBytes, 0, count);
        }
        fileNameOut.close();
        File readName = new File(System.getProperty("user.dir") + System.getProperty("file.separator") +
                 "files" + System.getProperty("file.separator")
                + "fileName.txt");
        Scanner sc = new Scanner(readName);

        while(sc.hasNext()){
            fileName = sc.nextLine();

        }
        sc.close();
        FileDataType newFile = new FileDataType(fileName);
        insertFile(newFile);
        File receivedFile = new File(System.getProperty("user.dir") + System.getProperty("file.separator") +
                    "files" + System.getProperty("file.separator")
                    + fileName);
        return fileName;
    }
    public void readFile(InputStream in, OutputStream out, byte[] bytes)throws IOException{
        int count;
        while ((count = in.read(bytes)) != -1) {
            out.write(bytes, 0, count);

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
