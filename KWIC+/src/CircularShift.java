import java.util.Scanner;

public class CircularShift {


	Alphabetizer alphabetizer = new Alphabetizer();


	public void CircularShiftSetUp(OrderedLine thisLine){

		OrderedLine newLine;
		//System.out.println("IN: Shift class");
		String line = thisLine.descriptor;
		
		if(line == null){
		
			//System.out.println("OUT: Shift class");
        		alphabetizer.upstream(thisLine);
        	}
      		else
        	{

			int linelength=line.length();

			while(linelength>=0){
				//==this is where to call the alphabetizer function==
	            
				newLine = new OrderedLine(line, thisLine.URLPart);
				
				alphabetizer.upstream(newLine);
			
				//===================================================
	           

				Scanner scan=new Scanner(line);
	           		String token=scan.next();
				
				if(scan.hasNext()){
			
					//System.out.println("Scan has next");

					line=line.substring(token.length()+1);
					linelength=linelength-token.length()-1;
					line=line+" "+token;

					//System.out.println("shift line =" +line);
				}

				else{

					linelength=linelength-token.length()-1;

					//System.out.println("shift line =" +line);
					//System.out.println("DOES NOT have next");
						
				}
			}

			//System.out.println("OUT: Shift class");
		}
	}
}
