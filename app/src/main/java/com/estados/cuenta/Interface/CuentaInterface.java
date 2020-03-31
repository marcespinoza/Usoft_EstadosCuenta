package com.estados.cuenta.Interface;

import com.estados.cuenta.Pojo.Cliente;

import java.util.ArrayList;

public interface CuentaInterface  {

    interface CuentaVista{
        void mostrarToast(String mensaje);
        void mostrarClientes(ArrayList lClientes);
    }

    interface CuentaModelo{
        void buscarCliente(String cliente);
    }

    interface CuentaPresentador{
        void enviarCliente(String cliente);
        void devolverCliente(ArrayList listaClientes);
        void mostrarMensaje(String mensaje);
    }

}
