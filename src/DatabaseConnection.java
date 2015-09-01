 
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.*;
 
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
				String sqlPassFaceTable = "CREATE TABLE IF NOT EXISTS PassFace (UserId VARCHAR(255), PassFace1 INTEGER, PassFace2 INTEGER, PassFace3 INTEGER, Direction1 CHARACTER(255), Direction2 CHARACTER(255), Direction3 CHARACTER(255), Displacement INTEGER)";
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
    void InsertDataInTable(String UserID, int passface[], Object dir[], Object disp[]) // INSERT DATA IN REGISTERED USER TABLE
    {
    	PreparedStatement preparedStatement = null;
    	PreparedStatement preparedStatement1 = null;
    	String InsertSql = "INSERT INTO USERS(UserId) VALUES (\" " + UserID + " \" )";
    	String Insert = "INSERT INTO PassFace(UserId, PassFace1, PassFace2, PassFace3, Direction1,  Direction2, Direction3, Displacement) VALUES (\" " + UserID + " \",  " + passface[0] + "," + passface[1]+","+passface[2]+"  ,\"" + (String)dir[0]+"\" , \"" + (String)dir[1]+"\" , \"" + (String)dir[2] + "\" , " + disp[0]+"" + disp[1] +""+ disp[2] + " )";
    	try 
    	{
			preparedStatement = connection.prepareStatement(InsertSql);
			preparedStatement.executeUpdate();
			
			preparedStatement1 = connection.prepareStatement(Insert);
			preparedStatement1.executeUpdate();
			
			//System.out.println("Record is inserted in table!");
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
			String query = "SELECT * FROM PassFace WHERE UserId= \" "+search+ "\"";
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
