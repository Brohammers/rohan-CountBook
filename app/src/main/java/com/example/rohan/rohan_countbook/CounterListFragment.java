package com.example.rohan.rohan_countbook;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rohan on 9/6/2017.
 */

public class CounterListFragment extends Fragment {

    OnCounterCardSelectedListener mCallback;
    OnAddSelectedListener mAddCallback;

    public interface OnCounterCardSelectedListener {

        void onCounterSelected(int index);
        void onIncrementDecrementSelected();

    }

    public interface OnAddSelectedListener {
        void onAddSelected();
    }


    private RecyclerView mRecyclerView;
    private CounterAdapter mCounterAdapter;
    private List<Counter> c;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.counter_list, container, false);
        setHasOptionsMenu(true);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        c = ((MainActivity) getActivity()).mCounters;

        mCounterAdapter = new CounterAdapter(c);
        mRecyclerView.setAdapter(mCounterAdapter);



        //do all your view setup here


        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.addCounter:
                mAddCallback.onAddSelected();
                return true;

            default:
                // default behaviour calls superclass
                return super.onOptionsItemSelected(item);

        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnCounterCardSelectedListener) activity;
            mAddCallback = (OnAddSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement interface(s)!");
        }
    }



    public class CounterAdapter extends RecyclerView.Adapter<CounterAdapter.CounterHolder> {

        private List<Counter> mCounterList;

        public CounterAdapter(List<Counter> counterList) {
            this.mCounterList = counterList;
        }

        @Override
        public CounterHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.counter_card, null);
            CounterHolder counterHolder = new CounterHolder(v);
            return counterHolder;
        }


        @Override
        public void onBindViewHolder(CounterHolder counterHolder, int index) {
            final Counter counter = mCounterList.get(index);
            final int INDEX = index;



            counterHolder.mName.setText(counter.getName());
            Integer i = counter.getCurrentValue();
            counterHolder.mCurrentValue.setText(i.toString());
            counterHolder.mDate.setText(counter.getLastModifiedDate());
            counterHolder.mDescription.setText(counter.getComment());


            counterHolder.mIncrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    counter.setCurrentValue(counter.getCurrentValue() + 1);

                    updateUI(INDEX);

                    mCallback.onIncrementDecrementSelected();

                    //Increment counter

                }
            });

            counterHolder.mDecrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    counter.setCurrentValue(counter.getCurrentValue() - 1);

                    updateUI(INDEX);

                    mCallback.onIncrementDecrementSelected();
                    //Decrement counter

                }
            });

            counterHolder.mDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mCallback.onCounterSelected(INDEX);

                }
            });

        }

        private void updateUI (int index) {
            mCounterAdapter.notifyItemChanged(index);
        }



        @Override
        public int getItemCount() {
            return mCounterList.size();
        }


        public class CounterHolder extends RecyclerView.ViewHolder {

            public TextView mName;
            public TextView mCurrentValue;
            public TextView mDate;
            public TextView mDescription;

            public ImageButton mIncrement;
            public ImageButton mDecrement;

            public CounterHolder(View view) {

                super(view);

                this.mName = (TextView) view.findViewById(R.id.name);
                this.mCurrentValue = (TextView) view.findViewById(R.id.currentValue);
                this.mDate = (TextView) view.findViewById(R.id.currentDateStamp);
                this.mDescription = (TextView) view.findViewById(R.id.Description);

                this.mIncrement = (ImageButton) view.findViewById(R.id.incrementCounter);
                this.mDecrement = (ImageButton) view.findViewById(R.id.decrementCounter);
            }
        }
    }

}
