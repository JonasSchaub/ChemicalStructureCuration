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
import org.openscience.cdk.interfaces.IPseudoAtom;

import java.util.Objects;

/**
 * Min bond count filter for filtering atom containers based on a minimum bond count.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public class MinBondCountFilter extends MaxBondCountFilter {

    /**
     * Constructor; initializes the class fields with the given values and sets the reporter. Bonds to implicit hydrogen
     * atoms and bonds with participation of instances of {@link IPseudoAtom} may or may not be considered. If bonds of
     * pseudo-atoms are not considered, their bonds to implicit hydrogen atoms are not considered either. Atom
     * containers that equal the given bond count threshold do not get filtered.
     *
     * @param aBondCountThreshold integer value of the min bond count threshold to filter by
     * @param aConsiderImplicitHydrogens boolean value whether implicit hydrogen atoms should be considered when
     *                                   calculating an atom containers bond count
     * @param aConsiderPseudoAtoms boolean value whether to consider bonds to pseudo-atoms
     * @param aReporter the reporter that is to be used when processing sets of structures
     * @throws NullPointerException if the given IReporter instance is null
     * @throws IllegalArgumentException if the given bond count threshold value is below zero
     */
    public MinBondCountFilter(int aBondCountThreshold, boolean aConsiderImplicitHydrogens,
            boolean aConsiderPseudoAtoms, IReporter aReporter)
            throws NullPointerException, IllegalArgumentException {
        super(aBondCountThreshold, aConsiderImplicitHydrogens, aConsiderPseudoAtoms, aReporter);
    }

    /**
     * Constructor; initializes the class fields with the given values; initializes the reporter with an instance of
     * {@link MarkDownReporter} Bonds to implicit hydrogen atoms and bonds with participation of instances of {@link
     * IPseudoAtom} may or may not be considered. If bonds of pseudo-atoms are not considered, their bonds to implicit
     * hydrogen atoms are not considered either. Atom containers that equal the given bond count threshold do not get
     * filtered.
     *
     * @param aBondCountThreshold integer value of the min bond count threshold to filter by
     * @param aConsiderImplicitHydrogens boolean value whether implicit hydrogen atoms should be considered when
     *                                   calculating an atom containers bond count
     * @param aConsiderPseudoAtoms boolean value whether to consider bonds to pseudo-atoms
     * @param aReportFilesDirectoryPath the directory path for the MarkDownReporter to create the report files at
     * @throws NullPointerException if the given String with the directory path is null
     * @throws IllegalArgumentException if the given bond count threshold value is below zero; if the given file path
     *                                  is no directory path
     */
    public MinBondCountFilter(int aBondCountThreshold, boolean aConsiderImplicitHydrogens,
            boolean aConsiderPseudoAtoms, String aReportFilesDirectoryPath)
            throws NullPointerException, IllegalArgumentException {
        super(aBondCountThreshold, aConsiderImplicitHydrogens, aConsiderPseudoAtoms, aReportFilesDirectoryPath);
    }

    @Override
    public boolean isFiltered(IAtomContainer anAtomContainer) throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, ErrorCodes.ATOM_CONTAINER_NULL_ERROR.name());
        //
        return !FilterUtils.exceedsOrEqualsBondCount(
                anAtomContainer,
                this.bondCountThreshold,
                this.considerImplicitHydrogens,
                this.considerPseudoAtoms
        );
    }

}
