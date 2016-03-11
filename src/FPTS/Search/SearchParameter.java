public class SearchParameter{
	private SearchMethod searchMethod;

	public Context(SearchMethod searchMethod){
		this.searchMethod = searchMethod;
	}

	public ArrayList<string> executeStrategy(ArrayList<marketEquity> marketEquities, string searchTerm) {
		ArrayList<string> results;
		for(i in marketEquities){			
			if(searchMethod.compare(searchTerm, i)){
				results.add(i);
			}
		}
		return results;
	}
}
