package com.estados.cuenta.Presentador;

import com.estados.cuenta.Interface.CuentaInterface;
import com.estados.cuenta.Modelo.CuentaModelo;
import com.estados.cuenta.Vista.CuentaActivity;

import java.util.ArrayList;

public class CuentaPresentador implements CuentaInterface.CuentaPresentador {

    public CuentaInterface.CuentaModelo cModelo;
    public CuentaInterface.CuentaVista cVista;

    public CuentaPresentador(CuentaActivity cVista) {
        this.cVista = cVista;
        cModelo = new CuentaModelo(this);
    }

    @Override
    public void enviarCliente(String cliente) {
        cModelo.buscarCliente(cliente);
    }

    @Override
    public void devolverCliente(ArrayList lClientes) {
        cVista.mostrarClientes(lClientes);
    }

    @Override
    public void mostrarMensaje(String mensaje) {

    }
}
