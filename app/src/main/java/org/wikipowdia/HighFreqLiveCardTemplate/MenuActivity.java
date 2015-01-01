package org.wikipowdia.HighFreqLiveCardTemplate;

import java.util.HashMap;
import java.util.Locale;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.UtteranceProgressListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/****************************************
 * creates the menu on click gesture within the live card
 * add new menus to the case switch statement
 *
 *
 */

public class MenuActivity extends Activity implements OnInitListener {
    private TextToSpeech tts;
    private boolean mAttachedToWindow;
    private boolean mTTSSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTTSSelected = false;
    }
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        mAttachedToWindow = true;
        openOptionsMenu();
    }
    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAttachedToWindow = false;
    }
    @Override
    public void openOptionsMenu() {
        if (mAttachedToWindow) {
            super.openOptionsMenu();
        } }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.stop:
                stopService(new Intent(this, AppService.class));
                return true;
            case R.id.tts:
                mTTSSelected = true;
                tts = new TextToSpeech(this, this);
                tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    // 1
                    @Override
                    public void onDone(String utteranceId) {
                        if (tts != null) {
                            tts.stop();
                            tts.shutdown();
                        }
                        finish(); }
                    @Override
                    public void onError(String utteranceId) {
                    }
                    @Override
                    public void onStart(String utteranceId) {
                    }
                });
                return true;
            case R.id.asr:
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                startActivityForResult(i, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        } }
    @Override
    public void onOptionsMenuClosed(Menu menu) {
        if (!mTTSSelected)
            finish();
    }
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) { } else {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"helloID");
// 2
                tts.speak("Hello Glass!", TextToSpeech.QUEUE_FLUSH, map);
// 3
            }
        }
    }
}