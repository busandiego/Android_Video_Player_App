package com.mx7.encodingtest;

public class NDK {
    static {

        System.loadLibrary("sample-ffmpeg");
    }


    public native int scanning(String filepath);
}
