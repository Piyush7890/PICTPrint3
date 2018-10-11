package com.piyush.pictprint;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.single.BasePermissionListener;
import com.piyush.pictprint.CJT.cdd.Color;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainFragment.DocumentAddedListener{


    @BindView(R.id.main_viewPager)
    ViewPager viewPager;
    @BindView(R.id.documents_queue)
    RecyclerView documentsQueue;
    QueueAdapter queueAdapter;
    @BindView(R.id.appBar)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.header_item)
    View header;
    @BindView(R.id.total_price)
    TextView totalPrice;
    @BindView(R.id.total_jobs)
    TextView totalJobs;
    @BindView(R.id.total_pages)
    TextView totalPages;
    @BindView(R.id.sliding_panel)
    SlidingUpPanelLayout slidingUpPanelLayout;
    @BindView(R.id.proceed_btn)
    Button proceed;
    List<Document> documents;
    private int price,total_pages,total_docs;
    int initialColor, lightFlags, darkFlags;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new BasePermissionListener())
                .check();
        lightFlags = slidingUpPanelLayout.getSystemUiVisibility();
        darkFlags = lightFlags|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        initialColor = getResources().getColor(R.color.colorPrimary);
        final ArgbEvaluator evaluator = new ArgbEvaluator();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new MainFragment());
        fragments.add(new ListFragment());
        List<String> titles = new ArrayList<>();
        titles.add("PRINT");
        titles.add("LOGS");
        viewPager.setAdapter(new PagerAdapter(
                getSupportFragmentManager(),
                fragments,
                titles
        ));
        tabLayout.setupWithViewPager(viewPager);
        setSupportActionBar(toolbar);
        documents = new ArrayList<>();
        queueAdapter = new QueueAdapter();
        documentsQueue.setAdapter(queueAdapter);
        documentsQueue.addItemDecoration(new DividerItemDecoration(this
                ,DividerItemDecoration.VERTICAL));
        price=total_docs=total_pages=0;
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                header.setAlpha(1-slideOffset);
                getWindow().setStatusBarColor(((Integer) evaluator.evaluate(slideOffset,initialColor, android.graphics.Color.WHITE )));
                if(slideOffset>0.75)
                    panel.setSystemUiVisibility(darkFlags);
                else
                    panel.setSystemUiVisibility(lightFlags);
            }

            @Override
            public void onPanelStateChanged(View panel,
                                            SlidingUpPanelLayout.PanelState previousState,
                                            SlidingUpPanelLayout.PanelState newState) {

            }
        });

    }

    @Override
    public void onDocumentAdded(Document document) {
        slidingUpPanelLayout.setPanelHeight(Math.round(Utils.convertDpToPixel(56,this)));
        documents.add(document);
        queueAdapter.addDocument(document);
        header.setVisibility(View.VISIBLE);
        price+=document.getPrice();
        total_docs++;
        total_pages+=document.getpages();
        totalPrice.setText(String.format("₹%s", String.valueOf(price)));
        if(total_docs==1) {
            totalJobs.setText("1 Document to print");
            totalPages.setText("1 page");

        }
        else
        {totalJobs.setText(String.format("%s Documents to print", String.valueOf(total_docs)));
        totalPages.setText(String.format("%s pages", String.valueOf(total_pages)));
    }}
}
