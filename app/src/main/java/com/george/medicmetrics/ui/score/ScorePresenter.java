package com.george.medicmetrics.ui.score;

import android.support.annotation.NonNull;

import com.george.medicmetrics.data.DataSource;
import com.george.medicmetrics.objects.Record;
import com.george.medicmetrics.ui.base.BasePresenter;

import java.util.Locale;

class ScorePresenter extends BasePresenter<ScoreContract.View> implements ScoreContract.Presenter {

    private DataSource mDataSource;

    ScorePresenter(@NonNull DataSource dataSource) {
        mDataSource = dataSource;
    }

    @Override
    public void loadScore(@NonNull Record record) {
        String respiratoryRate = String.format(Locale.getDefault(), "%.0f", record.getRespiratoryRate());
        mView.showRespiratoryRate(respiratoryRate);
        String bloodOxygen = String.format(Locale.getDefault(), "%.0f", record.getBloodOxygen());
        mView.showBloodOxygen(bloodOxygen);
        String bodyTemperature = String.format(Locale.getDefault(), "%.1f", record.getBodyTemperature());
        mView.showBodyTemperature(bodyTemperature);
        String systolicBloodPressure = String.format(Locale.getDefault(), "%.0f", record.getSystolicBloodPressure());
        mView.showSystolicBloodPressure(systolicBloodPressure);
        String heartRate = String.format(Locale.getDefault(), "%.0f", record.getHearRate());
        mView.showHeartRate(heartRate);
        if (record.isCatheterUsed()) {
            String milliliterPerHour = String.valueOf(record.getMilliliterPerHour());
            mView.showUrineOutput(milliliterPerHour);
        }
        mView.showOxygenSupplemented(record.isOxygenSupplemented() ? "Yes" : "No");
        mView.showConsciousnessLevel(record.getConsciousnessLevel());
        String score = String.valueOf(record.getScore());
        mView.showScore(score);
        mView.showTimestamp(record.getTimestamp());
        mView.changeCardColor(record.getColor());
        mView.showDescription(record.getDescription());
    }

    @Override
    public void saveRecord(@NonNull Record record) {
        int patientId = mDataSource.getPatientId();
        mDataSource.setRecord(patientId, record);
        mView.openDashboard();
    }
}
