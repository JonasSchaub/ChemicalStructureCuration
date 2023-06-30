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
import de.unijena.cheminf.curation.utils.ChemUtils;
import de.unijena.cheminf.curation.enums.MassComputationFlavours;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

import java.util.Objects;

/**
 * Max molecular mass filter for filtering atom containers based on a maximum molecular mass.
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

    /**
     * Constructor; initializes the class fields with the given values. Atom containers that equal the given molecular
     * mass threshold value do not get filtered.
     *
     * @param aMolecularMassThreshold double value of the molecular mass threshold to filter by
     * @param aFlavour MassComputationFlavours constant that switches the computation type of the mass calculation;
     *                 see: {@link MassComputationFlavours},
     *                      {@link AtomContainerManipulator#getMass(IAtomContainer, int)}
     * @throws NullPointerException if the given mass computation flavour is null
     * @throws IllegalArgumentException if the given molecular mass threshold value is less than zero
     */
    public MaxMolecularMassFilter(double aMolecularMassThreshold, MassComputationFlavours aFlavour) throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(aFlavour, "aFlavour (MassComputationFlavours constant) is null.");
        if (aMolecularMassThreshold < 0) {
            throw new IllegalArgumentException("aMolecularMassThreshold (double value) was less than 0.");
        }
        this.molecularMassThreshold = aMolecularMassThreshold;
        this.massComputationFlavour = aFlavour;
    }

    /**
     * Constructor; initializes the class fields with the given value and sets the mass computation type to {@link
     * MassComputationFlavours#MOL_WEIGHT}. Atom containers that equal the given molecular mass threshold value do not
     * get filtered.
     *
     * @param aMolecularMassThreshold double value of the molecular mass threshold value to filter by
     * @throws IllegalArgumentException if the given molecular mass threshold value is less than zero
     */
    public MaxMolecularMassFilter(double aMolecularMassThreshold) throws IllegalArgumentException {
        this(aMolecularMassThreshold, MassComputationFlavours.MOL_WEIGHT);
    }

    @Override
    public boolean isFiltered(IAtomContainer anAtomContainer) throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, ErrorCodes.ATOM_CONTAINER_NULL_ERROR.name());
        //
        return ChemUtils.getMass(anAtomContainer, this.massComputationFlavour) > this.molecularMassThreshold;
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
            // the threshold value being of an illegal value is also considered as fatal
            this.appendToReporter(anAtomContainer, ErrorCodes.UNEXPECTED_EXCEPTION_ERROR);
            throw anException;
        }
        this.appendToReporter(anAtomContainer, tmpErrorCode);
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
