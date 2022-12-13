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
package de.laeubisoft.grafikconverter.imagewriter;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import de.laeubisoft.grafikconverter.NamedItem;
import de.laeubisoft.grafikconverter.PixelStream;

/**
 * An {@link ImageWriter} transforms an {@link BufferedImage} to a series of
 * bytes written to an {@link OutputStream}
 * 
 * @author Christoph Läubrich
 */
public interface ImageWriter extends NamedItem {

    public static final String SERVICE_PROPERTY_NAME       = "imagewriter.name";
    public static final String SERVICE_PROPERTY_EXTENSIONS = "imagewriter.extensions";
    public static final String SERVICE_PROPERTY_MIMETYPES  = "imagewriter.mimetypes";

    /**
     * @return the name of default file extensions
     */
    String[] getFileExtensions();

    /**
     * @return the supported mime-types
     */
    String[] getMimeTypes();

    /**
     * Converts a stream of pixels into a stream of bytes
     * 
     * @param pixelStream
     *            the {@link PixelStream} to convert
     * @param outputStream
     *            the stream to write the byte data to
     * @throws IOException
     *             if writing to the stream or reading from the image produces
     *             an error
     */
    void writeImage(PixelStream pixelStream, OutputStream outputStream) throws IOException;
}
