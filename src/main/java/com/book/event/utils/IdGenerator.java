package com.book.event.utils;

import java.util.Random;

public class IdGenerator {

    public static String IdGen () {
        return String.valueOf( new Random().nextInt(100000000));
    }
}
