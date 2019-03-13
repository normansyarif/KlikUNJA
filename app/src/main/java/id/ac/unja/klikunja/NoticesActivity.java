package id.ac.unja.klikunja;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class NoticesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        String title = getIntent().getExtras().getString("title");

        Toast.makeText(this, title, Toast.LENGTH_SHORT).show();

    }
}
