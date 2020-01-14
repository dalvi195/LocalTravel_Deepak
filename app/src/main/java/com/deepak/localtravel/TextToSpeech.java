package com.deepak.localtravel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class TextToSpeech extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText tvResult;
    private ImageButton imageButton;
    private Button transLate;
    public TextView result;
    public Spinner spinner;
    String language_pair="en-hi";
    String url;

    //Set context
    Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_speech);

        tvResult = (EditText) findViewById(R.id.tvResult);
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        transLate= (Button) findViewById(R.id.transLate);
        transLate.setVisibility(View.GONE);
        Log.d("language_pair : 1 ",language_pair);
        result = (TextView) findViewById(R.id.result);

        spinner = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.language_arrays, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);

                try{
                    startActivityForResult(intent, 200);

                }
                catch(ActivityNotFoundException e){
                    Toast.makeText(getApplicationContext(), "Intent Problem", Toast.LENGTH_SHORT).show();

                }

            }
        });

        transLate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String textToBeTranslated = tvResult.getText().toString();
                    String urlText = textToBeTranslated.replaceAll(" ", "%20");
                    url = "https://translate.google.com/#auto/hi/"+urlText;
                    Log.d("URL", url);

                    //Default variables for translation
                    String textToBeTranslated1 = tvResult.getText().toString();
                    String languagePair = language_pair; //English to French ("<source_language>-<target_language>")
                    //Executing the translation function
                    Translate(textToBeTranslated1,languagePair);
                }
                catch (Exception e){}
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200){
            if(resultCode== RESULT_OK && data != null){
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                tvResult.setText(result.get(0));

            }
        }
    }

    //Function for calling executing the Translator Background Task
    void Translate(String textToBeTranslated,String languagePair){
        String values = "";
        TranslatorBackgroundTask translatorBackgroundTask= new TranslatorBackgroundTask(context);

        String result2 = null; // Returns the translated text as a String
        try {
            result2 = translatorBackgroundTask.execute(textToBeTranslated,languagePair).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        // String result = translatorBackgroundTask.doInBackground(textToBeTranslated,languagePair);
        Log.d("Translation Result 1",result2); // Logs the result in Android Monitor
        result.setText(result2);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        transLate.setVisibility(View.VISIBLE);
        Log.d("language_pair : ",language_pair);
        String selectLang = parent.getItemAtPosition(position).toString();
        if(selectLang== "Marathi"){
            language_pair = "en-mr" ;

        }else if(selectLang=="Hindi"){
            language_pair = "en-hi" ;

        }else if(selectLang=="Punjabi"){
            language_pair = "en-pa" ;

        }else if(selectLang=="Bangla"){
            language_pair = "en-bn" ;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
