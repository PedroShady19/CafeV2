package com.productions.esaf.cafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.productions.esaf.cafe.Interface.ItemClickListener;
import com.productions.esaf.cafe.Model.Food;
import com.productions.esaf.cafe.ViewHolder.FoodViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Food_list extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList;

    String  categoriaId="";
    FirebaseRecyclerAdapter<Food,FoodViewHolder> adapter;

    //Search Functionality
    FirebaseRecyclerAdapter<Food,FoodViewHolder> searchAdapter;
    List<String> suggestList= new ArrayList<>();
    MaterialSearchBar materialSearchBar;

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
        //Search Bar Functionality
        materialSearchBar=(MaterialSearchBar)findViewById(R.id.searchBar);
        materialSearchBar.setHint("Search your food name");

        loadSuggest();
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Quando o utilizador introduzir o texto colocar em letra minuscula
                List<String > suggest = new ArrayList<>();
                for(String search:suggestList)
                {
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //Quando a barra de procura está fechada restaurar o adpater
                if(!enabled)
                    recyclerView.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                //Quando a procura está concluida mostrar resultado do adapter
                startSearch(text);

            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    private void startSearch(CharSequence text) {
        searchAdapter= new FirebaseRecyclerAdapter<Food, FoodViewHolder>(
                Food.class,
                R.layout.food_item,
                FoodViewHolder.class,
                foodList.orderByChild("Name").equalTo(text.toString()) //comparar o nome
                ) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, Food model, int position) {
                viewHolder.food_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.food_image);

                final Food local=model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Start new Activity
                        Intent foodDetail = new Intent(Food_list.this,FoodDetail.class);
                        foodDetail.putExtra("FoodId",searchAdapter.getRef(position).getKey()); //Send food ID to new activity
                        startActivity(foodDetail);
                    }
                });
            }

        };
        recyclerView.setAdapter(searchAdapter);
    }


    private void loadSuggest() {
        foodList.orderByChild("MenuId").equalTo(categoriaId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                        {
                            Food item = postSnapshot.getValue(Food.class);
                            suggestList.add(item.getName()); //Adicionar o nome da comida para sugeridos

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        
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
                        //Start new Activity
                        Intent foodDetail = new Intent(Food_list.this,FoodDetail.class);
                        foodDetail.putExtra("FoodId",adapter.getRef(position).getKey()); //Send food ID to new activity
                        startActivity(foodDetail);
                    }
                });
            }
        };
        //Set Adapter
        recyclerView.setAdapter(adapter);
    }
}
