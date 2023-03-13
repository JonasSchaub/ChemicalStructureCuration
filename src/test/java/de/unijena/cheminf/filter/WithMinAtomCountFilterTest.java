package de.unijena.cheminf.filter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test class for the .withMinAtomCountFilter() method of class FilterPipeline.
 */
public class WithMinAtomCountFilterTest {

    //TODO: test everything with boolean aConsiderImplicitHydrogens = false?

    /**
     * Tests whether the instance returned by the .withMinAtomCountFilter() method of the class FilterPipeline is not
     * null.
     */
    @Test
    public void withMinAtomCountFilterMethodTest_returnsNotNull() {
        int tmpIntegerParameter = 0;
        boolean tmpBooleanParameter = true;
        Assertions.assertNotNull(new FilterPipeline().withMinAtomCountFilter(tmpIntegerParameter, tmpBooleanParameter));
    }

    /**
     * Tests whether the return value of the .withMinAtomCountFilter() method of class FilterPipeline is an instance of
     * FilterPipeline.
     */
    @Test
    public void withMinAtomCountFilterMethodTest_returnsFilterInstance() {
        int tmpIntegerParameter = 0;
        boolean tmpBooleanParameter = true;
        Assertions.assertInstanceOf(FilterPipeline.class, new FilterPipeline().withMinAtomCountFilter(tmpIntegerParameter, tmpBooleanParameter));
    }

    /**
     * Tests whether the listOfSelectedFilters of the FilterPipeline instance returned by the .withMinAtomCountFilter()
     * method of the class FilterPipeline is the same as the one of the original FilterPipeline instance.
     */
    @Test
    public void withMinAtomCountFilterMethodTest_returnedFilterContainsSameListOfSelectedFiltersAsOriginal() {
        FilterPipeline tmpOriginalFilterPipeline = new FilterPipeline();
        FilterPipeline tmpReturnedFilterPipeline = tmpOriginalFilterPipeline.withMinAtomCountFilter(0, true);
        Assertions.assertSame(tmpOriginalFilterPipeline.listOfSelectedFilters, tmpReturnedFilterPipeline.listOfSelectedFilters);
    }

    /**
     * Tests whether the listOfSelectedFilters of the FilterPipeline instance returned by the .withMinAtomCountFilter()
     * method of the class FilterPipeline was extended by one entry.
     */
    @Test
    public void withMinAtomCountFilterMethodTest_checksIfListOfSelectedFiltersWasExtendedByOne() {
        FilterPipeline tmpOriginalFilterPipeline = new FilterPipeline();
        int tmpListInitialSize = tmpOriginalFilterPipeline.listOfSelectedFilters.size();
        FilterPipeline tmpReturnedFilterPipeline = tmpOriginalFilterPipeline.withMinAtomCountFilter(0, true);
        Assertions.assertEquals(tmpListInitialSize + 1, tmpReturnedFilterPipeline.listOfSelectedFilters.size());
    }

    /**
     * Tests whether the listOfSelectedFilters of the FilterPipeline instance returned by the .withMinAtomCountFilter()
     * method of the class FilterPipeline was extended by the specific filter type.
     */
    @Test
    public void withMinAtomCountFilterMethodTest_considerImplicitHydrogens_checksIfListOfSelectedFiltersWasExtendedBySpecificFilterType() {
        FilterPipeline.FilterTypes tmpFilterType = FilterPipeline.FilterTypes.MIN_ATOM_COUNT_FILTER_CONSIDER_IMPLICIT_HYDROGENS;
        boolean tmpConsiderImplicitHydrogens = true;
        FilterPipeline tmpReturnedFilterPipeline = new FilterPipeline().withMinAtomCountFilter(0, tmpConsiderImplicitHydrogens);
        Assertions.assertSame(tmpFilterType, tmpReturnedFilterPipeline.listOfSelectedFilters.getLast());
    }

    /**
     * Tests whether the listOfSelectedFilters of the FilterPipeline instance returned by the .withMinAtomCountFilter()
     * method of the class FilterPipeline was extended by the specific filter type.
     */
    @Test
    public void withMinAtomCountFilterMethodTest_notConsiderImplicitHydrogens_checksIfListOfSelectedFiltersWasExtendedBySpecificFilterType() {
        FilterPipeline.FilterTypes tmpFilterType = FilterPipeline.FilterTypes.MIN_ATOM_COUNT_FILTER_NOT_CONSIDER_IMPLICIT_HYDROGENS;
        boolean tmpConsiderImplicitHydrogens = false;
        FilterPipeline tmpReturnedFilterPipeline = new FilterPipeline().withMinAtomCountFilter(0, tmpConsiderImplicitHydrogens);
        Assertions.assertSame(tmpFilterType, tmpReturnedFilterPipeline.listOfSelectedFilters.getLast());
    }

    /**
     * Tests whether the listOfFilterParameters of the FilterPipeline instance returned by the .withMinAtomCountFilter()
     * method of the class FilterPipeline is the same as the one of the original FilterPipeline instance.
     */
    @Test
    public void withMinAtomCountFilterMethodTest_returnedFilterContainsSameListOfFilterParametersAsOriginal() {
        FilterPipeline tmpOriginalFilterPipeline = new FilterPipeline();
        FilterPipeline tmpReturnedFilterPipeline = tmpOriginalFilterPipeline.withMinAtomCountFilter(0, true);
        Assertions.assertSame(tmpOriginalFilterPipeline.listOfFilterParameters, tmpReturnedFilterPipeline.listOfFilterParameters);
    }

    /**
     * Tests whether the listOfFilterParameters of the FilterPipeline instance returned by the .withMinAtomCountFilter()
     * method of the class FilterPipeline was extended by one entry.
     */
    @Test
    public void withMinAtomCountFilterMethodTest_checksIfListOfFilterParametersWasExtendedByOne() {
        FilterPipeline tmpOriginalFilterPipeline = new FilterPipeline();
        int tmpListInitialSize = tmpOriginalFilterPipeline.listOfFilterParameters.size();
        FilterPipeline tmpReturnedFilterPipeline = tmpOriginalFilterPipeline.withMinAtomCountFilter(0, true);
        Assertions.assertEquals(tmpListInitialSize + 1, tmpReturnedFilterPipeline.listOfFilterParameters.size());
    }

    /**
     * Tests whether the listOfFilterParameters of the FilterPipeline instance returned by the .withMinAtomCountFilter()
     * method of the class FilterPipeline was extended by the given integer parameter.
     */
    @Test
    public void withMinAtomCountFilterMethodTest_checksIfListOfFilterParametersWasExtendedByGivenIntegerParameter() {
        int tmpIntegerParameter = 0;
        FilterPipeline tmpReturnedFilterPipeline = new FilterPipeline().withMinAtomCountFilter(tmpIntegerParameter, true);
        Assertions.assertSame(tmpIntegerParameter, tmpReturnedFilterPipeline.listOfFilterParameters.getLast());
        Assertions.assertEquals(tmpIntegerParameter, tmpReturnedFilterPipeline.listOfFilterParameters.getLast());
    }

    /**
     * Tests whether the .withMinAtomCountFilter() method of the class FilterPipeline throws an IllegalArgumentException
     * if the given integer parameter is of a negative value.
     */
    @Test
    public void withMinAtomCountFilterMethodTest_throwsIllegalArgumentExceptionIfGivenMinAtomCountIsNegative() {    //TODO: do so?
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    int tmpNegativeMinAtomCount = -1;
                    new FilterPipeline().withMinAtomCountFilter(tmpNegativeMinAtomCount, true);
                }
        );
    }

}
