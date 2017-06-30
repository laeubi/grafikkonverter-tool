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

public class WriterContextOutputStream extends OutputStream {
    protected final WriterContext writerContext;
    private final int             perLine;
    int                           written = 0;
    boolean                       first   = true;
    boolean                       closed  = false;

    public WriterContextOutputStream(WriterContext writerContext, int bytesPerLine) {
        this.writerContext = writerContext;
        this.perLine = bytesPerLine;
    }

    @Override
    public void write(int b) throws IOException {
        if (first) {
            writeStart();
            first = false;
        }
        if (written == perLine) {
            writerContext.newLine();
            written = 0;
        }
        writerContext.writeByte((byte) (b & 0xFF));
        written++;
    }

    protected void writeStart() throws IOException {
        writerContext.writeHeader();
    }

    @Override
    public void close() throws IOException {
        if (!closed) {
            writeEnd();
            closed = true;
        }
        super.close();
    }

    protected void writeEnd() throws IOException {
        writerContext.writeFinish();
    }
}
