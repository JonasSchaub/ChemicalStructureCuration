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

package de.unijena.cheminf.filter;

import org.openscience.cdk.interfaces.IAtomContainer;

import java.util.Objects;

/**
 * Max atom count filter for filtering atom containers based on a maximum atom count.
 */
public class MaxAtomCountFilter extends Filter {

    /**
     * Integer value of the max atom count threshold.
     */
    protected final int maxAtomCount; //TODO: should the parameters be adjustable?

    /**
     * Boolean value whether implicit hydrogen atoms should be considered when calculating an atom containers atom
     * count.
     */
    protected final boolean considerImplicitHydrogens;

    /**
     * Constructor of the MaxAtomCountFilter class. Atom containers that equal the given max atom count do not get
     * filtered.
     *
     * @param aMaxAtomCount Integer value for the max atom count
     * @param aConsiderImplicitHydrogens Boolean value whether implicit hydrogen atoms should be considered when
     *                                  calculating an atom containers atom count
     * @throws IllegalArgumentException if the given max atom count is less than zero
     */
    public MaxAtomCountFilter(int aMaxAtomCount, boolean aConsiderImplicitHydrogens) throws IllegalArgumentException {
        //TODO: parameter tests?!
        if (aMaxAtomCount < 0) {    //TODO: would not harm the code but makes no sense
            throw new IllegalArgumentException("aMaxAtomCount (integer value) was < than 0.");
        }
        this.maxAtomCount = aMaxAtomCount;
        this.considerImplicitHydrogens = aConsiderImplicitHydrogens;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getsFiltered(IAtomContainer anAtomContainer) throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of IAtomContainer) is null.");
        //TODO: comment
        return FilterUtils.exceedsOrEqualsAtomCount(anAtomContainer, this.maxAtomCount + 1, this.considerImplicitHydrogens);
    }

    /**
     * Returns the max atom count.
     * Atom containers that equal the max atom count do not get filtered.
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