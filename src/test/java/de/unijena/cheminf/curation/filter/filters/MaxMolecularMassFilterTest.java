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
import de.unijena.cheminf.curation.TestUtils;
import de.unijena.cheminf.curation.filter.IFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

/**
 * Test class of class MaxMolecularMassFilter.
 */
public class MaxMolecularMassFilterTest {

    /**
     * Tests whether both public constructors throw an IllegalArgumentException if the given max molecular mass is of a
     * negative value.
     */
    @Test
    public void publicConstructorsTest_maxMolecularMassOfNegativeValue_bothConstructorsThrowIllegalArgumentException() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    double tmpMaxMolecularMass = -0.1;
                    MassComputationFlavours tmpFlavour = MassComputationFlavours.MOL_WEIGHT;
                    new MaxMolecularMassFilter(tmpMaxMolecularMass, tmpFlavour);
                }
        );
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    double tmpMaxMolecularMass = -0.1;
                    new MaxMolecularMassFilter(tmpMaxMolecularMass);
                }
        );
    }

    /**
     * Tests whether the public constructor with a 'mass flavour' as parameter throws a NullPointerException if the
     * given MassComputationFlavours constant is null.
     */
    @Test
    public void publicConstructorTest_flavourIsNull_throwsNullPointerException() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    double tmpMaxMolecularMass = 5.0;
                    MassComputationFlavours tmpFlavour = null;
                    new MaxMolecularMassFilter(tmpMaxMolecularMass, tmpFlavour);
                }
        );
    }

    /**
     * Tests whether method .isFiltered() of class MaxMolecularMassFilter returns true if a given atom container
     * exceeds the max molecular mass value; tested with different mass computation flavours; tested with multiple
     * atom containers.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void isFilteredMethodTest_atomContainersExceedingMaxMass_returnsTrue_testedWithDiffFlavours() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "CCO",
                "CCO",
                "C",
                "CH"
        );
        //one AC with atoms with a specified mass
        tmpAtomContainerSet.getAtomContainer(1).getAtom(0).setExactMass(13.0);
        tmpAtomContainerSet.getAtomContainer(1).getAtom(2).setExactMass(18.0);
        //
        MassComputationFlavours[] tmpFlavoursArray = new MassComputationFlavours[]{
                MassComputationFlavours.MOL_WEIGHT,
                MassComputationFlavours.MOL_WEIGHT_IGNORE_SPECIFIED,
                MassComputationFlavours.MONO_ISOTOPIC,
                MassComputationFlavours.MOST_ABUNDANT
        };
        IFilter tmpFilter;
        for (MassComputationFlavours tmpFlavour :
                tmpFlavoursArray) {
            for (IAtomContainer tmpAtomContainer :
                    tmpAtomContainerSet.atomContainers()) {
                tmpFilter = new MaxMolecularMassFilter(
                        ChemUtils.getMass(tmpAtomContainer, tmpFlavour) - 0.1,
                        tmpFlavour
                );
                Assertions.assertTrue(tmpFilter.isFiltered(tmpAtomContainer));
            }
        }
    }

    /**
     * Tests whether method .isFiltered() of class MaxMolecularMassFilter returns false if a given atom container falls
     * short of the max molecular mass value; tested with different mass computation flavours; tested with multiple
     * atom containers.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void isFilteredMethodTest_atomContainersFallingShortOfMaxMass_testedWithDiffFlavours() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "CCO",
                "CCO",
                "C",
                "CH"
        );
        //one AC with atoms with a specified mass
        tmpAtomContainerSet.getAtomContainer(1).getAtom(0).setExactMass(13.0);
        tmpAtomContainerSet.getAtomContainer(1).getAtom(2).setExactMass(18.0);
        //
        MassComputationFlavours[] tmpFlavoursArray = new MassComputationFlavours[]{
                MassComputationFlavours.MOL_WEIGHT,
                MassComputationFlavours.MOL_WEIGHT_IGNORE_SPECIFIED,
                MassComputationFlavours.MONO_ISOTOPIC,
                MassComputationFlavours.MOST_ABUNDANT
        };
        IFilter tmpFilter;
        for (MassComputationFlavours tmpFlavour :
                tmpFlavoursArray) {
            for (IAtomContainer tmpAtomContainer :
                    tmpAtomContainerSet.atomContainers()) {
                tmpFilter = new MaxMolecularMassFilter(
                        ChemUtils.getMass(tmpAtomContainer, tmpFlavour) + 0.1,
                        tmpFlavour
                );
                Assertions.assertFalse(tmpFilter.isFiltered(tmpAtomContainer));
            }
        }
    }

    /**
     * Tests whether method .isFiltered() of class MaxMolecularMassFilter returns true if a given atom container equals
     * the max molecular mass value; tested with different mass computation flavours; tested with multiple atom
     * containers.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void isFilteredMethodTest_atomContainersEqualingMaxMass_returnsTrue_testedWithDiffFlavours() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "CCO",
                "CCO",
                "C",
                "CH"
        );
        //one AC with atoms with a specified mass
        tmpAtomContainerSet.getAtomContainer(1).getAtom(0).setExactMass(13.0);
        tmpAtomContainerSet.getAtomContainer(1).getAtom(2).setExactMass(18.0);
        //
        MassComputationFlavours[] tmpFlavoursArray = new MassComputationFlavours[]{
                MassComputationFlavours.MOL_WEIGHT,
                MassComputationFlavours.MOL_WEIGHT_IGNORE_SPECIFIED,
                MassComputationFlavours.MONO_ISOTOPIC,
                MassComputationFlavours.MOST_ABUNDANT
        };
        IFilter tmpFilter;
        for (MassComputationFlavours tmpFlavour :
                tmpFlavoursArray) {
            for (IAtomContainer tmpAtomContainer :
                    tmpAtomContainerSet.atomContainers()) {
                tmpFilter = new MaxMolecularMassFilter(
                        ChemUtils.getMass(tmpAtomContainer, tmpFlavour),
                        tmpFlavour
                );
                Assertions.assertFalse(tmpFilter.isFiltered(tmpAtomContainer));
            }
        }
    }

    /**
     * Tests whether the .isFiltered() method throws a NullPointerException if the given IAtomContainer instance is
     * null.
     */
    @Test
    public void isFilteredMethodTest_atomContainerIsNull_throwsNullPointerException() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    double tmpMaxMolecularMass = 5.0;
                    MassComputationFlavours tmpFlavour = MassComputationFlavours.MOL_WEIGHT;
                    IFilter tmpMaxMolecularMassFilter = new MaxMolecularMassFilter(tmpMaxMolecularMass, tmpFlavour);
                    tmpMaxMolecularMassFilter.isFiltered(null);
                }
        );
    }

    /**
     * Tests whether the .process() method filters as expected for this filter; exemplary tested with 'mass flavour'
     * MolWeight; test 1.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void processMethodTest_multipleACs_massFlavourMolWeight_processesAsExpected_test1() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "C",        //approx. 16
                "C=CC=C",   //approx. 54 - filtered
                "CCO"       //approx. 40
        );
        int[] tmpNotFilteredArray = new int[]{0, 2};
        //
        double tmpMaxMolecularMass = 50.0;
        MassComputationFlavours tmpFlavour = MassComputationFlavours.MOL_WEIGHT;
        IFilter tmpMaxMolecularMassFilter = new MaxMolecularMassFilter(tmpMaxMolecularMass, tmpFlavour);
        IAtomContainerSet tmpFilteredACSet = tmpMaxMolecularMassFilter.process(tmpAtomContainerSet, false, true);
        Assertions.assertEquals(tmpNotFilteredArray.length, tmpFilteredACSet.getAtomContainerCount());
        for (int i = 0; i < tmpNotFilteredArray.length; i++) {
            Assertions.assertSame(tmpAtomContainerSet.getAtomContainer(tmpNotFilteredArray[i]), tmpFilteredACSet.getAtomContainer(i));
        }
    }

    /**
     * Tests whether the .process() method filters as expected; exemplary tested with 'mass flavour' MolWeight; test 2.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void processMethodTest_multipleACs_massFlavourMolWeight_processesAsExpected_test2() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "C",        //approx. 16
                "C=CC=C",   //approx. 54 - filtered
                "CCO"       //approx. 40 - filtered
        );
        int[] tmpNotFilteredArray = new int[]{0};
        //
        double tmpMaxMolecularMass = 30.0;
        MassComputationFlavours tmpFlavour = MassComputationFlavours.MOL_WEIGHT;
        IFilter tmpMaxMolecularMassFilter = new MaxMolecularMassFilter(tmpMaxMolecularMass, tmpFlavour);
        IAtomContainerSet tmpFilteredACSet = tmpMaxMolecularMassFilter.process(tmpAtomContainerSet, false, true);
        Assertions.assertEquals(tmpNotFilteredArray.length, tmpFilteredACSet.getAtomContainerCount());
        for (int i = 0; i < tmpNotFilteredArray.length; i++) {
            Assertions.assertSame(tmpAtomContainerSet.getAtomContainer(tmpNotFilteredArray[i]), tmpFilteredACSet.getAtomContainer(i));
        }
    }

    /**
     * Tests whether the .process() method throws a NullPointerException if the given IAtomContainerSet instance is
     * null.
     */
    @Test
    public void processMethodTest_atomContainerIsNull_throwsNullPointerException() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    double tmpMaxMolecularMass = 5.0;
                    MassComputationFlavours tmpFlavour = MassComputationFlavours.MOL_WEIGHT;
                    IFilter tmpMaxMolecularMassFilter = new MaxMolecularMassFilter(tmpMaxMolecularMass, tmpFlavour);
                    tmpMaxMolecularMassFilter.process(null, false, true);
                }
        );
    }

}
