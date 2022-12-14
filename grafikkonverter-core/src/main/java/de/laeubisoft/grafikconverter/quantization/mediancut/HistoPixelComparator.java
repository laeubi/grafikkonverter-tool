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
package de.laeubisoft.grafikconverter.quantization.mediancut;

import java.util.Comparator;

import de.laeubisoft.grafikconverter.ARGBPixel;
import de.laeubisoft.grafikconverter.ARGBPixel.ColorComponent;

public class HistoPixelComparator implements Comparator<HistoPixel> {
    private final ColorComponent maxComponent;
    private final ColorComponent midComponent;
    private final ColorComponent minComponent;

    public HistoPixelComparator(double dr, double dg, double db) {
        if (dr > dg && dr > db) {
            maxComponent = ColorComponent.RED;
            if (dg > db) {
                midComponent = ColorComponent.GREEN;
                minComponent = ColorComponent.BLUE;
            } else {
                midComponent = ColorComponent.BLUE;
                minComponent = ColorComponent.GREEN;
            }
        } else if (dg > dr && dg > db) {
            maxComponent = ColorComponent.GREEN;
            if (dr > db) {
                midComponent = ColorComponent.RED;
                minComponent = ColorComponent.BLUE;
            } else {
                midComponent = ColorComponent.BLUE;
                minComponent = ColorComponent.RED;
            }
        } else {
            maxComponent = ColorComponent.BLUE;
            if (dr > dg) {
                midComponent = ColorComponent.RED;
                minComponent = ColorComponent.GREEN;
            } else {
                midComponent = ColorComponent.GREEN;
                minComponent = ColorComponent.RED;
            }
        }
    }

    @Override
    public int compare(HistoPixel ho1, HistoPixel ho2) {
        ARGBPixel o1 = ho1.getPixel();
        ARGBPixel o2 = ho2.getPixel();
        int compare = Double.compare(o1.get(maxComponent), o2.get(maxComponent));
        if (compare == 0) {
            compare = Double.compare(o1.get(midComponent), o2.get(midComponent));
            if (compare == 0) {
                Double.compare(o1.get(minComponent), o2.get(minComponent));
            }
        }
        return compare;
    }

}
