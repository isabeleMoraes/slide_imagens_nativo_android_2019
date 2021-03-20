package imo.moraes.isabele.com.br.propaganda.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

import imo.moraes.isabele.com.br.propaganda.dao.CriaBanco;

public class BancoController {

    private static String CAMINHO_BANCO_IMPORTACAO;
    private static String CAMINHO_BANCO_EXPORTACAO ;

    private CriaBanco banco;

    public BancoController(Context context) {
        banco = new CriaBanco(context);
        CAMINHO_BANCO_IMPORTACAO = Environment.getExternalStorageDirectory()+"/Propagandas";
        CAMINHO_BANCO_EXPORTACAO = "/data/data/imo.moraes.isabele.com.br.propaganda/databases/Propagandas";
    }

    public boolean importarBanco() throws IOException {
        return banco.onImport(CAMINHO_BANCO_IMPORTACAO, CAMINHO_BANCO_EXPORTACAO);
    }

    public boolean exportarBanco() throws IOException {
        return banco.onExport(CAMINHO_BANCO_IMPORTACAO, CAMINHO_BANCO_EXPORTACAO);
    }

}
