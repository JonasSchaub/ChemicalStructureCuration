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
 * Test class for class MinAtomCountFilter.
 */
public class MinAtomCountFilterTest {

    /**
     * Tests whether the public constructor initializes all class fields with the given parameters; test 1.
     */
    @Test
    public void publicConstructorTest_initializesClassVarsWithGivenParams_test1() {
        int tmpMinAtomCount = 5;
        boolean tmpConsiderImplicitHydrogens = true;
        MinAtomCountFilter tmpMinAtomCountFilter = new MinAtomCountFilter(tmpMinAtomCount, tmpConsiderImplicitHydrogens);
        Assertions.assertEquals(tmpMinAtomCount, tmpMinAtomCountFilter.minAtomCount);
        Assertions.assertEquals(tmpConsiderImplicitHydrogens, tmpMinAtomCountFilter.considerImplicitHydrogens);
    }

    /**
     * Tests whether the public constructor initializes all class fields with the given parameters; test 2.
     */
    @Test
    public void publicConstructorTest_initializesClassVarsWithGivenParams_test2() {
        int tmpMinAtomCount = 10;
        boolean tmpConsiderImplicitHydrogens = false;
        MinAtomCountFilter tmpMinAtomCountFilter = new MinAtomCountFilter(tmpMinAtomCount, tmpConsiderImplicitHydrogens);
        Assertions.assertEquals(tmpMinAtomCount, tmpMinAtomCountFilter.minAtomCount);
        Assertions.assertEquals(tmpConsiderImplicitHydrogens, tmpMinAtomCountFilter.considerImplicitHydrogens);
    }

    /**
     * Tests whether the public constructor throws an IllegalArgumentException if the given min atom count is of a
     * negative value.
     */
    @Test
    public void publicConstructorTest_throwsIllegalArgumentExceptionIfMinAtomCountIsNegative() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    int tmpMinAtomCount = -1;
                    boolean tmpConsiderImplicitHydrogens = true;
                    new MinAtomCountFilter(tmpMinAtomCount, tmpConsiderImplicitHydrogens);
                }
        );
    }

    /**
     * Tests whether method .isFiltered() of class MinAtomCountFilter returns a boolean value.
     */
    @Test
    public void isFilteredMethodTest_returnsBoolean() {
        IAtomContainer tmpAtomContainer = new AtomContainer();
        int tmpMinAtomCount = 0;
        boolean tmpConsiderImplicitHydrogens = true;
        IFilter tmpFilter = new MinAtomCountFilter(tmpMinAtomCount, tmpConsiderImplicitHydrogens);
        Assertions.assertInstanceOf(Boolean.class, tmpFilter.isFiltered(tmpAtomContainer));
    }

    /**
     * Tests whether method .isFiltered() of class MinAtomCountFilter returns false if an AC exceeds the min atom
     * count considering implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void isFilteredMethodTest_returnsFalse_considerImplicitHydrogens() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("CCO");   //9 atoms
        int tmpMinAtomCount = 9;
        boolean tmpConsiderImplicitHydrogens = true;
        IFilter tmpFilter = new MinAtomCountFilter(tmpMinAtomCount, tmpConsiderImplicitHydrogens);
        Assertions.assertFalse(tmpFilter.isFiltered(tmpAtomContainer));
    }

    /**
     * Tests whether method .isFiltered() of class MinAtomCountFilter returns true if an AC does not exceed the min
     * atom count considering implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void isFilteredMethodTest_returnsTrue_considerImplicitHydrogens() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("CCO");   //9 atoms
        int tmpMinAtomCount = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        IFilter tmpFilter = new MinAtomCountFilter(tmpMinAtomCount, tmpConsiderImplicitHydrogens);
        Assertions.assertTrue(tmpFilter.isFiltered(tmpAtomContainer));
    }

    /**
     * Tests whether method .isFiltered() of class MinAtomCountFilter returns false if an AC exceeds the min atom
     * count not considering implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void isFilteredMethodTest_returnsFalse_notConsiderImplicitHydrogens() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("CCO");   //3 atoms
        int tmpMinAtomCount = 3;
        boolean tmpConsiderImplicitHydrogens = false;
        IFilter tmpFilter = new MinAtomCountFilter(tmpMinAtomCount, tmpConsiderImplicitHydrogens);
        Assertions.assertFalse(tmpFilter.isFiltered(tmpAtomContainer));
    }

    /**
     * Tests whether method .isFiltered() of class MinAtomCountFilter returns true if an AC does not exceed the min
     * atom count not considering implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void isFilteredMethodTest_returnsTrue_notConsiderImplicitHydrogens() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("CCO");   //3 atoms
        int tmpMinAtomCount = 4;
        boolean tmpConsiderImplicitHydrogens = false;
        IFilter tmpFilter = new MinAtomCountFilter(tmpMinAtomCount, tmpConsiderImplicitHydrogens);
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
                    int tmpMinAtomCount = 5;
                    boolean tmpConsiderImplicitHydrogens = true;
                    IFilter tmpMinAtomCountFilter = new MinAtomCountFilter(tmpMinAtomCount, tmpConsiderImplicitHydrogens);
                    tmpMinAtomCountFilter.isFiltered(null);
                }
        );
    }

    /**
     * Tests whether the return value of the .filter() method is not null and an instance of IAtomContainerSet.
     */
    @Test
    public void filterMethodTest_returnsIAtomContainerSetNotNull() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        int tmpMinAtomCount = 5;
        boolean tmpConsiderImplicitHydrogens = true;
        IFilter tmpMinAtomCountFilter = new MinAtomCountFilter(tmpMinAtomCount, tmpConsiderImplicitHydrogens);
        Object tmpReturnValue = tmpMinAtomCountFilter.process(tmpAtomContainerSet, false, true);
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
                "C=CC=C",   //10 (4) - filtered
                "c1ccccc1", //12 (6)
                "CCO"       // 9 (3) - filtered
        );
        int[] tmpNotFilteredArray = new int[]{1};
        //
        int tmpMinAtomCount = 11;
        boolean tmpConsiderImplicitHydrogens = true;
        IFilter tmpMinAtomCountFilter = new MinAtomCountFilter(tmpMinAtomCount, tmpConsiderImplicitHydrogens);
        IAtomContainerSet tmpFilteredACSet = tmpMinAtomCountFilter.process(tmpAtomContainerSet, false, true);
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
                "CCO",      // 9 (3) - filtered
                "c1ccccc1", //12 (6)
                "C=CC=C"    //10 (4)
        );
        int[] tmpNotFilteredArray = new int[]{1, 2};
        //
        int tmpMinAtomCount = 4;
        boolean tmpConsiderImplicitHydrogens = false;
        IFilter tmpMinAtomCountFilter = new MinAtomCountFilter(tmpMinAtomCount, tmpConsiderImplicitHydrogens);
        IAtomContainerSet tmpFilteredACSet = tmpMinAtomCountFilter.process(tmpAtomContainerSet, false, true);
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
                    int tmpMinAtomCount = 5;
                    boolean tmpConsiderImplicitHydrogens = true;
                    IFilter tmpMinAtomCountFilter = new MinAtomCountFilter(tmpMinAtomCount, tmpConsiderImplicitHydrogens);
                    tmpMinAtomCountFilter.process(null, false, true);
                }
        );
    }

}
