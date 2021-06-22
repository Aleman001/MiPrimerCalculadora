package com.ugb.miprimercalculadora;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DB miDB;
    Cursor datosAutosCursos = null;
    FloatingActionButton btn;
    ListView ltsAutos;
    ArrayList<automoviles> autosArrayList = new ArrayList<automoviles>();
    ArrayList<automoviles> autosArrayListCopy = new ArrayList<automoviles>();
    automoviles miAuto;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btn = findViewById(R.id.btnAgregarAutos);
        try {
            comprobardatos();
        }catch (Exception e){
            mostrarMsgToast(e.getMessage());
        }

        btn.setOnClickListener(v-> {
            agregarProductos("nuevo", new String[]{});
        } );
        buscarProducto();

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater mymenu =getMenuInflater();
        mymenu.inflate(R.menu.menu,menu);
        AdapterView.AdapterContextMenuInfo mymenuInfo = (AdapterView.AdapterContextMenuInfo)menuInfo;
        datosAutosCursos.moveToPosition(mymenuInfo.position);
        menu.setHeaderTitle(datosAutosCursos.getString(1));
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        try {
            switch (item.getItemId()){
                case R.id.mnxagregar:
                    agregarProductos("nuevo", new String[]{});
                    break;

                case R.id.mnxmodificar:
                    String [] datos ={
                            datosAutosCursos.getString(0),
                            datosAutosCursos.getString(1),
                            datosAutosCursos.getString(2),
                            datosAutosCursos.getString(3),
                            datosAutosCursos.getString(4),
                            datosAutosCursos.getString(5),
                            datosAutosCursos.getString(6),

                    };
                    agregarProductos("modificar",datos);
                    break;

                case R.id.mnxeliminar:
                    eliminarProdcuto();
                    break;

            }
        }catch (Exception e){
            mostrarMsgToast(e.getMessage());
        }

        return super.onContextItemSelected(item);
    }

    //Mostrar mensje
    private void mostrarMsgToast(String msg){
        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
    }

    //Eliminar producto
    private void eliminarProdcuto(){
        try {
            AlertDialog.Builder confirmacion = new AlertDialog.Builder(MainActivity.this);
            confirmacion.setTitle("¿Seguro desea eliminar?");
            confirmacion.setMessage(datosAutosCursos.getString(1));
            confirmacion.setPositiveButton("SI", ((dialog, which) -> {
                miDB = new DB (getApplicationContext(), "", null, 1);
                datosAutosCursos =miDB.admin_autos("eliminar", new String[]{datosAutosCursos.getString(0)});
                comprobardatos();
                mostrarMsgToast("Eliminado correcto");
                dialog.dismiss();
            }));
            confirmacion.setNegativeButton("NO", (dialog, which) -> {
                mostrarMsgToast("Se cancelo eliminar");
                dialog.dismiss();
            });
            confirmacion.create().show();

        }catch (Exception e){
            mostrarMsgToast(e.getMessage());

        }
    }
    //Buscar producto
    public void buscarProducto(){
        TextView temp = findViewById(R.id.txtBuscarAutos);
        temp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                autosArrayList.clear();
                if (temp.getText().toString().length()<1){
                    autosArrayList.addAll(autosArrayListCopy);
                }else{
                    for (automoviles PB: autosArrayListCopy){
                        String marca = PB.getMarca();
                        String modelo = PB.getModelo();
                        String anio = PB.getAnio();
                        String numeroMotor = PB.getNumeroMotor();
                        String numeroChasis = PB.getNumeroChasis();
                        String buscando = temp.getText().toString().trim().toLowerCase();
                        if (marca.toLowerCase().contains(buscando) ||
                                modelo.toLowerCase().contains(buscando)   ||
                                anio.toLowerCase().contains(buscando)   ||
                                numeroMotor.toLowerCase().contains(buscando)    ||
                                numeroChasis.toLowerCase().contains(buscando)){
                            autosArrayList.add(PB);
                        }
                    }
                }
                adaptadorImagenes autoEncontrado = new adaptadorImagenes(getApplicationContext(), autosArrayList);
                ltsAutos.setAdapter(autoEncontrado);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //metodo agregar producto

    private void agregarProductos(String accion, String [] datos ){
        try {
            Bundle parametroProducto = new Bundle();
            parametroProducto.putString("accion",accion);
            parametroProducto.putStringArray("datos",datos);
            Intent nuevoProducto = new Intent(getApplicationContext(), agregarProducto.class);
            nuevoProducto.putExtras(parametroProducto);
            startActivity(nuevoProducto);
        }catch (Exception e){
            mostrarMsgToast(e.getMessage());
        }
    }

    //Comprobar datos

    private void comprobardatos() {
        miDB = new DB(getApplicationContext(),"",null,1);
        datosAutosCursos = miDB.admin_autos("consultar",null);

        if( datosAutosCursos.moveToFirst() ){
            mostrarProductos();


        } else {
            mostrarMsgToast("Porfavor agregar datos");
            agregarProductos("nuevo", new String[]{});
        }
    }
    //mostrar productos
    private void mostrarProductos() {
        ltsAutos = findViewById(R.id.ltsautos);
        autosArrayList.clear();
        autosArrayListCopy.clear();

        do{
            miAuto = new automoviles(
                    datosAutosCursos.getString(0),//idauto
                    datosAutosCursos.getString(1),//marca
                    datosAutosCursos.getString(2),//modelo
                    datosAutosCursos.getString(3),//año
                    datosAutosCursos.getString(4),//numero de motor
                    datosAutosCursos.getString(5),//numero de Chasis
                    datosAutosCursos.getString(6) //urldefoto
            );
            autosArrayList.add(miAuto);
        }while(datosAutosCursos.moveToNext());

        try {
            adaptadorImagenes adaptadorImagenes = new adaptadorImagenes(getApplicationContext(), autosArrayList);
            ltsAutos.setAdapter(adaptadorImagenes);

            registerForContextMenu(ltsAutos);
            autosArrayListCopy.addAll(autosArrayList);

        }catch (Exception e){
            mostrarMsgToast(e.getMessage());
        }

    };
}




class automoviles{
    String idAuto;
    String marca;
    String modelo;
    String anio;
    String numeroMotor;
    String numeroChasis;
    String urlPhoto;

    public automoviles(String idAuto, String marca, String modelo, String anio, String numeroMotor, String numeroChasis, String urlPhoto) {
        this.idAuto = idAuto;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.numeroMotor = numeroMotor;
        this.numeroChasis = numeroChasis;
        this.urlPhoto = urlPhoto;
    }

    public String getIdAuto() {
        return idAuto;
    }

    public void setIdAuto(String idAuto) {
        this.idAuto = idAuto;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getNumeroMotor() {
        return numeroMotor;
    }

    public void setNumeroMotor(String numeroMotor) {
        this.numeroMotor = numeroMotor;
    }

    public String getNumeroChasis() {
        return numeroChasis;
    }

    public void setNumeroChasis(String numeroChasis) {
        this.numeroChasis = numeroChasis;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }
}

