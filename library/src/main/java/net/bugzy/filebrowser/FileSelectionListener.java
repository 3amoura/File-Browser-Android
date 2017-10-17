package net.bugzy.filebrowser;

import android.content.Context;

import java.io.File;

/**
 * Created by OMohamed on 2/16/2017.
 */

public interface FileSelectionListener {
    void onFileSelected(File file);
    void onFolderSelected(File file);
    Context getContext();
}
