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
import org.openscience.cdk.Atom;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

/**
 * Test class of the filters HasAllValidAtomicNumbersFilter and HasInvalidAtomicNumbersFilter.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public class AtomicNumberValidityFiltersTest {

    /**
     * Tests whether the public constructors of both classes initializes the respective class field with the given
     * parameter.
     */
    @Test
    public void publicConstructorTests_bothInitializeClassVarWithGivenParam_test1() {
        //test 1
        boolean tmpWildcardAtomicNumberAsValid = true;
        HasAllValidAtomicNumbersFilter tmpFilter1 = new HasAllValidAtomicNumbersFilter(tmpWildcardAtomicNumberAsValid,
                TestUtils.getTestReporterInstance());
        Assertions.assertTrue(tmpFilter1.wildcardAtomicNumberIsValid);
        HasInvalidAtomicNumbersFilter tmpFilter2 = new HasInvalidAtomicNumbersFilter(tmpWildcardAtomicNumberAsValid,
                TestUtils.getTestReporterInstance());
        Assertions.assertTrue(tmpFilter2.wildcardAtomicNumberIsValid);
        //test 2
        tmpWildcardAtomicNumberAsValid = false;
        tmpFilter1 = new HasAllValidAtomicNumbersFilter(tmpWildcardAtomicNumberAsValid,
                TestUtils.getTestReporterInstance());
        Assertions.assertFalse(tmpFilter1.wildcardAtomicNumberIsValid);
        tmpFilter2 = new HasInvalidAtomicNumbersFilter(tmpWildcardAtomicNumberAsValid,
                TestUtils.getTestReporterInstance());
        Assertions.assertFalse(tmpFilter2.wildcardAtomicNumberIsValid);
    }

    /**
     * Tests whether method .isFiltered() of class HasAllValidAtomicNumbersFilter returns false if given atom containers
     * with all valid atomic numbers and .isFiltered() of class HasInvalidAtomicNumbersFilter returns true; the boolean
     * parameter aWildcardAtomicNumberIsValid is without influence here.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     * @throws Exception if something went wrong
     */
    @Test
    public void isFilteredMethodTest_atomContainersWithAllValidAtomicNumbers() throws InvalidSmilesException, Exception {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "c1ccccc1",
                "C",
                "O=C=O"
        );
        //
        boolean tmpWildcardAtomicNumberIsValid = true;
        IFilter tmpHasAllValidAtomicNumbersFilter = new HasAllValidAtomicNumbersFilter(tmpWildcardAtomicNumberIsValid,
                TestUtils.getTestReporterInstance());
        IFilter tmpHasInvalidAtomicNumbersFilter = new HasInvalidAtomicNumbersFilter(tmpWildcardAtomicNumberIsValid,
                TestUtils.getTestReporterInstance());
        boolean tmpIsFilteredByHasAllValid;
        boolean tmpIsFilteredByHasInvalid;
        for (IAtomContainer tmpAtomContainer :
                tmpAtomContainerSet.atomContainers()) {
            tmpIsFilteredByHasAllValid = tmpHasAllValidAtomicNumbersFilter.isFiltered(tmpAtomContainer);
            Assertions.assertFalse(tmpIsFilteredByHasAllValid);
            tmpIsFilteredByHasInvalid = tmpHasInvalidAtomicNumbersFilter.isFiltered(tmpAtomContainer);
            //Assertions.assertTrue(tmpIsFilteredByHasInvalid);
            Assertions.assertEquals(tmpIsFilteredByHasAllValid, !tmpIsFilteredByHasInvalid);
        }
    }

    /**
     * Tests whether method .isFiltered() of class HasAllValidAtomicNumbersFilter returns true if given atom containers
     * with invalid atomic numbers and .isFiltered() of class HasInvalidAtomicNumbersFilter returns false; the boolean
     * parameter aWildcardAtomicNumberIsValid is without influence here.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     * @throws Exception if something went wrong
     */
    @Test
    public void isFilteredMethodTest_atomContainersWithInvalidAtomicNumbers() throws InvalidSmilesException, Exception {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "c1ccccc1",
                "C",
                "O=C=O"
        );
        tmpAtomContainerSet.getAtomContainer(0).getAtom(3).setAtomicNumber(-1);
        tmpAtomContainerSet.getAtomContainer(1).getAtom(0).setAtomicNumber(-10);
        tmpAtomContainerSet.getAtomContainer(2).getAtom(2).setAtomicNumber(119);
        //
        boolean tmpWildcardAtomicNumberIsValid = true;
        IFilter tmpHasAllValidAtomicNumbersFilter = new HasAllValidAtomicNumbersFilter(tmpWildcardAtomicNumberIsValid,
                TestUtils.getTestReporterInstance());
        IFilter tmpHasInvalidAtomicNumbersFilter = new HasInvalidAtomicNumbersFilter(tmpWildcardAtomicNumberIsValid,
                TestUtils.getTestReporterInstance());
        boolean tmpIsFilteredByHasAllValid;
        boolean tmpIsFilteredByHasInvalid;
        for (IAtomContainer tmpAtomContainer :
                tmpAtomContainerSet.atomContainers()) {
            tmpIsFilteredByHasAllValid = tmpHasAllValidAtomicNumbersFilter.isFiltered(tmpAtomContainer);
            Assertions.assertTrue(tmpIsFilteredByHasAllValid);
            tmpIsFilteredByHasInvalid = tmpHasInvalidAtomicNumbersFilter.isFiltered(tmpAtomContainer);
            //Assertions.assertFalse(tmpIsFilteredByHasInvalid);
            Assertions.assertEquals(tmpIsFilteredByHasAllValid, !tmpIsFilteredByHasInvalid);
        }
    }

    /**
     * Tests whether method .isFiltered() of class HasAllValidAtomicNumbersFilter returns false if given atom containers
     * with valid atomic numbers including zero and .isFiltered() of class HasInvalidAtomicNumbersFilter returns the
     * opposite, true, if the wildcard atomic number zero is considered as valid.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     * @throws Exception if something went wrong
     */
    @Test
    public void isFilteredMethodTest_atomContainersWithAllValidAtomicNumbers_zeroIsValid() throws InvalidSmilesException, Exception {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "C",
                "c1ccccc1",
                "O=C=O"
        );
        tmpAtomContainerSet.getAtomContainer(0).getAtom(0).setAtomicNumber(0);
        tmpAtomContainerSet.getAtomContainer(1).getAtom(3).setAtomicNumber(0);
        tmpAtomContainerSet.getAtomContainer(1).getAtom(4).setAtomicNumber(0);
        tmpAtomContainerSet.getAtomContainer(2).addAtom(new Atom(0));
        //
        boolean tmpWildcardAtomicNumberIsValid = true;
        IFilter tmpHasAllValidAtomicNumbersFilter = new HasAllValidAtomicNumbersFilter(tmpWildcardAtomicNumberIsValid,
                TestUtils.getTestReporterInstance());
        IFilter tmpHasInvalidAtomicNumbersFilter = new HasInvalidAtomicNumbersFilter(tmpWildcardAtomicNumberIsValid,
                TestUtils.getTestReporterInstance());
        boolean tmpIsFilteredByHasAllValid;
        boolean tmpIsFilteredByHasInvalid;
        for (IAtomContainer tmpAtomContainer :
                tmpAtomContainerSet.atomContainers()) {
            tmpIsFilteredByHasAllValid = tmpHasAllValidAtomicNumbersFilter.isFiltered(tmpAtomContainer);
            Assertions.assertFalse(tmpIsFilteredByHasAllValid);
            tmpIsFilteredByHasInvalid = tmpHasInvalidAtomicNumbersFilter.isFiltered(tmpAtomContainer);
            //Assertions.assertTrue(tmpIsFilteredByHasInvalid);
            Assertions.assertEquals(tmpIsFilteredByHasAllValid, !tmpIsFilteredByHasInvalid);
        }
    }

    /**
     * Tests whether method .isFiltered() of class HasAllValidAtomicNumbersFilter returns true if given atom containers
     * with valid atomic numbers and zero as atomic number and .isFiltered() of class HasInvalidAtomicNumbersFilter
     * returns the opposite, false, if the wildcard atomic number zero is considered as invalid.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     * @throws Exception if something went wrong
     */
    @Test
    public void isFilteredMethodTest_atomContainersWithAllValidAtomicNumbers_zeroIsInvalid() throws InvalidSmilesException, Exception {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "C",
                "c1ccccc1",
                "O=C=O"
        );
        tmpAtomContainerSet.getAtomContainer(0).getAtom(0).setAtomicNumber(0);
        tmpAtomContainerSet.getAtomContainer(1).getAtom(3).setAtomicNumber(0);
        tmpAtomContainerSet.getAtomContainer(1).getAtom(4).setAtomicNumber(0);
        tmpAtomContainerSet.getAtomContainer(2).addAtom(new Atom(0));
        //
        boolean tmpWildcardAtomicNumberIsValid = false;
        IFilter tmpHasAllValidAtomicNumbersFilter = new HasAllValidAtomicNumbersFilter(tmpWildcardAtomicNumberIsValid,
                TestUtils.getTestReporterInstance());
        IFilter tmpHasInvalidAtomicNumbersFilter = new HasInvalidAtomicNumbersFilter(tmpWildcardAtomicNumberIsValid,
                TestUtils.getTestReporterInstance());
        boolean tmpIsFilteredByHasAllValid;
        boolean tmpIsFilteredByHasInvalid;
        for (IAtomContainer tmpAtomContainer :
                tmpAtomContainerSet.atomContainers()) {
            tmpIsFilteredByHasAllValid = tmpHasAllValidAtomicNumbersFilter.isFiltered(tmpAtomContainer);
            Assertions.assertTrue(tmpIsFilteredByHasAllValid);
            tmpIsFilteredByHasInvalid = tmpHasInvalidAtomicNumbersFilter.isFiltered(tmpAtomContainer);
            //Assertions.assertFalse(tmpIsFilteredByHasInvalid);
            Assertions.assertEquals(tmpIsFilteredByHasAllValid, !tmpIsFilteredByHasInvalid);
        }
    }

    /**
     * Tests whether method .isFiltered() of class HasAllValidAtomicNumbersFilter returns true if a given atom container
     * has an atomic number being null and .isFiltered() of class HasInvalidAtomicNumbersFilter returns the opposite,
     * false; the boolean parameter aWildcardAtomicNumberIsValid is without influence here.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     * @throws Exception if something went wrong
     */
    @Test
    public void isFilteredMethodTest_atomContainersWithAnAtomicNumberBeingNull() throws InvalidSmilesException, Exception {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "C",
                "c1ccccc1",
                "O=C=O"
        );
        tmpAtomContainerSet.getAtomContainer(0).getAtom(0).setAtomicNumber(null);
        tmpAtomContainerSet.getAtomContainer(1).getAtom(3).setAtomicNumber(null);
        tmpAtomContainerSet.getAtomContainer(1).getAtom(4).setAtomicNumber(null);
        IAtom tmpAtom = new Atom();
        tmpAtom.setAtomicNumber(null);
        tmpAtomContainerSet.getAtomContainer(2).addAtom(tmpAtom);
        //
        boolean tmpWildcardAtomicNumberIsValid = true;
        IFilter tmpHasAllValidAtomicNumbersFilter = new HasAllValidAtomicNumbersFilter(tmpWildcardAtomicNumberIsValid,
                TestUtils.getTestReporterInstance());
        IFilter tmpHasInvalidAtomicNumbersFilter = new HasInvalidAtomicNumbersFilter(tmpWildcardAtomicNumberIsValid,
                TestUtils.getTestReporterInstance());
        boolean tmpIsFilteredByHasAllValid;
        boolean tmpIsFilteredByHasInvalid;
        for (IAtomContainer tmpAtomContainer :
                tmpAtomContainerSet.atomContainers()) {
            tmpIsFilteredByHasAllValid = tmpHasAllValidAtomicNumbersFilter.isFiltered(tmpAtomContainer);
            Assertions.assertTrue(tmpIsFilteredByHasAllValid);
            tmpIsFilteredByHasInvalid = tmpHasInvalidAtomicNumbersFilter.isFiltered(tmpAtomContainer);
            //Assertions.assertFalse(tmpIsFilteredByHasInvalid);
            Assertions.assertEquals(tmpIsFilteredByHasAllValid, !tmpIsFilteredByHasInvalid);
        }
    }

    /**
     * Tests whether method .isFiltered() of both classes throws a NullPointerException if the given IAtomContainer
     * instance is null.
     */
    @Test
    public void isFilteredMethodTest_throwNullPointerExceptionIfGivenAtomContainerIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    IAtomContainer tmpAtomContainer = null;
                    boolean tmpWildcardAtomicNumberIsValid = true;
                    new HasAllValidAtomicNumbersFilter(tmpWildcardAtomicNumberIsValid,
                            TestUtils.getTestReporterInstance()).isFiltered(tmpAtomContainer);
                }
        );
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    IAtomContainer tmpAtomContainer = null;
                    boolean tmpWildcardAtomicNumberIsValid = true;
                    new HasInvalidAtomicNumbersFilter(tmpWildcardAtomicNumberIsValid,
                            TestUtils.getTestReporterInstance()).isFiltered(tmpAtomContainer);
                }
        );
    }

    /**
     * Tests whether method .isFiltered() of both classes throws a NullPointerException if an atom of the given
     * IAtomContainer instance is null.
     */
    @Test
    public void isFilteredMethodTest_throwNullPointerExceptionIfAnAtomIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    IAtomContainer tmpAtomContainer = new AtomContainer();
                    tmpAtomContainer.addAtom(null);
                    boolean tmpWildcardAtomicNumberIsValid = true;
                    new HasAllValidAtomicNumbersFilter(tmpWildcardAtomicNumberIsValid,
                            TestUtils.getTestReporterInstance()).isFiltered(tmpAtomContainer);
                }
        );
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    IAtomContainer tmpAtomContainer = new AtomContainer();
                    tmpAtomContainer.addAtom(null);
                    boolean tmpWildcardAtomicNumberIsValid = true;
                    new HasInvalidAtomicNumbersFilter(tmpWildcardAtomicNumberIsValid,
                            TestUtils.getTestReporterInstance()).isFiltered(tmpAtomContainer);
                }
        );
    }

    /**
     * Tests whether the .filter() method of both classes filters as expected; test 1; wildcard atomic number is
     * considered as valid.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     * @throws Exception if something went wrong
     */
    @Test
    public void filterMethodTest_filtersAsExpected_test1_zeroIsValid() throws InvalidSmilesException, Exception {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "C=CC=C",
                "C",
                "O=C=O",
                "c1ccccc1",
                "CCO"
        );
        tmpAtomContainerSet.getAtomContainer(1).getAtom(0).setAtomicNumber(-1);
        tmpAtomContainerSet.getAtomContainer(2).getAtom(0).setAtomicNumber(119);
        tmpAtomContainerSet.getAtomContainer(3).getAtom(2).setAtomicNumber(0);
        tmpAtomContainerSet.getAtomContainer(4).getAtom(1).setAtomicNumber(null);
        boolean[] tmpIsInvalidArray = new boolean[]{
                false,  //all valid
                true,   //atomic number < 0
                true,   //atomic number > 118
                false,  //atomic number = 0
                true    //atomic number is null
        };
        //
        boolean tmpWildcardAtomicNumberIsValid = true;
        //
        IFilter tmpFilter = new HasAllValidAtomicNumbersFilter(tmpWildcardAtomicNumberIsValid,
                TestUtils.getTestReporterInstance());  //TODO: outsource code?
        IAtomContainerSet tmpFilteredACSet = tmpFilter.process(tmpAtomContainerSet, false);
        int tmpIndexInFilteredACSet = 0;
        for (int i = 0; i < tmpAtomContainerSet.getAtomContainerCount(); i++) {
            if (!tmpIsInvalidArray[i]) {
                Assertions.assertSame(tmpAtomContainerSet.getAtomContainer(i),
                        tmpFilteredACSet.getAtomContainer(tmpIndexInFilteredACSet));
                tmpIndexInFilteredACSet++;
            }
        }
        //tmpIndexInFilteredACSet should now equal the expected count of ACs in the filtered AC set
        Assertions.assertEquals(tmpIndexInFilteredACSet, tmpFilteredACSet.getAtomContainerCount());
        //
        tmpFilter = new HasInvalidAtomicNumbersFilter(tmpWildcardAtomicNumberIsValid, TestUtils.getTestReporterInstance());
        tmpFilteredACSet = tmpFilter.process(tmpAtomContainerSet, false);
        tmpIndexInFilteredACSet = 0;
        for (int i = 0; i < tmpAtomContainerSet.getAtomContainerCount(); i++) {
            if (tmpIsInvalidArray[i]) {
                Assertions.assertSame(tmpAtomContainerSet.getAtomContainer(i),
                        tmpFilteredACSet.getAtomContainer(tmpIndexInFilteredACSet));
                tmpIndexInFilteredACSet++;
            }
        }
        //tmpIndexInFilteredACSet should now equal the expected count of ACs in the filtered AC set
        Assertions.assertEquals(tmpIndexInFilteredACSet, tmpFilteredACSet.getAtomContainerCount());
    }

    /**
     * Tests whether the .filter() method of both classes filters as expected; test 2; wildcard atomic number is
     * considered as invalid.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     * @throws Exception if something went wrong
     */
    @Test
    public void filterMethodTest_filtersAsExpected_test2_zeroIsInvalid() throws InvalidSmilesException, Exception {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "c1ccccc1",
                "C=CC=C",
                "C",
                "CCO",
                "O=C=O"
        );
        tmpAtomContainerSet.getAtomContainer(1).getAtom(0).setAtomicNumber(-1);
        tmpAtomContainerSet.getAtomContainer(2).getAtom(0).setAtomicNumber(119);
        tmpAtomContainerSet.getAtomContainer(3).getAtom(2).setAtomicNumber(0);
        tmpAtomContainerSet.getAtomContainer(4).getAtom(1).setAtomicNumber(null);
        boolean[] tmpIsInvalidArray = new boolean[]{
                false,  //all valid
                true,   //atomic number < 0
                true,   //atomic number > 118
                true,   //atomic number = 0
                true    //atomic number is null
        };
        //
        boolean tmpWildcardAtomicNumberIsValid = false;
        //
        IFilter tmpFilter = new HasAllValidAtomicNumbersFilter(tmpWildcardAtomicNumberIsValid, TestUtils.getTestReporterInstance());
        IAtomContainerSet tmpFilteredACSet = tmpFilter.process(tmpAtomContainerSet, false);
        int tmpIndexInFilteredACSet = 0;
        for (int i = 0; i < tmpAtomContainerSet.getAtomContainerCount(); i++) {
            if (!tmpIsInvalidArray[i]) {
                Assertions.assertSame(tmpAtomContainerSet.getAtomContainer(i),
                        tmpFilteredACSet.getAtomContainer(tmpIndexInFilteredACSet));
                tmpIndexInFilteredACSet++;
            }
        }
        //tmpIndexInFilteredACSet should now equal the expected count of ACs in the filtered AC set
        Assertions.assertEquals(tmpIndexInFilteredACSet, tmpFilteredACSet.getAtomContainerCount());
        //
        tmpFilter = new HasInvalidAtomicNumbersFilter(tmpWildcardAtomicNumberIsValid, TestUtils.getTestReporterInstance());
        tmpFilteredACSet = tmpFilter.process(tmpAtomContainerSet, false);
        tmpIndexInFilteredACSet = 0;
        for (int i = 0; i < tmpAtomContainerSet.getAtomContainerCount(); i++) {
            if (tmpIsInvalidArray[i]) {
                Assertions.assertSame(tmpAtomContainerSet.getAtomContainer(i),
                        tmpFilteredACSet.getAtomContainer(tmpIndexInFilteredACSet));
                tmpIndexInFilteredACSet++;
            }
        }
        //tmpIndexInFilteredACSet should now equal the expected count of ACs in the filtered AC set
        Assertions.assertEquals(tmpIndexInFilteredACSet, tmpFilteredACSet.getAtomContainerCount());
    }

    /**
     * Tests whether the .filter() method of both classes throws a NullPointerException if the given IAtomContainerSet
     * instance is null.
     */
    @Test
    public void filterMethodTest_throwsNullPointerExceptionIfGivenIAtomContainerSetIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    IAtomContainer tmpAtomContainer = null;
                    boolean tmpWildcardAtomicNumberIsValid = true;
                    new HasAllValidAtomicNumbersFilter(tmpWildcardAtomicNumberIsValid,
                            TestUtils.getTestReporterInstance()).isFiltered(tmpAtomContainer);
                }
        );
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    IAtomContainer tmpAtomContainer = null;
                    boolean tmpWildcardAtomicNumberIsValid = true;
                    new HasInvalidAtomicNumbersFilter(tmpWildcardAtomicNumberIsValid,
                            TestUtils.getTestReporterInstance()).isFiltered(tmpAtomContainer);
                }
        );
    }

}
