package net.albertogarrido.studydrivetest

import android.graphics.Color

import java.util.Random

fun randomColor(): Int {
    val random = Random()
    return Color.argb(128, random.nextInt(256), random.nextInt(256), random.nextInt(256))
}



