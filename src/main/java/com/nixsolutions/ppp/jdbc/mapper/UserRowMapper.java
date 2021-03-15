package com.nixsolutions.ppp.jdbc.mapper;

import com.nixsolutions.ppp.jdbc.entity.Role;
import com.nixsolutions.ppp.jdbc.entity.User;
import com.nixsolutions.ppp.jdbc.tool.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        Role role = new Role();
        role.setId(resultSet.getLong("role.id"));
        role.setName(resultSet.getString("role.name"));

        User user = new User();
        user.setId(resultSet.getLong("user.id"));
        user.setLogin(resultSet.getString("user.login"));
        user.setPassword(resultSet.getString("user.password"));
        user.setEmail(resultSet.getString("user.email"));
        user.setFirstName(resultSet.getString("user.first_name"));
        user.setLastName(resultSet.getString("user.last_name"));
        user.setBirthday(resultSet.getDate("user.birthday"));
        user.setRole(role);

        return user;
    }
}
