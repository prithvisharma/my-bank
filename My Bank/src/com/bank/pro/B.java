
package com.bank.pro;

import com.bank.exceptions.InsufficientFundsException;
import com.bank.exceptions.InvalidAccountNumberException;
import com.bank.main.Customer;
import com.bank.main.Statement;
import com.bank.model.DBConfig;
import java.sql.SQLException;
import java.util.List;


public class B {

    private DBConfig db;
    public B(){
       db = new DBConfig();
    }
    public boolean checkAccountNumber(String accountNumber) throws SQLException, ClassNotFoundException, InvalidAccountNumberException {
        boolean status = db.checkAccountNumber(accountNumber);
        if(status){
            return true;
        }
        throw new InvalidAccountNumberException();
    }
    
    public List<Statement> fetchTransactions(Customer c) throws ClassNotFoundException, SQLException{
        List<Statement> list= db.fetchTransactions(c);
        return list;
    }
    
    public Customer fetchCustomerDetails(String accountNumber) throws ClassNotFoundException, SQLException {
        Customer c = db.fetchCustomerDetails(accountNumber);
        return c;
    }

    public void saveTransaction(Customer c, String amount) throws ClassNotFoundException, SQLException {
        db.savetransaction(c,amount);
    }

    public void updateBalance(Customer c, String amount) throws ClassNotFoundException, SQLException {
        db.updateBalance(c,amount);
    }

    public void saveTransactionWithdraw(Customer c, String amount) throws ClassNotFoundException, SQLException, InsufficientFundsException {
        db.saveTransactionWithdraw(c,amount);
    }

    public void updateBalanceWithdraw(Customer c, String amount) throws SQLException, ClassNotFoundException {
        db.updateBalanceWithdraw(c, amount);
    }
    
}
