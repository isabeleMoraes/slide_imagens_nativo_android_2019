package imo.moraes.isabele.com.br.propaganda.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import imo.moraes.isabele.com.br.propaganda.R;
import imo.moraes.isabele.com.br.propaganda.model.RecyclerViewOnClickListenerHack;


public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    final TextView nome;
    final TextView duracao;
    final ImageView imagem;

    RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

    public ViewHolder(@NonNull View itemView, RecyclerViewOnClickListenerHack r) {
        super(itemView);

        nome = itemView.findViewById(R.id.txt_nome_propaganda);
        duracao = itemView.findViewById(R.id.txt_duracao_propaganda);
        imagem = itemView.findViewById(R.id.image_view_propaganda);
        mRecyclerViewOnClickListenerHack = r;

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(mRecyclerViewOnClickListenerHack != null){
            mRecyclerViewOnClickListenerHack.onClickListener(view, getAdapterPosition());
        }
    }

}
