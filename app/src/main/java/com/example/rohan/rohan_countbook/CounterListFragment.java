package com.example.rohan.rohan_countbook;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Rohan on 9/6/2017.
 * Purpose: This is the list fragment and is the initial fragment displayed on screen.
 *          This fragment serves three distinct purposes:
 *          1. Provide Summary information about the counters (i.e. total number of counters)
 *          2. Show a list of all counters currently in the system
 *          3. Add a new counter
 *
 *  Design Rationale: This list fragment is largely independent of the activity hosting it. However,
 *                      it expects the activity to have implemented the callback listeners, and
 *                      initialized the counter storage class. Having said that, this fragment is still
 *                      highly customizable barring the aforementioned constraints. For example, the
 *                      information displayed on the list or summary can be changed without affecting
 *                      the main activity or details fragment.
 *
 *                    This fragment uses a RecyclerView (see note 1) instead of a list view, as the former
 *                    is recommended by Google for displaying longer lists as it is more memory efficient.
 *                    As such, a counter app can have lots and lots of counters, hence the RecyclerView was chosen.
 *
 *   Notes: 1. This class makes use of RecyclerView - a view provided by Google and documentation is
 *              here: https://developer.android.com/reference/android/support/v7/widget/RecyclerView.html
 *              Further, a brief tutorial was followed by me in understanding the view here
 *              (DISCLAIMER: This tutorial was followed prior to taking this course):
 *              https://developer.android.com/training/material/lists-cards.html
 *              This view is present in the android support library.
 */

public class CounterListFragment extends Fragment {

    private OnSelectedListener mCallback;

    public interface OnSelectedListener {

        void onCounterSelected(int index);
        void onAddSelected();

    }

    private TextView mCounterSummary;
    private RecyclerView mRecyclerView;
    private CounterAdapter mCounterAdapter;
    private CounterStorage mCounterStorage;

    public static CounterListFragment newInstance() {
        return new CounterListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mCallback = (OnSelectedListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnSelectedListener interface!");
        }
        mCounterStorage = CounterStorage.getCounterStorage();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.counter_list, container, false);
        setHasOptionsMenu(true);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mCounterAdapter = new CounterAdapter(mCounterStorage);
        mRecyclerView.setAdapter(mCounterAdapter);

        mCounterSummary = (TextView) v.findViewById(R.id.numberOfCounters);
        mCounterSummary.setText("Total number of counters: " + Integer.toString(mCounterStorage.getNumberOfCounters()));

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.addCounter:
                mCallback.onAddSelected();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class CounterAdapter extends RecyclerView.Adapter<CounterAdapter.CounterHolder> {

        private CounterStorage mCounterStorage;

        private CounterAdapter(CounterStorage counterStorage) {
            this.mCounterStorage = counterStorage;
        }

        @Override
        public CounterHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.counter_card, null);
            CounterHolder counterHolder = new CounterHolder(v);
            return counterHolder;
        }

        @Override
        public void onBindViewHolder(CounterHolder counterHolder, final int index) {
            counterHolder.onBind(mCounterStorage.getCounter(index), index);
        }

        private void updateUI () {
            mCounterAdapter.notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return mCounterStorage.getNumberOfCounters();
        }

        public class CounterHolder extends RecyclerView.ViewHolder {

            private TextView mName;
            private TextView mCurrentValue;
            private TextView mDate;
            private Button mIncrement;
            private Button mDecrement;

            private CounterHolder(View itemView) {

                super(itemView);

                this.mName = (TextView) itemView.findViewById(R.id.name);
                this.mCurrentValue = (TextView) itemView.findViewById(R.id.currentValue);
                this.mDate = (TextView) itemView.findViewById(R.id.currentDateStamp);
                this.mIncrement = (Button) itemView.findViewById(R.id.incrementCounter);
                this.mDecrement = (Button) itemView.findViewById(R.id.decrementCounter);
            }

            private void onBind(final Counter counter, final int index) {
                mName.setText(counter.getName());
                mCurrentValue.setText(Integer.toString(counter.getCurrentValue()));
                mDate.setText(counter.getLastModifiedDate());
                mIncrement.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        counter.incrementValue();
                        mCounterStorage.saveCounters(getActivity());
                        updateUI();

                    }
                });
                mDecrement.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (counterValidation(counter)) {
                            counter.decrementValue();
                            mCounterStorage.saveCounters(getActivity());
                            updateUI();
                        }
                    }
                });
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCallback.onCounterSelected(index);
                    }
                });
            }

            private boolean counterValidation(Counter counter) {
                if (counter.getCurrentValue() > 0) {
                    return true;
                } else {
                    Toast.makeText(getActivity(), "Cannot decrement counter below 0!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
    }

}
