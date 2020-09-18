package com.example.roomies.Matches;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomies.Chat.ChatActivity;
import com.example.roomies.MainActivity;
import com.example.roomies.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MatchesViewHolder extends RecyclerView.ViewHolder{ //function called to populate recycler view implements View.OnClickListener

    public TextView mMatchId,mMatchname,mUserSex,mDepartment,mBatch;
    public ImageView mMatchImage,mDeleteImage;
   // public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
   // public String currentUID;
   // private DatabaseReference usersDb,currUserDB;

    public MatchesViewHolder(@NonNull View itemView, final MatchesAdapter.RemoveItem listener) {
        super(itemView);
        //itemView.setOnClickListener(this);
        mMatchId = (TextView) itemView.findViewById(R.id.Matchid);
        mMatchname = (TextView) itemView.findViewById(R.id.MatchName);
        mMatchImage = (ImageView) itemView.findViewById(R.id.MatchImage);
        mUserSex = (TextView) itemView.findViewById(R.id.UserSex);
        mDepartment = (TextView) itemView.findViewById(R.id.Department);
        mBatch = (TextView) itemView.findViewById(R.id.Batch);
        mDeleteImage = (ImageView) itemView.findViewById(R.id.image_delete);

        //currentUID = user.getUid();
        //usersDb = FirebaseDatabase.getInstance().getReference().child("Users").child(mUserSex.toString()).child(mMatchId.toString()).child("connections");
        //currUserDB = FirebaseDatabase.getInstance().getReference().child("Users").child(mUserSex.toString()).child(currentUID).child("connections");
        mDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(listener != null)
                {
                    int position = getAdapterPosition(); //gets position of clicked item
                    if(position != RecyclerView.NO_POSITION)
                    {
                        listener.RemoveItemFromDb(mMatchId.getText().toString(),mUserSex.getText().toString());
                        listener.DeleteItem(position);
                    }
                }

            }
        });
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChatActivity.class);
                Bundle b = new Bundle();
                b.putString("matchId",mMatchId.getText().toString());
                b.putString("userSex",mUserSex.getText().toString());
                intent.putExtras(b);
                v.getContext().startActivity(intent);
            }
        });
    }

    /*private void RemoveItemFromDb()
    {
        usersDb.child("yes").child(currentUID).setValue(false);
        currUserDB.child("yes").child(mMatchId.toString()).setValue(false);

        usersDb.child("matches").child(currentUID).setValue(false);
        currUserDB.child("matches").child(mMatchId.toString()).setValue(false);
    }*/

    /*@Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), ChatActivity.class);
        Bundle b = new Bundle();
        b.putString("matchId",mMatchId.getText().toString());
        b.putString("userSex",mUserSex.getText().toString());
        intent.putExtras(b);
        view.getContext().startActivity(intent);
    }*/

}