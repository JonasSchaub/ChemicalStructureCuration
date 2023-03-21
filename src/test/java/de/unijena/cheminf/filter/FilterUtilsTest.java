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
import org.openscience.cdk.Bond;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IBond;

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
        Assertions.assertInstanceOf(Boolean.class, FilterUtils.exceedsOrEqualsBondCount(tmpAtomContainer,
                tmpBondCountThresholdValue, tmpConsiderImplicitHydrogens));
    }

    /**
     * Tests whether the .exceedsOrEqualsBondCount() method of class FilterUtils returns true if given atom container
     * exceeds the given threshold considering bonds to implicit hydrogen atoms.
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
     * does not exceed the given threshold considering bonds to implicit hydrogen atoms.
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
     * equals the given threshold considering bonds to implicit hydrogen atoms.
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
     * exceeds the given threshold not considering bonds to implicit hydrogen atoms.
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
     * does not exceed the given threshold not considering bonds to implicit hydrogen atoms.
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
     * equals the given threshold not considering bonds to implicit hydrogen atoms.
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

    /**
     * Tests whether the .countBondsOfSpecificOrder() method of class FilterUtils returns an integer value.
     */
    @Test
    public void countBondsOfSpecificOrderMethodTest_returnsIntegerValue() {
        IAtomContainer tmpAtomContainer = new AtomContainer();
        IBond.Order tmpBondOrder = IBond.Order.UNSET;
        boolean tmpConsiderImplicitHydrogens = true;
        Assertions.assertInstanceOf(Integer.class, FilterUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, tmpBondOrder, tmpConsiderImplicitHydrogens));
    }

    /**
     * Tests whether the .countBondsOfSpecificOrder() method of class FilterUtils returns the correct result when
     * counting bonds with bond order single not considering bonds to implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void countBondsOfSpecificOrderMethodTest_returnsBondTypeCount_singleBond_notConsideringImplHs_twoTests() throws InvalidSmilesException {
        IBond.Order tmpBondOrder = IBond.Order.SINGLE;
        boolean tmpConsiderImplicitHydrogens = false;
        //
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("NCC(=O)O");
        int tmpSingleBondCount = 3;
        Assertions.assertEquals(tmpSingleBondCount, FilterUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, tmpBondOrder, tmpConsiderImplicitHydrogens));
        tmpAtomContainer = TestUtils.parseSmilesString("C=CC=C");
        tmpSingleBondCount = 1;
        Assertions.assertEquals(tmpSingleBondCount, FilterUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, tmpBondOrder, tmpConsiderImplicitHydrogens));
    }

    /**
     * Tests whether the .countBondsOfSpecificOrder() method of class FilterUtils returns the correct result when
     * counting bonds with bond order single considering bonds to implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void countBondsOfSpecificOrderMethodTest_returnsBondTypeCount_singleBond_consideringImplHs_twoTests() throws InvalidSmilesException {
        IBond.Order tmpBondOrder = IBond.Order.SINGLE;
        boolean tmpConsiderImplicitHydrogens = true;
        //
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("NCC(=O)O");
        int tmpSingleBondCount = 8;
        Assertions.assertEquals(tmpSingleBondCount, FilterUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, tmpBondOrder, tmpConsiderImplicitHydrogens));
        tmpAtomContainer = TestUtils.parseSmilesString("C=CC=C");
        tmpSingleBondCount = 7;
        Assertions.assertEquals(tmpSingleBondCount, FilterUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, tmpBondOrder, tmpConsiderImplicitHydrogens));
    }

    /**
     * Tests whether the .countBondsOfSpecificOrder() method of class FilterUtils returns the correct result when
     * counting bonds with bond order double.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void countBondsOfSpecificOrderMethodTest_returnsBondTypeCount_doubleBond_twoTests() throws InvalidSmilesException {
        IBond.Order tmpBondOrder = IBond.Order.DOUBLE;
        boolean tmpConsiderImplicitHydrogens = false;   //can be ignored
        //
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("NCC(=O)O");
        int tmpDoubleBondCount = 1;
        Assertions.assertEquals(tmpDoubleBondCount, FilterUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, tmpBondOrder, tmpConsiderImplicitHydrogens));
        tmpAtomContainer = TestUtils.parseSmilesString("C=CC=CC=CC(=O)O");
        tmpDoubleBondCount = 4;
        Assertions.assertEquals(tmpDoubleBondCount, FilterUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, tmpBondOrder, tmpConsiderImplicitHydrogens));
    }

    /**
     * Tests whether the .countBondsOfSpecificOrder() method of class FilterUtils returns the correct result when
     * counting bonds with bond order triple.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void countBondsOfSpecificOrderMethodTest_returnsBondTypeCount_trippleBond_twoTests() throws InvalidSmilesException {
        IBond.Order tmpBondOrder = IBond.Order.TRIPLE;
        boolean tmpConsiderImplicitHydrogens = false;   //can be ignored
        //
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("N#CC#N");
        int tmpTripleBondCount = 2;
        Assertions.assertEquals(tmpTripleBondCount, FilterUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, tmpBondOrder, tmpConsiderImplicitHydrogens));
        tmpAtomContainer = TestUtils.parseSmilesString("O=C(O)C=CCC#CC#CCC#C");
        tmpTripleBondCount = 3;
        Assertions.assertEquals(tmpTripleBondCount, FilterUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, tmpBondOrder, tmpConsiderImplicitHydrogens));
    }

    /**
     * Tests whether the .countBondsOfSpecificOrder() method of class FilterUtils returns the correct result when
     * counting bonds with bond orders quadruple, quintuple and sextuple.
     */
    @Test
    public void countBondsOfSpecificOrderMethodTest_returnsBondTypeCount_bondOrdersHigherThanTriple() {
        boolean tmpConsiderImplicitHydrogens = false;   //can be ignored
        IBond.Order[] tmpBondOrderArray = new IBond.Order[]{
                IBond.Order.QUADRUPLE,
                IBond.Order.QUINTUPLE,
                IBond.Order.SEXTUPLE
        };
        IAtomContainer tmpAtomContainer = new AtomContainer();
        IBond tmpBond;
        int tmpSpecificBondCount;
        //
        for (IBond.Order tmpBondOrder : tmpBondOrderArray) {
            tmpSpecificBondCount = 0;
            Assertions.assertEquals(tmpSpecificBondCount, FilterUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, tmpBondOrder, tmpConsiderImplicitHydrogens));
            tmpBond = new Bond();
            tmpBond.setOrder(tmpBondOrder);
            tmpAtomContainer.addBond(tmpBond);
            tmpSpecificBondCount = 1;
            Assertions.assertEquals(tmpSpecificBondCount, FilterUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, tmpBondOrder, tmpConsiderImplicitHydrogens));
        }
    }

    /**
     * Tests whether the .countBondsOfSpecificOrder() method of class FilterUtils returns the correct result when
     * counting bonds with unset bond order.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void countBondsOfSpecificOrderMethodTest_returnsBondTypeCount_unsetBond_twoTests() throws InvalidSmilesException {
        IBond.Order tmpBondOrder = IBond.Order.UNSET;
        boolean tmpConsiderImplicitHydrogens = false;   //can be ignored
        //
        IAtomContainer tmpAtomContainer = new AtomContainer();
        IBond tmpBond = new Bond();
        tmpBond.setOrder(IBond.Order.UNSET);
        tmpAtomContainer.addBond(tmpBond);
        int tmpUnsetBondsCount = 1;
        Assertions.assertEquals(tmpUnsetBondsCount, FilterUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, tmpBondOrder, tmpConsiderImplicitHydrogens));
        tmpAtomContainer = TestUtils.parseSmilesString("CC(=O)C");
        tmpUnsetBondsCount = 3;
        for (int i = 0; i < tmpUnsetBondsCount; i++) {
            tmpAtomContainer.addBond(tmpBond);
        }
        Assertions.assertEquals(tmpUnsetBondsCount, FilterUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, tmpBondOrder, tmpConsiderImplicitHydrogens));
    }

    /**
     * Tests whether the .countBondsOfSpecificOrder() method of class FilterUtils returns the correct result when counting bonds
     * with no bond order (bond order is null).
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void countBondsOfSpecificOrderMethodTest_returnsBondTypeCount_bondOrderNull_twoTests() throws InvalidSmilesException {
        IBond.Order tmpBondOrder = null;
        boolean tmpConsiderImplicitHydrogens = false;   //can be ignored
        //
        IAtomContainer tmpAtomContainer = new AtomContainer();
        IBond tmpBond = new Bond();
        //tmpBond.setOrder(null);
        tmpAtomContainer.addBond(tmpBond);
        int tmpUndefinedBondsCount = 1;
        Assertions.assertEquals(tmpUndefinedBondsCount, FilterUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, tmpBondOrder, tmpConsiderImplicitHydrogens));
        tmpAtomContainer = TestUtils.parseSmilesString("CC(=O)C");
        tmpUndefinedBondsCount = 3;
        for (int i = 0; i < tmpUndefinedBondsCount; i++) {
            tmpAtomContainer.addBond(tmpBond);
        }
        Assertions.assertEquals(tmpUndefinedBondsCount, FilterUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, tmpBondOrder, tmpConsiderImplicitHydrogens));
    }

    /**
     * Tests how the .countBondsOfSpecificOrder() method of class FilterUtils reacts to aromatic systems not considering bonds to
     * implicit hydrogen atoms; benzene: 3 single, 3 double bonds; naphthalene: 6 single, 5 double bonds.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void countBondsOfSpecificOrderMethodTest_returnsBondTypeCount_aromaticSystem_shareOfSingleAndDoubleBonds_notConsiderImplHs_twoTests() throws InvalidSmilesException {
        boolean tmpConsiderImplicitHydrogens = false;
        //
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        int tmpExpectedSingleBondCount = 3;
        int tmpExpectedDoubleBondCount = 3;
        Assertions.assertEquals(tmpExpectedSingleBondCount, FilterUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, IBond.Order.SINGLE, tmpConsiderImplicitHydrogens));
        Assertions.assertEquals(tmpExpectedDoubleBondCount, FilterUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, IBond.Order.DOUBLE, tmpConsiderImplicitHydrogens));
        tmpAtomContainer = TestUtils.parseSmilesString("c1cccc2ccccc12");
        tmpExpectedSingleBondCount = 6;
        tmpExpectedDoubleBondCount = 5;
        Assertions.assertEquals(tmpExpectedSingleBondCount, FilterUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, IBond.Order.SINGLE, tmpConsiderImplicitHydrogens));
        Assertions.assertEquals(tmpExpectedDoubleBondCount, FilterUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, IBond.Order.DOUBLE, tmpConsiderImplicitHydrogens));
    }

    /**
     * Tests how the .countBondsOfSpecificOrder() method of class FilterUtils reacts to aromatic systems considering bonds to
     * implicit hydrogen atoms; benzene: 3 single, 3 double bonds; naphthalene: 5 single, 5 double bonds.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void countBondsOfSpecificOrderMethodTest_returnsBondTypeCount_aromaticSystem_shareOfSingleAndDoubleBonds_considerImplHs_twoTests() throws InvalidSmilesException {
        boolean tmpConsiderImplicitHydrogens = true;
        //
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        int tmpExpectedSingleBondCount = 9;
        int tmpExpectedDoubleBondCount = 3;
        Assertions.assertEquals(tmpExpectedSingleBondCount, FilterUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, IBond.Order.SINGLE, tmpConsiderImplicitHydrogens));
        Assertions.assertEquals(tmpExpectedDoubleBondCount, FilterUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, IBond.Order.DOUBLE, tmpConsiderImplicitHydrogens));
        tmpAtomContainer = TestUtils.parseSmilesString("c1cccc2ccccc12");
        tmpExpectedSingleBondCount = 14;
        tmpExpectedDoubleBondCount = 5;
        Assertions.assertEquals(tmpExpectedSingleBondCount, FilterUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, IBond.Order.SINGLE, tmpConsiderImplicitHydrogens));
        Assertions.assertEquals(tmpExpectedDoubleBondCount, FilterUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, IBond.Order.DOUBLE, tmpConsiderImplicitHydrogens));
    }

    /**
     * Tests whether the .countBondsOfSpecificOrder() method of class FilterUtils throws a NullPointerException if the given
     * IAtomContainer instance is null.
     */
    @Test
    public void countBondsOfSpecificOrderMethodTest_throwsNullPointerExceptionIfGivenAtomContainerIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    IAtomContainer tmpAtomContainer = null;
                    IBond.Order tmpBondOrder = IBond.Order.UNSET;
                    boolean tmpConsiderImplicitHydrogens = true;
                    Assertions.assertInstanceOf(Integer.class, FilterUtils.countBondsOfSpecificBondOrder(tmpAtomContainer,
                            tmpBondOrder, tmpConsiderImplicitHydrogens));
                }
        );
    }

    /**
     * Tests whether the .exceedsOrEqualsBondsOfSpecificBondOrderCount() method of class FilterUtils returns a boolean
     * value.
     */
    @Test
    public void exceedsOrEqualsBondsOfSpecificBondOrderCountTest_returnsBooleanValue() {
        IAtomContainer tmpAtomContainer = new AtomContainer();
        int tmpBondCountThresholdValue = 10;
        IBond.Order tmpBondOrder = IBond.Order.UNSET;
        boolean tmpConsiderImplicitHydrogens = true;
        Assertions.assertInstanceOf(
                Boolean.class,
                FilterUtils.exceedsOrEqualsBondsOfSpecificBondOrderCount(tmpAtomContainer, tmpBondOrder,
                        tmpBondCountThresholdValue, tmpConsiderImplicitHydrogens)
        );
    }

    /**
     * Tests whether the .exceedsOrEqualsBondsOfSpecificBondOrderCount() method of class FilterUtils returns true if
     * the given atom container exceeds the given threshold for bonds of bond order single considering and not
     * considering bonds to implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void exceedsOrEqualsBondsOfSpecificBondOrderCountTest_returnsTrue_singleBonds_thresholdExceeded() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("CCO");   //8 (2)
        IBond.Order tmpBondOrder = IBond.Order.SINGLE;
        //
        boolean tmpConsiderImplicitHydrogens = true;
        int tmpBondCountThresholdValue = 5;
        Assertions.assertTrue(FilterUtils.exceedsOrEqualsBondsOfSpecificBondOrderCount(
                tmpAtomContainer, tmpBondOrder, tmpBondCountThresholdValue, tmpConsiderImplicitHydrogens
        ));
        tmpConsiderImplicitHydrogens = false;
        tmpBondCountThresholdValue = 1;
        Assertions.assertTrue(FilterUtils.exceedsOrEqualsBondsOfSpecificBondOrderCount(
                tmpAtomContainer, tmpBondOrder, tmpBondCountThresholdValue, tmpConsiderImplicitHydrogens
        ));
    }

    /**
     * Tests whether the .exceedsOrEqualsBondsOfSpecificBondOrderCount() method of class FilterUtils returns false if
     * the given atom container does not exceed the given threshold for bonds of bond order single considering and not
     * considering bonds to implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void exceedsOrEqualsBondsOfSpecificBondOrderCountTest_returnsFalse_singleBonds_thresholdNotExceeded() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("CCO");   //8 (2)
        IBond.Order tmpBondOrder = IBond.Order.SINGLE;
        //
        boolean tmpConsiderImplicitHydrogens = true;
        int tmpBondCountThresholdValue = 10;
        Assertions.assertFalse(FilterUtils.exceedsOrEqualsBondsOfSpecificBondOrderCount(
                tmpAtomContainer, tmpBondOrder, tmpBondCountThresholdValue, tmpConsiderImplicitHydrogens
        ));
        tmpConsiderImplicitHydrogens = false;
        tmpBondCountThresholdValue = 4;
        Assertions.assertFalse(FilterUtils.exceedsOrEqualsBondsOfSpecificBondOrderCount(
                tmpAtomContainer, tmpBondOrder, tmpBondCountThresholdValue, tmpConsiderImplicitHydrogens
        ));
    }

    /**
     * Tests whether the .exceedsOrEqualsBondsOfSpecificBondOrderCount() method of class FilterUtils returns true if
     * the given atom container equals the given threshold for bonds of bond order single considering and not
     * considering bonds to implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void exceedsOrEqualsBondsOfSpecificBondOrderCountTest_returnsTrue_singleBonds_thresholdEqualed() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("CCO");   //8 (2)
        IBond.Order tmpBondOrder = IBond.Order.SINGLE;
        //
        boolean tmpConsiderImplicitHydrogens = true;
        int tmpBondCountThresholdValue = 8;
        Assertions.assertTrue(FilterUtils.exceedsOrEqualsBondsOfSpecificBondOrderCount(
                tmpAtomContainer, tmpBondOrder, tmpBondCountThresholdValue, tmpConsiderImplicitHydrogens
        ));
        tmpConsiderImplicitHydrogens = false;
        tmpBondCountThresholdValue = 2;
        Assertions.assertTrue(FilterUtils.exceedsOrEqualsBondsOfSpecificBondOrderCount(
                tmpAtomContainer, tmpBondOrder, tmpBondCountThresholdValue, tmpConsiderImplicitHydrogens
        ));
    }

    /**
     * Tests whether the .exceedsOrEqualsBondsOfSpecificBondOrderCount() method of class FilterUtils returns the
     * correct results if being tested with different bond orders.
     * Bond orders of value double, triple, quadruple, quintuple, sextuple, unset and null are being tested.
     */
    @Test
    public void exceedsOrEqualsBondsOfSpecificBondOrderCountTest_returnsTrue_threshold5_considerImplHs_8Bonds() {
        IBond.Order[] tmpBondOrderArray = new IBond.Order[]{
                IBond.Order.DOUBLE,
                IBond.Order.TRIPLE,
                IBond.Order.QUADRUPLE,
                IBond.Order.QUINTUPLE,
                IBond.Order.SEXTUPLE,
                IBond.Order.UNSET,
                null
        };
        IAtomContainer tmpAtomContainer = new AtomContainer();
        IBond tmpBond = new Bond();
        tmpAtomContainer.addBond(tmpBond);
        int tmpThresholdValue;
        boolean tmpConsiderImplicitHydrogens = false;   //can be ignored
        //
        for (IBond.Order tmpBondOrder : tmpBondOrderArray) {
            tmpBond.setOrder(tmpBondOrder);
            tmpThresholdValue = 0;  //exceeded
            Assertions.assertTrue(FilterUtils.exceedsOrEqualsBondsOfSpecificBondOrderCount(
                    tmpAtomContainer, tmpBondOrder, tmpThresholdValue, tmpConsiderImplicitHydrogens
            ));
            tmpThresholdValue = 1;  //equaled
            Assertions.assertTrue(FilterUtils.exceedsOrEqualsBondsOfSpecificBondOrderCount(
                    tmpAtomContainer, tmpBondOrder, tmpThresholdValue, tmpConsiderImplicitHydrogens
            ));
            tmpThresholdValue = 2;  //not exceeded
            Assertions.assertFalse(FilterUtils.exceedsOrEqualsBondsOfSpecificBondOrderCount(
                    tmpAtomContainer, tmpBondOrder, tmpThresholdValue, tmpConsiderImplicitHydrogens
            ));
        }
    }

    /**
     * Tests whether the .exceedsOrEqualsBondsOfSpecificBondOrderCount() method of class FilterUtils throws a
     * NullPointerException if the given IAtomContainer instance is null.
     */
    @Test
    public void exceedsOrEqualsBondsOfSpecificBondOrderCountTest_throwsNullPointerExceptionIfGivenAtomContainerIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    FilterUtils.exceedsOrEqualsBondsOfSpecificBondOrderCount(null, null, 10, true);
                }
        );
    }

    /**
     * Tests whether the .exceedsOrEqualsBondsOfSpecificBondOrderCount() method of class FilterUtils throws an
     * IllegalArgumentException if the given threshold value is of a negative value.
     */
    @Test
    public void exceedsOrEqualsBondsOfSpecificBondOrderCountTest_throwsIllegalArgumentExceptionIfGivenThresholdValueIsNegative() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    FilterUtils.exceedsOrEqualsBondsOfSpecificBondOrderCount(new AtomContainer(), null, -1, true);
                }
        );
    }

}
