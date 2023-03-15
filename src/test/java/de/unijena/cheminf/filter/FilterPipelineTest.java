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
import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

/**
 * Test class for the core methods and functions of class FilterPipeline.
 */
public class FilterPipelineTest {

    /*
    open TODOs:
     */

    /**
     * Tests whether all the atom containers of the atom container set given to the .filter() method of the class
     * FilterPipeline are preserved if no filter is applied.
     */
    @Test
    public void filterMethodTest_noFilterSelected_checksIfAllElementsArePreserved() {
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
     * FilterPipeline have a valid MolID attached. The ID should be attached as property and should be greater or equal
     * to zero.
     */
    @Test
    public void filterMethodTest_checksIfAllACsOfFilteredACSetHaveMolIDsAttached() {
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
     * respective MolID.
     */
    @Test
    public void filterMethodTest_noFilterSelected_checkIfAllElementsPreservedTheirCorrectMolID() {
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
    @Test
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
     * Tests the parameter-less, public constructor of the class FilterPipeline whether it initialises the
     * listOfSelectedFilters.
     * TODO: add further / future class variables
     */
    @Test
    public void publicFilterConstructorTest_instancesHaveListOfSelectedFiltersInitializedInConstructor() {
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        Assertions.assertNotNull(tmpFilterPipeline.listOfSelectedFilters);
        //Assertions.assertNotNull(tmpFilterPipeline.listOfSelectedFilters);
        //Assertions.assertNotNull(tmpFilterPipeline.listOfFilterParameters);
    }

    /**
     * Tests whether the initialised list of selected filters is empty.
     */
    @Test
    public void publicFilterConstructorTest_instancesListOfSelectedFiltersIsEmpty() {
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        Assertions.assertTrue(tmpFilterPipeline.listOfSelectedFilters.isEmpty());
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
    public void getListOfSelectedFiltersMethodTest_returnsClassFieldListOfSelectedFilters() {
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        Assertions.assertSame(tmpFilterPipeline.listOfSelectedFilters, tmpFilterPipeline.getListOfSelectedFilters());
    }

    /**
     * Tests whether the class field listOfSelectedFilters stores Filter instances.
     */
    @Test //TODO: can I test the stored data type (without anything being stored in the list yet)?
    public void listOfSelectedFiltersClassVarTest_storesFilterInstances() {
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        tmpFilterPipeline.listOfSelectedFilters.add(new MaxAtomCountFilter(0, true));
        Assertions.assertInstanceOf(Filter.class, tmpFilterPipeline.listOfSelectedFilters.getFirst());
    }

    /**
     * Tests whether the return value of the .filter() method of class FilterPipeline is not null.
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
     * FilterPipeline gets a MolID (atom container property "Filter.MolID") assigned during the filtering process.
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
     * Tests whether applying a filter that does not filter results in no atom container of the set being filtered.
     */
    @Test
    public void filterMethodTest_withAnEmptyFilter_sameAtomContainerCountBeforeAsAfter() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        IAtomContainerSet tmpFilteredAtomContainerSet = new FilterPipeline().withFilter(TestUtils.getAllTrueOrFalseFilter(false)).filter(tmpAtomContainerSet);
        Assertions.assertEquals(tmpAtomContainerSet.getAtomContainerCount(), tmpFilteredAtomContainerSet.getAtomContainerCount());
    }

    /**
     * Tests whether applying a filter that does not filter results in an atom container set containing the same atom
     * container instances in the same order as the original atom container set.
     * This should be the final form for applying a filter on an atom container set.
     */
    @Test
    public void filterMethodTest_withAnEmptyFilter_everyACOfTheOriginalSetIsContainedInTheFilteredSet() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        IAtomContainerSet tmpFilteredAtomContainerSet = new FilterPipeline().withFilter(TestUtils.getAllTrueOrFalseFilter(false)).filter(tmpAtomContainerSet);
        for (int i = 0; i < tmpAtomContainerSet.getAtomContainerCount(); i++) {
            Assertions.assertSame(tmpAtomContainerSet.getAtomContainer(i), tmpFilteredAtomContainerSet.getAtomContainer(i));
        }
    }

    /**
     * Tests whether applying the max atom count filter on a single atom container gives the correct result if implicit
     * hydrogen atoms are considered. Here the given atom container should not be filtered.
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
     * hydrogen atoms are considered. Here the given atom container should be filtered.
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
     * hydrogen atoms are considered. Here atom container 0 should be filtered, 1 and 2 not.
     * This should be the final form for applying a filter on an atom container set.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_withMaxAtomCountFilter_10_considerImplHs_3ACs_AC1Filtered() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "c1ccccc1", //12 - filtered
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
     * hydrogen atoms are not considered. Here the given atom container should not be filtered.
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
     * hydrogen atoms are not considered. Here the given atom container should be filtered.
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
     * hydrogen atoms are not considered. Here atom container 1 should be filtered, 0 and 2 not.
     * This should be the final form for applying a filter on an atom container set.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_withMaxAtomCountFilter_5_notConsiderImplHs_3ACs_AC2Filtered() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "NCC(=O)O", //5
                "c1ccccc1", //6 - filtered
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
     * hydrogen atoms are considered. Here the given atom container should not be filtered.
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
     * hydrogen atoms are considered. Here the given atom container should be filtered.
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
     * hydrogen atoms are considered. Here atom container 1 should be filtered, 0 and 2 not.
     * This should be the final form for applying a filter on an atom container set.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_withMinAtomCountFilter_10_considerImplHs_3ACs_AC2Filtered() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "c1ccccc1", //12
                "CCO",      //9 - filtered
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
     * hydrogen atoms are not considered. Here the given atom container should not be filtered.
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
     * hydrogen atoms are not considered. Here the given atom container should be filtered.
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
     * hydrogen atoms are not considered. Here atom container 2 should be filtered, 0 and 1 not.
     * This should be the final form for applying a filter on an atom container set.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_withMinAtomCountFilter_5_notConsiderImplHs_3ACs_AC3Filtered() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "NCC(=O)O", //5
                "c1ccccc1", //6
                "CCO"       //3 - filtered
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

    /**
     * Tests whether applying two filters on multiple atom containers leads to the correct results. Here atom container
     * 2 and 4 should be filtered; 0, 1, 3 and 5 not.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
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

    /**
     * Tests whether applying three filters on multiple atom containers leads to the correct results. Here atom
     * container 2, 3 and 4 should be filtered; 0, 1 and 5 not.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
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

    /**
     * Tests whether applying four filters on multiple atom containers leads to the correct results. Here atom container
     * 1, 2, 3 and 4 should be filtered; 0 and 5 not.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
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

    /**
     * Tests whether the results of applying four filters on multiple atom containers are independent of the order of
     * the applied filters.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
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
     * Filter.NOT_FILTERED_VALUE after a filtering process with a filter that does not filter.
     */
    @Test
    public void filterMethodTest_everyAtomContainerInTheFilteredACSetHasIntegerPropertyFilterIDSetToNotFilteredValue_withANotFilteringFilter() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        int tmpAnyIntegerValue = 0;
        FilterPipeline tmpFilterPipeline = new FilterPipeline().withFilter(TestUtils.getAllTrueOrFalseFilter(false));
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
    @Test
    public void filterMethodTest_everyAtomContainerInTheFilteredACSetHasIntegerPropertyFilterIDSetToNotFilteredValue_withMaxAtomCountFilter() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        int tmpAnyIntegerValue = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        FilterPipeline tmpFilterPipeline = new FilterPipeline().withMaxAtomCountFilter(tmpAnyIntegerValue, tmpConsiderImplicitHydrogens);
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
    @Test
    public void filterMethodTest_filterIDsOfFilteredAtomContainersAreGreaterThanOrEqualToZero_withMaxAtomCountFilter() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "c1ccccc1", //12 - filtered
                "CCO",      //9
                "NCC(=O)O"  //10 - filtered
        );
        FilterPipeline tmpFilterPipeline = new FilterPipeline().withMaxAtomCountFilter(9, true);
        tmpFilterPipeline.filter(tmpAtomContainerSet);
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

}
