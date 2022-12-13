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

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;

import de.laeubisoft.grafikconverter.ARGBPixel;
import de.laeubisoft.grafikconverter.ARGBPixel.ColorComponent;
import de.laeubisoft.grafikconverter.bytewriter.ByteWriter;
import de.laeubisoft.grafikconverter.bytewriter.WriterContext;
import de.laeubisoft.grafikconverter.palette.IndexedColorPalette;

public class SourceCodeByteWriter implements ByteWriter {

    private static final String DEFAULT_NEW_LINE  = "\r\n";
    private static final String DEFAULT_SEPERATOR = ",";
    private static final String DEFAULT_PADDING   = "0";

    private Radix               radix;

    private String              colorPalettePattern;
    private String              bytePrefix;
    private String              metaDataPattern;
    private String[]            extensions;
    private String              name;
    private String              description;
    private String              bytePadding;
    private String              byteSeperator;
    private String              newLine;
    private String              head;
    private String              tail;
    private String              indent;
    private String              byteNewLine;
    private Integer             dataPadding;

    protected void init(Map<String, ?> properties) {
        //Required Options for writer...
        checkRequired(properties, "writer.radix", "writer.extensions", "writer.name");
        radix = Radix.valueOf((String) properties.get("writer.radix"));
        name = (String) properties.get("writer.name");
        extensions = getExtensions(properties);
        //Optional options with defaults...
        bytePrefix = getWithDefault(properties, "writer.bytePrefix", Radix.DEFAULT_FORMAT.getPrefix(radix));
        description = getWithDefault(properties, "writer.description", "Erzeugt eine Datei im " + name + " Format.");
        bytePadding = getWithDefault(properties, "writer.byte_padding", DEFAULT_PADDING);
        byteSeperator = getWithDefault(properties, "writer.byte_seperator", DEFAULT_SEPERATOR);
        newLine = replaceSpecial(getWithDefault(properties, "writer.newline", DEFAULT_NEW_LINE));
        byteNewLine = replaceSpecial(getWithDefault(properties, "writer.byte_newline", newLine));
        dataPadding = getWithDefault(properties, "writer.data_padding", 0);
        //Optional options...
        colorPalettePattern = replaceSpecial((String) properties.get("writer.color_palette_pattern"));
        metaDataPattern = replaceSpecial((String) properties.get("writer.meta_data_pattern"));
        indent = replaceSpecial((String) properties.get("writer.indent"));
        head = replaceSpecial((String) properties.get("writer.head"));
        tail = replaceSpecial((String) properties.get("writer.tail"));
    }

    private static String[] getExtensions(Map<String, ?> properties) {
        Object property = properties.get("writer.extensions");
        if (property instanceof String[]) {
            return (String[]) property;
        } else if (property instanceof String) {
            return ((String) property).split(",");
        } else {
            return new String[0];
        }
    }

    @Override
    public boolean supportsColorPalette() {
        return colorPalettePattern != null;
    }

    @Override
    public boolean supportsMetadata() {
        return metaDataPattern != null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public WriterContext createContext(OutputStream stream) throws IOException {
        final Writer writer = new OutputStreamWriter(new BufferedOutputStream(stream), "US-ASCII");
        final int minlength = Radix.DEFAULT_FORMAT.getMinlength(radix);
        return new WriterContext() {

            private boolean firstByte;
            private int     bytesWritten;

            @Override
            public void writeMetaData(SortedMap<String, String> metaData) throws IOException {
                if (supportsMetadata()) {
                    for (Entry<String, String> entry : metaData.entrySet()) {
                        writer.write(metaDataPattern.replace("%key", entry.getKey()).replace("%value", entry.getValue()));
                        linebreak();
                    }
                } else {
                    throw new UnsupportedOperationException();
                }
            }

            @Override
            public void writeColorPalette(IndexedColorPalette palette) throws IOException {
                if (supportsColorPalette()) {
                    int maxColorIndex = palette.getMaxColorIndex();
                    ARGBPixel pixel = new ARGBPixel();
                    for (int index = 0; index < maxColorIndex; index++) {
                        if (palette.getColorAtIndex(pixel, index)) {
                            String line = colorPalettePattern.replace("%index", strVal(index))//
                                    .replace("%A", strVal(pixel.get(ColorComponent.ALPHA)))//
                                    .replace("%R", strVal(pixel.get(ColorComponent.RED)))//
                                    .replace("%G", strVal(pixel.get(ColorComponent.GREEN)))// 
                                    .replace("%B", strVal(pixel.get(ColorComponent.BLUE)));
                            writer.write(line);
                            if (supportsNewLine()) {
                                linebreak();
                            }
                        }
                    }
                } else {
                    throw new UnsupportedOperationException();
                }
            }

            protected void linebreak() throws IOException {
                writer.append(newLine);
            }

            private CharSequence strVal(double dVal) {
                return strVal((int) dVal);
            }

            private CharSequence strVal(int integer) {
                return String.valueOf(integer);
            }

            @Override
            public void newLine() throws IOException {
                if (supportsNewLine()) {
                    if (!firstByte) {
                        writer.write(byteSeperator);
                        firstByte = true;
                    }
                    writer.write(byteNewLine);
                    if (indent != null) {
                        writer.write(indent);
                    }
                } else {
                    throw new UnsupportedOperationException();
                }
            }

            @Override
            public void writeHeader() throws IOException {
                this.firstByte = true;
                if (head != null) {
                    writer.write(head);
                }
            }

            @Override
            public void writeByte(byte b) throws IOException {
                if (firstByte) {
                    firstByte = false;
                } else {
                    writer.write(byteSeperator);
                }
                String str = Integer.toString(b & 0xFF, radix.getRadix()).toUpperCase();
                writeBytePrefix();
                int paddings = minlength - str.length();
                while (paddings > 0) {
                    writer.write(bytePadding);
                    paddings--;
                }
                writer.write(str);
                bytesWritten++;
            }

            protected void writeBytePrefix() throws IOException {
                if (bytePrefix != null) {
                    writer.write(bytePrefix);
                }
            }

            @Override
            public void writeFinish() throws IOException {
                if (dataPadding != null) {
                    int mod = dataPadding.intValue();
                    if (mod > 0) {
                        while (bytesWritten % mod != 0) {
                            writeByte((byte) 0);
                        }
                    }
                }
                if (tail != null) {
                    writer.write(tail);
                }
                writer.flush();
            }

        };
    }

    @Override
    public String[] getPreferedExtensions() {
        return extensions.clone();
    }

    @Override
    public boolean supportsNewLine() {
        return byteNewLine != null && !byteNewLine.isEmpty();
    }

    @SuppressWarnings("unchecked")
    private static <T> T getWithDefault(Map<String, ?> properties, String key, T defaultValue) {
        Object object = properties.get(key);
        if (object == null) {
            return defaultValue;
        } else {
            if (defaultValue instanceof Integer && object instanceof String) {
                return (T) Integer.valueOf(Integer.parseInt((String) object));
            }
            return (T) object;
        }
    }

    private static void checkRequired(Map<String, ?> properties, String... requiredOptions) {
        for (String option : requiredOptions) {
            Object obj = properties.get(option);
            if (obj == null || (obj instanceof String && ((String) obj).isEmpty())) {
                throw new IllegalArgumentException("Property '" + option + "' is required and can't be empty!");
            }
        }
    }

    private static String replaceSpecial(String s) {
        if (s == null) {
            return null;
        }
        return s.replace("\\r", "\r").replace("\\n", "\n").replace("\\t", "\t");
    }

}
