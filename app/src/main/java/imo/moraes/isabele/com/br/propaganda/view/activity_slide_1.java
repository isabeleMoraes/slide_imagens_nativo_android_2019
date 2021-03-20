package imo.moraes.isabele.com.br.propaganda.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import java.io.IOException;

import imo.moraes.isabele.com.br.propaganda.R;
import imo.moraes.isabele.com.br.propaganda.model.ListaPropagandas;
import imo.moraes.isabele.com.br.propaganda.model.PropagandaController;

public class activity_slide_1 extends AppCompatActivity implements GestureDetector.OnGestureListener{

    private ListaPropagandas listaPropagandas;

    private int indexImagemAtual;

    private ViewFlipper viewFliper;

    //private SlidrInterface slidr;

    GestureDetectorCompat detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_1);

        viewFliper = findViewById(R.id.view_flipper_prog);

        Bundle parametros = getIntent().getExtras();

        if(parametros != null){
            Log.d("TESTE", "entrou no if");
            listaPropagandas = parametros.getParcelable("listaPropagandas");
            indexImagemAtual = parametros.getInt("indexInicial");
        }

            /*viewFliper.setFlipInterval(2000);
            viewFliper.startFlipping();*/

            while(indexImagemAtual < listaPropagandas.getListaPropagandas().size()) {
                flipperImages();
            }

        detector = new GestureDetectorCompat(this,this);

    }




    public void flipperImages(){
        Bitmap bitmap = null;
        try {
            ImageView imgView = new ImageView(this);
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), android.net.Uri.parse(listaPropagandas.getListaPropagandas().get(indexImagemAtual).getImagemURI()));
            imgView.setImageBitmap(bitmap);
            imgView.setScaleType(ImageView.ScaleType.FIT_XY);

            Log.d("IAMGEM " + indexImagemAtual, listaPropagandas.getListaPropagandas().get(indexImagemAtual).getImagemURI());

            viewFliper.addView(imgView);
            viewFliper.setFlipInterval(4000);
            viewFliper.setAutoStart(true);
            indexImagemAtual++;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        float sensivity = 50;

        if((motionEvent.getX() - motionEvent1.getX()) > sensivity && viewFliper.getDisplayedChild() == 1) {
            Log.d("MOVIMENTOU", "UM");
            //viewFliper.setInAnimation(R.anim.slide_out_left);
            //viewFliper.startAnimation(slide_in_right);
            viewFliper.showPrevious();


        }else if((motionEvent1.getX() - motionEvent.getX()) > sensivity && viewFliper.getDisplayedChild() == 0){
            Log.d("MOVIMENTOU", "DOIS");
            //viewFliper.startAnimation(slide_out_right);
            //viewFliper.startAnimation(slide_in_left);
                    viewFliper.showNext();

        }
        return true;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        /*float sensivity = 50;

        if((motionEvent.getX() - motionEvent1.getX()) > sensivity && viewFliper.getDisplayedChild() == 1) {
            Log.d("MOVIMENTOU", "UM");
            //viewFliper.setInAnimation(R.anim.slide_out_left);
            //viewFliper.startAnimation(slide_in_right);
            viewFliper.showPrevious();


        }else if((motionEvent1.getX() - motionEvent.getX()) > sensivity && viewFliper.getDisplayedChild() == 0){
            Log.d("MOVIMENTOU", "DOIS");
            //viewFliper.startAnimation(slide_out_right);
            //viewFliper.startAnimation(slide_in_left);
            ViewFlipper.
            viewFliper.showNext();

        }
        return true;*/

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        return super.onTouchEvent(event);

    }

    //Solução do video feito com botão. Comentado para poder fazer com slide na tela.
    /*private void chamarTela(){
        Intent it = new Intent(this, activity_slide_2.class);
        Bundle parametros = new Bundle();

        parametros.putParcelable("listaPropagandas", listaPropagandas);
        parametros.putInt("indexInicial", indexImagemAtual++);
        it.putExtras(parametros);
        startActivity(it);

        overridePendingTransition(R.anim.slide_in_rigth, R.anim.slide_out_left);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_rigth);
    }*/

    /*@Override
    public boolean onTouchEvent(final MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                Log.i("ON TOUCH EVENT", "DOWN + X: " + x + " Y " + y);
                break;

            case MotionEvent.ACTION_MOVE:
                chamarTela();
                break;

            case MotionEvent.ACTION_UP:
                Log.i("ON TOUCH EVENT", "UP + X: " + x + " Y " + y);
                break;

            default:
                break;
        }

        return super.onTouchEvent(event);
    }*/
}
