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


public class MainActivity extends AppCompatActivity {

    private LinearLayoutManager mLinearLayoutManager;


    private UserList userList;
    private RecyclerAdapter mAdapter;
    public ProgressDialog progressDialog;
    private UserList userListYellow;
    private UserList userListBlue;
    private UserList userListGreen;

    private Disposable peopleSubscription;
    private String TAG = this.getClass().getSimpleName();
    private SharedPreferences preferences;
    private String COLOR = "color";
    private HashMap<String, UserList> list = new HashMap<>();


    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (mAdapter != null) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        userList = userListYellow;

                        mAdapter.notifyDataSetChanged();
                        preferences.edit().putString(COLOR, Constants.COLORS.YELLOW.getColor()).apply();
                        return true;
                    case R.id.navigation_dashboard:
                        userList = userListBlue;
                        mAdapter.notifyDataSetChanged();
                        preferences.edit().putString(COLOR, Constants.COLORS.BLUE.getColor()).apply();
                        return true;
                    case R.id.navigation_notifications:
                        userList = userListGreen;
                        preferences.edit().putString(COLOR, Constants.COLORS.GREEN.getColor()).apply();
                        mAdapter.notifyDataSetChanged();
                        return true;
                }
            } else {
                getPeople(Constants.COLORS.YELLOW.getColor());
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
        if (savedInstanceState != null) {
            userList = (UserList) savedInstanceState.getSerializable(Constants.KEY_INSTANCE_STATE_USER_LIST);
            userListYellow = (UserList)savedInstanceState.getSerializable(Constants.KEY_INSTANCE_STATE_USER_LIST_YELLOW);
            userListBlue = (UserList)savedInstanceState.getSerializable(Constants.KEY_INSTANCE_STATE_USER_LIST_BLUE);
            userListGreen = (UserList)savedInstanceState.getSerializable(Constants.KEY_INSTANCE_STATE_USER_LIST_GREEN);
        }

        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        setUpProgressDialog();
        getPeople(color);

    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Constants.KEY_INSTANCE_STATE_USER_LIST, userList);
        outState.putSerializable(Constants.KEY_INSTANCE_STATE_USER_LIST_YELLOW, userListYellow);
        outState.putSerializable(Constants.KEY_INSTANCE_STATE_USER_LIST_BLUE, userListBlue);
        outState.putSerializable(Constants.KEY_INSTANCE_STATE_USER_LIST_GREEN, userListGreen);
    }


    private void setUpProgressDialog() {
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage(getString(R.string.progress_message));
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(Boolean.FALSE);
    }

    private void getPeople(final String color) {
        if (userListYellow == null || userListYellow.isEmpty()) {
            SingleObserver<HashMap<String, UserList>> observer = new SingleObserver<HashMap<String, UserList>>() {

                @Override
                public void onSubscribe(Disposable d) {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    progressDialog.show();
                    peopleSubscription = d;
                }

                @Override
                public void onSuccess(HashMap<String, UserList> peopleHashMap) {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    userListYellow = peopleHashMap.get(Constants.COLORS.YELLOW.getColor());
                    userListGreen = peopleHashMap.get(Constants.COLORS.GREEN.getColor());
                    userListBlue = peopleHashMap.get(Constants.COLORS.BLUE.getColor());
                    userList = userListYellow;
                    Log.d(TAG, "Subscribe Called Once");
                    mAdapter = new RecyclerAdapter();
                    mRecyclerView.setAdapter(mAdapter);
                }

                @Override
                public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                    Log.e(TAG, e.toString());
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    e.printStackTrace();

                }
            };

            getPeopleObservableArrayList()
                    .subscribe(observer);

        } else {
            if (color.equalsIgnoreCase(Constants.COLORS.YELLOW.getColor()) || color.length() == 0) {
                userList = userListYellow;
            } else if (color.equalsIgnoreCase(Constants.COLORS.YELLOW.getColor())) {
                userList = userListBlue;
            } else if (color.equalsIgnoreCase(Constants.COLORS.GREEN.getColor())) {
                userList = userListGreen;
            }
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();

            } else {
                mAdapter = new RecyclerAdapter();
                mRecyclerView.setAdapter(mAdapter);
            }

        }

    }



    private  Single<HashMap<String, UserList>> getPeopleObservableArrayList() {

        SingleOnSubscribe<ArrayList<String>> single = (e -> {
            ArrayList<String> names = getUserNameLists();
            e.onSuccess(names);

        });

        return Single.create(single)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(names -> UserList.createUserLists(names)).cache();


    }



    private ArrayList<String> getUserNameLists() throws IOException {
        ArrayList<String> lists = new ArrayList<>();
        for (Constants.COLORS color : Constants.COLORS.values()) {
            String list = getPeopleFromWeb();
            lists.add(list);

        }
        return lists;

    }



    private String getPeopleFromWeb() throws IOException {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.API_URL).newBuilder();
        urlBuilder.addQueryParameter(Constants.PARAM_CONST, Constants.NUMBER_OF_RESULTS);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        ResponseBody body = response.body();
        if (response.code() == HttpURLConnection.HTTP_OK) {

            return body.string();
        } else {

            throw new IOException("Bad Request: Server Response" + String.valueOf(response.code()) + " " + response.message());
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (peopleSubscription != null) {
            peopleSubscription.dispose();
        }
    }

    class RecyclerAdapter extends RecyclerView.Adapter<UserHolder> {


        @Override
        public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflatedView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ranger_item_row, parent, false);
            return new UserHolder(inflatedView);
        }

        @Override
        public void onBindViewHolder(UserHolder holder, int position) {
            User user = userList.get(position);
            holder.bindPhoto(user);
        }

        @Override
        public int getItemCount() {
            return userList.size();
        }
    }

    public class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.item_image)
        ImageView mItemImage;

        @BindView(R.id.item_name)
        TextView mItemName;

        public UserHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);
            v.setOnClickListener(this);
        }

        public void bindPhoto(final User user) {
            mItemName.setText(user.getName());
            Picasso.with(mItemImage.getContext()).load(user.getAvatar()).into(mItemImage);

        }

        @Override
        public void onClick(View v) {
            Log.d("RecyclerView", "CLICK!");
        }
    }


}
