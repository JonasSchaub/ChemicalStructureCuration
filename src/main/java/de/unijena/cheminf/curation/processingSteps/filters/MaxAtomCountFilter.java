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
import de.unijena.cheminf.curation.utils.FilterUtils;
import org.openscience.cdk.interfaces.IAtomContainer;

import java.util.Objects;

/**
 * Max atom count filter for filtering atom containers based on a maximum atom count.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public class MaxAtomCountFilter extends BaseFilter {

    /**
     * Integer value of the atom count threshold.
     */
    protected final int atomCountThreshold;

    /**
     * Boolean value whether implicit hydrogen atoms should be considered when calculating the atom count of an atom
     * container.
     */
    protected final boolean considerImplicitHydrogens;

    /**
     * Constructor; initializes the class fields with the given values. Implicit hydrogen atoms may or may not be
     * considered; atom containers that equal the given atom count threshold value do not get filtered.
     *
     * @param anAtomCountThreshold integer value of the max atom count threshold to filter by
     * @param aConsiderImplicitHydrogens boolean value whether implicit hydrogen atoms should be considered when
     *                                   calculating the atom count of an atom container
     * @throws IllegalArgumentException if the given atom count threshold value is less than zero
     */
    public MaxAtomCountFilter(int anAtomCountThreshold, boolean aConsiderImplicitHydrogens) throws IllegalArgumentException {
        if (anAtomCountThreshold < 0) {
            throw new IllegalArgumentException("anAtomCountThreshold (integer value) is less than 0.");
        }
        this.atomCountThreshold = anAtomCountThreshold;
        this.considerImplicitHydrogens = aConsiderImplicitHydrogens;
    }

    @Override
    public boolean isFiltered(IAtomContainer anAtomContainer) throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(anAtomContainer, ErrorCodes.ATOM_CONTAINER_NULL_ERROR.name());
        //
        return FilterUtils.exceedsOrEqualsAtomCount(
                anAtomContainer,
                this.atomCountThreshold + 1,
                this.considerImplicitHydrogens
        );
    }

    @Override
    protected void reportIssue(IAtomContainer anAtomContainer, Exception anException) throws Exception {
        String tmpExceptionMessageString = anException.getMessage();
        ErrorCodes tmpErrorCode;
        try {
            // the message of the exception is expected to match the name of an ErrorCodes enum's constant
            tmpErrorCode = ErrorCodes.valueOf(tmpExceptionMessageString);
        } catch (Exception aFatalException) {
            /*
             * the message string of the given exception did not match the name of an ErrorCodes enum's constant; the
             * exception is considered as fatal and re-thrown
             */
            // the threshold value being of an illegal value is also considered as fatal
            this.appendToReporter(anAtomContainer, ErrorCodes.UNEXPECTED_EXCEPTION_ERROR);
            throw anException;
        }
        this.appendToReporter(anAtomContainer, tmpErrorCode);
    }

    /**
     * Returns the atom count threshold value.
     *
     * @return Integer value
     */
    public int getAtomCountThreshold() {
        return this.atomCountThreshold;
    }

    /**
     * Returns whether implicit hydrogen atoms do get considered.
     *
     * @return Boolean value
     */
    public boolean isConsiderImplicitHydrogens() {
        return this.considerImplicitHydrogens;
    }

}
