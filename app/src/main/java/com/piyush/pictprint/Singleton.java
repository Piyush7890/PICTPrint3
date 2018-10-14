package com.piyush.pictprint;

import android.util.Log;

import com.piyush.pictprint.CJT.CloudJobTicket;
import com.piyush.pictprint.model.Document;

import java.util.List;

public class Singleton {

   
        // Create the instance
        private static Singleton instance;
    private List<Document> documents;
    private String cloudJobTicket;

    public static Singleton getInstance()
        {
            if (instance== null) {
                synchronized(Singleton.class) {
                    if (instance == null)
                        instance = new Singleton();
                }
            }
            // Return the instance
            return instance;
        }

        private Singleton()
        {
        }

    public void setDocuments(List<Document> documents)
    {

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

    public void setCloudJobTicket(String cloudJobTicket)
    {

        this.cloudJobTicket = cloudJobTicket;
    }

    public String getCloudJobTicket() {
        return cloudJobTicket;
    }
}
