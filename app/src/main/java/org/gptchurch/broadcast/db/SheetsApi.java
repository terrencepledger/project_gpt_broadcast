package org.gptchurch.broadcast.db;

import android.util.Log;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import org.gptchurch.broadcast.model.Sermon;
import org.gptchurch.broadcast.model.SermonLog;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class SheetsApi {

    public Sheets ss;
    private final String logSheetId = "1iGKe17OYVRDYmdpHguoQHau6bX_MoU1B9LmtsSGiAq8";


    public SheetsApi() throws IOException {

        GoogleCredentials credential;

        HttpTransport httpTransport = new com.google.api.client.http.javanet.NetHttpTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        credential = GoogleCredentials.fromStream(
                Objects.requireNonNull(getClass()
                        .getResourceAsStream(
                                "/swift-apogee-278906-f5d66447dec4.json"
                        ))
        ).createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));
        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(
                credential
        );

        ss = new Sheets(httpTransport, jsonFactory, requestInitializer);

    }

    public ArrayList<SermonLog> getAll() throws IOException {

        ArrayList<SermonLog> logs = new ArrayList<>();

        List<Sheet> spreadsheets = ss.spreadsheets().get(logSheetId).execute()
                .getSheets();
        String lastSheet = spreadsheets.get(spreadsheets.size() - 1).getProperties().getTitle();
        String range = String.format(Locale.US,"%s!A3:B", lastSheet);

        AtomicInteger index = new AtomicInteger();
        ss.spreadsheets().values().get(logSheetId, range).execute()
                .getValues().forEach(
                        row -> {
                            logs.add(parseRow(index.get(), row));
                            index.getAndIncrement();
                        }
        );

        return logs;

    }

    public ArrayList<Sermon> getAvailable() throws IOException {

        ArrayList<Sermon> sermons = new ArrayList<>();

        List<Sheet> spreadsheets = ss.spreadsheets().get(logSheetId).execute()
                .getSheets();
        String lastSheet = spreadsheets.get(spreadsheets.size() - 1).getProperties().getTitle();
        String range = String.format(Locale.US,"%s!A3:B", lastSheet);

        AtomicInteger index = new AtomicInteger();
        ss.spreadsheets().values().get(logSheetId, range).execute()
                .getValues().forEach(
                row -> {
                    SermonLog log = parseRow(index.get(), row);
                    if(checkSermon(log))
                        sermons.add(log.sermon);
                    else
                        sermons.remove(log.sermon);
                    index.getAndIncrement();
                }
        );

        return sermons;

    }

    private boolean checkSermon(SermonLog log) {

        //TODO grab amtDays from settings
        int amtDays = 60;

        Calendar beforeDate = Calendar.getInstance();
        beforeDate.add(Calendar.DATE, amtDays * -1);

        return log.sentDate == null || log.sentDate.before(beforeDate.getTime());

    }

    private SermonLog parseRow(int id, List<Object> row) {

        String name = row.get(0).toString().substring(9);
        Date parsedDate;
        Date sentDate;

        try {
            String date = row.get(0).toString().substring(0, 9);

            String pattern = "MM.dd.yy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.US);
            parsedDate = simpleDateFormat.parse(date);

            String pattern2 = "MM/dd/yyyy";
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(pattern2, Locale.US);
            sentDate = simpleDateFormat2.parse(row.get(1).toString());

        } catch (ParseException e) {
            parsedDate = new Date(0);
            sentDate = new Date(0);
            Log.e("ParseException", e.getMessage());
        }

        Sermon sermon = new Sermon(String.valueOf(id), name, parsedDate);

        return new SermonLog(sermon, sentDate);

    }

    public void update(Sermon sermon, Date sendDate) throws IOException {

        String pattern = "MM.dd.yy";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.US);

        ValueRange valueRange = new ValueRange();
        List<List<Object>> lists = new ArrayList<>();
        lists.add(new ArrayList<>());
        lists.get(0).add(formatter.format(sermon.dateUploaded) + " " + sermon.title);
        lists.get(0).add(sendDate.toString());
        valueRange.setValues(lists);
        List<Sheet> sheets = ss.spreadsheets().get(logSheetId).execute()
                .getSheets();
        ss.spreadsheets().values().append(logSheetId,
                sheets.get(sheets.size() - 1).getProperties().getTitle(),
                valueRange
        ).setValueInputOption("USER_ENTERED").execute();

    }
}
