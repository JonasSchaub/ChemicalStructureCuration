package de.unijena.cheminf.filter;

import de.unijena.cheminf.TestUtils;
import de.unijena.cheminf.filter.filters.MinAtomCountFilter;
import org.junit.jupiter.api.Assertions;
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
        Assertions.assertNotNull(new FilterPipeline().withFilter(tmpEmptyFilter));
    }

    /**
     * Tests whether the return value of the .withFilter() method of class FilterPipeline is an instance of
     * FilterPipeline.
     */
    @Test
    public void withFilterMethodTest_returnsFilterPipelineInstance() {
        IFilter tmpEmptyFilter = TestUtils.getAllTrueOrFalseFilter();
        Assertions.assertInstanceOf(FilterPipeline.class, new FilterPipeline().withFilter(tmpEmptyFilter));
    }

    /**
     * Tests whether the FilterPipeline instance returned by the .withFilter() method of class FilterPipeline is the
     * same as the one the method is called from.
     */
    @Test
    public void withFilterMethodTest_returnedFilterPipelineInstanceIsSameAsTheOneTheMethodIsCalledFrom() {
        IFilter tmpEmptyFilter = TestUtils.getAllTrueOrFalseFilter();
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        Assertions.assertSame(tmpFilterPipeline, tmpFilterPipeline.withFilter(tmpEmptyFilter));
    }

    /**
     * Tests whether the listOfSelectedFilters of the FilterPipeline instance returned by the .withFilter() method of
     * the class FilterPipeline is the same instance as before and was extended by one entry.
     */
    @Test
    public void withFilterMethodTest_checksIfListOfSelectedFiltersIsTheSameAndWasExtendedByOne() {
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        LinkedList<IFilter> tmpListOfSelectedFilters = tmpFilterPipeline.getListOfSelectedFilters();
        int tmpListInitialSize = tmpListOfSelectedFilters.size();
        IFilter tmpEmptyFilter = TestUtils.getAllTrueOrFalseFilter();
        tmpFilterPipeline.withFilter(tmpEmptyFilter);
        Assertions.assertSame(tmpListOfSelectedFilters, tmpFilterPipeline.getListOfSelectedFilters());
        Assertions.assertEquals(tmpListInitialSize + 1, tmpFilterPipeline.getListOfSelectedFilters().size());
    }

    /**
     * Tests whether the listOfSelectedFilters of the FilterPipeline instance returned by the .withFilter() method of
     * the class FilterPipeline was extended by the given IFilter instance.
     */
    @Test
    public void withFilterMethodTest_checksIfListOfSelectedFiltersWasExtendedByGivenFilterInstance_test1() {
        IFilter tmpEmptyFilter = TestUtils.getAllTrueOrFalseFilter();
        FilterPipeline tmpFilterPipeline = new FilterPipeline().withFilter(tmpEmptyFilter);
        Assertions.assertInstanceOf(IFilter.class, tmpFilterPipeline.getListOfSelectedFilters().getLast());
        Assertions.assertSame(tmpEmptyFilter, tmpFilterPipeline.getListOfSelectedFilters().getLast());
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
        FilterPipeline tmpFilterPipeline = new FilterPipeline().withFilter(tmpNewFilter);
        Assertions.assertInstanceOf(IFilter.class, tmpFilterPipeline.getListOfSelectedFilters().getLast());
        Assertions.assertSame(tmpNewFilter, tmpFilterPipeline.getListOfSelectedFilters().getLast());
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
                    new FilterPipeline().withFilter(null);
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
        FilterBase tmpEmptyFilter = TestUtils.getAllTrueOrFalseFilter();
        tmpFilterPipeline = tmpFilterPipeline.withFilter(tmpEmptyFilter).withFilter(tmpEmptyFilter);
        Assertions.assertEquals(tmpInitialListSize + 2, tmpFilterPipeline.getListOfSelectedFilters().size());
    }

    /**
     * Tests whether the list of selected filters is extended by the two specific IFilter instances in the correct order
     * if the .withFilter() method of the class FilterPipeline is called twice.
     */
    @Test
    public void withFilterMethodTest_combiningTwoFilters_bothSpecificIFilterInstancesAreAddedToListOfSelectedFiltersInCorrectOrder() {
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        IFilter tmpFilter1 = TestUtils.getAllTrueOrFalseFilter();
        IFilter tmpFilter2 = TestUtils.getAllTrueOrFalseFilter();
        tmpFilterPipeline.withFilter(tmpFilter1).withFilter(tmpFilter2);
        Assertions.assertSame(tmpFilter1, tmpFilterPipeline.getListOfSelectedFilters().get(0));
        Assertions.assertSame(tmpFilter2, tmpFilterPipeline.getListOfSelectedFilters().get(1));
    }

    /**
     * Tests whether the list of selected filters is extended by the respective number of entries if the .withFilter()
     * method of the class FilterPipeline is called multiple.
     */
    @Test
    public void withFilterMethodTest_combiningMultipleFilters_5_listOfSelectedFiltersIsExtendedByGivenFilterCount() {
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        int tmpInitialListSize = tmpFilterPipeline.getListOfSelectedFilters().size();
        IFilter tmpEmptyFilter = TestUtils.getAllTrueOrFalseFilter();
        tmpFilterPipeline.withFilter(tmpEmptyFilter).withFilter(tmpEmptyFilter).withFilter(tmpEmptyFilter)
                .withFilter(tmpEmptyFilter).withFilter(tmpEmptyFilter);
        Assertions.assertEquals(tmpInitialListSize + 5, tmpFilterPipeline.getListOfSelectedFilters().size());
    }

    /**
     * Tests whether the list of selected filters is extended by the specific IFilter instances in the correct order
     * if the .withFilter() method of the class FilterPipeline is called multiple times.
     */
    @Test
    public void withFilterMethodTest_combiningMultipleFilters_5_listOfSelectedFiltersIsExtendedBySpecificIFiltersInCorrectOrder() {
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        IFilter[] tmpFilterArray = new IFilter[5];
        for (int i = 0; i < tmpFilterArray.length; i++) {
            tmpFilterArray[i] = TestUtils.getAllTrueOrFalseFilter();
            tmpFilterPipeline.withFilter(tmpFilterArray[i] = TestUtils.getAllTrueOrFalseFilter());
        }
        for (int i = 0; i < tmpFilterPipeline.getListOfSelectedFilters().size(); i++) {
            Assertions.assertSame(tmpFilterArray[i], tmpFilterPipeline.getListOfSelectedFilters().get(i));
        }
    }

}
