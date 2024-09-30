package com.searpe.run_run.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.searpe.run_run.Entidades.clases.Participante;
import com.searpe.run_run.Holder.ResultadoViewHolder;
import com.searpe.run_run.R;

import java.util.ArrayList;

public class RecycleViewAdapter extends RecyclerView.Adapter<ResultadoViewHolder> {

    private ArrayList<Participante> mData;
    private LayoutInflater mInflater;
    private Context context;

    public RecycleViewAdapter(Context context, ArrayList<Participante> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ResultadoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.view_resultados, parent, false);
        return new ResultadoViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ResultadoViewHolder holder, int position) {
        Participante model = mData.get(position);
        holder.itemView.setVisibility(View.VISIBLE);
        holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        holder.getTv1().setText((position + 1) + ". " + model.getNombre());
        holder.getTv2().setText(model.getTiempoText());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
        Glide.with(context).load(model.getFoto()).apply(requestOptions).error(R.drawable.ic_outline_no_photography_24).into(holder.getFoto());

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen


    // convenience method for getting data at click position
    public Participante getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught

}
