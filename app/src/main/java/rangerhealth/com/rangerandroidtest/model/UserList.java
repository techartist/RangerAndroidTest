package rangerhealth.com.rangerandroidtest.model;

import android.support.annotation.VisibleForTesting;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import rangerhealth.com.rangerandroidtest.singleton.Constants;

import static rangerhealth.com.rangerandroidtest.singleton.Constants.eyes;
import static rangerhealth.com.rangerandroidtest.singleton.Constants.mouths;
import static rangerhealth.com.rangerandroidtest.singleton.Constants.noses;

/**
 * Created by kristywelsh on 10/31/17.
 */

public class UserList extends ArrayList<User> implements Serializable {
    private static final long serialVersionUID = 8520917967502603906L;
    private final String TAG = "UserList";
    public UserList() {
        super();

    }

    public UserList(String names, Constants.COLORS colors) {
        super();
        try {


            JSONArray array = new JSONArray(names);

            for (int i = 0; i < array.length(); i++) {
                String name = array.getString(i);


                User user = new User();

                Random generator = new Random();
                int randomEye = generator.nextInt(eyes.length);
                int randomNose = generator.nextInt(noses.length);
                int randomMouth = generator.nextInt(mouths.length);

                user.setName(name);

                user.setAvatar("https://api.adorable.io/avatars/face/" +
                        eyes[randomEye] + "/"
                        + noses[randomNose] + "/" + mouths[randomMouth] + "/" + colors.getRGB());

                this.add(user);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
    }

    @VisibleForTesting
    public static  HashMap<String, UserList>  createUserLists(ArrayList<String> strings) throws JSONException{
        HashMap<String, UserList> list = new HashMap<>();


        for (int j = 0;j<strings.size();j++) {
            Constants.COLORS color = Constants.getColorForList(j);

            UserList users = new UserList(strings.get(j), color);
            list.put(color.getColor(),users);

        }
        return list;


    }

}
