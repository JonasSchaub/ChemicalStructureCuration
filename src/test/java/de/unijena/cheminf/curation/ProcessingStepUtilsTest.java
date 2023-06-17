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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

/**
 * Test class of the ProcessingStepUtils class.
 *
 * @see ProcessingStepUtils
 */
public class ProcessingStepUtilsTest {

    /*
    TODO: rework tests of getArrayOfAssignedMolIDs
    TODO: rework tests of getAssignedMolID
     */

    /**
     * Tests whether the method .assignMolIdToAtomContainers() initializes the atom container property
     * {@link IProcessingStep#MOL_ID_PROPERTY_NAME} of every given atom container with a String containing the
     * respective index of the atom container in the atom container set.
     */
    @Test
    public void assignMolIdToAtomContainersTest_3ACs_assignsMolIDContainingTheACsIndexInSet() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        Assertions.assertEquals(3, tmpAtomContainerSet.getAtomContainerCount());
        //
        ProcessingStepUtils.assignMolIdToAtomContainers(tmpAtomContainerSet);
        for (int i = 0; i < tmpAtomContainerSet.getAtomContainerCount(); i++) {
            Assertions.assertEquals(i, Integer.parseInt(tmpAtomContainerSet.getAtomContainer(i).getProperty(IProcessingStep.MOL_ID_PROPERTY_NAME)));
        }
    }

    /**
     * Tests whether a NullPointerException is thrown if the atom container set given to method
     * .assignMolIdToAtomContainers() is null.
     */
    @Test
    public void assignMolIdToAtomContainersTest_null_throwNullPointerException() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    ProcessingStepUtils.assignMolIdToAtomContainers(null);
                }
        );
    }

    /**
     * Tests whether the method .removeMolIdPropertyFromAtomContainers() removes the respective property of all the
     * atom containers given set.
     */
    @Test
    public void removeMolIdPropertyFromAtomContainers_3ACs_removesMolIDProperty() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        ProcessingStepUtils.assignMolIdToAtomContainers(tmpAtomContainerSet);
        ProcessingStepUtils.removeMolIdPropertyFromAtomContainers(tmpAtomContainerSet);
        for (IAtomContainer tmpAtomContainer :
                tmpAtomContainerSet.atomContainers()) {
            Assertions.assertNull(tmpAtomContainer.getProperty(IProcessingStep.MOL_ID_PROPERTY_NAME));
        }
    }

    /**
     * Tests whether the value returned by the .getAssignedMolID() method of class FilterPipeline is of type Integer.
     */
    @Test
    @Disabled
    public void getAssignedMolIDTest_returnsInt() {
        IAtomContainer tmpAtomContainer = new AtomContainer();
        tmpAtomContainer.setProperty(CurationPipeline.MOL_ID_PROPERTY_NAME, 0);
        Assertions.assertInstanceOf(Integer.class, ProcessingStepUtils.getAssignedMolID(tmpAtomContainer));
    }

    /**
     * Tests whether the integer value returned by the .getAssignedMolID() method of class FilterPipeline has the value
     * of the given atom containers MolID (atom container property "Filter.MolID").
     */
    @Test
    @Disabled
    public void getAssignedMolIDTest_returnedIntEqualsAtomContainersMolID_MolID_0() {
        IAtomContainer tmpAtomContainer = new AtomContainer();
        int tmpMolID = 0;
        tmpAtomContainer.setProperty(CurationPipeline.MOL_ID_PROPERTY_NAME, tmpMolID);
        //
        Assertions.assertEquals(tmpMolID, ProcessingStepUtils.getAssignedMolID(tmpAtomContainer));
    }

    /**
     * Tests whether the integer value returned by the .getAssignedMolID() method of class FilterPipeline has the value
     * of the given atom containers MolID (atom container property "Filter.MolID").
     */
    @Test
    @Disabled
    public void getAssignedMolIDTest_returnedIntEqualsAtomContainersMolID_MolID_1() {
        IAtomContainer tmpAtomContainer = new AtomContainer();
        int tmpMolID = 1;
        tmpAtomContainer.setProperty(CurationPipeline.MOL_ID_PROPERTY_NAME, tmpMolID);
        //
        Assertions.assertEquals(tmpMolID, ProcessingStepUtils.getAssignedMolID(tmpAtomContainer));
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
                    ProcessingStepUtils.getAssignedMolID(null);
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
                    ProcessingStepUtils.getAssignedMolID(new AtomContainer());
                }
        );
    }

    /**
     * Tests whether an IllegalArgumentException is thrown if the MolID (atom container property "Filter.MolID") of the
     * atom container given to the .getAssignedMolID() method of class FilterPipeline is not an integer value.
     */
    @Test
    @Disabled
    public void getAssignedMolIDTest_throwsIllegalArgumentExceptionIfGivenAtomContainersMolIDIsNotOfDataTypeInt() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    IAtomContainer tmpAtomContainer = new AtomContainer();
                    tmpAtomContainer.setProperty(CurationPipeline.MOL_ID_PROPERTY_NAME, new Object());
                    ProcessingStepUtils.getAssignedMolID(tmpAtomContainer);
                }
        );
    }

    /**
     * Tests whether the return value of the .getArrayOfAssignedMolIDs() method of class FilterPipeline is not null.
     */
    @Test
    public void getArrayOfAssignedMolIDsTest_returnsNotNull() {
        Assertions.assertNotNull(ProcessingStepUtils.getArrayOfAssignedMolIDs(new AtomContainerSet()));
    }

    /**
     * Tests whether the return value of the .getArrayOfAssignedMolIDs() method of class FilterPipeline is an integer
     * array.
     */
    @Test
    @Disabled
    public void getArrayOfAssignedMolIDsTest_returnsArrayOfIntegers() {
        Assertions.assertInstanceOf(int[].class, ProcessingStepUtils.getArrayOfAssignedMolIDs(new AtomContainerSet()));
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
        ProcessingStepUtils.assignMolIdToAtomContainers(tmpAtomContainerSet);
        Assertions.assertEquals(1, ProcessingStepUtils.getArrayOfAssignedMolIDs(tmpAtomContainerSet).length);
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
        ProcessingStepUtils.assignMolIdToAtomContainers(tmpAtomContainerSet);
        String[] tmpMolIDArray = ProcessingStepUtils.getArrayOfAssignedMolIDs(tmpAtomContainerSet);
        Assertions.assertSame(tmpAtomContainerSet.getAtomContainer(0).getProperty(CurationPipeline.MOL_ID_PROPERTY_NAME), tmpMolIDArray[0]);
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
        ProcessingStepUtils.assignMolIdToAtomContainers(tmpAtomContainerSet);
        Assertions.assertEquals(3, ProcessingStepUtils.getArrayOfAssignedMolIDs(tmpAtomContainerSet).length);
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
        ProcessingStepUtils.assignMolIdToAtomContainers(tmpAtomContainerSet);
        String[] tmpMolIDArray = ProcessingStepUtils.getArrayOfAssignedMolIDs(tmpAtomContainerSet);
        for (int i = 0; i < tmpAtomContainerSet.getAtomContainerCount(); i++) {
            Assertions.assertSame(tmpAtomContainerSet.getAtomContainer(i).getProperty(CurationPipeline.MOL_ID_PROPERTY_NAME), tmpMolIDArray[i]);
        }
    }

    /** TODO?
     * Tests whether the .getArrayOfAssignedMolIDs() method of the class FilterPipeline returns an array of length three
     * if an atom container set of three atom containers is given and whether the values contained by the array equal
     * the inconsistently numbered MolIDs of the three atom containers.
     */
    @Test
    public void getArrayOfAssignedMolIDsTest_returnedValuesEqualMolIDsOfGivenACs_3ACs_manuallySetInconsistentMolIDs() {   //TODO
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        tmpAtomContainerSet.getAtomContainer(0).setProperty(CurationPipeline.MOL_ID_PROPERTY_NAME, "9");
        tmpAtomContainerSet.getAtomContainer(1).setProperty(CurationPipeline.MOL_ID_PROPERTY_NAME, "3");
        tmpAtomContainerSet.getAtomContainer(2).setProperty(CurationPipeline.MOL_ID_PROPERTY_NAME, "7");
        Assertions.assertEquals(3, tmpAtomContainerSet.getAtomContainerCount());
        //
        String[] tmpMolIDArray = ProcessingStepUtils.getArrayOfAssignedMolIDs(tmpAtomContainerSet);
        Assertions.assertEquals(3, tmpMolIDArray.length);
        for (int i = 0; i < tmpAtomContainerSet.getAtomContainerCount(); i++) {
            Assertions.assertSame(tmpAtomContainerSet.getAtomContainer(i).getProperty(CurationPipeline.MOL_ID_PROPERTY_NAME), tmpMolIDArray[i]);
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
                    ProcessingStepUtils.getArrayOfAssignedMolIDs(tmpAtomContainerSet);
                }
        );
    }

    /**
     * Tests whether the .getArrayOfAssignerMolIDs() method of class FilterPipeline throws an IllegalArgumentException
     * if a given atom container has a MolID that is no integer value.
     */
    @Test
    @Disabled
    public void getArrayOfAssignedMolIDsTest_throwsIllegalArgumentExceptionIfAnAtomContainerHasAMolIdThatIsNoIntegerValue() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(1);
                    tmpAtomContainerSet.getAtomContainer(0).setProperty(CurationPipeline.MOL_ID_PROPERTY_NAME, new Object());
                    ProcessingStepUtils.getArrayOfAssignedMolIDs(tmpAtomContainerSet);
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
                    ProcessingStepUtils.assignMolIdToAtomContainers(tmpAtomContainerSet);
                    tmpAtomContainerSet.getAtomContainer(2).setProperty(CurationPipeline.MOL_ID_PROPERTY_NAME, null);
                    ProcessingStepUtils.getArrayOfAssignedMolIDs(tmpAtomContainerSet);
                }
        );
    }

    /**
     * Tests whether a NullPointerException is thrown if the atom container set given to the .getArrayOfAssignedMolIDs()
     * method is null.
     */
    @Test
    public void getArrayOfAssignedMolIDsTest_null_throwsNullPointerException() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    ProcessingStepUtils.getArrayOfAssignedMolIDs(null);
                }
        );
    }

}
