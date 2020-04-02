package com.estados.cuenta.Interface;

import com.estados.cuenta.Pojo.Rubro;

import java.util.ArrayList;

public interface CuentaInterface  {

    interface CuentaVista{
        void mostrarToast(String mensaje);
        void mostrarClientes(ArrayList lClientes);
        void mostrarDescripcionRubro(ArrayList<Rubro> lRubros);
    }

    interface CuentaModelo{
        void buscarCliente(String cliente);
        void buscarRubro(String rubro);
    }

    interface CuentaPresentador{
        void enviarCliente(String cliente);
        void devolverCliente(ArrayList listaClientes);
        void mostrarMensaje(String mensaje);
        void enviarRubro(String rubro);
        void descripcionRubros(ArrayList<Rubro> lRubros);
    }

}
