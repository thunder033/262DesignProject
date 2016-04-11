package FPTS.PortfolioImporter;

import java.nio.file.Path;

/**
 * @author: Greg
 * Created: 3/13/16
 * Revised: 3/13/16
 * Description: Provides context for
 * importing, setting a different import
 * strategy based on input parameters.
 */
public class Importer {

    private Path _path;
    private ImportStrategy parsingStrategy;

    public Importer(Path path) {
        parsingStrategy = new CSVImporter();
        _path = path;
    }

    /**
     * Sets the parsing strategy the Importer will use
     * @param strategy a strategy based on the file format
     */
    public void setStrategy(ImportStrategy strategy) {
        parsingStrategy = strategy;
    }

    /**
     * Imports the file at Importer path and attempts to parse the raw data into models
     * @return
     */
    public ImportResult importData() {
        return parsingStrategy.execute(_path);
    }
}
