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
package de.laeubisoft.grafikconverter.ditheringalgorithm.floydsteinberg;

import java.util.HashSet;
import java.util.Set;

import de.laeubisoft.grafikconverter.ARGBPixel;
import de.laeubisoft.grafikconverter.ARGBPixel.ColorComponent;
import de.laeubisoft.grafikconverter.PixelBuffer;
import de.laeubisoft.grafikconverter.ditheringalgorithm.DitheringAlgorithm;
import de.laeubisoft.grafikconverter.ditheringalgorithm.DitheringOption;
import de.laeubisoft.grafikconverter.palette.ColorPalette;

public class FloydSteinbergDitheringAlgorithm implements DitheringAlgorithm {

    private static final double P1 = 7.0 / 16.0;
    private static final double P2 = 3.0 / 16.0;
    private static final double P3 = 5.0 / 16.0;
    private static final double P4 = 1.0 / 16.0;

    @Override
    public void dither(PixelBuffer image, ColorPalette colorPalette, Set<DitheringOption<?>> options) {
	// Init with defaults
	boolean useFloat = false;
	boolean evenOdd = true;
	for (DitheringOption<?> o : options) {
	    if (o instanceof FloatingPointOption) {
		useFloat = (Boolean) o.getValue();
	    } else if (o instanceof EvenOddOption) {
		evenOdd = (Boolean) o.getValue();
	    }
	}
	if (useFloat) {
	    // TODO Let handle on upper level??
	    image.enableFloatingPoint();
	    ditherPixel(image, colorPalette, evenOdd);
	} else {
	    ditherPixel(image, colorPalette, evenOdd);
	}
    }

    private static void ditherPixel(PixelBuffer image, ColorPalette colorPalette, boolean evenOdd) {
	int w = image.getWidth();
	int h = image.getHeight();
	ARGBPixel pixel = new ARGBPixel();
	for (int y = 0; y < h; y++) {
	    int x = 0;
	    boolean even = true;
	    if (evenOdd && ((y % 2) != 0)) {
		x = w - 1;
		even = false;
	    }
	    while (x >= 0 && x < w) {
		// Farbe Auslesen
		image.get(x, y, pixel);
		double qr = pixel.get(ColorComponent.RED);
		double qg = pixel.get(ColorComponent.GREEN);
		double qb = pixel.get(ColorComponent.BLUE);
		// Finde Farbe minimaler distanz
		colorPalette.getNearestPaletteColor(pixel);
		image.set(x, y, pixel);
		// quantisierungsfehler bestimmen
		qr -= pixel.get(ColorComponent.RED);
		qg -= pixel.get(ColorComponent.GREEN);
		qb -= pixel.get(ColorComponent.BLUE);
		// umliegende Pixel anpassen
		if (even) {
		    ratepixel(x + 1, y, image, pixel, w, h, P1, qr, qg, qb);
		    ratepixel(x - 1, y + 1, image, pixel, w, h, P2, qr, qg, qb);
		    ratepixel(x, y + 1, image, pixel, w, h, P3, qr, qg, qb);
		    ratepixel(x + 1, y + 1, image, pixel, w, h, P4, qr, qg, qb);
		    x++;
		} else {
		    ratepixel(x - 1, y, image, pixel, w, h, P1, qr, qg, qb);
		    ratepixel(x + 1, y + 1, image, pixel, w, h, P2, qr, qg, qb);
		    ratepixel(x, y + 1, image, pixel, w, h, P3, qr, qg, qb);
		    ratepixel(x - 1, y + 1, image, pixel, w, h, P4, qr, qg, qb);
		    x--;
		}
	    }
	}
    }

    private static void ratepixel(int x, int y, PixelBuffer image, ARGBPixel pixel, int w, int h, double factor,
	    double error_r, double error_g, double error_b) {
	if ((x > -1) & (y > -1) & (x < w) & (y < h)) {
	    image.get(x, y, pixel);
	    pixel.set(ColorComponent.RED, getNewPixelValue(pixel.get(ColorComponent.RED), factor, error_r));
	    pixel.set(ColorComponent.GREEN, getNewPixelValue(pixel.get(ColorComponent.GREEN), factor, error_g));
	    pixel.set(ColorComponent.BLUE, getNewPixelValue(pixel.get(ColorComponent.BLUE), factor, error_b));
	    image.set(x, y, pixel);
	}
    }

    private static double getNewPixelValue(double pixel, double faktor, double error) {
	double newv = (pixel + faktor * error);
	if (newv > 255.0d) {
	    return 255.0d;
	}
	return newv;
    }

    @Override
    public Set<DitheringOption<?>> createOptions() {
	HashSet<DitheringOption<?>> set = new HashSet<>();
	set.add(new FloatingPointOption());
	set.add(new EvenOddOption());
	return set;
    }

    @Override
    public String getName() {
	return "Floyd-Steinberg";
    }

    @Override
    public String getDescription() {
	return "Benutzt das Floyd-Steinberg Verfahren, die besten Ergebnisse sind mit der Float-Option möglich, diese verbraucht aber auch am meisten Speicher bei der Berechnung";
    }

}
