package net.deimon.abcd;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.TextView;
import com.example.R;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: deimon
 * Date: 4/23/12
 * Time: 12:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProgressMeter extends TextView {
    private Lesson lesson;
    public Paint paint;

    public ProgressMeter(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        setText("Yes");
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (lesson != null) {
            List<Memorial> memorials = lesson.getMemorials();
            float dx = ((float) getWidth()) / memorials.size();
            for (int i = 0; i < memorials.size(); i++) {
                Lesson.Status status = lesson.getStatus(memorials.get(i));
                int colorId = R.color.unknown;
                switch (status) {
                    case UNKNOWN:
                        colorId = R.color.unknown;
                        break;
                    case LEARNING:
                        colorId = R.color.learning;
                        break;
                    case MASTERED:
                        colorId = R.color.mastered;
                        break;
                }
                paint.setColor(getResources().getColor(colorId));
                canvas.drawOval(new RectF(dx * i, 0, dx * (i + 1), getHeight()), paint);

//                canvas.drawRect(new RectF(dx*i,0,dx*(i+1),getHeight()), paint);
            }
        }

    }
}
