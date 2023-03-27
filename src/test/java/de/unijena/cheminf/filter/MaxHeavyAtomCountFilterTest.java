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
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

/**
 * Test class for class MaxHeavyAtomCountFilter.
 */
public class MaxHeavyAtomCountFilterTest {

    /**
     * Tests whether the public constructor initializes all class fields with the given parameters; test 1.
     */
    @Test
    public void publicConstructorTest_initializesClassVarsWithGivenParams_test1() {
        int tmpMaxHeavyAtomCount = 5;
        MaxHeavyAtomCountFilter tmpMaxHeavyAtomCountFilter = new MaxHeavyAtomCountFilter(tmpMaxHeavyAtomCount);
        Assertions.assertEquals(tmpMaxHeavyAtomCount, tmpMaxHeavyAtomCountFilter.maxHeavyAtomCount);
    }

    /**
     * Tests whether the public constructor initializes all class fields with the given parameters; test 2.
     */
    @Test
    public void publicConstructorTest_initializesClassVarsWithGivenParams_test2() {
        int tmpMaxHeavyAtomCount = 10;
        MaxHeavyAtomCountFilter tmpMaxHeavyAtomCountFilter = new MaxHeavyAtomCountFilter(tmpMaxHeavyAtomCount);
        Assertions.assertEquals(tmpMaxHeavyAtomCount, tmpMaxHeavyAtomCountFilter.maxHeavyAtomCount);
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
     * Tests whether method .getsFiltered() of class MaxHeavyAtomCountFilter returns a boolean value.
     */
    @Test
    public void getsFilteredMethodTest_returnsBoolean() {
        IAtomContainer tmpAtomContainer = new AtomContainer();
        int tmpMaxHeavyAtomCount = 0;
        IFilter tmpFilter = new MaxHeavyAtomCountFilter(tmpMaxHeavyAtomCount);
        Assertions.assertInstanceOf(Boolean.class, tmpFilter.getsFiltered(tmpAtomContainer));
    }

    /**
     * Tests whether method .getsFiltered() of class MaxHeavyAtomCountFilter returns true if an AC exceeds the
     * max heavy atom count; tested with multiple atom containers.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void getsFilteredMethodTest_returnsTrueIfGivenAtomContainerExceedsTheMaxHeavyAtomCount() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "c1ccccc1", //6 atoms
                "C1CCCC1",  //5 atoms
                "CC(=O)O"   //4 atoms
        );
        int tmpMaxHeavyAtomCount = 3;
        IFilter tmpFilter = new MaxHeavyAtomCountFilter(tmpMaxHeavyAtomCount);
        for (IAtomContainer tmpAtomContainer :
                tmpAtomContainerSet.atomContainers()) {
            Assertions.assertTrue(tmpFilter.getsFiltered(tmpAtomContainer));
        }
    }

    /**
     * Tests whether method .getsFiltered() of class MaxHeavyAtomCountFilter returns false if an AC falls short of the
     * max heavy atom count; tested with multiple atom containers.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void getsFilteredMethodTest_returnsFalseIfGivenAtomContainerFallsShortOfTheMaxHeavyAtomCount() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "c1ccccc1", //6 atoms
                "C1CCCC1",  //5 atoms
                "CC(=O)O"   //4 atoms
        );
        int tmpMaxHeavyAtomCount = 7;
        IFilter tmpFilter = new MaxHeavyAtomCountFilter(tmpMaxHeavyAtomCount);
        for (IAtomContainer tmpAtomContainer :
                tmpAtomContainerSet.atomContainers()) {
            Assertions.assertFalse(tmpFilter.getsFiltered(tmpAtomContainer));
        }
    }

    /**
     * Tests whether method .getsFiltered() of class MaxHeavyAtomCountFilter returns false if an AC equals the max heavy
     * atom count; tested with multiple atom containers.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void getsFilteredMethodTest_returnsTrueIfGivenAtomContainerEqualsTheMaxHeavyAtomCount() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "c1ccccc1", //6 atoms
                "C1CCCC1",  //5 atoms
                "CC(=O)O"   //4 atoms
        );
        int[] tmpMaxHeavyAtomCountArray = new int[]{6, 5, 4};
        IFilter tmpFilter;
        for (int i = 0; i < tmpAtomContainerSet.getAtomContainerCount(); i++) {
            tmpFilter = new MaxHeavyAtomCountFilter(tmpMaxHeavyAtomCountArray[i]);
            Assertions.assertFalse(tmpFilter.getsFiltered(tmpAtomContainerSet.getAtomContainer(i)));
        }
    }

    /**
     * Tests whether the .getsFiltered() method throws a NullPointerException if the given IAtomContainer instance is
     * null.
     */
    @Test
    public void getsFilteredMethodTest_throwsNullPointerExceptionIfGivenAtomContainerIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    int tmpMaxHeavyAtomCount = 5;
                    IFilter tmpMaxHeavyAtomCountFilter = new MaxHeavyAtomCountFilter(tmpMaxHeavyAtomCount);
                    tmpMaxHeavyAtomCountFilter.getsFiltered(null);
                }
        );
    }

    /**
     * Tests whether the return value of the .filter() method is not null and an instance of IAtomContainerSet.
     */
    @Test
    public void filterMethodTest_returnsIAtomContainerSetNotNull() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        int tmpMaxHeavyAtomCount = 5;
        Filter tmpMaxHeavyAtomCountFilter = new MaxHeavyAtomCountFilter(tmpMaxHeavyAtomCount);
        Object tmpReturnValue = tmpMaxHeavyAtomCountFilter.filter(tmpAtomContainerSet);
        Assertions.assertNotNull(tmpReturnValue);
        Assertions.assertInstanceOf(IAtomContainerSet.class, tmpReturnValue);
    }

    /**
     * Tests whether the .filter() method filters as expected.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_filtersAsExpected_test1() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "C=CC=C",   //4
                "c1ccccc1", //6 - filtered
                "CCO"       //3
        );
        int[] tmpNotFilteredArray = new int[]{0, 2};
        //
        int tmpMaxHeavyAtomCount = 4;
        Filter tmpMaxHeavyAtomCountFilter = new MaxHeavyAtomCountFilter(tmpMaxHeavyAtomCount);
        IAtomContainerSet tmpFilteredACSet = tmpMaxHeavyAtomCountFilter.filter(tmpAtomContainerSet);
        Assertions.assertEquals(tmpNotFilteredArray.length, tmpFilteredACSet.getAtomContainerCount());
        for (int i = 0; i < tmpNotFilteredArray.length; i++) {
            Assertions.assertSame(tmpAtomContainerSet.getAtomContainer(tmpNotFilteredArray[i]), tmpFilteredACSet.getAtomContainer(i));
        }
    }

    /**
     * Tests whether the .filter() method filters as expected.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_filtersAsExpected_test2() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "c1ccccc1", //6 - filtered
                "CCO",      //3
                "C=CC=C"    //4 - filtered
        );
        int[] tmpNotFilteredArray = new int[]{1};
        //
        int tmpMaxHeavyAtomCount = 3;
        Filter tmpMaxHeavyAtomCountFilter = new MaxHeavyAtomCountFilter(tmpMaxHeavyAtomCount);
        IAtomContainerSet tmpFilteredACSet = tmpMaxHeavyAtomCountFilter.filter(tmpAtomContainerSet);
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
                    Filter tmpMaxHeavyAtomCountFilter = new MaxHeavyAtomCountFilter(tmpMaxHeavyAtomCount);
                    tmpMaxHeavyAtomCountFilter.filter(null);
                }
        );
    }

    /**
     * Tests the getter of maxHeavyAtomCount whether it returns maxHeavyAtomCount.
     */
    @Test
    public void getMaxHeavyAtomCountMethodTest_returnsMaxHeavyAtomCount() {
        int tmpMaxHeavyAtomCount = 5;
        MaxHeavyAtomCountFilter tmpMaxHeavyAtomCountFilter = new MaxHeavyAtomCountFilter(tmpMaxHeavyAtomCount);
        Assertions.assertSame(tmpMaxHeavyAtomCountFilter.maxHeavyAtomCount, tmpMaxHeavyAtomCountFilter.getMaxHeavyAtomCount());
    }

}