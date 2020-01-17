package com.bitstrips.example.contentproviderdemo;

import com.bitstrips.example.contentproviderdemo.query.QueryUris;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LandingActivity extends Activity {

    private static final String BITMOJI_PACKAGE_NAME = "com.bitstrips.imoji";
    private static final Uri CONNECTED_APPS_URI = Uri.parse("imoji://content-provider/connected-apps");
    private static final int REQUEST_ACCESS_CODE = 1;

    private TextView mMessageView;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        mMessageView = findViewById(R.id.message);
        mButton = findViewById(R.id.button);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Cursor cursor = getContentResolver().query(QueryUris.status(), null, null, null, null);

        if (cursor == null) {
            mMessageView.setText("Please download the Bitmoji app to continue");
            mButton.setOnClickListener(v ->
                    startActivity(new Intent(Intent.ACTION_VIEW)
                            .setData(Uri.parse("https://play.google.com/store/apps/details?id=" + BITMOJI_PACKAGE_NAME))));
            return;
        }

        try {
            cursor.moveToNext();
            String status = cursor.getString(cursor.getColumnIndex("status"));

            if (TextUtils.equals(status, "ready")) {
                startActivity(new Intent(this, MainActivity.class));
            } else if (TextUtils.equals(status, "no_avatar")) {
                mMessageView.setText("Please create your avatar in the Bitmoji app");
                mButton.setText("Open Bitmoji");
                mButton.setOnClickListener(v ->
                        startActivity(getPackageManager().getLaunchIntentForPackage(BITMOJI_PACKAGE_NAME)));
            } else {
                mMessageView.setText("Allow this app to access your Bitmoji");
                mButton.setText("Request Access");
                mButton.setOnClickListener(v -> startActivityForResult(
                        new Intent(Intent.ACTION_VIEW).setData(CONNECTED_APPS_URI),
                        REQUEST_ACCESS_CODE));
            }
        } finally {
            cursor.close();
        }
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_ACCESS_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Access granted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Access grant failed!", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
