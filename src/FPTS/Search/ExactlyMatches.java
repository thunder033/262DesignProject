package FPTS.Search;

public class ExactlyMatches implements SearchMethod{

        @Override
	public boolean compare(String searchTerm, String cmp){

		return cmp.equals(searchTerm);

	}/*
	public ArrayList doMethod(string searchTerm, ArrayList csvTable){
		ArrayList results;
		for(i in csvTable){
			if(i == searchTerm){
				results.add(i);
			}
		}
		return results;
	}*/
}
