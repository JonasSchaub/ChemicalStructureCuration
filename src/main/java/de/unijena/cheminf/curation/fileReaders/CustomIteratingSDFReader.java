/*
 * MIT License
 *
 * Copyright (c) 2023 Samuel Behr, Felix Baensch, Jonas Schaub, Christoph Steinbeck, and Achim Zielesny
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package de.unijena.cheminf.curation.fileReaders;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.io.ISimpleChemObjectReader;
import org.openscience.cdk.io.MDLReader;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.io.MDLV3000Reader;
import org.openscience.cdk.io.formats.IChemFormat;
import org.openscience.cdk.io.formats.IResourceFormat;
import org.openscience.cdk.io.formats.MDLFormat;
import org.openscience.cdk.io.formats.MDLV2000Format;
import org.openscience.cdk.io.formats.MDLV3000Format;
import org.openscience.cdk.io.iterator.DefaultIteratingChemObjectReader;
import org.openscience.cdk.io.iterator.IteratingSDFReader;
import org.openscience.cdk.io.setting.BooleanIOSetting;
import org.openscience.cdk.io.setting.IOSetting;
import org.openscience.cdk.tools.ILoggingTool;
import org.openscience.cdk.tools.LoggingToolFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Custom modification of the {@link IteratingSDFReader} that also sees null molecules (structures with the MDL block
 * failing to be read) as being a "next molecule". Also, the first line of the current entry and the line the reader is
 * currently at are made available. Does not extend the {@link IteratingSDFReader} but copies most of its code because
 * of the load of private variables with no getter or setter.
 *
 * @see IteratingSDFReader
 * @author modifications by Samuel Behr
 * @version 1.0.0.0
 */
public class CustomIteratingSDFReader extends DefaultIteratingChemObjectReader<IAtomContainer> {

    //<editor-fold desc="private variables and constants (copied)" defaultstate="collapsed">
    private BufferedReader input;
    private static final ILoggingTool logger               = LoggingToolFactory
            .createLoggingTool(IteratingSDFReader.class);
    private String                                          currentLine;
    private IChemFormat                                     currentFormat;

    private boolean                                         nextAvailableIsKnown;
    private boolean                                         hasNext;
    private final IChemObjectBuilder                        builder;
    private IAtomContainer                                  nextMolecule;

    private BooleanIOSetting forceReadAs3DCoords;

    // if an error is encountered the reader will skip over the error
    private boolean                                         skip                 = false;

    // buffer to store pre-read Mol records in
    private final StringBuilder                             buffer               = new StringBuilder(10000);

    private static final String                             LINE_SEPARATOR       = "\n";

    // patterns to match
    private static final Pattern MDL_VERSION          = Pattern.compile("[vV](2000|3000)");
    private static final String  M_END                = "M  END";
    private static final String  SDF_RECORD_SEPARATOR = "$$$$";
    private static final String  SDF_DATA_HEADER      = "> ";

    // map of MDL formats to their readers
    private final Map<IChemFormat, ISimpleChemObjectReader> readerMap            = new HashMap<>(
            5);
    //</editor-fold>

    //<editor-fold desc="private variables (custom)" defaultstate="collapsed">
    /**
     * The counter of how many molecules have been detected in the file so far - including null molecules / molecules
     * that failed to be imported.
     */
    private int moleculesInFileCounter = 0;

    /**
     * The count of so far read null molecules - structures that failed to be imported.
     */
    private int nullMoleculesCounter = 0;

    /**
     * The count of lines the entry of the "next" molecule starts at.
     */
    private int lineCountAtBeginOfNext = 0;

    /**
     * The count of lines read so far.
     */
    private int currentLineCount = 0;

    /**
     * Boolean value whether a fatal exception caused the {@link #hasNext()} method to return false.
     */
    private boolean endedWithFatalException = false;
    //</editor-fold>

    //<editor-fold desc="Constructors" defaultstate="collapsed">
    /**
     * Constructor; does the same as the respective constructor of the {@link IteratingSDFReader}.
     *
     * @param  in  The Reader to read from
     * @param builder The builder
     * @see IteratingSDFReader#IteratingSDFReader(Reader, IChemObjectBuilder)
     */
    public CustomIteratingSDFReader(Reader in, IChemObjectBuilder builder) {
        this(in, builder, false);
    }

    /**
     * Constructor; does the same as the respective constructor of the {@link IteratingSDFReader}.
     *
     * @param  in  The InputStream to read from
     * @param builder The builder
     * @see IteratingSDFReader#IteratingSDFReader(InputStream, IChemObjectBuilder)
     */
    public CustomIteratingSDFReader(InputStream in, IChemObjectBuilder builder) {
        this(new InputStreamReader(in), builder);
    }

    /**
     * Constructor; does the same as the respective constructor of the {@link IteratingSDFReader}.
     *
     * @param in       the {@link InputStream} to read from
     * @param builder  builder to use
     * @param skip     whether to skip null molecules
     * @see IteratingSDFReader#IteratingSDFReader(InputStream, IChemObjectBuilder, boolean)
     */
    public CustomIteratingSDFReader(InputStream in, IChemObjectBuilder builder, boolean skip) {
        this(new InputStreamReader(in), builder, skip);
    }

    /**
     * Constructor; does the same as the respective constructor of the {@link IteratingSDFReader}.
     *
     * @param in       the {@link Reader} to read from
     * @param builder  builder to use
     * @param skip     whether to skip null molecules
     * @see IteratingSDFReader#IteratingSDFReader(Reader, IChemObjectBuilder, boolean)
     */
    public CustomIteratingSDFReader(Reader in, IChemObjectBuilder builder, boolean skip) {
        this.builder = builder;
        this.setReader(in);
        this.initIOSettings();
        this.setSkip(skip);
    }
    //</editor-fold>

    //<editor-fold desc="public methods (modified)" defaultstate="collapsed">
    /**
     * Returns true if the file contains a next entry. Null molecules - structures that fail to be imported - may be
     * skipped (see {@link #setSkip(boolean)}).
     */
    @Override
    public boolean hasNext() {

        if (nextAvailableIsKnown) {
            return hasNext;
        }

        hasNext = false;
        nextMolecule = null;
        this.lineCountAtBeginOfNext = this.currentLineCount + 1;
        this.endedWithFatalException = false;

        // now try to parse the next Molecule
        try {
            currentFormat = (IChemFormat) MDLFormat.getInstance();

            int lineNum = 0;
            buffer.setLength(0);
            while ((currentLine = this.readLine(this.input)) != null) {

                // still in a molecule
                buffer.append(currentLine).append(LINE_SEPARATOR);
                lineNum++;

                // do MDL molfile version checking
                if (lineNum == 4) {
                    Matcher versionMatcher = MDL_VERSION.matcher(currentLine);
                    if (versionMatcher.find()) {
                        currentFormat = "2000".equals(versionMatcher.group(1)) ? (IChemFormat) MDLV2000Format.getInstance()
                                : (IChemFormat) MDLV3000Format.getInstance();
                    }
                }

                if (currentLine.startsWith(M_END)) {

                    logger.debug("MDL file part read: ", buffer);

                    IAtomContainer molecule = null;

                    try {
                        ISimpleChemObjectReader reader = getReader(currentFormat);
                        reader.setReader(new StringReader(buffer.toString()));
                        molecule = reader.read(builder.newAtomContainer());
                    } catch (Exception exception) { //TODO: specify the type of exception to be caught?
                        logger.error(String.format("Error while reading next molecule (index %d; line %d ff): %s",
                                this.moleculesInFileCounter, this.lineCountAtBeginOfNext, exception.getMessage()));
                        logger.debug(exception);
                    }
                    this.moleculesInFileCounter++;
                    hasNext = true;
                    nextAvailableIsKnown = true;

                    if (molecule != null) {
                        readDataBlockInto(molecule);
                        nextMolecule = molecule;
                        return true;
                    } else {
                        // null molecule, eat up the rest of the entry until '$$$$'
                        this.nullMoleculesCounter++;
                        String line;    //TODO: why not use this.currentLine here?
                        while ((line = this.readLine(this.input)) != null) {
                            if (line.startsWith(SDF_RECORD_SEPARATOR)) {
                                break;
                            }
                        }
                        if (!skip) {
                            return true;
                        }
                        // else: keep on going until a molecule != null is reached
                    }

                    // empty the buffer
                    buffer.setLength(0);
                    lineNum = 0;
                }

                // found SDF record separator ($$$$) without parsing a molecule (separator is detected
                // in readDataBlockInto()) the buffer is cleared and the iterator continues reading TODO: really?
                if (currentLine.startsWith(SDF_RECORD_SEPARATOR)) {
                    buffer.setLength(0);
                    lineNum = 0;
                }
            }
        } catch (IOException exception) {
            this.endedWithFatalException = true;
            logger.fatal(String.format("Fatal error while reading next molecule (index %d; line %dff) : %s",
                    this.moleculesInFileCounter, this.lineCountAtBeginOfNext, exception.getMessage()));
            logger.fatal(exception);
        }

        // reached end of file or failed the import due to a fatal exception
        return false;

    }

    /**
     * Indicate whether the reader should skip over SDF records
     * that cause problems. If true the reader will fetch the next
     * molecule
     *
     * @param skip ignore error molecules continue reading
     */
    public void setSkip(boolean skip) {
        this.skip = skip;
    }
    //</editor-fold>

    //<editor-fold desc="private methods (custom)" defaultstate="collapsed">
    /**
     * Reads and returns the next line and increases the {@link #currentLineCount} if it is not null.
     *
     * @param aBufferedReader buffered reader to read the next line from
     * @return the read line
     * @throws NullPointerException if the given reader is null
     * @throws IOException if the call of {@link BufferedReader#readLine()} causes an exception
     */
    private String readLine(BufferedReader aBufferedReader) throws IOException {
        Objects.requireNonNull(aBufferedReader, "aBufferedReader (instance of BufferedReader) is null.");
        String tmpReadLine = aBufferedReader.readLine();
        if (tmpReadLine != null) {
            this.currentLineCount++;
        }
        return tmpReadLine;
    }
    //</editor-fold>

    //<editor-fold desc="private and public methods (unmodified)">
    @Override
    public IResourceFormat getFormat() {
        return currentFormat;
    }

    /**
     * Method will return an appropriate reader for the provided format. Each reader is stored
     * in a map, if no reader is available for the specified format a new reader is created. The
     * {@see ISimpleChemObjectReadr#setErrorHandler(IChemObjectReaderErrorHandler)} and
     * {@see ISimpleChemObjectReadr#setReaderMode(DefaultIteratingChemObjectReader)}
     * methods are set.
     *
     * @param  format The format to obtain a reader for
     * @return        instance of a reader appropriate for the provided format
     */
    private ISimpleChemObjectReader getReader(IChemFormat format) {

        // create a new reader if not mapped
        if (!readerMap.containsKey(format)) {

            ISimpleChemObjectReader reader;
            if (format instanceof MDLV2000Format)
                reader = new MDLV2000Reader();
            else if (format instanceof MDLV3000Format)
                reader = new MDLV3000Reader();
            else if (format instanceof MDLFormat)
                reader = new MDLReader();
            else
                throw new IllegalArgumentException("Unexpected format: " + format);
            reader.setErrorHandler(this.errorHandler);
            reader.setReaderMode(this.mode);
            if (currentFormat instanceof MDLV2000Format) {
                reader.addSettings(getSettings());
            }

            readerMap.put(format, reader);

        }

        return readerMap.get(format);

    }

    private void readDataBlockInto(IAtomContainer m) throws IOException {
        String dataHeader;
        StringBuilder sb = new StringBuilder();
        currentLine = this.readLine(this.input);
        while (currentLine != null) {
            if (currentLine.startsWith(SDF_RECORD_SEPARATOR))
                break;
            logger.debug("looking for data header: ", currentLine);
            String str = currentLine;
            if (str.startsWith(SDF_DATA_HEADER)) {
                dataHeader = extractFieldName(str);
                skipOtherFieldHeaderLines(str);
                String data = extractFieldData(sb);
                if (dataHeader != null) {
                    logger.info("fieldName, data: ", dataHeader, ", ", data);
                    m.setProperty(dataHeader, data);
                }
            } else {
                break;
            }
        }
    }

    private String extractFieldData(StringBuilder data) throws IOException {
        data.setLength(0);
        while (currentLine != null && !currentLine.startsWith(SDF_RECORD_SEPARATOR)) {
            if (currentLine.startsWith(SDF_DATA_HEADER))
                break;
            logger.debug("data line: ", currentLine);
            if (data.length() > 0)
                data.append('\n');
            data.append(currentLine);
            currentLine = this.readLine(this.input);
        }
        // trim trailing newline
        int len = data.length();
        if (len > 1 && data.charAt(len-1) == '\n')
            data.setLength(len-1);
        return data.toString();
    }

    private String skipOtherFieldHeaderLines(String str) throws IOException {
        while (str.startsWith(SDF_DATA_HEADER)) {
            logger.debug("data header line: ", currentLine);
            currentLine = this.readLine(this.input);
            str = currentLine;
        }
        return str;
    }

    private String extractFieldName(String str) {
        int index = str.indexOf('<');
        if (index != -1) {
            int index2 = str.indexOf('>', index);
            if (index2 != -1) {
                return str.substring(index + 1, index2);
            }
        }
        return null;
    }

    /**
     * Returns the next {@link IAtomContainer}; might be null if the import of the respective structure failed.
     */
    @Override
    public IAtomContainer next() {
        if (!nextAvailableIsKnown) {
            hasNext = hasNext();
        }
        nextAvailableIsKnown = false;
        if (!hasNext) {
            throw new NoSuchElementException();
        }
        return nextMolecule;
    }

    @Override
    public void close() throws IOException {
        input.close();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setReader(Reader reader) {
        if (reader instanceof BufferedReader) {
            input = (BufferedReader) reader;
        } else {
            input = new BufferedReader(reader);
        }
        nextMolecule = null;
        nextAvailableIsKnown = false;
        hasNext = false;
    }

    @Override
    public void setReader(InputStream reader) {
        setReader(new InputStreamReader(reader));
    }

    private void initIOSettings() {
        forceReadAs3DCoords = new BooleanIOSetting("ForceReadAs3DCoordinates", IOSetting.Importance.LOW,
                "Should coordinates always be read as 3D?", "false");
        addSetting(forceReadAs3DCoords);
    }

    public void customizeJob() {
        fireIOSettingQuestion(forceReadAs3DCoords);
    }
    //</editor-fold>

    //<editor-fold desc="public properties (custom)" defaultstate="collapsed">
    /**
     * Returns the total count of molecules detected in the file so far - including null molecules / molecules that
     * failed to be imported.
     * TODO: rename? e.g. totalMoleculesCounter?
     *
     * @return integer value
     */
    public int getMoleculesInFileCounter() {
        return this.moleculesInFileCounter;
    }

    /**
     * Returns the count of so far read null molecules - structures that failed to be imported.
     * TODO: rename? e.g. failedImportsCounter?
     *
     * @return integer value
     */
    public int getNullMoleculesCounter() {
        return this.nullMoleculesCounter;
    }

    /**
     * Returns the count of lines the entry of the "next" molecule starts at.
     *
     * @return integer value
     */
    public int getLineCountAtBeginOfNext() {
        return this.lineCountAtBeginOfNext;
    }

    /**
     * Returns the count of lines read so far.
     *
     * @return integer value
     */
    public int getCurrentLineCount() {
        return this.currentLineCount;
    }

    /**
     * Returns true if a fatal exception caused the {@link #hasNext()} method to return false.
     *
     * @return boolean value
     */
    public boolean isEndedWithFatalException() {
        return this.endedWithFatalException;
    }
    //</editor-fold>

}
