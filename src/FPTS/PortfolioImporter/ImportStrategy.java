package FPTS.PortfolioImporter;

import FPTS.Models.Portfolio;

import java.nio.file.Path;

/**
 * @author: Greg
 * Created: 3/13/16
 * Revised: 3/13/16
 * Description: A method for parsing
 * raw data into a portfolio instance.
 */
public interface ImportStrategy {

    Portfolio execute(Path path);
}
