package com.example.j2demo;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.j2demo.databinding.ActivityMainBinding;
import com.example.j2demo.ui.pages.CameraFragment;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CustomDensity.setCustomDensity(this, getApplication());

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
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
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == CameraFragment.REQUEST_CODE_PERMISSIONS) {
//            if (CameraFragment.allPermissionGranted(this)) {
////                for (Fragment i: getSupportFragmentManager().getFragments()){
////                    Toast.makeText(this, "activity child fid: " + i.getId(), Toast.LENGTH_SHORT).show();
////                    Log.i("tttt", "activity child fid: " + i.getId());
////                }
//
//                Fragment mf = getSupportFragmentManager()
//                        .findFragmentById(R.id.nav_host_fragment_content_main);
//                if (mf != null) {
////                    for (Fragment i: mf.getChildFragmentManager().getFragments()){
////                        Toast.makeText(this, "mf child fid: " + i.getId(), Toast.LENGTH_SHORT).show();
////                        Log.i("tttt", "mf child fid: " + i.getId());
////                    }
//
////                    Fragment nf = mf.getChildFragmentManager().findFragmentById(R.id.nav_graph);
//                    Fragment nf = mf.getChildFragmentManager().getFragments().get(0);
//                    if (nf != null) {
////                        Fragment f = mf.getChildFragmentManager().findFragmentById(R.id.CameraFragment);
//                        Fragment f = nf.getChildFragmentManager().getFragments().get(0);
//                        if (f instanceof CameraFragment) {
//                            ((CameraFragment)f).startCamera(this);
//                        } else {
//                            Toast.makeText(this, "f is null", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        Toast.makeText(this, "nf is null", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(this, "mf is null", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                Toast.makeText(this, "Permissions not granted", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        }
//    }
}