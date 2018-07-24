package zubayer.docsites;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.VHolder> {
    ArrayList<String> doc_name,doc_text,post_Time,user_id,post_id;
    Activity context;
    Typeface forum_font;
    ProgressBar propic_progressBar;

    public ForumAdapter(Activity context,
                        ArrayList<String> doc_name,
                        ArrayList<String> doc_text,
                        ArrayList<String> post_Time,
                        ArrayList<String> user_id,
                        ArrayList<String> post_id) {
        this.doc_name = doc_name;
        this.doc_text = doc_text;
        this.post_Time=post_Time;
        this.user_id=user_id;
        this.post_id=post_id;
        this.context=context;

        forum_font= Typeface.createFromAsset(context.getAssets(),"kalpurush.ttf");
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=context.getLayoutInflater().inflate(R.layout.forum_layout,parent,false);
        return new VHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder holder, final int position) {
        holder.docName.setText(doc_name.get(position));
        holder.docName.setTypeface(forum_font);
        holder.docText.setText(doc_text.get(position));
        holder.docText.setTypeface(forum_font);
        holder.postTime.setText(post_Time.get(position));
        holder.postTime.setTypeface(forum_font);
        Glide.with(context).load("https://graph.facebook.com/" + user_id.get(position) + "/picture?width=800").into(holder.pic);

        holder.reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,Reply.class);
                intent.putExtra("id",user_id.get(position));
                intent.putExtra("name",doc_name.get(position));
                intent.putExtra("text",doc_text.get(position));
                intent.putExtra("time",post_Time.get(position));
                intent.putExtra("postID",post_id.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return doc_name.size();
    }

    public class VHolder extends RecyclerView.ViewHolder{

        TextView docName,docText,postTime,reply,options;
        CircularImageView pic;
        private VHolder(View itemView) {
            super(itemView);
            docName=(TextView)itemView.findViewById(R.id.user_name);
            docText=(TextView)itemView.findViewById(R.id.user_text);
            postTime=(TextView)itemView.findViewById(R.id.time);
            reply=(TextView)itemView.findViewById(R.id.reply);
            pic=(CircularImageView)itemView.findViewById(R.id.user_image);

        }
    }
}

