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

import java.util.Iterator;

/**
 * A linear Layout of pixel
 * 
 * @author Christoph Läubrich
 */
public interface PixelStream extends Iterator<ARGBPixel> {

    /**
     * @return the width of the linear Layout of pixels
     */
    public int getWidth();

    /**
     * @return the height of the linear Layout of pixels
     */
    public int getHeight();
}
