package com.example.android.sunshine.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.format.Time;

public class SunshineWatchFace {

    private static final String TIME_FORMAT_WITHOUT_SECONDS = "%02d.%02d";
    private static final String TIME_FORMAT_WITH_SECONDS = TIME_FORMAT_WITHOUT_SECONDS + ".%02d";
    private static final String DATE_FORMAT = "%02d.%02d.%d";
    private static final int DATE_AND_TIME_DEFAULT_COLOUR = Color.WHITE;
    private static final int BACKGROUND_DEFAULT_COLOUR = R.color.primary;

    private final Paint timePaint;
    private final Paint datePaint;
    private final Paint backgroundPaint;
    private final Time time;

    private boolean shouldShowSeconds = true;
    private int backgroundColour = R.color.primary;
    private int dateAndTimeColour = DATE_AND_TIME_DEFAULT_COLOUR;

    private String lowTemp, highTemp;
    private Bitmap bitmap;

    public static SunshineWatchFace newInstance(Context context) {
        Paint timePaint = new Paint();
        timePaint.setColor(DATE_AND_TIME_DEFAULT_COLOUR);
        timePaint.setTextSize(context.getResources().getDimension(R.dimen.time_size));
        timePaint.setAntiAlias(true);

        Paint datePaint = new Paint();
        datePaint.setColor(DATE_AND_TIME_DEFAULT_COLOUR);
        datePaint.setTextSize(context.getResources().getDimension(R.dimen.date_size));
        datePaint.setAntiAlias(true);

        Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(context.getResources().getColor(R.color.primary));

        return new SunshineWatchFace(timePaint, datePaint, backgroundPaint, new Time());
    }

    SunshineWatchFace(Paint timePaint, Paint datePaint, Paint backgroundPaint, Time time) {
        this.timePaint = timePaint;
        this.datePaint = datePaint;
        this.backgroundPaint = backgroundPaint;
        this.time = time;
    }

    public void setTemp(String highTemp, String lowTemp) {
        this.lowTemp = lowTemp;
        this.highTemp = highTemp;
    }

    public void setBitmap(Bitmap bm) {
        this.bitmap = bm;
    }

    public void draw(Canvas canvas, Rect bounds) {
        time.setToNow();
        canvas.drawRect(0, 0, bounds.width(), bounds.height(), backgroundPaint);

        String timeText = String.format(shouldShowSeconds ? TIME_FORMAT_WITH_SECONDS : TIME_FORMAT_WITHOUT_SECONDS, time.hour, time.minute, time.second);
        float timeXOffset = computeXOffset(timeText, timePaint, bounds);
        float timeYOffset = computeTimeYOffset(timeText, timePaint, bounds);
        canvas.drawText(timeText, timeXOffset, timeYOffset, timePaint);

        String dateText = String.format(DATE_FORMAT, time.monthDay, (time.month + 1), time.year);
        float dateXOffset = computeXOffset(dateText, datePaint, bounds);
        float dateYOffset = computeDateYOffset(dateText, datePaint);
        canvas.drawText(dateText, dateXOffset, timeYOffset + dateYOffset, datePaint);

        if (highTemp != null) {
            float tempXOffset = computeXOffset(highTemp, datePaint, bounds);
            float tempYOffset = computeTempYOffset(highTemp, datePaint);
            canvas.drawText(highTemp + "C", tempXOffset - 10.0f, tempYOffset + dateYOffset + timeYOffset, datePaint);
        }

        if (lowTemp != null) {
            float tempXOffset = computeLowXOffset(lowTemp, datePaint, bounds);
            float tempYOffset = computeTempYOffset(lowTemp, datePaint);
            canvas.drawText(lowTemp + "C", tempXOffset, tempYOffset + dateYOffset + timeYOffset, datePaint);
        }

        if (bitmap != null) {

            canvas.drawBitmap(bitmap, 40, timeYOffset + (bitmap.getHeight() - bitmap.getHeight() / 4), datePaint);

        }


    }

    private float computeImageYOffset(Bitmap bm, Paint tempPaint) {
        Rect textBounds = new Rect();
        //tempPaint.getTextBounds(tempText, 0, tempText.length(), textBounds);
        return bm.getHeight();
    }

    private float computeImageXOffset(String text, Paint paint, Rect watchBounds) {
        float centerX = watchBounds.exactCenterX();
        float timeLength = paint.measureText(text);
        return centerX + (timeLength);
    }

    private float computeTempYOffset(String tempText, Paint tempPaint) {
        Rect textBounds = new Rect();
        tempPaint.getTextBounds(tempText, 0, tempText.length(), textBounds);
        return textBounds.height() + 50.0f;
    }

    private float computeLowXOffset(String text, Paint paint, Rect watchBounds) {
        float centerX = watchBounds.exactCenterX();
        float timeLength = paint.measureText(text);
        return centerX + (timeLength);
    }

    private float computeXOffset(String text, Paint paint, Rect watchBounds) {
        float centerX = watchBounds.exactCenterX();
        float timeLength = paint.measureText(text);
        return centerX - (timeLength / 2.0f);
    }

    private float computeTimeYOffset(String timeText, Paint timePaint, Rect watchBounds) {
        float centerY = watchBounds.centerY();
        Rect textBounds = new Rect();
        timePaint.getTextBounds(timeText, 0, timeText.length(), textBounds);
        int textHeight = textBounds.height();
        return centerY - (textHeight);
    }

    private float computeDateYOffset(String dateText, Paint datePaint) {
        Rect textBounds = new Rect();
        datePaint.getTextBounds(dateText, 0, dateText.length(), textBounds);
        return textBounds.height() + 15.0f;
    }

    public void setAntiAlias(boolean antiAlias) {
        timePaint.setAntiAlias(antiAlias);
        datePaint.setAntiAlias(antiAlias);
    }

    public void updateDateAndTimeColourTo(int colour) {
        dateAndTimeColour = colour;
        timePaint.setColor(colour);
        datePaint.setColor(colour);
    }

    public void updateTimeZoneWith(String timeZone) {
        time.clear(timeZone);
        time.setToNow();
    }

    public void setShowSeconds(boolean showSeconds) {
        shouldShowSeconds = showSeconds;
    }

    public void updateBackgroundColourTo(int colour) {
        backgroundColour = colour;
        backgroundPaint.setColor(colour);
    }

    public void restoreBackgroundColour() {
        backgroundPaint.setColor(backgroundColour);
    }

    public void updateBackgroundColourToDefault() {
        backgroundPaint.setColor(R.color.primary);
    }

    public void updateDateAndTimeColourToDefault() {
        timePaint.setColor(DATE_AND_TIME_DEFAULT_COLOUR);
        datePaint.setColor(DATE_AND_TIME_DEFAULT_COLOUR);
    }

    public void restoreDateAndTimeColour() {
        timePaint.setColor(dateAndTimeColour);
        datePaint.setColor(dateAndTimeColour);
    }
}
