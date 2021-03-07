package com.ugb.miprimercalculadora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TabHost tbhConversores;
    RelativeLayout contenidoView;
    conversores miConversor = new conversores();
    Button btnConvertir;
    TextView tempVal;
    double tempval;
    Spinner spnOpcionDe, spnOpcionA;
    //Invocaion de menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Toast.makeText(getApplicationContext(),"Index: "+ item, Toast.LENGTH_LONG).show();
        int idMenu = item.getItemId();
        switch (idMenu){
            case R.id.mnxConversor:
                tbhConversores.setCurrentTab(0);
                break;

            case R.id.mnxCuota:
                tbhConversores.setCurrentTab(1);
                break;


        }
        return super.onOptionsItemSelected(item);
    }
       //ella no te ama
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Tabhost

        contenidoView = findViewById(R.id.contenidoView);
        tbhConversores = findViewById(R.id.tbhConversores);
        tbhConversores.setup();

        tbhConversores.addTab(tbhConversores.newTabSpec("Coversor").setContent(R.id.tabConversor).setIndicator("", getResources().getDrawable(R.drawable.xdmonedas)));
        tbhConversores.addTab(tbhConversores.newTabSpec("Cuota").setContent(R.id.tabCouta).setIndicator("", getResources().getDrawable(R.drawable.xdlongitud)));

        btnConvertir = findViewById(R.id.btnCalcular);
        btnConvertir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    tempVal = (TextView) findViewById(R.id.txtcantidad);
                    double cantidad = Double.parseDouble(tempVal.getText().toString());
                    spnOpcionDe = findViewById(R.id.cboDe);
                    spnOpcionA = findViewById(R.id.cboA);
                    tempVal = findViewById(R.id.lblRespuesta);
                    tempVal.setText("Respuesta: " + miConversor.convertir(0, spnOpcionDe.getSelectedItemPosition(), spnOpcionA.getSelectedItemPosition(), cantidad));
                }catch (Exception e){
                    tempVal = findViewById(R.id.lblRespuesta);
                    tempVal.setText("Por favor ingrese los valores correspondiente");
                    Toast.makeText(getApplicationContext(), "Por ingrese los valores correspondiente "+ e.getMessage(),Toast.LENGTH_SHORT).show();
            /*Snackbar snackbar = Snackbar.make(contenidoView, "Por favor ingrese los valores correspondiente", Snackbar.LENGTH_LONG);
            snackbar.show();*/
                }
            }
        });

    }
    class conversores {
        double[][] conversor = {
                {1.0, 0.111111, 0.000000035870064, 144.0, 0.00000009290304, 0.000009, 0.092903, 929.0304, 92903.04, 0.000024},/*Area*/

        };
        public double convertir(int opcion, int de, int a, double cantidad){

            return conversor[opcion][a] / conversor[opcion][de] * cantidad;
        }
    }
//for Pe
    public  double Cuouta(double m3, double precio){

        if ( m3 >= 1 && m3 <= 18){
            precio = 6.00;
       }
        return precio;
    }
}

