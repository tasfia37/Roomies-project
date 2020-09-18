package com.example.roomies.Matches;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.roomies.R;

import java.util.List;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesViewHolder> { //populates item_matches.xml with correct info
    private List<MatchesObject> matchesList;                                   //extending the MatchesViewHolder
    private Context context;
    private RemoveItem mListener;

    public interface RemoveItem{   //interface to delete items from recycler view
        void DeleteItem(int position);
        void RemoveItemFromDb(String matchID,String userSex);
    }

    public void setRemoveItem(RemoveItem listener)
    {
        mListener = listener;
    }

    public MatchesAdapter(List<MatchesObject> matchesList,Context context) //constructor to call from matches activity//the matches list is passes here
    {
        this.matchesList = matchesList;
        this.context = context;
    }
    public MatchesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //creating the views

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_matches,null,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        MatchesViewHolder rcv = new MatchesViewHolder((layoutView),mListener);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull MatchesViewHolder holder, int position) { //putting values into item_matches//i.e putting values into views
        holder.mMatchId.setText(matchesList.get(position).getUserId());
        holder.mMatchname.setText(matchesList.get(position).getName());
        holder.mUserSex.setText(matchesList.get(position).getUserSex());
        holder.mDepartment.setText(matchesList.get(position).getDepartment());
        holder.mBatch.setText(matchesList.get(position).getBatch());
        if(!matchesList.get(position).getProfileImageUrl().equals("default")) {
            Glide.with(context).load(matchesList.get(position).getProfileImageUrl()).into(holder.mMatchImage);
        }
    }

    @Override
    public int getItemCount() {
        return this.matchesList.size();
    }
}
