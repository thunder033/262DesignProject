package FPTS.PortfolioImporter;

/**
 * Created by traub_000 on 4/2/2016.
 *
 * Description: Error thrown when an invalid choice is selected for a
 * duplicate holding
 */
public class InvalidChoiceException extends Throwable {
    public InvalidChoiceException(String message) {
        super(message);
    }
}
