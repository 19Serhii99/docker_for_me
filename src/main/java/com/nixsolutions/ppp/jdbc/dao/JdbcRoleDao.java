package com.nixsolutions.ppp.jdbc.dao;

import com.nixsolutions.ppp.jdbc.entity.Role;
import com.nixsolutions.ppp.jdbc.entity.User;
import com.nixsolutions.ppp.jdbc.exception.RoleAlreadyExistsException;
import com.nixsolutions.ppp.jdbc.exception.RoleNotFoundException;
import com.nixsolutions.ppp.jdbc.mapper.RoleRowMapper;
import com.nixsolutions.ppp.jdbc.tool.Session;
import com.nixsolutions.ppp.jdbc.tool.TransactionTemplate;

import java.util.Objects;
import java.util.Optional;

/**
 * @author Serhii Nesterov
 */
public class JdbcRoleDao extends AbstractJdbcDao implements RoleDao {

    /**
     * The {@code INSERT INTO} query to create a new role <br>
     * {@code id} is excluded as {@code AUTO_INCREMENT} is used
     */
    private final static String CREATE = "INSERT INTO role (name) VALUES (?)";

    /**
     * The {@code UPDATE} query to update an existing role, namely its name.
     * The role's id is used to figure out what exact role needs to be updated
     */
    private final static String UPDATE = "UPDATE role SET name = ? WHERE id = ?";

    /**
     * The {@code DELETE} query to delete an existing role from the database by its
     * id
     */
    private final static String REMOVE = "DELETE FROM role WHERE id = ?";

    /**
     * The {@code SELECT} query to find one role by the specified name
     */
    private final static String FIND_BY_NAME = "SELECT id, name FROM role WHERE name = ?";

    /**
     * The {@code TransactionTemplate} is used to execute queries within a single
     * transaction
     */
    private final TransactionTemplate template;

    /**
     * Constructs a new instance of the {@code JdbcRoleDao} class
     *
     * @param template the template used to execute SQL statements within a transaction
     * @throws NullPointerException if the {@code template} is null
     */
    public JdbcRoleDao(TransactionTemplate template) {
        this.template = Objects.requireNonNull(template);
    }

    @Override
    public void create(Role role) {
        Objects.requireNonNull(role);
        Objects.requireNonNull(role.getName());
        template.execute(super::createConnection, session -> {
            requireRoleAbsence(session, role.getName());
            session.execute(CREATE, role.getName());
        });
    }

    @Override
    public void update(Role role) {
        Objects.requireNonNull(role);
        Objects.requireNonNull(role.getId());
        Objects.requireNonNull(role.getName());
        template.execute(super::createConnection, session -> {
            requireRoleExistence(session, role.getId());
            ensureNobodyHasRoleOf(session, role);
            session.execute(UPDATE, role.getName(), role.getId());
        });
    }

    @Override
    public void remove(Role role) {
        Objects.requireNonNull(role);
        Objects.requireNonNull(role.getId());
        template.execute(super::createConnection, session -> {
            requireRoleExistence(session, role.getId());
            requireNoReferenceToRole(session, role);
            session.execute(REMOVE, role.getId());
        });
    }

    @Override
    public Role findByName(String name) {
        Objects.requireNonNull(name);
        return template.executeAndReturn(super::createConnection,
                session -> findOptionalByName(session, name))
                .orElseThrow(RoleNotFoundException::new);
    }

    private Optional<Role> findOptionalByName(Session session, String name) {
        return session.queryForObject(FIND_BY_NAME, new RoleRowMapper(), name);
    }

    private void requireRoleExistence(Session session, Long id) {
        if (!hasRole(session, "id", id)) {
            throw new RoleNotFoundException();
        }
    }

    private void requireRoleAbsence(Session session, String name) {
        if (hasRole(session, "name", name)) {
            throw new RoleAlreadyExistsException();
        }
    }

    private void requireNoReferenceToRole(Session session, Role role) {
        if (session.exists(User.TABLE, "role_id", role.getId())) {
            throw new UnsupportedOperationException();
        }
    }

    private boolean hasRole(Session session, String field, Object value) {
        return session.exists(Role.TABLE, field, value);
    }

    private void ensureNobodyHasRoleOf(Session session, Role role) {
        findOptionalByName(session, role.getName())
                .ifPresent(other -> {
                    throw new RoleAlreadyExistsException();
                });
    }
}
