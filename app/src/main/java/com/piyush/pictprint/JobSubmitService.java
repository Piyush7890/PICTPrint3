package com.piyush.pictprint;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.piyush.pictprint.Utils.FileUtils;
import com.piyush.pictprint.api.SubmitJobService;
import com.piyush.pictprint.model.Document;

import org.parceler.Parcels;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class JobSubmitService extends IntentService {

    private NotificationChannel channel;
    private NotificationChannel channel2;
    private SubmitJobService submitJobService;
    private int documentsCount;
    private int NOTIF_ID_SUCCESS=200;
    private int NOTIF_ID_STARTED=300;
    private int NOTIF_ID_FAILED=400;
    private Builder uploadingBuilder;

    public JobSubmitService() {
        super(JobSubmitService.class.getName());


        submitJobService = new SubmitJobService("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel("Submit Job", "Job Submission", NotificationManager.IMPORTANCE_LOW);
            channel2 = new NotificationChannel("Submit Job2", "Job Submission", NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(false);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        }
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        documentsCount=0;
        int failedDocs=0;
        List<Document> documents = Parcels.unwrap(intent.getParcelableExtra("Documents"));
        this.documentsCount = documents.size();
       for(int i=0;i<documents.size();i++)
       {
           Document document = documents.get(i);
           Uri uri = document.getUri();
           final File file = FileUtils.getFile(getApplicationContext(), uri);
           CountingRequestBody requestBody = new CountingRequestBody(RequestBody.create(MediaType.parse(getContentResolver().getType(uri)), file), new CountingRequestBody.Listener() {
               @Override
               public void onRequestProgress(long bytesWritten, long contentLength) {
                   int progress = (int) ((double) bytesWritten * 100 / contentLength);
                   uploadingBuilder.setProgress(100,progress,false);
                   NotificationManagerCompat.from(getApplicationContext()).notify(NOTIF_ID_STARTED,uploadingBuilder.build());

               }
           });
           MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
           try {
               createNotifStarted(document, i+1);
               if (submitJobService.submitJob(part, document).execute().isSuccessful()) {
               //   createNotifSuccess(document, i);
               } else {
                   createNotifFailed(document,i+1);
                   failedDocs++;
               }
           } catch (IOException e) {
               e.printStackTrace();
               createNotifFailed(document,i+1);
               failedDocs++;

           }
       }
       NotificationManagerCompat.from(this).cancel(NOTIF_ID_STARTED);
        if(failedDocs==0)
            createNotifSuccess("Print jobs added successfully");
        else
            createNotifSuccess("Failed to add "+String.valueOf(failedDocs)+" out of "+String.valueOf(documentsCount)+" jobs");

    }

    private void createNotifSuccess(String text) {

        Builder builder = new Builder(this, "Submit Job")
                .setContentTitle(text)
                .setAutoCancel(true).setSmallIcon(R.drawable.ic_done_black_24dp);
        NotificationManagerCompat.from(this).notify(NOTIF_ID_SUCCESS, builder.build());

    }

    private void createNotifFailed(Document document, int id) {
        Builder builder = new Builder(this, "Submit Job")
                .setContentTitle("Failed to upload "+document.getName() )
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_close_black_24dp);
        NotificationManagerCompat.from(this).notify(id, builder.build());
    }

    private void createNotifStarted(Document document, int id) {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationManager.createNotificationChannel(channel);
            mNotificationManager.createNotificationChannel(channel2);
        }
        uploadingBuilder = new Builder(this, "Submit Job2")
                .setContentTitle("Uploading "+String.valueOf(id)+"/"+documentsCount+"...")
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setOnlyAlertOnce(true).setOngoing(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Uploading "+document.getName()))
                .setProgress(100, 0, false)
                .setAutoCancel(false).setSmallIcon(R.drawable.ic_print_black_24dp);
        NotificationManagerCompat.from(this).notify(NOTIF_ID_STARTED, uploadingBuilder.build());

    }



}