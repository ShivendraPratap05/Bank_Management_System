import java.sql.*;
import java.util.Scanner;
import static java.lang.Class.forName;

public class Banking 
{
	private static final String url="jdbc:mysql://localhost:3306/banking_db";
	private static final String username="root";
	private static final String password="866901";
	public static void main(String args[]) throws ClassNotFoundException,SQLException
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
		    System.out.println("Connection established successfully...");
		}
		catch (ClassNotFoundException e)
		{
			System.out.println(e.getMessage());
		}
	try
	{
		Connection con=DriverManager.getConnection(url,username,password);
		System.out.println("Driver Loaded Successfully...");
		Scanner sc=new Scanner (System.in);
		User u=new User(con,sc);
		Accounts ac=new Accounts(con,sc);
		AccountManager acm=new AccountManager(con,sc);
		
		String Email;
		long Account_Number;
		
		while(true)
		{
			System.out.println("                   ......WELCOME  TO  BANKING  SYSTEM......   ");
			System.out.println();
			System.out.println("1. Register");
			System.out.println("2. Login");
			System.out.println("3. Exit");
			System.out.print("Enter your choice:");
			int choice=sc.nextInt();
			switch(choice)
			{
			case 1:
				u.register();
				break;
			case 2:
				Email=u.login();
				if(Email!=null)
				{
					System.out.println();
					System.out.println("User Login Successfully....");
					if(!ac.Account_Exist(Email))
					{
						System.out.println();
						System.out.println("1. Open a New Bank Account");
						System.out.println("2. Exit");
						if(sc.nextInt()==1)
						{
							Account_Number=ac.Open_Account(Email);
							System.out.println();
							System.out.println("Your Account Created Successfully...");
							System.out.println("Your Account Number is :" +Account_Number);
						}
						else
						{
							break;
						}
					}
					Account_Number=ac.getAccount_Number(Email);
					int choice1=0;
					while(choice1!=5)
					{
						System.out.println("......................................");
						System.out.println();
						System.out.println("1. Credit Money");
						System.out.println("2. Debit Money");
						System.out.println("3. Transfer Money");
						System.out.println("4. Check Balance");
						System.out.println("5. Log Out");
						System.out.println("Enter Your Choice:");
						choice1=sc.nextInt();
						switch(choice1)
						{
						case 1:
							acm.Credit_Money(Account_Number);
							break;
						case 2:
							acm.Debit_Money(Account_Number);
							break;
						case 3:
							acm.Transfer_Money(Account_Number);
							break;
						case 4:
							acm.Get_Balance(Account_Number);
							break;
						case 5:
							break;
						default :
							System.out.println("Enter valid Choice...");
						}
					}
				}
				else
				{
					System.out.println();
					System.out.println("Incorrect Email or Password...");
				}
			case 3:
				System.out.println("                            THANKYOU FOR USING BANKING SYSTEM....");
				System.out.println();
				System.out.println("Exiting System....");
				return;
			default:
				System.out.println("Enter the Valid Choice:");
				break;
			}
		}
	}
	catch (SQLException e)
	{
		e.printStackTrace();
	}
	}
}