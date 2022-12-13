// Copyright 2022 Christoph Läubrich
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package de.laeubisoft.grafikconverter.ditheringalgorithm.schwellwert;

import java.util.HashSet;
import java.util.Set;

import de.laeubisoft.grafikconverter.ARGBPixel;
import de.laeubisoft.grafikconverter.PixelBuffer;
import de.laeubisoft.grafikconverter.ditheringalgorithm.DitheringAlgorithm;
import de.laeubisoft.grafikconverter.ditheringalgorithm.DitheringOption;
import de.laeubisoft.grafikconverter.palette.ColorPalette;

public class SchwellwertDitheringAlgorithm implements DitheringAlgorithm {

    @Override
    public void dither(PixelBuffer image, ColorPalette colorPalette, Set<DitheringOption<?>> options) {
	int w = image.getWidth();
	int h = image.getHeight();
	ARGBPixel p = new ARGBPixel();
	for (int x = 0; x < w; x++) {
	    for (int y = 0; y < h; y++) {
		image.get(x, y, p);
		colorPalette.getNearestPaletteColor(p);
		image.set(x, y, p);
	    }
	}
    }

    @Override
    public Set<DitheringOption<?>> createOptions() {
	return new HashSet<>();
    }

    @Override
    public String getName() {
	return "Schwellwert";
    }

    @Override
    public String getDescription() {
	return "Berechnet das Bild nach dem Schwellwert-Verfahren";
    }

}
