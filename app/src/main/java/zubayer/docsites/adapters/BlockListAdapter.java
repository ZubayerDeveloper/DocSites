package zubayer.docsites.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

import zubayer.docsites.R;

import static android.widget.Toast.makeText;

public class BlockListAdapter extends RecyclerView.Adapter<BlockListAdapter.VHolder> {
    ArrayList<String> blocked_name,blocked_id;
    Activity context;
    Typeface forum_font;
    FirebaseDatabase database;
    DatabaseReference block_Reference;
    public BlockListAdapter(Activity context,
                        ArrayList<String> blocked_id,
                        ArrayList<String> blocked_name) {
        this.blocked_id = blocked_id;
        this.blocked_name = blocked_name;
        this.context=context;
        database= FirebaseDatabase.getInstance();
        forum_font= Typeface.createFromAsset(context.getAssets(),"kalpurush.ttf");
    }

    @NonNull
    @Override
    public BlockListAdapter.VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=context.getLayoutInflater().inflate(R.layout.block_list_layout,parent,false);
        return new BlockListAdapter.VHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BlockListAdapter.VHolder holder, final int position) {
        block_Reference=database.getReference().child("block");
        holder.blockedname.setText(blocked_name.get(position));
        holder.blockedname.setTypeface(forum_font);
        holder.dounblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                AlertDialog dialog = builder.create();
                dialog.setMessage("Unblock " + blocked_name.get(position) + " ?");
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Unblock", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        block_Reference.child(blocked_id.get(position)).setValue(null);
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
        try {
            Glide.with(context).load("https://graph.facebook.com/" + blocked_id.get(position) + "/picture?width=800").into(holder.blocked_user_image);

        } catch (IndexOutOfBoundsException e) {
        }

    }

    @Override
    public int getItemCount() {
        return blocked_id.size();
    }

    public class VHolder extends RecyclerView.ViewHolder{

        TextView blockedname,dounblock;
        CircularImageView blocked_user_image;
        private VHolder(View itemView) {
            super(itemView);
            blockedname=(TextView)itemView.findViewById(R.id.blocked_name);
            dounblock=(TextView)itemView.findViewById(R.id.unblock);
            blocked_user_image=(CircularImageView)itemView.findViewById(R.id.blocked_user_image);
        }
    }
    private void myToast(String text) {
        Toast toast = makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }
}



