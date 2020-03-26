package com.estados.cuenta.Presentador;


import com.estados.cuenta.Interface.LoginInterface;
import com.estados.cuenta.Modelo.LoginModelo;
import com.estados.cuenta.Vista.LoginActivity;

public class LoginPresentador implements LoginInterface.Presentador {

    LoginInterface.Vista vista;
    LoginInterface.Modelo modelo;

    public LoginPresentador(LoginActivity vista){
        this.vista = vista;
        modelo = new LoginModelo(this);
    }

    @Override
    public void verificarUsuario(String usuario, String clave) {
        if(usuario.equals("") || clave.equals("")){
            vista.mostrarError("Complete todos los campos");
        }else{
            modelo.enviarUsuario(usuario, clave);
        }
    }

    @Override
    public void usuarioIncorrecto() {
        vista.mostrarError("Usuario/contraseña incorrectos");
    }

    @Override
    public void mostrarError(String error) {
        vista.mostrarError(error);
    }

    @Override
    public void verificarEmpresa(String empresa) {
        modelo.verificarEmpresa(empresa);
    }

    @Override
    public void resultadoConexion(boolean b, String mensaje) {
        vista.resultadoConexion(b, mensaje);
    }

    @Override
    public void devolverUsuario(String nombre) {
        vista.mostrarExito(nombre);
    }

}
