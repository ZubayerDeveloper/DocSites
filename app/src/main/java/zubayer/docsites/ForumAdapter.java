package zubayer.docsites;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
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
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.widget.Toast.makeText;

public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.VHolder> {
    ArrayList<String> doc_name, doc_text, post_Time, user_id, post_id, reply_preview, reply_preview_name,
            replier_id, user_device_token, commentCount, postImageUrl, replyImageUrl;
    Activity context;
    Typeface forum_font;
    SharedPreferences myIDpreference;
    String myID, myName;
    FirebaseDatabase database;
    DatabaseReference rootReference, unsubscribeReference, blockReference, imageReference;
    StorageReference storageRef;
    AlertDialog dialog;
    AlertDialog.Builder builder;

    public ForumAdapter(Activity context,
                        ArrayList<String> doc_name,
                        ArrayList<String> doc_text,
                        ArrayList<String> post_Time,
                        ArrayList<String> user_id,
                        ArrayList<String> post_id,
                        ArrayList<String> reply_preview,
                        ArrayList<String> reply_preview_name,
                        ArrayList<String> replier_id,
                        ArrayList<String> user_device_token,
                        ArrayList<String> commentCount,
                        ArrayList<String> postImageUrl,
                        ArrayList<String> replyImageUrl) {
        this.doc_name = doc_name;
        this.doc_text = doc_text;
        this.post_Time = post_Time;
        this.user_id = user_id;
        this.post_id = post_id;
        this.reply_preview = reply_preview;
        this.reply_preview_name = reply_preview_name;
        this.replier_id = replier_id;
        this.commentCount = commentCount;
        this.postImageUrl = postImageUrl;
        this.replyImageUrl = replyImageUrl;
        this.user_device_token = user_device_token;

        this.context = context;

        forum_font = Typeface.createFromAsset(context.getAssets(), "kalpurush.ttf");
        myIDpreference = context.getSharedPreferences("myID", Context.MODE_PRIVATE);
        myID = myIDpreference.getString("myID", null);

        database = FirebaseDatabase.getInstance();
        builder = new AlertDialog.Builder(context);

    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = context.getLayoutInflater().inflate(R.layout.forum_layout, parent, false);
        return new VHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final VHolder holder, final int position) {
//        try {
        holder.docName.setText(doc_name.get(position));
        holder.docName.setTypeface(forum_font);
        if (holder.docText.equals(" ")) {
            holder.docText.setVisibility(View.GONE);
        }
        if (doc_text.get(position).length() > 200) {
            holder.docText.setText(doc_text.get(position).substring(0, 200) + "...." + " continue reading");
        } else {
            holder.docText.setText(doc_text.get(position));

        }
        holder.docText.setTypeface(forum_font);
        try {
            holder.comment_count.setText(commentCount.get(position));
            if (reply_preview.get(position).equals("blank")) {
                holder.preview_reply.setVisibility(View.GONE);
                holder.preview_reply_name.setVisibility(View.GONE);
                holder.preview_pic.setVisibility(View.GONE);
                holder.varified_reply.setVisibility(View.GONE);
            } else {
                if (reply_preview.get(position).length() > 100) {
                    holder.preview_reply.setText(reply_preview.get(position).substring(0, 100) + "....");
                } else if (holder.preview_reply.equals(" ")) {
                    holder.preview_reply.setVisibility(View.GONE);
                } else {
                    holder.preview_reply.setText(reply_preview.get(position));
                }

                holder.preview_reply_name.setText(reply_preview_name.get(position));
                holder.preview_reply.setTypeface(forum_font);
                holder.preview_reply_name.setTypeface(forum_font);
                if (!replier_id.get(position).equals("1335608633238560")) {
                    holder.varified_reply.setVisibility(View.GONE);
                }
                Glide.with(context).load("https://graph.facebook.com/" + replier_id.get(position) + "/picture?width=800").into(holder.preview_pic);
            }
        } catch (IndexOutOfBoundsException e) {
        }
        holder.postTime.setText(post_Time.get(position));
        holder.postTime.setTypeface(forum_font);

//        } catch (IndexOutOfBoundsException e) {
//            context.startActivity(new Intent(context, Forum.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//        }
        try {
            Glide.with(context).load("https://graph.facebook.com/" + user_id.get(position) + "/picture?width=800").into(holder.pic);

            Glide.with(context).load(postImageUrl.get(position)).into(holder.postImage);
            if (postImageUrl.get(position).equals("blank")) {
                holder.progressBar.setVisibility(View.GONE);
            }
            if (!replyImageUrl.get(position).equals("blank")) {
                Glide.with(context).load(replyImageUrl.get(position)).into(holder.replyImage);
                holder.replyImage.setVisibility(View.VISIBLE);

            } else {
                holder.replyImage.setVisibility(View.GONE);
            }
        } catch (IndexOutOfBoundsException e) {
        } catch (NullPointerException e) {
        }
        holder.preview_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentPutExtra(position);
            }
        });

        holder.preview_reply_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentPutExtra(position);
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentPutExtra(position);
            }
        });
        holder.docText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentPutExtra(position);
            }
        });
        holder.postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browser(postImageUrl.get(position));
            }
        });

        holder.block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final HashMap<String, Object> blockList = new HashMap<>();
                blockList.put(user_id.get(position), doc_name.get(position));
                blockReference = database.getReference().child("block");
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                AlertDialog dialog = builder.create();
                dialog.setMessage("Block " + doc_name.get(position) + " ?");
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Block", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        blockReference.updateChildren(blockList).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                    }
                });
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                try {
                    dialog.show();
                } catch (WindowManager.BadTokenException e) {
                }

            }
        });
        holder.report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graphRequest();
                blockReference = database.getReference().child("user").child(post_id.get(position));
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                AlertDialog dialog = builder.create();
//                dialog.setMessage("Choose preference");
                dialog.setButton(DialogInterface.BUTTON1, "Report Post", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final HashMap<String, Object> reportList = new HashMap<>();
                        reportList.put(myID, myName);
                        blockReference.child("reportPost").updateChildren(reportList);
                    }
                });
                dialog.setButton(DialogInterface.BUTTON3, "Report ID", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final HashMap<String, Object> reportList = new HashMap<>();
                        reportList.put(myID, myName);
                        DatabaseReference reportIDreference = database.getReference().child("reportedID");
                        reportIDreference.child(user_id.get(position)).updateChildren(reportList);
                    }
                });
                try {
                    dialog.show();
                } catch (WindowManager.BadTokenException e) {
                }
            }
        });
        holder.docText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", doc_text.get(position));
                if (clipboard != null) {
                    clipboard.setPrimaryClip(clip);
                    Toast toast = makeText(context, "Text copied", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                return true;
            }
        });

        try {
            if (user_id.get(position).equals("1335608633238560")) {
                holder.varified.setVisibility(View.VISIBLE);
            } else {
                holder.varified.setVisibility(View.GONE);
            }
            if (myID.equals("1335608633238560")) {
                if (user_id.get(position).equals("1335608633238560")) {
                    holder.block.setVisibility(View.GONE);
                } else {
                    holder.block.setVisibility(View.VISIBLE);
                }
            } else {
                holder.block.setVisibility(View.GONE);
            }
            if (user_id.get(position).equals(myID) || user_id.get(position).equals("1335608633238560")) {
                holder.report.setVisibility(View.GONE);
            } else {
                holder.report.setVisibility(View.VISIBLE);
            }

            if (user_id.get(position).equals(myID) || myID.equals("1335608633238560")) {
                rootReference = database.getReference().child("user");
                unsubscribeReference = database.getReference().child("unsubscribe");
                storageRef = FirebaseStorage.getInstance().getReference("image/");
                imageReference = database.getReference().child("imageReference");
                holder.delete_post.setVisibility(View.VISIBLE);
                holder.delete_post.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog = builder.create();
                        dialog.setMessage("Delete post?");
                        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //first unsubscribe yourself
                                unsubscribe(post_id.get(position), user_device_token.get(position));

                                HashMap<String, Object> unsubscribeList = new HashMap<>();
                                unsubscribeList.put(post_id.get(position), "");
                                if (myID.equals("1335608633238560")) {
                                    // for admin delete, to provide other's device token unsubscription
                                    unsubscribeList.put(user_device_token.get(position), "");
                                }

                                unsubscribeReference.updateChildren(unsubscribeList).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                    }
                                });
                                rootReference.child(post_id.get(position)).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        notifyDataSetChanged();
                                        myToast("Post deleted");
                                    }
                                });

                                storageRef.child(post_id.get(position)).delete();
                                imageReference.child(post_id.get(position)).setValue(null);

                            }
                        });
                        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        dialog.show();
                    }
                });
            } else {
                holder.delete_post.setVisibility(View.GONE);

            }
        } catch (Exception e) {
            context.startActivity(new Intent(context, Forum.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
    }

    private void unsubscribe(final String topic, final String token) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
        FirebaseMessaging.getInstance().unsubscribeFromTopic(token);
    }

    private void intentPutExtra(int position) {
        Intent intent = new Intent(context, Reply.class);
        intent.putExtra("postID", post_id.get(position));
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return doc_name.size();
    }

    public class VHolder extends RecyclerView.ViewHolder {

        TextView docName, docText, postTime, preview_reply, delete_post, varified, varified_reply,
                preview_reply_name, block, comment_count, report;
        CircularImageView pic, preview_pic;
        CardView cardView;
        ImageView postImage, replyImage;
        ProgressBar progressBar;

        private VHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
            docName = (TextView) itemView.findViewById(R.id.user_name);
            docText = (TextView) itemView.findViewById(R.id.user_text);
            postTime = (TextView) itemView.findViewById(R.id.time);
            delete_post = (TextView) itemView.findViewById(R.id.delete_post);
            preview_reply = (TextView) itemView.findViewById(R.id.reply_preview_text);
            preview_reply_name = (TextView) itemView.findViewById(R.id.reply_preview_name);
            varified = (TextView) itemView.findViewById(R.id.varified);
            varified_reply = (TextView) itemView.findViewById(R.id.varified_reply);
            block = (TextView) itemView.findViewById(R.id.block);
            comment_count = (TextView) itemView.findViewById(R.id.comment_count);
            report = (TextView) itemView.findViewById(R.id.report);
            postImage = (ImageView) itemView.findViewById(R.id.postImage);
            replyImage = (ImageView) itemView.findViewById(R.id.replyImage);
            pic = (CircularImageView) itemView.findViewById(R.id.user_image);
            preview_pic = (CircularImageView) itemView.findViewById(R.id.reply_preview_image);
            progressBar = (ProgressBar) itemView.findViewById(R.id.forum_layout_progressbar);
        }
    }

    private void myToast(String text) {
        Toast toast = makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }

    private void graphRequest() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(final JSONObject object, GraphResponse response) {

                try {
                    myName = object.getString("first_name") + " " + object.getString("last_name");
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

    public void browser(String inurl) {
        Intent intent = new Intent(context, Browser.class);
        intent.putExtra("value", inurl);
        context.startActivity(intent);
    }
}

