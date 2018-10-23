package com.piyush.pictprint;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.piyush.pictprint.CJT.CloudJobTicket;
import com.piyush.pictprint.CJT.PageRangeTicketItem;
import com.piyush.pictprint.Utils.AppExecutors;
import com.piyush.pictprint.Utils.FileUtils;
import com.piyush.pictprint.Utils.GridSpacingItemDecoration;
import com.piyush.pictprint.Utils.MorphTransform;
import com.piyush.pictprint.Utils.Utils;
import com.piyush.pictprint.adapter.PreviewAdapter;
import com.piyush.pictprint.model.Document;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

public class MainFragment extends Fragment {


    @BindView(R.id.filter)
    FloatingActionButton filter;

    OvershootInterpolator overshoot;
    @BindView(R.id.parent)
    ViewGroup parent;
@BindView(R.id.empty_item)
    View emptyItem;
@BindView(R.id.add_file)
    Button addFile;
@BindView(R.id.header)
View header;
@BindView(R.id.icon)
    ImageView icon;
@BindView(R.id.file_size)
    TextView fileSize;
@BindView(R.id.file_name)
TextView fileName;
@BindView(R.id.preview_list)
    RecyclerView previewList;
@BindView(R.id.progressBar)
    ProgressBar progressBar;
@BindView(R.id.add_to_queue)
        Button addToQueue;
PreviewAdapter adapter;
AppExecutors executors;
Document document;
DocumentAddedListener listener;
    private int pageCount;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        executors = new AppExecutors();
        adapter = new PreviewAdapter();
        overshoot = new OvershootInterpolator();
    }

    public boolean isDocumentPresent()
    {
        return emptyItem.getVisibility() != View.VISIBLE;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = ((MainActivity) getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main,container, false);
        ButterKnife.bind(this, v);
        previewList.addItemDecoration(
                new GridSpacingItemDecoration(2,
                        Math.round(
                                Utils.convertDpToPixel(16,getContext())),true,0));
        previewList.setAdapter(adapter);
        addFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] mimeTypes =
                        {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                                "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                                "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                                "text/plain",
                                "application/pdf",
                                "image/jpg","image/png","image/jpeg"};
                String mimeTypesStr = "";


                Intent fileIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                for (String mimeType : mimeTypes) {
                    mimeTypesStr += mimeType + "|";
                }
                fileIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

                 //fileIntent.setType("application/pdf, image/*");
                //fileIntent.setType(mimeTypesStr.substring(0,mimeTypesStr.length() - 1));
                fileIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,false);
                fileIntent.addCategory(Intent.CATEGORY_OPENABLE);
                fileIntent.setType("*/*");
                startActivityForResult(fileIntent,250);
                Log.d("HIIII","HIII1");
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),CJTActivity.class);
                intent.putExtra("isPDF", document.getContentType().equals("application/pdf"));
                intent.putExtra("pageCount",document.getpages());
                MorphTransform.addExtras(intent,getResources().getColor(R.color.colorAccent)
                        ,filter.getHeight()/2);
                ActivityOptions options =
                        ActivityOptions.makeSceneTransitionAnimation(getActivity(),
                                filter, "sharedElement");
                startActivityForResult(intent,
                        500, options.toBundle());
            }
        });

        addToQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGone();
                if(document.getCloudJobTicket()==null)
                document.setCloudJobTicket(Utils.generateDefaultCJT());
                else {
                    CloudJobTicket ticket = Singleton.getInstance().getCloudJobTicket();
                    int copies = ticket.getPrinter().getCopies().getCopies();
                    float price =0;
                    if(document.getContentType().equals("application/pdf"))
                    {
                        List<PageRangeTicketItem.Interval> intervals = ticket.getPrinter().getPageRange().getIntervals();
                        for(int i=0;i<intervals.size();i++)
                    {
                        PageRangeTicketItem.Interval interval = intervals.get(i);
                        price+=interval.getEnd()-interval.getStart()+1;
                    }
                    document.setCopies(copies);
                    price*=copies;
                    document.setPrice(Math.round(price));
                }
                else if(document.getContentType().contains("image"))
                    price=copies*Utils.IMAGE_PRICE;
                    document.setCopies(copies);
                    document.setPrice(Math.round(price));
                }
                listener.onDocumentAdded(document);
            }
        });
        return v;
    }




    @SuppressLint("RestrictedApi")
    void setGone()
    {
        TransitionManager.beginDelayedTransition(parent);
        header.setVisibility(View.GONE);
        previewList.setVisibility(View.GONE);
        emptyItem.setVisibility(View.VISIBLE);
        filter.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==250 && resultCode == RESULT_OK)
        {
            List<Uri> uris = new ArrayList<>();
            if (data.getClipData() != null)
                for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                    uris.add(data.getClipData().getItemAt(i).getUri());
                }
            else {
                uris.add(data.getData());
            }
            emptyItem.setVisibility(View.GONE);
            header.setVisibility(View.VISIBLE);
            String type =getActivity().getContentResolver().getType(uris.get(0));
            Log.d("CONTENTTYPE",type);
            icon.setImageDrawable(Utils.getIcon(type,getContext()));
            String filename=Utils.queryName(getContext().getContentResolver(),uris.get(0));
            fileName.setText(filename);
            File file = FileUtils.getFile(getContext(),uris.get(0));
           fileSize.setText(FileUtils.getReadableFileSize((int)file.length()));
            document = new Document(filename,file.length(),System.currentTimeMillis(),type);
            document.setUri(uris.get(0));
            if(type.equals("application/pdf"))
           {
               progressBar.setVisibility(View.VISIBLE);
               try {
                   openFile(file);
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
           else if(type.contains("image"))
            {
                setFabVisible(true);
                document.setPrice(Utils.IMAGE_PRICE);
            }
            else {
                setFabVisible(true);
                document.setPrice(Utils.SINGLE_PAGE_PRICE);


            }

        }

        if(requestCode==500 && resultCode==RESULT_OK)
        {
             String cjt = new Gson().toJson(Singleton.getInstance().getCloudJobTicket());

            document.setCloudJobTicket(cjt);
        }
    }

    @SuppressLint("RestrictedApi")
    private void setFabVisible(boolean b) {
        if(b)
        {
            filter.setScaleX(0);
            filter.setScaleY(0);
            filter.setVisibility(View.VISIBLE);
            filter.animate().scaleX(1).scaleY(1).setInterpolator(overshoot).setDuration(350L).start();
        }
        else {

            filter.animate().scaleX(0).scaleY(0).start();
            filter.setVisibility(View.GONE);
        }
    }

    private void openFile(File file) throws IOException{

        addToQueue.setEnabled(false);
        final ParcelFileDescriptor descriptor = ParcelFileDescriptor
                .open(file,ParcelFileDescriptor.MODE_READ_ONLY);
        if(descriptor!=null) {
            executors.diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    final List<Bitmap> bitmaps = new ArrayList<>();
                    PdfRenderer  renderer = null;
                    try {
                        renderer = new PdfRenderer(descriptor);
                        pageCount=renderer.getPageCount();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    for(int i=0;i<renderer.getPageCount();i++) {
                        PdfRenderer.Page page = renderer.openPage(i);
                        Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(),
                                Bitmap.Config.ARGB_8888);
                        page.render(bitmap,
                                null,
                                null,
                                PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                        bitmaps.add(bitmap);
                        page.close();
                    }
                    executors.getMainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            addToQueue.setEnabled(true);
                            document.setPrice(pageCount);
                            document.setpages(pageCount);
                            progressBar.setVisibility(View.GONE);
                            previewList.setVisibility(View.VISIBLE);
                            setFabVisible(true);
                            adapter.setList(bitmaps);
                        }
                    });
                    renderer.close();
                }
            });

        }
    }

    public interface DocumentAddedListener
    {
        void onDocumentAdded(Document document);
    }
}
