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
import de.unijena.cheminf.curation.processingSteps.IProcessingStep;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test class of the test reporter.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public class TestReporterTest {

    /**
     * Tests whether the .report() method throws an exception if the report has not been initialized before the method
     * call.
     */
    @Test
    public void reportMethodTest_reportNotInitialized_throwsException() {
        IReporter tmpReporter = new TestReporter();
        Assertions.assertThrows(IllegalStateException.class, tmpReporter::report);
    }

    /**
     * Tests whether the .report() method throws no exception if it is called after the report was initialized and no
     * issues were reported.
     *
     * @throws Exception if something went wrong
     */
    @Test
    public void reportMethodTest_reportInitialized_noReports_throwsNoException() throws Exception {
        IReporter tmpReporter = new TestReporter();
        tmpReporter.initializeNewReport();
        Assertions.assertDoesNotThrow(tmpReporter::report);
    }

    /**
     * Tests whether the .report() method throws no exception if only error codes declared as allowed were reported.
     *
     * @throws Exception if something went wrong
     */
    @Test
    public void reportMethodTest_onlyAllowedErrorCodes_throwsNoException() throws Exception {
        ErrorCodes tmpAllowedErrorCode1 = ErrorCodes.ATOM_CONTAINER_NULL_ERROR;
        ErrorCodes tmpAllowedErrorCode2 = ErrorCodes.UNEXPECTED_EXCEPTION_ERROR;
        IReporter tmpReporter = new TestReporter(tmpAllowedErrorCode1, tmpAllowedErrorCode2);
        tmpReporter.initializeNewReport();
        //
        // appending example report data objects with allowed error codes
        Class<? extends IProcessingStep> tmpExampleClass = IProcessingStep.class;
        ReportDataObject tmpReportDataObject1 = new ReportDataObject(tmpAllowedErrorCode1, tmpExampleClass);
        ReportDataObject tmpReportDataObject2 = new ReportDataObject(tmpAllowedErrorCode2, tmpExampleClass);
        tmpReporter.appendReport(tmpReportDataObject1);
        tmpReporter.appendReport(tmpReportDataObject2);
        //
        Assertions.assertDoesNotThrow(tmpReporter::report);
    }

    /**
     * Tests whether the .report() method throws exception if a not allowed error code was reported.
     *
     * @throws Exception if something went wrong
     */
    @Test
    public void reportMethodTest_notAllowedErrorCode_throwsException() throws Exception {
        IReporter tmpReporter = new TestReporter();
        tmpReporter.initializeNewReport();
        //
        // appending an example report data object with a not allowed error code
        ErrorCodes tmpNotAllowedErrorCode = ErrorCodes.ATOM_CONTAINER_NULL_ERROR;
        Class<? extends IProcessingStep> tmpExampleClass = IProcessingStep.class;
        ReportDataObject tmpReportDataObject = new ReportDataObject(tmpNotAllowedErrorCode, tmpExampleClass);
        tmpReporter.appendReport(tmpReportDataObject);
        //
        Assertions.assertThrows(Exception.class, tmpReporter::report);
    }

    /**
     * Tests whether the .report() method throws an exception if the report has not been initialized before the method
     * call.
     *
     * @throws Exception if something went wrong
     */
    @Test
    public void reportMethodTest_endedWithFatalExceptionIsTrue_throwsException() throws Exception {
        IReporter tmpReporter = new TestReporter();
        tmpReporter.initializeNewReport();
        tmpReporter.setEndedWithFatalException(true);
        Assertions.assertThrows(Exception.class, tmpReporter::report);
    }

    /**
     * Tests whether the .appendReport() method increases the allowed error codes count if a report data object with an
     * allowed error code is passed.
     *
     * @throws Exception if something went wrong
     */
    @Test
    public void appendReportMethodTest_allowedErrorCode_increasesRespectiveCount() throws Exception {
        ErrorCodes tmpAllowedErrorCode = ErrorCodes.ATOM_CONTAINER_NULL_ERROR;
        TestReporter tmpReporter = new TestReporter(tmpAllowedErrorCode);
        tmpReporter.initializeNewReport();
        int tmpInitialCount = tmpReporter.getAllowedErrorCodesCount();
        // appending a report data object with an allowed error code
        Class<? extends IProcessingStep> tmpExampleClass = IProcessingStep.class;
        ReportDataObject tmpReportDataObject = new ReportDataObject(tmpAllowedErrorCode, tmpExampleClass);
        tmpReporter.appendReport(tmpReportDataObject);
        Assertions.assertEquals(tmpInitialCount + 1, tmpReporter.getAllowedErrorCodesCount());
    }

    /**
     * Tests whether the .appendReport() method increases the not allowed error codes count if a report data object with
     * a not allowed error code is passed.
     *
     * @throws Exception if something went wrong
     */
    @Test
    public void appendReportMethodTest_notAllowedErrorCode_increasesRespectiveCount() throws Exception {
        TestReporter tmpReporter = new TestReporter();
        tmpReporter.initializeNewReport();
        int tmpInitialCount = tmpReporter.getNotAllowedErrorCodesCount();
        // appending a report data object with an allowed error code
        ErrorCodes tmpNotAllowedErrorCode = ErrorCodes.ATOM_CONTAINER_NULL_ERROR;
        Class<? extends IProcessingStep> tmpExampleClass = IProcessingStep.class;
        ReportDataObject tmpReportDataObject = new ReportDataObject(tmpNotAllowedErrorCode, tmpExampleClass);
        tmpReporter.appendReport(tmpReportDataObject);
        Assertions.assertEquals(tmpInitialCount + 1, tmpReporter.getNotAllowedErrorCodesCount());
    }

    /**
     * Tests whether the .appendReport() method throws an exception if the method is called but the report has not been
     * initialized.
     */
    @Test
    public void appendReportMethodTest_reportNotInitialized_throwsException() {
        ErrorCodes tmpAllowedErrorCode = ErrorCodes.ATOM_CONTAINER_NULL_ERROR;
        Class<? extends IProcessingStep> tmpExampleClass = IProcessingStep.class;
        ReportDataObject tmpReportDataObject = new ReportDataObject(tmpAllowedErrorCode, tmpExampleClass);
        //
        IReporter tmpReporter = new TestReporter(tmpAllowedErrorCode);
        Assertions.assertThrows(IllegalStateException.class, () -> tmpReporter.appendReport(tmpReportDataObject));
    }

    /**
     * Tests whether the .appendReport() method throws an exception if the method is called but the report has not been
     * finished before.
     *
     * @throws Exception if something went wrong
     */
    @Test
    public void appendReportMethodTest_reportHasBeenFinished_throwsException() throws Exception {
        ErrorCodes tmpAllowedErrorCode = ErrorCodes.ATOM_CONTAINER_NULL_ERROR;
        Class<? extends IProcessingStep> tmpExampleClass = IProcessingStep.class;
        ReportDataObject tmpReportDataObject = new ReportDataObject(tmpAllowedErrorCode, tmpExampleClass);
        //
        IReporter tmpReporter = new TestReporter(tmpAllowedErrorCode);
        tmpReporter.initializeNewReport();
        tmpReporter.report();
        Assertions.assertThrows(IllegalStateException.class, () -> tmpReporter.appendReport(tmpReportDataObject));
    }

    /**
     * Tests whether the .initializeNewReport() method throws an exception if a former report has not been finished (by
     * a call of {@link IReporter#report()}.
     *
     * @throws Exception if something went wrong
     */
    @Test
    public void initializeNewReportMethodTest_formerReportHasNotBeenFinished_throwsException() throws Exception {
        IReporter tmpReporter = new TestReporter();
        tmpReporter.initializeNewReport();
        Assertions.assertThrows(IllegalStateException.class, tmpReporter::initializeNewReport);
    }

}
