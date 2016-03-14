package FPTS.Search;

public class BeginsWith implements SearchMethod{

        @Override
	public boolean compare(String searchTerm, String cmp){

		return searchTerm.equals(cmp.substring(0, searchTerm.length()));

	}

}