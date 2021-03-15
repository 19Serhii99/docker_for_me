package com.nixsolutions.ppp.jdbc.entity;

public class Role extends AbstractEntity {
    public final static String TABLE = "role";
    private String name;

    public Role() {
    }

    public Role(Long id, String name) {
        setId(id);
        setName(name);
    }

    public Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Role{" + "name='" + name + '\'' + "} " + super.toString();
    }
}
