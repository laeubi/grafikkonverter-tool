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
import java.io.OutputStream;
import java.util.SortedMap;

import de.laeubisoft.grafikconverter.palette.IndexedColorPalette;

public class RawByteWriter implements ByteWriter {

    private String[] extensions;

    @Override
    public WriterContext createContext(final OutputStream stream) {
        return new WriterContext() {

            @Override
            public void writeMetaData(SortedMap<String, String> metaData) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void writeHeader() throws IOException {
                // nothing to do
            }

            @Override
            public void writeFinish() throws IOException {
                //nothing to do
            }

            @Override
            public void writeByte(byte b) throws IOException {
                stream.write(b);
            }

            @Override
            public void newLine() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void writeColorPalette(IndexedColorPalette palette) {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public boolean supportsNewLine() {
        return false;
    }

    @Override
    public boolean supportsMetadata() {
        return false;
    }

    @Override
    public boolean supportsColorPalette() {
        return false;
    }

    @Override
    public String getName() {
        return "Binary";
    }

    @Override
    public String getDescription() {
        return "Schreibt die Bytes als Binärinformation in die Datei";
    }

    @Override
    public String[] getPreferedExtensions() {
        if (extensions != null) {
            return extensions.clone();
        }
        return new String[] { "bin" };
    }

    /**
     * Set the PreferedExtension for this {@link RawByteWriter}
     * 
     * @param extension
     */
    public void setExtension(String[] extension) {
        this.extensions = extension;
    }

}
