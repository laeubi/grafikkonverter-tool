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

import de.laeubisoft.grafikconverter.ARGBPixel;

public final class HistoPixel implements Comparable<HistoPixel> {

    private long      count = 1;
    private final ARGBPixel pixel;

    public HistoPixel(ARGBPixel init) {
        this.pixel = init.clone();
    }

    @Override
    public int compareTo(HistoPixel o) {
        long delta = count - o.count;
        if (delta > 0) {
            return 1;
        } else if (delta < 0) {
            return -1;
        } else {
            return 0;
        }
    }

    public long getCount() {
        return count;
    }

    public ARGBPixel getPixel() {
        return pixel;
    }

    @Override
    public String toString() {
        return pixel.toString() + " " + count;
    }

    public void increment() {
        count++;
    }

}
