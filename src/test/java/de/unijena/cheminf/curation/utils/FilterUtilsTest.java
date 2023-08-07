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

package de.unijena.cheminf.curation.utils;

import de.unijena.cheminf.curation.TestUtils;
import de.unijena.cheminf.curation.valenceHandling.valenceModels.IValenceModel;
import de.unijena.cheminf.curation.valenceHandling.valenceModels.PubChemValenceModel;
import de.unijena.cheminf.curation.valenceHandling.valenceModels.ValenceListBasedValenceModel;
import de.unijena.cheminf.curation.valenceHandling.valenceListHandling.ValenceListMatrixWrapper;
import de.unijena.cheminf.curation.valenceHandling.valenceListHandling.ValenceListMatrixWrapperTest;
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

import java.io.IOException;

/**
 * Test class for methods of class FilterUtils.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public class FilterUtilsTest {

    //<editor-fold desc="exceedsOrEqualsAtomCount() method tests" defaultstate="collapsed">
    /**
     * Tests whether the .exceedsOrEqualsAtomCount() method of class FilterUtils returns true if given atom container
     * exceeds the given threshold considering implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void exceedsOrEqualsAtomCountTest_returnsTrue_10AtomsThresholdConsideringImplicitHs_12Atoms()
            throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        //
        int tmpAtomCountThresholdValue = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        boolean tmpConsiderPseudoAtoms = true;
        boolean tmpResult = FilterUtils.exceedsOrEqualsAtomCount(
                tmpAtomContainer, tmpAtomCountThresholdValue, tmpConsiderImplicitHydrogens, tmpConsiderPseudoAtoms
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
    public void exceedsOrEqualsAtomCountTest_returnsFalse_15AtomsThresholdConsideringImplicitHs_12Atoms()
            throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        //
        int tmpAtomCountThresholdValue = 15;
        boolean tmpConsiderImplicitHydrogens = true;
        boolean tmpConsiderPseudoAtoms = true;
        boolean tmpResult = FilterUtils.exceedsOrEqualsAtomCount(tmpAtomContainer, tmpAtomCountThresholdValue,
                tmpConsiderImplicitHydrogens, tmpConsiderPseudoAtoms);
        Assertions.assertFalse(tmpResult);
    }

    /**
     * Tests whether the .exceedsOrEqualsAtomCount() method of class FilterUtils returns true if given atom container
     * equals the given threshold considering implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void exceedsOrEqualsAtomCountTest_returnsTrue_12AtomsThresholdConsideringImplicitHs_12Atoms()
            throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        //
        int tmpAtomCountThresholdValue = 12;
        boolean tmpConsiderImplicitHydrogens = true;
        boolean tmpConsiderPseudoAtoms = true;
        boolean tmpResult = FilterUtils.exceedsOrEqualsAtomCount(tmpAtomContainer, tmpAtomCountThresholdValue,
                tmpConsiderImplicitHydrogens, tmpConsiderPseudoAtoms);
        Assertions.assertTrue(tmpResult);
    }

    /**
     * Tests whether the .exceedsOrEqualsAtomCount() method of class FilterUtils returns true if given atom container
     * exceeds the given threshold not considering implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void exceedsOrEqualsAtomCountTest_returnsTrue_4AtomsThresholdNotConsideringImplicitHs_6Atoms()
            throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        //
        int tmpAtomCountThresholdValue = 4;
        boolean tmpConsiderImplicitHydrogens = false;
        boolean tmpConsiderPseudoAtoms = true;
        boolean tmpResult = FilterUtils.exceedsOrEqualsAtomCount(tmpAtomContainer, tmpAtomCountThresholdValue,
                tmpConsiderImplicitHydrogens, tmpConsiderPseudoAtoms);
        Assertions.assertTrue(tmpResult);
    }

    /**
     * Tests whether the .exceedsOrEqualsAtomCount() method of class FilterUtils returns true if given atom container
     * falls short of the given threshold not considering implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void exceedsOrEqualsAtomCountTest_returnsFalse_10AtomsThresholdNotConsideringImplicitHs_6Atoms()
            throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        //
        int tmpAtomCountThresholdValue = 10;
        boolean tmpConsiderImplicitHydrogens = false;
        boolean tmpConsiderPseudoAtoms = true;
        boolean tmpResult = FilterUtils.exceedsOrEqualsAtomCount(tmpAtomContainer, tmpAtomCountThresholdValue,
                tmpConsiderImplicitHydrogens, tmpConsiderPseudoAtoms);
        Assertions.assertFalse(tmpResult);
    }

    /**
     * Tests whether the .exceedsOrEqualsAtomCount() method of class FilterUtils returns true if given atom container
     * equals the given threshold not considering implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void exceedsOrEqualsAtomCountTest_returnsTrue_6AtomsThresholdNotConsideringImplicitHs_6Atoms()
            throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        //
        int tmpAtomCountThresholdValue = 6;
        boolean tmpConsiderImplicitHydrogens = false;
        boolean tmpConsiderPseudoAtoms = true;
        boolean tmpResult = FilterUtils.exceedsOrEqualsAtomCount(tmpAtomContainer, tmpAtomCountThresholdValue,
                tmpConsiderImplicitHydrogens, tmpConsiderPseudoAtoms);
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
                    FilterUtils.exceedsOrEqualsAtomCount(null, 10,
                            true, true);
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
                    FilterUtils.exceedsOrEqualsAtomCount(new AtomContainer(), -1,
                            true, true);
                }
        );
    }
    //</editor-fold>

    //<editor-fold desc="exceedsOrEqualsHeavyAtomCount() method tests" defaultstate="collapsed">
    /**
     * Tests whether the .exceedsOrEqualsHeavyAtomCount() method of class FilterUtils returns true if an atom container
     * exceeds the given threshold; tested with multiple atom containers.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void exceedsOrEqualsHeavyAtomCountTest_returnsTrueIfAnAtomContainerExceedsTheThreshold()
            throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "CC(=O)O",
                "O=C=O",
                "c1ccccc1"
        );
        int tmpThresholdValue = 2;
        boolean tmpConsiderPseudoAtoms = true;
        for (IAtomContainer tmpAtomContainer : tmpAtomContainerSet.atomContainers()) {
            Assertions.assertTrue(FilterUtils.exceedsOrEqualsHeavyAtomCount(tmpAtomContainer, tmpThresholdValue,
                    tmpConsiderPseudoAtoms));
        }
    }

    /**
     * Tests whether the .exceedsOrEqualsHeavyAtomCount() method of class FilterUtils returns false if an atom container
     * falls short of the given threshold; tested with multiple atom containers.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void exceedsOrEqualsHeavyAtomCountTest_returnsFalseIfAnAtomContainerFallsShortOfTheThreshold()
            throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "CC(=O)O",
                "O=C=O",
                "c1ccccc1"
        );
        int tmpThresholdValue = 7;
        boolean tmpConsiderPseudoAtoms = true;
        for (IAtomContainer tmpAtomContainer : tmpAtomContainerSet.atomContainers()) {
            Assertions.assertFalse(FilterUtils.exceedsOrEqualsHeavyAtomCount(tmpAtomContainer, tmpThresholdValue,
                    tmpConsiderPseudoAtoms));
        }
    }

    /**
     * Tests whether the .exceedsOrEqualsHeavyAtomCount() method of class FilterUtils returns true if an atom container
     * equals the given threshold; tested with multiple atom containers.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void exceedsOrEqualsHeavyAtomCountTest_returnsTrueIfAnAtomContainerEqualsTheThreshold()
            throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "CC(=O)O",
                "O=C=O",
                "c1ccccc1"
        );
        int[] tmpThresholdValueArray = new int[]{4, 3, 6};
        boolean tmpConsiderPseudoAtoms = true;
        IAtomContainer tmpAtomContainer;
        for (int i = 0; i < tmpAtomContainerSet.getAtomContainerCount(); i++) {
            tmpAtomContainer = tmpAtomContainerSet.getAtomContainer(i);
            Assertions.assertTrue(FilterUtils.exceedsOrEqualsHeavyAtomCount(tmpAtomContainer, tmpThresholdValueArray[i],
                    tmpConsiderPseudoAtoms));
        }
    }

    /**
     * Tests whether the .exceedsOrEqualsHeavyAtomCount() method of class FilterUtils throws a NullPointerException if
     * the given IAtomContainer instance is null.
     */
    @Test
    public void exceedsOrEqualsHeavyAtomCountTest_throwsNullPointerExceptionIfGivenAtomContainerIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    FilterUtils.exceedsOrEqualsHeavyAtomCount(null, 10, true);
                }
        );
    }

    /**
     * Tests whether the .exceedsOrEqualsHeavyAtomCount() method of class FilterUtils throws an IllegalArgumentException
     * if the given threshold (integer value) is of a negative value.
     */
    @Test
    public void exceedsOrEqualsHeavyAtomCountTest_throwsIllegalArgumentExceptionIfGivenThresholdValueIsNegative() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    int tmpSomeNegativeInteger = -1;
                    boolean tmpConsiderPseudoAtoms = true;
                    FilterUtils.exceedsOrEqualsHeavyAtomCount(new AtomContainer(), tmpSomeNegativeInteger,
                            tmpConsiderPseudoAtoms);
                }
        );
    }
    //</editor-fold>

    //<editor-fold desc="exceedsOrEqualsBondCount() method tests" defaultstate="collapsed">
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
        boolean tmpConsiderPseudoAtoms = true;
        Assertions.assertTrue(FilterUtils.exceedsOrEqualsBondCount(tmpAtomContainer, tmpBondCountThresholdValue,
                tmpConsiderImplicitHydrogens, tmpConsiderPseudoAtoms));
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
        boolean tmpConsiderPseudoAtoms = true;
        Assertions.assertFalse(FilterUtils.exceedsOrEqualsBondCount(tmpAtomContainer, tmpBondCountThresholdValue,
                tmpConsiderImplicitHydrogens, tmpConsiderPseudoAtoms));
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
        boolean tmpConsiderPseudoAtoms = true;
        Assertions.assertTrue(FilterUtils.exceedsOrEqualsBondCount(tmpAtomContainer, tmpBondCountThresholdValue,
                tmpConsiderImplicitHydrogens, tmpConsiderPseudoAtoms));
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
        boolean tmpConsiderPseudoAtoms = true;
        Assertions.assertTrue(FilterUtils.exceedsOrEqualsBondCount(tmpAtomContainer, tmpBondCountThresholdValue,
                tmpConsiderImplicitHydrogens, tmpConsiderPseudoAtoms));
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
        boolean tmpConsiderPseudoAtoms = true;
        Assertions.assertFalse(FilterUtils.exceedsOrEqualsBondCount(tmpAtomContainer, tmpBondCountThresholdValue,
                tmpConsiderImplicitHydrogens, tmpConsiderPseudoAtoms));
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
        boolean tmpConsiderPseudoAtoms = true;
        Assertions.assertTrue(FilterUtils.exceedsOrEqualsBondCount(tmpAtomContainer, tmpBondCountThresholdValue,
                tmpConsiderImplicitHydrogens, tmpConsiderPseudoAtoms));
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
                    FilterUtils.exceedsOrEqualsBondCount(null, 10,
                            true, true);
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
                    FilterUtils.exceedsOrEqualsBondCount(new AtomContainer(), -1,
                            true, true);
                }
        );
    }
    //</editor-fold>

    //<editor-fold desc="exceedsOrEqualsBondsOfSpecificBondOrderCount() method tests" defaultstate="collapsed">
    /**
     * Tests whether the .exceedsOrEqualsBondsOfSpecificBondOrderCount() method of class FilterUtils returns true if
     * the given atom container exceeds the given threshold for bonds of bond order single considering and not
     * considering bonds to implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void exceedsOrEqualsBondsOfSpecificBondOrderCountTest_returnsTrue_singleBonds_thresholdExceeded()
            throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("CCO");   //8 (2)
        IBond.Order tmpBondOrder = IBond.Order.SINGLE;
        //
        boolean tmpConsiderImplicitHydrogens = true;
        boolean tmpConsiderPseudoAtoms = true;
        int tmpBondCountThresholdValue = 5;
        Assertions.assertTrue(FilterUtils.exceedsOrEqualsBondsOfSpecificBondOrderCount(
                tmpAtomContainer, tmpBondOrder, tmpBondCountThresholdValue,
                tmpConsiderImplicitHydrogens, tmpConsiderPseudoAtoms
        ));
        tmpConsiderImplicitHydrogens = false;
        tmpBondCountThresholdValue = 1;
        Assertions.assertTrue(FilterUtils.exceedsOrEqualsBondsOfSpecificBondOrderCount(
                tmpAtomContainer, tmpBondOrder, tmpBondCountThresholdValue,
                tmpConsiderImplicitHydrogens, tmpConsiderPseudoAtoms
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
    public void exceedsOrEqualsBondsOfSpecificBondOrderCountTest_returnsFalse_singleBonds_thresholdNotExceeded()
            throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("CCO");   //8 (2)
        IBond.Order tmpBondOrder = IBond.Order.SINGLE;
        //
        boolean tmpConsiderImplicitHydrogens = true;
        boolean tmpConsiderPseudoAtoms = true;
        int tmpBondCountThresholdValue = 10;
        Assertions.assertFalse(FilterUtils.exceedsOrEqualsBondsOfSpecificBondOrderCount(
                tmpAtomContainer, tmpBondOrder, tmpBondCountThresholdValue,
                tmpConsiderImplicitHydrogens, tmpConsiderPseudoAtoms
        ));
        tmpConsiderImplicitHydrogens = false;
        tmpBondCountThresholdValue = 4;
        Assertions.assertFalse(FilterUtils.exceedsOrEqualsBondsOfSpecificBondOrderCount(
                tmpAtomContainer, tmpBondOrder, tmpBondCountThresholdValue,
                tmpConsiderImplicitHydrogens, tmpConsiderPseudoAtoms
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
    public void exceedsOrEqualsBondsOfSpecificBondOrderCountTest_returnsTrue_singleBonds_thresholdEqualed()
            throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("CCO");   //8 (2)
        IBond.Order tmpBondOrder = IBond.Order.SINGLE;
        //
        boolean tmpConsiderImplicitHydrogens = true;
        boolean tmpConsiderPseudoAtoms = true;
        int tmpBondCountThresholdValue = 8;
        Assertions.assertTrue(FilterUtils.exceedsOrEqualsBondsOfSpecificBondOrderCount(
                tmpAtomContainer, tmpBondOrder, tmpBondCountThresholdValue,
                tmpConsiderImplicitHydrogens, tmpConsiderPseudoAtoms
        ));
        tmpConsiderImplicitHydrogens = false;
        tmpBondCountThresholdValue = 2;
        Assertions.assertTrue(FilterUtils.exceedsOrEqualsBondsOfSpecificBondOrderCount(
                tmpAtomContainer, tmpBondOrder, tmpBondCountThresholdValue,
                tmpConsiderImplicitHydrogens, tmpConsiderPseudoAtoms
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
        boolean tmpConsiderPseudoAtoms = true;
        //
        for (IBond.Order tmpBondOrder : tmpBondOrderArray) {
            tmpBond.setOrder(tmpBondOrder);
            tmpThresholdValue = 0;  //exceeded
            Assertions.assertTrue(FilterUtils.exceedsOrEqualsBondsOfSpecificBondOrderCount(
                    tmpAtomContainer, tmpBondOrder, tmpThresholdValue,
                    tmpConsiderImplicitHydrogens, tmpConsiderPseudoAtoms
            ));
            tmpThresholdValue = 1;  //equaled
            Assertions.assertTrue(FilterUtils.exceedsOrEqualsBondsOfSpecificBondOrderCount(
                    tmpAtomContainer, tmpBondOrder, tmpThresholdValue,
                    tmpConsiderImplicitHydrogens, tmpConsiderPseudoAtoms
            ));
            tmpThresholdValue = 2;  //not exceeded
            Assertions.assertFalse(FilterUtils.exceedsOrEqualsBondsOfSpecificBondOrderCount(
                    tmpAtomContainer, tmpBondOrder, tmpThresholdValue,
                    tmpConsiderImplicitHydrogens, tmpConsiderPseudoAtoms
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
                    FilterUtils.exceedsOrEqualsBondsOfSpecificBondOrderCount(null, null,
                            10, true, true);
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
                    FilterUtils.exceedsOrEqualsBondsOfSpecificBondOrderCount(new AtomContainer(), null,
                            -1, true, true);
                }
        );
    }
    //</editor-fold>

    //<editor-fold desc="hasValidAtomicNumber(IAtom, boolean) method tests" defaultstate="collapsed">
    /**
     * Tests whether the .hasValidAtomicNumber() method of class FilterUtils returns true if the given atom has a valid
     * atomic number; tested atomic numbers: 1, 2, 8, 10, 118; it is made sure, that whether the wildcard number 0 is
     * included, has no impact on the result here.
     */
    @Test
    public void hasValidAtomicNumberTest_returnsTrueForDifferentValidAtomicNumbers() {
        int[] tmpAtomicNumbersArray = new int[]{
                IElement.H,     //atomic number: 1
                IElement.He,    //atomic number: 2
                IElement.O,     //atomic number: 8
                IElement.Ne,    //atomic number: 10
                IElement.Og     //atomic number: 118
        };
        IAtom tmpAtom = new Atom();
        boolean tmpIncludeWildcardNumber;
        for (int tmpAtomicNumber : tmpAtomicNumbersArray) {
            tmpAtom.setAtomicNumber(tmpAtomicNumber);
            tmpIncludeWildcardNumber = true;
            Assertions.assertTrue(FilterUtils.hasValidAtomicNumber(tmpAtom, tmpIncludeWildcardNumber));
            tmpIncludeWildcardNumber = false;
            Assertions.assertTrue(FilterUtils.hasValidAtomicNumber(tmpAtom, tmpIncludeWildcardNumber));
        }
    }

    /**
     * Tests whether the .hasValidAtomicNumber() method of class FilterUtils returns false if the given atom has a
     * negative atomic number; tested atomic numbers: -1, -2, -18, -100; whether the wildcard number 0 is included,
     * has no impact on the results here.
     */
    @Test
    public void hasValidAtomicNumberTest_returnsFalseForNegativeAtomicNumber() {
        int[] tmpAtomicNumbersArray = new int[]{
                -1,
                -2,
                -18,
                -100
        };
        IAtom tmpAtom = new Atom();
        boolean tmpIncludeWildcardNumber;
        for (int tmpAtomicNumber : tmpAtomicNumbersArray) {
            tmpAtom.setAtomicNumber(tmpAtomicNumber);
            tmpIncludeWildcardNumber = true;
            Assertions.assertFalse(FilterUtils.hasValidAtomicNumber(tmpAtom, tmpIncludeWildcardNumber));
            tmpIncludeWildcardNumber = false;
            Assertions.assertFalse(FilterUtils.hasValidAtomicNumber(tmpAtom, tmpIncludeWildcardNumber));
        }
    }

    /**
     * Tests whether the .hasValidAtomicNumber() method of class FilterUtils returns false if the given atom has an
     * atomic number that exceeds 118; tested atomic numbers: 119, 120, 150, 200; whether the wildcard number 0 is
     * included, has no impact on the results here.
     */
    @Test
    public void hasValidAtomicNumberTest_returnsFalseForAtomicNumbersGreater118() {
        int[] tmpAtomicNumbersArray = new int[]{
                119,
                120,
                150,
                200
        };
        IAtom tmpAtom = new Atom();
        boolean tmpIncludeWildcardNumber;
        for (int tmpAtomicNumber : tmpAtomicNumbersArray) {
            tmpAtom.setAtomicNumber(tmpAtomicNumber);
            tmpIncludeWildcardNumber = true;
            Assertions.assertFalse(FilterUtils.hasValidAtomicNumber(tmpAtom, tmpIncludeWildcardNumber));
            tmpIncludeWildcardNumber = false;
            Assertions.assertFalse(FilterUtils.hasValidAtomicNumber(tmpAtom, tmpIncludeWildcardNumber));
        }
    }

    /**
     * Tests whether the .hasValidAtomicNumber() method of class FilterUtils returns true if the given atom has an
     * atomic number of 0 and the wildcard number 0 is included, and returns false if the atomic number is 0 and the
     * wildcard number 0 is not to be included.
     */
    @Test
    public void hasValidAtomicNumberTest_returnsTrueOrFalseRespectivelyIfAtomicNumberIsZero() {
        IAtom tmpAtom = new Atom(IElement.Wildcard);    //atomic number: 0
        boolean tmpIncludeWildcardNumber = true;
        Assertions.assertTrue(FilterUtils.hasValidAtomicNumber(tmpAtom, tmpIncludeWildcardNumber));
        tmpIncludeWildcardNumber = false;
        Assertions.assertFalse(FilterUtils.hasValidAtomicNumber(tmpAtom, tmpIncludeWildcardNumber));
    }

    /**
     * Tests whether the .hasValidAtomicNumber() method of class FilterUtils throws a NullPointerException if the given
     * IAtom instance is null.
     */
    @Test
    public void hasValidAtomicNumberTest_atomIsNull_throwsNullPointerException() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    IAtom tmpAtom = null;
                    boolean tmpIncludeWildcardNumber = true;
                    FilterUtils.hasValidAtomicNumber(tmpAtom, tmpIncludeWildcardNumber);
                }
        );
    }

    /**
     * Tests whether the .hasValidAtomicNumber() method of class FilterUtils throws a NullPointerException if the atomic
     * number of the given IAtom instance is null; the boolean param has no impact on this.
     */
    @Test
    public void hasValidAtomicNumberTest_atomicNumberNull_throwsNullPointerException() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    IAtom tmpAtom = new Atom(); //has atomic number null
                    boolean tmpIncludeWildcardNumber = true;
                    FilterUtils.hasValidAtomicNumber(tmpAtom, tmpIncludeWildcardNumber);
                }
        );
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    IAtom tmpAtom = new Atom(); //has atomic number null
                    boolean tmpIncludeWildcardNumber = false;
                    FilterUtils.hasValidAtomicNumber(tmpAtom, tmpIncludeWildcardNumber);
                }
        );
    }
    //</editor-fold>

    //<editor-fold desc="hasValidAtomicNumber(IAtomContainer, boolean) method tests" defaultstate="collapsed">
    /**
     * Tests whether the .hasAllValidAtomicNumbers() method of class FilterUtils returns true if all atoms of the given
     * atom container have a valid atomic number; whether the wildcard atomic number is included has no impact here.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void hasAllValidAtomicNumbersTest_returnsTrueIfAllAtomicNumbersAreValid() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "NCC(=O)O",
                "C=CC=C",
                "C#N"
        );
        boolean tmpIncludeWildcardNumber;
        for (IAtomContainer tmpAtomContainer : tmpAtomContainerSet.atomContainers()) {
            tmpIncludeWildcardNumber = true;    //has no impact here
            Assertions.assertTrue(FilterUtils.hasAllValidAtomicNumbers(tmpAtomContainer, tmpIncludeWildcardNumber));
            tmpIncludeWildcardNumber = false;   //has no impact here
            Assertions.assertTrue(FilterUtils.hasAllValidAtomicNumbers(tmpAtomContainer, tmpIncludeWildcardNumber));
        }
    }

    /**
     * Tests whether the .hasAllValidAtomicNumbers() method of class FilterUtils returns true if all atoms of the given
     * atom container have a valid atomic number; whether the wildcard atomic number is included has no impact here.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void hasAllValidAtomicNumbersTest_returnsFalseIfAnAtomicNumberIsNegative() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "NCC(=O)O",
                "C=CC=C",
                "C#N"
        );
        IAtom tmpAtom = new Atom();
        //AtomContainer 0: additional atom with negative atomic number
        tmpAtom.setAtomicNumber(-1);
        tmpAtomContainerSet.getAtomContainer(0).addAtom(tmpAtom);
        //AtomContainer 1: two additional atoms with negative atomic numbers
        tmpAtom.setAtomicNumber(-5);
        tmpAtomContainerSet.getAtomContainer(1).addAtom(tmpAtom);
        tmpAtom.setAtomicNumber(-10);
        tmpAtomContainerSet.getAtomContainer(1).addAtom(tmpAtom);
        //AtomContainer 2: one of the existing atoms with negative atomic number
        tmpAtomContainerSet.getAtomContainer(2).getAtom(0).setAtomicNumber(-1);
        //
        boolean tmpIncludeWildcardNumber;
        for (IAtomContainer tmpAtomContainer : tmpAtomContainerSet.atomContainers()) {
            tmpIncludeWildcardNumber = true;    //has no impact
            Assertions.assertFalse(FilterUtils.hasAllValidAtomicNumbers(tmpAtomContainer, tmpIncludeWildcardNumber));
            tmpIncludeWildcardNumber = false;   //has no impact
            Assertions.assertFalse(FilterUtils.hasAllValidAtomicNumbers(tmpAtomContainer, tmpIncludeWildcardNumber));
        }
    }

    /**
     * Tests whether the .hasAllValidAtomicNumbers() method of class FilterUtils returns true for an atom container
     * having atoms with atomic numbers between 0 and 118 (both limits included) if the wildcard number zero should be
     * included / seen as valid and returns false if not.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void hasAllValidAtomicNumbersTest_testOfIncludeWildcardNumber() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("O=C=O");
        tmpAtomContainer.getAtom(1).setAtomicNumber(0);
        boolean tmpIncludeWildcardNumber = true;
        Assertions.assertTrue(FilterUtils.hasAllValidAtomicNumbers(tmpAtomContainer, tmpIncludeWildcardNumber));
        tmpIncludeWildcardNumber = false;
        Assertions.assertFalse(FilterUtils.hasAllValidAtomicNumbers(tmpAtomContainer, tmpIncludeWildcardNumber));
    }

    /**
     * Tests whether the .hasAllValidAtomicNumbers() method of class FilterUtils throws a NullPointerException if the
     * given IAtomContainer instance is null.
     */
    @Test
    public void hasAllValidAtomicNumbersTest_atomContainerIsNull_throwsNullPointerException() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    IAtomContainer tmpAtomContainer = null;
                    boolean tmpIncludeWildcardNumber = true;    //has no impact
                    FilterUtils.hasAllValidAtomicNumbers(tmpAtomContainer, tmpIncludeWildcardNumber);
                }
        );
    }

    /**
     * Tests whether the .hasAllValidAtomicNumbers() method of class FilterUtils throws a NullPointerException if the
     * given IAtomContainer instance contains IAtom instances with their atomic number being null.
     */
    @Test
    public void hasAllValidAtomicNumbersTest_atomicNumberIsNull_throwsNullPointerException() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    IAtomContainer tmpAtomContainer = new AtomContainer();
                    IAtom tmpAtom = new Atom(); //has atomic number null
                    tmpAtomContainer.addAtom(tmpAtom);
                    boolean tmpIncludeWildcardNumber = true;    //has no impact
                    FilterUtils.hasAllValidAtomicNumbers(tmpAtomContainer, tmpIncludeWildcardNumber);
                }
        );
    }
    //</editor-fold>

    //<editor-fold desc="hasAllValidValences() method tests" defaultstate="collapsed">
    /**
     * Tests whether the .hasAllValidValences() method of class FilterUtils returns true if all atoms of the given
     * atom container have a valence considered as valid (according to the valence model).
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void hasAllValidValencesTest_allValidValences_returnsTrue() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "O=[N+]([O-])[O-]", // nitrate
                "CC(C)(C)C",        // tetravalent carbon
                "CC(O)=O",          // acetic acid
                "[O-]H",            // hydroxide
                "[H+]"              // proton
        );
        boolean tmpWildcardAtomicNumberIsValid = false;
        IValenceModel tmpValenceModel = new PubChemValenceModel();
        //
        boolean tmpExpectedValue = true;
        for (IAtomContainer tmpAtomContainer : tmpAtomContainerSet.atomContainers()) {
            Assertions.assertEquals(tmpExpectedValue, FilterUtils.hasAllValidValences(tmpAtomContainer,
                    tmpWildcardAtomicNumberIsValid, tmpValenceModel));
        }
    }

    /**
     * Tests whether the .hasAllValidValences() method of class FilterUtils returns false if an atom of the given
     * atom container has a valence considered as invalid (according to the valence model).
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void hasAllValidValencesTest_incorrectValences_returnsFalse() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "O=[N]([O-])[O-]",  // uncharged tetravalent nitrogen
                "O=N(O)=O",         // pentavalent nitrogen
                "CC(C)(C)(C)C",     // pentavalent carbon
                "HFH",              // divalent fluor
                "[H++]"             // two times positive charged hydrogen
        );
        boolean tmpWildcardAtomicNumberIsValid = false;
        IValenceModel tmpValenceModel = new PubChemValenceModel();
        //
        boolean tmpExpectedValue = false;
        for (IAtomContainer tmpAtomContainer : tmpAtomContainerSet.atomContainers()) {
            Assertions.assertEquals(tmpExpectedValue, FilterUtils.hasAllValidValences(tmpAtomContainer,
                    tmpWildcardAtomicNumberIsValid, tmpValenceModel));
        }
    }

    /**
     * Tests whether the .hasAllValidValences() method of class FilterUtils returns true if an atom of the given atom
     * container contains a pseudo-atom with wildcard atomic number (zero) and atoms with wildcard atomic number are
     * generally considered as valid.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void hasAllValidValencesTest_considerWildcardAtomicNumberAsValid_structureWithRGroup_returnsTrue()
            throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("CC(=O)O[R]");
        boolean tmpWildcardAtomicNumberIsValid = true;
        IValenceModel tmpValenceModel = new PubChemValenceModel();
        //
        boolean tmpExpectedValue = true;
        Assertions.assertEquals(tmpExpectedValue, FilterUtils.hasAllValidValences(tmpAtomContainer,
                tmpWildcardAtomicNumberIsValid, tmpValenceModel));
    }

    /**
     * Tests whether the .hasAllValidValences() method of class FilterUtils returns false if an atom of the given atom
     * container contains a pseudo-atom with wildcard atomic number (zero), atoms with wildcard atomic number are
     * not generally considered as valid and the valence model does not cover the wildcard atomic number.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void hasAllValidValencesTest_notConsiderWildcardAtomicNumberAsValid_structureWithRGroup_returnsFalse()
            throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("CC(=O)O[R]");
        boolean tmpWildcardAtomicNumberIsValid = false;
        IValenceModel tmpValenceModel = new PubChemValenceModel();
        //
        boolean tmpExpectedValue = false;
        Assertions.assertEquals(tmpExpectedValue, FilterUtils.hasAllValidValences(tmpAtomContainer,
                tmpWildcardAtomicNumberIsValid, tmpValenceModel));
    }

    /**
     * Tests whether the .hasAllValidValences() method of class FilterUtils returns true if an atom of the given atom
     * container contains a pseudo-atom with wildcard atomic number (zero), atoms with wildcard atomic number are
     * not generally considered as valid but the valence model covers the specific atom configuration with wildcard
     * atomic number.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void hasAllValidValencesTest_wildcardInvalid_valenceModelAllowsSingleBondedPseudoAtoms_structureWithRGroup_returnsTrue()
            throws InvalidSmilesException, IOException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("CC(=O)O[R]");
        boolean tmpWildcardAtomicNumberIsValid = false;
        ValenceListMatrixWrapper tmpTestValenceListMatrixWrapper = new ValenceListMatrixWrapper(
                ValenceListMatrixWrapperTest.TEST_VALENCE_LIST_FILE_PATH_STRING,
                ValenceListMatrixWrapperTest.NUMBER_OF_LINES_IN_TEST_FILE
        );
        IValenceModel tmpValenceModel = new ValenceListBasedValenceModel(tmpTestValenceListMatrixWrapper);
        //
        // the test valence list covers atoms with atomic number zero and one sigma bond
        // therefore the R-group is considered as valid
        boolean tmpExpectedValue = true;
        Assertions.assertEquals(tmpExpectedValue, FilterUtils.hasAllValidValences(tmpAtomContainer,
                tmpWildcardAtomicNumberIsValid, tmpValenceModel));
    }
    //</editor-fold>

}
