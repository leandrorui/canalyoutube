package maddo.foo.youtubelogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    String urlWebServicesDesenvolvimento = "http://192.168.0.11/youtube/getUsuarios.php";
    String urlWebServicesProducao = "http://www.seusite.com.br/pastanosite/getUsuarios.php";

    StringRequest stringRequest;
    RequestQueue requestQueue;

    Button btnEntrar;
    EditText editLogin;
    EditText editSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        requestQueue = Volley.newRequestQueue(this);

        btnEntrar = findViewById(R.id.btnEntrar);
        editLogin = findViewById(R.id.editLogin);
        editSenha = findViewById(R.id.editSenha);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean validado = true;

                if(editLogin.getText().length()==0){
                    editLogin.setError("Campo Login Obrigatório");
                    editLogin.requestFocus();
                    validado = false;
                }

                if(editSenha.getText().length()==0){
                    editSenha.setError("Campo Senha Obrigatório");
                    editSenha.requestFocus();
                    validado = false;
                }

                if(validado){

                    Toast.makeText(getApplicationContext(),"Validando seus dados... espere...", Toast.LENGTH_SHORT).show();

                    validarLogin();

                }

            }
        });

    }


    private void validarLogin() {

        stringRequest = new StringRequest(Request.Method.POST,
                                          urlWebServicesDesenvolvimento,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("LogLogin", response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            boolean isErro = jsonObject.getBoolean("erro");

                            if(isErro){

                                Toast.makeText(getApplicationContext(),
                                        jsonObject.getString("mensagem"),
                                        Toast.LENGTH_LONG).show();
                            }else{

                                int perfil = jsonObject.getInt("perfil");

                                if(perfil==1){

                                    Intent novaTela = new Intent(LoginActivity.this,
                                            PainelAdministrativoActivity.class);
                                    startActivity(novaTela);
                                    finish();

                                }else if(perfil==2){

                                    Intent novaTela = new Intent(LoginActivity.this,
                                            MainActivity.class);
                                    startActivity(novaTela);
                                    finish();

                                }

                            }

                        }catch (Exception e){

                            Log.v("LogLogin", e.getMessage());

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LogLogin", error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("login",editLogin.getText().toString());
                params.put("senha",editSenha.getText().toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }

}
