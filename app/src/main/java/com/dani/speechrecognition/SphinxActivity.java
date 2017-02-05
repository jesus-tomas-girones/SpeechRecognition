package com.dani.speechrecognition;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;


public class SphinxActivity extends Activity implements
        RecognitionListener {

    private SpeechRecognizer recognizer;

    private static final String WAKEUP_SEARCH = "wakeup";

    private static final String KEYPHRASE = "input command";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sphynx);


        setUp();
    }


    void setUp(){
        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... params) {
                try {
                    Assets assets = new Assets(SphinxActivity.this);
                    File assetDir = assets.syncAssets();
                    recognizer = SpeechRecognizerSetup.defaultSetup()
                            .setAcousticModel(new File(assetDir, "es"))
                            .setDictionary(new File(assetDir, "es.dict"))
                            .setKeywordThreshold(1e-45f)
                            .setBoolean("-allphone_ci", true)
                            .getRecognizer();

                    /*recognizer = SpeechRecognizerSetup.defaultSetup()
                            .setAcousticModel(new File(assetDir, "en-us-ptm"))
                            .setDictionary(new File(assetDir, "cmudict-en-us.dict"))
                            .setKeywordThreshold(1e-45f)
                            .setBoolean("-allphone_ci", true)
                            .getRecognizer();*/

                    recognizer.addListener(SphinxActivity.this);

                    recognizer.addKeyphraseSearch(WAKEUP_SEARCH, KEYPHRASE);
                } catch (IOException e) {
                    return e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception result) {
                if (result != null) {
                    Toast.makeText(SphinxActivity.this, "Failed to init recognizer " + result, Toast.LENGTH_LONG);
                } else {
                    recognizer.startListening(WAKEUP_SEARCH);
                }
            }
        }.execute();

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis!=null && hypothesis.getHypstr().equals(KEYPHRASE)){
            Toast.makeText(this,"partial",Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onResult(Hypothesis hypothesis) {
        if (hypothesis!=null && hypothesis.getHypstr().equals(KEYPHRASE)){
            Toast.makeText(this,"encontrado",Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onError(Exception e) {

    }

    @Override
    public void onTimeout() {

    }
}
