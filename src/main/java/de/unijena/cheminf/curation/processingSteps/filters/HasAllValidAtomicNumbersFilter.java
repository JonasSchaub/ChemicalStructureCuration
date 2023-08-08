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
import de.unijena.cheminf.curation.reporter.IReporter;
import de.unijena.cheminf.curation.reporter.MarkDownReporter;
import de.unijena.cheminf.curation.utils.FilterUtils;
import org.openscience.cdk.interfaces.IAtomContainer;

import java.util.Objects;

/**
 * Has all valid atomic numbers filter to filter atom containers with all valid atomic numbers out of a set of atom
 * containers.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public class HasAllValidAtomicNumbersFilter extends BaseFilter {

    /**
     * Boolean value whether to consider the wildcard atomic number zero as a valid atomic number.
     */
    protected final boolean wildcardAtomicNumberIsValid;

    /**
     * Constructor; sets the reporter of the filter and whether the wildcard atomic number zero is to be considered as
     * a valid atomic number.
     *
     * @param aWildcardAtomicNumberIsValid boolean value whether the wildcard atomic number zero should be considered
     *                                     as a valid atomic number
     * @param aReporter the reporter that is to be used when processing sets of structures
     * @throws NullPointerException if the given IReporter instance is null
     */
    public HasAllValidAtomicNumbersFilter(boolean aWildcardAtomicNumberIsValid, IReporter aReporter) throws NullPointerException {
        super(aReporter, null);
        this.wildcardAtomicNumberIsValid = aWildcardAtomicNumberIsValid;
    }

    /**
     * Constructor; initializes the reporter of the filter with an instance of {@link MarkDownReporter} and sets whether
     * the wildcard atomic number zero is to be considered as a valid atomic number.
     *
     * @param aWildcardAtomicNumberIsValid boolean value whether the wildcard atomic number zero should be considered
     *                                     as a valid atomic number
     * @param aReportFilesDirectoryPath the directory path for the MarkDownReporter to create the report files at
     * @throws NullPointerException if the given String with the directory path is null
     * @throws IllegalArgumentException if the given file path is no directory path
     */
    public HasAllValidAtomicNumbersFilter(boolean aWildcardAtomicNumberIsValid, String aReportFilesDirectoryPath)
            throws NullPointerException, IllegalArgumentException {
        super(aReportFilesDirectoryPath, null);
        this.wildcardAtomicNumberIsValid = aWildcardAtomicNumberIsValid;
    }

    /**
     * {@inheritDoc}
     * <br>
     * The class field wildcardAtomicNumberIsValid decides whether zero is considered as a valid atomic number. An
     * atomic number being null is considered as an invalid atomic number.
     *
     * @throws NullPointerException if the given IAtomContainer instance or an IAtom instance contained by it is null
     */
    @Override
    public boolean isFiltered(IAtomContainer anAtomContainer) throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, ErrorCodes.ATOM_CONTAINER_NULL_ERROR.name());
        //
        try {
            return !FilterUtils.hasAllValidAtomicNumbers(anAtomContainer, this.wildcardAtomicNumberIsValid);
        } catch (NullPointerException aNullPointerException) {
            //check whether it is an ATOMIC_NUMBER_NULL_ERROR; re-throw exception otherwise
            try {
                if (ErrorCodes.valueOf(aNullPointerException.getMessage()) == ErrorCodes.ATOMIC_NUMBER_NULL_ERROR) {
                    //atomic number being null is considered as an invalid atomic number
                    return true;
                }
            } catch (Exception ignored) {
                //ignored
            }
            throw aNullPointerException;
        }
    }

    @Override
    protected void reportIssue(IAtomContainer anAtomContainer, Exception anException) throws Exception {
        String tmpExceptionMessageString = anException.getMessage();
        ErrorCodes tmpErrorCode;
        try {
            // the message of the exception is expected to match the name of an ErrorCodes enum's constant
            tmpErrorCode = ErrorCodes.valueOf(tmpExceptionMessageString);
        } catch (Exception aFatalException) {
            /*
             * the message string of the given exception did not match the name of an ErrorCodes enum's constant; the
             * exception is considered as fatal and re-thrown
             */
            this.appendToReport(ErrorCodes.UNEXPECTED_EXCEPTION_ERROR, anAtomContainer);
            throw anException;
        }
        this.appendToReport(tmpErrorCode, anAtomContainer);
    }

    /**
     * Returns whether the wildcard atomic number zero is considered as valid atomic number.
     *
     * @return Boolean value
     */
    public boolean isWildcardAtomicNumberIsValid() {
        return this.wildcardAtomicNumberIsValid;
    }

}
