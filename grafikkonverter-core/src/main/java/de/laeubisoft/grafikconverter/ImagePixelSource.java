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
package de.laeubisoft.grafikconverter;

import java.awt.image.BufferedImage;

public class ImagePixelSource implements PixelSource {

    protected final int[] buffer;
    private final int     w;
    private final int     h;

    public ImagePixelSource(BufferedImage image) {
        w = image.getWidth();
        h = image.getHeight();
        buffer = image.getRGB(0, 0, w, h, null, 0, w);
    }

    @Override
    public int getWidth() {
        return w;
    }

    @Override
    public int getHeight() {
        return h;
    }

    @Override
    public void get(int x, int y, ARGBPixel pixel) {
        int pixelindex = getPixelIndex(x, y);
        getBuffer(pixel, pixelindex);
    }

    protected int getPixelIndex(int x, int y) {
        if (x < 0 || x >= w) {
            throw new IndexOutOfBoundsException("value x=" + x + " is out of bounds [0," + w + ")");
        }
        if (y < 0 || y >= h) {
            throw new IndexOutOfBoundsException("value y=" + y + " is out of bounds [0," + h + ")");
        }
        int pixelindex = y * w + x;
        return pixelindex;
    }

    /**
     * Set the internal buffer of this source into the given image
     * 
     * @param image
     */
    public void updateImage(BufferedImage image) {
        image.setRGB(0, 0, w, h, buffer, 0, w);
    }

    protected void getBuffer(ARGBPixel pixel, int pixelindex) {
        int argb = buffer[pixelindex];
        pixel.set((argb >> 24) & 0xff, (argb >> 16) & 0xff, (argb >> 8) & 0xff, (argb) & 0xff);
    }

}
