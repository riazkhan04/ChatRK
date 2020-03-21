package riaz.chatrk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import riaz.chatrk.Adapter.FriendsListRecycler_Adapter;
import riaz.chatrk.Objects.User_ModelObj;
import riaz.chatrk.SharedPreferences.SharedPrefManager;

public class AllUser_Activity extends AppCompatActivity {

    Button sign_out;
    TextView userNm_tv;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInOptions gso;
    String username="",email="",sender_uid="";
    RecyclerView chat_recyclerV;
    DatabaseReference myDatabase_ref;
    ArrayList<User_ModelObj> user_arr;
    FriendsListRecycler_Adapter adapter;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alluser_activity_layout);

        sign_out=findViewById(R.id.sign_out);
        userNm_tv=findViewById(R.id.user_name);
        chat_recyclerV=findViewById(R.id.chat_recyclerV);
        sharedPrefManager=new SharedPrefManager(AllUser_Activity.this);

        user_arr=new ArrayList<>();


        username=getIntent().getStringExtra("username");
        email=getIntent().getStringExtra("My_email");
        sender_uid=getIntent().getStringExtra("My_uid");


        userNm_tv.setText("Welcome , "+username);

//        Toast.makeText(this, sender_uid, Toast.LENGTH_SHORT).show();


        getAllUsers();

        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signOut();
            }
        });


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        myDatabase_ref=FirebaseDatabase.getInstance().getReference();



    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                        Toast.makeText(AllUser_Activity.this, "Logout successfull !", Toast.LENGTH_SHORT).show();

                        Intent signout_intent=new Intent(AllUser_Activity.this,MainActivity.class);
                        startActivity(signout_intent);
                        finish();
                    }
                });

        FirebaseAuth.getInstance().signOut();//For signout from firebase
        sharedPrefManager.clearData();

    }

    public void getAllUsers(){

        progressDialog=new ProgressDialog(AllUser_Activity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final FirebaseUser f_user=FirebaseAuth.getInstance().getCurrentUser();

//        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();

        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("users");
//        DatabaseReference usersdRef = databaseReference.child("users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                user_arr.clear();

                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    User_ModelObj user_modelObj=ds.getValue(User_ModelObj.class);

                    if (!user_modelObj.getUid().equals(f_user.getUid())){

                        if (user_modelObj.getEmail().equals(email)){

                        }
                        else {
                            user_arr.add(user_modelObj);
                        }


                    }
                    progressDialog.dismiss();
                    //Set adapter here
                    chat_recyclerV.setHasFixedSize(true);
                    chat_recyclerV.setLayoutManager(new LinearLayoutManager(AllUser_Activity.this));

                    adapter=new FriendsListRecycler_Adapter(AllUser_Activity.this, user_arr,
                            new FriendsListRecycler_Adapter.ItemSelectListener() {
                                @Override
                                public void onFriends_ItemClickListener(int pos, String UID,
                                                                        String name,String image) {

//                                    Toast.makeText(AllUser_Activity.this, name, Toast.LENGTH_SHORT).show();


                                    sharedPrefManager.setString("Receiver_uid",UID);
                                    sharedPrefManager.setString("Sender_uid",sender_uid);

                                    Intent chat_intent=new Intent(AllUser_Activity.this,ChatActivity.class);
                                    chat_intent.putExtra("username",name);
                                    chat_intent.putExtra("sender_name",username);
                                    chat_intent.putExtra("unique_code",email+"_"+UID);
                                    chat_intent.putExtra("uid",sender_uid);
                                    chat_intent.putExtra("email",email);
                                    chat_intent.putExtra("profileImg",image);
                                    startActivity(chat_intent);



                                }
                            });
                    chat_recyclerV.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                progressDialog.dismiss();
            }
        });

    }


}
