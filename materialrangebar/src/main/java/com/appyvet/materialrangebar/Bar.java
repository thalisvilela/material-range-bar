/*
 * Copyright 2013, Edmodo, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with the License.
 * You may obtain a copy of the License in the LICENSE file, or at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.appyvet.materialrangebar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.TypedValue;

import java.util.List;

/**
 * This class represents the underlying gray bar in the RangeBar (without the
 * thumbs).
 */
public class Bar {

    // Member Variables ////////////////////////////////////////////////////////

    private final Paint mBarPaint;

    private final Paint mTickPaint;

    private Paint mLabelPaint;

    // Left-coordinate of the horizontal bar.
    private final float mLeftX;

    private final float mRightX;

    private final float mY;

    private int mNumSegments;

    private float mTickDistance;

    private final float mTickHeight;

    private CharSequence[] mTickLabels;

    // Constructor /////////////////////////////////////////////////////////////


    /**
     * Bar constructor
     *
     * @param ctx        the context
     * @param x          the start x co-ordinate
     * @param y          the y co-ordinate
     * @param length     the length of the bar in px
     * @param tickCount  the number of ticks on the bar
     * @param tickHeight the height of each tick
     * @param tickColor  the color of each tick
     * @param barWeight  the weight of the bar
     * @param barColor   the color of the bar
     * @param isBarRounded if the bar has rounded edges or not
     */
    public Bar(Context ctx,
               float x,
               float y,
               float length,
               int tickCount,
               float tickHeight,
               int tickColor,
               float barWeight,
               int barColor,
               boolean isBarRounded) {

        mLeftX = x;
        mRightX = x + length;
        mY = y;

        mNumSegments = tickCount - 1;
        mTickDistance = length / mNumSegments;
        mTickHeight = tickHeight;
        // Initialize the paint.
        mBarPaint = new Paint();
        mBarPaint.setColor(barColor);
        mBarPaint.setStrokeWidth(barWeight);
        mBarPaint.setAntiAlias(true);
        if (isBarRounded) {
            mBarPaint.setStrokeCap(Paint.Cap.ROUND);
        }
        mTickPaint = new Paint();
        mTickPaint.setColor(tickColor);
        mTickPaint.setStrokeWidth(barWeight);
        mTickPaint.setAntiAlias(true);
    }


    /**
     * Bar constructor
     *
     * @param ctx        the context
     * @param x          the start x co-ordinate
     * @param y          the y co-ordinate
     * @param length     the length of the bar in px
     * @param tickCount  the number of ticks on the bar
     * @param tickHeight the height of each tick
     * @param tickColor  the color of each tick
     * @param barWeight  the weight of the bar
     * @param barColor   the color of the bar
     * @param isBarRounded if the bar has rounded edges or not
     * @param tickLabelColor the color of each tick's label
     * @param tickLabels the label each tick
     */
    public Bar(Context ctx,
               float x,
               float y,
               float length,
               int tickCount,
               float tickHeight,
               int tickColor,
               float barWeight,
               int barColor,
               boolean isBarRounded,
               int tickLabelColor,
               CharSequence[] tickLabels) {
        this(ctx, x, y, length, tickCount, tickHeight, tickColor, barWeight, barColor, isBarRounded);

        if (tickLabels != null) {
            //Set text size in px from dp
            final int textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12,
                    ctx.getResources().getDisplayMetrics());

            // Creates the paint and sets the Paint values
            mLabelPaint = new Paint();
            mLabelPaint.setColor(tickLabelColor);
            mLabelPaint.setAntiAlias(true);
            mLabelPaint.setTextSize(textSize);

            mTickLabels = tickLabels;
        }
    }

    // Package-Private Methods /////////////////////////////////////////////////

    /**
     * Draws the bar on the given Canvas.
     *
     * @param canvas Canvas to draw on; should be the Canvas passed into {#link
     *               View#onDraw()}
     */
    public void draw(Canvas canvas) {

        canvas.drawLine(mLeftX, mY, mRightX, mY, mBarPaint);
    }

    /**
     * Get the x-coordinate of the left edge of the bar.
     *
     * @return x-coordinate of the left edge of the bar
     */
    public float getLeftX() {
        return mLeftX;
    }

    /**
     * Get the x-coordinate of the right edge of the bar.
     *
     * @return x-coordinate of the right edge of the bar
     */
    public float getRightX() {
        return mRightX;
    }

    /**
     * Gets the x-coordinate of the nearest tick to the given x-coordinate.
     *
     * @param thumb the thumb to find the nearest tick for
     * @return the x-coordinate of the nearest tick
     */
    public float getNearestTickCoordinate(PinView thumb) {

        final int nearestTickIndex = getNearestTickIndex(thumb);

        return mLeftX + (nearestTickIndex * mTickDistance);
    }

    /**
     * Gets the zero-based index of the nearest tick to the given thumb.
     *
     * @param thumb the Thumb to find the nearest tick for
     * @return the zero-based index of the nearest tick
     */
    public int getNearestTickIndex(PinView thumb) {

        int tickIndex = (int) ((thumb.getX() - mLeftX + mTickDistance / 2f) / mTickDistance);

        if (tickIndex > mNumSegments) {
            tickIndex = mNumSegments;
        } else if (tickIndex < 0) {
            tickIndex = 0;
        }
        return tickIndex;
    }


    /**
     * Set the number of ticks that will appear in the RangeBar.
     *
     * @param tickCount the number of ticks
     */
    public void setTickCount(int tickCount) {

        final float barLength = mRightX - mLeftX;

        mNumSegments = tickCount - 1;
        mTickDistance = barLength / mNumSegments;
    }

    private String getTickLabel(int index) {
        if (index >= mTickLabels.length) {
            return "";
        }

        return mTickLabels[index].toString();
    }

    // Private Methods /////////////////////////////////////////////////////////

    /**
     * Draws the tick marks on the bar.
     *
     * @param canvas Canvas to draw on; should be the Canvas passed into {#link
     *               View#onDraw()}
     */
    public void drawTicks(Canvas canvas, float pinRadius) {
        // Loop through and draw each tick (except final tick).
        int i = 0;
        for (;i < mNumSegments; i++) {
            final float x = i * mTickDistance + mLeftX;
            canvas.drawCircle(x, mY, mTickHeight, mTickPaint);

            if (mLabelPaint != null) {
                drawTickLabel(canvas, getTickLabel(i), x, pinRadius);
            }
        }
        // Draw final tick. We draw the final tick outside the loop to avoid any
        // rounding discrepancies.
        canvas.drawCircle(mRightX, mY, mTickHeight, mTickPaint);

        // Draw final tick's label outside the loop
        if (mLabelPaint != null) {
            drawTickLabel(canvas, getTickLabel(i), mRightX, pinRadius);
        }
    }

    private void drawTickLabel(Canvas canvas, final String label, float x, float pinRadius) {
        Rect labelBounds = new Rect();
        mLabelPaint.getTextBounds(label, 0, label.length(), labelBounds);
        canvas.drawText(label, x - labelBounds.width()/2, mY + labelBounds.height() + pinRadius, mLabelPaint);
    }
}
