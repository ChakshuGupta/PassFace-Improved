//IMPROVED PASSFACE SYSTEM
//BY CHAKSHU GUPTA

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;


class Window extends JFrame implements ActionListener
{
	//DECLARING REQUIRED VARIABLES
	JFrame window = new JFrame("PASSFACE SYSTEM");
	JFrame login;
	JFrame login2;
	JFrame login3;
	JFrame register = new JFrame("PASSFACE SYSTEM : Registeration");
	JPanel ImgPanel = new JPanel();
	
	JCheckBox[] cbox = new JCheckBox[20];
	ImageIcon[] image = new ImageIcon[20];
	ImageIcon[] selectedImage = new ImageIcon[20];
	JRadioButton[] button = new JRadioButton[9];
	JComboBox[] dir = new JComboBox[3];
	JComboBox[] displacement = new JComboBox[3];
	ArrayList<Integer> ReceivedValues = new ArrayList<Integer>();
	
	Object[] selectedDirection = new Object[3];
	Object[] selectedDisplacement = new Object[3];
	int[] selected_imgs = new int[3];
	
	//STATIC VARIABLE FOR CHECKING THE PASSFACE
	static int correctEntry=0;
	static int numberOfAttempts = 0;
	
	//CONSTRUCTOR 
	Window(int id)
	{
		window.setLocation(0,0);//SETTING WINDOW LOCATION
		window.setLayout(new BorderLayout()); // SETTING WINDOW LAYOUT
		window.setExtendedState(JFrame.MAXIMIZED_BOTH); //FOR MAXIMIZED WINDOW
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //END THE PROCESS ON PRESSING CLOSE BUTTON
		if(id==1)
		{
				JLabel heading = new JLabel("REGISTRATION");
				heading.setFont (heading.getFont ().deriveFont (34.0f));//SETTING FONT SIZE OF THE LABEL
				window.add(heading, BorderLayout.PAGE_START);
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
				window.add(ImgPanel, BorderLayout.CENTER);
				JButton submit = new JButton("SUBMIT");
				submit.setFont (submit.getFont ().deriveFont (16.0f));
				submit.setActionCommand("submit");
				submit.addActionListener(this);
				window.add(submit, BorderLayout.PAGE_END);
				
		}
		
		window.setVisible(true);
		
	}
	void register(int selected_images[])
	{
		register.setLocation(0,0);//SETTING WINDOW LOCATION
		register.setLayout(new BorderLayout()); // SETTING WINDOW LAYOUT
		register.setExtendedState(JFrame.MAXIMIZED_BOTH); //FOR MAXIMIZED WINDOW
		register.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //END THE PROCESS ON PRESSING CLOSE BUTTON
		
			JLabel heading = new JLabel("REGISTRATION: Step2");
			heading.setFont (heading.getFont ().deriveFont (34.0f));//SETTING FONT SIZE OF THE LABEL
			window.add(heading, BorderLayout.PAGE_START);
			
			JPanel registerPage = new JPanel();
			JPanel[] registerDetail = new JPanel[3];
			JPanel Imgs =new JPanel();
			
			
			Imgs.setLayout(new GridLayout(3,0));
			registerPage.setLayout(new GridLayout(3,0));
			
			String[] direction = {"UP","DOWN","LEFT","RIGHT" };//Options for drop down Menu
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
			
			register.add(Imgs, BorderLayout.LINE_START);
			register.add(registerPage, BorderLayout.CENTER);
			
			JButton submit = new JButton("SUBMIT");
			submit.setFont (submit.getFont ().deriveFont (16.0f));
			submit.setActionCommand("submit2");
			submit.addActionListener(this);
			register.add(submit, BorderLayout.PAGE_END);		
			register.setVisible(true);
		
	}
	void newAttempt(int selected_imgs[])
	{
		correctEntry=0;
		
		login(selected_imgs);		
		
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
			
			UniqueRandomNumbers uniqueValues = new UniqueRandomNumbers();
			int[] randomValues = new int[9];
			randomValues= uniqueValues.Unique_Values(selected_images, 1);
			
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
				
			}
			JButton nextPage = new JButton("NEXT");
			nextPage.setFont (nextPage.getFont ().deriveFont (18.0f));
			login.add(nextPage, BorderLayout.PAGE_END);
			nextPage.setActionCommand("Next");
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
	int findActualValue(int selectedPosition, Object direction, Object displacement)// Finding the value of the passface from the selected value
	{
		int val;
		int initColumn, initRow, finalRow, finalColumn;
		int disp = Integer.valueOf((String)displacement);//CONVERTING OBJECT TO INTEGER
		
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
		
		//CHECKING EACH SELECTED IMAGE FOR THE CORRESPONDING PASSFACE
		
		if(direction.equals("DOWN"))
		{
			finalColumn = initColumn;
			
			if(initRow>=disp)
			{
				finalRow = initRow-disp;				
			}
			else 
			{
				finalRow=initRow;
				for(int i=0; i<disp; i++)
				{
					if(finalRow==0)
					{
						finalRow = 2;
					}
					else
					{
						finalRow--;
					}
				}
				
			}		
			
			val = finalRow*3 + finalColumn;
			
			return val;
			
		}
		else if(direction.equals("UP"))
		{
			finalColumn = initColumn;
			if((initRow+disp)<=2)
			{
				finalRow=initRow+disp;
			}
			else
			{
				finalRow = (initRow+disp)%3;
			}
			//System.out.println(" Final - "+ finalColumn + " " + finalRow);
			val = finalRow*3 + finalColumn;
			//System.out.println(val);
			return val;
						
		}
		else if(direction.equals("RIGHT"))
		{
			finalRow = initRow;
			if(initColumn >= disp)
			{
				finalColumn = initColumn-disp;
				
			}
			else
			{
				finalColumn=initColumn;
				for(int i=0; i<disp; i++)
				{
					if(finalColumn==0)
					{
						finalColumn = 2;
					}
					else
					{
						finalColumn--;
					}
				}
			}
			//System.out.println(" Final - "+ finalColumn + " " + finalRow);
			val = finalRow*3 + finalColumn;// CALCULATING THE INDEX OF THE PASSFACE FROM ROW AND COLUMN VALUES OBTAINED
			//System.out.println(val);
			return val;
		}
		else if(direction.equals("LEFT"))
		{
			finalRow = initRow;
			
			if(disp==0)
			{
				finalColumn=initColumn;				
			}
			else if((initColumn+disp)<=2)
			{
				finalColumn=initColumn+disp;
			}
			else
			{
				finalColumn = (initColumn+disp)%3;
			}
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
		if(cmd=="submit")
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
				
					window.setVisible(false);
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
				selectedDisplacement[i] = displacement[i].getSelectedItem();
		
			}
			JOptionPane.showMessageDialog(null, "REGISTRATION COMPLETE!!", "alert", JOptionPane.INFORMATION_MESSAGE);
			register.setVisible(false);
			login(selected_imgs);
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
						val= findActualValue(i, selectedDirection[1], selectedDisplacement[1]);// CALLING THE FUNCTION WITH CURRENT INDEX VALUE AND THE DIRECTION AND DISPLACEMENT VALUES
						ReceivedValues.add(Integer.parseInt(button[val].getActionCommand()));
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
						val= findActualValue(i, selectedDirection[0], selectedDisplacement[0]); 
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
						val= findActualValue(i, selectedDirection[2], selectedDisplacement[2]);
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
						
					}
				}
				
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
		new Window(1);
	}
	
}