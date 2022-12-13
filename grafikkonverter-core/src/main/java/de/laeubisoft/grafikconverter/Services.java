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
package de.laeubisoft.grafikconverter;

import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;
import java.util.stream.Stream;

import de.laeubisoft.grafikconverter.bytewriter.ByteWriter;

/**
 * Allows unified access to items that can be provided by plugins on the classpath
 */
public class Services {

	private static final ServiceLoader<ByteWriter> byteWriter = ServiceLoader.load(ByteWriter.class, Services.class.getClassLoader());

	public static final Stream<ByteWriter> getByteWriter() {

		return byteWriter.stream().map(Provider::get);
	}
}
