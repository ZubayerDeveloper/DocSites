package zubayer.docsites;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class Blocklist extends Activity {

    RecyclerView recyclerView;
    BlockListAdapter adapter;
    ArrayList<String>blockID,blockName,unblock;
    LinearLayoutManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocklist);

        initializeWidgrttes();

        recyclerView.setAdapter(adapter);
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference blockReference=database.getReference().child("block");
        blockReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                blockID.clear();
                blockName.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    blockID.add(snapshot.getKey());
                    blockName.add(snapshot.getValue(String.class));
                }
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initializeWidgrttes() {
        recyclerView=(RecyclerView)findViewById(R.id.blocklist_recyclerView);
        blockID=new ArrayList<>();
        blockName=new ArrayList<>();
        adapter=new BlockListAdapter(Blocklist.this,blockID,blockName);
        manager = new LinearLayoutManager(Blocklist.this);
        recyclerView.setLayoutManager(manager);

    }
}
