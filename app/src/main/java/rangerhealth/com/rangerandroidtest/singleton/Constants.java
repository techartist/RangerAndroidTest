package rangerhealth.com.rangerandroidtest.singleton;


import android.support.annotation.VisibleForTesting;

/**
 * Created by kristywelsh on 10/30/17.
 */

public class Constants {

    public static final String API_URL = "http://namey.muffinlabs.com/name.json";
    public static final String PARAM_CONST = "count";
    public static final String NUMBER_OF_RESULTS = "10";

    public static final String KEY_INSTANCE_STATE_USER_LIST = "user_list";
    public static final String KEY_INSTANCE_STATE_USER_LIST_YELLOW = "user_list_yellow";
    public static final String KEY_INSTANCE_STATE_USER_LIST_GREEN = "user_list_green";
    public static final String KEY_INSTANCE_STATE_USER_LIST_BLUE = "user_list_blue";
    public static final String[] eyes = {"eyes1", "eyes2", "eyes3", "eyes4", "eyes5", "eyes6", "eyes7", "eyes8", "eyes9"};
    public static final String[] noses = {"nose1", "nose2", "nose3", "nose4", "nose5", "nose6", "nose7", "nose8", "nose9"};
    public static final String[] mouths = {"mouth1", "mouth2", "mouth3", "mouth4", "mouth5", "mouth6", "mouth7", "mouth8", "mouth9"};

    public static COLORS getColorForList(int i) {
        for (COLORS color : COLORS.values()) {
            if (i == COLORS.YELLOW.ordinal()) {
                return COLORS.YELLOW;
            } else if (i == COLORS.BLUE.ordinal()) {
                return COLORS.BLUE;
            } else if (i == COLORS.GREEN.ordinal()) {
                return COLORS.GREEN;
            }
        }
        return COLORS.YELLOW;

    }

    @VisibleForTesting
    public enum COLORS {
        YELLOW("yellow","ffff66"),
        GREEN("green", "63ef43"),
        BLUE("blue","4377ef");

        private String color;
        private String RGB;


        COLORS(String color, String RGB) {
            this.RGB = RGB;
            this.color = color;
        }

        public String getColor() {
            return color;
        }
        public String getRGB() { return RGB;}


    }
}
