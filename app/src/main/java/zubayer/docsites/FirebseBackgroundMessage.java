package zubayer.docsites;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebseBackgroundMessage extends FirebaseInstanceIdService{
    private static final String tokken="tokken";

    @Override
    public void onTokenRefresh() {
        String resent= FirebaseInstanceId.getInstance().getToken();
        Log.d(tokken,resent);
    }
}
