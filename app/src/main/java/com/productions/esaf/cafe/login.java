package com.productions.esaf.cafe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.productions.esaf.cafe.Model.Utilizador;
import com.productions.esaf.cafe.common.Common;

public class login extends AppCompatActivity {
    EditText edtPhone,edtPassword;
    Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Botoes e outros a usar
        edtPassword=findViewById(R.id.edtPassword);
        edtPhone=findViewById(R.id.edtPhone);
        btnSignIn=findViewById(R.id.btnSignIn);
        //Inicializar a Firebase
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference table_user=database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isConnectedToInternet(getBaseContext())) {

                    final ProgressDialog mDialog = new ProgressDialog(login.this);
                    mDialog.setMessage("Please wait....");
                    mDialog.show();

                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //Verificar se o utilizador existe na Firebase
                            if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                                //Get informação do utilizador
                                mDialog.dismiss();
                                Utilizador utilizador = dataSnapshot.child(edtPhone.getText().toString()).getValue(Utilizador.class);
                                //Verificar se utilizador entra
                                assert utilizador != null;
                                utilizador.setPhone(edtPhone.getText().toString());//ir buscar o numero de telemovel
                                if (utilizador.getPassword().equals(edtPassword.getText().toString())) {
                                    //Chamar o painel home do utilizador
                                    Intent homeIntent = new Intent(login.this, Home.class);
                                    Common.atualUtilizador = utilizador;
                                    startActivity(homeIntent);
                                    finish();
                                } else {
                                    Toast.makeText(login.this, "Wrong Password!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                mDialog.dismiss();
                                Toast.makeText(login.this, "Utilizador não existe na base de dados", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else
                {
                    Toast.makeText(login.this, "Please Check your internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
