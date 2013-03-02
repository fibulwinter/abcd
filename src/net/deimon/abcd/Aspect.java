package net.deimon.abcd;

import android.view.View;
import android.widget.Button;

/**
 * Created by IntelliJ IDEA.
 * User: deimon
 * Date: 4/22/12
 * Time: 8:46 PM
 * To change this template use File | Settings | File Templates.
 */
public enum Aspect {
    QUESTION,
    ANSWER,
    GROUP,
    ENGLISH_CAPS {
        @Override
        public void show(Memorial memorial, Button button) {
            button.setText(memorial.getName().toUpperCase());
            button.setOnClickListener(null);
        }
    },
    ENGLISH_CAPS_AUDIO {
        @Override
        public void show(final Memorial memorial, Button button) {
//            button.setBackgroundDrawable(ExamActivity.resources.getDrawable(com.example.R.drawable.audio));
            button.setText(memorial.getName().toUpperCase());
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Speaker.get().speakLater(memorial.getName());
                }
            });
        }
    },
    ENGLISH_SMALL {
        @Override
        public void show(Memorial memorial, Button button) {
            button.setText(memorial.getName().toLowerCase());
            button.setOnClickListener(null);
        }
    },
    RUSSIAN,
    ENGLISH_AUDIO {
        @Override
        public void show(final Memorial memorial, final Button button) {
//            button.setBackgroundDrawable(ExamActivity.resources.getDrawable(com.example.R.drawable.audio));
            Speaker.get().speakLater(memorial.getName());
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Speaker.get().speakLater(memorial.getName());
                }
            });
        }
    },
    PICTURE {
        @Override
        public void show(Memorial memorial, Button button) {
            Object picture = memorial.get(Aspect.PICTURE);
            if (picture == null) {
                picture = memorial.getName();
            }
            int drawable = ExamActivity.resources.getIdentifier((String) picture, "drawable", "com.example");
            button.setBackgroundDrawable(ExamActivity.resources.getDrawable(drawable));
        }
    };

    public void show(Memorial memorial, Button button) {
        button.setText(memorial.get(this).toString());
        button.setOnClickListener(null);
    }

}
