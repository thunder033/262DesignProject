package FPTS.Search;
/**
 * @author: Eric
 * Created: 3/12/16
 * Revised: 3/14/16
 * Description: A search method that checks if one
 * string contains another string.
 */
public class Contains implements SearchMethod{

        @Override
        //Comparison
	public boolean compare(String searchTerm, String cmp){
		return cmp.contains(searchTerm);
	}
}