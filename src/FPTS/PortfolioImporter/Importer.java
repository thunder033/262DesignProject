package FPTS.PortfolioImporter;

import FPTS.Models.Portfolio;

import java.nio.file.Path;

/**
 * Created by Greg on 3/13/2016.
 */
public class Importer {

    Path path;
    ImportStrategy parsingStrategy;

    public Importer(Path path) {
        parsingStrategy = new CSVImporter();
    }

    public void setStrategy(ImportStrategy strategy) {
        parsingStrategy = strategy;
    }

    public Portfolio importData() {
        return parsingStrategy.execute(path);
    }
}
