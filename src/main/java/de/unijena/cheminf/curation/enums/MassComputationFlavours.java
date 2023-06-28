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

package de.unijena.cheminf.curation.enums;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

/**
 * Enum that defines the different 'mass flavours' that specify the type of computation of the mass of a molecule.
 * The key distinction is the handling of specified/unspecified isotopes. A specified isotope is an atom that has
 * either {@link IAtom#setMassNumber(Integer)} or {@link IAtom#setExactMass(Double)} set to a non-null and non-zero
 * value.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 * @see AtomContainerManipulator#getMass(IAtomContainer, int)
 */
public enum MassComputationFlavours {

    /**
     * When calculating the mass of a molecule, this option uses the mass stored on atoms (IAtom.getExactMass())
     * or the average mass of the element when unspecified.
     *
     * @see AtomContainerManipulator#MolWeight
     */
    MOL_WEIGHT(AtomContainerManipulator.MolWeight),          //0x1

    /**
     * When calculating the mass of a molecule, this option ignores the mass stored on atoms (IAtom.getExactMass())
     * and uses the average mass of each element. This option is primarily provided for backwards compatibility.
     *
     * @see AtomContainerManipulator#MolWeightIgnoreSpecified
     */
    MOL_WEIGHT_IGNORE_SPECIFIED(AtomContainerManipulator.MolWeightIgnoreSpecified),    //0x2

    /**
     * When calculating the mass of a molecule, this option uses the mass stored on atoms IAtom.getExactMass()
     * or the mass of the major isotope when this is not specified.
     *
     * @see AtomContainerManipulator#MonoIsotopic
     */
    MONO_ISOTOPIC(AtomContainerManipulator.MonoIsotopic),    //0x3

    /**
     * When calculating the mass of a molecule, this option uses the mass stored on atoms IAtom.getExactMass()
     * and then calculates a distribution for any unspecified atoms and uses the most abundant distribution.
     * For example C6Br6 would have three 79Br and 81Br because their abundance is 51 and 49%.
     *
     * @see AtomContainerManipulator#MostAbundant
     */
    MOST_ABUNDANT(AtomContainerManipulator.MostAbundant);    //0x4

    /**
     * Integer value associated with the specific 'mass flavour'.
     */
    private final int associatedIntegerValue;

    /**
     * Internal constructor.
     *
     * @param anAssociatedIntegerValue the integer value the 'mass flavour' is associated with
     * @throws IllegalArgumentException if the given associated integer value is less than one or greater than four
     */
    MassComputationFlavours(int anAssociatedIntegerValue) throws IllegalArgumentException {
        if (anAssociatedIntegerValue < 1 || anAssociatedIntegerValue > 4) {
            throw new IllegalArgumentException("The integer value associated with a mass computation flavour " +
                    "may only be a value between one and four.");
        }
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
