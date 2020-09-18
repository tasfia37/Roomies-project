package com.example.roomies.Matches;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.roomies.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MatchesActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MatchesAdapter mMatchesAdapter;  //poiting to the custom adapter made for recycler view
    private RecyclerView.LayoutManager mMatchesLayoutManager;
    public String currentUserId; //logged in user
    public String userSex;
    public DatabaseReference usersDb,currUserDB;
    public String UserSexFinal,MatchIDFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        userSex= getIntent().getExtras().getString("userSex");

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        currUserDB = FirebaseDatabase.getInstance().getReference().child("Users");

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid(); //getting current user id

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mMatchesLayoutManager = new LinearLayoutManager(MatchesActivity.this);
        mRecyclerView.setLayoutManager(mMatchesLayoutManager);
        mMatchesAdapter = new MatchesAdapter(getDataSetMatches(), MatchesActivity.this);
        mRecyclerView.setAdapter(mMatchesAdapter);

        getUserMatchId();

        mMatchesAdapter.setRemoveItem(new MatchesAdapter.RemoveItem() { //setRemoveItem is the function
            @Override                                                   //RemoveItem is the interface
            public void DeleteItem(int position) {
                resultMatches.remove(position);
                mMatchesAdapter.notifyDataSetChanged();
                Toast.makeText(MatchesActivity.this,"User Unmatched",Toast.LENGTH_LONG).show();
            }

            @Override
            public void RemoveItemFromDb(String matchID, String userSex) {
                //DatabaseReference usersDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userSex).child(matchID).child("connections");
                //DatabaseReference currUserDB = FirebaseDatabase.getInstance().getReference().child("Users").child(userSex).child(currentUserId).child("connections");
                UserSexFinal = userSex;
                MatchIDFinal = matchID;
                DatabaseReference matcheduserDB = usersDb.child(UserSexFinal).child(MatchIDFinal).child("connections").child("yes").child(currentUserId);

                matcheduserDB.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            usersDb.child(UserSexFinal).child(MatchIDFinal).child("connections").child("yes").child(currentUserId).removeValue();
                            usersDb.child(UserSexFinal).child(MatchIDFinal).child("connections").child("matches").child(currentUserId).removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                DatabaseReference CurrentUserDB = currUserDB.child(UserSexFinal).child(currentUserId).child("connections").child("yes").child(MatchIDFinal);

                CurrentUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            currUserDB.child(UserSexFinal).child(currentUserId).child("connections").child("yes").child(MatchIDFinal).removeValue();
                            currUserDB.child(UserSexFinal).child(currentUserId).child("connections").child("matches").child(MatchIDFinal).removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                /*currUserDB.child("yes").child(matchID).setValue(false);

                usersDb.child("matches").child(currentUserId).setValue(false);
                currUserDB.child("matches").child(matchID).setValue(false);*/
            }
        });

    }


    private void getUserMatchId() {
        DatabaseReference matchDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userSex).child(currentUserId).child("connections").child("matches");
        matchDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { //loop the matches of current user
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot match : dataSnapshot.getChildren())
                    {
                        FetchMatchInformation(match.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void FetchMatchInformation(String key) { //getting match information
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userSex).child(key);
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String userId = dataSnapshot.getKey(); //as we are loosing the key value(parameter of function) after event listener call
                    String name = "";
                    String profileImageUrl = "";
                    String matchedUserSex="";
                    String Department="";
                    String Batch="";

                    if(dataSnapshot.child("name").getValue()!=null){
                        name=dataSnapshot.child("name").getValue().toString();
                    }
                    if(dataSnapshot.child("profileImageUrl").getValue()!=null){
                        profileImageUrl=dataSnapshot.child("profileImageUrl").getValue().toString();
                    }
                    if(dataSnapshot.child("department").getValue()!=null){
                        Department=dataSnapshot.child("department").getValue().toString();
                    }
                    if(dataSnapshot.child("batch").getValue()!=null){
                        Batch=dataSnapshot.child("batch").getValue().toString();
                    }

                    matchedUserSex = userSex;
                    MatchesObject obj =new MatchesObject(userId ,name, profileImageUrl,matchedUserSex,Department, Batch);
                    resultMatches.add(obj);
                    mMatchesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public ArrayList<MatchesObject> resultMatches = new ArrayList<MatchesObject>() ;  //list to store all the matches
    private List<MatchesObject> getDataSetMatches() {
        return resultMatches;
    }
}
