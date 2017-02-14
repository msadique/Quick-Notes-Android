package com.sadique.quicknotes;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.Xml;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView last_date ;
    private EditText notes;
    private QuickNotes qNotes;
    private String nChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        last_date = (TextView) findViewById(R.id.date_id);

        DateFormat df = new SimpleDateFormat("EEE MMM d, h:mm a");
        String date = df.format(Calendar.getInstance().getTime());
        last_date.setText(date);
        notes = (EditText) findViewById(R.id.notes_id);
        notes.setMovementMethod(new ScrollingMovementMethod());
        notes.setTextIsSelectable(true);
    }

    @Override
    protected void onResume() {
        super.onStart();
        qNotes = loadFile();
        if (qNotes != null) {
            if(qNotes.getLastDate().equals(""))
            {
                DateFormat df = new SimpleDateFormat("EEE MMM d, h:mm a");
                String date = df.format(Calendar.getInstance().getTime());
                last_date.setText(date);

            }
            else
                last_date.setText(qNotes.getLastDate());
            if(qNotes.getQuickNotes().equals(""))
            {
                DateFormat df = new SimpleDateFormat("EEE MMM d, h:mm a");
                String date = df.format(Calendar.getInstance().getTime());
                last_date.setText(date);

            }
            nChange = qNotes.getQuickNotes();
            notes.setText(qNotes.getQuickNotes());
            notes.setSelection(qNotes.getQuickNotes().length());
        }
    }
    private QuickNotes loadFile() {

        Log.d(TAG, "loadFile: Loading XML File");
        XmlPullParserFactory xmlFactoryObject;
        qNotes = new QuickNotes();
        try {

            InputStream is = getApplicationContext().openFileInput(getString(R.string.file_name));
            xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser parser = xmlFactoryObject.newPullParser();
            parser.setInput(is, null);

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {

                String lDate = parser.getName();
                if (lDate == null || !lDate.equals("quickNote")) {
                    eventType = parser.next();
                    continue;
                }
                eventType = parser.next();
                while (eventType != XmlPullParser.END_TAG) {
                    lDate = parser.getName();

                    if (lDate == null) {
                        eventType = parser.next();
                        continue;
                    }
                    switch (lDate) {
                        case "lastDate":
                            qNotes.setquickNotes(parser.nextText());
                            break;
                        case "notes":
                            qNotes.setQuickNotes(parser.nextText());
                            break;
                        default:
                    }
                    eventType = parser.next();
                }
            }
            return qNotes;
        } catch (FileNotFoundException e) {
            Toast.makeText(this, getString(R.string.no_notes), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return qNotes;
    }


    @Override
    protected void onPause() {
        super.onPause();

        qNotes.setquickNotes(last_date.getText().toString());

        qNotes.setQuickNotes(notes.getText().toString());

        saveProduct();
    }

    private void saveProduct() {

        if(nChange.equals(qNotes.getQuickNotes()))
           return;
        Log.d(TAG, "saveProduct: Saving XML File");

        try {

            StringWriter writer = new StringWriter();
            XmlSerializer xmlSerializer = Xml.newSerializer();
            xmlSerializer.setOutput(writer);
            xmlSerializer.startDocument("UTF-8", true);
            xmlSerializer.startTag(null, "quickNotes");
            xmlSerializer.startTag(null, "quickNote");
            if (qNotes != null) {
                xmlSerializer.startTag(null, "lastDate");
                DateFormat df = new SimpleDateFormat("EEE MMM d, h:mm a");
                String date = df.format(Calendar.getInstance().getTime());
                xmlSerializer.text(date);
                xmlSerializer.endTag(null, "lastDate");
                xmlSerializer.startTag(null, "notes");
                xmlSerializer.text(qNotes.getQuickNotes());
                xmlSerializer.endTag(null, "notes");
            }
            xmlSerializer.endTag(null, "quickNote");
            xmlSerializer.endTag(null, "quickNotes");
            xmlSerializer.endDocument();
            xmlSerializer.flush();

            FileOutputStream fos =
                    getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);
            fos.write(writer.toString().getBytes());
            fos.close();

            /// You do not need to do the below - this is
            /// only done here to show the XML content.
            ///
            Log.d(TAG, "saveProduct: XML:\n" + writer.toString());
            ///
            ///
            Toast.makeText(this, getString(R.string.notes_saved), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}
