package riaz.chatrk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import riaz.chatrk.Objects.User_ModelObj;
import riaz.chatrk.SharedPreferences.SharedPrefManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN=9000;
    private static String TAG="riaz.chatrk.mainactivity";
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    SignInButton google_signin_btn;
    Button sign_out_btn;
    FirebaseAuth mAuth;
    GoogleSignInAccount account;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref,usersRef;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        google_signin_btn=findViewById(R.id.google_signin_btn);
        sign_out_btn=findViewById(R.id.sign_out_btn);

        sharedPrefManager=new SharedPrefManager(MainActivity.this);
        mAuth=FirebaseAuth.getInstance();

        ref= database.getReference();

        if(mAuth.getCurrentUser() == null) {
            // Start sign in/sign up activity
//            startActivityForResult(
//                    AuthUI.getInstance()
//                            .createSignInIntentBuilder()
//                            .build(),
//                    RC_SIGN_IN

//            );
        } else {
            // User is already signed in. Therefore, display
            // a welcome Toast
//            Toast.makeText(this,
//                    "Welcome " + FirebaseAuth.getInstance()
//                            .getCurrentUser()
//                            .getDisplayName(),
//                    Toast.LENGTH_LONG)
//                    .show();

            Intent chat_intent=new Intent(MainActivity.this, AllUser_Activity.class);
            chat_intent.putExtra("username",FirebaseAuth.getInstance()
                                                    .getCurrentUser()
                                                    .getDisplayName());

            chat_intent.putExtra("My_email",FirebaseAuth.getInstance()
                    .getCurrentUser().getEmail());
            chat_intent.putExtra("My_uid",sharedPrefManager.getString("Sender_Uid"));
            startActivity(chat_intent);
            finish();

            // Load chat room contents
//            displayChatMessages();
        }

         gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        google_signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signIn();
            }
        });

        sign_out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signOut();
            }
        });



        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

//        updateUI(account);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
            if (!task.isSuccessful()){

                Toast.makeText(this, "Sign in Cancelled !", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Sign in Success !", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            account = completedTask.getResult(ApiException.class);

            firebaseAuthWithGoogle(account);


            // Signed in successfully, show authenticated UI.
//            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                        Toast.makeText(MainActivity.this, "Logout successful !", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

//                            if (acct.getId().equals(account.getId() )){
//
//                                Toast.makeText(MainActivity.this, "Email already exist !", Toast.LENGTH_SHORT).show();
//
//                            }
//                            else {

//                                Toast.makeText(MainActivity.this, "Not same", Toast.LENGTH_SHORT).show();


                                usersRef = ref.child("users");
                                String uid=acct.getId();


                                usersRef.child(uid)
//                            usersRef.child("user_"+System.currentTimeMillis())
                                        .setValue(new User_ModelObj(acct.getDisplayName(),
                                                acct.getEmail(),
                                                ""+acct.getPhotoUrl(),
                                                acct.getId()));

//                            }




//                            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
//                            DatabaseReference reference=firebaseDatabase.getReference("chat");

                            sharedPrefManager.setString("Sender_Uid",acct.getId());

                            Intent chat_intent=new Intent(MainActivity.this, AllUser_Activity.class);
                            chat_intent.putExtra("username",acct.getDisplayName());
                            chat_intent.putExtra("My_email",acct.getEmail());
                            chat_intent.putExtra("My_uid",acct.getId());
                            startActivity(chat_intent);
                            finish();

//                            Toast.makeText(MainActivity.this,"Welcome "+ acct.getId(), Toast.LENGTH_SHORT).show();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            Toast.makeText(MainActivity.this, "Authentication Failed !", Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

//    public void updateUI(GoogleSignInAccount account){
//
//        account=mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//
////                DatabaseReference reference=firebaseAuth.
//
//            }
//        });
//    }
}
