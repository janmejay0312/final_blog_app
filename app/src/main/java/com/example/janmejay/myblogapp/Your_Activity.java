package com.example.janmejay.myblogapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Your_Activity extends AppCompatActivity {


    private RecyclerView recyclerView1;
    private CardView cardView;
    private RecyclerView.LayoutManager layoutManager1;
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private AlertDialog.Builder builder;
    private ProgressBar progressBar;
    private android.support.v7.app.ActionBar actionBar;
    private String id5, userId;
    private Query query;
    private FirebaseRecyclerAdapter<Blog, MainActivity.BlogViewHolder1> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Blog> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Your_Blog");
        recyclerView1 = findViewById(R.id.recyclerview1);
        layoutManager1 = new LinearLayoutManager(this);
        recyclerView1.setHasFixedSize(true);
        android.support.v7.app.ActionBar a = getSupportActionBar();
        assert a != null;
        a.setHomeButtonEnabled(true);
        a.setDisplayHomeAsUpEnabled(true);
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView1.addItemDecoration(new EqualSpacingItemDecoration(16));
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("User").child(currentUser).child("id").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userId = dataSnapshot.getValue(String.class);
                query = mDatabase.orderByChild("user_id").equalTo(userId);
                options =
                        new FirebaseRecyclerOptions.Builder<Blog>()
                                .setQuery(query, Blog.class)
                                .build();
                firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, MainActivity.BlogViewHolder1>(options) {

                    @NonNull
                    @Override
                    public MainActivity.BlogViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);

                        return new MainActivity.BlogViewHolder1(view, getApplicationContext());
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull final MainActivity.BlogViewHolder1 holder, int position, @NonNull final Blog model) {
                        holder.setDescription(model.getDescription());
                        holder.setTitle(model.getTitle());
                        holder.setImage(model.getImage(), getApplicationContext());
                        holder.setTime(model.getTime());
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(v.getContext(), Edit_Activity.class);
                                intent.putExtra("rowId", model.getId());
                                intent.putExtra("title", model.getTitle());
                                intent.putExtra("description", model.getDescription());
                                intent.putExtra("image", model.getImage());
                                intent.putExtra("time", model.getTime());
                                v.getContext().startActivity(intent);
                            }
                        });
                        final Button button = holder.share;

                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                PopupMenu popup = new PopupMenu(button.getContext(), button);
                                popup.inflate(R.menu.menu_item1);
                                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        switch (item.getItemId()) {
                                            case R.id.action_edit:

                                                Context context = v.getContext();
                                                Intent intent = new Intent(context, PostActivity.class);
                                                intent.putExtra("rowId", model.getId());
                                                intent.putExtra("title", model.getTitle());
                                                intent.putExtra("description", model.getDescription());
                                                intent.putExtra("image", model.getImage());
                                                context.startActivity(intent);
                                                return true;

                                            case R.id.action_share:
                                                Intent in = new Intent(android.content.Intent.ACTION_SEND);
                                                in.putExtra(Intent.EXTRA_TEXT, model.getDescription());
                                                in.putExtra(Intent.EXTRA_TEXT, model.getImage());
                                                in.setType("text/plain");
                                                v.getContext().startActivity(Intent.createChooser(in, "share via"));
                                                break;
                                            case R.id.action_delete:
                                                mDatabase.child(model.getId()).removeValue();
                                                return true;
                                        }
                                        return false;
                                    }

                                });
                                popup.show();
                            }

                        });
                    }
                };
                firebaseRecyclerAdapter.startListening();
                recyclerView1.setAdapter(firebaseRecyclerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent homeIntent = new Intent(this, MainActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}





