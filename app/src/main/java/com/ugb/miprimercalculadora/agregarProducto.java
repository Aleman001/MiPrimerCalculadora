package com.ugb.miprimercalculadora;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class agregarProducto extends AppCompatActivity {
    FloatingActionButton btnAtras;
    ImageView imgFotoProducto;
    Intent tomarFotoIntent;
    String urlPhoto, idProdcuto, accion="nuevo";
    Button btn;
    DB miDB;
    TextView tempVal;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_productos);

        miDB = new DB (getApplicationContext(),"",null,1);

        btnAtras = findViewById(R.id.btnAtras);
        btnAtras.setOnClickListener(v -> {
            mostrarVistaPrincipal();
        });

        imgFotoProducto = findViewById(R.id.imgFotoProducto);
        imgFotoProducto.setOnClickListener(v -> {
            tomarFotoProducto();
        });

        btn = findViewById(R.id.btnGuardarProducto);
        btn.setOnClickListener(v->{
            agregarProducto();
            mostrarVistaPrincipal();

        });
        mostrarDatosProductos();
    }
    //agregar producto
    private void agregarProducto () {
        try {

            tempVal = findViewById(R.id.txtCodigo);
            String codigo = tempVal.getText().toString();

            tempVal = findViewById(R.id.txtNombreProducto);
            String producto = tempVal.getText().toString();

            tempVal = findViewById(R.id.txtMarca);
            String marca = tempVal.getText().toString();

            tempVal = findViewById(R.id.txtDescripcion);
            String descripcion = tempVal.getText().toString();

            tempVal = findViewById(R.id.txtPresentacion);
            String presentacion = tempVal.getText().toString();

            tempVal = findViewById(R.id.txtPrecio);
            String precio = tempVal.getText().toString();

            String [] datos = {idProdcuto,codigo,producto,marca,descripcion,presentacion,precio,urlPhoto};
            miDB.admin_productos(accion,datos);
            mostrarMsgToast("Producto guardado con exito");
            mostrarVistaPrincipal();
            mostrarDatosProductos();


        }catch (Exception e){
            mostrarMsgToast(e.getMessage() + "aqui hay error");
        }

    }
    //Tomar foto producto

    private void tomarFotoProducto(){
        tomarFotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (tomarFotoIntent.resolveActivity(getPackageManager()) != null){
            File photoProducto = null;
            try {
                photoProducto = crearImagenProducto();
            }catch (Exception e){
                mostrarMsgToast(e.getMessage());
            }

            if ( photoProducto !=null){
                try {
                    Uri uriPhotoProducto = FileProvider.getUriForFile(agregarProducto.this, "com.ugb.miprimercalculadora.fileprovider", photoProducto);
                    tomarFotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriPhotoProducto);
                    startActivityForResult(tomarFotoIntent, 1);

                }catch ( Exception e){
                    mostrarMsgToast(e.getMessage());
                }

            }else {
                mostrarMsgToast("No fue posible tomar la foto");

            }
        }
    }
    /*this.idProducto = idProducto;
   this.codigo = codigo;
   this.producto = producto;
   this.marca = marca;
   this.descripcion = descripcion;
   this.presentacion = presentacion;
   this.precio = precio;
   this.urlImg = urlImg*/
    //Mostras datos productos
    private void mostrarDatosProductos() {
        try {
            Bundle parametros= getIntent().getExtras();
            accion = parametros.getString("accion");
            if (accion.equals("modificar")){
                String[] datos = parametros.getStringArray("datos");
                idProdcuto = datos[0];
                tempVal = findViewById(R.id.txtCodigo);
                tempVal.setText(datos[1]);
                tempVal = findViewById(R.id.txtNombreProducto);
                tempVal.setText(datos[2]);
                tempVal = findViewById(R.id.txtMarca);
                tempVal.setText(datos[3]);
                tempVal = findViewById(R.id.txtDescripcion);
                tempVal.setText(datos[4]);
                tempVal = findViewById(R.id.txtPresentacion);
                tempVal.setText(datos[5]);
                tempVal = findViewById(R.id.txtPrecio);
                tempVal.setText(datos[6]);

                urlPhoto = datos[7];
                Bitmap img = BitmapFactory.decodeFile(urlPhoto);
                imgFotoProducto.setImageBitmap(img);
            }

        }catch (Exception e){
            mostrarMsgToast(e.getMessage());

        }

    }

    //Mostrar principal
    private void mostrarVistaPrincipal(){
        Intent iprincipal = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(iprincipal);
    }

    //Mensaje Toast
    private void mostrarMsgToast(String msg){
        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
    }


    //Crear Producto

    private File crearImagenProducto () throws Exception{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String nombreimagen = "imagen_" + timeStamp + "_";
        File dirAlmacenamiento = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        if ( dirAlmacenamiento.exists()==false){
            dirAlmacenamiento.mkdirs();
        }
        File image = File.createTempFile(nombreimagen,".jpg",dirAlmacenamiento);
        urlPhoto = image.getAbsolutePath();
        return image;
    }




    //Activity Result
    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode==1 && resultCode==RESULT_OK){
                Bitmap imagenBitmap = BitmapFactory.decodeFile(urlPhoto);
                imgFotoProducto.setImageBitmap(imagenBitmap);
            }

        }catch (Exception e){
            mostrarMsgToast(e.getMessage() + "Aca hay error xd xd xd");
        }
    }
}
