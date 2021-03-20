package imo.moraes.isabele.com.br.propaganda.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import imo.moraes.isabele.com.br.propaganda.R;
import imo.moraes.isabele.com.br.propaganda.model.ListaPropagandas;

public class activity_slide_2 extends AppCompatActivity {

    private ViewPager viewPager;

    private ListaPropagandas listaPropagandas;

    private int indexImagemAtual;

    private Timer timer;

    private int dalay;

    private int tamanhoLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_slide_2);

        viewPager = findViewById(R.id.view_pager_prog);

        Bundle parametros = getIntent().getExtras();

        if(parametros != null){
            listaPropagandas = parametros.getParcelable("listaPropagandas");
            indexImagemAtual = parametros.getInt("indexInicial");
        }

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this,listaPropagandas.getListaPropagandas());

        viewPager.setAdapter(viewPagerAdapter);

        dalay = listaPropagandas.getListaPropagandas().get(0).getDuracao()*1000;

        tamanhoLista = listaPropagandas.getListaPropagandas().size();

        createSlideShow();

    }

    private void createSlideShow(){
        Log.d("CHEGOU AQUI", "metodo");
        final int tamanhoLista = listaPropagandas.getListaPropagandas().size();
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {

            @Override
            public void run() {
                Log.d("CHEGOU AQUI", "Aquio 2 - Index: "+indexImagemAtual);

                viewPager.setCurrentItem(indexImagemAtual,true);

                indexImagemAtual++;
                Log.d("CHEGOU AQUI", "Aqui 3 - "+dalay);

                if(indexImagemAtual == tamanhoLista){
                    indexImagemAtual = 0;
                }
                dalay = listaPropagandas.getListaPropagandas().get(indexImagemAtual).getDuracao()*1000;
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d("CHEGOU AQUI", "Aqui 1");
                handler.post(runnable);
                try {
                    Thread.sleep(dalay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },2000, 1);

    }

    @Override
    public void onBackPressed() {
        //Não permite voltar
    }
}
