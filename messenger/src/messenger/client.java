//package messenger;
//check for resetting the pointer to beginning while reading a file
import java.io.*;
import java.util.*;
import java.net.*;
import java.util.regex.*;
class client implements Runnable
{
	private static Socket s;
	private  static BufferedReader in;
	private  static PrintStream out;
	private static  Scanner input=new Scanner(System.in);
	private static Map<String,Integer> users=new HashMap<>();
	private static Map<String,Map<String,Integer>> a;
	
	private  static BufferedWriter fo;
	private  static BufferedReader fi;
	private static ObjectOutputStream oos;
	private static ObjectInputStream ois;
	private static File fileName;
	private static String name;

	private static boolean hula=true;

	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException
	{	
		s=new Socket("localhost",7878);
		in=new BufferedReader(new InputStreamReader(s.getInputStream()));
		out=new PrintStream(s.getOutputStream());
		fileName=new File("list.txt");
		fo=new BufferedWriter(new FileWriter(fileName,true));
		fi=new BufferedReader(new FileReader(fileName));
		
		
		System.out.println(in.readLine());
		 name=input.nextLine();
		out.println(name);
		while(hula)
		{
			System.out.println(in.readLine());
			String password=input.nextLine();
			out.println(password);
			String r=in.readLine();
			System.out.println(r);
			if(r.startsWith("("))
			{
				hula=false;
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

						System.out.println(recieve);
					}
				
			
		}catch(IOException r){}
	
	}

}