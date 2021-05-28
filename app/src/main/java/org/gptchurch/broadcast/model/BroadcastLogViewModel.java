package org.gptchurch.broadcast.model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.api.services.drive.Drive;

import org.gptchurch.broadcast.db.DriveApi;
import org.gptchurch.broadcast.db.SheetsApi;
import org.gptchurch.broadcast.model.Sermon;
import org.gptchurch.broadcast.model.SermonLog;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

public class BroadcastLogViewModel extends ViewModel {

    private MutableLiveData<ArrayList<SermonLog>> sermonLogListData;
    private ArrayList<SermonLog> sermonLogList;

    public LiveData<ArrayList<SermonLog>> getLogs() {
        if (sermonLogListData == null) {
            sermonLogListData = new MutableLiveData<>();
            sermonLogList = new ArrayList<>();
            loadSermons();
        }
        return sermonLogListData;
    }

    private void loadSermons() {

        Thread thread = new Thread(() -> {

            try {

                SheetsApi sheets = new SheetsApi();

                sermonLogListData.postValue(sheets.getAll());

            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        thread.start();

    }

}