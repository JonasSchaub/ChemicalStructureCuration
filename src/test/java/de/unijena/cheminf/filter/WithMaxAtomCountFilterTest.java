package de.unijena.cheminf.filter;

import de.unijena.cheminf.filter.Filter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WithMaxAtomCountFilterTest {

    /**
     * Tests whether the instance returned by the .with...Filter() method of the class Filter is not null.
     */
    /*@Test
    public void with...FilterMethodTest_returnsNotNull() {
        Filter.FilterTypes tmpFilterType = Filter.FilterTypes.NONE;
        int tmpIntegerParameter = 0;
        Assertions.assertNotNull(new Filter().withFilter(tmpFilterType, tmpIntegerParameter));
    }*/

    /**
     * Tests whether the return value of the .with...Filter() method of class Filter is an instance of Filter.
     */
    /*@Test
    public void with...FilterMethodTest_returnsFilterInstance() {
        Filter.FilterTypes tmpFilterType = Filter.FilterTypes.NONE;
        int tmpIntegerParameter = 0;
        Assertions.assertInstanceOf(Filter.class, new Filter().withFilter(tmpFilterType, tmpIntegerParameter));
    }*/

    /**
     * Tests whether the listOfSelectedFilters of the Filter instance returned by the .with...Filter() method of the class
     * Filter is the same as the one of the original Filter instance.
     */
    /*@Test
    public void with...FilterMethodTest_returnedFilterContainsSameListOfSelectedFiltersAsOriginal() {
        Filter tmpOriginalFilter = new Filter();
        Filter tmpReturnedFilter = tmpOriginalFilter.withFilter(Filter.FilterTypes.NONE, 0);
        Assertions.assertSame(tmpOriginalFilter.listOfSelectedFilters, tmpReturnedFilter.listOfSelectedFilters);
    }*/

    /*@Test
    public void with...FilterMethodTest_checksIfListOfSelectedFiltersWasExtendedByOne() {
        Filter tmpOriginalFilter = new Filter();
        int tmpListInitialSize = tmpOriginalFilter.listOfSelectedFilters.size();
        Filter tmpReturnedFilter = tmpOriginalFilter.withFilter(Filter.FilterTypes.NONE, 0);
        Assertions.assertEquals(tmpListInitialSize + 1, tmpReturnedFilter.listOfSelectedFilters.size());
    }*/

    /*@Test
    public void with...FilterMethodTest_checksIfListOfSelectedFiltersWasExtendedByGivenFilterType() {
        Filter.FilterTypes tmpFilterType = Filter.FilterTypes.NONE;
        Filter tmpReturnedFilter = new Filter().withFilter(tmpFilterType, 0);
        Assertions.assertSame(tmpFilterType, tmpReturnedFilter.listOfSelectedFilters.getLast());
    }*/

    /**
     * Tests whether the listOfFilterParameters of the Filter instance returned by the .with...Filter() method of the class
     * Filter is the same as the one of the original Filter instance.
     */
    /*@Test
    public void with...FilterMethodTest_returnedFilterContainsSameListOfFilterParametersAsOriginal() {
        Filter tmpOriginalFilter = new Filter();
        Filter tmpReturnedFilter = tmpOriginalFilter.withFilter(Filter.FilterTypes.NONE, 0);
        Assertions.assertSame(tmpOriginalFilter.listOfFilterParameters, tmpReturnedFilter.listOfFilterParameters);
    }*/

    /*@Test
    public void with...FilterMethodTest_checksIfListOfFilterParametersWasExtendedByOne() {
        Filter tmpOriginalFilter = new Filter();
        int tmpListInitialSize = tmpOriginalFilter.listOfFilterParameters.size();
        Filter tmpReturnedFilter = tmpOriginalFilter.withFilter(Filter.FilterTypes.NONE, 0);
        Assertions.assertEquals(tmpListInitialSize + 1, tmpReturnedFilter.listOfFilterParameters.size());
    }*/

    /*@Test
    public void with...FilterMethodTest_checksIfListOfFilterParametersWasExtendedByGivenIntegerParameter() {
        int tmpIntegerParameter = 0;
        Filter tmpReturnedFilter = new Filter().withFilter(Filter.FilterTypes.NONE, tmpIntegerParameter);
        Assertions.assertSame(tmpIntegerParameter, tmpReturnedFilter.listOfFilterParameters.getLast());
    }*/

    /*@Test
    public void with...FilterMethodTest_throwsNullPointerExceptionIfGivenFilterTypeIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    new Filter().withFilter(null, 0);
                }
        );
    }*/

    /**
     * TODO
     */
    /*@Test
    public void withMaxAtomCountFilterMethodTest_returnsFilterInstance_notNull() {
        Assertions.assertNotNull(new Filter().withMaxAtomCountFilter(0, true));
        Assertions.assertInstanceOf(Filter.class, new Filter().withMaxAtomCountFilter(0, true));
    }*/

    /*@Test
    public void withMaxAtomCountFilterMethodTest_returnedFilterContainsSameListOfSelectedFiltersAsOriginal() {
        Filter tmpOriginalFilter = new Filter();
        Filter tmpReturnedFilter = tmpOriginalFilter.withMaxAtomCountFilter(0, true);
        Assertions.assertSame(tmpOriginalFilter.getListOfSelectedFilters(), tmpReturnedFilter.getListOfSelectedFilters());
    }*/

}
