package com.nixsolutions.ppp.jdbc.mapper;

import com.nixsolutions.ppp.jdbc.entity.Role;
import com.nixsolutions.ppp.jdbc.tool.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleRowMapper implements RowMapper<Role> {

    @Override
    public Role mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        Long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        return new Role(id, name);
    }
}
