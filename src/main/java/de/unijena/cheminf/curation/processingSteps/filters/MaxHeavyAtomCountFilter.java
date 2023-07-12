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
 * Max heavy atom count filter for filtering atom containers based on a maximum non-hydrogen atom count.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public class MaxHeavyAtomCountFilter extends BaseFilter {

    /**
     * Integer value of the heavy atom count threshold value.
     */
    protected final int heavyAtomCountThreshold;

    /**
     * Constructor; initializes the class fields with the given values. Atom containers that equal the given max atom
     * count do not get filtered.
     *
     * @param aHeavyAtomCountThreshold integer value of the heavy atom count threshold to filter by
     * @throws IllegalArgumentException if the given heavy atom count threshold value is less than zero
     */
    public MaxHeavyAtomCountFilter(int aHeavyAtomCountThreshold) throws IllegalArgumentException {
        if (aHeavyAtomCountThreshold < 0) {
            throw new IllegalArgumentException("aHeavyAtomCountThreshold (integer value) was less than 0.");
        }
        this.heavyAtomCountThreshold = aHeavyAtomCountThreshold;
    }

    @Override
    public boolean isFiltered(IAtomContainer anAtomContainer) throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, ErrorCodes.ATOM_CONTAINER_NULL_ERROR.name());
        //
        return FilterUtils.exceedsOrEqualsHeavyAtomCount(anAtomContainer, this.heavyAtomCountThreshold + 1);
    }

    @Override
    protected void reportIssue(IAtomContainer anAtomContainer, Exception anException) throws Exception {
        String tmpExceptionMessageString = anException.getMessage();
        boolean tmpIsExceptionFatal = false;
        ErrorCodes tmpErrorCode;
        try {
            // the message of the exception is expected to match the name of an ErrorCodes enum's constant
            tmpErrorCode = ErrorCodes.valueOf(tmpExceptionMessageString);
            if (tmpErrorCode == ErrorCodes.ILLEGAL_THRESHOLD_VALUE_ERROR) {
                // considered as fatal (should not happen)
                tmpIsExceptionFatal = true;
            }
        } catch (Exception aFatalException) {
            /*
             * the message string of the given exception did not match the name of an ErrorCodes enum's constant; the
             * exception is considered as fatal and re-thrown
             */
            // the threshold value being of an illegal value is also considered as fatal
            tmpErrorCode = ErrorCodes.UNEXPECTED_EXCEPTION_ERROR;
            tmpIsExceptionFatal = true;
        }
        this.appendToReporter(anAtomContainer, tmpErrorCode);
        // re-throw the exception if it is considered as fatal
        if (tmpIsExceptionFatal) {
            throw anException;
        }
    }

    /**
     * Returns the heavy atom count threshold value.
     *
     * @return Integer value
     */
    public int getHeavyAtomCountThreshold() {
        return this.heavyAtomCountThreshold;
    }

}
