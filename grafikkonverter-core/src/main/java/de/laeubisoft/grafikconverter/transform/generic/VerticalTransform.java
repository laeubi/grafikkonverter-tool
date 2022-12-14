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
package de.laeubisoft.grafikconverter.transform.generic;

import de.laeubisoft.grafikconverter.ARGBPixel;
import de.laeubisoft.grafikconverter.PixelSource;
import de.laeubisoft.grafikconverter.PixelStream;
import de.laeubisoft.grafikconverter.transform.LinearTransform;

public class VerticalTransform implements LinearTransform {

    @Override
    public PixelStream getIterator(final PixelSource source) {
        final int height = source.getHeight();
        final int width = source.getWidth();
        final ARGBPixel pixel = new ARGBPixel();
        return new PixelStream() {

            private int y = 0;
            private int x = 0;

            @Override
            public int getWidth() {
                return width;
            }

            @Override
            public int getHeight() {
                return height;
            }

            @Override
            public boolean hasNext() {
                return x < width && y < height;
            }

            @Override
            public ARGBPixel next() {
                source.get(x, y, pixel);
                y++;
                if (y >= height) {
                    y = 0;
                    x++;
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
        return "Vertikale Ausgabe";
    }

    @Override
    public String getDescription() {
        return "Gibt das Bild von Oben nach Unten und Links nach Rechts aus";
    }

}
