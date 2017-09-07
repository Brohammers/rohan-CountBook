package com.example.rohan.rohan_countbook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rohan on 9/6/2017.
 */

public class CounterDetailsFragment extends Fragment {

    private int mIndex;

    private EditText mName;
    private EditText mInitialValue;
    private EditText mCurrentValue;
    private EditText mDescription;
    private TextView mDate;
    private Button mResetCounter;
    private Button mSave;

    private Counter mCounter;

    OnCounterSavedListener mCallback;

    public interface OnCounterSavedListener {

        void onCounterSaved();
        void onDelete();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnCounterSavedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement interface");
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_details, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.deleteCounter:
                Toast.makeText(getActivity(), "to implement delete", Toast.LENGTH_SHORT).show();
                return true;
            default:
                // default behaviour calls superclass
                return super.onOptionsItemSelected(item);

        }

    }

    public static CounterDetailsFragment newInstance(int index) {

        Bundle args = new Bundle();
        args.putInt("index", index);
        args.putBoolean("isValid", true);
        CounterDetailsFragment fragment = new CounterDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.counter_details, container, false);
        setHasOptionsMenu(true);

        mName = (EditText) v.findViewById(R.id.editTextName);
        mInitialValue = (EditText) v.findViewById(R.id.editTextInitialValue);
        mCurrentValue = (EditText) v.findViewById(R.id.editTextCurrentValue);
        mDescription = (EditText) v.findViewById(R.id.editTextDescription);
        mDate = (TextView) v.findViewById(R.id.edit_date);
        mResetCounter = (Button) v.findViewById(R.id.resetCounter);
        mSave = (Button) v.findViewById(R.id.Save);

        if (getArguments() != null && getArguments().getBoolean("isValid")) {
            mIndex = getArguments().getInt("index");
            initExistingCounter(v);
        } else {
            initNewCounter(v);
        }

        return v;
    }

    private void initExistingCounter(View v) {
        mCounter = ((MainActivity) getActivity()).mCounters.get(mIndex);
        mName.setText(mCounter.getName());
        mInitialValue.setText(Integer.toString(mCounter.getInitialValue()));

        mCurrentValue.setText(Integer.toString(mCounter.getCurrentValue()));
        mCurrentValue.setEnabled(true);

        mDescription.setText(mCounter.getComment());
        mDate.setText(mCounter.getLastModifiedDate());

        mResetCounter.setEnabled(true);
        mResetCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentValue.setText(Integer.toString(mCounter.getInitialValue()));

            }
        });

        mSave.setEnabled(true);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (! (mCurrentValue.getText().toString().equals( ((MainActivity) getActivity()).mCounters.get(mIndex).getCurrentValue())) ) {
                    ((MainActivity) getActivity()).mCounters.get(mIndex).setLastModifiedDate();

                }
                ((MainActivity) getActivity()).mCounters.get(mIndex).setName(mName.getText().toString());
                ((MainActivity) getActivity()).mCounters.get(mIndex).setCurrentValue(Integer.valueOf(mCurrentValue.getText().toString()));
                ((MainActivity) getActivity()).mCounters.get(mIndex).setComment(mDescription.getText().toString());
                mCallback.onCounterSaved();
            }
        });


    }

    private void initNewCounter(View v) {
        mInitialValue.setEnabled(true);


        mSave.setEnabled(true);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (  (mName.getText().toString().equals("")) && (mInitialValue.getText().toString().equals("")) ) {

                    Toast.makeText(getActivity(), "Please enter name and initial value for counter", Toast.LENGTH_SHORT).show();
                    return;

                } else if (mName.getText().toString().equals("")) {

                    Toast.makeText(getActivity(), "Please enter name for counter", Toast.LENGTH_SHORT).show();
                    return;

                } else if (mInitialValue.getText().toString().equals("")) {

                    Toast.makeText(getActivity(), "Please enter initial value for counter", Toast.LENGTH_SHORT).show();
                    return;

                }

                Counter newCounter = new Counter(mName.getText().toString(), mDescription.getText().toString(), Integer.valueOf(mInitialValue.getText().toString()));
                ((MainActivity) getActivity()).mCounters.add(newCounter);

                Toast.makeText(getActivity(), "Counter successfully saved!", Toast.LENGTH_SHORT).show();

                mCallback.onCounterSaved();

            }
        });


    }

}
