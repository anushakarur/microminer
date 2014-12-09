import java.util.*;

public class NoiseWords 
{
	KWIC theApplet = null;
	CircularShift shift = new  CircularShift();
	
	public void upstream(OrderedLine line)
	{
		if(line.descriptor == null)
		{
			downstream(line);
		}
		else
			removeNoiseWords(line);
	}
	
	public void removeNoiseWords(OrderedLine line)
	{
		boolean valid = true;
		
		String descriptor = line.descriptor;
		//System.out.println("nw: " + descriptor);
		
		ArrayList<String> noiseWords = new ArrayList<String>();
		
		noiseWords.add("a");
		noiseWords.add("an");
		noiseWords.add("the");
		noiseWords.add("and");
		noiseWords.add("or");
		noiseWords.add("of");
		noiseWords.add("to");
		noiseWords.add("be");
		noiseWords.add("is");
		noiseWords.add("in");
		noiseWords.add("out");
		noiseWords.add("by");
		noiseWords.add("as");
		noiseWords.add("at");
		noiseWords.add("off");
		noiseWords.add("on");
		noiseWords.add("for");
		noiseWords.add("up");
		noiseWords.add("am");
		noiseWords.add("if");
		
		String cleanedDesc = "";
		
		Scanner sc = new Scanner(descriptor);
		
		while(sc.hasNext())
		{
			valid = true;
			String word = sc.next();
		
			for(int j = 0; j < noiseWords.size(); j++)
			{
				if(word.equalsIgnoreCase(noiseWords.get(j)))
				{
					valid = false;
				}
			}
			
			if(valid)
			{
				if(cleanedDesc.equals(""))
					cleanedDesc = word;
				else
					cleanedDesc = cleanedDesc + " " + word;
			}
		}

		if(!cleanedDesc.equals(""))
		{
			line.descriptor = cleanedDesc;
			//System.out.println("cleaned: " + cleanedDesc);
			downstream(line);
		}
		else
		{
			theApplet = KWIC.selfRef;
			theApplet.setError("No description entered or description contains only noise words.");
		}
	}
	
	public void downstream(OrderedLine line)
	{
		//System.out.println("called nw downstream");
		shift.CircularShiftSetUp(line);
	}
}
