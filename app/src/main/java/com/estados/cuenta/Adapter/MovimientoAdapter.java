package com.estados.cuenta.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.estados.cuenta.Pojo.CuentaItem;
import com.estados.cuenta.Pojo.ListItem;
import com.estados.cuenta.Pojo.CuentaHeader;
import com.estados.cuenta.R;

import java.util.ArrayList;

public class MovimientoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ArrayList<ListItem> lcuentas;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public MovimientoAdapter(ArrayList<ListItem> lcuentas) {
        this.lcuentas = lcuentas;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cuenta_header, parent, false);
            return new MovimientoAdapter.HeaderMovimientoHolder(view);
        } else if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cuenta_item, parent, false);
            return new MovimientoAdapter.MovimientoHolder(view);
        }
        else
            throw new RuntimeException("Could not inflate layout");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderMovimientoHolder) {
            // VHHeader VHheader = (VHHeader)holder;
            CuentaHeader  currentItem = (CuentaHeader) lcuentas.get(position);
            HeaderMovimientoHolder headerMovimientoHolder = (HeaderMovimientoHolder)holder;
            headerMovimientoHolder.moneda.setText(currentItem.getMoneda());
        } else if (holder instanceof MovimientoHolder) {
            CuentaItem cuentaItem = (CuentaItem) lcuentas.get(position);
            MovimientoHolder movimientoHolder = (MovimientoHolder) holder;
            movimientoHolder.fecha.setText(cuentaItem.getFecha());
            movimientoHolder.tdoc.setText(cuentaItem.getTdoc());
            movimientoHolder.serie.setText(cuentaItem.getSerie());
            movimientoHolder.numerodoc.setText(cuentaItem.getNumerodoc());
            movimientoHolder.importe.setText(cuentaItem.getImporte());
            movimientoHolder.saldo.setText(cuentaItem.getSaldo());

        }
    }

    @Override
    public int getItemViewType(int position){
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return lcuentas.get(position) instanceof CuentaHeader;
    }

    @Override
    public int getItemCount() {
        return lcuentas.size();
    }

    public class MovimientoHolder extends RecyclerView.ViewHolder{

        TextView fecha;
        TextView tdoc;
        TextView serie;
        TextView numerodoc;
        TextView importe;
        TextView saldo;

        public MovimientoHolder(@NonNull View itemView) {
            super(itemView);
            fecha = itemView.findViewById(R.id.fecha);
            tdoc = itemView.findViewById(R.id.tipodoc);
            serie = itemView.findViewById(R.id.serie);
            numerodoc = itemView.findViewById(R.id.numerodoc);
            importe = itemView.findViewById(R.id.importe);
            saldo = itemView.findViewById(R.id.saldo);
        }
    }

    public class HeaderMovimientoHolder extends RecyclerView.ViewHolder{

        TextView moneda;

        public HeaderMovimientoHolder(@NonNull View itemView) {
            super(itemView);
            moneda = itemView.findViewById(R.id.moneda);
        }
    }

}
