//   Copyright 2022 Christoph Läubrich
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
package de.laeubisoft.grafikconverter.transform.eadog;

import de.laeubisoft.grafikconverter.ARGBPixel;
import de.laeubisoft.grafikconverter.PixelSource;
import de.laeubisoft.grafikconverter.PixelStream;
import de.laeubisoft.grafikconverter.transform.LinearTransform;

/**
 * Ein Pixelfilter, welcher die Umrechnung für DOGM/KS108 Displays vornimmt bei
 * welchen Das speicherlayout nicht linear zur Pixeldarstellung ist
 * 
 * @author Christoph Läubrich
 */
public class DOGM128 implements LinearTransform {

    private static final int VERTICAL_PIXEL_COUNT = 8;

    @Override
    public PixelStream getIterator(final PixelSource source) {
        final int height = source.getHeight();
        final int width = source.getWidth();
        final int overflow = height % VERTICAL_PIXEL_COUNT;
        final ARGBPixel pixel = new ARGBPixel();
        return new PixelStream() {

            @Override
            public int getWidth() {
                return width;
            }

            @Override
            public int getHeight() {
                return height - overflow;
            }

            int y = 0;
            int x = 0;
            int z = VERTICAL_PIXEL_COUNT - 1;

            @Override
            public boolean hasNext() {
                return z >= 0 && x < getWidth() && y < getHeight();
            }

            @Override
            public ARGBPixel next() {
                //TODO prüfen für Bilder die nicht genau 8 in der Höhe sind!
                source.get(x, y + z, pixel);
                z--;
                if (z < 0) {
                    z = VERTICAL_PIXEL_COUNT - 1;
                    x++;
                    if (x == width) {
                        x = 0;
                        y += VERTICAL_PIXEL_COUNT;
                    }
                }
                return pixel;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

        };
    }

    @Override
    public String getName() {
        return "DOGM128/KS108 Filter";
    }

    @Override
    public String getDescription() {
        return "Umrechnung für DOGM/KS108 Displays bei welchen das Speicherlayout nicht linear zur Pixeldarstellung ist";
    }

}
