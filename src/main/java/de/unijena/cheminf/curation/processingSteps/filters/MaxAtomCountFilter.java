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

    /*
    TODO: rename maxAtomCount to maxAtomCountThreshold?
     */

    /**
     * Integer value of the max atom count threshold.
     */
    protected final int maxAtomCount;

    /**
     * Boolean value whether implicit hydrogen atoms should be considered when calculating an atom containers atom
     * count.
     */
    protected final boolean considerImplicitHydrogens;

    /**
     * Constructor of the MaxAtomCountFilter class. Implicit hydrogen atoms may or may not be considered; atom
     * containers that equal the given max atom count do not get filtered.
     *
     * @param aMaxAtomCount integer value of the max atom count to filter by
     * @param aConsiderImplicitHydrogens boolean value whether implicit hydrogen atoms should be considered when
     *                                  calculating an atom containers atom count
     * @throws IllegalArgumentException if the given max atom count is less than zero
     */
    public MaxAtomCountFilter(int aMaxAtomCount, boolean aConsiderImplicitHydrogens) throws IllegalArgumentException {
        if (aMaxAtomCount < 0) {    //TODO: would not harm the code but makes no sense
            throw new IllegalArgumentException("aMaxAtomCount (integer value) was < than 0.");
        }
        this.maxAtomCount = aMaxAtomCount;
        this.considerImplicitHydrogens = aConsiderImplicitHydrogens;
    }

    /**
     * {@inheritDoc}
     * <br>
     * Atom containers that equal the max atom count do not get filtered.
     *
     * @throws NullPointerException if the given IAtomContainer instance is null
     * @throws IllegalArgumentException if the filter has an illegal max atom count threshold value  TODO: should not happen
     */
    @Override
    public boolean isFiltered(IAtomContainer anAtomContainer) throws NullPointerException, IllegalArgumentException {
        return this.isFiltered(anAtomContainer, false);
    }

    /**
     * {@inheritDoc}
     * <br>
     * Atom containers that equal the max atom count do not get filtered.
     *
     * @throws NullPointerException if the given IAtomContainer instance is null and issues do not get reported to the
     * reporter
     * @throws IllegalArgumentException if the filter has an illegal max atom count threshold value  TODO: should not happen
     */
    @Override
    protected boolean isFiltered(IAtomContainer anAtomContainer, boolean aReportToReporter) throws NullPointerException, IllegalArgumentException {
        try {
            Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of IAtomContainer) is null.");
            //
            return FilterUtils.exceedsOrEqualsAtomCount(anAtomContainer, this.maxAtomCount + 1, this.considerImplicitHydrogens);
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
     * Returns the max atom count.
     *
     * @return Integer value
     */
    public int getMaxAtomCount() {
        return this.maxAtomCount;
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
