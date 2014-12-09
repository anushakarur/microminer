import java.util.ArrayList;
import java.util.Scanner;


public class Search 
{
	OutputSearch output = new OutputSearch();
	
	public static ArrayList<OrderedLine> theList = new ArrayList<OrderedLine>();
	
	public void upstream(OrderedLine line)
	{
		theList.add(line);
	}
	
	public void upstreamTwo(String searchTerms)
	{
		startSearch(searchTerms);
	}
	
	public void startSearch(String searchTerms)
	{
		ArrayList<String> matchedURLs = new ArrayList<String>();
		String url;
		String term;
		String line;
		String word;
		OrderedLine thisLine = new OrderedLine();
		Scanner scan2;
		if(theList.size() == 0)
		{
			thisLine.descriptor = "No data";
			thisLine.URLPart = "to search.";
			downstream(thisLine);
		}
			
		for(int i = 0; i < theList.size(); i++)
		{
			thisLine = theList.get(i);
			line = thisLine.descriptor;
			scan2 = new Scanner(line);
			word = scan2.next();
			//System.out.println("word: "+ word);
			Scanner scan = new Scanner(searchTerms);
			while(scan.hasNext())
			{
				term = scan.next();
				//System.out.println("term: " + term);
				if(word.equals(term))
				{
					//System.out.println("found match");
					url = theList.get(i).URLPart;
					if(!matchedURLs.contains(url))
					{
						matchedURLs.add(url);
						//System.out.println("match");
						downstream(thisLine);
					}
				}
			}
		}
		matchedURLs.clear();
	}
	
	public void downstream(OrderedLine line)
	{
		//System.out.println(line);
		//System.out.println("OUT: Alphabetizer class");

		output.upstream(line);
	}

}
