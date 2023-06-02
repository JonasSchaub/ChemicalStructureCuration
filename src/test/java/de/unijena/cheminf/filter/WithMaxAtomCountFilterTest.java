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

import de.unijena.cheminf.IProcessingStep;
import de.unijena.cheminf.filter.filters.MaxAtomCountFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
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
        Assertions.assertNotNull(new CurationPipeline().withMaxAtomCountFilter(tmpIntegerParameter, tmpBooleanParameter));
    }

    /**
     * Tests whether the return value of the .withMaxAtomCountFilter() method of class FilterPipeline is an instance of
     * FilterPipeline.
     */
    @Test
    public void withMaxAtomCountFilterMethodTest_returnsFilterPipelineInstance() {
        int tmpIntegerParameter = 10;
        boolean tmpBooleanParameter = true;
        Assertions.assertInstanceOf(CurationPipeline.class, new CurationPipeline().withMaxAtomCountFilter(tmpIntegerParameter, tmpBooleanParameter));
    }

    /**
     * Tests whether the FilterPipeline instance returned by the .withMaxAtomCountFilter() method of class
     * FilterPipeline is the same as the one the method is called from.
     */
    @Test
    public void withMaxAtomCountFilterMethodTest_returnedFilterPipelineInstanceIsSameAsTheOneTheMethodIsCalledFrom() {
        int tmpIntegerParameter = 10;
        boolean tmpBooleanParameter = true;
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        Assertions.assertSame(tmpCurationPipeline, tmpCurationPipeline.withMaxAtomCountFilter(tmpIntegerParameter, tmpBooleanParameter));
    }

    /** TODO
     * Tests whether the listOfSelectedFilters of the FilterPipeline instance returned by the .withMaxAtomCountFilter()
     * method of the class FilterPipeline is the same instance as before and was extended by one entry.
     */
    @Test
    @Disabled
    public void withMaxAtomCountFilterMethodTest_checksWhetherListOfSelectedFiltersIsTheSameAndWasExtendedByOne() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        LinkedList<IProcessingStep> tmpListOfSelectedFilters = tmpCurationPipeline.getListOfSelectedPipelineSteps();
        int tmpListInitialSize = tmpListOfSelectedFilters.size();
        tmpCurationPipeline.withMaxAtomCountFilter(10, true);
        Assertions.assertSame(tmpListOfSelectedFilters, tmpCurationPipeline.getListOfSelectedPipelineSteps());
        Assertions.assertEquals(tmpListInitialSize + 1, tmpCurationPipeline.getListOfSelectedPipelineSteps().size());
    }

    /**
     * Tests whether the listOfSelectedFilters of the FilterPipeline instance returned by the .withMaxAtomCountFilter()
     * method of the class FilterPipeline was extended by an instance of MaxAtomCountFilter.
     */
    @Test
    public void withMaxAtomCountFilterMethodTest_checksWhetherListOfSelectedFiltersWasExtendedByInstanceOfMaxAtomCountFilter() {
        int tmpIntegerParameter = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withMaxAtomCountFilter(tmpIntegerParameter, tmpConsiderImplicitHydrogens);
        Assertions.assertInstanceOf(MaxAtomCountFilter.class, tmpCurationPipeline.getListOfSelectedPipelineSteps().getLast());
    }

    /**
     * Tests whether the MaxAtomCountFilter added to the listOfSelectedFilters by the .withMaxAtomCountFilter() method
     * of the class FilterPipeline contains the given max atom count threshold value.
     */
    @Test
    public void withMaxAtomCountFilterMethodTest_checksWhetherAddedMaxAtomCountFilterHasGivenThresholdSet_twoTests() {
        int tmpThresholdValue = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withMaxAtomCountFilter(tmpThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertEquals(tmpThresholdValue, ((MaxAtomCountFilter) tmpCurationPipeline.getListOfSelectedPipelineSteps().getLast()).getMaxAtomCount());
        tmpThresholdValue = 20;
        tmpCurationPipeline = new CurationPipeline().withMaxAtomCountFilter(tmpThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertEquals(tmpThresholdValue, ((MaxAtomCountFilter) tmpCurationPipeline.getListOfSelectedPipelineSteps().getLast()).getMaxAtomCount());
    }

    /**
     * Tests whether the MaxAtomCountFilter added to the listOfSelectedFilters by the .withMaxAtomCountFilter() method
     * of the class FilterPipeline has the given boolean value set.
     */
    @Test
    public void withMaxAtomCountFilterMethodTest_checksWhetherAddedMaxAtomCountFilterHasGivenBooleanConsiderImplHsSet_twoTests() {
        int tmpThresholdValue = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withMaxAtomCountFilter(tmpThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertTrue(((MaxAtomCountFilter) tmpCurationPipeline.getListOfSelectedPipelineSteps().getLast()).isConsiderImplicitHydrogens());
        tmpConsiderImplicitHydrogens = false;
        tmpCurationPipeline = new CurationPipeline().withMaxAtomCountFilter(tmpThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertFalse(((MaxAtomCountFilter) tmpCurationPipeline.getListOfSelectedPipelineSteps().getLast()).isConsiderImplicitHydrogens());
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
                    new CurationPipeline().withMaxAtomCountFilter(tmpNegativeMaxAtomCount, true);
                }
        );
    }

}
