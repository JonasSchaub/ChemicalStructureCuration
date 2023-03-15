package de.unijena.cheminf.filter;

import de.unijena.cheminf.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

public class WithFilterMethodTest {

    /**
     * Tests whether the instance returned by the .withFilter() method of the class FilterPipeline is not null.
     */
    @Test
    public void withFilterMethodTest_returnsNotNull() {
        Filter tmpEmptyFilter = TestUtils.getAllTrueOrFalseFilter();
        Assertions.assertNotNull(new FilterPipeline().withFilter(tmpEmptyFilter));
    }

    /**
     * Tests whether the return value of the .withFilter() method of class FilterPipeline is an instance of
     * FilterPipeline.
     */
    @Test
    public void withFilterMethodTest_returnsFilterPipelineInstance() {
        Filter tmpEmptyFilter = TestUtils.getAllTrueOrFalseFilter();
        Assertions.assertInstanceOf(FilterPipeline.class, new FilterPipeline().withFilter(tmpEmptyFilter));
    }

    /**
     * Tests whether the FilterPipeline instance returned by the .withFilter() method of class FilterPipeline is the
     * same as the one the method is called from.
     */
    @Test
    public void withFilterMethodTest_returnedFilterPipelineInstanceIsSameAsTheOneTheMethodIsCalledFrom() {
        Filter tmpEmptyFilter = TestUtils.getAllTrueOrFalseFilter();
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
        LinkedList<Filter> tmpListOfSelectedFilters = tmpFilterPipeline.getListOfSelectedFilters();
        int tmpListInitialSize = tmpListOfSelectedFilters.size();
        Filter tmpEmptyFilter = TestUtils.getAllTrueOrFalseFilter();
        tmpFilterPipeline.withFilter(tmpEmptyFilter);
        Assertions.assertSame(tmpListOfSelectedFilters, tmpFilterPipeline.getListOfSelectedFilters());
        Assertions.assertEquals(tmpListInitialSize + 1, tmpFilterPipeline.getListOfSelectedFilters().size());
    }

    /**
     * Tests whether the listOfSelectedFilters of the FilterPipeline instance returned by the .withFilter() method of
     * the class FilterPipeline was extended by the given Filter instance.
     */
    @Test
    public void withFilterMethodTest_checksIfListOfSelectedFiltersWasExtendedByGivenFilterInstance() {
        Filter tmpEmptyFilter = TestUtils.getAllTrueOrFalseFilter();
        FilterPipeline tmpFilterPipeline = new FilterPipeline().withFilter(tmpEmptyFilter);
        Assertions.assertSame(tmpEmptyFilter, tmpFilterPipeline.getListOfSelectedFilters().getLast());
    }

    /**
     * Tests whether the listOfSelectedFilters of the FilterPipeline instance returned by the .withFilter() method of
     * the class FilterPipeline was extended by the given Filter instance.
     */
    @Test
    public void withFilterMethodTest_checksIfListOfSelectedFiltersWasExtendedByGivenFilterInstance_test2() {
        int tmpIntegerParameter = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        FilterPipeline tmpFilterPipeline = new FilterPipeline().withFilter(new MinAtomCountFilter(tmpIntegerParameter, tmpConsiderImplicitHydrogens));
        Assertions.assertInstanceOf(MinAtomCountFilter.class, tmpFilterPipeline.getListOfSelectedFilters().getLast());
    }

    /**
     * Tests whether the .withFilter() method of the class FilterPipeline throws a NullPointerException if the given
     * Filter instance is null.
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
        Filter tmpEmptyFilter = TestUtils.getAllTrueOrFalseFilter();
        tmpFilterPipeline = tmpFilterPipeline.withFilter(tmpEmptyFilter).withFilter(tmpEmptyFilter);
        Assertions.assertEquals(tmpInitialListSize + 2, tmpFilterPipeline.getListOfSelectedFilters().size());
    }

    /**
     * Tests whether the list of selected filters is extended by the two specific Filter instances in the correct order
     * if the .withFilter() method of the class FilterPipeline is called twice.
     */
    @Test
    public void withFilterMethodTest_combiningTwoFilters_bothSpecificFiltersAreAddedToListOfSelectedFiltersInCorrectOrder() {
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        Filter tmpFilter1 = TestUtils.getAllTrueOrFalseFilter();
        Filter tmpFilter2 = TestUtils.getAllTrueOrFalseFilter();
        tmpFilterPipeline.withFilter(tmpFilter1).withFilter(tmpFilter2);
        Assertions.assertEquals(tmpFilter1, tmpFilterPipeline.getListOfSelectedFilters().get(0));
        Assertions.assertEquals(tmpFilter2, tmpFilterPipeline.getListOfSelectedFilters().get(1));
    }

    /**
     * Tests whether the list of selected filters is extended by the respective count of entries if the .withFilter()
     * method of the class FilterPipeline is called multiple.
     */
    @Test
    public void withFilterMethodTest_combiningMultipleFilters_5_listOfSelectedFiltersIsExtendedByGivenFilterCount() {
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        int tmpInitialListSize = tmpFilterPipeline.getListOfSelectedFilters().size();
        Filter tmpEmptyFilter = TestUtils.getAllTrueOrFalseFilter();
        tmpFilterPipeline.withFilter(tmpEmptyFilter).withFilter(tmpEmptyFilter).withFilter(tmpEmptyFilter)
                .withFilter(tmpEmptyFilter).withFilter(tmpEmptyFilter);
        Assertions.assertEquals(tmpInitialListSize + 5, tmpFilterPipeline.getListOfSelectedFilters().size());
    }

    /**
     * Tests whether the list of selected filters is extended by the specific Filter instances in the correct order
     * if the .withFilter() method of the class FilterPipeline is called multiple times.
     */
    @Test
    public void withFilterMethodTest_combiningMultipleFilters_5_listOfSelectedFiltersIsExtendedBySpecificFiltersInCorrectOrder() {
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        Filter[] tmpFilterArray = new Filter[5];
        for (int i = 0; i < tmpFilterArray.length; i++) {
            tmpFilterArray[i] = TestUtils.getAllTrueOrFalseFilter();
            tmpFilterPipeline.withFilter(tmpFilterArray[i] = TestUtils.getAllTrueOrFalseFilter());
        }
        for (int i = 0; i < tmpFilterPipeline.getListOfSelectedFilters().size(); i++) {
            Assertions.assertSame(tmpFilterArray[i], tmpFilterPipeline.getListOfSelectedFilters().get(i));
        }
    }

}