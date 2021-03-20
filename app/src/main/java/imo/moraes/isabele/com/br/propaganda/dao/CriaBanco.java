package imo.moraes.isabele.com.br.propaganda.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class CriaBanco extends SQLiteOpenHelper {
    public static final String NOME_BANCO = "Propagandas";
    public static final int VERSAO = 1;
    public static String DB_FILEPATH = "/data/data/{package_name}/databases/database.db";


    public CriaBanco(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String tabelaActionFigure = "CREATE TABLE \"propaganda\" (\n" +
                "\t\"_id\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t\"nome\"\tTEXT NOT NULL,\n" +
                "\t\"duracao\"\tNUMERIC NOT NULL,\n" +
                "\t\"imagem_uri\"\tTEXT NOT NULL\n" +
                ");";

        sqLiteDatabase.execSQL(tabelaActionFigure);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String deletaTabelaActionFigure = "DROP TABLE IF EXISTS propaganda";

        sqLiteDatabase.execSQL(deletaTabelaActionFigure);
        onCreate(sqLiteDatabase);
    }

    public boolean onImport(String pathNewBD, String pathOldBD) throws IOException {
        // Close the SQLiteOpenHelper so it will commit the created empty
        // database to internal storage
        close();
        File caminhoBackupBanco = new File(pathNewBD, "Propagandas");
        File caminhoBancoAtivo = new File(pathOldBD);
        if (caminhoBackupBanco.exists()) {
            copyFile(new FileInputStream(caminhoBackupBanco), new FileOutputStream(caminhoBancoAtivo));
            // Access the copied database so SQLiteHelper will cache it and mark
            // it as created.
            getWritableDatabase().close();
            return true;
        }
        return false;
    }

    public boolean onExport(String pathNewBD, String pathOldBD){
        close();

        File caminhoBancoAtivo = new File(pathOldBD);

        File caminhoBackupBanco = new File(pathNewBD, "Propagandas");
        if (!caminhoBackupBanco.exists()){
            caminhoBackupBanco.mkdirs(); // ver se vai funcionar.
        }
        try {
            copyFile(new FileInputStream(caminhoBancoAtivo), new FileOutputStream(caminhoBackupBanco));
            return true;
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void copyFile(FileInputStream fromFile, FileOutputStream toFile) throws IOException {
        FileChannel fromChannel = null;
        FileChannel toChannel = null;
        try {
            fromChannel = fromFile.getChannel();
            toChannel = toFile.getChannel();
            fromChannel.transferTo(0, fromChannel.size(), toChannel);
        } finally {
            try {
                if (fromChannel != null) {
                    fromChannel.close();
                }
            } finally {
                if (toChannel != null) {
                    toChannel.close();
                }
            }
        }
    }
}
