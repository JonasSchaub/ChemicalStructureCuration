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

import de.unijena.cheminf.curation.enums.ErrorCodes;

import java.io.IOException;
import java.util.Objects;

/**
 * IReporter implementation for test purposes only; may be used to test the functionalities of IProcessingStep
 * implementations without the need to visually inspect a generated report. The test reporter gives the option to
 * declare a set of error codes as allowed; the number of times allowed and not allowed error codes are reported is
 * counted, and it is traced whether the reporter has been initialized. The {@link #report()} method throws exceptions
 * if not allowed error codes were reported, the processing ended with a fatal exception or the report has not been
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
     * Boolean value whether the report is initialized.
     */
    private boolean reportIsInitialized = false;

    /**
     * Boolean value whether the current report has been finished.
     */
    private boolean reportHasBeenFinished = false;

    /**
     * Constructor; initializes the test reporter and optionally takes a set of allowed error codes.
     *
     * @param aSetOfAllowedErrorCodes any number of allowed error codes
     */
    public TestReporter(ErrorCodes... aSetOfAllowedErrorCodes) {
        this.allowedErrorCodesArray = aSetOfAllowedErrorCodes;
    }

    /**
     * Sets {@link #isReportIsInitialized()} to true and calls {@link #clear()}. Throws an exception if a former report
     * has not been finished.
     *
     * @throws IllegalStateException if a former report has not been finished by calling {@link #report()} ({@link
     *                               #isReportIsInitialized()} is {@code true} but {@link #isReportHasBeenFinished()}
     *                               is not)
     */
    @Override
    public void initializeNewReport() throws IllegalStateException {
        if (this.reportIsInitialized && !this.reportHasBeenFinished) {
            throw new IllegalStateException("The former report has not been finished.");
        }
        this.reportIsInitialized = true;
        this.clear();
    }

    /**
     * Checks whether the error code contained by the given report data object is part of the set of allowed error
     * codes. Increases the count of allowed or of not allowed error codes respectively.
     *
     * @param aReportDataObject the report data object containing the data referring to the reported issue
     * @throws IllegalStateException if the report has not been initialized or already been finished
     */
    @Override
    public void appendReport(ReportDataObject aReportDataObject) throws NullPointerException, IllegalStateException {
        Objects.requireNonNull(aReportDataObject, "The passed report data object is null.");
        if (!this.reportIsInitialized) {
            throw new IllegalStateException("The report has not been initialized.");
        }
        if (this.reportHasBeenFinished) {
            throw new IllegalStateException("The report has already been finished.");
        }
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
     * Checks whether the reporter has been initialized, whether it has not been finished via a call of {@link IReporter
     * #report()} before and whether only allowed error codes were reported; throws an exception otherwise. Sets whether
     * the report has been finished to true. Calls {@link #clear()}.
     *
     * @throws IllegalStateException if the report has not been initialized; if the report has already been finished
     *                               (via a {@link IReporter#report()} method call) before; if the not allowed error
     *                               codes count exceeds zero
     */
    @Override
    public void report() throws IllegalStateException, Exception {
        if (!this.reportIsInitialized) {
            throw new IllegalStateException("The report has not been initialized.");
        }
        if (this.reportHasBeenFinished) {
            throw new IllegalStateException("The report has been finished before.");
        }
        if (this.notAllowedErrorCodesCount > 0) {
            throw new IllegalStateException("Not allowed error codes were reported.");
        }
        this.reportHasBeenFinished = true;
        //
        this.clear();
    }

    /**
     * Throws an exception if the method is called since it implies that a fatal exception has been thrown. Calls {@link
     * #clear()} before throwing the exception.
     *
     * @throws IllegalStateException always (for a well functioning processing step there should be no reason for this
     *                               method to be called)
     */
    @Override
    public void reportAfterFatalException() {
        this.clear();
        throw new IllegalStateException("The reportAfterFatalException() method has been called which implies that" +
                "a fatal exception has been thrown.");
    }

    /**
     * Clears the counts of allowed and not allowed error codes by setting them to zero. Does not reset whether the
     * reporter was initialized and whether the report has been finished (see {@link #reset()} for this).
     */
    @Override
    public void clear() {
        this.allowedErrorCodesCount = 0;
        this.notAllowedErrorCodesCount = 0;
    }

    /**
     * Resets all class fields (except the error codes array) to their initial values.
     */
    public void reset() {
        this.clear();
        this.reportIsInitialized = false;
        this.reportHasBeenFinished = false;
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

    /**
     * Returns whether the current report has been finished ({@link #report()} needs to be called for this).
     *
     * @return boolean value
     */
    public boolean isReportHasBeenFinished() {
        return this.reportHasBeenFinished;
    }

}
