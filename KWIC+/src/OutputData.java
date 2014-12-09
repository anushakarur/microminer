
/* Output alphabetized circular shifted data*/
public class OutputData{
	
	KWIC theApplet = null;
	
	/* This method represents the upstream socket for this filter */
	public void upstream(OrderedLine line){

		//System.out.println("IN: Output class");

		downstream(line);
		
	}

	
	/* This method represents the downstream socket for this filter */
	public void downstream(OrderedLine thisLine)
	{

		//System.out.println("output line ==> " +output);
		//System.out.println("OUT: Output class");

		// Send back to the JavaApplet the new updates
		
		theApplet = KWIC.selfRef;
		
		String output = thisLine.descriptor + " " + thisLine.URLPart;
		
		theApplet.setTextArea(output, 1);

		//KWIC.upstream(output);

	}
}