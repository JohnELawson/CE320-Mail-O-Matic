import java.util.ArrayList;

/**
 * A record holds a number of CsvFields
 * @author tcotti & jadamsc
 * @since 07/11/2014.
 */
public class CsvRecord {

    private ArrayList<CsvField> fields;

    public CsvRecord() {
        fields = new ArrayList<CsvField>();
    }

    public void addField(CsvField field) {
        fields.add(field);
    }

    public ArrayList<CsvField> getList() {
        return fields;
    }

    public String toString() {
        StringBuffer st = new StringBuffer();
        for (CsvField f : fields) {
            st.append(f + ", ");
        }
        st.setLength(st.length() - 2);
        return st.toString();
    }
}
