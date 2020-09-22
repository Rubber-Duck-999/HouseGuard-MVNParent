package com.house_guard.Common;

public class EmailResponse extends Topic {
    private Account[] Accounts;

    public EmailResponse() {
        for(int i = 0; i < 1; i++) {
            Accounts[i] = new Account("N/A", "N/A");
        }
    }

    public Account[] getAccounts() {
        return Accounts;
    }

    public void setAccounts(Account[] accounts) {
        this.Accounts = accounts;
    }
}

