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
import de.unijena.cheminf.curation.utils.ChemUtils;
import de.unijena.cheminf.curation.enums.MassComputationFlavours;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

import java.util.Objects;

/**
 * Min molecular mass filter for filtering atom containers based on a minimum molecular mass.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public class MinMolecularMassFilter extends MaxMolecularMassFilter {

    //<editor-fold desc="Constructors" defaultstate="collapsed">
    /**
     * Constructor; initializes the class fields with the given values and sets the reporter. Atom containers that equal
     * the given molecular mass threshold value do not get filtered.
     *
     * @param aMolecularMassThreshold double value of the molecular mass threshold to filter by
     * @param aFlavour MassComputationFlavours constant that switches the computation type of the mass calculation;
     *                 see: {@link MassComputationFlavours},
     *                      {@link AtomContainerManipulator#getMass(IAtomContainer, int)}
     * @param aReporter the reporter that is to be used when processing sets of structures
     * @throws NullPointerException if the given mass computation flavour or the IReporter instance is null
     * @throws IllegalArgumentException if the given molecular mass threshold value is below zero
     */
    public MinMolecularMassFilter(double aMolecularMassThreshold, MassComputationFlavours aFlavour, IReporter aReporter)
            throws NullPointerException, IllegalArgumentException {
        super(aMolecularMassThreshold, aFlavour, aReporter);
    }

    /**
     * Constructor; initializes the class fields with the given value, sets the mass computation type to {@link
     * MassComputationFlavours#MOL_WEIGHT} and sets the reporter. Atom containers that equal the given molecular mass
     * threshold value do not get filtered.
     *
     * @param aMolecularMassThreshold double value of the molecular mass threshold to filter by
     * @param aReporter the reporter that is to be used when processing sets of structures
     * @throws NullPointerException if the given IReporter instance is null
     * @throws IllegalArgumentException if the given molecular mass threshold value is below zero
     */
    public MinMolecularMassFilter(double aMolecularMassThreshold, IReporter aReporter)
            throws NullPointerException, IllegalArgumentException {
        super(aMolecularMassThreshold, aReporter);
    }

    /**
     * Constructor; initializes the class fields with the given values and sets the reporter; initializes the reporter
     * with an instance of {@link MarkDownReporter}. Atom containers that equal the given molecular mass threshold value
     * do not get filtered.
     *
     * @param aMolecularMassThreshold double value of the molecular mass threshold to filter by
     * @param aFlavour MassComputationFlavours constant that switches the computation type of the mass calculation;
     *                 see: {@link MassComputationFlavours},
     *                      {@link AtomContainerManipulator#getMass(IAtomContainer, int)}
     * @param aReportFilesDirectoryPath the directory path for the MarkDownReporter to create the report files at
     * @throws NullPointerException if the given mass computation flavour or the String with the directory path is null
     * @throws IllegalArgumentException if the given molecular mass threshold value is below zero; if the given file
     *                                  path is no directory path
     */
    public MinMolecularMassFilter(double aMolecularMassThreshold,
                                  MassComputationFlavours aFlavour,
                                  String aReportFilesDirectoryPath)
            throws NullPointerException, IllegalArgumentException {
        super(aMolecularMassThreshold, aFlavour, aReportFilesDirectoryPath);
    }

    /**
     * Constructor; initializes the class fields with the given value and sets the mass computation type to {@link
     * MassComputationFlavours#MOL_WEIGHT}; initializes the reporter with an instance of {@link MarkDownReporter}. Atom
     * containers that equal the given molecular mass threshold value do not get filtered.
     *
     * @param aMolecularMassThreshold double value of the molecular mass threshold to filter by
     * @param aReportFilesDirectoryPath the directory path for the MarkDownReporter to create the report files at
     * @throws NullPointerException if the given String with the directory path is null
     * @throws IllegalArgumentException if the given molecular mass threshold value is below zero; if the given file
     *                                  path is no directory path
     */
    public MinMolecularMassFilter(double aMolecularMassThreshold, String aReportFilesDirectoryPath)
            throws NullPointerException, IllegalArgumentException {
        super(aMolecularMassThreshold, aReportFilesDirectoryPath);
    }
    //</editor-fold>

    @Override
    public boolean isFiltered(IAtomContainer anAtomContainer) throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, ErrorCodes.ATOM_CONTAINER_NULL_ERROR.name());
        //
        return ChemUtils.getMass(anAtomContainer, this.massComputationFlavour) < this.molecularMassThreshold;
    }

}
