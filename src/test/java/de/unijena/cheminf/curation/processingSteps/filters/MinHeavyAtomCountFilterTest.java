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
 * Test class for class MinHeavyAtomCountFilter.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public class MinHeavyAtomCountFilterTest {

    /**
     * Tests whether the public constructor initializes all class fields with the given parameters; test 1.
     */
    @Test
    public void publicConstructorTest_initializesClassVarsWithGivenParams_test1() {
        int tmpMinHeavyAtomCount = 5;
        boolean tmpConsiderPseudoAtoms = true;
        MinHeavyAtomCountFilter tmpMinHeavyAtomCountFilter = new MinHeavyAtomCountFilter(tmpMinHeavyAtomCount,
                tmpConsiderPseudoAtoms, TestUtils.getTestReporterInstance());
        Assertions.assertEquals(tmpMinHeavyAtomCount, tmpMinHeavyAtomCountFilter.heavyAtomCountThreshold);
    }

    /**
     * Tests whether the public constructor initializes all class fields with the given parameters; test 2.
     */
    @Test
    public void publicConstructorTest_initializesClassVarsWithGivenParams_test2() {
        int tmpMinHeavyAtomCount = 10;
        boolean tmpConsiderPseudoAtoms = true;
        MinHeavyAtomCountFilter tmpMinHeavyAtomCountFilter = new MinHeavyAtomCountFilter(tmpMinHeavyAtomCount,
                tmpConsiderPseudoAtoms, TestUtils.getTestReporterInstance());
        Assertions.assertEquals(tmpMinHeavyAtomCount, tmpMinHeavyAtomCountFilter.heavyAtomCountThreshold);
    }

    /**
     * Tests whether the public constructor throws an IllegalArgumentException if the given min heavy atom count is of
     * a negative value.
     */
    @Test
    public void publicConstructorTest_throwsIllegalArgumentExceptionIfMinHeavyAtomCountIsNegative() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    int tmpMinHeavyAtomCount = -1;
                    boolean tmpConsiderPseudoAtoms = true;
                    new MinHeavyAtomCountFilter(tmpMinHeavyAtomCount, tmpConsiderPseudoAtoms,
                            TestUtils.getTestReporterInstance());
                }
        );
    }

    /**
     * Tests whether method .isFiltered() of class MinHeavyAtomCountFilter returns true if an AC exceeds the
     * min heavy atom count; tested with multiple atom containers.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     * @throws Exception if something went wrong
     */
    @Test
    public void isFilteredMethodTest_returnsTrueIfGivenAtomContainerExceedsTheMinHeavyAtomCount()
            throws InvalidSmilesException, Exception {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "c1ccccc1", //6 atoms
                "C1CCCC1",  //5 atoms
                "CC(=O)O"   //4 atoms
        );
        int tmpMinHeavyAtomCount = 7;
        boolean tmpConsiderPseudoAtoms = true;
        IFilter tmpFilter = new MinHeavyAtomCountFilter(tmpMinHeavyAtomCount, tmpConsiderPseudoAtoms,
                TestUtils.getTestReporterInstance());
        for (IAtomContainer tmpAtomContainer :
                tmpAtomContainerSet.atomContainers()) {
            Assertions.assertTrue(tmpFilter.isFiltered(tmpAtomContainer));
        }
    }

    /**
     * Tests whether method .isFiltered() of class MinHeavyAtomCountFilter returns false if an AC falls short of the
     * min heavy atom count; tested with multiple atom containers.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     * @throws Exception if something went wrong
     */
    @Test
    public void isFilteredMethodTest_returnsFalseIfGivenAtomContainerFallsShortOfTheMinHeavyAtomCount()
            throws InvalidSmilesException, Exception {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "c1ccccc1", //6 atoms
                "C1CCCC1",  //5 atoms
                "CC(=O)O"   //4 atoms
        );
        int tmpMinHeavyAtomCount = 3;
        boolean tmpConsiderPseudoAtoms = true;
        IFilter tmpFilter = new MinHeavyAtomCountFilter(tmpMinHeavyAtomCount, tmpConsiderPseudoAtoms,
                TestUtils.getTestReporterInstance());
        for (IAtomContainer tmpAtomContainer :
                tmpAtomContainerSet.atomContainers()) {
            Assertions.assertFalse(tmpFilter.isFiltered(tmpAtomContainer));
        }
    }

    /**
     * Tests whether method .isFiltered() of class MinHeavyAtomCountFilter returns false if an AC equals the min heavy
     * atom count; tested with multiple atom containers.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     * @throws Exception if something went wrong
     */
    @Test
    public void isFilteredMethodTest_returnsFalseIfGivenAtomContainerEqualsTheMinHeavyAtomCount()
            throws InvalidSmilesException, Exception {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "c1ccccc1", //6 atoms
                "C1CCCC1",  //5 atoms
                "CC(=O)O"   //4 atoms
        );
        int[] tmpMinHeavyAtomCountArray = new int[]{6, 5, 4};
        boolean tmpConsiderPseudoAtoms = true;
        IFilter tmpFilter;
        for (int i = 0; i < tmpAtomContainerSet.getAtomContainerCount(); i++) {
            tmpFilter = new MinHeavyAtomCountFilter(tmpMinHeavyAtomCountArray[i], tmpConsiderPseudoAtoms,
                    TestUtils.getTestReporterInstance());
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
                    int tmpMinHeavyAtomCount = 5;
                    boolean tmpConsiderPseudoAtoms = true;
                    IFilter tmpMinHeavyAtomCountFilter = new MinHeavyAtomCountFilter(tmpMinHeavyAtomCount,
                            tmpConsiderPseudoAtoms, TestUtils.getTestReporterInstance());
                    tmpMinHeavyAtomCountFilter.isFiltered(null);
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
        int tmpMinHeavyAtomCount = 5;
        boolean tmpConsiderPseudoAtoms = true;
        IFilter tmpMinHeavyAtomCountFilter = new MinHeavyAtomCountFilter(tmpMinHeavyAtomCount,
                tmpConsiderPseudoAtoms, TestUtils.getTestReporterInstance());
        Object tmpReturnValue = tmpMinHeavyAtomCountFilter.process(tmpAtomContainerSet, false);
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
                "CCO",      //3 - filtered
                "c1ccccc1"  //6
        );
        int[] tmpNotFilteredArray = new int[]{0, 2};
        //
        int tmpMinHeavyAtomCount = 4;
        boolean tmpConsiderPseudoAtoms = true;
        IFilter tmpMinHeavyAtomCountFilter = new MinHeavyAtomCountFilter(tmpMinHeavyAtomCount,
                tmpConsiderPseudoAtoms, TestUtils.getTestReporterInstance());
        IAtomContainerSet tmpFilteredACSet = tmpMinHeavyAtomCountFilter.process(tmpAtomContainerSet, false);
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
                "CCO",      //3 - filtered
                "c1ccccc1", //6
                "C=CC=C"    //4 - filtered
        );
        int[] tmpNotFilteredArray = new int[]{1};
        //
        int tmpMinHeavyAtomCount = 6;
        boolean tmpConsiderPseudoAtoms = true;
        IFilter tmpMinHeavyAtomCountFilter = new MinHeavyAtomCountFilter(tmpMinHeavyAtomCount,
                tmpConsiderPseudoAtoms, TestUtils.getTestReporterInstance());
        IAtomContainerSet tmpFilteredACSet = tmpMinHeavyAtomCountFilter.process(tmpAtomContainerSet, false);
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
                    int tmpMinHeavyAtomCount = 5;
                    boolean tmpConsiderPseudoAtoms = true;
                    IFilter tmpMinHeavyAtomCountFilter = new MinHeavyAtomCountFilter(tmpMinHeavyAtomCount,
                            tmpConsiderPseudoAtoms, TestUtils.getTestReporterInstance());
                    tmpMinHeavyAtomCountFilter.process(null, false);
                }
        );
    }

}
