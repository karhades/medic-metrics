package com.george.medicmetrics.data;

import android.support.annotation.NonNull;

import com.george.medicmetrics.objects.Patient;
import com.george.medicmetrics.objects.Record;

import java.util.List;

public class Repository implements DataSource {

    private LocalRepository mLocalRepository;
    private PreferencesRepository mPreferencesRepository;
    private static Repository sInstance;

    private Repository(LocalRepository localRepository, PreferencesRepository preferencesRepository) {
        mLocalRepository = localRepository;
        mPreferencesRepository = preferencesRepository;
    }

    public static Repository getInstance(LocalRepository localRepository, PreferencesRepository preferencesRepository) {
        if (sInstance == null) {
            sInstance = new Repository(localRepository, preferencesRepository);
        }
        return sInstance;
    }

    @Override
    public void setPatientId(int patientId) {
        mPreferencesRepository.setPatientId(patientId);
    }

    @Override
    public int getPatientId() {
        return mPreferencesRepository.getPatientId();
    }

    @Override
    public boolean isUserLoggedIn() {
        return mPreferencesRepository.getPatientId() != -1;
    }

    @Override
    public void validateUser(@NonNull String username, @NonNull String password, @NonNull Callback<Integer> callback) {
        int patientId = mLocalRepository.getPatient(username, password);
        if (patientId == -1) {
            callback.onFailure();
            return;
        }

        callback.onSuccess(patientId);
    }

    @Override
    public void setPatient(@NonNull Patient patient) {
        mLocalRepository.savePatient(patient);
    }

    @Override
    public void getPatientList(@NonNull Callback<List<Patient>> callback) {
        // TODO: Implement
    }

    @Override
    public void setRecord(int patientId, @NonNull Record record) {
        mLocalRepository.saveRecord(patientId, record);
    }
}
