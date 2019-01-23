//package messenger;
//available clients broadcast after shuting down server

//something to end the client using quit
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.*;
import java.io.Serializable;
public class server implements Serializable
{
	private static ServerSocket ss=null;
	private static Socket s=null;
	private static String userName=null;
	private static Map<String,clients> usere=new HashMap<>();
	public static Map<String,userinfo> check=new HashMap<>();
	public static Map<String,Map<String,Integer>> not=new HashMap<>();
	public static Map<String,Integer> broadcastNotif=new HashMap<>();
	protected final static String userpass="userpass.txt";
	private static String login;

	
	private static PrintStream outa=null;
	public static void main(String[] args) throws IOException,ClassNotFoundException,FileNotFoundException
	{	Map<String,userinfo> c=(Map<String,userinfo>)clients.retrieveFile(userpass);
		if(c!=null)check=c;
		ss=new ServerSocket(7878);
		System.out.println("searching for client");
		while(true)
		{	
			
			s=ss.accept();
			BufferedReader in=new BufferedReader(new InputStreamReader(s.getInputStream()));
			outa=new PrintStream(s.getOutputStream());
			boolean aa=true;
			while(aa)
			{
				login=in.readLine();
				switch(login)
				{
				case "1":{
							outa.println("enter a name");
							userName=in.readLine();
							outa.println("enter password");
							String password=in.readLine();outa.println("(username and password registered");
							System.out.println("client "+userName+" connected");
							check.put(userName, new userinfo(userName,password));
							if(check.containsKey(userName))
							{
							check.get(userName).alterOnlineStatus("online");
							clients.saveFile(check,userpass);
							}
							clients sa=new clients(s,usere,userName);
							usere.put(userName,sa);
							broadcastNotif.put("@"+userName,0);
							clients.saveFile(broadcastNotif,"broadcastNotification.txt" );
							sa.start();	
							aa=false;
					
						}break;
				case "2":{
							outa.println("enter a name");
							userName=in.readLine();
							outa.println("enter password");
							String password=in.readLine();			
							if(check.containsKey(userName))
							{
								if(check.get(userName).getPassword().equals(password))
								{
									clients a=new clients(s,usere,userName);
									outa.println("(userName and password matched");
									usere.put(userName, a);
									if(check.containsKey(userName))
									{
									check.get(userName).alterOnlineStatus("online");
									clients.saveFile(check, userpass);
									}
									a.start();
									aa=false;
								}
								else
								{
									outa.println("password did not match please re enter username and password");
									continue;
								}
							}
							else
							{
								outa.println("password did not match please re enter username and password");
								continue;
							}
					
						}break;
				default:outa.println("enter a valid input");
				}
			}
		}
	}
}


class userinfo implements Serializable
{
	private String password;
	private String userName;
	private String clientName;
	private String status="hi i using zoho chat";
	private String onlineStatus="online";
	userinfo(String userName,String password)
	{	this.userName=userName;
		this.clientName="@"+userName;
		this.password=password;
	}
	String getClientName()
	{
		return clientName;
	}
	String getUserName()
	{
		return userName;
	}
	String getStatus()
	{
		return status;
	}
	String getPassword()
	{
		return password;
	}
	void alterStatus(String status)
	{
		this.status=status;
	}
	String getOnlineStatus()
	{
		return onlineStatus;
	}
	void alterOnlineStatus(String onlineStatus)
	{
		this.onlineStatus=onlineStatus;
	}
}
class clients extends Thread 
{	
	private  Socket s;
	private Map<String,clients> usere=null;
	private BufferedReader in;
	private PrintStream out;
	private String clientName="";
	private String userName;
	private boolean a=true;
	private static Map<String,Map<String,Integer>> b;
	private static String notify="notify.txt";
	private static BufferedWriter fo;
	private static BufferedReader fi;
	private static File fileName;
	private static Map<String,Integer> notif=new HashMap<>();
	private boolean tempName=false;
	private static	int count=1;
	private String temporaryName="";
	private String status="online";
	private Map<String,Integer> aj;
	clients(Socket s,Map<String,clients> threads,String clientName)
	{
		this.s=s;
		this.usere=threads;
		this.userName=clientName;
		this.clientName="@"+clientName;
	}
	String getClientName()
	{
		return clientName;
	}
	
	 void availableClients(Map<String,clients> broadcast)throws FileNotFoundException,IOException,ClassNotFoundException
	{
		
		 for(clients a:usere.values())
		{
			 
			 if(a.tempName==false)
			 {
				a.out.println("(available clients are");
				for(userinfo b:server.check.values())
				{
					if(!a.userName.equals(b.getUserName()))
					{
							if(server.not.get(a.getClientName())==null)
							{
								a.out.println("->"+b.getClientName()+"-(0)-("+b.getOnlineStatus()+")-("+server.check.get(b.getUserName()).getStatus()+")");
							}
							else if(server.not.get(a.getClientName()).get(b.getClientName())==null)
							{
								a.out.println("->"+b.getClientName()+"-(0)-("+b.getOnlineStatus()+")-("+server.check.get(b.getUserName()).getStatus()+")");
							}
							else
							{	
								a.out.println("->"+b.getClientName()+"-("+server.not.get(a.getClientName()).get(b.getClientName())+")-("+b.getOnlineStatus()+")-("+server.check.get(b.getUserName()).getStatus()+")");
	
							}
					}
				}
					a.out.println("->@broadcast-("+server.broadcastNotif.get(a.clientName)+")");
					a.out.println("->@status");
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
			 aj=(Map<String,Integer>)retrieveFile("broadcastNotification.txt");
			 if(aj!=null)server.broadcastNotif=aj;
			 b=(Map<String,Map<String,Integer>>)retrieveFile(notify);
			if(b!=null)server.not=b;
			Map<String,userinfo> c=(Map<String,userinfo>)retrieveFile(server.userpass);
			if(c!=null)server.check=c;
		in=new BufferedReader(new InputStreamReader(s.getInputStream()));
		out=new PrintStream(s.getOutputStream());
		fileName=new File("list.txt");
		fo=new BufferedWriter(new FileWriter(fileName,true));
		fi=new BufferedReader(new FileReader(fileName));
		
		
		
		while(true)
		{
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
	           		 String ko;
	           		if (!words[1].isEmpty()) 
	           		{	
	           			if(words[1].equalsIgnoreCase("status"))
	           			{
	           				 server.check.get(userName).alterStatus(in.readLine());
	           				 saveFile(server.check,server.userpass);
	           			}
	           			else if(words[1].equalsIgnoreCase("broadcast"))
	           		
	           			{
	           				
           					server.broadcastNotif.put(clientName,0);
        					
        					saveFile(server.broadcastNotif,"broadcastNotification.txt");
        					tempName=true;
        					temporaryName="@"+words[1];
        					if(!fileName.exists())
        					{
        						fileName.createNewFile();
        						
        						
        					}
        					
        					while((ko=fi.readLine())!=null)
        					{
        						Pattern p=Pattern.compile("[@][\\w]+[@]"+words[1]+"[:]\\s[\\w|\\s]+[<][-]");
        						Matcher m=p.matcher(ko);
        						while(m.find())
        						{
        							
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
			           											           				
			           			String lin=in.readLine();
					           	if(lin.equalsIgnoreCase("exit"))
					           	{	
					           		
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
					           			for(clients z:usere.values())
					           			{
					           				if(z.temporaryName.equals("@broadcast"))
						           			{
						           				z.out.println(this.getClientName()+"@"+words[1]+": "+lin+" <- ");
						           			}
						           			else
						           			{
						           				server.broadcastNotif.put(z.clientName,server.broadcastNotif.get(z.clientName)+1);
						           				saveFile(server.broadcastNotif,"broadcastNotification.txt");
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
	           		
	           		
	           			else if(server.check.containsKey(words[1]))
	           			{
	           					if(!words[1].equals("broadcast"))
	           					{
	           						server.not.put("@"+words[1], notif);
	           					}
	           					server.not.put(clientName,notif);
	           					server.not.get(clientName).put("@"+words[1],0);
	        					
	        					saveFile(server.not,notify);
	        					tempName=true;
	        					temporaryName="@"+words[1];
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
									           				
				           			String lin=in.readLine();
						           	if(lin.equalsIgnoreCase("exit"))
						           	{	
						           		
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
					           				if(usere.containsKey(words[1]))
					           				{
							           			if(this.getClientName().equals(usere.get(words[1]).temporaryName))
							           			{
							           				usere.get(words[1]).out.println(this.getClientName()+"@"+words[1]+": "+lin+" <- ");
							           			}
							           			else
							           			{
							           				if(server.not.get("@"+words[1]).containsKey(clientName))
													{	System.out.println("check");
														server.not.get("@"+words[1]).put(clientName,(server.not.get("@"+words[1]).get(clientName)+1));
													}
													else
													{	System.out.println("check2");
														notif.put(clientName,count);
														server.not.put("@"+words[1],notif);

													}
							           				saveFile(server.not,notify);
							           			}

					           				}

					           	
					           		synchronized(this) 
					           		{	
					           			if(usere.containsKey(words[1]))
					           			{
							           		 if(usere.get(words[1]).tempName==false)
							    			 {
							           			usere.get(words[1]).out.println("(available clients are");
							    				for(userinfo b:server.check.values())
							    				{
							    					if(!usere.get(words[1]).userName.equals(b.getUserName()))
							    					{
							    							if(server.not.get(usere.get(words[1]).getClientName())==null)
							    							{	System.out.println("1st");
	
							    								usere.get(words[1]).out.println("->"+b.getClientName()+"-(0)-("+b.getOnlineStatus()+")-("+server.check.get(b.getUserName()).getStatus()+")");
							    							}
							    							else if(server.not.get(usere.get(words[1]).getClientName()).get(b.getClientName())==null)
							    							{	System.out.println("2st");
	
							    								usere.get(words[1]).out.println("->"+b.getClientName()+"-(0)-("+b.getOnlineStatus()+")-("+server.check.get(b.getUserName()).getStatus()+")");
							    							}
							    							else
							    							{	System.out.println("3st");
	
							    								usere.get(words[1]).out.println("->"+b.getClientName()+"-("+server.not.get(usere.get(words[1]).getClientName()).get(b.getClientName())+")-("+b.getOnlineStatus()+")-("+server.check.get(b.getUserName()).getStatus()+")");
							    	
							    							}
							    					}
							    				}
							    				usere.get(words[1]).out.println("->@broadcast-("+server.broadcastNotif.get(usere.get(words[1]).clientName)+")");
							    				usere.get(words[1]).out.println("->@status");
							    			 }			
					           			}
					           		}

						       	}  				
					         }
	           			}
	           		}
	         	}
          	}
			}

	       else if(line.equalsIgnoreCase("quit"))
	       {
	    	   
	    	   this.out.println("logging out");
	    	   server.check.get(this.userName).alterOnlineStatus("offline");
	    	   saveFile(server.check,server.userpass);
	    	   tempName=true;
	    	   this.out.println(line);
	    	   synchronized(this)
	           	{
	           		availableClients(usere);
	           	}
	    	   
	    	   in.close();
	    	   out.close();
	    	   s.close();
	    	   break;
	    	   
	    	   
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