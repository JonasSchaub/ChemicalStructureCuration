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
 * Min bonds of specific bond order filter for filtering atom containers based on a minimum count of bonds of a
 * specific bond order.
 */
public class MinBondsOfSpecificBondOrderFilter extends MaxBondsOfSpecificBondOrderFilter {

    /**
     * Constructor; initializes the class fields with the given values. When filtering on the count of bonds with bond
     * order single, bonds to implicit hydrogen atoms may or may not be considered; atom containers that equal the given
     * min specific bond count do not get filtered.
     *
     * @param aBondOrder bond order of bonds to count and filter on
     * @param aSpecificBondCountThreshold integer value of the min specific bond count threshold to filter by
     * @param aConsiderImplicitHydrogens boolean value whether implicit hydrogen atoms should be considered when
     *                                   counting bonds of bond order single
     * @throws IllegalArgumentException if the given min specific bond count is less than zero
     */
    public MinBondsOfSpecificBondOrderFilter(IBond.Order aBondOrder, int aSpecificBondCountThreshold,
                                             boolean aConsiderImplicitHydrogens) throws IllegalArgumentException {
        super(aBondOrder, aSpecificBondCountThreshold, aConsiderImplicitHydrogens);
    }

    @Override
    public boolean isFiltered(IAtomContainer anAtomContainer) throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, ErrorCodes.ATOM_CONTAINER_NULL_ERROR.name());
        //
        return !FilterUtils.exceedsOrEqualsBondsOfSpecificBondOrderCount(
                anAtomContainer,
                this.bondOrderOfInterest,
                this.specificBondCountThreshold,
                this.considerImplicitHydrogens
        );
    }

}
