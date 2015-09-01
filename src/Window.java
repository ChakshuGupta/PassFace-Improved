//IMPROVED PASSFACE SYSTEM
//BY CHAKSHU GUPTA

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

class Window extends JFrame implements ActionListener
{
	//DECLARING REQUIRED VARIABLES
	JFrame window = new JFrame("PASSFACE SYSTEM");
	JFrame name_register;
	JFrame login_name;
	JFrame login;
	JFrame login2;
	JFrame login3;
	JFrame register = new JFrame("PASSFACE SYSTEM : Registeration");
	JFrame register2 = new JFrame("PASSFACE SYSTEM : Registeration");
	JPanel ImgPanel = new JPanel();
	
	JTextField uid;
	JTextField user_id;
	
	JCheckBox[] cbox = new JCheckBox[20];
	ImageIcon[] image = new ImageIcon[20];
	ImageIcon[] selectedImage = new ImageIcon[20];
	JRadioButton[] button = new JRadioButton[9];
	JComboBox[] dir = new JComboBox[3];
	JComboBox[] displacement = new JComboBox[3];
	ArrayList<Integer> ReceivedValues = new ArrayList<Integer>();
	
	//Variables for registration
	Object[] selectedDirection = new Object[3];
	Object[] selectedDisplacement = new Object[3];
	int[] selected_imgs = new int[3];
	String UserID;
	String FName;
	String LName;
	
	 DatabaseConnection dbConn = new DatabaseConnection();
	
	//STATIC VARIABLE FOR CHECKING THE PASSFACE
	static int correctEntry=0;
	static int numberOfAttempts = 0;
	
	//CONSTRUCTOR
	Window()
	{
		window.setLocation(0,0);//SETTING WINDOW LOCATION
		window.setLayout(new GridLayout(3,0)); // SETTING WINDOW LAYOUT
		window.setSize(400, 400);
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //END THE PROCESS ON PRESSING CLOSE BUTTON
		JLabel heading = new JLabel("PASSFACE SYSTEM");
		heading.setFont (heading.getFont ().deriveFont (34.0f));//SETTING FONT SIZE OF THE LABEL
		window.add(heading);
		
		dbConn.createBannedUserTable();//Creating tables for banned users;
		dbConn.createUserTable();//Creating tables for users;
		
		JButton register = new JButton("REGISTER");
		register.setFont (register.getFont ().deriveFont (16.0f));
		register.setActionCommand("register");
		register.addActionListener(this);
		window.add(register);
		
		
		JButton login = new JButton("LOGIN");
		login.setFont (login.getFont ().deriveFont (16.0f));
		login.setActionCommand("login");
		login.addActionListener(this);
		window.add(login);
		window.setVisible(true);
			
		
	}	
	void name_register()
	{
		name_register= new JFrame("PASSFACE SYSTEM : Registeration");
		name_register.setLocation(0,0);//SETTING WINDOW LOCATION
		name_register.setLayout(new GridLayout(0,1)); // SETTING WINDOW LAYOUT
		name_register.setSize(350, 300);
		name_register.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //END THE PROCESS ON PRESSING CLOSE BUTTON
		JLabel heading = new JLabel("REGISTRATION");
		heading.setFont (heading.getFont ().deriveFont (34.0f));//SETTING FONT SIZE OF THE LABEL
		name_register.add(heading);
		
		uid = new JTextField();// Text Field to enter the User ID for Registration
		
		uid.addActionListener(this);
		
		JLabel userID = new JLabel("USER-ID - ");
//		JLabel Fname = new JLabel("FIRST NAME - ");
//		JLabel Lname = new JLabel("LAST NAME - ");
		
		
		name_register.add(userID);
		name_register.add(uid);
		
		JButton next = new JButton("NEXT->");
		next.setFont (next.getFont ().deriveFont (16.0f));
		next.setActionCommand("submit");
		next.addActionListener(this);
		name_register.add(next);
		name_register.setVisible(true);
	}
	void register()
	{
		register.setLocation(0,0);//SETTING WINDOW LOCATION
		register.setLayout(new BorderLayout()); // SETTING WINDOW LAYOUT
		register.setExtendedState(JFrame.MAXIMIZED_BOTH); //FOR MAXIMIZED WINDOW
		register.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //END THE PROCESS ON PRESSING CLOSE BUTTON
		JLabel heading = new JLabel("REGISTRATION");
		heading.setFont (heading.getFont ().deriveFont (34.0f));//SETTING FONT SIZE OF THE LABEL
		register.add(heading, BorderLayout.PAGE_START);
		for(int i=1; i<=20; i++)//Setting the images for registration
		{
			image[i-1]= new ImageIcon("Pics/"+i+".jpg");
			selectedImage[i-1]= new ImageIcon("Pics/"+i+"_s.jpg");//Images to differentiate the selected ones
			Image img = image[i-1].getImage();
			Image img_s = selectedImage[i-1].getImage();
			Image new_image= img.getScaledInstance(150, 150,  java.awt.Image.SCALE_SMOOTH);//Scaling the image to required size
			Image selected_image=img_s.getScaledInstance(150, 150,  java.awt.Image.SCALE_SMOOTH);
			ImageIcon newIcon = new ImageIcon(new_image);
			ImageIcon selectImage = new ImageIcon(selected_image);
			
			cbox[i-1]=new JCheckBox(""+i, newIcon);
			cbox[i-1].setSelected(false);
			cbox[i-1].setSelectedIcon(selectImage);// Setting the image for selected checkbox
			ImgPanel.add(cbox[i-1]);
		}
		ImgPanel.setLayout(new GridLayout(4,5));
		register.add(ImgPanel, BorderLayout.CENTER);
		JButton submit = new JButton("SUBMIT");
		submit.setFont (submit.getFont ().deriveFont (16.0f));
		submit.setActionCommand("submit1");
		submit.addActionListener(this);
		register.add(submit, BorderLayout.PAGE_END);
				
		
		
		register.setVisible(true);
		
	}
	void register(int selected_images[])
	{
		register2.setLocation(0,0);//SETTING WINDOW LOCATION
		register2.setLayout(new BorderLayout()); // SETTING WINDOW LAYOUT
		register2.setExtendedState(JFrame.MAXIMIZED_BOTH); //FOR MAXIMIZED WINDOW
		register2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //END THE PROCESS ON PRESSING CLOSE BUTTON
		
			JLabel heading = new JLabel("REGISTRATION: Step2");
			heading.setFont (heading.getFont ().deriveFont (34.0f));//SETTING FONT SIZE OF THE LABEL
			register2.add(heading, BorderLayout.PAGE_START);
			
			JPanel registerPage = new JPanel();
			JPanel[] registerDetail = new JPanel[3];
			JPanel Imgs =new JPanel();
			
			
			Imgs.setLayout(new GridLayout(3,0));
			registerPage.setLayout(new GridLayout(3,0));
			
			String[] direction = {"UP","DOWN","LEFT","RIGHT","NO CHANGE"};//Options for drop down Menu
			String[] disp = {"0" ,"1", "2"};
			
			for(int i=0; i<3; i++) // Taking the details for each selected image
			{
				image[i]= new ImageIcon("Pics/"+selected_images[i]+".jpg");
				Image img = image[i].getImage();
				Image new_image= img.getScaledInstance(200, 200,  java.awt.Image.SCALE_SMOOTH);
				ImageIcon newIcon = new ImageIcon(new_image);
				cbox[i]=new JCheckBox("", newIcon);
				Imgs.add(cbox[i]);
				
				registerDetail[i]= new JPanel();
				registerDetail[i].setLayout(new GridLayout(6,0));
				JLabel label1 = new JLabel("Enter Direction : ");
				JLabel label2 = new JLabel("Enter Displacement : ");
				dir[i] = new JComboBox(direction);
				displacement[i] = new JComboBox(disp);
				
				dir[i].addActionListener(this);
				displacement[i].addActionListener(this);
				
				registerDetail[i].add(label1);
				registerDetail[i].add(dir[i]);
				registerDetail[i].add(label2);
				registerDetail[i].add(displacement[i]);
				registerPage.add(registerDetail[i]);
				
			}
			
			register2.add(Imgs, BorderLayout.LINE_START);
			register2.add(registerPage, BorderLayout.CENTER);
			
			JButton submit = new JButton("SUBMIT");
			submit.setFont (submit.getFont ().deriveFont (16.0f));
			submit.setActionCommand("submit2");
			submit.addActionListener(this);
			register2.add(submit, BorderLayout.PAGE_END);		
			register2.setVisible(true);
		
	}
	void newAttempt(int selected_imgs[])
	{
		correctEntry=0;
		
		login(selected_imgs);		
		
	}
	void login()
	{
		
		login_name= new JFrame("PASSFACE SYSTEM : LOGIN");
		login_name.setLocation(0,0);//SETTING WINDOW LOCATION
		login_name.setLayout(new GridLayout(0,1)); // SETTING WINDOW LAYOUT
		login_name.setSize(350, 350);
		login_name.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //END THE PROCESS ON PRESSING CLOSE BUTTON
		JLabel heading = new JLabel("LOGIN");
		heading.setFont (heading.getFont ().deriveFont (34.0f));//SETTING FONT SIZE OF THE LABEL
		login_name.add(heading);
		
		user_id = new JTextField();	// Text Field to input User ID
		JLabel userID = new JLabel("ENTER USER ID - ");
		
		user_id.addActionListener(this);
		
		login_name.add(userID);
		login_name.add(user_id);
		
		JButton submit = new JButton("NEXT");
		submit.setActionCommand("submit_name");
		submit.addActionListener(this);
		
		login_name.add(submit);
		
		login_name.setVisible(true);
	}
	public void login(int selected_images[])
	{
			login = new JFrame("PASSFACE SYSTEM : LOGIN");
			login.setLocation(0,0);//SETTING WINDOW LOCATION
			login.setLayout(new BorderLayout()); // SETTING WINDOW LAYOUT
			login.setExtendedState(JFrame.MAXIMIZED_BOTH); //FOR MAXIMIZED WINDOW
			login.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //END THE PROCESS ON PRESSING CLOSE BUTTON
			
			JPanel loginPage = new JPanel();
			
			JLabel heading = new JLabel("LOGIN: Step1");
			heading.setFont (heading.getFont ().deriveFont (34.0f));//SETTING FONT SIZE OF THE LABEL
			login.add(heading, BorderLayout.PAGE_START);
			
			UniqueRandomNumbers uniqueValues = new UniqueRandomNumbers(); // Creating an object for Unique Random Numbers Class
			int[] randomValues = new int[9];
			randomValues= uniqueValues.Unique_Values(selected_images, 1); // Calling the Unique Values Function to find 9 unique values for login
			
			ButtonGroup group = new ButtonGroup();
			try
			{
				for(int i=0; i<9; i++)
				{
					BufferedImage buttonIcon = ImageIO.read(new File("Pics/"+ randomValues[i]+".jpg"));
					BufferedImage selectedIcon = ImageIO.read(new File("Pics/"+ randomValues[i]+"_s.jpg"));
					Image new_image= buttonIcon.getScaledInstance(200, 200,  java.awt.Image.SCALE_SMOOTH);
					Image selected_image= selectedIcon.getScaledInstance(200, 200,  java.awt.Image.SCALE_SMOOTH);
					
					button[i] = new JRadioButton(new ImageIcon(new_image));
					button[i].setSelectedIcon(new ImageIcon(selected_image));
					button[i].setSelected(false);
					group.add(button[i]);
						
					loginPage.add(button[i]);
					button[i].setActionCommand(""+randomValues[i]);
				    button[i].addActionListener(this);
					
				}
				randomValues= null;
				loginPage.setLayout(new GridLayout(3,0));
				login.add(loginPage, BorderLayout.CENTER);
				
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			JButton nextPage = new JButton("NEXT");
			nextPage.setFont (nextPage.getFont ().deriveFont (18.0f));
			login.add(nextPage, BorderLayout.PAGE_END);
			nextPage.setActionCommand("Next");// SETTING THE ACTION COMMAND FOR NEXT BUTTON
		    nextPage.addActionListener(this);
		    login.setVisible(true);
	}
	public void login_2(int[] exclude)
	{
		int[] random_values= new int[9];
		UniqueRandomNumbers unique_image = new UniqueRandomNumbers();
		
		JPanel Step2 = new JPanel(null);
		
		
		JLabel heading2 = new JLabel("LOGIN : Step 2");	
		heading2.setFont (heading2.getFont ().deriveFont (34.0f));
		
		login2 = new JFrame("PASSFACE SYSTEM : LOGIN - Step2");
		login2.setLocation(0,0);
		login2.setExtendedState(JFrame.MAXIMIZED_BOTH);
		login2.setLayout(new BorderLayout());
		login2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		login2.add(heading2, BorderLayout.PAGE_START);
		ButtonGroup group = new ButtonGroup();
		random_values = unique_image.Unique_Values(exclude, 0);
		try
		{
			for(int i=0; i<9; i++)
			{
				BufferedImage buttonIcon = ImageIO.read(new File("Pics/"+ random_values[i]+".jpg"));
				BufferedImage selectedIcon = ImageIO.read(new File("Pics/"+ random_values[i]+"_s.jpg"));
				Image new_image= buttonIcon.getScaledInstance(200, 200,  java.awt.Image.SCALE_SMOOTH);
				Image selected_image= selectedIcon.getScaledInstance(200, 200,  java.awt.Image.SCALE_SMOOTH);
				
				button[i] = new JRadioButton(new ImageIcon(new_image));
				button[i].setSelectedIcon(new ImageIcon(selected_image));
				button[i].setSelected(false);
				group.add(button[i]);
								
				Step2.add(button[i]);
				button[i].setActionCommand(""+random_values[i]);
			    button[i].addActionListener(this);
				
			}
			random_values=null;
			Step2.setLayout(new GridLayout(3,0));
			login2.add(Step2, BorderLayout.CENTER);
			
		}
		catch(Exception e)
		{
			
		}
		JButton nextPage = new JButton("NEXT");
		nextPage.setFont (nextPage.getFont ().deriveFont (18.0f));
		login2.add(nextPage, BorderLayout.PAGE_END);
		nextPage.setActionCommand("Next2");
	    nextPage.addActionListener(this);
		login2.setVisible(true);	
				
	}	
	
	public void login_3(int[] exclude)
	{
		int[] random_values= new int[9];
		UniqueRandomNumbers unique_image = new UniqueRandomNumbers();
		
		JPanel Step3 = new JPanel(null);
		
		JLabel heading3 = new JLabel("LOGIN : Step 3");	
		heading3.setFont (heading3.getFont ().deriveFont (34.0f));
		
		login3 = new JFrame("PASSFACE SYSTEM : LOGIN - Step3");
		login3.setLocation(0,0);
		login3.setExtendedState(JFrame.MAXIMIZED_BOTH);
		login3.setLayout(new BorderLayout());
		login3.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		login3.add(heading3, BorderLayout.PAGE_START);
		
		ButtonGroup group = new ButtonGroup();
		random_values = unique_image.Unique_Values(exclude, 2);
		try
		{
			for(int i=0; i<9; i++)
			{
				BufferedImage buttonIcon = ImageIO.read(new File("Pics/"+ random_values[i]+".jpg"));
				BufferedImage selectedIcon = ImageIO.read(new File("Pics/"+ random_values[i]+"_s.jpg"));
				Image new_image= buttonIcon.getScaledInstance(200, 200,  java.awt.Image.SCALE_SMOOTH);
				Image selected_image= selectedIcon.getScaledInstance(200, 200,  java.awt.Image.SCALE_SMOOTH);
				
				button[i] = new JRadioButton(new ImageIcon(new_image));
				button[i].setSelectedIcon(new ImageIcon(selected_image));
				button[i].setSelected(false);
				group.add(button[i]);
								
				Step3.add(button[i]);
				button[i].setActionCommand(""+random_values[i]);
			    button[i].addActionListener(this);
				
			}
			Step3.setLayout(new GridLayout(3,0));
			login3.add(Step3, BorderLayout.CENTER);
			
		}
		catch(Exception e)
		{
			
		}
		
		random_values=null;		
		JButton nextPage = new JButton("ENTER");
		nextPage.setFont (nextPage.getFont ().deriveFont (18.0f));
		login3.add(nextPage, BorderLayout.PAGE_END);
		nextPage.setActionCommand("Next3");
	    nextPage.addActionListener(this);
		login3.setVisible(true);	
		
		
	}
	int findActualValue(int selectedPosition, String direction, Object displacement)// Finding the value of the passface from the selected value
	{
		int val;
		int initColumn, initRow, finalRow, finalColumn;
		int disp = (int) displacement;
		
		initColumn = selectedPosition%3;//FINDING THE ROW AND COLUMN VALUES OF THE SELECTED IMAGE
		if(selectedPosition <3 && selectedPosition>=0)
		{
			initRow = 0;
		}
		else if(selectedPosition < 6 && selectedPosition >= 3)
		{
			initRow = 1;
		}
		else
		{
			initRow = 2;
		}
		
		//System.out.println(direction);
		//CHECKING EACH SELECTED IMAGE FOR THE CORRESPONDING PASSFACE
		
		if(direction.equals("DOWN"))
		{
			finalColumn = initColumn;
			
			if(initRow>=disp)
			{
				finalRow = (initRow-disp)%3;			
			}
			else 
			{
				finalRow = 3 + (initRow-disp)%3;
				
			}		
			
			val = finalRow*3 + finalColumn;
			
			return val;
			
		}
		else if(direction.equals("UP"))
		{
			finalColumn = initColumn;
			
			finalRow = (initRow+disp)%3;
			
			//System.out.println(" Final - "+ finalColumn + " " + finalRow);
			val = finalRow*3 + finalColumn;
			//System.out.println(val);
			return val;
						
		}
		else if(direction.equals("RIGHT"))
		{
			finalRow = initRow;
			finalColumn = (initColumn-disp)%3;
			if(initColumn >= disp)
			{
				finalColumn = (initColumn-disp)%3;
				
			}
			else
			{
				finalColumn = 3+(initColumn-disp) % 3;
			}
			//System.out.println(" Final - "+ finalColumn + " " + finalRow);
			val = finalRow*3 + finalColumn;// CALCULATING THE INDEX OF THE PASSFACE FROM ROW AND COLUMN VALUES OBTAINED
			//System.out.println(val);
			return val;
		}
		else if(direction.equals("LEFT"))
		{
			finalRow = initRow;
			
			finalColumn = (initColumn+disp)%3;
			
			//System.out.println(" Final - "+ finalColumn + " " + finalRow);
			val = finalRow*3 + finalColumn;
			//System.out.println(val);
			return val;
			
		}
		else
			return 0;
		
		
		
	}

	//ACTIONS PERFORMED BY BUTTONS
	public void actionPerformed(ActionEvent e)
	{
		String cmd = e.getActionCommand();
		if(cmd == "register") // THE COMMAND DESCRIPTION FOR REGISTER BUTTON
		{			
			window.setVisible(false);
			window.dispose();
			name_register();
			
		}
		else if(cmd == "login")// THE COMMAND DESCRIPTION FOR LOGIN BUTTON
		{
			int id;
			FindMACAddress address = new FindMACAddress();
			id = address.IPandMACAddress(0);
			
			if(id==1)
			{
				JOptionPane.showMessageDialog(null, "Your IP/ MAC Address is banned! Cannot Login", "alert", JOptionPane.INFORMATION_MESSAGE);
				window.setVisible(false);
				window.dispose();
				return;
			}
			else
			{			
				window.setVisible(false);
				window.dispose();
				login();
			}
		}
		else if(cmd=="submit")// THE COMMAND DESCRIPTION FOR SUBMIT BUTTON FOR REGISTRATION
		{
			UserID = uid.getText();
			ResultSet rs;
			//System.out.println(FName);
			
			if(UserID.equals(""))
			{
				JOptionPane.showMessageDialog(null, "Enter non-empty values.", "alert", JOptionPane.INFORMATION_MESSAGE);
			}
			else
			{
								
				DatabaseConnection dbconn = new DatabaseConnection();
				rs= dbconn.SearchInUserTable(UserID);
				int count=0;
				try
				{
					while(rs.next())
					{
						count++;
					}
					if(count==0)
					{
						name_register.setVisible(false);
						name_register.dispose();
						register();	
					}
					else
					{
						JOptionPane.showMessageDialog(null, "User ID already exists!", "alert", JOptionPane.INFORMATION_MESSAGE);
					}
				}
				catch (SQLException e1) 
				{
					e1.printStackTrace();
				}			
			}			
			
		}
		else if(cmd=="submit1")
		{
			//setting initial count to 0
			int count=0;
				
				//counting the number of images selected
				for(int i=0; i<20; i++)
				{
					if(cbox[i].isSelected()==true)
					{
						count++;						
					}
				}
				
				//If selected Images is 3 then save the image number in the selected_imgs array
				if(count==3)
				{
					//setting the initial value of the selected_imgs array
					for(int i=0; i<3; i++)
					{
						selected_imgs[i]=0;
					}
					
					int x=0;
					for(int i=0; i<20; i++)
					{
						if(cbox[i].isSelected()==true)
						{
							selected_imgs[x]=i+1;
							x++;
						}
					}
					JOptionPane.showMessageDialog(null, "Enter the details for the selected images");	
				
					register.setVisible(false);
					register(selected_imgs);
				}
				else //else clear the selection 
				{
					JOptionPane.showMessageDialog(null, "Need to select 3 images! ", "alert", JOptionPane.INFORMATION_MESSAGE);
					for(int i=0; i<20; i++) 
					{
						cbox[i].setSelected(false);
					}
				
				}
		}
		else if(cmd=="submit2")
		{
			
			for(int i=0; i<3; i++)
			{
				selectedDirection[i] = new Object();
				selectedDisplacement[i] = new Object();
				
				selectedDirection[i] = dir[i].getSelectedItem();
				String check = new String((String)selectedDirection[i]);
				if(check.equals("NO CHANGE"))
					selectedDisplacement[i] = "0";
				else
					selectedDisplacement[i] = displacement[i].getSelectedItem();
		
			}
			dbConn.createUserTable();
			dbConn.InsertDataInTable(UserID, selected_imgs, selectedDirection, selectedDisplacement);
			JOptionPane.showMessageDialog(null, "REGISTRATION COMPLETE!!", "alert", JOptionPane.INFORMATION_MESSAGE);
			register2.setVisible(false);
			register2.dispose();
			new Window();
			
		}
		else if(cmd=="submit_name")
		{
			String userEntered;
			userEntered = user_id.getText();
			ResultSet rs;
			
			DatabaseConnection dbconn = new DatabaseConnection();
			rs= dbconn.SearchInUserTable(userEntered);
				
			try 
			{
				int count=0;
				while(rs.next())
				{
					count++;
				}
				if(count>0)
				{
					rs.beforeFirst();
					while(rs.next())
					{
						selected_imgs[0]= rs.getInt("PassFace1");
						selected_imgs[1]= rs.getInt("PassFace2");
						selected_imgs[2]= rs.getInt("PassFace3");
						
						selectedDirection[0]= rs.getString("Direction1");
						selectedDirection[1]= rs.getString("Direction2");
						selectedDirection[2]= rs.getString("Direction3");
						
						selectedDisplacement[2]= (rs.getInt("Displacement")%10);
						selectedDisplacement[1]= ((rs.getInt("Displacement")/10)%10);
						selectedDisplacement[0]= ((rs.getInt("Displacement")/100)%10);
						
						login_name.setVisible(false);
						login(selected_imgs);
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "The given UserID is not registered!!", "alert", JOptionPane.INFORMATION_MESSAGE);
					login_name.setVisible(false);
					new Window();
				}
			} 
			catch (SQLException e1) 
			{
				e1.printStackTrace();
			}
			
		
		}
		else if(cmd=="Next")
		{
			int val;
			int count=0;
			for(int i=0; i<9; i++)
			{
				if(button[i].isSelected()==true)
				{
					count++;
				}
				
			}
			if(count==1)
			{
				for(int i=0; i<9; i++)
				{
					if(button[i].isSelected()==true)
					{
						val= findActualValue(i, (String)selectedDirection[1], selectedDisplacement[1]);// CALLING THE FUNCTION WITH CURRENT INDEX VALUE AND THE DIRECTION AND DISPLACEMENT VALUES
						ReceivedValues.add(Integer.parseInt(button[val].getActionCommand()));
						//System.out.println("val = "+ val);
					}
				}
				for(int i=0; i<9; i++)
				{
					button[i].setSelected(false);
				}
			
				login.dispose();
				login_2(selected_imgs);
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Need to select an image", "alert", JOptionPane.INFORMATION_MESSAGE);
				for(int i=0; i<9; i++)
				{
					button[i].setSelected(false);
					button[i]=null;
				}
			}
		
			
		}
		else if(cmd=="Next2")
		{
			int val;
			int count=0;
			for(int i=0; i<9; i++)
			{
				if(button[i].isSelected()==true)
				{
					count++;
				}
				
			}
			if(count==1)
			{
			
				for(int i=0; i<9; i++)
				{
					if(button[i].isSelected()==true)
					{
						val= findActualValue(i, (String)selectedDirection[0], selectedDisplacement[0]); 
						ReceivedValues.add(Integer.parseInt(button[val].getActionCommand()));
					}
				}
				
				for(int i=0; i<9; i++)
				{
					button[i].setSelected(false);
				}
				
				login2.dispose();
				login_3(selected_imgs);
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Need to select an image", "alert", JOptionPane.INFORMATION_MESSAGE);
				for(int i=0; i<9; i++)
				{
					button[i].setSelected(false);
					button[i]=null;
				}
			}
		}
		else if(cmd=="Next3")
		{
			int val;
			int count=0;
			for(int i=0; i<9; i++)
			{
				if(button[i].isSelected()==true)
				{
					count++;
				}
				
			}
			if(count==1)
			{
				for(int i=0; i<9; i++)
				{
					if(button[i].isSelected()==true)
					{
						val= findActualValue(i, (String)selectedDirection[2], selectedDisplacement[2]);
						ReceivedValues.add(Integer.parseInt(button[val].getActionCommand()));
					}
				}
				for(int i=0; i<3; i++)
				{
					if(ReceivedValues.contains(selected_imgs[i]))
					{
						correctEntry++;
					}
				}
				for(int i=0; i<9; i++)
				{
					button[i].setSelected(false);
				}
							
				if(correctEntry==3)
				{
					JOptionPane.showMessageDialog(null, "WELCOME! YOU HAVE LOGGED IN ", "alert", JOptionPane.INFORMATION_MESSAGE);
					
				}
				else
				{
					numberOfAttempts++;
					if(numberOfAttempts<3)
					{
						
						JOptionPane.showMessageDialog(null, "SORRY! INCORRECT PASSFACES! NUMBER OF ATTEMPTS LEFT: "+(3-numberOfAttempts), "alert", JOptionPane.INFORMATION_MESSAGE);
						newAttempt(selected_imgs);
					}
					else
					{
						JOptionPane.showMessageDialog(null, "NO ATTEMPTS LEFT!", "alert", JOptionPane.INFORMATION_MESSAGE);
						
						//FINDING IP ADDRESS AND MAC ADDRESS OF THE USER
						
						FindMACAddress address = new FindMACAddress();			
						address.IPandMACAddress(1);		
					}
				}
				login3.setVisible(false);
				login3.dispose();
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Need to select an image", "alert", JOptionPane.INFORMATION_MESSAGE);
				for(int i=0; i<9; i++)
				{
					button[i].setSelected(false);
					button[i]=null;
				}				
			}			
		}
	}
	public static void main(String args[])
	{
		new Window();
	}
	
}
