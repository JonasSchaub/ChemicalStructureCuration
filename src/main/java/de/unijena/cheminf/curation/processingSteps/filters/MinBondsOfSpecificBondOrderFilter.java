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
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IPseudoAtom;

import java.util.Objects;

/**
 * Min bonds of specific bond order filter for filtering atom containers based on a minimum count of bonds of a
 * specific bond order.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public class MinBondsOfSpecificBondOrderFilter extends MaxBondsOfSpecificBondOrderFilter {

    /**
     * Constructor; initializes the class fields with the given values and sets the reporter. When filtering on the
     * count of bonds with bond order single, bonds to implicit hydrogen atoms may or may not be considered. If the
     * second boolean parameter is false, instances of {@link IPseudoAtom} and their implicit hydrogen atoms are not
     * taken into account. Atom containers that equal the given min specific bond count do not get filtered.
     *
     * @param aBondOrder bond order of bonds to count and filter on
     * @param aSpecificBondCountThreshold integer value of the specific bond count threshold to filter by
     * @param aConsiderImplicitHydrogens boolean value whether implicit hydrogen atoms should be considered when
     *                                   counting bonds of bond order single
     * @param aConsiderPseudoAtoms boolean value whether to consider bonds to pseudo-atoms and their implicit hydrogens
     * @param aReporter the reporter that is to be used when processing sets of structures
     * @throws NullPointerException if the given IReporter instance is null
     * @throws IllegalArgumentException if the given specific bond count threshold is below zero
     */
    public MinBondsOfSpecificBondOrderFilter(IBond.Order aBondOrder, int aSpecificBondCountThreshold,
                                             boolean aConsiderImplicitHydrogens, boolean aConsiderPseudoAtoms,
                                             IReporter aReporter)
            throws NullPointerException, IllegalArgumentException {
        super(aBondOrder, aSpecificBondCountThreshold, aConsiderImplicitHydrogens, aConsiderPseudoAtoms, aReporter);
    }

    /**
     * Constructor; initializes the class fields with the given values; initializes the reporter with an instance of
     * {@link MarkDownReporter}. When filtering on the count of bonds with bond order single, bonds to implicit hydrogen
     * atoms may or may not be considered. If the second boolean parameter is false, instances of {@link IPseudoAtom}
     * and their implicit hydrogen atoms are not taken into account. Atom containers that equal the given min specific
     * bond count do not get filtered.
     *
     * @param aBondOrder bond order of bonds to count and filter on
     * @param aSpecificBondCountThreshold integer value of the specific bond count threshold to filter by
     * @param aConsiderImplicitHydrogens boolean value whether implicit hydrogen atoms should be considered when
     *                                   counting bonds of bond order single
     * @param aConsiderPseudoAtoms boolean value whether to consider bonds to pseudo-atoms and their implicit hydrogens
     * @param aReportFilesDirectoryPath the directory path for the MarkDownReporter to create the report files at
     * @throws NullPointerException if the given String with the directory path is null
     * @throws IllegalArgumentException if the given specific bond count threshold is below zero; if the given file path
     *                                  is no directory path
     */
    public MinBondsOfSpecificBondOrderFilter(IBond.Order aBondOrder, int aSpecificBondCountThreshold,
                                             boolean aConsiderImplicitHydrogens, boolean aConsiderPseudoAtoms,
                                             String aReportFilesDirectoryPath)
            throws NullPointerException, IllegalArgumentException {
        super(aBondOrder, aSpecificBondCountThreshold, aConsiderImplicitHydrogens, aConsiderPseudoAtoms, aReportFilesDirectoryPath);
    }

    @Override
    public boolean isFiltered(IAtomContainer anAtomContainer) throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, ErrorCodes.ATOM_CONTAINER_NULL_ERROR.name());
        //
        return !FilterUtils.exceedsOrEqualsBondsOfSpecificBondOrderCount(
                anAtomContainer,
                this.bondOrderOfInterest,
                this.specificBondCountThreshold,
                this.considerImplicitHydrogens,
                this.considerPseudoAtoms
        );
    }

}
