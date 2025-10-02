package com.juan.sistema.models;

import java.sql.Timestamp;

public class Movimiento {
    private int id;
    private int cuentaOrigenId;
    private int cuentaDestinoId;
    private double monto;
    private String tipoMovimiento;
    private Timestamp fechaMovimiento;

    public Movimiento(int id, int cuentaOrigenId, int cuentaDestinoId, double monto, String tipoMovimiento, Timestamp fechaMovimiento) {
        this.id = id;
        this.cuentaOrigenId = cuentaOrigenId;
        this.cuentaDestinoId = cuentaDestinoId;
        this.monto = monto;
        this.tipoMovimiento = tipoMovimiento;
        this.fechaMovimiento = fechaMovimiento;
    }

    public Movimiento(int cuentaOrigenId, int cuentaDestinoId, double monto, String tipoMovimiento) {
        this(-1, cuentaOrigenId, cuentaDestinoId, monto, tipoMovimiento, null);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCuentaOrigenId() {
        return cuentaOrigenId;
    }

    public void setCuentaOrigenId(int cuentaOrigenId) {
        this.cuentaOrigenId = cuentaOrigenId;
    }

    public int getCuentaDestinoId() {
        return cuentaDestinoId;
    }

    public void setCuentaDestinoId(int cuentaDestinoId) {
        this.cuentaDestinoId = cuentaDestinoId;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public Timestamp getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(Timestamp fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }
}