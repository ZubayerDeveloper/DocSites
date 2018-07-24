package zubayer.docsites;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.VHolder> {
    ArrayList<String> reply_doc_name,reply_doc_text,reply_user_id, post_reply_time,reply_id;
    Activity context;
    Typeface forum_font;

    public ReplyAdapter(Activity context,
                        ArrayList<String> reply_doc_name,
                        ArrayList<String> reply_doc_text,
                        ArrayList<String> reply_user_id,
                        ArrayList<String> post_reply_time,
                        ArrayList<String> reply_id) {
        this.reply_doc_name = reply_doc_name;
        this.reply_doc_text = reply_doc_text;
        this.reply_user_id=reply_user_id;
        this.post_reply_time =post_reply_time;
        this.reply_id=reply_id;
        this.context=context;

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
        Glide.with(context).load("https://graph.facebook.com/" + reply_user_id.get(position) + "/picture?width=800").into(holder.pic);

    }

    @Override
    public int getItemCount() {
        return reply_doc_name.size();
    }

    public class VHolder extends RecyclerView.ViewHolder{

        TextView docName,docText,postTime,reply;
        CircularImageView pic;
        private VHolder(View itemView) {
            super(itemView);
            docName=(TextView)itemView.findViewById(R.id.reply_name);
            docText=(TextView)itemView.findViewById(R.id.reply_text);
            postTime=(TextView)itemView.findViewById(R.id.reply_time);
            pic=(CircularImageView)itemView.findViewById(R.id.reply_image);

        }
    }
}


