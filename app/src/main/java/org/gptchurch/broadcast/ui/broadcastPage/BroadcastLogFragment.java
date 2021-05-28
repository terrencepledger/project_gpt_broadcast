package org.gptchurch.broadcast.ui.broadcastPage;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.gptchurch.broadcast.R;
import org.gptchurch.broadcast.model.BroadcastLogViewModel;
import org.gptchurch.broadcast.model.SermonLog;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BroadcastLogFragment extends Fragment {

    private BroadcastLogViewModel mViewModel;
    private ArrayList<SermonLog> _logs;
    private BroadcastLogRecyclerAdapter adapter;

    public static BroadcastLogFragment newInstance() {
        return new BroadcastLogFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_broadcast_log, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BroadcastLogViewModel.class);
        _logs = new ArrayList<>();
        RecyclerView logRecycler = requireView().findViewById(R.id.broadcastLogRecycler);
        adapter = new BroadcastLogRecyclerAdapter();
        logRecycler.setAdapter(adapter);
        // update UI
        mViewModel.getLogs().observe(getViewLifecycleOwner(), this::checkLogs);
    }

    private void checkLogs(ArrayList<SermonLog> logsData) {
        for (SermonLog log: logsData) {
            if(!_logs.contains(log)) {

                _logs.add(log);

                adapter.add(log);

            }
        }

    }
}