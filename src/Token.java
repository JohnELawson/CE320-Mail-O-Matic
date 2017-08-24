/**
 * Created by jadamsc,tcotti and rframp on 18/11/2014.
 */
public class Token {
    private int index;
    private String title;

    public Token(int index, String title) {
        this.index = index;
        this.title = title;
    }

    public int getIndex() {
        return index;
    }

    public String getTitle() {
        return title;
    }

    public String toString() { return "["+title+", "+index+"]"; }
}
