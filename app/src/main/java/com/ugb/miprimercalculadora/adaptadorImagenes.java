package com.ugb.miprimercalculadora;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class adaptadorImagenes extends BaseAdapter {
    Context context;
    ArrayList<automoviles> datosAutosArrayList;
    LayoutInflater layoutInflater;
    automoviles misAutos;

    public adaptadorImagenes(Context context, ArrayList<automoviles> datosAutosArrayList) {
        this.context = context;
        this.datosAutosArrayList = datosAutosArrayList;
    }

    @Override
    public int getCount() {
        return datosAutosArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return datosAutosArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(datosAutosArrayList.get(position).getIdAuto());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View Visor = layoutInflater.inflate(R.layout.listview_imagenes,parent,false);
        TextView temp = Visor.findViewById(R.id.lblMarca);
        ImageView img = Visor.findViewById(R.id.imgShow);

        try {
            misAutos = datosAutosArrayList.get(position);
            temp.setText(misAutos.getMarca());

            temp = Visor.findViewById(R.id.lblNumeroMotor);
            temp.setText("Motor numero: " + misAutos.getNumeroMotor());

            temp = Visor.findViewById(R.id.lblNumeroChasis);
            temp.setText("Chasis numero" + misAutos.getNumeroChasis());

            temp = Visor.findViewById(R.id.lblModelo);
            temp.setText("Modelo: " + misAutos.getModelo());

            temp = Visor.findViewById(R.id.lblaño);
            temp.setText("Año: " + misAutos.getAnio());

            Bitmap photo = BitmapFactory.decodeFile(misAutos.getUrlPhoto());
            img.setImageBitmap(photo);


        }catch (Exception e){
            mensajeToast(e.getMessage());
        }

        return Visor;
    }
    private void mensajeToast(String msg){
        Toast.makeText(context.getApplicationContext(),msg, Toast.LENGTH_LONG).show();
    }
}

