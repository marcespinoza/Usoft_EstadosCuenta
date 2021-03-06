package com.estados.cuenta.Vista;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.estados.cuenta.Adapter.MovimientoAdapter;
import com.estados.cuenta.Interface.PdfInterface;
import com.estados.cuenta.Pojo.CuentaItem;
import com.estados.cuenta.Pojo.ListItem;
import com.estados.cuenta.Presentador.PdfPresentador;
import com.estados.cuenta.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovimientoActivity extends AppCompatActivity implements PdfInterface.PdfVista {

    @BindView(R.id.toolbar_movimientos) Toolbar toolbar;
    @BindView(R.id.recycler_movimientos)  RecyclerView recyclerView;
    @BindView(R.id.main_layout_id) View view;
    @BindView(R.id.nro_cuenta) TextView nroCuenta;
    @BindView(R.id.nombre) TextView desc;
    @BindView(R.id.razon_social) TextView razonSocial;
    @BindView(R.id.rubro) TextView rubro;
    @BindView(R.id.fch_desde) TextView desde;
    @BindView(R.id.fch_hasta) TextView hasta;
    @BindView(R.id.desc_rubro) TextView descrubro;
    ArrayList<ListItem> lMovimientos;
    ArrayList<String> dataHeader;
    public PdfInterface.PdfPresentador pPresentador;
    String[] permissions= new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    public static final int MULTIPLE_PERMISSIONS = 10;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movimiento_activity);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(MovimientoActivity.this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Movimientos");
        pPresentador =  new PdfPresentador(this);
        dataHeader = getIntent().getExtras().getStringArrayList("header");
        //--------Datos del header-----//
        nroCuenta.setText(dataHeader.get(0));
        desc.setText(dataHeader.get(1));
        razonSocial.setText(dataHeader.get(2));
        desde.setText(dataHeader.get(3));
        hasta.setText(dataHeader.get(4));
        rubro.setText(dataHeader.get(5));
        descrubro.setText(dataHeader.get(6));
        //-------------//
        lMovimientos =GlobalApplication.getInstance().getCuentas();
        MovimientoAdapter movimientoAdapter = new MovimientoAdapter(lMovimientos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(movimientoAdapter);
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermission();
        }
    }



    @OnClick({R.id.pdf, R.id.excel})
    public void generarPdf(View view){
        progressDialog.showProgressDialog("Generando documento");
        switch (view.getId()) {
            case R.id.pdf:
                pPresentador.sendList(lMovimientos, dataHeader, true);
                break;
            case R.id.excel:
                pPresentador.sendList(lMovimientos, dataHeader, false);
                break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)     {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkPermission() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(this,p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS:{
                if (grantResults.length > 0) {
                    String permissionsDenied = "";
                    for (String per : permissions) {
                        if(grantResults[0] == PackageManager.PERMISSION_DENIED){
                            permissionsDenied += "\n" + per;
                            showDialogNotCancelable("Permissions mandatory",
                                    "All the permissions are required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            checkPermission();
                                        }
                                    });
                        }
                    }
                }
                return;
            }
        }
    }

    private void showDialogNotCancelable(String title, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setCancelable(false)
                .create()
                .show();
    }

    @Override
    public void showSnackBar(String filename) {
            progressDialog.finishDialog();
            final Snackbar snackbar = Snackbar.make(view, "Documento generado", Snackbar.LENGTH_LONG);
            snackbar.setAction("ABRIR", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File file = null;
                    file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+filename);
                    if(file.exists()) {
                        Intent target = new Intent(Intent.ACTION_VIEW);
                        if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.N) {
                            target.setDataAndType(Uri.parse(file.toString()), "application/pdf");
                        } else{
                            target.setDataAndType(Uri.fromFile(file), "application/pdf");
                        }
                        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                        Intent intent = Intent.createChooser(target, "Open File");
                        try {
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            Log.i("EXCEPTION", e.getMessage());
                        }
                    }
                }
            });

            snackbar.show();
        }

}
