package wapchief.com.collectiondemo.customView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.widget.TextView;

/**
 * @author wapchief
 * @date 2018/8/1
 */

@SuppressLint("AppCompatCustomView")
public class BoldTextView extends TextView {
    public BoldTextView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        TextPaint tp = new TextPaint();
        tp.setFakeBoldText(true);
        super.onDraw(canvas);
    }
}
