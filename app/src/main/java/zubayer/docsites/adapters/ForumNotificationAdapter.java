package zubayer.docsites.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

import zubayer.docsites.R;
import zubayer.docsites.activity.Reply;

public class ForumNotificationAdapter extends RecyclerView.Adapter<ForumNotificationAdapter.VHolder> {
    private ArrayList<String> notification_text, notification_time, users_sources, main_postID, seen_unseen,notification_ID;
    private Activity context;
    private Typeface forum_font;
    private FirebaseDatabase database;
    private DatabaseReference notification_Reference;
    private String myID;
    private Animation animation;
    public ForumNotificationAdapter(Activity context,
                                    ArrayList<String> notification_ID,
                                    ArrayList<String> users_sources,
                                    ArrayList<String> notification_text,
                                    ArrayList<String> notification_time,
                                    ArrayList<String> main_postID,
                                    ArrayList<String> seen_unseen) {
        this.users_sources = users_sources;
        this.notification_ID = notification_ID;
        this.notification_text = notification_text;
        this.notification_time = notification_time;
        this.main_postID = main_postID;
        this.seen_unseen = seen_unseen;
        this.context = context;
        animation= AnimationUtils.loadAnimation(context,R.anim.fade_in);
        database = FirebaseDatabase.getInstance();
        forum_font = Typeface.createFromAsset(context.getAssets(), "kalpurush.ttf");
        SharedPreferences myIDpreference = context.getSharedPreferences("myID", Context.MODE_PRIVATE);
        notification_Reference=database.getReference().child("notifications");
        myID= myIDpreference.getString("myID",null);
    }

    @NonNull
    @Override
    public ForumNotificationAdapter.VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = context.getLayoutInflater().inflate(R.layout.forum_notification_layout, parent, false);
        return new ForumNotificationAdapter.VHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ForumNotificationAdapter.VHolder holder, final int position) {
        notification_Reference = database.getReference().child("notifications");
        holder.notifiationText.setText(Html.fromHtml(notification_text.get(position)), TextView.BufferType.SPANNABLE);
        holder.notifiationText.setTypeface(forum_font);
        holder.time.setText(notification_time.get(position));
        holder.time.setTypeface(forum_font);
        if(seen_unseen.get(position).equals("unseen")){
            holder.relativeLayout.setBackgroundColor(Color.parseColor("#ddeef6"));
        }else {
            holder.relativeLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    animateDelete(holder);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            notification_Reference.child(myID).child(notification_ID.get(position)).setValue(null);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                }catch (Exception e){}
            }
        });
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(main_postID.get(position)!=null) {
                    context.startActivity(new Intent(context, Reply.class).putExtra("postID", main_postID.get(position)));
                    notification_Reference.child(myID).child(notification_ID.get(position)).child("seenUnseen").setValue("seen");
                }
            }
        });

        try {
            Glide.with(context).load("https://graph.facebook.com/" + users_sources.get(position) + "/picture?width=800").into(holder.userSource);

        } catch (IndexOutOfBoundsException e) {
        }

    }

    @Override
    public int getItemCount() {
        return users_sources.size();
    }

    public class VHolder extends RecyclerView.ViewHolder {

        TextView notifiationText, time, del;
        CircularImageView userSource;
        RelativeLayout relativeLayout;
        private VHolder(View itemView) {
            super(itemView);
            notifiationText = (TextView) itemView.findViewById(R.id.noti_text);
            time = (TextView) itemView.findViewById(R.id.notify_time);
            del = (TextView) itemView.findViewById(R.id.del_notification);
            userSource = (CircularImageView) itemView.findViewById(R.id.user_source_image);
            relativeLayout=(RelativeLayout)itemView.findViewById(R.id.relativelayout);
        }
    }
    private void animateDelete(VHolder holder) {
        holder.notifiationText.startAnimation(animation);
        holder.time.startAnimation(animation);
        holder.del.startAnimation(animation);
        holder.userSource.startAnimation(animation);
        holder.relativeLayout.startAnimation(animation);
    }

}
