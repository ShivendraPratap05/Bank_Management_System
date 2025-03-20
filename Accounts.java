import java.util.Scanner;
import java.sql.*;

public class Accounts 
{
	private Connection con;
	private Scanner sc;
	
	public Accounts(Connection con,Scanner sc)
	{
		this.con=con;
		this.sc=sc;
	}
	
	public long Open_Account(String Email)
	{
		if(!Account_Exist(Email))
		{
			String openAccount_query="insert into accounts(Account_Number,Full_Name,Email,Balance,Security_Pin) values(?,?,?,?,?)";
			sc.nextLine();
			System.out.print("Full Name:");
			String Full_Name=sc.nextLine();
			System.out.print("Initial Balance:");
			Double Balance=sc.nextDouble();
			sc.nextLine();
			System.out.print("Security Pin:");
			String Security_Pin=sc.nextLine();
			try
			{
				long Account_Number=GenerateAccount_Number();
				PreparedStatement pd=con.prepareStatement(openAccount_query);
				pd.setLong(1,Account_Number);
				pd.setString(2,Full_Name);
				pd.setString(3,Email);
				pd.setDouble(4,Balance);
				pd.setString(5, Security_Pin);
				int rowsAffected=pd.executeUpdate();
				if(rowsAffected>=0)
				{
					return Account_Number;
				}
				else
				{
					throw new RuntimeException("Account Creation Failed...");
				}
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		throw new RuntimeException("Account Already Exists....");
	}	
	
	public long getAccount_Number(String Email)
	{
		String query6="select Account_Number from accounts where Email=?";
		try
		{
			PreparedStatement pd=con.prepareStatement(query6);
			pd.setString(1,Email);
			ResultSet rs=pd.executeQuery();
			if(rs.next())
			{
				return rs.getLong("Account_Number");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		throw new RuntimeException("Account Number Does not Exist...");
		 
	}
	
	private long GenerateAccount_Number()
	{
		try
		{
			Statement stm=con.createStatement();
			ResultSet rs=stm.executeQuery("select Account_Number from accounts order by Account_Number desc limit 1");
			if(rs.next())
			{
				long LastAccount_Number=rs.getLong("Account_Number");
				return LastAccount_Number+1;
			}
			else
			{
				return 10000100;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return 10000100;
	}
	
	public boolean Account_Exist(String Email)
	{
		String query4="select Account_Number from accounts where Email=?";
		try
		{
			PreparedStatement pd=con.prepareStatement(query4);
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
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
}
