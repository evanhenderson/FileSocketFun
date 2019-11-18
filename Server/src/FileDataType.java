public class FileDataType {
    private int id;
    private String title;
    private String path;

    public FileDataType(){
        id = -1;
        title = "null";
        path = "null";
    }
    public FileDataType(String title, String path){
        this();
        this.title = title;
        this.path = path;
    }
    public FileDataType(int id, String title, String path){
        this.id = id;
        this.title = title;
        this.path = path;
    }
    @Override
    public String toString() {
        return id + " " + title;
    }

    public String getTitle() {
        return title;
    }

    public String getPath() {
        return path;
    }
}
