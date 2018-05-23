package com.productions.esaf.cafe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.productions.esaf.cafe.Interface.ItemClickListener;
import com.productions.esaf.cafe.Model.Food;
import com.productions.esaf.cafe.ViewHolder.FoodViewHolder;
import com.squareup.picasso.Picasso;

public class Food_list extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList;

    String  categoriaId="";
    FirebaseRecyclerAdapter<Food,FoodViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        //Firebase
        database = FirebaseDatabase.getInstance();
        foodList= database.getReference("Food");

        recyclerView = findViewById(R.id.recycler_food_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Get Intent from HOME
        if(getIntent() !=null)
            categoriaId= getIntent().getStringExtra("CategoriaId");
        if(!categoriaId.isEmpty())
        {
            loadlistFood(categoriaId);
        }
    }

    private void loadlistFood(String categoriaId) {
        adapter= new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class,
                R.layout.food_item,
                FoodViewHolder.class,
                foodList.orderByChild("MenuId").equalTo(categoriaId) //Like : SELECT * FROM Food where MenuId=
                ) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, Food model, int position) {
                viewHolder.food_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.food_image);

                final Food local=model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(Food_list.this,""+local.getName(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        //Set Adapter
        recyclerView.setAdapter(adapter);
    }
}