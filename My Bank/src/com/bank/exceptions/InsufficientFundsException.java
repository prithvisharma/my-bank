/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bank.exceptions;

/**
 *
 * @author Bittu
 */
public class InsufficientFundsException extends Exception {
    public String getMessage(){
        return "Insufficient Funds :( ";
    }
}
