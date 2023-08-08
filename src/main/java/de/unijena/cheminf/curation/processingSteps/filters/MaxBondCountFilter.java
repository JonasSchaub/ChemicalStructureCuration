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
 * Max bond count filter for filtering atom containers based on a maximum bond count.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public class MaxBondCountFilter extends BaseFilter {

    /**
     * Integer value of the bond count threshold.
     */
    protected final int bondCountThreshold;

    /**
     * Boolean value whether implicit hydrogen atoms should be considered when calculating an atom containers bond
     * count.
     */
    protected final boolean considerImplicitHydrogens;

    /**
     * Boolean value whether to consider bonds to instances of {@link IPseudoAtom}.
     */
    protected final boolean considerPseudoAtoms;

    /**
     * Constructor; initializes the class fields with the given values and sets the reporter. Bonds to implicit hydrogen
     * atoms and bonds with participation of instances of {@link IPseudoAtom} may or may not be considered. If bonds of
     * pseudo-atoms are not considered, their bonds to implicit hydrogen atoms are not considered either. Atom
     * containers that equal the given bond count threshold do not get filtered.
     *
     * @param aBondCountThreshold integer value of the max bond count threshold to filter by
     * @param aConsiderImplicitHydrogens boolean value whether implicit hydrogen atoms should be considered when
     *                                   calculating an atom containers bond count
     * @param aConsiderPseudoAtoms boolean value whether to consider bonds to pseudo-atoms and their implicit hydrogens
     * @param aReporter the reporter that is to be used when processing sets of structures
     * @throws NullPointerException if the given IReporter instance is null
     * @throws IllegalArgumentException if the given bond count threshold value is below zero
     */
    public MaxBondCountFilter(int aBondCountThreshold, boolean aConsiderImplicitHydrogens, boolean aConsiderPseudoAtoms,
                              IReporter aReporter)
            throws NullPointerException, IllegalArgumentException {
        super(aReporter, null);
        if (aBondCountThreshold < 0) {
            throw new IllegalArgumentException("aBondCountThreshold (integer value) is below zero.");
        }
        this.bondCountThreshold = aBondCountThreshold;
        this.considerImplicitHydrogens = aConsiderImplicitHydrogens;
        this.considerPseudoAtoms = aConsiderPseudoAtoms;
    }

    /**
     * Constructor; initializes the class fields with the given values; initializes the reporter with an instance of
     * {@link MarkDownReporter} Bonds to implicit hydrogen atoms and bonds with participation of instances of {@link
     * IPseudoAtom} may or may not be considered. If bonds of pseudo-atoms are not considered, their bonds to implicit
     * hydrogen atoms are not considered either. Atom containers that equal the given bond count threshold do not get
     * filtered.
     *
     * @param aBondCountThreshold integer value of the max bond count threshold to filter by
     * @param aConsiderImplicitHydrogens boolean value whether implicit hydrogen atoms should be considered when
     *                                   calculating an atom containers bond count
     * @param aConsiderPseudoAtoms boolean value whether to consider bonds to pseudo-atoms
     * @param aReportFilesDirectoryPath the directory path for the MarkDownReporter to create the report files at
     * @throws NullPointerException if the given String with the directory path is null
     * @throws IllegalArgumentException if the given bond count threshold value is below zero; if the given file path
     *                                  is no directory path
     */
    public MaxBondCountFilter(int aBondCountThreshold, boolean aConsiderImplicitHydrogens,
                              boolean aConsiderPseudoAtoms, String aReportFilesDirectoryPath)
            throws NullPointerException, IllegalArgumentException {
        super(aReportFilesDirectoryPath, null);
        if (aBondCountThreshold < 0) {
            throw new IllegalArgumentException("aBondCountThreshold (integer value) is below zero.");
        }
        this.bondCountThreshold = aBondCountThreshold;
        this.considerImplicitHydrogens = aConsiderImplicitHydrogens;
        this.considerPseudoAtoms = aConsiderPseudoAtoms;
    }

    /**
     * @throws NullPointerException {@inheritDoc}; if implicit hydrogen atoms are to be considered but the implicit
     *                              hydrogen count of an atom is null
     */
    @Override
    public boolean isFiltered(IAtomContainer anAtomContainer) throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, ErrorCodes.ATOM_CONTAINER_NULL_ERROR.name());
        //
        return FilterUtils.exceedsOrEqualsBondCount(
                anAtomContainer,
                this.bondCountThreshold + 1,
                this.considerImplicitHydrogens,
                this.considerPseudoAtoms
        );
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
     * Returns the bond count threshold value.
     *
     * @return Integer value
     */
    public int getBondCountThreshold() {
        return this.bondCountThreshold;
    }

    /**
     * Returns whether bonds to implicit hydrogen atoms are taken into account.
     *
     * @return Boolean value
     */
    public boolean isConsiderImplicitHydrogens() {
        return this.considerImplicitHydrogens;
    }

    /**
     * Returns whether bonds with participation of {@link IPseudoAtom} instances are taken into account.
     *
     * @return Boolean value
     */
    public boolean isConsiderPseudoAtoms() {
        return this.considerPseudoAtoms;
    }

}
