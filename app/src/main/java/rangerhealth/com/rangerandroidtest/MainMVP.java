package rangerhealth.com.rangerandroidtest;

import rangerhealth.com.rangerandroidtest.model.User;
import rangerhealth.com.rangerandroidtest.model.UserList;
import rangerhealth.com.rangerandroidtest.singleton.Constants;

/**
 * Created by kristywelsh on 11/2/17.
 */

/*
 * Aggregates all communication operations between MVP pattern layer:
 * Model, View and Presenter
 */
public interface MainMVP {

    /**
     * View mandatory methods. Available to Presenter
     *      Presenter -> View
     */
    interface RequiredViewOps {
        void setUpProgressDialog();
        void showProgressDialog();
        void dismissProgressDialog();
        void updateRecyclerView(UserList userList);
        void setAdapter(UserList userList);
    }

    /**
     * Operations offered from Presenter to View
     *      View -> Presenter
     */
    interface PresenterOps{
        void onDestroy();
        void updateRecyclerView(UserList userList);
        void showProgressDialog();
        void dismissProgressDialog();
        void getPeople(String color);
        void setAdapter(UserList userList);
        UserList getSavedObjects(String color);
        void setSaveObjects(UserList userList, String color);



    }


    /**
     * Model operations offered to Presenter
     *      Presenter -> Model
     */
    interface ModelOps {
        void getPeople(String color);
        UserList getSavedObjects(String color);
        void setSaveObjects(UserList userList, String colors);
        void onDestroy();
        // Any other data operation
    }
}
