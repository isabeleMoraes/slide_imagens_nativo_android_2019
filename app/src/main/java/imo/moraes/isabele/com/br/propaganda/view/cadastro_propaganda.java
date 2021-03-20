package imo.moraes.isabele.com.br.propaganda.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.channels.FileChannel;

import imo.moraes.isabele.com.br.propaganda.R;
import imo.moraes.isabele.com.br.propaganda.model.ListaPropagandas;
import imo.moraes.isabele.com.br.propaganda.model.Propaganda;
import imo.moraes.isabele.com.br.propaganda.model.PropagandaController;
import me.drakeet.materialdialog.MaterialDialog;

public class cadastro_propaganda extends AppCompatActivity {

    public static final int IMAGEM_INTERNA = 1;

    //Componentes da tela
    private EditText txtNome;
    private EditText txtDuracao;
    private ImageView imgPropaganda;
    private Button btnCarregarImagem;
    private Button btnGravar;
    private Button btnExcluir;

    // Dados
    private File dir;
    private Uri imagemSelecionada;
    private String pathImg;
    private MaterialDialog mMaterialDialog;
    private Propaganda propaganda;
    private PropagandaController propagandaController;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_propaganda);
        getSupportActionBar().setTitle(this.getResources().getString(R.string.action_bar_cadastro));

        txtNome = findViewById(R.id.txt_nome);
        txtDuracao = findViewById(R.id.txt_duracao);
        btnCarregarImagem = findViewById(R.id.btn_carregar);
        imgPropaganda = findViewById(R.id.img_propaganda);
        btnGravar = findViewById(R.id.btn_gravar);
        btnExcluir = findViewById(R.id.btn_excluir);

        propagandaController = new PropagandaController(getBaseContext());

        Bundle parametros = getIntent().getExtras();
        System.out.println(parametros);
        propaganda=null;

        if(parametros != null){
            propaganda = parametros.getParcelable("propaganda");
            carregarDadosPropaganda(propaganda);
        }

        pathImg = null;

        btnCarregarImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission( getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED ){
                    if( ActivityCompat.shouldShowRequestPermissionRationale( cadastro_propaganda.this, Manifest.permission.READ_EXTERNAL_STORAGE ) ){
                        dialogPermissao( "É necessario conceder permissão para acesso aos arquivos do aparelho", new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
                    }
                    else{
                        ActivityCompat.requestPermissions( cadastro_propaganda.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, IMAGEM_INTERNA );
                    }
                }
                else{
                    //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, IMAGEM_INTERNA);
                }
            }
        });

        btnGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dir = new File(Environment.getExternalStorageDirectory()+"/Propagandas");

                if (!dir.exists()){
                    dir.mkdirs();
                }

                if((pathImg == null || pathImg.isEmpty())&&propaganda==null){
                    Toast.makeText(getApplicationContext(), "Carregue uma imagem!", Toast.LENGTH_SHORT).show();
                }else{
                    if(propaganda == null){
                        gravarPropaganda();
                    }else{
                        atualizarPropaganda();
                    }

                }

            }
        });

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(propaganda != null){
                    dialogExcluir();
                }else{
                    Toast.makeText(getApplicationContext(), "Não há dados para ser excluido!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void gravarPropaganda(){
        String nome = txtNome.getText().toString();
        String duracaoCampo = txtDuracao.getText().toString();


        String retorno="";

        if(dadosValidos(nome, duracaoCampo)){

            //Movemos o arquivo!
            try {
                final File selecionada = new File(pathImg);
                final File novaImagem = new File(dir, selecionada.getName());

                moveFile(selecionada, novaImagem);

                int duracao = Integer.parseInt(duracaoCampo);
                Propaganda propaganda = new Propaganda(nome,duracao,novaImagem.toURI().toString());

                retorno = propagandaController.gravar(propaganda);

                dialogContinuar();
            } catch (IOException e) {
                System.out.println("Ocorreu um erro ao GRAVA. Retorno: " +retorno+ "Erro: "+e);

            }
        }else{
            Toast.makeText(getApplicationContext(), "Preencha todos os campos!", Toast.LENGTH_LONG).show();
        }
    }

    private void atualizarPropaganda(){
        String nome = txtNome.getText().toString();
        String duracaoCampo = txtDuracao.getText().toString();

        String retorno="";
        String URI=propaganda.getImagemURI();

        if(dadosValidos(nome, duracaoCampo)){

            //Movemos o arquivo!
            try {
                if(pathImg != null){
                    final File selecionada = new File(pathImg);
                    final File novaImagem = new File(dir, selecionada.getName());

                    moveFile(selecionada, novaImagem);
                    URI = selecionada.toURI().toString();
                }

                propaganda.setNome(nome);
                propaganda.setDuracao(Integer.parseInt(duracaoCampo));
                propaganda.setImagemURI(URI);

                retorno = propagandaController.alterar(propaganda);

                finish();
            } catch (IOException e) {
                System.out.println("Ocorreu um erro ao ATUALIZAR. Retorno: " +retorno+ "Erro: "+e);

            }
        }else{
            Toast.makeText(getApplicationContext(), "Preencha todos os campos!", Toast.LENGTH_LONG).show();
        }
    }

    public boolean dadosValidos(String nome, String duracao){
        if(nome.isEmpty() || duracao.isEmpty() || nome.equals("") || duracao.equals("")){
            return false;
        }else{
            return true;
        }
    }

    public void limparTela(){
        txtNome.setText("");
        txtDuracao.setText("");
        imgPropaganda.setImageResource(0);
        pathImg = null;
    }

    public void carregarDadosPropaganda(Propaganda propaganda){

        try {
            txtNome.setText(propaganda.getNome());
            txtDuracao.setText(String.valueOf(propaganda.getDuracao()));
            imgPropaganda.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(propaganda.getImagemURI())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == IMAGEM_INTERNA){

            if(resultCode == RESULT_OK){
                imagemSelecionada = intent.getData(); // pegando a uri da imagem selecionada

                String[] colunas = {MediaStore.Images.Media.DATA}; // pegando caminho da coluna que possui a imagem no banco do android

                //String wholeID = DocumentsContract.getDocumentId(imagemSelecionada);

               // String id = wholeID.split(":")[1];

                String sel = MediaStore.Images.Media._ID + "=?";


                Cursor cursor = getContentResolver().query(imagemSelecionada,colunas, null,null,null);
                //Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,colunas,  sel, new String[]{ wholeID },null);
                cursor.moveToFirst(); //movendo para a primeira linha.

                int indexColuna = cursor.getColumnIndex(colunas[0]);
                pathImg = cursor.getString(indexColuna);

                cursor.close();// Liberando recurso;

                //Bitmap bitmap = BitmapFactory.decodeFile(pathImg);
                //imgPropaganda.setImageBitmap(bitmap);
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imagemSelecionada);

                    imgPropaganda.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void dialogPermissao( String message, final String[] permissions ){
        mMaterialDialog = new MaterialDialog(this)
                .setTitle("Permission")
                .setMessage( message )
                .setPositiveButton("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ActivityCompat.requestPermissions(cadastro_propaganda.this, permissions, IMAGEM_INTERNA );
                        mMaterialDialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                });
        mMaterialDialog.show();

        //Necessario adicionar a libary implementation 'me.drakeet.materialdialog:library:1.2.2' no gradle
    }

    private void dialogContinuar(){
        mMaterialDialog = new MaterialDialog(this)
                .setTitle("Continuar?")
                .setMessage("Deseja continuar cadastrando?")
                .setPositiveButton("Sim", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        limparTela();
                        mMaterialDialog.dismiss();
                    }
                })
                .setNegativeButton("Não", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        mMaterialDialog.dismiss();
                    }
                });
        mMaterialDialog.show();
    }

    private void dialogExcluir(){
        mMaterialDialog = new MaterialDialog(this)
                .setTitle("Excluir??")
                .setMessage("Deseja realmente excluir?")
                .setPositiveButton("Sim", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        propagandaController.deletar(propaganda);
                        final File imagem = new File(propaganda.getImagemURI());
                        boolean texto = imagem.delete();
                        System.out.println("FOI? " +texto);
                        if(!imagem.delete()){
                            //Toast.makeText(getApplicationContext(), "Não foi possível remover a imagem!", Toast.LENGTH_SHORT).show();
                        }
                        finish();
                        mMaterialDialog.dismiss();
                    }
                })
                .setNegativeButton("Não", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                });
        mMaterialDialog.show();
    }

    private void moveFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }
        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
        //Alertamos, caso não consiga remover
        /*if(!sourceFile.delete()){
            Toast.makeText(getApplicationContext(), "Não foi possível remover a imagem!", Toast.LENGTH_SHORT).show();
        }*/
    }
}
