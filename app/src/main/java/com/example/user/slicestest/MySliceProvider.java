package com.example.user.slicestest;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.Slice;
import androidx.slice.SliceProvider;
import androidx.slice.builders.ListBuilder;
import androidx.slice.builders.ListBuilder.RowBuilder;
import androidx.slice.builders.SliceAction;

public class MySliceProvider extends SliceProvider {
    /**
     * Instantiate any required objects. Return true if the provider was successfully created,
     * false otherwise.
     */
    @Override
    public boolean onCreateSliceProvider() {
        return true;
    }

    /**
     * Converts URL to content URI (i.e. content://com.example.user.slicestest...)
     */
    @Override
    public Uri onMapIntentToUri(Intent intent) {
        // Note: implementing this is only required if you plan on catching URL requests.
        // This is an example solution.
        Uri.Builder uriBuilder = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT);
        if (intent == null) return uriBuilder.build();
        Uri data = intent.getData();
        if (data != null && data.getPath() != null) {
            String path = data.getPath().replace("/", "");
            uriBuilder = uriBuilder.path(path);
        }
        Context context = getContext();
        if (context != null) {
            uriBuilder = uriBuilder.authority(context.getPackageName());
        }
        return uriBuilder.build();
    }

    /**
     * Construct the Slice and bind data if available.
     */
    public Slice onBindSlice(Uri sliceUri) {
//        Context context = getContext();
//        SliceAction activityAction = createActivityAction();
//        if (context == null || activityAction == null) {
//            return null;
//        }
//        if ("/".equals(sliceUri.getPath())) {
//            // Path recognized. Customize the Slice using the androidx.slice.builders API.
//            // Note: ANRs and strict mode is enforced here so don't do any heavy operations.
//            // Only bind data that is currently available in memory.
//            return new ListBuilder(getContext(), sliceUri, ListBuilder.INFINITY)
//                    .addRow(
//                            new RowBuilder(context, sliceUri)
//                                    .setTitle("URI found.")
//                                    .setPrimaryAction(activityAction)
//                    )
//                    .build();
//        } else {
//            // Error: Path not found.
//            return new ListBuilder(getContext(), sliceUri, ListBuilder.INFINITY)
//                    .addRow(
//                            new RowBuilder(context, sliceUri)
//                                    .setTitle("URI not found.")
//                                    .setPrimaryAction(activityAction)
//                    )
//                    .build();
//        }
        final String path = sliceUri.getPath();
        switch (path) {
            case "/mainActivity":
                return createSlice(sliceUri);
        }
        return null;
    }


    /**
     * Slice has been pinned to external process. Subscribe to data source if necessary.
     */
    @Override
    public void onSlicePinned(Uri sliceUri) {
        // When data is received, call context.contentResolver.notifyChange(sliceUri, null) to
        // trigger MySliceProvider#onBindSlice(Uri) again.
    }

    /**
     * Unsubscribe from data source if necessary.
     */
    @Override
    public void onSliceUnpinned(Uri sliceUri) {
        // Remove any observers if necessary to avoid memory leaks.
    }

    public Slice createSlice(Uri sliceUri){
        SliceAction activityAction = createActivityAction();
        ListBuilder listBuilder = new ListBuilder(getContext(), sliceUri, ListBuilder.INFINITY);
        ListBuilder.RowBuilder rowBuilder = new ListBuilder.RowBuilder(listBuilder)
                .setTitle("Launch MainActivity.")
                .setPrimaryAction(activityAction);
        listBuilder.addRow(rowBuilder);
        return listBuilder.build();

    }

    private SliceAction createActivityAction() {
        //Instead of returning null, you should create a SliceAction. Here is an example:

        Intent intent = new Intent(getContext(), MainActivity.class);
        return new SliceAction(
                PendingIntent.getActivity(
                        getContext(), 0, intent, 0
                ),
                IconCompat.createWithResource(getContext(), R.drawable.ic_launcher_foreground),
                ListBuilder.ICON_IMAGE,
                "Launch MainActivity"
        );

    }
}
