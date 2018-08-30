package zubayer.docsites.adapters;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.HashMap;

import zubayer.docsites.R;
import zubayer.docsites.activity.Browser;
import zubayer.docsites.activity.ImageViewer;

import static android.widget.Toast.makeText;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.VHolder> {
    ArrayList<String> reply_doc_name,reply_doc_text,reply_user_id, post_reply_time,reply_id,repy_image_url;
    Activity context;
    Typeface forum_font;
    FirebaseDatabase database;
    DatabaseReference delete_reply_Reference,imageReference;
    SharedPreferences postIdPreference,myIDpreference;
    StorageReference storageRef;
    HashMap<String,Object>test;
    String postID,myID;
    public ReplyAdapter(Activity context,
                        ArrayList<String> reply_doc_name,
                        ArrayList<String> reply_doc_text,
                        ArrayList<String> reply_user_id,
                        ArrayList<String> post_reply_time,
                        ArrayList<String> reply_id,
                        ArrayList<String> repy_image_url) {
        this.reply_doc_name = reply_doc_name;
        this.reply_doc_text = reply_doc_text;
        this.reply_user_id=reply_user_id;
        this.post_reply_time =post_reply_time;
        this.reply_id=reply_id;
        this.repy_image_url=repy_image_url;
        this.context=context;
        database= FirebaseDatabase.getInstance();
        postIdPreference=context.getSharedPreferences("postid",Context.MODE_PRIVATE);
        postID=postIdPreference.getString("postid",null);
        test=new HashMap<>();
        test.put("test","texr");
        forum_font= Typeface.createFromAsset(context.getAssets(),"kalpurush.ttf");
    }

    @NonNull
    @Override
    public ReplyAdapter.VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=context.getLayoutInflater().inflate(R.layout.reply_layout,parent,false);
        return new ReplyAdapter.VHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReplyAdapter.VHolder holder, final int position) {
        holder.docName.setText(reply_doc_name.get(position));
        holder.docName.setTypeface(forum_font);
        holder.docText.setText(reply_doc_text.get(position));
        holder.docText.setTypeface(forum_font);
        holder.postTime.setText(post_reply_time.get(position));
        holder.postTime.setTypeface(forum_font);

        try {
            Glide.with(context).load("https://graph.facebook.com/" + reply_user_id.get(position) + "/picture?width=800").into(holder.pic);
            Glide.with(context).load(repy_image_url.get(position)).into(holder.replyImage);
            if(repy_image_url.get(position).equals("blank")){
                holder.progressBar.setVisibility(View.GONE);
            }

        } catch (IndexOutOfBoundsException e) {
        }
        delete_reply_Reference=database.getReference().child("user").child(postID).child("reply");
        imageReference = database.getReference().child("imageReference");
        storageRef = FirebaseStorage.getInstance().getReference("image/");
        myIDpreference = context.getSharedPreferences("myID",Context.MODE_PRIVATE);
        myID=myIDpreference.getString("myID",null);
        holder.replyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context,ImageViewer.class).putExtra("showImage",repy_image_url.get(position)));
            }
        });
        holder.docText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("",reply_doc_text.get(position));
                if(clipboard!=null){
                    clipboard.setPrimaryClip(clip);
                    Toast toast = makeText(context, "Text copied", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                return true;
            }
        });
        if(reply_user_id.get(position).equals("1335608633238560")){
            holder.varified.setVisibility(View.VISIBLE);
        }else {
            holder.varified.setVisibility(View.GONE);
        }
        if(reply_user_id.get(position).equals(myID)||myID.equals("1335608633238560")) {
            holder.delete_reply.setVisibility(View.VISIBLE);
            holder.delete_reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete_reply_Reference.child(reply_id.get(position)).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            notifyDataSetChanged();
                        }
                    });

                    storageRef.child(reply_id.get(position)).delete();
                    imageReference.child(reply_id.get(position)).setValue(null);
                }
            });
        }else {
            holder.delete_reply.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return reply_doc_name.size();
    }

    public class VHolder extends RecyclerView.ViewHolder{

        TextView docName,docText,postTime,delete_reply,varified;
        CircularImageView pic;
        ImageView replyImage;
        ProgressBar progressBar;
        private VHolder(View itemView) {
            super(itemView);
            docName=(TextView)itemView.findViewById(R.id.reply_name);
            docText=(TextView)itemView.findViewById(R.id.reply_text);
            postTime=(TextView)itemView.findViewById(R.id.reply_time);
            delete_reply=(TextView)itemView.findViewById(R.id.delete_reply);
            varified = (TextView) itemView.findViewById(R.id.varified);
            pic=(CircularImageView)itemView.findViewById(R.id.reply_image);
            replyImage = (ImageView) itemView.findViewById(R.id.replyImage);
            progressBar=(ProgressBar)itemView.findViewById(R.id.reply_layout_progressbar);
        }
    }
    public void browser(String inurl) {
        Intent intent = new Intent(context, Browser.class);
        intent.putExtra("value", inurl);
        context.startActivity(intent);
    }
}


