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

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScheduleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleFragment extends Fragment {
    private static final Integer CREATE_EVENT = 5000;

    private RecyclerView table;
    private TextView selectedDate;
    private RecyclerView.Adapter tableAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ScheduleDao scheduleDao;

    private OnFragmentInteractionListener mListener;
    private List<Schedule> schedules;
    private View view;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ScheduleFragment.
     */
    public static ScheduleFragment newInstance() {
        return new ScheduleFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_schedule, container, false);
        // Inflate the layout for this fragment
        table = (RecyclerView) view.findViewById(R.id.schedule_table);
        selectedDate = (TextView) view.findViewById(R.id.selectedDate);
        scheduleDao = new ScheduleDao(getActivity());
        schedules = Util.getImediate15(scheduleDao.findAllSchedules());
        tableAdapter = new ScheduleTableAdapter(schedules, this);


        mLayoutManager = new LinearLayoutManager(getActivity());
        table.setLayoutManager(mLayoutManager);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createEvent = new Intent(getActivity(), CreateEventActivity.class);
                getActivity().startActivityForResult(createEvent, CREATE_EVENT, null);
            }
        });

        table.setAdapter(tableAdapter);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        schedules = Util.getImediate15(scheduleDao.findAllSchedules());
        tableAdapter.notifyDataSetChanged();
        // This is important, otherwise the result will not be passed to the fragment
        super.onActivityResult(requestCode, resultCode, data);

    }
}
