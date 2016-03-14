package FPTS.Search;

//
//@author: Eric
//Created: 3/12/16
//Revised: 3/14/16
//Description: Search method to determine if
//an equity contains a value that begins with
//the search parameter.
//
public class BeginsWith implements SearchMethod{

        @Override
	public boolean compare(String searchTerm, String cmp){
                if (searchTerm.length() > cmp.length()){
                    return false;
                }
		return searchTerm.equals(cmp.substring(0, searchTerm.length()));

	}

}