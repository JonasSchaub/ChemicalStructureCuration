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

import de.unijena.cheminf.curation.reporter.IReporter;
import de.unijena.cheminf.curation.reporter.MarkDownReporter;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 * Has invalid atomic numbers filter to filter atom containers with atoms with invalid atomic numbers out of a set of
 * atom containers.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public class HasInvalidAtomicNumbersFilter extends HasAllValidAtomicNumbersFilter {

    /**
     * Constructor; sets the reporter of the filter and whether the wildcard atomic number zero is to be considered as
     * a valid atomic number.
     *
     * @param aWildcardAtomicNumberIsValid boolean value whether the wildcard atomic number zero should be considered
     *                                     as a valid atomic number
     * @param aReporter the reporter that is to be used when processing sets of structures
     * @throws NullPointerException if the given IReporter instance is null
     */
    public HasInvalidAtomicNumbersFilter(boolean aWildcardAtomicNumberIsValid, IReporter aReporter) throws NullPointerException {
        super(aWildcardAtomicNumberIsValid, aReporter);
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
    public HasInvalidAtomicNumbersFilter(boolean aWildcardAtomicNumberIsValid, String aReportFilesDirectoryPath)
            throws NullPointerException, IllegalArgumentException {
        super(aWildcardAtomicNumberIsValid, aReportFilesDirectoryPath);
    }

    @Override
    public boolean isFiltered(IAtomContainer anAtomContainer) throws NullPointerException {
        return !super.isFiltered(anAtomContainer);
    }

}
