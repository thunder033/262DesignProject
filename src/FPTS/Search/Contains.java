package FPTS.Search;

public class Contains implements SearchMethod{

        @Override
	public boolean compare(String searchTerm, String cmp){

		return cmp.contains(searchTerm);

	}
/*
	public ArrayList doMethod(string searchTerm, ArrayList csvTable){
		ArrayList results;
		for(i in csvTable){
			if(i.contains(searchTerm)){
				results.add(i);
			}
		}
		return results;
	}*/
}
