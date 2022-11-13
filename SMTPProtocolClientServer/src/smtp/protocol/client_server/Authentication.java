
package smtp.protocol.client_server;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 * @author Alexander Basho
 */
public class Authentication 
{
	//Create global variables for this class
	String username;
	String password;
	
	 
	//Create an list(array) with passwords. Passwords are writed in external file
	private static final String  Path = System.getProperty("user.dir");
	
	//Global variables take the value of function variables
	void SetUsernameAndPassword(String username,String password)
	{
		this.username = username;
		this.password = password;
	}
	
	//Get the Username of Client
	String GetUsername()
	{
		return username;	
	}
	
	//Get the Password of Client
	String GetPassword()
	{
		return password ;	
	}
	
	boolean Authentication() throws IOException
	{
		try
		{
			String ClientNames[] = {"alexanderbasho","giorgosalexopoulos","dimitrisgiannopoulos"};
			//Create an list(array) with passwords. Passwords are writed in external file
			List<String> Passwords = Files.readAllLines(Paths.get(Path+"\\Passwords\\passwords.txt"));
			//Check the password
			 if(!password.equals(Passwords.get(0)) && !password.equals(Passwords.get(1)) && !password.equals(Passwords.get(2)))
			 {
				 if((!username.equals(ClientNames[0])) && (!username.equals(ClientNames[1])) && (!username.equals(ClientNames[2])))
				 {
					 System.out.println("User "+GetUsername()+" doesn't exists"); 
					 return false;
				 }
				 else
				 {
					 System.out.println("User's password is incorrect."); 
					 return false;
				 }
			 }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return true;
	}
} 



