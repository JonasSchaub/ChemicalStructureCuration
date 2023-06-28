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

import de.unijena.cheminf.curation.utils.FilterUtils;
import org.openscience.cdk.interfaces.IAtomContainer;

import java.util.Objects;

/**
 * Min bond count filter for filtering atom containers based on a minimum bond count.
 */
public class MinBondCountFilter extends BaseFilter {

    /*
    TODO: handling of issues (reporting)
     */

    /**
     * Integer value of the min bond count threshold.
     */
    protected final int minBondCount;

    /**
     * Boolean value whether implicit hydrogen atoms should be considered when calculating an atom containers bond
     * count.
     */
    protected final boolean considerImplicitHydrogens;

    /**
     * Constructor of the MinBondCountFilter class. Bonds to implicit hydrogen atoms may or may not be considered; atom
     * containers that equal the given min bond count do not get filtered.
     *
     * @param aMinBondCount integer value of the min bond count to filter by
     * @param aConsiderImplicitHydrogens boolean value whether implicit hydrogen atoms should be considered when
     *                                  calculating an atom containers bond count
     * @throws IllegalArgumentException if the given min bond count is less than zero
     */
    public MinBondCountFilter(int aMinBondCount, boolean aConsiderImplicitHydrogens) throws IllegalArgumentException {
        if (aMinBondCount < 0) {    //TODO: would not harm the code but makes no sense
            throw new IllegalArgumentException("aMinBondCount (integer value) was < than 0.");
        }
        this.minBondCount = aMinBondCount;
        this.considerImplicitHydrogens = aConsiderImplicitHydrogens;
    }

    /**
     * {@inheritDoc}
     * Atom containers that equal the min bond count do not get filtered.
     */
    @Override
    protected boolean isFiltered(IAtomContainer anAtomContainer, boolean aReportToReporter) throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of IAtomContainer) is null.");
        //
        return !FilterUtils.exceedsOrEqualsBondCount(anAtomContainer, this.minBondCount, this.considerImplicitHydrogens);
    }

    /**
     * Returns the min bond count.
     *
     * @return Integer value
     */
    public int getMinBondCount() {
        return this.minBondCount;
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
