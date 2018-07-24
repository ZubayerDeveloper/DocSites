package zubayer.docsites;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

import static android.widget.Toast.makeText;

public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.VHolder> {
    public Typeface font;
    private ArrayList<String> titleArray;
    private ArrayList<String> imageArray;
    private Activity context;
    public MyAdapter2(Activity context, ArrayList<String> titleArray, ArrayList<String> imageArray) {
        this.titleArray = titleArray;
        this.imageArray = imageArray;
        this.context=context;
        font= Typeface.createFromAsset(context.getAssets(),"kalpurush.ttf");
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=context.getLayoutInflater().inflate(R.layout.customlyst,parent,false);
        return new VHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder holder, final int position) {
        holder.myTitle.setText(titleArray.get(position));
        holder.image.setText(imageArray.get(position));
        holder.myTitle.setTypeface(font);
        holder.myTitle.setText(titleArray.get(position));
        holder.myTitle.setTextColor(Color.parseColor("#123456"));
        holder.image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_SEND);
                intent.setType("dels/plain");
                intent.putExtra(Intent.EXTRA_TEXT,imageArray.get(position));
                context.startActivity(Intent.createChooser(intent,"Share using.."));
            }
        });
        holder.copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("",imageArray.get(position));
                if(clipboard!=null){
                    clipboard.setPrimaryClip(clip);
                    Toast toast = makeText(context, "Link copied", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
        if(imageArray.isEmpty()){
            holder.image.setVisibility(View.GONE);
            holder.copy.setVisibility(View.GONE);
        }else {
            holder.image.setVisibility(View.VISIBLE);
            holder.copy.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return titleArray.size();
    }

    public class VHolder extends RecyclerView.ViewHolder{


        TextView image,myTitle,copy;
        private VHolder(View itemView) {
            super(itemView);
            myTitle = (TextView) itemView.findViewById(R.id.idTitle);
            image=(TextView)itemView.findViewById(R.id.ImageView);
            copy=(TextView)itemView.findViewById(R.id.copy) ;

        }
    }
}

