package de.unijena.cheminf.filter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 */
public class WithMaxAtomCountFilterTest {

    //TODO: test everything with boolean aConsiderImplicitHydrogens = false?

    /**
     * Tests whether the instance returned by the .withMaxAtomCountFilter() method of the class Filter is not null.
     */
    @Test
    public void withMaxAtomCountFilterMethodTest_returnsNotNull() {
        int tmpIntegerParameter = 10;
        boolean tmpBooleanParameter = true;
        Assertions.assertNotNull(new Filter().withMaxAtomCountFilter(tmpIntegerParameter, tmpBooleanParameter));
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
     * Tests whether the return value of the .withMaxAtomCountFilter() method of class Filter is an instance of Filter.
     */
    @Test
    public void withMaxAtomCountFilterMethodTest_returnsFilterInstance() {
        int tmpIntegerParameter = 10;
        boolean tmpBooleanParameter = true;
        Assertions.assertInstanceOf(Filter.class, new Filter().withMaxAtomCountFilter(tmpIntegerParameter, tmpBooleanParameter));
    }

    /**
     * Tests whether the listOfSelectedFilters of the Filter instance returned by the .withMaxAtomCountFilter() method
     * of the class Filter is the same as the one of the original Filter instance.
     */
    @Test
    public void withMaxAtomCountFilterMethodTest_returnedFilterContainsSameListOfSelectedFiltersAsOriginal() {
        Filter tmpOriginalFilter = new Filter();
        Filter tmpReturnedFilter = tmpOriginalFilter.withMaxAtomCountFilter(10, true);
        Assertions.assertSame(tmpOriginalFilter.listOfSelectedFilters, tmpReturnedFilter.listOfSelectedFilters);
    }

    /**
     * Tests whether the listOfSelectedFilters of the Filter instance returned by the .withMaxAtomCountFilter() method
     * of the class Filter was extended by one entry.
     */
    @Test
    public void withMaxAtomCountFilterMethodTest_checksIfListOfSelectedFiltersWasExtendedByOne() {
        Filter tmpOriginalFilter = new Filter();
        int tmpListInitialSize = tmpOriginalFilter.listOfSelectedFilters.size();
        Filter tmpReturnedFilter = tmpOriginalFilter.withMaxAtomCountFilter(10, true);
        Assertions.assertEquals(tmpListInitialSize + 1, tmpReturnedFilter.listOfSelectedFilters.size());
    }

    /**
     * Tests whether the listOfSelectedFilters of the Filter instance returned by the .withMaxAtomCountFilter() method
     * of the class Filter was extended by the specific filter type.
     */
    @Test
    public void withMaxAtomCountFilterMethodTest_considerImplicitHydrogens_checksIfListOfSelectedFiltersWasExtendedBySpecificFilterType() {
        Filter.FilterTypes tmpFilterType = Filter.FilterTypes.MAX_ATOM_COUNT_FILTER_CONSIDER_IMPLICIT_HYDROGENS;
        boolean tmpConsiderImplicitHydrogens = true;
        Filter tmpReturnedFilter = new Filter().withMaxAtomCountFilter(10, tmpConsiderImplicitHydrogens);
        Assertions.assertSame(tmpFilterType, tmpReturnedFilter.listOfSelectedFilters.getLast());
    }

    /**
     * Tests whether the listOfSelectedFilters of the Filter instance returned by the .withMaxAtomCountFilter() method
     * of the class Filter was extended by the specific filter type.
     */
    @Test
    public void withMaxAtomCountFilterMethodTest_notConsiderImplicitHydrogens_checksIfListOfSelectedFiltersWasExtendedBySpecificFilterType() {
        Filter.FilterTypes tmpFilterType = Filter.FilterTypes.MAX_ATOM_COUNT_FILTER_NOT_CONSIDER_IMPLICIT_HYDROGENS;
        boolean tmpConsiderImplicitHydrogens = false;
        Filter tmpReturnedFilter = new Filter().withMaxAtomCountFilter(10, tmpConsiderImplicitHydrogens);
        Assertions.assertSame(tmpFilterType, tmpReturnedFilter.listOfSelectedFilters.getLast());
    }

    /**
     * Tests whether the listOfFilterParameters of the Filter instance returned by the .withMaxAtomCountFilter() method
     * of the class Filter is the same as the one of the original Filter instance.
     */
    @Test
    public void withMaxAtomCountFilterMethodTest_returnedFilterContainsSameListOfFilterParametersAsOriginal() {
        Filter tmpOriginalFilter = new Filter();
        Filter tmpReturnedFilter = tmpOriginalFilter.withMaxAtomCountFilter(10, true);
        Assertions.assertSame(tmpOriginalFilter.listOfFilterParameters, tmpReturnedFilter.listOfFilterParameters);
    }

    /**
     * Tests whether the listOfFilterParameters of the Filter instance returned by the .withMaxAtomCountFilter() method
     * of the class Filter was extended by one entry.
     */
    @Test
    public void withMaxAtomCountFilterMethodTest_checksIfListOfFilterParametersWasExtendedByOne() {
        Filter tmpOriginalFilter = new Filter();
        int tmpListInitialSize = tmpOriginalFilter.listOfFilterParameters.size();
        Filter tmpReturnedFilter = tmpOriginalFilter.withMaxAtomCountFilter(10, true);
        Assertions.assertEquals(tmpListInitialSize + 1, tmpReturnedFilter.listOfFilterParameters.size());
    }

    /**
     * Tests whether the listOfFilterParameters of the Filter instance returned by the .withMaxAtomCountFilter() method
     * of the class Filter was extended by the given integer parameter.
     */
    @Test
    public void withMaxAtomCountFilterMethodTest_checksIfListOfFilterParametersWasExtendedByGivenIntegerParameter() {
        int tmpMaxAtomCount = 10;
        Filter tmpReturnedFilter = new Filter().withMaxAtomCountFilter(tmpMaxAtomCount, true);
        Assertions.assertSame(tmpMaxAtomCount, tmpReturnedFilter.listOfFilterParameters.getLast());
        Assertions.assertEquals(tmpMaxAtomCount, tmpReturnedFilter.listOfFilterParameters.getLast());
    }

    /**
     * Tests whether the .withMaxAtomCountFilter() method of the class Filter throws an IllegalArgumentException if the
     * given integer parameter is of a negative value.
     */
    @Test
    public void withMaxAtomCountFilterMethodTest_throwsIllegalArgumentExceptionIfGivenMaxAtomCountIsNegative() {    //TODO: do so?
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    int tmpNegativeMaxAtomCount = -1;
                    new Filter().withMaxAtomCountFilter(tmpNegativeMaxAtomCount, true);
                }
        );
    }

}
