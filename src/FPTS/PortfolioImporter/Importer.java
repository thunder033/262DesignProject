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

    public void setStrategy(ImportStrategy strategy) {
        parsingStrategy = strategy;
    }

    public ImportResult importData() {
        return parsingStrategy.execute(_path);
    }
}
