package com.piyush.pictprint;

import android.app.Application;
import android.util.Log;

import com.piyush.pictprint.model.Document;

import java.util.List;

public class PICTPrint extends Application {

    private List<Document> documents;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void setDocuments(List<Document> documents)
    {

        Log.d("SIZE",String.valueOf(documents.size()));
        this.documents = documents;
    }

    public  List<Document> getDocuments()
    {
        return documents;
    }

    public void removeDocument(int index)
    {
        documents.remove(index);
    }
}

