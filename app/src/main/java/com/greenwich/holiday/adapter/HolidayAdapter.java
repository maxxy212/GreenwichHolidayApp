package com.greenwich.holiday.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.greenwich.holiday.BR;
import com.greenwich.holiday.R;
import com.greenwich.holiday.model.Holiday;
import com.greenwich.holiday.utils.DateUtil;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Package com.greenwich.holiday.adapter in
 * <p>
 * Project HolidayApp
 * <p>
 * Created by Maxwell on 4/24/21
 */
public class HolidayAdapter extends RealmRecyclerViewAdapter<Holiday, HolidayAdapter.HolidayItemHolder> {

    private DateUtil dateUtil;
    private Context context;

    public HolidayAdapter(@Nullable OrderedRealmCollection<Holiday> data, Context context) {
        super(data, true, true);
        this.context = context;
    }

    @NonNull
    @Override
    public HolidayItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding _binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.layout_req_list, parent, false);
        return new HolidayAdapter.HolidayItemHolder(_binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HolidayItemHolder holder, int position) {
        final Holiday holiday = getItem(position);
        holder.setHoliday(holiday);
    }

    public class HolidayItemHolder extends RecyclerView.ViewHolder {

        ViewDataBinding dataBinding;

        public HolidayItemHolder(ViewDataBinding itemView) {
            super(itemView.getRoot());
            dataBinding = itemView;
        }

        public void setHoliday(final Holiday holiday){
            dataBinding.setVariable(BR.holiday, holiday);
            dateUtil = new DateUtil(context);
            dataBinding.setVariable(BR.date, dateUtil.formatDate(holiday.created_at, null));
        }
    }
}
