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

package de.unijena.cheminf.curation.processingSteps.filters;

import de.unijena.cheminf.curation.TestUtils;
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
 *
 * @author Samuel Behr
 * @version 1.0.0.0
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
        boolean tmpConsiderPseudoAtoms = true;
        MaxBondsOfSpecificBondOrderFilter tmpMaxBondsOfSpecificBondOrderFilter
                = new MaxBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpMaxSpecificBondCount,
                tmpConsiderImplicitHydrogens, tmpConsiderPseudoAtoms, TestUtils.getTestReporterInstance());
        Assertions.assertEquals(tmpBondOrder, tmpMaxBondsOfSpecificBondOrderFilter.bondOrderOfInterest);
        Assertions.assertEquals(tmpMaxSpecificBondCount, tmpMaxBondsOfSpecificBondOrderFilter.specificBondCountThreshold);
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
        boolean tmpConsiderPseudoAtoms = true;
        MaxBondsOfSpecificBondOrderFilter tmpMaxBondsOfSpecificBondOrderFilter
                = new MaxBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpMaxSpecificBondCount,
                tmpConsiderImplicitHydrogens, tmpConsiderPseudoAtoms, TestUtils.getTestReporterInstance());
        Assertions.assertEquals(tmpBondOrder, tmpMaxBondsOfSpecificBondOrderFilter.bondOrderOfInterest);
        Assertions.assertEquals(tmpMaxSpecificBondCount, tmpMaxBondsOfSpecificBondOrderFilter.specificBondCountThreshold);
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
                    boolean tmpConsiderPseudoAtoms = true;
                    new MaxBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpMaxSpecificBondCount,
                            tmpConsiderImplicitHydrogens, tmpConsiderPseudoAtoms, TestUtils.getTestReporterInstance());
                }
        );
    }

    /**
     * Tests whether method .isFiltered() of class MaxBondsOfSpecificBondOrderFilter returns false if an AC does not
     * exceed the max specific bond count for bonds with bond order single considering bonds to implicit hydrogen atoms
     * and whether it returns true if it does exceed.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     * @throws Exception if something went wrong
     */
    @Test
    public void isFilteredMethodTest_returnsFalse_bondOrderSingle_considerImplicitHydrogens()
            throws InvalidSmilesException, Exception {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("CCO");   //8 bonds
        IBond.Order tmpBondOrder = IBond.Order.SINGLE;
        boolean tmpConsiderImplicitHydrogens = true;
        boolean tmpConsiderPseudoAtoms = true;
        //
        int tmpMaxSpecificBondCount = 9;    //fallen short
        IFilter tmpFilter = new MaxBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpMaxSpecificBondCount,
                tmpConsiderImplicitHydrogens, tmpConsiderPseudoAtoms, TestUtils.getTestReporterInstance());
        Assertions.assertFalse(tmpFilter.isFiltered(tmpAtomContainer));
        //
        tmpMaxSpecificBondCount = 8;    //equaled
        tmpFilter = new MaxBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpMaxSpecificBondCount,
                tmpConsiderImplicitHydrogens, tmpConsiderPseudoAtoms, TestUtils.getTestReporterInstance());
        Assertions.assertFalse(tmpFilter.isFiltered(tmpAtomContainer));
        //
        tmpMaxSpecificBondCount = 7;    //exceeded
        tmpFilter = new MaxBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpMaxSpecificBondCount,
                tmpConsiderImplicitHydrogens, tmpConsiderPseudoAtoms, TestUtils.getTestReporterInstance());
        Assertions.assertTrue(tmpFilter.isFiltered(tmpAtomContainer));
    }

    /**
     * Tests whether method .isFiltered() of class MaxBondsOfSpecificBondOrderFilter returns false if an AC does not
     * exceed the max specific bond count for bonds with bond order single not considering bonds to implicit hydrogen
     * atoms and whether it returns true if it does exceed.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     * @throws Exception if something went wrong
     */
    @Test
    public void isFilteredMethodTest_bondOrderSingle_notConsiderImplicitHydrogens() throws InvalidSmilesException, Exception {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("CCO");   //2 bonds
        IBond.Order tmpBondOrder = IBond.Order.SINGLE;
        boolean tmpConsiderImplicitHydrogens = false;
        boolean tmpConsiderPseudoAtoms = true;
        //
        int tmpMaxSpecificBondCount = 3;    //fallen short
        IFilter tmpFilter = new MaxBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpMaxSpecificBondCount,
                tmpConsiderImplicitHydrogens, tmpConsiderPseudoAtoms, TestUtils.getTestReporterInstance());
        Assertions.assertFalse(tmpFilter.isFiltered(tmpAtomContainer));
        //
        tmpMaxSpecificBondCount = 2;    //equaled
        tmpFilter = new MaxBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpMaxSpecificBondCount,
                tmpConsiderImplicitHydrogens, tmpConsiderPseudoAtoms, TestUtils.getTestReporterInstance());
        Assertions.assertFalse(tmpFilter.isFiltered(tmpAtomContainer));
        //
        tmpMaxSpecificBondCount = 1;    //exceeded
        tmpFilter = new MaxBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpMaxSpecificBondCount,
                tmpConsiderImplicitHydrogens, tmpConsiderPseudoAtoms, TestUtils.getTestReporterInstance());
        Assertions.assertTrue(tmpFilter.isFiltered(tmpAtomContainer));
    }

    /**
     * Tests whether method .isFiltered() of class MaxBondsOfSpecificBondOrderFilter returns false if a given atom
     * container is below or equals the threshold and true if the atom container exceeds the threshold; tests for bond
     * order being double.
     *
     * @throws Exception if something went wrong
     */
    @Test
    public void isFilteredMethodTest_bondOrderDouble() throws Exception {
        IBond.Order tmpBondOrder = IBond.Order.DOUBLE;
        int tmpThresholdValue = 1;
        boolean tmpConsiderImplicitHydrogens = false;   //can be ignored
        boolean tmpConsiderPseudoAtoms = true;
        IFilter tmpMaxSpecificBondsFilter = new MaxBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpThresholdValue,
                tmpConsiderImplicitHydrogens, tmpConsiderPseudoAtoms, TestUtils.getTestReporterInstance());
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
     *
     * @throws Exception if something went wrong
     */
    @Test
    public void isFilteredMethodTest_returnsTrue_notConsiderImplicitHydrogens() throws Exception {
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
        boolean tmpConsiderPseudoAtoms = true;
        IAtomContainer tmpAtomContainer = new AtomContainer();;
        IBond tmpBond;
        IFilter tmpMaxSpecificBondsFilter;
        //
        for (IBond.Order tmpBondOrder : tmpBondOrderArray) {
            tmpMaxSpecificBondsFilter = new MaxBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpThresholdValue,
                    tmpConsiderImplicitHydrogens, tmpConsiderPseudoAtoms, TestUtils.getTestReporterInstance());
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
                    boolean tmpConsiderPseudoAtoms = true;
                    IFilter tmpMaxBondsOfSpecificBondOrderFilter = new MaxBondsOfSpecificBondOrderFilter(
                            tmpBondOrder, tmpMaxBondsOfSpecificBondOrder, tmpConsiderImplicitHydrogens,
                            tmpConsiderPseudoAtoms, TestUtils.getTestReporterInstance()
                    );
                    tmpMaxBondsOfSpecificBondOrderFilter.isFiltered(null);
                }
        );
    }

    /**
     * Tests whether the return value of the .filter() method is not null and an instance of IAtomContainerSet.
     *
     * @throws Exception if something went wrong
     */
    @Test
    public void filterMethodTest_returnsIAtomContainerSetNotNull() throws Exception {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        IBond.Order tmpBondOrder = IBond.Order.UNSET;
        int tmpMaxSpecificBondCount = 5;
        boolean tmpConsiderImplicitHydrogens = true;
        boolean tmpConsiderPseudoAtoms = true;
        IFilter tmpMaxBondsOfSpecificBondOrderFilter = new MaxBondsOfSpecificBondOrderFilter(
                tmpBondOrder, tmpMaxSpecificBondCount, tmpConsiderImplicitHydrogens,
                tmpConsiderPseudoAtoms, TestUtils.getTestReporterInstance()
        );
        Object tmpReturnValue = tmpMaxBondsOfSpecificBondOrderFilter.process(tmpAtomContainerSet, false);
        Assertions.assertNotNull(tmpReturnValue);
        Assertions.assertInstanceOf(IAtomContainerSet.class, tmpReturnValue);
    }

    /**
     * Tests whether the .filter() method filters as expected; test for bond order double.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     * @throws Exception if something went wrong
     */
    @Test
    public void filterMethodTest_filtersAsExpected_exemplaryForBondOrderDouble() throws InvalidSmilesException, Exception {
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
        boolean tmpConsiderPseudoAtoms = true;
        IFilter tmpMaxBondsOfSpecificBondOrderFilter = new MaxBondsOfSpecificBondOrderFilter(
                tmpBondOrder, tmpMaxSpecificBondCount, tmpConsiderImplicitHydrogens,
                tmpConsiderPseudoAtoms, TestUtils.getTestReporterInstance()
        );
        IAtomContainerSet tmpFilteredACSet = tmpMaxBondsOfSpecificBondOrderFilter.process(tmpAtomContainerSet, false);
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
     * @throws Exception if something went wrong
     */
    @Test
    public void filterMethodTest_filtersAsExpected_exemplaryForBondOrderSingle_considerImplHs()
            throws InvalidSmilesException, Exception {
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
        boolean tmpConsiderPseudoAtoms = true;
        IFilter tmpMaxBondsOfSpecificBondOrderFilter = new MaxBondsOfSpecificBondOrderFilter(
                tmpBondOrder, tmpMaxSpecificBondCount, tmpConsiderImplicitHydrogens,
                tmpConsiderPseudoAtoms, TestUtils.getTestReporterInstance()
        );
        IAtomContainerSet tmpFilteredACSet = tmpMaxBondsOfSpecificBondOrderFilter.process(tmpAtomContainerSet, false);
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
     * @throws Exception if something went wrong
     */
    @Test
    public void filterMethodTest_filtersAsExpected_exemplaryForBondOrderSingle_notConsiderImplHs()
            throws InvalidSmilesException, Exception {
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
        boolean tmpConsiderPseudoAtoms = true;
        IFilter tmpMaxBondsOfSpecificBondOrderFilter = new MaxBondsOfSpecificBondOrderFilter(
                tmpBondOrder, tmpMaxSpecificBondCount, tmpConsiderImplicitHydrogens,
                tmpConsiderPseudoAtoms, TestUtils.getTestReporterInstance()
        );
        IAtomContainerSet tmpFilteredACSet = tmpMaxBondsOfSpecificBondOrderFilter.process(tmpAtomContainerSet, false);
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
                    boolean tmpConsiderPseudoAtoms = true;
                    IFilter tmpMaxBondsOfSpecificBondOrderFilter = new MaxBondsOfSpecificBondOrderFilter(
                            tmpBondOrder, tmpMaxSpecificBondCount, tmpConsiderImplicitHydrogens,
                            tmpConsiderPseudoAtoms, TestUtils.getTestReporterInstance()
                    );
                    tmpMaxBondsOfSpecificBondOrderFilter.process(null, false);
                }
        );
    }

}
