package rangerhealth.com.rangerandroidtest.model;


import java.io.Serializable;

/**
 * Created by kristywelsh on 10/31/17.
 */

public class User implements Serializable {
    private static final long serialVersionUID = 8536917967502603906L;

    private String name = "";
    private String avatar = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public User() {
    }

}
