package com.bank.authservice.util;

import com.bank.authservice.model.Account;
import java.util.HashMap;
import java.util.Map;

public class AccountStorage {

    public static Map<String, Account> accounts = new HashMap<>();

    static {
        accounts.put("sakshi", new Account("sakshi", 1000));
        accounts.put("admin", new Account("admin", 5000));
    }
}