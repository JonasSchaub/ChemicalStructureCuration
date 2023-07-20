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
import org.openscience.cdk.exception.CDKException;

import java.io.IOException;

/**
 * Interface of all reporters. Reporters are meant to generate reports out of data referring to issues with structures.
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
 * String tmpOptionalID = "uvw";
 * //
 * // appending a report data object to the reporter (as done by the processing steps)
 * ReportDataObject tmpReportDataObject = new ReportDataObject(
 *         tmpErrorCode, tmpClassOfProcessingStep, tmpProcesingStepIdentifier,
 *         tmpAtomContainer, tmpMolID, tmpOptionalID
 * );
 * tmpReporter.appendReport(tmpReportDataObject);
 * }</pre>
 *
 * The final call of
 * <pre>{@code
 * tmpReporter.report();
 * }</pre>
 * either generates or finalizes the report (depending on the reporter).
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
     * @throws Exception if an exception occurs initializing the report (reporter specific)
     */
    public void initializeNewReport() throws Exception;

    /**
     * Appends the reported data to the reporter. Depending on the reporter, the data is either kept in memory until the
     * {@link #report()} method is called, or directly written to the report.
     *
     * @param aReportDataObject the data to append to the report
     * @throws NullPointerException if the given ReportDataObject instance is null
     */
    public void appendReport(ReportDataObject aReportDataObject) throws NullPointerException;

    /**
     * Either generates the report out of all data kept in memory or finalizes the report that has already been
     * initialized and appended with the reported data (depending on the reporter).
     *
     * @throws Exception if an exception occurs generating / finalizing the report (reporter specific)
     */
    public void report() throws Exception;

    /**
     * Clears all data kept in memory (if the reporter keeps the appended data in memory).
     *
     * @throws Exception if the data can not be cleared (reporter specific)
     */
    public void clear() throws Exception;

    /**
     * Returns whether the processing, that is reported, ended with a fatal exception.
     *
     * @return boolean value
     */
    public boolean isEndedWithFatalException();

    /**
     * Sets whether the processing, that is reported, ended with a fatal exception.
     *
     * @param anEndedWithFatalException boolean value
     */
    public void setEndedWithFatalException(boolean anEndedWithFatalException);

}
