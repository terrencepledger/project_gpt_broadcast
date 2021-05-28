package org.gptchurch.broadcast.db;

import android.util.Log;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import org.gptchurch.broadcast.model.Sermon;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class DriveApi {

    public Drive drive;

    public DriveApi() throws IOException {

        GoogleCredentials credential;

        HttpTransport httpTransport = new com.google.api.client.http.javanet.NetHttpTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        credential = GoogleCredentials.fromStream(
                Objects.requireNonNull(getClass()
                        .getResourceAsStream(
                                "/swift-apogee-278906-f5d66447dec4.json"
                        ))
        ).createScoped(Collections.singleton(DriveScopes.DRIVE));
        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(
                credential
        );

        drive = new Drive(httpTransport, jsonFactory, requestInitializer);

    }

    public String findLink(Sermon sermon) throws IOException {

        String title = sermon.title;
        if(sermon.title.contains("'")) {
            Log.i("Test", "Inside");
            title = title.replaceAll("'", "\\\\'");
        }
        Log.i("Title", title);
        return drive.files().list().setQ(
                String.format("name contains '%s'", title)
        )
        .setFields("files(id, name, webViewLink)").execute().getFiles().get(0).getWebViewLink();

    }

    public ArrayList<Sermon> getAll() throws IOException {

        ArrayList<Sermon> sermons = new ArrayList<>();

        String parentFolderId = "1sdpTec2oKIO9lqNroCBaPIfJF6Y8ffbk";
        List<File> files = drive.files().list().setQ(
                String.format("'%s' in parents and name contains 'Sermons'",
                        parentFolderId
                )
        ).setOrderBy("name").execute().getFiles();

        for (File folder: files) {
            drive.files().list().setQ(
                String.format("'%s' in parents",
                        folder.getId()
                )
            ).setFields("files(id, name, webViewLink)")
            .execute().getFiles().forEach(file -> sermons.add(parseFile(file)));
        }

        return sermons;

    }

    private Sermon parseFile(File file) {

        String name = file.getName().substring(9, file.getName().length() - 21);
        Date parsedDate;

        try {
            String date = file.getName().substring(0, 9);

            String pattern = "MM.dd.yy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.US);

            parsedDate = simpleDateFormat.parse(date);

        } catch (ParseException e) {
            parsedDate = new Date(0);
            Log.e("ParseException", e.getMessage());
        }

        Sermon sermon = new Sermon(file.getId(), name, parsedDate);
        sermon.setDriveLink(file.getWebViewLink());

        return sermon;

    }

}
