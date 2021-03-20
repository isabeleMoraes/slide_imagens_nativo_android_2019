package imo.moraes.isabele.com.br.propaganda.view;

import android.content.Context;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.util.List;

import imo.moraes.isabele.com.br.propaganda.R;
import imo.moraes.isabele.com.br.propaganda.model.Propaganda;

public class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<Propaganda> listaPropagandas;


    public ViewPagerAdapter(Context context, List<Propaganda> listaPropagandas) {
        this.context = context;
        this.listaPropagandas = listaPropagandas;
    }

    @Override
    public int getCount() {
        return listaPropagandas.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_layou_view_pager,null);
        ImageView imageView = view.findViewById(R.id.img_propaganda_view_pager);
        try {
            imageView.setImageBitmap(MediaStore.Images.Media.getBitmap(context.getContentResolver(), android.net.Uri.parse(listaPropagandas.get(position).getImagemURI())));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY); //FIT_XY ou //CENTER_CROP
        } catch (IOException e) {
            e.printStackTrace();
        }

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view,0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }
}
