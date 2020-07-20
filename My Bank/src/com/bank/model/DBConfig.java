
package com.bank.model;

import com.bank.exceptions.InsufficientFundsException;
import com.bank.main.Customer;
import com.bank.main.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class DBConfig {
    
    String userDB = "root";
    String passwordDB = "";
    String url = "jdbc:mysql://localhost:3306/my_bank?autoReconnect=true&useSSL=false";
    String driver = "com.mysql.jdbc.Driver";
    
    private Connection con;
    
    private void dbConnect() throws ClassNotFoundException, SQLException{
        Class.forName(driver);
        con = DriverManager.getConnection(url, userDB, passwordDB);
    }
    
    private void dbClose() throws SQLException{
        con.close();
    }
    
    public void createAccount(Customer c) throws SQLException, 
            ClassNotFoundException{
        dbConnect();
        String sql = "insert into customer (name,address,email,account_number,"
                + "balance) values(?,?,?,?,?) ";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, c.getName());
        pstmt.setString(2, c.getAddress());
        pstmt.setString(3, c.getEmail());
        pstmt.setString(4, c.getAccountNumber());
        pstmt.setString(5, c.getBalance());
        pstmt.executeUpdate();
        dbClose();
    }

    public boolean checkAccountNumber(String accountNumber) throws SQLException, ClassNotFoundException {
        int count = 0;
        dbConnect();
        String sql = "Select * from customer where account_number=?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, accountNumber);
        ResultSet rst = pstmt.executeQuery();
        while(rst.next()){
            if(count==0)
                return true;
        }
        dbClose();
        return false;
    }

    public Customer fetchCustomerDetails(String accountNumber) throws ClassNotFoundException, SQLException {
        dbConnect();
        Customer c = new Customer();
        String sql = "Select * from customer where account_number=?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, accountNumber);
        ResultSet rst = pstmt.executeQuery();
        while(rst.next()){
            c.setName(rst.getString(2));
            c.setAddress(rst.getString(3));
            c.setEmail(rst.getString(4));
            c.setAccountNumber(rst.getString(5));
            c.setBalance(rst.getString(6));
        }
        dbClose();
        return c;
    }
    
    public List<Statement> fetchTransactions(Customer c) throws ClassNotFoundException, SQLException{
        dbConnect();
        List<Statement> statement_list = new ArrayList<>();
        String sql = "select date, time,"
                + " type, amount,"
                + " initial_balance,"
                + "final_balance from transactions where account_number=?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, c.getAccountNumber());
        ResultSet rst = pstmt.executeQuery();
        while (rst.next()){
            Statement statement = new Statement();
            statement.setDate(rst.getString("date"));
            statement.setTime(rst.getString("time"));
            statement.setType(rst.getString("type"));
            statement.setAmount(rst.getString("amount"));
            statement.setInitial_amount(rst.getString("initial_balance"));
            statement.setFinal_amount(rst.getString("final_balance"));
            statement_list.add(statement);
        }
        dbClose();
        //System.out.println(statement_list);
        return statement_list;
    }
    public void savetransaction(Customer c, String amount) throws ClassNotFoundException, SQLException {
        dbConnect();
        String sql = "insert into transactions (account_number,amount,initial_balance"
                + ",final_balance,date,time,type) values(?,?,?,?,?,?,?) ";
        double amt = Double.parseDouble(amount);
        double iniBal = Double.parseDouble(c.getBalance());
        double finBal = amt + iniBal;
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, c.getAccountNumber());
        pstmt.setString(2, amount);
        pstmt.setString(3, Double.toString(iniBal));
        pstmt.setString(4, Double.toString(finBal));
        pstmt.setString(5, LocalDate.now().format(DateTimeFormatter.ISO_DATE).toString());
        pstmt.setString(6, LocalTime.now().format(DateTimeFormatter.ISO_TIME).toString());
        pstmt.setString(7, "Deposit");
        pstmt.executeUpdate();
        dbClose();
    }

    public void updateBalance(Customer c, String amount) throws ClassNotFoundException, SQLException {
        dbConnect();
        String sql = "update customer set balance=? where account_number=? ";
        PreparedStatement pstmt = con.prepareStatement(sql);
        double amt = Double.parseDouble(amount);
        double iniBal = Double.parseDouble(c.getBalance());
        double finBal = amt+iniBal;
        pstmt.setString(1,Double.toString(finBal));
        pstmt.setString(2, c.getAccountNumber());
        pstmt.executeUpdate();
        dbClose();
    }

    public void saveTransactionWithdraw(Customer c, String amount) throws ClassNotFoundException, SQLException, InsufficientFundsException {
        
        double amt = Double.parseDouble(amount);
        double iniBal = Double.parseDouble(c.getBalance());
        if(iniBal >= amt){
            dbConnect();
            String sql = "insert into transactions (account_number,amount,initial_balance"
                + ",final_balance,date,time,type) values(?,?,?,?,?,?,?) ";
            double finBal = iniBal-amt;
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, c.getAccountNumber());
            pstmt.setString(2, amount);
            pstmt.setString(3, Double.toString(iniBal));
            pstmt.setString(4, Double.toString(finBal));
            pstmt.setString(5, LocalDate.now().format(DateTimeFormatter.ISO_DATE).toString());
            pstmt.setString(6, LocalTime.now().format(DateTimeFormatter.ISO_TIME).toString());
            pstmt.setString(7, "Withdrawal");
            pstmt.executeUpdate();
            dbClose();    
        }
        else{
            throw new InsufficientFundsException();
        }
    }

    public void updateBalanceWithdraw(Customer c, String amount) throws SQLException, ClassNotFoundException {
        dbConnect();
        String sql = "update customer set balance=? where account_number=? ";
        PreparedStatement pstmt = con.prepareStatement(sql);
        double amt = Double.parseDouble(amount);
        double iniBal = Double.parseDouble(c.getBalance());
        double finBal = iniBal-amt;
        pstmt.setString(1,Double.toString(finBal));
        pstmt.setString(2, c.getAccountNumber());
        pstmt.executeUpdate();
        dbClose();
    }
}