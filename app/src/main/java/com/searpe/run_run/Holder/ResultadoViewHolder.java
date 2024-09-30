package com.searpe.run_run.Holder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.searpe.run_run.R;

public class ResultadoViewHolder extends RecyclerView.ViewHolder {


    private TextView tv1,tv2,tv3,tv4,tv5,tv6;
    private CardView cv1;
    private Button btn1;
    private ImageView civFotoPerfil;

    public ResultadoViewHolder(@NonNull View itemView) {
        super(itemView);
        tv1 =itemView.findViewById(R.id.tv1);
        tv2 =itemView.findViewById(R.id.tv2);
        tv3 =itemView.findViewById(R.id.tv3);
        tv4 =itemView.findViewById(R.id.tv4);
        btn1 =itemView.findViewById(R.id.btn1);
        cv1 = itemView.findViewById(R.id.cv1);
        civFotoPerfil = itemView.findViewById(R.id.img1);
    }

    public Button getBtn1() {
        return btn1;
    }

    public void setBtn1(Button btn1) {
        this.btn1 = btn1;
    }

    public CardView getCv1() {
        return cv1;
    }

    public void setCv1(CardView cv1) {
        this.cv1 = cv1;
    }

    public ImageView getFoto() {
        return civFotoPerfil;
    }

    public void setCivFotoPerfil(ImageView civFotoPerfil) {
        this.civFotoPerfil = civFotoPerfil;
    }

    public TextView getTv6() {
        return tv6;
    }

    public void setTv6(TextView tv6) {
        this.tv6 = tv6;
    }

    public TextView getTv4() {
        return tv4;
    }

    public void setTv4(TextView tv4) {
        this.tv4 = tv4;
    }

    public TextView getTv5() {
        return tv5;
    }

    public void setTv5(TextView tv5) {
        this.tv5 = tv5;
    }

    public TextView getTv1() {
        return tv1;
    }

    public void setTv1(TextView tv1) {
        this.tv1 = tv1;
    }

    public TextView getTv2() {
        return tv2;
    }

    public void setTv2(TextView tv2) {
        this.tv2 = tv2;
    }

    public TextView getTv3() {
        return tv3;
    }

    public void setTv3(TextView tv3) {
        this.tv3 = tv3;
    }
}
