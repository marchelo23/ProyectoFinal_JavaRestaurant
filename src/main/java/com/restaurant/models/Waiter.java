package com.restaurant.models;

/**
 * Clase Waiter que hereda de Person.
 * Representa un mesero del restaurante.
 */
public class Waiter extends Person {
    private String employeeId;

    public Waiter() {
        super();
    }

    public Waiter(String id, String name, String phone, String employeeId) {
        super(id, name, phone);
        this.employeeId = employeeId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public String toString() {
        return "Waiter{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", employeeId='" + employeeId + '\'' +
                '}';
    }
}
