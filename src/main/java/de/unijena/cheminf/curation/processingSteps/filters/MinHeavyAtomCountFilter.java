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
 * Min heavy atom count filter for filtering atom containers based on a minimum non-hydrogen atom count.
 */
public class MinHeavyAtomCountFilter extends BaseFilter {

    /*
    TODO: handling of issues (reporting)
     */

    /**
     * Integer value of the min heavy atom count threshold.
     */
    protected final int minHeavyAtomCount;

    /**
     * Constructor of the MinHeavyAtomCountFilter class. Atom containers that equal the given min atom count do not get
     * filtered.
     *
     * @param aMinHeavyAtomCount integer value of the min heavy atom count to filter by
     * @throws IllegalArgumentException if the given min heavy atom count is less than zero
     */
    public MinHeavyAtomCountFilter(int aMinHeavyAtomCount) throws IllegalArgumentException {
        if (aMinHeavyAtomCount < 0) {    //TODO: would not harm the code but makes no sense
            throw new IllegalArgumentException("aMinHeavyAtomCount (integer value) was < than 0.");
        }
        this.minHeavyAtomCount = aMinHeavyAtomCount;
    }

    /**
     * {@inheritDoc}
     * Atom containers that equal the min heavy atom count do not get filtered.
     */
    @Override
    protected boolean isFiltered(IAtomContainer anAtomContainer, boolean aReportToReporter) throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of IAtomContainer) is null.");
        //
        return !FilterUtils.exceedsOrEqualsHeavyAtomCount(anAtomContainer, this.minHeavyAtomCount);
    }

    /**
     * Returns the min heavy atom count.
     *
     * @return Integer value
     */
    public int getMinHeavyAtomCount() {
        return this.minHeavyAtomCount;
    }

}
