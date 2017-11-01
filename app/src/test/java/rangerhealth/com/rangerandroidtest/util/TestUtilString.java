package rangerhealth.com.rangerandroidtest.util;

/**
 * Created by kristywelsh on 10/30/17.
 */

import java.io.IOException;
import java.io.InputStream;

public class TestUtilString {

    private String jsonString = "";

    public TestUtilString(String nameOfResource) {
        jsonString = getJsonString(nameOfResource);
    }

    public String getJsonString() {
        return jsonString;
    }

    public String getJsonString(String nameOfResource) {
        InputStream q = getClass().getClassLoader().getResourceAsStream(nameOfResource);
        byte[] contents = new byte[1024];

        int bytesRead = 0;
        String strFileContents = "";
        try {
            while ((bytesRead = q.read(contents)) != -1) {
                strFileContents += new String(contents, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();

        }

        return strFileContents;
    }
}