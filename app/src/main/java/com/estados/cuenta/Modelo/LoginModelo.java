package com.estados.cuenta.Modelo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.estados.cuenta.Interface.LoginInterface;
import com.estados.cuenta.Presentador.LoginPresentador;
import com.estados.cuenta.Vista.GlobalApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginModelo implements LoginInterface.Modelo {

    LoginInterface.Presentador presentador;
    Context context;
    SharedPreferences sharedPref;
    String bandera = "";

    public LoginModelo(LoginPresentador presentador){
        this.presentador=presentador;
        context = GlobalApplication.getContext();
        sharedPref = context.getSharedPreferences("datosconexion", Context.MODE_PRIVATE);
    }

    @Override
    public void enviarUsuario(String usuario, String clave) {
        loginRequest(usuario, clave);
    }

    @Override
    public void verificarEmpresa(String empresa) {
       new ConsultarEmpresa().execute(empresa);
    }


    private void loginRequest(String usuario, String contraseña) {
        String host = sharedPref.getString("host","");
        String empresa = sharedPref.getString("empresa","");
            String url = "http://"+host+":10701/api/index.php/api/getlogin";
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS).build();
            RequestBody formBody = new FormBody.Builder()
                    .add("usuario", usuario)
                    .add("clave", contraseña)
                    .add("empresa",empresa)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();


            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    presentador.mostrarError(e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    String mMessage = response.body().string();

                    try {
                        JSONObject json = new JSONObject(mMessage);
                        String mensaje = json.getString("mensaje");
                        if (mensaje.equals("false")) {
                            presentador.usuarioIncorrecto();
                        } else {
                            String usuario = json.getString("nombre");
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("usuario", usuario);
                            editor.commit();
                            presentador.devolverUsuario(usuario);
                        }
                    } catch (JSONException e) {
                        presentador.mostrarError("Error, intente nuevamente.");
                        e.printStackTrace();
                    }
                }
            });
      }

    class ConsultarEmpresa extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            try{
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                Connection connection = DriverManager.getConnection("jdbc:mysql://usoft.selfip.info:5806/usoft_mobile?useUnicode=true", "usoft_mobile", "orHfV3Cib5YoJZmEq4");
                String query = "SELECT host, habilitado FROM empresas WHERE empresa like '"+params[0]+"' and (sistema='AUTOCONSULTA' or sistema='LECTOR')";
                PreparedStatement stmt = connection.prepareStatement(query);
                /* stmt.setString(1, params[0]);*/
                ResultSet rs = stmt.executeQuery();
                if(rs.next()){
                    String host = rs.getString(1);
                    if(rs.getString(2).equals("N")){
                        bandera = "N";
                        return false;
                    }
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("empresa",params[0]);
                    editor.putString("host", host);
                    editor.commit();
                    return true;
                }else{
                    bandera = "vacio";
                    return false;
                }
            }catch(Exception e){
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(bandera.equals("vacio") && !result) {
                presentador.resultadoConexion(result, "No se encontró empresa.");
            }else if(bandera.equals("N") && !result){
                presentador.resultadoConexion(result, "Empresa no habilitada.");
            }else if(!result){
                presentador.resultadoConexion(result, "Error de conexión.");
            }else{
                presentador.resultadoConexion(result, "");
            }
        }

    }

}
