package com.estados.cuenta.Vista;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.estados.cuenta.Adapter.AutoCompleteClienteAdapter;
import com.estados.cuenta.Adapter.SpinnerRubroAdapter;
import com.estados.cuenta.Interface.CuentaInterface;
import com.estados.cuenta.Pojo.ListItem;
import com.estados.cuenta.Pojo.Cliente;
import com.estados.cuenta.Pojo.Rubro;
import com.estados.cuenta.Presentador.CuentaPresentador;
import com.estados.cuenta.R;
import com.google.android.material.button.MaterialButton;
import com.livefront.bridge.BuildConfig;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CuentaActivity extends AppCompatActivity implements CuentaInterface.CuentaVista{

    @BindView(R.id.calendarioinicial)ImageButton cInicial;
    @BindView(R.id.calendariofinal) ImageButton cFinal;
    @BindView(R.id.inicialDate) EditText iDate;
    @BindView(R.id.finalDate) EditText fDate;
    @BindView(R.id.searchcliente) ImageButton searchCliente;
    @BindView(R.id.cliente) AutoCompleteTextView autocompleteCliente;
    @BindView(R.id.descripcion) EditText descripcion;
    @BindView(R.id.razonsocial) EditText razonsocial;
    @BindView(R.id.cleancliente) ImageButton limpiarCliente;
    @BindView(R.id.rubro_spinner) AppCompatSpinner rSpinner;
    @BindView(R.id.radiogroup) RadioGroup radioGroup;
    @BindView(R.id.radiototal) RadioButton radioTotal;
    @BindView(R.id.radiopendiente) RadioButton radioPendiente;
    @BindView(R.id.radioemision) RadioButton radioEmision;
    @BindView(R.id.radiovto) RadioButton radioVto;
    @BindView(R.id.buscar) MaterialButton buscar;
    @BindView(R.id.androidversion2) TextView androidversion;
    @BindView(R.id.versionname2) TextView versionname;
    String nrocuenta = "";
    private int mYear, mMonth, mDay;
    public CuentaInterface.CuentaPresentador cPresentador;
    ProgressDialog pdialog;
    SharedPreferences sharedPref;
    SpinnerRubroAdapter spinnerRubroAdapter;
    ArrayList<Rubro> lRubros = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cuenta_activity);
        ButterKnife.bind(this);
        androidversion.setText(android.os.Build.VERSION.RELEASE.substring(0, 1));
        versionname.setText(BuildConfig.VERSION_NAME);
        lRubros.add(new Rubro("Rubro","Rubro"));
        pdialog = new ProgressDialog(this);
        buscar.setEnabled(false);
        sharedPref = getSharedPreferences("datosesion", Context.MODE_PRIVATE);
        cPresentador = new CuentaPresentador(this);
        spinnerRubroAdapter = new SpinnerRubroAdapter(this, R.layout.spinner, lRubros);
        searchCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cl = autocompleteCliente.getText().toString();
                if(!cl.isEmpty())
                enviarCliente(cl);
                showDialog("Obteniendo clientes..");
            }
        });
        limpiarCliente.setEnabled(false);
        limpiarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limpiarDetalle();
            }
        });
        autocompleteCliente.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!autocompleteCliente.getText().toString().equals("")){
                    limpiarCliente.setEnabled(true);
                }else{
                    limpiarCliente.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        rSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        rSpinner.setAdapter(spinnerRubroAdapter);
        rSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Rubro rubro = spinnerRubroAdapter.getItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radiototal:
                        cInicial.setEnabled(true);
                        changeBorder();
                        break;
                    case R.id.radiopendiente:
                        cInicial.setEnabled(false);
                        iDate.setText("");
                        changeBorder();
                        break;
                }
            }
        });
        setDate();
    }

    public void changeBorder(){
        iDate.setBackgroundColor(Color.TRANSPARENT);
        fDate.setBackgroundColor(Color.TRANSPARENT);
    }

    private void setDate(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = simpleDateFormat.format(c);
        fDate.setText(formattedDate);
    }

    public void limpiarDetalle(){
        autocompleteCliente.setText("");
        descripcion.setText("");
        razonsocial.setText("");
        spinnerRubroAdapter.clear();
        iDate.setText("");
        fDate.setText("");
        buscar.setEnabled(false);
        spinnerRubroAdapter.add(new Rubro("Rubro","Rubro"));
        setDate();
    }

    public void buscarDescripcionRubros(String rubro){
        cPresentador.enviarRubro(rubro);
    }

    @OnClick(R.id.buscar)
    public void movimientos(){
        String orden = "E";
        if(!radioEmision.isChecked())
            orden = "A";
        if(radioTotal.isChecked()){
            if(checkTotal()){
                pdialog.showProgressDialog("Obteniendo movimientos");
                Rubro rubro = (Rubro) rSpinner.getSelectedItem();
                cPresentador.obtenerMovimientos(nrocuenta, rubro.getNombre(), iDate.getText().toString(), fDate.getText().toString(), true, orden);
            }
        }else if(radioPendiente.isChecked()){
            if(checkPendiente()){
                pdialog.showProgressDialog("Obteniendo movimientos");
                Rubro rubro = (Rubro) rSpinner.getSelectedItem();
                cPresentador.obtenerMovimientos(nrocuenta, rubro.getNombre(), iDate.getText().toString(), fDate.getText().toString(), false, orden);
            }
        }
    }

    public boolean checkTotal(){
        if(!iDate.getText().toString().isEmpty() && !fDate.getText().toString().isEmpty()){
            iDate.setBackgroundResource(android.R.color.transparent);
            fDate.setBackgroundResource(android.R.color.transparent);
            return true;
        }else{
            if(iDate.getText().toString().isEmpty()){
                iDate.setBackgroundResource(R.drawable.red_border);
            }
            if(fDate.getText().toString().isEmpty()){
                fDate.setBackgroundResource(R.drawable.red_border);
            }
            return false;
        }
    }

    public boolean checkPendiente(){
        if(fDate.getText().toString().isEmpty()){
            fDate.setBackgroundResource(R.drawable.red_border);
            return false;
        }else{
            return true;
        }
    }

    @OnClick({R.id.calendarioinicial, R.id.calendariofinal})
    public void showCalendar(View v){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = null;
        switch(v.getId()){
            case R.id.calendarioinicial:
                datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        iDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        iDate.setBackgroundColor(Color.TRANSPARENT);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
                break;
            case R.id.calendariofinal:
                datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        fDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        fDate.setBackgroundColor(Color.TRANSPARENT);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
                break;
        }
        datePickerDialog.show();
    }

    @OnClick(R.id.logout)
    public void logout(){
        SharedPreferences.Editor editor =  sharedPref.edit();
        editor.putBoolean("sesion",false);
        editor.commit();
        Intent i = new Intent(CuentaActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    public void enviarCliente(String cliente){
        cPresentador.enviarCliente(cliente);
    }

    public void showDialog(String mensaje){
        runOnUiThread(new Runnable() {
            public void run() {
                pdialog.showProgressDialog(mensaje);
            }
        });
    }

    @Override
    public void mostrarToast(String mensaje) {
        pdialog.finishDialog();
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(CuentaActivity.this, mensaje, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void mostrarClientes(ArrayList lClientes) {
        pdialog.finishDialog();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                autocompleteCliente.setThreshold(1);
                autocompleteCliente.setAdapter(new AutoCompleteClienteAdapter(getApplicationContext(), R.layout.autocompletecliente_row, lClientes));
                autocompleteCliente.showDropDown();
                autocompleteCliente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Cliente cliente = (Cliente) adapterView.getItemAtPosition(i);
                        descripcion.setText(cliente.getNombre());
                        razonsocial.setText(cliente.getRazonsocial());
                        nrocuenta = cliente.getNrocuenta();
                        autocompleteCliente.requestFocus();
                        buscar.setEnabled(true);
                        buscarDescripcionRubros(cliente.getNrocuenta());
                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        showDialog("Obteniendo rubros..");
                    }
                });
            }
        });
    }

    @Override
    public void mostrarDescripcionRubro(ArrayList<Rubro> lRubros) {
        this.lRubros =lRubros;
        pdialog.finishDialog();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spinnerRubroAdapter.clear();
                spinnerRubroAdapter.setNotifyOnChange(true); //only need to call this once
                spinnerRubroAdapter.addAll(lRubros);
            }
        });
    }

    @Override
    public void mostrarMovimientos(ArrayList<ListItem> cuentas) {
        pdialog.finishDialog();
        Intent intent = new Intent(this, MovimientoActivity.class);
        GlobalApplication.getInstance().setCuentas(cuentas);
        Rubro rubro = (Rubro) rSpinner.getSelectedItem();
        ArrayList<String> lDescripcion = new ArrayList<>();
        lDescripcion.add(nrocuenta);
        lDescripcion.add(descripcion.getText().toString());
        lDescripcion.add(razonsocial.getText().toString());
        lDescripcion.add(iDate.getText().toString());
        lDescripcion.add(fDate.getText().toString());
        lDescripcion.add(rubro.getNombre());
        lDescripcion.add(rubro.getDescripcion());
        intent.putStringArrayListExtra("header",lDescripcion);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle InstanceState) {
        super.onSaveInstanceState(InstanceState);
        InstanceState.clear();
    }
}
