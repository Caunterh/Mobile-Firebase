package com.example.appfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RecuperarConta extends AppCompatActivity {

    private EditText recuperarEmail;
    private AppCompatButton bt_recuperar;
    private ImageButton bt_back;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_conta);
        this.iniciarComponentes();
        this.voltarTelaLogin();
        this.coreRecuperarConta();
    }//OnCreate

    private void coreRecuperarConta(){
        bt_recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recuperarConta();
            }
        });
    }

    private void recuperarConta(){
            String email = recuperarEmail.getText().toString();
            auth=FirebaseAuth.getInstance();
            if(!email.isEmpty()){
                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getBaseContext(), "Enviado com Sucesso", Toast.LENGTH_SHORT).show();
                        }
                    } //Fim onComplete
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                            trataErro(e.toString());
                    }//Fim OnFailure
                });//Fim onFailureListener
            }//Fim if
            else{
                Toast.makeText(getBaseContext(), "insira o Email", Toast.LENGTH_SHORT).show();
            }
    }//fim RecuperarConta

    public void trataErro(String erro){
        if (erro.contains("address is badly")){
            Toast.makeText(getBaseContext(), "Email Invalido!", Toast.LENGTH_SHORT).show();
        }
        else if (erro.contains("there is no user")){
            Toast.makeText(getBaseContext(), "Email não Cadastrado!", Toast.LENGTH_SHORT).show();
        }
        else if (erro.contains("INVALID_EMAIl")){
            Toast.makeText(getBaseContext(), "Email Invalido!", Toast.LENGTH_SHORT).show();
        }

        else{
            Toast.makeText(getBaseContext(), erro, Toast.LENGTH_SHORT).show();
        }
    }//FimTrataErro

    private void iniciarComponentes(){
      recuperarEmail = findViewById(R.id.editTextEmailRecuperarConta);
      bt_recuperar = findViewById(R.id.bt_recuperar);
      bt_back = findViewById(R.id.BackButtonRecuperarConta);
    }

    private void voltarTelaLogin(){
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( RecuperarConta.this, FormLogin.class);
                startActivity(intent);
                finish();
            }
        });
    }

}