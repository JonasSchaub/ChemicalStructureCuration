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

package de.unijena.cheminf.filter.filters;

import de.unijena.cheminf.filter.BaseFilter;
import de.unijena.cheminf.filter.FilterUtils;
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
     * Integer value of the max bond count threshold.
     */
    protected final int maxSpecificBondCount;

    /**
     * Boolean value whether implicit hydrogen atoms should be considered when counting bonds of bond order single of
     * given atom containers.
     */
    protected final boolean considerImplicitHydrogens;

    /**
     * Constructor of the MaxBondsOfSpecificBondOrderFilter class. When filtering on the count of bonds with bond order
     * single, bonds to implicit hydrogen atoms may or may not be considered; atom containers that equal the given max
     * specific bond count do not get filtered.
     *
     * @param aBondOrder bond order of bonds to count and filter on
     * @param aMaxSpecificBondCount integer value of the max specific bond count to filter by
     * @param aConsiderImplicitHydrogens boolean value whether implicit hydrogen atoms should be considered when
     *                                  counting an atom containers bonds with bond order single
     * @throws IllegalArgumentException if the given max specific bond count is less than zero
     */
    public MaxBondsOfSpecificBondOrderFilter(IBond.Order aBondOrder, int aMaxSpecificBondCount, boolean aConsiderImplicitHydrogens) throws IllegalArgumentException {
        if (aMaxSpecificBondCount < 0) {    //TODO: would not harm the code but makes no sense
            throw new IllegalArgumentException("aMaxSpecificBondCount (integer value) was < than 0.");
        }
        this.bondOrderOfInterest = aBondOrder;
        this.maxSpecificBondCount = aMaxSpecificBondCount;
        this.considerImplicitHydrogens = aConsiderImplicitHydrogens;
    }

    /**
     * {@inheritDoc}
     * Atom containers that equal the max specific bond count do not get filtered.
     */
    @Override
    public boolean isFiltered(IAtomContainer anAtomContainer) throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of IAtomContainer) is null.");
        //
        return FilterUtils.exceedsOrEqualsBondsOfSpecificBondOrderCount(anAtomContainer, this.bondOrderOfInterest, this.maxSpecificBondCount + 1, this.considerImplicitHydrogens);
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
     * Returns the max bond count.
     *
     * @return Integer value
     */
    public int getMaxSpecificBondCount() {
        return this.maxSpecificBondCount;
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
