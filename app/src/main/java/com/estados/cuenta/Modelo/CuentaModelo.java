package com.estados.cuenta.Modelo;

import android.content.Context;
import android.content.SharedPreferences;

import com.estados.cuenta.Interface.CuentaInterface;
import com.estados.cuenta.Pojo.ListItem;
import com.estados.cuenta.Pojo.Cliente;
import com.estados.cuenta.Pojo.CuentaHeader;
import com.estados.cuenta.Pojo.CuentaItem;
import com.estados.cuenta.Pojo.Rubro;
import com.estados.cuenta.Vista.GlobalApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CuentaModelo implements CuentaInterface.CuentaModelo {

    public CuentaInterface.CuentaPresentador cPresentador;
    SharedPreferences sharedPref;
    SharedPreferences sharedPrefConexion;
    Context context;
    Cliente cli;
    String urlServidor = "";


    public CuentaModelo(CuentaInterface.CuentaPresentador cPresentador) {
        this.cPresentador = cPresentador;
        context = GlobalApplication.getContext();
        sharedPref = context.getSharedPreferences("datosesion", Context.MODE_PRIVATE);
        sharedPrefConexion = context.getSharedPreferences("datosconexion", Context.MODE_PRIVATE);
        urlServidor = sharedPrefConexion.getString("host","");
    }


    @Override
    public void buscarCliente(String cliente) {
        getCliente(cliente);
    }

    @Override
    public void buscarRubro(String rubro) {
        getRubrosDesc(rubro);
    }

    @Override
    public void obtenerMovimientos(String nrocuenta, String rubro) {
        getMovimientos(nrocuenta, rubro);
    }

    public void getRubrosDesc(String nrocuenta) {
        urlServidor = sharedPrefConexion.getString("host","");
        String empresa = sharedPrefConexion.getString("empresa","");
        String url = "http://"+urlServidor+":10701/api/index.php/api/getrubros";
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS).build();
        RequestBody formBody = new FormBody.Builder()
                .add("nrocuenta", nrocuenta)
                .add("empresa",empresa)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                 cPresentador.mostrarMensaje(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String mMessage = response.body().string();
                try {
                    JSONObject json = new JSONObject(mMessage);
                    String mensaje = json.getString("mensaje");
                    if (mensaje.equals("false")) {
                        cPresentador.mostrarMensaje("No se encontró rubro");
                    } else {
                        JSONObject object;
                        JSONArray jsonArray = (JSONArray) json.get("0");
                        ArrayList<Rubro> lRubros = new ArrayList<>();
                        for(int i=0; i < jsonArray.length(); i++){
                            object = jsonArray.getJSONObject(i);
                            Rubro rub = new Rubro();
                            String rubro = object.getString("rubro");
                            String descripcion = object.getString("descripcion");
                            rub.setNombre(rubro);
                            rub.setDescripcion(descripcion);
                            lRubros.add(rub);
                        }
                        cPresentador.descripcionRubros(lRubros);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getCliente(String cliente) {
        urlServidor = sharedPrefConexion.getString("host","");
        String empresa = sharedPrefConexion.getString("empresa","");
        String url = "http://"+urlServidor+":10701/api/index.php/api/getnrocuenta";
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS).build();
        RequestBody formBody = new FormBody.Builder()
                .add("cuenta", cliente)
                .add("empresa",empresa)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                cPresentador.mostrarMensaje(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String mMessage = response.body().string();
                try {
                    JSONObject json = new JSONObject(mMessage);
                    String mensaje = json.getString("mensaje");
                    if (mensaje.equals("false")) {
                        cPresentador.mostrarMensaje("No se encontró articulo");
                    } else {
                        JSONObject object;
                        JSONArray jsonArray = (JSONArray) json.get("0");
                        ArrayList<Cliente> clientesList = new ArrayList<>();
                        for(int i=0; i < jsonArray.length(); i++){
                            object = jsonArray.getJSONObject(i);
                            Cliente cl = new Cliente();
                            String nro_cuenta = object.getString("nro_cuenta");
                            String nombre = object.getString("nombre");
                            String razonsocial = object.getString("razonsocial");
                            String direccion = object.getString("direccion");
                            String localidad = object.getString("localidad");
                            String dpto = object.getString("departamento");
                            cl.setNrocuenta(nro_cuenta);
                            cl.setNombre(nombre);
                            cl.setRazonsocial(razonsocial);
                            cl.setDireccion(direccion);
                            cl.setLocalidad(localidad);
                            cl.setDepartamento(dpto);
                            clientesList.add(cl);
                        }
                        cPresentador.devolverCliente(clientesList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getMovimientos(String nrocuenta, String rubro) {
        urlServidor = sharedPrefConexion.getString("host","");
        String empresa = sharedPrefConexion.getString("empresa","");
        String url = "http://"+urlServidor+":10701/api/index.php/api/getmovimientos";
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS).build();
        RequestBody formBody = new FormBody.Builder()
                .add("nrocuenta", nrocuenta)
                .add("rubro", rubro)
                .add("empresa","aromacos")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                cPresentador.mostrarMensaje(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String mMessage = response.body().string();
                try {
                    JSONObject json = new JSONObject(mMessage);
                    String mensaje = json.getString("mensaje");
                    if (mensaje.equals("false")) {
                        cPresentador.mostrarMensaje("No se encontraron moviemientos");
                    } else {
                        JSONObject object;
                        JSONArray jsonArray = (JSONArray) json.get("0");
                        ArrayList<ListItem> cuentaList = new ArrayList<>();
                        String moneda = "";
                        for(int i=0; i < jsonArray.length(); i++){
                            object = jsonArray.getJSONObject(i);
                            String moneda_ = object.getString("moneda");
                            if(!moneda.equals(moneda_)){
                                moneda = moneda_;
                                CuentaHeader ch;
                                if(moneda.equals("1")){
                                    ch = new CuentaHeader("U$S");
                                }else{
                                    ch = new CuentaHeader("$");
                                }
                                cuentaList.add(ch);
                            }
                            CuentaItem ci = new CuentaItem();
                            String fecha = object.getString("fecha");
                            String importe = object.getString("importetotal");
                            String saldo = object.getString("sub_importetotal");
                            ci.setFecha(fecha);
                            ci.setImporte(importe);
                            ci.setSaldo(saldo);
                            cuentaList.add(ci);
                        }
                        cPresentador.retornarMovimientos(cuentaList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
