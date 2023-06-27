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

package de.unijena.cheminf.curation.filter.filters;

import de.unijena.cheminf.curation.ChemUtils;
import de.unijena.cheminf.curation.MassComputationFlavours;
import de.unijena.cheminf.curation.filter.BaseFilter;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

import java.util.Objects;

/**
 * Min molecular mass filter for filtering atom containers based on a minimum molecular mass.
 */
public class MinMolecularMassFilter extends BaseFilter {

    /*
    TODO: handling of issues (reporting)
     */

    /**
     * Double value of the min molecular mass threshold.
     */
    protected final double minMolecularMass;

    /**
     * MassComputationFlavours constant that switches the computation type of the mass calculation.
     */
    protected final MassComputationFlavours massComputationFlavour;

    /**
     * Constructor of the MinMolecularMassFilter class. Atom containers that equal the given min molecular mass do not
     * get filtered.
     *
     * @param aMinMolecularMass double value of the min molecular mass to filter by
     * @param aFlavour MassComputationFlavours constant that switches the computation type of the mass calculation;
     *                 see: {@link MassComputationFlavours},
     *                      {@link AtomContainerManipulator#getMass(IAtomContainer, int)}
     * @throws NullPointerException if the given mass computation flavour is null
     * @throws IllegalArgumentException if the given min molecular mass is less than zero
     */
    public MinMolecularMassFilter(double aMinMolecularMass, MassComputationFlavours aFlavour) throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(aFlavour, "aFlavour (MassComputationFlavours constant) is null.");
        if (aMinMolecularMass < 0) {    //TODO: would not harm the code but makes no sense
            throw new IllegalArgumentException("aMinMolecularMass (double value) was < than 0.");
        }
        this.minMolecularMass = aMinMolecularMass;
        this.massComputationFlavour = aFlavour;
    }

    /**
     * Constructor of the MinMolecularMassFilter class. This constructor takes no specification of the 'mass flavour'
     * that switches the computation type of the mass calculation; {@link MassComputationFlavours#MOL_WEIGHT} is used
     * by default.
     * Atom containers that equal the given min molecular mass do not get filtered.
     *
     * @param aMinMolecularMass double value of the min molecular mass to filter by
     * @throws IllegalArgumentException if the given min molecular mass is less than zero
     */
    public MinMolecularMassFilter(double aMinMolecularMass) throws IllegalArgumentException {
        this(aMinMolecularMass, MassComputationFlavours.MOL_WEIGHT);
    }

    /**
     * {@inheritDoc}
     * Atom containers that equal the min molecular mass do not get filtered.
     */
    @Override
    protected boolean isFiltered(IAtomContainer anAtomContainer, boolean aReportToReporter) throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of IAtomContainer) is null.");
        //
        return ChemUtils.getMass(anAtomContainer, this.massComputationFlavour) < this.minMolecularMass;
    }

    /**
     * Returns the min molecular mass value.
     *
     * @return double value
     */
    public double getMinMolecularMass() {
        return this.minMolecularMass;
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
