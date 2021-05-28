package org.gptchurch.broadcast.ui.emailPage;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.gptchurch.broadcast.model.Sermon;
import org.gptchurch.broadcast.model.SermonsViewModel;

import java.util.ArrayList;

public class EmailFragmentAdapter extends FragmentStateAdapter {

    public ArrayList<EmailFragment> frags = new ArrayList<EmailFragment>(2);
    SermonsViewModel mViewModel;

    public EmailFragmentAdapter(FragmentActivity fm){
        super(fm);
        mViewModel = new ViewModelProvider(fm).get(SermonsViewModel.class);
        frags.add(new EmailFragment(fm.getBaseContext(), 0, mViewModel.getNextDate(0)));
        frags.add(new EmailFragment(fm.getBaseContext(), 1, mViewModel.getNextDate(1)));
        mViewModel.getAvailableSermons().observe(fm, this::loadEmails);
    }

//    TODO get emails from settings
    void loadEmails(ArrayList<Sermon> sermonsList) {
        frags.forEach(emailFragment -> emailFragment.updateList(sermonsList));
    }

    @Override
    public Fragment createFragment(int position) {
        return frags.get(position);
    }

    @Override
    public int getItemCount() {
        return frags.size();
    }
}