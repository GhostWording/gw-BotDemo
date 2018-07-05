package com.ghostwording.chatbot.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AssetsManager {

    public static String getStringFromAssetFile(Context context, String filePath) {
        String result = null;
        try {
            InputStream is = context.getResources().getAssets().open(filePath);
            result = convertStreamToString(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String convertStreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

}
