package com.example.pool.gui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pool.R;
import com.example.pool.action.EventAction;

import java.util.List;

public class EventsFragment extends Fragment {
    public RecyclerView mRecyclerView;
    private EventAdapter mEventAdapter;
    private List<EventAction> mEventsList;
    public EventsFragment(List<EventAction> mEventsList) {
        this.mEventsList = mEventsList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_events, container, false);
        mRecyclerView = v.findViewById(R.id.events_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FragmentManager fm=getActivity().getSupportFragmentManager();
        mEventAdapter = new EventAdapter(mEventsList,fm,getContext());
        mRecyclerView.setAdapter(mEventAdapter);
        return v;
    }

}
