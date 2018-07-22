package net.albertogarrido.studydrivetest;

import android.graphics.Color;

import java.util.Random;

public class Utils {

    private Utils() throws IllegalAccessException {
        throw new IllegalAccessException("No instances");
    }

    public static int getRandomColor() {
        Random random = new Random();
        return Color.argb(128, random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

}
