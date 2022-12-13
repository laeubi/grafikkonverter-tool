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

import de.laeubisoft.grafikconverter.ditheringalgorithm.DitheringOption;

public final class FloatingPointOption extends DitheringOption<Boolean> {

    public FloatingPointOption() {
	super(Boolean.TRUE, Boolean.class, "Float",
		"Verwendet Floating-Points für die Zwischenberechnung (Verbraucht aber mehr Speicher bei großen Bildern!)");
    }

    @Override
    public FloatingPointOption clone() {
        FloatingPointOption o = new FloatingPointOption();
        o.setValue(getValue());
        return o;
    }
}
