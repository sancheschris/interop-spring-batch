package com.batch.BatchSQLExample.processor;

import com.batch.BatchSQLExample.model.User;
import org.springframework.batch.item.ItemProcessor;

public class UserItemProcessor implements ItemProcessor<User, User> {

    @Override
    public User process(User user) throws Exception {

        Integer id = user.getId();
        String name = user.getName().toUpperCase();

        user = new User(id, name);
        return user;
    }
}
