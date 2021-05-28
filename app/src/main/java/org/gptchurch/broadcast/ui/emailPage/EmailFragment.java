package org.gptchurch.broadcast.ui.emailPage;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputEditText;

import org.gptchurch.broadcast.R;
import org.gptchurch.broadcast.model.Sermon;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EmailFragment extends Fragment {

    public String _sendTo;
    public String _emailBody;
    ArrayList<Sermon> _sermonsList;
    SermonSpinnerAdapter adapter;
    Spinner sermonSpinner;
    // FIXME sendDate
    String sendDate;
    int _pos;

    public EmailFragment(Context context, int position, String sendDate) {
        _pos = position;
        //TODO grab sendTo from preferences
        _sendTo = "8.tpledger@kscholars.org";
        this.sendDate = sendDate;
        _emailBody = "Sermon Date ... and something + ";
        _sermonsList = new ArrayList<>();
        adapter = new SermonSpinnerAdapter(context,
                R.layout.item_spinner, context.getResources());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void updateList(ArrayList<Sermon> sermons) {
        _sermonsList = sermons;
        adapter.updateList(
                new ArrayList<>(
                        sermons.stream().map(sermon -> sermon.title).collect(Collectors.toList()))
        );
        if (sermonSpinner != null) {
            sermonSpinner.setSelection(_pos);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_email, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sermonSpinner = view.findViewById(R.id.emailFragSpinner);
        TextInputEditText sendToText = view.findViewById(R.id.emailFragSendToText);
        TextInputEditText emailBodyText = view.findViewById(R.id.emailFragBodyText);
        sendToText.setText(_sendTo);
        sermonSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position,
                                               long id)
                    {
                        Sermon current = _sermonsList.get(
                                sermonSpinner.getSelectedItemPosition()
                        );

                        emailBodyText.setText(
                                String.format("%s\n\nAirdate: %s",
                                        current.getDriveLink(), sendDate)
                        );
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        emailBodyText.setText(String.format("%s", _emailBody));
                    }
                }
        );
        sendToText.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        _sendTo = s.toString();
                    }
                }
        );
        emailBodyText.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        _emailBody = s.toString();
                    }
                }
        );
        sermonSpinner.setAdapter(adapter);
        sermonSpinner.setSelection(_pos);
    }

}