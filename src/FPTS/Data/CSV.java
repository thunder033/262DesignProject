package FPTS.Data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collector;

/**
 * Created by Greg on 3/9/2016.
 */
public class CSV {

    private Path _filePath;

    public CSV(Path filePath)
    {
        _filePath = filePath;
    }

    /**
     * Parses a single CSV line into an array of values
     * @param csvLine the raw csv line
     * @return array of values in the csv line
     */
    public static String[] getValues(String csvLine) {
        //remove the first and last quote from the string, and then split by ","
        return csvLine.substring(1, csvLine.length() - 1).split("\",\"");
    }

    /**
     * Reads in the CSV file and converts into a multi-dimensional array of values
     * @return array of values contains in the CSV
     * @throws IOException
     */
    public String[][] Read() throws IOException {
        //read all lines from the file into a list and convert to stream
        return Files.readAllLines(_filePath).stream()
                //parse each CSV line into an array of string values
                .map(CSV::getValues)
                //convert the stream to multidimensional array
                .toArray(String[][]::new);
    }

    /**
     * Converts the data array into a CSV string and writes it
     * @param _data the array of values to write
     */
    public void Write(String[][] _data) {

    }
}
