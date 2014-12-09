import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.StringReader;
import java.io.IOException;
import java.util.*;

public class KWIC extends JApplet implements ActionListener{

	private JTabbedPane jtp = new JTabbedPane();

	private JPanel KWIC_container;
	private JPanel MM_container;

	private int width;
	private int height;

	
	/***** Default components *****/

	// Icons
	private Icon heading_icon = new ImageIcon("pic/img_KWIC.png");
	private Icon left_icon = new ImageIcon("pic/img_hl.png");
	private Icon inMessage_icon = new ImageIcon("pic/img_inMessage.png");
	private Icon submit_icon = new ImageIcon("pic/img_submit.png");	
	private Icon outMessage_icon = new ImageIcon("pic/img_outMessage.png");

	// Labels
	private JLabel heading = new JLabel(heading_icon, JLabel.CENTER);	
	private JLabel left = new JLabel(left_icon, JLabel.CENTER);
	private JLabel right = new JLabel(" ", JLabel.CENTER);
	private JLabel botton = new JLabel(" ");

	String clearStr = "<html><body><font size=2>Reset Data</font></body></html>";

	// Text Area
	private JLabel inMessage = new JLabel(inMessage_icon);	// Input1 message icon
	private JTextArea inText = new JTextArea(""); // This needs to be passed to the pipes		// Input1 : Website & descriptor data
	private JButton submit = new JButton("Submit", submit_icon);					// Submit Button : submit input1 data to pipes
	private JButton clear = new JButton(clearStr);// Clear Button : clear both input1 & output1
	private JLabel outMessage = new JLabel(outMessage_icon);					// Output1 message icon
	private JTextArea outText = new JTextArea("");							// Output1 : Alphabetized circular shifts from input1
	public JLabel errorMessage = new JLabel();

	// Scroll bar
	JScrollPane scrollingInText = new JScrollPane(inText);
	JScrollPane scrollingOutText = new JScrollPane(outText);

	/***** Additional components for Microminer *****/
	
	private Icon mm_heading_icon = new ImageIcon("pic/img_MM.png");						// Microminer heading icon
	private Icon searchMessage_icon = new ImageIcon("pic/img_searchMessage.png");					 
	private Icon search_icon = new ImageIcon("pic/img_submit.png");	
	private Icon resultsMessage_icon = new ImageIcon("pic/img_resultsMessage.png");

	private JLabel mm_heading = new JLabel(mm_heading_icon, JLabel.CENTER);					// Microminer heading label;
	private JLabel mm_left = new JLabel(left_icon, JLabel.CENTER);
	private JLabel mm_right = new JLabel(" ", JLabel.CENTER);
	private JLabel mm_botton = new JLabel(" ");

	private String clearSearchStr = "<html><body><font size=2>Clear Search</font></body></html>";

	private JLabel searchMessage = new JLabel(searchMessage_icon);						// Input2 message icon
	private JTextField searchField = new JTextField(50);							// Input2 : Enter keywords here
	private JButton search = new JButton("Search", search_icon);						// Search Button : Search for keywords in output1
	private JButton clearSearch = new JButton(clearSearchStr);// Clear Search Button : clear both search box & results
	private JLabel resultsMessage = new JLabel(resultsMessage_icon);					// Output2 message icon
	private JTextArea resultsText = new JTextArea("");							// Output2 : websites results

	// Scroll bar
	//JScrollPane scrollingSearchText = new JScrollPane(searchField);
	JScrollPane scrollingResultsText = new JScrollPane(resultsText);
	
	// Input class instance
	Input input = new Input();
	
	public static KWIC selfRef = null;

	/** Initialize the applet */
	public void init(){
		
		selfRef = this;

		// Set backgroundPanel
		setContentPane(new backgroundPanel());
		
		width = getWidth();
		height = getHeight();

		Container container = getContentPane();
		container.setLayout(new GridBagLayout());

		GridBagConstraints gbConstraints = new GridBagConstraints();

		gbConstraints.fill = GridBagConstraints.BOTH;
		gbConstraints.anchor = GridBagConstraints.CENTER;

		setKWIC_Container();
		setMicrominer_Container();

		jtp.add(MM_container, "MicroMiner: Search");		
		jtp.add(KWIC_container, "KWIC: Insert Data");

		jtp.setTabPlacement(JTabbedPane.TOP);

		container.add(left);
		container.add(jtp, gbConstraints);
		
	}

	public void setKWIC_Container(){

		/* KWIC JPanel subcontainer*/
		/* Set container to be GridBagLayout */

		KWIC_container = new JPanel(new GridBagLayout());
		KWIC_container.setPreferredSize(new Dimension(width,height));
		KWIC_container.setOpaque(false);

		// Create a GridBagConstraint object
		GridBagConstraints gbConstraints = new GridBagConstraints();

		gbConstraints.fill = GridBagConstraints.BOTH;
		gbConstraints.anchor = GridBagConstraints.CENTER;

		inMessage.setHorizontalAlignment(SwingConstants.LEFT);
		outMessage.setHorizontalAlignment(SwingConstants.LEFT);

		// TextArea to the container
		addComp(inMessage, KWIC_container, gbConstraints, 1, 1, 1, 2, 0, 0);
		addComp(scrollingInText, KWIC_container, gbConstraints, 2, 1, 1, 6, 3, 3);
		addComp(errorMessage, KWIC_container, gbConstraints, 4, 1, 1, 1, 0, 0);
		addComp(submit, KWIC_container, gbConstraints, 4, 3, 1, 1, 0, 0);
		addComp(clear, KWIC_container, gbConstraints, 4, 4, 1, 1, 0, 0);
		addComp(outMessage, KWIC_container, gbConstraints, 5, 1, 1, 2, 1, 0);
		addComp(scrollingOutText, KWIC_container, gbConstraints, 6, 1, 2, 6, 3, 3);

		// Labels to the container
		addComp(heading, KWIC_container, gbConstraints, 0, 0, 1, 6, 3, 1);
		//addComp(left, KWIC_container, gbConstraints, 0, 0, 10, 1, 1, 1);
		//addComp(right, KWIC_container, gbConstraints, 0, 5, 4, 3, 1, 1);
		//addComp(botton, KWIC_container, gbConstraints, 10, 0, 1, 4, 1, 1);

		// outText cannot be edited by user
		outText.setEditable(false);

		// Register Listener
		submit.addActionListener(this);
		clear.addActionListener(this);
	}
	
	public void setMicrominer_Container(){


		/* MM JPanel subcontainer*/
		/* Set container to be GridBagLayout */

	 	MM_container = new JPanel(new GridBagLayout());
		MM_container.setPreferredSize(new Dimension(width,height));
		MM_container.setOpaque(false);

		// Create a GridBagConstraint object
		GridBagConstraints gbConstraints = new GridBagConstraints();

		gbConstraints.fill = GridBagConstraints.BOTH;
		gbConstraints.anchor = GridBagConstraints.CENTER;

		searchMessage.setHorizontalAlignment(SwingConstants.LEFT);
		resultsMessage.setHorizontalAlignment(SwingConstants.LEFT);

		// TextArea to the container
		addComp(searchMessage, MM_container, gbConstraints, 1, 1, 1, 2, 0, 0);
		addComp(searchField, MM_container, gbConstraints, 2, 1, 1, 2, 0, 0);
		addComp(search, MM_container, gbConstraints, 2, 3, 1, 1, 0, 0);
		addComp(clearSearch, MM_container, gbConstraints, 2, 4, 1, 1, 0, 0);
		addComp(resultsMessage, MM_container, gbConstraints, 4, 1, 1, 2, 1, 0);
		addComp(scrollingResultsText, MM_container, gbConstraints, 5, 1, 2, 5, 3, 3);

		// Labels to the container
		addComp(mm_heading, MM_container, gbConstraints, 0, 0, 1, 6, 3, 1);
		addComp(mm_left, MM_container, gbConstraints, 0, 0, 10, 1, 1, 1);
		addComp(mm_right, MM_container, gbConstraints, 0, 5, 4, 3, 1, 1);
		addComp(mm_botton, MM_container, gbConstraints, 10, 0, 1, 4, 1, 1);

		// outText cannot be edited by user
		resultsText.setEditable(false);

		// Register Listener
		search.addActionListener(this);
		clearSearch.addActionListener(this);
	}
	
	/** This method will be involved when a button is clicked */
	public void actionPerformed(ActionEvent e){

		//System.out.println(e.getActionCommand() + " button is clicked at " +new java.util.Date(e.getWhen()));

		if(e.getActionCommand() == "Submit"){

			//System.out.println(">>submit");
			
			// Clear the out Text area
			outText.setText("");
			errorMessage.setText("");
		
			// sends text inputed to the input pipe
			downstream(inText.getText());
		}
		else if(e.getActionCommand() == "Search"){

			resultsText.setText(null);

			Search search = new Search();				
			search.upstreamTwo(searchField.getText()); 
		}

	
		else if(e.getActionCommand() == clearStr){
			
			//System.out.println(">>Clear");

			inText.setText("");
			outText.setText("");
			searchField.setText("");
			errorMessage.setText("");
			Alphabetizer.alphaList.clear();
			Search.theList.clear();
			inText.requestFocusInWindow();
		}
		

		else if(e.getActionCommand() == clearSearchStr){
			
			searchField.setText("");
			resultsText.setText("");
			searchField.requestFocusInWindow();
		}
	}
	
	public void setError(String str)
	{
		errorMessage.setText("    ERROR:  " + str);
		errorMessage.setForeground(Color.red);
	}

		
	public void setTextArea(String str, int outputOption){
		
		// Output alphabetized circular shifted data
		if(outputOption == 1){

			outText.append(str);
			outText.append("\n");
			inText.requestFocusInWindow();
		}

		// Output Search results
		else if(outputOption == 0){

			//resultsText.setText(null);
			resultsText.append(str);
			resultsText.append("\n");
			//resultsText.setText(str);
			
			searchField.requestFocusInWindow();
		}
	}

	
	/* This method represents the downstream socket for this filter */
	public void downstream(String output)
	{

		String str = "";
        
		BufferedReader reader = new BufferedReader(new StringReader(output));

		while(true)
		{
			try{
				str = reader.readLine();

			}
			
			catch(Exception e){
				e.printStackTrace();
			}
			
			input.upstream(str);

			//System.out.println("str = " +str);
			
			if(str == null || str.length() == 0){

				break;
			}	
		}
	}

	/** Add a component to the JPanel of GridBagLayout */
	private void addComp(Component c, JPanel container, 
					GridBagConstraints gbConstraints,
					int row, int column,
					int numberOfRows, int numberOfColumns,
					double weightx, double weighty){

		//Set parameters
		gbConstraints.gridy = row;		
		gbConstraints.gridx = column;
		gbConstraints.gridheight = numberOfRows;
		gbConstraints.gridwidth = numberOfColumns;	
		gbConstraints.weightx = weightx;
		gbConstraints.weighty = weighty;
	
		// Add component to the container with the specified layout
		container.add(c, gbConstraints);

	}	

	/** Add a component to the container of GridBagLayout */
	private void addComp(Component c, Container container, 
					GridBagConstraints gbConstraints,
					int row, int column,
					int numberOfRows, int numberOfColumns,
					double weightx, double weighty){

		//Set parameters
		gbConstraints.gridy = row;		
		gbConstraints.gridx = column;
		gbConstraints.gridheight = numberOfRows;
		gbConstraints.gridwidth = numberOfColumns;	
		gbConstraints.weightx = weightx;
		gbConstraints.weighty = weighty;
	
		// Add component to the container with the specified layout
		container.add(c, gbConstraints);

	}					

}

class backgroundPanel extends JPanel{

	private ImageIcon bgImageIcon = new ImageIcon("pic/img_mm_bg.jpg");
	private Image bgImage = bgImageIcon.getImage();

	public void paintComponent(Graphics g){

		g.drawImage(bgImage,0,0,getWidth(),getHeight(),this);	
	}
}