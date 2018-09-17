package zubayer.docsites.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import zubayer.docsites.R;
import zubayer.docsites.activity.Browser;
import zubayer.docsites.activity.Forum;
import zubayer.docsites.activity.ImageViewer;
import zubayer.docsites.activity.Reply;

import static android.widget.Toast.makeText;

public class LollipopAdapter extends RecyclerView.Adapter<LollipopAdapter.VHolder> {
    private ArrayList<String> doc_name, doc_text, post_Time, user_id, postImageUrl, post_id, commentCount;
    private Activity context;
    private Typeface fonts;
    private String myID, myName;
    private Animation animation;
    public LollipopAdapter(Activity context,
                           ArrayList<String> doc_name,
                           ArrayList<String> user_id,
                           ArrayList<String> doc_text,
                           ArrayList<String> post_Time,
                           ArrayList<String> postImageUrl,
                           ArrayList<String> post_id,
                           ArrayList<String> commentCount) {
        this.doc_name = doc_name;
        this.user_id = user_id;
        this.doc_text = doc_text;
        this.post_Time = post_Time;
        this.postImageUrl = postImageUrl;
        this.post_id = post_id;
        this.commentCount = commentCount;
        this.context = context;
        animation= AnimationUtils.loadAnimation(context,R.anim.fade_in);
        fonts = Typeface.createFromAsset(context.getAssets(), "kalpurush.ttf");
        SharedPreferences myIDpreference = context.getSharedPreferences("myID", Context.MODE_PRIVATE);
        myID = myIDpreference.getString("myID", null);
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = context.getLayoutInflater().inflate(R.layout.lollpop_layout, parent, false);
        return new VHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final VHolder holder, final int position) {
        holder.docName.setText(doc_name.get(position));
        holder.docName.setTypeface(fonts);

        if (holder.docText.equals(" ")) {
            holder.docText.setVisibility(View.GONE);
        }
        if (doc_text.get(position).length() > 400) {
            String text = "<font color=#b4b4b4> ...continue reading</font>";
            Scanner sc = new Scanner(doc_text.get(position));
            StringBuilder short_Text = new StringBuilder();
            short_Text.append(sc.nextLine()).append("<br/>")
                    .append(sc.nextLine()).append("<br/>")
                    .append(sc.nextLine()).append(text);
            holder.docText.setText(Html.fromHtml(short_Text.toString()), TextView.BufferType.SPANNABLE);
        } else {
            holder.docText.setText(doc_text.get(position));
        }
        if (doc_text.get(position).length() < 100) {
            holder.docText.setTextSize(27);
        }
        holder.docText.setTypeface(fonts);
        holder.postTime.setText(post_Time.get(position));
        holder.postTime.setTypeface(fonts);
        try {
            holder.reply.setText(commentCount.get(position));
            if (user_id.get(position).equals(myID) || user_id.get(position).equals("1335608633238560")) {
                holder.report.setVisibility(View.GONE);
            } else {
                holder.report.setVisibility(View.VISIBLE);
            }
            if (user_id.get(position).equals("1335608633238560")) {
                holder.varified.setVisibility(View.VISIBLE);
            } else {
                holder.varified.setVisibility(View.GONE);
            }
            if (user_id.get(position).equals(myID)) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference rootReference = database.getReference().child("user");
                final StorageReference storageRef = FirebaseStorage.getInstance().getReference("image/");
                final DatabaseReference imageReference = database.getReference().child("imageReference");
                holder.delete_post.setVisibility(View.VISIBLE);
                holder.delete_post.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog dialog;
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        dialog = builder.create();
                        dialog.setMessage("Delete post?");
                        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                animateDelete(holder);
                                animation.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        rootReference.child(post_id.get(position)).child("reply").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    storageRef.child(snapshot.getKey()).delete();
                                                    imageReference.child(snapshot.getKey()).setValue(null);
                                                }

                                                rootReference.child(post_id.get(position)).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        notifyDataSetChanged();
                                                        myToast("Post deleted");
                                                    }
                                                });

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });


                                        storageRef.child(post_id.get(position)).delete();
                                        imageReference.child(post_id.get(position)).setValue(null);
                                        notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });
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
        try {
            Glide.with(context).load("https://graph.facebook.com/" + user_id.get(position) + "/picture?width=800").into(holder.pic);
            Glide.with(context).load(postImageUrl.get(position)).into(holder.postImage);
            if (postImageUrl.get(position).equals("blank")) {
                holder.progressBar.setVisibility(View.GONE);
            }
        } catch (Exception e) {
        }
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
        holder.report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graphRequest();
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference blockReference = database.getReference().child("user").child(post_id.get(position));
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                AlertDialog dialog = builder.create();
//                dialog.setMessage("Choose preference");
                dialog.setButton(DialogInterface.BUTTON1, "Report Post", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final HashMap<String, Object> reportList = new HashMap<>();
                        reportList.put(myID, myName);
                        blockReference.child("reportPost").setValue(reportList);
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

        holder.reply.setOnClickListener(new View.OnClickListener() {
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
                context.startActivity(new Intent(context, ImageViewer.class).putExtra("showImage", postImageUrl.get(position)));
            }
        });
    }

    private void animateDelete(VHolder holder) {
        holder.relativeLayout.startAnimation(animation);
        holder.cardView.startAnimation(animation);
        holder.docName.startAnimation(animation);
        holder.docText.startAnimation(animation);
        holder.postTime.startAnimation(animation);
        holder.delete_post.startAnimation(animation);
        holder.varified.startAnimation(animation);
        holder.report.startAnimation(animation);
        holder.postImage.startAnimation(animation);
        holder.pic.startAnimation(animation);
        holder.progressBar.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return doc_name.size();
    }

    public class VHolder extends RecyclerView.ViewHolder {
        TextView docName, docText, postTime, delete_post, report, reply, varified;
        CircularImageView pic;
        CardView cardView;
        ImageView postImage;
        ProgressBar progressBar;
        RelativeLayout relativeLayout;

        private VHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.lollipopcardview);
            docName = (TextView) itemView.findViewById(R.id.user_name);
            pic = (CircularImageView) itemView.findViewById(R.id.user_image);
            docText = (TextView) itemView.findViewById(R.id.user_text);
            postTime = (TextView) itemView.findViewById(R.id.time);
            postImage = (ImageView) itemView.findViewById(R.id.postImage);
            progressBar = (ProgressBar) itemView.findViewById(R.id.lollipop_layout_progressbar);
            delete_post = (TextView) itemView.findViewById(R.id.delete_post);
            report = (TextView) itemView.findViewById(R.id.report);
            reply = (TextView) itemView.findViewById(R.id.reply);
            varified = (TextView) itemView.findViewById(R.id.lollipopvarified);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.lollipoop);
        }
    }

    private void intentPutExtra(int position) {
        Intent intent = new Intent(context, Reply.class);
        if (post_id.get(position) != null) {
            intent.putExtra("postID", post_id.get(position));
            context.startActivity(intent);
        }
    }

    private void myToast(String text) {
        Toast toast = makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void browser(String inurl) {
        Intent intent = new Intent(context, Browser.class);
        intent.putExtra("value", inurl);
        context.startActivity(intent);
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
}
