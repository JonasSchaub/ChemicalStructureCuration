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
import org.openscience.cdk.Atom;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.Bond;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IElement;

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
     * Tests whether the .countImplicitHydrogens() method of the class FilterUtils returns 6 when given an atom
     * container with 6 implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void countImplicitHydrogensTest_6ImplicitHydrogens() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        //
        Assertions.assertEquals(6, FilterUtils.countImplicitHydrogens(tmpAtomContainer));
    }

    /**
     * Tests whether the .countImplicitHydrogens() method of the class FilterUtils returns 6 when given an atom
     * container with 6 implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void countImplicitHydrogensTest_5ImplicitHydrogens() throws InvalidSmilesException {
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
     * Tests whether the .countExplicitHydrogens() method of the class FilterUtils returns an integer value.
     */
    @Test
    public void countExplicitHydrogensTest_returnsIntegerValue() {
        IAtomContainer tmpAtomContainer = new AtomContainer();
        Assertions.assertInstanceOf(Integer.class, FilterUtils.countExplicitHydrogens(tmpAtomContainer));
    }

    /**
     * Tests whether the .countExplicitHydrogens() method of the class FilterUtils returns 6 when given an atom
     * container with 6 explicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void countExplicitHydrogensTest_4ExplicitHydrogens() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("HC(H)(H)H");
        //
        Assertions.assertEquals(4, FilterUtils.countExplicitHydrogens(tmpAtomContainer));
    }

    /**
     * Tests whether the .countExplicitHydrogens() method of the class FilterUtils returns 6 when given an atom
     * container with 6 implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void countExplicitHydrogensTest_2Implicit_3ExplicitHydrogens() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("HN(H)CC(=O)OH");
        //
        Assertions.assertEquals(3, FilterUtils.countExplicitHydrogens(tmpAtomContainer));
    }

    /**
     * Tests whether the .countExplicitHydrogens() method of the class FilterUtils throws a NullPointerException if the
     * given IAtomContainer instance is null.
     */
    @Test
    public void countExplicitHydrogensTest_throwsNullPointerExceptionIfGivenAtomContainerIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    FilterUtils.countExplicitHydrogens(null);
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

    /**
     * Tests whether the .countAtomsOfAtomicNumbers() method of class FilterUtils returns an integer value.
     */
    @Test
    public void countAtomsOfAtomicNumbersMethodTest_returnsIntegerValue() {
        IAtomContainer tmpAtomContainer = new AtomContainer();
        Assertions.assertInstanceOf(Integer.class, FilterUtils.countAtomsOfAtomicNumbers(tmpAtomContainer));
    }

    /**
     * Tests whether the .countAtomsOfAtomicNumbers() method of class FilterUtils accepts a varying amount of integer
     * value parameters.
     */
    @Test
    public void countAtomsOfAtomicNumbersMethodTest_acceptsVaryingAmountOfIntegerParams() {
        Assertions.assertDoesNotThrow(() -> {
            IAtomContainer tmpAtomContainer = new AtomContainer();
            int tmpAnIntegerValue = 5;
            int tmpReturnValue;
            tmpReturnValue = FilterUtils.countAtomsOfAtomicNumbers(tmpAtomContainer);
            tmpReturnValue = FilterUtils.countAtomsOfAtomicNumbers(tmpAtomContainer, tmpAnIntegerValue);
            tmpReturnValue = FilterUtils.countAtomsOfAtomicNumbers(tmpAtomContainer, tmpAnIntegerValue, tmpAnIntegerValue);
            tmpReturnValue = FilterUtils.countAtomsOfAtomicNumbers(tmpAtomContainer, tmpAnIntegerValue, tmpAnIntegerValue, tmpAnIntegerValue);
        });
    }

    /**
     * Tests whether the .countAtomsOfAtomicNumbers() method of class FilterUtils counts the number of atoms of a single
     * atomic number in a given atom container correctly; here: carbon (atomic number: 6).
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed    //TODO: check everywhere: the / a SMILES
     */
    @Test
    public void countAtomsOfAtomicNumbersMethodTest_countOfCarbonAtoms() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "CC(=O)C(=O)O",  // 3
                "c1ccccc1", // 6
                "C1CCCC1",  // 5
                "NCC(=O)O", // 2
                "C=CC=C",   // 4
                "O"         // 0
        );
        int tmpCarbonAtomicNumber = IElement.C; //atomic number: 6
        int[] tmpExpectedAtomsCountArray = new int[]{3, 6, 5, 2, 4, 0};
        //
        IAtomContainer tmpAtomContainer;
        int tmpCalculatedAtomsCount;
        for (int i = 0; i < tmpAtomContainerSet.getAtomContainerCount(); i++) {
            tmpAtomContainer = tmpAtomContainerSet.getAtomContainer(i);
            tmpCalculatedAtomsCount = FilterUtils.countAtomsOfAtomicNumbers(tmpAtomContainer, tmpCarbonAtomicNumber);
            Assertions.assertEquals(tmpExpectedAtomsCountArray[i], tmpCalculatedAtomsCount);
        }
    }

    /**
     * Tests whether the .countAtomsOfAtomicNumbers() method of class FilterUtils counts the number of atoms of a single
     * atomic number in a given atom container correctly; here, exemplary for others:
     * - oxygen (atomic number: 8)
     * - nitrogen (atomic number: 7)
     * - sulphur (atomic number: 16).
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void countAtomsOfAtomicNumbersMethodTest_countAtomsOfDifferentAtomicNumbers_separateTests() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "S1SSSSS1",
                "O=S(O)(O)=O",
                "NCC(=O)O",
                "SCC(N)C(=O)O",
                "S(CC(N)C(=O)O)SCC(N)C(=O)O",
                "O"
        );
        //
        int[] tmpAtomicNumbersArray = new int[]{
                IElement.O, //atomic number: 8
                IElement.N, //atomic number: 7
                IElement.S  //atomic number: 16
        };
        //
        int[][] tmpExpectedAtomsCountMatrix = new int[tmpAtomicNumbersArray.length][tmpAtomContainerSet.getAtomContainerCount()];
        tmpExpectedAtomsCountMatrix[0] = new int[]{0, 4, 2, 2, 4, 1};
        tmpExpectedAtomsCountMatrix[1] = new int[]{0, 0, 1, 1, 2, 0};
        tmpExpectedAtomsCountMatrix[2] = new int[]{6, 1, 0, 1, 2, 0};
        //
        IAtomContainer tmpAtomContainer;
        int tmpAtomicNumber;
        int tmpCalculatedAtomsCount;
        for (int i = 0; i < tmpAtomicNumbersArray.length; i++) {     //i = index of atomic number
            tmpAtomicNumber = tmpAtomicNumbersArray[i];
            for (int j = 0; j < tmpAtomContainerSet.getAtomContainerCount(); j++) {     //j = index of atom container
                tmpAtomContainer = tmpAtomContainerSet.getAtomContainer(j);
                tmpCalculatedAtomsCount = FilterUtils.countAtomsOfAtomicNumbers(tmpAtomContainer, tmpAtomicNumber);
                Assertions.assertEquals(tmpExpectedAtomsCountMatrix[i][j], tmpCalculatedAtomsCount);
            }
        }
    }

    /**
     * Tests whether the .countAtomsOfAtomicNumbers() method of class FilterUtils counts the number of atoms with an
     * atomic number of one in a given atom container correctly, returning the sum of explicit and implicit hydrogen
     * atoms.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void countAtomsOfAtomicNumbersMethodTest_countOfHydrogenAtoms_returnsSumOfExplicitAndImplicitHs() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "HC(H)(H)H",    // 4 (0)
                "C",            // 4 (4)
                "CH",           // 4 (3)
                "C1CCCC1",      //10 (10)
                "C=C(H)C(H)=C", // 6 (4)
                "O"             // 2 (2)
        );
        int tmpHydrogenAtomicNumber = IElement.H; //atomic number: 1
        int[] tmpExpectedAtomsCountArray = new int[]{4, 4, 4, 10, 6, 2};
        //
        IAtomContainer tmpAtomContainer;
        int tmpCalculatedAtomsCount;
        for (int i = 0; i < tmpAtomContainerSet.getAtomContainerCount(); i++) {
            tmpAtomContainer = tmpAtomContainerSet.getAtomContainer(i);
            tmpCalculatedAtomsCount = FilterUtils.countAtomsOfAtomicNumbers(tmpAtomContainer, tmpHydrogenAtomicNumber);
            Assertions.assertEquals(tmpExpectedAtomsCountArray[i], tmpCalculatedAtomsCount);
        }
    }

    /**
     * Tests whether the .countAtomsOfAtomicNumbers() method of class FilterUtils counts the number of atoms of multiple
     * given atomic numbers in a given atom container correctly; test with given two, three and five atomic numbers.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void countAtomsOfAtomicNumbersMethodTest_returnsExpectedResultIfGivenMultipleAtomicNumbers() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "S1SSSSS1",                     //S6
                "O=S(O)(O)=O",                  //SO4H2
                "NCC(=O)O",                     //NC2O2H5
                "SCC(N)C(=O)O",                 //NSC3O2H7
                "S(CC(N)C(=O)O)SCC(N)C(=O)O",   //N2S2C6O4H12
                "O"                             //H2O
        );
        //
        int[][] tmpAtomicNumbersMatrix = new int[3][];
        int[][] tmpExpectedAtomsCountMatrix = new int[tmpAtomicNumbersMatrix.length][tmpAtomContainerSet.getAtomContainerCount()];
        //
        tmpAtomicNumbersMatrix[0] = new int[]{IElement.C, IElement.O};
        tmpExpectedAtomsCountMatrix[0] = new int[]{0, 4, 4, 5, 10, 1};
        //
        tmpAtomicNumbersMatrix[1] = new int[]{IElement.C, IElement.O, IElement.N};
        tmpExpectedAtomsCountMatrix[1] = new int[]{0, 4, 5, 6, 12, 1};
        //
        tmpAtomicNumbersMatrix[2] = new int[]{IElement.C, IElement.O, IElement.N, IElement.S, IElement.H};
        tmpExpectedAtomsCountMatrix[2] = new int[]{6, 7, 10, 14, 26, 3};
        //
        IAtomContainer tmpAtomContainer;
        int[] tmpAtomicNumbersArray;
        int tmpCalculatedAtomsCount;
        for (int i = 0; i < tmpAtomicNumbersMatrix.length; i++) {     //i = index of atomic number array
            tmpAtomicNumbersArray = tmpAtomicNumbersMatrix[i];
            for (int j = 0; j < tmpAtomContainerSet.getAtomContainerCount(); j++) {     //j = index of atom container
                tmpAtomContainer = tmpAtomContainerSet.getAtomContainer(j);
                tmpCalculatedAtomsCount = FilterUtils.countAtomsOfAtomicNumbers(tmpAtomContainer, tmpAtomicNumbersArray);
                Assertions.assertEquals(tmpExpectedAtomsCountMatrix[i][j], tmpCalculatedAtomsCount);
            }
        }
    }

    /**
     * Tests whether the .countAtomsOfAtomicNumbers() method of class FilterUtils throws a NullPointerException if the
     * given instance of IAtomContainer is null.
     */
    @Test
    public void countAtomsOfAtomicNumbersMethodTest_throwsNullPointerExceptionIfGivenAtomContainerIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    IAtomContainer tmpAtomContainer = null;
                    int tmpAnAtomicNumber = 5;
                    int tmpReturnValue = FilterUtils.countAtomsOfAtomicNumbers(tmpAtomContainer, tmpAnAtomicNumber);
                }
        );
    }

    /**
     * Tests whether the .countAtomsOfAtomicNumbers() method of class FilterUtils throws an IllegalArgumentException if
     * a given integer is of negative value; test 1, single parameter.
     */
    @Test
    public void countAtomsOfAtomicNumbersMethodTest_throwsIllegalArgumentExceptionIfGivenIntegerParamIsNegative_singleParam() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    IAtomContainer tmpAtomContainer = new AtomContainer();
                    int tmpNegativeIntegerValue = -1;
                    int tmpReturnValue = FilterUtils.countAtomsOfAtomicNumbers(tmpAtomContainer, tmpNegativeIntegerValue);
                }
        );
    }

    /**
     * Tests whether the .countAtomsOfAtomicNumbers() method of class FilterUtils throws an IllegalArgumentException if
     * a given integer is of negative value; test 2, multiple parameters.
     */
    @Test
    public void countAtomsOfAtomicNumbersMethodTest_throwsIllegalArgumentExceptionIfGivenIntegerParamIsNegative_multipleParams() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    IAtomContainer tmpAtomContainer = new AtomContainer();
                    int tmpNegativeIntegerValue = -1;
                    int tmpAcceptedValue = 5;
                    int tmpReturnValue = FilterUtils.countAtomsOfAtomicNumbers(
                            tmpAtomContainer,
                            tmpAcceptedValue,
                            tmpNegativeIntegerValue,
                            tmpAcceptedValue
                    );
                }
        );
    }

    /**
     * Tests whether the .countAtomsOfAtomicNumbers() method of class FilterUtils throws an IllegalArgumentException if
     * a given integer value exceeds the greatest currently known atomic number (currently 118); test 1, single
     * parameter.
     */
    @Test
    public void countAtomsOfAtomicNumbersMethodTest_throwsIllegalArgumentExceptionIfGivenIntegerParamIsGreaterThan118_singleParam() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    IAtomContainer tmpAtomContainer = new AtomContainer();
                    int tmpUnknownAtomicNumber = 119;
                    int tmpReturnValue = FilterUtils.countAtomsOfAtomicNumbers(tmpAtomContainer, tmpUnknownAtomicNumber);
                }
        );
    }

    /**
     * Tests whether the .countAtomsOfAtomicNumbers() method of class FilterUtils throws an IllegalArgumentException if
     * a given integer value exceeds the greatest currently known atomic number (currently 118); test 2, multiple
     * parameters.
     */
    @Test
    public void countAtomsOfAtomicNumbersMethodTest_throwsIllegalArgumentExceptionIfGivenIntegerParamIsGreaterThan118_multipleParams() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    IAtomContainer tmpAtomContainer = new AtomContainer();
                    int tmpUnknownAtomicNumber = 119;
                    int tmpAcceptedValue = 10;
                    int tmpReturnValue = FilterUtils.countAtomsOfAtomicNumbers(
                            tmpAtomContainer,
                            tmpAcceptedValue,
                            tmpAcceptedValue,
                            tmpUnknownAtomicNumber
                    );
                }
        );
    }

    /**
     * Tests whether the .getHeavyAtomsCount() method of class FilterUtils returns an integer value.
     */
    @Test
    public void getHeavyAtomsCountMethodTest_returnsIntegerValue() {
        IAtomContainer tmpAtomContainer = new AtomContainer();
        Assertions.assertInstanceOf(Integer.class, FilterUtils.getHeavyAtomsCount(tmpAtomContainer));
    }

    /**
     * Tests whether the .getHeavyAtomsCount() method of class FilterUtils returns the count of heavy atoms
     * (non-hydrogen atoms); multiple tests.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void getHeavyAtomsCountMethodTest_returnsCountOfHeavyAtoms() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "c1ccccc1",
                "NCC(=O)O",
                "NC(H)C(=O)OH",
                "C(H)CH",
                "O",
                "HOH"
        );
        int[] tmpExpectedHeavyAtomsCount = new int[]{6, 5, 5, 2, 1, 1};
        //
        for (int i = 0; i < tmpAtomContainerSet.getAtomContainerCount(); i++) {
            Assertions.assertEquals(
                    tmpExpectedHeavyAtomsCount[i],
                    FilterUtils.getHeavyAtomsCount(tmpAtomContainerSet.getAtomContainer(i))
            );
        }
    }

    /**
     * Tests whether the .getHeavyAtomsCount() method of class FilterUtils throws a NullPointerException if the given
     * IAtomContainer instance is null.
     */
    @Test
    public void getHeavyAtomsCountMethodTest_throwsNullPointerExceptionIfGivenAtomContainerIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    IAtomContainer tmpAtomContainer = null;
                    int tmpReturnValue = FilterUtils.getHeavyAtomsCount(tmpAtomContainer);
                }
        );
    }

    /**
     * Tests whether the .hasValidAtomicNumber() method of class FilterUtils returns an instance of Boolean if given
     * an atom with an atomic number not null; the boolean param has no impact on this.
     */
    @Test
    public void hasValidAtomicNumber_returnsInstanceOfBooleanIfAtomHasAtomicNumber() {
        IAtom tmpAtom = new Atom(1);
        boolean tmpIncludeWildcardNumber = true;
        Assertions.assertInstanceOf(Boolean.class, FilterUtils.hasValidAtomicNumber(tmpAtom, tmpIncludeWildcardNumber));
        tmpIncludeWildcardNumber = false;
        Assertions.assertInstanceOf(Boolean.class, FilterUtils.hasValidAtomicNumber(tmpAtom, tmpIncludeWildcardNumber));
    }

    /**
     * Tests whether the .hasValidAtomicNumber() method of class FilterUtils returns null if given an atom with an
     * atomic number of null; the boolean param has no impact on this.
     */
    @Test
    public void hasValidAtomicNumber_returnsNullIfAtomHasAtomicNumberNull() {
        IAtom tmpAtom = new Atom(); //has atomic number null
        boolean tmpIncludeWildcardNumber = true;
        Assertions.assertNull(FilterUtils.hasValidAtomicNumber(tmpAtom, tmpIncludeWildcardNumber));
        tmpIncludeWildcardNumber = false;
        Assertions.assertNull(FilterUtils.hasValidAtomicNumber(tmpAtom, tmpIncludeWildcardNumber));
    }

    /**
     * Tests whether the .hasValidAtomicNumber() method of class FilterUtils returns true if the given atom has a valid
     * atomic number; atomic number = 6; whether the wildcard number 0 is included, has no impact on the result here.
     */
    @Test
    public void hasValidAtomicNumber_returnsTrueForAtomicNumber6() {
        IAtom tmpAtom = new Atom(IElement.C);   //atomic number: 6
        boolean tmpIncludeWildcardNumber = true;
        Assertions.assertEquals(true, FilterUtils.hasValidAtomicNumber(tmpAtom, tmpIncludeWildcardNumber));
        tmpIncludeWildcardNumber = false;
        Assertions.assertEquals(true, FilterUtils.hasValidAtomicNumber(tmpAtom, tmpIncludeWildcardNumber));
    }

    /**
     * Tests whether the .hasValidAtomicNumber() method of class FilterUtils returns true if the given atom has a valid
     * atomic number; tested atomic numbers: 1, 2, 8, 10, 118; whether the wildcard number 0 is included, has no impact
     * on the results here.
     */
    @Test
    public void hasValidAtomicNumber_returnsTrueForDifferentValidAtomicNumbers() {
        int[] tmpAtomicNumbersArray = new int[]{
                IElement.H,     //atomic number: 1
                IElement.He,    //atomic number: 2
                IElement.O,     //atomic number: 8
                IElement.Ne,    //atomic number: 10
                IElement.Og     //atomic number: 118
        };
        IAtom tmpAtom = new Atom();
        boolean tmpIncludeWildcardNumber;
        for (int tmpAtomicNumber :
                tmpAtomicNumbersArray) {
            tmpAtom.setAtomicNumber(tmpAtomicNumber);
            tmpIncludeWildcardNumber = true;
            Assertions.assertEquals(true, FilterUtils.hasValidAtomicNumber(tmpAtom, tmpIncludeWildcardNumber));
            tmpIncludeWildcardNumber = false;
            Assertions.assertEquals(true, FilterUtils.hasValidAtomicNumber(tmpAtom, tmpIncludeWildcardNumber));
        }
    }

    /**
     * Tests whether the .hasValidAtomicNumber() method of class FilterUtils returns false if the given atom has a
     * negative atomic number; tested atomic numbers: -1, -2, -18, -100; whether the wildcard number 0 is included,
     * has no impact on the results here.
     */
    @Test
    public void hasValidAtomicNumber_returnsFalseForNegativeAtomicNumber() {
        int[] tmpAtomicNumbersArray = new int[]{
                -1,
                -2,
                -18,
                -100
        };
        IAtom tmpAtom = new Atom();
        boolean tmpIncludeWildcardNumber;
        for (int tmpAtomicNumber :
                tmpAtomicNumbersArray) {
            tmpAtom.setAtomicNumber(tmpAtomicNumber);
            tmpIncludeWildcardNumber = true;
            Assertions.assertEquals(false, FilterUtils.hasValidAtomicNumber(tmpAtom, tmpIncludeWildcardNumber));
            tmpIncludeWildcardNumber = false;
            Assertions.assertEquals(false, FilterUtils.hasValidAtomicNumber(tmpAtom, tmpIncludeWildcardNumber));
        }
    }

    /**
     * Tests whether the .hasValidAtomicNumber() method of class FilterUtils returns false if the given atom has an
     * atomic number that exceeds 118; tested atomic numbers: 119, 120, 150, 200; whether the wildcard number 0 is
     * included, has no impact on the results here.
     */
    @Test
    public void hasValidAtomicNumber_returnsFalseForAtomicNumbersGreater118() {
        int[] tmpAtomicNumbersArray = new int[]{
                119,
                120,
                150,
                200
        };
        IAtom tmpAtom = new Atom();
        boolean tmpIncludeWildcardNumber;
        for (int tmpAtomicNumber :
                tmpAtomicNumbersArray) {
            tmpAtom.setAtomicNumber(tmpAtomicNumber);
            tmpIncludeWildcardNumber = true;
            Assertions.assertEquals(false, FilterUtils.hasValidAtomicNumber(tmpAtom, tmpIncludeWildcardNumber));
            tmpIncludeWildcardNumber = false;
            Assertions.assertEquals(false, FilterUtils.hasValidAtomicNumber(tmpAtom, tmpIncludeWildcardNumber));
        }
    }

    /**
     * Tests whether the .hasValidAtomicNumber() method of class FilterUtils returns true if the given atom has an
     * atomic number of 0 and the wildcard number 0 is included, and returns false if the atomic number is 0 and the
     * wildcard number 0 is not to be included.
     */
    @Test
    public void hasValidAtomicNumber_returnsTrueOrFalseRespectivelyIfAtomicNumberIsZero() {
        IAtom tmpAtom = new Atom(IElement.Wildcard);    //atomic number: 0
        boolean tmpIncludeWildcardNumber = true;
        Assertions.assertEquals(true, FilterUtils.hasValidAtomicNumber(tmpAtom, tmpIncludeWildcardNumber));
        tmpIncludeWildcardNumber = false;
        Assertions.assertEquals(false, FilterUtils.hasValidAtomicNumber(tmpAtom, tmpIncludeWildcardNumber));
    }

    /**
     * Tests whether the .hasValidAtomicNumber() method of class FilterUtils throws a NullPointerException if the given
     * IAtom instance is null.
     */
    @Test
    public void hasValidAtomicNumber_throwsNullPointerExceptionIfGivenAtomIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    IAtom tmpAtom = null;
                    boolean tmpIncludeWildcardNumber = true;
                    Object tmpReturnValue = FilterUtils.hasValidAtomicNumber(tmpAtom, tmpIncludeWildcardNumber);
                }
        );
    }

}
