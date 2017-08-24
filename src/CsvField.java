/**
 * Holds a field in a CsvRecord
 * @author tcotti & jadamsc
 * @since 07/11/2014.
 */
public class CsvField {
    String name, value;

    public CsvField(String name, String val) {
        this.name = name;
        this.value = val;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String toString(){
        return("{" + name + ", " + value + "}");
    }
}
