package com.muradit.projectx.View.MainActivity.ui.tools;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.muradit.projectx.Model.others.CurrentLangLocal;
import com.muradit.projectx.R;
import com.muradit.projectx.View.MainActivity.MainActivity;
import com.muradit.projectx.View.login.loginView;
import com.muradit.projectx.ViewModel.toolsViewModel.ToolsViewModel;

import java.util.Locale;


public class ToolsFragment extends Fragment {

    private CardView englishCard,arabicCard;
    private ToolsViewModel viewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_tools, container, false);
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.language));

        init(root);

        return root;
    }

    private void init(View root) {
        viewModel=new ToolsViewModel(getActivity());
        englishCard=root.findViewById(R.id.engCard);
        arabicCard=root.findViewById(R.id.arCard);


        englishCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("en");
                englishCard.setEnabled(false);
                arabicCard.setEnabled(true);
                Toast.makeText(getActivity(), "English language", Toast.LENGTH_SHORT).show();
            }
        });

        arabicCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("ar");
                arabicCard.setEnabled(false);
                englishCard.setEnabled(true);
                Toast.makeText(getActivity(), "اللغة العربية", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setLocale(String lang) {
        Resources res=getResources();
        DisplayMetrics dm=res.getDisplayMetrics();
        Configuration conf=res.getConfiguration();
        conf.setLocale(new Locale(lang));
        res.updateConfiguration(conf,dm);
        CurrentLangLocal.Local=lang;

        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null)
          viewModel.updateLanguage(lang);


        Intent i=new Intent(getActivity(), loginView.class);
        startActivity(i);

    }
}