package apsupportapp.aperotechnologies.com.designapp.KeyProductPlan;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import apsupportapp.aperotechnologies.com.designapp.ConstsCore;
import apsupportapp.aperotechnologies.com.designapp.HorlyAnalysis.KeyProductActivity;
import apsupportapp.aperotechnologies.com.designapp.HorlyAnalysis.Option_Fragment;
import apsupportapp.aperotechnologies.com.designapp.MySingleton;
import apsupportapp.aperotechnologies.com.designapp.OnRowPressListener;
import apsupportapp.aperotechnologies.com.designapp.ProductNameBean;
import apsupportapp.aperotechnologies.com.designapp.R;
import apsupportapp.aperotechnologies.com.designapp.Reusable_Functions;
import apsupportapp.aperotechnologies.com.designapp.StyleDetailsBean;
import apsupportapp.aperotechnologies.com.designapp.SwitchingTabActivity;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by pamrutkar on 27/02/17.
 */
public class Plan_Option_Fragment extends Fragment {

    public static TableLayout tableAPlanOpt_Frag;
    public static TableLayout tableBPlanOpt_Frag;
    public static TableLayout tableCPlanOpt_Frag;
    public static TableLayout tableDPlanOpt_Frag;
    SegmentedGroup segmentedGroupOption;
    RadioButton opt_btnWTD,opt_btnLW;
    public static ViewGroup optionview;
    HorizontalScrollView horizontalScrollViewB;
    HorizontalScrollView horizontalScrollViewD;
    ArrayList<KeyPlanProductBean> productNameBeanArrayList;
    KeyPlanProductBean keyPlanProductBean;
    Gson gson;
    String option_seg_clk = "WTD";
    int planlevel = 2;
    String userId, bearertoken,achColor;
    ScrollView scrollViewC;
    ScrollView scrollViewD;
    RequestQueue queue;
    OnRowPressListener1 rowPressListener;
    Context context;
    RelativeLayout option_relativeLayout;
    private boolean opttoggleClick=false;
    public static RelativeLayout optrel;
    Button txtOptionGreen, txtOptionRed,txtOptionAmber;
    // set the header titles
    String headers[] = {
            "       Option        ",
            " PvA\n\t\tSales%  ",
            " PvA\n\t\tStk%   ",
            " Plan\n\tSales   ",
            " Sales\n\t\t(U)   ",
            " Plan\n\tStk   ",
            "  Stk\n\t\t(U)   "

    };

    int headerCellsWidth[] = new int[headers.length];

    TextView txtStoreCode, txtStoreDesc;
    MySingleton m_config;
    String prod_Name,option;

    int offsetvalue = 0, limit = 100;
    int count = 0;
    SharedPreferences sharedPreferences;
    TextView txtOptionName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_config = MySingleton.getInstance(getActivity());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        userId = sharedPreferences.getString("userId", "");
        bearertoken = sharedPreferences.getString("bearerToken", "");

        Log.e("onCreate ", "Opt Fragment");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        Log.e("TAG", "onCreateView: OptionFragment>>>>>" );

        optionview = (ViewGroup) inflater.inflate(R.layout.plan_option_fragment, container, false);
        context = optionview.getContext();
        option_relativeLayout = (RelativeLayout) optionview.findViewById(R.id.planactual_optrelLayout);
        option_relativeLayout.setBackgroundColor(Color.parseColor("#f8f6f6"));
        txtOptionName = (TextView) optionview.findViewById(R.id.prod_name_val);

        txtStoreCode = (TextView) optionview.findViewById(R.id.txtStoreCode);
        txtStoreDesc = (TextView) optionview.findViewById(R.id.txtStoreName);
        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap
        BasicNetwork network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        optrel = (RelativeLayout) optionview.findViewById(R.id.planoptactual_rel);
        segmentedGroupOption = (SegmentedGroup) optionview.findViewById(R.id.segmentedGrpOption);
        opt_btnWTD = (RadioButton) optionview.findViewById(R.id.planactual_optbtnWTD);
       // opt_btnWTD.toggle();
        opt_btnLW = (RadioButton) optionview.findViewById(R.id.planactual_optbtnLW);
        txtOptionGreen = (Button)optionview.findViewById(R.id.txtOptionGreen);
        txtOptionRed = (Button)optionview.findViewById(R.id.txtOptionRed);
        txtOptionAmber = (Button)optionview.findViewById(R.id.txtOptionAmber);

        // option tab click
        if (KeyProductPlanActivity.plan_pager.getCurrentItem() == 1 && KeyProductPlanActivity.productName.equals("")) {
            View snackview=optionview.findViewById(android.R.id.content);
            Snackbar.make(snackview, "Please select product to view options", Snackbar.LENGTH_LONG).show();

        }
       segmentedGroupOption.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (opttoggleClick == false){
                    switch (checkedId) {
                        case R.id.planactual_optbtnWTD:
                            if (option_seg_clk.equals("WTD")) {
                                  break;
                            }
                                option_seg_clk = "WTD";
                                optrel.setVisibility(View.VISIBLE);
                                tableAPlanOpt_Frag.removeAllViews();
                                tableBPlanOpt_Frag.removeAllViews();
                                tableCPlanOpt_Frag.removeAllViews();
                                tableDPlanOpt_Frag.removeAllViews();
                                if (Reusable_Functions.chkStatus(context)) {
                                    Reusable_Functions.hDialog();
                                    Reusable_Functions.sDialog(context, "Loading data...");
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    productNameBeanArrayList = new ArrayList<KeyPlanProductBean>();
                                    planlevel = 2;
                                    requestPlanOptionAPI(offsetvalue, limit);
                                } else {
                                    Toast.makeText(getContext(), "Check your network connectivity", Toast.LENGTH_LONG).show();
                                }

                            break;
                        case R.id.planactual_optbtnLW:
                            if (option_seg_clk.equals("LW")) {
                                break;
                            }
                                option_seg_clk = "LW";
                                optrel.setVisibility(View.VISIBLE);
                                tableAPlanOpt_Frag.removeAllViews();
                                tableBPlanOpt_Frag.removeAllViews();
                                tableCPlanOpt_Frag.removeAllViews();
                                tableDPlanOpt_Frag.removeAllViews();
                                if (Reusable_Functions.chkStatus(context)) {

                                    Reusable_Functions.hDialog();
                                    Reusable_Functions.sDialog(context, "Loading data...");
                                    offsetvalue = 0;
                                    limit = 100;
                                    count = 0;
                                    productNameBeanArrayList = new ArrayList<KeyPlanProductBean>();
                                    planlevel = 2;
                                    requestPlanOptionAPI(offsetvalue, limit);
                                } else {
                                    Toast.makeText(getContext(), "Check your network connectivity", Toast.LENGTH_LONG).show();
                                }

                            break;
                    }
            }
           else
                {
                   opttoggleClick=false;
                }


           }
        });
        txtOptionGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                optrel.setVisibility(View.VISIBLE);
                tableAPlanOpt_Frag.removeAllViews();
                tableBPlanOpt_Frag.removeAllViews();
                tableCPlanOpt_Frag.removeAllViews();
                tableDPlanOpt_Frag.removeAllViews();
                if (Reusable_Functions.chkStatus(context)) {

                    Reusable_Functions.hDialog();
                    Reusable_Functions.sDialog(context, "Loading data...");
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    planlevel = 2;
                    achColor = "Green";
                    productNameBeanArrayList = new ArrayList<KeyPlanProductBean>();
                    retainSegmentVal();
                    requestPlanOptionAchColorAPI(offsetvalue, limit);

                }
            }

        });
        txtOptionRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                optrel.setVisibility(View.VISIBLE);
                tableAPlanOpt_Frag.removeAllViews();
                tableBPlanOpt_Frag.removeAllViews();
                tableCPlanOpt_Frag.removeAllViews();
                tableDPlanOpt_Frag.removeAllViews();
                if (Reusable_Functions.chkStatus(context)) {

                    Reusable_Functions.hDialog();
                    Reusable_Functions.sDialog(context, "Loading data...");
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    planlevel = 2;
                    achColor = "Red";
                    productNameBeanArrayList = new ArrayList<KeyPlanProductBean>();
                    retainSegmentVal();
                    requestPlanOptionAchColorAPI(offsetvalue, limit);

                }
            }

        });
        txtOptionAmber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                optrel.setVisibility(View.VISIBLE);
                tableAPlanOpt_Frag.removeAllViews();
                tableBPlanOpt_Frag.removeAllViews();
                tableCPlanOpt_Frag.removeAllViews();
                tableDPlanOpt_Frag.removeAllViews();
                if (Reusable_Functions.chkStatus(context)) {

                    Reusable_Functions.hDialog();
                    Reusable_Functions.sDialog(context, "Loading data...");
                    offsetvalue = 0;
                    limit = 100;
                    count = 0;
                    planlevel = 2;
                    achColor = "Amber";
                    productNameBeanArrayList = new ArrayList<KeyPlanProductBean>();
                    retainSegmentVal();
                    requestPlanOptionAchColorAPI(offsetvalue, limit);


                }
            }

        });

        initComponents();
        setComponentsId();
        setScrollViewAndHorizontalScrollViewTag();
        gson = new Gson();
        horizontalScrollViewB.addView(tableBPlanOpt_Frag);
        scrollViewC.addView(tableCPlanOpt_Frag);
        scrollViewD.addView(horizontalScrollViewD);
        horizontalScrollViewD.addView(tableDPlanOpt_Frag);
        // add the components to be part of the main layout
        addComponentToMainLayout();
        //int headerCellsWidth[] = new int[headers.length];


        return optionview;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            rowPressListener = (OnRowPressListener1) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement IFragmentToActivity");
        }
    }

    public void fragmentCommunication(String productName,String segmentClick) {

        Log.e("productName ====>> ", " " + productName);
        prod_Name = productName;
        option_seg_clk = segmentClick;
        if (Reusable_Functions.chkStatus(context)) {
            Reusable_Functions.hDialog();
            Reusable_Functions.sDialog(context, "Loading data...");

            offsetvalue = 0;
            limit = 100;
            count = 0;
            planlevel = 2;
            txtOptionName.setText(productName);
            productNameBeanArrayList = new ArrayList<>();

            if (!KeyProductPlanActivity.productName.equals("")) {
                retainSegmentVal();
                requestPlanOptionAPI(offsetvalue, limit);
            }
        } else {
            Toast.makeText(getContext(), "Check your network connectivity", Toast.LENGTH_LONG).show();
        }
    }

    private void retainSegmentVal() {

         if(option_seg_clk.equals("WTD"))
        {
           opt_btnWTD.toggle();
        }else
        {
           opt_btnLW.toggle();

        }
    }

    // initalized components
    private void initComponents() {

        tableAPlanOpt_Frag = new TableLayout(this.context);
        tableBPlanOpt_Frag = new TableLayout(this.context);
        tableCPlanOpt_Frag = new TableLayout(this.context);
        tableDPlanOpt_Frag = new TableLayout(this.context);

        horizontalScrollViewB = new MyHorizontalScrollView(this.context);
        horizontalScrollViewD = new MyHorizontalScrollView(this.context);

        scrollViewC = new MyScrollView(this.context);
        scrollViewD = new MyScrollView(this.context);

        tableAPlanOpt_Frag.setBackgroundColor(Color.GREEN);
        horizontalScrollViewB.setBackgroundColor(Color.LTGRAY);
    }

    // set essential component IDs
    @SuppressWarnings("ResourceType")
    private void setComponentsId() {
        tableAPlanOpt_Frag.setId(1);
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
        componentB_Params.addRule(RelativeLayout.RIGHT_OF, tableAPlanOpt_Frag.getId());

        RelativeLayout.LayoutParams componentC_Params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        componentC_Params.addRule(RelativeLayout.BELOW, tableAPlanOpt_Frag.getId());

        RelativeLayout.LayoutParams componentD_Params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        componentD_Params.addRule(RelativeLayout.RIGHT_OF, scrollViewC.getId());
        componentD_Params.addRule(RelativeLayout.BELOW, horizontalScrollViewB.getId());

        // 'this' is a relative layout,
        // we extend this table layout as relative layout as seen during the creation of this class
        option_relativeLayout.addView(tableAPlanOpt_Frag);
        option_relativeLayout.addView(horizontalScrollViewB, componentB_Params);
        option_relativeLayout.addView(scrollViewC, componentC_Params);
        option_relativeLayout.addView(scrollViewD, componentD_Params);

    }

    private void addTableRowToTableA() {
        tableAPlanOpt_Frag.addView(this.componentATableRow());
    }

    private void addTableRowToTableB() {
        tableBPlanOpt_Frag.addView(this.componentBTableRow());
    }

    // generate table row of table A
    TableRow componentATableRow() {

        TableRow componentATableRow = new TableRow(this.context);
        componentATableRow.setBackgroundColor(Color.parseColor("#2277b1"));
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
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
        componentBTableRow.setBackgroundColor(Color.parseColor("#2277b1"));
        int headerFieldCount = headers.length;

        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
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
          //  Log.v("Product Data", this.headerCellsWidth[x] + "");
        }

        for (int k = 0; k < productNameBeanArrayList.size(); k++) {

            final TableRow tableRowForTableC = this.tableRowForTableC(productNameBeanArrayList.get(k).getLevel());
            final TableRow taleRowForTableD = this.taleRowForTableD(productNameBeanArrayList.get(k));
            tableRowForTableC.setBackgroundColor(Color.parseColor("#dfdedf"));
            taleRowForTableD.setBackgroundColor(Color.parseColor("#dfdedf"));
            final int i = k;
            tableRowForTableC.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                 //   Log.e( "Option name: ",""+productNameBeanArrayList.get(i).getLevel() );
                   // Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                    tableAPlanOpt_Frag.removeAllViews();
                    tableBPlanOpt_Frag.removeAllViews();
                    tableRowForTableC.removeAllViews();
                    taleRowForTableD.removeAllViews();
                    tableCPlanOpt_Frag.removeAllViews();
                    tableDPlanOpt_Frag.removeAllViews();
                    optionview.removeView(optrel);

                    LinearLayout layout = (LinearLayout) KeyProductPlanActivity.plan_pager.getParent();
                    TabLayout tab = (TabLayout) layout.getChildAt(1);
                    tab.addTab(tab.newTab().setText("SKU"));
                    tab.getTabAt(2).select();
                    rowPressListener.communicateToFragment3(productNameBeanArrayList.get(i).getLevel(),option_seg_clk);


                }
            });
            tableCPlanOpt_Frag.addView(tableRowForTableC);
            tableDPlanOpt_Frag.addView(taleRowForTableD);
        }
        Reusable_Functions.hDialog();
    }


    TableRow tableRowForTableC(String productNameDetails) {

        TableRow.LayoutParams params = new TableRow.LayoutParams(this.headerCellsWidth[0], TableRow.LayoutParams.MATCH_PARENT);
        params.setMargins(0, 2, 0, 0);
        TableRow tableRowForTableC = new TableRow(this.context);
        TextView textView = this.bodyTextView(productNameDetails);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        //  textView.setBackgroundResource(R.drawable.bg_pressed_text_color);
        tableRowForTableC.addView(textView, params);
        return tableRowForTableC;

    }

    TableRow taleRowForTableD(KeyPlanProductBean productDetails) {
        TableRow taleRowForTableD = new TableRow(this.context);
        int loopCount = ((TableRow) tableBPlanOpt_Frag.getChildAt(0)).getChildCount();
        NumberFormat format = NumberFormat.getNumberInstance(new Locale("", "in"));
        String info[] = {

                String.valueOf(Math.round(productDetails.getPvaSales())).concat("%"),
                String.valueOf(Math.round(productDetails.getPvaStock())).concat("%"),
                String.valueOf(format.format(Math.round(productDetails.getPlanSaleTotQty()))),
                String.valueOf(format.format(Math.round(productDetails.getSaleTotQty()))),
                String.valueOf(format.format(Math.round(productDetails.getPlanTargetStockQty()))),
                String.valueOf(format.format(Math.round(productDetails.getInvClosingQty()))),
        };

        for (int x = 0; x < loopCount; x++) {
            TableRow.LayoutParams params = new TableRow.LayoutParams(headerCellsWidth[x + 1], TableRow.LayoutParams.MATCH_PARENT);
            params.setMargins(2, 2, 0, 0);

            TextView textViewB = this.bodyTextView(info[x]);
            textViewB.setTextColor(Color.WHITE);
            if(productDetails.getPvaSales() >= Double.parseDouble("100"))
            {
                textViewB.setBackgroundColor(Color.parseColor("#70e503"));
            }
            else if(productDetails.getPvaSales() > Double.parseDouble("80") && productDetails.getPvaSales() < Double.parseDouble("100"))
            {
                textViewB.setBackgroundColor(Color.parseColor("#ff7e00"));

            }else if(productDetails.getPvaSales() < Double.parseDouble("80"))
            {
                textViewB.setBackgroundColor(Color.parseColor("#fe0000"));
            }

            taleRowForTableD.addView(textViewB, params);
        }

        return taleRowForTableD;

    }

    // table cell standard TextView
    TextView bodyTextView(String label) {

        TextView bodyTextView = new TextView(this.context);
        bodyTextView.setBackgroundColor(Color.parseColor("#f8f6f6"));
        bodyTextView.setText(label);
        bodyTextView.setTextSize(12f);
        bodyTextView.setGravity(Gravity.CENTER);
        bodyTextView.setPadding(5, 5, 5, 5);

        return bodyTextView;
    }

    // header standard TextView
    TextView headerTextView(String label) {

        TextView headerTextView = new TextView(this.context);
        headerTextView.setBackgroundColor(Color.WHITE);
        headerTextView.setText(label);
        headerTextView.setGravity(Gravity.CENTER);
        headerTextView.setPadding(5, 5, 5, 5);

        return headerTextView;
    }

    // resizing TableRow height starts here
    void resizeHeaderHeight() {

        TableRow productNameHeaderTableRow = (TableRow) tableAPlanOpt_Frag.getChildAt(0);
        TableRow productInfoTableRow = (TableRow) tableBPlanOpt_Frag.getChildAt(0);

        int rowAHeight = this.viewHeight(productNameHeaderTableRow);
        int rowBHeight = this.viewHeight(productInfoTableRow);

        TableRow tableRow = rowAHeight < rowBHeight ? productNameHeaderTableRow : productInfoTableRow;
        int finalHeight = rowAHeight > rowBHeight ? rowAHeight : rowBHeight;

        this.matchLayoutHeight(tableRow, finalHeight);
    }

    void getTableRowHeaderCellWidth() {

        int tableAChildCount = ((TableRow) tableAPlanOpt_Frag.getChildAt(0)).getChildCount();
        int tableBChildCount = ((TableRow) tableBPlanOpt_Frag.getChildAt(0)).getChildCount();

        for (int x = 0; x < (tableAChildCount + tableBChildCount); x++) {

            if (x == 0) {
                this.headerCellsWidth[x] = this.viewWidth(((TableRow) tableAPlanOpt_Frag.getChildAt(0)).getChildAt(x));
            } else {
                this.headerCellsWidth[x] = this.viewWidth(((TableRow) tableBPlanOpt_Frag.getChildAt(0)).getChildAt(x - 1));
            }

        }
    }

    // resize body table row height
    void resizeBodyTableRowHeight() {

        int tableC_ChildCount = tableCPlanOpt_Frag.getChildCount();

        for (int x = 0; x < tableC_ChildCount; x++) {

            TableRow productNameHeaderTableRow = (TableRow) tableCPlanOpt_Frag.getChildAt(x);
            TableRow productInfoTableRow = (TableRow) tableDPlanOpt_Frag.getChildAt(x);

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

    private void requestPlanOptionAPI(final int offset, int limit1) {
        String url = ConstsCore.web_url + "/v1/display/keyproductsplan/" + userId + "?view=" + option_seg_clk + "&level=" + planlevel +"&productName=" + prod_Name.replaceAll(" ", "%20").replaceAll("&", "%26") +"&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("URL   ", url + " " + bearertoken);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("PlanProductName Response", " " + response.toString() + "\nlength: " + response.length());
                        try {
                            int i;
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(getActivity(), "no data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {
                                for (i = 0; i < response.length(); i++) {

                                    JSONObject productName1 = response.getJSONObject(i);
                                   option = productName1.getString("level");
                                    double planSaleNetVal = productName1.getDouble("planSaleNetVal");
                                    double planSaleTotQty = productName1.getDouble("planSaleTotQty");
                                    double planTargetStockQty = productName1.getDouble("planTargetStockQty");
                                    double saleNetVal = productName1.getDouble("saleNetVal");
                                    double saleTotQty = productName1.getDouble("saleTotQty");
                                    double invClosingQty = productName1.getDouble("invClosingQty");
                                    double pvaSales = productName1.getDouble("pvaSales");
                                    double pvaStock = productName1.getDouble("pvaStock");
                                    String storeCode = productName1.getString("storeCode");
                                    String storeDesc = productName1.getString("storeDesc");
                                    String achColor = productName1.getString("achColor");
                                    keyPlanProductBean = new KeyPlanProductBean();
                                    keyPlanProductBean.setLevel(option);
                                    keyPlanProductBean.setPlanSaleNetVal(planSaleNetVal);
                                    keyPlanProductBean.setPlanSaleTotQty(planSaleTotQty);
                                    keyPlanProductBean.setInvClosingQty(invClosingQty);
                                    keyPlanProductBean.setPlanTargetStockQty(planTargetStockQty);
                                    keyPlanProductBean.setSaleNetVal(saleNetVal);
                                    keyPlanProductBean.setSaleTotQty(saleTotQty);
                                    keyPlanProductBean.setPvaSales(pvaSales);
                                    keyPlanProductBean.setPvaStock(pvaStock);
                                    keyPlanProductBean.setStoreCode(storeCode);
                                    keyPlanProductBean.setStoreDesc(storeDesc);
                                    productNameBeanArrayList.add(keyPlanProductBean);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestPlanOptionAPI(offsetvalue, limit);
                            } else if (response.length() < limit) {
                                for (i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                  option = productName1.getString("level");
                                    double planSaleNetVal = productName1.getDouble("planSaleNetVal");
                                    double planSaleTotQty = productName1.getDouble("planSaleTotQty");
                                    double planTargetStockQty = productName1.getDouble("planTargetStockQty");
                                    double saleNetVal = productName1.getDouble("saleNetVal");
                                    double saleTotQty = productName1.getDouble("saleTotQty");
                                    double invClosingQty = productName1.getDouble("invClosingQty");
                                    double pvaSales = productName1.getDouble("pvaSales");
                                    double pvaStock = productName1.getDouble("pvaStock");
                                    String storeCode = productName1.getString("storeCode");
                                    String storeDesc = productName1.getString("storeDesc");
                                    String achColor = productName1.getString("achColor");
                                    keyPlanProductBean = new KeyPlanProductBean();
                                    keyPlanProductBean.setLevel(option);
                                    keyPlanProductBean.setPlanSaleNetVal(planSaleNetVal);
                                    keyPlanProductBean.setPlanSaleTotQty(planSaleTotQty);
                                    keyPlanProductBean.setInvClosingQty(invClosingQty);
                                    keyPlanProductBean.setPlanTargetStockQty(planTargetStockQty);
                                    keyPlanProductBean.setSaleNetVal(saleNetVal);
                                    keyPlanProductBean.setSaleTotQty(saleTotQty);
                                    keyPlanProductBean.setPvaSales(pvaSales);
                                    keyPlanProductBean.setPvaStock(pvaStock);
                                    keyPlanProductBean.setStoreCode(storeCode);
                                    keyPlanProductBean.setStoreDesc(storeDesc);
                                    productNameBeanArrayList.add(keyPlanProductBean);
                                }

                                txtStoreCode.setText(productNameBeanArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(productNameBeanArrayList.get(0).getStoreDesc());
                                addTableRowToTableA();
                                addTableRowToTableB();
                                resizeHeaderHeight();
                                getTableRowHeaderCellWidth();
                                Log.e("view childcount", " " + optionview.getChildCount());
                                if (optionview.getChildCount() == 1) {
                                    scrollViewC.scrollTo(0, 0);
                                    scrollViewD.scrollTo(0, 0);
                                    optionview.addView(optrel);
                                }

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

    private void requestPlanOptionAchColorAPI(final int offset, int limit1) {
        String url = ConstsCore.web_url + "/v1/display/keyproductsplan/" + userId + "?view=" + option_seg_clk + "&level=" + planlevel +"&productName=" + prod_Name.replaceAll(" ", "%20").replaceAll("&", "%26")+"&achColor="+achColor+
                "&offset=" + offsetvalue + "&limit=" + limit;
        Log.e("URL   ", url + " " + bearertoken);

        final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("PlanProductName Response", " " + response.toString() + "\nlength: " + response.length());
                        try {
                            int i;
                            if (response.equals(null) || response == null || response.length() == 0 && count == 0) {
                                Reusable_Functions.hDialog();
                                Toast.makeText(getActivity(), "no data found", Toast.LENGTH_LONG).show();
                            } else if (response.length() == limit) {
                                for (i = 0; i < response.length(); i++) {

                                    JSONObject productName1 = response.getJSONObject(i);
                                    option = productName1.getString("level");
                                    double planSaleNetVal = productName1.getDouble("planSaleNetVal");
                                    double planSaleTotQty = productName1.getDouble("planSaleTotQty");
                                    double planTargetStockQty = productName1.getDouble("planTargetStockQty");
                                    double saleNetVal = productName1.getDouble("saleNetVal");
                                    double saleTotQty = productName1.getDouble("saleTotQty");
                                    double invClosingQty = productName1.getDouble("invClosingQty");
                                    double pvaSales = productName1.getDouble("pvaSales");
                                    double pvaStock = productName1.getDouble("pvaStock");
                                    String storeCode = productName1.getString("storeCode");
                                    String storeDesc = productName1.getString("storeDesc");
                                    String achColor = productName1.getString("achColor");

                                    keyPlanProductBean = new KeyPlanProductBean();
                                    keyPlanProductBean.setLevel(option);
                                    keyPlanProductBean.setPlanSaleNetVal(planSaleNetVal);
                                    keyPlanProductBean.setPlanSaleTotQty(planSaleTotQty);
                                    keyPlanProductBean.setInvClosingQty(invClosingQty);
                                    keyPlanProductBean.setPlanTargetStockQty(planTargetStockQty);
                                    keyPlanProductBean.setSaleNetVal(saleNetVal);
                                    keyPlanProductBean.setSaleTotQty(saleTotQty);
                                    keyPlanProductBean.setPvaSales(pvaSales);
                                    keyPlanProductBean.setPvaStock(pvaStock);
                                    keyPlanProductBean.setStoreCode(storeCode);
                                    keyPlanProductBean.setStoreDesc(storeDesc);
                                    productNameBeanArrayList.add(keyPlanProductBean);
                                }
                                offsetvalue = (limit * count) + limit;
                                count++;
                                requestPlanOptionAchColorAPI(offsetvalue, limit);
                            } else if (response.length() < limit) {
                                for (i = 0; i < response.length(); i++) {
                                    JSONObject productName1 = response.getJSONObject(i);
                                    option = productName1.getString("level");
                                    double planSaleNetVal = productName1.getDouble("planSaleNetVal");
                                    double planSaleTotQty = productName1.getDouble("planSaleTotQty");
                                    double planTargetStockQty = productName1.getDouble("planTargetStockQty");
                                    double saleNetVal = productName1.getDouble("saleNetVal");
                                    double saleTotQty = productName1.getDouble("saleTotQty");
                                    double invClosingQty = productName1.getDouble("invClosingQty");
                                    double pvaSales = productName1.getDouble("pvaSales");
                                    double pvaStock = productName1.getDouble("pvaStock");
                                    String storeCode = productName1.getString("storeCode");
                                    String storeDesc = productName1.getString("storeDesc");
                                    String achColor = productName1.getString("achColor");
                                    keyPlanProductBean = new KeyPlanProductBean();
                                    keyPlanProductBean.setLevel(option);
                                    keyPlanProductBean.setPlanSaleNetVal(planSaleNetVal);
                                    keyPlanProductBean.setPlanSaleTotQty(planSaleTotQty);
                                    keyPlanProductBean.setInvClosingQty(invClosingQty);
                                    keyPlanProductBean.setPlanTargetStockQty(planTargetStockQty);
                                    keyPlanProductBean.setSaleNetVal(saleNetVal);
                                    keyPlanProductBean.setSaleTotQty(saleTotQty);
                                    keyPlanProductBean.setPvaSales(pvaSales);
                                    keyPlanProductBean.setPvaStock(pvaStock);
                                    keyPlanProductBean.setStoreCode(storeCode);
                                    keyPlanProductBean.setStoreDesc(storeDesc);
                                    productNameBeanArrayList.add(keyPlanProductBean);
                                }

                                txtStoreCode.setText(productNameBeanArrayList.get(0).getStoreCode());
                                txtStoreDesc.setText(productNameBeanArrayList.get(0).getStoreDesc());
                                addTableRowToTableA();
                                addTableRowToTableB();
                                resizeHeaderHeight();
                                getTableRowHeaderCellWidth();
                                Log.e("view childcount", " " + optionview.getChildCount());
                                if (optionview.getChildCount() == 1) {
                                    scrollViewC.scrollTo(0, 0);
                                    scrollViewD.scrollTo(0, 0);
                                    optionview.addView(optrel);
                                }


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
