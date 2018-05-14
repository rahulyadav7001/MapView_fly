package com.example.flytta.flyttatestapp.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flytta.flyttatestapp.R;
import com.example.flytta.flyttatestapp.adapter.CustomViewPagerAdapter;
import com.example.flytta.flyttatestapp.dataobject.DetailLocationDO;
import com.example.flytta.flyttatestapp.service.APIClient;
import com.example.flytta.flyttatestapp.service.APIInterface;
import com.example.flytta.flyttatestapp.utils.AppConstant;
import com.example.flytta.flyttatestapp.utils.NetworkUtility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationDetailActivity extends AppCompatActivity {
    private String service_ID = "";
    private APIInterface apiInterface = null;
    private ProgressDialog progressDoalog;
    private DetailLocationDO detailLocationDO = new DetailLocationDO();
    private TextView tv_Area, tv_City,tv_AreaZone,tv_Size,tv_facing,tv_Agreement,tv_configuration,tv_loca,tv_m_rent,tv_advance,tv_maintenance,tv_prop_name,tv_user,tv_phone;
    private CustomViewPagerAdapter mCustomPagerAdapter;
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_detail);
        initializeControl();
        bindControl();
        if (service_ID != null && !service_ID.equalsIgnoreCase("")) {
            getData(service_ID);
        } else {
            Toast.makeText(LocationDetailActivity.this, "Location not found", Toast.LENGTH_SHORT).show();

        }

    }

    private void getData(String service_ID) {
        if (NetworkUtility.isNetworkAvailable(LocationDetailActivity.this)) {
            progressDoalog.show();
            Call<DetailLocationDO> call = apiInterface.getLoactionDetail(service_ID);
            call.enqueue(new Callback<DetailLocationDO>() {
                @Override
                public void onResponse(Call<DetailLocationDO> call, Response<DetailLocationDO> response) {
                    progressDoalog.dismiss();
                    Log.d("Service call :", "Success");
                    Log.d("url ", "" + call.request().url().toString());
                    detailLocationDO = response.body();
                    refreshView(detailLocationDO);
                }
                @Override
                public void onFailure(Call<DetailLocationDO> call, Throwable t) {
                    Log.e("Service call :", "failed");
                    progressDoalog.dismiss();
                }
            });
        } else {
            Toast.makeText(LocationDetailActivity.this, getResources().getString(R.string.ConnectionIssue), Toast.LENGTH_SHORT).show();
        }

    }

    private void refreshView(DetailLocationDO detailLocationDO) {
        if (detailLocationDO != null && detailLocationDO.getItem() != null) {
            tv_Area.setText(detailLocationDO.getItem().getProject());
            tv_City.setText(detailLocationDO.getItem().getCity());

            tv_AreaZone.setText("- "+detailLocationDO.getItem().getTotalArea()+" "+ detailLocationDO.getItem().getSize1());
            tv_Size.setText("- "+detailLocationDO.getItem().getParking());
            tv_facing.setText("- "+detailLocationDO.getItem().getFacing());
            tv_Agreement.setText("- "+detailLocationDO.getItem().getAgreement());
            tv_configuration.setText("- "+detailLocationDO.getItem().getConfiguration());
            tv_loca.setText("- "+detailLocationDO.getItem().getLoca());
            tv_m_rent.setText("Rent: "+detailLocationDO.getItem().getMRent());
            tv_advance.setText("Advance: "+detailLocationDO.getItem().getAdvance());
            tv_maintenance.setText("Maintenance: "+detailLocationDO.getItem().getMaintenance());
            tv_prop_name.setText("- "+detailLocationDO.getItem().getPropName());
            tv_user.setText("- "+detailLocationDO.getItem().getUser());
            tv_phone.setText("- "+detailLocationDO.getItem().getPhone());

            mCustomPagerAdapter.refresh(detailLocationDO.getItem().getPicurl());
        }

    }

    private void bindControl() {
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            service_ID = null;
        } else {
            service_ID = extras.getString(AppConstant.Extra_Key_ServiceID);
        }
        mCustomPagerAdapter = new CustomViewPagerAdapter(this);
        mViewPager.setAdapter(mCustomPagerAdapter);

    }

    private void initializeControl() {

        tv_Area = (TextView) findViewById(R.id.tv_Area);
        tv_City = (TextView) findViewById(R.id.tv_City);
        tv_AreaZone = (TextView) findViewById(R.id.tv_AreaZone);
        tv_Size = (TextView) findViewById(R.id.tv_Size);
        tv_facing = (TextView) findViewById(R.id.tv_facing);
        tv_Agreement = (TextView) findViewById(R.id.tv_Agreement);
        tv_configuration = (TextView) findViewById(R.id.tv_configuration);
        tv_loca = (TextView) findViewById(R.id.tv_loca);
        tv_m_rent = (TextView) findViewById(R.id.tv_m_rent);
        tv_advance = (TextView) findViewById(R.id.tv_advance);
        tv_maintenance = (TextView) findViewById(R.id.tv_maintenance);
        tv_user = (TextView) findViewById(R.id.tv_user);
        tv_prop_name = (TextView) findViewById(R.id.tv_prop_name);
        tv_phone = (TextView) findViewById(R.id.tv_phone);

        mViewPager = (ViewPager) findViewById(R.id.vp_img);

        progressDoalog = new ProgressDialog(LocationDetailActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage(getString(R.string.PleaseWait));
        progressDoalog.setCancelable(false);
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        apiInterface = APIClient.getClient().create(APIInterface.class);
    }
}
