package Utils;

import com.badlogic.gdx.graphics.Color;

public class Definitions {
    // ERROR CODES
    public static final int SUCCESS = 0;
    public static final int UNKNOWN_TYPE_ERROR = -1;

    // TERMINAL COLORS
    public static final String ANSI_RESET = "\u001B[0m";

    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";

    public static Color[] colors = {
            Color.GRAY,
            Color.GREEN,
            Color.BLUE,
            Color.RED,
            Color.YELLOW,
            Color.PURPLE,
            Color.PINK,
            Color.OLIVE,
            Color.WHITE,
            Color.BROWN,
            Color.ORANGE,
            Color.CYAN,
            Color.MAGENTA,
            Color.LIME,
            Color.GOLD,
            Color.SALMON
    };
}
