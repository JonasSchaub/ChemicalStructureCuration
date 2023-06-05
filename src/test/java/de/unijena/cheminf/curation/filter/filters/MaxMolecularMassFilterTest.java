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
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

/**
 * Test class of class MaxMolecularMassFilter.
 */
public class MaxMolecularMassFilterTest {

    /**
     * Tests whether both public constructors initialize the class var maxMolecularMass with the given double value;
     * test 1.
     */
    @Test
    public void publicConstructorsTest_bothInitializeMaxMolecularMassWithGivenParam_test1() {
        double tmpMaxMolecularMass = 5.0;
        //specified mass computation flavour
        MassComputationFlavours tmpFlavour = MassComputationFlavours.MolWeight;
        MaxMolecularMassFilter tmpMaxMolecularMassFilter = new MaxMolecularMassFilter(tmpMaxMolecularMass, tmpFlavour);
        Assertions.assertEquals(tmpMaxMolecularMass, tmpMaxMolecularMassFilter.maxMolecularMass);
        //default mass computation flavour
        tmpMaxMolecularMassFilter = new MaxMolecularMassFilter(tmpMaxMolecularMass);
        Assertions.assertEquals(tmpMaxMolecularMass, tmpMaxMolecularMassFilter.maxMolecularMass);
    }

    /**
     * Tests whether both public constructors initialize the class var maxMolecularMass with the given double value;
     * test 2.
     */
    @Test
    public void publicConstructorsTest_bothInitializeMaxMolecularMassWithGivenParam_test2() {
        double tmpMaxMolecularMass = 10.0;
        //specified mass computation flavour
        MassComputationFlavours tmpFlavour = MassComputationFlavours.MolWeight;
        MaxMolecularMassFilter tmpMaxMolecularMassFilter = new MaxMolecularMassFilter(tmpMaxMolecularMass, tmpFlavour);
        Assertions.assertEquals(tmpMaxMolecularMass, tmpMaxMolecularMassFilter.maxMolecularMass);
        //default mass computation flavour
        tmpMaxMolecularMassFilter = new MaxMolecularMassFilter(tmpMaxMolecularMass);
        Assertions.assertEquals(tmpMaxMolecularMass, tmpMaxMolecularMassFilter.maxMolecularMass);
    }

    /**
     * Tests whether the public constructor that takes a 'mass flavour' initializes the respective class var with the
     * given MassComputationFlavours constant; two tests.
     */
    @Test
    public void publicConstructorWithSpecifiedFlavourTest_initializesMassComputationFlavourWithGivenFlavour_twoTests() {
        double tmpMaxMolecularMass = 5.0;
        //test 1
        MassComputationFlavours tmpFlavour = MassComputationFlavours.MolWeight;
        MaxMolecularMassFilter tmpMaxMolecularMassFilter = new MaxMolecularMassFilter(tmpMaxMolecularMass, tmpFlavour);
        Assertions.assertEquals(tmpFlavour, tmpMaxMolecularMassFilter.massComputationFlavour);
        //test 2
        tmpFlavour = MassComputationFlavours.MonoIsotopic;
        tmpMaxMolecularMassFilter = new MaxMolecularMassFilter(tmpMaxMolecularMass, tmpFlavour);
        Assertions.assertEquals(tmpFlavour, tmpMaxMolecularMassFilter.massComputationFlavour);
    }

    /**
     * Tests whether the public constructor that takes no 'mass flavour' initializes the respective class var with
     * MassComputationFlavours constant MolWeight.
     */
    @Test
    public void publicConstructorNoFlavourTest_initializesMassComputationFlavourWithFlavourMolWeight() {
        double tmpMaxMolecularMass = 5.0;
        MaxMolecularMassFilter tmpMaxMolecularMassFilter = new MaxMolecularMassFilter(tmpMaxMolecularMass);
        Assertions.assertEquals(MassComputationFlavours.MolWeight, tmpMaxMolecularMassFilter.massComputationFlavour);
    }

    /**
     * Tests whether both public constructors throw an IllegalArgumentException if the given max molecular mass is of a
     * negative value.
     */
    @Test
    public void publicConstructorsTest_bothThrowIllegalArgumentExceptionIfMaxMolecularMassIsNegative() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    double tmpMaxMolecularMass = -0.1;
                    MassComputationFlavours tmpFlavour = MassComputationFlavours.MolWeight;
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
     * Tests whether the public constructor that takes a 'mass flavour' throws a NullPointerException if the given
     * MassComputationFlavours constant is null.
     */
    @Test
    public void publicConstructorWithSpecifiedFlavourTest_throwsNullPointerExceptionIfGivenFlavourIsNull() {
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
     * Tests whether method .isFiltered() of class MaxMolecularMassFilter returns a boolean value.
     */
    @Test
    public void isFilteredMethodTest_returnsBoolean() {
        IAtomContainer tmpAtomContainer = new AtomContainer();
        double tmpMaxMolecularMass = 5.0;
        MassComputationFlavours tmpFlavour = MassComputationFlavours.MolWeight;
        IFilter tmpFilter = new MaxMolecularMassFilter(tmpMaxMolecularMass, tmpFlavour);
        Assertions.assertInstanceOf(Boolean.class, tmpFilter.isFiltered(tmpAtomContainer));
    }

    /**
     * Tests whether method .isFiltered() of class MaxMolecularMassFilter returns true if an AC exceeds the max
     * molecular mass; tested with different mass computation flavours; tested with multiple atom containers.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void isFilteredMethodTest_returnsTrueIfGivenAtomContainerExceedsTheMaxMass_diffFlavoursTested() throws InvalidSmilesException {
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
                MassComputationFlavours.MolWeight,
                MassComputationFlavours.MolWeightIgnoreSpecified,
                MassComputationFlavours.MonoIsotopic,
                MassComputationFlavours.MostAbundant
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
     * Tests whether method .isFiltered() of class MaxMolecularMassFilter returns false if an AC falls short of the max
     * molecular mass; tested with different mass computation flavours; tested with multiple atom containers.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void isFilteredMethodTest_returnsFalseIfGivenAtomContainerFallsShortOfTheMaxMass_diffFlavoursTested() throws InvalidSmilesException {
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
                MassComputationFlavours.MolWeight,
                MassComputationFlavours.MolWeightIgnoreSpecified,
                MassComputationFlavours.MonoIsotopic,
                MassComputationFlavours.MostAbundant
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
     * Tests whether method .isFiltered() of class MaxMolecularMassFilter returns true if an AC equals the max
     * molecular mass; tested with different mass computation flavours; tested with multiple atom containers.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void isFilteredMethodTest_returnsTrueIfGivenAtomContainerEqualsTheMaxMass_diffFlavoursTested() throws InvalidSmilesException {
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
                MassComputationFlavours.MolWeight,
                MassComputationFlavours.MolWeightIgnoreSpecified,
                MassComputationFlavours.MonoIsotopic,
                MassComputationFlavours.MostAbundant
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
    public void isFilteredMethodTest_throwsNullPointerExceptionIfGivenAtomContainerIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    double tmpMaxMolecularMass = 5.0;
                    MassComputationFlavours tmpFlavour = MassComputationFlavours.MolWeight;
                    IFilter tmpMaxMolecularMassFilter = new MaxMolecularMassFilter(tmpMaxMolecularMass, tmpFlavour);
                    tmpMaxMolecularMassFilter.isFiltered(null);
                }
        );
    }

    /**
     * Tests whether the return value of the .filter() method is not null and an instance of IAtomContainerSet.
     */
    @Test
    public void filterMethodTest_returnsIAtomContainerSetNotNull() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        double tmpMaxMolecularMass = 5.0;
        MassComputationFlavours tmpFlavour = MassComputationFlavours.MolWeight;
        IFilter tmpMaxMolecularMassFilter = new MaxMolecularMassFilter(tmpMaxMolecularMass, tmpFlavour);
        Object tmpReturnValue = tmpMaxMolecularMassFilter.filter(tmpAtomContainerSet);
        Assertions.assertNotNull(tmpReturnValue);
        Assertions.assertInstanceOf(IAtomContainerSet.class, tmpReturnValue);
    }

    /**
     * Tests whether the .filter() method filters as expected; exemplary tested with 'mass flavour' MolWeight; test 1.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_filtersAsExpected_massFlavourMolWeight_test1() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "C",        //approx. 16
                "C=CC=C",   //approx. 54 - filtered
                "CCO"       //approx. 40
        );
        int[] tmpNotFilteredArray = new int[]{0, 2};
        //
        double tmpMaxMolecularMass = 50.0;
        MassComputationFlavours tmpFlavour = MassComputationFlavours.MolWeight;
        IFilter tmpMaxMolecularMassFilter = new MaxMolecularMassFilter(tmpMaxMolecularMass, tmpFlavour);
        IAtomContainerSet tmpFilteredACSet = tmpMaxMolecularMassFilter.filter(tmpAtomContainerSet);
        Assertions.assertEquals(tmpNotFilteredArray.length, tmpFilteredACSet.getAtomContainerCount());
        for (int i = 0; i < tmpNotFilteredArray.length; i++) {
            Assertions.assertSame(tmpAtomContainerSet.getAtomContainer(tmpNotFilteredArray[i]), tmpFilteredACSet.getAtomContainer(i));
        }
    }

    /**
     * Tests whether the .filter() method filters as expected; exemplary tested with 'mass flavour' MolWeight; test 2.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_filtersAsExpected_massFlavourMolWeight_test2() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "C",        //approx. 16
                "C=CC=C",   //approx. 54 - filtered
                "CCO"       //approx. 40 - filtered
        );
        int[] tmpNotFilteredArray = new int[]{0};
        //
        double tmpMaxMolecularMass = 30.0;
        MassComputationFlavours tmpFlavour = MassComputationFlavours.MolWeight;
        IFilter tmpMaxMolecularMassFilter = new MaxMolecularMassFilter(tmpMaxMolecularMass, tmpFlavour);
        IAtomContainerSet tmpFilteredACSet = tmpMaxMolecularMassFilter.filter(tmpAtomContainerSet);
        Assertions.assertEquals(tmpNotFilteredArray.length, tmpFilteredACSet.getAtomContainerCount());
        for (int i = 0; i < tmpNotFilteredArray.length; i++) {
            Assertions.assertSame(tmpAtomContainerSet.getAtomContainer(tmpNotFilteredArray[i]), tmpFilteredACSet.getAtomContainer(i));
        }
    }

    /**
     * Tests whether the .filter() method throws a NullPointerException if the given IAtomContainerSet instance is null.
     */
    @Test
    public void filterMethodTest_throwsNullPointerExceptionIfGivenIAtomContainerSetIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    double tmpMaxMolecularMass = 5.0;
                    MassComputationFlavours tmpFlavour = MassComputationFlavours.MolWeight;
                    IFilter tmpMaxMolecularMassFilter = new MaxMolecularMassFilter(tmpMaxMolecularMass, tmpFlavour);
                    tmpMaxMolecularMassFilter.filter(null);
                }
        );
    }

    /** TODO: why does this one fail?
     * Tests the getter of maxMolecularMass whether it returns maxMolecularMass.
     */
    @Test
    public void getMaxMolecularMassMethodTest_returnsMaxMolecularMass() {
        double tmpMaxMolecularMass = 5.0;
        MassComputationFlavours tmpFlavour = MassComputationFlavours.MolWeight;
        MaxMolecularMassFilter tmpMaxMolecularMassFilter = new MaxMolecularMassFilter(tmpMaxMolecularMass, tmpFlavour);
        Assertions.assertSame(tmpMaxMolecularMassFilter.maxMolecularMass, tmpMaxMolecularMassFilter.getMaxMolecularMass());
    }

    /**
     * Tests the getter of massComputationFlavour whether it returns massComputationFlavour.
     */
    @Test
    public void getMassComputationFlavourMethodTest_returnsMassComputationFlavour() {
        double tmpMaxMolecularMass = 5.0;
        MassComputationFlavours tmpFlavour = MassComputationFlavours.MolWeight;
        MaxMolecularMassFilter tmpMaxMolecularMassFilter = new MaxMolecularMassFilter(tmpMaxMolecularMass, tmpFlavour);
        Assertions.assertSame(tmpMaxMolecularMassFilter.massComputationFlavour, tmpMaxMolecularMassFilter.getMassComputationFlavour());
    }

}
