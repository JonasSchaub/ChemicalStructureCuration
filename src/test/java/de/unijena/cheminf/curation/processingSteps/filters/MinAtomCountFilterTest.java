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
 * Test class for class MinAtomCountFilter.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public class MinAtomCountFilterTest {

    /**
     * Tests whether the public constructor initializes all class fields with the given parameters; test 1.
     */
    @Test
    public void publicConstructorTest_initializesClassVarsWithGivenParams_test1() {
        int tmpMinAtomCount = 5;
        boolean tmpConsiderImplicitHydrogens = true;
        boolean tmpConsiderPseudoAtoms = true;
        MinAtomCountFilter tmpMinAtomCountFilter = new MinAtomCountFilter(tmpMinAtomCount, tmpConsiderImplicitHydrogens,
                tmpConsiderPseudoAtoms, TestUtils.getDefaultReporterInstance());
        Assertions.assertEquals(tmpMinAtomCount, tmpMinAtomCountFilter.atomCountThreshold);
        Assertions.assertEquals(tmpConsiderImplicitHydrogens, tmpMinAtomCountFilter.considerImplicitHydrogens);
    }

    /**
     * Tests whether the public constructor initializes all class fields with the given parameters; test 2.
     */
    @Test
    public void publicConstructorTest_initializesClassVarsWithGivenParams_test2() {
        int tmpMinAtomCount = 10;
        boolean tmpConsiderImplicitHydrogens = false;
        boolean tmpConsiderPseudoAtoms = true;
        MinAtomCountFilter tmpMinAtomCountFilter = new MinAtomCountFilter(tmpMinAtomCount, tmpConsiderImplicitHydrogens,
                tmpConsiderPseudoAtoms, TestUtils.getDefaultReporterInstance());
        Assertions.assertEquals(tmpMinAtomCount, tmpMinAtomCountFilter.atomCountThreshold);
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
                    boolean tmpConsiderPseudoAtoms = true;
                    new MinAtomCountFilter(tmpMinAtomCount, tmpConsiderImplicitHydrogens,
                            tmpConsiderPseudoAtoms, TestUtils.getDefaultReporterInstance());
                }
        );
    }

    /**
     * Tests whether method .isFiltered() of class MinAtomCountFilter returns false if an AC exceeds the min atom
     * count considering implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     * @throws Exception if something went wrong
     */
    @Test
    public void isFilteredMethodTest_returnsFalse_considerImplicitHydrogens() throws InvalidSmilesException, Exception {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("CCO");   //9 atoms
        int tmpMinAtomCount = 9;
        boolean tmpConsiderImplicitHydrogens = true;
        boolean tmpConsiderPseudoAtoms = true;
        IFilter tmpFilter = new MinAtomCountFilter(tmpMinAtomCount, tmpConsiderImplicitHydrogens,
                tmpConsiderPseudoAtoms, TestUtils.getDefaultReporterInstance());
        Assertions.assertFalse(tmpFilter.isFiltered(tmpAtomContainer));
    }

    /**
     * Tests whether method .isFiltered() of class MinAtomCountFilter returns true if an AC does not exceed the min
     * atom count considering implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     * @throws Exception if something went wrong
     */
    @Test
    public void isFilteredMethodTest_returnsTrue_considerImplicitHydrogens() throws InvalidSmilesException, Exception {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("CCO");   //9 atoms
        int tmpMinAtomCount = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        boolean tmpConsiderPseudoAtoms = true;
        IFilter tmpFilter = new MinAtomCountFilter(tmpMinAtomCount, tmpConsiderImplicitHydrogens,
                tmpConsiderPseudoAtoms, TestUtils.getDefaultReporterInstance());
        Assertions.assertTrue(tmpFilter.isFiltered(tmpAtomContainer));
    }

    /**
     * Tests whether method .isFiltered() of class MinAtomCountFilter returns false if an AC exceeds the min atom
     * count not considering implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     * @throws Exception if something went wrong
     */
    @Test
    public void isFilteredMethodTest_returnsFalse_notConsiderImplicitHydrogens() throws InvalidSmilesException, Exception {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("CCO");   //3 atoms
        int tmpMinAtomCount = 3;
        boolean tmpConsiderImplicitHydrogens = false;
        boolean tmpConsiderPseudoAtoms = true;
        IFilter tmpFilter = new MinAtomCountFilter(tmpMinAtomCount, tmpConsiderImplicitHydrogens,
                tmpConsiderPseudoAtoms, TestUtils.getDefaultReporterInstance());
        Assertions.assertFalse(tmpFilter.isFiltered(tmpAtomContainer));
    }

    /**
     * Tests whether method .isFiltered() of class MinAtomCountFilter returns true if an AC does not exceed the min
     * atom count not considering implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     * @throws Exception if something went wrong
     */
    @Test
    public void isFilteredMethodTest_returnsTrue_notConsiderImplicitHydrogens() throws InvalidSmilesException, Exception {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("CCO");   //3 atoms
        int tmpMinAtomCount = 4;
        boolean tmpConsiderImplicitHydrogens = false;
        boolean tmpConsiderPseudoAtoms = true;
        IFilter tmpFilter = new MinAtomCountFilter(tmpMinAtomCount, tmpConsiderImplicitHydrogens,
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
                    int tmpMinAtomCount = 5;
                    boolean tmpConsiderImplicitHydrogens = true;
                    boolean tmpConsiderPseudoAtoms = true;
                    IFilter tmpMinAtomCountFilter = new MinAtomCountFilter(tmpMinAtomCount, tmpConsiderImplicitHydrogens,
                            tmpConsiderPseudoAtoms, TestUtils.getDefaultReporterInstance());
                    tmpMinAtomCountFilter.isFiltered(null);
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
        int tmpMinAtomCount = 5;
        boolean tmpConsiderImplicitHydrogens = true;
        boolean tmpConsiderPseudoAtoms = true;
        IFilter tmpMinAtomCountFilter = new MinAtomCountFilter(tmpMinAtomCount, tmpConsiderImplicitHydrogens,
                tmpConsiderPseudoAtoms, TestUtils.getDefaultReporterInstance());
        Object tmpReturnValue = tmpMinAtomCountFilter.process(tmpAtomContainerSet, false);
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
                "C=CC=C",   //10 (4) - filtered
                "c1ccccc1", //12 (6)
                "CCO"       // 9 (3) - filtered
        );
        int[] tmpNotFilteredArray = new int[]{1};
        //
        int tmpMinAtomCount = 11;
        boolean tmpConsiderImplicitHydrogens = true;
        boolean tmpConsiderPseudoAtoms = true;
        IFilter tmpMinAtomCountFilter = new MinAtomCountFilter(tmpMinAtomCount, tmpConsiderImplicitHydrogens,
                tmpConsiderPseudoAtoms, TestUtils.getDefaultReporterInstance());
        IAtomContainerSet tmpFilteredACSet = tmpMinAtomCountFilter.process(tmpAtomContainerSet, false);
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
                "CCO",      // 9 (3) - filtered
                "c1ccccc1", //12 (6)
                "C=CC=C"    //10 (4)
        );
        int[] tmpNotFilteredArray = new int[]{1, 2};
        //
        int tmpMinAtomCount = 4;
        boolean tmpConsiderImplicitHydrogens = false;
        boolean tmpConsiderPseudoAtoms = true;
        IFilter tmpMinAtomCountFilter = new MinAtomCountFilter(tmpMinAtomCount, tmpConsiderImplicitHydrogens,
                tmpConsiderPseudoAtoms, TestUtils.getDefaultReporterInstance());
        IAtomContainerSet tmpFilteredACSet = tmpMinAtomCountFilter.process(tmpAtomContainerSet, false);
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
                    boolean tmpConsiderPseudoAtoms = true;
                    IFilter tmpMinAtomCountFilter = new MinAtomCountFilter(tmpMinAtomCount, tmpConsiderImplicitHydrogens,
                            tmpConsiderPseudoAtoms, TestUtils.getDefaultReporterInstance());
                    tmpMinAtomCountFilter.process(null, false);
                }
        );
    }

}
