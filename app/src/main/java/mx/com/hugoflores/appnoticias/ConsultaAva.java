package mx.com.hugoflores.appnoticias;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ConsultaAva extends AppCompatActivity implements View.OnClickListener{

    String [] Ciudades={"Seleccione Municipo","Mixquiahula", "Progreso" , "Acayuclan"};
    Spinner sp_ciudades,sp_marca;
    String[] marcas;
    Button btConsulta;
    EditText editText, fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_ava);

        sp_ciudades = (Spinner) findViewById(R.id.sp_ciudades);
        editText = (EditText) findViewById(R.id.etTitulobusca);
        sp_marca = (Spinner) findViewById(R.id.sp_marca);
        marcas= getResources().getStringArray(R.array.marcas_auto);
        fecha = (EditText) findViewById(R.id.etFechaCon);
        btConsulta = (Button) findViewById(R.id.buttonC);
        btConsulta.setOnClickListener(this);
        CargaCiudades();
        //CargaMarcas();
//        RegistraAdaptadores();
    }

    public void CargaCiudades(){
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,Ciudades);
        sp_ciudades.setAdapter(adaptador);
    }



    public void RegistraAdaptadores(){
        sp_ciudades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent , View view, int position, long id){
                Toast.makeText(getApplicationContext(),Ciudades[position],Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){
                Toast.makeText(getApplicationContext(),"",Toast.LENGTH_SHORT).show();
            }
        });

        sp_marca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent , View view, int position, long id){
                Toast.makeText(getApplicationContext(),marcas[position],Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){
                Toast.makeText(getApplicationContext(),"",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void CargaMarcas(){
        ArrayAdapter<CharSequence> adaptador = ArrayAdapter.createFromResource(this,R.array.marcas_auto, android.R.layout.simple_spinner_item);
        sp_marca.setAdapter(adaptador);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonC:
                String titulo = editText.getText().toString();
                String date = fecha.getText().toString();
                String seleccion = sp_ciudades.getSelectedItem().toString();
                if (!sp_ciudades.getSelectedItem().toString().equals("Seleccione Municipo")&& !titulo.equals("")&& !date.equals("")){
                    Intent intent = new Intent(getApplicationContext(), showConsultaAva.class);
                    intent.putExtra("Municipio", seleccion);
                    intent.putExtra("Fecha", date);
                    intent.putExtra("Titulo", titulo);
                    startActivity(intent);
                }
                else if (!sp_ciudades.getSelectedItem().toString().equals("Seleccione Municipo")&& !titulo.equals("")){
                    Intent intent = new Intent(getApplicationContext(), showConsultaAva.class);
                    intent.putExtra("Municipio", seleccion);
                    intent.putExtra("Titulo", titulo);
                    startActivity(intent);
                }
                else if (!date.equals("")&& !titulo.equals("")){
                    Intent intent = new Intent(getApplicationContext(), showConsultaAva.class);
                    intent.putExtra("Municipio", seleccion);
                    intent.putExtra("Titulo", titulo);
                    startActivity(intent);
                }

                else if (!sp_ciudades.getSelectedItem().toString().equals("Seleccione Municipo")){
                    Intent intent = new Intent(getApplicationContext(), showConsultaAva.class);
                    intent.putExtra("Municipio", seleccion);
                    startActivity(intent);
                }
                else if (!titulo.equals("")){
                    Intent intent = new Intent(getApplicationContext(), showConsultaAva.class);
                    intent.putExtra("Titulo", titulo);
                    startActivity(intent);
                }
                else if (!date.equals("")){
                    Intent intent = new Intent(getApplicationContext(), showConsultaAva.class);
                    intent.putExtra("Fecha", date);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Introdusca un tipo de busqueda", Toast.LENGTH_SHORT).show();
                }



//                Toast.makeText(getApplicationContext(),seleccion,Toast.LENGTH_SHORT).show();
                /*Intent intent = new Intent(getApplicationContext(), showConsultaAva.class);
                intent.putExtra("Medio", seleccion);
                startActivity(intent);*/
                break;
            default:
                break;

        }
    }
}
