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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import java.util.ArrayList;
import java.util.HashMap;

import static android.widget.Toast.makeText;

public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.VHolder> {
    ArrayList<String> doc_name, doc_text, post_Time, user_id, post_id, reply_preview,reply_preview_name,
            replier_id, user_device_token,commentCount;
    Activity context;
    Typeface forum_font;
    SharedPreferences myIDpreference;
    String myID;
    FirebaseDatabase database;
    DatabaseReference rootReference,unsubscribeReference,blockReference;
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
                        ArrayList<String> commentCount) {
        this.doc_name = doc_name;
        this.doc_text = doc_text;
        this.post_Time = post_Time;
        this.user_id = user_id;
        this.post_id = post_id;
        this.reply_preview = reply_preview;
        this.reply_preview_name = reply_preview_name;
        this.replier_id = replier_id;
        this.commentCount = commentCount;
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
        holder.docName.setText(doc_name.get(position));
        holder.docName.setTypeface(forum_font);
        if(doc_text.get(position).length()>200){
            holder.docText.setText(doc_text.get(position).substring(0,200)+"...."+" continue reading");
        }else {
            holder.docText.setText(doc_text.get(position));

        }

        holder.docText.setTypeface(forum_font);
        holder.comment_count.setText(commentCount.get(position));
        holder.postTime.setText(post_Time.get(position));
        holder.postTime.setTypeface(forum_font);
        try {
            if (reply_preview.get(position).contains("blank")) {
                holder.preview_reply.setVisibility(View.GONE);
                holder.preview_reply_name.setVisibility(View.GONE);
                holder.preview_pic.setVisibility(View.GONE);
                holder.varified_reply.setVisibility(View.GONE);
            } else {
                if(reply_preview.get(position).length()>100){
                    holder.preview_reply.setText(reply_preview.get(position).substring(0,100)+"....");
                }else {
                    holder.preview_reply.setText(reply_preview.get(position));
                }

                holder.preview_reply_name.setText(reply_preview_name.get(position));
                holder.preview_reply.setTypeface(forum_font);
                holder.preview_reply_name.setTypeface(forum_font);
                if(!replier_id.get(position).equals("1335608633238560")){
                    holder.varified_reply.setVisibility(View.GONE);
                }
                Glide.with(context).load("https://graph.facebook.com/" + replier_id.get(position) + "/picture?width=800").into(holder.preview_pic);
            }
        } catch (IndexOutOfBoundsException e) {
        }

        Glide.with(context).load("https://graph.facebook.com/" + user_id.get(position) + "/picture?width=800").into(holder.pic);

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
        holder.block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final HashMap<String, Object> blockList=new HashMap<>();
                blockList.put(user_id.get(position),"");
                blockReference = database.getReference().child("block");
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                AlertDialog dialog=builder.create();
                dialog.setMessage("Block "+doc_name.get(position)+" ?");
                dialog.setButton(DialogInterface.BUTTON1, "Block", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        blockReference.updateChildren(blockList).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                    }
                });
                dialog.setButton(DialogInterface.BUTTON3, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                try {
                    dialog.show();
                }catch (WindowManager.BadTokenException e){}

            }
        });
        holder.report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blockReference = database.getReference().child("user").child(post_id.get(position));
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                AlertDialog dialog=builder.create();
                dialog.setMessage("Choose preference");
                dialog.setButton(DialogInterface.BUTTON1, "Report Post", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final HashMap<String, Object> reportList=new HashMap<>();
                        reportList.put(user_id.get(position),"");
                        blockReference.child("reportPost").setValue(reportList);
                    }
                });
                dialog.setButton(DialogInterface.BUTTON3, "Report ID", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final HashMap<String, Object> reportList=new HashMap<>();
                        reportList.put(myID,"");
                        DatabaseReference reportIDreference = database.getReference().child("reportID");
                        reportIDreference.child(user_id.get(position)).updateChildren(reportList);
                    }
                });
                try {
                    dialog.show();
                }catch (WindowManager.BadTokenException e){}

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
            if(user_id.get(position).equals("1335608633238560")){
                holder.varified.setVisibility(View.VISIBLE);
            }else {
                holder.varified.setVisibility(View.GONE);
            }
            if(myID.equals("1335608633238560")){
                if(user_id.get(position).equals("1335608633238560")) {
                    holder.block.setVisibility(View.GONE);
                }else {
                    holder.block.setVisibility(View.VISIBLE);
                }
            }else {
                holder.block.setVisibility(View.GONE);
            }
            if(user_id.get(position).equals(myID)){
                holder.report.setVisibility(View.GONE);
            }else {
                holder.report.setVisibility(View.VISIBLE);
            }

            if (user_id.get(position).equals(myID) || myID.equals("1335608633238560")) {
                rootReference = database.getReference().child("user");
                unsubscribeReference = database.getReference().child("unsubscribe");
                holder.delete_post.setVisibility(View.VISIBLE);
                holder.delete_post.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog = builder.create();
                        dialog.setMessage("Delete post?");
                        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                unsubscribe(post_id.get(position),user_device_token.get(position));
                                HashMap<String, Object> unsubscribeList=new HashMap<>();
                                unsubscribeList.put(post_id.get(position),post_id.get(position));
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
        }catch (Exception e){
            context.startActivity(new Intent(context,Forum.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
    }

    private void unsubscribe(final String topic, final String token) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
        FirebaseMessaging.getInstance().unsubscribeFromTopic(token);
    }

    private void intentPutExtra(int position) {
        Intent intent = new Intent(context, Reply.class);
        intent.putExtra("id", user_id.get(position));
        intent.putExtra("name", doc_name.get(position));
        intent.putExtra("text", doc_text.get(position));
        intent.putExtra("time", post_Time.get(position));
        intent.putExtra("postID", post_id.get(position));
        intent.putExtra("devicetoken", user_device_token.get(position));
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return doc_name.size();
    }

    public class VHolder extends RecyclerView.ViewHolder {

        TextView docName, docText, postTime, preview_reply, delete_post,varified,varified_reply,
                preview_reply_name,block,comment_count,report;
        CircularImageView pic, preview_pic;
        CardView cardView;

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
            varified_reply= (TextView) itemView.findViewById(R.id.varified_reply);
            block= (TextView) itemView.findViewById(R.id.block);
            comment_count= (TextView) itemView.findViewById(R.id.comment_count);
            report= (TextView) itemView.findViewById(R.id.report);
            pic = (CircularImageView) itemView.findViewById(R.id.user_image);
            preview_pic = (CircularImageView) itemView.findViewById(R.id.reply_preview_image);

        }
    }

    private void myToast(String text) {
        Toast toast = makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }

}

