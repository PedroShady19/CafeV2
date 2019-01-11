package com.productions.esaf.cafe;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.productions.esaf.cafe.Database.Database;
import com.productions.esaf.cafe.Model.Order;
import com.productions.esaf.cafe.Model.Request;
import com.productions.esaf.cafe.ViewHolder.CartAdapter;
import com.productions.esaf.cafe.common.Common;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    TextView txtTotalPrice;
    Button btnPlace;

    List<Order> cart= new ArrayList<>();

    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //Firebase
        database = FirebaseDatabase.getInstance();
        requests=database.getReference("Requests");
        //Init
        recyclerView =findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = findViewById(R.id.total);
        btnPlace= findViewById(R.id.btnPlaceOrder);

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cart.size() >0)
                    showAlertDialog();
                else
                {
                    Toast.makeText(Cart.this, "The cart is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loadListFood();
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog= new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("Apenas mais um passo!");
        alertDialog.setMessage("Coloque a sua morada: ");

        LayoutInflater inflater= this.getLayoutInflater();
        View order_adress_comment= inflater.inflate(R.layout.order_adress_comment,null);

        final MaterialEditText edtAdress = order_adress_comment.findViewById(R.id.edtAdress);
        final MaterialEditText edtComment = order_adress_comment.findViewById(R.id.edtComment);

                alertDialog.setView(order_adress_comment);
                alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Criar novo Request
                        Request request = new Request(
                                Common.atualUtilizador.getPhone(),
                                Common.atualUtilizador.getName(),
                                edtAdress.getText().toString(),
                                txtTotalPrice.getText().toString(),
                                "0",//status
                                edtComment.getText().toString(),
                                cart
                        );

                        //Submeter para o firebase
                        //Usar System.CurrentMilli para a chave
                        requests.child(String.valueOf(System.currentTimeMillis()))
                                .setValue(request);
                        //Submeter cart
                        new Database(getBaseContext()).cleanCart();
                        Toast.makeText(Cart.this,"Pedido realizado, obrigado",Toast.LENGTH_SHORT).show();
                        finish();
                    }

                });
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alertDialog.show();

    }

    private void loadListFood() {
        cart= new Database(this).getCarts();
        adapter= new CartAdapter(cart,this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        //Calcular o preco total
        int total=0;
        for (Order order:cart)
            total+=(Integer.parseInt(order.getFoodPrice()))*(Integer.parseInt(order.getQuantity()));
        Locale locale = new Locale("en","US");
        NumberFormat fmt=NumberFormat.getCurrencyInstance(locale);

        txtTotalPrice.setText(fmt.format(total));

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Common.DELETE))
            deleteCart(item.getOrder());
        return true;
    }

    private void deleteCart(int position) {
        cart.remove(position);
        new Database(this).cleanCart();
        for (Order item:cart)
            new Database(this).addToCart(item);
        loadListFood();
    }
}
