package apsupportapp.aperotechnologies.com.designapp.HorlyAnalysis;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.MySingleton;
import apsupportapp.aperotechnologies.com.designapp.OnRowPressListener;
import apsupportapp.aperotechnologies.com.designapp.Prod_FilterActivity;
import apsupportapp.aperotechnologies.com.designapp.ProductNameBean;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.SearchActivity1;


/**
 * Created by pamrutkar on 23/08/16.
 */
@SuppressWarnings("ALL")
public class ProductName_Fragment extends Fragment {
    TableLayout tableAProd_Frag;
    TableLayout tableBProd_Frag;
    TableLayout tableCProd_Frag;
    TableLayout tableDProd_Frag;
    Button btnProdFilter;
    ViewGroup view;
    HorizontalScrollView horizontalScrollViewB;
    HorizontalScrollView horizontalScrollViewD;
    ArrayList<ProductNameBean> productNameBeanArrayList;
    ScrollView scrollViewC;
    ScrollView scrollViewD;
    RequestQueue queue;
    Context context;
    RelativeLayout relativeLayout;
    public static RelativeLayout relProd_Frag;
    // set the header titles
    String headers[] = {
            "              Product Name            ",
            "    L2H Sls    ",
            "    Day Sls    ",
            "    WTD Sls    ",
            "    Day PvA %    ",
            "    WTD PvA %    ",
            "    SOH    ",
            "    GIT    ",

    };

    int headerCellsWidth[] = new int[headers.length];
    ProductNameBean productNameBean;
    TextView txtStoreCode, txtStoreDesc;
    String userId, bearertoken;
    MySingleton m_config;
    int offsetvalue = 0, limit = 100;
    int count = 0;
    int componentId1=1,componentId2=2,componentId3=3,componentId4=4;
    OnRowPressListener rowPressListener;
    SharedPreferences sharedPreferences;
    private String NetPercent;
    String f_productName;
    TextView txt_subdepName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");
        m_config = MySingleton.getInstance(getActivity());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = (ViewGroup) inflater.inflate(R.layout.productname_fragment, container, false);
        context = view.getContext();
        txtStoreCode = (TextView) view.findViewById(R.id.txtStoreCode);
        txtStoreDesc = (TextView) view.findViewById(R.id.txtStoreName);
        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        f_productName = getActivity().getIntent().getStringExtra("filterproductname");
        relProd_Frag = (RelativeLayout) view.findViewById(R.id.rel);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout);
        relativeLayout.setBackgroundColor(Color.parseColor("#f8f6f6"));  //dfdedf
        relProd_Frag.setVisibility(View.VISIBLE);
        btnProdFilter = (Button) view.findViewById(R.id.imageBtnFilter);
        txt_subdepName = (TextView) view.findViewById(R.id.txtSubDeptName);
        Log.e("parent", " " + KeyProductActivity.viewPager.getParent());
        LinearLayout layout = (LinearLayout) KeyProductActivity.viewPager.getParent();
        TabLayout tab = (TabLayout) layout.getChildAt(1);
        if (tab.getTabCount() == 3) {
            tab.removeTabAt(2);
        }
        if(tab.getTabCount() == 2)
        {
            tab.removeTabAt(1);
        }
        if (Reusable_Functions.chkStatus(context)) {

            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");
            offsetvalue = 0;
            limit = 100;
            count = 0;
            Log.e("SearchActivity1.searchProductName", " " + SearchActivity1.searchSubDept + " === " + SearchActivity1.searchProductName + " === " + SearchActivity1.searchArticleOption);
            Log.e("--- ", " " + (!SearchActivity1.searchSubDept.equals("")));
            Log.e("--- ", " " + (!SearchActivity1.searchProductName.equals("")));
            Log.e("--- ", " " + (!SearchActivity1.searchArticleOption.equals("")));

            // this condition is checked because when we click on option tab from SKU this gets called
            if (SearchActivity1.searchSubDept.equals("") && SearchActivity1.searchProductName.equals("") && f_productName == null)// && SearchActivity1.searchArticleOption.equals(""))
            {
                productNameBeanArrayList = new ArrayList<ProductNameBean>();
                requestProductAPI(offsetvalue, limit);
                txt_subdepName.setVisibility(View.GONE);
            }
            if (f_productName != null)
            {
                productNameBeanArrayList = new ArrayList<ProductNameBean>();
                requestFilterProductName(offsetvalue, limit);
                txt_subdepName.setText(f_productName.replaceAll("%20", " ").replaceAll("%26", "&"));
                txt_subdepName.setVisibility(View.VISIBLE);
            }

            if (!SearchActivity1.searchSubDept.equals(""))
            {
                productNameBeanArrayList = new ArrayList<ProductNameBean>();
                requestSearchSubDeptAPI(offsetvalue, limit);
                txt_subdepName.setText(SearchActivity1.searchSubDept.replaceAll("%20", " ").replaceAll("%26", "&"));
                txt_subdepName.setVisibility(View.VISIBLE);
            }

            if (!SearchActivity1.searchProductName.equals(""))
            {
                productNameBeanArrayList = new ArrayList<ProductNameBean>();
                requestSearchProductName(offsetvalue, limit);
                txt_subdepName.setVisibility(View.GONE);

            }
        } else {
            Toast.makeText(getContext(), "Check your network connectivity", Toast.LENGTH_LONG).show();
        }


        initComponents();
        setComponentsId();
        setScrollViewAndHorizontalScrollViewTag();
        // no need to assemble component A, since it is just a table
        horizontalScrollViewB.addView(tableBProd_Frag);
        scrollViewC.addView(tableCProd_Frag);
        scrollViewD.addView(horizontalScrollViewD);
        horizontalScrollViewD.addView(tableDProd_Frag);

        addComponentToMainLayout();
        btnProdFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Prod_FilterActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }


    private void requestFilterProductName(int offsetvalue1, int limit1) {

        String url = ConstsCore.web_url + "/v1/display/hourlytransproducts/" + userId + "?productName=" + f_productName.replaceAll(" ", "%20").replaceAll("&", "%26") + "&offset" + offsetvalue + "&limit" + limit;
        Log.i(" Filter Prod URL----------   ", url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Filter Product :", response.toString());
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(getActivity(), "no data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {

                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String ProductName = productName1.getString("productName");

                                    int L2Hrs_Net_Sales = productName1.getInt("last2HourSaleTotQty");
                                    int Day_Net_Sales = productName1.getInt("fordaySaleTotQty");
                                    int WTD_Net_Sales = productName1.getInt("wtdSaleTotQty");
                                    double Day_Net_Sales_Percent = productName1.getDouble("fordayPvaSalesUnitsPercent");
                                    double WTD_Net_Sales_Percent = productName1.getDouble("wtdPvaSalesUnitsPercent");
                                    int Forday_Plan_Sale_Tot_Qty = productName1.getInt("fordayPlanSaleTotQty");
                                    int Wtd_Plan_Sale_Tot_Qty = productName1.getInt("wtdPlanSaleTotQty");
                                    int SOH = productName1.getInt("stkOnhandQty");
                                    int GIT = productName1.getInt("stkGitQty");
                                    String Storecode = productName1.getString("storeCode");
                                    String storeDesc = productName1.getString("storeDesc");
                                    productNameBean = new ProductNameBean();
                                    productNameBean.setProductName(ProductName);
                                    Log.e("Product Name:", ProductName);
                                    productNameBean.setL2hrsNetSales(L2Hrs_Net_Sales);
                                    productNameBean.setDayNetSales(Day_Net_Sales);
                                    productNameBean.setWtdNetSales(WTD_Net_Sales);
                                    productNameBean.setDayNetSalesPercent(Day_Net_Sales_Percent);
                                    productNameBean.setWtdNetSalesPercent(WTD_Net_Sales_Percent);
                                    productNameBean.setSoh(SOH);
                                    productNameBean.setGit(GIT);
                                    productNameBean.setStoreCode(Storecode);

                                    productNameBean.setStoreDesc(storeDesc);

                                    Log.e("Response Lenght", "" + response.length());

                                    productNameBeanArrayList.add(productNameBean);

                                    txtStoreCode.setText(productNameBeanArrayList.get(i).getStoreCode());
                                    txtStoreDesc.setText(productNameBeanArrayList.get(i).getStoreDesc());
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestFilterProductName(offsetvalue, limit);
                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String ProductName = productName1.getString("productName");

                                    int L2Hrs_Net_Sales = productName1.getInt("last2HourSaleTotQty");
                                    int Day_Net_Sales = productName1.getInt("fordaySaleTotQty");
                                    int WTD_Net_Sales = productName1.getInt("wtdSaleTotQty");
                                    double Day_Net_Sales_Percent = productName1.getDouble("fordayPvaSalesUnitsPercent");
                                    double WTD_Net_Sales_Percent = productName1.getDouble("wtdPvaSalesUnitsPercent");
                                    int Forday_Plan_Sale_Tot_Qty = productName1.getInt("fordayPlanSaleTotQty");
                                    int Wtd_Plan_Sale_Tot_Qty = productName1.getInt("wtdPlanSaleTotQty");
                                    int SOH = productName1.getInt("stkOnhandQty");
                                    int GIT = productName1.getInt("stkGitQty");
                                    String Storecode = productName1.getString("storeCode");
                                    String storeDesc = productName1.getString("storeDesc");

                                    productNameBean = new ProductNameBean();
                                    productNameBean.setProductName(ProductName);

                                    productNameBean.setL2hrsNetSales(L2Hrs_Net_Sales);
                                    productNameBean.setDayNetSales(Day_Net_Sales);
                                    productNameBean.setWtdNetSales(WTD_Net_Sales);
                                    productNameBean.setDayNetSalesPercent(Day_Net_Sales_Percent);
                                    productNameBean.setWtdNetSalesPercent(WTD_Net_Sales_Percent);
                                    productNameBean.setSoh(SOH);
                                    productNameBean.setGit(GIT);
                                    productNameBean.setStoreCode(Storecode);

                                    Log.e("StoreCode", productNameBean.getStoreCode());
                                    productNameBean.setStoreDesc(storeDesc);

                                    Log.e("Response Lenght", "" + response.length());
                                    productNameBeanArrayList.add(productNameBean);
                                    Log.e("Array List After----", "" + productNameBeanArrayList.size());

                                    txtStoreCode.setText(productNameBeanArrayList.get(i).getStoreCode());
                                    txtStoreDesc.setText(productNameBeanArrayList.get(i).getStoreDesc());
                                }
                                Collections.sort(productNameBeanArrayList, new Comparator<ProductNameBean>() {
                                    public int compare(ProductNameBean one, ProductNameBean other) {
                                        return new Integer(one.getWtdNetSales()).compareTo(new Integer(other.getWtdNetSales()));
                                    }
                                });
                                Collections.reverse(productNameBeanArrayList);
                                addTableRowToTableA();
                                addTableRowToTableB();
                                resizeHeaderHeight();
                                getTableRowHeaderCellWidth();
                                generateTableC_AndTable_B();
                                resizeBodyTableRowHeight();

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
                params.put("Authorization", "Bearer " + bearertoken);
                return params;
            }
        };
        int socketTimeout = 60000;//5 seconds

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }

    private void requestSearchSubDeptAPI(int offsetvalue1, int limit1) {

        String url = ConstsCore.web_url + "/v1/display/hourlytransproducts/" + userId + "?view=productName&prodLevel3Desc=" + SearchActivity1.searchSubDept + "&offset" + offsetvalue + "&limit" + limit;
        Log.i("URL   ", url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Sub Dept Response", response.toString());
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(getActivity(), "no data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {

                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String ProductName = productName1.getString("productName");
                                    Log.e("Product Name:", ProductName);
                                    int L2Hrs_Net_Sales = productName1.getInt("last2HourSaleTotQty");
                                    int Day_Net_Sales = productName1.getInt("fordaySaleTotQty");
                                    int WTD_Net_Sales = productName1.getInt("wtdSaleTotQty");
                                    double Day_Net_Sales_Percent = productName1.getDouble("fordayPvaSalesUnitsPercent");
                                    double WTD_Net_Sales_Percent = productName1.getDouble("wtdPvaSalesUnitsPercent");
                                    int Forday_Plan_Sale_Tot_Qty = productName1.getInt("fordayPlanSaleTotQty");
                                    int Wtd_Plan_Sale_Tot_Qty = productName1.getInt("wtdPlanSaleTotQty");
                                    int SOH = productName1.getInt("stkOnhandQty");
                                    int GIT = productName1.getInt("stkGitQty");
                                    String Storecode = productName1.getString("storeCode");
                                    String storeDesc = productName1.getString("storeDesc");

                                    productNameBean = new ProductNameBean();
                                    productNameBean.setProductName(ProductName);
                                    Log.e("Product Name:", ProductName);
                                    productNameBean.setL2hrsNetSales(L2Hrs_Net_Sales);
                                    productNameBean.setDayNetSales(Day_Net_Sales);
                                    productNameBean.setWtdNetSales(WTD_Net_Sales);
                                    productNameBean.setDayNetSalesPercent(Day_Net_Sales_Percent);
                                    productNameBean.setWtdNetSalesPercent(WTD_Net_Sales_Percent);
                                    productNameBean.setSoh(SOH);
                                    productNameBean.setGit(GIT);
                                    productNameBean.setStoreCode(Storecode);

                                    Log.e("StoreCode", productNameBean.getStoreCode());
                                    productNameBean.setStoreDesc(storeDesc);

                                    Log.e("Response Lenght", "" + response.length());

                                    productNameBeanArrayList.add(productNameBean);

                                    txtStoreCode.setText(productNameBeanArrayList.get(i).getStoreCode());
                                    txtStoreDesc.setText(productNameBeanArrayList.get(i).getStoreDesc());
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSearchSubDeptAPI(offsetvalue, limit);
                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String ProductName = productName1.getString("productName");
                                    Log.e("Product Name:", ProductName);
                                    int L2Hrs_Net_Sales = productName1.getInt("last2HourSaleTotQty");
                                    int Day_Net_Sales = productName1.getInt("fordaySaleTotQty");
                                    int WTD_Net_Sales = productName1.getInt("wtdSaleTotQty");
                                    double Day_Net_Sales_Percent = productName1.getDouble("fordayPvaSalesUnitsPercent");
                                    double WTD_Net_Sales_Percent = productName1.getDouble("wtdPvaSalesUnitsPercent");
                                    int Forday_Plan_Sale_Tot_Qty = productName1.getInt("fordayPlanSaleTotQty");
                                    int Wtd_Plan_Sale_Tot_Qty = productName1.getInt("wtdPlanSaleTotQty");
                                    int SOH = productName1.getInt("stkOnhandQty");
                                    int GIT = productName1.getInt("stkGitQty");
                                    String Storecode = productName1.getString("storeCode");
                                    String storeDesc = productName1.getString("storeDesc");

                                    productNameBean = new ProductNameBean();
                                    productNameBean.setProductName(ProductName);
                                    Log.e("Product Name:", ProductName);
                                    productNameBean.setL2hrsNetSales(L2Hrs_Net_Sales);
                                    productNameBean.setDayNetSales(Day_Net_Sales);
                                    productNameBean.setWtdNetSales(WTD_Net_Sales);
                                    productNameBean.setDayNetSalesPercent(Day_Net_Sales_Percent);
                                    productNameBean.setWtdNetSalesPercent(WTD_Net_Sales_Percent);
                                    productNameBean.setSoh(SOH);
                                    productNameBean.setGit(GIT);
                                    productNameBean.setStoreCode(Storecode);

                                    Log.e("StoreCode", productNameBean.getStoreCode());
                                    productNameBean.setStoreDesc(storeDesc);

                                    Log.e("Response Lenght", "" + response.length());

                                    productNameBeanArrayList.add(productNameBean);
                                    Log.e("Array List After----", "" + productNameBeanArrayList.size());

                                    txtStoreCode.setText(productNameBeanArrayList.get(i).getStoreCode());
                                    txtStoreDesc.setText(productNameBeanArrayList.get(i).getStoreDesc());
                                }
                                Collections.sort(productNameBeanArrayList, new Comparator<ProductNameBean>() {
                                    public int compare(ProductNameBean one, ProductNameBean other) {
                                        return new Integer(one.getWtdNetSales()).compareTo(new Integer(other.getWtdNetSales()));
                                    }
                                });
                                Collections.reverse(productNameBeanArrayList);

                                addTableRowToTableA();
                                addTableRowToTableB();
                                resizeHeaderHeight();
                                getTableRowHeaderCellWidth();
                                generateTableC_AndTable_B();
                                resizeBodyTableRowHeight();
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
                params.put("Authorization", "Bearer " + bearertoken);
                return params;
            }
        };
        int socketTimeout = 60000;//5 seconds

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }


    private void requestSearchProductName(int offsetvalue1, int limit1) {

        String url = ConstsCore.web_url + "/v1/display/hourlytransproducts/" + userId + "?productName=" + SearchActivity1.searchProductName + "&offset" + offsetvalue + "&limit" + limit;
        Log.i("URL   ", url);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Sub Dept Response", response.toString());
                        try {
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(getActivity(), "no data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {

                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String ProductName = productName1.getString("productName");
                                    Log.e("Product Name:", ProductName);
                                    int L2Hrs_Net_Sales = productName1.getInt("last2HourSaleTotQty");
                                    int Day_Net_Sales = productName1.getInt("fordaySaleTotQty");
                                    int WTD_Net_Sales = productName1.getInt("wtdSaleTotQty");
                                    double Day_Net_Sales_Percent = productName1.getDouble("fordayPvaSalesUnitsPercent");
                                    double WTD_Net_Sales_Percent = productName1.getDouble("wtdPvaSalesUnitsPercent");
                                    int Forday_Plan_Sale_Tot_Qty = productName1.getInt("fordayPlanSaleTotQty");
                                    int Wtd_Plan_Sale_Tot_Qty = productName1.getInt("wtdPlanSaleTotQty");
                                    int SOH = productName1.getInt("stkOnhandQty");
                                    int GIT = productName1.getInt("stkGitQty");
                                    String Storecode = productName1.getString("storeCode");
                                    String storeDesc = productName1.getString("storeDesc");

                                    productNameBean = new ProductNameBean();
                                    productNameBean.setProductName(ProductName);
                                    Log.e("Product Name:", ProductName);
                                    productNameBean.setL2hrsNetSales(L2Hrs_Net_Sales);
                                    productNameBean.setDayNetSales(Day_Net_Sales);
                                    productNameBean.setWtdNetSales(WTD_Net_Sales);
                                    productNameBean.setDayNetSalesPercent(Day_Net_Sales_Percent);
                                    productNameBean.setWtdNetSalesPercent(WTD_Net_Sales_Percent);
                                    productNameBean.setSoh(SOH);
                                    productNameBean.setGit(GIT);
                                    productNameBean.setStoreCode(Storecode);

                                    Log.e("StoreCode", productNameBean.getStoreCode());
                                    productNameBean.setStoreDesc(storeDesc);
                                    Log.e("Response Lenght", "" + response.length());
                                    productNameBeanArrayList.add(productNameBean);
                                    txtStoreCode.setText(productNameBeanArrayList.get(i).getStoreCode());
                                    txtStoreDesc.setText(productNameBeanArrayList.get(i).getStoreDesc());
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestSearchProductName(offsetvalue, limit);
                            } else if (response.length() < limit) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String ProductName = productName1.getString("productName");
                                    Log.e("Product Name:", ProductName);
                                    int L2Hrs_Net_Sales = productName1.getInt("last2HourSaleTotQty");
                                    int Day_Net_Sales = productName1.getInt("fordaySaleTotQty");
                                    int WTD_Net_Sales = productName1.getInt("wtdSaleTotQty");
                                    double Day_Net_Sales_Percent = productName1.getDouble("fordayPvaSalesUnitsPercent");
                                    double WTD_Net_Sales_Percent = productName1.getDouble("wtdPvaSalesUnitsPercent");
                                    int Forday_Plan_Sale_Tot_Qty = productName1.getInt("fordayPlanSaleTotQty");
                                    int Wtd_Plan_Sale_Tot_Qty = productName1.getInt("wtdPlanSaleTotQty");
                                    int SOH = productName1.getInt("stkOnhandQty");
                                    int GIT = productName1.getInt("stkGitQty");
                                    String Storecode = productName1.getString("storeCode");
                                    String storeDesc = productName1.getString("storeDesc");

                                    productNameBean = new ProductNameBean();
                                    productNameBean.setProductName(ProductName);
                                    Log.e("Product Name:", ProductName);
                                    productNameBean.setL2hrsNetSales(L2Hrs_Net_Sales);
                                    productNameBean.setDayNetSales(Day_Net_Sales);
                                    productNameBean.setWtdNetSales(WTD_Net_Sales);
                                    productNameBean.setDayNetSalesPercent(Day_Net_Sales_Percent);
                                    productNameBean.setWtdNetSalesPercent(WTD_Net_Sales_Percent);
                                    productNameBean.setSoh(SOH);
                                    productNameBean.setGit(GIT);
                                    productNameBean.setStoreCode(Storecode);

                                    Log.e("StoreCode", productNameBean.getStoreCode());
                                    productNameBean.setStoreDesc(storeDesc);

                                    Log.e("Response Lenght", "" + response.length());
                                    productNameBeanArrayList.add(productNameBean);
                                    Log.e("Array List After----", "" + productNameBeanArrayList.size());
                                    txtStoreCode.setText(productNameBeanArrayList.get(i).getStoreCode());
                                    txtStoreDesc.setText(productNameBeanArrayList.get(i).getStoreDesc());
                                }
                                Collections.sort(productNameBeanArrayList, new Comparator<ProductNameBean>() {
                                    public int compare(ProductNameBean one, ProductNameBean other) {
                                        return new Integer(one.getWtdNetSales()).compareTo(new Integer(other.getWtdNetSales()));
                                    }
                                });
                                Collections.reverse(productNameBeanArrayList);

                                addTableRowToTableA();
                                addTableRowToTableB();
                                resizeHeaderHeight();
                                getTableRowHeaderCellWidth();
                                generateTableC_AndTable_B();
                                resizeBodyTableRowHeight();
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
                params.put("Authorization", "Bearer " + bearertoken);
                return params;
            }
        };
        int socketTimeout = 60000;//5 seconds

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            rowPressListener = (OnRowPressListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getContext().toString() + " must implement onButtonPressed");
        }
    }

    // initalized components
    private void initComponents() {
        tableAProd_Frag = new TableLayout(this.context);
        tableBProd_Frag = new TableLayout(this.context);
        tableCProd_Frag = new TableLayout(this.context);
        tableDProd_Frag = new TableLayout(this.context);
        horizontalScrollViewB = new MyHorizontalScrollView(this.context);
        horizontalScrollViewD = new MyHorizontalScrollView(this.context);
        scrollViewC = new MyScrollView(this.context);
        scrollViewD = new MyScrollView(this.context);
        tableAProd_Frag.setBackgroundColor(Color.parseColor("#000000"));
        horizontalScrollViewB.setBackgroundColor(Color.parseColor("#dfdedf"));
    }

    // set essential component IDs
    @SuppressWarnings("ResourceType")
    private void setComponentsId() {
        tableAProd_Frag.setId(1);
        horizontalScrollViewB.setId(2);
        scrollViewC.setId(3);
        scrollViewD.setId(4);
    }

    // set tags for some horizontal and vertical scroll view
    private void setScrollViewAndHorizontalScrollViewTag() {
        horizontalScrollViewB.setTag("horizontal scroll view b");
        horizontalScrollViewD.setTag("horizontal scroll view d");
        scrollViewC.setTag("scroll view c");
        scrollViewD.setTag("scroll view d");
    }

    // we add the components here in our TableMainLayout
    private void addComponentToMainLayout() {
        // RelativeLayout params were very useful here
        // the addRule method is the key to arrange the components properly
        RelativeLayout.LayoutParams componentB_Params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        componentB_Params.addRule(RelativeLayout.RIGHT_OF, this.tableAProd_Frag.getId());
        RelativeLayout.LayoutParams componentC_Params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        componentC_Params.addRule(RelativeLayout.BELOW, this.tableAProd_Frag.getId());
        RelativeLayout.LayoutParams componentD_Params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        componentD_Params.addRule(RelativeLayout.RIGHT_OF, scrollViewC.getId());
        componentD_Params.addRule(RelativeLayout.BELOW, horizontalScrollViewB.getId());

        // 'this' is a relative layout,
        // we extend this table layout as relative layout as seen during the creation of this class
        relativeLayout.addView(tableAProd_Frag);
        relativeLayout.addView(horizontalScrollViewB, componentB_Params);
        relativeLayout.addView(scrollViewC, componentC_Params);
        relativeLayout.addView(scrollViewD, componentD_Params);
    }


    private void addTableRowToTableA() {
        tableAProd_Frag.addView(this.componentATableRow());
    }

    private void addTableRowToTableB() {
        tableBProd_Frag.addView(this.componentBTableRow());
    }

    // generate table row of table A
    TableRow componentATableRow() {
        TableRow componentATableRow = new TableRow(this.context);
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, 80);
        params.setMargins(2, 0, 0, 0);
        TextView textView = this.headerTextView(headers[0]);
        textView.setBackgroundColor(Color.parseColor("#2277b1"));
        textView.setTextColor(Color.parseColor("#ffffff"));
        componentATableRow.addView(textView);
        return componentATableRow;
    }

    // generate table row of table B
    TableRow componentBTableRow() {

        TableRow componentBTableRow = new TableRow(this.context);

        int headerFieldCount = headers.length;

        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, 80);
        params.setMargins(2, 0, 0, 0);

        for (int x = 0; x < (headerFieldCount - 1); x++) {
            TextView textView = this.headerTextView(this.headers[x + 1]);
            textView.setBackgroundColor(Color.parseColor("#2277b1"));
            textView.setTextColor(Color.parseColor("#ffffff"));
            textView.setLayoutParams(params);
            componentBTableRow.addView(textView);
        }
        return componentBTableRow;
    }

    private void generateTableC_AndTable_B() {

        // just seeing some header cell width
        for (int x = 0; x < this.headerCellsWidth.length; x++) {
            Log.v("Product Data", this.headerCellsWidth[x] + "");
        }

        for (int k = 0; k < productNameBeanArrayList.size(); k++) {
            final TableRow tableRowForTableCProd_Frag;
            tableRowForTableCProd_Frag = this.tableRowForTableCProd_Frag(productNameBeanArrayList.get(k).getProductName());
            TableRow.LayoutParams params = new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT, 120);
            params.setMargins(2, 0, 0, 0);

            final TableRow tableRowForTableDProd_Frag = this.tableRowForTableDProd_Frag(productNameBeanArrayList.get(k));
            tableRowForTableCProd_Frag.setBackgroundColor(Color.parseColor("#dfdedf"));
            tableRowForTableDProd_Frag.setBackgroundColor(Color.parseColor("#dfdedf"));
            final int i = k;

            tableRowForTableCProd_Frag.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    KeyProductActivity.prodName = productNameBeanArrayList.get(i).getProductName();
                    relProd_Frag.setVisibility(View.GONE);
                    ViewPager viewPager = (ViewPager) view.getParent();
                    LinearLayout layout = (LinearLayout) viewPager.getParent();
                    TabLayout tab = (TabLayout) layout.getChildAt(1);
                    if (tab.getTabCount() == 3) {
                        tab.removeTabAt(2);
                    }
                    if(tab.getTabCount() == 2) {
                        tab.removeTabAt(1);
                    }
                    tab.addTab(tab.newTab().setText("OPTION"));
                    tab.getTabAt(1).select();
                    rowPressListener.communicateToFragment2(productNameBeanArrayList.get(i).getProductName());
                }
            });
            this.tableCProd_Frag.addView(tableRowForTableCProd_Frag);
            this.tableDProd_Frag.addView(tableRowForTableDProd_Frag);
        }
        Reusable_Functions.hDialog();
    }

    TableRow tableRowForTableCProd_Frag(String productNameDetails) {

        TableRow.LayoutParams params = new TableRow.LayoutParams(this.headerCellsWidth[0], TableRow.LayoutParams.MATCH_PARENT);
        params.setMargins(2, 2, 0, 0);
        TableRow tableRowForTableCProd_Frag = new TableRow(this.context);
        TextView textView = this.bodyTextView(productNameDetails);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        Log.e("Value", textView.getText().toString());
        tableRowForTableCProd_Frag.addView(textView, params);
        return tableRowForTableCProd_Frag;
    }

    TableRow tableRowForTableDProd_Frag(ProductNameBean productDetails) {


        TableRow tableRowForTableDProd_Frag = new TableRow(this.context);
        int loopCount = ((TableRow) this.tableBProd_Frag.getChildAt(0)).getChildCount();
        NetPercent = String.valueOf(productDetails.getDayNetSalesPercent()).concat("%");
        NumberFormat format = NumberFormat.getNumberInstance(new Locale("","in"));
        String info[] = {
                String.valueOf(productDetails.getL2hrsNetSales()),
                String.valueOf(productDetails.getDayNetSales()),
                String.valueOf(format.format(productDetails.getWtdNetSales())),
                String.valueOf(String.format("%.1f", productDetails.getDayNetSalesPercent())).concat("%"),
                String.valueOf(String.format("%.1f", productDetails.getWtdNetSalesPercent())).concat("%"),
                String.valueOf(format.format(productDetails.getSoh())),
                String.valueOf(format.format(productDetails.getGit()))
        };

        for (int x = 0; x < loopCount; x++) {
            TableRow.LayoutParams params = new TableRow.LayoutParams(headerCellsWidth[x + 1], TableRow.LayoutParams.MATCH_PARENT);
            params.setMargins(2, 2, 0, 0);
            TextView textViewB = this.bodyTextView(String.valueOf(info[x]));
            if (tableRowForTableDProd_Frag.getChildAt(3) != null) {
                TextView txtDayNetSalesPercent = (TextView) tableRowForTableDProd_Frag.getChildAt(3);

                if (productDetails.getDayNetSalesPercent() >= Double.parseDouble("70") && productDetails.getDayNetSalesPercent() <= Double.parseDouble("80")) {
                    txtDayNetSalesPercent.setTextColor(Color.parseColor("#FFBF00"));

                } else if (productDetails.getDayNetSalesPercent() > Double.parseDouble("80")) {
                    txtDayNetSalesPercent.setTextColor(Color.GREEN);

                } else if (productDetails.getDayNetSalesPercent() < Double.parseDouble("70")) {
                    txtDayNetSalesPercent.setTextColor(Color.RED);

                }
            }
            if (tableRowForTableDProd_Frag.getChildAt(4) != null) {
                TextView txtWtdNetSalesPercent = (TextView) tableRowForTableDProd_Frag.getChildAt(4);

                if (productDetails.getWtdNetSalesPercent() >= Double.parseDouble("70") && productDetails.getWtdNetSalesPercent() <= Double.parseDouble("80")) {
                    txtWtdNetSalesPercent.setTextColor(Color.parseColor("#FFBF00"));

                } else if (productDetails.getWtdNetSalesPercent() > Double.parseDouble("80")) {
                    txtWtdNetSalesPercent.setTextColor(Color.GREEN);

                } else if (productDetails.getWtdNetSalesPercent() < Double.parseDouble("70")) {
                    txtWtdNetSalesPercent.setTextColor(Color.RED);

                }
            }
            tableRowForTableDProd_Frag.addView(textViewB, params);
        }
        return tableRowForTableDProd_Frag;

    }

    // table cell standard TextView
    TextView bodyTextView(String label) {

        TextView bodyTextView = new TextView(this.context);
        bodyTextView.setBackgroundColor(Color.parseColor("#f8f6f6"));
        bodyTextView.setText(label);
        bodyTextView.setGravity(Gravity.CENTER);
        bodyTextView.setPadding(5, 5, 5, 5);
        return bodyTextView;
    }

    // header standard TextView
    TextView headerTextView(String label) {

        TextView headerTextView = new TextView(this.context);
        headerTextView.setBackgroundColor(Color.parseColor("#f8f6f6"));
        headerTextView.setText(label);
        headerTextView.setGravity(Gravity.CENTER);
        headerTextView.setPadding(5, 5, 5, 5);
        return headerTextView;
    }

    // resizing TableRow height starts here
    void resizeHeaderHeight() {
        TableRow productNameHeaderTableRow = (TableRow) this.tableAProd_Frag.getChildAt(0);
        TableRow productInfoTableRow = (TableRow) this.tableBProd_Frag.getChildAt(0);
        int rowAHeight = this.viewHeight(productNameHeaderTableRow);
        int rowBHeight = this.viewHeight(productInfoTableRow);
        TableRow tableRow = rowAHeight < rowBHeight ? productNameHeaderTableRow : productInfoTableRow;
        int finalHeight = rowAHeight > rowBHeight ? rowAHeight : rowBHeight;
        this.matchLayoutHeight(tableRow, finalHeight);
    }

    void getTableRowHeaderCellWidth() {
        int tableAChildCount = ((TableRow) this.tableAProd_Frag.getChildAt(0)).getChildCount();
        int tableBChildCount = ((TableRow) this.tableBProd_Frag.getChildAt(0)).getChildCount();
        for (int x = 0; x < (tableAChildCount + tableBChildCount); x++) {
            if (x == 0) {
                this.headerCellsWidth[x] = this.viewWidth(((TableRow) this.tableAProd_Frag.getChildAt(0)).getChildAt(x));
            } else {
                this.headerCellsWidth[x] = this.viewWidth(((TableRow) this.tableBProd_Frag.getChildAt(0)).getChildAt(x - 1));
            }

        }
    }

    // resize body table row height
    void resizeBodyTableRowHeight() {

        int tableC_ChildCount = this.tableCProd_Frag.getChildCount();

        for (int x = 0; x < tableC_ChildCount; x++) {

            TableRow productNameHeaderTableRow = (TableRow) this.tableCProd_Frag.getChildAt(x);
            TableRow productInfoTableRow = (TableRow) this.tableDProd_Frag.getChildAt(x);

            int rowAHeight = this.viewHeight(productNameHeaderTableRow);
            int rowBHeight = this.viewHeight(productInfoTableRow);

            TableRow tableRow = rowAHeight < rowBHeight ? productNameHeaderTableRow : productInfoTableRow;
            int finalHeight = rowAHeight > rowBHeight ? rowAHeight : rowBHeight;

            this.matchLayoutHeight(tableRow, finalHeight);
        }
    }

    // match all height in a table row
    // to make a standard TableRow height
    private void matchLayoutHeight(TableRow tableRow, int height) {

        int tableRowChildCount = tableRow.getChildCount();

        // if a TableRow has only 1 child
        if (tableRow.getChildCount() == 1) {
            View view = tableRow.getChildAt(0);
            TableRow.LayoutParams params = (TableRow.LayoutParams) view.getLayoutParams();
            params.height = height - (params.bottomMargin + params.topMargin);
            return;
        }

        // if a TableRow has more than 1 child
        for (int x = 0; x < tableRowChildCount; x++) {
            View view = tableRow.getChildAt(x);
            TableRow.LayoutParams params = (TableRow.LayoutParams) view.getLayoutParams();
            if (!isTheHeighestLayout(tableRow, x)) {
                params.height = height - (params.bottomMargin + params.topMargin);
                return;
            }
        }
    }

    // check if the view has the highest height in a TableRow
    private boolean isTheHeighestLayout(TableRow tableRow, int layoutPosition) {

        int tableRowChildCount = tableRow.getChildCount();
        int heighestViewPosition = -1;
        int viewHeight = 0;

        for (int x = 0; x < tableRowChildCount; x++) {
            View view = tableRow.getChildAt(x);
            int height = this.viewHeight(view);

            if (viewHeight < height) {
                heighestViewPosition = x;
                viewHeight = height;
            }
        }

        return heighestViewPosition == layoutPosition;
    }

    // read a view's height
    private int viewHeight(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return view.getMeasuredHeight();
    }

    // read a view's width
    private int viewWidth(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return view.getMeasuredWidth();
    }

    // horizontal scroll view custom class
    class MyHorizontalScrollView extends HorizontalScrollView {

        public MyHorizontalScrollView(Context context) {
            super(context);
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {
            String tag = (String) this.getTag();

            if (tag.equalsIgnoreCase("horizontal scroll view b")) {
                horizontalScrollViewD.scrollTo(l, 0);
            } else {
                horizontalScrollViewB.scrollTo(l, 0);
            }
        }
    }

    // scroll view custom class
    class MyScrollView extends ScrollView {

        public MyScrollView(Context context) {
            super(context);
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {

            String tag = (String) this.getTag();
            if (tag.equalsIgnoreCase("scroll view c")) {
                scrollViewD.scrollTo(0, t);
            } else {
                scrollViewC.scrollTo(0, t);
            }
        }
    }

    private void requestProductAPI(int offsetvalue1, int limit1) {
        String url = ConstsCore.web_url + "/v1/display/hourlytransproducts/" + userId + "?offset=" + offsetvalue + "&limit=" + limit;
        Log.i("URL   ", url + " " + bearertoken);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("ProductName Response", response.toString());
                        try {
                            int i;
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(getActivity(), "no product data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {
                                for ( i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String ProductName = productName1.getString("productName");

                                    int L2Hrs_Net_Sales = productName1.getInt("last2HourSaleTotQty");
                                    int Day_Net_Sales = productName1.getInt("fordaySaleTotQty");
                                    int WTD_Net_Sales = productName1.getInt("wtdSaleTotQty");
                                    double Day_Net_Sales_Percent = productName1.getDouble("fordayPvaSalesUnitsPercent");
                                    double WTD_Net_Sales_Percent = productName1.getDouble("wtdPvaSalesUnitsPercent");
                                    int Forday_Plan_Sale_Tot_Qty = productName1.getInt("fordayPlanSaleTotQty");
                                    int Wtd_Plan_Sale_Tot_Qty = productName1.getInt("wtdPlanSaleTotQty");
                                    int SOH = productName1.getInt("stkOnhandQty");
                                    int GIT = productName1.getInt("stkGitQty");
                                    String Storecode = productName1.getString("storeCode");
                                    String storeDesc = productName1.getString("storeDesc");

                                    productNameBean = new ProductNameBean();
                                    productNameBean.setProductName(ProductName);

                                    productNameBean.setL2hrsNetSales(L2Hrs_Net_Sales);
                                    productNameBean.setDayNetSales(Day_Net_Sales);
                                    productNameBean.setWtdNetSales(WTD_Net_Sales);
                                    productNameBean.setDayNetSalesPercent(Day_Net_Sales_Percent);
                                    productNameBean.setWtdNetSalesPercent(WTD_Net_Sales_Percent);
                                    productNameBean.setSoh(SOH);
                                    productNameBean.setGit(GIT);
                                    productNameBean.setStoreCode(Storecode);
                                    productNameBean.setStoreDesc(storeDesc);
                                    productNameBeanArrayList.add(productNameBean);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestProductAPI(offsetvalue, limit);
                            } else if (response.length() < limit) {
                                for ( i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    String ProductName = productName1.getString("productName");

                                    int L2Hrs_Net_Sales = productName1.getInt("last2HourSaleTotQty");
                                    int Day_Net_Sales = productName1.getInt("fordaySaleTotQty");
                                    int WTD_Net_Sales = productName1.getInt("wtdSaleTotQty");
                                    double Day_Net_Sales_Percent = productName1.getDouble("fordayPvaSalesUnitsPercent");
                                    double WTD_Net_Sales_Percent = productName1.getDouble("wtdPvaSalesUnitsPercent");
                                    int Forday_Plan_Sale_Tot_Qty = productName1.getInt("fordayPlanSaleTotQty");
                                    int Wtd_Plan_Sale_Tot_Qty = productName1.getInt("wtdPlanSaleTotQty");
                                    int SOH = productName1.getInt("stkOnhandQty");
                                    int GIT = productName1.getInt("stkGitQty");
                                    String Storecode = productName1.getString("storeCode");
                                    String storeDesc = productName1.getString("storeDesc");

                                    productNameBean = new ProductNameBean();
                                    productNameBean.setProductName(ProductName);

                                    productNameBean.setL2hrsNetSales(L2Hrs_Net_Sales);
                                    productNameBean.setDayNetSales(Day_Net_Sales);
                                    productNameBean.setWtdNetSales(WTD_Net_Sales);
                                    productNameBean.setDayNetSalesPercent(Day_Net_Sales_Percent);
                                    productNameBean.setWtdNetSalesPercent(WTD_Net_Sales_Percent);
                                    productNameBean.setSoh(SOH);
                                    productNameBean.setGit(GIT);
                                    productNameBean.setStoreCode(Storecode);
                                    productNameBean.setStoreDesc(storeDesc);
                                    productNameBeanArrayList.add(productNameBean);
                                }

                                Collections.sort(productNameBeanArrayList, new Comparator<ProductNameBean>() {
                                    public int compare(ProductNameBean one, ProductNameBean other) {
                                        return new Integer(one.getWtdNetSales()).compareTo(new Integer(other.getWtdNetSales()));
                                    }
                                });
                                Collections.reverse(productNameBeanArrayList);
                                txtStoreCode.setText(productNameBeanArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(productNameBeanArrayList.get(0).getStoreDesc());
                                addTableRowToTableA();
                                addTableRowToTableB();
                                resizeHeaderHeight();
                                getTableRowHeaderCellWidth();
                                generateTableC_AndTable_B();
                                resizeBodyTableRowHeight();
                            }
                        } catch (Exception e) {

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
                params.put("Authorization", "Bearer " + bearertoken);

                Log.e("params ", " " + params);
                return params;
            }
        };
        int socketTimeout = 60000;//5 seconds

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }
}
