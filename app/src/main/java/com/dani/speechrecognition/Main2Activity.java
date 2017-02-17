package com.dani.speechrecognition;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.dani.speechrecognition.Vocabulary.editDistance;


public class Main2Activity extends Activity {

    private String TAG = "Main2";
    private SpeechRecognizer mSpeechRecognizer;
    private Intent mSpeechRecognizerIntent;
    private boolean mIslistening;

    private ImageButton btnSpeak;
    private TextView txtSpeechInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        //((AudioManager)getSystemService(AUDIO_SERVICE)).setStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_MUTE,0);
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.getPackageName());


        SpeechRecognitionListener listener = new SpeechRecognitionListener();
        mSpeechRecognizer.setRecognitionListener(listener);

        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
            }
        });


//Test
        Vocabulary voc = new Vocabulary("casa", "lápiz", "libro", "árbol");
        if( voc.wordToSimbol("lápiz")!=1) {
            Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
        }
        if( !voc.simbolToWord(3).equals("árbol")) {
            Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
        }

        if(editDistance("árbol", "arbo") != 2) {
            Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
        }

        int[] simbols = voc.wordsToSimbols("casa árbo libro");
        String s = voc.simbolsTowords(simbols);
        if( !s.equals("casa árbol libro")) {
            Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
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

    protected class SpeechRecognitionListener implements RecognitionListener
    {

        @Override
        public void onBeginningOfSpeech()
        {
            Log.e(TAG, "onBeginingOfSpeech");
        }

        @Override
        public void onBufferReceived(byte[] buffer)
        {

        }

        @Override
        public void onEndOfSpeech()
        {
            Log.e(TAG, "onEndOfSpeech");
        }

        @Override
        public void onError(int error)
        {
            mSpeechRecognizer.startListening(mSpeechRecognizerIntent);

            Log.e(TAG, "error = " + error);
        }

        @Override
        public void onEvent(int eventType, Bundle params)
        {
            Log.e(TAG, "onEvent");
        }

        @Override
        public void onPartialResults(Bundle partialResults)
        {
            Log.e(TAG, "onPartialResults");
            ArrayList<String> matches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            txtSpeechInput.setText(matches.get(0));
        }

        @Override
        public void onReadyForSpeech(Bundle bundle)
        {
            Log.e(TAG, "onReadyForSpeech"); //$NON-NLS-1$

        }

        @Override
        public void onResults(Bundle results)
        {
            Log.e(TAG, "onResults"); //$NON-NLS-1$
            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            // matches are the return values of speech recognition engine
            // Use these values for whatever you wish to do
            txtSpeechInput.setText(matches.get(0));
        }

        @Override
        public void onRmsChanged(float rmsdB)
        {
            Log.e(TAG, "onRmsChanged");
        }
    }
}
