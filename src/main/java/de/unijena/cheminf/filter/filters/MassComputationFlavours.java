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

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

/**
 * Enum that defines the different 'mass flavours' that specify the computation type for the calculation of the
 * mass of a molecule. The key distinction is how specified/unspecified isotopes are handled. A specified isotope
 * is an atom that has either {@link IAtom#setMassNumber(Integer)} or {@link IAtom#setExactMass(Double)} set to
 * non-null and non-zero.
 *
 * @see AtomContainerManipulator#getMass(IAtomContainer, int)
 */
public enum MassComputationFlavours {   //TODO: Computation / Calculation ?

    /**
     * When calculating the mass of a molecule, this option uses the mass stored on atoms (IAtom.getExactMass())
     * or the average mass of the element when unspecified.
     *
     * @see AtomContainerManipulator#MolWeight
     */
    MolWeight(AtomContainerManipulator.MolWeight),

    /**
     * When calculating the mass of a molecule, this option ignores the mass stored on atoms (IAtom.getExactMass())
     * and uses the average mass of each element. This option is primarily provided for backwards compatibility.
     *
     * @see AtomContainerManipulator#MolWeightIgnoreSpecified
     */
    MolWeightIgnoreSpecified(AtomContainerManipulator.MolWeightIgnoreSpecified),

    /**
     * When calculating the mass of a molecule, this option uses the mass stored on atoms IAtom.getExactMass()
     * or the mass of the major isotope when this is not specified.
     *
     * @see AtomContainerManipulator#MonoIsotopic
     */
    MonoIsotopic(AtomContainerManipulator.MonoIsotopic),

    /**
     * When calculating the mass of a molecule, this option uses the mass stored on atoms IAtom.getExactMass()
     * and then calculates a distribution for any unspecified atoms and uses the most abundant distribution.
     * For example C6Br6 would have three 79Br and 81Br because their abundance is 51 and 49%.
     *
     * @see AtomContainerManipulator#MostAbundant
     */
    MostAbundant(AtomContainerManipulator.MostAbundant);

    /**
     * Integer value associated with the specific 'mass flavour'.
     */
    private final int associatedIntegerValue;

    /**
     * Internal constructor.
     *
     * @param anAssociatedIntegerValue the integer value the 'mass flavour' is associated with
     */
    MassComputationFlavours(int anAssociatedIntegerValue) {
        this.associatedIntegerValue = anAssociatedIntegerValue;
    }

    /**
     * Returns the integer value the 'mass flavour' is associated with.
     *
     * @return integer value
     */
    public int getAssociatedIntegerValue() {
        return this.associatedIntegerValue;
    }

}
