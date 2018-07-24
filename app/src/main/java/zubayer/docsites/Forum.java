package zubayer.docsites;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import me.anwarshahriar.calligrapher.Calligrapher;

import static android.widget.Toast.makeText;

public class Forum extends Activity {

    AlertDialog alert;
    AlertDialog.Builder alertBuilder;
    CallbackManager callbackManager;
    CircularImageView post_image;
    ImageView send;
    ForumAdapter adapter;
    GraphRequest request;
    LinearLayoutManager manager;
    RecyclerView recyclerView;
    ArrayList<String> namess, textss, times, post_id, user_id;
    EditText edit_text;
    SharedPreferences loginPreference, myIDpreference;
    FirebaseDatabase database;
    DatabaseReference rootReference;
    HashMap<String, Object> post;
    boolean isLoggedIn;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    String facebookAuth, facebook_id, facebook_user_name, facebook_user_text, facebook_post_time;
    TextView fbname;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.forum);

        initialize();
        progressDialog = ProgressDialog.show(this, "", "Connecting to Database...", true, false);
        genetatekeyHash();
        if (dataconnected()) {
            facebookLigin();
        } else {
            alertMessage("Turn on data", "Try again", "Exit");
        }

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postQuery();
            }
        });
    }

    private void loadForumPost() {
        rootReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                namess.clear();
                textss.clear();
                times.clear();
                user_id.clear();
                post_id.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    post_id.add(0, snapshot.getKey());
                    namess.add(0, snapshot.child("name").getValue(String.class));
                    textss.add(0, snapshot.child("text").getValue(String.class));
                    times.add(0, snapshot.child("time").getValue(String.class));
                    user_id.add(0, snapshot.child("id").getValue(String.class));
                }
                progressDialog.dismiss();
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void graphRequest() {
        request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(final JSONObject object, GraphResponse response) {

                try {
                    facebook_id = object.getString("id");
                    myIDpreference.edit().putString("myID", facebook_id).apply();
                    facebook_user_name = object.getString("first_name") + " " + object.getString("last_name");
                    Glide.with(Forum.this).load("https://graph.facebook.com/" + facebook_id + "/picture?width=800").into(post_image);
                    loadForumPost();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void facebookLigin() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                graphRequest();

            }

            @Override
            public void onCancel() {
                progressDialog.dismiss();
                alertMessage("You need to log in first", "Try again", "Exit");
            }

            @Override
            public void onError(FacebookException error) {
                progressDialog.dismiss();
                alertMessage("Log in failed", "Try again", "Exit");
                Log.d("error", error.getMessage());
                LoginManager.getInstance().logOut();
            }
        });

    }

    private void getUserInfo(JSONObject object) {
        try {
            String idname = object.getString("first_name") + " " + object.getString("last_name");
//            userID = object.getString("id");
//            userIDpreference = getSharedPreferences("userIDpreference", Context.MODE_PRIVATE);
//            userIDpreference.edit().putString("userID", userID).apply();
//
//            profileUrl = "https://www.facebook.com/app_scoped_user_id/" + object.getString("id");
//            user_Name.setText(idname);
//            birthday.setText(object.getString("email"));
//            Picasso.get().load("https://graph.facebook.com/" + userID + "/picture?width=800").into(imageView);
//            profile.put("propic", object.getString("id"));
//            checkProfileConfirmation();

        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser newUser = mAuth.getCurrentUser();
                    updateUI(newUser);
                    myToaster("Facaebook accesstoken handled");
                } else {
                    myToaster("could not register to firebase");
                }
            }
        });
    }

    private void updateUI(FirebaseUser newUser) {
        facebookAuth = newUser.getUid();
        myToaster("uid got");
    }

    private void postQuery() {
        if (edit_text.getText().toString().length() != 0) {
            post.put("name", facebook_user_name);
            post.put("text", edit_text.getText().toString());
            post.put("time", postDate());
            post.put("id", facebook_id);
            rootReference.child(currentPostTime()).setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    edit_text.setText(null);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    myToaster("failed");
                }
            });
        } else {
            myToaster("Empty text");
        }
    }

    private void initialize() {
        alertBuilder = new AlertDialog.Builder(Forum.this);
        alert = alertBuilder.create();
        Calligrapher replyFont = new Calligrapher(Forum.this);
        replyFont.setFont(Forum.this, "kalpurush.ttf", true);
        namess = new ArrayList<>();
        textss = new ArrayList<>();
        times = new ArrayList<>();
        post_id = new ArrayList<>();
        user_id = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(this);
        loginPreference = getSharedPreferences("loggedin", Context.MODE_PRIVATE);
        myIDpreference = getSharedPreferences("myID", Context.MODE_PRIVATE);
        database = FirebaseDatabase.getInstance();
        rootReference = database.getReference().child("user");
        post = new HashMap<>();
        edit_text = (EditText) findViewById(R.id.edit_text);
        recyclerView = (RecyclerView) findViewById(R.id.forum_recyclerView);
        send = (ImageView) findViewById(R.id.send);
        adapter = new ForumAdapter(Forum.this, namess, textss, times, user_id, post_id);
        manager = new LinearLayoutManager(Forum.this);
        post_image = (CircularImageView) findViewById(R.id.post_image);
        recyclerView.setLayoutManager(manager);
    }

    private void myToaster(String text) {
        Toast toast = makeText(Forum.this, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void genetatekeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "zubayer.docsites",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    private String postDate() {
        long mydate = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy h:mm a");
        facebook_post_time = sdf.format(mydate);
        return facebook_post_time;
    }

    private String currentPostTime() {
        long mydate = System.currentTimeMillis();
        facebook_post_time = Long.toString(mydate);
        return facebook_post_time;
    }

    private String calculatePostTime(String post_time, int divide) {
        Long current_time = System.currentTimeMillis();
        Long postTime = Long.parseLong(post_time);

        Long difference = ((current_time - postTime) / divide);
        String ago = Long.toString(difference);
        return ago;

    }

    private boolean dataconnected() {
        boolean dataConnected = false;
        boolean wifiIsAvailable, mobileDataIsAvailable;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            wifiIsAvailable = networkInfo.isConnected();
            networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            mobileDataIsAvailable = networkInfo.isConnected();
            dataConnected = wifiIsAvailable || mobileDataIsAvailable;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataConnected;
    }

    private void alertMessage(String text, String positiveButtonName, String negativeButtonName) {
        alert = alertBuilder.create();
        alert.setCancelable(false);
        alert.setMessage(text);
        alert.setButton(DialogInterface.BUTTON1, positiveButtonName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Forum.this, Forum.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        alert.setButton(DialogInterface.BUTTON2, negativeButtonName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alert.show();
    }

}
