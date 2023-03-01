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
import org.openscience.cdk.interfaces.IAtomContainerSet;

/**
 *
 */
public class FilterTest {

    /**
     * Tests whether the method .assignIdTiAtomContainers() of the class Filter assigns an ID to a single atom container
     * contained in a given atom container set. The ID should be set as property to the atom container.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void assignIdToAtomContainersTest_singleAC() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings("");
        Assertions.assertEquals(1, tmpAtomContainerSet.getAtomContainerCount());
        //
        Filter tmpFilter = new Filter();
        tmpFilter.assignIdToAtomContainers(tmpAtomContainerSet);
        Assertions.assertEquals(0, (Integer) tmpAtomContainerSet.getAtomContainer(0).getProperty(Filter.MOL_ID_PROPERTY_NAME));
    }

    /**
     * Tests whether the method .assignIdTiAtomContainers() of the class Filter assigns IDs to multiple atom containers
     * contained by a given atom container set. The ID should be set as property to each atom container.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void assignIdToAtomContainersTest_multipleACs() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings("", "", "");
        Assertions.assertEquals(3, tmpAtomContainerSet.getAtomContainerCount());
        //
        Filter tmpFilter = new Filter();
        tmpFilter.assignIdToAtomContainers(tmpAtomContainerSet);
        for (int i = 0; i < 3; i++) {
            Assertions.assertEquals(i, (Integer) tmpAtomContainerSet.getAtomContainer(i).getProperty(Filter.MOL_ID_PROPERTY_NAME));
        }
    }

    /**
     * Tests whether a NullPointerException is thrown if the atom container set given to the .assignIdToAtomContainers()
     * method of the class Filter is null.
     */
    @Test
    public void assignIdToAtomContainersTest_throwNullPointerExceptionIfGivenParamIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    Filter tmpFilter = new Filter();
                    tmpFilter.assignIdToAtomContainers(null);
                }
        );
    }

    /*@Test   //TODO: check every single AC for not being null? I did not even find a way to create that situation
    public void assignIdToAtomContainersTest_throwNullPointerExceptionIfOneOfTheACsIsNull() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = this.parseSmilesString("", "", "");
        //IAtomContainer tmpAtomContainer = null;
        //tmpAtomContainerSet.replaceAtomContainer(1, tmpAtomContainer);
        //tmpAtomContainerSet.addAtomContainer(tmpAtomContainer);
        Assertions.assertEquals(3, tmpAtomContainerSet.getAtomContainerCount());
        //
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    Filter tmpFilter = new Filter();
                    tmpFilter.assignIdToAtomContainers(tmpAtomContainerSet);
                }
        );
    }*/

    /**
     * TODO
     * @throws InvalidSmilesException
     */
    @Test
    public void getArrayOfAssignedMolIDsTest_1AC() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings("");
        Assertions.assertEquals(1, tmpAtomContainerSet.getAtomContainerCount());
        //
        Filter tmpFilter = new Filter();
        tmpFilter.assignIdToAtomContainers(tmpAtomContainerSet);
        int[] tmpMolIDArray = tmpFilter.getArrayOfAssignedMolIDs(tmpAtomContainerSet);
        Assertions.assertEquals(1, tmpMolIDArray.length);
        Assertions.assertEquals((Integer) tmpAtomContainerSet.getAtomContainer(0).getProperty(Filter.MOL_ID_PROPERTY_NAME), tmpMolIDArray[0]);
    }

    /**
     * TODO
     * @throws InvalidSmilesException
     */
    @Test
    public void getArrayOfAssignedMolIDsTest_3ACs_consistentNumbering() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings("", "", "");
        Assertions.assertEquals(3, tmpAtomContainerSet.getAtomContainerCount());
        //
        Filter tmpFilter = new Filter();
        tmpFilter.assignIdToAtomContainers(tmpAtomContainerSet);
        int[] tmpMolIDArray = tmpFilter.getArrayOfAssignedMolIDs(tmpAtomContainerSet);
        Assertions.assertEquals(3, tmpMolIDArray.length);
        for (int i = 0; i < tmpAtomContainerSet.getAtomContainerCount(); i++) {
            Assertions.assertEquals((Integer) tmpAtomContainerSet.getAtomContainer(i).getProperty(Filter.MOL_ID_PROPERTY_NAME), tmpMolIDArray[i]);
        }
    }

    /**
     * TODO
     * @throws InvalidSmilesException
     */
    @Test
    public void getArrayOfAssignedMolIDsTest_3ACs_manuallySetInconsistentMolIDs() throws InvalidSmilesException {   //TODO
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings("", "", "");
        tmpAtomContainerSet.getAtomContainer(0).setProperty(Filter.MOL_ID_PROPERTY_NAME, 9);
        tmpAtomContainerSet.getAtomContainer(1).setProperty(Filter.MOL_ID_PROPERTY_NAME, 3);
        tmpAtomContainerSet.getAtomContainer(2).setProperty(Filter.MOL_ID_PROPERTY_NAME, 7);
        Assertions.assertEquals(3, tmpAtomContainerSet.getAtomContainerCount());
        //
        Filter tmpFilter = new Filter();
        int[] tmpMolIDArray = tmpFilter.getArrayOfAssignedMolIDs(tmpAtomContainerSet);
        Assertions.assertEquals(3, tmpMolIDArray.length);
        for (int i = 0; i < tmpAtomContainerSet.getAtomContainerCount(); i++) {
            Assertions.assertEquals((Integer) tmpAtomContainerSet.getAtomContainer(i).getProperty(Filter.MOL_ID_PROPERTY_NAME), tmpMolIDArray[i]);
        }
    }

    /**
     * Tests whether all the atom containers of the atom container set given to the .filter() method of the class Filter
     * are preserved if no filter is applied.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_noFilterSelected_checkIfAllElementsArePreserved() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings("", "", "");
        Assertions.assertEquals(3, tmpAtomContainerSet.getAtomContainerCount());
        //
        Filter tmpFilter = new Filter();
        IAtomContainerSet tmpFilteredACSet = tmpFilter.filter(tmpAtomContainerSet);
        Assertions.assertEquals(tmpAtomContainerSet.getAtomContainerCount(), tmpFilteredACSet.getAtomContainerCount());
        for (int i = 0; i < tmpAtomContainerSet.getAtomContainerCount(); i++) {
            Assertions.assertEquals(tmpAtomContainerSet.getAtomContainer(i), tmpFilteredACSet.getAtomContainer(i));
        }
    }

    /**
     * Tests whether all the atom containers of the atom container set returned by the .filter() method of the class
     * Filter have a valid ID attached. The ID should be attached as property and should be greater or equal to zero.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_checkIfAllACsOfFilteredACSetHaveIDsAttached() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings("", "", "");
        Assertions.assertEquals(3, tmpAtomContainerSet.getAtomContainerCount());
        //
        Filter tmpFilter = new Filter();
        IAtomContainerSet tmpFilteredACSet = tmpFilter.filter(tmpAtomContainerSet);
        for (IAtomContainer tmpAtomContainer : tmpFilteredACSet.atomContainers()) {
            Assertions.assertEquals(Integer.class, tmpAtomContainer.getProperty(Filter.MOL_ID_PROPERTY_NAME).getClass());
            Assertions.assertTrue((Integer) tmpAtomContainer.getProperty(Filter.MOL_ID_PROPERTY_NAME) >= 0);
        }
    }

    /**
     * Tests whether the atom containers preserved by the .filter() method of the class Filter preserved its respective
     * ID.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_noFilterSelected_checkIfAllElementsPreservedTheirCorrectID() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings("", "", "");
        Assertions.assertEquals(3, tmpAtomContainerSet.getAtomContainerCount());
        //
        Filter tmpFilter = new Filter();
        IAtomContainerSet tmpFilteredACSet = tmpFilter.filter(tmpAtomContainerSet);
        //TODO: should the IDs be assigned to the original ACSet or to a copy of it?
        //tmpFilter.assignIdToAtomContainers(tmpAtomContainerSet);
        for (int i = 0; i < tmpAtomContainerSet.getAtomContainerCount(); i++) {
            //Assertions.assertEquals(i, (Integer) tmpAtomContainerSet.getAtomContainer(i).getProperty(Filter.MOL_ID_PROPERTY_NAME));
            Assertions.assertEquals(tmpAtomContainerSet.getAtomContainer(i).getProperty(Filter.MOL_ID_PROPERTY_NAME), (Integer) tmpFilteredACSet.getAtomContainer(i).getProperty(Filter.MOL_ID_PROPERTY_NAME));
        }
    }

    /**
     * Tests whether a NullPointerException is thrown if the atom container set given to the .filter() method of the
     * class Filter is null.
     */
    @Test   //TODO: has never been "red" since the check is also performed in .assignIdToAtomContainers(); I added the check in .filter() anyway
    public void filterMethodTest_throwNullPointerExceptionIfGivenParamIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    Filter tmpFilter = new Filter();
                    tmpFilter.filter(null);
                }
        );
    }

    /**
     * This should be the final form for using a filter on an atom container set. TODO: red phase
     *
     * @throws InvalidSmilesException
     */
    /*@Test
    public void filterMethodTest_filterOnMaxAtomCount10_singleAcWith9Atoms() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings("CCO");
        Assertions.assertEquals(1, tmpAtomContainerSet.getAtomContainerCount());
        //
        Filter tmpFilter = new Filter();
        int tmpMaxAtomCount = 10;
        boolean tmpConsiderImplicitHydrogen = true;
        tmpFilter = tmpFilter.withMaxAtomCountFilter(tmpMaxAtomCount, tmpConsiderImplicitHydrogen);
        IAtomContainerSet tmpFilteredACSet = tmpFilter.filter(tmpAtomContainerSet);
        Assertions.assertEquals(1, tmpFilteredACSet.getAtomContainerCount());
        Assertions.assertEquals(tmpAtomContainerSet.getAtomContainer(0), tmpFilteredACSet.getAtomContainer(0));
    }*/

    /*@Test
    public void filterMethodTest_filterOnMaxAtomCount10_singleAcWith12Atoms() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings("c1ccccc1");
        Assertions.assertEquals(1, tmpAtomContainerSet.getAtomContainerCount());
        //
        Filter tmpFilter = new Filter();
        int tmpMaxAtomCount = 10;
        boolean tmpConsiderImplicitHydrogen = true;
        tmpFilter = tmpFilter.withMaxAtomCountFilter(tmpMaxAtomCount, tmpConsiderImplicitHydrogen);
        IAtomContainerSet tmpFilteredACSet = tmpFilter.filter(tmpAtomContainerSet);
        Assertions.assertEquals(0, tmpFilteredACSet.getAtomContainerCount());
    }*/

    /*@Test
    public void filterOnMaxAtomCount() throws InvalidSmilesException {  //TODO: wip; adjourned
        IAtomContainerSet tmpACSet = this.parseSmilesString(
                "c1ccccc1",
                "O=C1C=C(OC=2C1=C(O)C=C(O)C2C(C=3C=CC=CC3)CC(=O)OC)C=4C=CC=CC4",
                "O=C1OC2=CC(=CC(OC3OC(CO)C(O)C(O)C3O)=C2C4=C1CCC4)C",
                "O=C(NC1OCOC2C1OC(CC(OC)CO)C(C)(C)C2OC)C(O)C3(OC)OC(C)C(C(=C)C3)C"
        );
        int tmpAtomCount;
        for (IAtomContainer tmpAtomContainer :
                tmpACSet.atomContainers()) {
            tmpAtomCount = tmpAtomContainer.getAtomCount();
            for (IAtom tmpAtom :
                    tmpAtomContainer.atoms()) {
                tmpAtomCount += tmpAtom.getImplicitHydrogenCount();
            }
            System.out.println("tmpAtomCount = " + tmpAtomCount);
        }
    }*/

}
