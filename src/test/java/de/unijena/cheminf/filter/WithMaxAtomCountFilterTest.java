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
 * Test class for the .withMaxAtomCountFilter() method of class FilterPipeline.
 */
public class WithMaxAtomCountFilterTest {

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
    public void withMaxAtomCountFilterMethodTest_checksWhetherListOfSelectedFiltersIsTheSameAndWasExtendedByOne() {
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
    public void withMaxAtomCountFilterMethodTest_checksWhetherListOfSelectedFiltersWasExtendedByInstanceOfMaxAtomCountFilter() {
        int tmpIntegerParameter = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        FilterPipeline tmpFilterPipeline = new FilterPipeline().withMaxAtomCountFilter(tmpIntegerParameter, tmpConsiderImplicitHydrogens);
        Assertions.assertInstanceOf(MaxAtomCountFilter.class, tmpFilterPipeline.getListOfSelectedFilters().getLast());
    }

    /**
     * Tests whether the MaxAtomCountFilter added to the listOfSelectedFilters by the .withMaxAtomCountFilter() method
     * of the class FilterPipeline contains the given max atom count threshold value.
     */
    @Test
    public void withMaxAtomCountFilterMethodTest_checksWhetherAddedMaxAtomCountFilterHasGivenThresholdSet_twoTests() {
        int tmpThresholdValue = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        FilterPipeline tmpFilterPipeline = new FilterPipeline().withMaxAtomCountFilter(tmpThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertEquals(tmpThresholdValue, ((MaxAtomCountFilter) tmpFilterPipeline.getListOfSelectedFilters().getLast()).getMaxAtomCount());
        tmpThresholdValue = 20;
        tmpFilterPipeline = new FilterPipeline().withMaxAtomCountFilter(tmpThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertEquals(tmpThresholdValue, ((MaxAtomCountFilter) tmpFilterPipeline.getListOfSelectedFilters().getLast()).getMaxAtomCount());
    }

    /**
     * Tests whether the MaxAtomCountFilter added to the listOfSelectedFilters by the .withMaxAtomCountFilter() method
     * of the class FilterPipeline has the given boolean value set.
     */
    @Test
    public void withMaxAtomCountFilterMethodTest_checksWhetherAddedMaxAtomCountFilterHasGivenBooleanConsiderImplHsSet_twoTests() {
        int tmpThresholdValue = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        FilterPipeline tmpFilterPipeline = new FilterPipeline().withMaxAtomCountFilter(tmpThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertTrue(((MaxAtomCountFilter) tmpFilterPipeline.getListOfSelectedFilters().getLast()).isConsiderImplicitHydrogens());
        tmpConsiderImplicitHydrogens = false;
        tmpFilterPipeline = new FilterPipeline().withMaxAtomCountFilter(tmpThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertFalse(((MaxAtomCountFilter) tmpFilterPipeline.getListOfSelectedFilters().getLast()).isConsiderImplicitHydrogens());
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
