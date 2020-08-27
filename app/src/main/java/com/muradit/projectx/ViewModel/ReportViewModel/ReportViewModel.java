package com.muradit.projectx.ViewModel.ReportViewModel;

import android.content.Context;

import com.muradit.projectx.Repository.ReportRepository.reportRepository;

public class ReportViewModel {
    private reportRepository reportRepository;

    public ReportViewModel(Context context) {
        reportRepository =new reportRepository(context);
    }

    public void submitReport(String report){
        reportRepository.submitReport(report);
    }
}
