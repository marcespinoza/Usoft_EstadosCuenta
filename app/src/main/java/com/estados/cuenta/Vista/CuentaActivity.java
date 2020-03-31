package com.estados.cuenta.Vista;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.estados.cuenta.Adapter.AutoCompleteClienteAdapter;
import com.estados.cuenta.Interface.CuentaInterface;
import com.estados.cuenta.Pojo.Cliente;
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
    private int mYear, mMonth, mDay, mHour, mMinute;
    public CuentaInterface.CuentaPresentador cPresentador;
    ProgressDialog pdialog;
    SharedPreferences sharedPref;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cuenta_activity);
        ButterKnife.bind(this);
        pdialog = new ProgressDialog(this);
        sharedPref = getSharedPreferences("datosesion", Context.MODE_PRIVATE);
        cPresentador = new CuentaPresentador(this);
        searchCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cl = autocompleteCliente.getText().toString();
                if(!cl.isEmpty())
                enviarCliente(cl);
            }
        });
    }

    @OnClick(R.id.calendarioinicial)
    public void showCalendar(){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        iDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
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
                        Cliente clientes = (Cliente) adapterView.getItemAtPosition(i);
                /*cliente.setText(clientes.getNombre());
                codcli.setText(clientes.getNrocuenta());
                codigoPersona = clientes.getPersona();
                desart.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(desart, InputMethodManager.SHOW_IMPLICIT);*/
                    }
                });
            }
        });
    }
}
