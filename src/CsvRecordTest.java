import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class CsvRecordTest {

    @Test
    public void testAddField() throws Exception {
        CsvRecord record = new CsvRecord();
        record.addField(new CsvField("name","Charis"));
        ArrayList<CsvField> list;
        list = record.getList();
        assertTrue(list.get(0).getName().equals("name"));
        assertTrue(list.get(0).getValue().equals("Charis"));
    }

    @Test
    public void testGetList() throws Exception {
        CsvRecord record = new CsvRecord();
        CsvField field = new CsvField("name","Charis");
        record.addField(field);

        ArrayList<CsvField> list= new ArrayList<CsvField>();
        list.add(field);

        ArrayList<CsvField> list2 = new ArrayList<CsvField>();
        list2 = record.getList();

        assertTrue(list.containsAll(list2));
    }

    @Test
    public void testToString() throws Exception {
        CsvRecord record = new CsvRecord();
        CsvField field = new CsvField("name", "Charis");
        record.addField(field);
        assertTrue(record.toString().equals(field.toString()));
    }
}