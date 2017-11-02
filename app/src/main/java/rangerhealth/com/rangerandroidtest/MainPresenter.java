package rangerhealth.com.rangerandroidtest;

import java.lang.ref.WeakReference;

import rangerhealth.com.rangerandroidtest.model.MainModel;
import rangerhealth.com.rangerandroidtest.model.UserList;

/**
 * Created by kristywelsh on 11/2/17.
 */

public class MainPresenter implements  MainMVP.PresenterOps {
    @Override
    public void setAdapter(UserList userList) {
        mView.get().setAdapter(userList);
    }

    @Override
    public UserList getSavedObjects(String color) {
        return mModel.getSavedObjects(color);
    }

    @Override
    public void setSaveObjects(UserList userList, String color) {
        mModel.setSaveObjects(userList, color);

    }

    // Layer View reference
    private WeakReference<MainMVP.RequiredViewOps> mView;
    // Layer Model reference
    private MainMVP.ModelOps mModel;

    // Configuration change state
    private boolean mIsChangingConfig;

    public MainPresenter(MainMVP.RequiredViewOps mView) {
        this.mView = new WeakReference<>(mView);
        this.mModel = new MainModel(this);
    }


    @Override
    public void onDestroy() {
        mView = null;
        mModel.onDestroy();

    }



    @Override
    public void updateRecyclerView(UserList userList) {
        mView.get().updateRecyclerView(userList);

    }

    @Override
    public void showProgressDialog() {
        mView.get().showProgressDialog();


    }

    @Override
    public void dismissProgressDialog() {
        mView.get().dismissProgressDialog();

    }

    @Override
    public void getPeople(String color) {
        mModel.getPeople(color);
    }





}
