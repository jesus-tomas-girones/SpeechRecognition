package com.dani.speechrecognition;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;


public class SphinxSpaActivity extends Activity implements
        RecognitionListener {

    private SpeechRecognizer recognizer;

    private static final String WAKEUP_SEARCH = "wakeup";
    private static final String MAIN_SEARCH = "main";
    private static final String CONFIRMATION_SEARCH = "confirmation";

    private boolean izquierdo = true;
    private boolean delantero = true;
    private int posicion = 1;



    private static final String KEYPHRASE = "despertar";

    private TextView tV, tV2;

    TextToSpeech ttS;

    public enum Estado {
        STANDBY, PRINCIPAL, REPETIRCOMANDO, REPETIRDEFECTOS
    }

    private Estado estado;

    private int numPalabras;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sphynx);
        tV = (TextView) findViewById(R.id.txtSpeechInput);
        tV2 = (TextView) findViewById(R.id.txtSpeechInput2);

        estado = Estado.STANDBY;

        ((Button) findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cambiarA(Estado.PRINCIPAL);
                recognizer.stop();
                recognizer.startListening(MAIN_SEARCH,5000);
            }
        });

        ttS =new TextToSpeech(this, null);
        setUp();
        Log.e("Tag", "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        recognizer.cancel();
        recognizer.shutdown();
    }

    void cambiarA(Estado estado){
        recognizer.stop();
        numPalabras = 0;
        Log.e("SphinxSpa",estado.name());
        switch (estado){
            case STANDBY:
                recognizer.startListening(WAKEUP_SEARCH);
                ttS.speak("Modo espera",TextToSpeech.QUEUE_FLUSH,null);
                break;
            case PRINCIPAL:
                recognizer.startListening(MAIN_SEARCH,10000);
                break;
            case REPETIRCOMANDO:
                recognizer.startListening(CONFIRMATION_SEARCH,10000);
                break;
            default:
                cambiarA(Estado.STANDBY);
        }
    }


    void setUp(){
        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... params) {
                try {
                    Assets assets = new Assets(SphinxSpaActivity.this);
                    File assetDir = assets.syncAssets();
                    recognizer = SpeechRecognizerSetup.defaultSetup()
                            .setAcousticModel(new File(assetDir, "es"))
                            .setDictionary(new File(assetDir, "es-red.dict"))
                            .setKeywordThreshold(1e-45f)
                            .setBoolean("-allphone_ci", true)
                            .getRecognizer();

                    /*recognizer = SpeechRecognizerSetup.defaultSetup()
                            .setAcousticModel(new File(assetDir, "en-us-ptm"))
                            .setDictionary(new File(assetDir, "cmudict-en-us.dict"))
                            .setKeywordThreshold(1e-45f)
                            .setBoolean("-allphone_ci", true)
                            .getRecognizer();*/

                    recognizer.addListener(SphinxSpaActivity.this);

                    //Standby
                    recognizer.addKeyphraseSearch(WAKEUP_SEARCH, KEYPHRASE);

                    //Main
                    recognizer.addGrammarSearch(MAIN_SEARCH, new File(assetDir, "principal.gram"));

                    //Confirmation
                    recognizer.addGrammarSearch(CONFIRMATION_SEARCH, new File(assetDir, "sino.gram"));


                } catch (IOException e) {
                    return e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception result) {
                if (result != null) {
                    Toast.makeText(SphinxSpaActivity.this, "Failed to init recognizer " + result, Toast.LENGTH_LONG);
                } else {
                    tV.setText("Pulsa el boton");
                    Toast.makeText(SphinxSpaActivity.this, "Starting", Toast.LENGTH_LONG);
                    Log.e("Tag", "onPostExecute");
                    //cambiarA(Estado.STANDBY);
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

        if (hypothesis==null){
            return;
        }
        String s = hypothesis.getHypstr();
        tV.setText(""+ hypothesis.getBestScore()+" "+s);



    }

    @Override
    public void onResult(Hypothesis hypothesis) {
        if (hypothesis!=null){
            Log.e("Tag", "onResult " + hypothesis.getHypstr());
            Toast.makeText(this,"encontrado",Toast.LENGTH_LONG);
            //cambiarA(Estado.STANDBY);
            recognizer.stop();
        }
    }

    @Override
    public void onError(Exception e) {

    }

    @Override
    public void onTimeout() {

    }
}
