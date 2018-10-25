package com.piyush.pictprint;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.printservice.PrintService;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.piyush.pictprint.api.LoginService;
import com.piyush.pictprint.api.SubmitJobService;
import com.piyush.pictprint.model.JobsListWrapper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListFragment extends Fragment implements SubmitJobService.JobsFetchedListener {


    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.logsList)
    RecyclerView recyclerView;

    LogAdapter adapter;
    private SubmitJobService service;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String token = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("Token",null);
        if(token!=null)
        service = new SubmitJobService(token);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

      View root = inflater.inflate(R.layout.fragment_list,container,false);
        ButterKnife.bind(this,root);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                service.fetchJobs(ListFragment.this);
            }
        });
        refreshLayout.setRefreshing(true);
        service.fetchJobs(this);
        adapter = new LogAdapter(getContext());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        return root;
    }

    @Override
    public void onJobsFetched(JobsListWrapper jobsListWrapper) {
        refreshLayout.setRefreshing(false);
        if(jobsListWrapper.isSuccess())
            adapter.submitList(jobsListWrapper.getJobs());
        else Toast.makeText(getContext(), jobsListWrapper.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onJobsFetchFailed(String error) {
        refreshLayout.setRefreshing(false);
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }
}
