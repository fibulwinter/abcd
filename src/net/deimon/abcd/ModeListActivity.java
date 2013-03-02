package net.deimon.abcd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.R;

/**
 * Created by IntelliJ IDEA.
 * User: deimon
 * Date: 4/23/12
 * Time: 11:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class ModeListActivity extends Activity {
    public static final String TOPIC = "topic";
    public static final String QUESTION_MODE = "question_mode";
    public static final String ANSWER_MODE = "answer_mode";


    public static final int LESSON_COMPLETE = 1;

    private String lessonTopic = "animals";
    private Aspect questionAspect = Aspect.ENGLISH_CAPS_AUDIO;
    private Aspect answerAspect = Aspect.PICTURE;

    public String[] topics = {
            "multiplication1",
            "multiplication2",
            "multiplication3",
            "animals",
            "food",
            "furniture",
            "sky",
            "toys",
    };
    public Aspect[] questionAspects = new Aspect[]{
            Aspect.ENGLISH_CAPS_AUDIO,
            Aspect.ENGLISH_CAPS,
            Aspect.ENGLISH_SMALL,
            Aspect.PICTURE};
    public Aspect[] answerAspects = new Aspect[]{
            Aspect.PICTURE,
            Aspect.ENGLISH_CAPS,
            Aspect.ENGLISH_SMALL,
            Aspect.ANSWER
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modes);
        Speaker.get().init(this);

        ArrayAdapter<String> topicAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, topics);
        topicAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner topicSpinner = (Spinner) findViewById(R.id.lesson_topic);
        topicSpinner.setAdapter(topicAdapter);
        topicSpinner.setSelection(0);
        topicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                lessonTopic = topics[i];
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ArrayAdapter<Aspect> questionAdapter = new ArrayAdapter<Aspect>(this, android.R.layout.simple_spinner_item, questionAspects);
        questionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner questionSpinner = (Spinner) findViewById(R.id.question_mode);
        questionSpinner.setAdapter(questionAdapter);
        questionSpinner.setSelection(0);


        ArrayAdapter<Aspect> answerAdapter = new ArrayAdapter<Aspect>(this, android.R.layout.simple_spinner_item, answerAspects) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View dropDownView = super.getDropDownView(position, convertView, parent);
                boolean enabled = answerAspects[position] != questionAspect;
                dropDownView.setEnabled(enabled);
                dropDownView.setClickable(!enabled);
                return dropDownView;
            }
        };
        answerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner answerSpinner = (Spinner) findViewById(R.id.answer_mode);
        answerSpinner.setAdapter(answerAdapter);
        answerSpinner.setSelection(0);
        questionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                questionAspect = questionAspects[i];
                if (answerAspect == questionAspect) {
                    if (answerAspects[0] == questionAspect) {
                        answerSpinner.setSelection(1);
                    } else {
                        answerSpinner.setSelection(0);
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                questionAspect = Aspect.ENGLISH_CAPS_AUDIO;
            }
        });
        answerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                answerAspect = answerAspects[i];
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                answerAspect = Aspect.PICTURE;
            }
        });
    }

    public void start(View v) {
        Intent intent = new Intent(this, ExamActivity.class);
        intent.putExtra(TOPIC, lessonTopic);
        intent.putExtra(QUESTION_MODE, questionAspect.name());
        intent.putExtra(ANSWER_MODE, answerAspect.name());
        startActivityForResult(intent, LESSON_COMPLETE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LESSON_COMPLETE) {
            TextView textView = (TextView) findViewById(R.id.lesson_result);
            if (resultCode == Activity.RESULT_OK) {
                textView.setText("Done");
                Speaker.get().speakLater("Well done!");
            } else {
                textView.setText("Canceled");
            }
        }
    }


    @Override
    public void onDestroy() {
        // Don't forget to shutdown!
        Speaker.get().kill();
        super.onDestroy();
    }
}
