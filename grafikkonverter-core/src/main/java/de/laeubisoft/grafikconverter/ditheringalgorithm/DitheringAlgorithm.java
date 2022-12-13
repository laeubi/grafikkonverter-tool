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
package de.laeubisoft.grafikconverter.ditheringalgorithm;

import java.util.Set;

import de.laeubisoft.grafikconverter.NamedItem;
import de.laeubisoft.grafikconverter.PixelBuffer;
import de.laeubisoft.grafikconverter.palette.ColorPalette;

/**
 * A Dithering-Algorithm is used to reduce the color-count of an image while
 * maintaining the illusion of a larger color space
 * 
 * @author Christoph Läubrich
 */
public interface DitheringAlgorithm extends NamedItem {

    /**
     * filter the given {@link PixelBuffer}
     * 
     * @param pixelBuffer
     *            the {@link PixelBuffer} to filter
     * @param colorPalette
     *            the {@link ColorPalette} to use
     * @param options
     *            optional Array of {@link DitheringOption}
     * @return a new dithererd image or the transformed original image
     */
    public void dither(PixelBuffer pixelBuffer, ColorPalette colorPalette, Set<DitheringOption<?>> options);

    /**
     * Creates a new Array of options that can be used to configure the
     * Algorithm, it should be taken care that these options are not modified by
     * concurrent threads in that case each thread should use its own copy of
     * options
     * 
     * @return an array of {@link DitheringOption}s or an empty array if no such
     *         options exits
     */
    public Set<DitheringOption<?>> createOptions();

}
