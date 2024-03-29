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
 * Max molecular mass filter for filtering atom containers based on a maximum molecular mass.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public class MaxMolecularMassFilter extends BaseFilter {

    /**
     * Double value of the molecular mass threshold.
     */
    protected final double molecularMassThreshold;

    /**
     * MassComputationFlavours constant that switches the computation type of the mass calculation.
     */
    protected final MassComputationFlavours massComputationFlavour;

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
    public MaxMolecularMassFilter(double aMolecularMassThreshold, MassComputationFlavours aFlavour, IReporter aReporter)
            throws NullPointerException, IllegalArgumentException {
        super(aReporter, null);
        Objects.requireNonNull(aFlavour, "aFlavour (MassComputationFlavours constant) is null.");
        if (aMolecularMassThreshold < 0) {
            throw new IllegalArgumentException("aMolecularMassThreshold (double value) is below zero.");
        }
        this.molecularMassThreshold = aMolecularMassThreshold;
        this.massComputationFlavour = aFlavour;
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
    public MaxMolecularMassFilter(double aMolecularMassThreshold, IReporter aReporter)
            throws NullPointerException, IllegalArgumentException {
        this(aMolecularMassThreshold, MassComputationFlavours.MOL_WEIGHT, aReporter);
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
    public MaxMolecularMassFilter(double aMolecularMassThreshold,
                                  MassComputationFlavours aFlavour,
                                  String aReportFilesDirectoryPath)
            throws NullPointerException, IllegalArgumentException {
        super(aReportFilesDirectoryPath, null);
        Objects.requireNonNull(aFlavour, "aFlavour (MassComputationFlavours constant) is null.");
        if (aMolecularMassThreshold < 0) {
            throw new IllegalArgumentException("aMolecularMassThreshold (double value) is below zero.");
        }
        this.molecularMassThreshold = aMolecularMassThreshold;
        this.massComputationFlavour = aFlavour;
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
    public MaxMolecularMassFilter(double aMolecularMassThreshold, String aReportFilesDirectoryPath)
            throws NullPointerException, IllegalArgumentException {
        this(aMolecularMassThreshold, MassComputationFlavours.MOL_WEIGHT, aReportFilesDirectoryPath);
    }
    //</editor-fold>

    @Override
    public boolean isFiltered(IAtomContainer anAtomContainer) throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, ErrorCodes.ATOM_CONTAINER_NULL_ERROR.name());
        //
        return ChemUtils.getMass(anAtomContainer, this.massComputationFlavour) > this.molecularMassThreshold;
    }

    @Override
    protected void reportIssue(IAtomContainer anAtomContainer, Exception anException) throws Exception {
        String tmpExceptionMessageString = anException.getMessage();
        boolean tmpIsExceptionFatal = false;
        ErrorCodes tmpErrorCode;
        try {
            // the message of the exception is expected to match the name of an ErrorCodes enum's constant
            tmpErrorCode = ErrorCodes.valueOf(tmpExceptionMessageString);
            if (tmpErrorCode == ErrorCodes.FLAVOUR_NULL_ERROR) {
                // considered as fatal (should not happen)
                tmpIsExceptionFatal = true;
            }
        } catch (Exception aFatalException) {
            // the message string did not match the name of an ErrorCodes enum's constant; the exception is
            // unexpected and considered as fatal
            tmpErrorCode = ErrorCodes.UNEXPECTED_EXCEPTION_ERROR;
            tmpIsExceptionFatal = true;
        }
        this.appendToReport(tmpErrorCode, anAtomContainer);
        // re-throw the exception if it is considered as fatal
        if (tmpIsExceptionFatal) {
            throw anException;
        }
    }

    /**
     * Returns the molecular mass threshold value.
     *
     * @return double value
     */
    public double getMolecularMassThreshold() {
        return this.molecularMassThreshold;
    }

    /**
     * Returns the mass computation flavour that is used to compute the mass of a molecule.
     *
     * @return MassComputationFlavours constant
     */
    public MassComputationFlavours getMassComputationFlavour() {
        return this.massComputationFlavour;
    }

}
