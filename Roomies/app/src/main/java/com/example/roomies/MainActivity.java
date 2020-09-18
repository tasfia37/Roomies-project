package com.example.roomies;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.roomies.Cards.arrayAdapter;
import com.example.roomies.Cards.cards;
import com.example.roomies.Matches.MatchesActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private cards card_data[];
    private com.example.roomies.Cards.arrayAdapter arrayAdapter;
    private int i;
    private FirebaseAuth mAuth;
    public String currentUid;
    private DatabaseReference usersDb;

    ListView listView;
    List<cards> rowItems; //used to hold card information about potential matches

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usersDb= FirebaseDatabase.getInstance().getReference().child("Users"); //used to initialize DatabaseReference to access DB tree

        mAuth = FirebaseAuth.getInstance();

        checkUserSex();


        rowItems= new ArrayList<>();


        arrayAdapter = new arrayAdapter(this, R.layout.item,  rowItems );

        SwipeFlingAdapterView flingContainer =(SwipeFlingAdapterView) findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {  //event listner for left right swipe
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                cards obj= (cards)dataObject; //getting information out of the current card getting swiped from the screen
                String userId= obj.getUserId();
                usersDb.child(userSex).child(userId).child("connections").child("no").child(currentUid).setValue(true);
                Toast.makeText(MainActivity.this,"left",Toast.LENGTH_SHORT).show();
                //Toast.makeText(MainActivity.this,currentUid,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                cards obj= (cards)dataObject;
                String userId= obj.getUserId();
                usersDb.child(userSex).child(userId).child("connections").child("yes").child(currentUid).setValue(true);
                isConnectionMatch(userId); //to check is this user has also swiped right the current user
                Toast.makeText(MainActivity.this,"right",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(MainActivity.this,"click",Toast.LENGTH_SHORT).show();
            }
        });

    }

        private void isConnectionMatch(String userId) { //to check after i swiped right if this user also swiped right on me
            DatabaseReference currentUserConnectionDb = usersDb.child(userSex).child(currentUid).child("connections").child("yes").child(userId); //reference to the yes node of current user
                                                                                                                                                  //and checking if the yes node has the userId value
            currentUserConnectionDb.addListenerForSingleValueEvent(new ValueEventListener() { //checking if reference exists(single value event listner)
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {//after this listener is called we loose userId value
                if(dataSnapshot.exists()){
                    Toast.makeText(MainActivity.this,"New Match",Toast.LENGTH_SHORT).show();

                    String key = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();

                    //usersDb.child(userSex).child(dataSnapshot.getKey()).child("connections").child("matches").child(currentUid).setValue(true);
                    usersDb.child(userSex).child(dataSnapshot.getKey()).child("connections").child("matches").child(currentUid).child("ChatId").setValue(key);

                    //usersDb.child(userSex).child(currentUid).child("connections").child("matches").child(dataSnapshot.getKey()).setValue(true);
                    usersDb.child(userSex).child(currentUid).child("connections").child("matches").child(dataSnapshot.getKey()).child("ChatId").setValue(key);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String userSex;
    private String oppositeuserSex;

    public void checkUserSex()  //function to find user sex
    {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //used to get logged in user info
        currentUid=user.getUid(); //getting the logged in user id
        DatabaseReference maleDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Male"); //used to access DB tree
        maleDb.addChildEventListener(new ChildEventListener() { //event listner to loop through the DB tree
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { //loops through all the child
                if(dataSnapshot.getKey().equals(user.getUid())) //checking currentUID matches with an ID from the male tree
                {
                    userSex = "Male";
                    oppositeuserSex = "Female";
                    getOppositeSexUsers();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        DatabaseReference femaleDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Female");
        femaleDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getKey().equals(user.getUid()))
                {
                    userSex = "Female";
                    oppositeuserSex= "Male";
                    getOppositeSexUsers();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void getOppositeSexUsers() //used to add users of the same sex into the cards
    {
        DatabaseReference oppositeSexDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userSex);
        oppositeSexDb.addChildEventListener(new ChildEventListener() { //event listner to loop through the DB tree
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    if(dataSnapshot.exists() && !dataSnapshot.child("connections").child("yes").hasChild(currentUid) && !dataSnapshot.getKey().equals(currentUid) ) {
                        String profileImageUrl= "default";
                        if(!dataSnapshot.child("profileImageUrl").getValue().equals("default")){
                            profileImageUrl = dataSnapshot.child("profileImageUrl").getValue().toString();
                        }
                        cards item = new cards(dataSnapshot.getKey(),dataSnapshot.child("name").getValue().toString(),profileImageUrl,dataSnapshot.child("batch").getValue().toString(),dataSnapshot.child("department").getValue().toString());
                        rowItems.add(item); //adding information about potential matches to  display
                        arrayAdapter.notifyDataSetChanged();
                    }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    public void logoutUser(View view) {

        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this,LoginorRegistrationActivity.class);
        startActivity(intent);
        finish();
        return;
    }

    public void goToSettings(View view) {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        intent.putExtra("userSex",userSex);
        startActivity(intent);
        return;
    }

    public void goToMatches(View view) {
        Intent intent = new Intent(MainActivity.this, MatchesActivity.class);
        intent.putExtra("userSex",userSex);
        startActivity(intent);
        return;
    }
}
