import android.util.Log;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import rangerhealth.com.rangerandroidtest.MainActivity;
import rangerhealth.com.rangerandroidtest.model.UserList;
import rangerhealth.com.rangerandroidtest.util.TestUtilString;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by kristywelsh on 10/31/17.
 */

public class PeopleTest {

    String TAG = "PeopleTest";
    HashMap<String, UserList> userLists = new HashMap<>();

    @Before
    public void setUp() {
        userLists = new HashMap<>();
        String strJson = new TestUtilString("mock_data.json").getJsonString();
        assertNotNull(strJson);
        try {
            userLists = MainActivity.createHashMap(strJson);
        } catch(JSONException e) {
            Log.e(TAG,e.toString());
        }
        assertNotNull(userLists);

    }

    @Test
    public void getPeople() {
        UserList userListYellow = userLists.get(MainActivity.COLORS.YELLOW.getColor());
        assertEquals(userListYellow.get(0).getName(),"David");
        UserList userListBlue = userLists.get(MainActivity.COLORS.BLUE.getColor());
        assertEquals(userListBlue.get(1).getName(),"Robert");
        UserList userListGreen = userLists.get(MainActivity.COLORS.GREEN.getColor());
        assertEquals(userListGreen.get(2).getName(), "Donna");

    }


}
