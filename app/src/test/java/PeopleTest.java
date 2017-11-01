import android.util.Log;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
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
        ArrayList<String> strings = new ArrayList<>();
        userLists = new HashMap<>();
        String strJson = new TestUtilString("mock_data.json").getJsonString();
        assertNotNull(strJson);
        strings.add(strJson);
        String strJson2 = new TestUtilString("second_list_mock_data.json").getJsonString();
        assertNotNull(strJson2);
        strings.add(strJson2);
        String strJson3 = new TestUtilString("third_list_mock_data.json").getJsonString();
        assertNotNull(strJson3);
        strings.add(strJson3);
        try {
            userLists = MainActivity.createUserLists(strings);
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
        assertEquals(userListBlue.get(1).getName(),"David");
        UserList userListGreen = userLists.get(MainActivity.COLORS.GREEN.getColor());
        assertEquals(userListGreen.get(2).getName(), "John");

    }


}
