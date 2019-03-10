package id.ac.unja.klikunja;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

public class OpiniDetailActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView mTitle, mAuthor, mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opini_detail);

        toolbar = findViewById(R.id.opini_toolbar);
        mTitle = findViewById(R.id.opini_title);
        mAuthor = findViewById(R.id.opini_author);
        mContent = findViewById(R.id.opini_body);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Opini");

        setText();
    }

    private void setText() {
        Intent intent = getIntent();
        mTitle.setText(intent.getStringExtra("title"));
        mAuthor.setText(intent.getStringExtra("author"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mContent.setText(Html.fromHtml(intent.getStringExtra("content"), Html.FROM_HTML_MODE_COMPACT));
        } else {
            mContent.setText(Html.fromHtml(intent.getStringExtra("content")));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
