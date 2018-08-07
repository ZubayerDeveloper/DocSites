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
import android.support.design.internal.NavigationMenu;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import me.anwarshahriar.calligrapher.Calligrapher;

import static android.widget.Toast.makeText;

public class Reply extends Activity {
    AlertDialog alert;
    AlertDialog.Builder alertBuilder;
    CircularImageView userImage, replyImage;
    FabSpeedDial forum_subscription;
    ReplyAdapter adapter;
    LinearLayoutManager manager;
    RecyclerView recyclerView;
    ArrayList<String> replyName, replyTexts, replyUserId, replyPostId, replyTime;
    EditText edit_reply;
    SharedPreferences loginPreference, myIdPreference, postIdPreference, devicetokenPreference;
    FirebaseDatabase database;
    DatabaseReference replyReference, deleteReference, unsubscribeReference, rootReference;
    HashMap<String, Object> reply_post;
    String userid, intentName, intentText, time, postID, rootPost,blocked,
            facebook_user_name, myID, postHoldersDeviceToken, replyTextFromEditText;
    boolean loggedin;
    ProgressDialog progressDialog;
    ImageView replyButton;
    TextView reply_post_name, reply_post_text, post_time, delete_Post,varified;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reply);

        getIntentvalue();
        initialize();
        setValueToMainPost();
        loadBlocklist();
        progressDialog = ProgressDialog.show(this, "", "Connecting to Database...", true, false);
        replyReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                replyName.clear();
                replyTexts.clear();
                replyUserId.clear();
                replyPostId.clear();
                replyTime.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    replyPostId.add(snapshot.getKey());
                    replyName.add(snapshot.child("name").getValue(String.class));
                    replyTexts.add(snapshot.child("text").getValue(String.class));
                    replyTime.add(elapsedTime(snapshot.getKey(), snapshot.child("time").getValue(String.class)));
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

                rootReference = database.getReference().child("user");
                rootReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot.getKey().equals(postID)) {
                                rootPost = snapshot.getKey();
                            }

                        }
                        if (rootPost != null) {
                            postReply();
                            rootPost=null;
                        } else {
                            myToaster("This post does not exist");
                            finish();
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                FirebaseMessaging.getInstance().unsubscribeFromTopic(postID).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        workSequence(postID, postHoldersDeviceToken);

                    }
                });
            }

        });
        delete_Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.setMessage("Delete post?");
                alert.setButton(DialogInterface.BUTTON_POSITIVE, "Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        unSubscribe(postID, postHoldersDeviceToken);
                        HashMap<String, Object> unsubscribeList = new HashMap<>();
                        unsubscribeReference = database.getReference().child("unsubscribe");
                        unsubscribeList.put(postID, postID);
                        unsubscribeReference.updateChildren(unsubscribeList).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                        deleteReference = database.getReference().child("user");
                        deleteReference.child(postID).setValue(null);
                        finish();
                    }
                });
                alert.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alert.show();


            }
        });
        forum_subscription.setMenuListener(new FabSpeedDial.MenuListener() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                return true;
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.reply_on:
                        subscribePreference(postID, postHoldersDeviceToken);
                        break;
                    case R.id.reply_off:
                        unSubscribePreference(postID, postHoldersDeviceToken);
                        break;
                }
                return false;
            }

            @Override
            public void onMenuClosed() {

            }
        });

    }

    private void workSequence(final String postID, final String postHoldersDeviceToken) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(postID).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                sendFCMPush(facebook_user_name, " replied to " + intentName + "\'s post \n" + replyTextFromEditText, "/topics/" + postID);

            }
        });
        FirebaseMessaging.getInstance().unsubscribeFromTopic(postHoldersDeviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (!postHoldersDeviceToken.contains(myID)) {
                    sendFCMPush(facebook_user_name, "replied to your post...\n\n" + replyTextFromEditText, "/topics/" + postHoldersDeviceToken);
                }
            }
        });
    }

    private void unSubscribePreference(final String topic, final String token) {
        if (!token.contains(myID)) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    myToaster("Notification turned off");
                }
            });
        }
        if (token.contains(myID)) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(token).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    myToaster("Notification turned off");
                }
            });
        }

    }

    private void subscribePreference(final String topic, final String token) {
        if (!token.contains(myID)) {
            FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    myToaster("Notification turned on");
                }
            });
        }
        if (token.contains(myID)) {
            FirebaseMessaging.getInstance().subscribeToTopic(token).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    myToaster("Notification turned on");
                }
            });
        }

    }

    private void setValueToMainPost() {
        if (userid.equals(myID) || myID.equals("1335608633238560")) {
            delete_Post.setVisibility(View.VISIBLE);
        } else {
            delete_Post.setVisibility(View.GONE);

        }
        if (userid.equals("1335608633238560")) {
            varified.setVisibility(View.VISIBLE);
        } else {
            varified.setVisibility(View.GONE);

        }
        if (dataconnected()) {
            graphRequest();
        } else {
            alertMessage("Turn on data", "Try again", "Exit");
        }
        reply_post_name.setText(intentName);
        reply_post_text.setText(intentText);
        post_time.setText(time);
        Glide.with(this).load("https://graph.facebook.com/" + userid + "/picture?width=800").into(userImage);
        Glide.with(Reply.this).load("https://graph.facebook.com/" + myID + "/picture?width=800").into(replyImage);
    }

    private void getIntentvalue() {
        userid = getIntent().getExtras().getString("id");
        intentName = getIntent().getExtras().getString("name");
        intentText = getIntent().getExtras().getString("text");
        postID = getIntent().getExtras().getString("postID");
        time = getIntent().getExtras().getString("time");
        postHoldersDeviceToken = getIntent().getExtras().getString("devicetoken");
    }

    private void myToaster(String text) {
        Toast toast = makeText(Reply.this, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void subscribeTopic(String topic) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic);
    }

    private void unSubscribe(final String topic, final String token) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
        FirebaseMessaging.getInstance().unsubscribeFromTopic(token);
    }

    private void postReply() {
        if (edit_reply.getText().toString().length() != 0) {
            replyTextFromEditText = edit_reply.getText().toString();
            reply_post.put("name", facebook_user_name);
            reply_post.put("text", replyTextFromEditText);
            reply_post.put("time", replyDate());
            reply_post.put("myID", myID);
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
        }
    }

    private void initialize() {
        alertBuilder = new AlertDialog.Builder(Reply.this);
        alert = alertBuilder.create();
        alert.setCancelable(false);
        Calligrapher replyFont = new Calligrapher(Reply.this);
        replyFont.setFont(Reply.this, "kalpurush.ttf", true);
        reply_post_name = (TextView) findViewById(R.id.reply_post_name);
        reply_post_text = (TextView) findViewById(R.id.reply_post_text);
        post_time = (TextView) findViewById(R.id.post_time);
        delete_Post = (TextView) findViewById(R.id.delete_post);
        varified = (TextView) findViewById(R.id.varified);
        forum_subscription = (FabSpeedDial) findViewById(R.id.reply_subscription);
        replyName = new ArrayList<>();
        replyTexts = new ArrayList<>();
        replyUserId = new ArrayList<>();
        replyPostId = new ArrayList<>();
        replyTime = new ArrayList<>();
        loginPreference = getSharedPreferences("loggedin", Context.MODE_PRIVATE);
        myIdPreference = getSharedPreferences("myID", Context.MODE_PRIVATE);
        myID = myIdPreference.getString("myID", null);
        postIdPreference = getSharedPreferences("postid", Context.MODE_PRIVATE);
        postIdPreference.edit().putString("postid", postID).apply();
        devicetokenPreference = getSharedPreferences("token", Context.MODE_PRIVATE);
        database = FirebaseDatabase.getInstance();
        replyReference = database.getReference().child("user").child(postID).child("reply");
        reply_post = new HashMap<>();
        edit_reply = (EditText) findViewById(R.id.edit_reply);
        recyclerView = (RecyclerView) findViewById(R.id.reply_forum_recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        replyButton = (ImageView) findViewById(R.id.reply_post);
        adapter = new ReplyAdapter(Reply.this, replyName, replyTexts, replyUserId, replyTime, replyPostId);
        manager = new LinearLayoutManager(Reply.this);
        recyclerView.setLayoutManager(manager);
        userImage = (CircularImageView) findViewById(R.id.reply_user_image);
        replyImage = (CircularImageView) findViewById(R.id.reply_post_image);
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
                    facebook_user_name = object.getString("first_name") + " " + object.getString("last_name");
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

    private String elapsedTime(String postTimeMillis, String genuine_pot_time) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy h:mm a");
        String ago;
        long one_second = 1000;
        long one_minute = one_second * 60;
        long one_hour = one_minute * 60;
        long day = one_hour * 24;
        long post_time = Long.parseLong(postTimeMillis);
        String post_time_DateFormat = sdf.format(post_time).substring(0, 11);
        long current_time = System.currentTimeMillis();
        String current_time_DateFormat = sdf.format(current_time).substring(0, 11);
        long elapse_time = current_time - post_time;

        if (elapse_time < one_second) {
            ago = "jut now";
        } else if (elapse_time >= one_second && elapse_time < one_minute) {
            if (elapse_time / one_second == 1) {
                ago = "1 second";
            } else {
                ago = Long.toString(elapse_time / one_second) + " seconds ago";
            }

        } else if (elapse_time >= one_minute && elapse_time < one_hour) {
            if (elapse_time / one_minute == 1) {
                ago = "1 min";
            } else {
                ago = Long.toString(elapse_time / one_minute) + " minutes ago";
            }

        } else if (elapse_time >= one_hour && elapse_time < day) {
            if (elapse_time / one_hour == 1) {
                ago = "1 hour";
            } else if (elapse_time / one_hour > 1 && post_time_DateFormat.equals(current_time_DateFormat)) {
                ago = Long.toString(elapse_time / one_hour) + " hours ago";
            } else {
                ago = "Yesterday at " + sdf.format(post_time).substring(12);
            }
        } else if (elapse_time > day && elapse_time < day * 2) {
            ago = "Yesterday at " + sdf.format(post_time).substring(12);
        } else {
            ago = genuine_pot_time;
        }
        return ago;

    }

    private void sendFCMPush(String title, String message, final String to) {

        JSONObject obj = null;
        JSONObject objData;
        JSONObject dataobjData;

        try {
            obj = new JSONObject();
            objData = new JSONObject();

            objData.put("body", message);
            objData.put("title", title);
            objData.put("sound", "default");
            objData.put("icon", "icon_name"); //   icon_name
//            objData.put("tag", token);
            JSONObject put = objData.put("priority", "high");

            dataobjData = new JSONObject();
            dataobjData.put("text", message);
            dataobjData.put("title", title);
//            "/topics/"+salary.getText().toString()
            obj.put("to", to);
            //obj.put("priority", "high");

            obj.put("notification", objData);
            obj.put("data", dataobjData);
            Log.e("return here>>", obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, "https://fcm.googleapis.com/fcm/send", obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("True", response + "");
                        if (!postHoldersDeviceToken.contains(myID)) { // if not my post
                            subscribeTopic(postID);
                        } else {
                            subscribeTopic(postHoldersDeviceToken);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("False", error.getMessage());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "key=" + getString(R.string.serverKey));
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        int socketTimeout = 1000 * 60;// 60 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);
        requestQueue.add(jsObjRequest);
    }

    private void loadBlocklist() {
        DatabaseReference blockReference = database.getReference().child("block");
        blockReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(snapshot.getKey().equals(myID)){
                        blocked=snapshot.getKey();
                    }
                    if(blocked!=null){
                        edit_reply.setEnabled(false);
                        AlertDialog.Builder builder=new AlertDialog.Builder(Reply.this);
                        AlertDialog dialog=builder.create();
                        dialog.setMessage("You can not reply to forum post");
                        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        try {
                            dialog.show();
                        }catch (WindowManager.BadTokenException e){}

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
