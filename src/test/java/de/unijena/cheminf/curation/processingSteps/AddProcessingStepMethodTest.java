package de.unijena.cheminf.curation.processingSteps;

import de.unijena.cheminf.curation.TestUtils;
import de.unijena.cheminf.curation.processingSteps.filters.BaseFilter;
import de.unijena.cheminf.curation.processingSteps.filters.IFilter;
import de.unijena.cheminf.curation.processingSteps.filters.MinAtomCountFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

/**
 * Test class of the CurationPipeline method .addProcessingStep().
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public class AddProcessingStepMethodTest {

    /**
     * Tests whether the FilterPipeline instance returned by the .addProcessingStep() method of class FilterPipeline is the
     * same as the one the method is called from.
     */
    @Test
    public void addProcessingStepMethodTest_returnedFilterPipelineInstanceIsSameAsTheOneTheMethodIsCalledFrom() {
        IFilter tmpEmptyFilter = TestUtils.getAllTrueOrFalseFilter();
        CurationPipeline tmpCurationPipeline = new CurationPipeline(TestUtils.getTestReporterInstance());
        Assertions.assertSame(tmpCurationPipeline, tmpCurationPipeline.addProcessingStep(tmpEmptyFilter));
    }

    /**
     * Tests whether the listOfSelectedFilters of the FilterPipeline instance returned by the .addProcessingStep() method of
     * the class FilterPipeline is the same instance as before and was extended by one entry.
     */
    @Test
    public void addProcessingStepMethodTest_checksIfListOfPipelineStepsIsTheSameAndWasExtendedByOne() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline(TestUtils.getTestReporterInstance());
        LinkedList<IProcessingStep> tmpListOfPipelineSteps = tmpCurationPipeline.getListOfPipelineSteps();
        int tmpListInitialSize = tmpListOfPipelineSteps.size();
        IFilter tmpEmptyFilter = TestUtils.getAllTrueOrFalseFilter();
        tmpCurationPipeline.addProcessingStep(tmpEmptyFilter);
        Assertions.assertSame(tmpListOfPipelineSteps, tmpCurationPipeline.getListOfPipelineSteps());
        Assertions.assertEquals(tmpListInitialSize + 1, tmpCurationPipeline.getListOfPipelineSteps().size());
    }

    /**
     * Tests whether the listOfSelectedFilters of the FilterPipeline instance returned by the .addProcessingStep() method of
     * the class FilterPipeline was extended by the given IFilter instance.
     */
    @Test
    public void addProcessingStepMethodTest_checksIfListOfSelectedFiltersWasExtendedByGivenFilterInstance_test1() {
        IFilter tmpEmptyFilter = TestUtils.getAllTrueOrFalseFilter();
        CurationPipeline tmpCurationPipeline = new CurationPipeline(TestUtils.getTestReporterInstance())
                .addProcessingStep(tmpEmptyFilter);
        Assertions.assertInstanceOf(IFilter.class, tmpCurationPipeline.getListOfPipelineSteps().getLast());
        Assertions.assertSame(tmpEmptyFilter, tmpCurationPipeline.getListOfPipelineSteps().getLast());
    }

    /**
     * Tests whether the listOfSelectedFilters of the FilterPipeline instance returned by the .addProcessingStep() method of
     * the class FilterPipeline was extended by the given IFilter instance.
     */
    @Test
    public void addProcessingStepMethodTest_checksIfListOfSelectedFiltersWasExtendedByGivenFilterInstance_test2() {
        int tmpIntegerParameter = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        boolean tmpConsiderPseudoAtoms = true;
        IFilter tmpNewFilter = new MinAtomCountFilter(tmpIntegerParameter, tmpConsiderImplicitHydrogens,
                tmpConsiderPseudoAtoms, TestUtils.getTestReporterInstance());
        CurationPipeline tmpCurationPipeline = new CurationPipeline(TestUtils.getTestReporterInstance())
                .addProcessingStep(tmpNewFilter);
        Assertions.assertInstanceOf(IFilter.class, tmpCurationPipeline.getListOfPipelineSteps().getLast());
        Assertions.assertSame(tmpNewFilter, tmpCurationPipeline.getListOfPipelineSteps().getLast());
    }

    /**
     * Tests whether the .addProcessingStep() method of the class FilterPipeline throws a NullPointerException if the given
     * IFilter instance is null.
     */
    @Test
    public void addProcessingStepMethodTest_throwsNullPointerExceptionIfGivenFilterInstanceIsNull() {    //TODO: do so?
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    new CurationPipeline(TestUtils.getTestReporterInstance()).addProcessingStep(null);
                }
        );
    }

    /**
     * Tests whether the list of selected filters is extended by two entries if the .addProcessingStep() method of the class
     * FilterPipeline is called twice.
     */
    @Test
    public void addProcessingStepMethodTest_combiningTwoFilters_twoFiltersAreAddedToListOfSelectedFilters() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline(TestUtils.getTestReporterInstance());
        int tmpInitialListSize = tmpCurationPipeline.getListOfPipelineSteps().size();
        BaseFilter tmpEmptyFilter = TestUtils.getAllTrueOrFalseFilter();
        tmpCurationPipeline = tmpCurationPipeline.addProcessingStep(tmpEmptyFilter).addProcessingStep(tmpEmptyFilter);
        Assertions.assertEquals(tmpInitialListSize + 2, tmpCurationPipeline.getListOfPipelineSteps().size());
    }

    /**
     * Tests whether the list of selected filters is extended by the two specific IFilter instances in the correct order
     * if the .addProcessingStep() method of the class FilterPipeline is called twice.
     */
    @Test
    public void addProcessingStepMethodTest_combiningTwoFilters_bothSpecificIFilterInstancesAreAddedToListOfSelectedFiltersInCorrectOrder() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline(TestUtils.getTestReporterInstance());
        IFilter tmpFilter1 = TestUtils.getAllTrueOrFalseFilter();
        IFilter tmpFilter2 = TestUtils.getAllTrueOrFalseFilter();
        tmpCurationPipeline.addProcessingStep(tmpFilter1).addProcessingStep(tmpFilter2);
        Assertions.assertSame(tmpFilter1, tmpCurationPipeline.getListOfPipelineSteps().get(0));
        Assertions.assertSame(tmpFilter2, tmpCurationPipeline.getListOfPipelineSteps().get(1));
    }

    /**
     * Tests whether the list of selected filters is extended by the respective number of entries if the .addProcessingStep()
     * method of the class FilterPipeline is called multiple.
     */
    @Test
    public void addProcessingStepMethodTest_combiningMultipleFilters_5_listOfSelectedFiltersIsExtendedByGivenFilterCount() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline(TestUtils.getTestReporterInstance());
        int tmpInitialListSize = tmpCurationPipeline.getListOfPipelineSteps().size();
        IFilter tmpEmptyFilter = TestUtils.getAllTrueOrFalseFilter();
        tmpCurationPipeline.addProcessingStep(tmpEmptyFilter).addProcessingStep(tmpEmptyFilter).addProcessingStep(tmpEmptyFilter)
                .addProcessingStep(tmpEmptyFilter).addProcessingStep(tmpEmptyFilter);
        Assertions.assertEquals(tmpInitialListSize + 5, tmpCurationPipeline.getListOfPipelineSteps().size());
    }

    /**
     * Tests whether the list of selected filters is extended by the specific IFilter instances in the correct order
     * if the .addProcessingStep() method of the class FilterPipeline is called multiple times.
     */
    @Test
    public void addProcessingStepMethodTest_combiningMultipleFilters_5_listOfSelectedFiltersIsExtendedBySpecificIFiltersInCorrectOrder() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline(TestUtils.getTestReporterInstance());
        IFilter[] tmpFilterArray = new IFilter[5];
        for (int i = 0; i < tmpFilterArray.length; i++) {
            tmpFilterArray[i] = TestUtils.getAllTrueOrFalseFilter();
            tmpCurationPipeline.addProcessingStep(tmpFilterArray[i] = TestUtils.getAllTrueOrFalseFilter());
        }
        for (int i = 0; i < tmpCurationPipeline.getListOfPipelineSteps().size(); i++) {
            Assertions.assertSame(tmpFilterArray[i], tmpCurationPipeline.getListOfPipelineSteps().get(i));
        }
    }

}
