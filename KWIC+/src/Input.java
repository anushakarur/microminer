import java.util.*;
import java.awt.Color;
import java.net.URL;

public class Input{

	KWIC theApplet = null;
	NoiseWords nw = new  NoiseWords();
	
	/* This method represents the upstream socket for this filter */
	public void upstream(String str){

		
		//System.out.println("IN: Input class");

		getvalidline(str);
		
	}
	
	/* Checking for invalid characters from a line
	/** THIS METHOD NEEDS TO BE COMPLETED**/
	private void getvalidline(String line){
	
		OrderedLine newLine = null;
		String outputline="";
		URL thisURL = null;
		String word;
		boolean valid;
		boolean isURL = false;

	

		if(line != null && line.length() != 0)

		{

			Scanner scanword=new Scanner(line);
			
			while(scanword.hasNext()){

				valid = true;
				isURL = false;
				word = scanword.next();

				//System.out.println("Word======= " +word);
				
				if(word.startsWith("http://"))
				{
					try
					{
						thisURL = new URL(word);
						if(isValidURL(thisURL))
						{
							isURL = true;
						}
						else
						{
							theApplet = KWIC.selfRef;
							theApplet.setError(word + " is not a valid URL.");//, Color.red);
						}
					}
					catch(Exception ex)
					{
					}
				}

				for(int i=0;i<word.length();i++){ //check each character in the word is valid or not

					if(!Character.isLowerCase(word.charAt(i)) && !Character.isUpperCase(word.charAt(i)) && word.charAt(i)!='-'){
						valid=false;
					}
				}
				


				if(valid && !isURL){ //if the word is valid, add to the line
					if(isEmpty(outputline))
						outputline=word;
					else
						outputline=outputline+" "+word;
				}
				else if(!isURL)
				{
					theApplet = KWIC.selfRef;
					theApplet.setError(word + " is not valid input and has been removed.");
				}
			}
		
			if(!isEmpty(outputline) && thisURL != null && isURL){
	
				//System.out.println("To shift this string == " +outputline);
				String url;
				url = thisURL.toString();
				newLine = new OrderedLine(outputline, url);
				//System.out.println(newLine.descriptor);
				downstream(newLine);

			}
		}

		else{
			outputline = null;
			newLine = new OrderedLine(outputline, null);
				
			//System.out.println("To shift this string == " +outputline);
			downstream(newLine);
		}

	}

	public boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}
	
	public boolean isValidURL(URL url)
	{
		boolean valid = true;
		String str = url.toString();
		if(!str.endsWith(".edu") && !str.endsWith(".com") && !str.endsWith(".org") && !str.endsWith(".net"))
		{
			valid = false;
		}
		String identifier = str.substring(7, str.length()-4);
			for(int i=0;i<identifier.length();i++){ //check each character in the word is valid or not
	
				if(!Character.isLowerCase(identifier.charAt(i)) && !Character.isUpperCase(identifier.charAt(i)) && !Character.isDigit(identifier.charAt(i)) && identifier.charAt(i) != '.'){
					valid=false;
				}
			}
	
		return valid;
	}
		
	/* This method represents the downstream socket for this filter */
	public void downstream(OrderedLine output){

		
		//System.out.println(output);
		//System.out.println("OUT: Input class");

		nw.upstream(output);
	}
}
