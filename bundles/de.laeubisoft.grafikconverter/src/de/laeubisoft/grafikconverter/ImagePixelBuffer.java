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
import java.awt.image.RenderedImage;

import de.laeubisoft.grafikconverter.ARGBPixel.ColorComponent;

/**
 * An implementation that use an image as a source
 * 
 * @author Christoph Läubrich
 */
public class ImagePixelBuffer extends ImagePixelSource implements PixelBuffer {

    public ImagePixelBuffer(BufferedImage image) {
        super(image);
    }

    private double[][] floatBuffer;

    @Override
    public void enableFloatingPoint() {
        //TODO support a floating buffer that spawns eg. the width of the image to save space...
        if (floatBuffer == null) {
            double[][] floatBufferTemp = new double[buffer.length][4];
            ARGBPixel pixel = new ARGBPixel();
            for (int i = 0; i < buffer.length; i++) {
                getBuffer(pixel, i);
                floatBufferTemp[i][ColorComponent.ALPHA.ordinal()] = pixel.get(ColorComponent.ALPHA);
                floatBufferTemp[i][ColorComponent.RED.ordinal()] = pixel.get(ColorComponent.RED);
                floatBufferTemp[i][ColorComponent.GREEN.ordinal()] = pixel.get(ColorComponent.GREEN);
                floatBufferTemp[i][ColorComponent.BLUE.ordinal()] = pixel.get(ColorComponent.BLUE);
            }
            floatBuffer = floatBufferTemp;
        }
    }

    @Override
    public void set(int x, int y, ARGBPixel pixel) {
        int pixelindex = getPixelIndex(x, y);
        setBuffer(pixel, pixelindex);
    }

    private void setBuffer(ARGBPixel pixel, int pixelindex) {
        double a = pixel.get(ColorComponent.ALPHA);
        double r = pixel.get(ColorComponent.RED);
        double g = pixel.get(ColorComponent.GREEN);
        double b = pixel.get(ColorComponent.BLUE);
        buffer[pixelindex] = (toInt(a) << 24) | (toInt(r) << 16) | (toInt(g) << 8) | toInt(b);
        if (floatBuffer != null) {
            floatBuffer[pixelindex][ColorComponent.ALPHA.ordinal()] = a;
            floatBuffer[pixelindex][ColorComponent.RED.ordinal()] = r;
            floatBuffer[pixelindex][ColorComponent.GREEN.ordinal()] = g;
            floatBuffer[pixelindex][ColorComponent.BLUE.ordinal()] = b;
        }
    }

    @Override
    protected void getBuffer(ARGBPixel pixel, int pixelindex) {
        if (floatBuffer != null) {
            double[] ds = floatBuffer[pixelindex];
            pixel.set(ds[0], ds[1], ds[2], ds[3]);
        } else {
            super.getBuffer(pixel, pixelindex);
        }
    }

    private static int toInt(double color) {
        //TODO rounding?
        int value = (int) color;
        return value & 0xFF;
    }

    /**
     * converts a {@link PixelStream} into a {@link RenderedImage}
     * 
     * @param pixelStream
     * @return
     */
    public static RenderedImage convertStream(PixelStream pixelStream) {
        //TODO support Alpha... this seems to produce strange results for several fileformats (e.g. JPEG)
        BufferedImage image = new BufferedImage(pixelStream.getWidth(), pixelStream.getHeight(), BufferedImage.TYPE_INT_RGB);
        ImagePixelBuffer buffer = new ImagePixelBuffer(image);
        int index = 0;
        while (pixelStream.hasNext()) {
            ARGBPixel p = pixelStream.next();
            buffer.setBuffer(p, index);
            index++;
        }
        buffer.updateImage(image);
        return image;
    }

}
