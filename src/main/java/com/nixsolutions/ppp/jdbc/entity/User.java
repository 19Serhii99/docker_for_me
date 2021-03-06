package com.nixsolutions.ppp.jdbc.entity;

import java.sql.Date;

public class User extends AbstractEntity {
    public final static String TABLE = "user";
    private String login;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private Date birthday;
    private Role role;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" + "login='" + login + '\'' + ", password='" + password
                + '\'' + ", email='" + email + '\'' + ", firstName='"
                + firstName + '\'' + ", lastName='" + lastName + '\''
                + ", birthday=" + birthday + ", role=" + role + "} " + super
                .toString();
    }
}
