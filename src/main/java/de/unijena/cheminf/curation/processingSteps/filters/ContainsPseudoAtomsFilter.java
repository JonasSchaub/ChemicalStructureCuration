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

package de.unijena.cheminf.curation.processingSteps.filters;

import de.unijena.cheminf.curation.enums.ErrorCodes;
import de.unijena.cheminf.curation.reporter.IReporter;
import de.unijena.cheminf.curation.reporter.MarkDownReporter;
import de.unijena.cheminf.curation.utils.ChemUtils;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IPseudoAtom;

/**
 * Contains pseudo-atoms filter to filter atom containers that contain pseudo-atoms out of a set of atom containers.
 * Pseudo-atoms are expected to be instances of {@link IPseudoAtom}.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public class ContainsPseudoAtomsFilter extends BaseFilter {

    /**
     * Constructor; calls the respective super passing the given reporter and not specifying the optional ID property
     * name.
     *
     * @param aReporter the reporter that is to be used when processing sets of structures
     * @throws NullPointerException if the given IReporter instance is null
     */
    public ContainsPseudoAtomsFilter(IReporter aReporter) throws NullPointerException {
        super(aReporter, null);
    }

    /**
     * Constructor; calls the respective super passing the given directory path and not specifying the optional ID
     * property name. The super initializes the reporter with an instance of {@link MarkDownReporter}.
     *
     * @param aReportFilesDirectoryPath the directory path for the MarkDownReporter to create the report files at
     * @throws NullPointerException if the given String with the directory path is null
     * @throws IllegalArgumentException if the given file path is no directory path
     */
    public ContainsPseudoAtomsFilter(String aReportFilesDirectoryPath)
            throws NullPointerException, IllegalArgumentException {
        super(aReportFilesDirectoryPath, null);
    }

    @Override
    public boolean isFiltered(IAtomContainer anAtomContainer) throws NullPointerException, Exception {
        return ChemUtils.containsPseudoAtoms(anAtomContainer);
    }

    /**
     * Handles the given exception by appending a report to the reporter; if the message string of the given exception
     * does not match the name of {@link ErrorCodes#ATOM_CONTAINER_NULL_ERROR}, any unexpected, fatal exception has been
     * encountered and the exception gets re-thrown.
     *
     * @param anAtomContainer the atom container the issue refers to
     * @param anException the thrown exception
     * @throws Exception if the message string of the given exception does not match the name of {@link
     *                   ErrorCodes#ATOM_CONTAINER_NULL_ERROR}
     */
    @Override
    protected void reportIssue(IAtomContainer anAtomContainer, Exception anException) throws NullPointerException, Exception {
        String tmpExceptionMessageString = anException.getMessage();
        ErrorCodes tmpErrorCode;
        try {
            // try to get the respective constant of the ErrorCodes enum
            tmpErrorCode = ErrorCodes.valueOf(tmpExceptionMessageString);
            if (tmpErrorCode == ErrorCodes.ATOM_CONTAINER_NULL_ERROR) {
                // not fatal
                this.appendToReport(tmpErrorCode, anAtomContainer);
                return;
            }
        } catch (Exception aFatalException) {
            tmpErrorCode = ErrorCodes.UNEXPECTED_EXCEPTION_ERROR;
        }
        // exception is considered as fatal; append to the reporter and re-throw the exception
        this.appendToReport(tmpErrorCode, anAtomContainer);
        throw anException;
    }

}
