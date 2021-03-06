package com.george.medicmetrics.ui.anamnesis;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.george.medicmetrics.R;
import com.george.medicmetrics.data.DataSource;
import com.george.medicmetrics.data.Injection;
import com.george.medicmetrics.objects.Record;
import com.george.medicmetrics.ui.base.BaseFragment;
import com.george.medicmetrics.ui.report.ReportActivity;

import java.util.List;

public class AnamnesisFragment extends BaseFragment<AnamnesisContract.Presenter> implements AnamnesisContract.View {

    private AnamnesisAdapter mAnamnesisAdapter;
    private TextView mEmptyTextView;

    public static AnamnesisFragment newInstance() {
        return new AnamnesisFragment();
    }

    @NonNull
    @Override
    protected AnamnesisContract.Presenter createPresenter() {
        DataSource dataSource = Injection.provideDataSource(getContext());
        return new AnamnesisPresenter(dataSource);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_anamnesis, container, false);
        setupToolbar(view);
        setupRecyclerView(view);
        mEmptyTextView = view.findViewById(R.id.empty_text_view);
        return view;
    }

    private void setupToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(getActivity());
            }
        });
    }

    private void setupRecyclerView(View view) {
        mAnamnesisAdapter = new AnamnesisAdapter(null, new AnamnesisAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int recordId) {
                Intent intent = ReportActivity.newIntent(getContext(), recordId);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAnamnesisAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.loadAnamnesis();
    }

    @Override
    public void showAnamnesis(@NonNull List<Record> recordList) {
        mAnamnesisAdapter.setRecordList(recordList);
        mAnamnesisAdapter.notifyDataSetChanged();
    }

    @Override
    public void showEmptyAnamnesis() {
        mEmptyTextView.setVisibility(View.VISIBLE);
    }
}
