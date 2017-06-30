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

/**
 * This class serves as a shared buffer space to allow decoding/encoding of a
 * pixel inside a single integer in ARGB Format while maintain operations to
 * work on the individual rgb values. If you need to store the state of several
 * Pixel you must clone it!
 * 
 * @author Christoph Läubrich
 */
public final class ARGBPixel implements Cloneable {

    public static enum ColorComponent {
        ALPHA,
        RED,
        GREEN,
        BLUE
    };

    private double a;
    private double r;
    private double g;
    private double b;
    private int    hashCode;

    public ARGBPixel() {
    }

    public ARGBPixel(double a, double r, double g, double b) {
        set(a, r, g, b);
    }

    public final void set(double a, double r, double g, double b) {
        setValues(a, r, g, b);
    }

    public final double get(ColorComponent colorComponent) {
        switch (colorComponent) {
            case ALPHA:
                return a;
            case RED:
                return r;
            case BLUE:
                return b;
            case GREEN:
                return g;
            default:
                throw new IllegalArgumentException("unsupported color component " + colorComponent);
        }
    }

    public final void set(ColorComponent colorComponent, double value) {
        switch (colorComponent) {
            case ALPHA:
                setValues(value, r, g, b);
                return;
            case RED:
                setValues(a, value, g, b);
                return;
            case GREEN:
                setValues(a, r, value, b);
                return;
            case BLUE:
                setValues(a, r, g, value);
                return;
            default:
                throw new IllegalArgumentException("unsupported color component " + colorComponent);
        }
    }

    @Override
    public ARGBPixel clone() {
        ARGBPixel p = new ARGBPixel();
        copyTo(p);
        return p;
    }

    /**
     * Copy the colors of this pixel to the other pixel
     * 
     * @param other
     */
    public void copyTo(ARGBPixel other) {
        other.setValues(a, r, g, b);
    }

    /**
     * Copy the color from the other pixel to this pixel
     * 
     * @param other
     */
    public void copyFrom(ARGBPixel other) {
        setValues(other.a, other.r, other.g, other.b);
    }

    protected void setValues(double a, double r, double g, double b) {
        this.a = a;
        this.r = r;
        this.g = g;
        this.b = b;
        hashCode = calcHashCode();
    }

    @Override
    public String toString() {
        return "[a=" + a + ", r=" + r + ", g=" + g + ", b=" + b + "]";
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    private int calcHashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(a);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(b);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(g);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(r);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ARGBPixel other = (ARGBPixel) obj;
        return equalsColor(other);
    }

    public boolean equalsColor(ARGBPixel other) {
        if (Double.doubleToLongBits(a) != Double.doubleToLongBits(other.a))
            return false;
        if (Double.doubleToLongBits(b) != Double.doubleToLongBits(other.b))
            return false;
        if (Double.doubleToLongBits(g) != Double.doubleToLongBits(other.g))
            return false;
        if (Double.doubleToLongBits(r) != Double.doubleToLongBits(other.r))
            return false;
        return true;
    }

}
