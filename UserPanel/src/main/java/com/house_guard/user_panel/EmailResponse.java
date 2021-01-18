package com.house_guard.user_panel;

import java.util.ArrayList;

public class EmailResponse extends Topic {
    private ArrayList<Account> Accounts;

    public EmailResponse() {
        this.Accounts = new ArrayList<Account>();
    }

    public void addAccount(Account account) {
        Accounts.add(account);
    }

    public ArrayList getAccounts() {
        return this.Accounts;
    }
}

