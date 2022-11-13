package smtp.protocol.client_server;
class CaesarEncoderDecoder 
{ 
	//Implementation of Encoder 
	String Encoder(String message,int key) //"message"  is the message for send and "key" is the shift for the characters 'This must be equal with key in Decoder'
	{
		String encryptedMessage = ""; //Encrypted Message initially is null
		char character;
		
		for(int i = 0; i < message.length(); ++i)
		{
			character = message.charAt(i);
			
			//Implementation of Encoder - Lowercase Chars
			if(character >= 'a' && character <= 'z')
			{
				character = (char)(character + key);
	            if(character > 'z')
	            {
	               character = (char)(character +'a'-'z'-1);
	            }
	          //Implementation of Encoder - Create the Encrypted message from adding characters to message
	            encryptedMessage +=character;
	        }
			
			//Implementation of Encoder - Uppercase Chars
	        else if(character >= 'A' &&character <= 'Z')
	        {
	           character = (char)(character + key);
	            if(character > 'Z')
	            {
	               character = (char)(character +'A'-'Z'-1);
	            }
	          //Implementation of Encoder - Create the Encrypted message from adding characters to message
	            encryptedMessage +=character;
	        }
			
			//Implementation of Encoder - Create the Encrypted message from adding characters to message
	        else 
	        {
	        	encryptedMessage +=character;
	        }
		}
		return encryptedMessage; //Implementation of Encoder - Return the Encrypted message
	}
	
//-----------------------------------------------------------------------------------------------------------------------------------
	
	//Implementation of Decoder
	String Decoder(String message,int key)//"message"  is the message for recieve and "key" is the shift for the characters 'This must be equal with key in Encoder'
	{
		String decryptedMessage = ""; //Decrypted Message initially is null
		char character;
		
		for(int i = 0; i < message.length(); ++i)
		{
			character = message.charAt(i);
			
			//Implementation of Decoder - Lowercase Chars
			if(character >= 'a' &&character <= 'z')
			{
				character = (char)(character - key);
	            if(character < 'a')
	            {
	               character = (char)(character -'a'+'z'+1);
	            }
		    //Implementation of Decoder - Create the Decrypted message from adding characters to message
            decryptedMessage +=character;
			}
			
		//Implementation of Decoder - Uppercase Chars
        else if(character >= 'A' &&character <= 'Z')
        {
           character = (char)(character - key);
            
            if(character < 'A')
            {
               character = (char)(character -'A'+'Z'+1);
            } 
            //Implementation of Decoder - Create the Decrypted message from adding characters to message
            decryptedMessage +=character;
        }
			
		//Implementation of Decoder - Create the Decrypted message from adding characters to message
        else 
        {
        	decryptedMessage +=character;
        }
	}
	return decryptedMessage; //Implementation of Decoder - Return the Decrypted message
}    
} 