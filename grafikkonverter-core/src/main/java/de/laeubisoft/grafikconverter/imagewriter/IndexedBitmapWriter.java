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
package de.laeubisoft.grafikconverter.imagewriter;

import java.io.IOException;
import java.io.OutputStream;

import de.laeubisoft.grafikconverter.PixelStream;
import de.laeubisoft.grafikconverter.palette.IndexedColorPalette;

public class IndexedBitmapWriter implements ImageWriter {

    private IndexedColorPalette palette;
    private boolean             packBits      = true;
    private boolean             preserverByte = false;

    public IndexedBitmapWriter(IndexedColorPalette palette) {
        this.palette = palette;
    }

    @Override
    public String getName() {
        return "Indizierte Bitmap";
    }

    @Override
    public String[] getFileExtensions() {
        return new String[] { "bin" };
    }

    @Override
    public String[] getMimeTypes() {
        return new String[] { "application/x-index-bmp" };
    }

    @Override
    public void writeImage(PixelStream image, OutputStream outputStream) throws IOException {
        int colorCount = palette.getColorCount();
        int bitsPerColor = requiredBits(colorCount);
        int currentByte = 0;
        int bitsInByte = 0;
        int maxPerByte = getMaxPerByte(bitsPerColor);
        if (outputStream instanceof MetadataOutputStream) {
            MetadataOutputStream meta = (MetadataOutputStream) outputStream;
            meta.writeMetadata("ColorCount", String.valueOf(colorCount));
            meta.writeMetadata("BitsPerColor", String.valueOf(bitsPerColor));
            meta.writeMetadata("ColorsPerByte", String.valueOf(maxPerByte / bitsPerColor));
            meta.writeMetadata("ImageWidth", String.valueOf(image.getWidth()));
            meta.writeMetadata("ImageHeight", String.valueOf(image.getHeight()));
        }
        while (image.hasNext()) {
            //z.B. 101
            int colorIndex = palette.getColorIndex(image.next());
            for (int shift = bitsPerColor - 1; shift >= 0; shift--) {
                if ((colorIndex & (1 << shift)) == 0) {
                    currentByte = currentByte << 1;
                } else {
                    currentByte = (currentByte << 1) | 1;
                }
                bitsInByte++;
                if (bitsInByte == maxPerByte) {
                    outputStream.write(currentByte);
                    currentByte = 0;
                    bitsInByte = 0;
                }
            }
        }
        if (bitsInByte > 0) {
            //add padding and write the rest...
            for (int i = bitsInByte; i < maxPerByte; i++) {
                currentByte = currentByte << 1;
            }
            outputStream.write(currentByte);
        }
    }

    private int getMaxPerByte(int bitsPerColor) {
        if (isPackBits()) {
            if (isPreserverByte()) {
                switch (bitsPerColor) {
                    case 1:
                    case 2:
                    case 4:
                    case 8:
                        return 8;
                    case 3:
                        return 6;
                    case 5:
                    case 6:
                    case 7:
                        return bitsPerColor;
                }
                throw new IllegalArgumentException("invalid bit count (" + bitsPerColor + ")");
            } else {
                return 8;
            }
        } else {
            return bitsPerColor;
        }
    }

    @Override
    public String getDescription() {
        return "Gibt das Bild als indizierte Bitmap mit der erstellten Farbpalette aus";
    }

    private static int requiredBits(int i) {
        if (i < 2) {
            return 1;
        }
        i = i - 1;
        int counter = 0;
        while (i > 0) {
            counter++;
            i = i / 2;

        }
        return counter;
    }

    public boolean isPackBits() {
        return packBits;
    }

    /**
     * set if the Writer should pack bits (default true) of consecutive colors
     * to a single bit
     * 
     * @param packBits
     */
    public void setPackBits(boolean packBits) {
        this.packBits = packBits;
    }

    public boolean isPreserverByte() {
        return preserverByte;
    }

    /**
     * Set if packing is enabled if a color should span more than one byte
     * 
     * @param preserverByte
     */
    public void setPreserverByte(boolean preserverByte) {
        this.preserverByte = preserverByte;
    }

}
