package com.example.eyjobro.ets2_aga;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.graphics.Typeface;

public class user_profile_screen extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_screen);

        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "Fonts/Roboto-Italic.ttf");
        TextView myTextView = (TextView)findViewById(R.id.textView11);
        myTextView.setTypeface(myTypeface);

        Typeface myTypeface1 = Typeface.createFromAsset(getAssets(), "Fonts/NotoSansTelugu-Bold.ttf");
        TextView myTextView1 = (TextView)findViewById(R.id.textView6);
        myTextView1.setTypeface(myTypeface1);

        Typeface myTypeface2 = Typeface.createFromAsset(getAssets(), "Fonts/NotoSansBengali-Regular.ttf");
        TextView myTextView2 = (TextView)findViewById(R.id.textView8);
        myTextView2.setTypeface(myTypeface2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_profile_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
