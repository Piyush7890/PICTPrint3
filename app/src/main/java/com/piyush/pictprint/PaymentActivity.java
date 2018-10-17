package com.piyush.pictprint;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.paytm.pgsdk.Log;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.piyush.pictprint.Utils.ColorUtil;
import com.piyush.pictprint.Utils.FileUtils;
import com.piyush.pictprint.Utils.FilterTouchHelperCallback;
import com.piyush.pictprint.adapter.QueueAdapter2;
import com.piyush.pictprint.api.ChecksumService;
import com.piyush.pictprint.api.SubmitJobService;
import com.piyush.pictprint.model.Checksum;
import com.piyush.pictprint.model.Document;
import com.piyush.pictprint.model.SubmitResponse;
import com.piyush.pictprint.views.TotalItemsLayout;

import org.parceler.Parcels;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.plaidapp.ui.recyclerview.FilterSwipeDismissListener;



public class PaymentActivity extends AppCompatActivity implements
        PaytmPaymentTransactionCallback,
        SubmitJobService.Listener, FilterSwipeDismissListener {

    private List<Document> doucments;

    @BindView(R.id.pdf_total_amount)
    TotalItemsLayout pdf_layout;

    @BindView(R.id.docs_total_amount)
    TotalItemsLayout docs_layout;

    @BindView(R.id.images_total_amount)
    TotalItemsLayout img_layout;

    @BindView(R.id.parent)
    ViewGroup parent;

    @BindView(R.id.proceed)
    Button proceed;

    @BindView(R.id.items)
    RecyclerView items;

    @BindView(R.id.total_amount)
    TextSwitcher textSwitcher;

    @BindView(R.id.total_amount_layout)
    ViewGroup total_amount_layout;


    int tempcount=0;
    ChecksumService service;
    SubmitJobService submitJobService;
    QueueAdapter2 adapter;
    int count=0;
    Random random = new Random();
    int randm;
    private boolean onDocumentRemoved = false;
    private boolean submitCompleteed=false, submitStarted=false;


    @Override
    public void onBackPressed() {
        if(onDocumentRemoved){
            setResult(RESULT_OK);
            finish();
            Singleton.getInstance().setDocuments(doucments);
        }
        super.onBackPressed();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
        textSwitcher.setFactory(mFactory);
        this.doucments = (Singleton.getInstance()).getDocuments();
        setTotalItems();
       updateCount();


        final int finalCount = count;
        ValueAnimator animator = ValueAnimator.ofInt(0,count);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                textSwitcher.setText(String.valueOf((int) animation.getAnimatedValue()));
            }
        });
        animator.setDuration(100*count);
        animator.setInterpolator(new FastOutSlowInInterpolator());
        animator.start();
        service = new ChecksumService(PreferenceManager.getDefaultSharedPreferences(this).getString("Token",""));
        String token = PreferenceManager.getDefaultSharedPreferences(PaymentActivity.this).getString("Token", null);
        submitJobService = new SubmitJobService(token);
        adapter = new QueueAdapter2();
        items.setAdapter(adapter);
        ItemTouchHelper.Callback  callback = new FilterTouchHelperCallback( this,this);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(items);
        adapter.addDocuments(doucments);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               String Username = PreferenceManager.getDefaultSharedPreferences(PaymentActivity.this).getString("Username", null);

                randm = random.nextInt(10000)+1;
                if(Username!=null)
                service.getChecksum(Username,String.valueOf(finalCount),String.valueOf(randm), new ChecksumService.onChecksumGenerated() {
                    @Override
                    public void onSuccess(Checksum checksum, String token) {
                        initializePaytmPayment(checksum.value, token);
                    }

                    @Override
                    public void onFailure(String t, String token) {
                        Toast.makeText(PaymentActivity.this, t, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    private void initializePaytmPayment(String checksunHash, String token)
    {



        PaytmPGService Service = PaytmPGService.getStagingService();
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("MID", "PICTPr16616768265254");
        paramMap.put("ORDER_ID", String.valueOf(randm));
        paramMap.put("CUST_ID", token);
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("TXN_AMOUNT", String.valueOf(count));
        paramMap.put("WEBSITE", "APPSTAGING");
        paramMap.put("CALLBACK_URL", "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=");
        paramMap.put("CHECKSUMHASH", checksunHash);
        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
        PaytmOrder order = new PaytmOrder(paramMap);
        Service.initialize(order, null);
        Service.startPaymentTransaction(this,
                true,
                true,
                this);

    }

    private void setTotalItems() {
        int pdf_count=0,img_count=0,doc_count=0,pdf_pages=0;
        for(int i=0;i<doucments.size();i++)
        {
            Document document = doucments.get(i);
            if(document.getContentType().contains("pdf"))
            {
                pdf_count++;
                pdf_pages+=document.getpages();
            }
            else if(document.getContentType().contains("image"))
                img_count++;
            else
                doc_count++;
        }
        pdf_layout.setAmount(pdf_pages);
        pdf_layout.setPages(pdf_pages);
        pdf_layout.setTotal_text(pdf_count==1?"1 PDF":pdf_count+" PDFs");

        img_layout.setAmount(2*img_count);
        img_layout.setPages(img_count);
        img_layout.setTotal_text(img_count==1?"1 Image":img_count+" Images");

        docs_layout.setAmount(2*doc_count);
        docs_layout.setPages(doc_count);
        docs_layout.setTotal_text(doc_count==1?"1 Image":doc_count+" Images");


        TransitionManager.beginDelayedTransition(parent);
        pdf_layout.setVisibility(pdf_count==0?View.GONE:View.VISIBLE);
        img_layout.setVisibility(img_count==0?View.GONE:View.VISIBLE);
        docs_layout.setVisibility(doc_count==0?View.GONE:View.VISIBLE);
    }

    @Override
    public void onTransactionResponse(Bundle inResponse) {
        Intent i = new Intent(PaymentActivity.this, JobSubmitService.class);
        i.putExtra("Documents",Parcels.wrap(doucments));
        startService(i);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void networkNotAvailable() {
        Toast.makeText(this, "Network not available", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void clientAuthenticationFailed(String inErrorMessage) {
        Toast.makeText(this, inErrorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void someUIErrorOccurred(String inErrorMessage) {
        Toast.makeText(this, inErrorMessage, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
        Toast.makeText(this, inErrorMessage, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressedCancelTransaction() {
        Toast.makeText(this, "Transaction Cancelled", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
        Toast.makeText(this, inErrorMessage, Toast.LENGTH_SHORT).show();

    }


    @Override
    protected void onDestroy() {
        submitJobService.cancel();
        super.onDestroy();
    }

    private ViewSwitcher.ViewFactory mFactory = new ViewSwitcher.ViewFactory() {

        @Override
        public View makeView() {
            TextView t = new TextView(PaymentActivity.this);
            t.setGravity(Gravity.CENTER);
            t.setTextAppearance(PaymentActivity.this, R.style.TextSwitcher);
            return t;
        }
    };

    @Override
    public synchronized void success(SubmitResponse response, Document document) {


        Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show();
    }

    private void updateMessage()
    {
    }

    private void updateUI()
    {
        submitCompleteed=true;
        Toast.makeText(PaymentActivity.this,"Jobs Submitted", Toast.LENGTH_SHORT).show();
    }
    @Override
    public synchronized void failure(Throwable t, Document document) {

    }

    @Override
    public void onItemDismiss(int position) {
        doucments.remove(position);
        setTotalItems();
        adapter.setDoucments(doucments);
        onDocumentRemoved=true;
        updateCount();
        textSwitcher.setCurrentText(String.valueOf(count));
        if(doucments.isEmpty())
            onBackPressed();

    }

    private void updateCount() {
        count=0;
        for(int i=0;i<doucments.size();i++)
            count+=doucments.get(i).getPrice();
    }
}
