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
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

/**
 * Test class of the ProcessingStepUtils class.
 *
 * @see ProcessingStepUtils
 */
public class ProcessingStepUtilsTest {

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
            Assertions.assertEquals(i, Integer.parseInt(tmpAtomContainerSet.getAtomContainer(i)
                    .getProperty(IProcessingStep.MOL_ID_PROPERTY_NAME)));
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
     * Tests whether the .getAssignedMolID() method returns a string representation of the MolID assigned to the given
     * atom container.
     */
    @Test
    public void getAssignedMolIDTest_returnsMolIDStringRepresentation() {
        IAtomContainer tmpAtomContainer = new AtomContainer();
        //
        String tmpMolIDString = "10";
        tmpAtomContainer.setProperty(IProcessingStep.MOL_ID_PROPERTY_NAME, tmpMolIDString);
        Assertions.assertSame(tmpMolIDString, ProcessingStepUtils.getAssignedMolID(tmpAtomContainer));
        //
        Integer tmpMolIDInteger = 5;
        tmpAtomContainer.setProperty(IProcessingStep.MOL_ID_PROPERTY_NAME, tmpMolIDInteger);
        Assertions.assertInstanceOf(String.class, ProcessingStepUtils.getAssignedMolID(tmpAtomContainer));
        Assertions.assertEquals(tmpMolIDInteger, Integer.parseInt(ProcessingStepUtils.getAssignedMolID(tmpAtomContainer)));
    }

    /**
     * Tests whether method .getAssignedMolID() throws a NullPointerException if the given atom container is null.
     */
    @Test
    public void getAssignedMolIDTest_null_throwsNullPointerException() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    ProcessingStepUtils.getAssignedMolID(null);
                }
        );
    }

    /**
     * Tests whether method .getAssignedMolID() throws an IllegalArgumentException if the atom container property
     * {@link IProcessingStep#MOL_ID_PROPERTY_NAME} is null.
     */
    @Test
    public void getAssignedMolIDTest_atomContainerWithMolIDNull_throwsIllegalArgumentException() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    ProcessingStepUtils.getAssignedMolID(new AtomContainer());
                }
        );
    }

    /**
     * Tests whether the array returned by method .getArrayOfAssignedMolIDs() contains the string representations of
     * the MolIDs of the given atom containers.
     */
    @Test
    public void getArrayOfAssignedMolIDsTest_3ACs_manuallySetMolIDs_returnsArrayOfStringRepresentationsOfMolIDs() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        tmpAtomContainerSet.getAtomContainer(0).setProperty(IProcessingStep.MOL_ID_PROPERTY_NAME, "9");
        tmpAtomContainerSet.getAtomContainer(1).setProperty(IProcessingStep.MOL_ID_PROPERTY_NAME, "3");
        tmpAtomContainerSet.getAtomContainer(2).setProperty(IProcessingStep.MOL_ID_PROPERTY_NAME, 7);
        Assertions.assertEquals(3, tmpAtomContainerSet.getAtomContainerCount());
        //
        String[] tmpMolIDArray = ProcessingStepUtils.getArrayOfAssignedMolIDs(tmpAtomContainerSet);
        Assertions.assertEquals(3, tmpMolIDArray.length);
        Assertions.assertSame(ProcessingStepUtils.getAssignedMolID(tmpAtomContainerSet.getAtomContainer(0)), tmpMolIDArray[0]);
        Assertions.assertSame(ProcessingStepUtils.getAssignedMolID(tmpAtomContainerSet.getAtomContainer(1)), tmpMolIDArray[1]);
        Assertions.assertEquals(
                (Integer) tmpAtomContainerSet.getAtomContainer(2).getProperty(IProcessingStep.MOL_ID_PROPERTY_NAME),
                Integer.parseInt(tmpMolIDArray[2])
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

}
