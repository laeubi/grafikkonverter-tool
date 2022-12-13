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

import java.util.HashSet;
import java.util.Set;

import de.laeubisoft.grafikconverter.PixelBuffer;

/**
 * This algorithm simply do nothing on
 * {@link #dither(PixelBuffer, de.laeubisoft.grafikconverter.palette.ColorPalette, Set)}
 * 
 * @author Christoph Läubrich
 */
public class NullTransform implements DitheringAlgorithm {

    private String name;

    public NullTransform(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void dither(PixelBuffer pixelBuffer, de.laeubisoft.grafikconverter.palette.ColorPalette colorPalette, Set<DitheringOption<?>> options) {
    }

    @Override
    public Set<DitheringOption<?>> createOptions() {
        return new HashSet<DitheringOption<?>>();
    }

    @Override
    public String getDescription() {
        return "Gibt das Bild unverändert zurück";
    }
}
