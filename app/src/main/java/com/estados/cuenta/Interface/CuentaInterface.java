package com.estados.cuenta.Interface;

import com.estados.cuenta.Pojo.ListItem;
import com.estados.cuenta.Pojo.Rubro;

import java.util.ArrayList;

public interface CuentaInterface  {

    interface CuentaVista{
        void mostrarToast(String mensaje);
        void mostrarClientes(ArrayList lClientes);
        void mostrarDescripcionRubro(ArrayList<Rubro> lRubros);
        void mostrarMovimientos(ArrayList<ListItem> cuentas);
    }

    interface CuentaModelo{
        void buscarCliente(String cliente);
        void buscarRubro(String rubro);
        void obtenerMovimientos(String nrocuenta, String rubro);
    }

    interface CuentaPresentador{
        void enviarCliente(String cliente);
        void devolverCliente(ArrayList listaClientes);
        void mostrarMensaje(String mensaje);
        void enviarRubro(String rubro);
        void descripcionRubros(ArrayList<Rubro> lRubros);
        void obtenerMovimientos(String nrocuenta, String rubro);
        void retornarMovimientos(ArrayList<ListItem> cuentas);
    }

}
