package riaz.chatrk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import riaz.chatrk.Adapter.FriendsListRecycler_Adapter;
import riaz.chatrk.Adapter.MessageRecycler_ViewAdapter;
import riaz.chatrk.Objects.AllChatCommunicationMessage_Obj;
import riaz.chatrk.Objects.User_ModelObj;
import riaz.chatrk.SharedPreferences.SharedPrefManager;

public class ChatActivity extends AppCompatActivity {

    TextView username;
    CircleImageView profile_img;
    String Uname="",uid="",unique_code="",sender_name="",email="",pro_Image,receiver_uid="";
    RecyclerView chat_msg_recycler;
    EditText message_editText;
    AppCompatImageView send_msg_img;
    MessageRecycler_ViewAdapter msg_adapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref,chat_ref_code,chat_ref;
    String current_format_date;
    Calendar c = Calendar.getInstance();
    ArrayList<AllChatCommunicationMessage_Obj> msg_arr;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;
    int unique_id;
    float unique=0.0f;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_message_layout);

        username=findViewById(R.id.username);
        profile_img=findViewById(R.id.profile_img);
        chat_msg_recycler=findViewById(R.id.chat_msg_recycler);
        message_editText=findViewById(R.id.message_editText);
        send_msg_img=findViewById(R.id.send_msg_img);
        sharedPrefManager=new SharedPrefManager(ChatActivity.this);

        msg_arr=new ArrayList<>();


        Uname=getIntent().getStringExtra("username");
        sender_name=getIntent().getStringExtra("sender_name");
        unique_code=getIntent().getStringExtra("unique_code");
//        uid=getIntent().getStringExtra("uid");
        email=getIntent().getStringExtra("email");
        pro_Image=getIntent().getStringExtra("profileImg");

        receiver_uid=sharedPrefManager.getString("Receiver_uid");
        uid=sharedPrefManager.getString("Sender_uid");

        username.setText(Uname);

        Glide.with(ChatActivity.this)
                .load(Uri.parse(pro_Image))
                .error(R.drawable.com_facebook_profile_picture_blank_portrait)
                .centerCrop()
                .into(profile_img);
//        Toast.makeText(this, receiver_uid, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, uid, Toast.LENGTH_SHORT).show();

        unique_id=Integer.parseInt(uid.substring(0,5))+Integer.parseInt(receiver_uid.substring(0,5));
//        Toast.makeText(this, String.valueOf(unique_id), Toast.LENGTH_SHORT).show();


//        try {
//            if (Integer.parseInt(uid)<Integer.parseInt(receiver_uid)){
//
//                unique_id=uid+receiver_uid;
//            }
//            else {
//                unique_id=receiver_uid+uid;
//            }
//        }
//        catch (NumberFormatException ne){
//            ne.printStackTrace();
//        }


        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference();
        chat_ref_code= ref.child("Chat_message");
        chat_ref= chat_ref_code.child(""+unique_id);


        //yyyy-MM-ddThh:mm:ssxxxZ

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssSSSZ");
         current_format_date= df.format(c.getTime());


        getAllMessages();


        send_msg_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (message_editText.getText().toString().trim().equals("")){

                    Toast.makeText(ChatActivity.this, "Please enter your message !", Toast.LENGTH_SHORT).show();
                }
                else {

                    Toast.makeText(ChatActivity.this, "Sending... !", Toast.LENGTH_SHORT).show();

                    chat_ref.child("msg_"+System.currentTimeMillis())
                            .setValue(new AllChatCommunicationMessage_Obj
                                    (message_editText.getText().toString().trim(),
                                    current_format_date,
                                            sender_name,
                                            uid,
                                            email));

                    message_editText.setText("");
                }


            }
        });

    }

    public void getAllMessages(){

        progressDialog=new ProgressDialog(ChatActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading messages...");
        progressDialog.show();

        final FirebaseUser f_user= FirebaseAuth.getInstance().getCurrentUser();


        DatabaseReference databaseReference=FirebaseDatabase.getInstance()
                .getReference("Chat_message")
                .child(""+unique_id);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                msg_arr.clear();

                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    AllChatCommunicationMessage_Obj msg_obj=ds.getValue(AllChatCommunicationMessage_Obj.class);

//                    if (!msg_obj.getSender_id().equals(f_user.getUid())){
                        msg_arr.add(msg_obj);
//                    }
                    //Set adapter here
                    chat_msg_recycler.setHasFixedSize(true);
                    chat_msg_recycler.setLayoutManager(new LinearLayoutManager(ChatActivity.this));

                    msg_adapter=new MessageRecycler_ViewAdapter(msg_arr,
                            ChatActivity.this,
                                    msg_obj.getEmail());
                    chat_msg_recycler.setAdapter(msg_adapter);
                    chat_msg_recycler.smoothScrollToPosition(msg_arr.size()-1);
                    msg_adapter.notifyDataSetChanged();

                    progressDialog.dismiss();
                }
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                progressDialog.dismiss();
            }
            });

    }
}
