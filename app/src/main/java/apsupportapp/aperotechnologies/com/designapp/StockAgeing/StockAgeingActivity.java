package apsupportapp.aperotechnologies.com.designapp.StockAgeing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.DashBoardActivity;
import apsupportapp.aperotechnologies.com.designapp.FreshnessIndex.InventoryFilterActivity;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.SalesAnalysis.SalesFilterActivity;
import apsupportapp.aperotechnologies.com.designapp.TopOptionCutSize.TopFullCut;
import apsupportapp.aperotechnologies.com.designapp.TopOptionCutSize.TopOptionAdapter;
import apsupportapp.aperotechnologies.com.designapp.model.OptionEfficiencyDetails;
import apsupportapp.aperotechnologies.com.designapp.model.RunningPromoListDisplay;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by pamrutkar on 05/12/16.
 */

public class StockAgeingActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    TextView stock_txtStoreCode, stock_txtStoreName;
    RelativeLayout stock_BtnBack, stock_BtnFilter, stock_quickFilter, quickFilterPopup, quickFilter_baseLayout, qfDoneLayout, quickFilter_BorderLayout;
    RunningPromoListDisplay StockAgeingListDisplay;
    private SharedPreferences sharedPreferences;
    String userId, bearertoken;
    private static String seasongroup = "Current";
    String TAG = "StockAgeingActivity";
    private int count = 0;
    private int limit = 10;
    private int offsetvalue = 0;
    private int top = 10;
    CheckBox checkAgeing1, checkAgeing2, checkAgeing3;
    RadioButton checkCurrent, checkPrevious, checkOld, checkUpcoming;
    Context context = this;
    private RequestQueue queue;
    private Gson gson;
    ListView StockAgListView;
    ArrayList<RunningPromoListDisplay> StockAgeingList;
    private int focusposition = 0;
    private boolean userScrolled;
    StockAgeingAdapter stockAgeingAdapter;
    private View footer;
    private String lazyScroll = "OFF";
    private SegmentedGroup stock_segmented;
    private RadioButton stock_fashion, stock_core;
    private ToggleButton Toggle_stock_fav;
    private static String corefashion = "Fashion";
    private static String checkSeasonGpVal = null;
    String checkAgeingVal = null;
    private boolean coreSelection = false;
    private boolean from_filter=false;
    private String selectedString="";
    public static Activity stockAgeing;
    private boolean toggleClick=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_ageing);
        getSupportActionBar().hide();
        initalise();
//corefashion  checkSeasonGpVal seasongroup
        gson = new Gson();
        stockAgeing=this;
        StockAgeingList = new ArrayList<RunningPromoListDisplay>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        Log.e(TAG, "userID and token" + userId + "and this is" + bearertoken);
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        StockAgListView.setTag("FOOTER");
        StockAgListView.setVisibility(View.VISIBLE);
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            offsetvalue = 0;
            limit = 10;
            count = 0;
            top = 10;

            if (getIntent().getStringExtra("selectedDept") == null) {
                from_filter = false;
            } else if (getIntent().getStringExtra("selectedDept") != null) {
                selectedString = getIntent().getStringExtra("selectedDept");
                //   selectedString = selectedString.replace(" ","%20");
                from_filter = true;

            }
            RetainFromMain_filter();
            requestStockAgeingApi(selectedString);
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
        // bestPromoAdapter = new BestPromoAdapter(BestpromoList,context);
        footer = getLayoutInflater().inflate(R.layout.bestpromo_footer, null);

        StockAgListView.addFooterView(footer);

    }

    private void RetainFromMain_filter()
    {
        //corefashion  checkSeasonGpVal seasongroup

        toggleClick=true;

        if(corefashion.equals("Fashion"))
        {
            stock_fashion.toggle();
            coreSelection=false;

        }else
        {
            stock_core.toggle();
            coreSelection=true;
        }
        baseclick();
    }

    private void baseclick()
    {
        if (checkSeasonGpVal == null && checkAgeingVal == null) {
            checkCurrent.setChecked(true);
            checkPrevious.setChecked(false);
            checkOld.setChecked(false);
            checkUpcoming.setChecked(false);
            checkAgeing1.setChecked(false);
            checkAgeing2.setChecked(false);
            checkAgeing3.setChecked(false);

        } else {
            switch (checkSeasonGpVal.toString()) {
                case "Current":
                    checkCurrent.setChecked(true);
                    checkPrevious.setChecked(false);
                    checkOld.setChecked(false);
                    checkUpcoming.setChecked(false);

                    Log.e("Current checked", "" + checkCurrent.isChecked());
                    break;

                case "Previous":
                    checkPrevious.setChecked(true);
                    checkCurrent.setChecked(false);
                    checkOld.setChecked(false);
                    checkUpcoming.setChecked(false);
                    Log.e("Previous checked", "" + checkPrevious.isChecked());
                    break;
                case "Old":
                    checkOld.setChecked(true);
                    checkCurrent.setChecked(false);
                    checkPrevious.setChecked(false);
                    checkUpcoming.setChecked(false);
                    Log.e("Old checked", "" + checkOld.isChecked());
                    break;
                case "Upcoming":
                    checkUpcoming.setChecked(true);
                    checkCurrent.setChecked(false);
                    checkOld.setChecked(false);
                    checkPrevious.setChecked(false);
                    Log.e("Upcoming checked", "" + checkUpcoming.isChecked());
                    break;
            }

                 /*   switch (checkAgeingVal.toString()) {
                        case "CheckAgeing1":
                            checkAgeing1.setChecked(true);
                            checkAgeing2.setChecked(false);
                            checkAgeing3.setChecked(false);
                            break;
                        case "CheckAgeing2":
                            checkAgeing2.setChecked(true);
                            checkAgeing1.setChecked(false);
                            checkAgeing3.setChecked(false);
                            break;
                        case "CheckAgeing3":
                            checkAgeing3.setChecked(true);
                            checkAgeing2.setChecked(false);
                            checkAgeing1.setChecked(false);
                            break;
                    }*/
        }
    }

    private void requestStockAgeingApi(final String selectedString) {

        if (Reusable_Functions.chkStatus(context)) {

            String url;
            if (from_filter) {
                if (coreSelection) {

                    //core selection without season params

                    url = ConstsCore.web_url + "/v1/display/stockageing/" + userId + "?offset=" + offsetvalue + "&limit=" + limit +"&level=" + SalesFilterActivity.level_filter + selectedString + "&top=" + top + "&corefashion=" + corefashion ;
                } else {

                    // fashion select with season params

                    url = ConstsCore.web_url + "/v1/display/stockageing/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&level=" + SalesFilterActivity.level_filter + selectedString + "&top=" + top + "&corefashion=" + corefashion + "&seasongroup=" + seasongroup ;
                }
            } else {
                if (coreSelection) {

                    //core selection without season params

                    url = ConstsCore.web_url + "/v1/display/stockageing/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&top=" + top + "&corefashion=" + corefashion ;
                } else {

                    // fashion select with season params

                    url = ConstsCore.web_url + "/v1/display/stockageing/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&top=" + top + "&corefashion=" + corefashion + "&seasongroup=" + seasongroup ;
                }
            }


            // String url = ConstsCore.web_url + "/v1/display/stockageing/" + userId + "?offset=" + offsetvalue + "&limit=" + limit + "&top=" + top + "&corefashion=" + corefashion ;

            Log.e(TAG, "URL" + url);
            final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.i(TAG, "Stock Aging : " + " " + response);
                            Log.i(TAG, "response" + "" + response.length());
                            StockAgListView.setVisibility(View.VISIBLE);
                            try {
                                if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                    Reusable_Functions.hDialog();
                                    Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                                    StockAgListView.removeFooterView(footer);
                                    StockAgListView.setTag("FOOTER_REMOVE");
                                    if (StockAgeingList.size() == 0) {
                                        StockAgListView.setVisibility(View.GONE);
                                    }
                                    return;

                                } else if (response.length() == limit) {
                                    Log.e(TAG, "Top eql limit");
                                    for (int i = 0; i < response.length(); i++) {

                                        StockAgeingListDisplay = gson.fromJson(response.get(i).toString(), RunningPromoListDisplay.class);
                                        StockAgeingList.add(StockAgeingListDisplay);

                                    }
                                    offsetvalue = offsetvalue + 10;
                                    top = top + 10;
                                    //  count++;

                                    // requestStockAgeingApi();

                                } else if (response.length() < limit) {
                                    Log.e(TAG, "promo /= limit");
                                    for (int i = 0; i < response.length(); i++) {

                                        StockAgeingListDisplay = gson.fromJson(response.get(i).toString(), RunningPromoListDisplay.class);
                                        StockAgeingList.add(StockAgeingListDisplay);

                                    }
                                    offsetvalue = offsetvalue + 10;
                                    top = top + 10;
                                }


                           /* if(popPromo==10)
                            {
                                topOptionAdapter = new TopOptionAdapter(TopOptionList,context);
                                TopOptionListView.setAdapter(topOptionAdapter);
                                popPromo=0;

                            }*/

                                if (lazyScroll.equals("ON")) {
                                    Log.i(TAG, "Set Lazy scroll and notify are ON : ");
                                    stockAgeingAdapter.notifyDataSetChanged();
                                    lazyScroll = "OFF";
                                    footer.setVisibility(View.GONE);


                                } else {
                                    stockAgeingAdapter = new StockAgeingAdapter(StockAgeingList, context);
                                    StockAgListView.setAdapter(stockAgeingAdapter);
                                    Log.i(TAG, "Set Adapter calling: ");
                                    stock_txtStoreCode.setText(StockAgeingList.get(0).getStoreCode());
                                    stock_txtStoreName.setText(StockAgeingList.get(0).getStoreDescription());


                                }


                                Reusable_Functions.hDialog();
                            } catch (Exception e) {


                                StockAgeingList.clear();
                                stockAgeingAdapter.notifyDataSetChanged();
                                StockAgListView.setVisibility(View.GONE);
                                Reusable_Functions.hDialog();
                                StockAgListView.removeFooterView(footer);
                                StockAgListView.setTag("FOOTER_REMOVE");
                                Toast.makeText(context, "Data failed...", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                                Log.e(TAG, "catch...Error" + e.toString());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Reusable_Functions.hDialog();
                            Toast.makeText(context, "Server not found...", Toast.LENGTH_SHORT).show();
                            StockAgListView.removeFooterView(footer);
                            StockAgListView.setTag("FOOTER_REMOVE");
                         //   Log.e(TAG, "onErrorResponse: " + error.getMessage().toString());
                            StockAgListView.setVisibility(View.GONE);

                            error.printStackTrace();
                        }
                    }

            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
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


//---------------seton Click list listener------------------//

            StockAgListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                public int VisibleItemCount, TotalItemCount, FirstVisibleItem;

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                    if (FirstVisibleItem + VisibleItemCount == TotalItemCount && scrollState == SCROLL_STATE_IDLE && lazyScroll.equals("OFF")) {
                        if (StockAgListView.getTag().equals("FOOTER_REMOVE")) {
                            StockAgListView.addFooterView(footer);
                            StockAgListView.setTag("FOOTER_ADDED");

                        }
                        footer.setVisibility(View.VISIBLE);
                        lazyScroll = "ON";
                        Log.i(TAG, "calling Scroll api: " + FirstVisibleItem + " " + VisibleItemCount + " " + TotalItemCount);
                        requestStockAgeingApi(selectedString);
                        //Reusable_Functions.sDialog(context, "Loading data...");
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                    this.FirstVisibleItem = firstVisibleItem;
                    this.VisibleItemCount = visibleItemCount;
                    this.TotalItemCount = totalItemCount;

                }
            });
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
            Reusable_Functions.hDialog();
            StockAgListView.removeFooterView(footer);
            StockAgListView.setTag("FOOTER_REMOVE");
        }
    }


    private void initalise() {
        stock_txtStoreCode = (TextView) findViewById(R.id.txtStoreCode);
        stock_txtStoreName = (TextView) findViewById(R.id.txtStoreName);
        stock_BtnBack = (RelativeLayout) findViewById(R.id.stockAgeing_imageBtnBack);
        stock_BtnFilter = (RelativeLayout) findViewById(R.id.stockAgeing_imgfilter);
        stock_quickFilter = (RelativeLayout) findViewById(R.id.sa_quickFilter);
        quickFilterPopup = (RelativeLayout) findViewById(R.id.quickFilterPopup);
        quickFilterPopup.setVisibility(View.GONE);
        quickFilter_baseLayout = (RelativeLayout) findViewById(R.id.quickFilter_baseLayout);
        qfDoneLayout = (RelativeLayout) findViewById(R.id.qfDoneLayout);
        quickFilter_BorderLayout = (RelativeLayout) findViewById(R.id.quickFilter_BorderLayout);
        StockAgListView = (ListView) findViewById(R.id.stockListView);

        stock_segmented = (SegmentedGroup) findViewById(R.id.stock_segmented);
        stock_core = (RadioButton) findViewById(R.id.stock_core);
        stock_fashion = (RadioButton) findViewById(R.id.stock_fashion);
       // stock_fashion.toggle();

        Toggle_stock_fav = (ToggleButton) findViewById(R.id.toggle_top_fav);
        checkCurrent = (RadioButton) findViewById(R.id.checkCurrent);
        checkPrevious = (RadioButton) findViewById(R.id.checkPrevious);
        checkOld = (RadioButton) findViewById(R.id.checkOld);
        checkUpcoming = (RadioButton) findViewById(R.id.checkUpcoming);
        checkAgeing1 = (CheckBox) findViewById(R.id.checkAgeing1);
        checkAgeing2 = (CheckBox) findViewById(R.id.checkAgeing2);
        checkAgeing3 = (CheckBox) findViewById(R.id.checkAgeing3);
        checkCurrent.setOnClickListener(this);
        checkPrevious.setOnClickListener(this);
        checkOld.setOnClickListener(this);
        checkUpcoming.setOnClickListener(this);
        checkAgeing3.setOnClickListener(this);
        checkAgeing2.setOnClickListener(this);
        checkAgeing1.setOnClickListener(this);


        qfDoneLayout.setOnClickListener(this);
        stock_segmented.setOnCheckedChangeListener(StockAgeingActivity.this);
        stock_BtnBack.setOnClickListener(StockAgeingActivity.this);
        stock_BtnFilter.setOnClickListener(this);
        stock_quickFilter.setOnClickListener(this);
        quickFilter_baseLayout.setOnClickListener(this);
        quickFilter_BorderLayout.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.stockAgeing_imageBtnBack:
            /*    Intent intent = new Intent(StockAgeingActivity.this, DashBoardActivity.class);
                intent.putExtra("BACKTO","inventory");
                startActivity(intent);*/
                onBackPressed();
                break;
            case R.id.toggle_stock_fav:
                if (Toggle_stock_fav.isChecked()) {
                    Toggle_stock_fav.setChecked(true);
                } else {
                    Toggle_stock_fav.setChecked(false);
                }

                break;
            case R.id.stockAgeing_imgfilter:
                Intent intent1 = new Intent(StockAgeingActivity.this, SalesFilterActivity.class);
                intent1.putExtra("checkfrom", "stockAgeing");
                startActivity(intent1);
                //  finish();
                break;
            case R.id.sa_quickFilter:
                quickFilterPopup.setVisibility(View.VISIBLE);
                break;
            case R.id.quickFilter_baseLayout:
                baseclick();
                quickFilterPopup.setVisibility(View.GONE);
                break;
            case R.id.qfDoneLayout:

                if (Reusable_Functions.chkStatus(context)) {


                    if (checkCurrent.isChecked()) {
                        checkSeasonGpVal = "Current";
                        quickFilterPopup.setVisibility(View.GONE);
                        popupCurrent();
                        break;


                    } else if (checkPrevious.isChecked()) {
                        checkSeasonGpVal = "Previous";
                        quickFilterPopup.setVisibility(View.GONE);
                        popupPrevious();
                        break;


                    } else if (checkOld.isChecked()) {
                        checkSeasonGpVal = "Old";
                        quickFilterPopup.setVisibility(View.GONE);
                        popupOld();
                        break;


                    } else if (checkUpcoming.isChecked()) {
                        checkSeasonGpVal = "Upcoming";
                        quickFilterPopup.setVisibility(View.GONE);
                        popupUpcoming();
                        break;

                    } else {
                        Log.e("Uncheck1", "----" + checkSeasonGpVal);
                        //Toast.makeText(this, "Uncheck", Toast.LENGTH_SHORT).show();

                    }
                    if (checkAgeing1.isChecked()) {
                        //popupUpcoming();
                        checkAgeingVal = "CheckAgeing1";
                        quickFilterPopup.setVisibility(View.GONE);

                    } else if (checkAgeing2.isChecked()) {
                        //popupUpcoming();
                        checkAgeingVal = "CheckAgeing2";
                        quickFilterPopup.setVisibility(View.GONE);
                    } else if (checkAgeing3.isChecked()) {
                        //popupUpcoming();
                        checkAgeingVal = "CheckAgeing3";
                        quickFilterPopup.setVisibility(View.GONE);
                    } else {
                        Log.e("Uncheck2", "----" + checkAgeingVal);

                        //Toast.makeText(this, "Uncheck", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();

                }

                break;
            case R.id.checkCurrent:
                checkCurrent.setChecked(true);
                checkPrevious.setChecked(false);
                checkOld.setChecked(false);
                checkUpcoming.setChecked(false);

                break;
            case R.id.checkPrevious:

                checkPrevious.setChecked(true);
                checkCurrent.setChecked(false);
                checkOld.setChecked(false);
                checkUpcoming.setChecked(false);

                break;
            case R.id.checkOld:

                checkOld.setChecked(true);
                checkCurrent.setChecked(false);
                checkPrevious.setChecked(false);
                checkUpcoming.setChecked(false);

                break;
            case R.id.checkUpcoming:

                checkUpcoming.setChecked(true);
                checkCurrent.setChecked(false);
                checkOld.setChecked(false);
                checkPrevious.setChecked(false);

                break;
            case R.id.checkAgeing1:

                checkAgeing1.setChecked(true);
                checkAgeing2.setChecked(false);
                checkAgeing3.setChecked(false);
                break;
            case R.id.checkAgeing2:

                checkAgeing2.setChecked(true);
                checkAgeing1.setChecked(false);
                checkAgeing3.setChecked(false);
                break;
            case R.id.checkAgeing3:

                checkAgeing3.setChecked(true);
                checkAgeing1.setChecked(false);
                checkAgeing2.setChecked(false);
                break;
        }

    }

    private void popupCurrent() {
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            limit = 10;
            offsetvalue = 0;
            top = 10;
            seasongroup = "Current";
            StockAgeingList.clear();
            requestStockAgeingApi(selectedString);
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    private void popupPrevious() {
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            limit = 10;
            offsetvalue = 0;
            top = 10;
            seasongroup = "Previous";
            StockAgeingList.clear();
            requestStockAgeingApi(selectedString);
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    private void popupOld() {
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            limit = 10;
            offsetvalue = 0;
            top = 10;
            seasongroup = "Old";
            StockAgeingList.clear();
            requestStockAgeingApi(selectedString);
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    private void popupUpcoming() {
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            limit = 10;
            offsetvalue = 0;
            top = 10;
            seasongroup = "Upcoming";
            StockAgeingList.clear();
            requestStockAgeingApi(selectedString);
        } else {
            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if(toggleClick==false) {

            switch (checkedId) {
                case R.id.stock_core:
                    if (stock_core.isChecked()) {
                        //Reusable_Functions.hDialog();
                        limit = 10;
                        offsetvalue = 0;
                        top = 10;
                        corefashion = "Core";
                        lazyScroll = "OFF";
                        if (Reusable_Functions.chkStatus(context)) {
                            StockAgeingList.clear();
                            Reusable_Functions.sDialog(context, "Loading data...");
                            StockAgListView.setVisibility(View.GONE);
                            coreSelection = true;
                            requestStockAgeingApi(selectedString);
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            StockAgListView.setVisibility(View.GONE);

                        }
                    }
                    break;
                case R.id.stock_fashion:
                    if (stock_fashion.isChecked()) {
                        //    Reusable_Functions.hDialog();
                        limit = 10;
                        offsetvalue = 0;
                        top = 10;
                        corefashion = "Fashion";
                        lazyScroll = "OFF";
                        if (Reusable_Functions.chkStatus(context)) {
                            StockAgeingList.clear();
                            StockAgListView.setVisibility(View.GONE);
                            Reusable_Functions.sDialog(context, "Loading data...");
                            coreSelection = false;
                            requestStockAgeingApi(selectedString);
                        } else {
                            Toast.makeText(context, "Check your network connectivity", Toast.LENGTH_SHORT).show();
                            StockAgListView.setVisibility(View.GONE);

                        }
                    }
                    break;

            }
        }else
        {
            toggleClick=false;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       /* Intent intent = new Intent(StockAgeingActivity.this, DashBoardActivity.class);
        intent.putExtra("BACKTO","inventory");
        startActivity(intent);*/
        //corefashion  checkSeasonGpVal seasongroup
        corefashion=null;
        checkSeasonGpVal=null;
        seasongroup=null;

        corefashion="Fashion";
        seasongroup="Current";
        finish();
    }

}
