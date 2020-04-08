package com.estados.cuenta.Pojo;


import android.os.Parcel;

import java.io.Serializable;

public class CuentaItem extends ListItem implements Serializable {

    String moneda;
    String fecha;
    String importe;
    String saldo;


    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getImporte() {
        return importe;
    }

    public void setImporte(String importe) {
        this.importe = importe;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

}
