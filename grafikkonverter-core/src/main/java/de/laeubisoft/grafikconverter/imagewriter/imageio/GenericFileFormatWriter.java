//   Copyright 2022 Christoph Läubrich
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
package de.laeubisoft.grafikconverter.imagewriter.imageio;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.imageio.ImageIO;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageWriterSpi;

import de.laeubisoft.grafikconverter.ImagePixelBuffer;
import de.laeubisoft.grafikconverter.PixelStream;
import de.laeubisoft.grafikconverter.imagewriter.ImageWriter;

public class GenericFileFormatWriter implements ImageWriter {

    private final String[] extensions;
    private final String[] mimeTypes;
    private final String displayname;
    private final String formatName;
    private final String description;

    private GenericFileFormatWriter(String displayname, String description, String formatName, String[] ext,
	    String[] mimeTypes) {
	this.displayname = displayname;
	this.description = description;
	this.formatName = formatName;
	this.extensions = ext;
	this.mimeTypes = mimeTypes;
    }

    @Override
    public String getName() {
	return displayname;
    }

    @Override
    public void writeImage(PixelStream pixelStream, OutputStream outputStream) throws IOException {
	// to write an image with image I/O we first must convert the stream to an
	// ordinary image
	ImageIO.write(ImagePixelBuffer.convertStream(pixelStream), formatName, outputStream);
    }

    @Override
    public String[] getFileExtensions() {
	if (extensions == null) {
	    return new String[0];
	}
	return extensions.clone();
    }

    @Override
    public String[] getMimeTypes() {
	if (mimeTypes == null) {
	    return new String[0];
	}
	return mimeTypes.clone();
    }

    @Override
    public String getDescription() {
	return description;
    }

    public static Stream<ImageWriter> writers() {
	Iterator<ImageWriterSpi> providers = IIORegistry.getDefaultInstance().getServiceProviders(ImageWriterSpi.class,
		true);
	return StreamSupport
		.stream(Spliterators.spliteratorUnknownSize(providers, Spliterator.ORDERED), false).map(writerSpi -> {
		    String[] mimeTypes = writerSpi.getMIMETypes();
		    String[] formatNames = writerSpi.getFormatNames();
		    String[] fileSuffixes = writerSpi.getFileSuffixes();
		    return new GenericFileFormatWriter(writerSpi.getDescription(Locale.getDefault()),
			    getLongDescription(writerSpi), formatNames[0], fileSuffixes, mimeTypes);
		});
    }

    private static String getLongDescription(ImageWriterSpi writer) {
	StringBuilder sb = new StringBuilder(writer.getDescription(Locale.getDefault()));
	sb.append(" - Version ");
	sb.append(writer.getVersion());
	sb.append(" - Hersteller ");
	sb.append(writer.getVendorName());
	sb.append(Arrays.toString(writer.getMIMETypes()));
	return sb.toString();
    }

}
