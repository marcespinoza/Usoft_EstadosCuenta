package com.estados.cuenta.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.estados.cuenta.Pojo.Cliente;
import com.estados.cuenta.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AutoCompleteClienteAdapter extends ArrayAdapter<Cliente> {

    private List<Cliente> clienteListFull;

    public AutoCompleteClienteAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<Cliente> clienteList) {
        super(context, textViewResourceId, clienteList);
        clienteListFull = new ArrayList<>(clienteList);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return clienteFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.autocompletecliente_row, parent, false
            );
        }

        TextView textViewName = convertView.findViewById(R.id.nombrecliente);
        TextView textViewNro = convertView.findViewById(R.id.nrocuenta);

        Cliente Cliente = getItem(position);

        if (Cliente != null) {
            textViewNro.setText(Cliente.getNrocuenta());
            textViewName.setText(Cliente.getNombre());
        }

        return convertView;
    }

    private Filter clienteFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Cliente> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(clienteListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Cliente item : clienteListFull) {
                    if (item.getNombre().toLowerCase().contains(filterPattern)) {
                        suggestions.add(item);
                    }
                }
            }
            Collections.sort(suggestions, new Comparator<Cliente>() {
                @Override
                public int compare(Cliente s1, Cliente s2) {
                    return s1.getNombre().compareToIgnoreCase(s2.getNombre());
                }
            });
            results.values = suggestions;
            results.count = suggestions.size();

            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Cliente) resultValue).getNombre();
        }
    };
}

