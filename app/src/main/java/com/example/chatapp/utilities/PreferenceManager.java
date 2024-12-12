package com.example.chatapp.utilities;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * PreferenceManager is a utility class for managing shared preferences in the application.
 * It provides methods to save, retrieve, and clear key-value pairs in a consistent way.
 */
public class PreferenceManager {
    private final SharedPreferences sharedPreferences;
    /**
     * Constructor to initialize the PreferenceManager with a context.
     *
     * @param context The application context used to access shared preferences.
     */
    public PreferenceManager (Context context) {
       sharedPreferences = context.getSharedPreferences(Constants.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }
    /**
     * Saves a boolean value in shared preferences.
     *
     * @param key   The key to identify the value.
     * @param value The boolean value to save.
     */

    public void putBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }
    /**
     * Retrieves a boolean value from shared preferences.
     *
     * @param key The key to identify the value.
     * @return The boolean value associated with the key, or false if not found.
     */

   public Boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);

   }

    /**
     * Saves a string value in shared preferences.
     *
     * @param key   The key to identify the value.
     * @param value The string value to save.
     */

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
   }
    /**
     * Retrieves a string value from shared preferences.
     *
     * @param key The key to identify the value.
     * @return The string value associated with the key, or null if not found.
     */
   public String getString(String key) {
        return sharedPreferences.getString(key, null);
   }

   public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
   }


}
