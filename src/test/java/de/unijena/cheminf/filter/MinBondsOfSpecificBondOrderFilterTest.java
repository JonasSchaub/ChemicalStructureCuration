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

package de.unijena.cheminf.filter;

import de.unijena.cheminf.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.Bond;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IBond;

/**
 * Test class of class MinBondsOfSpecificBondOrderFilter.
 */
public class MinBondsOfSpecificBondOrderFilterTest {

    /**
     * Tests whether the public constructor initializes all class fields with the given parameters; test 1.
     */
    @Test
    public void publicConstructorTest_initializesClassVarsWithGivenParams_test1() {
        IBond.Order tmpBondOrder = IBond.Order.UNSET;
        int tmpMinSpecificBondCount = 5;
        boolean tmpConsiderImplicitHydrogens = true;
        MinBondsOfSpecificBondOrderFilter tmpMinBondsOfSpecificBondOrderFilter
                = new MinBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpMinSpecificBondCount, tmpConsiderImplicitHydrogens);
        Assertions.assertEquals(tmpBondOrder, tmpMinBondsOfSpecificBondOrderFilter.bondOrderOfInterest);
        Assertions.assertEquals(tmpMinSpecificBondCount, tmpMinBondsOfSpecificBondOrderFilter.minSpecificBondCount);
        Assertions.assertEquals(tmpConsiderImplicitHydrogens, tmpMinBondsOfSpecificBondOrderFilter.considerImplicitHydrogens);
    }

    /**
     * Tests whether the public constructor initializes all class fields with the given parameters; test 2.
     */
    @Test
    public void publicConstructorTest_initializesClassVarsWithGivenParams_test2() {
        IBond.Order tmpBondOrder = IBond.Order.UNSET;
        int tmpMinSpecificBondCount = 5;
        boolean tmpConsiderImplicitHydrogens = true;
        MinBondsOfSpecificBondOrderFilter tmpMinBondsOfSpecificBondOrderFilter
                = new MinBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpMinSpecificBondCount, tmpConsiderImplicitHydrogens);
        Assertions.assertEquals(tmpBondOrder, tmpMinBondsOfSpecificBondOrderFilter.bondOrderOfInterest);
        Assertions.assertEquals(tmpMinSpecificBondCount, tmpMinBondsOfSpecificBondOrderFilter.minSpecificBondCount);
        Assertions.assertEquals(tmpConsiderImplicitHydrogens, tmpMinBondsOfSpecificBondOrderFilter.considerImplicitHydrogens);
    }

    /**
     * Tests whether the public constructor throws an IllegalArgumentException if the given min specific bond count is
     * of a negative value.
     */
    @Test
    public void publicConstructorTest_throwsIllegalArgumentExceptionIfMinSpecificBondCountIsNegative() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    IBond.Order tmpBondOrder = IBond.Order.UNSET;
                    int tmpMinSpecificBondCount = -1;
                    boolean tmpConsiderImplicitHydrogens = true;
                    new MinBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpMinSpecificBondCount, tmpConsiderImplicitHydrogens);
                }
        );
    }

    /**
     * Tests whether method .isFiltered() of class MinBondsOfSpecificBondOrderFilter returns a boolean value.
     */
    @Test
    public void isFilteredMethodTest_returnsBoolean() {
        IAtomContainer tmpAtomContainer = new AtomContainer();
        IBond.Order tmpBondOrder = IBond.Order.UNSET;
        int tmpMinSpecificBondCount = 0;
        boolean tmpConsiderImplicitHydrogens = true;
        IFilter tmpFilter = new MinBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpMinSpecificBondCount, tmpConsiderImplicitHydrogens);
        Assertions.assertInstanceOf(Boolean.class, tmpFilter.isFiltered(tmpAtomContainer));
    }

    /**
     * Tests whether method .isFiltered() of class MinBondsOfSpecificBondOrderFilter returns false if an AC does not
     * exceed the min specific bond count for bonds with bond order single considering bonds to implicit hydrogen atoms
     * and whether it returns true if it does exceed.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void isFilteredMethodTest_returnsFalse_bondOrderSingle_considerImplicitHydrogens() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("CCO");   //8 bonds
        IBond.Order tmpBondOrder = IBond.Order.SINGLE;
        boolean tmpConsiderImplicitHydrogens = true;
        //
        int tmpMinSpecificBondCount = 7;    //exceeded
        IFilter tmpFilter = new MinBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpMinSpecificBondCount, tmpConsiderImplicitHydrogens);
        Assertions.assertFalse(tmpFilter.isFiltered(tmpAtomContainer));
        //
        tmpMinSpecificBondCount = 8;    //equaled
        tmpFilter = new MinBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpMinSpecificBondCount, tmpConsiderImplicitHydrogens);
        Assertions.assertFalse(tmpFilter.isFiltered(tmpAtomContainer));
        //
        tmpMinSpecificBondCount = 9;    //fallen short
        tmpFilter = new MinBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpMinSpecificBondCount, tmpConsiderImplicitHydrogens);
        Assertions.assertTrue(tmpFilter.isFiltered(tmpAtomContainer));
    }

    /**
     * Tests whether method .isFiltered() of class MinBondsOfSpecificBondOrderFilter returns false if an AC does not
     * exceed the min specific bond count for bonds with bond order single not considering bonds to implicit hydrogen
     * atoms and whether it returns true if it does exceed.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void isFilteredMethodTest_bondOrderSingle_notConsiderImplicitHydrogens() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("CCO");   //2 bonds
        IBond.Order tmpBondOrder = IBond.Order.SINGLE;
        boolean tmpConsiderImplicitHydrogens = false;
        //
        int tmpMinSpecificBondCount = 1;    //exceeded
        IFilter tmpFilter = new MinBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpMinSpecificBondCount, tmpConsiderImplicitHydrogens);
        Assertions.assertFalse(tmpFilter.isFiltered(tmpAtomContainer));
        //
        tmpMinSpecificBondCount = 2;    //equaled
        tmpFilter = new MinBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpMinSpecificBondCount, tmpConsiderImplicitHydrogens);
        Assertions.assertFalse(tmpFilter.isFiltered(tmpAtomContainer));
        //
        tmpMinSpecificBondCount = 3;    //fallen short
        tmpFilter = new MinBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpMinSpecificBondCount, tmpConsiderImplicitHydrogens);
        Assertions.assertTrue(tmpFilter.isFiltered(tmpAtomContainer));
    }

    /**
     * Tests whether method .isFiltered() of class MinBondsOfSpecificBondOrderFilter returns false if a given atom
     * container is below or equals the threshold and true if the atom container exceeds the threshold; tests for bond
     * order being double.
     */
    @Test
    public void isFilteredMethodTest_bondOrderDouble() {
        IBond.Order tmpBondOrder = IBond.Order.DOUBLE;
        int tmpThresholdValue = 1;
        boolean tmpConsiderImplicitHydrogens = false;   //can be ignored
        IFilter tmpMinSpecificBondsFilter = new MinBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpThresholdValue, tmpConsiderImplicitHydrogens);
        //below threshold
        IAtomContainer tmpAtomContainer = new AtomContainer();
        Assertions.assertTrue(tmpMinSpecificBondsFilter.isFiltered(tmpAtomContainer));
        //threshold equaled
        IBond tmpBond = new Bond();
        tmpBond.setOrder(tmpBondOrder);
        tmpAtomContainer.addBond(tmpBond);
        Assertions.assertFalse(tmpMinSpecificBondsFilter.isFiltered(tmpAtomContainer));
        //threshold exceeded
        tmpAtomContainer.addBond(tmpBond);
        Assertions.assertFalse(tmpMinSpecificBondsFilter.isFiltered(tmpAtomContainer));
    }

    /**
     * Tests whether method .isFiltered() of class MinBondsOfSpecificBondOrderFilter returns false if a given atom
     * container is below or equals the threshold and true if the atom container exceeds the threshold; tests for bond
     * order being triple, quadruple, quintuple, unset and null.
     */
    @Test
    public void isFilteredMethodTest_returnsTrue_notConsiderImplicitHydrogens() {
        IBond.Order[] tmpBondOrderArray = new IBond.Order[]{
                IBond.Order.DOUBLE,
                IBond.Order.TRIPLE,
                IBond.Order.QUADRUPLE,
                IBond.Order.QUINTUPLE,
                IBond.Order.SEXTUPLE,
                IBond.Order.UNSET,
                null
        };
        int tmpThresholdValue = 1;
        boolean tmpConsiderImplicitHydrogens = false;   //can be ignored
        IAtomContainer tmpAtomContainer = new AtomContainer();;
        IBond tmpBond;
        IFilter tmpMinSpecificBondsFilter;
        //
        for (IBond.Order tmpBondOrder : tmpBondOrderArray) {
            tmpMinSpecificBondsFilter = new MinBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpThresholdValue, tmpConsiderImplicitHydrogens);
            tmpBond = new Bond();
            tmpBond.setOrder(tmpBondOrder);
            //
            //below threshold
            Assertions.assertTrue(tmpMinSpecificBondsFilter.isFiltered(tmpAtomContainer));
            //
            //threshold equaled
            tmpAtomContainer.addBond(tmpBond);
            Assertions.assertFalse(tmpMinSpecificBondsFilter.isFiltered(tmpAtomContainer));
            //
            //threshold exceeded
            tmpAtomContainer.addBond(tmpBond);
            Assertions.assertFalse(tmpMinSpecificBondsFilter.isFiltered(tmpAtomContainer));
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
                    IBond.Order tmpBondOrder = IBond.Order.UNSET;
                    int tmpMinBondsOfSpecificBondOrder = 5;
                    boolean tmpConsiderImplicitHydrogens = true;
                    IFilter tmpMinBondsOfSpecificBondOrderFilter = new MinBondsOfSpecificBondOrderFilter(
                            tmpBondOrder, tmpMinBondsOfSpecificBondOrder, tmpConsiderImplicitHydrogens
                    );
                    tmpMinBondsOfSpecificBondOrderFilter.isFiltered(null);
                }
        );
    }

    /**
     * Tests whether the return value of the .filter() method is not null and an instance of IAtomContainerSet.
     */
    @Test
    public void filterMethodTest_returnsIAtomContainerSetNotNull() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        IBond.Order tmpBondOrder = IBond.Order.UNSET;
        int tmpMinSpecificBondCount = 5;
        boolean tmpConsiderImplicitHydrogens = true;
        Filter tmpMinBondsOfSpecificBondOrderFilter = new MinBondsOfSpecificBondOrderFilter(
                tmpBondOrder, tmpMinSpecificBondCount, tmpConsiderImplicitHydrogens
        );
        Object tmpReturnValue = tmpMinBondsOfSpecificBondOrderFilter.filter(tmpAtomContainerSet);
        Assertions.assertNotNull(tmpReturnValue);
        Assertions.assertInstanceOf(IAtomContainerSet.class, tmpReturnValue);
    }

    /**
     * Tests whether the .filter() method filters as expected; test for bond order double.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_filtersAsExpected_exemplaryForBondOrderDouble() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "C=CC=C",   // 2
                "c1ccccc1", // 3
                "CC(=O)O"   // 1 - filtered
        );
        int[] tmpNotFilteredArray = new int[]{0, 1};
        //
        IBond.Order tmpBondOrder = IBond.Order.DOUBLE;
        int tmpMinSpecificBondCount = 2;
        boolean tmpConsiderImplicitHydrogens = false;   //can be ignored
        Filter tmpMinBondsOfSpecificBondOrderFilter = new MinBondsOfSpecificBondOrderFilter(
                tmpBondOrder, tmpMinSpecificBondCount, tmpConsiderImplicitHydrogens
        );
        IAtomContainerSet tmpFilteredACSet = tmpMinBondsOfSpecificBondOrderFilter.filter(tmpAtomContainerSet);
        Assertions.assertEquals(tmpNotFilteredArray.length, tmpFilteredACSet.getAtomContainerCount());
        for (int i = 0; i < tmpNotFilteredArray.length; i++) {
            Assertions.assertSame(tmpAtomContainerSet.getAtomContainer(tmpNotFilteredArray[i]), tmpFilteredACSet.getAtomContainer(i));
        }
    }

    /**
     * Tests whether the .filter() method filters as expected; test for bond order single considering bonds to implicit
     * hydrogen atoms.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_filtersAsExpected_exemplaryForBondOrderSingle_considerImplHs() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "C=CC=C",   // 7
                "CC(=O)O",  // 6 - filtered
                "c1ccccc1"  // 9
        );
        int[] tmpNotFilteredArray = new int[]{0, 2};
        //
        IBond.Order tmpBondOrder = IBond.Order.SINGLE;
        int tmpMinSpecificBondCount = 7;
        boolean tmpConsiderImplicitHydrogens = true;
        Filter tmpMinBondsOfSpecificBondOrderFilter = new MinBondsOfSpecificBondOrderFilter(
                tmpBondOrder, tmpMinSpecificBondCount, tmpConsiderImplicitHydrogens
        );
        IAtomContainerSet tmpFilteredACSet = tmpMinBondsOfSpecificBondOrderFilter.filter(tmpAtomContainerSet);
        Assertions.assertEquals(tmpNotFilteredArray.length, tmpFilteredACSet.getAtomContainerCount());
        for (int i = 0; i < tmpNotFilteredArray.length; i++) {
            Assertions.assertSame(tmpAtomContainerSet.getAtomContainer(tmpNotFilteredArray[i]), tmpFilteredACSet.getAtomContainer(i));
        }
    }

    /**
     * Tests whether the .filter() method filters as expected; test for bond order single not considering bonds to
     * implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_filtersAsExpected_exemplaryForBondOrderSingle_notConsiderImplHs() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "c1ccccc1", // 3
                "C=CC=C",   // 1 - filtered
                "CC(=O)O"   // 2 - filtered
        );
        int[] tmpNotFilteredArray = new int[]{0};
        //
        IBond.Order tmpBondOrder = IBond.Order.SINGLE;
        int tmpMinSpecificBondCount = 3;
        boolean tmpConsiderImplicitHydrogens = false;
        Filter tmpMinBondsOfSpecificBondOrderFilter = new MinBondsOfSpecificBondOrderFilter(
                tmpBondOrder, tmpMinSpecificBondCount, tmpConsiderImplicitHydrogens
        );
        IAtomContainerSet tmpFilteredACSet = tmpMinBondsOfSpecificBondOrderFilter.filter(tmpAtomContainerSet);
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
                    IBond.Order tmpBondOrder = IBond.Order.UNSET;
                    int tmpMinSpecificBondCount = 5;
                    boolean tmpConsiderImplicitHydrogens = true;
                    Filter tmpMinBondsOfSpecificBondOrderFilter = new MinBondsOfSpecificBondOrderFilter(
                            tmpBondOrder, tmpMinSpecificBondCount, tmpConsiderImplicitHydrogens
                    );
                    tmpMinBondsOfSpecificBondOrderFilter.filter(null);
                }
        );
    }

    /**
     * Tests the getter of bondOrderOfInterest whether it returns bondOrderOfInterest.
     */
    @Test
    public void getBondOrderOfInterestMethodTest_returnsBondOrderOfInterest() {
        IBond.Order tmpBondOrder = IBond.Order.UNSET;
        int tmpMinSpecificBondCount = 5;
        boolean tmpConsiderImplicitHydrogens = true;
        MinBondsOfSpecificBondOrderFilter tmpMinBondsOfSpecificBondOrderFilter = new MinBondsOfSpecificBondOrderFilter(
                tmpBondOrder, tmpMinSpecificBondCount, tmpConsiderImplicitHydrogens
        );
        Assertions.assertSame(tmpMinBondsOfSpecificBondOrderFilter.bondOrderOfInterest,
                tmpMinBondsOfSpecificBondOrderFilter.getBondOrderOfInterest());
    }

    /**
     * Tests the getter of minSpecificBondCount whether it returns minSpecificBondCount.
     */
    @Test
    public void getMinSpecificBondCountMethodTest_returnsMinSpecificBondCount() {
        IBond.Order tmpBondOrder = IBond.Order.UNSET;
        int tmpMinSpecificBondCount = 5;
        boolean tmpConsiderImplicitHydrogens = true;
        MinBondsOfSpecificBondOrderFilter tmpMinBondsOfSpecificBondOrderFilter = new MinBondsOfSpecificBondOrderFilter(
                tmpBondOrder, tmpMinSpecificBondCount, tmpConsiderImplicitHydrogens
        );
        Assertions.assertSame(tmpMinBondsOfSpecificBondOrderFilter.minSpecificBondCount,
                tmpMinBondsOfSpecificBondOrderFilter.getMinSpecificBondCount());
    }

    /**
     * Tests the getter of considerImplicitHydrogens whether it returns considerImplicitHydrogens.
     */
    @Test
    public void isConsiderImplicitHydrogensMethodTest_returnsConsiderImplicitHydrogens() {
        IBond.Order tmpBondOrder = IBond.Order.UNSET;
        int tmpMinSpecificBondCount = 5;
        boolean tmpConsiderImplicitHydrogens = true;
        MinBondsOfSpecificBondOrderFilter tmpMinBondsOfSpecificBondOrderFilter = new MinBondsOfSpecificBondOrderFilter(
                tmpBondOrder, tmpMinSpecificBondCount, tmpConsiderImplicitHydrogens
        );
        Assertions.assertSame(tmpMinBondsOfSpecificBondOrderFilter.considerImplicitHydrogens,
                tmpMinBondsOfSpecificBondOrderFilter.isConsiderImplicitHydrogens());
    }

}
