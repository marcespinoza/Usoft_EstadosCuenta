package com.estados.cuenta.Vista;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.estados.cuenta.Adapter.MovimientoAdapter;
import com.estados.cuenta.Pojo.ListItem;
import com.estados.cuenta.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovimientoActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_movimientos) Toolbar toolbar;
    @BindView(R.id.recycler_movimientos)  RecyclerView recyclerView;
    ArrayList<ListItem> lMovimientos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movimiento_activity);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Movimientos");
        Bundle bundle = getIntent().getExtras();
        Intent intent = getIntent();
        bundle = intent.getBundleExtra("BUNDLE");
        lMovimientos = (ArrayList<ListItem>) bundle.getSerializable("MOVIMIENTOS");
        MovimientoAdapter movimientoAdapter = new MovimientoAdapter(lMovimientos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(movimientoAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
