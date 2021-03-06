package com.example.rohan.rohan_countbook;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Rohan on 9/6/2017.
 * Purpose: This is the Counter class and is used for modelling the state and behaviour of a given
 *          counter.
 *
 * Design Rationale: This class was created to define exactly what it means to be a counter.
 */

public class Counter {

    private String mName;
    private String mComment;
    private String  mLastModifiedDate;
    private int mInitialValue;
    private int mCurrentValue;

    public Counter(String name, String comment, int initialValue, int currentValue) {
        this.mName = name;
        this.mComment = comment;
        setLastModifiedDate();
        this.mInitialValue = initialValue;
        this.mCurrentValue = currentValue;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String comment) {
        this.mComment = comment;
    }

    public String getLastModifiedDate() {
        return mLastModifiedDate;
    }

    public void setLastModifiedDate() {
        this.mLastModifiedDate = getCurrentDate();
    }

    public int getInitialValue() {
        return mInitialValue;
    }

    public void setInitialValue(int initialValue) {
        this.mInitialValue = initialValue;
    }

    public int getCurrentValue() {
        return mCurrentValue;
    }

    public void setCurrentValue(int currentValue) {
        this.mCurrentValue = currentValue;
        this.setLastModifiedDate();
    }

    public void incrementValue() {
        this.setCurrentValue(this.getCurrentValue() + 1);
        this.setLastModifiedDate();
    }

    public void decrementValue() {
        this.setCurrentValue(this.getCurrentValue() - 1);
        this.setLastModifiedDate();
    }

    private String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }
}
