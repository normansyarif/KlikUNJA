package id.ac.unja.klikunja;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bn_main);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

//        int intentFragment = 1;
//
//        switch (intentFragment){
//            case 1:
//                loadFragment(new HomeFragment());
//                break;
//            case 2:
//                loadFragment(new NewsFragment());
//                break;
//            case 3:
//                loadFragment(new EventsFragment());
//                break;
//            case 4:
//                loadFragment(new OpiniFragment());
//                break;
//        }

        loadFragment(new HomeFragment());
    }

    private boolean loadFragment(Fragment fragment){
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        switch (menuItem.getItemId()){
            case R.id.home_menu:
                fragment = new HomeFragment();
                break;
            case R.id.search_menu:
                fragment = new NewsFragment();
                break;
            case R.id.events_menu:
                fragment = new EventsFragment();
                break;
            case R.id.opini_menu:
                fragment = new OpiniFragment();
                break;
        }
        return loadFragment(fragment);
    }

}
