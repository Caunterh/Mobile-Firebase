package com.example.appfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FormCadastro extends AppCompatActivity {

    private ImageButton imageButtonVoltar;
    private EditText edit_nome, edit_email, edit_senha, edit_Senha2;
    private String usuarioId;
    private AppCompatButton bt_cadastrar;
    private String[]msg={"Preencha todos os dados!","Cadastro Realizado","Senhas não coincidem"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);
        this.iniciarComponentes();
        this.voltarLogin();
        this.gerenciarCadastro();
    }

    private void voltarLogin(){
        this.imageButtonVoltar=findViewById(R.id.BackButtonCadastro);
        imageButtonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( FormCadastro.this, FormLogin.class);
                startActivity(intent);
            }
        });
    }

    private void iniciarComponentes(){
        edit_nome=findViewById(R.id.EditTextNomeCadastro);
        edit_email=findViewById(R.id.EditTextEmailCadastro);
        edit_senha=findViewById(R.id.EditTextSenhaCadastro);
        edit_Senha2=findViewById(R.id.EditTextSenha2Cadastro);
        bt_cadastrar=findViewById(R.id.bt_Cadastrar);

    }

    private void gerenciarCadastro(){
        bt_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = edit_nome.getText().toString();
                String email = edit_email.getText().toString();
                String senha = edit_senha.getText().toString();
                String senha2 = edit_Senha2.getText().toString();
                if(nome.isEmpty() || email.isEmpty() || senha.isEmpty() || senha2.isEmpty()){
                    Snackbar objSnackbar = Snackbar.make(view, msg[0], Snackbar.LENGTH_SHORT);
                    objSnackbar.setBackgroundTint(Color.WHITE);
                    objSnackbar.setTextColor(Color.BLACK);
                    objSnackbar.show();
                }
                else if(!senha.equals(senha2)){
                    Snackbar objSnackbar = Snackbar.make(view, msg[2], Snackbar.LENGTH_SHORT);
                    objSnackbar.setBackgroundTint(Color.WHITE);
                    objSnackbar.setTextColor(Color.BLACK);
                    objSnackbar.show();

                }
                else{
                    cadastrarUser(view, email, senha);
                }
            }
        });
    }//FIM gerenciarCadastro

    private void cadastrarUser(View view, String email, String senha){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            salvarDadosUsuario();
                            Snackbar objSnackbar = Snackbar.make(view, msg[1], Snackbar.LENGTH_SHORT);
                            objSnackbar.setBackgroundTint(Color.WHITE);
                            objSnackbar.setTextColor(Color.BLACK);
                            objSnackbar.show();
                            new Handler().postDelayed(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent objIntent = new Intent(FormCadastro.this, FormLogin.class);
                                            startActivity(objIntent);
                                            finish();
                                        }//fim run
                                    }
                            , 3000);
                        }//FIM if
                        else{
                                String erro="";
                                try {
                                    throw task.getException();
                                }
                                catch(FirebaseAuthWeakPasswordException e){
                                    erro="Digite uma senha com, no minimo, 6 caracteres";
                                }//FimCatch Senha
                                catch(FirebaseAuthUserCollisionException e){
                                    erro="já existe Conta vinculada ao email";
                                }//fimCatch User
                                catch(FirebaseAuthInvalidCredentialsException e){
                                    erro="Esse Email Invalido";
                                }//fimCatch
                                catch(Exception e){
                                    erro="Erro ao cadastrar";
                                }
                                Snackbar objSnackbar = Snackbar.make(view, erro, Snackbar.LENGTH_SHORT);
                                objSnackbar.setBackgroundTint(Color.WHITE);
                                objSnackbar.setTextColor(Color.BLACK);
                                objSnackbar.show();
                        }//Fim else
                    }//Fim onComplete
                }//Fim onCompleteListener
        );
    }//Fim cadastrarUser

    private void salvarDadosUsuario(){
            String nome = edit_nome.getText().toString();
            FirebaseFirestore db=FirebaseFirestore.getInstance();
            Map<String, Object> usuario= new HashMap<>();
            usuario.put("nome", nome);
            usuarioId=FirebaseAuth.getInstance().getCurrentUser().getUid();
            DocumentReference documentReference = db.collection("Usuarios").document(usuarioId);
            documentReference.set(usuario).addOnSuccessListener(
                    new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("d","Sucesso ao Salvar dados");
                        }
                    }).addOnFailureListener(
                    new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("db_erro","Erro ao Salvar dados" + e.getMessage());
                        }
                    }
            );
    }//fimSalvarDadosUsuario
}