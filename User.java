import javax.xml.transform.Result;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class User 
{
	private Scanner sc;
	private Connection con;
	
	public User(Connection con,Scanner sc)
	{
		this.con=con;
		this.sc=sc;
	}
	public void register()
	{
		sc.nextLine();
		System.out.print("Enter Full_Name:");
		String Full_Name=sc.nextLine();
		System.out.print("Email:");
		String Email=sc.nextLine();
		System.out.print("password:");
		String Password=sc.nextLine();
		if(Email_Exist(Email))
		{
			System.out.println("Email Already Exist...");
			return;
		}
		String register_query="insert into user(Full_Name,Email,Password) values(?,?,?);";
		try
		{
			PreparedStatement pd=con.prepareStatement(register_query);
			pd.setString(1, Full_Name);
			pd.setString(2,Email);
			pd.setString(3,Password);
			int rowsAffected=pd.executeUpdate();
			if(rowsAffected>0)
			{
				System.out.println("                           ....Regestration successfully....");
			}
			else
			{
				System.out.println("Registration Failed...");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public String login()
	{
		sc.nextLine();
		System.out.println();
		System.out.print("Email:");
		String Email=sc.nextLine();
		System.out.print("Password:");
		String Password=sc.nextLine();
		String login_query="select * from user where Email=? and Password=?";
		try
		{
			PreparedStatement pd=con.prepareStatement(login_query);
			pd.setString(1,Email);
			pd.setString(2,Password);
			ResultSet rs=pd.executeQuery();
			if(rs.next())
			{
				return Email;
			}
			else
			{
				return null;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean Email_Exist(String Email)
	{ 
		String query3="select * from user where Email=?";
		try
		{
			PreparedStatement pd=con.prepareStatement(query3);
			pd.setString(1,Email);
			ResultSet rs=pd.executeQuery();
			if(rs.next())
			{
				return true;
			}
			else
			{
				return false; 
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
		
	}
}
