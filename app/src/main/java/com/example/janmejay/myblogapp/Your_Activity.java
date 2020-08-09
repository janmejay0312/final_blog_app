package com.example.janmejay.myblogapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Your_Activity extends AppCompatActivity {


    private RecyclerView recyclerView1;
    private CardView cardView;
    private RecyclerView.LayoutManager layoutManager1;
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private AlertDialog.Builder builder;
    private ProgressBar progressBar;
    private ActionBar actionBar;
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
        ActionBar a = getSupportActionBar();
        a.setTitle("Your Blog");
        LayoutInflater inflator = LayoutInflater.from(this);
        View v = inflator.inflate(R.layout.action_bar_layout, null);
        ((LinearLayout) v).setGravity(Gravity.RIGHT);

        a.setCustomView(v);



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
                /*        final Button button = holder.share;

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

                        });*/
                        holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                            @Override
                            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                                menu.setHeaderTitle("Select Action");
                                MenuItem edit = menu.add(Menu.NONE,1,1,"Edit");
                                MenuItem delete = menu.add(Menu.NONE,2,2,"Delete");
                                MenuItem share=menu.add(Menu.NONE,3,3,"Share");
                                edit.setOnMenuItemClickListener(onChange);
                                delete.setOnMenuItemClickListener(onChange);
                                share.setOnMenuItemClickListener(onChange);
                            }

                            private final MenuItem.OnMenuItemClickListener onChange = new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    switch (item.getItemId()){
                                        case 1:
                                            Intent intent = new Intent(getApplicationContext(), PostActivity.class);
                                            intent.putExtra("rowId", model.getId());
                                            intent.putExtra("title", model.getTitle());
                                            intent.putExtra("description", model.getDescription());
                                            intent.putExtra("image", model.getImage());
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            getApplicationContext().startActivity(intent);

                                            return true;
                                        case 2:
                                            mDatabase.child(model.getId()).removeValue();
                                            return true;
                                        case 3:
                                            Intent in = new Intent(android.content.Intent.ACTION_SEND);
                                            in.putExtra(Intent.EXTRA_TEXT, model.getDescription());
                                            in.putExtra(Intent.EXTRA_TEXT, model.getImage());
                                            in.setType("text/plain");
                                            getApplicationContext().startActivity(Intent.createChooser(in, "share via"));
                                            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                            return true;

                                    }
                                    return false;
                                }
                            };
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





