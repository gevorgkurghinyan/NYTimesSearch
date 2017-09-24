package com.gevkurg.nytimessearch.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.gevkurg.nytimessearch.R;
import com.gevkurg.nytimessearch.models.SearchFilter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterFragment extends DialogFragment {
    public static final String FILTER_OPTIONS_KEY = "filterOptionsKey";
    public static final int DEFAULT_SORT_POSITION = 0;

    @BindView(R.id.dpBeginDate)
    DatePicker dpBeginDate;
    @BindView(R.id.spSortOrder)
    Spinner spSortOrder;
    @BindView(R.id.cbArts)
    CheckBox cbArts;
    @BindView(R.id.cbFashionAndStyle)
    CheckBox cbFashionAndStyle;
    @BindView(R.id.cbSports)
    CheckBox cbSports;
    @BindView(R.id.btnSaveFilter)
    Button btnSaveFilter;

    private SearchFilter mSearchFilter;
    private SearchFilterUpdateListener mSearchFilterUpdateListener;

    public FilterFragment() {
    }

    public interface SearchFilterUpdateListener {
        void onFilterOptionsChanged(SearchFilter searchFilter);
    }

    public static FilterFragment newInstance(String title) {
        FilterFragment fragment = new FilterFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mSearchFilter = getArguments().getParcelable(FilterFragment.FILTER_OPTIONS_KEY);
        mSearchFilterUpdateListener = (SearchFilterUpdateListener) getActivity();
        btnSaveFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mSearchFilter.setYear(dpBeginDate.getYear());
                mSearchFilter.setMonthOfYear(dpBeginDate.getMonth());
                mSearchFilter.setDayOfMonth(dpBeginDate.getDayOfMonth());
                mSearchFilter.setDateSelected(true);

                final String sortOrder = spSortOrder.getSelectedItem().toString();
                mSearchFilter.setSortOrder(SearchFilter.SortOrder.valueOf(sortOrder));
                final String arts = getString(R.string.chk_arts);

                if (cbArts.isChecked()) {
                    mSearchFilter.addNewsDesk(arts);
                } else {
                    mSearchFilter.removeNewsDesk(arts);
                }

                final String fashionStyle = getString(R.string.chk_fashion_and_style);

                if (cbFashionAndStyle.isChecked()) {
                    mSearchFilter.addNewsDesk(fashionStyle);
                } else {
                    mSearchFilter.removeNewsDesk(fashionStyle);
                }

                final String sports = getString(R.string.chk_sports);

                if (cbSports.isChecked()) {
                    mSearchFilter.addNewsDesk(sports);
                } else {
                    mSearchFilter.removeNewsDesk(sports);
                }

                if (mSearchFilterUpdateListener != null) {
                    mSearchFilterUpdateListener.onFilterOptionsChanged(mSearchFilter);
                    getDialog().dismiss();
                }
            }
        });

        spSortOrder.setSelection(DEFAULT_SORT_POSITION);
        populateUI();
    }

    private void populateUI() {
        if (mSearchFilter.getDate() != null) {
            dpBeginDate.updateDate(mSearchFilter.getYear(),
                    mSearchFilter.getMonthOfYear(), mSearchFilter.getDayOfMonth());
        }

        if (mSearchFilter.getSortOrder() != null) {
            switch (mSearchFilter.getSortOrder()) {
                case Newest:
                    spSortOrder.setSelection(SearchFilter.SortOrder.Newest.ordinal());
                    break;
                default:
                    spSortOrder.setSelection(SearchFilter.SortOrder.Oldest.ordinal());
            }
        }

        cbArts.setChecked(false);
        cbArts.setChecked(false);
        cbArts.setChecked(false);

        if (mSearchFilter.getNewsDeskValues() != null) {
            for (String newsDeskValue : mSearchFilter.getNewsDeskValues()) {
                if (newsDeskValue.equals(getString(R.string.chk_arts))) {
                    cbArts.setChecked(true);
                } else if (newsDeskValue.equals(getString(R.string.chk_sports))) {
                    cbSports.setChecked(true);
                } else if (newsDeskValue.equals(getString(R.string.chk_fashion_and_style))) {
                    cbFashionAndStyle.setChecked(true);
                }
            }
        }
    }
}