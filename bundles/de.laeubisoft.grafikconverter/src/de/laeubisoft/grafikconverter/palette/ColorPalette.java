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

import de.laeubisoft.grafikconverter.ARGBPixel;

public interface ColorPalette {

    /**
     * @return the number of colors in this palette
     */
    public int getColorCount();

    /**
     * computes the nearest possible palette color, the new value is set inside
     * the given ARGBPixel
     * 
     * @param pixel
     *            the value to match
     * @param maxdistance
     *            the limit value to use for matching
     */
    public void getNearestPaletteColor(ARGBPixel pixel);

}
