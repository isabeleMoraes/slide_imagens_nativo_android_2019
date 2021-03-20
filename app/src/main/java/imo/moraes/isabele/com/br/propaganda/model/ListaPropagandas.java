package imo.moraes.isabele.com.br.propaganda.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ListaPropagandas implements Parcelable {
    List<Propaganda> listaPropagandas;

    public ListaPropagandas() {
        this.listaPropagandas = new ArrayList<Propaganda>();
    }

    public ListaPropagandas(List<Propaganda> listaPropagandas) {
        this.listaPropagandas = listaPropagandas;
    }

    public ListaPropagandas(Parcel parcel) {
        this.listaPropagandas = new ArrayList<Propaganda>();
        parcel.readList(this.listaPropagandas, Propaganda.class.getClassLoader());

    }

    public void setListaPropagandas(List<Propaganda> listaPropagandas) {
        this.listaPropagandas = listaPropagandas;
    }

    public void setPropaganda(Propaganda propaganda){
        listaPropagandas.add(propaganda);
    }

    public List<Propaganda> getListaPropagandas() {
        return listaPropagandas;
    }

    public Propaganda getPropaganda(int index){
        return listaPropagandas.get(index);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeList(listaPropagandas);
    }

    public static final Parcelable.Creator<ListaPropagandas> CREATOR = new Parcelable.Creator<ListaPropagandas>(){
        @Override
        public ListaPropagandas createFromParcel(Parcel parcel) {
            return new ListaPropagandas(parcel);
        }

        @Override
        public ListaPropagandas[] newArray(int i) {
            return new ListaPropagandas[i];
        }
    };
}
