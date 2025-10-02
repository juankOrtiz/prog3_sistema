package com.juan.sistema.models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Cuenta {
    private final IntegerProperty id;
    private final IntegerProperty usuarioId;
    private final StringProperty descripcion;
    private final DoubleProperty saldo;

    public Cuenta(int id, int usuarioId, String descripcion, double saldo) {
        this.id = new SimpleIntegerProperty(id);
        this.usuarioId = new SimpleIntegerProperty(usuarioId);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.saldo = new SimpleDoubleProperty(saldo);
    }

    public Cuenta(int usuarioId, String descripcion, double saldo) {
        this(-1, usuarioId, descripcion, saldo);
    }

    public IntegerProperty idProperty() { return id; }
    public IntegerProperty usuarioIdProperty() { return usuarioId; }
    public StringProperty descripcionProperty() { return descripcion; }
    public DoubleProperty saldoProperty() { return saldo; }

    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }

    public int getUsuarioId() { return usuarioId.get(); }
    public void setUsuarioId(int usuarioId) { this.usuarioId.set(usuarioId); }

    public String getDescripcion() { return descripcion.get(); }
    public void setDescripcion(String descripcion) { this.descripcion.set(descripcion); }

    public double getSaldo() { return saldo.get(); }
    public void setSaldo(double saldo) { this.saldo.set(saldo); }
}