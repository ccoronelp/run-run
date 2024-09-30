package com.searpe.run_run.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.searpe.run_run.Entidades.clases.Evento;
import com.searpe.run_run.R;
import com.searpe.run_run.recursos.RecursosStatic;

import java.util.ArrayList;


/**
 * Created by imiranda on 01/08/2017.
 */

public class ListViewFilterAdapter extends BaseAdapter {

    public ArrayList<Evento> data;
    public static Activity activity;

    public ListViewFilterAdapter(Activity a, ArrayList<Evento> d) {
        this.activity = a;
        this.data = d;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Evento getItem(int position) {
        Evento conserva = data.get(position);
        return conserva;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.view_item, null);

            viewHolder = new ViewHolder();

            viewHolder.tv1 = convertView
                    .findViewById(R.id.tv1);
            viewHolder.tv2 = convertView
                    .findViewById(R.id.tv2);

            convertView.setTag(viewHolder);
            convertView.setTag(R.id.tv1, viewHolder.tv1);
            convertView.setTag(R.id.tv2, viewHolder.tv2);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Evento Evento = data.get(position);
        viewHolder.tv1.setText(Evento.getNombre());
        viewHolder.tv2.setText(RecursosStatic.formatter.format(Evento.getDistancia()) + " km (" + Evento.getFecha() + " " + Evento.getHora()+")");
        return convertView;
    }



    class ViewHolder {
        protected TextView tv1, tv2;
    }
}
