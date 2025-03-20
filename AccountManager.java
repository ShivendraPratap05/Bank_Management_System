import java.util.Scanner;
import java.sql.*;

public class AccountManager 
{
	private Connection con;
	private Scanner sc;
	
	public AccountManager(Connection con,Scanner sc)
	{
		this.con=con;
		this.sc=sc;
	}
	
	public void Credit_Money(long Account_Number) throws SQLException
	{
		sc.nextLine();
		System.out.print("Amount:");
		double Amount=sc.nextDouble();
		sc.nextLine();
		System.out.print("SecurityPin:");
		String Security_Pin=sc.next();
		try
		{
			con.setAutoCommit(false);
			if(Account_Number!=0)
			{
				PreparedStatement pd=con.prepareStatement("select * from accounts where Account_Number=? and Security_Pin=?");
				pd.setLong(1,Account_Number);
				pd.setString(2,Security_Pin);
				ResultSet rs=pd.executeQuery();
				
				if(rs.next())
				{
					String Credit_query="update accounts set Balance=Balance + ? where Account_Number=?";
					PreparedStatement pd1=con.prepareStatement(Credit_query);
					pd1.setDouble(1, Amount);
					pd1.setLong(2,Account_Number);
					int rowsAffected=pd1.executeUpdate();
					if(rowsAffected>0)
					{
						System.out.println("Rs."+Amount+"Credited Successfully...");
						con.commit();
						con.setAutoCommit(true);
						return;
					}
					else
					{
						System.out.println("Transaction Failed...");
						con.rollback();
						con.setAutoCommit(true);
					}
				}
				else
				{
					System.out.println("Invalid Pin.....");
				}
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		con.setAutoCommit(true);
	}
	
	public void Debit_Money(long Account_Number) throws SQLException
	{
		sc.nextLine();
		System.out.print("Amount:");
		double Amount=sc.nextDouble();
		sc.nextLine();
		System.out.println("Security Pin:");
		String Security_Pin=sc.nextLine();
		try
		{
			con.setAutoCommit(false);
			if(Account_Number!=0)
			{
				PreparedStatement pd=con.prepareStatement("select * from accounts where Account_Number=? and Security_Pin=?");
				pd.setDouble(1,Account_Number);
				pd.setString(2,Security_Pin);
				ResultSet rs=pd.executeQuery();
				
				if(rs.next())
				{
					double Current_Balance=rs.getDouble("Balance");
					if(Amount<=Current_Balance)
					{
						String Debit_query="update accounts set Balance=Balance-? where Account_Number=?";
						PreparedStatement pdd=con.prepareStatement(Debit_query);
						pdd.setDouble(1,Amount);
						pdd.setLong(2,Account_Number);
						int rowsAffected=pdd.executeUpdate();
						if(rowsAffected>0)
						{
							System.out.print("Rs. "+Amount+" Debited Successfully...");
							con.commit();
							con.setAutoCommit(true);
							return;
						}
						else
						{
							System.out.println("Transaction Failed...");
							con.rollback();
							con.setAutoCommit(true);
						}
					}
					else
					{
						System.out.println("Insufficient Balance....");
					}
				}
				else
				{
					System.out.println("Invalid Pin.....");
				}
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		con.setAutoCommit(true);
	}
	public void Transfer_Money(long Sender_Account_Number)throws SQLException
	{
		sc.nextLine();
		System.out.println("Receiver Account Number:");
		long Receiver_Account_Number=sc.nextLong();
		System.out.println("Amount:");
		double Amount=sc.nextDouble();
		sc.nextLine();
		System.out.println("Security_Pin:");
		String Security_Pin=sc.next();
		try
		{
			con.setAutoCommit(false);
			if(Sender_Account_Number!=0 && Receiver_Account_Number!=0)
			{
			    PreparedStatement pd=con.prepareStatement("select * from accounts where Account_Number=? and Security_Pin=?");
			    pd.setLong(1,Sender_Account_Number);
			    pd.setString(2,Security_Pin);
			    ResultSet rs=pd.executeQuery();
			    if(rs.next())
			    {
			    	double Current_Balance=rs.getDouble("Balance");
			    	if(Amount<=Current_Balance)
			    	{
			    		String Credit_query="update accounts set Balance = Balance + ? where Account_Number=?";
			    		String Debit_query="update accounts set Balance = Balance - ? where Account_Number =?";
			    		PreparedStatement creditpd=con.prepareStatement(Credit_query);
			    		PreparedStatement debitpd=con.prepareStatement(Debit_query);
			    		creditpd.setDouble(1,Amount);
			    		creditpd.setLong(2, Receiver_Account_Number);
			    		debitpd.setDouble(1,Amount);
			    		debitpd.setLong(2, Sender_Account_Number);
			    		int rowsAffected1=creditpd.executeUpdate();
			    		int rowsAffected2=debitpd.executeUpdate();
			    		if(rowsAffected1>0 && rowsAffected2>0)
			    		{
			    			System.out.print("Transaction Successfull...");
			    			System.out.println("rs. "+Amount+ "Transfered Successfully....");
			    			con.commit();
			    			con.setAutoCommit(true);
			    			return;
			    		}
			    		else
			    		{
			    			System.out.println("Money Transfering Failed....");
			    			con.rollback();
			    			con.setAutoCommit(true);
			    		}
			    	}
			    	else
			    	{
			    		System.out.println("Insufficient Balance....");
			    	}
			    }
			    else
			    {
			    	System.out.println("Invalid Security Pin...");
			    }
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		con.setAutoCommit(true);
	}
	
	public void Get_Balance(long Account_Number)
	{
		sc.nextLine();
		System.out.print("Security_Pin:");
		String Security_Pin=sc.nextLine();
		try
		{
		    PreparedStatement pd=con.prepareStatement("select * from accounts where Account_Number=? and Security_Pin=?");
		    pd.setDouble(1, Account_Number);
		    pd.setString(2,Security_Pin);
		    ResultSet rs=pd.executeQuery();
		    if(rs.next())
		    {
		    	double Balance=rs.getDouble("Balance");
		    	System.out.println("Balance:"+Balance);
		    }
		    else
		    {
		    	System.out.println("Invalid Pin....");
		    }
		}
		catch (SQLException e)
		{
		}
	}
}