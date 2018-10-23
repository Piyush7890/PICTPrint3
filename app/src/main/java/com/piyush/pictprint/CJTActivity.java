package com.piyush.pictprint;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.piyush.pictprint.CJT.CloudJobTicket;
import com.piyush.pictprint.CJT.ColorTicketItem;
import com.piyush.pictprint.CJT.CopiesTicketItem;
import com.piyush.pictprint.CJT.MarginsTicketItem;
import com.piyush.pictprint.CJT.PageOrientationTicketItem;
import com.piyush.pictprint.CJT.PageRangeTicketItem;
import com.piyush.pictprint.CJT.PrintTicketSection;
import com.piyush.pictprint.CJT.cdd.Color;
import com.piyush.pictprint.CJT.cdd.PageOrientationType;
import com.piyush.pictprint.CJT.cdd.Type;
import com.piyush.pictprint.Utils.MorphTransform;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CJTActivity extends AppCompatActivity {

    @BindView(R.id.parent)
    ViewGroup parent;

    @BindView(R.id.container)
    View container;

    @BindView(R.id.et_copies)
    EditText copies;

    @BindView(R.id.et_range)
    EditText range;

    @BindView(R.id.color_spinner)
    Spinner color;

    @BindView(R.id.orientation_spinner)
    Spinner orientation;

    @BindView(R.id.et_left)
    EditText left;

    @BindView(R.id.et_top)
    EditText top;

    @BindView(R.id.et_right)
    EditText right;

    @BindView(R.id.et_bottom)
    EditText bottom;

    @BindView(R.id.cancel)
    Button cancel;

    @BindView(R.id.apply)
    Button apply;

    boolean isPDF;
    int count;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cjt);
        ButterKnife.bind(this);
        MorphTransform.setup(this, container,
                ContextCompat.getColor(this, R.color.background_light),
                getResources().getDimensionPixelSize(R.dimen.dialog_corners));
        isPDF = getIntent().getBooleanExtra("isPDF",false);
        count = getIntent().getIntExtra("pageCount",1);
        range.setText(String.format("1-%s", String.valueOf(count)));
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.Colors, R.layout.spinner_item);
        arrayAdapter.setDropDownViewResource( R.layout.simple_spinner_item);
        color.setAdapter( arrayAdapter);
        color.setSelection(1);

        arrayAdapter = ArrayAdapter.createFromResource(this, R.array.Orientation, R.layout.spinner_item);
        arrayAdapter.setDropDownViewResource( R.layout.simple_spinner_item);
        orientation.setAdapter(arrayAdapter);
        orientation.setSelection(0);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if(!isPDF) {
            range.setEnabled(false);
            range.setFocusable(false);
        }
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CloudJobTicket cloudJobTicket = new CloudJobTicket();
                cloudJobTicket.setVersion("1.0");
                PrintTicketSection printTicketSection = new PrintTicketSection();
                ColorTicketItem colorTicketItem = new ColorTicketItem();
                Type Color = null;
                switch (color.getSelectedItem().toString())
                {
                    case "Color":
                        Color=Type.STANDARD_COLOR;
                        break;
                    case "Black and White":
                        Color=Type.STANDARD_MONOCHROME;
                        break;
                }
                colorTicketItem.setType(Color);
                printTicketSection.setColor(colorTicketItem);
                CopiesTicketItem copiesTicketItem = new CopiesTicketItem();
                copiesTicketItem.setCopies(Integer.valueOf(copies.getText().toString()));
                printTicketSection.setCopies(copiesTicketItem);
                MarginsTicketItem marginsTicketItem = new MarginsTicketItem();
                marginsTicketItem.setLeftMicrons(Integer.valueOf(left.getText().toString()));
                marginsTicketItem.setTopMicrons(Integer.valueOf(top.getText().toString()));
                marginsTicketItem.setRightMicrons(Integer.valueOf(right.getText().toString()));
                marginsTicketItem.setBottomMicrons(Integer.valueOf(bottom.getText().toString()));
                printTicketSection.setMargins(marginsTicketItem);
                PageOrientationTicketItem pageOrientationTicketItem = new PageOrientationTicketItem();
                PageOrientationType pageOrientationType = null;
                switch (orientation.getSelectedItem().toString())
                {
                    case "Portrait":
                        pageOrientationType=PageOrientationType.PORTRAIT;
                        break;
                    case "Landscape":
                        pageOrientationType=PageOrientationType.LANDSCAPE;
                        break;
                }
                pageOrientationTicketItem.setType(pageOrientationType);
                printTicketSection.setPageOrientation(pageOrientationTicketItem);
                if(isPDF)
                {
                    PageRangeTicketItem rangeTicketItem = new PageRangeTicketItem();
                    String[] ranges = range.getText().toString().split(",");
                    List<PageRangeTicketItem.Interval> intervals = new ArrayList<>();
                    for (String range : ranges) {
                        String splits[] = range.split("-");
                        intervals.add(new PageRangeTicketItem.Interval(Integer.valueOf(splits[0]),
                                Integer.valueOf(splits[1])));
                    }
                    rangeTicketItem.setIntervals(intervals);
                    printTicketSection.setPageRange(rangeTicketItem);
                }
                cloudJobTicket.setPrinter(printTicketSection);
               // String cjt = new Gson().toJson(cloudJobTicket);
                //Log.d("CLOUDJOB", cjt);
                Singleton.getInstance().setCloudJobTicket(cloudJobTicket);
                setResult(RESULT_OK);
                finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
