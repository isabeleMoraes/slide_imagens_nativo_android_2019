package imo.moraes.isabele.com.br.propaganda.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import imo.moraes.isabele.com.br.propaganda.dao.CriaBanco;

public class PropagandaController {
    private static final String NOME_TABELA = "propaganda";
    private ContentValues valores;
    private long retornoOperacao;
    private CriaBanco banco;

    public PropagandaController(Context context) {
        banco = new CriaBanco(context);
    }

    public String gravar(Propaganda propaganda){
        valores = new ContentValues();
        extrairDados(propaganda);
        retornoOperacao = banco.getWritableDatabase().insert(NOME_TABELA,null,valores);

        return verificaSucesso("gravar");
    }

    public String alterar(Propaganda propaganda){
        valores = new ContentValues();
        extrairDados(propaganda);
        String[] parametros = {String.valueOf(propaganda.getId())};
        retornoOperacao =banco.getWritableDatabase().update(NOME_TABELA,valores,"_id=?",parametros);
        Log.d("ID_ALTERADO:", String.valueOf(propaganda.getId()));
        Log.d("DADOS_ALTERADOS:", valores.toString());
        return verificaSucesso("alterar");
    }

    public String deletar(Propaganda propaganda){
        String[] parametros = {String.valueOf(propaganda.getId())};
        retornoOperacao = banco.getWritableDatabase().delete(NOME_TABELA,"_id=?",parametros);
        return verificaSucesso("deletar");
    }

    public List<Propaganda> buscarTodos(){
        SQLiteDatabase db = banco.getWritableDatabase();

        Cursor c = db.rawQuery("select * from propaganda;", null);

        List<Propaganda> listaPropagandas = new ArrayList<Propaganda>();

        while(c.moveToNext()){

            int id = c.getInt(c.getColumnIndex("_id"));
            String nome = c.getString(c.getColumnIndex("nome"));
            int duracao = c.getInt(c.getColumnIndex("duracao"));
            String imagemURI = c.getString(c.getColumnIndex("imagem_uri"));

            Propaganda propaganda = new Propaganda(id,nome,duracao,imagemURI);

            listaPropagandas.add(propaganda);
        }

        c.close();

        return listaPropagandas;
    }

    public void extrairDados(Propaganda propaganda){
        valores.put("nome",propaganda.getNome());
        valores.put("duracao",propaganda.getDuracao());
        valores.put("imagem_uri",propaganda.getImagemURI());

    }

    public String verificaSucesso(String operacao){
        if(retornoOperacao == -1){
            return "ERRO ao "+operacao+" propaganda";
        }else{
            return "Sucesso ao "+operacao+" propaganda";
        }
    }
}
