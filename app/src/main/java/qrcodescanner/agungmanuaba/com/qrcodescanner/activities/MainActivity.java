package qrcodescanner.agungmanuaba.com.qrcodescanner.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import qrcodescanner.agungmanuaba.com.qrcodescanner.R;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    private Button scanBtn, quitBtn, testInfoBtn, testMapsBtn;
    private TextView formatTxt, contentTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        scanBtn = (Button) findViewById(R.id.scan_button);
        quitBtn = (Button) findViewById(R.id.quit_button);
        formatTxt = (TextView) findViewById(R.id.scan_format);
        contentTxt = (TextView) findViewById(R.id.scan_content);
        testInfoBtn = (Button) findViewById(R.id.test_info_button);
        testMapsBtn = (Button) findViewById(R.id.test_map_button);

        scanBtn.setOnClickListener(this);
        quitBtn.setOnClickListener(this);
        testInfoBtn.setOnClickListener(this);
        testMapsBtn.setOnClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scan_button:
                PackageManager packageManager = getApplicationContext().getPackageManager();
                if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                    IntentIntegrator integrator = new IntentIntegrator(this);
                    integrator.setPrompt(getString(R.string.scan_prompt));
                    integrator.setCaptureActivity(CaptureActivityAnyOrientation.class);
                    integrator.setOrientationLocked(false);
                    integrator.initiateScan();
                } else {
                    // Toast
                    Toast toast = Toast.makeText(getApplicationContext(), "Your device has not camera feature!", Toast.LENGTH_SHORT);
                    toast.show();
                }

                break;
            case R.id.quit_button:
                this.finish();
                break;
            case R.id.test_info_button:
                Intent intent1 = new Intent(MainActivity.this, InfoActivity.class);
                intent1.putExtra("qrcode_id", "");
                startActivity(intent1);
                break;
            case R.id.test_map_button:
                Intent intent2 = new Intent(MainActivity.this, InfoMapsActivity.class);
                intent2.putExtra("qrcode_id", "");
                startActivity(intent2);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);

        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanningResult != null) {
            // we have a result
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();

            formatTxt.setText("FORMAT: " + scanFormat);
            contentTxt.setText("CONTENT: " + scanContent);

            Intent intent = new Intent(MainActivity.this, InfoActivity.class);
            intent.putExtra("qrcode_id", scanContent);
            startActivity(intent);
        } else {
            // Toast
            Toast toast = Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
