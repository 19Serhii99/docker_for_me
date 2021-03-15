package com.nixsolutions.ppp.jdbc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nixsolutions.ppp.jdbc.dao.JdbcUserDao;
import com.nixsolutions.ppp.jdbc.dao.UserDao;
import com.nixsolutions.ppp.jdbc.entity.User;
import com.nixsolutions.ppp.jdbc.tool.JdbcTransactionTemplate;
import com.nixsolutions.ppp.jdbc.tool.TransactionTemplate;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        TransactionTemplate template = new JdbcTransactionTemplate();
        UserDao userDao = new JdbcUserDao(template);
        List<User> all = userDao.findAll();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(all);
        System.out.printf("List of users: \n%s", json);
    }
}
