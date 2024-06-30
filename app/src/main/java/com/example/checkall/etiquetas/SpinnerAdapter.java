package com.example.checkall.etiquetas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.checkall.R;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<Etiqueta> {

    private List<Etiqueta> items;
    private Context context;

    public SpinnerAdapter(@NonNull Context context, int resource, @NonNull List<Etiqueta> items) {
        super(context, resource, items);
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.spinner_items, parent, false);
        }

        Etiqueta currentItem = items.get(position);

        TextView textView = row.findViewById(R.id.txt_opcion);
        textView.setText(currentItem.getName());
        textView.setBackgroundResource(currentItem.getColorId());

        return row;
    }
}
