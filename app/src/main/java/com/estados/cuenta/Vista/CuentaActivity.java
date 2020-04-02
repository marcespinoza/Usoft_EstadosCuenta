package com.estados.cuenta.Vista;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.estados.cuenta.Adapter.AutoCompleteClienteAdapter;
import com.estados.cuenta.Adapter.SpinnerRubroAdapter;
import com.estados.cuenta.Interface.CuentaInterface;
import com.estados.cuenta.Pojo.Cliente;
import com.estados.cuenta.Pojo.Rubro;
import com.estados.cuenta.Presentador.CuentaPresentador;
import com.estados.cuenta.R;

import java.util.ArrayList;
import java.util.Calendar;

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
    @BindView(R.id.descripcion_rubro) EditText descRubro;
    private int mYear, mMonth, mDay, mHour, mMinute;
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
        lRubros.add(new Rubro("Rubro","Rubro"));
        pdialog = new ProgressDialog(this);
        sharedPref = getSharedPreferences("datosesion", Context.MODE_PRIVATE);
        cPresentador = new CuentaPresentador(this);
        spinnerRubroAdapter = new SpinnerRubroAdapter(this, R.layout.spinner, lRubros);
        searchCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cl = autocompleteCliente.getText().toString();
                if(!cl.isEmpty())
                enviarCliente(cl);
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
                descRubro.setText(rubro.getDescripcion());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void limpiarDetalle(){
        autocompleteCliente.setText("");
        descripcion.setText("");
        razonsocial.setText("");
        spinnerRubroAdapter.clear();
        spinnerRubroAdapter.add(new Rubro("Rubro","Rubro"));
    }

    public void buscarDescripcionRubros(String rubro){
        cPresentador.enviarRubro(rubro);
    }

    @OnClick(R.id.calendarioinicial)
    public void showCalendar(){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> iDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year), mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void enviarCliente(String cliente){
        runOnUiThread(new Runnable() {
            public void run() {
                pdialog.showProgressDialog("Obteniendo clientes..");
            }
        });
        cPresentador.enviarCliente(cliente);
    }

    @Override
    public void mostrarToast(String mensaje) {

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
                        autocompleteCliente.requestFocus();
                        buscarDescripcionRubros(cliente.getNrocuenta());
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(autocompleteCliente, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
            }
        });
    }

    @Override
    public void mostrarDescripcionRubro(ArrayList<Rubro> lRubros) {
        this.lRubros =lRubros;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spinnerRubroAdapter.clear();
                spinnerRubroAdapter.setNotifyOnChange(true); //only need to call this once
                spinnerRubroAdapter.addAll(lRubros);
            }
        });
    }
}
