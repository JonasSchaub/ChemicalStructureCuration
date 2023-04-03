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

import de.unijena.cheminf.filter.FilterBase;
import de.unijena.cheminf.filter.FilterUtils;
import org.openscience.cdk.interfaces.IAtomContainer;

import java.util.Objects;

/**
 * Max heavy atom count filter for filtering atom containers based on a maximum non-hydrogen atom count.
 */
public class MaxHeavyAtomCountFilter extends FilterBase {

    /**
     * Integer value of the max heavy atom count threshold.
     */
    protected final int maxHeavyAtomCount;

    /**
     * Constructor of the MaxHeavyAtomCountFilter class. Atom containers that equal the given max atom count do not get
     * filtered.
     *
     * @param aMaxHeavyAtomCount integer value of the max heavy atom count to filter by
     * @throws IllegalArgumentException if the given max heavy atom count is less than zero
     */
    public MaxHeavyAtomCountFilter(int aMaxHeavyAtomCount) throws IllegalArgumentException {
        if (aMaxHeavyAtomCount < 0) {    //TODO: would not harm the code but makes no sense
            throw new IllegalArgumentException("aMaxHeavyAtomCount (integer value) was < than 0.");
        }
        this.maxHeavyAtomCount = aMaxHeavyAtomCount;
    }

    /**
     * {@inheritDoc}
     * Atom containers that equal the max heavy atom count do not get filtered.
     */
    @Override
    public boolean isFiltered(IAtomContainer anAtomContainer) throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of IAtomContainer) is null.");
        //
        return FilterUtils.exceedsOrEqualsHeavyAtomCount(anAtomContainer, this.maxHeavyAtomCount + 1);
    }

    /**
     * Returns the max heavy atom count.
     *
     * @return Integer value
     */
    public int getMaxHeavyAtomCount() {
        return this.maxHeavyAtomCount;
    }

}
