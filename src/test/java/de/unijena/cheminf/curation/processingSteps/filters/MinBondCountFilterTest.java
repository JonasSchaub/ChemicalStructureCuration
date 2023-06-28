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
 * Test class for class MinBondCountFilter.
 */
public class MinBondCountFilterTest {

    /**
     * Tests whether the public constructor initializes all class fields with the given parameters; test 1.
     */
    @Test
    public void publicConstructorTest_initializesClassVarsWithGivenParams_test1() {
        int tmpMinBondCount = 5;
        boolean tmpConsiderImplicitHydrogens = true;
        MinBondCountFilter tmpMinBondCountFilter = new MinBondCountFilter(tmpMinBondCount, tmpConsiderImplicitHydrogens);
        Assertions.assertEquals(tmpMinBondCount, tmpMinBondCountFilter.minBondCount);
        Assertions.assertEquals(tmpConsiderImplicitHydrogens, tmpMinBondCountFilter.considerImplicitHydrogens);
    }

    /**
     * Tests whether the public constructor initializes all class fields with the given parameters; test 2.
     */
    @Test
    public void publicConstructorTest_initializesClassVarsWithGivenParams_test2() {
        int tmpMinBondCount = 10;
        boolean tmpConsiderImplicitHydrogens = false;
        MinBondCountFilter tmpMinBondCountFilter = new MinBondCountFilter(tmpMinBondCount, tmpConsiderImplicitHydrogens);
        Assertions.assertEquals(tmpMinBondCount, tmpMinBondCountFilter.minBondCount);
        Assertions.assertEquals(tmpConsiderImplicitHydrogens, tmpMinBondCountFilter.considerImplicitHydrogens);
    }

    /**
     * Tests whether the public constructor throws an IllegalArgumentException if the given min bond count is of a
     * negative value.
     */
    @Test
    public void publicConstructorTest_throwsIllegalArgumentExceptionIfMinBondCountIsNegative() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    int tmpMinBondCount = -1;
                    boolean tmpConsiderImplicitHydrogens = true;
                    new MinBondCountFilter(tmpMinBondCount, tmpConsiderImplicitHydrogens);
                }
        );
    }

    /**
     * Tests whether method .isFiltered() of class MinBondCountFilter returns a boolean value.
     */
    @Test
    public void isFilteredMethodTest_returnsBoolean() {
        IAtomContainer tmpAtomContainer = new AtomContainer();
        int tmpMinBondCount = 0;
        boolean tmpConsiderImplicitHydrogens = true;
        IFilter tmpFilter = new MinBondCountFilter(tmpMinBondCount, tmpConsiderImplicitHydrogens);
        Assertions.assertInstanceOf(Boolean.class, tmpFilter.isFiltered(tmpAtomContainer));
    }

    /**
     * Tests whether method .isFiltered() of class MinBondCountFilter returns false if an AC exceeds the min bond
     * count considering bonds to implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void isFilteredMethodTest_returnsFalse_considerImplicitHydrogens() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("C=CC=C");   //9 bonds
        int tmpMinBondCount = 9;
        boolean tmpConsiderImplicitHydrogens = true;
        IFilter tmpFilter = new MinBondCountFilter(tmpMinBondCount, tmpConsiderImplicitHydrogens);
        Assertions.assertFalse(tmpFilter.isFiltered(tmpAtomContainer));
    }

    /**
     * Tests whether method .isFiltered() of class MinBondCountFilter returns true if an AC does not exceed the min
     * bond count considering bonds to implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void isFilteredMethodTest_returnsTrue_considerImplicitHydrogens() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("C=CC=C");   //9 bonds
        int tmpMinBondCount = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        IFilter tmpFilter = new MinBondCountFilter(tmpMinBondCount, tmpConsiderImplicitHydrogens);
        Assertions.assertTrue(tmpFilter.isFiltered(tmpAtomContainer));
    }

    /**
     * Tests whether method .isFiltered() of class MinBondCountFilter returns false if an AC exceeds the min bond
     * count not considering bonds to implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void isFilteredMethodTest_returnsFalse_notConsiderImplicitHydrogens() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("C=CC=C");   //3 bonds
        int tmpMinBondCount = 3;
        boolean tmpConsiderImplicitHydrogens = false;
        IFilter tmpFilter = new MinBondCountFilter(tmpMinBondCount, tmpConsiderImplicitHydrogens);
        Assertions.assertFalse(tmpFilter.isFiltered(tmpAtomContainer));
    }

    /**
     * Tests whether method .isFiltered() of class MinBondCountFilter returns true if an AC does not exceed the min
     * bond count not considering bonds to implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void isFilteredMethodTest_returnsTrue_notConsiderImplicitHydrogens() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("C=CC=C");   //3 bonds
        int tmpMinBondCount = 4;
        boolean tmpConsiderImplicitHydrogens = false;
        IFilter tmpFilter = new MinBondCountFilter(tmpMinBondCount, tmpConsiderImplicitHydrogens);
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
                    int tmpMinBondCount = 5;
                    boolean tmpConsiderImplicitHydrogens = true;
                    IFilter tmpMinBondCountFilter = new MinBondCountFilter(tmpMinBondCount, tmpConsiderImplicitHydrogens);
                    tmpMinBondCountFilter.isFiltered(null);
                }
        );
    }

    /**
     * Tests whether the return value of the .filter() method is not null and an instance of IAtomContainerSet.
     */
    @Test
    public void filterMethodTest_returnsIAtomContainerSetNotNull() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        int tmpMinBondCount = 5;
        boolean tmpConsiderImplicitHydrogens = true;
        IFilter tmpMinBondCountFilter = new MinBondCountFilter(tmpMinBondCount, tmpConsiderImplicitHydrogens);
        Object tmpReturnValue = tmpMinBondCountFilter.process(tmpAtomContainerSet, false, true);
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
                "C=CC=C",   // 9 (3) - filtered
                "c1ccccc1", //12 (6)
                "CCO"       // 8 (2) - filtered
        );
        int[] tmpNotFilteredArray = new int[]{1};
        //
        int tmpMinBondCount = 11;
        boolean tmpConsiderImplicitHydrogens = true;
        IFilter tmpMinBondCountFilter = new MinBondCountFilter(tmpMinBondCount, tmpConsiderImplicitHydrogens);
        IAtomContainerSet tmpFilteredACSet = tmpMinBondCountFilter.process(tmpAtomContainerSet, false, true);
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
                "CCO",      // 8 (2) - filtered
                "c1ccccc1", //12 (6)
                "C=CC=C"    // 9 (3)
        );
        int[] tmpNotFilteredArray = new int[]{1, 2};
        //
        int tmpMinBondCount = 3;
        boolean tmpConsiderImplicitHydrogens = false;
        IFilter tmpMinBondCountFilter = new MinBondCountFilter(tmpMinBondCount, tmpConsiderImplicitHydrogens);
        IAtomContainerSet tmpFilteredACSet = tmpMinBondCountFilter.process(tmpAtomContainerSet, false, true);
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
                    int tmpMinBondCount = 5;
                    boolean tmpConsiderImplicitHydrogens = true;
                    IFilter tmpMinBondCountFilter = new MinBondCountFilter(tmpMinBondCount, tmpConsiderImplicitHydrogens);
                    tmpMinBondCountFilter.process(null, false, true);
                }
        );
    }

}
