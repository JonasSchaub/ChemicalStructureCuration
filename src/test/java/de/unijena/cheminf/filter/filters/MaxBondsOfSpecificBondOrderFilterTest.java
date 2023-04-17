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

import de.unijena.cheminf.TestUtils;
import de.unijena.cheminf.filter.BaseFilter;
import de.unijena.cheminf.filter.IFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.Bond;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IBond;

/**
 * Test class of class MaxBondsOfSpecificBondOrderFilter.
 */
public class MaxBondsOfSpecificBondOrderFilterTest {

    /**
     * Tests whether the public constructor initializes all class fields with the given parameters; test 1.
     */
    @Test
    public void publicConstructorTest_initializesClassVarsWithGivenParams_test1() {
        IBond.Order tmpBondOrder = IBond.Order.UNSET;
        int tmpMaxSpecificBondCount = 5;
        boolean tmpConsiderImplicitHydrogens = true;
        MaxBondsOfSpecificBondOrderFilter tmpMaxBondsOfSpecificBondOrderFilter
                = new MaxBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpMaxSpecificBondCount, tmpConsiderImplicitHydrogens);
        Assertions.assertEquals(tmpBondOrder, tmpMaxBondsOfSpecificBondOrderFilter.bondOrderOfInterest);
        Assertions.assertEquals(tmpMaxSpecificBondCount, tmpMaxBondsOfSpecificBondOrderFilter.maxSpecificBondCount);
        Assertions.assertEquals(tmpConsiderImplicitHydrogens, tmpMaxBondsOfSpecificBondOrderFilter.considerImplicitHydrogens);
    }

    /**
     * Tests whether the public constructor initializes all class fields with the given parameters; test 2.
     */
    @Test
    public void publicConstructorTest_initializesClassVarsWithGivenParams_test2() {
        IBond.Order tmpBondOrder = IBond.Order.UNSET;
        int tmpMaxSpecificBondCount = 5;
        boolean tmpConsiderImplicitHydrogens = true;
        MaxBondsOfSpecificBondOrderFilter tmpMaxBondsOfSpecificBondOrderFilter
                = new MaxBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpMaxSpecificBondCount, tmpConsiderImplicitHydrogens);
        Assertions.assertEquals(tmpBondOrder, tmpMaxBondsOfSpecificBondOrderFilter.bondOrderOfInterest);
        Assertions.assertEquals(tmpMaxSpecificBondCount, tmpMaxBondsOfSpecificBondOrderFilter.maxSpecificBondCount);
        Assertions.assertEquals(tmpConsiderImplicitHydrogens, tmpMaxBondsOfSpecificBondOrderFilter.considerImplicitHydrogens);
    }

    /**
     * Tests whether the public constructor throws an IllegalArgumentException if the given max specific bond count is
     * of a negative value.
     */
    @Test
    public void publicConstructorTest_throwsIllegalArgumentExceptionIfMaxSpecificBondCountIsNegative() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    IBond.Order tmpBondOrder = IBond.Order.UNSET;
                    int tmpMaxSpecificBondCount = -1;
                    boolean tmpConsiderImplicitHydrogens = true;
                    new MaxBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpMaxSpecificBondCount, tmpConsiderImplicitHydrogens);
                }
        );
    }

    /**
     * Tests whether method .isFiltered() of class MaxBondsOfSpecificBondOrderFilter returns a boolean value.
     */
    @Test
    public void isFilteredMethodTest_returnsBoolean() {
        IAtomContainer tmpAtomContainer = new AtomContainer();
        IBond.Order tmpBondOrder = IBond.Order.UNSET;
        int tmpMaxSpecificBondCount = 0;
        boolean tmpConsiderImplicitHydrogens = true;
        IFilter tmpFilter = new MaxBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpMaxSpecificBondCount, tmpConsiderImplicitHydrogens);
        Assertions.assertInstanceOf(Boolean.class, tmpFilter.isFiltered(tmpAtomContainer));
    }

    /**
     * Tests whether method .isFiltered() of class MaxBondsOfSpecificBondOrderFilter returns false if an AC does not
     * exceed the max specific bond count for bonds with bond order single considering bonds to implicit hydrogen atoms
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
        int tmpMaxSpecificBondCount = 9;    //fallen short
        IFilter tmpFilter = new MaxBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpMaxSpecificBondCount, tmpConsiderImplicitHydrogens);
        Assertions.assertFalse(tmpFilter.isFiltered(tmpAtomContainer));
        //
        tmpMaxSpecificBondCount = 8;    //equaled
        tmpFilter = new MaxBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpMaxSpecificBondCount, tmpConsiderImplicitHydrogens);
        Assertions.assertFalse(tmpFilter.isFiltered(tmpAtomContainer));
        //
        tmpMaxSpecificBondCount = 7;    //exceeded
        tmpFilter = new MaxBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpMaxSpecificBondCount, tmpConsiderImplicitHydrogens);
        Assertions.assertTrue(tmpFilter.isFiltered(tmpAtomContainer));
    }

    /**
     * Tests whether method .isFiltered() of class MaxBondsOfSpecificBondOrderFilter returns false if an AC does not
     * exceed the max specific bond count for bonds with bond order single not considering bonds to implicit hydrogen
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
        int tmpMaxSpecificBondCount = 3;    //fallen short
        IFilter tmpFilter = new MaxBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpMaxSpecificBondCount, tmpConsiderImplicitHydrogens);
        Assertions.assertFalse(tmpFilter.isFiltered(tmpAtomContainer));
        //
        tmpMaxSpecificBondCount = 2;    //equaled
        tmpFilter = new MaxBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpMaxSpecificBondCount, tmpConsiderImplicitHydrogens);
        Assertions.assertFalse(tmpFilter.isFiltered(tmpAtomContainer));
        //
        tmpMaxSpecificBondCount = 1;    //exceeded
        tmpFilter = new MaxBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpMaxSpecificBondCount, tmpConsiderImplicitHydrogens);
        Assertions.assertTrue(tmpFilter.isFiltered(tmpAtomContainer));
    }

    /**
     * Tests whether method .isFiltered() of class MaxBondsOfSpecificBondOrderFilter returns false if a given atom
     * container is below or equals the threshold and true if the atom container exceeds the threshold; tests for bond
     * order being double.
     */
    @Test
    public void isFilteredMethodTest_bondOrderDouble() {
        IBond.Order tmpBondOrder = IBond.Order.DOUBLE;
        int tmpThresholdValue = 1;
        boolean tmpConsiderImplicitHydrogens = false;   //can be ignored
        IFilter tmpMaxSpecificBondsFilter = new MaxBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpThresholdValue, tmpConsiderImplicitHydrogens);
        //below threshold
        IAtomContainer tmpAtomContainer = new AtomContainer();
        Assertions.assertFalse(tmpMaxSpecificBondsFilter.isFiltered(tmpAtomContainer));
        //threshold equaled
        IBond tmpBond = new Bond();
        tmpBond.setOrder(tmpBondOrder);
        tmpAtomContainer.addBond(tmpBond);
        Assertions.assertFalse(tmpMaxSpecificBondsFilter.isFiltered(tmpAtomContainer));
        //threshold exceeded
        tmpAtomContainer.addBond(tmpBond);
        Assertions.assertTrue(tmpMaxSpecificBondsFilter.isFiltered(tmpAtomContainer));
    }

    /**
     * Tests whether method .isFiltered() of class MaxBondsOfSpecificBondOrderFilter returns false if a given atom
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
        IFilter tmpMaxSpecificBondsFilter;
        //
        for (IBond.Order tmpBondOrder : tmpBondOrderArray) {
            tmpMaxSpecificBondsFilter = new MaxBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpThresholdValue, tmpConsiderImplicitHydrogens);
            tmpBond = new Bond();
            tmpBond.setOrder(tmpBondOrder);
            //
            //below threshold
            Assertions.assertFalse(tmpMaxSpecificBondsFilter.isFiltered(tmpAtomContainer));
            //
            //threshold equaled
            tmpAtomContainer.addBond(tmpBond);
            Assertions.assertFalse(tmpMaxSpecificBondsFilter.isFiltered(tmpAtomContainer));
            //
            //threshold exceeded
            tmpAtomContainer.addBond(tmpBond);
            Assertions.assertTrue(tmpMaxSpecificBondsFilter.isFiltered(tmpAtomContainer));
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
                    int tmpMaxBondsOfSpecificBondOrder = 5;
                    boolean tmpConsiderImplicitHydrogens = true;
                    IFilter tmpMaxBondsOfSpecificBondOrderFilter = new MaxBondsOfSpecificBondOrderFilter(
                            tmpBondOrder, tmpMaxBondsOfSpecificBondOrder, tmpConsiderImplicitHydrogens
                    );
                    tmpMaxBondsOfSpecificBondOrderFilter.isFiltered(null);
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
        int tmpMaxSpecificBondCount = 5;
        boolean tmpConsiderImplicitHydrogens = true;
        BaseFilter tmpMaxBondsOfSpecificBondOrderFilter = new MaxBondsOfSpecificBondOrderFilter(
                tmpBondOrder, tmpMaxSpecificBondCount, tmpConsiderImplicitHydrogens
        );
        Object tmpReturnValue = tmpMaxBondsOfSpecificBondOrderFilter.filter(tmpAtomContainerSet);
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
                "c1ccccc1", // 3 - filtered
                "CC(=O)O"   // 1
        );
        int[] tmpNotFilteredArray = new int[]{0, 2};
        //
        IBond.Order tmpBondOrder = IBond.Order.DOUBLE;
        int tmpMaxSpecificBondCount = 2;
        boolean tmpConsiderImplicitHydrogens = false;   //can be ignored
        BaseFilter tmpMaxBondsOfSpecificBondOrderFilter = new MaxBondsOfSpecificBondOrderFilter(
                tmpBondOrder, tmpMaxSpecificBondCount, tmpConsiderImplicitHydrogens
        );
        IAtomContainerSet tmpFilteredACSet = tmpMaxBondsOfSpecificBondOrderFilter.filter(tmpAtomContainerSet);
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
                "CC(=O)O",  // 6
                "c1ccccc1"  // 9 - filtered
        );
        int[] tmpNotFilteredArray = new int[]{0, 1};
        //
        IBond.Order tmpBondOrder = IBond.Order.SINGLE;
        int tmpMaxSpecificBondCount = 7;
        boolean tmpConsiderImplicitHydrogens = true;
        BaseFilter tmpMaxBondsOfSpecificBondOrderFilter = new MaxBondsOfSpecificBondOrderFilter(
                tmpBondOrder, tmpMaxSpecificBondCount, tmpConsiderImplicitHydrogens
        );
        IAtomContainerSet tmpFilteredACSet = tmpMaxBondsOfSpecificBondOrderFilter.filter(tmpAtomContainerSet);
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
                "c1ccccc1", // 3 - filtered
                "C=CC=C",   // 1
                "CC(=O)O"   // 2 - filtered
        );
        int[] tmpNotFilteredArray = new int[]{1};
        //
        IBond.Order tmpBondOrder = IBond.Order.SINGLE;
        int tmpMaxSpecificBondCount = 1;
        boolean tmpConsiderImplicitHydrogens = false;
        BaseFilter tmpMaxBondsOfSpecificBondOrderFilter = new MaxBondsOfSpecificBondOrderFilter(
                tmpBondOrder, tmpMaxSpecificBondCount, tmpConsiderImplicitHydrogens
        );
        IAtomContainerSet tmpFilteredACSet = tmpMaxBondsOfSpecificBondOrderFilter.filter(tmpAtomContainerSet);
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
                    int tmpMaxSpecificBondCount = 5;
                    boolean tmpConsiderImplicitHydrogens = true;
                    BaseFilter tmpMaxBondsOfSpecificBondOrderFilter = new MaxBondsOfSpecificBondOrderFilter(
                            tmpBondOrder, tmpMaxSpecificBondCount, tmpConsiderImplicitHydrogens
                    );
                    tmpMaxBondsOfSpecificBondOrderFilter.filter(null);
                }
        );
    }

    /**
     * Tests the getter of bondOrderOfInterest whether it returns bondOrderOfInterest.
     */
    @Test
    public void getBondOrderOfInterestMethodTest_returnsBondOrderOfInterest() {
        IBond.Order tmpBondOrder = IBond.Order.UNSET;
        int tmpMaxSpecificBondCount = 5;
        boolean tmpConsiderImplicitHydrogens = true;
        MaxBondsOfSpecificBondOrderFilter tmpMaxBondsOfSpecificBondOrderFilter = new MaxBondsOfSpecificBondOrderFilter(
                tmpBondOrder, tmpMaxSpecificBondCount, tmpConsiderImplicitHydrogens
        );
        Assertions.assertSame(tmpMaxBondsOfSpecificBondOrderFilter.bondOrderOfInterest,
                tmpMaxBondsOfSpecificBondOrderFilter.getBondOrderOfInterest());
    }

    /**
     * Tests the getter of maxSpecificBondCount whether it returns maxSpecificBondCount.
     */
    @Test
    public void getMaxSpecificBondCountMethodTest_returnsMaxSpecificBondCount() {
        IBond.Order tmpBondOrder = IBond.Order.UNSET;
        int tmpMaxSpecificBondCount = 5;
        boolean tmpConsiderImplicitHydrogens = true;
        MaxBondsOfSpecificBondOrderFilter tmpMaxBondsOfSpecificBondOrderFilter = new MaxBondsOfSpecificBondOrderFilter(
                tmpBondOrder, tmpMaxSpecificBondCount, tmpConsiderImplicitHydrogens
        );
        Assertions.assertSame(tmpMaxBondsOfSpecificBondOrderFilter.maxSpecificBondCount,
                tmpMaxBondsOfSpecificBondOrderFilter.getMaxSpecificBondCount());
    }

    /**
     * Tests the getter of considerImplicitHydrogens whether it returns considerImplicitHydrogens.
     */
    @Test
    public void isConsiderImplicitHydrogensMethodTest_returnsConsiderImplicitHydrogens() {
        IBond.Order tmpBondOrder = IBond.Order.UNSET;
        int tmpMaxSpecificBondCount = 5;
        boolean tmpConsiderImplicitHydrogens = true;
        MaxBondsOfSpecificBondOrderFilter tmpMaxBondsOfSpecificBondOrderFilter = new MaxBondsOfSpecificBondOrderFilter(
                tmpBondOrder, tmpMaxSpecificBondCount, tmpConsiderImplicitHydrogens
        );
        Assertions.assertSame(tmpMaxBondsOfSpecificBondOrderFilter.considerImplicitHydrogens,
                tmpMaxBondsOfSpecificBondOrderFilter.isConsiderImplicitHydrogens());
    }

}
