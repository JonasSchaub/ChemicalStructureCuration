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
 * Min atom count filter for filtering atom containers based on a minimum atom count.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public class MinAtomCountFilter extends MaxAtomCountFilter {

    /**
     * Constructor; initializes the class fields with the given values and sets the reporter. Implicit hydrogen atoms
     * may or may not be considered; atom containers that equal the given atom count threshold value do not get
     * filtered.
     *
     * @param anAtomCountThreshold integer value of the min atom count threshold to filter by
     * @param aConsiderImplicitHydrogens boolean value whether implicit hydrogen atoms should be considered when
     *                                   calculating the atom count of an atom container
     * @param aReporter the reporter that is to be used when processing sets of structures
     * @throws NullPointerException if the given IReporter instance is null
     * @throws IllegalArgumentException if the given atom count threshold value is below zero
     */
    public MinAtomCountFilter(int anAtomCountThreshold, boolean aConsiderImplicitHydrogens, IReporter aReporter)
            throws NullPointerException, IllegalArgumentException {
        super(anAtomCountThreshold, aConsiderImplicitHydrogens, aReporter);
    }

    /**
     * Constructor; initializes the class fields with the given values; initializes the reporter with an instance of
     * {@link MarkDownReporter}. Implicit hydrogen atoms may or may not be considered; atom containers that equal the
     * given atom count threshold value do not get filtered.
     *
     * @param anAtomCountThreshold integer value of the min atom count threshold to filter by
     * @param aConsiderImplicitHydrogens boolean value whether implicit hydrogen atoms should be considered when
     *                                   calculating the atom count of an atom container
     * @param aReportFilesDirectoryPath the directory path for the MarkDownReporter to create the report files at
     * @throws NullPointerException if the given String with the directory path is null
     * @throws IllegalArgumentException if the given atom count threshold value is below zero; if the given file path
     *                                  is no directory path
     */
    public MinAtomCountFilter(int anAtomCountThreshold, boolean aConsiderImplicitHydrogens, String aReportFilesDirectoryPath)
            throws NullPointerException, IllegalArgumentException {
        super(anAtomCountThreshold, aConsiderImplicitHydrogens, aReportFilesDirectoryPath);
    }

    @Override
    public boolean isFiltered(IAtomContainer anAtomContainer) throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(anAtomContainer, ErrorCodes.ATOM_CONTAINER_NULL_ERROR.name());
        //
        return !FilterUtils.exceedsOrEqualsAtomCount(
                anAtomContainer,
                this.atomCountThreshold,
                this.considerImplicitHydrogens
        );
    }

}
