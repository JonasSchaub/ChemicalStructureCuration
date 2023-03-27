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
 * Min atom count filter for filtering atom containers based on a minimum atom count.
 */
public class MinAtomCountFilter extends Filter {

    /**
     * Integer value of the min atom count threshold.
     */
    protected final int minAtomCount;

    /**
     * Boolean value whether implicit hydrogen atoms should be considered when calculating an atom containers atom
     * count.
     */
    protected final boolean considerImplicitHydrogens;

    /**
     * Constructor of the MinAtomCountFilter class. Implicit hydrogen atoms may or may not be considered; atom
     * containers that equal the given min atom count do not get filtered.
     *
     * @param aMinAtomCount integer value of the min atom count to filter by
     * @param aConsiderImplicitHydrogens boolean value whether implicit hydrogen atoms should be considered when
     *                                  calculating an atom containers atom count
     * @throws IllegalArgumentException if the given min atom count is less than zero
     */
    public MinAtomCountFilter(int aMinAtomCount, boolean aConsiderImplicitHydrogens) throws IllegalArgumentException {
        if (aMinAtomCount < 0) {    //TODO: would not harm the code but makes no sense
            throw new IllegalArgumentException("aMinAtomCount (integer value) was < than 0.");
        }
        this.minAtomCount = aMinAtomCount;
        this.considerImplicitHydrogens = aConsiderImplicitHydrogens;
    }

    /**
     * {@inheritDoc}
     * Atom containers that equal the min atom count do not get filtered.
     */
    @Override
    public boolean isFiltered(IAtomContainer anAtomContainer) throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of IAtomContainer) is null.");
        //
        return !FilterUtils.exceedsOrEqualsAtomCount(anAtomContainer, this.minAtomCount, this.considerImplicitHydrogens);
    }

    /**
     * Returns the min atom count.
     *
     * @return Integer value
     */
    public int getMinAtomCount() {
        return this.minAtomCount;
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
