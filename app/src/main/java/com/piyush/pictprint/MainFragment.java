package com.piyush.pictprint;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

public class MainFragment extends Fragment {


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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        executors = new AppExecutors();
        adapter = new PreviewAdapter();
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

                Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                fileIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,false);
                fileIntent.addCategory(Intent.CATEGORY_OPENABLE);
                fileIntent.setType("*/*");
                startActivityForResult(fileIntent,250);
            }
        });

        addToQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGone();
                listener.onDocumentAdded(document);
            }
        });
        return v;
    }

    void setGone()
    {
        TransitionManager.beginDelayedTransition(parent);
        header.setVisibility(View.GONE);
        previewList.setVisibility(View.GONE);
        emptyItem.setVisibility(View.VISIBLE);
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
            icon.setImageDrawable(Utils.getIcon(type,getContext()));
            String filename=Utils.queryName(getContext().getContentResolver(),uris.get(0));
            fileName.setText(filename);
            File file = FileUtils.getFile(getContext(),uris.get(0));
           fileSize.setText(FileUtils.getReadableFileSize((int)file.length()));
            document = new Document(filename,file.length(),System.currentTimeMillis(),type);
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
                document.setPrice(Utils.IMAGE_PRICE);
            }

        }
    }

    private void openFile(File file) throws IOException{

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
                        document.setpages(renderer.getPageCount());
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
                            progressBar.setVisibility(View.GONE);
                            previewList.setVisibility(View.VISIBLE);
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
