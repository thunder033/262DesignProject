public class ExactlyMatches implements SearchMethod{
	public boolean compare(string searchTerm, string cmp){
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
