package com.productions.esaf.cafe;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.productions.esaf.cafe.Model.Utilizador;
import com.productions.esaf.cafe.common.Common;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import io.paperdb.Paper;

public class login extends AppCompatActivity {
    EditText edtPhone,edtPassword;
    Button btnSignIn;
    CheckBox checkBoxRemember;
    TextView txtForgotpwd;

    FirebaseDatabase database;
    DatabaseReference table_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Botoes e outros a usar
        edtPassword=findViewById(R.id.edtPassword);
        edtPhone=findViewById(R.id.edtPhone);
        btnSignIn=findViewById(R.id.btnSignIn);
        checkBoxRemember = findViewById(R.id.checkbox_RememberMe);
        txtForgotpwd=findViewById(R.id.txtForgotPassword);
        //Paper
        Paper.init(this);
        //Inicializar a Firebase
        database=FirebaseDatabase.getInstance();
        table_user=database.getReference("User");

        txtForgotpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForgotPwdDialog();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isConnectedToInternet(getBaseContext())) {

                    //Guardar dados utilizador checkbox
                    if(checkBoxRemember.isChecked())
                    {
                        Paper.book().write(Common.USER_KEY,edtPhone.getText().toString());
                        Paper.book().write(Common.PWD_KEY,edtPassword.getText().toString());
                    }

                    final ProgressDialog mDialog = new ProgressDialog(login.this);
                    mDialog.setMessage("Please wait....");
                    mDialog.show();

                    table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
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

                                    table_user.removeEventListener(this);
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

    private void showForgotPwdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Forgot Password");
        builder.setMessage("Enter your secure code");

        LayoutInflater inflater = this.getLayoutInflater();
        View forgot_view = inflater.inflate(R.layout.forgot_password_layout,null);
        builder.setView(forgot_view);
        builder.setIcon(R.drawable.ic_security_black_24dp);

        final MaterialEditText edtPhone= (MaterialEditText) forgot_view.findViewById(R.id.edtPhone);
        final MaterialEditText edtSecureCode= (MaterialEditText) forgot_view.findViewById(R.id.edtSecureCode);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Check if user available
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Utilizador user = dataSnapshot.child(Objects.requireNonNull(edtPhone.getText()).toString())
                                .getValue(Utilizador.class);
                        if(Objects.requireNonNull(user).getSecureCode().equals(Objects.requireNonNull(edtSecureCode.getText()).toString()))
                            Toast.makeText(login.this, "Your password is: "+user.getPassword(),Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(login.this, "Wrong Secure Code", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
}
