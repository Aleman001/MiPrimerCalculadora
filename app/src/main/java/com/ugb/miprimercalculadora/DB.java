package com.ugb.miprimercalculadora;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DB extends SQLiteOpenHelper {
    Context miContext;
    static String nombreDB = "db_autos";
    static String tblAuto = "CREATE TABLE tblautos(idAuto integer primary key autoincrement, marca text, modelo text, anio text, numeroMotor text, numeroChasis text, urlPhoto text)";


            /*i.   Marca
            ii.   Modelo
            iii.   Año
            iv.   Numero de Motor
            v.   Numero de Chasis */

    public DB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, nombreDB, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tblAuto);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor admin_autos (String accion, String [] datos ) {
        Cursor datosCursor = null;
        SQLiteDatabase sqLiteDatabaseW = getWritableDatabase();
        SQLiteDatabase sqLiteDataBaseR =getReadableDatabase();
        switch (accion){
            case "consultar":
                datosCursor = sqLiteDataBaseR.rawQuery("select * from tblautos order by marca", null);
                break;

            case "nuevo":
                sqLiteDatabaseW.execSQL("INSERT INTO tblautos(marca, modelo, anio, numeroMotor, numeroChasis, urlPhoto) VALUES ('"+datos[1]+"', '"+datos[2]+"', '"+datos[3]+"', '"+datos[4]+"', " +
                        "'"+datos[5]+"', '"+datos[6]+"')");
                break;

            case "modificar":
                try{
                    sqLiteDatabaseW.execSQL("UPDATE tblautos SET marca ='"+datos[1]+"', modelo='"+datos[2]+"', anio = '"+datos[3]+"', numeroMotor = '"+datos[4]+"', " +
                            "numeroChasis = '"+datos[5]+"', urlPhoto = '"+datos[6]+"' WHERE idAuto = '"+datos[0]+"' ");

                }catch (Exception e){
                    Toast.makeText(miContext.getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();


                }

                break;

            case "eliminar":
                sqLiteDatabaseW.execSQL("DELETE FROM tblautos WHERE idAuto = '"+datos[0]+"'");
                break;

        }


        return datosCursor;



    }

}