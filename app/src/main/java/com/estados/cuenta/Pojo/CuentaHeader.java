package com.estados.cuenta.Pojo;

import android.os.Parcel;

import java.io.Serializable;

public class CuentaHeader extends ListItem implements Serializable {

    String moneda;

    public CuentaHeader(String moneda) {
        this.moneda = moneda;
    }


    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }
}
