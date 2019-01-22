//package messenger;
//check for resetting the pointer to beginning while reading a file
import java.io.*;
import java.util.*;
import java.net.*;
class client implements Runnable
{
	private static Socket s;
	private  static BufferedReader in;
	private  static PrintStream out;
	private static  Scanner input=new Scanner(System.in);
	private static String name;

	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException
	{	
//		System.out.println("enter ip address");
//		String ipAddress=input.nextLine();
		s=new Socket("localhost",7878);
		in=new BufferedReader(new InputStreamReader(s.getInputStream()));
		out=new PrintStream(s.getOutputStream());
		while(true)
		{
		System.out.println("Press 1 to sign up\nPress 2 to log in");
		out.println(input.nextLine());
		System.out.println(in.readLine());
		 name=input.nextLine();
		out.println(name);
		
			System.out.println(in.readLine());
			String password=input.nextLine();
			out.println(password);
			String r=in.readLine();
			System.out.println(r);
			if(r.startsWith("("))
			{
				break;
			}
		}
		new Thread(new client()).start();
		
		
		while(s.isConnected())
		{
			String send=input.nextLine();
			
			if(send.equalsIgnoreCase("exit")||send.startsWith("@"))
			{
				System.out.print("\033[H\033[2J");
			}
			if(send.equals("quit"))
			{
				out.println(send);
				break;
			}
			out.println(send);
		}
	}
	public void run()
	{
	try{
			String recieve="";
			recieve=in.readLine();
			System.out.println(recieve);
			while((recieve=in.readLine())!=null)
			{	
				
		
				if(recieve.startsWith("("))	
				{
				System.out.print("\033[H\033[2J");
				}
				if(recieve.equals("quit"))
				{
					break;
				}

				System.out.println(recieve);
			}
				
			
		}catch(IOException r){}
	
	}

}