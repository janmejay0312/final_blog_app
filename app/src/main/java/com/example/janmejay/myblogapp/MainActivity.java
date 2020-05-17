package com.example.janmejay.myblogapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CardView cardView;
    private String you;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private StorageReference astorage;
    private AlertDialog.Builder builder;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private Uri uri;
    private static final int GALLERY_REQUEST = 1;
    private Button choose;
    private ImageView your_photo;
    private String imageUri;
    //private ImageView like;
    //private ImageView dislike;
    private FirebaseRecyclerAdapter<Blog, BlogViewHolder1> firebaseRecyclerAdapter;
    //this is main activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Your_Blog");
        recyclerView = findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
//setting navigation drawer.
        dl = findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);

        dl.addDrawerListener(t);
        t.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView) findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.account:
                        Intent i = new Intent(MainActivity.this, Your_Activity.class);
                        startActivity(i);
                        break;
                    case R.id.settings:
                        Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.mycart:
                        builder.setMessage("Logging out").setTitle("do you really to log out ");
                        builder.setMessage("Logging out")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        logOut();
                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();

                            }
                        });
                        //Creating dialog box
                        AlertDialog alert = builder.create();
                        //Setting the title manually
                        alert.setTitle("do you really want to log out ");
                        alert.show();
                        break;

                    default:
                        return true;
                }
                return true;
            }
        });
        if(FirebaseAuth.getInstance().getCurrentUser()==null)
        {

            startActivity(new Intent( this,LoginActivity.class));

        }
        //setting Profile name
        you = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final View headerLayout =
                nv.getHeaderView(0);

     //   DatabaseReference name=FirebaseDatabase.getInstance().getReference().child("User").child("name");

        FirebaseDatabase.getInstance().getReference().child("User").child(you).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String your_name = dataSnapshot.getValue(String.class);
                final TextView account_name = headerLayout.findViewById(R.id.account_your);
                account_name.setText(your_name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//setting Profileimage
        your_photo=headerLayout.findViewById(R.id.photo);
        Button button=headerLayout.findViewById(R.id.choose);
        your_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_REQUEST);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProfilePicture();
            }
        });
//setting recyclerview

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new EqualSpacingItemDecoration(26));
        builder = new AlertDialog.Builder(this);
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                }
            }
        };
        FirebaseRecyclerOptions<Blog> options =
                new FirebaseRecyclerOptions.Builder<Blog>()
                        .setQuery(mDatabase, Blog.class)
                        .build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder1>(options) {

            @NonNull
            @Override
            public BlogViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);

                return new BlogViewHolder1(view, getApplicationContext());
            }

            @Override
            protected void onBindViewHolder(@NonNull final BlogViewHolder1 holder, int position, @NonNull final Blog model) {
                holder.setDescription(model.getDescription());
                holder.setTitle(model.getTitle());

                holder.setImage(model.getImage(), getApplicationContext());
                holder.setTime(model.getTime());

                holder.like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.like.setEnabled(false);
                        mDatabase.child(model.getId()).child("like").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                long l=dataSnapshot.getValue(long.class);
                                l++;
                                mDatabase.child(model.getId()).child("like").setValue(l);
                                holder.setUpvote(l);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });

                holder.dislike.setOnClickListener(new View.OnClickListener() {
                    @Override

                    public void onClick(View v) {
                        holder.dislike.setEnabled(false);
                        mDatabase.child(model.getId()).child("dislike").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                long l=dataSnapshot.getValue(long.class);
                                l++;
                                mDatabase.child(model.getId()).child("dislike").setValue(l);
                                holder.setDownvote(l);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
                //clicking recyclerview item
                final ImageView image1 = holder.image;
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
//setting pop-up button
            /*    button.setOnClickListener(new View.OnClickListener() {
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
/*holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select Action");
        MenuItem edit = menu.add(Menu.NONE,1,1,"Edit");
        MenuItem delete = menu.add(Menu.NONE,2,2,"Delete");
        edit.setOnMenuItemClickListener(onChange);
        delete.setOnMenuItemClickListener(onChange);
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
                    getApplicationContext().startActivity(intent);
                    return true;
                case 2:
                    mDatabase.child(model.getId()).removeValue();
                    return true;
            }
            return false;
        }
    };
});*/


            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);



    }

    private void setProfilePicture() {
        astorage = FirebaseStorage.getInstance().getReference().child("Photos").child(uri.getLastPathSegment());
        astorage.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                astorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("image").setValue(uri.toString());

                    }
                });
            }
        });
        FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                imageUri = dataSnapshot.getValue(String.class);
                Glide.with(getApplicationContext()).load(imageUri).apply(RequestOptions.circleCropTransform()).into(your_photo);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    protected void onRestart(){
        super.onRestart();

    }
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
        firebaseRecyclerAdapter.startListening();
        String user=FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("User").child(user).child("image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String prof=dataSnapshot.getValue(String.class);
                Glide.with(getApplicationContext()).load(prof).apply(RequestOptions.circleCropTransform()).into(your_photo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }




    public static  class BlogViewHolder1 extends RecyclerView.ViewHolder   {
        View mView;
        Context context;
        Button share;
        ImageView image;
        private ImageView like;
        private ImageView dislike;
        TextView noOfLike;
        TextView noOfDislike;

        BlogViewHolder1(View itemView,Context context) {
            super(itemView);
            mView=itemView;
            this.context=context;
         //   share=itemView.findViewById(R.id.share);

            image=itemView.findViewById(R.id.image1);
            like=itemView.findViewById(R.id.upvote);
            dislike=itemView.findViewById(R.id.downvote);
//itemVi//ew.setOnCreateContextMenuListener(this);
        }

        public void setTitle(String title){
            TextView postTitle=mView.findViewById(R.id.text3);
            postTitle.setText(title);


        }
        public void setDescription(final String description){
            TextView postDesc=mView.findViewById(R.id.text4);
            postDesc.setText(description);

        }
        public void setImage(String image,Context context){

            ImageView postImage=mView.findViewById(R.id.image1);
            postImage.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(context).load(image).apply(RequestOptions.centerCropTransform()).into(postImage);
        }
        public void setTime(String time){
            TextView textView=mView.findViewById(R.id.time);
            textView.setText(time);
        }
       public void setUpvote(long upvote){
           noOfLike=itemView.findViewById(R.id.no_of_like);
           noOfLike.setText(String.valueOf(upvote));
       }
       public void setDownvote(long downvote){
           noOfDislike=itemView.findViewById(R.id.no_of_dislike);
           noOfDislike.setText(String.valueOf(downvote));
       }
    /*   public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem edit = menu.add(Menu.NONE,1,1,"Edit");
            MenuItem delete = menu.add(Menu.NONE,2,2,"Delete");
            MenuItem share=menu.add(Menu.NONE,3,3,"Share");


            edit.setOnMenuItemClickListener(onChange);
            delete.setOnMenuItemClickListener(onChange);
        }
        private final MenuItem.OnMenuItemClickListener onChange = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case 1:
                        Toast.makeText(context,"Delete",Toast.LENGTH_LONG).show();

                        return true;
                    case 2:
                        Toast.makeText(context,"Delete",Toast.LENGTH_LONG).show();
                        return true;
                    case 3:

                }
                return false;
            }
        };


        @Override
        public void onClick(View v) {

        }*/
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        return super.onCreateOptionsMenu(menu);
    }
    //going to post activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_add){
            Intent intent=new Intent(MainActivity.this,PostActivity.class);
            startActivity(intent);}
        if(t.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    private void logOut() {
        firebaseAuth.signOut();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_REQUEST&&resultCode==RESULT_OK&&data.getData()!=null){
            uri=data.getData();
            your_photo.setImageURI(uri);
        }
    }
}