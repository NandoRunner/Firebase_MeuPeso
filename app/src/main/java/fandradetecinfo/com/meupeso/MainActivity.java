package fandradetecinfo.com.meupeso;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import fandradetecinfo.com.meupeso.Views.Fragment00;
import fandradetecinfo.com.meupeso.Views.Fragment01;
import fandradetecinfo.com.meupeso.Views.Fragment02;
import fandradetecinfo.com.meupeso.Views.Fragment03;
import fandradetecinfo.com.meupeso.Views.Fragment04;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    public static String usuarioId;
    public static String usuarioNome;
    public static int posFragment = 1;
    private ViewPager pager;

    AccessToken accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Firebase.setAndroidContext(this);

        pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {

                case 0:
                    return Fragment00.newInstance("Usuários");
                case 1:
                    return Fragment01.newInstance("Registros");
                case 2:
                    return Fragment02.newInstance("Registros do usuário: ");
                case 3:
                    return Fragment03.newInstance("Médias do usuário: ");
                case 4:
                    return Fragment04.newInstance("Totalizadores");
                default:
                    return Fragment02.newInstance("Registros do usuário: ");
            }
        }

        @Override
        public int getCount() {
            return 5;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken == null) {
            finish();
        }
    }

    public void btnSair(View view){
        LoginManager.getInstance().logOut();
        finish();
    }

    public ViewPager getViewPager() {
        if (null == pager) {
            pager = (ViewPager) findViewById(R.id.viewPager);
        }
        return pager;
    }

    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }
}
