package apsupportapp.aperotechnologies.com.designapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.widget.RelativeLayout;

import apsupportapp.aperotechnologies.com.designapp.BestPerformersInventory.BestPerformerInventory;
import apsupportapp.aperotechnologies.com.designapp.FloorAvailability.FloorAvailabilityActivity;
import apsupportapp.aperotechnologies.com.designapp.HorlyAnalysis.KeyProductActivity;
import apsupportapp.aperotechnologies.com.designapp.SellThruExceptions.SaleThruInventory;
import apsupportapp.aperotechnologies.com.designapp.SkewedSize.SkewedSizesActivity;
import apsupportapp.aperotechnologies.com.designapp.StockAgeing.StockAgeingActivity;
import apsupportapp.aperotechnologies.com.designapp.VisualAssortmentSwipe.VisualAssortmentActivity;

public class SwitchingTabActivity extends AppCompatActivity {

    RelativeLayout backButton, imageBtnHomePage;
    public static Activity switchingTabActivity;
    public static ViewPager viewPager;
    public static TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switching_tab);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        getSupportActionBar().hide();

        switchingTabActivity = this;

        backButton = (RelativeLayout) findViewById(R.id.imageBtnBack1);
        imageBtnHomePage = (RelativeLayout) findViewById(R.id.imageBtnHomePage);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             onBackPressed();
            }
        });

        imageBtnHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SwitchingTabActivity.this, DashBoardActivity.class);
                startActivity(intent);
                finish();
                if(getIntent().getStringExtra("checkFrom").equals("bestInventory"))
                {
                    BestPerformerInventory.bestperoformer.finish();

                }

            }
        });
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Details"));
        tabLayout.addTab(tabLayout.newTab().setText("Style Size"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (ViewPager) findViewById(R.id.pager);
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(getIntent().getStringExtra("checkFrom").equals("SkewedActivity"))
        {
            Intent intent = new Intent(SwitchingTabActivity.this, SkewedSizesActivity.class);
            startActivity(intent);
            finish();
        }
        else if(getIntent().getStringExtra("checkFrom").equals("floor_availability"))
        {
            Intent intent = new Intent(SwitchingTabActivity.this, FloorAvailabilityActivity.class);
            startActivity(intent);
            finish();
        }
        else if(getIntent().getStringExtra("checkFrom").equals("stockAgeing"))
        {
            Intent intent = new Intent(SwitchingTabActivity.this, StockAgeingActivity.class);
            startActivity(intent);
            finish();
        }
        else if(getIntent().getStringExtra("checkFrom").equals("sell_thru_exception"))
        {
            Intent intent = new Intent(SwitchingTabActivity.this, SaleThruInventory.class);
            startActivity(intent);
            finish();
        }
        else if(getIntent().getStringExtra("checkFrom").equals("topCut"))
        {
            Intent intent = new Intent(SwitchingTabActivity.this, SaleThruInventory.class);
            startActivity(intent);
            finish();
        }
        else if(getIntent().getStringExtra("checkFrom").equals("option_fragment"))
        {
            Intent intent = new Intent(SwitchingTabActivity.this, KeyProductActivity.class);
            startActivity(intent);
            finish();
        }
        else if(getIntent().getStringExtra("checkFrom").equals("visualAssortment"))
        {
            Intent intent = new Intent(SwitchingTabActivity.this, VisualAssortmentActivity.class);
            startActivity(intent);
            finish();
        }
        else if(getIntent().getStringExtra("checkFrom").equals("bestInventory"))
        {
           // Intent intent = new Intent(SwitchingTabActivity.this, VisualAssortmentActivity.class);
           // startActivity(intent);
            finish();
        }
        else if(getIntent().getStringExtra("checkFrom").equals("styleActivity")){
            Intent intent = new Intent(SwitchingTabActivity.this, StyleActivity.class);
            intent.putExtra("selCollectionname", getIntent().getExtras().getString("selCollectionname"));
            intent.putExtra("selOptionName", getIntent().getExtras().getString("selOptionName"));
            startActivity(intent);
            finish();
        }
    }
}