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

package de.unijena.cheminf.curation;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

import java.util.Objects;

/**
 * Class for the computation of a molecules mass.
 */
public class MassComputation {

    /**
     * Calculates the mass of a molecule (given IAtomContainer instance). This function takes a 'mass flavour' that
     * switches the computation type of the mass calculation. The key distinction is how specified/unspecified isotopes
     * are handled. A specified isotope is an atom that has either {@link IAtom#setMassNumber(Integer)} or
     * {@link IAtom#setExactMass(Double)} set to non-null and non-zero value.
     * The mass of the molecule is calculated using the CDK method
     * {@link AtomContainerManipulator#getMass(IAtomContainer, int)}.
     *
     * @param anAtomContainer IAtomContainer instance to calculate the mass of
     * @param aFlavour MassCalculationFlavours constant that switches the computation type of the mass calculation;
     *                 see: {@link MassComputation.Flavours},
     *                      {@link AtomContainerManipulator#getMass(IAtomContainer, int)}
     * @return double value of the mass of the molecule
     * @throws NullPointerException if the given IAtomContainer instance or the given mass computation flavour is null
     * @see MassComputation.Flavours
     * @see AtomContainerManipulator#getMass(IAtomContainer, int)
     */
    public static double getMass(IAtomContainer anAtomContainer, MassComputation.Flavours aFlavour)
            throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of IAtomContainer) is null.");
        Objects.requireNonNull(aFlavour, "aFlavour (constant of MassComputation.Flavours) is null.");
        //TODO: use MolWeight as default if null is given?
        //TODO: throw an exception if the associated integer is no value between 1 and 4? Added a check to the MassComputationFlavours internal constructor
        return AtomContainerManipulator.getMass(anAtomContainer, aFlavour.getAssociatedIntegerValue());
    }

    /**
     * Enum that defines the different 'mass flavours' that specify the type of computation of the mass of a molecule.
     * The key distinction is the handling of specified/unspecified isotopes. A specified isotope is an atom that has
     * either {@link IAtom#setMassNumber(Integer)} or {@link IAtom#setExactMass(Double)} set to a non-null and non-zero
     * value.
     *
     * @see AtomContainerManipulator#getMass(IAtomContainer, int)
     */
    public enum Flavours {

        /**
         * When calculating the mass of a molecule, this option uses the mass stored on atoms (IAtom.getExactMass())
         * or the average mass of the element when unspecified.
         *
         * @see AtomContainerManipulator#MolWeight
         */
        MolWeight(AtomContainerManipulator.MolWeight),          //0x1

        /**
         * When calculating the mass of a molecule, this option ignores the mass stored on atoms (IAtom.getExactMass())
         * and uses the average mass of each element. This option is primarily provided for backwards compatibility.
         *
         * @see AtomContainerManipulator#MolWeightIgnoreSpecified
         */
        MolWeightIgnoreSpecified(AtomContainerManipulator.MolWeightIgnoreSpecified),    //0x2

        /**
         * When calculating the mass of a molecule, this option uses the mass stored on atoms IAtom.getExactMass()
         * or the mass of the major isotope when this is not specified.
         *
         * @see AtomContainerManipulator#MonoIsotopic
         */
        MonoIsotopic(AtomContainerManipulator.MonoIsotopic),    //0x3

        /**
         * When calculating the mass of a molecule, this option uses the mass stored on atoms IAtom.getExactMass()
         * and then calculates a distribution for any unspecified atoms and uses the most abundant distribution.
         * For example C6Br6 would have three 79Br and 81Br because their abundance is 51 and 49%.
         *
         * @see AtomContainerManipulator#MostAbundant
         */
        MostAbundant(AtomContainerManipulator.MostAbundant);    //0x4

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
        Flavours(int anAssociatedIntegerValue) throws IllegalArgumentException {
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

}
