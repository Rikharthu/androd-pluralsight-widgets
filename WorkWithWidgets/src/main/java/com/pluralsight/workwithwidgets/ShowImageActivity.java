package com.pluralsight.workwithwidgets;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ImageView;

public class ShowImageActivity extends Activity {

  public static final String EXTRA_IMAGE_RESOURCE_ID = "image resource id";
  private static final int DEFAULT_IMAGE_RESOURCE_ID = R.drawable.fwankwin_in_a_box_64x64;
//  private static final int DEFAULT_IMAGE_RESOURCE_ID = R.drawable.fwankwin_chair_kitkat;

  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        if (savedInstanceState == null) {
          Intent startIntent = getIntent();
          int imageViewResourceId =
              startIntent.getIntExtra(EXTRA_IMAGE_RESOURCE_ID, DEFAULT_IMAGE_RESOURCE_ID);

          PlaceholderFragment fragment = new PlaceholderFragment();
          Bundle args = new Bundle(1);
          args.putInt(EXTRA_IMAGE_RESOURCE_ID, imageViewResourceId);
          fragment.setArguments(args);
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.show_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_show_image, container, false);

          // Get the imageView and set the image resource to the
          // id value stored received from the Activity's intent
          int imageViewResourceId = getArguments().getInt(EXTRA_IMAGE_RESOURCE_ID);
          ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);
          imageView.setImageResource(imageViewResourceId);

          return rootView;
        }
    }

}
