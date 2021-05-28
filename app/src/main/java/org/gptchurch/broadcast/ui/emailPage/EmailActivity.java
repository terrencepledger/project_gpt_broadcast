package org.gptchurch.broadcast.ui.emailPage;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.gptchurch.broadcast.R;
import org.gptchurch.broadcast.db.SheetsApi;
import org.gptchurch.broadcast.model.Sermon;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

public class EmailActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        TabLayout tabLayout = findViewById(R.id.emailActivityTabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Home"));
        tabLayout.addTab(tabLayout.newTab().setText("About"));
        tabLayout.addTab(tabLayout.newTab().setText("Contact"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager2 viewPager = findViewById(R.id.emailActivityViewPager);
        viewPager.setAdapter(new EmailFragmentAdapter(this));
        viewPager.setOffscreenPageLimit(1);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText("Email " + (position + 1) )).attach();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        findViewById(R.id.sendEmailBtn).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EmailFragmentAdapter adapter = (EmailFragmentAdapter)viewPager.getAdapter();
                        EmailFragment frag1 = adapter.frags.get(0);
                        EmailFragment frag2 = adapter.frags.get(1);
                        int pos1 = frag1.sermonSpinner.getSelectedItemPosition();
                        int pos2 = frag2.sermonSpinner
                                .getSelectedItemPosition();
                        sendEmail(frag1, pos1, getSendDate(0));
                        sendEmail(frag2, pos2, getSendDate(1));
                    }
                }
        );

    }

    private Date getSendDate(int pos) {
        Date sendDate;
        switch (pos) {
            case 0:
                sendDate = new Date();
            case 1:
                sendDate = new Date(0);
            default:
                sendDate = new Date();
        }
        return sendDate;
    }

    private void sendEmail(EmailFragment frag, int pos, Date sendDate) {

        Sermon sermon = frag._sermonsList.get(pos);
        String prettyDate = DateFormat.getDateInstance().format(sendDate);
        //TODO grab several of these variables from preferences
        String gmailUser = "gptbroadcast@gmail.com";
        String gmailPass = "soundroom";
        String toEmail = frag._sendTo;
        String ccEmail = "8.tpledger@kscholars.org";
        String subject = String.format("Bishop Donaldson \"%s\" Airdate: %s",
                sermon.title, prettyDate
        );
        String body = frag._emailBody;

        BackgroundMail.newBuilder(this)
                .withUsername(gmailUser)
                .withPassword(gmailPass)
                .withMailTo(toEmail)
                .withMailCc(ccEmail)
                .withType(BackgroundMail.TYPE_PLAIN)
                .withSubject(subject)
                .withBody(body)
                .withSendingMessage("Sending...")
                .withOnSuccessCallback(
                        new BackgroundMail.OnSendingCallback() {
                            @Override
                            public void onSuccess() {
                                Thread thread = new Thread(() -> {

                                    try {
                                        sermon.update(sendDate);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                });

                                thread.start();

                            }

                            @Override
                            public void onFail(Exception e) {

                            }
                        }
                )
                .send();

    }

}