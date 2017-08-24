import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * CsvFileReader
 * A class that can generate a list of CSV Records from a given file
 * @author tcotti & jadamsc
 * @since 07/11/2014.
 */
 public class CsvFileReader {

    private File csvFile;
    private ArrayList<CsvRecord> csvList;
    private ArrayList<String> titles;

    /**
     * loads a file into the CsvFileReader object
     * @param file The file to load
     */
    public void readInFile(File file) {
        csvFile = file;
    }

    /**
     * Generate an array of records (containing feilds) from the provided csvFile
     * @param file The file from which to generate
     * @return An array of CsvRecords
     */
    public ArrayList<CsvRecord> generateList(File file) {
        readInFile(file);
        ArrayList<CsvRecord> returnList = new ArrayList<CsvRecord>();
        String line;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(csvFile));

            line = reader.readLine();
            // Get titles
            titles = new ArrayList<String>();
            for (String nextTitle: line.split(",")) {
                titles.add(nextTitle);
            }
            line = reader.readLine();
            // Get records
            boolean alreadyWarned = false;
            while ((line != null)) {
                CsvRecord newRecord = new CsvRecord();

                int i = 0;
                for (String nextLine: line.split(",")) {
                    try {
                        CsvField newField = new CsvField(titles.get(i), nextLine);
                        newRecord.addField(newField);
                        i++;
                    }catch (Exception e){
                        if (!alreadyWarned) {
                            JOptionPane.showMessageDialog(null, "Error reading file - maybe it's not a .csv ?");
                            alreadyWarned = true;
                            return null;
                        }
                    }

                }
                returnList.add(newRecord);
                line = reader.readLine();
            }
            return returnList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return The list of records
     * Call generateList() first
     */
    public ArrayList<CsvRecord> getList() {
        return csvList;
    }

    /**
     * @return A list of CsvField titles
     * Call generateList() first
     */
    public ArrayList<String> getTitles() {
        return titles;
    }

    /**
     * Checks the file to ensure it exists and can be parsed properly
     * @param file The file to check
     * @return Whether or not the file is valid for use in the file reader
     * @throws Exception Any problems with the file
     */
    public boolean validateFile(File file)throws Exception{
        try {
            if (!file.isFile())
                return false;
        } catch(Exception e){
            return false;
        }
        return true;
    }
}

