package com.example.appfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FormLogin extends AppCompatActivity {
    private TextView textTelaCadastro, textTelaRecuperarConta;

    private EditText edit_email, edit_senha;
    private AppCompatButton bt_logar;
    private ProgressBar progressBar;
    private String[]msg={"Preencha todos os dados!","login Realizado"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);
        this.iniciarComponentes();
        this.chamaTelaCadastro();
        this.chamarTelaRecuperarConta();
        this.login();//Autenticar Usuario
    }

    protected void onStart(){
        super.onStart();
        FirebaseUser usuarioCorrente=FirebaseAuth.getInstance().getCurrentUser();
        if(usuarioCorrente !=null){
            chamarTelaPrincipal();
        }//Fim if
    }

    //método para logar
    public void login(){
        bt_logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edit_email.getText().toString();
                String senha = edit_senha.getText().toString();
                if (email.isEmpty() || senha.isEmpty()) {
                    Snackbar objSnackbar = Snackbar.make(view, msg[0], Snackbar.LENGTH_SHORT);
                    objSnackbar.setBackgroundTint(Color.WHITE);
                    objSnackbar.setTextColor(Color.BLACK);
                    objSnackbar.show();
                } else {
                    autenticarUsuario(view, email, senha);
                }
            }//Fim OnClick
        });
    }//Fim Login

    public void autenticarUsuario(View view, String email, String senha){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressBar.setVisibility(View.VISIBLE);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    chamarTelaPrincipal();
                                }
                            }, 3000);
                        }//Fim if
                        else{
                            String erro="Erro na Autenticação!";
                            try {
                                throw task.getException();
                            }
                            catch (Exception e){
                                Snackbar objSnackbar = Snackbar.make(view, erro, Snackbar.LENGTH_SHORT);
                                objSnackbar.setBackgroundTint(Color.WHITE);
                                objSnackbar.setTextColor(Color.BLACK);
                                objSnackbar.show();
                            }
                        }//Fim else
                    }//Fim onComplete
                }//Fim OnCompleteListerner
        );//Fim addOnCompleteListener
    }//fim autenticar Usuario

    public void chamarTelaPrincipal(){
        Intent intent = new Intent( FormLogin.this, TelaPrincipal.class);
        startActivity(intent);
        finish();
    }

    public void chamarTelaRecuperarConta(){
        textTelaRecuperarConta = findViewById(R.id.TextViewRecuperarLogin);
        textTelaRecuperarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( FormLogin.this, RecuperarConta.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void chamaTelaCadastro(){
        textTelaCadastro = findViewById(R.id.TextViewCadastrarLogin);
        textTelaCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( FormLogin.this, FormCadastro.class);
                startActivity(intent);
                finish();
            }
        });
    }//Fim TelaCadastro
        private void iniciarComponentes(){
            edit_email=findViewById(R.id.editTextEmailLogin);
            edit_senha=findViewById(R.id.editTextSenhaLogin);

            bt_logar=findViewById(R.id.bt_logar);

            progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.INVISIBLE);


    }//Fim Iniciar Componentes
}//Fim Classe