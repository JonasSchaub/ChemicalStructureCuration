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
import de.unijena.cheminf.curation.enums.MassComputationFlavours;
import de.unijena.cheminf.curation.utils.ChemUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.Bond;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IElement;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

/**
 * Test class for methods of class ChemUtils.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public class ChemUtilsTest {

    //<editor-fold desc="countAtoms() method tests" defaultstate="collapsed">
    /**
     * Tests whether the .countAtoms() method of the class ChemUtils counts the number of atoms of benzene correct
     * considering implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void countAtomsTest_consideringImplicitHydrogens_12atoms() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        //
        boolean tmpConsiderImplicitHydrogens = true;
        int tmpAtomCount = ChemUtils.countAtoms(tmpAtomContainer, tmpConsiderImplicitHydrogens);
        Assertions.assertEquals(12, tmpAtomCount);
    }

    /**
     * Tests whether the .countAtoms() method of the class ChemUtils counts the number of atoms of benzene correct
     * not considering implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void countAtomsTest_notConsideringImplicitHydrogens_6atoms() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        //
        boolean tmpConsiderImplicitHydrogens = false;
        int tmpAtomCount = ChemUtils.countAtoms(tmpAtomContainer, tmpConsiderImplicitHydrogens);
        Assertions.assertEquals(6, tmpAtomCount);
    }

    /**
     * Tests whether the .countAtoms() method of the class ChemUtils counts the number of atoms of glycine correct
     * not considering implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void countAtomsTest_notConsideringImplicitHydrogens_5atoms() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("NCC(=O)O");
        //
        boolean tmpConsiderImplicitHydrogens = false;
        int tmpAtomCount = ChemUtils.countAtoms(tmpAtomContainer, tmpConsiderImplicitHydrogens);
        Assertions.assertEquals(5, tmpAtomCount);
    }

    /**
     * Tests whether the .countAtoms() method of the class ChemUtils counts the number of atoms of glycine correct
     * considering implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void countAtomsTest_consideringImplicitHydrogens_10atoms() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("NCC(=O)O");
        //
        boolean tmpConsiderImplicitHydrogens = true;
        int tmpAtomCount = ChemUtils.countAtoms(tmpAtomContainer, tmpConsiderImplicitHydrogens);
        Assertions.assertEquals(10, tmpAtomCount);
    }

    /**
     * Tests whether the .countAtoms() method of the class ChemUtils throws a NullPointerException if the given
     * IAtomContainer instance is null.
     */
    @Test
    public void countAtomsTest_throwsNullPointerExceptionIfGivenAtomContainerIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    ChemUtils.countAtoms(null, true);
                }
        );
    }
    //</editor-fold>

    //<editor-fold desc="countImplicitHydrogens() method tests" defaultstate="collapsed">
    /**
     * Tests whether the .countImplicitHydrogens() method of the class ChemUtils returns 6 when given an atom
     * container with 6 implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void countImplicitHydrogensTest_6ImplicitHydrogens() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        //
        Assertions.assertEquals(6, ChemUtils.countImplicitHydrogens(tmpAtomContainer));
    }

    /**
     * Tests whether the .countImplicitHydrogens() method of the class ChemUtils returns 6 when given an atom
     * container with 6 implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void countImplicitHydrogensTest_5ImplicitHydrogens() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("NCC(=O)O");
        //
        Assertions.assertEquals(5, ChemUtils.countImplicitHydrogens(tmpAtomContainer));
    }

    /**
     * Tests whether the .countImplicitHydrogens() method of the class ChemUtils throws a NullPointerException if the
     * given IAtomContainer instance is null.
     */
    @Test
    public void countImplicitHydrogensTest_throwsNullPointerExceptionIfGivenAtomContainerIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    ChemUtils.countImplicitHydrogens(null);
                }
        );
    }
    //</editor-fold>

    //<editor-fold desc="countExplicitHydrogens() method tests" defaultstate="collapsed">
    /**
     * Tests whether the .countExplicitHydrogens() method of the class ChemUtils returns 6 when given an atom
     * container with 6 explicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void countExplicitHydrogensTest_4ExplicitHydrogens() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("HC(H)(H)H");
        //
        Assertions.assertEquals(4, ChemUtils.countExplicitHydrogens(tmpAtomContainer));
    }

    /**
     * Tests whether the .countExplicitHydrogens() method of the class ChemUtils returns 6 when given an atom
     * container with 6 implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void countExplicitHydrogensTest_2Implicit_3ExplicitHydrogens() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("HN(H)CC(=O)OH");
        //
        Assertions.assertEquals(3, ChemUtils.countExplicitHydrogens(tmpAtomContainer));
    }

    /**
     * Tests whether the .countExplicitHydrogens() method of the class ChemUtils throws a NullPointerException if the
     * given IAtomContainer instance is null.
     */
    @Test
    public void countExplicitHydrogensTest_throwsNullPointerExceptionIfGivenAtomContainerIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    ChemUtils.countExplicitHydrogens(null);
                }
        );
    }
    //</editor-fold>

    //<editor-fold desc="countBonds() method tests" defaultstate="collapsed">
    /**
     * Tests whether the .countBonds() method of class ChemUtils returns the correct bond count not considering
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
        Assertions.assertEquals(tmpBondCount, ChemUtils.countBonds(tmpAtomContainer, tmpConsiderImplicitHydrogens));
    }

    /**
     * Tests whether the .countBonds() method of class ChemUtils returns the correct bond count not considering
     * implicit hydrogen atoms; test 2.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void countBondsMethodTest_notConsideringImplicitHydrogens_test2_5Bonds() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString(
                "C1CCCC1"   //5
        );
        int tmpBondCount = 5;
        boolean tmpConsiderImplicitHydrogens = false;
        Assertions.assertEquals(tmpBondCount, ChemUtils.countBonds(tmpAtomContainer, tmpConsiderImplicitHydrogens));
    }

    /**
     * Tests whether the .countBonds() method of class ChemUtils returns the correct bond count not considering
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
            Assertions.assertEquals(tmpBondCountArray[i], ChemUtils.countBonds(tmpAtomContainer, tmpConsiderImplicitHydrogens));
        }
    }

    /**
     * Tests whether the .countBonds() method of class ChemUtils returns the correct bond count considering implicit
     * hydrogen atoms; test 1.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void countBondsMethodTest_consideringImplicitHydrogens_test1_7Bonds() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString(
                "CC"    //7
        );
        int tmpBondCount = 7;
        boolean tmpConsiderImplicitHydrogens = true;
        Assertions.assertEquals(tmpBondCount, ChemUtils.countBonds(tmpAtomContainer, tmpConsiderImplicitHydrogens));
    }

    /**
     * Tests whether the .countBonds() method of class ChemUtils returns the correct bond count considering implicit
     * hydrogen atoms; test 2.
     *
     * @throws InvalidSmilesException if the SMILES string could not be parsed
     */
    @Test
    public void countBondsMethodTest_consideringImplicitHydrogens_test2_15Bonds() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString(
                "C1CCCC1"   //15
        );
        int tmpBondCount = 15;
        boolean tmpConsiderImplicitHydrogens = true;
        Assertions.assertEquals(tmpBondCount, ChemUtils.countBonds(tmpAtomContainer, tmpConsiderImplicitHydrogens));
    }

    /**
     * Tests whether the .countBonds() method of class ChemUtils returns the correct bond count considering implicit
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
            Assertions.assertEquals(tmpBondCountArray[i], ChemUtils.countBonds(tmpAtomContainer, tmpConsiderImplicitHydrogens));
        }
    }

    /**
     * Tests whether the .countBonds() method of class ChemUtils throws a NullPointerException if the given
     * IAtomContainer instance is null.
     */
    @Test
    public void countBondsMethodTest_throwsNullPointerExceptionIfGivenAtomContainerIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    boolean tmpConsiderImplicitHydrogens = true;
                    ChemUtils.countBonds(null, tmpConsiderImplicitHydrogens);
                }
        );
    }
    //</editor-fold>

    //<editor-fold desc="countBondsOfSpecificOrder() method tests" defaultstate="collapsed">
    /**
     * Tests whether the .countBondsOfSpecificOrder() method of class ChemUtils returns the correct result when
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
        Assertions.assertEquals(tmpSingleBondCount, ChemUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, tmpBondOrder, tmpConsiderImplicitHydrogens));
        tmpAtomContainer = TestUtils.parseSmilesString("C=CC=C");
        tmpSingleBondCount = 1;
        Assertions.assertEquals(tmpSingleBondCount, ChemUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, tmpBondOrder, tmpConsiderImplicitHydrogens));
    }

    /**
     * Tests whether the .countBondsOfSpecificOrder() method of class ChemUtils returns the correct result when
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
        Assertions.assertEquals(tmpSingleBondCount, ChemUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, tmpBondOrder, tmpConsiderImplicitHydrogens));
        tmpAtomContainer = TestUtils.parseSmilesString("C=CC=C");
        tmpSingleBondCount = 7;
        Assertions.assertEquals(tmpSingleBondCount, ChemUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, tmpBondOrder, tmpConsiderImplicitHydrogens));
    }

    /**
     * Tests whether the .countBondsOfSpecificOrder() method of class ChemUtils returns the correct result when
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
        Assertions.assertEquals(tmpDoubleBondCount, ChemUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, tmpBondOrder, tmpConsiderImplicitHydrogens));
        tmpAtomContainer = TestUtils.parseSmilesString("C=CC=CC=CC(=O)O");
        tmpDoubleBondCount = 4;
        Assertions.assertEquals(tmpDoubleBondCount, ChemUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, tmpBondOrder, tmpConsiderImplicitHydrogens));
    }

    /**
     * Tests whether the .countBondsOfSpecificOrder() method of class ChemUtils returns the correct result when
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
        Assertions.assertEquals(tmpTripleBondCount, ChemUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, tmpBondOrder, tmpConsiderImplicitHydrogens));
        tmpAtomContainer = TestUtils.parseSmilesString("O=C(O)C=CCC#CC#CCC#C");
        tmpTripleBondCount = 3;
        Assertions.assertEquals(tmpTripleBondCount, ChemUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, tmpBondOrder, tmpConsiderImplicitHydrogens));
    }

    /**
     * Tests whether the .countBondsOfSpecificOrder() method of class ChemUtils returns the correct result when
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
            Assertions.assertEquals(tmpSpecificBondCount, ChemUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, tmpBondOrder, tmpConsiderImplicitHydrogens));
            tmpBond = new Bond();
            tmpBond.setOrder(tmpBondOrder);
            tmpAtomContainer.addBond(tmpBond);
            tmpSpecificBondCount = 1;
            Assertions.assertEquals(tmpSpecificBondCount, ChemUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, tmpBondOrder, tmpConsiderImplicitHydrogens));
        }
    }

    /**
     * Tests whether the .countBondsOfSpecificOrder() method of class ChemUtils returns the correct result when
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
        Assertions.assertEquals(tmpUnsetBondsCount, ChemUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, tmpBondOrder, tmpConsiderImplicitHydrogens));
        tmpAtomContainer = TestUtils.parseSmilesString("CC(=O)C");
        tmpUnsetBondsCount = 3;
        for (int i = 0; i < tmpUnsetBondsCount; i++) {
            tmpAtomContainer.addBond(tmpBond);
        }
        Assertions.assertEquals(tmpUnsetBondsCount, ChemUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, tmpBondOrder, tmpConsiderImplicitHydrogens));
    }

    /**
     * Tests whether the .countBondsOfSpecificOrder() method of class ChemUtils returns the correct result when counting bonds
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
        Assertions.assertEquals(tmpUndefinedBondsCount, ChemUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, tmpBondOrder, tmpConsiderImplicitHydrogens));
        tmpAtomContainer = TestUtils.parseSmilesString("CC(=O)C");
        tmpUndefinedBondsCount = 3;
        for (int i = 0; i < tmpUndefinedBondsCount; i++) {
            tmpAtomContainer.addBond(tmpBond);
        }
        Assertions.assertEquals(tmpUndefinedBondsCount, ChemUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, tmpBondOrder, tmpConsiderImplicitHydrogens));
    }

    /**
     * Tests how the .countBondsOfSpecificOrder() method of class ChemUtils reacts to aromatic systems not considering bonds to
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
        Assertions.assertEquals(tmpExpectedSingleBondCount, ChemUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, IBond.Order.SINGLE, tmpConsiderImplicitHydrogens));
        Assertions.assertEquals(tmpExpectedDoubleBondCount, ChemUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, IBond.Order.DOUBLE, tmpConsiderImplicitHydrogens));
        tmpAtomContainer = TestUtils.parseSmilesString("c1cccc2ccccc12");
        tmpExpectedSingleBondCount = 6;
        tmpExpectedDoubleBondCount = 5;
        Assertions.assertEquals(tmpExpectedSingleBondCount, ChemUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, IBond.Order.SINGLE, tmpConsiderImplicitHydrogens));
        Assertions.assertEquals(tmpExpectedDoubleBondCount, ChemUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, IBond.Order.DOUBLE, tmpConsiderImplicitHydrogens));
    }

    /**
     * Tests how the .countBondsOfSpecificOrder() method of class ChemUtils reacts to aromatic systems considering bonds to
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
        Assertions.assertEquals(tmpExpectedSingleBondCount, ChemUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, IBond.Order.SINGLE, tmpConsiderImplicitHydrogens));
        Assertions.assertEquals(tmpExpectedDoubleBondCount, ChemUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, IBond.Order.DOUBLE, tmpConsiderImplicitHydrogens));
        tmpAtomContainer = TestUtils.parseSmilesString("c1cccc2ccccc12");
        tmpExpectedSingleBondCount = 14;
        tmpExpectedDoubleBondCount = 5;
        Assertions.assertEquals(tmpExpectedSingleBondCount, ChemUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, IBond.Order.SINGLE, tmpConsiderImplicitHydrogens));
        Assertions.assertEquals(tmpExpectedDoubleBondCount, ChemUtils.countBondsOfSpecificBondOrder(tmpAtomContainer, IBond.Order.DOUBLE, tmpConsiderImplicitHydrogens));
    }

    /**
     * Tests whether the .countBondsOfSpecificOrder() method of class ChemUtils throws a NullPointerException if the given
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
                    Assertions.assertInstanceOf(Integer.class, ChemUtils.countBondsOfSpecificBondOrder(tmpAtomContainer,
                            tmpBondOrder, tmpConsiderImplicitHydrogens));
                }
        );
    }
    //</editor-fold>

    //<editor-fold desc="countAtomsOfAtomicNumbers() method tests" defaultstate="collapsed">
    /**
     * Tests whether the .countAtomsOfAtomicNumbers() method of class ChemUtils accepts a varying amount of integer
     * value parameters.
     */
    @Test
    public void countAtomsOfAtomicNumbersMethodTest_acceptsVaryingAmountOfIntegerParams() {
        Assertions.assertDoesNotThrow(() -> {
            IAtomContainer tmpAtomContainer = new AtomContainer();
            int tmpAnIntegerValue = 5;
            int tmpReturnValue;
            tmpReturnValue = ChemUtils.countAtomsOfAtomicNumbers(tmpAtomContainer);
            tmpReturnValue = ChemUtils.countAtomsOfAtomicNumbers(tmpAtomContainer, tmpAnIntegerValue);
            tmpReturnValue = ChemUtils.countAtomsOfAtomicNumbers(tmpAtomContainer, tmpAnIntegerValue, tmpAnIntegerValue);
            tmpReturnValue = ChemUtils.countAtomsOfAtomicNumbers(tmpAtomContainer, tmpAnIntegerValue, tmpAnIntegerValue, tmpAnIntegerValue);
        });
    }

    /**
     * Tests whether the .countAtomsOfAtomicNumbers() method of class ChemUtils counts the number of atoms of a single
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
            tmpCalculatedAtomsCount = ChemUtils.countAtomsOfAtomicNumbers(tmpAtomContainer, tmpCarbonAtomicNumber);
            Assertions.assertEquals(tmpExpectedAtomsCountArray[i], tmpCalculatedAtomsCount);
        }
    }

    /**
     * Tests whether the .countAtomsOfAtomicNumbers() method of class ChemUtils counts the number of atoms of a single
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
                tmpCalculatedAtomsCount = ChemUtils.countAtomsOfAtomicNumbers(tmpAtomContainer, tmpAtomicNumber);
                Assertions.assertEquals(tmpExpectedAtomsCountMatrix[i][j], tmpCalculatedAtomsCount);
            }
        }
    }

    /**
     * Tests whether the .countAtomsOfAtomicNumbers() method of class ChemUtils counts the number of atoms with an
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
            tmpCalculatedAtomsCount = ChemUtils.countAtomsOfAtomicNumbers(tmpAtomContainer, tmpHydrogenAtomicNumber);
            Assertions.assertEquals(tmpExpectedAtomsCountArray[i], tmpCalculatedAtomsCount);
        }
    }

    /**
     * Tests whether the .countAtomsOfAtomicNumbers() method of class ChemUtils counts the number of atoms of multiple
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
                tmpCalculatedAtomsCount = ChemUtils.countAtomsOfAtomicNumbers(tmpAtomContainer, tmpAtomicNumbersArray);
                Assertions.assertEquals(tmpExpectedAtomsCountMatrix[i][j], tmpCalculatedAtomsCount);
            }
        }
    }

    /**
     * Tests whether the .countAtomsOfAtomicNumbers() method of class ChemUtils throws a NullPointerException if the
     * given instance of IAtomContainer is null.
     */
    @Test
    public void countAtomsOfAtomicNumbersMethodTest_throwsNullPointerExceptionIfGivenAtomContainerIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    IAtomContainer tmpAtomContainer = null;
                    int tmpAnAtomicNumber = 5;
                    ChemUtils.countAtomsOfAtomicNumbers(tmpAtomContainer, tmpAnAtomicNumber);
                }
        );
    }

    /**
     * Tests whether the .countAtomsOfAtomicNumbers() method of class ChemUtils throws an IllegalArgumentException if
     * a given integer is of negative value; test 1, single parameter.
     */
    @Test
    public void countAtomsOfAtomicNumbersMethodTest_throwsIllegalArgumentExceptionIfGivenIntegerParamIsNegative_singleParam() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    IAtomContainer tmpAtomContainer = new AtomContainer();
                    int tmpNegativeIntegerValue = -1;
                    ChemUtils.countAtomsOfAtomicNumbers(tmpAtomContainer, tmpNegativeIntegerValue);
                }
        );
    }

    /**
     * Tests whether the .countAtomsOfAtomicNumbers() method of class ChemUtils throws an IllegalArgumentException if
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
                    ChemUtils.countAtomsOfAtomicNumbers(
                            tmpAtomContainer,
                            tmpAcceptedValue,
                            tmpNegativeIntegerValue,
                            tmpAcceptedValue
                    );
                }
        );
    }

    /**
     * Tests whether the .countAtomsOfAtomicNumbers() method of class ChemUtils throws an IllegalArgumentException if
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
                    ChemUtils.countAtomsOfAtomicNumbers(tmpAtomContainer, tmpUnknownAtomicNumber);
                }
        );
    }

    /**
     * Tests whether the .countAtomsOfAtomicNumbers() method of class ChemUtils throws an IllegalArgumentException if
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
                    ChemUtils.countAtomsOfAtomicNumbers(
                            tmpAtomContainer,
                            tmpAcceptedValue,
                            tmpAcceptedValue,
                            tmpUnknownAtomicNumber
                    );
                }
        );
    }
    //</editor-fold>

    //<editor-fold desc="getHeavyAtomCount() method tests" defaultstate="collapsed">
    /**
     * Tests whether the .getHeavyAtomsCount() method of class ChemUtils returns the count of heavy atoms
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
                    ChemUtils.getHeavyAtomsCount(tmpAtomContainerSet.getAtomContainer(i))
            );
        }
    }

    /**
     * Tests whether the .getHeavyAtomsCount() method of class ChemUtils throws a NullPointerException if the given
     * IAtomContainer instance is null.
     */
    @Test
    public void getHeavyAtomsCountMethodTest_throwsNullPointerExceptionIfGivenAtomContainerIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    IAtomContainer tmpAtomContainer = null;
                    ChemUtils.getHeavyAtomsCount(tmpAtomContainer);
                }
        );
    }
    //</editor-fold>

    //<editor-fold desc="getMass() method tests" defaultstate="collapsed">
    /**
     * Tests whether the values returned by the .getMass() method of class ChemUtils equal the mass calculated
     * by the .getMass() method of class AtomContainerManipulator.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     * @see AtomContainerManipulator#getMass(IAtomContainer, int)
     */
    @Test
    public void getMassMethodTest_calculatedValuesEqualAtomContainerManipulatorMethodGetMassResults_withDiffFlavours()
            throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "CCO",
                "NCC(=O)O",
                "C",
                "CH",
                "HCH"
        );
        MassComputationFlavours[] tmpFlavoursArray = new MassComputationFlavours[]{
                MassComputationFlavours.MOL_WEIGHT,
                MassComputationFlavours.MOL_WEIGHT_IGNORE_SPECIFIED,
                MassComputationFlavours.MONO_ISOTOPIC,
                MassComputationFlavours.MOST_ABUNDANT
        };
        for (MassComputationFlavours tmpFlavour :
                tmpFlavoursArray) {
            for (IAtomContainer tmpAtomContainer :
                    tmpAtomContainerSet.atomContainers()) {
                Assertions.assertEquals(
                        AtomContainerManipulator.getMass(tmpAtomContainer, tmpFlavour.getAssociatedIntegerValue()),
                        ChemUtils.getMass(tmpAtomContainer, tmpFlavour)
                );
            }
        }
    }

    /**
     * Tests whether the values returned by the .getMass() method of class ChemUtils equal the masses calculated
     * by the .getMass() method of class AtomContainerManipulator if atoms have a specified mass.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     * @see AtomContainerManipulator#getMass(IAtomContainer, int)
     */
    @Test
    public void getMassMethodTest_calculatedValuesEqualAtomContainerManipulatorMethodGetMassResults_withDiffFlavours_withSpecifiedMass()
            throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "CCO",
                "CCO"
        );
        tmpAtomContainerSet.getAtomContainer(1).getAtom(0).setExactMass(13.0);
        tmpAtomContainerSet.getAtomContainer(1).getAtom(2).setExactMass(18.0);
        MassComputationFlavours[] tmpFlavoursArray = new MassComputationFlavours[]{
                MassComputationFlavours.MOL_WEIGHT,
                MassComputationFlavours.MOL_WEIGHT_IGNORE_SPECIFIED,
                MassComputationFlavours.MONO_ISOTOPIC,
                MassComputationFlavours.MOST_ABUNDANT
        };
        for (MassComputationFlavours tmpFlavour :
                tmpFlavoursArray) {
            for (IAtomContainer tmpAtomContainer :
                    tmpAtomContainerSet.atomContainers()) {
                Assertions.assertEquals(
                        AtomContainerManipulator.getMass(tmpAtomContainer, tmpFlavour.getAssociatedIntegerValue()),
                        ChemUtils.getMass(tmpAtomContainer, tmpFlavour)
                );
            }
        }
    }

    /**
     * Tests whether the .getMass() method of class ChemUtils throws a NullPointerException if the given
     * IAtomContainer instance is null.
     */
    @Test
    public void getMassMethodTest_atomContainerNull_throwsNullPointerException() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    IAtomContainer tmpAtomContainer = null;
                    MassComputationFlavours tmpFlavour = MassComputationFlavours.MOL_WEIGHT;
                    ChemUtils.getMass(tmpAtomContainer, tmpFlavour);
                }
        );
    }

    /**
     * Tests whether the .getMass() method of class ChemUtils throws a NullPointerException if the given
     * MassComputationFlavours constant is null.
     */
    @Test
    public void getMassMethodTest_massComputationFlavourNull_throwsNullPointerException() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    IAtomContainer tmpAtomContainer = new AtomContainer();
                    MassComputationFlavours tmpFlavour = null;
                    ChemUtils.getMass(tmpAtomContainer, tmpFlavour);
                }
        );
    }
    //</editor-fold>

}
