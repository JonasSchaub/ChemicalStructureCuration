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
 */
public class MaxAtomCountFilter extends BaseFilter {

    /**
     * Integer value of the atom count threshold.
     */
    protected final int atomCountThresholdValue;    //TODO: remove the "...Value" ?

    /**
     * Boolean value whether implicit hydrogen atoms should be considered when calculating an atom containers atom
     * count.
     */
    protected final boolean considerImplicitHydrogens;

    /**
     * Constructor of the MaxAtomCountFilter class. Implicit hydrogen atoms may or may not be considered; atom
     * containers that equal the given max atom count do not get filtered.
     *
     * @param anAtomCountThresholdValue  integer value of the max atom count threshold to filter by
     * @param aConsiderImplicitHydrogens boolean value whether implicit hydrogen atoms should be considered when
     *                                   calculating an atom containers atom count
     * @throws IllegalArgumentException if the given max atom count threshold value is less than zero
     */
    public MaxAtomCountFilter(int anAtomCountThresholdValue, boolean aConsiderImplicitHydrogens) throws IllegalArgumentException {
        if (anAtomCountThresholdValue < 0) {
            throw new IllegalArgumentException("anAtomCountThresholdValue (integer value) is less than 0.");
        }
        this.atomCountThresholdValue = anAtomCountThresholdValue;
        this.considerImplicitHydrogens = aConsiderImplicitHydrogens;
    }

    /**
     * {@inheritDoc}
     * <br>
     * Atom containers that equal the atom count threshold value do not get filtered.
     *
     * @throws NullPointerException if the given IAtomContainer instance is null
     * @throws IllegalArgumentException if the filter has an illegal atom count threshold value  TODO: should not happen
     */
    @Override
    public boolean isFiltered(IAtomContainer anAtomContainer) throws NullPointerException, IllegalArgumentException {
        return this.isFiltered(anAtomContainer, false);
    }

    /**
     * {@inheritDoc}
     * <br>
     * Atom containers that equal the atom count threshold value do not get filtered.
     *
     * @throws NullPointerException if the given IAtomContainer instance is null and issues do not get reported to the
     * reporter
     * @throws IllegalArgumentException if the filter has an illegal atom count threshold value  TODO: should not happen
     */
    @Override
    protected boolean isFiltered(IAtomContainer anAtomContainer, boolean aReportToReporter) throws NullPointerException, IllegalArgumentException {
        try {
            Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of IAtomContainer) is null.");
            //
            return FilterUtils.exceedsOrEqualsAtomCount(anAtomContainer, this.atomCountThresholdValue + 1, this.considerImplicitHydrogens);
        } catch (Exception anException) {
            if (aReportToReporter) {
                ErrorCodes tmpErrorCode;
                //TODO: is it possible to do this using a switch?
                if (NullPointerException.class.equals(anException.getClass())) {
                    tmpErrorCode = ErrorCodes.ATOM_CONTAINER_NULL_ERROR;
                } else {
                    tmpErrorCode = ErrorCodes.UNEXPECTED_EXCEPTION_ERROR;
                }
                this.appendReportToReporter(anAtomContainer, tmpErrorCode);
                return true;
            } else {
                throw anException;
            }
        }
    }

    /**
     * Returns the atom count threshold value.
     *
     * @return Integer value
     */
    public int getAtomCountThresholdValue() {
        return this.atomCountThresholdValue;
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
