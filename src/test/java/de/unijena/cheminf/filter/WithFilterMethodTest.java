package de.unijena.cheminf.filter;

import de.unijena.cheminf.IProcessingStep;
import de.unijena.cheminf.TestUtils;
import de.unijena.cheminf.filter.filters.MinAtomCountFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

/**
 * Test class of the FilterPipeline method .withFilter().
 */
public class WithFilterMethodTest {

    /**
     * Tests whether the instance returned by the .withFilter() method of the class FilterPipeline is not null.
     */
    @Test
    public void withFilterMethodTest_returnsNotNull() {
        IFilter tmpEmptyFilter = TestUtils.getAllTrueOrFalseFilter();
        Assertions.assertNotNull(new CurationPipeline().addProcessingStep(tmpEmptyFilter));
    }

    /**
     * Tests whether the return value of the .withFilter() method of class FilterPipeline is an instance of
     * FilterPipeline.
     */
    @Test
    public void withFilterMethodTest_returnsFilterPipelineInstance() {
        IFilter tmpEmptyFilter = TestUtils.getAllTrueOrFalseFilter();
        Assertions.assertInstanceOf(CurationPipeline.class, new CurationPipeline().addProcessingStep(tmpEmptyFilter));
    }

    /**
     * Tests whether the FilterPipeline instance returned by the .withFilter() method of class FilterPipeline is the
     * same as the one the method is called from.
     */
    @Test
    public void withFilterMethodTest_returnedFilterPipelineInstanceIsSameAsTheOneTheMethodIsCalledFrom() {
        IFilter tmpEmptyFilter = TestUtils.getAllTrueOrFalseFilter();
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        Assertions.assertSame(tmpCurationPipeline, tmpCurationPipeline.addProcessingStep(tmpEmptyFilter));
    }

    /** TODO
     * Tests whether the listOfSelectedFilters of the FilterPipeline instance returned by the .withFilter() method of
     * the class FilterPipeline is the same instance as before and was extended by one entry.
     */
    @Test
    @Disabled
    public void withFilterMethodTest_checksIfListOfSelectedFiltersIsTheSameAndWasExtendedByOne() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        LinkedList<IProcessingStep> tmpListOfSelectedFilters = tmpCurationPipeline.getListOfSelectedPipelineSteps();
        int tmpListInitialSize = tmpListOfSelectedFilters.size();
        IFilter tmpEmptyFilter = TestUtils.getAllTrueOrFalseFilter();
        tmpCurationPipeline.addProcessingStep(tmpEmptyFilter);
        Assertions.assertSame(tmpListOfSelectedFilters, tmpCurationPipeline.getListOfSelectedPipelineSteps());
        Assertions.assertEquals(tmpListInitialSize + 1, tmpCurationPipeline.getListOfSelectedPipelineSteps().size());
    }

    /**
     * Tests whether the listOfSelectedFilters of the FilterPipeline instance returned by the .withFilter() method of
     * the class FilterPipeline was extended by the given IFilter instance.
     */
    @Test
    public void withFilterMethodTest_checksIfListOfSelectedFiltersWasExtendedByGivenFilterInstance_test1() {
        IFilter tmpEmptyFilter = TestUtils.getAllTrueOrFalseFilter();
        CurationPipeline tmpCurationPipeline = new CurationPipeline().addProcessingStep(tmpEmptyFilter);
        Assertions.assertInstanceOf(IFilter.class, tmpCurationPipeline.getListOfSelectedPipelineSteps().getLast());
        Assertions.assertSame(tmpEmptyFilter, tmpCurationPipeline.getListOfSelectedPipelineSteps().getLast());
    }

    /**
     * Tests whether the listOfSelectedFilters of the FilterPipeline instance returned by the .withFilter() method of
     * the class FilterPipeline was extended by the given IFilter instance.
     */
    @Test
    public void withFilterMethodTest_checksIfListOfSelectedFiltersWasExtendedByGivenFilterInstance_test2() {
        int tmpIntegerParameter = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        IFilter tmpNewFilter = new MinAtomCountFilter(tmpIntegerParameter, tmpConsiderImplicitHydrogens);
        CurationPipeline tmpCurationPipeline = new CurationPipeline().addProcessingStep(tmpNewFilter);
        Assertions.assertInstanceOf(IFilter.class, tmpCurationPipeline.getListOfSelectedPipelineSteps().getLast());
        Assertions.assertSame(tmpNewFilter, tmpCurationPipeline.getListOfSelectedPipelineSteps().getLast());
    }

    /**
     * Tests whether the .withFilter() method of the class FilterPipeline throws a NullPointerException if the given
     * IFilter instance is null.
     */
    @Test
    public void withFilterMethodTest_throwsNullPointerExceptionIfGivenFilterInstanceIsNull() {    //TODO: do so?
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    new CurationPipeline().addProcessingStep(null);
                }
        );
    }

    /**
     * Tests whether the list of selected filters is extended by two entries if the .withFilter() method of the class
     * FilterPipeline is called twice.
     */
    @Test
    public void withFilterMethodTest_combiningTwoFilters_twoFiltersAreAddedToListOfSelectedFilters() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        int tmpInitialListSize = tmpCurationPipeline.getListOfSelectedPipelineSteps().size();
        BaseFilter tmpEmptyFilter = TestUtils.getAllTrueOrFalseFilter();
        tmpCurationPipeline = tmpCurationPipeline.addProcessingStep(tmpEmptyFilter).addProcessingStep(tmpEmptyFilter);
        Assertions.assertEquals(tmpInitialListSize + 2, tmpCurationPipeline.getListOfSelectedPipelineSteps().size());
    }

    /**
     * Tests whether the list of selected filters is extended by the two specific IFilter instances in the correct order
     * if the .withFilter() method of the class FilterPipeline is called twice.
     */
    @Test
    public void withFilterMethodTest_combiningTwoFilters_bothSpecificIFilterInstancesAreAddedToListOfSelectedFiltersInCorrectOrder() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        IFilter tmpFilter1 = TestUtils.getAllTrueOrFalseFilter();
        IFilter tmpFilter2 = TestUtils.getAllTrueOrFalseFilter();
        tmpCurationPipeline.addProcessingStep(tmpFilter1).addProcessingStep(tmpFilter2);
        Assertions.assertSame(tmpFilter1, tmpCurationPipeline.getListOfSelectedPipelineSteps().get(0));
        Assertions.assertSame(tmpFilter2, tmpCurationPipeline.getListOfSelectedPipelineSteps().get(1));
    }

    /**
     * Tests whether the list of selected filters is extended by the respective number of entries if the .withFilter()
     * method of the class FilterPipeline is called multiple.
     */
    @Test
    public void withFilterMethodTest_combiningMultipleFilters_5_listOfSelectedFiltersIsExtendedByGivenFilterCount() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        int tmpInitialListSize = tmpCurationPipeline.getListOfSelectedPipelineSteps().size();
        IFilter tmpEmptyFilter = TestUtils.getAllTrueOrFalseFilter();
        tmpCurationPipeline.addProcessingStep(tmpEmptyFilter).addProcessingStep(tmpEmptyFilter).addProcessingStep(tmpEmptyFilter)
                .addProcessingStep(tmpEmptyFilter).addProcessingStep(tmpEmptyFilter);
        Assertions.assertEquals(tmpInitialListSize + 5, tmpCurationPipeline.getListOfSelectedPipelineSteps().size());
    }

    /**
     * Tests whether the list of selected filters is extended by the specific IFilter instances in the correct order
     * if the .withFilter() method of the class FilterPipeline is called multiple times.
     */
    @Test
    public void withFilterMethodTest_combiningMultipleFilters_5_listOfSelectedFiltersIsExtendedBySpecificIFiltersInCorrectOrder() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        IFilter[] tmpFilterArray = new IFilter[5];
        for (int i = 0; i < tmpFilterArray.length; i++) {
            tmpFilterArray[i] = TestUtils.getAllTrueOrFalseFilter();
            tmpCurationPipeline.addProcessingStep(tmpFilterArray[i] = TestUtils.getAllTrueOrFalseFilter());
        }
        for (int i = 0; i < tmpCurationPipeline.getListOfSelectedPipelineSteps().size(); i++) {
            Assertions.assertSame(tmpFilterArray[i], tmpCurationPipeline.getListOfSelectedPipelineSteps().get(i));
        }
    }

}
