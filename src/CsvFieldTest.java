import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class CsvFieldTest {

    @Test
    public void testGetName() throws Exception {
        CsvField field = new CsvField("name","Charis");
        assertTrue(field.getName().equals("name"));
    }

    @Test
    public void testGetValue() throws Exception {
        CsvField field = new CsvField("name","Charis");
        assertTrue(field.getValue().equals("Charis"));

    }

    @Test
    public void testToString() throws Exception {
        CsvField field = new CsvField("name","Charis");
        assertTrue(field.toString().equals("{name, Charis}"));

    }
}