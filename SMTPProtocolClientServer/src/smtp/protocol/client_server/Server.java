package smtp.protocol.client_server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Alexander Basho
 */
public class Server 
{
 
    /**
     * Define the port of the Server
     */
    public static final int PORT=5000; 
	
    /**
     *
     * @param args
     * @throws IOException
     */
    //Synchronize sockets 'Threads'
    public synchronized static void main(String[] args)throws IOException 
	{
		new Server().runServer();
	}
	
    /**
     *
     * @throws IOException
     */
    public void runServer() throws IOException 
	{
		try 
		{
			//Create Socket for the Server
			ServerSocket serverSocket = new ServerSocket(PORT); 
			System.out.println("S: Server is up and it is ready for connections");
			
			while(true)
			{
				//Client socket is accepted from the server
				Socket socket = serverSocket.accept(); 
				new ServerThread(socket).start();
				System.out.println("S: Client with "+socket+" is Connected");
			}
		}
		catch (Exception e)
		{
            System.out.println("Error : " + e.getMessage());
        }
	}
}
