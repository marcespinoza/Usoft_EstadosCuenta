package com.estados.cuenta.Vista;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.estados.cuenta.BuildConfig;
import com.estados.cuenta.Interface.LoginInterface;
import com.estados.cuenta.Presentador.LoginPresentador;
import com.estados.cuenta.R;
import com.google.android.material.button.MaterialButton;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LoginActivity extends AppCompatActivity implements LoginInterface.Vista {

    LoginInterface.Presentador presentador;

    @BindView(R.id.login_button)
    MaterialButton login;
    @BindView(R.id.usuario)
    EditText usuario;
    @BindView(R.id.clave)
    EditText clave;
    @BindView(R.id.logologin)
    ImageView logoLogin;
    @BindView(R.id.androidversion)
    TextView androidversion;
    @BindView(R.id.versionname)
    TextView versionname;
    @BindView(R.id.cambiarempresa) TextView cambiarEmpresa;
    ProgressDialog pDialog;
    MaterialButton enviarConexion;
    ProgressBar progressBar;
    TextView errorEmpresa;
    EditText empresaText, claveText;
    Dialog dialog;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        //---Empresa dialog--///
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.custompopup);
        //------------------------//
        progressBar = dialog.findViewById(R.id.progressBar);
        enviarConexion = dialog.findViewById(R.id.enviarempresa);
        claveText = dialog.findViewById(R.id.clave);
        empresaText = dialog.findViewById(R.id.empresa);
        errorEmpresa = dialog.findViewById(R.id.textoError);
        errorEmpresa.setVisibility(View.INVISIBLE);
        ButterKnife.bind(this);
        androidversion.setText(android.os.Build.VERSION.RELEASE.substring(0, 1));
        versionname.setText(BuildConfig.VERSION_NAME);
        pDialog = new ProgressDialog(this);
        presentador = new LoginPresentador(this);
        sharedPref = this.getSharedPreferences("datosesion", Context.MODE_PRIVATE);
        boolean sesion = sharedPref.getBoolean("sesion",false);
        cambiarEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                empresaText.setEnabled(true);
                claveText.setEnabled(true);
                empresaText.setText("");
                claveText.setText("");
                enviarConexion.setEnabled(true);
                mostrarDialogoConexion();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(LoginActivity.this, CuentaActivity.class);
                startActivity(intent);*/
                loginUsuario(usuario.getText().toString(), clave.getText().toString());
            }
        });
        if(sesion){
            login();
        }else{
            mostrarDialogoConexion();
        }
    }

    @Override
    public void loginUsuario(String usuario, String clave) {
        pDialog.showProgressDialog("Espere por favor...");
        presentador.verificarUsuario(usuario, clave);
    }


    @Override
    public void mostrarExito(String usuari) {
        String usu = usuario.getText().toString();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("sesion", true);
        editor.putString("usuario",usu);
        editor.commit();
        pDialog.finishDialog();
        login();
    }

    @Override
    public void resultadoConexion(boolean b, String mensaje) {
        progressBar.setVisibility(View.INVISIBLE);
        if(b){
            dialog.dismiss();
        }else{
            claveText.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            enviarConexion.setEnabled(true);
            empresaText.setEnabled(true);
            claveText.setEnabled(true);
            errorEmpresa.setVisibility(View.VISIBLE);
            errorEmpresa.setText(mensaje);
        }
    }

    private void login(){
        String usu = sharedPref.getString("usuario", "");
        Intent intent = new Intent(this, CuentaActivity.class);
        if(usuario.getText().toString().equalsIgnoreCase("admin") || usu.equalsIgnoreCase("admin")){
            intent.putExtra("admin",true);
        }
        startActivity(intent);
        finish();
    }


    public static String sha256(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            Log.i("CLAVE", hexString.toString());
            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes("UTF-8"));
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        } catch(UnsupportedEncodingException ex){
        }
        return null;
    }

    public boolean checkHash(String clave, String empresa){
        if(clave.equals(sha256(MD5("usoft"+empresa)))){
            return true;
        }
        return false;
    }

    public void mostrarDialogoConexion(){

        enviarConexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorEmpresa.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                enviarConexion.setEnabled(false);
                empresaText.setEnabled(false);
                claveText.setEnabled(false);
                if(checkHash(claveText.getText().toString(), empresaText.getText().toString())){
                   presentador.verificarEmpresa(empresaText.getText().toString());
                }else{
                    resultadoConexion(false, "Clave errónea");
                }
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if(i == KeyEvent.KEYCODE_BACK){
                    finish();
                    return true;
                }
                return false;
            }
        });
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @Override
    public void mostrarError(String error) {
        pDialog.finishDialog();
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(LoginActivity.this, "Error: "+error, Toast.LENGTH_SHORT).show(); //Correcto
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
