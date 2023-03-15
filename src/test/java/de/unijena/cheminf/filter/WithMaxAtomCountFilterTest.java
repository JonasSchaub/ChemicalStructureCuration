package de.unijena.cheminf.filter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

/**
 * Test class for the .withMaxAtomCountFilter() method of class FilterPipeline.
 */
public class WithMaxAtomCountFilterTest {   //TODO: check if everything is up to date

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

    /**
     * Tests whether the return value of the .withMaxAtomCountFilter() method of class FilterPipeline is an instance of
     * FilterPipeline.
     */
    @Test
    public void withMaxAtomCountFilterMethodTest_returnsFilterPipelineInstance() {
        int tmpIntegerParameter = 10;
        boolean tmpBooleanParameter = true;
        Assertions.assertInstanceOf(FilterPipeline.class, new FilterPipeline().withMaxAtomCountFilter(tmpIntegerParameter, tmpBooleanParameter));
    }

    /**
     * Tests whether the FilterPipeline instance returned by the .withMaxAtomCountFilter() method of class
     * FilterPipeline is the same as the one the method is called from.
     */
    @Test
    public void withMaxAtomCountFilterMethodTest_returnedFilterPipelineInstanceIsSameAsTheOneTheMethodIsCalledFrom() {
        int tmpIntegerParameter = 10;
        boolean tmpBooleanParameter = true;
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        Assertions.assertSame(tmpFilterPipeline, tmpFilterPipeline.withMaxAtomCountFilter(tmpIntegerParameter, tmpBooleanParameter));
    }

    /**
     * Tests whether the listOfSelectedFilters of the FilterPipeline instance returned by the .withMaxAtomCountFilter()
     * method of the class FilterPipeline is the same instance as before and was extended by one entry.
     */
    @Test
    public void withMaxAtomCountFilterMethodTest_checksIfListOfSelectedFiltersIsTheSameAndWasExtendedByOne() {
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        LinkedList<IFilter> tmpListOfSelectedFilters = tmpFilterPipeline.getListOfSelectedFilters();
        int tmpListInitialSize = tmpListOfSelectedFilters.size();
        tmpFilterPipeline.withMaxAtomCountFilter(10, true);
        Assertions.assertSame(tmpListOfSelectedFilters, tmpFilterPipeline.getListOfSelectedFilters());
        Assertions.assertEquals(tmpListInitialSize + 1, tmpFilterPipeline.getListOfSelectedFilters().size());
    }

    /**
     * Tests whether the listOfSelectedFilters of the FilterPipeline instance returned by the .withMaxAtomCountFilter()
     * method of the class FilterPipeline was extended by an instance of MaxAtomCountFilter.
     */
    @Test
    public void withMaxAtomCountFilterMethodTest_checksIfListOfSelectedFiltersWasExtendedByInstanceOfMaxAtomCountFilter() {
        int tmpIntegerParameter = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        FilterPipeline tmpFilterPipeline = new FilterPipeline().withMaxAtomCountFilter(tmpIntegerParameter, tmpConsiderImplicitHydrogens);
        Assertions.assertInstanceOf(MaxAtomCountFilter.class, tmpFilterPipeline.getListOfSelectedFilters().getLast());
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
