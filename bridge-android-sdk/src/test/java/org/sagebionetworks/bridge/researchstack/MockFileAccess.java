package org.sagebionetworks.bridge.researchstack;

import android.content.Context;

import com.google.common.collect.Maps;

import org.mockito.Mock;
import org.researchstack.backbone.storage.file.FileAccess;
import org.researchstack.backbone.storage.file.aes.Encrypter;

import java.util.Map;

/**
 * Created by liujoshua on 9/19/16.
 */
public class MockFileAccess implements FileAccess {
    private final Map<String, byte[]> files;

    public MockFileAccess() {
        this.files = Maps.newHashMap();
    }

    @Override
    public void writeData(Context context, String path, byte[] data) {
        files.put(path, data);
    }

    @Override
    public byte[] readData(Context context, String path) {
        return files.get(path);
    }

    @Override
    public void moveData(Context context, String fromPath, String toPath) {
        files.put(toPath, files.remove(fromPath));
    }

    @Override
    public boolean dataExists(Context context, String path) {
        return files.containsKey(path);
    }

    @Override
    public void clearData(Context context, String path) {
        files.remove(path);
    }

    @Override
    public void setEncrypter(Encrypter encrypter) {

    }
}
