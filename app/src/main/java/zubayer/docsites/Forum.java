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
import android.support.design.internal.NavigationMenu;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import me.anwarshahriar.calligrapher.Calligrapher;

import static android.widget.Toast.makeText;

public class Forum extends Activity {

    AlertDialog alert;
    AlertDialog.Builder alertBuilder;
    CallbackManager callbackManager;
    CircularImageView post_image;
    ImageView send;
    ForumAdapter adapter;
    FabSpeedDial forum_subscription;
    GraphRequest request;
    LinearLayoutManager manager;
    RecyclerView recyclerView;
    ArrayList<String> namess, textss, times, user_id, post_id, get_post_id_serially, comment_count,
            total_comments_each_post, preview_replierID, preview_replierText, preview_replierName, user_device_token;
    EditText edit_text;
    SharedPreferences loginPreference, myIDpreference, notificationPreference;
    FirebaseDatabase database;
    DatabaseReference rootReference, reply_preview_reference, unsubscribeReference, blockReference;
    HashMap<String, Object> post;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    String facebookAuth, facebook_id, facebook_user_name, postID, blocked, reported,
            replierID, replierName, facebook_post_time, snapshotKey, myDeviceToken;
    long postSize, reportSize;

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
                postQuery(currentPostTime());
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
                    case R.id.forum_on:
                        FirebaseMessaging.getInstance().subscribeToTopic("forum").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                myToaster("Notification turned on");
                                notificationPreference.edit().putBoolean("unsubscribed_post_noti", false).apply();
                            }
                        });
                        break;
                    case R.id.forum_off:
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("forum").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                myToaster("Notification turned off");
                                notificationPreference.edit().putBoolean("unsubscribed_post_noti", true).apply();
                            }
                        });
                        break;
                }
                return false;
            }

            @Override
            public void onMenuClosed() {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        adapter.notifyDataSetChanged();
//        loadForumPost();
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
                get_post_id_serially.clear();
                preview_replierID.clear();
                preview_replierText.clear();
                preview_replierName.clear();
                user_device_token.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    post_id.add(0, snapshot.getKey());
                    user_id.add(0, snapshot.child("id").getValue(String.class));
                    namess.add(0, snapshot.child("name").getValue(String.class));
                    textss.add(0, snapshot.child("text").getValue(String.class));
                    times.add(0, elapsedTime(snapshot.getKey(), snapshot.child("time").getValue(String.class)));
                    user_device_token.add(0, snapshot.child("devicetoken").getValue(String.class));
                    replyPreview(snapshot.getKey());
                    perPost_reportCount(snapshot.getKey());
                }

//                Iterator iterator = get_post_id_serially.iterator();
//
//                for (int i = 0; i < get_post_id_serially.size(); i++) {
//                    if (iterator.hasNext()) {
//                        replyPreview(get_post_id_serially.get(i));
//                    }
//                }
                progressDialog.dismiss();
                getSize();
                loadReportedIDlist();
                loadBlocklist();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void perPost_reportCount(final String post_ID) {
        reply_preview_reference = database.getReference().child("user").child(post_ID).child("reportPost");
        reply_preview_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> total_report_each_post = new ArrayList<>();
                final HashMap<String,Object> unsubscribeList=new HashMap<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    total_report_each_post.add(snapshot.getKey());

                }
                if (total_report_each_post.size() != 0) {
                    if (total_report_each_post.size() > 10) {
                        rootReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                unsubscribeList.put(post_ID,"");
                                unsubscribeList.put(dataSnapshot.child(post_ID).child("devicetoken").getValue(String.class),"");
                                try{
                                unsubscribeReference.updateChildren(unsubscribeList);
                                }catch (Exception e){}
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        rootReference.child(post_ID).setValue(null);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void replyPreview(final String post_ID) {
        reply_preview_reference = database.getReference().child("user").child(post_ID).child("reply");
        reply_preview_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                replierID = "blank";
                postID = "blank";
                replierName = "blank";
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshotKey = snapshot.getKey();
                    total_comments_each_post.add(snapshot.getKey());
                    replierID = snapshot.child("myID").getValue(String.class);
                    postID = snapshot.child("text").getValue(String.class);
                    replierName = snapshot.child("name").getValue(String.class);
                }
                if (total_comments_each_post.size() != 0) {
                    if (total_comments_each_post.size() > 1) {
                        comment_count.add(0, Integer.toString(total_comments_each_post.size()) + " comments");
                    } else {
                        comment_count.add(0, Integer.toString(total_comments_each_post.size()) + " comment");
                    }

                } else {
                    comment_count.add(0, "Reply");
                }
                total_comments_each_post.clear();
                preview_replierText.add(0, postID);
                preview_replierName.add(0, replierName);
                preview_replierID.add(0, replierID);
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
                    myToaster(e.getMessage());
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

    private void postQuery(String current_post_time) {
        String post_text = edit_text.getText().toString();
        if (edit_text.getText().toString().length() != 0) {
            post.put("name", facebook_user_name);
            post.put("text", post_text);
            post.put("time", postDate());
            post.put("id", facebook_id);
            myDeviceToken = current_post_time + facebook_id;
            post.put("devicetoken", myDeviceToken);
            rootReference.child(current_post_time).setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    myToaster("Posted succesfully");
                    edit_text.setText(null);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    myToaster("failed");
                }
            });
            workSequence(post_text);

        } else {
            myToaster("Empty text");
        }
    }

    private void initialize() {
        alertBuilder = new AlertDialog.Builder(Forum.this);
        alert = alertBuilder.create();
        Calligrapher replyFont = new Calligrapher(Forum.this);
        forum_subscription = (FabSpeedDial) findViewById(R.id.forum_subscription);
        replyFont.setFont(Forum.this, "kalpurush.ttf", true);
        namess = new ArrayList<>();
        textss = new ArrayList<>();
        times = new ArrayList<>();
        post_id = new ArrayList<>();
        user_id = new ArrayList<>();
        preview_replierID = new ArrayList<>();
        preview_replierText = new ArrayList<>();
        preview_replierName = new ArrayList<>();
        get_post_id_serially = new ArrayList<>();
        user_device_token = new ArrayList<>();
        comment_count = new ArrayList<>();
        total_comments_each_post = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(this);
        loginPreference = getSharedPreferences("loggedin", Context.MODE_PRIVATE);
        myIDpreference = getSharedPreferences("myID", Context.MODE_PRIVATE);
        notificationPreference = getSharedPreferences("forum_notification", Context.MODE_PRIVATE);
        database = FirebaseDatabase.getInstance();
        rootReference = database.getReference().child("user");
        unsubscribeReference = database.getReference().child("unsubscribe");
        post = new HashMap<>();
        edit_text = (EditText) findViewById(R.id.edit_text);
        recyclerView = (RecyclerView) findViewById(R.id.forum_recyclerView);
        send = (ImageView) findViewById(R.id.send);
        adapter = new ForumAdapter(Forum.this, namess, textss, times, user_id,
                post_id, preview_replierText, preview_replierName, preview_replierID, user_device_token, comment_count);
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
        } else if (elapse_time >= day && elapse_time < day * 1.5) {
            ago = "Yesterday at " + sdf.format(post_time).substring(12);
        } else {
            ago = genuine_pot_time;
        }
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

    private void subscribeTopic(String topic) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    private void sendFCMPush(String title, String message) {

        JSONObject obj = null;
        JSONObject objData;
        JSONObject dataobjData;

        try {
            obj = new JSONObject();
            objData = new JSONObject();

            objData.put("body", message);
            objData.put("title", title);
            objData.put("sound", "default");
            objData.put("icon", "ic_launcher_foreground"); //   icon_name
            objData.put("tag", "tg");
//            JSONObject put = objData.put("priority", "high");

            dataobjData = new JSONObject();
            dataobjData.put("text", message);
            dataobjData.put("title", title);
//            "/topics/"+salary.getText().toString()
            obj.put("to", "/topics/forum");
            obj.put("priority", "high");

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
                        if (!notificationPreference.getBoolean("unsubscribed_post_noti", false)) {
                            subscribeTopic("forum");
                        }
                        subscribeTopic(myDeviceToken);
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

    private void workSequence(final String post_text) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic("forum").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                sendFCMPush(facebook_user_name, post_text);
            }
        });
    }

    private void loadBlocklist() {
        blockReference = database.getReference().child("block");
        blockReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals(facebook_id)) {
                        blocked = snapshot.getKey();
                    }
                    if (blocked != null) {
                        edit_text.setEnabled(false);
                        alert.dismiss();
                        alert.setMessage("You are permanently blocked from forum. You still can read forum posts but cannot post anything");
                        alert.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogl, int which) {
                            }
                        });
                        try {

                            alert.show();
                        } catch (WindowManager.BadTokenException e) {
                        }

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void loadReportedIDlist() {
        final DatabaseReference reportReference = database.getReference().child("reportedID");
        reportReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> reporterIDList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals(facebook_id)) {
                        reporterIDList.add(snapshot.getKey());
                        reported = snapshot.getKey();
                    }
                }
                if (reported != null) {
                    autoBlock(reported);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void autoBlock(final String reported) {
        final DatabaseReference autoBlockReference = database.getReference().child("reportID").child(reported);
        autoBlockReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> reporterIDlist = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    reporterIDlist.add(snapshot.getKey());
                }
                if (reporterIDlist.size() > 1) {
                    blockReference = database.getReference().child("block");
                    HashMap<String, Object> autoBlock = new HashMap<>();
                    autoBlock.put(reported, "");
                    blockReference.updateChildren(autoBlock);
                    autoBlockReference.child(reporterIDlist.get(0)).setValue(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getSize() {
        DatabaseReference postReference = database.getReference().child("postSize");

        postReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postSize = dataSnapshot.getValue(Long.class);
                if (post_id.size() > postSize) {
                    HashMap<String, Object> unsubscribeList = new HashMap<>();
                    unsubscribeList.put(post_id.get(post_id.size() - 1), "");
                    unsubscribeList.put(user_device_token.get(user_device_token.size() - 1), "");
                    unsubscribeReference.updateChildren(unsubscribeList).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });
                    rootReference.child(post_id.get(post_id.size() - 1)).setValue(null, null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
