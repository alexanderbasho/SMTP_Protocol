package smtp.protocol.client_server;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author Alexander Basho
 */
public class ServerThread extends Thread {
	Socket socket;
	
    /**
     *
     * @param socket
     */
    //Synchronize sockets 'Threads'
    public ServerThread(Socket socket){
		synchronized(this)
		{
		this.socket = socket;
		}
	}
	@SuppressWarnings("deprecation")
	public void run()  
	{	
		final String  Path = System.getProperty("user.dir");
		
		//Create Decoder object
		CaesarEncoderDecoder decrypt = new CaesarEncoderDecoder(); 
		
		//Create object from SaltAndHashPassword to hash and salt the passwords
		SaltAndHashPassword hashedsaltedpassword = new SaltAndHashPassword(); 
		
		//Define the shift for the decoder 'This must be equal with s in Encoder'
		int s = 4; 
		
		String SP = " ";
		//Create arrays for ClientNames and ServerDomains
		String ServerDomains[] = {"basho.com","hotmail.com","gmail.com"};
		String ClientNames[] = {"alexanderbasho","giorgosalexopoulos","dimitrisgiannopoulos"};
		//First User = alexanderbasho@basho.com
		//Second User = giorgosalexopoulos@hotmail.com
		//Third User = dimitrisgiannopoulos@gmail.com
		
		try 
		{
			//Mainresponse is the serverresponse
			String mainresponse = null;
			
			//Create datainputstream for read buffer. In other words to recieve and read client messages
			DataInputStream InputStream = new DataInputStream(socket.getInputStream());
			
			//Create file with the passwords of the users
			List<String> Passwords = Files.readAllLines(Paths.get(Path+"\\Passwords\\passwords.txt"));
			
			//Create variables for save username, password and saltedandhashed passwords
			String username = InputStream.readUTF();
			String password = InputStream.readUTF();
			
			String generatedSecuredPasswordHash;
			
			try 
			{
				//Save saltedandhashed password to the variable generatedSecuredPasswordHash
				generatedSecuredPasswordHash = hashedsaltedpassword.generateStorngPasswordHash(password);
				
				//Create boolean to validate passwords. True if passwords are equal and False if are different.
				boolean matched0 = hashedsaltedpassword.validatePassword(Passwords.get(0), generatedSecuredPasswordHash);
				boolean matched1 = hashedsaltedpassword.validatePassword(Passwords.get(1), generatedSecuredPasswordHash);
				boolean matched2 = hashedsaltedpassword.validatePassword(Passwords.get(2), generatedSecuredPasswordHash);
				
			//Server response to client when he is just connected
			if(username.contains(ClientNames[0]) && password.equals(Passwords.get(0)) && matched0==true)
			{				
					//Create dataoutputstream for write to buffer. In other words to send messages and to response to client
					DataOutputStream OutputStream1 = new DataOutputStream(socket.getOutputStream());
					mainresponse = "S: Hello "+ClientNames[0]+"!"+"\nS: 220 service ready";
	        		OutputStream1.writeUTF(mainresponse);
	        		OutputStream1.flush();
			}		

			//Server response to client when he is just connected
			else if(username.contains(ClientNames[1]) && password.equals(Passwords.get(1)) && matched1==true)
			{		
					//Create dataoutputstream for write to buffer. In other words to send messages and to response to client
					DataOutputStream OutputStream1 = new DataOutputStream(socket.getOutputStream());
					mainresponse = "S: Hello "+ClientNames[1]+"!"+"\nS: 220 service ready";
	        		OutputStream1.writeUTF(mainresponse);
	        		OutputStream1.flush();
			}
			
			//Server response to client when he is just connected
			else if(username.contains(ClientNames[2]) && password.equals(Passwords.get(2)) && matched2==true)
			{
					//Create dataoutputstream for write to buffer. In other words to send messages and to response to client
					DataOutputStream OutputStream1 = new DataOutputStream(socket.getOutputStream());
					mainresponse = "S: Hello "+ClientNames[2]+"!"+"\nS: 220 service ready";
	        		OutputStream1.writeUTF(mainresponse);
	        		OutputStream1.flush();
			}
			else if(username.equals(username) && password.equals(password))
			{
					//Create dataoutputstream for write to buffer. In other words to send messages and to response to client
					DataOutputStream OutputStream1 = new DataOutputStream(socket.getOutputStream());
					mainresponse = "S: Hello "+username+"!"+"\nS: 220 service ready";
	        		OutputStream1.writeUTF(mainresponse);
	        		OutputStream1.flush();	        		
			}
			} 
			catch (NoSuchAlgorithmException | InvalidKeySpecException e) 
			{
				e.printStackTrace();
			}
			
			//Create variable to save inputs from client
			String message = null;
			
			while ((message = InputStream.readLine()) != null) 
			{

	        for(int i=0;i<ClientNames.length;i++)
	        {	
	        	//Decrypt the message of client
	        	message = decrypt.Decoder(message, s);
	        	System.out.println(message);
	        		//Server response in HELO command of Client
	        		if(message.equals("HELO"+SP+ServerDomains[0]+"CRLF"))
		            {
	        			//Create dataoutputstream for write to buffer. In other words to send messages and to response to client
						DataOutputStream OutputStream1 = new DataOutputStream(socket.getOutputStream());
		            	mainresponse = "S: 250 OK";
		        		OutputStream1.writeUTF(mainresponse);
		        		OutputStream1.flush();
		        		break;
		            }
	        		
	        		//Server response in HELO command of Client
	        		else if(message.equals("HELO"+SP+ServerDomains[1]+"CRLF"))
		            {
	        			//Create dataoutputstream for write to buffer. In other words to send messages and to response to client
						DataOutputStream OutputStream1 = new DataOutputStream(socket.getOutputStream());
		            	mainresponse = "S: 250 OK";
		        		OutputStream1.writeUTF(mainresponse);
		        		OutputStream1.flush();
		        		break;
		            } 
	        		
	        		//Server response in HELO command of Client
	        		else if(message.equals("HELO"+SP+ServerDomains[2]+"CRLF"))
		            {
	        			//Create dataoutputstream for write to buffer. In other words to send messages and to response to client
						DataOutputStream OutputStream1 = new DataOutputStream(socket.getOutputStream());
		            	mainresponse = "S: 250 OK";
		        		OutputStream1.writeUTF(mainresponse);
		        		OutputStream1.flush();
		        		break;
		            } 
	        		//Server response in EHLO command of Client
                    else if(message.equals("HELO"+SP+username+"CRLF"))
        {
			//Create dataoutputstream for write to buffer. In other words to send messages and to response to client
			DataOutputStream OutputStream1 = new DataOutputStream(socket.getOutputStream());
        	mainresponse = "S: 250 OK";
    		OutputStream1.writeUTF(mainresponse);
    		OutputStream1.flush();
    		break;
        } 
                             
	        		//Server response in EHLO command of Client
	        		if(message.equals("EHLO"+SP+ServerDomains[0]+"CRLF"))
		            {
	        			//Create dataoutputstream for write to buffer. In other words to send messages and to response to client
						DataOutputStream OutputStream1 = new DataOutputStream(socket.getOutputStream());
		            	mainresponse = "S: 250 OK";
		        		OutputStream1.writeUTF(mainresponse);
		        		OutputStream1.flush();
		        		break;
		            }
	        		
	        		//Server response in EHLO command of Client
	        		else if(message.equals("EHLO"+SP+ServerDomains[1]+"CRLF"))
		            {
	        			//Create dataoutputstream for write to buffer. In other words to send messages and to response to client
						DataOutputStream OutputStream1 = new DataOutputStream(socket.getOutputStream());
		            	mainresponse = "S: 250 OK";
		        		OutputStream1.writeUTF(mainresponse);
		        		OutputStream1.flush();;
		        		break;
		            } 
	        		
	        		//Server response in EHLO command of Client
	        		else if(message.equals("EHLO"+SP+ServerDomains[2]+"CRLF"))
		            {
	        			//Create dataoutputstream for write to buffer. In other words to send messages and to response to client
						DataOutputStream OutputStream1 = new DataOutputStream(socket.getOutputStream());
		            	mainresponse = "S: 250 OK";
		        		OutputStream1.writeUTF(mainresponse);
		        		OutputStream1.flush();
		        		break;
		            }
				//Server response in EHLO command of Client
                                else if(message.equals("EHLO"+SP+username+"CRLF"))
		            {
	        			//Create dataoutputstream for write to buffer. In other words to send messages and to response to client
						DataOutputStream OutputStream1 = new DataOutputStream(socket.getOutputStream());
		            	mainresponse = "S: 250 OK";
		        		OutputStream1.writeUTF(mainresponse);
		        		OutputStream1.flush();
		        		break;
		            } 
	        		
	        	//Server response in 'MAIL From:' command of Client
		        if (message.contains("MAIL"+SP+"From:"))
	            {
	        		if(message.contains(ServerDomains[0]) && message.contains(ServerDomains[1]) && message.contains(ServerDomains[2]))
	        		{
	        			if ((!message.contains(ClientNames[0]) && !message.contains(ClientNames[1]) && !message.contains(ClientNames[2])))
			            {
	        				//Create dataoutputstream for write to buffer. In other words to send messages and to response to client
	    					DataOutputStream OutputStream1 = new DataOutputStream(socket.getOutputStream());
							mainresponse = "S: 250 OK";
			       		 	OutputStream1.writeUTF(mainresponse);
			       		 	OutputStream1.flush();
			       		 	break;
			            }
	        			//Create dataoutputstream for write to buffer. In other words to send messages and to response to client
						DataOutputStream OutputStream1 = new DataOutputStream(socket.getOutputStream());
	        			mainresponse = "S: 251 User not Local. BYE BYE";
		        		OutputStream1.writeUTF(mainresponse);
		        		OutputStream1.flush();
		        		break;
	        		}
	        		else if (message.contains(ClientNames[0]) && message.contains(ClientNames[1]) && message.contains(ClientNames[2]))
	        		{
	        			//Create dataoutputstream for write to buffer. In other words to send messages and to response to client
						DataOutputStream OutputStream1 = new DataOutputStream(socket.getOutputStream());
	        			mainresponse = "S: 250 OK";
		        		OutputStream1.writeUTF(mainresponse);
		        		OutputStream1.flush();	
		        		break;
	        		}
	        		else
	        		{
	        			//Create dataoutputstream for write to buffer. In other words to send messages and to response to client
						DataOutputStream OutputStream1 = new DataOutputStream(socket.getOutputStream());
	        			mainresponse = "S: 250 OK";
		        		OutputStream1.writeUTF(mainresponse);
		        		OutputStream1.flush();	
		        		break;
	        		}
	            }
		        
		        	//Server response in 'RCPT To:' command of Client
		        	if (message.contains("RCPT"+SP+"To:"))
		            {
		        		if(!message.contains(ServerDomains[0]) && !message.contains(ServerDomains[1]) && !message.contains(ServerDomains[2]))
		        		{
		        			if ((!message.contains(ClientNames[0]) && !message.contains(ClientNames[1]) && !message.contains(ClientNames[2])))
				            {
		        				//Create dataoutputstream for write to buffer. In other words to send messages and to response to client
		    					DataOutputStream OutputStream1 = new DataOutputStream(socket.getOutputStream());
								mainresponse = "S: 550 Not user with this email.";
				       		 	OutputStream1.writeUTF(mainresponse);
				       		 	OutputStream1.flush();
				       		 	break;
				            }
		        			//Create dataoutputstream for write to buffer. In other words to send messages and to response to client
							DataOutputStream OutputStream1 = new DataOutputStream(socket.getOutputStream());
		        			mainresponse = "S: 251 User not Local; Forwarding!";
			        		OutputStream1.writeUTF(mainresponse);
			        		OutputStream1.flush();
			        		break;
		        		}
		        		
		        		else if (!message.contains(ClientNames[0]) && !message.contains(ClientNames[1]) && !message.contains(ClientNames[2]))
		        		{
		        			//Create dataoutputstream for write to buffer. In other words to send messages and to response to client
							DataOutputStream OutputStream1 = new DataOutputStream(socket.getOutputStream());
		        			mainresponse = "S: Not User with this email";
			        		OutputStream1.writeUTF(mainresponse);
			        		OutputStream1.flush();	
			        		break;
		        		}
		        		
		        		else
		        		{
		        			//Create dataoutputstream for write to buffer. In other words to send messages and to response to client
							DataOutputStream OutputStream1 = new DataOutputStream(socket.getOutputStream());
		        			mainresponse = "S: 250 OK";
			        		OutputStream1.writeUTF(mainresponse);
			        		OutputStream1.flush();
			        		break;
		        		}
		            }
		        	
		        	//Server response in 'VRFY' command of Client
		        	if (message.equals("VRFY alex") || message.equals("VRFY alexander") )
		            {
		        		//Create dataoutputstream for write to buffer. In other words to send messages and to response to client
						DataOutputStream OutputStream1 = new DataOutputStream(socket.getOutputStream());
		        		mainresponse = ClientNames[0]+"@"+ServerDomains[0];
		        		OutputStream1.writeUTF("S: The email for the user you entered is: "+mainresponse);
		        		OutputStream1.flush();	
		        		break;
		            }

		        	//Server response in 'VRFY' command of Client
		        	else if (message.equals("VRFY giorgos"))
		            {
		        		//Create dataoutputstream for write to buffer. In other words to send messages and to response to client
						DataOutputStream OutputStream1 = new DataOutputStream(socket.getOutputStream());
		            	mainresponse = ClientNames[1]+"@"+ServerDomains[1];
		        		OutputStream1.writeUTF("S: The email for the user you entered is: "+mainresponse);
		        		OutputStream1.flush();	
		        		break;
		            }

		        	//Server response in 'VRFY' command of Client
		        	else if (message.equals("VRFY dimitris"))
		            {
		        		//Create dataoutputstream for write to buffer. In other words to send messages and to response to client
						DataOutputStream OutputStream1 = new DataOutputStream(socket.getOutputStream());
		        		mainresponse = ClientNames[2]+"@"+ServerDomains[2];
		        		OutputStream1.writeUTF("S: The email for the user you entered is: "+mainresponse);
		        		OutputStream1.flush();	
		        		break;
		            }
		        	
		        	//Server response in 'VRFY' command of Client
		        	if(message.contains("VRFY"))
		        	{
			        	if (!message.equals("VRFY alex") || !message.equals("VRFY alexander") && !message.equals("VRFY giorgos") && !message.equals("VRFY dimitris"))
			        	{
			        		//Create dataoutputstream for write to buffer. In other words to send messages and to response to client
							DataOutputStream OutputStream1 = new DataOutputStream(socket.getOutputStream());
			        		mainresponse = "S: 252 Cannot VRFY this user";
			        		OutputStream1.writeUTF(mainresponse);
			        		OutputStream1.flush();
			        		break;
			        	}
		        	}
		        	
		        	//Server response in 'EXPN' command of Client
		        	if (message.contains("EXPN"))
		            {
		        		for(i=0;i<3;i++)
		    	        {
		        			//Create dataoutputstream for write to buffer. In other words to send messages and to response to client
							DataOutputStream OutputStream1 = new DataOutputStream(socket.getOutputStream());
			        		OutputStream1.writeUTF("S: "+"250 - "+ClientNames[i]+" "+"<"+ClientNames[i]+"@"+ServerDomains[i]+">");
			        		OutputStream1.flush();	
		    	        }
		            }

		        	//Server response in 'DATA' command of Client
		        	if(message.equals("DATA"+"CRLF"))
		            {
		        		//Create dataoutputstream for write to buffer. In other words to send messages and to response to client
						DataOutputStream OutputStream1 = new DataOutputStream(socket.getOutputStream());
		            	mainresponse = "S: 354 start mail input";
	            		OutputStream1.writeUTF(mainresponse);
	            		OutputStream1.flush();
	            		break;
		            }
		        	
		        	//Server response in '<CRLF>.<CRLF>' command of Client
		        	if(message.equals("<CRLF>.<CRLF>")) 
		            {
		        		//Create dataoutputstream for write to buffer. In other words to send messages and to response to client
						DataOutputStream OutputStream1 = new DataOutputStream(socket.getOutputStream());
		            	mainresponse = "S: 250 OK";
	            		OutputStream1.writeUTF(mainresponse);
	            		OutputStream1.flush();
	            		break;
		            }
		        	
		        	//Server response in 'QUIT' command of Client
		        	if(message.equals("QUIT"+"CRLF")) 
		            {
		        		//Create dataoutputstream for write to buffer. In other words to send messages and to response to client
						DataOutputStream OutputStream1 = new DataOutputStream(socket.getOutputStream());
		        		mainresponse = "S: 221 BYE BYE\nSocket with port: "+socket.getPort()+" closed";
	            		OutputStream1.writeUTF(mainresponse);
	            		OutputStream1.flush();
	            		break;
		            }
		        	
		        	//Server response in 'HELP' command of Client
		        	if (message.contains("HELP"))
		            {
		        		//Create dataoutputstream for write to buffer. In other words to send messages and to response to client
						DataOutputStream OutputStream1 = new DataOutputStream(socket.getOutputStream());
		        		mainresponse = "S: Command 'HELO' -> Use to connect with Server.\nS: Command 'MAIL From:' -> Use to define the sender email.\nS: Command 'RCPT To:' -> Use to define the recipient email.\nS: Command 'DATA' -> Use to send data to Server.\nS: Command '<CRLF>.<CRLF>' -> Use to end the data to Server.\nS: Command 'QUIT' -> Use to close the connection with Server and close the Socket.\nS: Command 'VRFY' -> Use to verify user email with his username.\nS: Command 'EXPN' -> Use to expand and show all the emails.\nS: Command 'RSET' -> Use to reset and send a new email.\nS: Command 'NOOP' -> Use to check if the server is still connected and is able to communicate with the client.\n";
			        	OutputStream1.writeUTF(mainresponse);
			        	OutputStream1.flush(); 
		            }
		        	
		        	//Server response in 'NOOP' command of Client
		        	if(message.equals("NOOP"))
		            {
		        		//Create dataoutputstream for write to buffer. In other words to send messages and to response to client
						DataOutputStream OutputStream1 = new DataOutputStream(socket.getOutputStream());
		            	mainresponse = "S: 250 OK";
	            		OutputStream1.writeUTF(mainresponse);
	            		OutputStream1.flush();
	            		break;
		            }
		        	
		        	//Server response in 'RSET' command of Client
		        	if(message.equals("RSET"))
		            {
		        		//Create dataoutputstream for write to buffer. In other words to send messages and to response to client
						DataOutputStream OutputStream1 = new DataOutputStream(socket.getOutputStream());
		            	mainresponse = "S: 250 OK";
	            		OutputStream1.writeUTF(mainresponse);
	            		OutputStream1.flush();
	            		break;
		            }
	        }	  
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
