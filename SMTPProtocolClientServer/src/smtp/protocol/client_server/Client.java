package smtp.protocol.client_server;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;
import java.net.Socket;
import java.util.Date;

/**
 *
 * @author Alexander Basho
 */
public class Client 
{
	
 //Create input object for user input	
 static Scanner scan = new Scanner(System.in); 
 
//Create global variables for using in this class
 private static int s = 4; //Define the shift for the decoder 'This must be equal with s in Decoder'
 private static String mailboxname;
 private static String senderemail;
 private static String SP = " ";
 private static final int PORT=5000;
 
 //Enter your location of this folder
 private static final String  path = System.getProperty("user.dir"); 
 
    /**
     *
     * @return the mailboxname
     */
 //Get the mailboxname (Recipient Email)
    public static String Getmailboxname()
 {
	 return Client.mailboxname;
 }
 
    /**
     *
     * @param name
     */
//Set the mailboxname (Recipient Email) - value in name in this method will be equal with mailboxname
    public static void Setmailboxname(String name)
 {
	 mailboxname = name;
 }
 
    /**
     *
     * @return the senderemail
     */
 //Get the Sender Email (MAIL From: Email)
    public static String GetSenderEmail()
 {
	 return Client.senderemail;
 }
 
    /**
     *
     * @param sender
     */
//Set the Sender Email (MAIL From: Email)
    public static void SetSenderEmail(String sender)
 {
	 senderemail = sender;
 }
 
    /**
     *
     * @param args
     * @throws UnknownHostException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws UnknownHostException, IOException,NoSuchAlgorithmException, InvalidKeySpecException, FileNotFoundException
 {
	
	 //Set the server IP
	 String serverIP = "localhost";
	 String username; //Create variable to save username string
	 String password; //Create variable to save password string
	 int third = 0 ; //Initialize variable for use after in a while loop
	 
	 //Create Decoder object
	 CaesarEncoderDecoder encrypt = new CaesarEncoderDecoder();
	 
	 //Create object from SaltAndHashPassword to hash and salt the passwords
	 SaltAndHashPassword hashedsaltedpassword = new SaltAndHashPassword();
	 
	 //Create Authentication object
	 Authentication authentication = new Authentication();

	 //Create arrays for ClientNames and ServerDomains
	 String ClientNames[] = {"alexanderbasho","giorgosalexopoulos","dimitrisgiannopoulos"}; 
	 String ServerDomains[] = {"basho.com","hotmail.com","gmail.com"};
	 //First User = alexanderbasho@basho.com
	 //Second User = giorgosalexopoulos@hotmail.com
	 //Third User = dimitrisgiannopoulos@gmail.com	 
	
 try
 {
	 System.out.println("Examples -> Server users:\n//First User: Username: alexanderbasho Password: alex1998\n//Second User: Username: giorgosalexopoulos Password: giorgos1998\n//Third User: Username: dimitrisgiannopoulos Password: dimitris1998\n You can write any username-password.");
	 //Client input his username and password
	 System.out.println("C: Enter username: ");
	 username = scan.nextLine(); 
	 System.out.println("C: Enter password: ");
	 password = scan.nextLine();
         //Create Client Socket
	 Socket socket = new Socket(serverIP,PORT);

	 //Create dataoutputstream to send messages and data to Server
	 DataOutputStream OutputStream = new DataOutputStream(socket.getOutputStream());
	 
         //Send username and password to Server
         OutputStream.writeUTF(username);
	 OutputStream.flush();
	 OutputStream.writeUTF(password);
	 OutputStream.flush();

	 //Create writer to send messages to Server, buffered reader to take input from user and datainputstream to read data from buffer (Server responses-messages)
       	 PrintWriter printWriter = new PrintWriter(socket.getOutputStream(),true);
	 BufferedReader bufferedReader = new java.io.BufferedReader(new InputStreamReader(System.in));
	 DataInputStream dataIn = new DataInputStream(socket.getInputStream());

	 
	 
 System.out.println("C: Client "+ username + "("+socket+")" + " Connected");
 String Input = null;
 Scanner scan = new Scanner(System.in);
 System.out.println(dataIn.readUTF());
 System.out.println("\nC: 1) Send Email\nC: 2) Delete all emails of all users\nC: 3) Delete all emails from user who are loged in\nC: 4) Read all emails from user who are loged in\nC: 5) Send Quit\n");
 
 while(true) //All messages that Cliend send to Server are encrypted.
 {
	//Select specified commands where are connected with specified numbers
	 int first = scan.nextInt();
	 //Check if the inserted number is correct. If true then continue or if false try again
	 while(first != 1 && first != 2 && first != 3  && first != 4  && first != 5)
	 {
	 	System.out.println("C: Not a correct number");
	 	first = scan.nextInt();
	 }
	 while(first == 1 || first == 2 || first == 3 || first == 4 || first == 5)
	 {
             rset:
		 if(first == 1)
		 {
			 System.out.println("\nC: 0) Send EHLO\nC: 1) Send HELO\nC: 2) Send RSET\nC: 3) Send NOOP\nC: 4) Send VRFY\nC: 5) Send EXPN\nC: 6) Send HELP\nC: 7) Send QUIT\n");
			//Select specified commands where are connected with specified numbers
			 int second = scan.nextInt();
			//Check if the inserted number is correct. If true then continue or if false try again
		 while(second != 0 && second != 1 && second != 2 && second != 3 && second != 4 && second != 5 && second != 6 && second != 7)
		 {
		 	System.out.println("C: Not a correct number");
		 	second = scan.nextInt();
		 }
		 while(second == 0 || second == 1 || second == 2 || second == 3 || second == 4|| second== 5 || second == 6 || second == 7 || second == 8)	
		 {	 
			//Select specified commands where are connected with specified numbers
		 if(second == 0 || second == 1)
			 {
				//Client send 'EHLO' command on the Server
				 if(username.equals(ClientNames[0]) && second == 1)
				 {
					 printWriter.println(encrypt.Encoder("HELO"+SP+ServerDomains[0]+"CRLF", s));
					 System.out.println(dataIn.readUTF());
					 System.out.println("\nC: 1) Send MAIL From Email\nC: 2) Send RSET\nC: 3) Send NOOP\nC: 4) Send VRFY\nC: 5) Send EXPN\nC: 6) Send HELP\nC: 7) Send QUIT\n");
					 third = scan.nextInt();
				 }
                                 
                                 else if(username.equals(username) && second == 1)
				 {
					 printWriter.println(encrypt.Encoder("HELO"+SP+username+"CRLF", s));
					 System.out.println(dataIn.readUTF());
					 System.out.println("\nC: 1) Send MAIL From Email\nC: 2) Send RSET\nC: 3) Send NOOP\nC: 4) Send VRFY\nC: 5) Send EXPN\nC: 6) Send HELP\nC: 7) Send QUIT\n");
					 third = scan.nextInt();
				 }
                                 
				 else if(username.equals(ClientNames[1]) && second == 1)
				 {
					 printWriter.println(encrypt.Encoder("HELO"+SP+ServerDomains[1]+"CRLF", s));
					 System.out.println(dataIn.readUTF());
					 System.out.println("\nC: 1) Send MAIL From Email\nC: 2) Send RSET\nC: 3) Send NOOP\nC: 4) Send VRFY\nC: 5) Send EXPN\nC: 6) Send HELP\nC: 7) Send QUIT\n");
					 third = scan.nextInt();
				 }
				 else if(username.equals(ClientNames[2]) && second == 1)
				 {
					 printWriter.println(encrypt.Encoder("HELO"+SP+ServerDomains[2]+"CRLF", s));
					 System.out.println(dataIn.readUTF());
					 System.out.println("\nC: 1) Send MAIL From Email\nC: 2) Send RSET\nC: 3) Send NOOP\nC: 4) Send VRFY\nC: 5) Send EXPN\nC: 6) Send HELP\nC: 7) Send QUIT\n");
					 third = scan.nextInt();
				 }
				 
				 if(username.equals(ClientNames[0]) && second == 0)
				 {
					 printWriter.println(encrypt.Encoder("EHLO"+SP+ServerDomains[0]+"CRLF", s));
					 System.out.println(dataIn.readUTF());
					 System.out.println("\nC: 1) Send MAIL From Email\nC: 2) Send RSET\nC: 3) Send NOOP\nC: 4) Send VRFY\nC: 5) Send EXPN\nC: 6) Send HELP\nC: 7) Send QUIT\n");
					 third = scan.nextInt();
				 }
                                 else if(username.equals(username) && second == 0)
				 {
					 printWriter.println(encrypt.Encoder("EHLO"+SP+username+"CRLF", s));
					 System.out.println(dataIn.readUTF());
					 System.out.println("\nC: 1) Send MAIL From Email\nC: 2) Send RSET\nC: 3) Send NOOP\nC: 4) Send VRFY\nC: 5) Send EXPN\nC: 6) Send HELP\nC: 7) Send QUIT\n");
					 third = scan.nextInt();
				 }
				 else if(username.equals(ClientNames[1]) && second == 0)
				 {
					 printWriter.println(encrypt.Encoder("EHLO"+SP+ServerDomains[1]+"CRLF", s));
					 System.out.println(dataIn.readUTF());
					 System.out.println("\nC: 1) Send MAIL From Email\nC: 2) Send RSET\nC: 3) Send NOOP\nC: 4) Send VRFY\nC: 5) Send EXPN\nC: 6) Send HELP\nC: 7) Send QUIT\n");
					 third = scan.nextInt();
				 }
				 else if(username.equals(ClientNames[2]) && second == 0)
				 {
					 printWriter.println(encrypt.Encoder("EHLO"+SP+ServerDomains[2]+"CRLF", s));
					 System.out.println(dataIn.readUTF());
					 System.out.println("\nC: 1) Send MAIL From Email\nC: 2) Send RSET\nC: 3) Send NOOP\nC: 4) Send VRFY\nC: 5) Send EXPN\nC: 6) Send HELP\nC: 7) Send QUIT\n");
					 third = scan.nextInt();
				 }
				//Check if the inserted number is correct. If true then continue or if false try again				
				 while(third != 1 && third != 2 && third != 3 && third != 4 && third != 5 && third != 6 && third != 7)
				 {
				 	System.out.println("C: Not a correct number");
				 	third = scan.nextInt();
				 }
				 while(third == 1 || third == 2 || third == 3 || third == 4 || third == 5 || third == 6 || third == 7 || third == 8)	
				 {
				 if(third == 1)
				 {
					 //Client send 'MAIL From:' command on the Server
                     System.out.println("Examples -> Server Users : \n//First User = alexanderbasho@basho.com\n//Second User = giorgosalexopoulos@hotmail.com\n//Third User = dimitrisgiannopoulos@gmail.com\n You can write any email\n");
                     System.out.println("C: Enter your email"+"\n"+"MAIL From: ");
                     String from = bufferedReader.readLine();
					 printWriter.println(encrypt.Encoder("MAIL"+SP+"From:"+from+"CRLF", s));
					 //Set the Sender Email
					 SetSenderEmail(from);
					 System.out.println(dataIn.readUTF());
					 System.out.println("\nC: 1) Send RCPT To Email\nC: 2) Send RSET\nC: 3) Send NOOP\nC: 4) Send VRFY\nC: 5) Send EXPN\nC: 6) Send HELP\nC: 7) Send QUIT\n");
					 //Select specified commands where are connected with specified numbers
					 int fourth = scan.nextInt();
					 //Check if the inserted number is correct. If true then continue or if false try again
					 while(fourth != 1 && fourth != 2 && fourth != 3 && fourth != 4 && fourth != 5 && fourth != 6 && fourth != 7)
					 {
					 	System.out.println("C: Not a correct number");
					 	fourth = scan.nextInt();
					 }
					 while(fourth == 1 || fourth == 2 || fourth == 3 || fourth == 4|| fourth == 5 || fourth == 6 || fourth == 7 || fourth == 8)	
					 {
					 if(fourth == 1)
					 {
						 //Client send 'RCPT To:' command on the Server
                         System.out.println("Examples -> Server Users : \n//First User = alexanderbasho@basho.com\n//Second User = giorgosalexopoulos@hotmail.com\n//Third User = dimitrisgiannopoulos@gmail.com\n You can write any email.\n");
                         System.out.println("C: Enter recipient email"+"\n"+"RCPT To: ");
                         String rcpt = bufferedReader.readLine();
						 printWriter.println(encrypt.Encoder("RCPT"+SP+"To:"+rcpt+"CRLF", s));
						 //Set the mailboxname (Recipient Email)
						 Setmailboxname(rcpt);
						 File file = null;  // initialize file variable
						 //when sender and recipient is a server user
						 if((Getmailboxname().equals("alexanderbasho@basho.com") || Getmailboxname().equals("giorgosalexopoulos@hotmail.com") || Getmailboxname().equals("dimitrisgiannopoulos@gmail.com")) && (GetSenderEmail().equals("alexanderbasho@basho.com") || GetSenderEmail().equals("giorgosalexopoulos@hotmail.com") || GetSenderEmail().equals("dimitrisgiannopoulos@gmail.com")))
						 {
							 file = new File(path+"\\Mailbox\\"+GetSenderEmail()+"\\"+Getmailboxname()+".txt");
						 }
						 //when sender and recipient is not a server user
						 else if((!Getmailboxname().equals("alexanderbasho@basho.com") && !GetSenderEmail().equals("alexanderbasho@basho.com")) && (!Getmailboxname().equals("giorgosalexopoulos@hotmail.com") && !GetSenderEmail().equals("giorgosalexopoulos@hotmail.com")) && (!Getmailboxname().equals("dimitrisgiannopoulos@gmail.com") && !GetSenderEmail().equals("dimitrisgiannopoulos@gmail.com")))
						 {
							 file = new File(path+"\\"+".txt");
						 } 
						//when sender is a server user and recipient is not a server user
						 else if((!Getmailboxname().equals("alexanderbasho@basho.com") || !Getmailboxname().equals("giorgosalexopoulos@hotmail.com") || !Getmailboxname().equals("dimitrisgiannopoulos@gmail.com")) && (GetSenderEmail().equals("alexanderbasho@basho.com") || GetSenderEmail().equals("giorgosalexopoulos@hotmail.com") || GetSenderEmail().equals("dimitrisgiannopoulos@gmail.com")))
						 {
							 file = new File(path+"\\Mailbox\\"+GetSenderEmail()+"\\"+Getmailboxname()+".txt");
						 }
						 //when recipient is a server user and sender is not a server user
						 else if((Getmailboxname().equals("alexanderbasho@basho.com") || Getmailboxname().equals("giorgosalexopoulos@hotmail.com") || Getmailboxname().equals("dimitrisgiannopoulos@gmail.com")) && (!GetSenderEmail().equals("alexanderbasho@basho.com") || !GetSenderEmail().equals("giorgosalexopoulos@hotmail.com") || !GetSenderEmail().equals("dimitrisgiannopoulos@gmail.com")))
						 {
							 file = new File(path+"\\Mailbox\\"+Getmailboxname()+"\\"+Getmailboxname()+".txt");
						 }
						 
						     file.setWritable(true);
							 file.setReadable(true);
							 file.getAbsolutePath();
							 FileWriter writer;
							 writer = new FileWriter(file,true);
							 BufferedWriter write = new BufferedWriter(writer);
							 Date date = new Date();
							 write.write("Date: "+date);
							 write.newLine();
							 write.write("MAIL From: "+from);
							 write.newLine();
							 write.write("RCPT To: "+rcpt);
							 write.newLine();
							 
						 System.out.println(dataIn.readUTF()); //Print server response
						 System.out.println("\nC: 1) Send DATA\nC: 2) Send RSET\nC: 3) Send NOOP\nC: 4) Send VRFY\nC: 5) Send EXPN\nC: 6) Send HELP\nC: 7) Send QUIT\n");
						 //Select specified commands where are connected with specified numbers
						 int fifth = scan.nextInt();
						 //Check if the inserted number is correct. If true then continue or if false try again
						 while(fifth != 1 && fifth != 2 && fifth != 3 && fifth != 4 && fifth != 5 && fifth != 6 && fifth != 7)
						 {
						 	System.out.println("C: Not a correct number");
						 	fifth = scan.nextInt();
						 }
						 while(fifth == 1 || fifth == 2 || fifth == 3 || fifth == 4|| fifth == 5 || fifth == 6 || fifth == 7 || fifth == 8)	
						 {
						 if(fifth == 1)
						 {
							 //Client send 'DATA' command on the Server
							 printWriter.println(encrypt.Encoder("DATA"+"CRLF", s));
							 System.out.println(dataIn.readUTF());
							 System.out.println("Enter mail Subject: ");
							 Input = bufferedReader.readLine();
							 write.write("Subject: "+Input);
							 write.newLine();
							 System.out.println("Write your message. (End data in a new line with <CRLF>.<CRLF>)");
							 write.write("Message:\n");
						 while(!Input.equals("<CRLF>.<CRLF>"))
						 {
							 //Client send '<CRLF>.<CRLF>' command on the Server
							 Input = bufferedReader.readLine();
						 if (Input.equals("<CRLF>.<CRLF>"))
						 {
							 write.write("\n");
						 }
						 else
						 {
							 write.write(Input);
							 write.newLine();
						 }
						 }
						 printWriter.println(encrypt.Encoder(Input, s));
						 System.out.println(dataIn.readUTF());
						 System.out.println("\nC: 1) Send RSET\nC: 2) Send NOOP\nC: 3) Send VRFY\nC: 4) Send EXPN\nC: 5) Send HELP\nC: 6) Send the email and QUIT\n");
						 //Select specified commands where are connected with specified numbers
						 int sixth = scan.nextInt();
						 //Check if the inserted number is correct. If true then continue or if false try again
						 while(sixth != 1 && sixth != 2 && sixth != 3 && sixth != 4 && sixth != 5 && sixth != 6)
						 {
						 	System.out.println("C: Not a correct number");
						 	sixth = scan.nextInt();
						 }
						while(sixth == 1 || sixth == 2 || sixth == 3 || sixth == 4 || sixth == 5 ||sixth == 6 || sixth == 7 || sixth == 8)	
						{
						 if( sixth == 1)
							 {
							 	 //Client send 'RSET' command on the Server
								 printWriter.println(encrypt.Encoder("RSET", s));
								 System.out.println(dataIn.readUTF());
								 write.close();
								 Files.deleteIfExists(Paths.get(path+"\\Mailbox\\"+GetSenderEmail()+"\\"+Getmailboxname()+".txt"));
								 first=1;
								 break rset;
							 }
							 else if(sixth == 6)
							 {
								//Client send 'QUIT' command on the Server
								 printWriter.println(encrypt.Encoder("QUIT"+"CRLF", s));
								 System.out.println(dataIn.readUTF());
								 write.close();
								 socket.close();
								 break;
							 }
							 else if(sixth == 3) 
							 {
								 //Client send 'VRFY' command on the Server
								 //Enter username to verify
								 System.out.println("C: Enter username:");
								 Input = bufferedReader.readLine();
								 printWriter.println(encrypt.Encoder("VRFY "+Input, s));
								 System.out.println(dataIn.readUTF());
								 System.out.println("\nC: 1) Send RSET\nC: 2) Send NOOP\nC: 3) Send VRFY\nC: 4) Send EXPN\nC: 5) Send HELP\nC: 6) Send the email and QUIT\n");
								 sixth = scan.nextInt();
							 }
							 else if(sixth == 4)
							 {
								 //Client send 'EXPN' command on the Server
								 printWriter.println(encrypt.Encoder("EXPN", s));
								 for(int j =0; j<3;j++)
								 {
									 System.out.println(dataIn.readUTF());
								 }
								 System.out.println("\nC: 1) Send RSET\nC: 2) Send NOOP\nC: 3) Send VRFY\nC: 4) Send EXPN\nC: 5) Send HELP\nC: 6) Send the email and QUIT\n");
								 sixth = scan.nextInt();
							 } 
							 else if(sixth == 5)
							 {
								 //Client send 'HELP' command on the Server
								 printWriter.println(encrypt.Encoder("HELP", s));
								 System.out.println(dataIn.readUTF());
								 System.out.println("\nC: 1) Send RSET\nC: 2) Send NOOP\nC: 3) Send VRFY\nC: 4) Send EXPN\nC: 5) Send HELP\nC: 6) Send the email and QUIT\n");
								 sixth = scan.nextInt();
							 }
							 else if(sixth == 2)
							 {
								 //Client send 'NOOP' command on the Server
								 printWriter.println(encrypt.Encoder("NOOP", s));
								 System.out.println(dataIn.readUTF());
								 System.out.println("\nC: 1) Send RSET\nC: 2) Send NOOP\nC: 3) Send VRFY\nC: 4) Send EXPN\nC: 5) Send HELP\nC: 6) Send the email and QUIT\n");
								 sixth = scan.nextInt();
							 }
						 }
						 }
						 else if(fifth == 2)
						 {
							 printWriter.println(encrypt.Encoder("RSET", s));
							 System.out.println(dataIn.readUTF());
							 write.close();
							 Files.deleteIfExists(Paths.get(path+"\\Mailbox\\"+GetSenderEmail()+"\\"+Getmailboxname()+".txt"));
							 first=1;
							 break rset;
						 }
						 else if(fifth == 7)
						 {
							 printWriter.println(encrypt.Encoder("QUIT"+"CRLF", s));
							 System.out.println(dataIn.readUTF());
							 socket.close();
							 break;
						 }
						 else if(fifth == 4) 
						 {
							 System.out.println("C: Enter username:");
							 Input = bufferedReader.readLine();
							 printWriter.println(encrypt.Encoder("VRFY "+Input, s));
							 System.out.println(dataIn.readUTF());
							 System.out.println("\nC: 1) Send DATA\nC: 2) Send RSET\nC: 3) Send NOOP\nC: 4) Send VRFY\nC: 5) Send EXPN\nC: 6) Send HELP\nC: 7) Send QUIT\n");
							 fifth = scan.nextInt();
						 }
						 else if(fifth == 5)
						 {
							 printWriter.println(encrypt.Encoder("EXPN", s));
							 for(int j =0; j<3;j++)
							 {
								 System.out.println(dataIn.readUTF());
							 }
							 System.out.println("\nC: 1) Send DATA\nC: 2) Send RSET\nC: 3) Send NOOP\nC: 4) Send VRFY\nC: 5) Send EXPN\nC: 6) Send HELP\nC: 7) Send QUIT\n");
							 fifth = scan.nextInt();
						 } 
						 else if(fifth == 6)
						 {
							 printWriter.println(encrypt.Encoder("HELP", s));
							 System.out.println(dataIn.readUTF());
							 System.out.println("\nC: 1) Send DATA\nC: 2) Send RSET\nC: 3) Send NOOP\nC: 4) Send VRFY\nC: 5) Send EXPN\nC: 6) Send HELP\nC: 7) Send QUIT\n");
							 fifth = scan.nextInt();
						 }
						 
						 else if(fifth == 3)
						 {
							 printWriter.println(encrypt.Encoder("NOOP", s));
							 System.out.println(dataIn.readUTF());
							 System.out.println("\nC: 1) Send DATA\nC: 2) Send RSET\nC: 3) Send NOOP\nC: 4) Send VRFY\nC: 5) Send EXPN\nC: 6) Send HELP\nC: 7) Send QUIT\n");
							 fifth = scan.nextInt();
						 }
					 } 
					 } 
				else if(fourth == 2)
				 {
					 printWriter.println(encrypt.Encoder("RSET", s));
					 System.out.println(dataIn.readUTF());
					 Files.deleteIfExists(Paths.get(path+"\\Mailbox\\"+GetSenderEmail()+"\\"+Getmailboxname()+".txt"));
					 first=1;
					 break rset;
				 }
				 else if(fourth == 7)
				 {
					 printWriter.println(encrypt.Encoder("QUIT"+"CRLF", s));
					 System.out.println(dataIn.readUTF());
					 socket.close();
					 break;
				 }
				 else if(fourth == 4) 
				 {
					 System.out.println("C: Enter username:");
					 Input = bufferedReader.readLine();
					 printWriter.println(encrypt.Encoder("VRFY "+Input, s));
					 System.out.println(dataIn.readUTF());
					 System.out.println("\nC: 1) Send RCPT To Email\nC: 2) Send RSET\nC: 3) Send NOOP\nC: 4) Send VRFY\nC: 5) Send EXPN\nC: 6) Send HELP\nC: 7) Send QUIT\n");
					 fourth = scan.nextInt();
				 }
				 else if(fourth == 5)
				 {
					 printWriter.println(encrypt.Encoder("EXPN", s));
					 for(int i =0; i<3;i++)
					 {
					 System.out.println(dataIn.readUTF());
					 }
					 System.out.println("\nC: 1) Send RCPT To Email\nC: 2) Send RSET\nC: 3) Send NOOP\nC: 4) Send VRFY\nC: 5) Send EXPN\nC: 6) Send HELP\nC: 7) Send QUIT\n");
					 fourth = scan.nextInt();
				 } 
				 else if(fourth == 6)
				 {
					 printWriter.println(encrypt.Encoder("HELP", s));
					 System.out.println(dataIn.readUTF());
					 System.out.println("\nC: 1) Send RCPT To Email\nC: 2) Send RSET\nC: 3) Send NOOP\nC: 4) Send VRFY\nC: 5) Send EXPN\nC: 6) Send HELP\nC: 7) Send QUIT\n");
					 fourth = scan.nextInt();
				 }
				 
				 else if(fourth == 3)
				 {
					 printWriter.println(encrypt.Encoder("NOOP", s));
					 System.out.println(dataIn.readUTF());
					 System.out.println("\nC: 1) Send RCPT To Email\nC: 2) Send RSET\nC: 3) Send NOOP\nC: 4) Send VRFY\nC: 5) Send EXPN\nC: 6) Send HELP\nC: 7) Send QUIT\n");
					 fourth = scan.nextInt();
				 }
				 }
				 } 
			 else if(third == 2)
			 {
				 printWriter.println(encrypt.Encoder("RSET", s));
				 System.out.println(dataIn.readUTF());
				 Files.deleteIfExists(Paths.get(path+"\\Mailbox\\"+GetSenderEmail()+"\\"+Getmailboxname()+".txt"));
				 first=1;
				 break rset;
			 }
			 else if(third == 7)
			 {
				 printWriter.println(encrypt.Encoder("QUIT"+"CRLF", s));
				 System.out.println(dataIn.readUTF()); 
				 socket.close();
				 break;
			 }
			 else if(third == 4) 
			 {
				 System.out.println("C: Enter username:");
				 Input = bufferedReader.readLine();
				 printWriter.println(encrypt.Encoder("VRFY "+Input, s));
				 System.out.println(dataIn.readUTF());
				 System.out.println("\nC: 1) Send MAIL From Email\nC: 2) Send RSET\nC: 3) Send NOOP\nC: 4) Send VRFY\nC: 5) Send EXPN\nC: 6) Send HELP\nC: 7) Send QUIT\n");
				 third = scan.nextInt();
			 }
			 else if(third == 5)
			 {
				 printWriter.println(encrypt.Encoder("EXPN", s));
				 for(int i =0; i<3;i++)
				 {
				 System.out.println(dataIn.readUTF());
				 }
				 System.out.println("\nC: 1) Send MAIL From Email\nC: 2) Send RSET\nC: 3) Send NOOP\nC: 4) Send VRFY\nC: 5) Send EXPN\nC: 6) Send HELP\nC: 7) Send QUIT\n");
				 third = scan.nextInt();
			 } 
			 else if(third == 6)
			 {
				 printWriter.println(encrypt.Encoder("HELP", s));
				 System.out.println(dataIn.readUTF());
				 System.out.println("\nC: 1) Send MAIL From Email\nC: 2) Send RSET\nC: 3) Send NOOP\nC: 4) Send VRFY\nC: 5) Send EXPN\nC: 6) Send HELP\nC: 7) Send QUIT\n");
				 third = scan.nextInt();
			 }
			 else if(third == 3)
			 {
				 printWriter.println(encrypt.Encoder("NOOP", s));
				 System.out.println(dataIn.readUTF());
				 System.out.println("\nC: 1) Send MAIL From Email\nC: 2) Send RSET\nC: 3) Send NOOP\nC: 4) Send VRFY\nC: 5) Send EXPN\nC: 6) Send HELP\nC: 7) Send QUIT\n");
				 third = scan.nextInt();
			 }
			 }
			 }	
		 else if(second == 2)
		 {
			 printWriter.println(encrypt.Encoder("RSET", s));
			 System.out.println(dataIn.readUTF());
			 Files.deleteIfExists(Paths.get(path+"\\Mailbox\\"+GetSenderEmail()+"\\"+Getmailboxname()+".txt"));
			 first=1;
			 break rset;
		 }
		 else if(second == 7)
		 {
			 printWriter.println(encrypt.Encoder("QUIT"+"CRLF", s));
			 System.out.println(dataIn.readUTF()); 
			 socket.close();
			 break;
		 }
		 else if(second == 4) 
		 {
			 System.out.println("C: Enter username:");
			 Input = bufferedReader.readLine();
			 printWriter.println(encrypt.Encoder("VRFY "+Input, s));
			 System.out.println(dataIn.readUTF());
			 System.out.println("\nC: 1) Send HELO\nC: 2) Send RSET\nC: 3) Send NOOP\nC: 4) Send VRFY\nC: 5) Send EXPN\nC: 6) Send HELP\nC: 7) Send QUIT\n");
			 second = scan.nextInt();
		 }
		 else if(second == 5)
		 {
			 printWriter.println(encrypt.Encoder("EXPN", s));
			 for(int i =0; i<3;i++)
			 {
			 System.out.println(dataIn.readUTF());
			 }
			 System.out.println("\nC: 1) Send HELO\nC: 2) Send RSET\nC: 3) Send NOOP\nC: 4) Send VRFY\nC: 5) Send EXPN\nC: 6) Send HELP\nC: 7) Send QUIT\n");
			 second = scan.nextInt();
		 } 
		 else if(second == 6)
		 {
			 printWriter.println(encrypt.Encoder("HELP", s));
			 System.out.println(dataIn.readUTF());
			 System.out.println("\nC: 1) Send HELO\nC: 2) Send RSET\nC: 3) Send NOOP\nC: 4) Send VRFY\nC: 5) Send EXPN\nC: 6) Send HELP\nC: 7) Send QUIT\n");
			 second = scan.nextInt();
		 }
		 else if(second == 3)
		 {
			 printWriter.println(encrypt.Encoder("NOOP", s));
			 System.out.println(dataIn.readUTF());
			 System.out.println("\nC: 1) Send HELO\nC: 2) Send RSET\nC: 3) Send NOOP\nC: 4) Send VRFY\nC: 5) Send EXPN\nC: 6) Send HELP\nC: 7) Send QUIT\n");
			 second = scan.nextInt();
		 }
		 }
		 }
	 out:
 if(first == 2 || first == 3 || first == 4)
 {
	//Save username and password in Authentication method
	 authentication.SetUsernameAndPassword(username, password);

		 OutputStream.writeUTF(authentication.GetUsername());
		 OutputStream.flush();
		 OutputStream.writeUTF(authentication.GetPassword());
		 OutputStream.flush();
	      
		 //Authentication
		 if(authentication.Authentication() == false)
		 {
			 socket.close();
			 return;
		 }
	        
		 //Create an saltedandhashed password for password of user
		 String generatedSecuredPasswordHash = hashedsaltedpassword.generateStorngPasswordHash(password);
		 
		 //System.out.println(generatedSecuredPasswordHash); //Display salted and hashed passwords
		 encrypt.Encoder(generatedSecuredPasswordHash, s);
         while(authentication.GetUsername().equals("alexanderbasho") && authentication.GetPassword().equals("alex1998") || authentication.GetUsername().equals("dimitrisgiannopoulos") && authentication.GetPassword().equals("dimitris1998") || authentication.GetUsername().equals("giorgosalexopoulos") && authentication.GetPassword().equals("giorgos1998"))
         {

 //Delete all mails from the mailbox of user who are log in
 if(first == 2)
 {
	 Files.walk(Paths.get(path+"\\Mailbox\\"))
    .filter(Files::isRegularFile)
    .map(Path::toFile)
    .forEach(File::delete);
	System.out.println("C: All emails deleted successfully\n");
	System.out.println("\nC: 1) Send Email\nC: 2) Delete all emails of all users\nC: 3) Delete all emails from user who are loged in\nC: 4) Read all emails from user who are loged in\nC: 5) Send Quit\n");
	first = scan.nextInt(); 
	if(first==1 || first ==5)
	{
		break out;
	}
 } 
	 
 else if(first == 3)
 {
	 if(username.equals(ClientNames[0]))
	 {
		 Files.walk(Paths.get(path+"\\Mailbox\\alexanderbasho@basho.com\\"))
		    .filter(Files::isRegularFile)
		    .map(Path::toFile)
		    .forEach(File::delete);
		 System.out.println("C: All emails from "+ClientNames[0]+" deleted successfully\n");
		 System.out.println("\nC: 1) Send Email\nC: 2) Delete all emails of all users\nC: 3) Delete all emails from user who are loged in\nC: 4) Read all emails from user who are loged in\nC: 5) Send Quit\n");
		 first = scan.nextInt(); 
		 if(first==1 || first ==5)
			{
				break out;
			}
		 break;
	 }
	 else if(username.equals(ClientNames[1]))
	 {
		 Files.walk(Paths.get(path+"\\Mailbox\\giorgosalexopoulos@hotmail.com\\"))
		    .filter(Files::isRegularFile)
		    .map(Path::toFile)
		    .forEach(File::delete);
		 System.out.println("C: All emails from "+ClientNames[1]+" deleted successfully\n");
		 System.out.println("\nC: 1) Send Email\nC: 2) Delete all emails of all users\nC: 3) Delete all emails from user who are loged in\nC: 4) Read all emails from user who are loged in\nC: 5) Send Quit\n"); 
		 first = scan.nextInt(); 
		 if(first==1 || first ==5)
			{
				break out;
			}
		 break;
	 }
	 else if(username.equals(ClientNames[2]))
	 {
		 Files.walk(Paths.get(path+"\\Mailbox\\dimitrisgiannopoulos@gmail.com\\"))
		    .filter(Files::isRegularFile)
		    .map(Path::toFile)
		    .forEach(File::delete);
		 System.out.println("C: All emails from "+ClientNames[2]+" deleted successfully\n");
		 System.out.println("\nC: 1) Send Email\nC: 2) Delete all emails of all users\nC: 3) Delete all emails from user who are loged in\nC: 4) Read all emails from user who are loged in\nC: 5) Send Quit\n");
		 first = scan.nextInt(); 
		 if(first==1 || first ==5)
			{
				break out;
			}
		 break;
	 }
 }
	 
 else if(first == 4)
 {
	 if(username.equals(ClientNames[0]))
	 {
		 File file = new File (path+"\\Mailbox\\alexanderbasho@basho.com\\dimitrisgiannopoulos@gmail.com.txt");
		 File file1 = new File (path+"\\Mailbox\\alexanderbasho@basho.com\\alexanderbasho@basho.com.txt");
		 File file2 = new File (path+"\\Mailbox\\alexanderbasho@basho.com\\giorgosalexopoulos@hotmail.com.txt");
			Scanner scanner = new Scanner (file);
			Scanner scanner1 = new Scanner (file1);
			Scanner scanner2 = new Scanner (file2);
			while(scan.hasNextLine())
			{
			  System.out.println(scanner.nextLine());
			  if(!scanner.hasNextLine())
				{
				break;
				}
			}
			while(scan.hasNextLine())
			{
			  System.out.println(scanner1.nextLine());
			  if(!scanner1.hasNextLine())
				{
				break;
				}
			}
			while(scanner2.hasNextLine())
			{
			  System.out.println(scanner2.nextLine());
			  if(!scanner2.hasNextLine())
				{
				break;
				}
			}
			System.out.println("\nC: 1) Send Email\nC: 2) Delete all emails of all users\nC: 3) Delete all emails from user who are loged in\nC: 4) Read all emails from user who are loged in\nC: 5) Send Quit\n");
			scanner2.close();
			scanner1.close();
			scanner.close();
			first = scan.nextInt();   
			if(first==1 || first ==5)
			{
				break out;
			}
	 }
	 
	 if(username.equals(ClientNames[1]))
	 {
		 File file = new File (path+"\\Mailbox\\giorgosalexopoulos@hotmail.com\\dimitrisgiannopoulos@gmail.com.txt");
		 File file1 = new File (path+"\\Mailbox\\giorgosalexopoulos@hotmail.com\\alexanderbasho@basho.com.txt");
		 File file2 = new File (path+"\\Mailbox\\giorgosalexopoulos@hotmail.com\\giorgosalexopoulos@hotmail.com.txt");
			Scanner scanner = new Scanner (file);
			Scanner scanner1 = new Scanner (file1);
			Scanner scanner2 = new Scanner (file2);
			while(scan.hasNextLine())
			{
			  System.out.println(scanner.nextLine());
			  if(!scanner.hasNextLine())
				{
				break;
				}
			}
			while(scan.hasNextLine())
			{
			  System.out.println(scanner1.nextLine());
			  if(!scanner1.hasNextLine())
				{
				break;
				}
			}
			while(scanner2.hasNextLine())
			{
			  System.out.println(scanner2.nextLine());
			  if(!scanner2.hasNextLine())
				{
				break;
				}
			}
			System.out.println("\nC: 1) Send Email\nC: 2) Delete all emails of all users\nC: 3) Delete all emails from user who are loged in\nC: 4) Read all emails from user who are loged in\nC: 5) Send Quit\n");
			scanner2.close();
			scanner1.close();
			scanner.close();
			first = scan.nextInt(); 
			if(first==1 || first ==5)
			{
				break out;
			}
	 }
	
	 if(username.equals(ClientNames[2]))
	 {
		 File file = new File (path+"\\Mailbox\\dimitrisgiannopoulos@gmail.com\\dimitrisgiannopoulos@gmail.com.txt");
		 File file1 = new File (path+"\\Mailbox\\dimitrisgiannopoulos@gmail.com\\alexanderbasho@basho.com.txt");
		 File file2 = new File (path+"\\Mailbox\\dimitrisgiannopoulos@gmail.com\\giorgosalexopoulos@hotmail.com.txt");
			Scanner scanner = new Scanner (file);
			Scanner scanner1 = new Scanner (file1);
			Scanner scanner2 = new Scanner (file2);
			while(scan.hasNextLine())
			{
			  System.out.println(scanner.nextLine());
			  if(!scanner.hasNextLine())
				{
				break;
				}
			}
			while(scan.hasNextLine())
			{
			  System.out.println(scanner1.nextLine());
			  if(!scanner1.hasNextLine())
				{
				break;
				}
			}
			while(scanner2.hasNextLine())
			{
			  System.out.println(scanner2.nextLine());
			  if(!scanner2.hasNextLine())
				{
				break;
				}
			}
			System.out.println("\nC: 1) Send Email\nC: 2) Delete all emails of all users\nC: 3) Delete all emails from user who are loged in\nC: 4) Read all emails from user who are loged in\nC: 5) Send Quit\n");
			scanner2.close();
			scanner1.close();
			scanner.close();
			first = scan.nextInt(); 
			if(first==1 || first ==5)
			{
				break out;
			}
	 }
         }	
         }
 }
//Client send 'QUIT' command on the Server and close the program
 if(first == 5)
 {
	 printWriter.println(encrypt.Encoder("QUIT"+"CRLF", s));
	 socket.close();
	 System.out.println(dataIn.readUTF());
	 break;
 }
 }
 }
 }
 catch (Exception e)
 {
 System.out.println("C: Error : " + e.getMessage());
 }
}
}