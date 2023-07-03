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
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

/**
 * Test class for class MaxHeavyAtomCountFilter.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public class MaxHeavyAtomCountFilterTest {

    /**
     * Tests whether the public constructor initializes all class fields with the given parameters; test 1.
     */
    @Test
    public void publicConstructorTest_initializesClassVarsWithGivenParams_test1() {
        int tmpMaxHeavyAtomCount = 5;
        MaxHeavyAtomCountFilter tmpMaxHeavyAtomCountFilter = new MaxHeavyAtomCountFilter(tmpMaxHeavyAtomCount);
        Assertions.assertEquals(tmpMaxHeavyAtomCount, tmpMaxHeavyAtomCountFilter.heavyAtomCountThreshold);
    }

    /**
     * Tests whether the public constructor initializes all class fields with the given parameters; test 2.
     */
    @Test
    public void publicConstructorTest_initializesClassVarsWithGivenParams_test2() {
        int tmpMaxHeavyAtomCount = 10;
        MaxHeavyAtomCountFilter tmpMaxHeavyAtomCountFilter = new MaxHeavyAtomCountFilter(tmpMaxHeavyAtomCount);
        Assertions.assertEquals(tmpMaxHeavyAtomCount, tmpMaxHeavyAtomCountFilter.heavyAtomCountThreshold);
    }

    /**
     * Tests whether the public constructor throws an IllegalArgumentException if the given max heavy atom count is of
     * a negative value.
     */
    @Test
    public void publicConstructorTest_throwsIllegalArgumentExceptionIfMaxHeavyAtomCountIsNegative() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    int tmpMaxHeavyAtomCount = -1;
                    new MaxHeavyAtomCountFilter(tmpMaxHeavyAtomCount);
                }
        );
    }

    /**
     * Tests whether method .isFiltered() of class MaxHeavyAtomCountFilter returns true if an AC exceeds the
     * max heavy atom count; tested with multiple atom containers.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     * @throws Exception if something went wrong
     */
    @Test
    public void isFilteredMethodTest_returnsTrueIfGivenAtomContainerExceedsTheMaxHeavyAtomCount() throws InvalidSmilesException, Exception {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "c1ccccc1", //6 atoms
                "C1CCCC1",  //5 atoms
                "CC(=O)O"   //4 atoms
        );
        int tmpMaxHeavyAtomCount = 3;
        IFilter tmpFilter = new MaxHeavyAtomCountFilter(tmpMaxHeavyAtomCount);
        for (IAtomContainer tmpAtomContainer :
                tmpAtomContainerSet.atomContainers()) {
            Assertions.assertTrue(tmpFilter.isFiltered(tmpAtomContainer));
        }
    }

    /**
     * Tests whether method .isFiltered() of class MaxHeavyAtomCountFilter returns false if an AC falls short of the
     * max heavy atom count; tested with multiple atom containers.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     * @throws Exception if something went wrong
     */
    @Test
    public void isFilteredMethodTest_returnsFalseIfGivenAtomContainerFallsShortOfTheMaxHeavyAtomCount() throws InvalidSmilesException, Exception {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "c1ccccc1", //6 atoms
                "C1CCCC1",  //5 atoms
                "CC(=O)O"   //4 atoms
        );
        int tmpMaxHeavyAtomCount = 7;
        IFilter tmpFilter = new MaxHeavyAtomCountFilter(tmpMaxHeavyAtomCount);
        for (IAtomContainer tmpAtomContainer :
                tmpAtomContainerSet.atomContainers()) {
            Assertions.assertFalse(tmpFilter.isFiltered(tmpAtomContainer));
        }
    }

    /**
     * Tests whether method .isFiltered() of class MaxHeavyAtomCountFilter returns false if an AC equals the max heavy
     * atom count; tested with multiple atom containers.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     * @throws Exception if something went wrong
     */
    @Test
    public void isFilteredMethodTest_returnsTrueIfGivenAtomContainerEqualsTheMaxHeavyAtomCount() throws InvalidSmilesException, Exception {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "c1ccccc1", //6 atoms
                "C1CCCC1",  //5 atoms
                "CC(=O)O"   //4 atoms
        );
        int[] tmpMaxHeavyAtomCountArray = new int[]{6, 5, 4};
        IFilter tmpFilter;
        for (int i = 0; i < tmpAtomContainerSet.getAtomContainerCount(); i++) {
            tmpFilter = new MaxHeavyAtomCountFilter(tmpMaxHeavyAtomCountArray[i]);
            Assertions.assertFalse(tmpFilter.isFiltered(tmpAtomContainerSet.getAtomContainer(i)));
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
                    int tmpMaxHeavyAtomCount = 5;
                    IFilter tmpMaxHeavyAtomCountFilter = new MaxHeavyAtomCountFilter(tmpMaxHeavyAtomCount);
                    tmpMaxHeavyAtomCountFilter.isFiltered(null);
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
        int tmpMaxHeavyAtomCount = 5;
        IFilter tmpMaxHeavyAtomCountFilter = new MaxHeavyAtomCountFilter(tmpMaxHeavyAtomCount);
        Object tmpReturnValue = tmpMaxHeavyAtomCountFilter.process(tmpAtomContainerSet, false, true);
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
                "C=CC=C",   //4
                "c1ccccc1", //6 - filtered
                "CCO"       //3
        );
        int[] tmpNotFilteredArray = new int[]{0, 2};
        //
        int tmpMaxHeavyAtomCount = 4;
        IFilter tmpMaxHeavyAtomCountFilter = new MaxHeavyAtomCountFilter(tmpMaxHeavyAtomCount);
        IAtomContainerSet tmpFilteredACSet = tmpMaxHeavyAtomCountFilter.process(tmpAtomContainerSet, false, true);
        Assertions.assertEquals(tmpNotFilteredArray.length, tmpFilteredACSet.getAtomContainerCount());
        for (int i = 0; i < tmpNotFilteredArray.length; i++) {
            Assertions.assertSame(tmpAtomContainerSet.getAtomContainer(tmpNotFilteredArray[i]), tmpFilteredACSet.getAtomContainer(i));
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
                "c1ccccc1", //6 - filtered
                "CCO",      //3
                "C=CC=C"    //4 - filtered
        );
        int[] tmpNotFilteredArray = new int[]{1};
        //
        int tmpMaxHeavyAtomCount = 3;
        IFilter tmpMaxHeavyAtomCountFilter = new MaxHeavyAtomCountFilter(tmpMaxHeavyAtomCount);
        IAtomContainerSet tmpFilteredACSet = tmpMaxHeavyAtomCountFilter.process(tmpAtomContainerSet, false, true);
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
                    int tmpMaxHeavyAtomCount = 5;
                    IFilter tmpMaxHeavyAtomCountFilter = new MaxHeavyAtomCountFilter(tmpMaxHeavyAtomCount);
                    tmpMaxHeavyAtomCountFilter.process(null, false, true);
                }
        );
    }

}
