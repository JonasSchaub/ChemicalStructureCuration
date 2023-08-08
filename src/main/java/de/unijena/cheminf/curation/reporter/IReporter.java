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

package de.unijena.cheminf.curation.reporter;

import de.unijena.cheminf.curation.processingSteps.IProcessingStep;
import org.apache.commons.lang3.NotImplementedException;

import java.io.IOException;

/**
 * Interface of all reporters; reporters are meant to generate reports out of data referring to issues with structures.
 * This data might have its origin in the processing of sets of structures by instances of {@link IProcessingStep}
 * implementations.
 * <br>
 * <br>
 * <b>Example Usage</b>
 * <pre>{@code
 * IReporter tmpReporter = new *AnIReporterImplementation*();
 * // initialize the report; depending on the reporter, this might not be necessary
 * tmpReporter.initializeNewReport();
 * //
 * // generation of exemplary data
 * ErrorCodes tmpErrorCode = ErrorCodes.INVALID_ATOMIC_NUMBER_ERROR;
 * Class<? extends IProcessingStep> tmpClassOfProcessingStep = HasAllValidAtomicNumbersFilter.class;
 * String tmpProcesingStepIdentifier = "xyz";
 * IAtomContainer tmpAtomContainer = new AtomContainer();
 * String tmpMolID = "abc";
 * String tmpExternalID = "uvw";
 * //
 * // appending a report data object to the reporter (as done by the processing steps)
 * ReportDataObject tmpReportDataObject = new ReportDataObject(
 *         tmpErrorCode, tmpClassOfProcessingStep, tmpProcesingStepIdentifier,
 *         tmpAtomContainer, tmpMolID, tmpExternalID
 * );
 * tmpReporter.appendReport(tmpReportDataObject);
 * //
 * // generating / finalizing the report respectively (depending on the reporter) via
 * tmpReporter.report();
 * // or (if the reported processing process ended with a fatal exception) via
 * tmpReporter.reportAfterFatalException();
 * }</pre>
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public interface IReporter {

    /**
     * Either initializes the report or does nothing (depending on the reporter). Latter is the case, if the reporter
     * keeps all data in memory until the {@code .report()} method is called; the previously appended data might be
     * cleared. If the data is directly written to the report by the {@code .appendReport()} method, this method
     * initializes the report that is then finalized by the {@code .report()} method.
     *
     * @throws IOException if an IOException occurs initializing the report (dependent on the reporter)
     */
    public void initializeNewReport() throws IOException;

    /**
     * Appends the reported data to the reporter. Depending on the reporter, the data is either kept in memory until the
     * {@link #report()} method is called, or directly written to the report.
     *
     * @param aReportDataObject the data referring to the reported issue
     * @throws NullPointerException if the given ReportDataObject instance is null; if the report has not been
     *                              initialized (dependent on the reporter)
     */
    public void appendReport(ReportDataObject aReportDataObject) throws NullPointerException;

    /**
     * Either generates the report out of all data kept in memory or finalizes the report that has already been
     * initialized and appended with the reported data (depending on the type of reporter). Data kept in memory is
     * cleared after finishing the report. If the processing ended with a fatal exception, use the {@link
     * #reportAfterFatalException()} method instead.
     *
     * @throws IOException if an IOException occurs generating or writing to the file (dependent on the reporter)
     * @see #reportAfterFatalException()
     */
    public void report() throws Exception;  //TODO: change to IOException (the MarkDownReporter should not need to throw a CDKException)

    /**
     * Tries to generate / finish the report (see {@link #report()}) even though the processing ended with a fatal
     * exception and appends a respective notification to the report. Data kept in memory is cleared after finishing
     * the report.
     *
     * @throws IOException      if generating / writing to the report file is not possible (if the respective reporter
     *                          does so)
     * @throws RuntimeException in case the reported processing-process was interrupted due to a runtime exception, the
     *                          same reason might hinder the report from being generated / finished
     * @see #report()
     */
    public default void reportAfterFatalException() throws IOException {
        //TODO: remove default after this method has been implemented in the MarkDownReporter
        throw new NotImplementedException("Has yet to be implemented.");
    }

    /**
     * Clears all data kept in memory (if the reporter keeps the appended data in memory).
     */
    public void clear();

}
