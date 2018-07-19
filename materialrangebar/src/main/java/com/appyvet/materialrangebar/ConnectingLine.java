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

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the blue connecting line between the two thumbs.
 */
public class ConnectingLine {

    // Member Variables ////////////////////////////////////////////////////////

    private final List<Paint> paints = new ArrayList<>();

    private final float mY;

    // Constructor /////////////////////////////////////////////////////////////

    /**
     * Constructor for connecting line
     *
     * @param y                    the y co-ordinate for the line
     * @param connectingLineWeight the weight of the line
     * @param connectingLineColors the color of the line
     */
    public ConnectingLine(float y, float connectingLineWeight,
                          ArrayList<Integer> connectingLineColors) {

        // Initialize the paint, set values
        for (int color : connectingLineColors) {
            Paint paint = new Paint();
            paint.setColor(color);
            paint.setStrokeWidth(connectingLineWeight);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setAntiAlias(true);

            paints.add(paint);
        }

        mY = y;
    }

    // Package-Private Methods /////////////////////////////////////////////////

    /**
     * Draw the connecting line between the two thumbs in rangebar.
     *
     * @param canvas     the Canvas to draw to
     * @param leftThumb  the left thumb
     * @param rightThumb the right thumb
     */
    public void draw(Canvas canvas, PinView leftThumb, PinView rightThumb) {
        int spacing = canvas.getWidth() / paints.size();
        for (int index = 0; index < paints.size(); index++) {
            float startX = spacing * index;
            float endX = startX + spacing;

            float leftX = leftThumb.getX();
            float rightX = rightThumb.getX();

            float drawStartX = -1;
            float drawEndX = -1;

            if (startX < leftX && endX > rightX) {
                drawStartX = leftX;
                drawEndX = rightX;
            } else if (startX >= leftX && endX <= rightX) {
                drawStartX = startX;
                drawEndX = endX;
            } else if (startX >= leftX && endX >= rightX) {
                drawStartX = startX;
                drawEndX = endX - (endX - rightX);
            } else if (endX >= leftX && endX <= rightX) {
                drawStartX = leftX;
                drawEndX = endX;
            }

            if (drawStartX > -1 && drawEndX > -1 && drawStartX <= drawEndX) {
                Paint currentPaint = paints.get(index);

                canvas.drawLine(drawStartX, mY, drawEndX, mY, currentPaint);
            }
        }

    }

    /**
     * Draw the connecting line between for single slider.
     *
     * @param canvas     the Canvas to draw to
     * @param rightThumb the right thumb
     * @param leftMargin the left margin
     */
    public void draw(Canvas canvas, float leftMargin, PinView rightThumb) {
        int spacing = canvas.getWidth() / paints.size();
        for (int index = 0; index < paints.size(); index++) {
            float startX = spacing * index;
            if (index == 0)
                startX += leftMargin;

            float endX = startX + spacing;

            float drawEndX;
            if (endX <= rightThumb.getX()) {
                drawEndX = endX;
            } else {
                drawEndX = endX - (endX - rightThumb.getX());
            }

            Paint currentPaint = paints.get(index);
            canvas.drawLine(startX, mY, drawEndX, mY, currentPaint);
        }
    }
}
