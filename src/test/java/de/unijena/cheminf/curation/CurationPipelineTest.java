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

import de.unijena.cheminf.curation.filter.BaseFilter;
import de.unijena.cheminf.curation.filter.filters.MaxAtomCountFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

import java.util.Objects;

/**
 * Test class for the core methods and functions of class FilterPipeline.
 */
public class CurationPipelineTest {

    /*
    open TODOs:
     */

    /**
     * TODO: modified
     * Tests whether all the atom containers of the atom container set given to the .curate() method of the class
     * CurationPipeline are preserved if no filter is applied.
     */
    @Test
    public void filterMethodTest_noFilterSelected_checksIfAllElementsArePreserved() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        Assertions.assertEquals(3, tmpAtomContainerSet.getAtomContainerCount());
        //
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        IAtomContainerSet tmpFilteredACSet = tmpCurationPipeline.process(tmpAtomContainerSet, true, true);
        Assertions.assertEquals(tmpAtomContainerSet.getAtomContainerCount(), tmpFilteredACSet.getAtomContainerCount());
        for (int i = 0; i < tmpAtomContainerSet.getAtomContainerCount(); i++) {
            Assertions.assertSame(tmpAtomContainerSet.getAtomContainer(i).getProperty(CurationPipeline.MOL_ID_PROPERTY_NAME),
                    tmpFilteredACSet.getAtomContainer(i).getProperty(CurationPipeline.MOL_ID_PROPERTY_NAME));
        }
    }

    /**
     * TODO: modified
     * Tests whether all the atom containers of the atom container set returned by the .filter() method of the class
     * FilterPipeline have a valid MolID attached. The ID should be attached as property and should be greater or equal
     * to zero.
     */
    @Test
    public void filterMethodTest_checksIfAllACsOfFilteredACSetHaveMolIDsAttached() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        Assertions.assertEquals(3, tmpAtomContainerSet.getAtomContainerCount());
        //
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        IAtomContainerSet tmpFilteredACSet = tmpCurationPipeline.process(tmpAtomContainerSet, true, true);
        for (IAtomContainer tmpAtomContainer : tmpFilteredACSet.atomContainers()) {
            Assertions.assertInstanceOf(String.class, tmpAtomContainer.getProperty(CurationPipeline.MOL_ID_PROPERTY_NAME));
            Assertions.assertTrue(Integer.parseInt(tmpAtomContainer.getProperty(CurationPipeline.MOL_ID_PROPERTY_NAME)) >= 0);
        }
    }

    /**
     * TODO: modified
     * Tests whether the atom containers preserved by the .filter() method of the class FilterPipeline preserved its
     * respective MolID.
     */
    @Test
    public void filterMethodTest_noFilterSelected_checkIfAllElementsPreservedTheirCorrectMolID() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        Assertions.assertEquals(3, tmpAtomContainerSet.getAtomContainerCount());
        //
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        IAtomContainerSet tmpFilteredACSet = tmpCurationPipeline.process(tmpAtomContainerSet, true, true);
        //TODO: should the IDs be assigned to the original ACSet or to a copy of it?
        //tmpFilter.assignIdToAtomContainers(tmpAtomContainerSet);
        for (int i = 0; i < tmpAtomContainerSet.getAtomContainerCount(); i++) {
            //Assertions.assertEquals(i, (Integer) tmpAtomContainerSet.getAtomContainer(i).getProperty(Filter.MOL_ID_PROPERTY_NAME));
            Assertions.assertSame(tmpAtomContainerSet.getAtomContainer(i).getProperty(CurationPipeline.MOL_ID_PROPERTY_NAME),
                    tmpFilteredACSet.getAtomContainer(i).getProperty(CurationPipeline.MOL_ID_PROPERTY_NAME));
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
                    CurationPipeline tmpCurationPipeline = new CurationPipeline();
                    tmpCurationPipeline.process(null, false, true);
                }
        );
    }

    /**
     * Tests the parameter-less, public constructor of the class CurationPipeline whether it initializes
     * listOfPipelineSteps.
     */
    @Test
    public void publicFilterConstructorTest_noParameter_listOfSelectedFiltersInitialized() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        Assertions.assertNotNull(tmpCurationPipeline.listOfPipelineSteps);
    }

    /**
     * Tests the parameter-less, public constructor of the class CurationPipeline whether the initialized
     * listOfPipelineSteps is empty.
     */
    @Test
    public void publicFilterConstructorTest_noParameter_instancesListOfSelectedFiltersIsEmpty() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        Assertions.assertTrue(tmpCurationPipeline.listOfPipelineSteps.isEmpty());
    }

    /**
     * Tests the parameter-less, public constructor of the class FilterPipeline whether it sets optionalIDPropertyName
     * to null.
     */
    /*
    @Test
    public void publicFilterConstructorTest_noParameter_optionalIDPropertyNameIsNull() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        Assertions.assertNull(tmpCurationPipeline.optionalIDPropertyName);
    }
    */

    /**
     * Tests public constructor of the class FilterPipeline with String parameter whether it initializes
     * listOfSelectedFilters.
     */
    @Test
    public void publicFilterConstructorTest_withStringParameter_listOfSelectedFiltersInitialized() {
        String tmpPropertyNameString = "SomeString";
        CurationPipeline tmpCurationPipeline = new CurationPipeline(tmpPropertyNameString);
        Assertions.assertNotNull(tmpCurationPipeline.listOfPipelineSteps);
    }

    /**
     * Tests whether the initialised list of selected filters is empty.
     */
    @Test
    public void publicFilterConstructorTest_withStringParameter_instancesListOfSelectedFiltersIsEmpty() {
        String tmpPropertyNameString = "SomeString";
        CurationPipeline tmpCurationPipeline = new CurationPipeline(tmpPropertyNameString);
        Assertions.assertTrue(tmpCurationPipeline.listOfPipelineSteps.isEmpty());
    }

    /**
     * Tests the parameter-less, public constructor of the class FilterPipeline whether it sets the field
     * optionalIDPropertyName to the given String.
     */
    /*
    @Test
    public void publicFilterConstructorTest_withStringParameter_optionalIDPropertyNameIsSameAsGivenString() {
        String tmpPropertyNameString = "SomeString";
        CurationPipeline tmpCurationPipeline = new CurationPipeline(tmpPropertyNameString);
        Assertions.assertSame(tmpPropertyNameString, tmpCurationPipeline.optionalIDPropertyName);
    }
    */

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
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        Assertions.assertSame(tmpCurationPipeline.listOfPipelineSteps, tmpCurationPipeline.getListOfPipelineSteps());
    }

    /**
     * Tests whether the class field listOfSelectedFilters stores Filter instances.
     */
    @Test //TODO: can I test the stored data type (without anything being stored in the list yet)?
    public void listOfSelectedFiltersClassVarTest_storesFilterInstances() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        tmpCurationPipeline.listOfPipelineSteps.add(new MaxAtomCountFilter(0, true));
        Assertions.assertInstanceOf(BaseFilter.class, tmpCurationPipeline.listOfPipelineSteps.getFirst());
    }

    /**
     * Tests whether the .getOptionalIDPropertyName() method of the class FilterPipeline returns the instances field
     * optionalIDPropertyName.
     */
    /*
    @Test
    public void getOptionalIDPropertyNameMethodTest_returnsClassFieldOptionalIDPropertyName() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        Assertions.assertSame(tmpCurationPipeline.optionalIDPropertyName, tmpCurationPipeline.getOptionalIDPropertyName());
    }
    */

    /**
     * Tests whether the return value of the .filter() method of class FilterPipeline is not null.
     */
    @Test
    public void filterMethodTest_returnsNotNull() {
        Assertions.assertNotNull(new CurationPipeline().process(new AtomContainerSet(), true, true));
    }

    /**
     * Tests whether the return value of the .filter() method of class FilterPipeline is an instance of
     * IAtomContainerSet.
     */
    @Test
    public void filterMethodTest_returnsInstanceOfIAtomContainerSet() {
        Assertions.assertInstanceOf(IAtomContainerSet.class, new CurationPipeline().process(new AtomContainerSet(), true, true));
    }

    /**
     * Tests whether the atom container set returned by the .filter() method of class FilterPipeline contains equal or
     * less atom containers than the atom container set given to the method.
     */
    @Test
    public void filterMethodTest_returnedAtomContainerSetContainsEqualOrLessAtomContainersThanTheGivenACS() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        IAtomContainerSet tmpFilteredAtomContainerSet = new CurationPipeline().process(tmpAtomContainerSet, true, true);
        Assertions.assertTrue(tmpAtomContainerSet.getAtomContainerCount() >= tmpFilteredAtomContainerSet.getAtomContainerCount());
    }

    /**
     * Tests whether every atom container returned by the .filter() method of class FilterPipeline has an MolID (atom
     * container property "Filter.MolID") assigned.
     */
    @Test
    public void filterMethodTest_everyAtomContainerInTheReturnedSetHasPropertyMolIDSet() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        IAtomContainerSet tmpFilteredAtomContainerSet = new CurationPipeline().process(tmpAtomContainerSet, true, true);
        for (IAtomContainer tmpAtomContainer :
                tmpFilteredAtomContainerSet.atomContainers()) {
            Assertions.assertNotNull(tmpAtomContainer.getProperty(CurationPipeline.MOL_ID_PROPERTY_NAME));
        }
    }

    /**
     * Tests whether the MolIDs (atom container property "Filter.MolID") assigned to every atom container returned by
     * the .filter() method of class FilterPipeline are of data type Integer.
     */
    @Test
    public void filterMethodTest_everyAtomContainerInTheReturnedSetHasPropertyMolIDOfTypeInteger() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        IAtomContainerSet tmpFilteredAtomContainerSet = new CurationPipeline().process(tmpAtomContainerSet, true, true);
        for (IAtomContainer tmpAtomContainer :
                tmpFilteredAtomContainerSet.atomContainers()) {
            Assertions.assertNotNull(tmpAtomContainer.getProperty(CurationPipeline.MOL_ID_PROPERTY_NAME));
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
            Assertions.assertNull(tmpAtomContainer.getProperty(CurationPipeline.MOL_ID_PROPERTY_NAME));
        }
        new CurationPipeline().process(tmpAtomContainerSet, true, true);
        for (IAtomContainer tmpAtomContainer :
                tmpAtomContainerSet.atomContainers()) {
            Assertions.assertNotNull(tmpAtomContainer.getProperty(CurationPipeline.MOL_ID_PROPERTY_NAME));
        }
    }

    /**
     * Tests whether the MolIDs (atom container property "Filter.MolID") assigned to every atom container of the atom
     * container set given to the .filter() method of class FilterPipeline are of data type Integer.
     */
    @Test
    @Disabled
    public void filterMethodTest_everyAtomContainerInTheGivenACSetHasPropertyMolIDOfTypeIntegerAfterwards() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        for (IAtomContainer tmpAtomContainer :
                tmpAtomContainerSet.atomContainers()) {
            Assertions.assertNull(tmpAtomContainer.getProperty(CurationPipeline.MOL_ID_PROPERTY_NAME));
        }
        IAtomContainerSet tmpFilteredAtomContainerSet = new CurationPipeline().process(tmpAtomContainerSet, true, true);
        for (IAtomContainer tmpAtomContainer :
                tmpFilteredAtomContainerSet.atomContainers()) {
            Assertions.assertInstanceOf(Integer.class, tmpAtomContainer.getProperty(CurationPipeline.MOL_ID_PROPERTY_NAME));
        }
    }

    /**
     * Tests whether every atom container of the atom container set returned by the .filter() method of class
     * FilterPipeline is present in the set given to it.
     */
    @Test
    public void filterMethodTest_everyMolIDInFilteredAtomContainerSetIsPresentInTheGivenACS() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        IAtomContainerSet tmpFilteredAtomContainerSet = new CurationPipeline().process(tmpAtomContainerSet, true, true);
        boolean tmpMolIdIsPresent;
        for (String tmpFilteredSetMolID :
                ProcessingStepUtils.getArrayOfAssignedMolIDs(tmpFilteredAtomContainerSet)) {
            tmpMolIdIsPresent = false;
            for (String tmpOriginalSetMolID :
                    ProcessingStepUtils.getArrayOfAssignedMolIDs(tmpAtomContainerSet)) {
                if (Objects.equals(tmpFilteredSetMolID, tmpOriginalSetMolID)) {
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
                    new CurationPipeline().process(null, false, true);
                }
        );
    }

    /**
     * Tests whether applying a filter that does not filter results in no atom container of the set being filtered.
     */
    @Test
    public void filterMethodTest_withAnEmptyFilter_sameAtomContainerCountBeforeAsAfter() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        IAtomContainerSet tmpFilteredAtomContainerSet = new CurationPipeline()
                .addProcessingStep(TestUtils.getAllTrueOrFalseFilter(false))
                .process(tmpAtomContainerSet, true, true);
        Assertions.assertEquals(tmpAtomContainerSet.getAtomContainerCount(), tmpFilteredAtomContainerSet.getAtomContainerCount());
    }

    /**
     * TODO: modified
     * Tests whether applying a filter that does not filter results in an atom container set containing the atom
     * containers with the same MolID in the same order as the original atom container set.
     * This should be the final form for applying a filter on an atom container set.
     */
    @Test
    public void filterMethodTest_withAnEmptyFilter_everyACOfTheOriginalSetIsContainedInTheFilteredSet() {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.getSetOfEmptyAtomContainers(3);
        IAtomContainerSet tmpFilteredAtomContainerSet = new CurationPipeline()
                .addProcessingStep(TestUtils.getAllTrueOrFalseFilter(false))
                .process(tmpAtomContainerSet, true, true);
        for (int i = 0; i < tmpAtomContainerSet.getAtomContainerCount(); i++) {
            Assertions.assertSame(tmpAtomContainerSet.getAtomContainer(i).getProperty(CurationPipeline.MOL_ID_PROPERTY_NAME),
                    tmpFilteredAtomContainerSet.getAtomContainer(i).getProperty(CurationPipeline.MOL_ID_PROPERTY_NAME));
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
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        int tmpMaxAtomCount = 14;
        boolean tmpConsiderImplicitHydrogen = true;
        tmpCurationPipeline = tmpCurationPipeline.withMaxAtomCountFilter(tmpMaxAtomCount, tmpConsiderImplicitHydrogen);
        IAtomContainerSet tmpFilteredACSet = tmpCurationPipeline.process(tmpAtomContainerSet, true, true);
        Assertions.assertEquals(1, tmpFilteredACSet.getAtomContainerCount());
        Assertions.assertArrayEquals(new String[]{"0"}, ProcessingStepUtils.getArrayOfAssignedMolIDs(tmpFilteredACSet));
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
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        int tmpMaxAtomCount = 10;
        boolean tmpConsiderImplicitHydrogen = true;
        tmpCurationPipeline = tmpCurationPipeline.withMaxAtomCountFilter(tmpMaxAtomCount, tmpConsiderImplicitHydrogen);
        IAtomContainerSet tmpFilteredACSet = tmpCurationPipeline.process(tmpAtomContainerSet, true, true);
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
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        int tmpMaxAtomCount = 10;
        boolean tmpConsiderImplicitHydrogen = true;
        tmpCurationPipeline = tmpCurationPipeline.withMaxAtomCountFilter(tmpMaxAtomCount, tmpConsiderImplicitHydrogen);
        IAtomContainerSet tmpFilteredACSet = tmpCurationPipeline.process(tmpAtomContainerSet, true, true);
        Assertions.assertEquals(2, tmpFilteredACSet.getAtomContainerCount());
        Assertions.assertArrayEquals(new String[]{"1", "2"}, ProcessingStepUtils.getArrayOfAssignedMolIDs(tmpFilteredACSet));
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
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        int tmpMaxAtomCount = 6;
        boolean tmpConsiderImplicitHydrogen = false;
        tmpCurationPipeline = tmpCurationPipeline.withMaxAtomCountFilter(tmpMaxAtomCount, tmpConsiderImplicitHydrogen);
        IAtomContainerSet tmpFilteredACSet = tmpCurationPipeline.process(tmpAtomContainerSet, true, true);
        Assertions.assertEquals(1, tmpFilteredACSet.getAtomContainerCount());
        Assertions.assertArrayEquals(new String[]{"0"}, ProcessingStepUtils.getArrayOfAssignedMolIDs(tmpFilteredACSet));
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
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        int tmpMaxAtomCount = 4;
        boolean tmpConsiderImplicitHydrogen = false;
        tmpCurationPipeline = tmpCurationPipeline.withMaxAtomCountFilter(tmpMaxAtomCount, tmpConsiderImplicitHydrogen);
        IAtomContainerSet tmpFilteredACSet = tmpCurationPipeline.process(tmpAtomContainerSet, true, true);
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
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        int tmpMaxAtomCount = 5;
        boolean tmpConsiderImplicitHydrogen = false;
        tmpCurationPipeline = tmpCurationPipeline.withMaxAtomCountFilter(tmpMaxAtomCount, tmpConsiderImplicitHydrogen);
        IAtomContainerSet tmpFilteredACSet = tmpCurationPipeline.process(tmpAtomContainerSet, true, true);
        Assertions.assertEquals(2, tmpFilteredACSet.getAtomContainerCount());
        Assertions.assertArrayEquals(new String[]{"0", "2"}, ProcessingStepUtils.getArrayOfAssignedMolIDs(tmpFilteredACSet));
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
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        int tmpMinAtomCount = 10;
        boolean tmpConsiderImplicitHydrogen = true;
        tmpCurationPipeline = tmpCurationPipeline.withMinAtomCountFilter(tmpMinAtomCount, tmpConsiderImplicitHydrogen);
        IAtomContainerSet tmpFilteredACSet = tmpCurationPipeline.process(tmpAtomContainerSet, true, true);
        Assertions.assertEquals(1, tmpFilteredACSet.getAtomContainerCount());
        Assertions.assertArrayEquals(new String[]{"0"}, ProcessingStepUtils.getArrayOfAssignedMolIDs(tmpFilteredACSet));
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
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        int tmpMinAtomCount = 14;
        boolean tmpConsiderImplicitHydrogen = true;
        tmpCurationPipeline = tmpCurationPipeline.withMinAtomCountFilter(tmpMinAtomCount, tmpConsiderImplicitHydrogen);
        IAtomContainerSet tmpFilteredACSet = tmpCurationPipeline.process(tmpAtomContainerSet, true, true);
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
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        int tmpMinAtomCount = 10;
        boolean tmpConsiderImplicitHydrogen = true;
        tmpCurationPipeline = tmpCurationPipeline.withMinAtomCountFilter(tmpMinAtomCount, tmpConsiderImplicitHydrogen);
        IAtomContainerSet tmpFilteredACSet = tmpCurationPipeline.process(tmpAtomContainerSet, true, true);
        Assertions.assertEquals(2, tmpFilteredACSet.getAtomContainerCount());
        Assertions.assertArrayEquals(new String[]{"0", "2"}, ProcessingStepUtils.getArrayOfAssignedMolIDs(tmpFilteredACSet));
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
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        int tmpMinAtomCount = 4;
        boolean tmpConsiderImplicitHydrogen = false;
        tmpCurationPipeline = tmpCurationPipeline.withMinAtomCountFilter(tmpMinAtomCount, tmpConsiderImplicitHydrogen);
        IAtomContainerSet tmpFilteredACSet = tmpCurationPipeline.process(tmpAtomContainerSet, true, true);
        Assertions.assertEquals(1, tmpFilteredACSet.getAtomContainerCount());
        Assertions.assertArrayEquals(new String[]{"0"}, ProcessingStepUtils.getArrayOfAssignedMolIDs(tmpFilteredACSet));
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
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        int tmpMinAtomCount = 6;
        boolean tmpConsiderImplicitHydrogen = false;
        tmpCurationPipeline = tmpCurationPipeline.withMinAtomCountFilter(tmpMinAtomCount, tmpConsiderImplicitHydrogen);
        IAtomContainerSet tmpFilteredACSet = tmpCurationPipeline.process(tmpAtomContainerSet, true, true);
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
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        int tmpMinAtomCount = 5;
        boolean tmpConsiderImplicitHydrogen = false;
        tmpCurationPipeline = tmpCurationPipeline.withMinAtomCountFilter(tmpMinAtomCount, tmpConsiderImplicitHydrogen);
        IAtomContainerSet tmpFilteredACSet = tmpCurationPipeline.process(tmpAtomContainerSet, true, true);
        Assertions.assertEquals(2, tmpFilteredACSet.getAtomContainerCount());
        Assertions.assertArrayEquals(new String[]{"0", "1"}, ProcessingStepUtils.getArrayOfAssignedMolIDs(tmpFilteredACSet));
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
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withMaxAtomCountFilter(12, true).withMinAtomCountFilter(9, true);
        IAtomContainerSet tmpFilteredACSet = tmpCurationPipeline.process(tmpAtomContainerSet, true, true);
        Assertions.assertEquals(4, tmpFilteredACSet.getAtomContainerCount());
        Assertions.assertArrayEquals(new String[]{"0", "1", "3", "5"}, ProcessingStepUtils.getArrayOfAssignedMolIDs(tmpFilteredACSet));
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
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withMaxAtomCountFilter(12, true)
                .withMinAtomCountFilter(9, true)
                .withMaxAtomCountFilter(5, false);
        IAtomContainerSet tmpFilteredACSet = tmpCurationPipeline.process(tmpAtomContainerSet, true, true);
        Assertions.assertEquals(3, tmpFilteredACSet.getAtomContainerCount());
        Assertions.assertArrayEquals(new String[]{"0", "3", "5"}, ProcessingStepUtils.getArrayOfAssignedMolIDs(tmpFilteredACSet));
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
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withMaxAtomCountFilter(12, true)
                .withMinAtomCountFilter(9, true)
                .withMaxAtomCountFilter(5, false)
                .withMinAtomCountFilter(4, false);
        IAtomContainerSet tmpFilteredACSet = tmpCurationPipeline.process(tmpAtomContainerSet, true, true);
        Assertions.assertEquals(2, tmpFilteredACSet.getAtomContainerCount());
        Assertions.assertArrayEquals(new String[]{"0", "5"}, ProcessingStepUtils.getArrayOfAssignedMolIDs(tmpFilteredACSet));
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
        CurationPipeline tmpCurationPipeline1 = new CurationPipeline().withMaxAtomCountFilter(12, true)
                .withMinAtomCountFilter(9, true)
                .withMaxAtomCountFilter(5, false)
                .withMinAtomCountFilter(4, false);
        CurationPipeline tmpCurationPipeline2 = new CurationPipeline().withMaxAtomCountFilter(5, false)
                .withMinAtomCountFilter(9, true)
                .withMinAtomCountFilter(4, false)
                .withMaxAtomCountFilter(12, true);
        IAtomContainerSet tmpFilteredACSet1 = tmpCurationPipeline1.process(tmpAtomContainerSet, true, true);
        IAtomContainerSet tmpFilteredACSet2 = tmpCurationPipeline2.process(tmpAtomContainerSet, true, true);
        //test for correct and identical length
        Assertions.assertEquals(2, tmpFilteredACSet1.getAtomContainerCount());
        Assertions.assertEquals(tmpFilteredACSet1.getAtomContainerCount(), tmpFilteredACSet2.getAtomContainerCount());
        //test for correct and identical result
        Assertions.assertArrayEquals(new String[]{"1", "4"}, ProcessingStepUtils.getArrayOfAssignedMolIDs(tmpFilteredACSet1));
        Assertions.assertArrayEquals(ProcessingStepUtils.getArrayOfAssignedMolIDs(tmpFilteredACSet1), ProcessingStepUtils.getArrayOfAssignedMolIDs(tmpFilteredACSet2));
    }

}
