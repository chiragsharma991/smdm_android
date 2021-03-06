package apsupportapp.aperotechnologies.com.designapp;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;


import apsupportapp.aperotechnologies.com.designapp.BestPerformersInventory.BestPerformerInventory;
import apsupportapp.aperotechnologies.com.designapp.BestPerformersPromo.BestPerformerActivity;
import apsupportapp.aperotechnologies.com.designapp.Collaboration.Status.StatusActivity;
import apsupportapp.aperotechnologies.com.designapp.Collaboration.to_do.To_Do;
import apsupportapp.aperotechnologies.com.designapp.ExpiringPromo.ExpiringPromoActivity;
import apsupportapp.aperotechnologies.com.designapp.Feedback.Feedback;
import apsupportapp.aperotechnologies.com.designapp.Feedback.FeedbackList;
import apsupportapp.aperotechnologies.com.designapp.FloorAvailability.FloorAvailabilityActivity;
import apsupportapp.aperotechnologies.com.designapp.FreshnessIndex.FreshnessIndexActivity;
import apsupportapp.aperotechnologies.com.designapp.HorlyAnalysis.KeyProductActivity;
import apsupportapp.aperotechnologies.com.designapp.KeyProductPlan.KeyProductPlanActivity;
import apsupportapp.aperotechnologies.com.designapp.OptionEfficiency.OptionEfficiencyActivity;
import apsupportapp.aperotechnologies.com.designapp.PvaSalesAnalysis.SalesPvAActivity;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesAnalysisActivity1;

import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesFilterActivity;
import apsupportapp.aperotechnologies.com.designapp.SellThruExceptions.SaleThruInventory;
import apsupportapp.aperotechnologies.com.designapp.SkewedSize.SkewedSizesActivity;
import apsupportapp.aperotechnologies.com.designapp.StockAgeing.StockAgeingActivity;
import apsupportapp.aperotechnologies.com.designapp.TargetStockExceptions.TargetStockExceptionActivity;
import apsupportapp.aperotechnologies.com.designapp.UpcomingPromo.UpcomingPromo;
import apsupportapp.aperotechnologies.com.designapp.VisualAssortmentSwipe.VisualAssortmentActivity;
import apsupportapp.aperotechnologies.com.designapp.VisualAssortmentSwipe.VisualReportActivity;
import apsupportapp.aperotechnologies.com.designapp.WorstPerformersInventory.WorstPerformerInventory;
import apsupportapp.aperotechnologies.com.designapp.model.EtlStatus;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class DashBoardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {

    ImageButton imageBtnStyle, imageBtnKeyProducts, imgBtnSales, imgBtnVisualAssortment,ActualKeyProduct;
    ImageButton btnFloorAvailability,btnTargetStockExcep,btnSellThruExcep,btnVisualReport;
    ImageButton imgBtnPvaAnalysis,imgBtnRunningPromo,BtnUpcomingpromo,BtnExpiringpromo,BtnBestWorstpromo,btnBestPerformersInv;
    ImageButton btnFeshnessindex,BtnOnlyWorstpromo,btnOptionEfficiency,To_do_image_button,Status_image_button,
            btnSkewedSize,btnCutSize,btnStockAgeing,BtnWorstPerformers,FeedbackList_btn,Feedback_btn;


    LinearLayout hourlyFlash,productInfo,visualAssort,sales,promoAnalysis,inventory,linplanactual,Collaboration_subView,Feedback_linear;
    TextView hourlyFlashTxt,productInfoTxt,visualAssortTxt,salesTxt,promoAnalysisTxt,inventoryTxt,RefreshTime,planvsActualtxt,Collaboration,Feedback;

    //ExpandableHeightGridView style_grid;
    EventAdapter eventAdapter;
    String hrflash="NO";
    String pdInfo="NO";
    String TAG="DashBoardActivity";
    String vsAssort="NO";
    String sAles="NO";
    String pmAnalysis="NO";
    String inVENtory="NO";
    String planActual= "NO";
    String Collab="NO";
    String feedback_flag="NO";
    RequestQueue queue;
    boolean flag=true;
    String userId, bearertoken;
    SharedPreferences sharedPreferences;
    //private Boolean exit = false;
    ArrayList<String> arrayList,eventUrlList;
    Context context;
    MySingleton m_config;


    ArrayList<ProductNameBean> productNameBeanArrayList;


    //Event ViewPager

    ViewPager pager;
    PagerAdapter adapter;
    ImageView imgdot;
    LinearLayout li;
    Timer timer;
    int page = 0;


    //variable for storing collection list in style activity in searchable spinner
    public static List _collectionitems;
    private boolean Promo=false;
    private boolean HourlyFlash=false;
    private boolean ProductInfo=false;
    private boolean VisualAssort=false;
    private boolean Sales=false;
    private boolean Inventory=false;
    private boolean PlanActual = false;
    private boolean Collab_bool=false;
    private boolean feedback_bool=false;
    private Gson gson;
    private EtlStatus etlStatus;
    private ArrayList<EtlStatus>etlStatusList;

    //git tese 10/1/2017


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        _collectionitems = new ArrayList();

        double val=0.3663;
        Log.e("val",String.format("%.1f", val));

        context = this;
        m_config= MySingleton.getInstance(context);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        userId = sharedPreferences.getString("userId","");
        bearertoken = sharedPreferences.getString("bearerToken","");

        Log.e(TAG,"userId"+userId);
        Log.e(TAG,"bearertoken"+bearertoken);

        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();

        arrayList = new ArrayList<>();
        eventUrlList = new ArrayList<>();
        productNameBeanArrayList=new ArrayList<>();
        Bundle data=getIntent().getExtras();
        gson = new Gson();



        //to call Collection API
//        at merging
//        Bundle bundle = getIntent().getExtras();
//        userId = bundle.getString("userId");
//        Log.d("userId", "  " + userId);





//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);


        initializeUI();







        //Marketing events API
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading events...");
            requestMarketingEventsAPI();
        } else {
            // Reusable_Functions.hDialog();
            Toast.makeText(DashBoardActivity.this, "Check your network connectivity", Toast.LENGTH_LONG).show();
        }

//        BtnOnlyWorstpromo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(DashBoardActivity.this, WorstPerformerActivity.class);
//                startActivity(intent);
//                if(timer != null)
//                {
//                    timer.cancel();
//                }
//               // finish();
//            }
//        });

        btnSkewedSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this, SkewedSizesActivity.class);
                startActivity(intent);
                if(timer != null)
                {
                    timer.cancel();
                }
                // finish();
            }
        });
        BtnWorstPerformers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this, WorstPerformerInventory.class);
                startActivity(intent);
                if(timer != null)
                {
                    timer.cancel();
                }
                //  finish();
            }
        });
//        btnCutSize.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(DashBoardActivity.this, TopFullCut.class);
//                startActivity(intent);
//                if(timer != null)
//                {
//                    timer.cancel();
//                }
//                // finish();
//            }
//        });


        BtnBestWorstpromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DashBoardActivity.this, BestPerformerActivity.class);
                startActivity(intent);
                Log.e(TAG, "btn best: log " );

                if(timer != null)
                {
                    timer.cancel();
                }
                // finish();
            }
        });

        BtnExpiringpromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DashBoardActivity.this, ExpiringPromoActivity.class);
                startActivity(intent);
                if(timer != null)
                {
                    timer.cancel();
                }
                // finish();
            }
        });

        To_do_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new To_Do().StartIntent(DashBoardActivity.this);
                if(timer != null)
                {
                    timer.cancel();
                }
                // finish();
            }
        });

        Status_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new StatusActivity().StartIntent(DashBoardActivity.this);
                if(timer != null)
                {
                    timer.cancel();
                }
                // finish();
            }
        });

        Feedback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Feedback().StartIntent(DashBoardActivity.this);
                if(timer != null)
                {
                    timer.cancel();
                }
                // finish();
            }
        });

        FeedbackList_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FeedbackList().StartIntent(DashBoardActivity.this);
                if(timer != null)
                {
                    timer.cancel();
                }
            }
        });

        BtnUpcomingpromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DashBoardActivity.this, UpcomingPromo.class);
                startActivity(intent);
                if(timer != null)
                {
                    timer.cancel();
                }
                // finish();
            }
        });


        imgBtnRunningPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DashBoardActivity.this, StatusActivity.class);
                startActivity(intent);
                if(timer != null)
                {
                    timer.cancel();
                }
                //  finish();
            }
        });





        imageBtnStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                requestArticleOptionsAPI();
                Intent intent = new Intent(DashBoardActivity.this, StyleActivity.class);
                startActivity(intent);
                if(timer != null)
                {
                    timer.cancel();
                }
                //   finish();

            }
        });

        imageBtnKeyProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this,KeyProductActivity.class);
                startActivity(intent);
                if(timer != null)
                {
                    timer.cancel();
                }
                //  finish();

            }

        });


        imgBtnSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this,SalesAnalysisActivity1.class);
                startActivity(intent);
                if(timer != null)
                {
                    timer.cancel();
                }
                //  finish();
            }
        });

        imgBtnPvaAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this,SalesPvAActivity.class);
                startActivity(intent);
                if(timer != null)
                {
                    timer.cancel();
                }
                //   finish();
            }
        });

        imgBtnVisualAssortment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this,VisualAssortmentActivity.class);
                startActivity(intent);
                if(timer != null)
                {
                    timer.cancel();
                }

                //  finish();
            }
        });
        btnFeshnessindex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this,FreshnessIndexActivity.class);
                startActivity(intent);
                if(timer != null)
                {
                    timer.cancel();
                }

                //   finish();
            }
        });
        btnOptionEfficiency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this,OptionEfficiencyActivity.class);
                startActivity(intent);
                if(timer != null)
                {
                    timer.cancel();
                }

                //   finish();
            }
        });
        btnStockAgeing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this,StockAgeingActivity.class);
                startActivity(intent);
                if(timer != null)
                {
                    timer.cancel();
                }

                //   finish();
            }
        });
        btnBestPerformersInv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this,BestPerformerInventory.class);
                startActivity(intent);
                if(timer != null)
                {
                    timer.cancel();
                }

                //    finish();
            }
        });
        btnFloorAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this,FloorAvailabilityActivity.class);
                startActivity(intent);
                if(timer != null)
                {
                    timer.cancel();
                }

                //  finish();
            }
        });
        btnTargetStockExcep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this,TargetStockExceptionActivity.class);
                startActivity(intent);
                if(timer != null)
                {
                    timer.cancel();
                }

                //   finish();
            }
        });
        btnSellThruExcep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this,SaleThruInventory.class);
                startActivity(intent);
                if(timer != null)
                {
                    timer.cancel();
                }

                //    finish();
            }
        });
        btnVisualReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this,VisualReportActivity.class);
                startActivity(intent);
                if(timer != null)
                {
                    timer.cancel();
                }
            }
        });   ActualKeyProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this,KeyProductPlanActivity.class);
                startActivity(intent);
                if(timer != null)
                {
                    timer.cancel();
                }
            }
        });

        RefreshTimeAPI();

    }


    @Override
    protected void onStart() {
        super.onStart();
        if(Promo)
        {
            promoAnalysis.setVisibility(View.VISIBLE);

        }else
        {
            promoAnalysis.setVisibility(View.GONE);

        }
        if(HourlyFlash)
        {
            hourlyFlash.setVisibility(View.VISIBLE);

        }else
        {
            hourlyFlash.setVisibility(View.GONE);

        }
        if(ProductInfo)
        {
            productInfo.setVisibility(View.VISIBLE);

        }else
        {
            productInfo.setVisibility(View.GONE);

        }
        if(VisualAssort)
        {
            visualAssort.setVisibility(View.VISIBLE);

        }else
        {
            visualAssort.setVisibility(View.GONE);

        }
        if(Sales)
        {
            sales.setVisibility(View.VISIBLE);

        }else
        {
            sales.setVisibility(View.GONE);

        }
        if(Inventory)
        {
            inventory.setVisibility(View.VISIBLE);

        }else
        {
            inventory.setVisibility(View.GONE);

        } if(Collab_bool)
        {
            Collaboration_subView.setVisibility(View.VISIBLE);

        }else
        {
            Collaboration_subView.setVisibility(View.GONE);

        }
        if(PlanActual)
        {
            linplanactual.setVisibility(View.VISIBLE);
        }else
        {
            linplanactual.setVisibility(View.GONE);
        }

        if(feedback_bool)
        {
            Feedback_linear.setVisibility(View.VISIBLE);

        }else
        {
            Feedback_linear.setVisibility(View.GONE);

        }





    }

    private void requestMarketingEventsAPI() {

        String url = ConstsCore.web_url + "/v1/display/dashboard/" + userId;

        //String url = "https://ra.manthan.com/v1/display/dashboard/270389" ;
        Log.i("URL   ", url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("MarketingEvent Response", response.toString());
                        try {


                            if (response.equals(null) || response == null) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(DashBoardActivity.this, "No data found", Toast.LENGTH_LONG).show();
                            } else {
                                for (int i = 0; i < response.length(); i++)
                                {
                                    JSONObject jsonOject = response.getJSONObject(i);
                                    String imageURL = jsonOject.getString("imageName");
                                    // Log.e("imageURL", "\"+""+imageURL+""+\"");
                                    eventUrlList.add(imageURL);

                                }

                                //eventAdapter = new EventAdapter(DashBoardActivity.this, eventUrlList);
                                //style_grid.setAdapter(eventAdapter);
                                EventScroller();




                            }

                        } catch (Exception e) {
                            Log.e("Exception e", e.toString() + "");
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Reusable_Functions.hDialog();
                        error.printStackTrace();
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer "+bearertoken);

                Log.e("params "," "+params);
                return params;
            }
        };
        int socketTimeout = 60000;//5 seconds

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);



    }


    private void initializeUI() {


        hourlyFlashTxt=(TextView)findViewById(R.id.headersmdm);
        productInfoTxt=(TextView)findViewById(R.id.productinfo);
        visualAssortTxt=(TextView)findViewById(R.id.visualAssort);
        salesTxt=(TextView)findViewById(R.id.headersales);

        promoAnalysisTxt=(TextView)findViewById(R.id.headerpromo);
        inventoryTxt=(TextView)findViewById(R.id.headerinvent);
        RefreshTime=(TextView)findViewById(R.id.refreshTime);
        planvsActualtxt = (TextView)findViewById(R.id.headerplanactual) ;
        Collaboration=(TextView)findViewById(R.id.collaboration);
        Feedback=(TextView)findViewById(R.id.feedback);

        hourlyFlashTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
        productInfoTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
        visualAssortTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
        salesTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
        promoAnalysisTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
        inventoryTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
        planvsActualtxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
        inventoryTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
        inventoryTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
        Collaboration.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
        Feedback.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);

        hourlyFlashTxt.setOnClickListener(this);
        productInfoTxt.setOnClickListener(this);
        visualAssortTxt.setOnClickListener(this);
        salesTxt.setOnClickListener(this);
        promoAnalysisTxt.setOnClickListener(this);
        inventoryTxt.setOnClickListener(this);
        Collaboration.setOnClickListener(this);
        Feedback.setOnClickListener(this);

        planvsActualtxt.setOnClickListener(this);



        hourlyFlash=(LinearLayout)findViewById(R.id.lin1);
        productInfo=(LinearLayout)findViewById(R.id.lineartwo);
        visualAssort=(LinearLayout)findViewById(R.id.linearthree);
        sales=(LinearLayout)findViewById(R.id.lin2);
        promoAnalysis=(LinearLayout)findViewById(R.id.lin3);
        inventory=(LinearLayout)findViewById(R.id.lin4);
        Collaboration_subView=(LinearLayout)findViewById(R.id.collaboration_subView);
        Feedback_linear=(LinearLayout)findViewById(R.id.feedback_linear);
        linplanactual = (LinearLayout)findViewById(R.id.linplanactual);

        imageBtnStyle=(ImageButton)findViewById(R.id.imageBtnStyle);
        imageBtnKeyProducts=(ImageButton)findViewById(R.id.imageBtnKeyProducts);
        imgBtnSales = (ImageButton) findViewById(R.id.btnSales);
        imgBtnVisualAssortment = (ImageButton) findViewById(R.id.btnVisualAssortment);
        ActualKeyProduct = (ImageButton) findViewById(R.id.actualKeyProduct);
        imgBtnPvaAnalysis = (ImageButton) findViewById(R.id.btnPVA);
        imgBtnRunningPromo=(ImageButton)findViewById(R.id.btnRunningpromo);
        BtnUpcomingpromo=(ImageButton)findViewById(R.id.btnUpcomingpromo);
        BtnExpiringpromo=(ImageButton)findViewById(R.id.btnExpiringpromo);
        BtnBestWorstpromo=(ImageButton)findViewById(R.id.btnBestWorstpromo);
        //BtnOnlyWorstpromo=(ImageButton)findViewById(R.id.btnOnlyWorstpromo);
        BtnWorstPerformers=(ImageButton)findViewById(R.id.btnWorstPerformers);
        btnFeshnessindex=(ImageButton)findViewById(R.id.btnFeshnessindex);
        btnOptionEfficiency = (ImageButton)findViewById(R.id.btnOptionEfficiency);
   //     btnCutSize = (ImageButton)findViewById(R.id.btnCutSize);
        btnSkewedSize =(ImageButton)findViewById(R.id.btnSkewedSize);
        btnStockAgeing = (ImageButton)findViewById(R.id.btnStockAgeing);
        btnBestPerformersInv = (ImageButton)findViewById(R.id.btnBestPerformers);
        btnFloorAvailability =(ImageButton)findViewById(R.id.btnFloorAvailability);
        btnTargetStockExcep = (ImageButton)findViewById(R.id.btnTargetStockExcep);
        btnSellThruExcep = (ImageButton)findViewById(R.id.btnSellThruExcep);
        btnVisualReport = (ImageButton)findViewById(R.id.btnVisualReport);
        To_do_image_button = (ImageButton)findViewById(R.id.to_do);
        Status_image_button = (ImageButton)findViewById(R.id.status);
        Feedback_btn = (ImageButton)findViewById(R.id.feedback_btn);
        FeedbackList_btn = (ImageButton)findViewById(R.id.feedbackList_btn);
//        style_grid = (ExpandableHeightGridView) findViewById(R.id.spotsView);
//        style_grid.setExpanded(true);

        pager = (ViewPager) findViewById(R.id.viewpager);

        TabLayout tab=(TabLayout)findViewById(R.id.dotTab_dashboard);
        tab.setupWithViewPager(pager, true);


        li = (LinearLayout) findViewById(R.id.lill);
        li.setOrientation(LinearLayout.HORIZONTAL);

        final  ScrollView scrollview=(ScrollView)findViewById(R.id.scrollView);

        scrollview.post(new Runnable() {

            public void run() {
                scrollview.fullScroll(View.FOCUS_UP);
                //scrollview.pageScroll(View.FOCUS_UP);
            }
        });



    }


    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
            SalesFilterActivity.level_filter = 1;
            SalesAnalysisActivity1.selectedsegValue = null;
            SalesAnalysisActivity1.level = 1;
            Intent intent = new Intent(DashBoardActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
            NotificationManager notifManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notifManager.cancelAll();
            return true;
        }
        else if(id == R.id.aboutus)
        {
            Intent intent = new Intent(DashBoardActivity.this,AboutUsActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void RefreshTimeAPI()
    {
        String url = ConstsCore.web_url+"/v1/display/etlstatus/"+userId;
        Log.e(TAG, "requestLoginAPI: "+url);
        etlStatusList=new ArrayList<EtlStatus>();

        // final String password = sharedPreferences.getString("password","");
        //  final String auth_code = sharedPreferences.getString("authcode","");

        // Log.e("authcode"," "+auth_code);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        Log.i(TAG,"Login   Response   "+response.toString()+"\n length is"+response.length());
                        try
                        {
                            if(response == null || response.equals(null))
                            {
                                RefreshTime.setText("N/A");
                            }else
                            {

                                for (int i = 0; i < response.length(); i++) {

                                    etlStatus = gson.fromJson(response.get(i).toString(), EtlStatus.class);
                                    etlStatusList.add(etlStatus);

                                }
                                RefreshTime.setText(etlStatusList.get(0).getLastETLDate());
                            }

//                            String bearerToken = response.getString("bearerToken");
//                            SharedPreferences.Editor editor = sharedPreferences.edit();
//                            editor.putString("bearerToken",bearerToken);
//                            editor.apply();
//
//                            //Marketing events API
//                            requestMarketingEventsAPI();



                        }
                        catch(Exception e)
                        {
                            Log.e(TAG,"Exception e =  "+e.getMessage());
                            RefreshTime.setText("N/A");

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)

                    {
                        Log.e(TAG,"Response.ErrorListener e"+error.getMessage());
                        RefreshTime.setText("N/A");

                        error.printStackTrace();
                    }
                }

        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                //String auth_code = "Basic " + Base64.encodeToString((uname+":"+password).getBytes(), Base64.NO_WRAP); //Base64.NO_WRAP flag
                //  Log.i("Auth Code", auth_code);
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + bearertoken);
                return params;


            }
        };
        int socketTimeout = 60000;//5 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }


    private void EventScroller()
    {

        Log.e("eventURLLIST"," "+eventUrlList);
        for (int i = 0; i < eventUrlList.size(); i++) {

            imgdot = new ImageView(this);//new View(DashBoardActivity.this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
            layoutParams.setMargins(3, 3, 3, 3);
            imgdot.setLayoutParams(layoutParams);
            //imgdot.setBackgroundColor(Color.parseColor("#666666"));
            imgdot.setImageResource(R.mipmap.dots_unselected);
            li.addView(imgdot);

        }

        // Pass results to ToDoViewPagerAdapter Class
        adapter = new EventPagerAdapter(DashBoardActivity.this, eventUrlList, li, pager);
        // Binds the Adapter to the ViewPager
        pager.setAdapter(adapter);
        Reusable_Functions.hDialog();
        pageSwitcher(10);
    }

    public void pageSwitcher(int seconds) {
        timer = new Timer(); // At this line a new Thread will be created
        timer.scheduleAtFixedRate(new RemindTask(), 0, seconds * 1000); // delay

    }

    @Override
    public void onClick(View v)
    {
        Log.e(TAG,"on click on log");

        switch (v.getId())
        {


            case R.id.headersmdm:
                if(hrflash.equals("NO")){
                    hourlyFlash.setVisibility(View.VISIBLE);
                    productInfo.setVisibility(View.GONE);
                    visualAssort.setVisibility(View.GONE);
                    sales.setVisibility(View.GONE);
                    promoAnalysis.setVisibility(View.GONE);
                    inventory.setVisibility(View.GONE);
                    linplanactual.setVisibility(View.GONE);
                    Collaboration_subView.setVisibility(View.GONE);
                    Feedback_linear.setVisibility(View.GONE);
                    Log.e(TAG, "onClick:  headersmdm" );

                    hourlyFlashTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.uplist,0);
                    productInfoTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    visualAssortTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    salesTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    promoAnalysisTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    inventoryTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    Collaboration.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    planvsActualtxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    Feedback.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);

                    hrflash="YES";
                    pdInfo="NO";
                    vsAssort="NO";
                    sAles="NO";
                    pmAnalysis="NO";
                    inVENtory="NO";
                    planActual = "NO";
                    feedback_flag="NO";

                    HourlyFlash=true;
                    Promo=false;
                    ProductInfo=false;
                    VisualAssort=false;
                    Sales=false;
                    Inventory=false;
                    Collab_bool=false;
                    feedback_bool=false;
                    Collab="NO";
                    PlanActual = false;

                }else
                {
                    hourlyFlash.setVisibility(View.GONE);
                    hourlyFlashTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    hrflash="NO";
                    HourlyFlash=false;

                }
                break;

            case R.id.productinfo:
                if(pdInfo.equals("NO")){
                    hourlyFlash.setVisibility(View.GONE);
                    productInfo.setVisibility(View.VISIBLE);
                    visualAssort.setVisibility(View.GONE);
                    sales.setVisibility(View.GONE);
                    promoAnalysis.setVisibility(View.GONE);
                    inventory.setVisibility(View.GONE);
                    linplanactual.setVisibility(View.GONE);
                    Feedback_linear.setVisibility(View.GONE);
                    Collaboration_subView.setVisibility(View.GONE);

                    hourlyFlashTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    productInfoTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.uplist,0);
                    visualAssortTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    salesTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    promoAnalysisTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    inventoryTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    Collaboration.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    planvsActualtxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    Feedback.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    Log.e(TAG, "onClick:  productinfo" );


                    pdInfo="YES";
                    hrflash="NO";
                    vsAssort="NO";
                    sAles="NO";
                    pmAnalysis="NO";
                    inVENtory="NO";
                    planActual="NO";
                    feedback_flag="NO";

                    ProductInfo=true;
                    HourlyFlash=false;
                    Promo=false;
                    VisualAssort=false;
                    Sales=false;
                    Inventory=false;
                    PlanActual=false;
                    feedback_bool=false;
                    Collab_bool=false;
                    Collab="NO";

                }else
                {
                    productInfo.setVisibility(View.GONE);
                    productInfoTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    pdInfo="NO";
                    ProductInfo=false;


                }
                break;

            case R.id.visualAssort:
                if(vsAssort.equals("NO")){
                    hourlyFlash.setVisibility(View.GONE);
                    productInfo.setVisibility(View.GONE);
                    visualAssort.setVisibility(View.VISIBLE);
                    sales.setVisibility(View.GONE);
                    promoAnalysis.setVisibility(View.GONE);
                    inventory.setVisibility(View.GONE);
                    linplanactual.setVisibility(View.GONE);
                    Collaboration_subView.setVisibility(View.GONE);
                    Feedback_linear.setVisibility(View.GONE);
                    Log.e(TAG, "onClick:  visualAssort" );

                    hourlyFlashTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    productInfoTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    visualAssortTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.uplist,0);
                    salesTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    promoAnalysisTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    inventoryTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    Collaboration.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    planvsActualtxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    Feedback.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);

                    vsAssort="YES";
                    hrflash="NO";
                    pdInfo="NO";
                    sAles="No";
                    pmAnalysis="NO";
                    inVENtory="NO";
                    planActual = "NO";
                    feedback_flag = "NO";

                    VisualAssort=true;
                    HourlyFlash=false;
                    Promo=false;
                    ProductInfo=false;
                    Sales=false;
                    Inventory=false;
                    Collab_bool=false;
                    feedback_bool=false;
                    Collab="NO";

                    PlanActual=false;
                }else
                {
                    visualAssort.setVisibility(View.GONE);
                    visualAssortTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    vsAssort="NO";
                    VisualAssort=false;

                } break;

            case R.id.headersales:
                if(sAles.equals("NO")){
                    hourlyFlash.setVisibility(View.GONE);
                    productInfo.setVisibility(View.GONE);
                    visualAssort.setVisibility(View.GONE);
                    sales.setVisibility(View.VISIBLE);
                    promoAnalysis.setVisibility(View.GONE);
                    inventory.setVisibility(View.GONE);
                    linplanactual.setVisibility(View.GONE);
                    Feedback_linear.setVisibility(View.GONE);

                    Collaboration_subView.setVisibility(View.GONE);
                    Log.e(TAG, "onClick:  headersales" );
                    hourlyFlashTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    productInfoTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    visualAssortTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    salesTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.uplist,0);
                    promoAnalysisTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    inventoryTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    Collaboration.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    planvsActualtxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    Feedback.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);

                    sAles="YES";
                    hrflash="NO";
                    pdInfo="NO";
                    vsAssort="NO";
                    pmAnalysis="NO";
                    inVENtory="NO";
                    planActual="NO";
                    feedback_flag="NO";

                    Sales=true;
                    HourlyFlash=false;
                    Promo=false;
                    ProductInfo=false;
                    VisualAssort=false;
                    Inventory=false;
                    Collab_bool=false;
                    feedback_bool=false;
                    Collab="NO";
                    PlanActual=false;
                }else
                {
                    sales.setVisibility(View.GONE);
                    salesTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    sAles="NO";
                    Sales=false;

                } break;

            case R.id.headerpromo:
                if(pmAnalysis.equals("NO")){
                    hourlyFlash.setVisibility(View.GONE);
                    productInfo.setVisibility(View.GONE);
                    visualAssort.setVisibility(View.GONE);
                    sales.setVisibility(View.GONE);
                    promoAnalysis.setVisibility(View.VISIBLE);
                    inventory.setVisibility(View.GONE);
                    Collaboration_subView.setVisibility(View.GONE);
                    linplanactual.setVisibility(View.GONE);
                    Feedback_linear.setVisibility(View.GONE);

                    hourlyFlashTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    productInfoTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    visualAssortTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    salesTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    promoAnalysisTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.uplist,0);
                    inventoryTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    Collaboration.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    planvsActualtxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    Feedback.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);

                    pmAnalysis="YES";
                    hrflash="NO";
                    pdInfo="NO";
                    vsAssort="NO";
                    sAles="NO";
                    inVENtory="NO";
                    feedback_flag="NO";
                    planActual="NO";

                    HourlyFlash=false;
                    Promo=true;
                    ProductInfo=false;
                    VisualAssort=false;
                    Sales=false;
                    Inventory=false;
                    feedback_bool=false;
                    Collab_bool=false;
                    Collab="NO";

                    PlanActual=false;
                    Log.e(TAG, "onClick:  headerpromo" );

                }else
                {
                    promoAnalysis.setVisibility(View.GONE);
                    promoAnalysisTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    pmAnalysis="NO";
                    Promo=false;
                } break;

            case R.id.headerinvent:
                if(inVENtory.equals("NO")){
                    hourlyFlash.setVisibility(View.GONE);
                    productInfo.setVisibility(View.GONE);
                    visualAssort.setVisibility(View.GONE);
                    sales.setVisibility(View.GONE);
                    promoAnalysis.setVisibility(View.GONE);
                    inventory.setVisibility(View.VISIBLE);
                    linplanactual.setVisibility(View.GONE);
                    Feedback_linear.setVisibility(View.GONE);
                    Collaboration_subView.setVisibility(View.GONE);


                    hourlyFlashTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    productInfoTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    visualAssortTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    salesTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    promoAnalysisTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    inventoryTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.uplist,0);
                    planvsActualtxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    Feedback.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    Collaboration.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);

                    inVENtory="YES";
                    hrflash="NO";
                    pdInfo="NO";
                    vsAssort="NO";
                    Collab="NO";
                    sAles="NO";
                    feedback_flag="NO";
                    pmAnalysis="NO";
                    planActual="NO";

                    HourlyFlash=false;
                    Promo=false;
                    ProductInfo=false;
                    VisualAssort=false;
                    Sales=false;
                    Inventory=true;
                    Collab_bool=false;
                    feedback_bool=false;
                    PlanActual=false;
                    Log.e(TAG, "onClick:  headerinvent" );

                }else
                {
                    inventory.setVisibility(View.GONE);
                    inventoryTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    inVENtory="NO";
                    Inventory=false;
                } break;


            case R.id.collaboration:
                if(Collab.equals("NO")){
                    hourlyFlash.setVisibility(View.GONE);
                    productInfo.setVisibility(View.GONE);
                    visualAssort.setVisibility(View.GONE);
                    sales.setVisibility(View.GONE);
                    promoAnalysis.setVisibility(View.GONE);
                    inventory.setVisibility(View.GONE);
                    linplanactual.setVisibility(View.GONE);
                    Feedback_linear.setVisibility(View.GONE);
                    Collaboration_subView.setVisibility(View.VISIBLE);

                    hourlyFlashTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    productInfoTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    visualAssortTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    salesTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    promoAnalysisTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    Feedback.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    inventoryTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    Collaboration.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.uplist,0);
                    planvsActualtxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);

                    inVENtory="No";
                    hrflash="NO";
                    pdInfo="NO";
                    vsAssort="NO";
                    sAles="NO";
                    feedback_flag="NO";
                    pmAnalysis="NO";
                    Collab="YES";
                    planActual = "NO";
                    HourlyFlash=false;
                    Promo=false;
                    ProductInfo=false;
                    VisualAssort=false;
                    Sales=false;
                    Inventory=false;
                    feedback_bool=false;
                    PlanActual = false;
                    Collab_bool=true;
                    Log.e(TAG, "onClick:  Collaboration event" );

                }else
                {
                    Collaboration_subView.setVisibility(View.GONE);
                    Collaboration.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    Collab="NO";
                    Collab_bool=false;
                } break;



            case R.id.headerplanactual:
                if(planActual.equals("NO")){
                    hourlyFlash.setVisibility(View.GONE);
                    productInfo.setVisibility(View.GONE);
                    visualAssort.setVisibility(View.GONE);
                    sales.setVisibility(View.GONE);
                    promoAnalysis.setVisibility(View.GONE);
                    inventory.setVisibility(View.GONE);
                    Feedback_linear.setVisibility(View.GONE);
                    linplanactual.setVisibility(View.VISIBLE);
                    Collaboration_subView.setVisibility(View.GONE);
                    hourlyFlashTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    productInfoTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    visualAssortTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    salesTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    Feedback.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    promoAnalysisTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    inventoryTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    planvsActualtxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.uplist,0);
                    Collaboration.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);

                    planActual = "YES";
                    inVENtory="NO";
                    hrflash="NO";
                    pdInfo="NO";
                    vsAssort="NO";
                    feedback_flag="NO";
                    sAles="NO";
                    pmAnalysis="NO";
                    Collab="NO";
                    Collab_bool=false;
                    HourlyFlash=false;
                    Promo=false;
                    ProductInfo=false;
                    VisualAssort=false;
                    feedback_bool=false;
                    Sales=false;
                    Inventory=false;
                    PlanActual = true;
                    Log.e(TAG, "onClick:  headerplanActual" );

                }else
                {
                    linplanactual.setVisibility(View.GONE);
                    planvsActualtxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    planActual="NO";
                    PlanActual=false;
                } break;



            case R.id.feedback:
                if(feedback_flag.equals("NO")){
                    hourlyFlash.setVisibility(View.GONE);
                    productInfo.setVisibility(View.GONE);
                    visualAssort.setVisibility(View.GONE);
                    sales.setVisibility(View.GONE);
                    promoAnalysis.setVisibility(View.GONE);
                    inventory.setVisibility(View.GONE);
                    Feedback_linear.setVisibility(View.VISIBLE);
                    linplanactual.setVisibility(View.GONE);
                    Collaboration_subView.setVisibility(View.GONE);
                    hourlyFlashTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    productInfoTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    visualAssortTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    salesTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    Feedback.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.uplist,0);
                    promoAnalysisTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    inventoryTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    planvsActualtxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    Collaboration.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);

                    planActual = "NO";
                    inVENtory="NO";
                    hrflash="NO";
                    pdInfo="NO";
                    vsAssort="NO";
                    feedback_flag="YES";
                    sAles="NO";
                    pmAnalysis="NO";
                    Collab="NO";
                    Collab_bool=false;
                    HourlyFlash=false;
                    Promo=false;
                    ProductInfo=false;
                    VisualAssort=false;
                    feedback_bool=true;
                    Sales=false;
                    Inventory=false;
                    PlanActual = false;
                    Log.e(TAG, "onClick:  headerplanActual" );

                }else
                {
                    Feedback_linear.setVisibility(View.GONE);
                    Feedback.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.downlist,0);
                    feedback_flag="NO";
                    feedback_bool=false;
                } break;
        }





    }

    // this is an inner class...
    class RemindTask extends TimerTask {

        @Override
        public void run() {

            // As the TimerTask run on a seprate thread from UI thread we have
            // to call runOnUiThread to do work on UI thread.
            runOnUiThread(new Runnable() {
                public void run() {

                    if (page == eventUrlList.size() - 1) { // In my case the number of pages are 5
                        page = 0;
                        pager.setCurrentItem(0);
                    } else
                    {
                        page = page + 1;
                        pager.setCurrentItem(page);
                    }
                }
            });

        }
    }

}
