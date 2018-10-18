package zubayer.docsites.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import me.anwarshahriar.calligrapher.Calligrapher;
import zubayer.docsites.R;
import zubayer.docsites.adapters.ReplyAdapter;

import static android.widget.Toast.makeText;

public class Reply extends Activity {
    AlertDialog alert;
    AlertDialog.Builder alertBuilder;
    CardView chooser_cardview;
    CallbackManager callbackManager;
    CircularImageView userImage, replyImage;
    ReplyAdapter adapter;
    LinearLayoutManager manager;
    RecyclerView recyclerView;
    ArrayList<String> replyName, replyTexts, replyUserId, replyPostId, replyTime, reply_imageURL,
            notifyMe;
    EditText edit_reply;
    SharedPreferences loginPreference, myIdPreference, postIdPreference, devicetokenPreference;
    FirebaseDatabase database;
    DatabaseReference replyReference, deleteReference, rootReference, imageReference;
    StorageReference storageRef;
    HashMap<String, Object> reply_post;
    String userid, intentName, intentText, time, postID, reply_image_name, blocked, postImage_url,
            facebook_user_name, myID, notifyPostOwner, reply_texts, first_name;
    ProgressDialog progressDialog;
    ProgressBar replyProgressbar;
    ImageView replyButton, postImage, pic_preview, notify, notifyOff;
    TextView reply_post_name, reply_post_text, post_time, delete_Post, varified, imageChooser, del_chooser;
    Uri imageUri;
    boolean commented = false;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            chooser_cardview.setVisibility(View.VISIBLE);
            Glide.with(this).load(imageUri).into(pic_preview);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        getIntentvalue();
        initialize();
        setValueToMainPost();
        loadBlocklist();
        replyReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                replyName.clear();
                replyTexts.clear();
                replyUserId.clear();
                replyPostId.clear();
                replyTime.clear();
                notifyMe.clear();
                reply_imageURL.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    replyPostId.add(snapshot.getKey());
                    replyName.add(snapshot.child("name").getValue(String.class));
                    replyTexts.add(snapshot.child("text").getValue(String.class));
                    reply_imageURL.add(snapshot.child("imageUrl").getValue(String.class));
                    replyTime.add(elapsedTime(snapshot.getKey(), snapshot.child("time").getValue(String.class)));
                    replyUserId.add(snapshot.child("myID").getValue(String.class));
                    notifyMe.add(snapshot.child("notifyMe").getValue(String.class));
                }
                try {
                    replyProgressbar.setVisibility(View.GONE);
                } catch (Exception e) {
                }
                notifyStatus();
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        reply_post_text.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) Reply.this.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", reply_post_text.getText().toString());
                if (clipboard != null) {
                    clipboard.setPrimaryClip(clip);
                    Toast toast = makeText(Reply.this, "Text copied", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                return true;
            }
        });
        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reply_image_name = replyTime();
                if (imageUri != null) {
                    upload(reply_image_name);
                    HashMap<String, Object> imageref = new HashMap<>();
                    imageref.put(reply_image_name, "");
                    imageReference.updateChildren(imageref);
                } else {
                    reply_post.put("imageUrl", "blank");
                    if (edit_reply.getText().toString().length() != 0) {

                        reply_texts = edit_reply.getText().toString();
                        reply_post.put("text", reply_texts);
                        postReply(reply_image_name);
                    }

                }
                chooser_cardview.setVisibility(View.GONE);
            }

        });
        imageChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        del_chooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUri = null;
                chooser_cardview.setVisibility(View.GONE);
            }
        });
        delete_Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.setMessage("Delete post?");
                alert.setCancelable(true);
                alert.setButton(DialogInterface.BUTTON_POSITIVE, "Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteReference = database.getReference().child("user");
                        replyReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    storageRef.child(snapshot.getKey()).delete();
                                    imageReference.child(snapshot.getKey()).setValue(null);
                                }
                                storageRef.child(postID).delete();
                                imageReference.child(postID).setValue(null);
                                deleteReference.child(postID).setValue(null);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

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
        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Reply.this, ImageViewer.class).putExtra("showImage", postImage_url));
            }
        });

        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnOffNotification();
                notifyStatus();
            }
        });
        notifyOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnOnNotification();
                notifyStatus();
            }
        });

    }

    private void turnOffNotification() {
        HashMap<String, Object> notifyme = new HashMap<>();
        notifyme.put("notifyMe", "no");
        if (userid.equals(myID)) {
            rootReference.updateChildren(notifyme);
            myToaster("Notification turned OFF for this post");
        } else {
            for (int i = 0; i < replyUserId.size(); i++) {
                if (replyUserId.get(i).equals(myID)) {
                    HashMap<String, Object> yesno = new HashMap<>();
                    yesno.put("notifyMe", "no");
                    replyReference.child(replyPostId.get(i)).updateChildren(yesno);
                }
                if (i == replyUserId.size() - 1) {
                    myToaster("Notification turned OFF for this post");
                }
            }
        }
    }

    private void turnOnNotification() {

        HashMap<String, Object> notifyme = new HashMap<>();
        notifyme.put("notifyMe", "yes");
        if (userid.equals(myID)) {
            rootReference.updateChildren(notifyme);
            myToaster("Notification turned ON for this post");
        } else {
            for (int i = 0; i < replyUserId.size(); i++) {
                if (replyUserId.get(i).equals(myID)) {
                    HashMap<String, Object> yesno = new HashMap<>();
                    yesno.put("notifyMe", "yes");
                    replyReference.child(replyPostId.get(i)).updateChildren(yesno);
                    if (i == replyUserId.size() - 1) {
                        myToaster("Notification turned ON for this post");
                    }
                    commented = true;
                }
            }
            if (!commented) {
                myToaster("Comment something to turn ON notification");
            }
        }
    }

    private void notifyStatus() {
        try {
            if (userid.equals(myID)) {
                rootReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("notifyMe").getValue(String.class) != null) {
                            if (dataSnapshot.child("notifyMe").getValue(String.class).equals("yes")) {
                                notify.setVisibility(View.VISIBLE);
                                notifyOff.setVisibility(View.GONE);
                            } else {
                                notify.setVisibility(View.GONE);
                                notifyOff.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } else if (!replyUserId.isEmpty()) {
                for (int i = replyUserId.size() - 1; i > -1; i--) {
                    if (replyUserId.get(i).equals(myID)) {
                        replyReference.child(replyPostId.get(i)).child("notifyMe").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                try {
                                    if (dataSnapshot.getValue(String.class).equals("yes")) {
                                        notify.setVisibility(View.VISIBLE);
                                        notifyOff.setVisibility(View.GONE);
                                    } else {
                                        notify.setVisibility(View.GONE);
                                        notifyOff.setVisibility(View.VISIBLE);
                                    }
                                } catch (Exception e) {
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        break;
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    private void setValueToMainPost() {

        rootReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                intentName = dataSnapshot.child("name").getValue(String.class);
                intentText = dataSnapshot.child("text").getValue(String.class);
                userid = dataSnapshot.child("id").getValue(String.class);
                time = dataSnapshot.child("time").getValue(String.class);
                notifyPostOwner = dataSnapshot.child("notifyMe").getValue(String.class);
                postImage_url = dataSnapshot.child("imageUrl").getValue(String.class);

                if (userid != null && myID != null) {

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
                    reply_post_name.setText(intentName);
                    reply_post_text.setText(intentText);
                    String posttime = elapsedTime(postID, time);
                    post_time.setText(posttime);
                    try {
                        Glide.with(Reply.this).load("https://graph.facebook.com/" + userid + "/picture?width=800").into(userImage);
                        Glide.with(Reply.this).load("https://graph.facebook.com/" + myID + "/picture?width=800").into(replyImage);
                        Glide.with(Reply.this).load(postImage_url).into(postImage);
                    } catch (Exception e) {
                    }
                } else {
//                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void getIntentvalue() {
        postID = getIntent().getExtras().getString("postID");
    }

    private void myToaster(String text) {
        Toast toast = makeText(Reply.this, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void postReply(final String reply_image_name) {
        reply_post.put("name", facebook_user_name);
        reply_post.put("time", replyDate());
        reply_post.put("myID", myID);
        reply_post.put("notifyMe", "yes");
        replyReference.child(reply_image_name).setValue(reply_post).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myToaster("Reply failed");
            }
        });

        final DatabaseReference notificationReference = database.getReference().child("notifications");
        HashMap<String, Object> noti = new HashMap<>();
        if (facebook_user_name != null && !userid.equals(myID) && notifyPostOwner.equals("yes")) {
            //notify post holder except for me
            noti.put("notificationText", "<font color=#990000>" + facebook_user_name + "</font> commented on your post");
            noti.put("mainPostID", postID);
            noti.put("time", replyDate());
            noti.put("myid", myID);
            noti.put("seenUnseen", "unseen");
            notificationReference.child(userid).child(reply_image_name).updateChildren(noti);
            notificationReference.child(userid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<String> notificatinSize = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        notificatinSize.add(snapshot.getKey());
                    }
                    if (notificatinSize.size() > 10) {
                        notificationReference.child(userid).child(notificatinSize.get(0)).setValue(null);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            sendFCMPush(facebook_user_name, "commented on your post...\n\n" + reply_texts, "/topics/" + userid, postID);

        }
        //turn off my notification from reply to avoid same multiple notification to every my comment
        for (int position = 0; position < replyUserId.size(); position++) {
            if (replyUserId.get(position).equals(myID)) {
                HashMap<String, Object> yesno = new HashMap<>();
                yesno.put("notifyMe", "no");
                replyReference.child(replyPostId.get(position)).updateChildren(yesno);
            }
        }
        for (int i = replyUserId.size() - 1; i > -1; i--) {
            //notify all repliers
            if (facebook_user_name != null && !replyUserId.get(i).equals(myID) && !replyUserId.get(i).equals(userid) && notifyMe.get(i).equals("yes")) {
                noti.put("notificationText", "<font color=#000099>" + facebook_user_name + "</font> commented on " + "<font color=#000099>" + intentName + "</font>\'s post");
                noti.put("mainPostID", postID);
                noti.put("time", replyDate());
                noti.put("myid", myID);
                noti.put("seenUnseen", "unseen");

                final int finalI = i;
                notificationReference.child(replyUserId.get(i)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<String> notificatinSize = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            notificatinSize.add(snapshot.getKey());
                        }
                        if (notificatinSize.size() > 10) {
                            notificationReference.child(replyUserId.get(finalI)).child(notificatinSize.get(0)).setValue(null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                notificationReference.child(replyUserId.get(i)).child(reply_image_name).updateChildren(noti);
                sendFCMPush(facebook_user_name, " commented on " + intentName + "\'s post \n" + reply_texts, "/topics/" + replyUserId.get(i), postID);
            }
        }
        edit_reply.setText(null);
    }

    private void initialize() {
        callbackManager = CallbackManager.Factory.create();
        alertBuilder = new AlertDialog.Builder(Reply.this);
        alert = alertBuilder.create();
        alert.setCancelable(false);
        Calligrapher replyFont = new Calligrapher(Reply.this);
        replyFont.setFont(Reply.this, "kalpurush.ttf", true);
        imageChooser = (TextView) findViewById(R.id.imageChooser);
        del_chooser = (TextView) findViewById(R.id.del_chooser);
        chooser_cardview = (CardView) findViewById(R.id.chooser_cardview);
        chooser_cardview.setVisibility(View.GONE);
        reply_post_name = (TextView) findViewById(R.id.reply_post_name);
        reply_post_text = (TextView) findViewById(R.id.reply_post_text);
        post_time = (TextView) findViewById(R.id.post_time);
        delete_Post = (TextView) findViewById(R.id.delete_post);
        varified = (TextView) findViewById(R.id.varified);
        notify = (ImageView) findViewById(R.id.notify);
        notifyOff = (ImageView) findViewById(R.id.notifyOff);
        replyProgressbar = (ProgressBar) findViewById(R.id.replyProgressbar);
        replyProgressbar.setVisibility(View.VISIBLE);
        replyName = new ArrayList<>();
        replyTexts = new ArrayList<>();
        replyUserId = new ArrayList<>();
        replyPostId = new ArrayList<>();
        replyTime = new ArrayList<>();
        reply_imageURL = new ArrayList<>();
        notifyMe = new ArrayList<>();
        loginPreference = getSharedPreferences("loggedin", Context.MODE_PRIVATE);
        myIdPreference = getSharedPreferences("myID", Context.MODE_PRIVATE);
        myID = myIdPreference.getString("myID", null);
        postIdPreference = getSharedPreferences("postid", Context.MODE_PRIVATE);
        postIdPreference.edit().putString("postid", postID).apply();
        devicetokenPreference = getSharedPreferences("token", Context.MODE_PRIVATE);
        database = FirebaseDatabase.getInstance();
        if(postID!=null){rootReference = database.getReference().child("user").child(postID);}
        replyReference = database.getReference().child("user").child(postID).child("reply");
        imageReference = database.getReference().child("imageReference");
        storageRef = FirebaseStorage.getInstance().getReference("image/");
        reply_post = new HashMap<>();
        edit_reply = (EditText) findViewById(R.id.edit_reply);
        recyclerView = (RecyclerView) findViewById(R.id.reply_forum_recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        replyButton = (ImageView) findViewById(R.id.reply_post);
        postImage = (ImageView) findViewById(R.id.postImage);
        pic_preview = (ImageView) findViewById(R.id.pic_preview);
        adapter = new ReplyAdapter(Reply.this, replyName, replyTexts, replyUserId, replyTime, replyPostId, reply_imageURL);
        manager = new LinearLayoutManager(Reply.this);
        recyclerView.setLayoutManager(manager);
        userImage = (CircularImageView) findViewById(R.id.reply_user_image);
        replyImage = (CircularImageView) findViewById(R.id.reply_post_image);
        SharedPreferences user_name_preference = getSharedPreferences("myname", Context.MODE_PRIVATE);
        facebook_user_name = user_name_preference.getString("myname", null);
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (edit_reply.getText().toString().length() != 0 || imageUri != null) {
            alert.setMessage("Discard reply?");
            alert.setButton(DialogInterface.BUTTON_POSITIVE, "Discard", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alert.setButton(DialogInterface.BUTTON_NEGATIVE, "Keep", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alert.show();
        } else {
            super.onBackPressed();

        }
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

    private void sendFCMPush(String title, String message, final String to, final String data) {

        JSONObject obj = null;
        JSONObject objData;
        JSONObject dataobjData;

        try {
            obj = new JSONObject();
            objData = new JSONObject();

            objData.put("body", message);
            objData.put("title", title);
            objData.put("sound", "default");
            objData.put("icon", "icon_name");

            //   icon_name
//            objData.put("tag", token);
//            JSONObject put = objData.put("priority", "high");

            dataobjData = new JSONObject();
            dataobjData.put("postID", data);
            dataobjData.put("intent", "back");
//            "/topics/"+salary.getText().toString()
            obj.put("to", to);
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
                    if (snapshot.getKey().equals(myID)) {
                        blocked = snapshot.getKey();
                    }
                    if (blocked != null) {
                        edit_reply.setEnabled(false);
                        alert.dismiss();
                        alert = alertBuilder.create();
                        alert.setMessage("You can not reply to forum post");
                        alert.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
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

    public void browser(String inurl) {
        Intent intent = new Intent(Reply.this, Browser.class);
        intent.putExtra("value", inurl);
        startActivity(intent);
    }

    private void upload(final String reply_image_name) {
        progressDialog = ProgressDialog.show(Reply.this, "", "uploading...", true, true);
        final StorageReference storageRef = FirebaseStorage.getInstance().getReference("image/");
        storageRef.child(reply_image_name).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                storageRef.child(reply_image_name).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadUrl) {
                        reply_post.put("imageUrl", downloadUrl.toString());
                        if (edit_reply.getText().toString().length() != 0) {
                            reply_texts = edit_reply.getText().toString();
                            reply_post.put("text", reply_texts);
                        } else {
                            reply_texts = " ";
                            reply_post.put("text", reply_texts);
                        }
                        postReply(reply_image_name);
                        progressDialog.dismiss();
                        imageUri = null;
                    }
                });


            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                alert.setMessage("Upload failed");
                alert.setButton(DialogInterface.BUTTON_POSITIVE, "ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                long progress = taskSnapshot.getBytesTransferred();
                progressDialog.dismiss();
                progressDialog = ProgressDialog.show(Reply.this, "", "uploading..." + (int) progress * 100 / taskSnapshot.getTotalByteCount() + "%", true, true);

            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 11);
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
        alert.setButton(DialogInterface.BUTTON_POSITIVE, positiveButtonName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Reply.this, Forum.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        alert.setButton(DialogInterface.BUTTON_NEGATIVE, negativeButtonName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alert.show();
    }

    private void graphRequest() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(final JSONObject object, GraphResponse response) {

                try {
                    myID = object.getString("id");
                    subscribeTopic(myID);
                    myIdPreference.edit().putString("myID", myID).apply();
                    if(object!=null){
                        facebook_user_name = object.getString("first_name") + " " + object.getString("last_name");
                        myIdPreference.edit().putString("myName", facebook_user_name).apply();
                        SharedPreferences user_name_preference = getSharedPreferences("myname", Context.MODE_PRIVATE);
                        user_name_preference.edit().putString("myname", facebook_user_name).apply();
                        first_name = object.getString("first_name");
                    }else {
                        startActivity(new Intent(Reply.this,Forum.class));
                    }

                    Glide.with(Reply.this).load("https://graph.facebook.com/" + myID + "/picture?width=800").into(replyImage);
                } catch (JSONException e) {
                    myToaster(e.getMessage());
                }
                setValueToMainPost();
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void subscribeTopic(String topic) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

}
