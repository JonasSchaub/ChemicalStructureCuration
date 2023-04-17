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

package de.unijena.cheminf.filter.filters;

import de.unijena.cheminf.ChemUtils;
import de.unijena.cheminf.MassComputationFlavours;
import de.unijena.cheminf.filter.FilterBase;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

import java.util.Objects;

/**
 * Max molecular mass filter for filtering atom containers based on a maximum molecular mass.
 */
public class MaxMolecularMassFilter extends FilterBase {

    /**
     * Double value of the max molecular mass threshold.
     */
    protected final double maxMolecularMass;

    /**
     * MassComputationFlavours constant that switches the computation type of the mass calculation.
     */
    protected final MassComputationFlavours massComputationFlavour;

    /**
     * Constructor of the MaxMolecularMassFilter class. Atom containers that equal the given max molecular mass do not
     * get filtered.
     *
     * @param aMaxMolecularMass double value of the max molecular mass to filter by
     * @param aFlavour MassComputationFlavours constant that switches the computation type of the mass calculation;
     *                 see: {@link MassComputationFlavours},
     *                      {@link AtomContainerManipulator#getMass(IAtomContainer, int)}
     * @throws NullPointerException if the given constant of MassComputationFlavours is null
     * @throws IllegalArgumentException if the given max molecular mass is less than zero
     */
    public MaxMolecularMassFilter(double aMaxMolecularMass, MassComputationFlavours aFlavour) throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(aFlavour, "aFlavour (MassComputationFlavours constant) is null.");
        if (aMaxMolecularMass < 0) {    //TODO: would not harm the code but makes no sense
            throw new IllegalArgumentException("aMaxMolecularMass (double value) was < than 0.");
        }
        this.maxMolecularMass = aMaxMolecularMass;
        this.massComputationFlavour = aFlavour;
    }

    /**
     * Constructor of the MaxMolecularMassFilter class. This constructor takes no specification of the 'mass flavour'
     * that switches the computation type of the mass calculation; {@link MassComputationFlavours#MolWeight} is used by
     * default.
     * Atom containers that equal the given max molecular mass do not get filtered.
     *
     * @param aMaxMolecularMass double value of the max molecular mass to filter by
     * @throws IllegalArgumentException if the given max molecular mass is less than zero
     */
    public MaxMolecularMassFilter(double aMaxMolecularMass) throws IllegalArgumentException {
        this(aMaxMolecularMass, MassComputationFlavours.MolWeight);
    }

    /**
     * {@inheritDoc}
     * Atom containers that equal the max molecular mass do not get filtered.
     */
    @Override
    public boolean isFiltered(IAtomContainer anAtomContainer) throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of IAtomContainer) is null.");
        //
        return ChemUtils.getMass(anAtomContainer, this.massComputationFlavour) > this.maxMolecularMass;
    }

    /**
     * Returns the max molecular mass value.
     *
     * @return double value
     */
    public double getMaxMolecularMass() {
        return this.maxMolecularMass;
    }

    /**
     * Returns the mass computation flavour.
     *
     * @return MassComputationFlavours constant
     */
    public MassComputationFlavours getMassComputationFlavour() {
        return this.massComputationFlavour;
    }

}
