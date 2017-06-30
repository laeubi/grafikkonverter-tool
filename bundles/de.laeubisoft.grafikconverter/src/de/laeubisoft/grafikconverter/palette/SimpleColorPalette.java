//   Copyright 2017 Christoph Läubrich
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.
package de.laeubisoft.grafikconverter.palette;

import java.util.concurrent.atomic.AtomicInteger;

import de.laeubisoft.grafikconverter.ARGBPixel;
import de.laeubisoft.grafikconverter.ARGBPixel.ColorComponent;

public class SimpleColorPalette implements IndexedColorPalette {

    private int             colorIndex   = 2;

    public static final int SCHWELLE_MAX = 255;
    private ARGBPixel[]     color        = { new ARGBPixel(255, 255, 255, 255), new ARGBPixel(255, 0, 0, 0), new ARGBPixel(255, 127, 127, 127) };

    //TODO calculate the optimal value from image data
    private AtomicInteger   schwelle     = new AtomicInteger((int) (SCHWELLE_MAX / 2d));

    private int             colorCount   = -1;

    @Override
    public void getNearestPaletteColor(ARGBPixel pixel) {
        boolean colorSet = getColorAtIndex(pixel, getColorIndex(pixel));
        if (!colorSet) {
            //this should never happen but can be the case when the palette has no colors at all
            throw new IllegalStateException("can't find a color in palette?!?");
        }
    }

    @Override
    public int getColorIndex(ARGBPixel search) {
        if (search == null) {
            return -1;
        }
        double minDistance = Double.MAX_VALUE;
        int minIndex = -1;
        int schwelle = getSchwelle().get();
        double faktor = 1.5 - schwelle / (double) (SCHWELLE_MAX + 1);
        for (int i = 0; isValidIndex(i); i++) {
            ARGBPixel c = color[i];
            if (c != null) {
                if (c.equalsColor(search)) {
                    //perfect match
                    return i;
                } else {
                    //compute deltas
                    double da = geDelta(c, search, faktor, ColorComponent.ALPHA);
                    double dr = geDelta(c, search, faktor, ColorComponent.RED);
                    double dg = geDelta(c, search, faktor, ColorComponent.GREEN);
                    double db = geDelta(c, search, faktor, ColorComponent.BLUE);
                    double dist = da + dr + dg + db;
                    if (dist < minDistance) {
                        minDistance = dist;
                        minIndex = i;
                    }
                }
            }
        }
        return minIndex;
    }

    private double geDelta(ARGBPixel c, ARGBPixel search, double faktor, ColorComponent component) {
        return Math.abs(c.get(component) * faktor - search.get(component));
    }

    public AtomicInteger getSchwelle() {
        return schwelle;
    }

    @Override
    public int getColorCount() {
        if (colorCount < 0) {
            int c = 0;
            for (int i = 0; isValidIndex(i); i++) {
                if (color[i] != null) {
                    c++;
                }
            }
            colorCount = c;
        }
        return colorCount;
    }

    private boolean isValidIndex(int i) {
        return i < colorIndex && i < color.length;
    }

    public void setMaxColorIndex(int newIdx) {
        this.colorIndex = newIdx;
        colorCount = -1;
    }

    @Override
    public int getMaxColorIndex() {
        return colorIndex;
    }

    @Override
    public boolean getColorAtIndex(ARGBPixel pixel, int index) {
        if (isValidIndex(index)) {
            ARGBPixel is = color[index];
            if (is != null) {
                is.copyTo(pixel);
                return true;
            }
        }
        return false;
    }

    public void setColor(int index, ARGBPixel pixel) {
        if (pixel == null) {
            if (index < color.length) {
                color[index] = null;
            }
        } else {
            if (index >= color.length) {
                ARGBPixel[] colorNew = new ARGBPixel[index + 10];
                System.arraycopy(color, 0, colorNew, 0, color.length);
                color = colorNew;
            }
            if (color[index] == null) {
                color[index] = new ARGBPixel();
            }
            color[index].copyFrom(pixel);
        }
        colorCount = -1;
    }

}
