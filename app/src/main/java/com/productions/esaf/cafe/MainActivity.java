package com.productions.esaf.cafe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.productions.esaf.cafe.Model.Utilizador;
import com.productions.esaf.cafe.common.Common;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {


    //Firebase
    FirebaseStorage storage;
    StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Firebase Init
        storage=FirebaseStorage.getInstance();
        mStorageRef=storage.getReference();

        //Init View
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnSignUp = findViewById(R.id.btnSignUp);
        //Paper
        Paper.init(this);


        //Botao Login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login=new Intent(MainActivity.this,login.class);
                startActivity(login);
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUp=new Intent(MainActivity.this,SignUp.class);
                startActivity(signUp);
            }
        });

        //Check Remember
        String user = Paper.book().read(Common.USER_KEY);
        String pwd = Paper.book().read(Common.PWD_KEY);
        if(user != null && pwd!= null)
        {
            if(!user.isEmpty()&& !pwd.isEmpty())
                SignIn(user,pwd);
        }

    }

    private void SignIn(final String phone, final String pwd) {
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference table_user=database.getReference("User");
            //Guardar dados utilizador checkbox

            final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
            mDialog.setMessage("Please wait....");
            mDialog.show();

            table_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Verificar se o utilizador existe na Firebase
                    if (dataSnapshot.child(phone).exists()) {
                        //Get informação do utilizador
                        mDialog.dismiss();
                        Utilizador utilizador = dataSnapshot.child(phone).getValue(Utilizador.class);
                        //Verificar se utilizador entra
                        assert utilizador != null;
                        utilizador.setPhone(phone);//ir buscar o numero de telemovel
                        if (utilizador.getPassword().equals(pwd)) {
                            //Chamar o painel home do utilizador
                            Intent homeIntent = new Intent(MainActivity.this, Home.class);
                            Common.atualUtilizador = utilizador;
                            startActivity(homeIntent);
                            finish();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Wrong Password!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        mDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Utilizador não existe na base de dados", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        Handle action bar item clicks here. The action bar will
        automatically handle clicks on the Home/Up button, so long
        as you specify a parent activity in AndroidManifest.xml.
        */
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
