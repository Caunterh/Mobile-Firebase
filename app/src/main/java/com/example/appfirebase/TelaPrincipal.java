package com.example.appfirebase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class TelaPrincipal extends AppCompatActivity {
    private Button bt_deslogar;
    private TextView nomeUsuario, emailUsuario;
    protected String usuarioId;
    protected FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);
        deslogar();
        this.iniciarComponentes();
    }

    protected void onStart(){
        super.onStart();
        usuarioId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        String email =FirebaseAuth.getInstance().getCurrentUser().getEmail();
        DocumentReference obj= db.collection("Usuarios").document(usuarioId);
        obj.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value !=null){
                    nomeUsuario.setText(value.getString("nome"));
                    emailUsuario.setText(email);
                }
            }
        });
    }

    private void iniciarComponentes(){

        nomeUsuario=findViewById(R.id.textViewNomeUsuarioPrincipal);
        emailUsuario=findViewById(R.id.textViewEmailUsuarioPrincipal);
        db=FirebaseFirestore.getInstance();

    }

    private void deslogar(){
         bt_deslogar=findViewById(R.id.buttonDeslogar);
         bt_deslogar.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 FirebaseAuth.getInstance().signOut();
                 Intent objIntent = new Intent(TelaPrincipal.this, FormLogin.class);
                 startActivity(objIntent);
                 finish();
             }
         });
    }

}