package com.estados.cuenta.Presentador;

import com.estados.cuenta.Interface.CuentaInterface;
import com.estados.cuenta.Pojo.ListItem;
import com.estados.cuenta.Modelo.CuentaModelo;
import com.estados.cuenta.Pojo.Rubro;
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
        cVista.mostrarToast(mensaje);
    }

    @Override
    public void enviarRubro(String rubro) {
        cModelo.buscarRubro(rubro);
    }

    @Override
    public void descripcionRubros(ArrayList<Rubro> lRubros) {
        cVista.mostrarDescripcionRubro(lRubros);
    }

    @Override
    public void obtenerMovimientos(String nrocuenta, String rubro, String f_inicial, String f_final, boolean b, String orden) {
        cModelo.obtenerMovimientos(nrocuenta, rubro, f_inicial, f_final, b, orden);
    }

    @Override
    public void retornarMovimientos(ArrayList<ListItem> cuentas) {
        cVista.mostrarMovimientos(cuentas);
    }
}
