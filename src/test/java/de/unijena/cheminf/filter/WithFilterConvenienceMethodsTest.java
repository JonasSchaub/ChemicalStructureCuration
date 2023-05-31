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

import de.unijena.cheminf.MassComputationFlavours;
import de.unijena.cheminf.TestUtils;
import de.unijena.cheminf.filter.filters.HasAllValidAtomicNumbersFilter;
import de.unijena.cheminf.filter.filters.HasInvalidAtomicNumbersFilter;
import de.unijena.cheminf.filter.filters.MaxBondCountFilter;
import de.unijena.cheminf.filter.filters.MaxBondsOfSpecificBondOrderFilter;
import de.unijena.cheminf.filter.filters.MaxHeavyAtomCountFilter;
import de.unijena.cheminf.filter.filters.MaxMolecularMassFilter;
import de.unijena.cheminf.filter.filters.MinBondCountFilter;
import de.unijena.cheminf.filter.filters.MinBondsOfSpecificBondOrderFilter;
import de.unijena.cheminf.filter.filters.MinHeavyAtomCountFilter;
import de.unijena.cheminf.filter.filters.MinMolecularMassFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IBond;

/**
 * Test class of FilterPipeline methods to add specific filters to the pipeline.
 */
public class WithFilterConvenienceMethodsTest {

    /*
    TODO: add tests for every new filter class
    //
    TODO: place tests of filter method else where / in a separate class?
     */

    /**
     * Tests whether the value returned by the .withMaxBondCountFilter() method of the class FilterPipeline is a
     * FilterPipeline instance.
     */
    @Test
    public void withMaxBondCountFilterMethodTest_returnsFilterPipelineInstance() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        int tmpIntegerParameter = 10;
        boolean tmpBooleanParameter = true;
        Assertions.assertInstanceOf(tmpCurationPipeline.getClass(), tmpCurationPipeline.withMaxBondCountFilter(tmpIntegerParameter, tmpBooleanParameter));
    }

    /**
     * Tests whether the instance returned by the .withMaxBondCountFilter() method of the class FilterPipeline is the
     * FilterPipeline instance the method was called of.
     */
    @Test
    public void withMaxBondCountFilterMethodTest_returnsFilterPipelineInstanceItWasCalledOf() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        int tmpIntegerParameter = 10;
        boolean tmpBooleanParameter = true;
        Assertions.assertSame(tmpCurationPipeline, tmpCurationPipeline.withMaxBondCountFilter(tmpIntegerParameter, tmpBooleanParameter));
    }

    /**
     * Tests whether the listOfSelectedFilters of the FilterPipeline instance returned by the .withMaxBondCountFilter()
     * method of the class FilterPipeline was extended by an instance of MaxBondCountFilter.
     */
    @Test
    public void withMaxBondCountFilterMethodTest_checksWhetherListOfSelectedFiltersWasExtendedByInstanceOfMaxBondCountFilter() {
        int tmpIntegerParameter = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withMaxBondCountFilter(tmpIntegerParameter, tmpConsiderImplicitHydrogens);
        Assertions.assertInstanceOf(MaxBondCountFilter.class, tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast());
    }

    /**
     * Tests whether the MaxBondCountFilter added to the listOfSelectedFilters by the .withMaxBondCountFilter() method
     * of the class FilterPipeline contains the given max bond count threshold value.
     */
    @Test
    public void withMaxBondCountFilterMethodTest_checksWhetherAddedMaxBondCountFilterHasGivenThresholdSet_twoTests() {
        int tmpThresholdValue = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withMaxBondCountFilter(tmpThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertEquals(tmpThresholdValue, ((MaxBondCountFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).getMaxBondCount());
        tmpThresholdValue = 20;
        tmpCurationPipeline = new CurationPipeline().withMaxBondCountFilter(tmpThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertEquals(tmpThresholdValue, ((MaxBondCountFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).getMaxBondCount());
    }

    /**
     * Tests whether the MaxBondCountFilter added to the listOfSelectedFilters by the .withMaxBondCountFilter() method
     * of the class FilterPipeline has the given boolean value set.
     */
    @Test
    public void withMaxBondCountFilterMethodTest_checksWhetherAddedMaxBondCountFilterHasGivenBooleanConsiderImplHsSet_twoTests() {
        int tmpThresholdValue = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withMaxBondCountFilter(tmpThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertTrue(((MaxBondCountFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).isConsiderImplicitHydrogens());
        tmpConsiderImplicitHydrogens = false;
        tmpCurationPipeline = new CurationPipeline().withMaxBondCountFilter(tmpThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertFalse(((MaxBondCountFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).isConsiderImplicitHydrogens());
    }

    /**
     * Tests whether the .withMaxBondCountFilter() method of the class FilterPipeline throws an IllegalArgumentException
     * if the given integer parameter is of a negative value.
     */
    @Test
    public void withMaxBondCountFilterMethodTest_throwsIllegalArgumentExceptionIfGivenMaxBondCountIsNegative() {    //TODO: do so?
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    int tmpNegativeMaxBondCount = -1;
                    new CurationPipeline().withMaxBondCountFilter(tmpNegativeMaxBondCount, true);
                }
        );
    }

    /**
     * Tests whether the .filter() method of class FilterPipeline behaves as expected when filtering with a
     * MaxBondCountFilter considering bonds to implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_withMaxBondCountFilter_considerImplHs_multipleMolecules() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "CC(=O)O",  // 7 (3)
                "c1ccccc1", //12 (6) - filtered
                "C1CCCC1",  //15 (5) - filtered
                "NCC(=O)O", // 9 (4)
                "C=CC=C",   // 9 (3)
                "CCO"       // 8 (2)
        );
        boolean[] tmpIsFilteredArray = new boolean[]{false, true, true, false, false, false};
        //
        int tmpMaxBondCount = 9;
        boolean tmpConsiderImplicitHydrogens = true;
        IFilter tmpFilter = new MaxBondCountFilter(tmpMaxBondCount, tmpConsiderImplicitHydrogens);
        //
        TestUtils.filterPipeline_getsFilteredMethodTest_testsBehaviorOfMethodWithSpecificFilter(
                tmpFilter, tmpAtomContainerSet, tmpIsFilteredArray
        );
    }

    /**
     * Tests whether the .filter() method of class FilterPipeline behaves as expected when filtering with a
     * MaxBondCountFilter not considering bonds to implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_withMaxBondCountFilter_notConsiderImplHs_multipleMolecules() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "c1ccccc1", //12 (6) - filtered
                "CC(=O)O",  // 7 (3)
                "NCC(=O)O", // 9 (4) - filtered
                "C1CCCC1",  //15 (5) - filtered
                "C=CC=C",   // 9 (3)
                "CCO"       // 8 (2)
        );
        boolean[] tmpIsFilteredArray = new boolean[]{true, false, true, true, false, false};
        //
        int tmpMaxBondCount = 3;
        boolean tmpConsiderImplicitHydrogens = false;
        IFilter tmpFilter = new MaxBondCountFilter(tmpMaxBondCount, tmpConsiderImplicitHydrogens);
        //
        TestUtils.filterPipeline_getsFilteredMethodTest_testsBehaviorOfMethodWithSpecificFilter(
                tmpFilter, tmpAtomContainerSet, tmpIsFilteredArray
        );
    }

    /**
     * Tests whether the value returned by the .withMinBondCountFilter() method of the class FilterPipeline is an
     * FilterPipeline instance.
     */
    @Test
    public void withMinBondCountFilterMethodTest_returnsFilterPipelineInstance() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        int tmpIntegerParameter = 10;
        boolean tmpBooleanParameter = true;
        Assertions.assertInstanceOf(tmpCurationPipeline.getClass(), tmpCurationPipeline.withMinBondCountFilter(tmpIntegerParameter, tmpBooleanParameter));
    }

    /**
     * Tests whether the instance returned by the .withMinBondCountFilter() method of the class FilterPipeline is the
     * FilterPipeline instance the method was called of.
     */
    @Test
    public void withMinBondCountFilterMethodTest_returnsFilterPipelineInstanceItWasCalledOf() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        int tmpIntegerParameter = 10;
        boolean tmpBooleanParameter = true;
        Assertions.assertSame(tmpCurationPipeline, tmpCurationPipeline.withMinBondCountFilter(tmpIntegerParameter, tmpBooleanParameter));
    }

    /**
     * Tests whether the listOfSelectedFilters of the FilterPipeline instance returned by the .withMinBondCountFilter()
     * method of the class FilterPipeline was extended by an instance of MinBondCountFilter.
     */
    @Test
    public void withMinBondCountFilterMethodTest_checksWhetherListOfSelectedFiltersWasExtendedByInstanceOfMinBondCountFilter() {
        int tmpIntegerParameter = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withMinBondCountFilter(tmpIntegerParameter, tmpConsiderImplicitHydrogens);
        Assertions.assertInstanceOf(MinBondCountFilter.class, tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast());
    }

    /**
     * Tests whether the MinBondCountFilter added to the listOfSelectedFilters by the .withMinBondCountFilter() method
     * of the class FilterPipeline contains the given min bond count threshold value.
     */
    @Test
    public void withMinBondCountFilterMethodTest_checksWhetherAddedMinBondCountFilterHasGivenThresholdSet_twoTests() {
        int tmpThresholdValue = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withMinBondCountFilter(tmpThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertEquals(tmpThresholdValue, ((MinBondCountFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).getMinBondCount());
        tmpThresholdValue = 20;
        tmpCurationPipeline = new CurationPipeline().withMinBondCountFilter(tmpThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertEquals(tmpThresholdValue, ((MinBondCountFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).getMinBondCount());
    }

    /**
     * Tests whether the MinBondCountFilter added to the listOfSelectedFilters by the .withMinBondCountFilter() method
     * of the class FilterPipeline has the given boolean value set.
     */
    @Test
    public void withMinBondCountFilterMethodTest_checksWhetherAddedMinBondCountFilterHasGivenBooleanConsiderImplHsSet_twoTests() {
        int tmpThresholdValue = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withMinBondCountFilter(tmpThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertTrue(((MinBondCountFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).isConsiderImplicitHydrogens());
        tmpConsiderImplicitHydrogens = false;
        tmpCurationPipeline = new CurationPipeline().withMinBondCountFilter(tmpThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertFalse(((MinBondCountFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).isConsiderImplicitHydrogens());
    }

    /**
     * Tests whether the .withMinBondCountFilter() method of the class FilterPipeline throws an IllegalArgumentException
     * if the given integer parameter is of a negative value.
     */
    @Test
    public void withMinBondCountFilterMethodTest_throwsIllegalArgumentExceptionIfGivenMinBondCountIsNegative() {    //TODO: do so?
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    int tmpNegativeMinBondCount = -1;
                    new CurationPipeline().withMinBondCountFilter(tmpNegativeMinBondCount, true);
                }
        );
    }

    /**
     * Tests whether the .filter() method of class FilterPipeline behaves as expected when filtering with a
     * MinBondCountFilter considering bonds to implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_withMinBondCountFilter_considerImplHs_multipleMolecules() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "CC(=O)O",  // 7 (3) - filtered
                "c1ccccc1", //12 (6)
                "C1CCCC1",  //15 (5)
                "NCC(=O)O", // 9 (4)
                "C=CC=C",   // 9 (3)
                "CCO"       // 8 (2) - filtered
        );
        boolean[] tmpGotFilteredArray = new boolean[]{true, false, false, false, false, true};
        //
        int tmpMinBondCount = 9;
        boolean tmpConsiderImplicitHydrogens = true;
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withMinBondCountFilter(tmpMinBondCount, tmpConsiderImplicitHydrogens);
        IAtomContainerSet tmpFilteredACSet = tmpCurationPipeline.curate(tmpAtomContainerSet);
        int tmpIndexInFilteredSet = 0;
        int tmpFilterID;
        for (int i = 0; i < tmpGotFilteredArray.length; i++) {
            tmpFilterID = tmpAtomContainerSet.getAtomContainer(i).getProperty(CurationPipeline.FILTER_ID_PROPERTY_NAME);
            if (!tmpGotFilteredArray[i]) {
                Assertions.assertEquals(CurationPipeline.NOT_FILTERED_VALUE, tmpFilterID);
                Assertions.assertSame(tmpAtomContainerSet.getAtomContainer(i), tmpFilteredACSet.getAtomContainer(tmpIndexInFilteredSet));
                tmpIndexInFilteredSet++;
                continue;
            }
            Assertions.assertTrue(tmpFilterID != CurationPipeline.NOT_FILTERED_VALUE);
        }
    }

    /**
     * Tests whether the .filter() method of class FilterPipeline behaves as expected when filtering with a
     * MinBondCountFilter not considering bonds to implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_withMinBondCountFilter_notConsiderImplHs_multipleMolecules() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "c1ccccc1", //12 (6)
                "CC(=O)O",  // 7 (3) - filtered
                "NCC(=O)O", // 9 (4)
                "C1CCCC1",  //15 (5)
                "C=CC=C",   // 9 (3) - filtered
                "CCO"       // 8 (2) - filtered
        );
        boolean[] tmpGotFilteredArray = new boolean[]{false, true, false, false, true, true};
        //
        int tmpMinBondCount = 4;
        boolean tmpConsiderImplicitHydrogens = false;
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withMinBondCountFilter(tmpMinBondCount, tmpConsiderImplicitHydrogens);
        IAtomContainerSet tmpFilteredACSet = tmpCurationPipeline.curate(tmpAtomContainerSet);
        int tmpIndexInFilteredSet = 0;
        int tmpFilterID;
        for (int i = 0; i < tmpGotFilteredArray.length; i++) {
            tmpFilterID = tmpAtomContainerSet.getAtomContainer(i).getProperty(CurationPipeline.FILTER_ID_PROPERTY_NAME);
            if (!tmpGotFilteredArray[i]) {
                Assertions.assertEquals(CurationPipeline.NOT_FILTERED_VALUE, tmpFilterID);
                Assertions.assertSame(tmpAtomContainerSet.getAtomContainer(i), tmpFilteredACSet.getAtomContainer(tmpIndexInFilteredSet));
                tmpIndexInFilteredSet++;
                continue;
            }
            Assertions.assertTrue(tmpFilterID != CurationPipeline.NOT_FILTERED_VALUE);
        }
    }

    /**
     * Tests whether the value returned by the .withMaxBondsOfSpecificBondOrderFilter() method of the class
     * FilterPipeline is an FilterPipeline instance.
     */
    @Test
    public void withMaxBondsOfSpecificBondOrderFilterMethodTest_returnsFilterPipelineInstance() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        IBond.Order tmpBondOrder = IBond.Order.UNSET;
        int tmpIntegerParameter = 10;
        boolean tmpBooleanParameter = true;
        Assertions.assertInstanceOf(tmpCurationPipeline.getClass(), tmpCurationPipeline.withMaxBondsOfSpecificBondOrderFilter(
                tmpBondOrder, tmpIntegerParameter, tmpBooleanParameter
        ));
    }

    /**
     * Tests whether the instance returned by the .withMaxBondsOfSpecificBondOrderFilter() method of the class
     * FilterPipeline is the FilterPipeline instance the method was called of.
     */
    @Test
    public void withMaxBondsOfSpecificBondOrderFilterMethodTest_returnsFilterPipelineInstanceItWasCalledOf() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        IBond.Order tmpBondOrder = IBond.Order.UNSET;
        int tmpIntegerParameter = 10;
        boolean tmpBooleanParameter = true;
        Assertions.assertSame(tmpCurationPipeline, tmpCurationPipeline.withMaxBondsOfSpecificBondOrderFilter(
                tmpBondOrder, tmpIntegerParameter, tmpBooleanParameter
        ));
    }

    /**
     * Tests whether the listOfSelectedFilters of the FilterPipeline instance returned by the
     * .withMaxBondsOfSpecificBondOrderFilter() method of the class FilterPipeline was extended by an instance of
     * MaxBondsOfSpecificBondOrderFilter.
     */
    @Test
    public void withMaxBondsOfSpecificBondOrderFilterMethodTest_checksWhetherListOfSelectedFiltersWasExtendedByInstanceOfMaxBondsOfSpecificBondOrderFilter() {
        IBond.Order tmpBondOrder = IBond.Order.UNSET;
        int tmpIntegerParameter = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withMaxBondsOfSpecificBondOrderFilter(tmpBondOrder,
                tmpIntegerParameter, tmpConsiderImplicitHydrogens);
        Assertions.assertInstanceOf(MaxBondsOfSpecificBondOrderFilter.class, tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast());
    }

    /**
     * Tests whether the MaxBondsOfSpecificBondOrderFilter added to the listOfSelectedFilters by the
     * .withMaxBondsOfSpecificBondOrderFilter() method of the class FilterPipeline contains the given bond order of
     * interest.
     */
    @Test
    public void withMaxBondsOfSpecificBondOrderFilterMethodTest_checksWhetherAddedMaxBondsOfSpecificBondOrderFilterHasGivenBondOrderSet_twoTests() {
        IBond.Order tmpBondOrder = IBond.Order.UNSET;
        int tmpThresholdValue = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withMaxBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertEquals(tmpBondOrder, ((MaxBondsOfSpecificBondOrderFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).getBondOrderOfInterest());
        tmpBondOrder = IBond.Order.DOUBLE;
        tmpCurationPipeline = new CurationPipeline().withMaxBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertEquals(tmpBondOrder, ((MaxBondsOfSpecificBondOrderFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).getBondOrderOfInterest());
    }

    /**
     * Tests whether the MaxBondsOfSpecificBondOrderFilter added to the listOfSelectedFilters by the
     * .withMaxBondsOfSpecificBondOrderFilter() method of the class FilterPipeline contains the given max specific bond
     * count threshold value.
     */
    @Test
    public void withMaxBondsOfSpecificBondOrderFilterMethodTest_checksWhetherAddedMaxBondsOfSpecificBondOrderFilterHasGivenThresholdSet_twoTests() {
        IBond.Order tmpBondOrder = IBond.Order.UNSET;
        int tmpThresholdValue = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withMaxBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertEquals(tmpThresholdValue, ((MaxBondsOfSpecificBondOrderFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).getMaxSpecificBondCount());
        tmpThresholdValue = 20;
        tmpCurationPipeline = new CurationPipeline().withMaxBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertEquals(tmpThresholdValue, ((MaxBondsOfSpecificBondOrderFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).getMaxSpecificBondCount());
    }

    /**
     * Tests whether the MaxBondsOfSpecificBondOrderFilter added to the listOfSelectedFilters by the
     * .withMaxBondsOfSpecificBondOrderFilter() method of the class FilterPipeline has the given boolean value set.
     */
    @Test
    public void withMaxBondsOfSpecificBondOrderFilterMethodTest_checksWhetherAddedMaxBondsOfSpecificBondOrderFilterHasGivenBooleanConsiderImplHsSet_twoTests() {
        IBond.Order tmpBondOrder = IBond.Order.UNSET;
        int tmpThresholdValue = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withMaxBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertTrue(((MaxBondsOfSpecificBondOrderFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).isConsiderImplicitHydrogens());
        tmpConsiderImplicitHydrogens = false;
        tmpCurationPipeline = new CurationPipeline().withMaxBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertFalse(((MaxBondsOfSpecificBondOrderFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).isConsiderImplicitHydrogens());
    }

    /**
     * Tests whether the .withMaxBondsOfSpecificBondOrderFilter() method of the class FilterPipeline throws an
     * IllegalArgumentException if the given integer parameter is of a negative value.
     */
    @Test
    public void withMaxBondsOfSpecificBondOrderFilterMethodTest_throwsIllegalArgumentExceptionIfGivenMaxBondCountIsNegative() {    //TODO: do so?
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    IBond.Order tmpBondOrder = IBond.Order.UNSET;
                    int tmpNegativeMaxBondCount = -1;
                    new CurationPipeline().withMaxBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpNegativeMaxBondCount, true);
                }
        );
    }

    /**
     * Tests whether the .filter() method of class FilterPipeline behaves as expected when filtering with a
     * MaxBondsOfSpecificBondOrderFilter with bond order single and considering bonds to implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_withMaxBondsOfSpecificBondOrderFilter_bondOrderSingle_considerImplHs() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "c1ccccc1", // 9 (3) - filtered
                "CC(=O)O",  // 6 (2)
                "O",        // 2 (0)
                "CCO",      // 8 (2) - filtered
                "C=CC=C"    // 7 (1)
        );
        boolean[] tmpIsFilteredArray = new boolean[]{true, false, false, true, false};
        //
        IBond.Order tmpBondOrder = IBond.Order.SINGLE;
        int tmpMaxSpecificBondCount = 7;
        boolean tmpConsiderImplicitHydrogens = true;
        IFilter tmpFilter = new MaxBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpMaxSpecificBondCount, tmpConsiderImplicitHydrogens);
        //
        TestUtils.filterPipeline_getsFilteredMethodTest_testsBehaviorOfMethodWithSpecificFilter(
                tmpFilter, tmpAtomContainerSet, tmpIsFilteredArray
        );
    }

    /**
     * Tests whether the .filter() method of class FilterPipeline behaves as expected when filtering with a
     * MaxBondsOfSpecificBondOrderFilter with bond order single and not considering bonds to implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_withMaxBondsOfSpecificBondOrderFilter_notConsiderImplHs_multipleMolecules() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "c1ccccc1", // 9 (3) - filtered
                "CC(=O)O",  // 6 (2) - filtered
                "O",        // 2 (0)
                "CCO",      // 8 (2) - filtered
                "C=CC=C"    // 7 (1)
        );
        boolean[] tmpIsFilteredArray = new boolean[]{true, true, false, true, false};
        //
        IBond.Order tmpBondOrder = IBond.Order.SINGLE;
        int tmpMaxSpecificBondCount = 1;
        boolean tmpConsiderImplicitHydrogens = false;
        IFilter tmpFilter = new MaxBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpMaxSpecificBondCount, tmpConsiderImplicitHydrogens);
        //
        TestUtils.filterPipeline_getsFilteredMethodTest_testsBehaviorOfMethodWithSpecificFilter(
                tmpFilter, tmpAtomContainerSet, tmpIsFilteredArray
        );
    }

    /**
     * Tests whether the .filter() method of class FilterPipeline behaves as expected when filtering with a
     * MaxBondsOfSpecificBondOrderFilter with bond order double; test is exemplary for filtering on other bond orders.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_withMaxBondsOfSpecificBondOrderFilter_bondOrderDouble_exemplaryForFilteringOnOtherBondOrders() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "CC(=O)O",  // 1
                "c1ccccc1", // 3 - filtered
                "NCC(=O)O", // 1
                "CCO",      // 0
                "C=CC=C"    // 2 - filtered
        );
        boolean[] tmpIsFilteredArray = new boolean[]{false, true, false, false, true};
        //
        IBond.Order tmpBondOrder = IBond.Order.DOUBLE;
        int tmpMaxSpecificBondCount = 1;
        boolean tmpConsiderImplicitHydrogens = true;    //can be ignored
        IFilter tmpFilter = new MaxBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpMaxSpecificBondCount, tmpConsiderImplicitHydrogens);
        //
        TestUtils.filterPipeline_getsFilteredMethodTest_testsBehaviorOfMethodWithSpecificFilter(
                tmpFilter, tmpAtomContainerSet, tmpIsFilteredArray
        );
    }

    /**
     * Tests whether the value returned by the .withMinBondsOfSpecificBondOrderFilter() method of the class
     * FilterPipeline is an FilterPipeline instance.
     */
    @Test
    public void withMinBondsOfSpecificBondOrderFilterMethodTest_returnsFilterPipelineInstance() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        IBond.Order tmpBondOrder = IBond.Order.UNSET;
        int tmpIntegerParameter = 10;
        boolean tmpBooleanParameter = true;
        Assertions.assertInstanceOf(tmpCurationPipeline.getClass(), tmpCurationPipeline.withMinBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpIntegerParameter, tmpBooleanParameter));
    }

    /**
     * Tests whether the instance returned by the .withMinBondsOfSpecificBondOrderFilter() method of the class
     * FilterPipeline is the FilterPipeline instance the method was called of.
     */
    @Test
    public void withMinBondsOfSpecificBondOrderFilterMethodTest_returnsFilterPipelineInstanceItWasCalledOf() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        IBond.Order tmpBondOrder = IBond.Order.UNSET;
        int tmpIntegerParameter = 10;
        boolean tmpBooleanParameter = true;
        Assertions.assertSame(tmpCurationPipeline, tmpCurationPipeline.withMinBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpIntegerParameter, tmpBooleanParameter));
    }

    /**
     * Tests whether the listOfSelectedFilters of the FilterPipeline instance returned by the
     * .withMinBondsOfSpecificBondOrderFilter() method of the class FilterPipeline was extended by an instance of
     * MinBondsOfSpecificBondOrderFilter.
     */
    @Test
    public void withMinBondsOfSpecificBondOrderFilterMethodTest_checksWhetherListOfSelectedFiltersWasExtendedByInstanceOfMinBondsOfSpecificBondOrderFilter() {
        IBond.Order tmpBondOrder = IBond.Order.UNSET;
        int tmpIntegerParameter = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withMinBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpIntegerParameter, tmpConsiderImplicitHydrogens);
        Assertions.assertInstanceOf(MinBondsOfSpecificBondOrderFilter.class, tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast());
    }

    /**
     * Tests whether the MinBondsOfSpecificBondOrderFilter added to the listOfSelectedFilters by the
     * .withMinBondsOfSpecificBondOrderFilter() method of the class FilterPipeline contains the given bond order of
     * interest.
     */
    @Test
    public void withMinBondsOfSpecificBondOrderFilterMethodTest_checksWhetherAddedMinBondsOfSpecificBondOrderFilterHasGivenBondOrderSet_twoTests() {
        IBond.Order tmpBondOrder = IBond.Order.UNSET;
        int tmpThresholdValue = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withMinBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertEquals(tmpBondOrder, ((MinBondsOfSpecificBondOrderFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).getBondOrderOfInterest());
        tmpBondOrder = IBond.Order.DOUBLE;
        tmpCurationPipeline = new CurationPipeline().withMinBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertEquals(tmpBondOrder, ((MinBondsOfSpecificBondOrderFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).getBondOrderOfInterest());
    }

    /**
     * Tests whether the MinBondsOfSpecificBondOrderFilter added to the listOfSelectedFilters by the
     * .withMinBondsOfSpecificBondOrderFilter() method of the class FilterPipeline contains the given min specific bond
     * count threshold value.
     */
    @Test
    public void withMinBondsOfSpecificBondOrderFilterMethodTest_checksWhetherAddedMinBondsOfSpecificBondOrderFilterHasGivenThresholdSet_twoTests() {
        IBond.Order tmpBondOrder = IBond.Order.UNSET;
        int tmpThresholdValue = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withMinBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertEquals(tmpThresholdValue, ((MinBondsOfSpecificBondOrderFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).getMinSpecificBondCount());
        tmpThresholdValue = 20;
        tmpCurationPipeline = new CurationPipeline().withMinBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertEquals(tmpThresholdValue, ((MinBondsOfSpecificBondOrderFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).getMinSpecificBondCount());
    }

    /**
     * Tests whether the MinBondsOfSpecificBondOrderFilter added to the listOfSelectedFilters by the
     * .withMinBondsOfSpecificBondOrderFilter() method of the class FilterPipeline has the given boolean value set.
     */
    @Test
    public void withMinBondsOfSpecificBondOrderFilterMethodTest_checksWhetherAddedMinBondsOfSpecificBondOrderFilterHasGivenBooleanConsiderImplHsSet_twoTests() {
        IBond.Order tmpBondOrder = IBond.Order.UNSET;
        int tmpThresholdValue = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withMinBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertTrue(((MinBondsOfSpecificBondOrderFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).isConsiderImplicitHydrogens());
        tmpConsiderImplicitHydrogens = false;
        tmpCurationPipeline = new CurationPipeline().withMinBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertFalse(((MinBondsOfSpecificBondOrderFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).isConsiderImplicitHydrogens());
    }

    /**
     * Tests whether the .withMinBondsOfSpecificBondOrderFilter() method of the class FilterPipeline throws an
     * IllegalArgumentException if the given integer parameter is of a negative value.
     */
    @Test
    public void withMinBondsOfSpecificBondOrderFilterMethodTest_throwsIllegalArgumentExceptionIfGivenMinBondCountIsNegative() {    //TODO: do so?
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    IBond.Order tmpBondOrder = IBond.Order.UNSET;
                    int tmpNegativeMinBondCount = -1;
                    new CurationPipeline().withMinBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpNegativeMinBondCount, true);
                }
        );
    }

    /**
     * Tests whether the .filter() method of class FilterPipeline behaves as expected when filtering with a
     * MinBondsOfSpecificBondOrderFilter with bond order single and considering bonds to implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_withMinBondsOfSpecificBondOrderFilter_bondOrderSingle_considerImplHs() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "c1ccccc1", // 9 (3)
                "CC(=O)O",  // 6 (2) - filtered
                "O",        // 2 (0) - filtered
                "CCO",      // 8 (2)
                "C=CC=C"    // 7 (1)
        );
        boolean[] tmpIsFilteredArray = new boolean[]{false, true, true, false, false};
        //
        IBond.Order tmpBondOrder = IBond.Order.SINGLE;
        int tmpMinSpecificBondCount = 7;
        boolean tmpConsiderImplicitHydrogens = true;
        IFilter tmpFilter = new MinBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpMinSpecificBondCount, tmpConsiderImplicitHydrogens);
        //
        TestUtils.filterPipeline_getsFilteredMethodTest_testsBehaviorOfMethodWithSpecificFilter(
                tmpFilter, tmpAtomContainerSet, tmpIsFilteredArray
        );
    }

    /**
     * Tests whether the .filter() method of class FilterPipeline behaves as expected when filtering with a
     * MinBondsOfSpecificBondOrderFilter with bond order single and not considering bonds to implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_withMinBondsOfSpecificBondOrderFilter_notConsiderImplHs_multipleMolecules() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "c1ccccc1", // 9 (3)
                "CC(=O)O",  // 6 (2)
                "O",        // 2 (0) - filtered
                "CCO",      // 8 (2)
                "C=CC=C"    // 7 (1) - filtered
        );
        boolean[] tmpIsFilteredArray = new boolean[]{false, false, true, false, true};
        //
        IBond.Order tmpBondOrder = IBond.Order.SINGLE;
        int tmpMinSpecificBondCount = 2;
        boolean tmpConsiderImplicitHydrogens = false;
        IFilter tmpFilter = new MinBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpMinSpecificBondCount, tmpConsiderImplicitHydrogens);
        //
        TestUtils.filterPipeline_getsFilteredMethodTest_testsBehaviorOfMethodWithSpecificFilter(
                tmpFilter, tmpAtomContainerSet, tmpIsFilteredArray
        );
    }

    /**
     * Tests whether the .filter() method of class FilterPipeline behaves as expected when filtering with a
     * MinBondsOfSpecificBondOrderFilter with bond order double; test is exemplary for filtering on other bond orders.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_withMinBondsOfSpecificBondOrderFilter_bondOrderDouble_exemplaryForFilteringOnOtherBondOrders() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "CC(=O)O",  // 1 - filtered
                "c1ccccc1", // 3
                "NCC(=O)O", // 1 - filtered
                "CCO",      // 0 - filtered
                "C=CC=C"    // 2
        );
        boolean[] tmpIsFilteredArray = new boolean[]{true, false, true, true, false};
        //
        IBond.Order tmpBondOrder = IBond.Order.DOUBLE;
        int tmpMinSpecificBondCount = 2;
        boolean tmpConsiderImplicitHydrogens = true;    //can be ignored
        IFilter tmpFilter = new MinBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpMinSpecificBondCount, tmpConsiderImplicitHydrogens);
        //
        TestUtils.filterPipeline_getsFilteredMethodTest_testsBehaviorOfMethodWithSpecificFilter(
                tmpFilter, tmpAtomContainerSet, tmpIsFilteredArray
        );
    }

    /**
     * Tests whether the value returned by the .withMaxHeavyAtomCountFilter() method of the class FilterPipeline is a
     * FilterPipeline instance.
     */
    @Test
    public void withMaxHeavyAtomCountFilterMethodTest_returnsFilterPipelineInstance() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        int tmpIntegerParameter = 10;
        Assertions.assertInstanceOf(tmpCurationPipeline.getClass(), tmpCurationPipeline.withMaxHeavyAtomCountFilter(tmpIntegerParameter));
    }

    /**
     * Tests whether the instance returned by the .withMaxHeavyAtomCountFilter() method of the class FilterPipeline is
     * the FilterPipeline instance the method was called of.
     */
    @Test
    public void withMaxHeavyAtomCountFilterMethodTest_returnsFilterPipelineInstanceItWasCalledOf() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        int tmpIntegerParameter = 10;
        Assertions.assertSame(tmpCurationPipeline, tmpCurationPipeline.withMaxHeavyAtomCountFilter(tmpIntegerParameter));
    }

    /**
     * Tests whether the listOfSelectedFilters of the FilterPipeline instance returned by the .withMaxHeavyAtomCountFilter()
     * method of the class FilterPipeline was extended by an instance of MaxHeavyAtomCountFilter.
     */
    @Test
    public void withMaxHeavyAtomCountFilterMethodTest_checksWhetherListOfSelectedFiltersWasExtendedByInstanceOfMaxHeavyAtomCountFilter() {
        int tmpIntegerParameter = 10;
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withMaxHeavyAtomCountFilter(tmpIntegerParameter);
        Assertions.assertInstanceOf(MaxHeavyAtomCountFilter.class, tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast());
    }

    /**
     * Tests whether the MaxHeavyAtomCountFilter added to the listOfSelectedFilters by the .withMaxHeavyAtomCountFilter()
     * method of the class FilterPipeline contains the given max heavy atom count threshold value.
     */
    @Test
    public void withMaxHeavyAtomCountFilterMethodTest_checksWhetherAddedMaxHeavyAtomCountFilterHasGivenThresholdSet_twoTests() {
        int tmpThresholdValue = 10;
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withMaxHeavyAtomCountFilter(tmpThresholdValue);
        Assertions.assertEquals(tmpThresholdValue, ((MaxHeavyAtomCountFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).getMaxHeavyAtomCount());
        tmpThresholdValue = 20;
        tmpCurationPipeline = new CurationPipeline().withMaxHeavyAtomCountFilter(tmpThresholdValue);
        Assertions.assertEquals(tmpThresholdValue, ((MaxHeavyAtomCountFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).getMaxHeavyAtomCount());
    }

    /**
     * Tests whether the .withMaxHeavyAtomCountFilter() method of the class FilterPipeline throws an IllegalArgumentException
     * if the given integer parameter is of a negative value.
     */
    @Test
    public void withMaxHeavyAtomCountFilterMethodTest_throwsIllegalArgumentExceptionIfGivenMaxHeavyAtomCountIsNegative() {    //TODO: do so?
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    int tmpNegativeMaxHeavyAtomCount = -1;
                    new CurationPipeline().withMaxHeavyAtomCountFilter(tmpNegativeMaxHeavyAtomCount);
                }
        );
    }

    /**
     * Tests whether the .filter() method of class FilterPipeline behaves as expected in filtering process with a
     * MaxHeavyAtomCountFilter; test 1.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_withMaxHeavyAtomCountFilter_multipleMolecules_test1() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "CC(=O)O",  // 4
                "c1ccccc1", // 6 - filtered
                "C1CCCC1",  // 5 - filtered
                "NCC(=O)O", // 5 - filtered
                "O",        // 1
                "CCO"       // 3
        );
        boolean[] tmpIsFilteredArray = new boolean[]{false, true, true, true, false, false};
        //
        int tmpMaxHeavyAtomCount = 4;
        IFilter tmpFilter = new MaxHeavyAtomCountFilter(tmpMaxHeavyAtomCount);
        //
        TestUtils.filterPipeline_getsFilteredMethodTest_testsBehaviorOfMethodWithSpecificFilter(
                tmpFilter, tmpAtomContainerSet, tmpIsFilteredArray
        );
    }

    /**
     * Tests whether the .filter() method of class FilterPipeline behaves as expected in filtering process with a
     * MaxHeavyAtomCountFilter; test 2.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_withMaxHeavyAtomCountFilter_multipleMolecules_test2() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "CC(=O)O",  // 4 - filtered
                "c1ccccc1", // 6 - filtered
                "C1CCCC1",  // 5 - filtered
                "O",        // 1
                "NCC(=O)O", // 5 - filtered
                "CCO"       // 3
        );
        boolean[] tmpIsFilteredArray = new boolean[]{true, true, true, false, true, false};
        //
        int tmpMaxHeavyAtomCount = 3;
        IFilter tmpFilter = new MaxHeavyAtomCountFilter(tmpMaxHeavyAtomCount);
        //
        TestUtils.filterPipeline_getsFilteredMethodTest_testsBehaviorOfMethodWithSpecificFilter(
                tmpFilter, tmpAtomContainerSet, tmpIsFilteredArray
        );
    }

    /**
     * Tests whether the value returned by the .withMinHeavyAtomCountFilter() method of the class FilterPipeline is a
     * FilterPipeline instance.
     */
    @Test
    public void withMinHeavyAtomCountFilterMethodTest_returnsFilterPipelineInstance() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        int tmpIntegerParameter = 10;
        Assertions.assertInstanceOf(tmpCurationPipeline.getClass(), tmpCurationPipeline.withMinHeavyAtomCountFilter(tmpIntegerParameter));
    }

    /**
     * Tests whether the instance returned by the .withMinHeavyAtomCountFilter() method of the class FilterPipeline is
     * the FilterPipeline instance the method was called of.
     */
    @Test
    public void withMinHeavyAtomCountFilterMethodTest_returnsFilterPipelineInstanceItWasCalledOf() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        int tmpIntegerParameter = 10;
        Assertions.assertSame(tmpCurationPipeline, tmpCurationPipeline.withMinHeavyAtomCountFilter(tmpIntegerParameter));
    }

    /**
     * Tests whether the listOfSelectedFilters of the FilterPipeline instance returned by the .withMinHeavyAtomCountFilter()
     * method of the class FilterPipeline was extended by an instance of MinHeavyAtomCountFilter.
     */
    @Test
    public void withMinHeavyAtomCountFilterMethodTest_checksWhetherListOfSelectedFiltersWasExtendedByInstanceOfMinHeavyAtomCountFilter() {
        int tmpIntegerParameter = 10;
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withMinHeavyAtomCountFilter(tmpIntegerParameter);
        Assertions.assertInstanceOf(MinHeavyAtomCountFilter.class, tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast());
    }

    /**
     * Tests whether the MinHeavyAtomCountFilter added to the listOfSelectedFilters by the .withMinHeavyAtomCountFilter()
     * method of the class FilterPipeline contains the given min heavy atom count threshold value.
     */
    @Test
    public void withMinHeavyAtomCountFilterMethodTest_checksWhetherAddedMinHeavyAtomCountFilterHasGivenThresholdSet_twoTests() {
        int tmpThresholdValue = 10;
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withMinHeavyAtomCountFilter(tmpThresholdValue);
        Assertions.assertEquals(tmpThresholdValue, ((MinHeavyAtomCountFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).getMinHeavyAtomCount());
        tmpThresholdValue = 20;
        tmpCurationPipeline = new CurationPipeline().withMinHeavyAtomCountFilter(tmpThresholdValue);
        Assertions.assertEquals(tmpThresholdValue, ((MinHeavyAtomCountFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).getMinHeavyAtomCount());
    }

    /**
     * Tests whether the .withMinHeavyAtomCountFilter() method of the class FilterPipeline throws an IllegalArgumentException
     * if the given integer parameter is of a negative value.
     */
    @Test
    public void withMinHeavyAtomCountFilterMethodTest_throwsIllegalArgumentExceptionIfGivenMinHeavyAtomCountIsNegative() {    //TODO: do so?
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    int tmpNegativeMinHeavyAtomCount = -1;
                    new CurationPipeline().withMinHeavyAtomCountFilter(tmpNegativeMinHeavyAtomCount);
                }
        );
    }

    /**
     * Tests whether the .filter() method of class FilterPipeline behaves as expected in a filtering process with a
     * MinHeavyAtomCountFilter; test 1.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_withMinHeavyAtomCountFilter_multipleMolecules_test1() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "CC(=O)O",  // 4 - filtered
                "c1ccccc1", // 6
                "C1CCCC1",  // 5
                "NCC(=O)O", // 5
                "O",        // 1 - filtered
                "CCO"       // 3 - filtered
        );
        boolean[] tmpIsFilteredArray = new boolean[]{true, false, false, false, true, true};
        //
        int tmpMinHeavyAtomCount = 5;
        IFilter tmpFilter = new MinHeavyAtomCountFilter(tmpMinHeavyAtomCount);
        //
        TestUtils.filterPipeline_getsFilteredMethodTest_testsBehaviorOfMethodWithSpecificFilter(
                tmpFilter, tmpAtomContainerSet, tmpIsFilteredArray
        );
    }

    /**
     * Tests whether the .filter() method of class FilterPipeline behaves as expected in a filtering process with a
     * MinHeavyAtomCountFilter; test 2.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_withMinHeavyAtomCountFilter_multipleMolecules_test2() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "CC(=O)O",  // 4
                "c1ccccc1", // 6
                "C1CCCC1",  // 5
                "O",        // 1 - filtered
                "NCC(=O)O", // 5
                "CCO"       // 3 - filtered
        );
        boolean[] tmpIsFilteredArray = new boolean[]{false, false, false, true, false, true};
        //
        int tmpMinHeavyAtomCount = 4;
        IFilter tmpFilter = new MinHeavyAtomCountFilter(tmpMinHeavyAtomCount);
        //
        TestUtils.filterPipeline_getsFilteredMethodTest_testsBehaviorOfMethodWithSpecificFilter(
                tmpFilter, tmpAtomContainerSet, tmpIsFilteredArray
        );
    }

    /**
     * Tests whether the value returned by the .withHasAllValidAtomicNumbersFilter() method of the class FilterPipeline
     * is the FilterPipeline instance the method was called of.
     */
    @Test
    public void withHasAllValidAtomicNumbersFilterMethodTest_returnsFilterPipelineInstanceItself() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        boolean tmpWildcardAtomicNumberIsValid = true;
        Assertions.assertInstanceOf(tmpCurationPipeline.getClass(), tmpCurationPipeline.withHasAllValidAtomicNumbersFilter(tmpWildcardAtomicNumberIsValid));
    }

    /**
     * Tests whether the listOfSelectedFilters of the FilterPipeline instance returned by the .withHasAllValidAtomicNumbersFilter()
     * method of the class FilterPipeline was extended by an instance of HasAllValidAtomicNumbersFilter.
     */
    @Test
    public void withHasAllValidAtomicNumbersFilterMethodTest_checksWhetherListOfSelectedFiltersWasExtendedByInstanceOfHasAllValidAtomicNumbersFilter() {
        boolean tmpWildcardAtomicNumberIsValid = true;
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withHasAllValidAtomicNumbersFilter(tmpWildcardAtomicNumberIsValid);
        Assertions.assertInstanceOf(HasAllValidAtomicNumbersFilter.class, tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast());
    }

    /**
     * Tests whether the HasAllValidAtomicNumbersFilter added to the listOfSelectedFilters by the .withHasAllValidAtomicNumbersFilter()
     * method of the class FilterPipeline has the boolean class field wildcardAtomicNumberIsValid set to the given
     * boolean value.
     */
    @Test
    public void withHasAllValidAtomicNumbersFilterMethodTest_checksWhetherAddedHasAllValidAtomicNumbersFilterHasGivenBooleanValueSet_twoTests() {
        boolean tmpWildcardAtomicNumberIsValid = true;
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withHasAllValidAtomicNumbersFilter(tmpWildcardAtomicNumberIsValid);
        Assertions.assertEquals(tmpWildcardAtomicNumberIsValid,
                ((HasAllValidAtomicNumbersFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).isWildcardAtomicNumberIsValid());
        tmpWildcardAtomicNumberIsValid = false;
        tmpCurationPipeline = new CurationPipeline().withHasAllValidAtomicNumbersFilter(tmpWildcardAtomicNumberIsValid);
        Assertions.assertEquals(tmpWildcardAtomicNumberIsValid,
                ((HasAllValidAtomicNumbersFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).isWildcardAtomicNumberIsValid());
    }

    /**
     * Tests whether the .filter() method of class FilterPipeline behaves as expected in a filtering process with a
     * HasAllValidAtomicNumbersFilter; test 2; wildcard atomic number is considered as valid.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_withHasAllValidAtomicNumbersFilter_multipleMolecules_test1_zeroIsValid() throws InvalidSmilesException {
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
        boolean[] tmpIsFilteredArray = new boolean[]{
                false,  //all valid
                true,   //atomic number < 0
                true,   //atomic number > 118
                false,  //atomic number = 0
                true    //atomic number is null
        };
        //
        boolean tmpWildcardAtomicNumberIsValid = true;
        //
        IFilter tmpFilter = new HasAllValidAtomicNumbersFilter(tmpWildcardAtomicNumberIsValid);
        TestUtils.filterPipeline_getsFilteredMethodTest_testsBehaviorOfMethodWithSpecificFilter(
                tmpFilter, tmpAtomContainerSet, tmpIsFilteredArray
        );
    }

    /**
     * Tests whether the .filter() method of class FilterPipeline behaves as expected in a filtering process with a
     * HasAllValidAtomicNumbersFilter; test 2; wildcard atomic number is considered as invalid.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_withHasAllValidAtomicNumbersFilter_multipleMolecules_test2_zeroIsInvalid() throws InvalidSmilesException {
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
        boolean[] tmpIsFilteredArray = new boolean[]{
                false,  //all valid
                true,   //atomic number < 0
                true,   //atomic number > 118
                true,   //atomic number = 0
                true    //atomic number is null
        };
        //
        boolean tmpWildcardAtomicNumberIsValid = false;
        //
        IFilter tmpFilter = new HasAllValidAtomicNumbersFilter(tmpWildcardAtomicNumberIsValid);
        TestUtils.filterPipeline_getsFilteredMethodTest_testsBehaviorOfMethodWithSpecificFilter(
                tmpFilter, tmpAtomContainerSet, tmpIsFilteredArray
        );
    }

    /**
     * Tests whether the value returned by the .withHasInvalidAtomicNumbersFilter() method of the class FilterPipeline
     * is the FilterPipeline instance the method was called of.
     */
    @Test
    public void withHasInvalidAtomicNumbersFilterMethodTest_returnsFilterPipelineInstanceItself() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        boolean tmpWildcardAtomicNumberIsValid = true;
        Assertions.assertInstanceOf(tmpCurationPipeline.getClass(), tmpCurationPipeline.withHasInvalidAtomicNumbersFilter(tmpWildcardAtomicNumberIsValid));
    }

    /**
     * Tests whether the listOfSelectedFilters of the FilterPipeline instance returned by the .withHasInvalidAtomicNumbersFilter()
     * method of the class FilterPipeline was extended by an instance of HasInvalidAtomicNumbersFilter.
     */
    @Test
    public void withHasInvalidAtomicNumbersFilterMethodTest_checksWhetherListOfSelectedFiltersWasExtendedByInstanceOfHasInvalidAtomicNumbersFilter() {
        boolean tmpWildcardAtomicNumberIsValid = true;
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withHasInvalidAtomicNumbersFilter(tmpWildcardAtomicNumberIsValid);
        Assertions.assertInstanceOf(HasInvalidAtomicNumbersFilter.class, tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast());
    }

    /**
     * Tests whether the HasInvalidAtomicNumbersFilter added to the listOfSelectedFilters by the .withHasInvalidAtomicNumbersFilter()
     * method of the class FilterPipeline has the boolean class field wildcardAtomicNumberIsValid set to the given
     * boolean value.
     */
    @Test
    public void withHasInvalidAtomicNumbersFilterMethodTest_checksWhetherAddedHasInvalidAtomicNumbersFilterHasGivenBooleanValueSet_twoTests() {
        boolean tmpWildcardAtomicNumberIsValid = true;
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withHasInvalidAtomicNumbersFilter(tmpWildcardAtomicNumberIsValid);
        Assertions.assertEquals(tmpWildcardAtomicNumberIsValid,
                ((HasInvalidAtomicNumbersFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).isWildcardAtomicNumberIsValid());
        tmpWildcardAtomicNumberIsValid = false;
        tmpCurationPipeline = new CurationPipeline().withHasInvalidAtomicNumbersFilter(tmpWildcardAtomicNumberIsValid);
        Assertions.assertEquals(tmpWildcardAtomicNumberIsValid,
                ((HasInvalidAtomicNumbersFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).isWildcardAtomicNumberIsValid());
    }

    /**
     * Tests whether the .filter() method of class FilterPipeline behaves as expected in a filtering process with a
     * HasInvalidAtomicNumbersFilter; test 2; wildcard atomic number is considered as valid.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_withHasInvalidAtomicNumbersFilter_multipleMolecules_test1_zeroIsValid() throws InvalidSmilesException {
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
        boolean[] tmpIsFilteredArray = new boolean[]{
                true,   //all valid
                false,  //atomic number < 0
                false,  //atomic number > 118
                true,   //atomic number = 0
                false   //atomic number is null
        };
        //
        boolean tmpWildcardAtomicNumberIsValid = true;
        //
        IFilter tmpFilter = new HasInvalidAtomicNumbersFilter(tmpWildcardAtomicNumberIsValid);
        TestUtils.filterPipeline_getsFilteredMethodTest_testsBehaviorOfMethodWithSpecificFilter(
                tmpFilter, tmpAtomContainerSet, tmpIsFilteredArray
        );
    }

    /**
     * Tests whether the .filter() method of class FilterPipeline behaves as expected in a filtering process with a
     * HasInvalidAtomicNumbersFilter; test 2; wildcard atomic number is considered as invalid.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_withHasInvalidAtomicNumbersFilter_multipleMolecules_test2_zeroIsInvalid() throws InvalidSmilesException {
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
        boolean[] tmpIsFilteredArray = new boolean[]{
                true,   //all valid
                false,  //atomic number < 0
                false,  //atomic number > 118
                false,  //atomic number = 0
                false   //atomic number is null
        };
        //
        boolean tmpWildcardAtomicNumberIsValid = false;
        //
        IFilter tmpFilter = new HasInvalidAtomicNumbersFilter(tmpWildcardAtomicNumberIsValid);
        TestUtils.filterPipeline_getsFilteredMethodTest_testsBehaviorOfMethodWithSpecificFilter(
                tmpFilter, tmpAtomContainerSet, tmpIsFilteredArray
        );
    }

    /**
     * Tests whether the value returned by the .withMaxMolecularMassFilter() method of the class FilterPipeline with
     * MassComputationFlavours parameter is the FilterPipeline instance the method was called of.
     */
    @Test
    public void withMaxMolecularMassFilterMethodTest_withFlavour_returnsFilterPipelineInstanceItself() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        double tmpThresholdValue = 10.0;
        MassComputationFlavours tmpFlavour = MassComputationFlavours.MolWeight;
        Assertions.assertInstanceOf(tmpCurationPipeline.getClass(), tmpCurationPipeline.withMaxMolecularMassFilter(tmpThresholdValue, tmpFlavour));
    }

    /**
     * Tests whether the listOfSelectedFilters of the FilterPipeline instance returned by the .withMaxMolecularMassFilter()
     * method of the class FilterPipeline with MassComputationFlavours parameter was extended by an instance of
     * MaxMolecularMassFilter.
     */
    @Test
    public void withMaxMolecularMassFilterMethodTest_withFlavour_listOfSelectedFiltersWasExtendedByInstanceOfMaxMolecularMassFilter() {
        double tmpThresholdValue = 10.0;
        MassComputationFlavours tmpFlavour = MassComputationFlavours.MolWeight;
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withMaxMolecularMassFilter(tmpThresholdValue, tmpFlavour);
        Assertions.assertInstanceOf(MaxMolecularMassFilter.class, tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast());
    }

    /**
     * Tests whether the MaxMolecularMassFilter added to the listOfSelectedFilters by the .withMaxMolecularMassFilter()
     * method of the class FilterPipeline with MassComputationFlavours parameter has the given threshold value set to
     * it; two tests.
     */
    @Test
    public void withMaxMolecularMassFilterMethodTest_withFlavour_newFilterHasGivenThresholdValue_twoTests() {
        double tmpThresholdValue = 10.0;
        MassComputationFlavours tmpFlavour = MassComputationFlavours.MolWeight;
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withMaxMolecularMassFilter(tmpThresholdValue, tmpFlavour);
        Assertions.assertEquals(tmpThresholdValue, ((MaxMolecularMassFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).getMaxMolecularMass());
        tmpThresholdValue = 20.0;
        tmpCurationPipeline = new CurationPipeline().withMaxMolecularMassFilter(tmpThresholdValue, tmpFlavour);
        Assertions.assertEquals(tmpThresholdValue, ((MaxMolecularMassFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).getMaxMolecularMass());
    }

    /**
     * Tests whether the MaxMolecularMassFilter added to the listOfSelectedFilters by the .withMaxMolecularMassFilter()
     * method of the class FilterPipeline with MassComputationFlavours parameter has the given mass computation flavour
     * set to it; two tests.
     */
    @Test
    public void withMaxMolecularMassFilterMethodTest_withFlavour_newFilterHasGivenMassComputationFlavour_twoTests() {
        double tmpThresholdValue = 10.0;
        MassComputationFlavours tmpFlavour = MassComputationFlavours.MolWeight;
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withMaxMolecularMassFilter(tmpThresholdValue, tmpFlavour);
        Assertions.assertEquals(tmpFlavour, ((MaxMolecularMassFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).getMassComputationFlavour());
        tmpFlavour = MassComputationFlavours.MonoIsotopic;
        tmpCurationPipeline = new CurationPipeline().withMaxMolecularMassFilter(tmpThresholdValue, tmpFlavour);
        Assertions.assertEquals(tmpFlavour, ((MaxMolecularMassFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).getMassComputationFlavour());
    }

    /**
     * Tests whether the .withMaxMolecularMassFilter() method of the class FilterPipeline with MassComputationFlavours
     * parameter throws an IllegalArgumentException if the given max molecular mass threshold value is of a negative
     * value; two tests.
     */
    @Test
    public void withMaxMolecularMassFilterMethodTest_withFlavour_throwsIllegalArgumentExceptionIfGivenThresholdValueIsOfNegativeValue_twoTests() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    double tmpThresholdValue = -0.1;
                    MassComputationFlavours tmpFlavour = MassComputationFlavours.MolWeight;
                    new CurationPipeline().withMaxMolecularMassFilter(tmpThresholdValue, tmpFlavour);
                }
        );
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    double tmpThresholdValue = -5.0;
                    MassComputationFlavours tmpFlavour = MassComputationFlavours.MolWeight;
                    new CurationPipeline().withMaxMolecularMassFilter(tmpThresholdValue, tmpFlavour);
                }
        );
    }

    /**
     * Tests whether the .withMaxMolecularMassFilter() method of the class FilterPipeline with MassComputationFlavours
     * parameter throws a NullPointerException if the given mass computation flavour is null.
     */
    @Test
    public void withMaxMolecularMassFilterMethodTest_withFlavour_throwsNullPointerExceptionIfGivenFlavourIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    double tmpThresholdValue = 10.0;
                    MassComputationFlavours tmpFlavour = null;
                    new CurationPipeline().withMaxMolecularMassFilter(tmpThresholdValue, tmpFlavour);
                }
        );
    }

    /**
     * Tests whether the value returned by the .withMaxMolecularMassFilter() method of the class FilterPipeline with
     * no MassComputationFlavours parameter is the FilterPipeline instance the method was called of.
     */
    @Test
    public void withMaxMolecularMassFilterMethodTest_noFlavour_returnsFilterPipelineInstanceItself() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        double tmpThresholdValue = 10.0;
        Assertions.assertInstanceOf(tmpCurationPipeline.getClass(), tmpCurationPipeline.withMaxMolecularMassFilter(tmpThresholdValue));
    }

    /**
     * Tests whether the listOfSelectedFilters of the FilterPipeline instance returned by the .withMaxMolecularMassFilter()
     * method of the class FilterPipeline with no MassComputationFlavours parameter was extended by an instance of
     * MaxMolecularMassFilter.
     */
    @Test
    public void withMaxMolecularMassFilterMethodTest_noFlavour_listOfSelectedFiltersWasExtendedByInstanceOfMaxMolecularMassFilter() {
        double tmpThresholdValue = 10.0;
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withMaxMolecularMassFilter(tmpThresholdValue);
        Assertions.assertInstanceOf(MaxMolecularMassFilter.class, tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast());
    }

    /**
     * Tests whether the MaxMolecularMassFilter added to the listOfSelectedFilters by the .withMaxMolecularMassFilter()
     * method of the class FilterPipeline with no MassComputationFlavours parameter has the given threshold value set to
     * it; two tests.
     */
    @Test
    public void withMaxMolecularMassFilterMethodTest_noFlavour_newFilterHasGivenThresholdValue_twoTests() {
        double tmpThresholdValue = 10.0;
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withMaxMolecularMassFilter(tmpThresholdValue);
        Assertions.assertEquals(tmpThresholdValue, ((MaxMolecularMassFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).getMaxMolecularMass());
        tmpThresholdValue = 20.0;
        tmpCurationPipeline = new CurationPipeline().withMaxMolecularMassFilter(tmpThresholdValue);
        Assertions.assertEquals(tmpThresholdValue, ((MaxMolecularMassFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).getMaxMolecularMass());
    }

    /**
     * Tests whether the MaxMolecularMassFilter added to the listOfSelectedFilters by the .withMaxMolecularMassFilter()
     * method of the class FilterPipeline with no MassComputationFlavours parameter has {@link MassComputationFlavours#MolWeight}
     * set as mass computation flavour.
     */
    @Test
    public void withMaxMolecularMassFilterMethodTest_noFlavour_newFilterHasMolWeightAsMassComputationFlavour() {
        double tmpThresholdValue = 10.0;
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withMaxMolecularMassFilter(tmpThresholdValue);
        Assertions.assertEquals(
                MassComputationFlavours.MolWeight,
                ((MaxMolecularMassFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).getMassComputationFlavour()
        );
    }

    /**
     * Tests whether the .withMaxMolecularMassFilter() method of the class FilterPipeline with no MassComputationFlavours
     * parameter throws an IllegalArgumentException if the given max molecular mass threshold value is of a negative
     * value; two tests.
     */
    @Test
    public void withMaxMolecularMassFilterMethodTest_noFlavour_throwsIllegalArgumentExceptionIfGivenThresholdValueIsOfNegativeValue_twoTests() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    double tmpThresholdValue = -0.1;
                    new CurationPipeline().withMaxMolecularMassFilter(tmpThresholdValue);
                }
        );
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    double tmpThresholdValue = -5.0;
                    new CurationPipeline().withMaxMolecularMassFilter(tmpThresholdValue);
                }
        );
    }

    //TODO: test .filter() method with MaxMolecularMassFilter?

    /**
     * Tests whether the value returned by the .withMinMolecularMassFilter() method of the class FilterPipeline with
     * MassComputationFlavours parameter is the FilterPipeline instance the method was called of.
     */
    @Test
    public void withMinMolecularMassFilterMethodTest_withFlavour_returnsFilterPipelineInstanceItself() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        double tmpThresholdValue = 10.0;
        MassComputationFlavours tmpFlavour = MassComputationFlavours.MolWeight;
        Assertions.assertInstanceOf(tmpCurationPipeline.getClass(), tmpCurationPipeline.withMinMolecularMassFilter(tmpThresholdValue, tmpFlavour));
    }

    /**
     * Tests whether the listOfSelectedFilters of the FilterPipeline instance returned by the .withMinMolecularMassFilter()
     * method of the class FilterPipeline with MassComputationFlavours parameter was extended by an instance of
     * MinMolecularMassFilter.
     */
    @Test
    public void withMinMolecularMassFilterMethodTest_withFlavour_listOfSelectedFiltersWasExtendedByInstanceOfMinMolecularMassFilter() {
        double tmpThresholdValue = 10.0;
        MassComputationFlavours tmpFlavour = MassComputationFlavours.MolWeight;
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withMinMolecularMassFilter(tmpThresholdValue, tmpFlavour);
        Assertions.assertInstanceOf(MinMolecularMassFilter.class, tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast());
    }

    /**
     * Tests whether the MinMolecularMassFilter added to the listOfSelectedFilters by the .withMinMolecularMassFilter()
     * method of the class FilterPipeline with MassComputationFlavours parameter has the given threshold value set to
     * it; two tests.
     */
    @Test
    public void withMinMolecularMassFilterMethodTest_withFlavour_newFilterHasGivenThresholdValue_twoTests() {
        double tmpThresholdValue = 10.0;
        MassComputationFlavours tmpFlavour = MassComputationFlavours.MolWeight;
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withMinMolecularMassFilter(tmpThresholdValue, tmpFlavour);
        Assertions.assertEquals(tmpThresholdValue, ((MinMolecularMassFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).getMinMolecularMass());
        tmpThresholdValue = 20.0;
        tmpCurationPipeline = new CurationPipeline().withMinMolecularMassFilter(tmpThresholdValue, tmpFlavour);
        Assertions.assertEquals(tmpThresholdValue, ((MinMolecularMassFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).getMinMolecularMass());
    }

    /**
     * Tests whether the MinMolecularMassFilter added to the listOfSelectedFilters by the .withMinMolecularMassFilter()
     * method of the class FilterPipeline with MassComputationFlavours parameter has the given mass computation flavour
     * set to it; two tests.
     */
    @Test
    public void withMinMolecularMassFilterMethodTest_withFlavour_newFilterHasGivenMassComputationFlavour_twoTests() {
        double tmpThresholdValue = 10.0;
        MassComputationFlavours tmpFlavour = MassComputationFlavours.MolWeight;
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withMinMolecularMassFilter(tmpThresholdValue, tmpFlavour);
        Assertions.assertEquals(tmpFlavour, ((MinMolecularMassFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).getMassComputationFlavour());
        tmpFlavour = MassComputationFlavours.MonoIsotopic;
        tmpCurationPipeline = new CurationPipeline().withMinMolecularMassFilter(tmpThresholdValue, tmpFlavour);
        Assertions.assertEquals(tmpFlavour, ((MinMolecularMassFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).getMassComputationFlavour());
    }

    /**
     * Tests whether the .withMinMolecularMassFilter() method of the class FilterPipeline with MassComputationFlavours
     * parameter throws an IllegalArgumentException if the given min molecular mass threshold value is of a negative
     * value; two tests.
     */
    @Test
    public void withMinMolecularMassFilterMethodTest_withFlavour_throwsIllegalArgumentExceptionIfGivenThresholdValueIsOfNegativeValue_twoTests() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    double tmpThresholdValue = -0.1;
                    MassComputationFlavours tmpFlavour = MassComputationFlavours.MolWeight;
                    new CurationPipeline().withMinMolecularMassFilter(tmpThresholdValue, tmpFlavour);
                }
        );
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    double tmpThresholdValue = -5.0;
                    MassComputationFlavours tmpFlavour = MassComputationFlavours.MolWeight;
                    new CurationPipeline().withMinMolecularMassFilter(tmpThresholdValue, tmpFlavour);
                }
        );
    }

    /**
     * Tests whether the .withMinMolecularMassFilter() method of the class FilterPipeline with MassComputationFlavours
     * parameter throws a NullPointerException if the given mass computation flavour is null.
     */
    @Test
    public void withMinMolecularMassFilterMethodTest_withFlavour_throwsNullPointerExceptionIfGivenFlavourIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    double tmpThresholdValue = 10.0;
                    MassComputationFlavours tmpFlavour = null;
                    new CurationPipeline().withMinMolecularMassFilter(tmpThresholdValue, tmpFlavour);
                }
        );
    }

    /**
     * Tests whether the value returned by the .withMinMolecularMassFilter() method of the class FilterPipeline with
     * no MassComputationFlavours parameter is the FilterPipeline instance the method was called of.
     */
    @Test
    public void withMinMolecularMassFilterMethodTest_noFlavour_returnsFilterPipelineInstanceItself() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        double tmpThresholdValue = 10.0;
        Assertions.assertInstanceOf(tmpCurationPipeline.getClass(), tmpCurationPipeline.withMinMolecularMassFilter(tmpThresholdValue));
    }

    /**
     * Tests whether the listOfSelectedFilters of the FilterPipeline instance returned by the .withMinMolecularMassFilter()
     * method of the class FilterPipeline with no MassComputationFlavours parameter was extended by an instance of
     * MinMolecularMassFilter.
     */
    @Test
    public void withMinMolecularMassFilterMethodTest_noFlavour_listOfSelectedFiltersWasExtendedByInstanceOfMinMolecularMassFilter() {
        double tmpThresholdValue = 10.0;
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withMinMolecularMassFilter(tmpThresholdValue);
        Assertions.assertInstanceOf(MinMolecularMassFilter.class, tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast());
    }

    /**
     * Tests whether the MinMolecularMassFilter added to the listOfSelectedFilters by the .withMinMolecularMassFilter()
     * method of the class FilterPipeline with no MassComputationFlavours parameter has the given threshold value set to
     * it; two tests.
     */
    @Test
    public void withMinMolecularMassFilterMethodTest_noFlavour_newFilterHasGivenThresholdValue_twoTests() {
        double tmpThresholdValue = 10.0;
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withMinMolecularMassFilter(tmpThresholdValue);
        Assertions.assertEquals(tmpThresholdValue, ((MinMolecularMassFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).getMinMolecularMass());
        tmpThresholdValue = 20.0;
        tmpCurationPipeline = new CurationPipeline().withMinMolecularMassFilter(tmpThresholdValue);
        Assertions.assertEquals(tmpThresholdValue, ((MinMolecularMassFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).getMinMolecularMass());
    }

    /**
     * Tests whether the MinMolecularMassFilter added to the listOfSelectedFilters by the .withMinMolecularMassFilter()
     * method of the class FilterPipeline with no MassComputationFlavours parameter has {@link MassComputationFlavours#MolWeight}
     * set as mass computation flavour.
     */
    @Test
    public void withMinMolecularMassFilterMethodTest_noFlavour_newFilterHasMolWeightAsMassComputationFlavour() {
        double tmpThresholdValue = 10.0;
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withMinMolecularMassFilter(tmpThresholdValue);
        Assertions.assertEquals(
                MassComputationFlavours.MolWeight,
                ((MinMolecularMassFilter) tmpCurationPipeline.getListOfSelectedProcessingSteps().getLast()).getMassComputationFlavour()
        );
    }

    /**
     * Tests whether the .withMinMolecularMassFilter() method of the class FilterPipeline with no MassComputationFlavours
     * parameter throws an IllegalArgumentException if the given min molecular mass threshold value is of a negative
     * value; two tests.
     */
    @Test
    public void withMinMolecularMassFilterMethodTest_noFlavour_throwsIllegalArgumentExceptionIfGivenThresholdValueIsOfNegativeValue_twoTests() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    double tmpThresholdValue = -0.1;
                    new CurationPipeline().withMinMolecularMassFilter(tmpThresholdValue);
                }
        );
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    double tmpThresholdValue = -5.0;
                    new CurationPipeline().withMinMolecularMassFilter(tmpThresholdValue);
                }
        );
    }

    //TODO: test .filter() method with MinMolecularMassFilter?

}
