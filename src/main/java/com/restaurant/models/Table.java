package com.restaurant.models;

/**
 * Clase Table que representa una mesa del restaurante.
 */
public class Table {
    private int tableNumber;
    private int capacity;
    private boolean isOccupied;

    public Table() {
    }

    public Table(int tableNumber, int capacity) {
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.isOccupied = false;
    }

    public Table(int tableNumber, int capacity, boolean isOccupied) {
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.isOccupied = isOccupied;
    }

    public void occupy() {
        this.isOccupied = true;
    }

    public void free() {
        this.isOccupied = false;
    }

    // Getters and Setters
    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    @Override
    public String toString() {
        String status = isOccupied ? "Ocupada" : "Disponible";
        return "Mesa " + tableNumber + " (Capacidad: " + capacity + ", Estado: " + status + ")";
    }
}
