package com.skt.tmaphot.activity.main.hotdeal;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skt.tmaphot.MainApplication;
import com.skt.tmaphot.R;
import com.skt.tmaphot.activity.IRecyclerItem;
import com.skt.tmaphot.activity.IRecyclerViewDataAdapter;

import java.util.ArrayList;
import java.util.List;

public class HotdealRecyclerViewDataAdapter extends RecyclerView.Adapter<HotdealRecyclerViewHolder> implements IRecyclerViewDataAdapter {

    private Context context;
    private ArrayList<IRecyclerItem> viewItemList;


    public HotdealRecyclerViewDataAdapter(Context context, ArrayList<IRecyclerItem> viewItemList) {
        this.context = context;
        this.viewItemList = viewItemList;
    }


    public HotdealRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get LayoutInflater object.
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // Inflate the RecyclerView item layout xml.
        View itemView = layoutInflater.inflate(R.layout.main_hotdeal_recycler_item, parent, false);

        // Create and return our customRecycler View Holder object.
        HotdealRecyclerViewHolder ret = new HotdealRecyclerViewHolder(itemView);
        return ret;
    }

    @Override
    public void onBindViewHolder(HotdealRecyclerViewHolder holder, int position) {
        if(viewItemList!=null) {
            // Get car item dto in list.
            HotdealRecyclerViewItem viewItem = (HotdealRecyclerViewItem)viewItemList.get(position);

            if(viewItem != null) {
                // Set car item title.
                MainApplication.loadUrlImage(context , viewItem.getUrl(), holder.getmImgReview());
                holder.getmTitleText().setText(viewItem.getTitle());
                holder.getmMenueText().setText(viewItem.getMenu());
            }
        }
    }

    @Override
    public int getItemCount() {
        int ret = 0;
        if(viewItemList!=null)
        {
            ret = viewItemList.size();
        }
        return ret;
    }

    @Override
    public void notifyData() {
        this.notifyDataSetChanged();
    }
}