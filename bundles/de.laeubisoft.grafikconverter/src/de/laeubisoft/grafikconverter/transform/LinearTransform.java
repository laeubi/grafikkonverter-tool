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
package de.laeubisoft.grafikconverter.transform;

import de.laeubisoft.grafikconverter.ARGBPixel;
import de.laeubisoft.grafikconverter.NamedItem;
import de.laeubisoft.grafikconverter.PixelStream;
import de.laeubisoft.grafikconverter.PixelSource;

/**
 * Converts {@link PixelSource} into a linear series of pixels
 * 
 * @author Christoph Läubrich
 */
public interface LinearTransform extends NamedItem {

    /**
     * @param source
     *            the source to use to extract pixels
     * @return an iterator that can be queried for the next {@link ARGBPixel}
     *         value
     */
    public PixelStream getIterator(PixelSource source);

}
