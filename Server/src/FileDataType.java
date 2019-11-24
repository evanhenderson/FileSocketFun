public class FileDataType {
    private int id;
    private String title;


    public FileDataType(){
        id = -1;
        title = "null";

    }
    public FileDataType(String title){
        this();
        this.title = title;

    }
    public FileDataType(int id, String title, String path){
        this.id = id;
        this.title = title;

    }
    @Override
    public String toString() {
        return id + " " + title;
    }

    public String getTitle() {
        return title;
    }

}
