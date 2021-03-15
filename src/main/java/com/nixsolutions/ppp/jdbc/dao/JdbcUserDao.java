package com.nixsolutions.ppp.jdbc.dao;

import com.nixsolutions.ppp.jdbc.entity.Role;
import com.nixsolutions.ppp.jdbc.entity.User;
import com.nixsolutions.ppp.jdbc.exception.CustomSqlException;
import com.nixsolutions.ppp.jdbc.exception.InvalidBirthdayException;
import com.nixsolutions.ppp.jdbc.exception.RoleNotFoundException;
import com.nixsolutions.ppp.jdbc.exception.UserAlreadyExistsException;
import com.nixsolutions.ppp.jdbc.exception.UserNotFoundException;
import com.nixsolutions.ppp.jdbc.mapper.UserRowMapper;
import com.nixsolutions.ppp.jdbc.tool.PreparedStatementSetter;
import com.nixsolutions.ppp.jdbc.tool.Session;
import com.nixsolutions.ppp.jdbc.tool.TransactionTemplate;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Serhii Nesterov
 */
public class JdbcUserDao extends AbstractJdbcDao implements UserDao {

    private final static String CREATE =
            "INSERT INTO user (login, password, email, first_name, last_name, birthday, role_id) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private final static String FIND_ALL =
            "SELECT u.id, u.login, u.password, u.email, u.first_name, u.last_name, u.birthday, r.id, r.name "
                    + "FROM user u LEFT JOIN role r ON u.role_id = r.id";

    private final static String FIND_BY_LOGIN = FIND_ALL + " WHERE u.login = ?";

    private final static String FIND_BY_EMAIL = FIND_ALL + " WHERE u.email = ?";

    private final static String UPDATE =
            "UPDATE user SET login = ?, password = ?, email = ?, first_name = ?, last_name = ?, "
                    + "birthday = ?, role_id = ? WHERE id = ?";

    private final static String REMOVE = "DELETE FROM user WHERE id = ?";

    /**
     * The {@code TransactionTemplate} is used to execute queries within a single
     * transaction
     */
    private final TransactionTemplate template;

    /**
     * Constructs a new instance of the {@code JdbcUserDao} class
     *
     * @param template the template used to execute SQL statements within a transaction
     * @throws NullPointerException if the {@code template} is null
     */
    public JdbcUserDao(TransactionTemplate template) {
        this.template = Objects.requireNonNull(template);
    }

    @Override
    public void create(User user) {
        requireNonNull(user);
        requireValidBirthday(user.getBirthday());
        template.execute(super::createConnection, session -> {
            requireRoleExistence(session, user.getRole());
            requireLoginAbsence(session, user.getLogin());
            requireEmailAbsence(session, user.getEmail());
            session.execute(CREATE, createInsertionSetter(user));
        });
    }

    @Override
    public void update(User user) {
        requireNonNull(user);
        requireValidBirthday(user.getBirthday());
        Objects.requireNonNull(user.getId());
        template.execute(super::createConnection, session -> {
            requireUserExistence(session, user);
            requireRoleExistence(session, user.getRole());
            ensureNobodyHasLoginOf(session, user);
            ensureNobodyHasEmailOf(session, user);
            session.execute(UPDATE, createUpdateSetter(user));
        });
    }

    @Override
    public void remove(User user) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(user.getId());
        template.execute(super::createConnection, session -> {
            requireUserExistence(session, user);
            session.execute(REMOVE, user.getId());
        });
    }

    @Override
    public List<User> findAll() {
        return template.executeAndReturn(super::createConnection,
                    session -> session.queryForList(FIND_ALL, new UserRowMapper()));
    }

    @Override
    public User findByLogin(String login) {
        return template.executeAndReturn(super::createConnection,
                session -> findOptionalByLogin(session, login))
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User findByEmail(String email) {
        return template.executeAndReturn(super::createConnection,
                session -> findOptionalByEmail(session, email))
                .orElseThrow(UserNotFoundException::new);
    }

    private void ensureNobodyHasLoginOf(Session session, User user) {
        findOptionalByLogin(session, user.getLogin())
                .ifPresent(other -> {
                    if (!other.equals(user)) {
                        throw new UserAlreadyExistsException("This login is busy");
                    }
                });
    }

    private void ensureNobodyHasEmailOf(Session session, User user) {
        findOptionalByEmail(session, user.getEmail())
                .ifPresent(other -> {
                    if (!other.equals(user)) {
                        throw new UserAlreadyExistsException("This email is busy");
                    }
                });
    }

    private Optional<User> findOptionalByLogin(Session session, String login) {
        return findByField(session, FIND_BY_LOGIN, login);
    }

    private Optional<User> findOptionalByEmail(Session session, String email) {
        return findByField(session, FIND_BY_EMAIL, email);
    }

    private Optional<User> findByField(Session session, String query, String field) {
        return session.queryForObject(query, new UserRowMapper(), field);
    }

    /**
     * Checks all of the not null fields if they are null. If so, then the method
     * throws {@code NullPointerException}
     *
     * @param user the user to be checked for the not null fields
     * @throws NullPointerException if one of the not null fields is null
     */
    private void requireNonNull(User user) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(user.getLogin());
        Objects.requireNonNull(user.getPassword());
        Objects.requireNonNull(user.getEmail());
        Objects.requireNonNull(user.getFirstName());
        Objects.requireNonNull(user.getLastName());
        Objects.requireNonNull(user.getRole());
        Objects.requireNonNull(user.getRole().getId());
    }

    /**
     * Validates the user's birthday
     *
     * @param date the date to be validated
     * @throws InvalidBirthdayException if the {@code date} is later than the current date
     */
    private void requireValidBirthday(Date date) {
        if (date != null && date.after(new java.util.Date())) {
            throw new InvalidBirthdayException("Birthday cannot be later than the current date");
        }
    }

    /**
     * Verifies whether or not the {@code role} exists
     *
     * @param session the session to execute queries
     * @param role    the to be verified
     * @throws RoleNotFoundException if the {@code role} does not exist
     * @throws CustomSqlException    if any SQL error occurs
     */
    private void requireRoleExistence(Session session, Role role) {
        if (!session.exists(Role.TABLE, "id", role.getId())) {
            throw new RoleNotFoundException();
        }
    }

    private void requireUserExistence(Session session, User user) {
        if (!session.exists(User.TABLE, "id", user.getId())) {
            throw new UserNotFoundException();
        }
    }

    private void requireLoginAbsence(Session session, String login) {
        requireUserAbsence(session, "login", login);
    }

    private void requireEmailAbsence(Session session, String email) {
        requireUserAbsence(session, "email", email);
    }

    private void requireUserAbsence(Session session, String field, Object value) {
        if (session.exists(User.TABLE, field, value)) {
            throw new UserAlreadyExistsException();
        }
    }

    private PreparedStatementSetter createInsertionSetter(User user) {
        return statement -> fillFields(statement, user);
    }

    private PreparedStatementSetter createUpdateSetter(User user) {
        return statement -> {
            fillFields(statement, user);
            statement.setLong(8, user.getId());
        };
    }

    private void fillFields(PreparedStatement statement, User user)
            throws SQLException {
        statement.setString(1, user.getLogin());
        statement.setString(2, user.getPassword());
        statement.setString(3, user.getEmail());
        statement.setString(4, user.getFirstName());
        statement.setString(5, user.getLastName());
        statement.setDate(6, user.getBirthday());
        statement.setLong(7, user.getRole().getId());
    }
}
