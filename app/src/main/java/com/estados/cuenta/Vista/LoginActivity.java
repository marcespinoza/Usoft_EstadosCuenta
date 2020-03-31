package com.estados.cuenta.Vista;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
    EditText empresaText;
    Dialog dialog;
    SharedPreferences sharedPref;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.custompopup);
        progressBar = dialog.findViewById(R.id.progressBar);
        enviarConexion = dialog.findViewById(R.id.enviarempresa);
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
                empresaText.setText("");
                enviarConexion.setEnabled(true);
                mostrarDialogoConexion();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, CuentaActivity.class);
                startActivity(intent);
               /* loginUsuario(usuario.getText().toString(), clave.getText().toString());*/
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

    public void clearPopup(){

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
            empresaText.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            enviarConexion.setEnabled(true);
            empresaText.setEnabled(true);
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

    public void mostrarDialogoConexion(){

        enviarConexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorEmpresa.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                enviarConexion.setEnabled(false);
                empresaText.setEnabled(false);
                presentador.verificarEmpresa(empresaText.getText().toString());
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
