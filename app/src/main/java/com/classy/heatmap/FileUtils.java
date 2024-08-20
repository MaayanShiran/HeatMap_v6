package com.classy.heatmap;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileUtils {

    private static final String TAG = "FileUtils";

    public static void saveInteractionDataToFile(Context context, List<InteractionData> data) {
        if (isExternalStorageWritable()) {
            File file = new File(context.getExternalFilesDir(null), "interaction_data.txt");

            try (FileWriter writer = new FileWriter(file, true)) {
                for (InteractionData interaction : data) {
                    String line = interaction.getX() + "," + interaction.getY() + "," + interaction.getType() + "\n";
                    writer.append(line);
                }
                writer.flush();
            } catch (IOException e) {
                Log.e(TAG, "Error writing to file", e);
            }
        } else {
            Log.e(TAG, "External storage not writable");
        }
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}
