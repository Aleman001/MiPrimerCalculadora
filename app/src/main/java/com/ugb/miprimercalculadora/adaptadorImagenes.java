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

import java.util.ArrayList;

public class adaptadorImagenes extends BaseAdapter {
    Context context;
    ArrayList<productos> datosProductosArrayList;
    LayoutInflater layoutInflater;
    productos misProductos;

    public adaptadorImagenes(Context context, ArrayList<productos> datosProductosArrayList) {
        this.context = context;
        this.datosProductosArrayList = datosProductosArrayList;
    }

    @Override
    public int getCount() {
        return datosProductosArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return datosProductosArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(datosProductosArrayList.get(position).getIdProducto());
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.listview_imagenes, parent, false);
        TextView tempVal = itemView.findViewById(R.id.lblTitulo);
        ImageView imgViewView = itemView.findViewById(R.id.imgPhoto);
        try{
            misProductos = datosProductosArrayList.get(position);
            tempVal.setText(misProductos.getProducto());

            tempVal = itemView.findViewById(R.id.lblcodigo);
            tempVal.setText(misProductos.getCodigo());

            tempVal = itemView.findViewById(R.id.lblpresentacion);
            tempVal.setText(misProductos.getMarca());

            Bitmap imagenBitmap = BitmapFactory.decodeFile(misProductos.getUrlImg());
            imgViewView.setImageBitmap(imagenBitmap);

        }catch (Exception e){
        }
        return itemView;
    }



}
