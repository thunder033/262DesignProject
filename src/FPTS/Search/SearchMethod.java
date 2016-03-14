package FPTS.Search;

/**
 * @author: Eric
 * Created: 3/12/16
 * Revised: 3/14/16
 * Description: Abstract search method
 */
public interface SearchMethod {
   public boolean compare(String searchTerm, String cmp);
}