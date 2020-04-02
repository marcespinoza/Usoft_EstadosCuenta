package com.estados.cuenta.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.estados.cuenta.Pojo.Rubro;

import java.util.ArrayList;

public class SpinnerRubroAdapter extends ArrayAdapter<Rubro> {


    public SpinnerRubroAdapter(Context context, int textViewResourceId,  ArrayList<Rubro> values) {
        super(context, textViewResourceId, values);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(this.getItem(position).getNombre());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(this.getItem(position).getNombre());
        return label;
    }
}