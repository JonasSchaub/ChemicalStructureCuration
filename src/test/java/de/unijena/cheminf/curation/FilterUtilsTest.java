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

package de.unijena.cheminf.curation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 */
public class FilterUtilsTest {

    /**
     * Tests whether the .countAtoms() method of the class FilterUtils counts the number of atoms of benzene correct
     * considering implicit hydrogen.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void countAtomsTest_consideringImplicitHydrogen_12atoms() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        //
        boolean tmpConsiderImplicitHydrogen = true;
        int tmpAtomCount = FilterUtils.countAtoms(tmpAtomContainer, tmpConsiderImplicitHydrogen);
        Assertions.assertEquals(12, tmpAtomCount);
    }

    /**
     * Tests whether the .countAtoms() method of the class FilterUtils counts the number of atoms of benzene correct
     * not considering implicit hydrogen.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void countAtomsTest_notConsideringImplicitHydrogen_6atoms() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        //
        boolean tmpConsiderImplicitHydrogen = false;
        int tmpAtomCount = FilterUtils.countAtoms(tmpAtomContainer, tmpConsiderImplicitHydrogen);
        Assertions.assertEquals(6, tmpAtomCount);
    }

    /**
     * Tests whether the .countAtoms() method of the class FilterUtils counts the number of atoms of pyruvate correct
     * not considering implicit hydrogen.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void countAtomsTest_notConsideringImplicitHydrogen_5atoms() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("NCC(=O)O");
        //
        boolean tmpConsiderImplicitHydrogen = false;
        int tmpAtomCount = FilterUtils.countAtoms(tmpAtomContainer, tmpConsiderImplicitHydrogen);
        Assertions.assertEquals(5, tmpAtomCount);
    }

    /**
     * Tests whether the .countAtoms() method of the class FilterUtils counts the number of atoms of pyruvate correct
     * considering implicit hydrogen.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void countAtomsTest_consideringImplicitHydrogen_10atoms() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("NCC(=O)O");
        //
        boolean tmpConsiderImplicitHydrogen = true;
        int tmpAtomCount = FilterUtils.countAtoms(tmpAtomContainer, tmpConsiderImplicitHydrogen);
        Assertions.assertEquals(10, tmpAtomCount);
    }

    /**
     * Tests whether the .countImplicitHydrogen() method of the class FilterUtils returns 6 when given an AtomContainer
     * with 6 implicit hydrogen.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void countImplicitHydrogenTest_6Hydrogen() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        //
        Assertions.assertEquals(6, FilterUtils.countImplicitHydrogen(tmpAtomContainer));
    }

    /**
     * Tests whether the .countImplicitHydrogen() method of the class FilterUtils returns 6 when given an AtomContainer
     * with 6 implicit hydrogen.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void countImplicitHydrogenTest_5Hydrogen() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("NCC(=O)O");
        //
        Assertions.assertEquals(5, FilterUtils.countImplicitHydrogen(tmpAtomContainer));
    }

    @Test   //TODO: has never been failed
    public void countImplicitHydrogenTest_throwExceptionInCaseOfInvalidParameters_atomContainerIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    FilterUtils.countImplicitHydrogen(null);
                }
        );
    }

    @Test
    public void exceedsOrEqualsAtomCountTest_10AtomsThreshold_12AtomsConsideringImplicitHs() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        //
        int tmpAtomCountThreshold = 10;
        boolean tmpConsiderImplicitHydrogen = true;
        boolean tmpResult = FilterUtils.exceedsOrEqualsAtomCount(tmpAtomContainer, tmpAtomCountThreshold, tmpConsiderImplicitHydrogen);
        Assertions.assertTrue(tmpResult);
    }

    @Test
    public void exceedsOrEqualsAtomCountTest_15AtomsThreshold_12AtomsConsideringImplicitHs() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        //
        int tmpAtomCountThreshold = 15;
        boolean tmpConsiderImplicitHydrogen = true;
        boolean tmpResult = FilterUtils.exceedsOrEqualsAtomCount(tmpAtomContainer, tmpAtomCountThreshold, tmpConsiderImplicitHydrogen);
        Assertions.assertFalse(tmpResult);
    }

    @Test
    public void exceedsOrEqualsAtomCountTest_12AtomsThreshold_12AtomsConsideringImplicitHs() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        //
        int tmpAtomCountThreshold = 12;
        boolean tmpConsiderImplicitHydrogen = true;
        boolean tmpResult = FilterUtils.exceedsOrEqualsAtomCount(tmpAtomContainer, tmpAtomCountThreshold, tmpConsiderImplicitHydrogen);
        Assertions.assertTrue(tmpResult);
    }

    @Test
    public void exceedsOrEqualsAtomCountTest_10AtomsThreshold_6AtomsNotConsideringImplicitHs() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        //
        int tmpAtomCountThreshold = 10;
        boolean tmpConsiderImplicitHydrogen = false;
        boolean tmpResult = FilterUtils.exceedsOrEqualsAtomCount(tmpAtomContainer, tmpAtomCountThreshold, tmpConsiderImplicitHydrogen);
        Assertions.assertFalse(tmpResult);
    }

    @Test
    public void exceedsOrEqualsAtomCountTest_4AtomsThreshold_6AtomsNotConsideringImplicitHs() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        //
        int tmpAtomCountThreshold = 4;
        boolean tmpConsiderImplicitHydrogen = false;
        boolean tmpResult = FilterUtils.exceedsOrEqualsAtomCount(tmpAtomContainer, tmpAtomCountThreshold, tmpConsiderImplicitHydrogen);
        Assertions.assertTrue(tmpResult);
    }

    @Test   //TODO: has never been red (due to check in countAtoms()); I added the parameter check anyways
    public void exceedsOrEqualsAtomCountTest_throwExceptionInCaseOfInvalidParameters_atomContainerIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    FilterUtils.exceedsOrEqualsAtomCount(null, 10, true);
                }
        );
    }

    @Test
    public void exceedsOrEqualsAtomCountTest_throwExceptionInCaseOfInvalidParameters_thresholdIsNegative() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    FilterUtils.exceedsOrEqualsAtomCount(new AtomContainer(), -1, true);
                }
        );
    }

}
