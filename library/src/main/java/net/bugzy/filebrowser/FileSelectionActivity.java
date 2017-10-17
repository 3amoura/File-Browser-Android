package net.bugzy.filebrowser;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.File;

public class FileSelectionActivity extends AppCompatActivity implements FileSelectionListener {
    private final int STORAGE_REQUEST_CODE = 919;

    private RecyclerView fileRecyclerView;
    private CustomToolbar toolbar;
    private TextView pathTextView;
    private FileSelectionAdapter fileSelectionAdapter;
    private String[] extensionsToAccept;
    private String rootFolder = null;

    @NonNull
    public static Intent open(@NonNull Activity activity, boolean chooseFolder, @Nullable String path) {
        Intent intent = new Intent(activity, FileSelectionActivity.class);
        intent.putExtra(FileBrowser.CHANGE_FOLDER_INTENT_EXTRA, chooseFolder);
        intent.putExtra(FileBrowser.FILE_PATH_INTENT_EXTRA, path);
        return intent;
    }

    @NonNull
    public static Intent open(@NonNull Activity activity, boolean chooseFolder) {
        return open(activity, chooseFolder, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_selection);
        findElements();
        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
        } else {
            toolbar.setVisibility(View.GONE);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getDataFromIntent();
        checkPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case STORAGE_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                boolean permissionsGranted = true;
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        permissionsGranted = false;
                        finish();
                    }
                }
                if (permissionsGranted) {
                    loadFiles();
                }
            }
        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        STORAGE_REQUEST_CODE);
            } else {
                loadFiles();
            }
        } else {
            loadFiles();
        }
    }

    private void findElements() {
        fileRecyclerView = (RecyclerView) findViewById(R.id.files_recycler_view);
        toolbar = (CustomToolbar) findViewById(R.id.toolbar);
        pathTextView = (TextView) findViewById(R.id.path_label);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (extensionsToAccept.length == 0) {
            getMenuInflater().inflate(R.menu.select_folder_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(Activity.RESULT_CANCELED);
            finish();
        } else if (item.getItemId() == R.id.select_folder) {
            onFileSelected(fileSelectionAdapter.getCurrentPath());
        }
        return super.onOptionsItemSelected(item);
    }

    private void getDataFromIntent() {
        if (getIntent().getBooleanExtra(FileBrowser.CHANGE_FOLDER_INTENT_EXTRA, false)) {
            extensionsToAccept = new String[]{};
        } else {
            // new String[]{"pdf", "txt", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "jpg", "png", "gif", "jpeg"};
            extensionsToAccept = new String[]{"mp3"};
        }

        if (getIntent().hasExtra(FileBrowser.FILE_PATH_INTENT_EXTRA)) {
            rootFolder = getIntent().getStringExtra(FileBrowser.FILE_PATH_INTENT_EXTRA);
        }
    }

    private void loadFiles() {
        fileSelectionAdapter = new FileSelectionAdapter(this, extensionsToAccept, rootFolder);
        fileRecyclerView.setAdapter(fileSelectionAdapter);
    }

    @Override
    public void onBackPressed() {
        if (fileSelectionAdapter.canGoUp()) {
            fileSelectionAdapter.goUp();
        } else {
            setResult(Activity.RESULT_CANCELED);
            super.onBackPressed();
        }
    }

    @Override
    public void onFileSelected(File file) {
        Intent intent = new Intent();
        intent.putExtra(FileBrowser.FILE_PATH_INTENT_EXTRA, file);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onFolderSelected(File file) {
        pathTextView.setText(file.getAbsolutePath());
    }

    @Override
    public Context getContext() {
        return this;
    }
}
