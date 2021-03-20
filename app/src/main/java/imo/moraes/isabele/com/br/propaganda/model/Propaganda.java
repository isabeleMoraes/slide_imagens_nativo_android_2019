package imo.moraes.isabele.com.br.propaganda.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Propaganda implements Parcelable {
    private int id;
    private String nome;
    private int duracao;
    private String imagemURI;

    public Propaganda(String nome, int duracao, String imagemURI) {
        this.id = 0;
        this.nome = nome;
        this.duracao = duracao;
        this.imagemURI = imagemURI;
    }

    public Propaganda(int id, String nome, int duracao, String imagemURI) {
        this.id = id;
        this.nome = nome;
        this.duracao = duracao;
        this.imagemURI = imagemURI;
    }

    public Propaganda(Parcel parcel) {
        this.id = parcel.readInt();
        this.nome = parcel.readString();
        this.duracao = parcel.readInt();
        this.imagemURI = parcel.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    public String getImagemURI() {
        return imagemURI;
    }

    public void setImagemURI(String imagemURI) {
        this.imagemURI = imagemURI;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(nome);
        parcel.writeInt(duracao);
        parcel.writeString(imagemURI);
    }

    public static final Parcelable.Creator<Propaganda> CREATOR = new Parcelable.Creator<Propaganda>(){
        @Override
        public Propaganda createFromParcel(Parcel parcel) {
            return new Propaganda(parcel);
        }

        @Override
        public Propaganda[] newArray(int i) {
            return new Propaganda[0];
        }
    };
}
