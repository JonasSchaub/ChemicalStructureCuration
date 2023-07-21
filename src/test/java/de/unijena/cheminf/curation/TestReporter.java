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

package de.unijena.cheminf.curation;

import de.unijena.cheminf.curation.enums.ErrorCodes;
import de.unijena.cheminf.curation.reporter.IReporter;
import de.unijena.cheminf.curation.reporter.ReportDataObject;

import java.util.Objects;

/**
 * IReporter implementation for test purposes only; may be used to test the functionalities of IProcessingStep
 * implementations without the need to visually inspect a generated report. The test reporter gives the option to
 * declare a set of error codes as allowed. The number of times allowed and not allowed error codes are reported is
 * counted, and it is traced whether the reporter has been initialized. The {@link #report()} method throws exceptions
 * if not allowed error codes were counted, the processing ended with a fatal exception or the reporter has not been
 * initialized.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public class TestReporter implements IReporter {

    /**
     * Array of error codes that are declared as allowed.
     */
    private final ErrorCodes[] allowedErrorCodesArray;

    /**
     * The number of times report data objects with allowed error codes were appended to the reporter.
     */
    private int allowedErrorCodesCount = 0;

    /**
     * The number of times report data objects with not allowed error codes were appended to the reporter.
     */
    private int notAllowedErrorCodesCount = 0;

    /**
     * Boolean value whether the data processing the report refers to ended with a fatal exception.
     */
    private boolean endedWithFatalException = false;

    /**
     * Boolean value whether the report is initialized.
     */
    private boolean reportIsInitialized = false;

    /**
     * Constructor; initializes the test reporter and optionally takes a set of allowed error codes.
     *
     * @param aSetOfAllowedErrorCodes any number of allowed error codes
     */
    public TestReporter(ErrorCodes... aSetOfAllowedErrorCodes) {
        this.allowedErrorCodesArray = aSetOfAllowedErrorCodes;
    }

    /**
     * Sets {@link #isReportIsInitialized()} to true and resets the counts of allowed and not allowed error codes by
     * calling the method {@link #clear()}.
     */
    @Override
    public void initializeNewReport() throws Exception {
        this.clear();
    }

    /**
     * Checks whether the error code contained by the given report data object is part of the set of allowed error
     * codes. Increases the count of allowed or of not allowed error codes respectively.
     *
     * @param aReportDataObject the report data object containing the data referring to the reported issue
     */
    @Override
    public void appendReport(ReportDataObject aReportDataObject) throws NullPointerException {
        //TODO
        Objects.requireNonNull(aReportDataObject, "The passed report data object is null.");
        if (this.allowedErrorCodesArray != null) {
            ErrorCodes tmpReporterErrorCode = aReportDataObject.getErrorCode();
            for (ErrorCodes tmpErrorCode : this.allowedErrorCodesArray) {
                if (tmpErrorCode == tmpReporterErrorCode) {
                    this.allowedErrorCodesCount++;
                    return;
                }
            }
        }
        this.notAllowedErrorCodesCount++;
    }

    /**
     * Checks whether the reporter was initialized and whether no not allowed error codes were reported and the
     * processing did not end with a fatal exception. Throws exceptions otherwise. Does not reset the counts or flags.
     *
     * @throws Exception if the not allowed error codes count exceeds zero; if the ended with fatal exception flag is
     *                   true; if the report has not been initialized
     */
    @Override
    public void report() throws Exception {if (!this.reportIsInitialized) {
            throw new Exception("The report has not been initialized.");
        }
        if (this.endedWithFatalException) {
            throw new Exception("Processing ended with fatal exception.");
        }
        if (this.notAllowedErrorCodesCount > 0) {
            throw new Exception("Not allowed error codes were reported.");
        }
    }

    /**
     * Clears the counts of allowed and not allowed error codes by resetting them to zero; resets whether the processing
     * ended with a fatal exception to false. Does not reset whether the reporter was initialized (use {@link #reset()}
     * for this).
     */
    @Override
    public void clear() {
        this.allowedErrorCodesCount = 0;
        this.notAllowedErrorCodesCount = 0;
        this.endedWithFatalException = false;
    }

    /**
     * Resets all class fields (except the error codes array) to their initial values.
     */
    public void reset() {
        this.clear();
        this.reportIsInitialized = false;
    }

    @Override
    public boolean isEndedWithFatalException() {
        return this.endedWithFatalException;
    }

    @Override
    public void setEndedWithFatalException(boolean anEndedWithFatalException) {
        this.endedWithFatalException = anEndedWithFatalException;
    }

    /**
     * Returns the array of allowed error codes.
     *
     * @return array of ErrorCodes
     */
    public ErrorCodes[] getAllowedErrorCodesArray() {
        return this.allowedErrorCodesArray;
    }

    /**
     * Returns the number of times report data objects with allowed error codes were appended to the reporter.
     *
     * @return integer value
     */
    public int getAllowedErrorCodesCount() {
        return this.allowedErrorCodesCount;
    }

    /**
     * Returns the number of times report data objects with not allowed error codes were appended to the reporter.
     *
     * @return integer value
     */
    public int getNotAllowedErrorCodesCount() {
        return this.notAllowedErrorCodesCount;
    }

    /**
     * Returns whether the report is initialized.
     *
     * @return boolean value
     */
    public boolean isReportIsInitialized() {
        return this.reportIsInitialized;
    }

}
