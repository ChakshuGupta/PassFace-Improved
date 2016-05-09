import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
 
public class FindMACAddress {
	
	   public int IPandMACAddress( int id)
	   {
		   
			InetAddress ip;
			DatabaseConnection dbconn = new DatabaseConnection();
			
			try 
			{
		 
				ip = InetAddress.getLocalHost();
						 
				NetworkInterface network = NetworkInterface.getByInetAddress(ip);
		 
				byte[] mac = network.getHardwareAddress();
			 
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < mac.length; i++) 
				{
					sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));		
				}
				
				if(dbconn.SearchInBannedUserTable(ip.getHostAddress(), sb.toString()))
				{
					if(id==0)
					{
						return 1;
					}
					else
						return 0;
				}
				else
				{
					if(id==0)
					{
						return 2;
					}
					else
					{
						InsertInDatabase(ip.getHostAddress(), sb.toString());
						return 0;
					}
				}
				
		 
			} 
			catch (UnknownHostException e) 
			{
		 
				e.printStackTrace();
		 
			} 
			catch (SocketException e)
			{		 
				e.printStackTrace();		 
			}
			
			return 0;
		 
		}
	   void InsertInDatabase(String ip, String mac)
	   {
		   	DatabaseConnection dbConn = new DatabaseConnection();		
			dbConn.createBannedUserTable();
			dbConn.InsertDataInTable(ip, mac);
	   }
}
