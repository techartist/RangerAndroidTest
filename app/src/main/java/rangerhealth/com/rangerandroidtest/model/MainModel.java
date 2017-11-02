package rangerhealth.com.rangerandroidtest.model;

import android.util.Log;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import rangerhealth.com.rangerandroidtest.MainActivity;
import rangerhealth.com.rangerandroidtest.MainMVP;
import rangerhealth.com.rangerandroidtest.singleton.Constants;

import static android.content.ContentValues.TAG;

/**
 * Created by kristywelsh on 11/2/17.
 */

public class MainModel implements MainMVP.ModelOps {
    private UserList userListYellow;
    private UserList userListBlue;
    private UserList userListGreen;
    private UserList userList;
    private Disposable peopleSubscription;

    // Presenter reference
    private MainMVP.PresenterOps mPresenter;

    public MainModel(MainMVP.PresenterOps mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public void getPeople(final String color) {
        if (userListYellow == null || userListYellow.isEmpty()) {
            SingleObserver<HashMap<String, UserList>> observer = new SingleObserver<HashMap<String, UserList>>() {

                @Override
                public void onSubscribe(Disposable d) {
                    mPresenter.dismissProgressDialog();
                    mPresenter.showProgressDialog();

                    peopleSubscription = d;
                }

                @Override
                public void onSuccess(HashMap<String, UserList> peopleHashMap) {
                    mPresenter.dismissProgressDialog();
                    userListYellow = peopleHashMap.get(Constants.COLORS.YELLOW.getColor());
                    userListGreen = peopleHashMap.get(Constants.COLORS.GREEN.getColor());
                    userListBlue = peopleHashMap.get(Constants.COLORS.BLUE.getColor());
                    userList = userListYellow;
                    Log.d(TAG, "Subscribe Called Once");
                    mPresenter.setAdapter(userList);
                }

                @Override
                public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                    Log.e(TAG, e.toString());
                    mPresenter.dismissProgressDialog();
                    e.printStackTrace();

                }
            };

            getPeopleObservableArrayList()
                    .subscribe(observer);

        } else {
            if (color.equalsIgnoreCase(Constants.COLORS.YELLOW.getColor()) || color.length() == 0) {
                mPresenter.setAdapter(userListYellow);
            } else if (color.equalsIgnoreCase(Constants.COLORS.YELLOW.getColor())) {
                mPresenter.setAdapter(userListBlue);
            } else if (color.equalsIgnoreCase(Constants.COLORS.GREEN.getColor())) {
                mPresenter.setAdapter(userListGreen);
            } else {
                mPresenter.setAdapter(userList);
            }

        }

    }

    @Override
    public UserList getSavedObjects(String color) {
        if (color.equalsIgnoreCase(Constants.COLORS.YELLOW.getColor()) || color.length() == 0) {
            return userListYellow;
        } else if (color.equalsIgnoreCase(Constants.COLORS.BLUE.getColor())) {
            return userListBlue;
        } else if (color.equalsIgnoreCase(Constants.COLORS.GREEN.getColor())) {
            return userListGreen;
        }
        return null;
    }

    @Override
    public void setSaveObjects(UserList userList, String color) {

        if (color.equalsIgnoreCase(Constants.COLORS.YELLOW.getColor()) || color.length() == 0) {
            userListYellow = userList;
        } else if (color.equalsIgnoreCase(Constants.COLORS.YELLOW.getColor())) {
            userListBlue = userList;
        } else if (color.equalsIgnoreCase(Constants.COLORS.GREEN.getColor())) {
            userListGreen = userList;
        }

    }


    private Single<HashMap<String, UserList>> getPeopleObservableArrayList() {

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
    public void onDestroy() {
        if (peopleSubscription != null) {
            peopleSubscription.dispose();
        }

    }
}
