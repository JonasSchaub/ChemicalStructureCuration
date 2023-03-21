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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

/**
 * Test class for the .withMinAtomCountFilter() method of class FilterPipeline.
 */
public class WithMinAtomCountFilterTest {

    /**
     * Tests whether the instance returned by the .withMinAtomCountFilter() method of the class FilterPipeline is not
     * null.
     */
    @Test
    public void withMinAtomCountFilterMethodTest_returnsNotNull() {
        int tmpIntegerParameter = 10;
        boolean tmpBooleanParameter = true;
        Assertions.assertNotNull(new FilterPipeline().withMinAtomCountFilter(tmpIntegerParameter, tmpBooleanParameter));
    }

    /**
     * Tests whether the return value of the .withMinAtomCountFilter() method of class FilterPipeline is an instance of
     * FilterPipeline.
     */
    @Test
    public void withMinAtomCountFilterMethodTest_returnsFilterPipelineInstance() {
        int tmpIntegerParameter = 10;
        boolean tmpBooleanParameter = true;
        Assertions.assertInstanceOf(FilterPipeline.class, new FilterPipeline().withMinAtomCountFilter(tmpIntegerParameter, tmpBooleanParameter));
    }

    /**
     * Tests whether the FilterPipeline instance returned by the .withMinAtomCountFilter() method of class
     * FilterPipeline is the same as the one the method is called from.
     */
    @Test
    public void withMinAtomCountFilterMethodTest_returnedFilterPipelineInstanceIsSameAsTheOneTheMethodIsCalledFrom() {
        int tmpIntegerParameter = 10;
        boolean tmpBooleanParameter = true;
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        Assertions.assertSame(tmpFilterPipeline, tmpFilterPipeline.withMinAtomCountFilter(tmpIntegerParameter, tmpBooleanParameter));
    }

    /**
     * Tests whether the listOfSelectedFilters of the FilterPipeline instance returned by the .withMinAtomCountFilter()
     * method of the class FilterPipeline is the same instance as before and was extended by one entry.
     */
    @Test
    public void withMinAtomCountFilterMethodTest_checksWhetherListOfSelectedFiltersIsTheSameAndWasExtendedByOne() {
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        LinkedList<IFilter> tmpListOfSelectedFilters = tmpFilterPipeline.getListOfSelectedFilters();
        int tmpListInitialSize = tmpListOfSelectedFilters.size();
        tmpFilterPipeline.withMinAtomCountFilter(10, true);
        Assertions.assertSame(tmpListOfSelectedFilters, tmpFilterPipeline.getListOfSelectedFilters());
        Assertions.assertEquals(tmpListInitialSize + 1, tmpFilterPipeline.getListOfSelectedFilters().size());
    }

    /**
     * Tests whether the listOfSelectedFilters of the FilterPipeline instance returned by the .withMinAtomCountFilter()
     * method of the class FilterPipeline was extended by an instance of MinAtomCountFilter.
     */
    @Test
    public void withMinAtomCountFilterMethodTest_checksWhetherListOfSelectedFiltersWasExtendedByInstanceOfMinAtomCountFilter() {
        int tmpIntegerParameter = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        FilterPipeline tmpFilterPipeline = new FilterPipeline().withMinAtomCountFilter(tmpIntegerParameter, tmpConsiderImplicitHydrogens);
        Assertions.assertInstanceOf(MinAtomCountFilter.class, tmpFilterPipeline.getListOfSelectedFilters().getLast());
    }

    /**
     * Tests whether the MinAtomCountFilter added to the listOfSelectedFilters by the .withMinAtomCountFilter() method
     * of the class FilterPipeline contains the given min atom count threshold value.
     */
    @Test
    public void withMinAtomCountFilterMethodTest_checksWhetherAddedMinAtomCountFilterHasGivenThresholdSet_twoTests() {
        int tmpThresholdValue = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        FilterPipeline tmpFilterPipeline = new FilterPipeline().withMinAtomCountFilter(tmpThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertEquals(tmpThresholdValue, ((MinAtomCountFilter) tmpFilterPipeline.getListOfSelectedFilters().getLast()).getMinAtomCount());
        tmpThresholdValue = 20;
        tmpFilterPipeline = new FilterPipeline().withMinAtomCountFilter(tmpThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertEquals(tmpThresholdValue, ((MinAtomCountFilter) tmpFilterPipeline.getListOfSelectedFilters().getLast()).getMinAtomCount());
    }

    /**
     * Tests whether the MinAtomCountFilter added to the listOfSelectedFilters by the .withMinAtomCountFilter() method
     * of the class FilterPipeline has the given boolean value set.
     */
    @Test
    public void withMinAtomCountFilterMethodTest_checksWhetherAddedMinAtomCountFilterHasGivenBooleanConsiderImplHsSet_twoTests() {
        int tmpThresholdValue = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        FilterPipeline tmpFilterPipeline = new FilterPipeline().withMinAtomCountFilter(tmpThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertTrue(((MinAtomCountFilter) tmpFilterPipeline.getListOfSelectedFilters().getLast()).isConsiderImplicitHydrogens());
        tmpConsiderImplicitHydrogens = false;
        tmpFilterPipeline = new FilterPipeline().withMinAtomCountFilter(tmpThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertFalse(((MinAtomCountFilter) tmpFilterPipeline.getListOfSelectedFilters().getLast()).isConsiderImplicitHydrogens());
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
