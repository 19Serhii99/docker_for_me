package com.nixsolutions.ppp.jdbc.dao;

import com.nixsolutions.ppp.jdbc.entity.Role;
import com.nixsolutions.ppp.jdbc.exception.CustomSqlException;
import com.nixsolutions.ppp.jdbc.exception.RoleAlreadyExistsException;
import com.nixsolutions.ppp.jdbc.exception.RoleNotFoundException;

/**
 * The {@code RoleDao} is responsible for querying the "Role" storage, containing
 * basic necessary operations to manipulate data of the storage
 *
 * @author Serhii Nesterov
 */
public interface RoleDao {

    /**
     * Creates a new {@code Role} in the database if it has not created yet. The
     * method omits the {@code role's id} as it uses {@code AUTO_INCREMENT} in order
     * to automatically generate the next id value
     *
     * @param role the role to be created
     * @throws NullPointerException       if the {@code role} or its {@code name} is {@code null}
     * @throws RoleAlreadyExistsException if the {@code role} already exists
     * @throws CustomSqlException         if any SQL error occurs
     */
    void create(Role role)
            throws RoleAlreadyExistsException, CustomSqlException;

    /**
     * Updates the existing {@code Role} in the database. The {@code role's id} and
     * {@code name} must be provided
     *
     * @param role the role to be updated
     * @throws NullPointerException       if the {@code role}, its {@code id} or {@code name} is null
     * @throws RoleAlreadyExistsException if the new name of the {@code role} is busy
     * @throws CustomSqlException         if any SQL error occurs
     */
    void update(Role role)
            throws RoleAlreadyExistsException, CustomSqlException;

    /**
     * Removes the {@code Role} from the database. The {@code Role} can be removed
     * only if there is no any user with this {@code Role}
     *
     * @param role the role to be removed
     * @throws NullPointerException          if the {@code role} or its {@code id} is null
     * @throws RoleNotFoundException         if the {@code role} not found
     * @throws UnsupportedOperationException if any user has this {@code role}
     * @throws CustomSqlException            if any SQL error occurs
     */
    void remove(Role role)
            throws RoleNotFoundException, UnsupportedOperationException,
            CustomSqlException;

    /**
     * Finds one role by the specified {@code name}
     *
     * @param name the name to search for the {@code Role}
     * @return the {@code Role} found by its name
     * @throws NullPointerException  if the {@code name} is null
     * @throws RoleNotFoundException if any {@code Role} not found
     * @throws CustomSqlException    if any SQL error occurs
     */
    Role findByName(String name)
            throws RoleNotFoundException, CustomSqlException;
}
