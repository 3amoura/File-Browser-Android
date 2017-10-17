package bugzy.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import net.bugzy.filebrowser.FileSelectionActivity;

import java.io.File;

import bugzy.filebrowser.R;

import static net.bugzy.filebrowser.FileBrowser.CHANGE_FOLDER_REQUEST;
import static net.bugzy.filebrowser.FileBrowser.FILE_PATH_INTENT_EXTRA;
import static net.bugzy.filebrowser.FileBrowser.SELECT_FILE_REQUEST;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onSelectFolderClicked(View view) {
      startActivityForResult(FileSelectionActivity.open(this, true), CHANGE_FOLDER_REQUEST);
    }

    public void onSelectFileClicked(View view) {
        startActivityForResult(FileSelectionActivity.open(this, true), SELECT_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case FileBrowser.CHANGE_FOLDER_REQUEST:
                    String filePath = ((File) data.getSerializableExtra(FILE_PATH_INTENT_EXTRA)).getAbsolutePath();
                    Toast.makeText(this, filePath, Toast.LENGTH_SHORT).show();
//                    break;
//            }
        }
    }
}
