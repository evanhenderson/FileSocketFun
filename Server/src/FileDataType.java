/**
 * @author Evan Henderson
 */
public class FileDataType {
    /**
     * @field id for database entry
     */
    private int id;
    /**
     * @field title to be stored in database
     */
    private String title;

    /**
     * Default Value Constructor
     */
    public FileDataType(){
        id = -1;
        title = "null";

    }

    /**
     * Constructor with title defined
     * @param title
     */
    public FileDataType(String title){
        this();
        this.title = title;

    }

    /**
     * Constructor with both id and title defined
     * @param id
     * @param title
     */
    public FileDataType(int id, String title){
        this.id = id;
        this.title = title;

    }

    /**
     * Overrides toString()
     * @return String representation of object
     */
    @Override
    public String toString() {
        return id + " " + title;
    }

    /**
     * title getter
     * @return returns title as a String
     */
    public String getTitle() {
        return title;
    }

}
