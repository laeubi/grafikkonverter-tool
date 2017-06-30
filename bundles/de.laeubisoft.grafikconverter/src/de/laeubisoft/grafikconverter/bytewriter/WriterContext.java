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
package de.laeubisoft.grafikconverter.bytewriter;

import java.io.IOException;
import java.util.SortedMap;

import de.laeubisoft.grafikconverter.palette.IndexedColorPalette;

public interface WriterContext {

    /**
     * Writes the given Metadata (if supported)
     * 
     * @param metaData
     * @throws IOException
     */
    public void writeMetaData(SortedMap<String, String> metaData) throws IOException;

    /**
     * Writes the given ColorPalette (if supported)
     * 
     * @param palette
     * @throws IOException
     */
    public void writeColorPalette(IndexedColorPalette palette) throws IOException;

    /**
     * writes a new line (if supported)
     * 
     * @throws IOException
     */
    public void newLine() throws IOException;

    /**
     * Write any header that might be required
     * 
     * @param out
     * @throws IOException
     */
    public void writeHeader() throws IOException;

    /**
     * Writes a single byte
     * 
     * @param b
     * @param out
     * @throws IOException
     */
    public void writeByte(byte b) throws IOException;

    /**
     * Finish writing
     * 
     * @param out
     * @throws IOException
     */
    public void writeFinish() throws IOException;
}
