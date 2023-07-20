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
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

/**
 * Test class for class MaxAtomCountFilter.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public class MaxAtomCountFilterTest {

    /**
     * Tests whether the public constructor initializes all class fields with the given parameters; test 1.
     */
    @Test
    public void publicConstructorTest_initializesClassVarsWithGivenParams_test1() {
        int tmpMaxAtomCount = 5;
        boolean tmpConsiderImplicitHydrogens = true;
        boolean tmpConsiderPseudoAtoms = true;
        MaxAtomCountFilter tmpMaxAtomCountFilter = new MaxAtomCountFilter(tmpMaxAtomCount, tmpConsiderImplicitHydrogens,
                tmpConsiderPseudoAtoms, TestUtils.getDefaultReporterInstance());
        Assertions.assertEquals(tmpMaxAtomCount, tmpMaxAtomCountFilter.atomCountThreshold);
        Assertions.assertEquals(tmpConsiderImplicitHydrogens, tmpMaxAtomCountFilter.considerImplicitHydrogens);
    }

    /**
     * Tests whether the public constructor initializes all class fields with the given parameters; test 2.
     */
    @Test
    public void publicConstructorTest_initializesClassVarsWithGivenParams_test2() {
        int tmpMaxAtomCount = 10;
        boolean tmpConsiderImplicitHydrogens = false;
        boolean tmpConsiderPseudoAtoms = true;
        MaxAtomCountFilter tmpMaxAtomCountFilter = new MaxAtomCountFilter(tmpMaxAtomCount, tmpConsiderImplicitHydrogens,
                tmpConsiderPseudoAtoms, TestUtils.getDefaultReporterInstance());
        Assertions.assertEquals(tmpMaxAtomCount, tmpMaxAtomCountFilter.atomCountThreshold);
        Assertions.assertEquals(tmpConsiderImplicitHydrogens, tmpMaxAtomCountFilter.considerImplicitHydrogens);
    }

    /**
     * Tests whether the public constructor throws an IllegalArgumentException if the given max atom count is of a
     * negative value.
     */
    @Test
    public void publicConstructorTest_throwsIllegalArgumentExceptionIfMaxAtomCountIsNegative() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    int tmpMaxAtomCount = -1;
                    boolean tmpConsiderImplicitHydrogens = true;
                    boolean tmpConsiderPseudoAtoms = true;
                    new MaxAtomCountFilter(tmpMaxAtomCount, tmpConsiderImplicitHydrogens, tmpConsiderPseudoAtoms,
                            TestUtils.getDefaultReporterInstance());
                }
        );
    }

    /**
     * Tests whether method .isFiltered() of class MaxAtomCountFilter returns false if an AC does not exceed the max
     * atom count considering implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     * @throws Exception if something went wrong
     */
    @Test
    public void isFilteredMethodTest_returnsFalse_considerImplicitHydrogens() throws InvalidSmilesException, Exception {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("CCO");   //9 atoms
        int tmpMaxAtomCount = 9;
        boolean tmpConsiderImplicitHydrogens = true;
        boolean tmpConsiderPseudoAtoms = true;
        IFilter tmpFilter = new MaxAtomCountFilter(tmpMaxAtomCount, tmpConsiderImplicitHydrogens,
                tmpConsiderPseudoAtoms, TestUtils.getDefaultReporterInstance());
        Assertions.assertFalse(tmpFilter.isFiltered(tmpAtomContainer));
    }

    /**
     * Tests whether method .isFiltered() of class MaxAtomCountFilter returns true if an AC exceeds the max atom
     * count considering implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     * @throws Exception if something went wrong
     */
    @Test
    public void isFilteredMethodTest_returnsTrue_considerImplicitHydrogens() throws InvalidSmilesException, Exception {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("CCO");   //9 atoms
        int tmpMaxAtomCount = 8;
        boolean tmpConsiderImplicitHydrogens = true;
        boolean tmpConsiderPseudoAtoms = true;
        IFilter tmpFilter = new MaxAtomCountFilter(tmpMaxAtomCount, tmpConsiderImplicitHydrogens,
                tmpConsiderPseudoAtoms, TestUtils.getDefaultReporterInstance());
        Assertions.assertTrue(tmpFilter.isFiltered(tmpAtomContainer));
    }

    /**
     * Tests whether method .isFiltered() of class MaxAtomCountFilter returns false if an AC does not exceed the max
     * atom count not considering implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     * @throws Exception if something went wrong
     */
    @Test
    public void isFilteredMethodTest_returnsFalse_notConsiderImplicitHydrogens() throws InvalidSmilesException, Exception {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("CCO");   //3 atoms
        int tmpMaxAtomCount = 3;
        boolean tmpConsiderImplicitHydrogens = false;
        boolean tmpConsiderPseudoAtoms = true;
        IFilter tmpFilter = new MaxAtomCountFilter(tmpMaxAtomCount, tmpConsiderImplicitHydrogens,
                tmpConsiderPseudoAtoms, TestUtils.getDefaultReporterInstance());
        Assertions.assertFalse(tmpFilter.isFiltered(tmpAtomContainer));
    }

    /**
     * Tests whether method .isFiltered() of class MaxAtomCountFilter returns true if an AC exceeds the max atom
     * count not considering implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     * @throws Exception if something went wrong
     */
    @Test
    public void isFilteredMethodTest_returnsTrue_notConsiderImplicitHydrogens() throws InvalidSmilesException, Exception {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("CCO");   //3 atoms
        int tmpMaxAtomCount = 2;
        boolean tmpConsiderImplicitHydrogens = false;
        boolean tmpConsiderPseudoAtoms = true;
        IFilter tmpFilter = new MaxAtomCountFilter(tmpMaxAtomCount, tmpConsiderImplicitHydrogens,
                tmpConsiderPseudoAtoms, TestUtils.getDefaultReporterInstance());
        Assertions.assertTrue(tmpFilter.isFiltered(tmpAtomContainer));
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
                    int tmpMaxAtomCount = 5;
                    boolean tmpConsiderImplicitHydrogens = true;
                    boolean tmpConsiderPseudoAtoms = true;
                    IFilter tmpMaxAtomCountFilter = new MaxAtomCountFilter(tmpMaxAtomCount,
                            tmpConsiderImplicitHydrogens, tmpConsiderPseudoAtoms, TestUtils.getDefaultReporterInstance());
                    tmpMaxAtomCountFilter.isFiltered(null);
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
        int tmpMaxAtomCount = 5;
        boolean tmpConsiderImplicitHydrogens = true;
        boolean tmpConsiderPseudoAtoms = true;
        IFilter tmpMaxAtomCountFilter = new MaxAtomCountFilter(tmpMaxAtomCount, tmpConsiderImplicitHydrogens,
                tmpConsiderPseudoAtoms, TestUtils.getDefaultReporterInstance());
        Object tmpReturnValue = tmpMaxAtomCountFilter.process(tmpAtomContainerSet, false);
        Assertions.assertNotNull(tmpReturnValue);
        Assertions.assertInstanceOf(IAtomContainerSet.class, tmpReturnValue);
    }

    /**
     * Tests whether the .filter() method filters as expected.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     * @throws Exception if something went wrong
     */
    @Test
    public void filterMethodTest_filtersAsExpected_test1() throws InvalidSmilesException, Exception {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "C=CC=C",   //10 (4)
                "c1ccccc1", //12 (6) - filtered
                "CCO"       // 9 (3)
        );
        int[] tmpNotFilteredArray = new int[]{0, 2};
        //
        int tmpMaxAtomCount = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        boolean tmpConsiderPseudoAtoms = true;
        IFilter tmpMaxAtomCountFilter = new MaxAtomCountFilter(tmpMaxAtomCount, tmpConsiderImplicitHydrogens,
                tmpConsiderPseudoAtoms, TestUtils.getDefaultReporterInstance());
        IAtomContainerSet tmpFilteredACSet = tmpMaxAtomCountFilter.process(tmpAtomContainerSet, false);
        Assertions.assertEquals(tmpNotFilteredArray.length, tmpFilteredACSet.getAtomContainerCount());
        for (int i = 0; i < tmpNotFilteredArray.length; i++) {
            Assertions.assertSame(tmpAtomContainerSet.getAtomContainer(tmpNotFilteredArray[i]),
                    tmpFilteredACSet.getAtomContainer(i));
        }
    }

    /**
     * Tests whether the .filter() method filters as expected.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     * @throws Exception if something went wrong
     */
    @Test
    public void filterMethodTest_filtersAsExpected_test2() throws InvalidSmilesException, Exception {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "c1ccccc1", //12 (6) - filtered
                "CCO",      // 9 (3)
                "C=CC=C"    //10 (4) - filtered
        );
        int[] tmpNotFilteredArray = new int[]{1};
        //
        int tmpMaxAtomCount = 3;
        boolean tmpConsiderImplicitHydrogens = false;
        boolean tmpConsiderPseudoAtoms = true;
        IFilter tmpMaxAtomCountFilter = new MaxAtomCountFilter(tmpMaxAtomCount, tmpConsiderImplicitHydrogens,
                tmpConsiderPseudoAtoms, TestUtils.getDefaultReporterInstance());
        IAtomContainerSet tmpFilteredACSet = tmpMaxAtomCountFilter.process(tmpAtomContainerSet, false);
        Assertions.assertEquals(tmpNotFilteredArray.length, tmpFilteredACSet.getAtomContainerCount());
        for (int i = 0; i < tmpNotFilteredArray.length; i++) {
            Assertions.assertSame(tmpAtomContainerSet.getAtomContainer(tmpNotFilteredArray[i]),
                    tmpFilteredACSet.getAtomContainer(i));
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
                    int tmpMaxAtomCount = 5;
                    boolean tmpConsiderImplicitHydrogens = true;
                    boolean tmpConsiderPseudoAtoms = true;
                    IFilter tmpMaxAtomCountFilter = new MaxAtomCountFilter(tmpMaxAtomCount,
                            tmpConsiderImplicitHydrogens, tmpConsiderPseudoAtoms, TestUtils.getDefaultReporterInstance());
                    tmpMaxAtomCountFilter.process(null, false);
                }
        );
    }

}
