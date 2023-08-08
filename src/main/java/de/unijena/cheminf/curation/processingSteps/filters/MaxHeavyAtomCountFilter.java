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
import de.unijena.cheminf.curation.utils.FilterUtils;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IPseudoAtom;

import java.util.Objects;

/**
 * Max heavy atom count filter for filtering atom containers based on a maximum non-hydrogen atom count.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public class MaxHeavyAtomCountFilter extends BaseFilter {

    /**
     * Integer value of the heavy atom count threshold value.
     */
    protected final int heavyAtomCountThreshold;

    /**
     * Boolean value whether instances of {@link IPseudoAtom} should be considered when calculating the atom count.
     */
    protected final boolean considerPseudoAtoms;

    /**
     * Constructor; initializes the class fields with the given values and sets the reporter. Atom containers that equal
     * the given max atom count do not get filtered.
     *
     * @param aHeavyAtomCountThreshold integer value of the heavy atom count threshold to filter by
     * @param aConsiderPseudoAtoms boolean value whether to consider pseudo-atoms in the heavy atoms count
     * @param aReporter the reporter that is to be used when processing sets of structures
     * @throws NullPointerException if the given IReporter instance is null
     * @throws IllegalArgumentException if the given heavy atom count threshold value is below zero
     */
    public MaxHeavyAtomCountFilter(int aHeavyAtomCountThreshold, boolean aConsiderPseudoAtoms, IReporter aReporter)
            throws NullPointerException, IllegalArgumentException {
        super(aReporter, null);
        if (aHeavyAtomCountThreshold < 0) {
            throw new IllegalArgumentException("aHeavyAtomCountThreshold (integer value) is below zero.");
        }
        this.heavyAtomCountThreshold = aHeavyAtomCountThreshold;
        this.considerPseudoAtoms = aConsiderPseudoAtoms;
    }

    /**
     * Constructor; initializes the class fields with the given values; initializes the reporter with an instance of
     * {@link MarkDownReporter}. Atom containers that equal the given max atom count do not get filtered.
     *
     * @param aHeavyAtomCountThreshold integer value of the heavy atom count threshold to filter by
     * @param aConsiderPseudoAtoms boolean value whether to consider pseudo-atoms in the heavy atoms count
     * @param aReportFilesDirectoryPath the directory path for the MarkDownReporter to create the report files at
     * @throws NullPointerException if the given String with the directory path is null
     * @throws IllegalArgumentException if the given heavy atom count threshold value is below zero; if the given file
     *                                  path is no directory path
     */
    public MaxHeavyAtomCountFilter(int aHeavyAtomCountThreshold, boolean aConsiderPseudoAtoms,
                                   String aReportFilesDirectoryPath)
            throws NullPointerException, IllegalArgumentException {
        super(aReportFilesDirectoryPath, null);
        if (aHeavyAtomCountThreshold < 0) {
            throw new IllegalArgumentException("aHeavyAtomCountThreshold (integer value) is below zero.");
        }
        this.heavyAtomCountThreshold = aHeavyAtomCountThreshold;
        this.considerPseudoAtoms = aConsiderPseudoAtoms;
    }

    @Override
    public boolean isFiltered(IAtomContainer anAtomContainer) throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, ErrorCodes.ATOM_CONTAINER_NULL_ERROR.name());
        //
        return FilterUtils.exceedsOrEqualsHeavyAtomCount(anAtomContainer, this.heavyAtomCountThreshold + 1,
                this.considerPseudoAtoms);
    }

    @Override
    protected void reportIssue(IAtomContainer anAtomContainer, Exception anException) throws Exception {
        String tmpExceptionMessageString = anException.getMessage();
        boolean tmpIsExceptionFatal = false;
        ErrorCodes tmpErrorCode;
        try {
            // the message of the exception is expected to match the name of an ErrorCodes enum's constant
            tmpErrorCode = ErrorCodes.valueOf(tmpExceptionMessageString);
            if (tmpErrorCode == ErrorCodes.ILLEGAL_THRESHOLD_VALUE_ERROR) {
                // considered as fatal (should not happen)
                tmpIsExceptionFatal = true;
            }
        } catch (Exception aFatalException) {
            /*
             * the message string of the given exception did not match the name of an ErrorCodes enum's constant; the
             * exception is considered as fatal and re-thrown
             */
            // the threshold value being of an illegal value is also considered as fatal
            tmpErrorCode = ErrorCodes.UNEXPECTED_EXCEPTION_ERROR;
            tmpIsExceptionFatal = true;
        }
        this.appendToReport(tmpErrorCode, anAtomContainer);
        // re-throw the exception if it is considered as fatal
        if (tmpIsExceptionFatal) {
            throw anException;
        }
    }

    /**
     * Returns the heavy atom count threshold value.
     *
     * @return Integer value
     */
    public int getHeavyAtomCountThreshold() {
        return this.heavyAtomCountThreshold;
    }

    /**
     * Returns whether {@link IPseudoAtom} instances are taken into account.
     *
     * @return Boolean value
     */
    public boolean isConsiderPseudoAtoms() {
        return this.considerPseudoAtoms;
    }

}
