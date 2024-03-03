package com.jc.jcsports.utils.videotrim;

import android.media.Image;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Environment;

import com.jc.jcsports.utils.Utils;

import java.io.IOException;
import java.nio.ByteBuffer;

public class NDK {

    private String input;
    private String output;
    private int startMs;
    private int endMs;

    static {
        System.loadLibrary("NDK");
    }

    public NDK(String input, String output, int startMs, int endMs) {
        this.input = input;
        this.output = output;
        this.startMs = startMs;
        this.endMs = endMs;
    }

    public int testMethod(String arg0, String arg1) {
        Utils.logCheck("arg0 " + arg0 + " , arg1 " + arg1);
        int result = test(arg0, arg1);
        Utils.logCheck("result " + result);
        return result;
    }

    public String makeThumbNail(String resultOutput, String originPath) {
        Utils.logCheck("arg0 " + resultOutput + " , arg1 " + originPath);
        String result = videoFileToPpm(resultOutput, originPath);
        return result;
    }

    public int compressVideo() {
        int result = ffmpeg(input, output, startMs, endMs);
        return result;
    }

    private native int ffmpeg(String input, String output, int startMs, int endMs);

    private native String videoFileToPpm(String input, String output);

    private native int mp4Andmp3Remux(String input, String output, int startMs, int endMs);

    private native int mp3ControlDuration(String input, String output, int startMs, int endMs);

    private native int test(String resultOutput, String output);

}
