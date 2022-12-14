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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.laeubisoft.grafikconverter.ARGBPixel;
import de.laeubisoft.grafikconverter.ARGBPixel.ColorComponent;
import de.laeubisoft.grafikconverter.PixelSource;
import de.laeubisoft.grafikconverter.quantization.ColorQuantizer;

public class PaulHeckbertColorQuantizer implements ColorQuantizer {

    public static final boolean USE_AVG = false;

    @Override
    public String getName() {
        return "Paul Heckbert Median Cut";
    }

    @Override
    public String getDescription() {
        return "Inspired by COLOR IMAGE QUANTIZATION FOR FRAME BUFFER DISPLAY by Paul S. Heckbert (1982)";
    }

    @Override
    public List<ARGBPixel> getColors(PixelSource source, int numberOfColors) {
        //create a histogram of all colors
        List<HistoPixel> list = createHistogram(source);
        orderBucket(list);
        if (list.size() <= numberOfColors) {
            //if we have less than the number of requested colors we simply return the full list
            List<ARGBPixel> result = new ArrayList<>();
            for (HistoPixel histoPixel : list) {
                result.add(histoPixel.getPixel());
            }
            return result;
        } else if (numberOfColors < 2) {
            //if we have no more than 2 colors we can return the top of the list
            return Collections.singletonList(getTopColor(list));
        }
        int numberOfBuckets = getNumberOfBuckets(numberOfColors);
        List<List<HistoPixel>> allBuckets = splitBucket(list);
        while (allBuckets.size() < numberOfBuckets) {
            List<List<HistoPixel>> newBuckets = new ArrayList<>();
            for (List<HistoPixel> bucket : allBuckets) {
                List<List<HistoPixel>> splitBuckets = splitBucket(bucket);
                newBuckets.add(splitBuckets.get(0));
                newBuckets.add(splitBuckets.get(1));
            }
            allBuckets = newBuckets;
        }
        List<ARGBPixel> result = new ArrayList<>();
        for (List<HistoPixel> bucket : allBuckets) {
            ARGBPixel topColor;
            if (USE_AVG) {
                topColor = getAvgColor(bucket);
            } else {
                topColor = getTopColor(bucket);
            }
            if (topColor != null) {
                result.add(topColor);
            }
        }
        return result;
    }

    protected static List<HistoPixel> createHistogram(PixelSource source) {
        ARGBPixel getter = new ARGBPixel();
        Map<ARGBPixel, HistoPixel> list = new HashMap<>();
        for (int x = 0; x < source.getWidth(); x++) {
            for (int y = 0; y < source.getHeight(); y++) {
                source.get(x, y, getter);
                HistoPixel p = list.get(getter);
                if (p == null) {
                    ARGBPixel clone = getter.clone();
                    list.put(clone, new HistoPixel(clone));
                } else {
                    p.increment();
                }
            }
        }
        return new ArrayList<>(list.values());
    }

    private static ARGBPixel getTopColor(List<HistoPixel> list) {
        if (list.size() > 0) {
            return list.get(0).getPixel();
        }
        return null;
    }

    private static ARGBPixel getAvgColor(List<HistoPixel> list) {
        //TODO the algorithm mentions that the average color should be taken but this produces bad results
        //check what the average should mean -> e.g. color with the smallest distance from the average color?
        if (list.size() > 0) {
            ARGBPixel pixel = new ARGBPixel();
            double sum_r = 0;
            double sum_g = 0;
            double sum_b = 0;
            for (HistoPixel p : list) {
                ARGBPixel argbPixel = p.getPixel();
                sum_r += argbPixel.get(ColorComponent.RED);
                sum_g += argbPixel.get(ColorComponent.GREEN);
                sum_b += argbPixel.get(ColorComponent.BLUE);
            }
            pixel.set(ColorComponent.RED, sum_r / list.size());
            pixel.set(ColorComponent.GREEN, sum_g / list.size());
            pixel.set(ColorComponent.BLUE, sum_b / list.size());
            return pixel;
        }
        return null;
    }

    protected static List<List<HistoPixel>> splitBucket(List<HistoPixel> list) {
        int size = list.size();
        int index = size / 2;
        //TODO calculate the median other than simply getting the half / third
        //        for (HistoPixel histoPixel : list) {
        //
        //        }
        return splitBucket(list, index);
    }

    protected static List<List<HistoPixel>> splitBucket(List<HistoPixel> list, int border) {
        List<List<HistoPixel>> result = new ArrayList<>();
        if (border >= list.size()) {
            result.add(list);
            result.add(new ArrayList<HistoPixel>());
        } else {
            List<HistoPixel> sub1 = list.subList(0, border);
            List<HistoPixel> sub2 = list.subList(border, list.size());
            orderBucket(sub1);
            orderBucket(sub2);
            result.add(sub1);
            result.add(sub2);
        }
        return result;
    }

    protected static void orderBucket(List<HistoPixel> list) {
        HistoPixel min = Collections.min(list);
        HistoPixel max = Collections.max(list);
        //get the delta between min and max
        double dr = delta(min, max, ColorComponent.RED);
        double dg = delta(min, max, ColorComponent.GREEN);
        double db = delta(min, max, ColorComponent.BLUE);
        Collections.sort(list, Collections.reverseOrder(new HistoPixelComparator(dr, dg, db)));
    }

    private static double delta(HistoPixel min, HistoPixel max, ColorComponent colorComponent) {
        return Math.abs(max.getPixel().get(colorComponent) - min.getPixel().get(colorComponent));
    }

    public static int getNumberOfBuckets(int numberOfColors) {
        int buckets = 2;
        while (buckets * 2 <= numberOfColors) {
            buckets = buckets * 2;
        }
        return buckets;
    }

}
