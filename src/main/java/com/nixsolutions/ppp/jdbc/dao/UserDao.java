package com.nixsolutions.ppp.jdbc.dao;

import com.nixsolutions.ppp.jdbc.entity.User;
import com.nixsolutions.ppp.jdbc.exception.CustomSqlException;
import com.nixsolutions.ppp.jdbc.exception.InvalidBirthdayException;
import com.nixsolutions.ppp.jdbc.exception.RoleNotFoundException;
import com.nixsolutions.ppp.jdbc.exception.UserAlreadyExistsException;
import com.nixsolutions.ppp.jdbc.exception.UserNotFoundException;

import java.util.List;

/**
 * The {@code UserDao} is responsible for querying the "User" storage, containing
 * basic necessary operations to manipulate data of the storage
 *
 * @author Serhii Nesterov
 */
public interface UserDao {

    /**
     * Creates a new {@code User} in the database. {@code user's id} is ignored
     * as {@code AUTO_INCREMENT} is used to generate a new identification
     *
     * @param user the user to be created
     * @throws NullPointerException       if the {@code user} or any of its not null fields is null
     * @throws RoleNotFoundException      if the {@code user's role} not found
     * @throws UserAlreadyExistsException if the specified {@code login} or {@code email} already exists
     * @throws InvalidBirthdayException   if the specified {@code birthday} is invalid (later than the current date)
     * @throws CustomSqlException         if any SQL error occurs
     */
    void create(User user)
            throws RoleNotFoundException, UserAlreadyExistsException,
            InvalidBirthdayException, CustomSqlException;

    /**
     * Updates the existing {@code user} in the database. The {@code user's id} and its
     * all mandatory fields have to be provided
     *
     * @param user the user to be updated
     * @throws NullPointerException       if the {@code user} or any of its not null fields is null
     * @throws RoleNotFoundException      if the {@code user's role} not found
     * @throws UserNotFoundException      if the {@code user's id} is wrong
     * @throws UserAlreadyExistsException if another user has the specified {@code login} or {@code email}
     * @throws InvalidBirthdayException   if the specified {@code birthday} is invalid (later than the current date)
     * @throws CustomSqlException         if any SQL error occurs
     */
    void update(User user) throws RoleNotFoundException, UserNotFoundException,
            UserAlreadyExistsException, InvalidBirthdayException,
            CustomSqlException;

    /**
     * Removes the existing {@code user} in the database. The {@code user's id} is
     * required
     *
     * @param user the user to be removed
     * @throws NullPointerException  if the {@code user} or its {@code id} is null
     * @throws UserNotFoundException if the {@code user's id} is wrong
     * @throws CustomSqlException    if any SQL error occurs
     */
    void remove(User user) throws UserNotFoundException, CustomSqlException;

    /**
     * Finds and returns all the users in the database
     *
     * @return all the users in the database
     * @throws CustomSqlException if any SQL error occurs
     * @see #findByLogin(String)
     * @see #findByEmail(String)
     */
    List<User> findAll() throws CustomSqlException;

    /**
     * Finds and return the {@code User} by its login
     *
     * @param login the login to search by
     * @return the {@code User} fetched with the query
     * @throws UserNotFoundException if any user not found
     * @throws CustomSqlException    if any SQL error occurs
     * @see #findByEmail(String) 
     */
    User findByLogin(String login)
            throws UserNotFoundException, CustomSqlException;

    /**
     * Finds and return the {@code User} by its email
     *
     * @param email the email to search by
     * @return the {@code User} fetched with the query
     * @throws UserNotFoundException if any user not found
     * @throws CustomSqlException    if any SQL error occurs
     * @see #findByLogin(String)
     */
    User findByEmail(String email)
            throws UserNotFoundException, CustomSqlException;
}
