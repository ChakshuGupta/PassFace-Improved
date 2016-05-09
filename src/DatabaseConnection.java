 
import java.sql.*;
import java.util.ArrayList;
import org.json.simple.JSONObject;
 
public class DatabaseConnection
{
	
	Connection connection = null;
	Statement statement = null;
	
	void createDB()//CREATE THE DATABASE
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost/","root", "root");
			if(connection!=null)
			{
				statement = connection.createStatement();	
				String sql = "CREATE DATABASE IF NOT EXISTS USERS";
				statement.executeUpdate(sql);
			}
			else
			{
				System.out.print("Error in Connection");
			}
		}
		catch(ClassNotFoundException | SQLException e)
		{
			e.printStackTrace();
		}
		
	}
 
    void createBannedUserTable() // CREATE THE BANNED USER TABLE
    {
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			createDB();
			connection = DriverManager.getConnection("jdbc:mysql://localhost/USERS","root", "root");
			if(connection!=null)
			{
				statement = connection.createStatement();	
				String sql = "CREATE TABLE IF NOT EXISTS BANNED_USERS (IP_Address VARCHAR(255), MAC_Address VARCHAR(255))";
				statement.executeUpdate(sql);
			}
			else
			{
				System.out.print("Error in Connection");
			}
		}
		catch(ClassNotFoundException | SQLException e)
		{
			e.printStackTrace();
		}
    }
    void InsertDataInTable(String ip, String mac)  // INSERT DATA IN THE BANNED USER TABLE
    {
    	PreparedStatement preparedStatement = null;
    	String InsertSql = "INSERT INTO BANNED_USERS(IP_Address, MAC_Address) VALUES (\""+ip+"\", \"" + mac+"\" )";
    	
    	try 
    	{
			preparedStatement = connection.prepareStatement(InsertSql);
			preparedStatement.executeUpdate();
			
			System.out.println("Record is inserted in table!");
		} 
    	catch (SQLException e) 
    	{			
			e.printStackTrace();
		}
    }
    void createUserTable() // CREATE REGISTERED USER TABLE
    {
    	try
		{
			Class.forName("com.mysql.jdbc.Driver");
			createDB();
			connection = DriverManager.getConnection("jdbc:mysql://localhost/USERS","root", "root");
			if(connection!=null)
			{
				statement = connection.createStatement();	
				String sqlUserTable = "CREATE TABLE IF NOT EXISTS USERS (UserId VARCHAR(255))";
				String sqlPassFaceTable = "CREATE TABLE IF NOT EXISTS login_details (userid VARCHAR(255) NOT NULL, passface VARCHAR(255) DEFAULT NULL, PRIMARY KEY (userid))";
				statement.executeUpdate(sqlUserTable);
				statement.executeUpdate(sqlPassFaceTable);
				
			}
			else
			{
				System.out.print("Error in Connection");
			}
		}
		catch(ClassNotFoundException | SQLException e)
		{
			e.printStackTrace();
		}
    	
    }
    @SuppressWarnings("unchecked")
	void InsertDataInTable(String userid, ArrayList<Integer> passface, ArrayList<Object> dir, ArrayList<Object> disp) // INSERT DATA IN REGISTERED USER TABLE
    {
    	PreparedStatement preparedStatement = null;
    	ArrayList<JSONObject> passface_details = new ArrayList<JSONObject>();
    	for (int i=0; i<passface.size(); i++)
    	{
    		JSONObject pass = new JSONObject();
    		pass.put("image", passface.get(i));
    		pass.put("direction", dir.get(i));
    		pass.put("displacement", disp.get(i));
    		passface_details.add(pass);
    	}
    	
    	String insert_query = "INSERT INTO login_details(userid, passface) VALUES (\" "+userid+" \", \'"+passface_details.toString()+"\')";
    	try 
    	{
    			
			preparedStatement = connection.prepareStatement(insert_query);
			preparedStatement.executeUpdate();
		
		} 
    	catch (SQLException e) 
    	{			
			e.printStackTrace();
		}
    }
    ResultSet SearchInUserTable(String search) // SEARCH USERID IN THE REGISTERED USER TABLE
    {
    	try
    	{
	    	Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost/USERS","root", "root");
			statement = connection.createStatement();	
			String query = "SELECT * FROM login_details WHERE UserId= \" "+search+ "\"";
			ResultSet rs = statement.executeQuery(query);
			
			return rs;
			
    	}
    	catch (ClassNotFoundException | SQLException e)
    	{
    		e.printStackTrace();
    	}
		return null;
    }
    boolean SearchInBannedUserTable(String ip, String mac)	//SEARCH IP AND MAC ADDRESS IN BANNED USER TABLE
    {
    	int count=0;
    	try
    	{
	    	Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost/USERS","root", "root");
			statement = connection.createStatement();	
			String query = "SELECT * FROM BANNED_USERS WHERE MAC_Address= \""+mac+"\"OR IP_Address= \""+ip+"\"";
			ResultSet rs = statement.executeQuery(query);
			while(rs.next())
			{
				count++;
			}
			if(count>0)
			{
				return true;
			}
			
			return false;
			
			
    	}
    	catch (ClassNotFoundException | SQLException e)
    	{
    		e.printStackTrace();
    	}
    	return false;
		
    }
  
    
}