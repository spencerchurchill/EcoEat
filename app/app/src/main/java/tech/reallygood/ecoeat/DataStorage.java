package tech.reallygood.ecoeat;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DataStorage {
    SharedPreferences prefs = null;
    SharedPreferences.Editor editor = null;

    public DataStorage(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences("data", Context.MODE_PRIVATE);
    }

    public Set<String> getStoredData(String key) {
        return prefs.getStringSet(key, new HashSet<String>());
    }

    public void saveData(String key, Set<String> data) {
        editor = prefs.edit();
        editor.putStringSet(key, data);
        editor.apply();
    }
}
