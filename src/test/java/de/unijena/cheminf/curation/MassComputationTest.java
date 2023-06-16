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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

/**
 * Test class of class MassComputation.
 *
 * @see MassComputation
 */
public class MassComputationTest {

    /**
     * Tests whether the values returned by the .getMass() method of class MassComputation equal the mass calculated
     * by the .getMass() method of class AtomContainerManipulator.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     * @see AtomContainerManipulator#getMass(IAtomContainer, int)
     */
    @Test
    public void getMassMethodTest_calculatedValuesEqualAtomContainerManipulatorMethodGetMassResults_withDiffFlavours()
            throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "CCO",
                "NCC(=O)O",
                "C",
                "CH",
                "HCH"
        );
        MassComputation.Flavours[] tmpFlavoursArray = new MassComputation.Flavours[]{
                MassComputation.Flavours.MolWeight,
                MassComputation.Flavours.MolWeightIgnoreSpecified,
                MassComputation.Flavours.MonoIsotopic,
                MassComputation.Flavours.MostAbundant
        };
        for (MassComputation.Flavours tmpFlavour :
                tmpFlavoursArray) {
            for (IAtomContainer tmpAtomContainer :
                    tmpAtomContainerSet.atomContainers()) {
                Assertions.assertEquals(
                        AtomContainerManipulator.getMass(tmpAtomContainer, tmpFlavour.getAssociatedIntegerValue()),
                        MassComputation.getMass(tmpAtomContainer, tmpFlavour)
                );
            }
        }
    }

    /**
     * Tests whether the values returned by the .getMass() method of class MassComputation equal the masses calculated
     * by the .getMass() method of class AtomContainerManipulator if atoms have a specified mass.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     * @see AtomContainerManipulator#getMass(IAtomContainer, int)
     */
    @Test
    public void getMassMethodTest_calculatedValuesEqualAtomContainerManipulatorMethodGetMassResults_withDiffFlavours_withSpecifiedMass()
            throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "CCO",
                "CCO"
        );
        tmpAtomContainerSet.getAtomContainer(1).getAtom(0).setExactMass(13.0);
        tmpAtomContainerSet.getAtomContainer(1).getAtom(2).setExactMass(18.0);
        MassComputation.Flavours[] tmpFlavoursArray = new MassComputation.Flavours[]{
                MassComputation.Flavours.MolWeight,
                MassComputation.Flavours.MolWeightIgnoreSpecified,
                MassComputation.Flavours.MonoIsotopic,
                MassComputation.Flavours.MostAbundant
        };
        for (MassComputation.Flavours tmpFlavour :
                tmpFlavoursArray) {
            for (IAtomContainer tmpAtomContainer :
                    tmpAtomContainerSet.atomContainers()) {
                Assertions.assertEquals(
                        AtomContainerManipulator.getMass(tmpAtomContainer, tmpFlavour.getAssociatedIntegerValue()),
                        MassComputation.getMass(tmpAtomContainer, tmpFlavour)
                );
            }
        }
    }

    /**
     * Tests whether the .getMass() method of class MassComputation throws a NullPointerException if the given
     * IAtomContainer instance is null.
     */
    @Test
    public void getMassMethodTest_atomContainerNull_throwsNullPointerException() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    IAtomContainer tmpAtomContainer = null;
                    MassComputation.Flavours tmpFlavour = MassComputation.Flavours.MolWeight;
                    MassComputation.getMass(tmpAtomContainer, tmpFlavour);
                }
        );
    }

    /**
     * Tests whether the .getMass() method of class MassComputation throws a NullPointerException if the given
     * MassComputation.Flavours constant is null.
     */
    @Test
    public void getMassMethodTest_massComputationFlavourNull_throwsNullPointerException() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    IAtomContainer tmpAtomContainer = new AtomContainer();
                    MassComputation.Flavours tmpFlavour = null;
                    MassComputation.getMass(tmpAtomContainer, tmpFlavour);
                }
        );
    }

}
