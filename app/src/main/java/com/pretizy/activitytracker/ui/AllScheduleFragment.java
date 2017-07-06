package com.pretizy.activitytracker.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pretizy.activitytracker.R;
import com.pretizy.activitytracker.Util;
import com.pretizy.activitytracker.adapters.ScheduleTableAdapter;
import com.pretizy.activitytracker.model.Schedule;
import com.pretizy.activitytracker.model.ScheduleDao;

import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AllScheduleFragment.} interface
 * to handle interaction events.
 * Use the {@link AllScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllScheduleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final Integer CREATE_EVENT = 5000;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView table;
    private TextView selectedDate;
    private RecyclerView.Adapter tableAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ScheduleDao scheduleDao;

    private ScheduleFragment.OnFragmentInteractionListener mListener;
    private List<Schedule> schedules;
    private Date date;
    private RecyclerView.LayoutManager dLayoutManager;
    private View view;

    public AllScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllScheduleFragment newInstance(String param1, String param2) {
        AllScheduleFragment fragment = new AllScheduleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_all_schedule, container, false);
        // Inflate the layout for this fragment
        table = (RecyclerView) view.findViewById(R.id.schedule_table);
        selectedDate = (TextView) view.findViewById(R.id.selectedDate);
        scheduleDao = new ScheduleDao(getActivity());
        schedules = Util.sortSchedule(scheduleDao.findAllSchedules());
        tableAdapter = new ScheduleTableAdapter(schedules, this);


        mLayoutManager = new LinearLayoutManager(getActivity());
        table.setLayoutManager(mLayoutManager);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createEvent = new Intent(getActivity(), CreateEventActivity.class);
                startActivityForResult(createEvent, CREATE_EVENT, null);
            }
        });

        table.setAdapter(tableAdapter);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ScheduleFragment.OnFragmentInteractionListener) {
            mListener = (ScheduleFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        schedules = Util.sortSchedule(scheduleDao.findAllSchedules());
        tableAdapter.notifyDataSetChanged();
        // This is important, otherwise the result will not be passed to the fragment
        super.onActivityResult(requestCode, resultCode, data);

    }

}
