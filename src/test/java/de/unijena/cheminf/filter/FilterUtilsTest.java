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
 * Test class for methods of class FilterUtils.
 */
public class FilterUtilsTest {

    /**
     * Tests whether the .countAtoms() method of the class FilterUtils returns an integer value.
     */
    @Test
    public void countAtomsTest_returnsIntegerValue() {
        IAtomContainer tmpAtomContainer = new AtomContainer();
        boolean tmpConsiderImplicitHydrogens = true;
        Assertions.assertInstanceOf(Integer.class, FilterUtils.countAtoms(tmpAtomContainer, tmpConsiderImplicitHydrogens));
    }

    /**
     * Tests whether the .countAtoms() method of the class FilterUtils counts the number of atoms of benzene correct
     * considering implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void countAtomsTest_consideringImplicitHydrogens_12atoms() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        //
        boolean tmpConsiderImplicitHydrogens = true;
        int tmpAtomCount = FilterUtils.countAtoms(tmpAtomContainer, tmpConsiderImplicitHydrogens);
        Assertions.assertEquals(12, tmpAtomCount);
    }

    /**
     * Tests whether the .countAtoms() method of the class FilterUtils counts the number of atoms of benzene correct
     * not considering implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void countAtomsTest_notConsideringImplicitHydrogens_6atoms() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        //
        boolean tmpConsiderImplicitHydrogens = false;
        int tmpAtomCount = FilterUtils.countAtoms(tmpAtomContainer, tmpConsiderImplicitHydrogens);
        Assertions.assertEquals(6, tmpAtomCount);
    }

    /**
     * Tests whether the .countAtoms() method of the class FilterUtils counts the number of atoms of glycine correct
     * not considering implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void countAtomsTest_notConsideringImplicitHydrogens_5atoms() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("NCC(=O)O");
        //
        boolean tmpConsiderImplicitHydrogens = false;
        int tmpAtomCount = FilterUtils.countAtoms(tmpAtomContainer, tmpConsiderImplicitHydrogens);
        Assertions.assertEquals(5, tmpAtomCount);
    }

    /**
     * Tests whether the .countAtoms() method of the class FilterUtils counts the number of atoms of glycine correct
     * considering implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void countAtomsTest_consideringImplicitHydrogens_10atoms() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("NCC(=O)O");
        //
        boolean tmpConsiderImplicitHydrogens = true;
        int tmpAtomCount = FilterUtils.countAtoms(tmpAtomContainer, tmpConsiderImplicitHydrogens);
        Assertions.assertEquals(10, tmpAtomCount);
    }

    /**
     * Tests whether the .countAtoms() method of the class FilterUtils throws a NullPointerException if the given
     * IAtomContainer instance is null.
     */
    @Test
    public void countAtomsTest_throwsNullPointerExceptionIfGivenAtomContainerIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    FilterUtils.countAtoms(null, true);
                }
        );
    }

    /**
     * Tests whether the .countImplicitHydrogens() method of the class FilterUtils returns an integer value.
     */
    @Test
    public void countImplicitHydrogensTest_returnsIntegerValue() {
        IAtomContainer tmpAtomContainer = new AtomContainer();
        Assertions.assertInstanceOf(Integer.class, FilterUtils.countImplicitHydrogens(tmpAtomContainer));
    }

    /**
     * Tests whether the .countImplicitHydrogens() method of the class FilterUtils returns 6 when given an AtomContainer
     * with 6 implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void countImplicitHydrogensTest_6Hydrogen() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        //
        Assertions.assertEquals(6, FilterUtils.countImplicitHydrogens(tmpAtomContainer));
    }

    /**
     * Tests whether the .countImplicitHydrogens() method of the class FilterUtils returns 6 when given an AtomContainer
     * with 6 implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void countImplicitHydrogensTest_5Hydrogen() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("NCC(=O)O");
        //
        Assertions.assertEquals(5, FilterUtils.countImplicitHydrogens(tmpAtomContainer));
    }

    /**
     * Tests whether the .countImplicitHydrogens() method of the class FilterUtils throws a NullPointerException if the
     * given IAtomContainer instance is null.
     */
    @Test
    public void countImplicitHydrogensTest_throwsNullPointerExceptionIfGivenAtomContainerIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    FilterUtils.countImplicitHydrogens(null);
                }
        );
    }

    /**
     * Tests whether the .exceedsOrEqualsAtomCount() method of class FilterUtils returns a boolean value.
     */
    @Test
    public void exceedsOrEqualsAtomCountTest_returnsBooleanValue() {
        IAtomContainer tmpAtomContainer = new AtomContainer();
        int tmpAtomCountThresholdValue = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        Assertions.assertInstanceOf(
                Boolean.class,
                FilterUtils.exceedsOrEqualsAtomCount(tmpAtomContainer, tmpAtomCountThresholdValue, tmpConsiderImplicitHydrogens)
        );
    }

    /**
     * Tests whether the .exceedsOrEqualsAtomCount() method of class FilterUtils returns true if given atom container
     * exceeds the given threshold considering implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void exceedsOrEqualsAtomCountTest_returnsTrue_10AtomsThresholdConsideringImplicitHs_12Atoms() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        //
        int tmpAtomCountThresholdValue = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        boolean tmpResult = FilterUtils.exceedsOrEqualsAtomCount(
                tmpAtomContainer, tmpAtomCountThresholdValue, tmpConsiderImplicitHydrogens
        );
        Assertions.assertTrue(tmpResult);
    }

    /**
     * Tests whether the .exceedsOrEqualsAtomCount() method of class FilterUtils returns false if given atom container
     * does not exceed the given threshold considering implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void exceedsOrEqualsAtomCountTest_returnsFalse_15AtomsThresholdConsideringImplicitHs_12Atoms() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        //
        int tmpAtomCountThresholdValue = 15;
        boolean tmpConsiderImplicitHydrogens = true;
        boolean tmpResult = FilterUtils.exceedsOrEqualsAtomCount(tmpAtomContainer, tmpAtomCountThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertFalse(tmpResult);
    }

    /**
     * Tests whether the .exceedsOrEqualsAtomCount() method of class FilterUtils returns true if given atom container
     * equals the given threshold considering implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void exceedsOrEqualsAtomCountTest_returnsTrue_12AtomsThresholdConsideringImplicitHs_12Atoms() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        //
        int tmpAtomCountThresholdValue = 12;
        boolean tmpConsiderImplicitHydrogens = true;
        boolean tmpResult = FilterUtils.exceedsOrEqualsAtomCount(tmpAtomContainer, tmpAtomCountThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertTrue(tmpResult);
    }

    /**
     * Tests whether the .exceedsOrEqualsAtomCount() method of class FilterUtils returns true if given atom container
     * exceeds the given threshold not considering implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void exceedsOrEqualsAtomCountTest_returnsTrue_4AtomsThresholdNotConsideringImplicitHs_6Atoms() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        //
        int tmpAtomCountThresholdValue = 4;
        boolean tmpConsiderImplicitHydrogens = false;
        boolean tmpResult = FilterUtils.exceedsOrEqualsAtomCount(tmpAtomContainer, tmpAtomCountThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertTrue(tmpResult);
    }

    /**
     * Tests whether the .exceedsOrEqualsAtomCount() method of class FilterUtils returns true if given atom container
     * does not exceed the given threshold not considering implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void exceedsOrEqualsAtomCountTest_returnsFalse_10AtomsThresholdNotConsideringImplicitHs_6Atoms() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        //
        int tmpAtomCountThresholdValue = 10;
        boolean tmpConsiderImplicitHydrogens = false;
        boolean tmpResult = FilterUtils.exceedsOrEqualsAtomCount(tmpAtomContainer, tmpAtomCountThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertFalse(tmpResult);
    }

    /**
     * Tests whether the .exceedsOrEqualsAtomCount() method of class FilterUtils returns true if given atom container
     * equals the given threshold not considering implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void exceedsOrEqualsAtomCountTest_returnsTrue_6AtomsThresholdNotConsideringImplicitHs_6Atoms() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        //
        int tmpAtomCountThresholdValue = 6;
        boolean tmpConsiderImplicitHydrogens = false;
        boolean tmpResult = FilterUtils.exceedsOrEqualsAtomCount(tmpAtomContainer, tmpAtomCountThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertTrue(tmpResult);
    }

    /**
     * Tests whether the .exceedsOrEqualsAtomCount() method of class FilterUtils throws a NullPointerException if the
     * given IAtomContainer instance is null.
     */
    @Test
    public void exceedsOrEqualsAtomCountTest_throwsNullPointerExceptionIfGivenAtomContainerIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    FilterUtils.exceedsOrEqualsAtomCount(null, 10, true);
                }
        );
    }

    /**
     * Tests whether the .exceedsOrEqualsAtomCount() method of class FilterUtils throws an IllegalArgumentException if
     * the given threshold (integer value) is of a negative value.
     */
    @Test
    public void exceedsOrEqualsAtomCountTest_throwsIllegalArgumentExceptionIfGivenThresholdValueIsNegative() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    FilterUtils.exceedsOrEqualsAtomCount(new AtomContainer(), -1, true);
                }
        );
    }

    /**
     * Tests whether the .countBonds() method of class FilterUtils returns an integer value.
     */
    @Test
    public void countBondsMethodTest_returnsIntegerValue() {
        IAtomContainer tmpAtomContainer = new AtomContainer();
        boolean tmpConsiderImplicitHydrogens = true;
        Assertions.assertInstanceOf(Integer.class, FilterUtils.countBonds(tmpAtomContainer, tmpConsiderImplicitHydrogens));
    }

    /**
     * Tests whether the .countBonds() method of class FilterUtils returns the correct bond count not considering
     * implicit hydrogen atoms; test 1.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void countBondsMethodTest_notConsideringImplicitHydrogens_test1_1Bond() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString(
                "CC"    //1
        );
        int tmpBondCount = 1;
        boolean tmpConsiderImplicitHydrogens = false;
        Assertions.assertEquals(tmpBondCount, FilterUtils.countBonds(tmpAtomContainer, tmpConsiderImplicitHydrogens));
    }

    /**
     * Tests whether the .countBonds() method of class FilterUtils returns the correct bond count not considering
     * implicit hydrogen atoms; test 2.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void countBondsMethodTest_notConsideringImplicitHydrogens_test2_5Bond() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString(
                "C1CCCC1"   //5
        );
        int tmpBondCount = 5;
        boolean tmpConsiderImplicitHydrogens = false;
        Assertions.assertEquals(tmpBondCount, FilterUtils.countBonds(tmpAtomContainer, tmpConsiderImplicitHydrogens));
    }

    /**
     * Tests whether the .countBonds() method of class FilterUtils returns the correct bond count not considering
     * implicit hydrogen atoms; test 3; four atom containers with different characteristics.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void countBondsMethodTest_notConsideringImplicitHydrogens_test3_4MolsWithDiffCharacteristics() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "CCO",  //2 - single bonds only
                "C=CC=C",   //3 - with double bonds
                "C#N",      //1 - with triple bond
                "c1ccccc1"  //6 - aromatic system
        );
        int[] tmpBondCountArray = new int[]{2, 3, 1, 6};
        boolean tmpConsiderImplicitHydrogens = false;
        IAtomContainer tmpAtomContainer;
        for (int i = 0; i < tmpAtomContainerSet.getAtomContainerCount(); i++) {
            tmpAtomContainer = tmpAtomContainerSet.getAtomContainer(i);
            Assertions.assertEquals(tmpBondCountArray[i], FilterUtils.countBonds(tmpAtomContainer, tmpConsiderImplicitHydrogens));
        }
    }

    /**
     * Tests whether the .countBonds() method of class FilterUtils returns the correct bond count considering implicit
     * hydrogen atoms; test 1.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void countBondsMethodTest_consideringImplicitHydrogens_test1_7Bond() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString(
                "CC"    //7
        );
        int tmpBondCount = 7;
        boolean tmpConsiderImplicitHydrogens = true;
        Assertions.assertEquals(tmpBondCount, FilterUtils.countBonds(tmpAtomContainer, tmpConsiderImplicitHydrogens));
    }

    /**
     * Tests whether the .countBonds() method of class FilterUtils returns the correct bond count considering implicit
     * hydrogen atoms; test 2.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void countBondsMethodTest_consideringImplicitHydrogens_test2_15Bond() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString(
                "C1CCCC1"   //15
        );
        int tmpBondCount = 15;
        boolean tmpConsiderImplicitHydrogens = true;
        Assertions.assertEquals(tmpBondCount, FilterUtils.countBonds(tmpAtomContainer, tmpConsiderImplicitHydrogens));
    }

    /**
     * Tests whether the .countBonds() method of class FilterUtils returns the correct bond count considering implicit
     * hydrogen atoms; test 3; four atom containers with different characteristics.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void countBondsMethodTest_consideringImplicitHydrogens_test3_4MolsWithDiffCharacteristics() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "CCO",  //8 - single bonds only
                "C=CC=C",   //9 - with double bonds
                "C#N",      //2 - with triple bond
                "c1ccccc1"  //12 - aromatic system
        );
        int[] tmpBondCountArray = new int[]{8, 9, 2, 12};
        boolean tmpConsiderImplicitHydrogens = true;
        IAtomContainer tmpAtomContainer;
        for (int i = 0; i < tmpAtomContainerSet.getAtomContainerCount(); i++) {
            tmpAtomContainer = tmpAtomContainerSet.getAtomContainer(i);
            Assertions.assertEquals(tmpBondCountArray[i], FilterUtils.countBonds(tmpAtomContainer, tmpConsiderImplicitHydrogens));
        }
    }

    /**
     * Tests whether the .countBonds() method of class FilterUtils throws a NullPointerException if the given
     * IAtomContainer instance is null.
     */
    @Test
    public void countBondsMethodTest_throwsNullPointerExceptionIfGivenAtomContainerIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    boolean tmpConsiderImplicitHydrogens = true;
                    FilterUtils.countBonds(null, tmpConsiderImplicitHydrogens);
                }
        );
    }

    /**
     * Tests whether the .exceedsOrEqualsBondCount() method of class FilterUtils returns a boolean value.
     */
    @Test
    public void exceedsOrEqualsBondCountTest_returnsBooleanValue() {
        IAtomContainer tmpAtomContainer = new AtomContainer();
        int tmpBondCountThresholdValue = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        Assertions.assertInstanceOf(
                Boolean.class,
                FilterUtils.exceedsOrEqualsBondCount(tmpAtomContainer, tmpBondCountThresholdValue, tmpConsiderImplicitHydrogens)
        );
    }

    /**
     * Tests whether the .exceedsOrEqualsBondCount() method of class FilterUtils returns true if given atom container
     * exceeds the given threshold considering implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void exceedsOrEqualsBondCountTest_returnsTrue_threshold5_considerImplHs_8Bonds() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("CCO");
        //
        int tmpBondCountThresholdValue = 5;
        boolean tmpConsiderImplicitHydrogens = true;
        Assertions.assertTrue(FilterUtils.exceedsOrEqualsBondCount(tmpAtomContainer, tmpBondCountThresholdValue, tmpConsiderImplicitHydrogens));
    }

    /**
     * Tests whether the .exceedsOrEqualsBondCount() method of class FilterUtils returns false if given atom container
     * does not exceed the given threshold considering implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void exceedsOrEqualsBondCountTest_returnsFalse_threshold10_considerImplHs_8Bonds() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("CCO");
        //
        int tmpBondCountThresholdValue = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        Assertions.assertFalse(FilterUtils.exceedsOrEqualsBondCount(tmpAtomContainer, tmpBondCountThresholdValue, tmpConsiderImplicitHydrogens));
    }

    /**
     * Tests whether the .exceedsOrEqualsBondCount() method of class FilterUtils returns true if given atom container
     * equals the given threshold considering implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void exceedsOrEqualsBondCountTest_returnsTrue_threshold8_considerImplHs_8Bonds() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("CCO");
        //
        int tmpBondCountThresholdValue = 8;
        boolean tmpConsiderImplicitHydrogens = true;
        Assertions.assertTrue(FilterUtils.exceedsOrEqualsBondCount(tmpAtomContainer, tmpBondCountThresholdValue, tmpConsiderImplicitHydrogens));
    }

    /**
     * Tests whether the .exceedsOrEqualsBondCount() method of class FilterUtils returns true if given atom container
     * exceeds the given threshold not considering implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void exceedsOrEqualsBondCountTest_returnsTrue_threshold3_notConsiderImplHs_4Bonds() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("NCC(=O)O");
        //
        int tmpBondCountThresholdValue = 3;
        boolean tmpConsiderImplicitHydrogens = false;
        Assertions.assertTrue(FilterUtils.exceedsOrEqualsBondCount(tmpAtomContainer, tmpBondCountThresholdValue, tmpConsiderImplicitHydrogens));
    }

    /**
     * Tests whether the .exceedsOrEqualsBondCount() method of class FilterUtils returns false if given atom container
     * does not exceed the given threshold not considering implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void exceedsOrEqualsBondCountTest_returnsFalse_threshold5_notConsiderImplHs_4Bonds() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("NCC(=O)O");
        //
        int tmpBondCountThresholdValue = 5;
        boolean tmpConsiderImplicitHydrogens = false;
        Assertions.assertFalse(FilterUtils.exceedsOrEqualsBondCount(tmpAtomContainer, tmpBondCountThresholdValue, tmpConsiderImplicitHydrogens));
    }

    /**
     * Tests whether the .exceedsOrEqualsBondCount() method of class FilterUtils returns true if given atom container
     * equals the given threshold not considering implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void exceedsOrEqualsBondCountTest_returnsTrue_threshold4_notConsiderImplHs_4Bonds() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("NCC(=O)O");
        //
        int tmpBondCountThresholdValue = 4;
        boolean tmpConsiderImplicitHydrogens = false;
        Assertions.assertTrue(FilterUtils.exceedsOrEqualsBondCount(tmpAtomContainer, tmpBondCountThresholdValue, tmpConsiderImplicitHydrogens));
    }

    /**
     * Tests whether the .exceedsOrEqualsBondCount() method of class FilterUtils throws a NullPointerException if the
     * given IAtomContainer instance is null.
     */
    @Test
    public void exceedsOrEqualsBondCountTest_throwsNullPointerExceptionIfGivenAtomContainerIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    FilterUtils.exceedsOrEqualsBondCount(null, 10, true);
                }
        );
    }

    /**
     * Tests whether the .exceedsOrEqualsBondCount() method of class FilterUtils throws an IllegalArgumentException if
     * the given threshold (integer value) is of a negative value.
     */
    @Test
    public void exceedsOrEqualsBondCountTest_throwsIllegalArgumentExceptionIfGivenThresholdValueIsNegative() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    FilterUtils.exceedsOrEqualsBondCount(new AtomContainer(), -1, true);
                }
        );
    }

}
