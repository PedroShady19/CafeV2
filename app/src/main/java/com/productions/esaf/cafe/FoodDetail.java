package com.productions.esaf.cafe;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.productions.esaf.cafe.Database.Database;
import com.productions.esaf.cafe.Model.Food;
import com.productions.esaf.cafe.Model.Order;
import com.productions.esaf.cafe.Model.Rating;
import com.productions.esaf.cafe.common.Common;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.util.Arrays;

public class FoodDetail extends AppCompatActivity implements RatingDialogListener {

    TextView  food_name,food_price,food_description;
    ImageView food_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart,btnRating;
    RatingBar ratingBar;
    ElegantNumberButton numberButton;

    String foodID="";

    FirebaseDatabase database;
    DatabaseReference foods;
    DatabaseReference ratingTbl;
    Food currentFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        //Firebase
        database =FirebaseDatabase.getInstance();
        foods= database.getReference("Food");
        ratingTbl=database.getReference("Rating");

        //Init view
        numberButton =findViewById(R.id.number_button);
        btnCart = findViewById(R.id.btnCart);
        btnRating= findViewById(R.id.btnRating);
        ratingBar=(RatingBar) findViewById(R.id.ratingBar);

        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogRating();
            }
        });

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addToCart(new Order(
                        foodID,currentFood.getName(),numberButton.getNumber(),currentFood.getPrice(),currentFood.getDiscount()
                ));
                Toast.makeText(FoodDetail.this, "Added to Cart", Toast.LENGTH_SHORT).show();
            }
        });

        food_description= findViewById(R.id.food_description);
        food_name= findViewById(R.id.food_name);
        food_price= findViewById(R.id.food_price);
        food_image= findViewById(R.id.img_food);

        collapsingToolbarLayout= findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        //Get Food ID from intent
        if(getIntent()!=null)
            foodID=getIntent().getStringExtra("foodId");
        if(!foodID.isEmpty())
        {
            if(Common.isConnectedToInternet(getBaseContext()))
            {
                getDetailFood(foodID);
                getRatingFood(foodID);
            }
            else
            {
                Toast.makeText(FoodDetail.this, "Please check your connection", Toast.LENGTH_SHORT).show();
                
            }
        }
    }

    private void getRatingFood(String foodID) {
        Query foodRating = ratingTbl.orderByChild("foodId").equalTo(foodID);
        foodRating.addValueEventListener(new ValueEventListener() {
            int count=0,sum=0;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    Rating item=postSnapshot.getValue(Rating.class);
                    assert item != null;
                    sum+=Integer.parseInt(item.getRatingValue());
                    count++;
                }
                if(count !=0)
                {
                    float average = sum/count;
                    ratingBar.setRating(average);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showDialogRating() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Very Bad","Not Good","I'm Ok","Good","Excelent"))
                .setDefaultRating(1)
                .setTitle("Rate this food")
                .setDescription("Please rate the food and give your feeback")
                .setTitleTextColor(R.color.colorPrimary)
                .setDescriptionTextColor(R.color.colorPrimary)
                .setHint("Please write your comment here...")
                .setHintTextColor(R.color.colorAccent)
                .setCommentTextColor(android.R.color.white)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(FoodDetail.this)
                .show();
    }

    private void getDetailFood(final String foodID) {
        foods.child(foodID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentFood =dataSnapshot.getValue(Food.class);

                //Set Image
                Picasso.with(getBaseContext()).load(currentFood.getImage()).into(food_image);
                collapsingToolbarLayout.setTitle(currentFood.getName());
                food_price.setText(currentFood.getPrice());
                food_name.setText(currentFood.getName());
                food_description.setText(currentFood.getDescription());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(int value, String comments) {
        //Get Rating
        final Rating rating= new Rating(Common.atualUtilizador.getPhone(),foodID,String.valueOf(comments),
                String.valueOf(value));
        ratingTbl.child(Common.atualUtilizador.getPhone()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(Common.atualUtilizador.getPhone()).exists())
                {
                    //Remove old value
                    ratingTbl.child(Common.atualUtilizador.getPhone()).removeValue();
                    //Update new value
                    ratingTbl.child(Common.atualUtilizador.getPhone()).setValue(rating);
                }
                else
                {
                    //Update new value
                    ratingTbl.child(Common.atualUtilizador.getPhone()).setValue(rating);
                }
                Toast.makeText(FoodDetail.this, "Thank you for your feeback", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
