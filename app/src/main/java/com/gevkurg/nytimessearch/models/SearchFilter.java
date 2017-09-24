package com.gevkurg.nytimessearch.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchFilter implements Parcelable {

    private int year;
    private int monthOfYear;
    private int dayOfMonth;
    private boolean dateSelected;
    private SortOrder sortOrder;
    private Set<String> newsDeskValues;

    public SearchFilter() {
        sortOrder = SortOrder.Newest;
        newsDeskValues = new HashSet<>();
        //set today's date
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        monthOfYear = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        dateSelected = false;
    }

    public Set<String> getNewsDeskValues() {
        return newsDeskValues;
    }

    public void setNewsDeskValues(Set<String> newsDeskValues) {
        this.newsDeskValues = newsDeskValues;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    public void addNewsDesk(String newsDesk) {
        newsDeskValues.add(newsDesk);
    }

    public void removeNewsDesk(String newsDesk) {
        newsDeskValues.remove(newsDesk);
    }

    public String getNewDeskValues() {
        if (newsDeskValues == null || newsDeskValues.isEmpty()) return null;
        StringBuilder sb = new StringBuilder();
        for (String newsDeskValue : newsDeskValues) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append("\"" + newsDeskValue + "\"");
        }
        return sb.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.year);
        dest.writeInt(this.monthOfYear);
        dest.writeInt(this.dayOfMonth);
        dest.writeInt(this.sortOrder == null ? -1 : this.sortOrder.ordinal());
        dest.writeInt(dateSelected ? 1 : 0);
        dest.writeInt(this.newsDeskValues.size());
        if (this.newsDeskValues.size() > 0) {
            List<String> newDeskValues = new ArrayList<>(this.newsDeskValues);
            dest.writeStringList(newDeskValues);
        }
    }

    protected SearchFilter(Parcel in) {
        this.year = in.readInt();
        this.monthOfYear = in.readInt();
        this.dayOfMonth = in.readInt();
        int tmpSortOrder = in.readInt();
        this.sortOrder = tmpSortOrder == -1 ? null : SortOrder.values()[tmpSortOrder];
        final int dateSelectedInt = in.readInt();
        this.dateSelected = dateSelectedInt == 1 ? true : false;
        final int newDeskValuesSize = in.readInt();
        this.newsDeskValues = new HashSet<>();
        if (newDeskValuesSize > 0) {
            List<String> newDeskValues = new ArrayList<>();
            in.readStringList(newDeskValues);
            this.newsDeskValues.addAll(newDeskValues);
        }
    }

    public static final Parcelable.Creator<SearchFilter> CREATOR = new Parcelable.Creator<SearchFilter>() {
        @Override
        public SearchFilter createFromParcel(Parcel source) {
            return new SearchFilter(source);
        }

        @Override
        public SearchFilter[] newArray(int size) {
            return new SearchFilter[size];
        }
    };

    public String getDate() {
        return dateSelected ?
                String.format("%d/%d/%d", monthOfYear + 1, dayOfMonth, year) :
                null;
    }

    public String getDateWithoutSeparator() {
        return dateSelected ?
                String.format("%d%02d%02d", year, monthOfYear + 1, dayOfMonth) :
                null;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public int getMonthOfYear() {
        return monthOfYear;
    }

    public void setMonthOfYear(int monthOfYear) {
        this.monthOfYear = monthOfYear;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setDateSelected(boolean dateSelected) {
        this.dateSelected = dateSelected;
    }

    public enum SortOrder {
        Newest,
        Oldest
    }
}
