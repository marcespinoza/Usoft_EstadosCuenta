package com.estados.cuenta.Vista;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.estados.cuenta.R;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CuentaActivity extends AppCompatActivity {

    @BindView(R.id.calendarioinicial)ImageButton cInicial;
    @BindView(R.id.calendariofinal) ImageButton cFinal;
    @BindView(R.id.inicialDate) EditText iDate;
    @BindView(R.id.finalDate) EditText fDate;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cuenta_activity);
        ButterKnife.bind(this);
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

}
