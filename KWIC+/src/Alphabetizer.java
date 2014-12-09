import java.util.*;

public class Alphabetizer 
{
	/* this stores our list of lines while they are alphabetized */
	public static ArrayList<OrderedLine> alphaList = new ArrayList<OrderedLine>();

	Search thisSearch = new Search();
	OutputData output = new OutputData();
	
	/* this is our upstream facing socket for this filter */
	public boolean upstream(OrderedLine thisLine)
	{

		//System.out.println("IN: Alphabetizer class");
		//System.out.println("size: " +alphaList.size());
		
		String input = thisLine.descriptor;

		/* get the current size of our list */
		int size = alphaList.size();

		
		/* check to see if we received the 0 length string
		 * this string is the indicator that we have all the data */
		if(input == null || input.length() == 0)
		{
			thisSearch.theList.clear();
			/* if we received the 0 length string, then output the
			 * list, line by line, to the downstream socket */
			for(int j = 0; j < size; j++)
			{
				downstream(alphaList.get(j));
			}

			//once all the lines are send to output pipe, then all temporary data is erased.
			
			//System.out.println("--------END OF LINES-------");
			//alphaList.clear();
			
			return true;
		}
		
		String inList; //store line in list for alphabetic comparison
		
		/* result of alphabetic comparison
		 *  1: the 1st parameter comes before the 2nd parameter alphabetically
		 * -1: the 1st parameter comes after the 2nd parameter alphabetically
		 *  0: the 1st and 2nd parameters are identical 
		 */
		int compareResult = 0;
		
		//If this is the first line, just store it
		if(size == 0)
		{
			alphaList.add(thisLine);
		}
		
		/* Store each line in the appropriate spot in the list based
		 * on alphabetic order defined in the "compare" method
		 */
		for(int i = 0; i < size; i++)
		{

			
			// Get the next item in the list
			OrderedLine testLine = alphaList.get(i);
			inList = testLine.descriptor;
			// Compare our new item with this item
			compareResult = compare(input, inList);
			/* If our new item comes before the list item, insert it here */
			if(compareResult == 1 || compareResult == 0)
			{
				alphaList.add(i, thisLine);
				return true;
			}
			/* If we get to the end of the list without having stored our
			 * new line, then attach it at the end of the list.
			 */
			else if(i == size -1)
			{
				alphaList.add(thisLine);
				return true;
			}
		}
		return false;
	}
	
	/* This method represents the downstream socket for this filter */
	public void downstream(OrderedLine line)
	{
		//System.out.println(line);
		//System.out.println("OUT: Alphabetizer class");

		thisSearch.upstream(line);
		output.upstream(line);
	}
	
	/* This method defines our alphabetical order. It is passed 2 strings and
	 * determines the alphabetical order of the two strings
	 */
	public int compare(String s1, String s2)
	{
		char[] alphabeticOrder =	{' ','-','a','A','b','B','c','C','d','D','e','E',
									'f','F','g','G','h','H','i','I','j','J','k','K',
									'l','L','m','M','n','N','o','O','p','P','q','Q',
									'r','R','s','S','t','T','u','U','v','V','w','W',
									'x','X','y','Y','z','Z'};
		
		int firstIndex = 0;
		int secondIndex = 0;
		
		/* Go through each character of the 1st string */
		for(int y = 0; y < s1.length(); y++)
		{
			/* We have iterated through each character in the 1st string.
			 * Both strings have been identical to this point.
			 * The 2nd string is longer than the 1st string.
			 * So the 2nd string comes after the 1st string alphabetically.
			 */
			if(y > s2.length()-1)
			{
				return -1;
			}
			/* Find the alphabetical position of the y'th character in the 1st string. */ 
			for(int x = 0; x < 54; x++)
			{
				if(alphabeticOrder[x] == s1.charAt(y))
				{
					firstIndex = x;
					break;
				}
			}
			/* Find the alphabetical position of the y'th character in the 2nd string. */
			for(int k = 0; k < 54; k++)
			{
				if(alphabeticOrder[k] == s2.charAt(y))
				{
					secondIndex = k;
					break;
				}
			}
			/* If the position of the y'th character of the 1st string is less than the
			 * position of the y'th character of the 2nd string, the 1st string
			 * alphabetically precedes the 2nd string. */
			if(firstIndex < secondIndex)
				return 1;
			/* If the position of the y'th character of the 1st string is more than the
			 * position of the y'th character of the 2nd string, the 1st string
			 * alphabetically procedes the 2nd string. */
			else if(firstIndex > secondIndex)
				return -1;
		}
		/* If there were no differences and the 1st string is shorter in length, then
		 * it comes first alphabetically */
		if(s1.length() < s2.length())
			return 1;
		/* If there were no differences and the 1st string is longer in length, then
		 * it comes second alphabetically */
		else if(s1.length() > s2.length())
			return -1;
		/* The strings are identical */
		else
			return 0;
	}
}