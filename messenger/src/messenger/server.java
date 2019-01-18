package messenger;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.*;
public class server
{
	private static ServerSocket ss=null;
	private static Socket s=null;
	private static String userName=null;
	private static Map<String,clients> usere=new HashMap<>();
	private static Map<String,String> check=new HashMap<>();
	public static Map<String,Map<String,Integer>> not=new HashMap<>();
	
	private static PrintStream outa=null;
	private static boolean n=true;
	public static void main(String[] args) throws IOException
	{
		ss=new ServerSocket(7878);
		System.out.println("searching for client");
		System.out.println("hfgd");
		while(true)
		{	
			
			s=ss.accept();
			BufferedReader in=new BufferedReader(new InputStreamReader(s.getInputStream()));
			outa=new PrintStream(s.getOutputStream());
			outa.println("enter a name");
			userName=in.readLine();
			outa.println("enter password");
			String password=in.readLine();			
			if(usere.containsKey(userName))
			{
				if(check.get(userName).equals(password))
				{
					clients a=new clients(s,usere,userName);
					outa.println("(userName and password matched");
					usere.put(userName, a);
					a.start();
					break;
				}
				else
				{
					outa.println("password did not match please re enter username and password");
					continue;
				}
			}
			else
			{outa.println("(username and password registered");
			System.out.println("client "+userName+" connected");
			check.put(userName, password);	
			clients sa=new clients(s,usere,userName);
			usere.put(userName,sa);
			sa.start();
			
			
			}
		}
	}
}



class clients extends Thread
{	
	private  Socket s;
	private Map<String,clients> usere=null;
	private BufferedReader in;
	private PrintStream out;
	private String clientName="";
	private boolean a=true;
	private static Map<String,Map<String,Integer>> b;
	private static String notify="notify.txt";
	private  static BufferedWriter fo;
	private static BufferedReader fi;
	private static File fileName;
	private static Map<String,Integer> notif=new HashMap<>();
	private  boolean tempName=false;
	private static	int count=1;
	private  String temporaryName="";
	

	clients(Socket s,Map<String,clients> threads,String clientName)
	{
		this.s=s;
		this.usere=threads;
		this.clientName="@"+clientName;
		a=true;

	}
	String getClientName()
	{
		return clientName;
	}
	
	public void stap()
	{
		a=false;
	}
	 void availableClients(Map<String,clients> broadcast)throws FileNotFoundException,IOException,ClassNotFoundException
	{
		 b=(Map<String,Map<String,Integer>>)retrieveFile(notify);
			if(b!=null)server.not=b;
		 for(clients a:usere.values())
		{
			 
			 if(a.tempName==false)
			 {
				a.out.println("(available clients are");
				for(clients b:usere.values())
				{
					if(b!=a)
					{
							if(server.not.get(a.getClientName())==null)
							{
								a.out.println("->"+b.getClientName()+"-(0)");
							}
							else if(server.not.get(a.getClientName()).get(b.getClientName())==null)
							{
								a.out.println("->"+b.getClientName()+"-(0)");
							}
							else
							{	
								a.out.println("->"+b.getClientName()+"-("+server.not.get(a.getClientName()).get(b.getClientName())+")");
	
							}
					
					}
					
				}
			 }
		}

	}
	public static void saveFile(Object orderList2, String fileName)throws FileNotFoundException, IOException, ClassNotFoundException
	{
	File file=new File(fileName);
	
		ObjectOutputStream o=new ObjectOutputStream(new FileOutputStream(file));
		o.writeObject(orderList2);
		
		o.close();

	
		
	}
	public static Object retrieveFile(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException
	{	//boolean exists;
		File file=new File(fileName);
		if(file.exists())
		{
			ObjectInputStream i=new ObjectInputStream(new FileInputStream(file));
			 return i.readObject();
			 
			
		
		
		}
		return null;
	
	}
	
	public void run()
	{
		try{
		in=new BufferedReader(new InputStreamReader(s.getInputStream()));
		out=new PrintStream(s.getOutputStream());
		fileName=new File("list.txt");
		fo=new BufferedWriter(new FileWriter(fileName,true));
		fi=new BufferedReader(new FileReader(fileName));
		
		
		while(a)
		{
			b=(Map<String,Map<String,Integer>>)retrieveFile(notify);
			if(b!=null)server.not=b;
			
			synchronized(this)
			{
				availableClients(usere);
				
			}String line=in.readLine();
			
	
			if(line.startsWith("@"))
			{
				String[] words = line.split("@", 2);
          		if (words.length > 1 && words[1] != null)
          		{ 
	           		 words[1] = words[1].trim();
	           		 words[0]=words[0].trim();
	           		if (!words[1].isEmpty()) 
	           		{
	           			
	           				if(usere.containsKey(words[1]))
	           				{
	           					server.not.put("@"+words[1], notif);
	           					server.not.put(clientName,notif);
	           					server.not.get(clientName).put("@"+words[1],0);
	        					
	        					saveFile(server.not,notify);
	        					tempName=true;
	        					temporaryName="@"+words[1];
	        					String ko;
	        					if(!fileName.exists())
	        					{
	        						fileName.createNewFile();
	        						
	        						
	        					}
	        					
	        					while((ko=fi.readLine())!=null)
	        					{
	        						Pattern p=Pattern.compile("([@]"+words[1]+"|"+clientName+")("+clientName+"|[@]"+words[1]+")[:]\\s[\\w|\\s]+[<][-]");
	        						Matcher m=p.matcher(ko);
	        						while(m.find())
	        						{
	        							System.out.println("hi");
	        							if(m.group().length()!=0)
	        							{
	        								this.out.println(m.group().trim());
	        								

	        							}
	        						}
	        					}
	        					if((ko=fi.readLine())==null)
	        					{
	        						fi.close();
	        						fi=new BufferedReader(new FileReader(fileName));
	        					}
				           		while(true)
				           		{	
				           			b=(Map<String,Map<String,Integer>>)retrieveFile(notify);
				        			if(b!=null)server.not=b;									           				
				           			String lin=in.readLine();
						           	if(lin.equalsIgnoreCase("exit"))
						           	{	
						           		System.out.println("exiting");
						           		tempName=false;
										temporaryName="";
						         		break;
						           	}
						          	else
						           	{
				           			
						          		fo.write(this.getClientName()+"@"+words[1]+": "+lin+" <- ");
					           			fo.flush();
					           			if(tempName==true)
					           			{
						           			if(this.getClientName().equals(usere.get(words[1]).temporaryName))
						           			{
						           				usere.get(words[1]).out.println(this.getClientName()+"@"+words[1]+": "+lin+" <- ");
						           				
						           			}
						           			else
						           			{
						           				if(server.not.get("@"+words[1]).containsKey(clientName))
												{	
													server.not.get("@"+words[1]).put(clientName,(server.not.get("@"+words[1]).get(clientName)+1));
													saveFile(server.not,notify);
														
												}
												else
												{
													notif.put(clientName,count);
													server.not.put("@"+words[1],notif);
													saveFile(server.not,notify);

												}
						           				
						           			}
					           			}
					           			synchronized(this)
							           	{
							           		availableClients(usere);
							           	}
						           					
						           	}
						           						           		
					           				
					           	}
					        

			           	}
			        }
			    }
			}
			      	
	       else if(line.startsWith("#"))
	       {
				           		
	    	   synchronized(this)
	    	   {
	    		   for(clients z:usere.values())
	    		   {
	    			   z.out.println(line);
	    		   }
				
	    	   }
	       }
	       else if(line.equalsIgnoreCase("quit"))
	       {
	    	   
	    	   this.out.println("logging out");
	    	   stap();
	    	   
	    	   in.close();
	    	   out.close();
	    	   s.close();
	    	   this.stop();
	       }
	       else 
	       {
	    	   out.println("enter a valid input");
	       }	
			
		}
		}catch(IOException e){}
		catch(ClassNotFoundException q) {}
				
	}
}
///usere/ajay-pt2293/eclipse-workspace/messenger