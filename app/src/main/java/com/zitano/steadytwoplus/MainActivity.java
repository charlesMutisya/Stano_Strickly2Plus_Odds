package com.zitano.steadytwoplus;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.zitano.steadytwoplus.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton fab_main, fab1_mail, fab2_share,fab3_rate;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;

    Boolean isOpen = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PagerAdapter sectionsPagerAdapte = new FragmentAdaptrer(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapte);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        FirebaseMessaging.getInstance().subscribeToTopic("jackpot");
        AudienceNetworkAds.initialize(this);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        fab_main = findViewById(R.id.fab);
        fab1_mail = findViewById(R.id.fab1);
        fab2_share = findViewById(R.id.fab2);
        fab3_rate= findViewById(R.id.fab3);

        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_closed);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_anticlock);





        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpen) {
                    fab3_rate.startAnimation(fab_close);
                    fab2_share.startAnimation(fab_close);
                    fab1_mail.startAnimation(fab_close);
                    fab_main.startAnimation(fab_anticlock);
                    fab3_rate.setClickable(false);
                    fab2_share.setClickable(false);
                    fab1_mail.setClickable(false);

                    isOpen = false;
                } else {
                    fab3_rate.startAnimation(fab_open);
                    fab2_share.startAnimation(fab_open);
                    fab1_mail.startAnimation(fab_open);
                    fab_main.startAnimation(fab_clock);
                    fab3_rate.setClickable(true);
                    fab2_share.setClickable(true);
                    fab1_mail.setClickable(true);
                    isOpen = true;
                }

            }

        });
        fab3_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent RateIntent =
                            new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + getPackageName()));
                    startActivity(RateIntent);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Unable to connect try again later...",
                            Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        fab2_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "The Winners zone,the all in one Strictly 2+ football prediction app. Download it here https://play.google.com/store/apps/details?id=com.zitano.steadytwoplus";
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, " Strictly 2+ Odds");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(sharingIntent);

            }
        });
        fab1_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent feedback = new Intent(view.getContext(), FeedBack.class);

                startActivity(feedback);

            }
        });

    }

    class FragmentAdaptrer extends FragmentPagerAdapter{

        public FragmentAdaptrer(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
           switch (position){
               case 0:
                   return new DailyTips();

               case 1:
                   return  new Steady2plus();

               case 2:
                   return  new SuperSingle();

               case 3:
                   return  new GoalScorer();

               case 4:
                   return  new LiveScore();

           }

            return new DailyTips();
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Daily Tips";
                case 1:
                    return "2+ Tips";
                case 2:
                    return "Super Single";
                case 3:
                    return "Goal Scorer";
                case 4:
                    return "Livescore";
                default:
                    return "Daily Tips";
            }
        }
    }

}