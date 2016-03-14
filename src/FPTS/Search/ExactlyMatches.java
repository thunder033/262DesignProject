package FPTS.Search;

/**
 * @author: Eric
 * Created: 3/12/16
 * Revised: 3/14/16
 * Description: Search Method to check
 * if an equity has a parameter that exactly 
 * matches a searched parameter.
 */
public class ExactlyMatches implements SearchMethod{

        @Override
	public boolean compare(String searchTerm, String cmp){

		return cmp.equals(searchTerm);

	}
}
