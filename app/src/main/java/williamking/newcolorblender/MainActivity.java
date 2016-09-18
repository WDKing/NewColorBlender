package williamking.newcolorblender;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;

/**
 *  Assistance and Code Snippets acquired from Android's Developer Website
 *
 *  @author William King
 */
public class MainActivity extends AppCompatActivity {

    // Variables

    // Colors
    int firstBlendingColor;
    int secondBlendingColor;
    int blendedColor;

    // SurfaceViews
    SurfaceView firstColorDisplay;
    SurfaceView secondColorDisplay;
    SurfaceView blendedColorDisplay;

    // SeekBar
    SeekBar mixingBar;
    int barMax = 100000;

    // Intent Related Information
    Intent getNewColor;
    int leftSurfaceValue = 1;
    int rightSurfaceValue = 2;


    // Overidden Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set initial background color values for all three surface views
        firstBlendingColor = Color.rgb(0,0,0);
        secondBlendingColor = Color.rgb(255,255,255);
        blendedColor = Color.rgb(0,0,0);

        // SurfaceViews for Colors to Blend and Blended Colors
        firstColorDisplay = (SurfaceView) findViewById(R.id.viewColorToBlend1);
        secondColorDisplay = (SurfaceView) findViewById(R.id.viewColorToBlend2);
        blendedColorDisplay =(SurfaceView) findViewById(R.id.viewBlendedColor);

        // Initialize Background Colors for SurfaceViews
        firstColorDisplay.setBackgroundColor(firstBlendingColor);
        secondColorDisplay.setBackgroundColor(secondBlendingColor);
        blendedColorDisplay.setBackgroundColor(firstBlendingColor);

        // Initialize Intent
        getNewColor = new Intent(Intent.ACTION_SEND);
        getNewColor.addCategory(Intent.CATEGORY_DEFAULT);



        // Establish listener for seeker bar
        mixingBar = (SeekBar) findViewById(R.id.blenderBar);
        mixingBar.setMax(barMax);

        mixingBar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean userChange) {
                blendedColor = Color.rgb(
                        (int) ( ( Color.red(firstBlendingColor)
                                * ( (float)( barMax - progress) / barMax ) )
                                + ( Color.red(secondBlendingColor)
                                * ( (float)progress / barMax ) ) ),
                        (int) ( ( Color.green(firstBlendingColor)
                                * ( (float)( barMax - progress) / barMax ) )
                                + ( Color.green(secondBlendingColor)
                                * ( (float)progress / barMax ) ) ),
                        (int) ( ( Color.blue(firstBlendingColor)
                                * ( (float)( barMax - progress) / barMax ) )
                                + ( Color.blue(secondBlendingColor)
                                * ( (float)progress / barMax ) ) ) );

                Log.i("onProgressChanged","mixColor1: " + firstBlendingColor
                        + ", mixColor2: " + secondBlendingColor
                        + ", blendedColor: " + blendedColor
                        + ", progress: " + progress);

                blendedColorDisplay.setBackgroundColor(blendedColor);
            } // onProgressChanged

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Required to be overridden. Not used.
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Required to be overridden. Not used.
            }
        });

        // Establish Listener for Lefthand SurfaceView
        firstColorDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getNewColor.resolveActivity(getPackageManager()) != null) {
                    Log.i("Call Intent - First","Calls the Intent");
                    startActivityForResult(getNewColor,leftSurfaceValue);
                }
                else {
                    Log.i("Call Intent - First","Failed to call the intent");
                }
            }
        });

        // Establish Listener for Righthand SurfaceView
        secondColorDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getNewColor.resolveActivity(getPackageManager()) != null) {
                    Log.i("Call Intent - Second","Calls the Intent");
                    startActivityForResult(getNewColor,rightSurfaceValue);
                }
                else {
                    Log.i("Call Intent - Second","Failed to call the intent");
                }
            }
        });


    } // onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    } // onCreateOptionsMenu

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
    } // onOptionsItemSelected

    // Manage Intent Receipt
    protected void onActivityResult(int requestCode, int resultCode, Intent returnedColor) {
        Log.i("onActivityResult","Reaches onActivityResult");
        if(resultCode == Activity.RESULT_OK) {

            // Values to manage colors
            int[] colorComponentsToPrint = returnedColor.getIntArrayExtra("Color to Print");

            int redToPrint = colorComponentsToPrint[0];
            int greenToPrint = colorComponentsToPrint[1];
            int blueToPrint = colorComponentsToPrint[2];

            if(requestCode == leftSurfaceValue) {
                firstBlendingColor = Color.rgb(redToPrint,greenToPrint,blueToPrint);

                firstColorDisplay.setBackgroundColor(
                                  Color.rgb(redToPrint,greenToPrint,blueToPrint));
                firstColorDisplay.refreshDrawableState();
            }

            if(requestCode == rightSurfaceValue) {
                secondBlendingColor = Color.rgb(redToPrint,greenToPrint,blueToPrint);
                
                secondColorDisplay.setBackgroundColor(
                                  Color.rgb(redToPrint,greenToPrint,blueToPrint));
                secondColorDisplay.refreshDrawableState();
            }

            Log.i("onActivityResult", "Red: " + redToPrint
                   + ", Green: " + greenToPrint
                   + ", Blue: " + blueToPrint);
        } // if(resultCode == Activity.RESULT_OK)
    } // onActivityResult

} // MainActivity
