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
 * Test class for the FilterPipeline class. It holds all the test methods that are associated with the FilterID, which
 * is assigned to every atom container of an atom container set that is passed to the .filter() method of class
 * FilterPipeline. This is done to track the ID of the Filter (index in the listOfSelectedFilters) an atom container has
 * been filtered by and thereby trace the filtering process.
 */
public class FilterPipelineFilterIDTests {

    /**
     * Tests whether the value returned by the .getAssignedFilterID() method of class FilterPipeline is of type Integer.
     */
    @Test
    public void getAssignedFilterID_returnsIntegerValue() {
        IAtomContainer tmpAtomContainer = new AtomContainer();
        int tmpAnyFilterID = 0;
        tmpAtomContainer.setProperty(CurationPipeline.FILTER_ID_PROPERTY_NAME, tmpAnyFilterID);
        Assertions.assertInstanceOf(Integer.class, new CurationPipeline().getAssignedFilterID(tmpAtomContainer));
    }

    /**
     * Tests whether the integer value returned by the .getAssignedFilterID() method of class FilterPipeline has the
     * value of the given atom containers FilterID (atom container property "FilterPipeline.FilterID").
     */
    @Test
    public void getAssignedFilterID_returnedValueEqualsFilterIDOfGivenAC_0() {
        IAtomContainer tmpAtomContainer = new AtomContainer();
        int tmpAnyFilterID = 0;
        tmpAtomContainer.setProperty(CurationPipeline.FILTER_ID_PROPERTY_NAME, tmpAnyFilterID);
        Assertions.assertEquals(tmpAnyFilterID, new CurationPipeline().getAssignedFilterID(tmpAtomContainer));
    }

    /**
     * Tests whether the integer value returned by the .getAssignedFilterID() method of class FilterPipeline has the
     * value of the given atom containers FilterID (atom container property "FilterPipeline.FilterID").
     */
    @Test
    public void getAssignedFilterID_returnedValueEqualsFilterIDOfGivenAC_1() {
        IAtomContainer tmpAtomContainer = new AtomContainer();
        int tmpAnyFilterID = 1;
        tmpAtomContainer.setProperty(CurationPipeline.FILTER_ID_PROPERTY_NAME, tmpAnyFilterID);
        Assertions.assertEquals(tmpAnyFilterID, new CurationPipeline().getAssignedFilterID(tmpAtomContainer));
    }

    /**
     * Tests whether a NullPointerException is thrown if the atom container given to the .getAssignedFilterID() method
     * of class FilterPipeline is null.
     */
    @Test
    public void getAssignedFilterID_throwsNullPointerExceptionIfGivenAtomContainerIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    new CurationPipeline().getAssignedFilterID(null);
                }
        );
    }

    /**
     * Tests whether an IllegalArgumentException is thrown if the FilterID (atom container property
     * "FilterPipeline.FilterID") of the atom container given to the .getAssignedFilterID() method of class
     * FilterPipeline is null.
     */
    @Test
    public void getAssignedFilterID_throwsIllegalArgumentExceptionIfGivenACsFilterIDIsNull() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    new CurationPipeline().getAssignedFilterID(new AtomContainer());
                }
        );
    }

    /**
     * Tests whether an IllegalArgumentException is thrown if the FilterID (atom container property
     * "FilterPipeline.FilterID") of the atom container given to the .getAssignedFilterID() method of class
     * FilterPipeline is not an integer value.
     */
    @Test
    public void getAssignedFilterID_throwsIllegalArgumentExceptionIfGivenACsFilterIDIsNotOfTypeInteger() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    IAtomContainer tmpAtomContainer = new AtomContainer();
                    tmpAtomContainer.setProperty(CurationPipeline.FILTER_ID_PROPERTY_NAME, new Object());
                    new CurationPipeline().getAssignedFilterID(tmpAtomContainer);
                }
        );
    }

    /**
     * Tests whether the return value of the .getArrayOfAssignedFilterIDs() method of class FilterPipeline is not null.
     */
    @Test
    public void getArrayOfAssignedFilterIDsTest_returnsNotNull() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        Assertions.assertNotNull(tmpCurationPipeline.getArrayOfAssignedFilterIDs(new AtomContainerSet()));
    }

    /**
     * Tests whether the return value of the .getArrayOfAssignedFilterIDs() method of class FilterPipeline is an integer
     * array.
     */
    @Test
    public void getArrayOfAssignedFilterIDsTest_returnsArrayOfIntegers() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        Assertions.assertInstanceOf(int[].class, tmpCurationPipeline.getArrayOfAssignedFilterIDs(new AtomContainerSet()));
    }

    /**
     * Tests whether the .getArrayOfAssignedFilterIDs() method of the class FilterPipeline returns an array of length
     * one if an atom container set with a single atom container is given.
     */
    @Test
    public void getArrayOfAssignedFilterIDsTest_returnsArrayOfLengthOneIfGiven1AC() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(1);
        int tmpAnyFilterID = 0;
        tmpAtomContainerSet.getAtomContainer(0).setProperty(CurationPipeline.FILTER_ID_PROPERTY_NAME, tmpAnyFilterID);
        //
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        Assertions.assertEquals(1, tmpCurationPipeline.getArrayOfAssignedFilterIDs(tmpAtomContainerSet).length);
    }

    /**
     * Tests whether the integer value contained by the array returned by the .getArrayOfAssignedFilterIDs() method of
     * class FilterPipeline equals the FilterID (atom container property "FilterPipeline.FilterID") assigned to the
     * single atom container contained by the given atom container set.
     */
    @Test
    public void getArrayOfAssignedFilterIDsTest_returnedValueEqualsFilterIDOfGivenAtomContainer_FilterID0() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(1);
        int tmpFilterID = 0;
        tmpAtomContainerSet.getAtomContainer(0).setProperty(CurationPipeline.FILTER_ID_PROPERTY_NAME, tmpFilterID);
        //
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        int[] tmpFilterIDArray = tmpCurationPipeline.getArrayOfAssignedFilterIDs(tmpAtomContainerSet);
        Assertions.assertEquals(tmpFilterID, tmpFilterIDArray[0]);
    }

    /**
     * Tests whether one integer value contained by the array returned by the .getArrayOfAssignedFilterIDs() method of
     * class FilterPipeline equals the FilterID (atom container property "FilterPipeline.FilterID") assigned to the
     * single atom container contained by the given atom container set.
     */
    @Test
    public void getArrayOfAssignedFilterIDsTest_returnedValueEqualsFilterIDOfGivenAtomContainer_FilterID1() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(1);
        int tmpFilterID = 1;
        tmpAtomContainerSet.getAtomContainer(0).setProperty(CurationPipeline.FILTER_ID_PROPERTY_NAME, tmpFilterID);
        //
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        int[] tmpFilterIDArray = tmpCurationPipeline.getArrayOfAssignedFilterIDs(tmpAtomContainerSet);
        Assertions.assertEquals(tmpFilterID, tmpFilterIDArray[0]);
    }

    /**
     * Tests whether the .getArrayOfAssignedFilterIDs() method of the class FilterPipeline returns an array of length
     * three if given an atom container set of three atom containers.
     */
    @Test
    public void getArrayOfAssignedFilterIDsTest_returnsArrayOfLengthThreeIfGiven3ACs() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        for (IAtomContainer tmpAtomContainer :
                tmpAtomContainerSet.atomContainers()) {
            tmpAtomContainer.setProperty(CurationPipeline.FILTER_ID_PROPERTY_NAME, 0);
        }
        //
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        Assertions.assertEquals(3, tmpCurationPipeline.getArrayOfAssignedFilterIDs(tmpAtomContainerSet).length);
    }

    /**
     * Tests whether the three integer values contained by the array returned by the .getArrayOfAssignedFilterIDs()
     * method of class FilterPipeline equal the FilterIDs (atom container property "FilterPipeline.FilterID") assigned
     * to the three atom containers contained by the given atom container set. Here, the three FilterIDs are of a
     * uniform value.
     */
    @Test
    public void getArrayOfAssignedFilterIDsTest_returnedValuesEqualsMolIDsOfGivenACs_3ACs_all0() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        int tmpFilterID = 0;
        for (IAtomContainer tmpAtomContainer :
                tmpAtomContainerSet.atomContainers()) {
            tmpAtomContainer.setProperty(CurationPipeline.FILTER_ID_PROPERTY_NAME, tmpFilterID);
        }
        //
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        int[] tmpFilterIDArray = tmpCurationPipeline.getArrayOfAssignedFilterIDs(tmpAtomContainerSet);
        for (int i = 0; i < tmpAtomContainerSet.getAtomContainerCount(); i++) {
            Assertions.assertEquals(tmpFilterID, tmpFilterIDArray[i]);
        }
    }

    /**
     * Tests whether the three integer values contained by the array returned by the .getArrayOfAssignedFilterIDs()
     * method of class FilterPipeline equal the FilterIDs (atom container property "FilterPipeline.FilterID") assigned
     * to the three atom containers contained by the given atom container set. Here, the three FilterIDs are of
     * different values.
     */
    @Test
    public void getArrayOfAssignedFilterIDsTest_returnedValuesEqualsMolIDsOfGivenACs_3ACs_differentIDs() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        int[] tmpFilterIDArray = new int[]{4, 0, 2};
        for (int i = 0; i < tmpAtomContainerSet.getAtomContainerCount(); i++) {
            tmpAtomContainerSet.getAtomContainer(i).setProperty(CurationPipeline.FILTER_ID_PROPERTY_NAME, tmpFilterIDArray[i]);
        }
        //
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        Assertions.assertArrayEquals(tmpFilterIDArray, tmpCurationPipeline.getArrayOfAssignedFilterIDs(tmpAtomContainerSet));
    }

    /**
     * Tests whether a NullPointerException is thrown if the atom container set given to the
     * .getArrayOfAssignedFilterIDs() method of the class FilterPipeline is null.
     */
    @Test
    public void getArrayOfAssignedFilterIDsTest_throwNullPointerExceptionIfGivenAtomContainerSetIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    new CurationPipeline().getArrayOfAssignedFilterIDs(null);
                }
        );
    }

    /**
     * Tests whether an IllegalArgumentException is thrown if the FilterID (atom container property
     * "FilterPipeline.FilterID") of an atom container of the atom container set given to the
     * .getArrayOfAssignedFilterIDs() method of class FilterPipeline is null.
     */
    @Test
    public void getArrayOfAssignedFilterIDs_throwsIllegalArgumentExceptionIfAGivenACsFilterIDIsNull() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(1);
                    new CurationPipeline().getArrayOfAssignedFilterIDs(tmpAtomContainerSet);
                }
        );
    }

    /**
     * Tests whether an IllegalArgumentException is thrown if the FilterID (atom container property
     * "FilterPipeline.FilterID") of an atom container of the atom container set given to the
     * .getArrayOfAssignedFilterIDs() method of class FilterPipeline is not an integer value.
     */
    @Test
    public void getArrayOfAssignedFilterIDs_throwsIllegalArgumentExceptionIfAGivenACsFilterIDIsNotOfTypeInteger() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(1);
                    tmpAtomContainerSet.getAtomContainer(0).setProperty(CurationPipeline.FILTER_ID_PROPERTY_NAME, new Object());
                    new CurationPipeline().getArrayOfAssignedFilterIDs(tmpAtomContainerSet);
                }
        );
    }

    //TODO: any further test methods for .getArrayOfAssignedFilterIDs()?

}
