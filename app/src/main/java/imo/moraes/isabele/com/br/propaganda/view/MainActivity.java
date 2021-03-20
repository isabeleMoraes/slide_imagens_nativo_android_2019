package imo.moraes.isabele.com.br.propaganda.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

import imo.moraes.isabele.com.br.propaganda.R;
import imo.moraes.isabele.com.br.propaganda.model.BancoController;
import imo.moraes.isabele.com.br.propaganda.model.ListaPropagandas;
import imo.moraes.isabele.com.br.propaganda.model.Propaganda;
import imo.moraes.isabele.com.br.propaganda.model.PropagandaController;
import imo.moraes.isabele.com.br.propaganda.model.RecyclerViewOnClickListenerHack;
import me.drakeet.materialdialog.MaterialDialog;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RecyclerViewOnClickListenerHack{

    private RecyclerView recyclerView;
    private ListaPropagandas listaPropagandas;
    private MaterialDialog mMaterialDialog;
    private BancoController bancoController;

    private AdapterPropagandas adapterPropagandas;
    private PropagandaController propagandaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        propagandaController = new PropagandaController(getBaseContext());
        listaPropagandas = new ListaPropagandas(propagandaController.buscarTodos());

        adapterPropagandas = new AdapterPropagandas(listaPropagandas.getListaPropagandas(),getBaseContext());

        recyclerView = findViewById(R.id.recycle_view_propagandas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        bancoController = new BancoController(this);
    }

    @Override
    public void onClickListener(View view, int position) {
        adapterPropagandas = (AdapterPropagandas) recyclerView.getAdapter();
        Propaganda propaganda = adapterPropagandas.getPropaganda(position);
        Intent intent = new Intent(this, cadastro_propaganda.class);

        Log.d("CLICK: " , "Acionou o metodo de click");

        Bundle parametros = new Bundle();
        parametros.putParcelable("propaganda", propaganda);
        intent.putExtras(parametros);
        startActivity(intent);
    }

    private void carregarLista(){
        listaPropagandas.setListaPropagandas(propagandaController.buscarTodos());
        adapterPropagandas = new AdapterPropagandas(listaPropagandas.getListaPropagandas(),getBaseContext());
        adapterPropagandas.setmRecyclerViewOnClickListenerHack(this);
        recyclerView.setAdapter(adapterPropagandas);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarLista();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent it = new Intent();
        BancoController bancoController = new BancoController(this);

        if (id == R.id.nav_cadastro) {
            it = new Intent(this, cadastro_propaganda.class);
            startActivity(it);
        } else if (id == R.id.nav_galeria) {
            exibirPropagandas(it);
        } else if (id == R.id.nav_importar) {
            importarBanco(bancoController);
        } else if (id == R.id.nav_exportar) {
            exportarBanco();
        } else if (id == R.id.nav_exportar) {
            it = new Intent(this, sobre.class);
            startActivity(it);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void exibirPropagandas(Intent it){
        if(!listaPropagandas.getListaPropagandas().isEmpty()){
            it = new Intent(this, activity_slide_2.class);

            Bundle parametros = new Bundle();
            parametros.putParcelable("listaPropagandas", listaPropagandas);
            parametros.putInt("indexInicial", 0);
            it.putExtras(parametros);
            startActivity(it);
        }else{
            Toast.makeText(this, "Não há propagandas cadastradas", Toast.LENGTH_LONG).show();
        }
    }

    private void importarBanco(BancoController bancoController){
        try {
            if(bancoController.importarBanco()){
                carregarLista();
                Toast.makeText(this, "Banco de dados importado com sucesso", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Não foi possivel importar o banco de dados. Motivo: " + e, Toast.LENGTH_LONG).show();
        }
    }

    private void exportarBanco(){
        try {
            if(bancoController.exportarBanco()){
                carregarLista();
                Toast.makeText(this, "Banco de dados exportado com sucesso", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(this, "Não foi possivel exportar o banco de dados. Motivo: " + e, Toast.LENGTH_LONG).show();
        }
    }

    private void dialogPermissao( String message, final String[] permissions ){
        mMaterialDialog = new MaterialDialog(this)
                .setTitle("Permission")
                .setMessage( message )
                .setPositiveButton("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ActivityCompat.requestPermissions(MainActivity.this, permissions, 1 );
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


}
