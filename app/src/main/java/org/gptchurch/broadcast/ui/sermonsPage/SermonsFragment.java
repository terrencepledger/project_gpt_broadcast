package org.gptchurch.broadcast.ui.sermonsPage;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.gptchurch.broadcast.R;
import org.gptchurch.broadcast.model.Sermon;
import org.gptchurch.broadcast.model.SermonsViewModel;

import java.util.ArrayList;

public class SermonsFragment extends Fragment {

    private SermonsViewModel mViewModel;
    private ArrayList<Sermon> _sermons;
    private TableLayout table;

    public static SermonsFragment newInstance() {
        return new SermonsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sermons, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SermonsViewModel.class);
        _sermons = new ArrayList<>();
        table = requireView().findViewById(R.id.sermonTable);
        // update UI
        mViewModel.getSermons().observe(getViewLifecycleOwner(), this::checkNormalSermons);
        mViewModel.getAvailableSermons().observe(getViewLifecycleOwner(), this::checkAvailSermons);
    }

    private ArrayList<View> checkNormalSermons(ArrayList<Sermon> sermonsData) {

        ArrayList<View> views = new ArrayList<>();
        for (Sermon sermon: sermonsData) {
            if(!_sermons.contains(sermon)) {

                _sermons.add(sermon);

                TableRow.LayoutParams params = new TableRow.LayoutParams(
                        0,
                        TableRow.LayoutParams.WRAP_CONTENT, 1
                );
                TableRow row = new TableRow(getContext());
                TextView nameText = new TextView(getContext());
                nameText.setText(sermon.title);
                nameText.setPaintFlags(nameText.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
                nameText.setLayoutParams(params);
                nameText.setGravity(Gravity.CENTER);
                nameText.setOnClickListener(
                        v -> {
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(sermon.getDriveLink()));
                            startActivity(i);
                        }
                );
                TextView dateText = new TextView(getContext());
                dateText.setText(sermon.getPrettyDate());
                dateText.setLayoutParams(params);
                dateText.setGravity(Gravity.CENTER);
                Button removeBtn = new Button(getContext());
                removeBtn.setText("Remove Sermon");
                removeBtn.setLayoutParams(params);

                row.setWeightSum(3);
                row.addView(nameText, params);
                row.addView(dateText, params);
                row.addView(removeBtn, params);

                TableRow.LayoutParams params2 = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                );
                params.gravity = Gravity.FILL & Gravity.CENTER_VERTICAL;
                row.setLayoutParams(params2);
                table.addView(row, params2);
                views.add(nameText);
                views.add(dateText);
            }
        }
        return views;
    }

    private void checkAvailSermons(ArrayList<Sermon> sermonsData) {

        ArrayList<View> views = checkNormalSermons(sermonsData);
        if(views.size() == 0) {
            for(View view: views) {
                view.setBackgroundColor(getResources().getColor(R.color.green, null));
            }
        }

    }

}