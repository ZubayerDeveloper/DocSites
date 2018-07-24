package zubayer.docsites;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import me.anwarshahriar.calligrapher.Calligrapher;

import static android.widget.Toast.makeText;

public class Reply extends Activity {
    AlertDialog alert;
    AlertDialog.Builder alertBuilder;
    CircularImageView userImage,replyImage;
    ReplyAdapter adapter;
    LinearLayoutManager manager;
    RecyclerView recyclerView;
    ArrayList<String> replyName, replyTexts,replyUserId,replyPostId,replyTime;
    EditText edit_reply;
    SharedPreferences loginPreference;
    FirebaseDatabase database;
    DatabaseReference replyReference;
    HashMap<String, Object> reply_post;
    String userid,intentName,intentText,time,postID,facebook_id,facebook_user_name,replyID;
    boolean loggedin;
    ProgressDialog progressDialog;
    ImageView replyButton;
    TextView reply_post_name,reply_post_text,post_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reply);

        userid=getIntent().getExtras().getString("id");
        intentName=getIntent().getExtras().getString("name");
        intentText=getIntent().getExtras().getString("text");
        postID=getIntent().getExtras().getString("postID");
        time=getIntent().getExtras().getString("time");
        initialize();
        if(dataconnected()) {
            graphRequest();
        }else {
            alertMessage("Turn on data","Try again","Exit");
        }
        reply_post_name.setText(intentName);
        reply_post_text.setText(intentText);
        post_time.setText(time);
        Glide.with(this).load("https://graph.facebook.com/" + userid + "/picture?width=800").into(userImage);

        subscribeTopic(postID);
        myToaster(postID);
        progressDialog = ProgressDialog.show(this, "", "Connecting to Database...", true, false);
        replyReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                replyName.clear();replyTexts.clear();replyUserId.clear();replyPostId.clear();replyTime.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    replyPostId.add(snapshot.getKey());
                    replyName.add(snapshot.child("name").getValue(String.class));
                    replyTexts.add(snapshot.child("text").getValue(String.class));
                    replyTime.add(snapshot.child("time").getValue(String.class));
                    replyUserId.add(snapshot.child("myID").getValue(String.class));
                }
                progressDialog.dismiss();
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postReply();
            }
        });
    }

    private void myToaster(String text) {
        Toast toast = makeText(Reply.this, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void subscribeTopic(String topic) {
//        FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {

//            }
//        });
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(Reply.this,"Unsubscribed",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postReply() {
        if (edit_reply.getText().toString().length()!=0) {
            reply_post.put("name", facebook_user_name);
            reply_post.put("text", edit_reply.getText().toString());
            reply_post.put("time", replyDate());
            reply_post.put("myID", facebook_id);


            replyReference.child(replyTime()).setValue(reply_post).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    myToaster("success");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    myToaster("failed");
                }
            });
            edit_reply.setText(null);
        }else {
            myToaster("Empty text");
        }
    }

    private void initialize() {
        Calligrapher replyFont=new Calligrapher(Reply.this);
        replyFont.setFont(Reply.this,"kalpurush.ttf",true);
        reply_post_name=(TextView)findViewById(R.id.reply_post_name);
        reply_post_text=(TextView)findViewById(R.id.reply_post_text);
        post_time=(TextView)findViewById(R.id.post_time);
        replyName = new ArrayList<>();
        replyTexts = new ArrayList<>();
        replyUserId = new ArrayList<>();
        replyPostId = new ArrayList<>();
        replyTime = new ArrayList<>();
        loginPreference = getSharedPreferences("loggedin", Context.MODE_PRIVATE);
        database = FirebaseDatabase.getInstance();
        replyReference = database.getReference().child("user").child(postID).child("reply");
        reply_post = new HashMap<>();
        edit_reply = (EditText) findViewById(R.id.edit_reply);
        recyclerView = (RecyclerView) findViewById(R.id.reply_forum_recyclerView);
        replyButton = (ImageView) findViewById(R.id.reply_post);
        adapter = new ReplyAdapter(Reply.this, replyName, replyTexts,replyUserId,replyTime,replyPostId);
        manager = new LinearLayoutManager(Reply.this);
        recyclerView.setLayoutManager(manager);
        userImage=(CircularImageView)findViewById(R.id.reply_user_image);
        replyImage=(CircularImageView)findViewById(R.id.reply_post_image);
    }
    private String replyTime() {
        long mydate = System.currentTimeMillis();
        String reply_time = Long.toString(mydate);
        return reply_time;
    }
    private String replyDate() {
        long mydate = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy h:mm a");
        String reply_time = sdf.format(mydate);
        return reply_time;
    }
    private void graphRequest() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(final JSONObject object, GraphResponse response) {

                try {
                    facebook_id=object.getString("id");
                    facebook_user_name = object.getString("first_name") + " " + object.getString("last_name");
                    Glide.with(Reply.this).load("https://graph.facebook.com/" + facebook_id+ "/picture?width=800").into(replyImage);
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
        alertBuilder=new AlertDialog.Builder(Reply.this);
        alert = alertBuilder.create();
        alert.setCancelable(false);
        alert.setMessage(text);
        alert.setButton(DialogInterface.BUTTON1, positiveButtonName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Reply.this, Forum.class);
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
