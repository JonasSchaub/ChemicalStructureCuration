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
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

import java.util.InvalidPropertiesFormatException;

/**
 * Test class for the core methods and functions of class FilterPipeline.
 */
public class FilterPipelineTest {

    /**
     * Tests whether the method .assignMolIdToAtomContainers() of the class FilterPipeline assigns an MolID to a single
     * atom container contained in a given atom container set. The ID should be set as property to the atom container.
     */
    @Test
    public void assignMolIdToAtomContainersTest_singleAC_propertyMolIDIsNotNull() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(1);
        Assertions.assertEquals(1, tmpAtomContainerSet.getAtomContainerCount());
        //
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        tmpFilterPipeline.assignMolIdToAtomContainers(tmpAtomContainerSet);
        Assertions.assertNotNull(tmpAtomContainerSet.getAtomContainer(0).getProperty(FilterPipeline.MOL_ID_PROPERTY_NAME));
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
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        tmpFilterPipeline.assignMolIdToAtomContainers(tmpAtomContainerSet);
        Assertions.assertInstanceOf(Integer.class, tmpAtomContainerSet.getAtomContainer(0).getProperty(FilterPipeline.MOL_ID_PROPERTY_NAME));
    }

    /**
     * Tests whether the method .assignMolIdToAtomContainers() of the class FilterPipeline assigns an MolID of expected
     * value to a single atom container contained in a given atom container set.
     */
    @Test
    public void assignMolIdToAtomContainersTest_singleAC_propertyMolIDIsOfExpectedValue() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(1);
        Assertions.assertEquals(1, tmpAtomContainerSet.getAtomContainerCount());
        //
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        tmpFilterPipeline.assignMolIdToAtomContainers(tmpAtomContainerSet);
        Assertions.assertEquals(0, (Integer) tmpAtomContainerSet.getAtomContainer(0).getProperty(FilterPipeline.MOL_ID_PROPERTY_NAME));
    }

    /**
     * Tests whether the method .assignMolIdToAtomContainers() of the class FilterPipeline assigns MolIDs of expected
     * values to multiple atom containers contained by a given atom container set.
     */
    @Test
    public void assignMolIdToAtomContainersTest_multipleACs_propertyMolIDIsOfExpectedValueRespectively() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        Assertions.assertEquals(3, tmpAtomContainerSet.getAtomContainerCount());
        //
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        tmpFilterPipeline.assignMolIdToAtomContainers(tmpAtomContainerSet);
        for (int i = 0; i < 3; i++) {
            Assertions.assertEquals(i, (Integer) tmpAtomContainerSet.getAtomContainer(i).getProperty(FilterPipeline.MOL_ID_PROPERTY_NAME));
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
                    FilterPipeline tmpFilterPipeline = new FilterPipeline();
                    tmpFilterPipeline.assignMolIdToAtomContainers(null);
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
     * Tests whether the .hasMolIdAssigned() method of class FilterPipeline returns a boolean value.
     */
    @Test
    public void hasMolIdAssigned_returnsBooleanValue() throws InvalidPropertiesFormatException {
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        Assertions.assertInstanceOf(Boolean.class, tmpFilterPipeline.hasMolIdAssigned(new AtomContainer()));
    }

    /**
     * Tests whether the .hasMolIdAssigned() method of class FilterPipeline returns true if the given atom container
     * has an MolID (atom container property FilterPipeline.MolID) of type integer assigned.
     */
    @Test
    public void hasMolIdAssigned_returnsTrueIfAGivenAtomContainerHasAnIntegerTypeMolIdAssigned() throws InvalidPropertiesFormatException {
        IAtomContainer tmpAtomContainer = new AtomContainer();
        int tmpAnyMolID = 0;
        tmpAtomContainer.setProperty(FilterPipeline.MOL_ID_PROPERTY_NAME, tmpAnyMolID);
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        Assertions.assertTrue(tmpFilterPipeline.hasMolIdAssigned(tmpAtomContainer));
    }

    /**
     * Tests whether the .hasMolIdAssigned() method of class FilterPipeline returns false if the given atom container
     * has no MolID (atom container property FilterPipeline.MolID) assigned.
     */
    @Test
    public void hasMolIdAssigned_returnsFalseIfAGivenAtomContainerHasNoMolIdAssigned() throws InvalidPropertiesFormatException {
        IAtomContainer tmpAtomContainer = new AtomContainer();
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        Assertions.assertFalse(tmpFilterPipeline.hasMolIdAssigned(tmpAtomContainer));
    }

    /**
     * Tests whether the .hasMolIdAssigned() method of class FilterPipeline throws an InvalidPropertiesFormatException
     * if the given atom container has an MolID (atom container property FilterPipeline.MolID) assigned that is no
     * integer value.
     */
    @Test
    public void hasMolIdAssigned_throwsWhatIfGivenAtomContainersMolIdIsNotAnInteger() {
        Assertions.assertThrows(
                InvalidPropertiesFormatException.class, //TODO: change type of exception?
                () -> {
                    IAtomContainer tmpAtomContainer = new AtomContainer();
                    tmpAtomContainer.setProperty(FilterPipeline.MOL_ID_PROPERTY_NAME, new Object());
                    new FilterPipeline().hasMolIdAssigned(tmpAtomContainer);
                }
        );
    }

    /**
     * Tests whether a NullPointerException is thrown if the atom container instance given to the .hasMolIdAssigned()
     * method of class FilterPipeline is null.
     */
    @Test
    public void hasMolIdAssigned_throwsNullPointerExceptionIfGivenAtomContainerIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    new FilterPipeline().hasMolIdAssigned(null);
                }
        );
    }

    /**
     * Tests whether the value returned by the .getAssignedMolID() method of class FilterPipeline is of type Integer.
     */
    @Test
    public void getAssignedMolIDTest_returnsInt() {
        IAtomContainer tmpAtomContainer = new AtomContainer();
        tmpAtomContainer.setProperty(FilterPipeline.MOL_ID_PROPERTY_NAME, 0);
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        Assertions.assertInstanceOf(Integer.class, tmpFilterPipeline.getAssignedMolID(tmpAtomContainer));
    }

    /**
     * Tests whether the integer value returned by the .getAssignedMolID() method of class FilterPipeline has the value
     * of the given atom containers MolID (atom container property "Filter.MolID").
     */
    @Test
    public void getAssignedMolIDTest_returnedIntEqualsAtomContainersMolID_MolID0() {
        IAtomContainer tmpAtomContainer = new AtomContainer();
        int tmpMolID = 0;
        tmpAtomContainer.setProperty(FilterPipeline.MOL_ID_PROPERTY_NAME, tmpMolID);
        //
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        Assertions.assertEquals(tmpMolID, tmpFilterPipeline.getAssignedMolID(tmpAtomContainer));
    }

    /**
     * Tests whether the integer value returned by the .getAssignedMolID() method of class FilterPipeline has the value
     * of the given atom containers MolID (atom container property "Filter.MolID").
     */
    @Test
    public void getAssignedMolIDTest_returnedIntEqualsAtomContainersMolID_MolID1() {
        IAtomContainer tmpAtomContainer = new AtomContainer();
        int tmpMolID = 1;
        tmpAtomContainer.setProperty(FilterPipeline.MOL_ID_PROPERTY_NAME, tmpMolID);
        //
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        Assertions.assertEquals(tmpMolID, tmpFilterPipeline.getAssignedMolID(tmpAtomContainer));
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
                    new FilterPipeline().getAssignedMolID(null);
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
                    new FilterPipeline().getAssignedMolID(new AtomContainer());
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
                    tmpAtomContainer.setProperty(FilterPipeline.MOL_ID_PROPERTY_NAME, new Object());
                    new FilterPipeline().getAssignedMolID(tmpAtomContainer);
                }
        );
    }

    /**
     * Tests whether the return value of the .getArrayOfAssignedMolIDs() method of class FilterPipeline is not null.
     */
    @Test
    public void getArrayOfAssignedMolIDsTest_returnsNotNull() {
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        Assertions.assertNotNull(tmpFilterPipeline.getArrayOfAssignedMolIDs(new AtomContainerSet()));
    }

    /**
     * Tests whether the return value of the .getArrayOfAssignedMolIDs() method of class FilterPipeline is an integer
     * array.
     */
    @Test
    public void getArrayOfAssignedMolIDsTest_returnsArrayOfIntegers() {
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        Assertions.assertInstanceOf(int[].class, tmpFilterPipeline.getArrayOfAssignedMolIDs(new AtomContainerSet()));
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
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        tmpFilterPipeline.assignMolIdToAtomContainers(tmpAtomContainerSet);
        Assertions.assertEquals(1, tmpFilterPipeline.getArrayOfAssignedMolIDs(tmpAtomContainerSet).length);
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
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        tmpFilterPipeline.assignMolIdToAtomContainers(tmpAtomContainerSet);
        int[] tmpMolIDArray = tmpFilterPipeline.getArrayOfAssignedMolIDs(tmpAtomContainerSet);
        Assertions.assertEquals((Integer) tmpAtomContainerSet.getAtomContainer(0).getProperty(FilterPipeline.MOL_ID_PROPERTY_NAME), tmpMolIDArray[0]);
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
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        tmpFilterPipeline.assignMolIdToAtomContainers(tmpAtomContainerSet);
        Assertions.assertEquals(3, tmpFilterPipeline.getArrayOfAssignedMolIDs(tmpAtomContainerSet).length);
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
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        tmpFilterPipeline.assignMolIdToAtomContainers(tmpAtomContainerSet);
        int[] tmpMolIDArray = tmpFilterPipeline.getArrayOfAssignedMolIDs(tmpAtomContainerSet);
        for (int i = 0; i < tmpAtomContainerSet.getAtomContainerCount(); i++) {
            Assertions.assertEquals((Integer) tmpAtomContainerSet.getAtomContainer(i).getProperty(FilterPipeline.MOL_ID_PROPERTY_NAME), tmpMolIDArray[i]);
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
        tmpAtomContainerSet.getAtomContainer(0).setProperty(FilterPipeline.MOL_ID_PROPERTY_NAME, 9);
        tmpAtomContainerSet.getAtomContainer(1).setProperty(FilterPipeline.MOL_ID_PROPERTY_NAME, 3);
        tmpAtomContainerSet.getAtomContainer(2).setProperty(FilterPipeline.MOL_ID_PROPERTY_NAME, 7);
        Assertions.assertEquals(3, tmpAtomContainerSet.getAtomContainerCount());
        //
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        int[] tmpMolIDArray = tmpFilterPipeline.getArrayOfAssignedMolIDs(tmpAtomContainerSet);
        Assertions.assertEquals(3, tmpMolIDArray.length);
        for (int i = 0; i < tmpAtomContainerSet.getAtomContainerCount(); i++) {
            Assertions.assertEquals((Integer) tmpAtomContainerSet.getAtomContainer(i).getProperty(FilterPipeline.MOL_ID_PROPERTY_NAME), tmpMolIDArray[i]);
        }
    }

    /** //TODO: set an error value where no MolID exists or throw an exception?
     * TODO
     */
    @Test
    public void getArrayOfAssignedMolIDsTest_setsErrorValueWhereMolIDIsNotSet() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(1);
        //
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        int[] tmpMolIDArray = tmpFilterPipeline.getArrayOfAssignedMolIDs(tmpAtomContainerSet);
        Assertions.assertEquals(1, tmpMolIDArray.length);
        Assertions.assertEquals(FilterPipeline.MOL_ID_ERROR_VALUE, tmpMolIDArray[0]);
    }

    /*@Test //TODO: I found no way to cause this situation
    public void getArrayOfAssignedMolIDsTest_setsErrorValueWhereAnAtomContainerIsNull() {
        IAtomContainerSet tmpAtomContainerSet = new AtomContainerSet();
        tmpAtomContainerSet.addAtomContainer(null);
        //
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        int[] tmpMolIDArray = tmpFilter.getArrayOfAssignedMolIDs(tmpAtomContainerSet);
        Assertions.assertEquals(1, tmpMolIDArray.length);
        Assertions.assertEquals(Filter.MOL_ID_ERROR_VALUE, tmpMolIDArray[0]);
    }*/

    /**
     * TODO
     */
    @Test
    public void getArrayOfAssignedMolIDsTest_ACsWithMolIDAndWithoutCombined_2With_1Without() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        Assertions.assertEquals(3, tmpAtomContainerSet.getAtomContainerCount());
        //
        int tmpAnyMolIDForAC_0 = 4;
        int tmpAnyMolIDForAC_2 = 3;
        tmpAtomContainerSet.getAtomContainer(0).setProperty(FilterPipeline.MOL_ID_PROPERTY_NAME, tmpAnyMolIDForAC_0);
        tmpAtomContainerSet.getAtomContainer(2).setProperty(FilterPipeline.MOL_ID_PROPERTY_NAME, tmpAnyMolIDForAC_2);
        int[] tmpMolIDArray = new FilterPipeline().getArrayOfAssignedMolIDs(tmpAtomContainerSet);
        Assertions.assertEquals(3, tmpMolIDArray.length);
        Assertions.assertArrayEquals(new int[]{tmpAnyMolIDForAC_0, FilterPipeline.MOL_ID_ERROR_VALUE, tmpAnyMolIDForAC_2}, tmpMolIDArray);
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
                    new FilterPipeline().getArrayOfAssignedMolIDs(null);
                }
        );
    }

    /**
     * Tests whether all the atom containers of the atom container set given to the .filter() method of the class
     * FilterPipeline are preserved if no filter is applied.
     */
    @Test
    public void filterMethodTest_noFilterSelected_checkIfAllElementsArePreserved() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        Assertions.assertEquals(3, tmpAtomContainerSet.getAtomContainerCount());
        //
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        IAtomContainerSet tmpFilteredACSet = tmpFilterPipeline.filter(tmpAtomContainerSet);
        Assertions.assertEquals(tmpAtomContainerSet.getAtomContainerCount(), tmpFilteredACSet.getAtomContainerCount());
        for (int i = 0; i < tmpAtomContainerSet.getAtomContainerCount(); i++) {
            Assertions.assertEquals(tmpAtomContainerSet.getAtomContainer(i), tmpFilteredACSet.getAtomContainer(i));
        }
    }

    /**
     * Tests whether all the atom containers of the atom container set returned by the .filter() method of the class
     * FilterPipeline have a valid ID attached. The ID should be attached as property and should be greater or equal to
     * zero.
     */
    @Test
    public void filterMethodTest_checkIfAllACsOfFilteredACSetHaveIDsAttached() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        Assertions.assertEquals(3, tmpAtomContainerSet.getAtomContainerCount());
        //
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        IAtomContainerSet tmpFilteredACSet = tmpFilterPipeline.filter(tmpAtomContainerSet);
        for (IAtomContainer tmpAtomContainer : tmpFilteredACSet.atomContainers()) {
            Assertions.assertInstanceOf(Integer.class, tmpAtomContainer.getProperty(FilterPipeline.MOL_ID_PROPERTY_NAME));
            Assertions.assertTrue((Integer) tmpAtomContainer.getProperty(FilterPipeline.MOL_ID_PROPERTY_NAME) >= 0);
        }
    }

    /**
     * Tests whether the atom containers preserved by the .filter() method of the class FilterPipeline preserved its
     * respective ID.
     */
    @Test
    public void filterMethodTest_noFilterSelected_checkIfAllElementsPreservedTheirCorrectID() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        Assertions.assertEquals(3, tmpAtomContainerSet.getAtomContainerCount());
        //
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        IAtomContainerSet tmpFilteredACSet = tmpFilterPipeline.filter(tmpAtomContainerSet);
        //TODO: should the IDs be assigned to the original ACSet or to a copy of it?
        //tmpFilter.assignIdToAtomContainers(tmpAtomContainerSet);
        for (int i = 0; i < tmpAtomContainerSet.getAtomContainerCount(); i++) {
            //Assertions.assertEquals(i, (Integer) tmpAtomContainerSet.getAtomContainer(i).getProperty(Filter.MOL_ID_PROPERTY_NAME));
            Assertions.assertEquals(tmpAtomContainerSet.getAtomContainer(i).getProperty(FilterPipeline.MOL_ID_PROPERTY_NAME),
                    (Integer) tmpFilteredACSet.getAtomContainer(i).getProperty(FilterPipeline.MOL_ID_PROPERTY_NAME));
        }
    }

    /**
     * Tests whether a NullPointerException is thrown if the atom container set given to the .filter() method of the
     * class FilterPipeline is null.
     */
    @Test   //TODO: has never been "red" since the check is also performed in .assignIdToAtomContainers(); I added the check in .filter() anyways
    public void filterMethodTest_throwNullPointerExceptionIfGivenParamIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    FilterPipeline tmpFilterPipeline = new FilterPipeline();
                    tmpFilterPipeline.filter(null);
                }
        );
    }

    /**
     * Tests the parameter-less, public constructor of the class FilterPipeline whether it initialises multiple class
     * fields.
     * TODO: add further / future class variables
     */
    @Test
    public void publicFilterConstructorTest_instancesHaveListOfSelectedFiltersAndListOfFilterParametersInitializedInConstructor() {
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        Assertions.assertNotNull(tmpFilterPipeline.listOfSelectedFilters);
        Assertions.assertNotNull(tmpFilterPipeline.listOfFilterParameters);
    }

    /**
     * Tests whether the initialised list of selected filters is empty.
     */
    @Test
    public void publicFilterConstructorTest_instancesListOfSelectedFiltersIsEmpty() {
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        Assertions.assertTrue(tmpFilterPipeline.listOfSelectedFilters.isEmpty());
    }

    /**
     * Tests whether the initialised list of filter parameters is empty.
     */
    @Test
    public void publicFilterConstructorTest_instancesListOfFilterParametersIsEmpty() {
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        Assertions.assertTrue(tmpFilterPipeline.listOfFilterParameters.isEmpty());
    }

    /** TODO: remove (?!)
     * Tests whether the protected constructor of the class FilterPipeline returns a copy of the original FilterPipeline
     * instance holding the same instances of listOfSelectedFilters and listOfFilterParameters as the copied original.
     */
    /*@Test
    public void protectedFilterConstructorTest_newFilterContainsSameListOfSelectedFiltersAndListOfFilterParametersAsOriginal() {
        FilterPipeline tmpOriginalFilterPipeline = new FilterPipeline();
        FilterPipeline tmpFilterPipelineCopy = new FilterPipeline(tmpOriginalFilterPipeline);
        Assertions.assertSame(tmpOriginalFilterPipeline.listOfSelectedFilters, tmpFilterPipelineCopy.listOfSelectedFilters);
        Assertions.assertSame(tmpOriginalFilterPipeline.listOfFilterParameters, tmpFilterPipelineCopy.listOfFilterParameters);
    }*/

    /**
     * Tests whether the .getListOfSelectedFilters() method of the class FilterPipeline returns the instances field
     * listOfSelectedFilters.
     */
    @Test
    public void getListOfSelectedFiltersMethodTest_returnsListOfSelectedFilters() {
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        Assertions.assertSame(tmpFilterPipeline.listOfSelectedFilters, tmpFilterPipeline.getListOfSelectedFilters());
    }

    /**
     * Tests whether the class field listOfSelectedFilters stores constants of the enum class Filter.FilterTypes.
     */
    @Test //TODO: can I test the stored data type (without anything being stored in the list yet)?
    public void listOfSelectedFiltersClassVarTest_storesConstantsOfEnumFilterTypes() {
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        tmpFilterPipeline.listOfSelectedFilters.add(FilterPipeline.FilterTypes.NONE);
        Assertions.assertInstanceOf(FilterPipeline.FilterTypes.class, tmpFilterPipeline.listOfSelectedFilters.getFirst());
    }

    /**
     * Tests whether the .getListOfSelectedFilters() method of the class FilterPipeline returns the instances field
     * listOfFilterParameters.
     */
    @Test
    public void getListOfFilterParametersMethodTest_returnsListOfFilterParameters() {
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        Assertions.assertSame(tmpFilterPipeline.listOfFilterParameters, tmpFilterPipeline.getListOfFilterParameters());
    }

    /**
     * Tests whether the class field listOfFilterParameters stores Integer values.
     */
    @Test //TODO: can I test the stored data type (without anything being stored in the list yet)?
    public void listOfFilterParametersClassVarTest_storesIntegers() {
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        tmpFilterPipeline.listOfFilterParameters.add(0);
        Assertions.assertInstanceOf(Integer.class, tmpFilterPipeline.listOfFilterParameters.getFirst());
    }

    /**
     * Tests whether the instance returned by the .withFilter() method of the class FilterPipeline is not null.
     */
    @Test
    public void withFilterMethodTest_returnsNotNull() {
        FilterPipeline.FilterTypes tmpFilterType = FilterPipeline.FilterTypes.NONE;
        int tmpIntegerParameter = 0;
        Assertions.assertNotNull(new FilterPipeline().withFilter(tmpFilterType, tmpIntegerParameter));
    }

    /**
     * Tests whether the return value of the .withFilter() method of class FilterPipeline is an instance of
     * FilterPipeline.
     */
    @Test
    public void withFilterMethodTest_returnsFilterPipelineInstance() {
        FilterPipeline.FilterTypes tmpFilterType = FilterPipeline.FilterTypes.NONE;
        int tmpIntegerParameter = 0;
        Assertions.assertInstanceOf(FilterPipeline.class, new FilterPipeline().withFilter(tmpFilterType, tmpIntegerParameter));
    }

    /**
     * Tests whether the FilterPipeline instance returned by the .withFilter() method of the class FilterPipeline is
     * the same as the original FilterPipeline instance (the instance the method was called from).
     */
    @Test
    public void withFilterMethodTest_returnedFilterPipelineInstanceIsTheSameAsTheOriginal() {
        FilterPipeline tmpOriginalFilterPipeline = new FilterPipeline();
        FilterPipeline tmpReturnedFilterPipeline = tmpOriginalFilterPipeline.withFilter(FilterPipeline.FilterTypes.NONE, 0);
        Assertions.assertSame(tmpOriginalFilterPipeline, tmpReturnedFilterPipeline);
    }

    /**
     * Tests whether the listOfSelectedFilters of the FilterPipeline instance returned by the .withFilter() method of
     * the class FilterPipeline is the same as the one of the original FilterPipeline instance.
     */
    @Test
    public void withFilterMethodTest_returnedFilterPipelineContainsSameListOfSelectedFiltersAsOriginal() {
        FilterPipeline tmpOriginalFilterPipeline = new FilterPipeline();
        FilterPipeline tmpReturnedFilterPipeline = tmpOriginalFilterPipeline.withFilter(FilterPipeline.FilterTypes.NONE, 0);
        Assertions.assertSame(tmpOriginalFilterPipeline.listOfSelectedFilters, tmpReturnedFilterPipeline.listOfSelectedFilters);
    }


    /**
     * Tests whether the listOfSelectedFilters of the FilterPipeline instance returned by the .withFilter() method of
     * the class FilterPipeline was extended by one entry.
     */
    @Test
    public void withFilterMethodTest_checksIfListOfSelectedFiltersWasExtendedByOne() {
        FilterPipeline tmpOriginalFilterPipeline = new FilterPipeline();
        int tmpListInitialSize = tmpOriginalFilterPipeline.listOfSelectedFilters.size();
        FilterPipeline tmpReturnedFilterPipeline = tmpOriginalFilterPipeline.withFilter(FilterPipeline.FilterTypes.NONE, 0);
        Assertions.assertEquals(tmpListInitialSize + 1, tmpReturnedFilterPipeline.listOfSelectedFilters.size());
    }

    /**
     * Tests whether the listOfSelectedFilters of the FilterPipeline instance returned by the .withFilter() method of
     * the class FilterPipeline was extended by the given filter type.
     */
    @Test
    public void withFilterMethodTest_checksIfListOfSelectedFiltersWasExtendedByGivenFilterType() {
        FilterPipeline.FilterTypes tmpFilterType = FilterPipeline.FilterTypes.NONE;
        FilterPipeline tmpReturnedFilterPipeline = new FilterPipeline().withFilter(tmpFilterType, 0);
        Assertions.assertSame(tmpFilterType, tmpReturnedFilterPipeline.listOfSelectedFilters.getLast());
    }

    /**
     * Tests whether the listOfFilterParameters of the FilterPipeline instance returned by the .withFilter() method of
     * the class FilterPipeline is the same as the one of the original FilterPipeline instance.
     */
    @Test
    public void withFilterMethodTest_returnedFilterPipelineContainsSameListOfFilterParametersAsOriginal() {
        FilterPipeline tmpOriginalFilterPipeline = new FilterPipeline();
        FilterPipeline tmpReturnedFilterPipeline = tmpOriginalFilterPipeline.withFilter(FilterPipeline.FilterTypes.NONE, 0);
        Assertions.assertSame(tmpOriginalFilterPipeline.listOfFilterParameters, tmpReturnedFilterPipeline.listOfFilterParameters);
    }

    /**
     * Tests whether the listOfFilterParameters of the FilterPipeline instance returned by the .withFilter() method of
     * the class FilterPipeline was extended by one entry.
     */
    @Test
    public void withFilterMethodTest_checksIfListOfFilterParametersWasExtendedByOne() {
        FilterPipeline tmpOriginalFilterPipeline = new FilterPipeline();
        int tmpListInitialSize = tmpOriginalFilterPipeline.listOfFilterParameters.size();
        FilterPipeline tmpReturnedFilterPipeline = tmpOriginalFilterPipeline.withFilter(FilterPipeline.FilterTypes.NONE, 0);
        Assertions.assertEquals(tmpListInitialSize + 1, tmpReturnedFilterPipeline.listOfFilterParameters.size());
    }

    /**
     * Tests whether the listOfFilterParameters of the FilterPipeline instance returned by the .withFilter() method of
     * the class FilterPipeline was extended by the given integer parameter.
     */
    @Test
    public void withFilterMethodTest_checksIfListOfFilterParametersWasExtendedByGivenIntegerParameter() {
        int tmpIntegerParameter = 0;
        FilterPipeline tmpReturnedFilterPipeline = new FilterPipeline().withFilter(FilterPipeline.FilterTypes.NONE, tmpIntegerParameter);
        Assertions.assertSame(tmpIntegerParameter, tmpReturnedFilterPipeline.listOfFilterParameters.getLast());
        Assertions.assertEquals(tmpIntegerParameter, tmpReturnedFilterPipeline.listOfFilterParameters.getLast());
    }

    /**
     * Tests whether the .withFilter() method of the class FilterPipeline throws a NullPointerException if the given
     * Filter.FilterType is null.
     */
    @Test
    public void withFilterMethodTest_throwsNullPointerExceptionIfGivenFilterTypeIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    new FilterPipeline().withFilter(null, 0);
                }
        );
    }

    /**
     * Tests whether the list of selected filters is extended by two entries if the .withFilter() method of the class
     * FilterPipeline is called twice.
     */
    @Test
    public void withFilterMethodTest_combiningTwoFilters_twoFiltersAreAddedToListOfSelectedFilters() {
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        int tmpInitialListSize = tmpFilterPipeline.getListOfSelectedFilters().size();
        //Assertions.assertEquals(0, tmpFilter.getListOfSelectedFilters().size());
        FilterPipeline.FilterTypes tmpAnyFilterType = FilterPipeline.FilterTypes.NONE;
        int tmpAnyIntegerValue = 0;
        tmpFilterPipeline = tmpFilterPipeline.withFilter(tmpAnyFilterType, tmpAnyIntegerValue).withFilter(tmpAnyFilterType, tmpAnyIntegerValue);
        Assertions.assertEquals(tmpInitialListSize + 2, tmpFilterPipeline.getListOfSelectedFilters().size());
    }

    /**
     * Tests whether the list of selected filters is extended by the two specific filters if the .withFilter() method
     * of the class FilterPipeline is called twice.
     */
    @Test
    public void withFilterMethodTest_combiningTwoFilters_bothSpecificFiltersAreAddedToListOfSelectedFiltersInCorrectOrder() {
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        FilterPipeline.FilterTypes tmpFilterType1 = FilterPipeline.FilterTypes.MAX_ATOM_COUNT_FILTER_CONSIDER_IMPLICIT_HYDROGENS;
        FilterPipeline.FilterTypes tmpFilterType2 = FilterPipeline.FilterTypes.MIN_ATOM_COUNT_FILTER_CONSIDER_IMPLICIT_HYDROGENS;
        int tmpAnyIntegerValue = 0;
        tmpFilterPipeline = tmpFilterPipeline.withFilter(tmpFilterType1, tmpAnyIntegerValue).withFilter(tmpFilterType2, tmpAnyIntegerValue);
        Assertions.assertEquals(tmpFilterType1, tmpFilterPipeline.getListOfSelectedFilters().get(0));
        Assertions.assertEquals(tmpFilterType2, tmpFilterPipeline.getListOfSelectedFilters().get(1));
    }

    /**
     * Tests whether the list of selected filters is extended by five entries if the .withFilter() method of the class
     * FilterPipeline is called five times.
     */
    @Test
    public void withFilterMethodTest_combiningMultipleFilters_5_fiveFiltersAreAddedToListOfSelectedFilters() {
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        int tmpInitialListSize = tmpFilterPipeline.getListOfSelectedFilters().size();
        //Assertions.assertEquals(0, tmpFilter.getListOfSelectedFilters().size());
        FilterPipeline.FilterTypes tmpAnyFilterType = FilterPipeline.FilterTypes.NONE;
        int tmpAnyIntegerValue = 0;
        int tmpAddedFiltersCount = 5;
        for (int i = 0; i < tmpAddedFiltersCount; i++) {
            tmpFilterPipeline = tmpFilterPipeline.withFilter(tmpAnyFilterType, tmpAnyIntegerValue);
        }
        Assertions.assertEquals(tmpInitialListSize + tmpAddedFiltersCount, tmpFilterPipeline.getListOfSelectedFilters().size());
    }

    /**
     * Tests whether the list of selected filters is extended by the five specific filters if the .withFilter() method
     * of the class FilterPipeline is called five times.
     */
    @Test
    public void withFilterMethodTest_combiningMultipleFilters_5_fiveSpecificFiltersAreAddedToListOfSelectedFiltersInCorrectOrder() {
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        FilterPipeline.FilterTypes[] tmpFilterTypesArray = new FilterPipeline.FilterTypes[5];
        tmpFilterTypesArray[0] = FilterPipeline.FilterTypes.MAX_ATOM_COUNT_FILTER_CONSIDER_IMPLICIT_HYDROGENS;
        tmpFilterTypesArray[1] = FilterPipeline.FilterTypes.MIN_ATOM_COUNT_FILTER_CONSIDER_IMPLICIT_HYDROGENS;
        tmpFilterTypesArray[2] = FilterPipeline.FilterTypes.MAX_ATOM_COUNT_FILTER_NOT_CONSIDER_IMPLICIT_HYDROGENS;
        tmpFilterTypesArray[3] = FilterPipeline.FilterTypes.MIN_ATOM_COUNT_FILTER_NOT_CONSIDER_IMPLICIT_HYDROGENS;
        tmpFilterTypesArray[4] = FilterPipeline.FilterTypes.NONE;
        int tmpAnyIntegerValue = 0;
        for (FilterPipeline.FilterTypes tmpFilterType :
                tmpFilterTypesArray) {
            tmpFilterPipeline = tmpFilterPipeline.withFilter(tmpFilterType, tmpAnyIntegerValue);
        }
        for (int i = 0; i < 5; i++) {
            Assertions.assertEquals(tmpFilterTypesArray[i], tmpFilterPipeline.getListOfSelectedFilters().get(i));
        }
    }

    /**
     * Tests whether the list of filter parameters is extended by two entries if the .withFilter() method of the class
     * FilterPipeline is called twice.
     */
    @Test
    public void withFilterMethodTest_combiningTwoFilters_twoIntegerValuesAreAddedToListOfFilterParameters() {
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        int tmpInitialListSize = tmpFilterPipeline.getListOfSelectedFilters().size();
        //Assertions.assertEquals(0, tmpFilter.getListOfFilterParameters().size());
        FilterPipeline.FilterTypes tmpAnyFilterType = FilterPipeline.FilterTypes.NONE;
        int tmpAnyIntegerValue = 0;
        tmpFilterPipeline = tmpFilterPipeline.withFilter(tmpAnyFilterType, tmpAnyIntegerValue).withFilter(tmpAnyFilterType, tmpAnyIntegerValue);
        Assertions.assertEquals(tmpInitialListSize + 2, tmpFilterPipeline.getListOfFilterParameters().size());
    }

    /**
     * Tests whether the list of filter parameters is extended by the two specific integer values if the .withFilter()
     * method of the class FilterPipeline is called twice.
     */
    @Test
    public void withFilterMethodTest_combiningTwoFilters_bothSpecificIntegerValuesAreAddedToListOfFilterParametersInCorrectOrder() {
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        FilterPipeline.FilterTypes tmpAnyFilterType = FilterPipeline.FilterTypes.NONE;
        int tmpIntegerValue1 = 0;
        int tmpIntegerValue2 = 1;
        tmpFilterPipeline = tmpFilterPipeline.withFilter(tmpAnyFilterType, tmpIntegerValue1).withFilter(tmpAnyFilterType, tmpIntegerValue2);
        Assertions.assertEquals(tmpIntegerValue1, tmpFilterPipeline.getListOfFilterParameters().get(0));
        Assertions.assertEquals(tmpIntegerValue2, tmpFilterPipeline.getListOfFilterParameters().get(1));
    }

    /**
     * Tests whether the list of filter parameters is extended by five entries if the .withFilter() method of the class
     * FilterPipeline is called five times.
     */
    @Test
    public void withFilterMethodTest_combiningMultipleFilters_5_fiveIntegerValuesAreAddedToListOfFilterParameters() {
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        int tmpInitialListSize = tmpFilterPipeline.getListOfSelectedFilters().size();
        //Assertions.assertEquals(0, tmpFilter.getListOfFilterParameters().size());
        FilterPipeline.FilterTypes tmpAnyFilterType = FilterPipeline.FilterTypes.NONE;
        int tmpAnyIntegerValue = 0;
        int tmpAddedFiltersCount = 5;
        for (int i = 0; i < tmpAddedFiltersCount; i++) {
            tmpFilterPipeline = tmpFilterPipeline.withFilter(tmpAnyFilterType, tmpAnyIntegerValue);
        }
        Assertions.assertEquals(tmpInitialListSize + tmpAddedFiltersCount, tmpFilterPipeline.getListOfFilterParameters().size());
    }

    /**
     * Tests whether the list of filter parameters is extended by the five specific integer values if the .withFilter()
     * method of the class FilterPipeline is called five times.
     */
    @Test
    public void withFilterMethodTest_combiningMultipleFilters_5_fiveSpecificIntegerValuesAreAddedToListOfSelectedFiltersInCorrectOrder() {
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        FilterPipeline.FilterTypes tmpAnyFilterType = FilterPipeline.FilterTypes.NONE;
        int[] tmpIntegerValues = new int[5];
        for (int i = 0; i < 5; i++) {
            tmpIntegerValues[i] = i;
        }
        for (int tmpIntegerValue :
                tmpIntegerValues) {
            tmpFilterPipeline = tmpFilterPipeline.withFilter(tmpAnyFilterType, tmpIntegerValue);
        }
        for (int i = 0; i < 5; i++) {
            Assertions.assertEquals(tmpIntegerValues[i], tmpFilterPipeline.getListOfFilterParameters().get(i));
        }
    }

    /**
     * Tests whether the return value of the .filter() method of class FilterPipeline is null.
     */
    @Test
    public void filterMethodTest_returnsNotNull() {
        Assertions.assertNotNull(new FilterPipeline().filter(new AtomContainerSet()));
    }

    /**
     * Tests whether the return value of the .filter() method of class FilterPipeline is an instance of
     * IAtomContainerSet.
     */
    @Test
    public void filterMethodTest_returnsInstanceOfIAtomContainerSet() {
        Assertions.assertInstanceOf(IAtomContainerSet.class, new FilterPipeline().filter(new AtomContainerSet()));
    }

    /**
     * Tests whether the atom container set returned by the .filter() method of class FilterPipeline contains equal or
     * less atom containers than the atom container set given to the method.
     */
    @Test
    public void filterMethodTest_returnedAtomContainerSetContainsEqualOrLessAtomContainersThanTheGivenACS() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        IAtomContainerSet tmpFilteredAtomContainerSet = new FilterPipeline().filter(tmpAtomContainerSet);
        Assertions.assertTrue(tmpAtomContainerSet.getAtomContainerCount() >= tmpFilteredAtomContainerSet.getAtomContainerCount());
    }

    /**
     * Tests whether every atom container returned by the .filter() method of class FilterPipeline has an MolID (atom
     * container property "Filter.MolID") assigned.
     */
    @Test
    public void filterMethodTest_everyAtomContainerInTheReturnedSetHasPropertyMolIDSet() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        IAtomContainerSet tmpFilteredAtomContainerSet = new FilterPipeline().filter(tmpAtomContainerSet);
        for (IAtomContainer tmpAtomContainer :
                tmpFilteredAtomContainerSet.atomContainers()) {
            Assertions.assertNotNull(tmpAtomContainer.getProperty(FilterPipeline.MOL_ID_PROPERTY_NAME));
        }
    }

    /**
     * Tests whether the MolIDs (atom container property "Filter.MolID") assigned to every atom container returned by
     * the .filter() method of class FilterPipeline are of data type Integer.
     */
    @Test
    public void filterMethodTest_everyAtomContainerInTheReturnedSetHasPropertyMolIDOfTypeInteger() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        IAtomContainerSet tmpFilteredAtomContainerSet = new FilterPipeline().filter(tmpAtomContainerSet);
        for (IAtomContainer tmpAtomContainer :
                tmpFilteredAtomContainerSet.atomContainers()) {
            Assertions.assertNotNull(tmpAtomContainer.getProperty(FilterPipeline.MOL_ID_PROPERTY_NAME));
        }
    }

    /**
     * Tests whether every atom container of the atom container set given to the .filter() method of class
     * FilterPipeline has an MolID (atom container property "Filter.MolID") assigned afterwards.
     */
    @Test
    public void filterMethodTest_everyAtomContainerInTheGivenACSetHasPropertyMolIDSetAfterwards() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        for (IAtomContainer tmpAtomContainer :
                tmpAtomContainerSet.atomContainers()) {
            Assertions.assertNull(tmpAtomContainer.getProperty(FilterPipeline.MOL_ID_PROPERTY_NAME));
        }
        new FilterPipeline().filter(tmpAtomContainerSet);
        for (IAtomContainer tmpAtomContainer :
                tmpAtomContainerSet.atomContainers()) {
            Assertions.assertNotNull(tmpAtomContainer.getProperty(FilterPipeline.MOL_ID_PROPERTY_NAME));
        }
    }

    /**
     * Tests whether the MolIDs (atom container property "Filter.MolID") assigned to every atom container of the atom
     * container set given to the .filter() method of class FilterPipeline are of data type Integer.
     */
    @Test
    public void filterMethodTest_everyAtomContainerInTheGivenACSetHasPropertyMolIDOfTypeIntegerAfterwards() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        for (IAtomContainer tmpAtomContainer :
                tmpAtomContainerSet.atomContainers()) {
            Assertions.assertNull(tmpAtomContainer.getProperty(FilterPipeline.MOL_ID_PROPERTY_NAME));
        }
        IAtomContainerSet tmpFilteredAtomContainerSet = new FilterPipeline().filter(tmpAtomContainerSet);
        for (IAtomContainer tmpAtomContainer :
                tmpFilteredAtomContainerSet.atomContainers()) {
            Assertions.assertInstanceOf(Integer.class, tmpAtomContainer.getProperty(FilterPipeline.MOL_ID_PROPERTY_NAME));
        }
    }

    /**
     * Tests whether every atom container of the atom container set returned by the .filter() method of class
     * FilterPipeline is present in the set given to it.
     */
    @Test
    public void filterMethodTest_everyMolIDInFilteredAtomContainerSetIsPresentInTheGivenACS() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        IAtomContainerSet tmpFilteredAtomContainerSet = new FilterPipeline().filter(tmpAtomContainerSet);
        boolean tmpMolIdIsPresent;
        for (int tmpFilteredSetMolID :
                new FilterPipeline().getArrayOfAssignedMolIDs(tmpFilteredAtomContainerSet)) {
            tmpMolIdIsPresent = false;
            for (int tmpOriginalSetMolID :
                    new FilterPipeline().getArrayOfAssignedMolIDs(tmpAtomContainerSet)) {
                if (tmpFilteredSetMolID == tmpOriginalSetMolID) {
                    tmpMolIdIsPresent = true;
                    break;
                }
            }
            Assertions.assertTrue(tmpMolIdIsPresent);
        }
    }

    /**
     * Tests whether a NullPointerException is thrown if the atom container set given to the .filter() method of class
     * FilterPipeline is null.
     */
    @Test
    public void filterMethodTest_throwsNullPointerExceptionIfGivenIAtomContainerSetIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    new FilterPipeline().filter(null);
                }
        );
    }

    /**
     * Tests whether applying the filter NONE results in no atom container of the set being filtered.
     * This should be the final form for applying a filter on an atom container set.
     */
    @Test
    public void filterMethodTest_withFilterNONE_sameAtomContainerCountBeforeAsAfter() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        int tmpAnyIntegerValue = 0;
        IAtomContainerSet tmpFilteredAtomContainerSet = new FilterPipeline().withFilter(FilterPipeline.FilterTypes.NONE, tmpAnyIntegerValue).filter(tmpAtomContainerSet);
        Assertions.assertEquals(tmpAtomContainerSet.getAtomContainerCount(), tmpFilteredAtomContainerSet.getAtomContainerCount());
    }

    /**
     * Tests whether applying the filter NONE results in an atom container set containing the same atom container
     * instances in the same order as the original atom container set.
     * This should be the final form for applying a filter on an atom container set.
     */
    @Test
    public void filterMethodTest_withFilterNONE_everyACOfTheOriginalSetIsContainedInTheFilteredSet() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        int tmpAnyIntegerValue = 0;
        IAtomContainerSet tmpFilteredAtomContainerSet = new FilterPipeline().withFilter(FilterPipeline.FilterTypes.NONE, tmpAnyIntegerValue).filter(tmpAtomContainerSet);
        for (int i = 0; i < tmpAtomContainerSet.getAtomContainerCount(); i++) {
            Assertions.assertSame(tmpAtomContainerSet.getAtomContainer(i), tmpFilteredAtomContainerSet.getAtomContainer(i));
        }
    }

    /**
     * Tests whether applying the max atom count filter on a single atom container gives the correct result if implicit
     * hydrogen are considered. Here the given atom container should not be filtered.
     * This should be the final form for applying a filter on an atom container set.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_withMaxAtomCountFilter_14_considerImplHs_singleAcWith12Atoms() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings("c1ccccc1");
        Assertions.assertEquals(1, tmpAtomContainerSet.getAtomContainerCount());
        //
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        int tmpMaxAtomCount = 14;
        boolean tmpConsiderImplicitHydrogen = true;
        tmpFilterPipeline = tmpFilterPipeline.withMaxAtomCountFilter(tmpMaxAtomCount, tmpConsiderImplicitHydrogen);
        IAtomContainerSet tmpFilteredACSet = tmpFilterPipeline.filter(tmpAtomContainerSet);
        Assertions.assertEquals(1, tmpFilteredACSet.getAtomContainerCount());
        Assertions.assertSame(tmpAtomContainerSet.getAtomContainer(0), tmpFilteredACSet.getAtomContainer(0));
        Assertions.assertArrayEquals(new int[]{0}, tmpFilterPipeline.getArrayOfAssignedMolIDs(tmpFilteredACSet));
    }

    /**
     * Tests whether applying the max atom count filter on a single atom container gives the correct result if implicit
     * hydrogen are considered. Here the given atom container should be filtered.
     * This should be the final form for applying a filter on an atom container set.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_withMaxAtomCountFilter_10_considerImplHs_singleAcWith12Atoms() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings("c1ccccc1");
        Assertions.assertEquals(1, tmpAtomContainerSet.getAtomContainerCount());
        //
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        int tmpMaxAtomCount = 10;
        boolean tmpConsiderImplicitHydrogen = true;
        tmpFilterPipeline = tmpFilterPipeline.withMaxAtomCountFilter(tmpMaxAtomCount, tmpConsiderImplicitHydrogen);
        IAtomContainerSet tmpFilteredACSet = tmpFilterPipeline.filter(tmpAtomContainerSet);
        Assertions.assertEquals(0, tmpFilteredACSet.getAtomContainerCount());
    }

    /**
     * Tests whether applying the max atom count filter on multiple atom containers gives the correct result if implicit
     * hydrogen are considered. Here atom container 0 should be filtered, 1 and 2 not.
     * This should be the final form for applying a filter on an atom container set.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_withMaxAtomCountFilter_10_considerImplHs_3ACs_AC1Filtered() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "c1ccccc1", //12
                "CCO",      //9
                "NCC(=O)O"  //10
        );
        Assertions.assertEquals(3, tmpAtomContainerSet.getAtomContainerCount());
        //
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        int tmpMaxAtomCount = 10;
        boolean tmpConsiderImplicitHydrogen = true;
        tmpFilterPipeline = tmpFilterPipeline.withMaxAtomCountFilter(tmpMaxAtomCount, tmpConsiderImplicitHydrogen);
        IAtomContainerSet tmpFilteredACSet = tmpFilterPipeline.filter(tmpAtomContainerSet);
        Assertions.assertEquals(2, tmpFilteredACSet.getAtomContainerCount());
        Assertions.assertArrayEquals(new int[]{1, 2}, tmpFilterPipeline.getArrayOfAssignedMolIDs(tmpFilteredACSet));
    }

    /**
     * Tests whether applying the max atom count filter on a single atom container gives the correct result if implicit
     * hydrogen are not considered. Here the given atom container should not be filtered.
     * This should be the final form for applying a filter on an atom container set.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_withMaxAtomCountFilter_6_notConsiderImplHs_singleAcWith5Atoms() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings("NCC(=O)O");
        Assertions.assertEquals(1, tmpAtomContainerSet.getAtomContainerCount());
        //
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        int tmpMaxAtomCount = 6;
        boolean tmpConsiderImplicitHydrogen = false;
        tmpFilterPipeline = tmpFilterPipeline.withMaxAtomCountFilter(tmpMaxAtomCount, tmpConsiderImplicitHydrogen);
        IAtomContainerSet tmpFilteredACSet = tmpFilterPipeline.filter(tmpAtomContainerSet);
        Assertions.assertEquals(1, tmpFilteredACSet.getAtomContainerCount());
        Assertions.assertSame(tmpAtomContainerSet.getAtomContainer(0), tmpFilteredACSet.getAtomContainer(0));
        Assertions.assertArrayEquals(new int[]{0}, tmpFilterPipeline.getArrayOfAssignedMolIDs(tmpFilteredACSet));
    }

    /**
     * Tests whether applying the max atom count filter on a single atom container gives the correct result if implicit
     * hydrogen are not considered. Here the given atom container should be filtered.
     * This should be the final form for applying a filter on an atom container set.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_withMaxAtomCountFilter_4_notConsiderImplHs_singleAcWith5Atoms() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings("NCC(=O)O");
        Assertions.assertEquals(1, tmpAtomContainerSet.getAtomContainerCount());
        //
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        int tmpMaxAtomCount = 4;
        boolean tmpConsiderImplicitHydrogen = false;
        tmpFilterPipeline = tmpFilterPipeline.withMaxAtomCountFilter(tmpMaxAtomCount, tmpConsiderImplicitHydrogen);
        IAtomContainerSet tmpFilteredACSet = tmpFilterPipeline.filter(tmpAtomContainerSet);
        Assertions.assertEquals(0, tmpFilteredACSet.getAtomContainerCount());
    }

    /**
     * Tests whether applying the max atom count filter on multiple atom containers gives the correct result if implicit
     * hydrogen are not considered. Here atom container 1 should be filtered, 0 and 2 not.
     * This should be the final form for applying a filter on an atom container set.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_withMaxAtomCountFilter_5_notConsiderImplHs_3ACs_AC2Filtered() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "NCC(=O)O", //5
                "c1ccccc1", //6
                "CCO"       //3
        );
        Assertions.assertEquals(3, tmpAtomContainerSet.getAtomContainerCount());
        //
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        int tmpMaxAtomCount = 5;
        boolean tmpConsiderImplicitHydrogen = false;
        tmpFilterPipeline = tmpFilterPipeline.withMaxAtomCountFilter(tmpMaxAtomCount, tmpConsiderImplicitHydrogen);
        IAtomContainerSet tmpFilteredACSet = tmpFilterPipeline.filter(tmpAtomContainerSet);
        Assertions.assertEquals(2, tmpFilteredACSet.getAtomContainerCount());
        Assertions.assertArrayEquals(new int[]{0, 2}, tmpFilterPipeline.getArrayOfAssignedMolIDs(tmpFilteredACSet));
    }

    /**
     * Tests whether applying the min atom count filter on a single atom container gives the correct result if implicit
     * hydrogen are considered. Here the given atom container should not be filtered.
     * This should be the final form for applying a filter on an atom container set.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_withMinAtomCountFilter_10_considerImplHs_singleAcWith12Atoms() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings("c1ccccc1");
        Assertions.assertEquals(1, tmpAtomContainerSet.getAtomContainerCount());
        //
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        int tmpMinAtomCount = 10;
        boolean tmpConsiderImplicitHydrogen = true;
        tmpFilterPipeline = tmpFilterPipeline.withMinAtomCountFilter(tmpMinAtomCount, tmpConsiderImplicitHydrogen);
        IAtomContainerSet tmpFilteredACSet = tmpFilterPipeline.filter(tmpAtomContainerSet);
        Assertions.assertEquals(1, tmpFilteredACSet.getAtomContainerCount());
        Assertions.assertSame(tmpAtomContainerSet.getAtomContainer(0), tmpFilteredACSet.getAtomContainer(0));
        Assertions.assertArrayEquals(new int[]{0}, tmpFilterPipeline.getArrayOfAssignedMolIDs(tmpFilteredACSet));
    }

    /**
     * Tests whether applying the min atom count filter on a single atom container gives the correct result if implicit
     * hydrogen are considered. Here the given atom container should be filtered.
     * This should be the final form for applying a filter on an atom container set.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_withMinAtomCountFilter_14_considerImplHs_singleAcWith12Atoms() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings("c1ccccc1");
        Assertions.assertEquals(1, tmpAtomContainerSet.getAtomContainerCount());
        //
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        int tmpMinAtomCount = 14;
        boolean tmpConsiderImplicitHydrogen = true;
        tmpFilterPipeline = tmpFilterPipeline.withMinAtomCountFilter(tmpMinAtomCount, tmpConsiderImplicitHydrogen);
        IAtomContainerSet tmpFilteredACSet = tmpFilterPipeline.filter(tmpAtomContainerSet);
        Assertions.assertEquals(0, tmpFilteredACSet.getAtomContainerCount());
    }

    /**
     * Tests whether applying the min atom count filter on multiple atom containers gives the correct result if implicit
     * hydrogen are considered. Here atom container 1 should be filtered, 0 and 2 not.
     * This should be the final form for applying a filter on an atom container set.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_withMinAtomCountFilter_10_considerImplHs_3ACs_AC2Filtered() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "c1ccccc1", //12
                "CCO",      //9
                "NCC(=O)O"  //10
        );
        Assertions.assertEquals(3, tmpAtomContainerSet.getAtomContainerCount());
        //
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        int tmpMinAtomCount = 10;
        boolean tmpConsiderImplicitHydrogen = true;
        tmpFilterPipeline = tmpFilterPipeline.withMinAtomCountFilter(tmpMinAtomCount, tmpConsiderImplicitHydrogen);
        IAtomContainerSet tmpFilteredACSet = tmpFilterPipeline.filter(tmpAtomContainerSet);
        Assertions.assertEquals(2, tmpFilteredACSet.getAtomContainerCount());
        Assertions.assertArrayEquals(new int[]{0, 2}, tmpFilterPipeline.getArrayOfAssignedMolIDs(tmpFilteredACSet));
    }

    /**
     * Tests whether applying the min atom count filter on a single atom container gives the correct result if implicit
     * hydrogen are not considered. Here the given atom container should not be filtered.
     * This should be the final form for applying a filter on an atom container set.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_withMinAtomCountFilter_4_notConsiderImplHs_singleAcWith5Atoms() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings("NCC(=O)O");
        Assertions.assertEquals(1, tmpAtomContainerSet.getAtomContainerCount());
        //
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        int tmpMinAtomCount = 4;
        boolean tmpConsiderImplicitHydrogen = false;
        tmpFilterPipeline = tmpFilterPipeline.withMinAtomCountFilter(tmpMinAtomCount, tmpConsiderImplicitHydrogen);
        IAtomContainerSet tmpFilteredACSet = tmpFilterPipeline.filter(tmpAtomContainerSet);
        Assertions.assertEquals(1, tmpFilteredACSet.getAtomContainerCount());
        Assertions.assertSame(tmpAtomContainerSet.getAtomContainer(0), tmpFilteredACSet.getAtomContainer(0));
        Assertions.assertArrayEquals(new int[]{0}, tmpFilterPipeline.getArrayOfAssignedMolIDs(tmpFilteredACSet));
    }

    /**
     * Tests whether applying the min atom count filter on a single atom container gives the correct result if implicit
     * hydrogen are not considered. Here the given atom container should be filtered.
     * This should be the final form for applying a filter on an atom container set.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_withMinAtomCountFilter_6_notConsiderImplHs_singleAcWith5Atoms() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings("NCC(=O)O");
        Assertions.assertEquals(1, tmpAtomContainerSet.getAtomContainerCount());
        //
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        int tmpMinAtomCount = 6;
        boolean tmpConsiderImplicitHydrogen = false;
        tmpFilterPipeline = tmpFilterPipeline.withMinAtomCountFilter(tmpMinAtomCount, tmpConsiderImplicitHydrogen);
        IAtomContainerSet tmpFilteredACSet = tmpFilterPipeline.filter(tmpAtomContainerSet);
        Assertions.assertEquals(0, tmpFilteredACSet.getAtomContainerCount());
    }

    /**
     * Tests whether applying the min atom count filter on multiple atom containers gives the correct result if implicit
     * hydrogen are not considered. Here atom container 2 should be filtered, 0 and 1 not.
     * This should be the final form for applying a filter on an atom container set.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_withMinAtomCountFilter_5_notConsiderImplHs_3ACs_AC3Filtered() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "NCC(=O)O", //5
                "c1ccccc1", //6
                "CCO"       //3
        );
        Assertions.assertEquals(3, tmpAtomContainerSet.getAtomContainerCount());
        //
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        int tmpMinAtomCount = 5;
        boolean tmpConsiderImplicitHydrogen = false;
        tmpFilterPipeline = tmpFilterPipeline.withMinAtomCountFilter(tmpMinAtomCount, tmpConsiderImplicitHydrogen);
        IAtomContainerSet tmpFilteredACSet = tmpFilterPipeline.filter(tmpAtomContainerSet);
        Assertions.assertEquals(2, tmpFilteredACSet.getAtomContainerCount());
        Assertions.assertArrayEquals(new int[]{0, 1}, tmpFilterPipeline.getArrayOfAssignedMolIDs(tmpFilteredACSet));
    }

    //TODO: test for combining two filters
    @Test
    public void filterMethodTest_combiningTwoFilters_withMaxAtomCountFilterConsiderImplHs_12_withMinAtomCountFilterConsiderImplHs_9() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "NCC(=O)O", //10 (5)
                "c1ccccc1", //12 (6)
                "C1CCCC1",  //15 (5) - filtered
                "CCO",      // 9 (3)
                "CC(=O)O",  // 8 (4) - filtered
                "C=CC=C"    //10 (4)
        );
        //
        FilterPipeline tmpFilterPipeline = new FilterPipeline().withMaxAtomCountFilter(12, true).withMinAtomCountFilter(9, true);
        IAtomContainerSet tmpFilteredACSet = tmpFilterPipeline.filter(tmpAtomContainerSet);
        Assertions.assertEquals(4, tmpFilteredACSet.getAtomContainerCount());
        Assertions.assertArrayEquals(new int[]{0, 1, 3, 5}, tmpFilterPipeline.getArrayOfAssignedMolIDs(tmpFilteredACSet));
    }

    @Test
    public void filterMethodTest_combiningThreeFilters_withMaxAtomCountFilterConsImplHs_12_withMinAtomCountFilterConsImplHs_9_withMaxAtomCountFilterNotConsImplHs_5()
            throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "NCC(=O)O", //10 (5)
                "c1ccccc1", //12 (6) - filtered
                "C1CCCC1",  //15 (5) - filtered
                "CCO",      // 9 (3)
                "CC(=O)O",  // 8 (4) - filtered
                "C=CC=C"    //10 (4)
        );
        //
        FilterPipeline tmpFilterPipeline = new FilterPipeline().withMaxAtomCountFilter(12, true)
                .withMinAtomCountFilter(9, true)
                .withMaxAtomCountFilter(5, false);
        IAtomContainerSet tmpFilteredACSet = tmpFilterPipeline.filter(tmpAtomContainerSet);
        Assertions.assertEquals(3, tmpFilteredACSet.getAtomContainerCount());
        Assertions.assertArrayEquals(new int[]{0, 3, 5}, tmpFilterPipeline.getArrayOfAssignedMolIDs(tmpFilteredACSet));
    }

    @Test
    public void filterMethodTest_combiningFourFilters_withMaxAtomCountFilterConsImplHs_12_withMinAtomCountFilterConsImplHs_9_withMaxAtomCountFilterNotConsImplHs_5_withMinAtomCountFilterNotConsImplHs_5()
            throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "NCC(=O)O", //10 (5)
                "c1ccccc1", //12 (6) - filtered
                "C1CCCC1",  //15 (5) - filtered
                "CCO",      // 9 (3) - filtered
                "CC(=O)O",  // 8 (4) - filtered
                "C=CC=C"    //10 (4)
        );
        //
        FilterPipeline tmpFilterPipeline = new FilterPipeline().withMaxAtomCountFilter(12, true)
                .withMinAtomCountFilter(9, true)
                .withMaxAtomCountFilter(5, false)
                .withMinAtomCountFilter(4, false);
        IAtomContainerSet tmpFilteredACSet = tmpFilterPipeline.filter(tmpAtomContainerSet);
        Assertions.assertEquals(2, tmpFilteredACSet.getAtomContainerCount());
        Assertions.assertArrayEquals(new int[]{0, 5}, tmpFilterPipeline.getArrayOfAssignedMolIDs(tmpFilteredACSet));
    }

    @Test
    public void filterMethodTest_combiningFourFilters_resultIsIndependentOfTheFiltersOrder() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "C1CCCC1",  //15 (5) - filtered
                "NCC(=O)O", //10 (5)
                "CC(=O)O",  // 8 (4) - filtered
                "c1ccccc1", //12 (6) - filtered
                "C=CC=C",   //10 (4)
                "CCO"       // 9 (3) - filtered
        );
        //
        FilterPipeline tmpFilterPipeline1 = new FilterPipeline().withMaxAtomCountFilter(12, true)
                .withMinAtomCountFilter(9, true)
                .withMaxAtomCountFilter(5, false)
                .withMinAtomCountFilter(4, false);
        FilterPipeline tmpFilterPipeline2 = new FilterPipeline().withMaxAtomCountFilter(5, false)
                .withMinAtomCountFilter(9, true)
                .withMinAtomCountFilter(4, false)
                .withMaxAtomCountFilter(12, true);
        IAtomContainerSet tmpFilteredACSet1 = tmpFilterPipeline1.filter(tmpAtomContainerSet);
        IAtomContainerSet tmpFilteredACSet2 = tmpFilterPipeline2.filter(tmpAtomContainerSet);
        //test for correct and identical length
        Assertions.assertEquals(2, tmpFilteredACSet1.getAtomContainerCount());
        Assertions.assertEquals(tmpFilteredACSet1.getAtomContainerCount(), tmpFilteredACSet2.getAtomContainerCount());
        //test for correct and identical result
        Assertions.assertArrayEquals(new int[]{1, 4}, tmpFilterPipeline1.getArrayOfAssignedMolIDs(tmpFilteredACSet1));
        Assertions.assertArrayEquals(tmpFilterPipeline1.getArrayOfAssignedMolIDs(tmpFilteredACSet1), tmpFilterPipeline1.getArrayOfAssignedMolIDs(tmpFilteredACSet2));
    }

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

    /**
     * Tests whether the value returned by the .getAssignedFilterID() method of class FilterPipeline is of type Integer.
     */
    @Test
    public void getAssignedFilterID_returnsIntegerValue() {
        IAtomContainer tmpAtomContainer = new AtomContainer();
        int tmpAnyFilterID = 0;
        tmpAtomContainer.setProperty(FilterPipeline.FILTER_ID_PROPERTY_NAME, tmpAnyFilterID);
        Assertions.assertInstanceOf(Integer.class, new FilterPipeline().getAssignedFilterID(tmpAtomContainer));
    }

    /**
     * Tests whether the integer value returned by the .getAssignedFilterID() method of class FilterPipeline has the
     * value of the given atom containers FilterID (atom container property "FilterPipeline.FilterID").
     */
    @Test
    public void getAssignedFilterID_returnedValueEqualsFilterIDOfGivenAC_0() {
        IAtomContainer tmpAtomContainer = new AtomContainer();
        int tmpAnyFilterID = 0;
        tmpAtomContainer.setProperty(FilterPipeline.FILTER_ID_PROPERTY_NAME, tmpAnyFilterID);
        Assertions.assertEquals(tmpAnyFilterID, new FilterPipeline().getAssignedFilterID(tmpAtomContainer));
    }

    /**
     * Tests whether the integer value returned by the .getAssignedFilterID() method of class FilterPipeline has the
     * value of the given atom containers FilterID (atom container property "FilterPipeline.FilterID").
     */
    @Test
    public void getAssignedFilterID_returnedValueEqualsFilterIDOfGivenAC_1() {
        IAtomContainer tmpAtomContainer = new AtomContainer();
        int tmpAnyFilterID = 1;
        tmpAtomContainer.setProperty(FilterPipeline.FILTER_ID_PROPERTY_NAME, tmpAnyFilterID);
        Assertions.assertEquals(tmpAnyFilterID, new FilterPipeline().getAssignedFilterID(tmpAtomContainer));
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
                    new FilterPipeline().getAssignedFilterID(null);
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
                    new FilterPipeline().getAssignedFilterID(new AtomContainer());
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
                    tmpAtomContainer.setProperty(FilterPipeline.FILTER_ID_PROPERTY_NAME, new Object());
                    new FilterPipeline().getAssignedFilterID(tmpAtomContainer);
                }
        );
    }

    /**
     * Tests whether the return value of the .getArrayOfAssignedFilterIDs() method of class FilterPipeline is not null.
     */
    @Test
    public void getArrayOfAssignedFilterIDsTest_returnsNotNull() {
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        Assertions.assertNotNull(tmpFilterPipeline.getArrayOfAssignedFilterIDs(new AtomContainerSet()));
    }

    /**
     * Tests whether the return value of the .getArrayOfAssignedFilterIDs() method of class FilterPipeline is an integer
     * array.
     */
    @Test
    public void getArrayOfAssignedFilterIDsTest_returnsArrayOfIntegers() {
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        Assertions.assertInstanceOf(int[].class, tmpFilterPipeline.getArrayOfAssignedFilterIDs(new AtomContainerSet()));
    }

    /**
     * Tests whether the .getArrayOfAssignedFilterIDs() method of the class FilterPipeline returns an array of length
     * one if an atom container set with a single atom container is given.
     */
    @Test
    public void getArrayOfAssignedFilterIDsTest_returnsArrayOfLengthOneIfGiven1AC() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(1);
        int tmpAnyFilterID = 0;
        tmpAtomContainerSet.getAtomContainer(0).setProperty(FilterPipeline.FILTER_ID_PROPERTY_NAME, tmpAnyFilterID);
        //
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        Assertions.assertEquals(1, tmpFilterPipeline.getArrayOfAssignedFilterIDs(tmpAtomContainerSet).length);
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
        tmpAtomContainerSet.getAtomContainer(0).setProperty(FilterPipeline.FILTER_ID_PROPERTY_NAME, tmpFilterID);
        //
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        int[] tmpFilterIDArray = tmpFilterPipeline.getArrayOfAssignedFilterIDs(tmpAtomContainerSet);
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
        tmpAtomContainerSet.getAtomContainer(0).setProperty(FilterPipeline.FILTER_ID_PROPERTY_NAME, tmpFilterID);
        //
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        int[] tmpFilterIDArray = tmpFilterPipeline.getArrayOfAssignedFilterIDs(tmpAtomContainerSet);
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
            tmpAtomContainer.setProperty(FilterPipeline.FILTER_ID_PROPERTY_NAME, 0);
        }
        //
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        Assertions.assertEquals(3, tmpFilterPipeline.getArrayOfAssignedFilterIDs(tmpAtomContainerSet).length);
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
            tmpAtomContainer.setProperty(FilterPipeline.FILTER_ID_PROPERTY_NAME, tmpFilterID);
        }
        //
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        int[] tmpFilterIDArray = tmpFilterPipeline.getArrayOfAssignedFilterIDs(tmpAtomContainerSet);
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
            tmpAtomContainerSet.getAtomContainer(i).setProperty(FilterPipeline.FILTER_ID_PROPERTY_NAME, tmpFilterIDArray[i]);
        }
        //
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        Assertions.assertArrayEquals(tmpFilterIDArray, tmpFilterPipeline.getArrayOfAssignedFilterIDs(tmpAtomContainerSet));
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
                    new FilterPipeline().getArrayOfAssignedFilterIDs(null);
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
                    new FilterPipeline().getArrayOfAssignedFilterIDs(tmpAtomContainerSet);
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
                    tmpAtomContainerSet.getAtomContainer(0).setProperty(FilterPipeline.FILTER_ID_PROPERTY_NAME, new Object());
                    new FilterPipeline().getArrayOfAssignedFilterIDs(tmpAtomContainerSet);
                }
        );
    }

    //TODO: any further test methods for .getArrayOfAssignedFilterIDs()?

    //TODO: following are tests of the FilterID (a tool for tracking / tracing the filter process)
    /**
     * Tests whether every atom container of the atom container set given to the .filter() method of class
     * FilterPipeline has an atom container property FilterPipeline.FilterID of type integer after the filtering
     * process.
     */
    @Test
    public void filterMethodTest_everyAtomContainerInTheOriginalSetHasIntegerPropertyFilterIDAfterFiltering() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        new FilterPipeline().filter(tmpAtomContainerSet);
        for (IAtomContainer tmpAtomContainer :
                tmpAtomContainerSet.atomContainers()) {
            Assertions.assertNotNull(tmpAtomContainer.getProperty(FilterPipeline.FILTER_ID_PROPERTY_NAME));
        }
    }

    /**
     * Tests whether every atom container of the atom container set returned by the .filter() method of class
     * FilterPipeline has an atom container property FilterPipeline.FilterID that equals the value of
     * Filter.NOT_FILTERED_VALUE after a filtering process with no filter being selected.
     */
    @Test
    public void filterMethodTest_everyAtomContainerInTheFilteredACSetHasIntegerPropertyFilterIDSetToNotFilteredValue_noFilter() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        IAtomContainerSet tmpFilteredACSet = new FilterPipeline().filter(tmpAtomContainerSet);
        for (IAtomContainer tmpAtomContainer :
                tmpFilteredACSet.atomContainers()) {
            Assertions.assertEquals(FilterPipeline.NOT_FILTERED_VALUE, (Integer) tmpAtomContainer.getProperty(FilterPipeline.FILTER_ID_PROPERTY_NAME));
        }
    }

    /**
     * Tests whether every atom container of the atom container set returned by the .filter() method of class
     * FilterPipeline has an atom container property FilterPipeline.FilterID that equals the value of
     * Filter.NOT_FILTERED_VALUE after a filtering process with filter NONE.
     */
    @Test
    public void filterMethodTest_everyAtomContainerInTheFilteredACSetHasIntegerPropertyFilterIDSetToNotFilteredValue_withFilterNONE() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        int tmpAnyIntegerValue = 0;
        FilterPipeline tmpFilterPipeline = new FilterPipeline().withFilter(FilterPipeline.FilterTypes.NONE, tmpAnyIntegerValue);
        IAtomContainerSet tmpFilteredACSet = tmpFilterPipeline.filter(tmpAtomContainerSet);
        for (IAtomContainer tmpAtomContainer :
                tmpFilteredACSet.atomContainers()) {
            Assertions.assertEquals(FilterPipeline.NOT_FILTERED_VALUE, (Integer) tmpAtomContainer.getProperty(FilterPipeline.FILTER_ID_PROPERTY_NAME));
        }
    }

    /**
     * Tests whether every atom container of the atom container set returned by the .filter() method of class
     * FilterPipeline has an atom container property FilterPipeline.FilterID that equals the value of
     * Filter.NOT_FILTERED_VALUE after a filtering process with max atom count filter.
     */
    @Test   //TODO: wip
    public void filterMethodTest_everyAtomContainerInTheFilteredACSetHasIntegerPropertyFilterIDSetToNotFilteredValue_withMaxAtomCountFilter() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        int tmpAnyIntegerValue = 10;
        FilterPipeline tmpFilterPipeline = new FilterPipeline().withMaxAtomCountFilter(tmpAnyIntegerValue, true);
        IAtomContainerSet tmpFilteredACSet = tmpFilterPipeline.filter(tmpAtomContainerSet);
        for (IAtomContainer tmpAtomContainer :
                tmpFilteredACSet.atomContainers()) {
            Assertions.assertEquals(FilterPipeline.NOT_FILTERED_VALUE, (Integer) tmpAtomContainer.getProperty(FilterPipeline.FILTER_ID_PROPERTY_NAME));
        }
    }

    /**
     * Tests whether every atom container of the atom container set given to the .filter() method of class
     * FilterPipeline that has been filtered during the filtering process has an atom container property
     * FilterPipeline.FilterID with a value greater than or equal to zero afterwards (filtering with max atom count
     * filter).
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test //TODO: wip
    public void filterMethodTest_filterIDsOfFilteredAtomContainersAreGreaterThanOrEqualToZero_withMaxAtomCountFilter() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "c1ccccc1", //12 - filtered
                "CCO",      //9
                "NCC(=O)O"  //10 - filtered
        );
        FilterPipeline tmpFilterPipeline = new FilterPipeline().withMaxAtomCountFilter(9, true);
        tmpFilterPipeline.filter(tmpAtomContainerSet);
        System.out.println(tmpAtomContainerSet.getAtomContainer(0).getProperty(FilterPipeline.FILTER_ID_PROPERTY_NAME).toString());
        System.out.println(tmpAtomContainerSet.getAtomContainer(2).getProperty(FilterPipeline.FILTER_ID_PROPERTY_NAME).toString());
        Assertions.assertTrue((Integer) tmpAtomContainerSet.getAtomContainer(0).getProperty(FilterPipeline.FILTER_ID_PROPERTY_NAME) >= 0);
        Assertions.assertTrue((Integer) tmpAtomContainerSet.getAtomContainer(2).getProperty(FilterPipeline.FILTER_ID_PROPERTY_NAME) >= 0);
    }

    /**
     * Tests whether every atom container in the atom container set given to the .filter() method of class
     * FilterPipeline that has been filtered during the filtering process has an atom container property
     * FilterPipeline.FilterID with a value greater than or equal to zero afterwards (filtering with multiple
     * filters; test 1).
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_filterIDsEqualIndexOfFilterTheAtomContainersGotFilteredBy_multipleFilters_test1() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "NCC(=O)O", //10 (5) - filtered by 2
                "c1ccccc1", //12 (6) - filtered by 1
                "C1CCCC1",  //15 (5) - filtered by 1
                "CCO",      // 9 (3)
                "CC(=O)O",  // 8 (4) - filtered by 0
                "C=CC=C"    //10 (4)
        );
        int tmpAnyValue = -1;
        int[] tmpExpectedValuesArray = new int[]{2, 1, 1, tmpAnyValue, 0, tmpAnyValue};
        FilterPipeline tmpFilterPipeline = new FilterPipeline().withMinAtomCountFilter(9, true)
                .withMaxAtomCountFilter(10, true)
                .withMaxAtomCountFilter(4, false);
        tmpFilterPipeline.filter(tmpAtomContainerSet);
        for (int i = 0; i < tmpExpectedValuesArray.length; i++) {
            if (i == 3 || i == 5) {
                continue;
            }
            Assertions.assertEquals(tmpExpectedValuesArray[i], (Integer) tmpAtomContainerSet.getAtomContainer(i).getProperty(FilterPipeline.FILTER_ID_PROPERTY_NAME));
        }
    }

    /**
     * Tests whether every atom container in the atom container set given to the .filter() method of class
     * FilterPipeline that has been filtered during the filtering process has an atom container property
     * FilterPipeline.FilterID with a value greater than or equal to zero afterwards (filtering with multiple
     * filters; test 2).
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_filterIDsEqualIndexOfFilterTheAtomContainersGotFilteredBy_multipleFilters_test2() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "NCC(=O)O", //10 (5)
                "c1ccccc1", //12 (6)
                "C1CCCC1",  //15 (5) - filtered by 0
                "CCO",      // 9 (3) - filtered by 2
                "CC(=O)O",  // 8 (4) - filtered by 1
                "C=CC=C"    //10 (4)
        );
        int tmpAnyValue = -1;
        int[] tmpExpectedValuesArray = new int[]{tmpAnyValue, tmpAnyValue, 0, 2, 1, tmpAnyValue};
        FilterPipeline tmpFilterPipeline = new FilterPipeline().withMaxAtomCountFilter(12, true)
                .withMinAtomCountFilter(9, true)
                .withMinAtomCountFilter(5, false);
        tmpFilterPipeline.filter(tmpAtomContainerSet);
        for (int i = 0; i < tmpExpectedValuesArray.length; i++) {
            if (i == 0 || i == 1 || i == 5) {
                continue;
            }
            Assertions.assertEquals(tmpExpectedValuesArray[i], (Integer) tmpAtomContainerSet.getAtomContainer(i).getProperty(FilterPipeline.FILTER_ID_PROPERTY_NAME));
        }
    }

    /**
     * Tests whether every atom container in the atom container set returned by the .filter() method of class
     * FilterPipeline has an atom container property FilterPipeline.FilterID that equals the value of
     * Filter.NOT_FILTERED_VALUE after a filtering process with multiple filters.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_everyAtomContainerInTheFilteredACSetHasIntegerPropertyFilterIDSetToNotFilteredValue_multipleFilters() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "CC(=O)O",  // 8 (4) - filtered
                "c1ccccc1", //12 (6)
                "C1CCCC1",  //15 (5) - filtered
                "NCC(=O)O", //10 (5)
                "C=CC=C",   //10 (4)  - filtered
                "CCO"       // 9 (3) - filtered
        );
        boolean[] tmpGotFilteredArray = new boolean[]{true, false, true, false, true, true};
        FilterPipeline tmpFilterPipeline = new FilterPipeline().withMaxAtomCountFilter(12, true)
                .withMinAtomCountFilter(9, true)
                .withMinAtomCountFilter(5, false);
        IAtomContainerSet tmpFilteredACSet = tmpFilterPipeline.filter(tmpAtomContainerSet);
        for (int i = 0; i < tmpGotFilteredArray.length; i++) {
            if (tmpGotFilteredArray[i]) {
                continue;
            }
            Assertions.assertEquals(FilterPipeline.NOT_FILTERED_VALUE, (Integer) tmpAtomContainerSet.getAtomContainer(i).getProperty(FilterPipeline.FILTER_ID_PROPERTY_NAME));
        }
        //ensure that the instances in the filtered set are the same as those in the original set
        Assertions.assertSame(tmpFilteredACSet.getAtomContainer(0), tmpAtomContainerSet.getAtomContainer(1));
        Assertions.assertSame(tmpFilteredACSet.getAtomContainer(1), tmpAtomContainerSet.getAtomContainer(3));
    }
    //TODO: further tests and implementation of FilterID?

}
