package imo.moraes.isabele.com.br.propaganda.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.List;

import imo.moraes.isabele.com.br.propaganda.R;
import imo.moraes.isabele.com.br.propaganda.model.Propaganda;
import imo.moraes.isabele.com.br.propaganda.model.PropagandaController;
import imo.moraes.isabele.com.br.propaganda.model.RecyclerViewOnClickListenerHack;

public class AdapterPropagandas extends RecyclerView.Adapter {
    private List<Propaganda> propagandas;
    private Context context;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;


    public AdapterPropagandas(List<Propaganda> propagandas, Context context) {
        this.propagandas = propagandas;
        this.context = context;
    }

    public void setmRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r){
        mRecyclerViewOnClickListenerHack =r;
    }

    public Propaganda getPropaganda(int index){
        return propagandas.get(index);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_card_view,viewGroup,false);

        ViewHolder holder = new ViewHolder(view, mRecyclerViewOnClickListenerHack);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        ViewHolder holder = (ViewHolder) viewHolder;

        Propaganda propaganda = propagandas.get(position) ;

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), android.net.Uri.parse(propaganda.getImagemURI()));

            holder.nome.setText(propaganda.getNome());
            holder.duracao.setText(String.valueOf(propaganda.getDuracao()));
            holder.imagem.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return propagandas.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
