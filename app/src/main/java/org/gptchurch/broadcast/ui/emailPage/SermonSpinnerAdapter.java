package org.gptchurch.broadcast.ui.emailPage;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import org.gptchurch.broadcast.R;
import org.jetbrains.annotations.NotNull;

public class SermonSpinnerAdapter extends ArrayAdapter<String> {

    private ArrayList<String> sermonTitles;
    public Resources res;
    String currRowVal = "temp";
    LayoutInflater inflater;

    public SermonSpinnerAdapter(Context context,
                         int textViewResourceId,
                         Resources resLocal) {
        super(context, textViewResourceId, Collections.singletonList(""));
        this.sermonTitles = new ArrayList<>();
        this.res = resLocal;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getDropDownView(int position, View convertView, @NotNull ViewGroup parent) {
        return getCustomView(position, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, parent);
    }

    public View getCustomView(int position, ViewGroup parent) {
        View row = inflater.inflate(R.layout.item_spinner, parent, false);
        currRowVal = sermonTitles.get(position);
        TextView label = row.findViewById(R.id.spinnerItem);
        label.setText(currRowVal);
        return row;
    }

    public void updateList(ArrayList<String> titles) {
        this.sermonTitles = titles;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return sermonTitles.size();
    }
}