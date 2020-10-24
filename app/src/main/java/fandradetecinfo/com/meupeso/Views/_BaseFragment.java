package fandradetecinfo.com.meupeso.Views;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.Nullable;

import fandradetecinfo.com.meupeso.MainActivity;

public abstract class _BaseFragment extends Fragment {

    protected Context ctx;

    protected ListView minhaLista;

    protected String TAG;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity)getActivity()).getViewPager().setCurrentItem(MainActivity.posFragment);
    }

    protected void initListener()
    {
        FirebaseFirestore.getInstance().collection(TAG)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        try {
                            if (e != null) {
                                Log.d("LogX Firelog", "Exception", e);
                            }
                            boolean achou = false;
                            for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                                if ((doc.getType() == DocumentChange.Type.ADDED)
                                        || (doc.getType() == DocumentChange.Type.REMOVED)
                                        || (doc.getType() == DocumentChange.Type.MODIFIED)) {
                                    achou = true;
                                }
                            }
                            if (achou) {
                                carregarLista();
                            }
                        }catch (Exception ex)
                        {
                            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_LONG);
                        }
                    }
                });
    }

    abstract void carregarLista();

}
