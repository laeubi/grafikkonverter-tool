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
package de.laeubisoft.grafikconverter.bytewriter.csv;

public enum Radix {
    BIN(2, "Binär", "0b", 8),
    HEX(16, "Hexadezimal", "0x", 2),
    DEC(10, "Dezimal", null, 1);
    private final int    radix;
    private final String name;
    private final String prefix;
    private final int    minlength;

    Radix(int rad, String name, String prefix, int minlength) {
        radix = rad;
        this.name = name;
        this.prefix = prefix;
        this.minlength = minlength;
    }

    public int getRadix() {
        return radix;
    }

    public static final RadixFormat DEFAULT_FORMAT = new RadixFormat() {

        @Override
        public String getPrefix(Radix r) {
            return r.prefix;
        }

        @Override
        public String getName(Radix r) {
            return r.name;
        }

        @Override
        public int getMinlength(Radix r) {
            return r.minlength;
        }
    };

}
