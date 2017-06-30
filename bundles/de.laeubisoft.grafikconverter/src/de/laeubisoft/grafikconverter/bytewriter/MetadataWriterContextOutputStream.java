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
import java.util.TreeMap;

import de.laeubisoft.grafikconverter.imagewriter.MetadataOutputStream;

public class MetadataWriterContextOutputStream extends WriterContextOutputStream implements MetadataOutputStream {
    private SortedMap<String, String> metaCache = new TreeMap<String, String>();

    private boolean                   started;

    public MetadataWriterContextOutputStream(WriterContext writerContext, int bytesPerLine) {
        super(writerContext, bytesPerLine);
    }

    @Override
    public void writeMetadata(String key, String value) throws IOException {
        if (started) {
            writerContext.writeMetaData(metaCache);
            metaCache.clear();
        } else {
            metaCache.put(key, value);
        }
    }

    @Override
    protected void writeStart() throws IOException {
        writerContext.writeMetaData(metaCache);
        metaCache.clear();
        started = true;
        super.writeStart();
    }

}
