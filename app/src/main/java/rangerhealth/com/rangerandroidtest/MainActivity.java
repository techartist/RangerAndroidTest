package rangerhealth.com.rangerandroidtest;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import rangerhealth.com.rangerandroidtest.model.User;
import rangerhealth.com.rangerandroidtest.model.UserList;
import rangerhealth.com.rangerandroidtest.singleton.Constants;

import static rangerhealth.com.rangerandroidtest.singleton.Constants.eyes;
import static rangerhealth.com.rangerandroidtest.singleton.Constants.mouths;
import static rangerhealth.com.rangerandroidtest.singleton.Constants.noses;


public class MainActivity extends AppCompatActivity implements MainMVP.RequiredViewOps {

    private LinearLayoutManager mLinearLayoutManager;


    private UserList userList;
    public ProgressDialog progressDialog;
    private UserList userListYellow;
    private UserList userListBlue;
    private UserList userListGreen;

    private Disposable peopleSubscription;
    private String TAG = this.getClass().getSimpleName();
    private SharedPreferences preferences;
    private String COLOR = "color";
    private HashMap<String, UserList> list = new HashMap<>();
    private RecyclerAdapter adapter;
    private MainMVP.PresenterOps mPresenter;


    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (adapter != null) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        adapter.setUserList(mPresenter.getSavedObjects(Constants.COLORS.YELLOW.getColor()));

                        adapter.notifyDataSetChanged();
                        preferences.edit().putString(COLOR, Constants.COLORS.YELLOW.getColor()).apply();
                        return true;
                    case R.id.navigation_dashboard:
                        adapter.setUserList(mPresenter.getSavedObjects(Constants.COLORS.BLUE.getColor()));
                        adapter.notifyDataSetChanged();
                        preferences.edit().putString(COLOR, Constants.COLORS.BLUE.getColor()).apply();
                        return true;
                    case R.id.navigation_notifications:
                        adapter.setUserList(mPresenter.getSavedObjects(Constants.COLORS.GREEN.getColor()));
                        preferences.edit().putString(COLOR, Constants.COLORS.GREEN.getColor()).apply();
                        adapter.notifyDataSetChanged();
                        return true;
                }
            } else {
                mPresenter.getPeople(Constants.COLORS.YELLOW.getColor());
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        preferences =PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String color = preferences.getString(COLOR, Constants.COLORS.YELLOW.getColor());

        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        setUpProgressDialog();
        mPresenter = new MainPresenter(this);
        mPresenter.getPeople(color);


    }


    @Override
    public void setUpProgressDialog() {
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage(getString(R.string.progress_message));
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(Boolean.FALSE);
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }

    @Override
    public void updateRecyclerView(UserList userList) {
        if (adapter != null) {
            adapter.setUserList(userList);
            adapter.notifyDataSetChanged();
        } else {
            setAdapter(userList);
        }

    }

    @Override
    public void setAdapter(UserList userList) {
        adapter = new RecyclerAdapter(userList);
        mRecyclerView.setAdapter(adapter);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }


}
