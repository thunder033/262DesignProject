package FPTS.PortfolioImporter;

import FPTS.Models.Portfolio;

import java.nio.file.Path;

/**
 * Created by Greg on 3/13/2016.
 * A method for parsing raw data into a portfolio instance
 */
public interface ImportStrategy {

    Portfolio execute(Path path);
}
