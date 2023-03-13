package de.unijena.cheminf.filter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test class for the .withMaxAtomCountFilter() method of class FilterPipeline.
 */
public class WithMaxAtomCountFilterTest {

    //TODO: test everything with boolean aConsiderImplicitHydrogens = false?

    /**
     * Tests whether the instance returned by the .withMaxAtomCountFilter() method of the class FilterPipeline is not
     * null.
     */
    @Test
    public void withMaxAtomCountFilterMethodTest_returnsNotNull() {
        int tmpIntegerParameter = 10;
        boolean tmpBooleanParameter = true;
        Assertions.assertNotNull(new FilterPipeline().withMaxAtomCountFilter(tmpIntegerParameter, tmpBooleanParameter));
    }

    /*@Test     //e.g. for testing everything for both boolean parameter values
    public void withMaxAtomCountFilterMethodTest_considerImplicitHydrogens_returnsNotNull() {
        this.withMaxAtomCountFilterMethodTest_returnsNotNull(true);
    }
    @Test
    public void withMaxAtomCountFilterMethodTest_notConsiderImplicitHydrogens_returnsNotNull() {
        this.withMaxAtomCountFilterMethodTest_returnsNotNull(false);
    }
    private void withMaxAtomCountFilterMethodTest_returnsNotNull(boolean aConsiderImplicitHydrogens) {
        int tmpIntegerParameter = 10;
        boolean tmpBooleanParameter = aConsiderImplicitHydrogens;
        Assertions.assertNotNull(new Filter().withMaxAtomCountFilter(tmpIntegerParameter, tmpBooleanParameter));
    }*/

    /**
     * Tests whether the return value of the .withMaxAtomCountFilter() method of class FilterPipeline is an instance of
     * FilterPipeline.
     */
    @Test
    public void withMaxAtomCountFilterMethodTest_returnsFilterInstance() {
        int tmpIntegerParameter = 10;
        boolean tmpBooleanParameter = true;
        Assertions.assertInstanceOf(FilterPipeline.class, new FilterPipeline().withMaxAtomCountFilter(tmpIntegerParameter, tmpBooleanParameter));
    }

    /**
     * Tests whether the listOfSelectedFilters of the Filter instance returned by the .withMaxAtomCountFilter() method
     * of the class FilterPipeline is the same as the one of the original FilterPipeline instance.
     */
    @Test
    public void withMaxAtomCountFilterMethodTest_returnedFilterContainsSameListOfSelectedFiltersAsOriginal() {
        FilterPipeline tmpOriginalFilterPipeline = new FilterPipeline();
        FilterPipeline tmpReturnedFilterPipeline = tmpOriginalFilterPipeline.withMaxAtomCountFilter(10, true);
        Assertions.assertSame(tmpOriginalFilterPipeline.listOfSelectedFilters, tmpReturnedFilterPipeline.listOfSelectedFilters);
    }

    /**
     * Tests whether the listOfSelectedFilters of the FilterPipeline instance returned by the .withMaxAtomCountFilter()
     * method of the class FilterPipeline was extended by one entry.
     */
    @Test
    public void withMaxAtomCountFilterMethodTest_checksIfListOfSelectedFiltersWasExtendedByOne() {
        FilterPipeline tmpOriginalFilterPipeline = new FilterPipeline();
        int tmpListInitialSize = tmpOriginalFilterPipeline.listOfSelectedFilters.size();
        FilterPipeline tmpReturnedFilterPipeline = tmpOriginalFilterPipeline.withMaxAtomCountFilter(10, true);
        Assertions.assertEquals(tmpListInitialSize + 1, tmpReturnedFilterPipeline.listOfSelectedFilters.size());
    }

    /**
     * Tests whether the listOfSelectedFilters of the FilterPipeline instance returned by the .withMaxAtomCountFilter()
     * method of the class FilterPipeline was extended by the specific filter type.
     */
    @Test
    public void withMaxAtomCountFilterMethodTest_considerImplicitHydrogens_checksIfListOfSelectedFiltersWasExtendedBySpecificFilterType() {
        FilterPipeline.FilterTypes tmpFilterType = FilterPipeline.FilterTypes.MAX_ATOM_COUNT_FILTER_CONSIDER_IMPLICIT_HYDROGENS;
        boolean tmpConsiderImplicitHydrogens = true;
        FilterPipeline tmpReturnedFilterPipeline = new FilterPipeline().withMaxAtomCountFilter(10, tmpConsiderImplicitHydrogens);
        Assertions.assertSame(tmpFilterType, tmpReturnedFilterPipeline.listOfSelectedFilters.getLast());
    }

    /**
     * Tests whether the listOfSelectedFilters of the FilterPipeline instance returned by the .withMaxAtomCountFilter()
     * method of the class FilterPipeline was extended by the specific filter type.
     */
    @Test
    public void withMaxAtomCountFilterMethodTest_notConsiderImplicitHydrogens_checksIfListOfSelectedFiltersWasExtendedBySpecificFilterType() {
        FilterPipeline.FilterTypes tmpFilterType = FilterPipeline.FilterTypes.MAX_ATOM_COUNT_FILTER_NOT_CONSIDER_IMPLICIT_HYDROGENS;
        boolean tmpConsiderImplicitHydrogens = false;
        FilterPipeline tmpReturnedFilterPipeline = new FilterPipeline().withMaxAtomCountFilter(10, tmpConsiderImplicitHydrogens);
        Assertions.assertSame(tmpFilterType, tmpReturnedFilterPipeline.listOfSelectedFilters.getLast());
    }

    /**
     * Tests whether the listOfFilterParameters of the FilterPipeline instance returned by the .withMaxAtomCountFilter()
     * method of the class FilterPipeline is the same as the one of the original Filter instance.
     */
    @Test
    public void withMaxAtomCountFilterMethodTest_returnedFilterContainsSameListOfFilterParametersAsOriginal() {
        FilterPipeline tmpOriginalFilterPipeline = new FilterPipeline();
        FilterPipeline tmpReturnedFilterPipeline = tmpOriginalFilterPipeline.withMaxAtomCountFilter(10, true);
        Assertions.assertSame(tmpOriginalFilterPipeline.listOfFilterParameters, tmpReturnedFilterPipeline.listOfFilterParameters);
    }

    /**
     * Tests whether the listOfFilterParameters of the FilterPipeline instance returned by the .withMaxAtomCountFilter()
     * method of the class FilterPipeline was extended by one entry.
     */
    @Test
    public void withMaxAtomCountFilterMethodTest_checksIfListOfFilterParametersWasExtendedByOne() {
        FilterPipeline tmpOriginalFilterPipeline = new FilterPipeline();
        int tmpListInitialSize = tmpOriginalFilterPipeline.listOfFilterParameters.size();
        FilterPipeline tmpReturnedFilterPipeline = tmpOriginalFilterPipeline.withMaxAtomCountFilter(10, true);
        Assertions.assertEquals(tmpListInitialSize + 1, tmpReturnedFilterPipeline.listOfFilterParameters.size());
    }

    /**
     * Tests whether the listOfFilterParameters of the FilterPipeline instance returned by the .withMaxAtomCountFilter()
     * method of the class FilterPipeline was extended by the given integer parameter.
     */
    @Test
    public void withMaxAtomCountFilterMethodTest_checksIfListOfFilterParametersWasExtendedByGivenIntegerParameter() {
        int tmpMaxAtomCount = 10;
        FilterPipeline tmpReturnedFilterPipeline = new FilterPipeline().withMaxAtomCountFilter(tmpMaxAtomCount, true);
        Assertions.assertSame(tmpMaxAtomCount, tmpReturnedFilterPipeline.listOfFilterParameters.getLast());
        Assertions.assertEquals(tmpMaxAtomCount, tmpReturnedFilterPipeline.listOfFilterParameters.getLast());
    }

    /**
     * Tests whether the .withMaxAtomCountFilter() method of the class FilterPipeline throws an IllegalArgumentException
     * if the given integer parameter is of a negative value.
     */
    @Test
    public void withMaxAtomCountFilterMethodTest_throwsIllegalArgumentExceptionIfGivenMaxAtomCountIsNegative() {    //TODO: do so?
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    int tmpNegativeMaxAtomCount = -1;
                    new FilterPipeline().withMaxAtomCountFilter(tmpNegativeMaxAtomCount, true);
                }
        );
    }

}
