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
package de.laeubisoft.grafikconverter.quantization;

import java.util.List;

import de.laeubisoft.grafikconverter.ARGBPixel;
import de.laeubisoft.grafikconverter.NamedItem;
import de.laeubisoft.grafikconverter.PixelSource;

/**
 * A {@link ColorQuantizer} computes a number of best fitting candidates to use
 * for color reduction of an image
 * 
 * @author Christoph Läubrich
 */
public interface ColorQuantizer extends NamedItem {

    /**
     * Computes a list of color candidates based on the given
     * {@link PixelSource} and the number of preferred colors
     * 
     * @param source
     *            the source where colors should be extracted from
     * @param numberOfColors
     *            maximum number of colors to return
     * @return a list that contains at least one color
     */
    public List<ARGBPixel> getColors(PixelSource source, int numberOfColors);
}
