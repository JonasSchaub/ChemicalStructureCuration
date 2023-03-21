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
 * Test class for class MaxAtomCountFilter.
 */
public class MaxAtomCountFilterTest {

    /**
     * Tests whether the public constructor initializes all class fields with the given parameters; test 1.
     */
    @Test
    public void publicConstructorTest_initializesClassVarsWithGivenParams_test1() {
        int tmpMaxAtomCount = 5;
        boolean tmpConsiderImplicitHydrogens = true;
        MaxAtomCountFilter tmpMaxAtomCountFilter = new MaxAtomCountFilter(tmpMaxAtomCount, tmpConsiderImplicitHydrogens);
        Assertions.assertEquals(tmpMaxAtomCount, tmpMaxAtomCountFilter.getMaxAtomCount());
        Assertions.assertEquals(tmpConsiderImplicitHydrogens, tmpMaxAtomCountFilter.isConsiderImplicitHydrogens());
    }

    /**
     * Tests whether the public constructor initializes all class fields with the given parameters; test 2.
     */
    @Test
    public void publicConstructorTest_initializesClassVarsWithGivenParams_test2() {
        int tmpMaxAtomCount = 10;
        boolean tmpConsiderImplicitHydrogens = false;
        MaxAtomCountFilter tmpMaxAtomCountFilter = new MaxAtomCountFilter(tmpMaxAtomCount, tmpConsiderImplicitHydrogens);
        Assertions.assertEquals(tmpMaxAtomCount, tmpMaxAtomCountFilter.getMaxAtomCount());
        Assertions.assertEquals(tmpConsiderImplicitHydrogens, tmpMaxAtomCountFilter.isConsiderImplicitHydrogens());
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
                    new MaxAtomCountFilter(tmpMaxAtomCount, tmpConsiderImplicitHydrogens);
                }
        );
    }

    /**
     * Tests whether method .getsFiltered() of class MaxAtomCountFilter returns a boolean value.
     */
    @Test
    public void getsFilteredMethodTest_returnsBoolean() {
        IAtomContainer tmpAtomContainer = new AtomContainer();
        int tmpMaxAtomCount = 0;
        boolean tmpConsiderImplicitHydrogens = true;
        Filter tmpFilter = new MaxAtomCountFilter(tmpMaxAtomCount, tmpConsiderImplicitHydrogens);
        Assertions.assertInstanceOf(Boolean.class, tmpFilter.getsFiltered(tmpAtomContainer));
    }

    /**
     * Tests whether method .getsFiltered() of class MaxAtomCountFilter returns false if an AC does not exceed the max
     * atom count considering implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void getsFilteredMethodTest_returnsFalse_considerImplicitHydrogens() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("CCO");   //9 atoms
        int tmpMaxAtomCount = 9;
        boolean tmpConsiderImplicitHydrogens = true;
        Filter tmpFilter = new MaxAtomCountFilter(tmpMaxAtomCount, tmpConsiderImplicitHydrogens);
        Assertions.assertFalse(tmpFilter.getsFiltered(tmpAtomContainer));
    }

    /**
     * Tests whether method .getsFiltered() of class MaxAtomCountFilter returns true if an AC exceeds the max atom
     * count considering implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void getsFilteredMethodTest_returnsTrue_considerImplicitHydrogens() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("CCO");   //9 atoms
        int tmpMaxAtomCount = 8;
        boolean tmpConsiderImplicitHydrogens = true;
        Filter tmpFilter = new MaxAtomCountFilter(tmpMaxAtomCount, tmpConsiderImplicitHydrogens);
        Assertions.assertTrue(tmpFilter.getsFiltered(tmpAtomContainer));
    }

    /**
     * Tests whether method .getsFiltered() of class MaxAtomCountFilter returns false if an AC does not exceed the max
     * atom count not considering implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void getsFilteredMethodTest_returnsFalse_notConsiderImplicitHydrogens() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("CCO");   //3 atoms
        int tmpMaxAtomCount = 3;
        boolean tmpConsiderImplicitHydrogens = false;
        Filter tmpFilter = new MaxAtomCountFilter(tmpMaxAtomCount, tmpConsiderImplicitHydrogens);
        Assertions.assertFalse(tmpFilter.getsFiltered(tmpAtomContainer));
    }

    /**
     * Tests whether method .getsFiltered() of class MaxAtomCountFilter returns true if an AC exceeds the max atom
     * count not considering implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void getsFilteredMethodTest_returnsTrue_notConsiderImplicitHydrogens() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("CCO");   //3 atoms
        int tmpMaxAtomCount = 2;
        boolean tmpConsiderImplicitHydrogens = false;
        Filter tmpFilter = new MaxAtomCountFilter(tmpMaxAtomCount, tmpConsiderImplicitHydrogens);
        Assertions.assertTrue(tmpFilter.getsFiltered(tmpAtomContainer));
    }

    /**
     * Tests whether the return value of the .filter() method is not null and an instance of IAtomContainerSet.
     */
    @Test
    public void filterMethodTest_returnsIAtomContainerSetNotNull() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        int tmpMaxAtomCount = 5;
        boolean tmpConsiderImplicitHydrogens = true;
        MaxAtomCountFilter tmpMaxAtomCountFilter = new MaxAtomCountFilter(tmpMaxAtomCount, tmpConsiderImplicitHydrogens);
        Object tmpReturnValue = tmpMaxAtomCountFilter.filter(tmpAtomContainerSet);
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
                "C=CC=C",   //10 (4)
                "c1ccccc1", //12 (6) - filtered
                "CCO"       // 9 (3)
        );
        int[] tmpNotFilteredArray = new int[]{0, 2};
        //
        int tmpMaxAtomCount = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        MaxAtomCountFilter tmpMaxAtomCountFilter = new MaxAtomCountFilter(tmpMaxAtomCount, tmpConsiderImplicitHydrogens);
        IAtomContainerSet tmpFilteredACSet = tmpMaxAtomCountFilter.filter(tmpAtomContainerSet);
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
                "c1ccccc1", //12 (6) - filtered
                "CCO",      // 9 (3)
                "C=CC=C"    //10 (4) - filtered
        );
        int[] tmpNotFilteredArray = new int[]{1};
        //
        int tmpMaxAtomCount = 3;
        boolean tmpConsiderImplicitHydrogens = false;
        MaxAtomCountFilter tmpMaxAtomCountFilter = new MaxAtomCountFilter(tmpMaxAtomCount, tmpConsiderImplicitHydrogens);
        IAtomContainerSet tmpFilteredACSet = tmpMaxAtomCountFilter.filter(tmpAtomContainerSet);
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
                    int tmpMaxAtomCount = 5;
                    boolean tmpConsiderImplicitHydrogens = true;
                    MaxAtomCountFilter tmpMaxAtomCountFilter = new MaxAtomCountFilter(tmpMaxAtomCount, tmpConsiderImplicitHydrogens);
                    tmpMaxAtomCountFilter.filter(null);
                }
        );
    }

    /**
     * Tests the getter of maxAtomCount whether it returns maxAtomCount.
     */
    @Test
    public void getMaxAtomCountMethodTest_returnsMaxAtomCount() {
        int tmpMaxAtomCount = 5;
        boolean tmpConsiderImplicitHydrogens = true;
        MaxAtomCountFilter tmpMaxAtomCountFilter = new MaxAtomCountFilter(tmpMaxAtomCount, tmpConsiderImplicitHydrogens);
        Assertions.assertSame(tmpMaxAtomCountFilter.maxAtomCount, tmpMaxAtomCountFilter.getMaxAtomCount());
    }

    /**
     * Tests the getter of considerImplicitHydrogens whether it returns considerImplicitHydrogens.
     */
    @Test
    public void isConsiderImplicitHydrogensMethodTest_returnsConsiderImplicitHydrogens() {
        int tmpMaxAtomCount = 5;
        boolean tmpConsiderImplicitHydrogens = true;
        MaxAtomCountFilter tmpMaxAtomCountFilter = new MaxAtomCountFilter(tmpMaxAtomCount, tmpConsiderImplicitHydrogens);
        Assertions.assertSame(tmpMaxAtomCountFilter.considerImplicitHydrogens, tmpMaxAtomCountFilter.isConsiderImplicitHydrogens());
    }

}
