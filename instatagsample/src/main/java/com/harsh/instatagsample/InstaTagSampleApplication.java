/*
 * Copyright 2017 Harsh Sharma
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.harsh.instatagsample;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;


public class InstaTagSampleApplication extends Application {
    private static InstaTagSampleApplication instaTagSampleApplication;
    public static final String SHARED_PREF_NAME = "insta_tag_preferences";
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public InstaTagSampleApplication() {
        instaTagSampleApplication = this;
    }

    public static InstaTagSampleApplication getInstance() {
        if (instaTagSampleApplication == null) {
            instaTagSampleApplication = new InstaTagSampleApplication();
        }
        if (instaTagSampleApplication.sharedPreferences == null) {
            instaTagSampleApplication.sharedPreferences = instaTagSampleApplication.getSharedPreferences(SHARED_PREF_NAME,
                    Context.MODE_PRIVATE);
        }

        return instaTagSampleApplication;
    }

    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    private String getString(String key, String deafultValue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(key, deafultValue);
        }

        return deafultValue;
    }

    private void putString(String key, String value) {
        try {
            if (sharedPreferences != null) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(key, value);
                editor.apply();
            }
        } catch (Exception e) {
            Log.e(SHARED_PREF_NAME, "Unable Put String in Shared preference", e);
        }
    }


    public ArrayList<TaggedPhoto> getTaggedPhotos() {
        String json = getString(Keys.TAGGED_PHOTOS.getKeyName(), "");
        ArrayList<TaggedPhoto> taggedPhotoArrayList;
        if (!json.equals("")) {
            taggedPhotoArrayList = new Gson().fromJson(json, new TypeToken<ArrayList<TaggedPhoto>>() {
            }.getType());
        } else {
            taggedPhotoArrayList = new ArrayList<>();
        }
        return taggedPhotoArrayList;
    }

    public void setTaggedPhotos(ArrayList<TaggedPhoto> taggedPhotoArrayList) {
        putString(Keys.TAGGED_PHOTOS.getKeyName(), toJson(taggedPhotoArrayList));
    }

    private enum Keys {
        TAGGED_PHOTOS("TAGGED_PHOTOS");
        private String keyName;

        Keys(String label) {
            this.keyName = label;
        }

        public String getKeyName() {
            return keyName;
        }
    }


    public static String toJson(Object object) {
        try {
            Gson gson = new com.google.gson.Gson();
            return gson.toJson(object);
        } catch (Exception e) {
            Log.e(SHARED_PREF_NAME, "Error In Converting ModelToJson", e);
        }
        return "";
    }

}
