package com.example.parkingsystem;

import android.content.Context;
import android.graphics.Canvas;
import android.view.Gravity;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

/* draw_chart위에 마커뷰 표기하기 위한 클래스 */
public class Draw_MarkerView extends MarkerView {
    private TextView tvContent;
    private Context context;
    public Draw_MarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        tvContent = (TextView) findViewById(R.id.tvContent);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        if (e instanceof CandleEntry) {

            CandleEntry ce = (CandleEntry) e;
            tvContent.setBackgroundResource(R.drawable.stats_marker_img);
            tvContent.setText("Rate: " + Utils.formatNumber(ce.getHigh(), 0, true));
        } else {
            tvContent.setBackgroundResource(R.drawable.stats_marker_img);
            tvContent.setText("Rate: " + Utils.formatNumber(e.getY(), 0, true));
        }

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -(getHeight()+getHeight()/3));
    }

}
