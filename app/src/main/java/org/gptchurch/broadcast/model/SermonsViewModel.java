package org.gptchurch.broadcast.model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import org.gptchurch.broadcast.db.DriveApi;
import org.gptchurch.broadcast.db.SheetsApi;
import org.gptchurch.broadcast.model.Sermon;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class SermonsViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Sermon>> sermonListData;
    MutableLiveData<ArrayList<Sermon>> availableSermonData = new MutableLiveData<>();

    public LiveData<ArrayList<Sermon>> getSermons() {
        if (sermonListData == null) {
            sermonListData = new MutableLiveData<>();
            availableSermonData = new MutableLiveData<>();

            loadSermons();
        }
        return sermonListData;
    }

    public LiveData<ArrayList<Sermon>> getAvailableSermons() {
        if (sermonListData == null) {
            sermonListData = new MutableLiveData<>();
            availableSermonData = new MutableLiveData<>();
            loadLogs();
        }
        return availableSermonData;
    }

    private void loadSermons() {

        Thread thread = new Thread(() -> {
            try {

                DriveApi drive = new DriveApi();

                sermonListData.postValue(drive.getAll());

            } catch (IOException e) {
                Log.e("Uhohx2", e.getMessage());
            }

        });

        thread.start();

    }

    private void loadLogs() {

        Thread thread = new Thread(() -> {

            try {

                ArrayList<Sermon> corrected = new ArrayList<>();

                SheetsApi sheets = new SheetsApi();
                DriveApi drive = new DriveApi();
                ArrayList<Sermon> sermons = sheets.getAvailable();

                for (Sermon sermon: sermons) {
                    sermon.setDriveLink(drive.findLink(sermon));
                    corrected.add(sermon);
                    availableSermonData.postValue(corrected);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        thread.start();

    }

}