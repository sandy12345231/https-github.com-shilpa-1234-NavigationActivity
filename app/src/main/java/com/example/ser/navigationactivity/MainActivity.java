package com.example.ser.navigationactivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("ProKart");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        SharedPreferences sharedPreferences  = getSharedPreferences("Data",MODE_PRIVATE);
        String imageUrl = sharedPreferences.getString("imageUrl",null);
        String name = sharedPreferences.getString("name", null);
        String email = sharedPreferences.getString("Email",null);

        Log.d("1234", "onCreate: "+imageUrl+name+email);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View view = navigationView.getHeaderView(0);
        ImageView imageView = (ImageView)view.findViewById(R.id.imageProfile);
        TextView textViewName = (TextView)view.findViewById(R.id.textName);
        TextView textViewEmail = (TextView)view.findViewById(R.id.textViewEmailAddress);
        textViewEmail.setText(email);
       //Glide.with(this).load(imageUrl).into(imageView);
        textViewName.setText(name);

        navigationView.setNavigationItemSelectedListener(this);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(), 2000, 4000);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void goToVisitingCard(View view) {

        Intent intent = new Intent(MainActivity.this, ProductListingitem.class);
        intent.putExtra("category", "Visiting Card");
        startActivity(intent);
    }

    public void goToShoes(View view) {
        Intent intent = new Intent(MainActivity.this, ProductListingitem.class);
        intent.putExtra("category", "Shoes");
        startActivity(intent);
    }

    public void goToKeychain(View view) {
        Intent intent = new Intent(MainActivity.this, ProductListingitem.class);
        intent.putExtra("category", "Keychain");
        startActivity(intent);
    }

    public void goToHandbag(View view) {
        Intent intent = new Intent(MainActivity.this, ProductListingitem.class);
        intent.putExtra("category", "Handbag");
        startActivity(intent);
    }

    public void goTojewelery(View view) {
        Intent intent = new Intent(MainActivity.this, ProductListingitem.class);
        intent.putExtra("category", "Jewelery");
        startActivity(intent);
    }

    public void goToTshirt(View view) {
        Intent intent = new Intent(MainActivity.this, ProductListingitem.class);
        intent.putExtra("category", "T-shirt");
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.viewCart) {
            Intent intent = new Intent(MainActivity.this, AddToCartActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_order) {

            Intent intent = new Intent(MainActivity.this, ConfirmOrderActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_share) {

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "Our Prokart is Online Shopping application.. Please download this application and shopping.");
            startActivity(Intent.createChooser(intent, "Share with"));

        } else if (id == R.id.nav_logout) {

            SharedPreferences sharedPreferences = getSharedPreferences("Data", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() == 0) {
                        viewPager.setCurrentItem(1);
                    } else if (viewPager.getCurrentItem() == 1) {
                        viewPager.setCurrentItem(2);
                    } else viewPager.setCurrentItem(0);
                }
            });
        }

        {

        }

    }

}
