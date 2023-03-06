package de.unijena.cheminf.filter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 */
public class WithMinAtomCountFilterTest {

    //TODO: test everything with boolean aConsiderImplicitHydrogens = false?

    /**
     * Tests whether the instance returned by the .withMinAtomCountFilter() method of the class Filter is not null.
     */
    @Test
    public void withMinAtomCountFilterMethodTest_returnsNotNull() {
        int tmpIntegerParameter = 0;
        boolean tmpBooleanParameter = true;
        Assertions.assertNotNull(new Filter().withMinAtomCountFilter(tmpIntegerParameter, tmpBooleanParameter));
    }

    /**
     * Tests whether the return value of the .withMinAtomCountFilter() method of class Filter is an instance of Filter.
     */
    @Test
    public void withMinAtomCountFilterMethodTest_returnsFilterInstance() {
        int tmpIntegerParameter = 0;
        boolean tmpBooleanParameter = true;
        Assertions.assertInstanceOf(Filter.class, new Filter().withMinAtomCountFilter(tmpIntegerParameter, tmpBooleanParameter));
    }

    /**
     * Tests whether the listOfSelectedFilters of the Filter instance returned by the .withMinAtomCountFilter() method
     * of the class Filter is the same as the one of the original Filter instance.
     */
    @Test
    public void withMinAtomCountFilterMethodTest_returnedFilterContainsSameListOfSelectedFiltersAsOriginal() {
        Filter tmpOriginalFilter = new Filter();
        Filter tmpReturnedFilter = tmpOriginalFilter.withMinAtomCountFilter(0, true);
        Assertions.assertSame(tmpOriginalFilter.listOfSelectedFilters, tmpReturnedFilter.listOfSelectedFilters);
    }

    /**
     * Tests whether the listOfSelectedFilters of the Filter instance returned by the .withMinAtomCountFilter() method
     * of the class Filter was extended by one entry.
     */
    @Test
    public void withMinAtomCountFilterMethodTest_checksIfListOfSelectedFiltersWasExtendedByOne() {
        Filter tmpOriginalFilter = new Filter();
        int tmpListInitialSize = tmpOriginalFilter.listOfSelectedFilters.size();
        Filter tmpReturnedFilter = tmpOriginalFilter.withMinAtomCountFilter(0, true);
        Assertions.assertEquals(tmpListInitialSize + 1, tmpReturnedFilter.listOfSelectedFilters.size());
    }

    /**
     * Tests whether the listOfSelectedFilters of the Filter instance returned by the .withMinAtomCountFilter() method
     * of the class Filter was extended by the specific filter type.
     */
    @Test
    public void withMinAtomCountFilterMethodTest_considerImplicitHydrogens_checksIfListOfSelectedFiltersWasExtendedBySpecificFilterType() {
        Filter.FilterTypes tmpFilterType = Filter.FilterTypes.MIN_ATOM_COUNT_FILTER_CONSIDER_IMPLICIT_HYDROGENS;
        boolean tmpConsiderImplicitHydrogens = true;
        Filter tmpReturnedFilter = new Filter().withMinAtomCountFilter(0, tmpConsiderImplicitHydrogens);
        Assertions.assertSame(tmpFilterType, tmpReturnedFilter.listOfSelectedFilters.getLast());
    }

    /**
     * Tests whether the listOfSelectedFilters of the Filter instance returned by the .withMinAtomCountFilter() method
     * of the class Filter was extended by the specific filter type.
     */
    @Test
    public void withMinAtomCountFilterMethodTest_notConsiderImplicitHydrogens_checksIfListOfSelectedFiltersWasExtendedBySpecificFilterType() {
        Filter.FilterTypes tmpFilterType = Filter.FilterTypes.MIN_ATOM_COUNT_FILTER_NOT_CONSIDER_IMPLICIT_HYDROGENS;
        boolean tmpConsiderImplicitHydrogens = false;
        Filter tmpReturnedFilter = new Filter().withMinAtomCountFilter(0, tmpConsiderImplicitHydrogens);
        Assertions.assertSame(tmpFilterType, tmpReturnedFilter.listOfSelectedFilters.getLast());
    }

    /**
     * Tests whether the listOfFilterParameters of the Filter instance returned by the .withMinAtomCountFilter() method
     * of the class Filter is the same as the one of the original Filter instance.
     */
    @Test
    public void withMinAtomCountFilterMethodTest_returnedFilterContainsSameListOfFilterParametersAsOriginal() {
        Filter tmpOriginalFilter = new Filter();
        Filter tmpReturnedFilter = tmpOriginalFilter.withMinAtomCountFilter(0, true);
        Assertions.assertSame(tmpOriginalFilter.listOfFilterParameters, tmpReturnedFilter.listOfFilterParameters);
    }

    /**
     * Tests whether the listOfFilterParameters of the Filter instance returned by the .withMinAtomCountFilter() method
     * of the class Filter was extended by one entry.
     */
    @Test
    public void withMinAtomCountFilterMethodTest_checksIfListOfFilterParametersWasExtendedByOne() {
        Filter tmpOriginalFilter = new Filter();
        int tmpListInitialSize = tmpOriginalFilter.listOfFilterParameters.size();
        Filter tmpReturnedFilter = tmpOriginalFilter.withMinAtomCountFilter(0, true);
        Assertions.assertEquals(tmpListInitialSize + 1, tmpReturnedFilter.listOfFilterParameters.size());
    }

    /**
     * Tests whether the listOfFilterParameters of the Filter instance returned by the .withMinAtomCountFilter() method
     * of the class Filter was extended by the given integer parameter.
     */
    @Test
    public void withMinAtomCountFilterMethodTest_checksIfListOfFilterParametersWasExtendedByGivenIntegerParameter() {
        int tmpIntegerParameter = 0;
        Filter tmpReturnedFilter = new Filter().withMinAtomCountFilter(tmpIntegerParameter, true);
        Assertions.assertSame(tmpIntegerParameter, tmpReturnedFilter.listOfFilterParameters.getLast());
        Assertions.assertEquals(tmpIntegerParameter, tmpReturnedFilter.listOfFilterParameters.getLast());
    }

    /**
     * Tests whether the .withMinAtomCountFilter() method of the class Filter throws an IllegalArgumentException if the
     * given integer parameter is of a negative value.
     */
    @Test
    public void withMinAtomCountFilterMethodTest_throwsIllegalArgumentExceptionIfGivenMinAtomCountIsNegative() {    //TODO: do so?
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    int tmpNegativeMinAtomCount = -1;
                    new Filter().withMinAtomCountFilter(tmpNegativeMinAtomCount, true);
                }
        );
    }

}
