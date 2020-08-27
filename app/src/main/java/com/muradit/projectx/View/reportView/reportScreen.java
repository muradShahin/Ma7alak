package com.muradit.projectx.View.reportView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.muradit.projectx.R;
import com.muradit.projectx.ViewModel.ReportViewModel.ReportViewModel;

public class reportScreen extends AppCompatActivity {
    private Button submit;
    private EditText problemText;
    private ReportViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_screen);

        getSupportActionBar().hide();
        DisplayMetrics displayMetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width=displayMetrics.widthPixels;
        int height=displayMetrics.heightPixels;

        getWindow().setLayout((int)(width *0.8),(int)(height*0.8));

        init();
    }

    private void init() {
        viewModel=new ReportViewModel(this);
        submit=findViewById(R.id.submit);
        problemText=findViewById(R.id.helpText);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.submitReport(problemText.getText().toString());
            }
        });
    }
}