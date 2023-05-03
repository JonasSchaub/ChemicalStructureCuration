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
import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

/**
 * Test class for the FilterPipeline class. It holds all the test methods that are associated with the MolID, which is
 * an ID that is assigned to every atom container of an atom container set that was passed to the .filter() method of
 * class FilterPipeline to uniquely identify each single atom container.
 */
public class CurationPipelineMolIDTests {

    /**
     * Tests whether the method .assignMolIdToAtomContainers() of the class FilterPipeline assigns an MolID to a single
     * atom container contained in a given atom container set. The ID should be set as property to the atom container.
     */
    @Test
    public void assignMolIdToAtomContainersTest_singleAC_propertyMolIDIsNotNull() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(1);
        Assertions.assertEquals(1, tmpAtomContainerSet.getAtomContainerCount());
        //
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        tmpCurationPipeline.assignMolIdToAtomContainers(tmpAtomContainerSet);
        Assertions.assertNotNull(tmpAtomContainerSet.getAtomContainer(0).getProperty(CurationPipeline.MOL_ID_PROPERTY_NAME));
    }

    /**
     * Tests whether the atom container property assigned by the method .assignMolIdToAtomContainers() of the class
     * FilterPipeline is of type Integer.
     */
    @Test
    public void assignMolIdToAtomContainersTest_singleAC_propertyMolIDIsOfTypeInteger() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(1);
        Assertions.assertEquals(1, tmpAtomContainerSet.getAtomContainerCount());
        //
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        tmpCurationPipeline.assignMolIdToAtomContainers(tmpAtomContainerSet);
        Assertions.assertInstanceOf(Integer.class, tmpAtomContainerSet.getAtomContainer(0).getProperty(CurationPipeline.MOL_ID_PROPERTY_NAME));
    }

    /**
     * Tests whether the method .assignMolIdToAtomContainers() of the class FilterPipeline assigns a MolID of to a
     * single atom container that equals its index in the atom container set passed to the method.
     */
    @Test
    public void assignMolIdToAtomContainersTest_propertyMolIDEqualsTheACsIndexInTheACSet_singleAC() {
        IAtomContainer tmpAtomContainer = new AtomContainer();
        IAtomContainerSet tmpAtomContainerSet = new AtomContainerSet();
        tmpAtomContainerSet.addAtomContainer(tmpAtomContainer);
        Assertions.assertEquals(1, tmpAtomContainerSet.getAtomContainerCount());
        //
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        tmpCurationPipeline.assignMolIdToAtomContainers(tmpAtomContainerSet);
        Assertions.assertSame(
                tmpAtomContainer,
                tmpAtomContainerSet.getAtomContainer(tmpAtomContainer.getProperty(CurationPipeline.MOL_ID_PROPERTY_NAME))
        );
    }

    /**
     * Tests whether the method .assignMolIdToAtomContainers() of the class FilterPipeline assigns a MolID of to each
     * atom container in the atom container set passed to the method that equals its index in the atom container set.
     */
    @Test
    public void assignMolIdToAtomContainersTest_multipleACs_propertyMolIDEqualsTheACsIndexInTheACSet() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        Assertions.assertEquals(3, tmpAtomContainerSet.getAtomContainerCount());
        //
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        tmpCurationPipeline.assignMolIdToAtomContainers(tmpAtomContainerSet);
        for (IAtomContainer tmpAtomContainer :
                tmpAtomContainerSet.atomContainers()) {
            Assertions.assertSame(
                    tmpAtomContainer,
                    tmpAtomContainerSet.getAtomContainer(tmpAtomContainer.getProperty(CurationPipeline.MOL_ID_PROPERTY_NAME))
            );
        }
    }

    /**
     * Tests whether a NullPointerException is thrown if the atom container set given to the
     * .assignMolIdToAtomContainers() method of the class FilterPipeline is null.
     */
    @Test
    public void assignMolIdToAtomContainersTest_throwNullPointerExceptionIfGivenParamIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    CurationPipeline tmpCurationPipeline = new CurationPipeline();
                    tmpCurationPipeline.assignMolIdToAtomContainers(null);
                }
        );
    }

    /*@Test   //TODO: check every single AC for not being null? I did not even find a way to create / cause that situation
    public void assignMolIdToAtomContainersTest_throwNullPointerExceptionIfOneOfTheACsIsNull() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        //IAtomContainer tmpAtomContainer = null;
        //tmpAtomContainerSet.replaceAtomContainer(1, tmpAtomContainer);
        //tmpAtomContainerSet.addAtomContainer(tmpAtomContainer);
        Assertions.assertEquals(3, tmpAtomContainerSet.getAtomContainerCount());
        //
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    FilterPipeline tmpFilterPipeline = new FilterPipeline();
                    tmpFilter.assignMolIdToAtomContainers(tmpAtomContainerSet);
                }
        );
    }*/

    /**
     * Tests whether the value returned by the .getAssignedMolID() method of class FilterPipeline is of type Integer.
     */
    @Test
    public void getAssignedMolIDTest_returnsInt() {
        IAtomContainer tmpAtomContainer = new AtomContainer();
        tmpAtomContainer.setProperty(CurationPipeline.MOL_ID_PROPERTY_NAME, 0);
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        Assertions.assertInstanceOf(Integer.class, tmpCurationPipeline.getAssignedMolID(tmpAtomContainer));
    }

    /**
     * Tests whether the integer value returned by the .getAssignedMolID() method of class FilterPipeline has the value
     * of the given atom containers MolID (atom container property "Filter.MolID").
     */
    @Test
    public void getAssignedMolIDTest_returnedIntEqualsAtomContainersMolID_MolID0() {
        IAtomContainer tmpAtomContainer = new AtomContainer();
        int tmpMolID = 0;
        tmpAtomContainer.setProperty(CurationPipeline.MOL_ID_PROPERTY_NAME, tmpMolID);
        //
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        Assertions.assertEquals(tmpMolID, tmpCurationPipeline.getAssignedMolID(tmpAtomContainer));
    }

    /**
     * Tests whether the integer value returned by the .getAssignedMolID() method of class FilterPipeline has the value
     * of the given atom containers MolID (atom container property "Filter.MolID").
     */
    @Test
    public void getAssignedMolIDTest_returnedIntEqualsAtomContainersMolID_MolID1() {
        IAtomContainer tmpAtomContainer = new AtomContainer();
        int tmpMolID = 1;
        tmpAtomContainer.setProperty(CurationPipeline.MOL_ID_PROPERTY_NAME, tmpMolID);
        //
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        Assertions.assertEquals(tmpMolID, tmpCurationPipeline.getAssignedMolID(tmpAtomContainer));
    }

    /**
     * Tests whether a NullPointerException is thrown if the atom container given to the .getAssignedMolID() method of
     * class FilterPipeline is null.
     */
    @Test
    public void getAssignedMolIDTest_throwsNullPointerExceptionIfGivenAtomContainerIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    new CurationPipeline().getAssignedMolID(null);
                }
        );
    }

    /**
     * Tests whether an IllegalArgumentException is thrown if the MolID (atom container property "Filter.MolID") of the
     * atom container given to the .getAssignedMolID() method of class FilterPipeline is null.
     */
    @Test
    public void getAssignedMolIDTest_throwsIllegalArgumentExceptionIfGivenAtomContainersMolIDIsNull() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    new CurationPipeline().getAssignedMolID(new AtomContainer());
                }
        );
    }

    /**
     * Tests whether an IllegalArgumentException is thrown if the MolID (atom container property "Filter.MolID") of the
     * atom container given to the .getAssignedMolID() method of class FilterPipeline is not an integer value.
     */
    @Test
    public void getAssignedMolIDTest_throwsIllegalArgumentExceptionIfGivenAtomContainersMolIDIsNotOfDataTypeInt() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    IAtomContainer tmpAtomContainer = new AtomContainer();
                    tmpAtomContainer.setProperty(CurationPipeline.MOL_ID_PROPERTY_NAME, new Object());
                    new CurationPipeline().getAssignedMolID(tmpAtomContainer);
                }
        );
    }

    /**
     * Tests whether the return value of the .getArrayOfAssignedMolIDs() method of class FilterPipeline is not null.
     */
    @Test
    public void getArrayOfAssignedMolIDsTest_returnsNotNull() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        Assertions.assertNotNull(tmpCurationPipeline.getArrayOfAssignedMolIDs(new AtomContainerSet()));
    }

    /**
     * Tests whether the return value of the .getArrayOfAssignedMolIDs() method of class FilterPipeline is an integer
     * array.
     */
    @Test
    public void getArrayOfAssignedMolIDsTest_returnsArrayOfIntegers() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        Assertions.assertInstanceOf(int[].class, tmpCurationPipeline.getArrayOfAssignedMolIDs(new AtomContainerSet()));
    }

    /**
     * Tests whether the .getArrayOfAssignedMolIDs() method of the class FilterPipeline returns an array of length one
     * if an atom container set with a single atom container is given.
     */
    @Test
    public void getArrayOfAssignedMolIDsTest_returnsArrayOfLengthOneIfGiven1AC() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(1);
        Assertions.assertEquals(1, tmpAtomContainerSet.getAtomContainerCount());
        //
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        tmpCurationPipeline.assignMolIdToAtomContainers(tmpAtomContainerSet);
        Assertions.assertEquals(1, tmpCurationPipeline.getArrayOfAssignedMolIDs(tmpAtomContainerSet).length);
    }

    /**
     * Tests whether one integer value contained by the array returned by the .getArrayOfAssignedMolIDs() method of
     * class FilterPipeline equals the MolID (atom container property "Filter.MolID") assigned to the single atom
     * container contained by the given atom container set.
     */
    @Test
    public void getArrayOfAssignedMolIDsTest_returnedValueEqualsMolIDOfGivenAtomContainer() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(1);
        Assertions.assertEquals(1, tmpAtomContainerSet.getAtomContainerCount());
        //
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        tmpCurationPipeline.assignMolIdToAtomContainers(tmpAtomContainerSet);
        int[] tmpMolIDArray = tmpCurationPipeline.getArrayOfAssignedMolIDs(tmpAtomContainerSet);
        Assertions.assertEquals((Integer) tmpAtomContainerSet.getAtomContainer(0).getProperty(CurationPipeline.MOL_ID_PROPERTY_NAME), tmpMolIDArray[0]);
    }

    //TODO: adopt following methods
    //TODO: use the .getAssignedMolID() method in .getArrayOfAssignedMolIDs()

    /**
     * Tests whether the .getArrayOfAssignedMolIDs() method of the class FilterPipeline returns an array of length three
     * if given an atom container set of three atom containers.
     */
    @Test
    public void getArrayOfAssignedMolIDsTest_returnsArrayOfLengthThreeIfGiven3ACs() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        Assertions.assertEquals(3, tmpAtomContainerSet.getAtomContainerCount());
        //
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        tmpCurationPipeline.assignMolIdToAtomContainers(tmpAtomContainerSet);
        Assertions.assertEquals(3, tmpCurationPipeline.getArrayOfAssignedMolIDs(tmpAtomContainerSet).length);
    }

    /** TODO
     * Tests whether the .getArrayOfAssignedMolIDs() method of the class FilterPipeline returns an array of length three
     * if an atom container set of three atom containers is given and whether the values contained by the array equal
     * the consistently numbered MolIDs of the three atom containers.
     */
    @Test
    public void getArrayOfAssignedMolIDsTest_returnedValuesEqualsMolIDsOfGivenACs_3ACs_consistentNumbering() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        Assertions.assertEquals(3, tmpAtomContainerSet.getAtomContainerCount());
        //
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        tmpCurationPipeline.assignMolIdToAtomContainers(tmpAtomContainerSet);
        int[] tmpMolIDArray = tmpCurationPipeline.getArrayOfAssignedMolIDs(tmpAtomContainerSet);
        for (int i = 0; i < tmpAtomContainerSet.getAtomContainerCount(); i++) {
            Assertions.assertEquals((Integer) tmpAtomContainerSet.getAtomContainer(i).getProperty(CurationPipeline.MOL_ID_PROPERTY_NAME), tmpMolIDArray[i]);
        }
    }

    /** TODO?
     * Tests whether the .getArrayOfAssignedMolIDs() method of the class FilterPipeline returns an array of length three
     * if an atom container set of three atom containers is given and whether the values contained by the array equal
     * the inconsistently numbered MolIDs of the three atom containers.
     */
    @Test
    public void getArrayOfAssignedMolIDsTest_returnedValuesEqualsMolIDsOfGivenACs_3ACs_manuallySetInconsistentMolIDs() {   //TODO
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        tmpAtomContainerSet.getAtomContainer(0).setProperty(CurationPipeline.MOL_ID_PROPERTY_NAME, 9);
        tmpAtomContainerSet.getAtomContainer(1).setProperty(CurationPipeline.MOL_ID_PROPERTY_NAME, 3);
        tmpAtomContainerSet.getAtomContainer(2).setProperty(CurationPipeline.MOL_ID_PROPERTY_NAME, 7);
        Assertions.assertEquals(3, tmpAtomContainerSet.getAtomContainerCount());
        //
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        int[] tmpMolIDArray = tmpCurationPipeline.getArrayOfAssignedMolIDs(tmpAtomContainerSet);
        Assertions.assertEquals(3, tmpMolIDArray.length);
        for (int i = 0; i < tmpAtomContainerSet.getAtomContainerCount(); i++) {
            Assertions.assertEquals((Integer) tmpAtomContainerSet.getAtomContainer(i).getProperty(CurationPipeline.MOL_ID_PROPERTY_NAME), tmpMolIDArray[i]);
        }
    }

    /**
     * Tests whether the .getArrayOfAssignerMolIDs() method of class FilterPipeline throws an IllegalArgumentException
     * if a given atom container lacks a MolID.
     */
    @Test
    public void getArrayOfAssignedMolIDsTest_throwsIllegalArgumentExceptionIfAnAtomContainerLacksAMolID() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(1);
                    new CurationPipeline().getArrayOfAssignedMolIDs(tmpAtomContainerSet);
                }
        );
    }

    /**
     * Tests whether the .getArrayOfAssignerMolIDs() method of class FilterPipeline throws an IllegalArgumentException
     * if a given atom container has a MolID that is no integer value.
     */
    @Test
    public void getArrayOfAssignedMolIDsTest_throwsIllegalArgumentExceptionIfAnAtomContainerHasAMolIdThatIsNoIntegerValue() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(1);
                    tmpAtomContainerSet.getAtomContainer(0).setProperty(CurationPipeline.FILTER_ID_PROPERTY_NAME, new Object());
                    new CurationPipeline().getArrayOfAssignedMolIDs(tmpAtomContainerSet);
                }
        );
    }

    /**
     * Tests whether the .getArrayOfAssignerMolIDs() method of class FilterPipeline throws an IllegalArgumentException
     * if one of multiple given atom containers lacks a MolID.
     */
    @Test
    public void getArrayOfAssignedMolIDsTest_multipleACs_oneWithNoMolID_throwsIllegalArgumentException() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
                    CurationPipeline tmpCurationPipeline = new CurationPipeline();
                    tmpCurationPipeline.assignMolIdToAtomContainers(tmpAtomContainerSet);
                    tmpAtomContainerSet.getAtomContainer(2).setProperty(CurationPipeline.MOL_ID_PROPERTY_NAME, null);
                    tmpCurationPipeline.getArrayOfAssignedMolIDs(tmpAtomContainerSet);
                }
        );
    }

    /**
     * Tests whether the .getArrayOfAssignerMolIDs() method of class FilterPipeline throws an IllegalArgumentException
     * if one of multiple given atom containers has a MolID that is no integer value.
     */
    @Test
    public void getArrayOfAssignedMolIDsTest_multipleACs_oneWithNotIntegerValueMolID_throwsIllegalArgumentException() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
                    CurationPipeline tmpCurationPipeline = new CurationPipeline();
                    tmpCurationPipeline.assignMolIdToAtomContainers(tmpAtomContainerSet);
                    tmpAtomContainerSet.getAtomContainer(1).setProperty(CurationPipeline.MOL_ID_PROPERTY_NAME, new Object());
                    tmpCurationPipeline.getArrayOfAssignedMolIDs(tmpAtomContainerSet);
                }
        );
    }

    /**
     * Tests whether a NullPointerException is thrown if the atom container set given to the .getArrayOfAssignedMolIDs()
     * method of the class FilterPipeline is null.
     */
    @Test
    public void getArrayOfAssignedMolIDsTest_throwNullPointerExceptionIfGivenAtomContainerSetIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    new CurationPipeline().getArrayOfAssignedMolIDs(null);
                }
        );
    }

}
