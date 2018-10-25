package com.piyush.pictprint;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.single.BasePermissionListener;
import com.piyush.pictprint.Utils.Utils;
import com.piyush.pictprint.adapter.PagerAdapter;
import com.piyush.pictprint.adapter.QueueAdapter;
import com.piyush.pictprint.model.Document;
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
    int primaryColor;
    AlertDialog.Builder builder;
    AlertDialog proceedDialog;
    AlertDialog exitDialog;

    DialogInterface.OnShowListener plistener = new DialogInterface.OnShowListener() {
        @Override
        public void onShow(DialogInterface dialog) {
            proceedDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(primaryColor);
            proceedDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(primaryColor);        }
    };

    DialogInterface.OnShowListener elistener = new DialogInterface.OnShowListener() {
        @Override
        public void onShow(DialogInterface dialog) {
            exitDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(primaryColor);
            exitDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(primaryColor);        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog);

         proceedDialog = builder.setTitle("Proceed?")
                .setMessage("You haven't added the document.")
                .setPositiveButton("PROCEED ANYWAYS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(new Intent(MainActivity.this, PaymentActivity.class),350);

                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                }).create();
      proceedDialog.setOnShowListener(plistener);


         exitDialog = builder.setTitle("Are you sure?")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                }).create();
        exitDialog.setOnShowListener(elistener);





        primaryColor = getResources().getColor(R.color.colorPrimary);
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new BasePermissionListener())
                .check();

        lightFlags = slidingUpPanelLayout.getSystemUiVisibility();
        darkFlags = lightFlags|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        initialColor = getResources().getColor(R.color.colorPrimary);
        final ArgbEvaluator evaluator = new ArgbEvaluator();
        final List<Fragment> fragments = new ArrayList<>();
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
                Singleton.getInstance().setDocuments(documents);
                if(((MainFragment) fragments.get(0)).isDocumentPresent())
                {


                           // .
                    // setIcon(android.R.drawable.ic_dialog_alert)
                            proceedDialog.show();

                }
                else
                    startActivityForResult(new Intent(MainActivity.this, PaymentActivity.class),350);

                // ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,header,"paymentTransition");
            }
        });
        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                header.setAlpha(1-slideOffset);
                getWindow().setStatusBarColor(((Integer) evaluator.evaluate(slideOffset,
                        initialColor,
                        android.graphics.Color.WHITE )));
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.log_out: {
                logOut();
                Intent i = new Intent(this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
                break;
            }
        }
        return false;
    }

    private void logOut() {
        SharedPreferences.Editor editor =PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.remove("Token");
        editor.remove("Username");
        editor.remove("LoggedIn");
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==350 && resultCode==RESULT_OK)
        {
            documents = (Singleton.getInstance()).getDocuments();
            if(documents.isEmpty())
                slidingUpPanelLayout.setPanelHeight(0);
            price=total_docs=total_pages=0;
            for(int i=0;i<documents.size();i++) {
                price += documents.get(i).getPrice();
                total_pages+=documents.get(i).getpages();
            }
            total_docs = documents.size();
            setTotalDocs(total_docs,total_pages);
            setPrice(price);
            queueAdapter.addDocuments(documents);

        }
    }

    @Override
    public void onDocumentAdded(Document document) {
        slidingUpPanelLayout.setPanelHeight(Math.round(Utils.convertDpToPixel(56,this)));
        documents.add(document);

        queueAdapter.setDoucments(documents);

        header.setVisibility(View.VISIBLE);
        price+=document.getPrice();
        total_docs++;
        setPrice(price);
        total_pages+=document.getpages();
        setTotalDocs(total_docs,total_pages);

    }


    @Override
    public void onBackPressed() {



            //    .setIcon(android.R.drawable.ic_dialog_alert)
                exitDialog.show();
    }

    private void setPrice(int price)
    {
        totalPrice.setText(String.format("₹%s", String.valueOf(price)));

    }

    public void setTotalDocs(int total_docs,int total_pages)
    {
        if(total_docs==1) {
            totalJobs.setText("1 Document to print");
            totalPages.setText("1 page");

        }
        else
        {totalJobs.setText(String.format("%s Documents to print", String.valueOf(total_docs)));
            totalPages.setText(String.format("%s pages", String.valueOf(total_pages)));
        }
    }
}
