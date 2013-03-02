package net.deimon.abcd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.R;
import org.xmlpull.v1.XmlPullParser;

import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: deimon
 * Date: 4/22/12
 * Time: 5:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExamActivity extends Activity {
    private Lesson lesson;
    private boolean answerIsCorrect = true;
    private View.OnClickListener wrongAnswer = new View.OnClickListener() {
        public void onClick(View view) {
            answerIsCorrect = false;
            Speaker.get().play("choicebad");
//            Speaker.get().speakLater("No");
        }
    };
    private View.OnClickListener rightAnswer = new View.OnClickListener() {
        public void onClick(View view) {
            if (lesson.result(answerIsCorrect)) {
                setResult(RESULT_OK);
                finish();
            }
//            Speaker.get().play("confirmation");
            Speaker.get().play("choicegood");
//            ExamActivity.tts.speak("Yes",TextToSpeech.QUEUE_ADD,null);
            any();
        }
    };
    public static Resources resources;
    private Aspect questionAspect;
    private Aspect answerAspect;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pict);
        resources = getResources();
        questionAspect = Aspect.valueOf(getIntent().getExtras().getString(ModeListActivity.QUESTION_MODE));
        answerAspect = Aspect.valueOf(getIntent().getExtras().getString(ModeListActivity.ANSWER_MODE));

        LessonFactory lessonFactory = null;
        try {
            int xmlId = ExamActivity.resources.getIdentifier(
                    getIntent().getExtras().getString(ModeListActivity.TOPIC), "xml", "com.example");
            XmlResourceParser xpp = resources.getXml(xmlId);
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType() == XmlPullParser.START_TAG) {
                    if (xpp.getName().equals("Aspects")) {
                        xpp.next();
                        String[] aspectNames = xpp.getText().split(",");
                        Aspect[] aspects = new Aspect[aspectNames.length];
                        for (int i = 0; i < aspectNames.length; i++) {
                            aspects[i] = Aspect.valueOf(aspectNames[i]);
                        }
                        lessonFactory = new LessonFactory(aspects);
                    } else if (xpp.getName().equals("Memorial")) {
                        xpp.next();
                        String[] v = xpp.getText().split(",");
                        String name = v[0];
                        String[] values = new String[v.length - 1];
                        for (int i = 1; i < v.length; i++) {
                            values[i - 1] = v[i];
                        }
                        lessonFactory.add(name, values);
                    } else if (xpp.getName().equals("Title")) {
                        xpp.next();
                        setTitle(xpp.getText());
                    } else if (xpp.getName().equals("Shuffle")) {
                        xpp.next();
                        lessonFactory.setShuffle(true);
                    }
                }
                xpp.next();
            }
        } catch (Throwable t) {
            Toast
                    .makeText(this, "Request failed: " + t.toString(), Toast.LENGTH_LONG)
                    .show();
        }


/*        LessonFactory f = new LessonFactory(Aspect.ENGLISH_SMALL, Aspect.PICTURE, Aspect.ENGLISH_AUDIO);
        f.add("bat", R.drawable.bat);
        f.add("bear", R.drawable.bear);
        f.add("bird", R.drawable.bird);
        f.add("butterfly", R.drawable.butterfly);
        f.add("cat", R.drawable.cat);
//        f.add("chick", R.drawable.chick);
//        f.add("clam", R.drawable.clam);
//        f.add("cow", R.drawable.cow);
//        f.add("crow", R.drawable.crow);
//        f.add("deer", R.drawable.deer);
//        f.add("dog", R.drawable.dog);
//        f.add("fish", R.drawable.fish);
//        f.add("frog", R.drawable.frog);
//        f.add("goose", R.drawable.goose);
//        f.add("hen", R.drawable.hen);
//        f.add("horse", R.drawable.horse);
//        f.add("kitten", R.drawable.kitten);
//        f.add("mice", R.drawable.mice);
//        f.add("owl", R.drawable.owl);
//        f.add("pig", R.drawable.pig);
//        f.add("rabbit", R.drawable.rabbit);
//        f.add("rat", R.drawable.rat);
//        f.add("snail", R.drawable.snail);
//        f.add("snake", R.drawable.snake);
//        f.add("tiger", R.drawable.tiger);
//        f.add("toad", R.drawable.toad);
//        f.add("zebra", R.drawable.zebra);*/
        lesson = new SmartLesson(lessonFactory.getMemorials());
        ((ProgressMeter) findViewById(R.id.progress_meter)).setLesson(lesson);
        any();
    }

    private void any() {
        Memorial question = lesson.next();
        questionAspect.show(question, (Button) findViewById(R.id.question));
        HashSet<Memorial> used = new HashSet<Memorial>();
        int[] ids = {R.id.answer0, R.id.answer1, R.id.answer2, R.id.answer3};
        for (int i = 0; i < 4; i++) {
            Memorial answer;
            do {
                answer = lesson.randomMemorial();

            } while (answer == question || used.contains(answer) || answer.getGroup().equals(question.getGroup()));
            used.add(answer);
            Button button = (Button) findViewById(ids[i]);
            answerAspect.show(answer, button);
            button.setOnClickListener(wrongAnswer);
        }
        Button button = (Button) findViewById(ids[(int) (Math.random() * 4)]);
        answerAspect.show(question, button);
        button.setOnClickListener(rightAnswer);
        findViewById(R.id.progress_meter).postInvalidate();
        answerIsCorrect = true;
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure to stop exam?")
                .setPositiveButton("Yes, stop", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setResult(Activity.RESULT_CANCELED);
                        ExamActivity.this.finish();
                    }
                })
                .setNegativeButton("Nope, continue", null)
                .show();
    }
}