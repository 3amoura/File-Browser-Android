package bugzy.filebrowser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;

public class FileSelectionActivity extends AppCompatActivity implements FileSelectionListener {

    private RecyclerView fileRecyclerView;
    private CustomToolbar toolbar;
    private FileSelectionAdapter fileSelectionAdapter;
    private String[] extensionsToAccept;
    private String rootFolder = null;

    public static void open(@NonNull Activity activity, boolean chooseFolder, @Nullable String path) {
        Intent intent = new Intent(activity, FileSelectionActivity.class);
        intent.putExtra(Constants.CHANGE_FOLDER_INTENT_EXTRA, chooseFolder);
        intent.putExtra(Constants.FILE_PATH_INTENT_EXTRA, path);
        activity.startActivityForResult(intent, Constants.CHANGE_FOLDER_REQUEST);
    }

    public static void open(@NonNull Activity activity, boolean chooseFolder) {
        open(activity, chooseFolder, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_selection);
        findElements();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getDataFromIntent();
        loadFiles();
    }

    private void findElements() {
        fileRecyclerView = (RecyclerView) findViewById(R.id.files_recycler_view);
        toolbar = (CustomToolbar) findViewById(R.id.toolbar);
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
        if (getIntent().getBooleanExtra(Constants.CHANGE_FOLDER_INTENT_EXTRA, false)) {
            extensionsToAccept = new String[]{};
        } else {
            // new String[]{"pdf", "txt", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "jpg", "png", "gif", "jpeg"};
            extensionsToAccept = new String[]{"mp3"};
        }

        if (getIntent().hasExtra(Constants.FILE_PATH_INTENT_EXTRA)) {
            rootFolder = getIntent().getStringExtra(Constants.FILE_PATH_INTENT_EXTRA);
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
        intent.putExtra(Constants.FILE_PATH_INTENT_EXTRA, file);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public Context getContext() {
        return this;
    }
}
