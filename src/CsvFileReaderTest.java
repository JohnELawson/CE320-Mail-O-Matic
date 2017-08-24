import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class CsvFileReaderTest {

    public CsvFileReader cfr;
    public static ArrayList<CsvRecord> testList;

    @org.junit.Before
    public void setUp() throws Exception {
        File testfile = new File("testfile.csv");
        cfr = new CsvFileReader();
        testList = cfr.generateList(testfile);
    }

    @org.junit.Test
    public void testReadFile() throws Exception {
        File testfile = new File("testfile.csv");
        //String text = "\"one\",\"two\",\"three\",\"four\"";
        //BufferedWriter out = new BufferedWriter(new FileWriter(testfile));
        //out.write(text);
        //out.close();
        ArrayList<CsvRecord> testList = cfr.generateList(testfile);
        assertTrue(testList.toString().equals("[{\"Firstnumber\", \"one\"}, {\"SecondNumber\", \"two\"}, {\"Thirdnumber\", \"three\"}, {\"Forthnumber\", \"four\"}, {\"Firstnumber\", \"five\"}, {\"SecondNumber\", \"six\"}, {\"Thirdnumber\", \"seven\"}, {\"Forthnumber\", \"eight\"}]"));
    }

    @org.junit.Test
    public void testTitles() throws Exception {
        File testfile = new File("testfile.csv");
        ArrayList<String> theTitles = cfr.getTitles();
        assertTrue(theTitles.toString().equals("[\"Firstnumber\", \"SecondNumber\", \"Thirdnumber\", \"Forthnumber\"]"));
    }

//    @org.junit.Test
//    public void testFieldName() throws Exception {
//       CsvRecord testLine = testList.get(1);
//        CsvField testField = testLine.getList().get(0);
//        assertTrue(testField.getTitle().toString().equals("\"Firstnumber\""));
//    }

    @org.junit.Test
    public void testValidateNullFile() throws Exception {
        File nullfile = null;
        assertTrue(cfr.validateFile(nullfile) == false);
    }
}