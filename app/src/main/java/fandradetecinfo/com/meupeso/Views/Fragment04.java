package fandradetecinfo.com.meupeso.Views;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import fandradetecinfo.com.meupeso.Controllers.BalancaDigitalController;
import fandradetecinfo.com.meupeso.R;
import fandradetecinfo.com.meupeso.Relatorio;

public class Fragment04 extends Fragment {

    //private BalancaDigitalController controller;
    private View vw;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vw = inflater.inflate(R.layout.frag_04, container, false);

        TextView tv = (TextView) vw.findViewById(R.id.tvFrag04);
        tv.setText(getArguments().getString("msg"));

        BalancaDigitalController.getInstance().init(getActivity());

        return vw;
    }

    public static Fragment04 newInstance(String text) {

        Fragment04 f = new Fragment04();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            Activity a = getActivity();
            if(a != null)
            {
                a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                this.montarGrid();
                Log.i("LogX", "Frag 04 - Carregou grid");
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void montarGrid()
    {
        List<String> listHeader = new ArrayList<String>();
        listHeader.add("Usuario");
        listHeader.add("Nome");
        listHeader.add("Sexo");
        listHeader.add("Peso Médio");
        listHeader.add("IMC");
        listHeader.add("Registros");

        GridView gridViewHeader=(GridView) vw.findViewById(R.id.grvFrag04Header);

        GridView gridView=(GridView) vw.findViewById(R.id.grvFrag04);

        BalancaDigitalController.getInstance().carregarGrid(listHeader, gridViewHeader, gridView, Relatorio.Totais);
    }

}