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
import org.openscience.cdk.interfaces.IBond;

import java.util.Objects;

/**
 * Max bonds of specific bond order filter for filtering atom containers based on a maximum count of bonds of a
 * specific bond order.
 */
public class MaxBondsOfSpecificBondOrderFilter extends BaseFilter {

    /**
     * IBond.Order of bonds to count and filter on.
     */
    protected final IBond.Order bondOrderOfInterest;

    /**
     * Integer value of the bond count threshold of bonds of specific bond order.
     */
    protected final int specificBondCountThreshold;

    /**
     * Boolean value whether implicit hydrogen atoms should be considered when counting bonds of bond order single.
     */
    protected final boolean considerImplicitHydrogens;

    /**
     * Constructor; initializes the class fields with the given values. When filtering on the count of bonds with bond
     * order single, bonds to implicit hydrogen atoms may or may not be considered; atom containers that equal the given
     * max specific bond count do not get filtered.
     *
     * @param aBondOrder bond order of bonds to count and filter on
     * @param aSpecificBondCountThreshold integer value of the specific bond count threshold to filter by
     * @param aConsiderImplicitHydrogens boolean value whether implicit hydrogen atoms should be considered when
     *                                   counting bonds of bond order single
     * @throws IllegalArgumentException if the given max specific bond count is less than zero
     */
    public MaxBondsOfSpecificBondOrderFilter(IBond.Order aBondOrder, int aSpecificBondCountThreshold,
                                             boolean aConsiderImplicitHydrogens) throws IllegalArgumentException {
        if (aSpecificBondCountThreshold < 0) {
            throw new IllegalArgumentException("aSpecificBondCountThreshold (integer value) is less than 0.");
        }
        this.bondOrderOfInterest = aBondOrder;
        this.specificBondCountThreshold = aSpecificBondCountThreshold;
        this.considerImplicitHydrogens = aConsiderImplicitHydrogens;
    }

    @Override
    public boolean isFiltered(IAtomContainer anAtomContainer) throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, ErrorCodes.ATOM_CONTAINER_NULL_ERROR.name());
        //
        return FilterUtils.exceedsOrEqualsBondsOfSpecificBondOrderCount(
                anAtomContainer, this.bondOrderOfInterest,
                this.specificBondCountThreshold + 1,
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
     * Returns the bond order of the bonds to count and filter on.
     *
     * @return IBond.Order constant
     */
    public IBond.Order getBondOrderOfInterest() {
        return this.bondOrderOfInterest;
    }

    /**
     * Returns the bond count threshold of bonds of specific bond order.
     *
     * @return Integer value
     */
    public int getSpecificBondCountThreshold() {
        return this.specificBondCountThreshold;
    }

    /**
     * Returns whether bonds to implicit hydrogen atoms are taken into account.
     *
     * @return Boolean value
     */
    public boolean isConsiderImplicitHydrogens() {
        return this.considerImplicitHydrogens;
    }

}
