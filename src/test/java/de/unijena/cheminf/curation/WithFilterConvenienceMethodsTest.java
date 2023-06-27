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

package de.unijena.cheminf.curation;

import de.unijena.cheminf.curation.filter.IFilter;
import de.unijena.cheminf.curation.filter.filters.HasAllValidAtomicNumbersFilter;
import de.unijena.cheminf.curation.filter.filters.HasInvalidAtomicNumbersFilter;
import de.unijena.cheminf.curation.filter.filters.MaxAtomCountFilter;
import de.unijena.cheminf.curation.filter.filters.MaxBondCountFilter;
import de.unijena.cheminf.curation.filter.filters.MaxBondsOfSpecificBondOrderFilter;
import de.unijena.cheminf.curation.filter.filters.MaxHeavyAtomCountFilter;
import de.unijena.cheminf.curation.filter.filters.MaxMolecularMassFilter;
import de.unijena.cheminf.curation.filter.filters.MinAtomCountFilter;
import de.unijena.cheminf.curation.filter.filters.MinBondCountFilter;
import de.unijena.cheminf.curation.filter.filters.MinBondsOfSpecificBondOrderFilter;
import de.unijena.cheminf.curation.filter.filters.MinHeavyAtomCountFilter;
import de.unijena.cheminf.curation.filter.filters.MinMolecularMassFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IBond;

/**
 * Test class of CurationPipeline methods to add specific filters to the pipeline.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public class WithFilterConvenienceMethodsTest {

    /*
    TODO: add tests for every new filter class
    //
    TODO: revisit tests of .process() method with respective filters
     */

    //<editor-fold desc="withMaxAtomCountFilter method tests" defaultstate="collapsed">
    /**
     * Tests whether the .withMaxAtomCountFilter() method of the class CurationPipeline returns the CurationPipeline
     * instance itself and adds a MaxAtomCountFilter to the list of processing steps.
     */
    @Test
    public void withMaxAtomCountFilterMethodTest_returnsPipelineInstanceItself_addsFilterToList() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        int tmpInitialListSize = tmpCurationPipeline.getListOfPipelineSteps().size();
        int tmpThresholdValue = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        CurationPipeline tmpReturnedPipeline = tmpCurationPipeline.withMaxAtomCountFilter(
                tmpThresholdValue, tmpConsiderImplicitHydrogens
        );
        Assertions.assertSame(tmpReturnedPipeline, tmpReturnedPipeline);
        Assertions.assertEquals(tmpInitialListSize + 1, tmpReturnedPipeline.getListOfPipelineSteps().size());
        Assertions.assertInstanceOf(MaxAtomCountFilter.class, tmpReturnedPipeline.getListOfPipelineSteps().getLast());
    }

    /**
     * Tests whether the .withMaxAtomCountFilter() method of the class CurationPipeline throws an
     * IllegalArgumentException if the given integer parameter is of a negative value.
     */
    @Test
    public void withMaxAtomCountFilterMethodTest_negativeThresholdValue_throwsIllegalArgumentException() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    int tmpNegativeMaxAtomCount = -1;
                    new CurationPipeline().withMaxAtomCountFilter(tmpNegativeMaxAtomCount, true);
                }
        );
    }

    //TODO: test .process() method with MaxAtomCountFilter?
    //</editor-fold>

    //<editor-fold desc="withMinAtomCountFilter method tests" defaultstate="collapsed">
    /**
     * Tests whether the .withMinAtomCountFilter() method of the class CurationPipeline returns the CurationPipeline
     * instance itself and adds a MinAtomCountFilter to the list of processing steps.
     */
    @Test
    public void withMinAtomCountFilterMethodTest_returnsPipelineInstanceItself_addsFilterToList() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        int tmpInitialListSize = tmpCurationPipeline.getListOfPipelineSteps().size();
        int tmpThresholdValue = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        CurationPipeline tmpReturnedPipeline = tmpCurationPipeline.withMinAtomCountFilter(
                tmpThresholdValue, tmpConsiderImplicitHydrogens
        );
        Assertions.assertSame(tmpReturnedPipeline, tmpReturnedPipeline);
        Assertions.assertEquals(tmpInitialListSize + 1, tmpReturnedPipeline.getListOfPipelineSteps().size());
        Assertions.assertInstanceOf(MinAtomCountFilter.class, tmpReturnedPipeline.getListOfPipelineSteps().getLast());
    }

    /**
     * Tests whether the .withMinAtomCountFilter() method of the class CurationPipeline throws an
     * IllegalArgumentException if the given integer parameter is of a negative value.
     */
    @Test
    public void withMinAtomCountFilterMethodTest_negativeThresholdValue_throwsIllegalArgumentException() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    int tmpNegativeMinAtomCount = -1;
                    new CurationPipeline().withMinAtomCountFilter(tmpNegativeMinAtomCount, true);
                }
        );
    }

    //TODO: test .process() method with MinAtomCountFilter?
    //</editor-fold>

    //<editor-fold desc="withMaxBondCountFilter method tests" defaultstate="collapsed">
    /**
     * Tests whether the .withMaxBondCountFilter() method of the class CurationPipeline returns the CurationPipeline
     * instance itself and adds a MaxBondCountFilter to the list of processing steps.
     */
    @Test
    public void withMaxBondCountFilterMethodTest_returnsPipelineInstanceItself_addsFilterToList() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        int tmpInitialListSize = tmpCurationPipeline.getListOfPipelineSteps().size();
        int tmpIntegerParameter = 10;
        boolean tmpBooleanParameter = true;
        CurationPipeline tmpReturnedPipeline = tmpCurationPipeline.withMaxBondCountFilter(tmpIntegerParameter, tmpBooleanParameter);
        Assertions.assertSame(tmpReturnedPipeline, tmpReturnedPipeline);
        Assertions.assertEquals(tmpInitialListSize + 1, tmpReturnedPipeline.getListOfPipelineSteps().size());
        Assertions.assertInstanceOf(MaxBondCountFilter.class, tmpReturnedPipeline.getListOfPipelineSteps().getLast());
    }

    /**
     * Tests whether the .withMaxBondCountFilter() method of the class CurationPipeline throws an IllegalArgumentException
     * if the given integer parameter is of a negative value.
     */
    @Test
    public void withMaxBondCountFilterMethodTest_negativeThresholdValue_throwsIllegalArgumentException() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    int tmpNegativeMaxBondCount = -1;
                    new CurationPipeline().withMaxBondCountFilter(tmpNegativeMaxBondCount, true);
                }
        );
    }

    /**
     * TODO
     * Tests whether the .process() method of class CurationPipeline behaves as expected when filtering with a
     * MaxBondCountFilter considering bonds to implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void processMethodTest_withMaxBondCountFilter_considerImplHs_multipleMolecules() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "CC(=O)O",  // 7 (3)
                "c1ccccc1", //12 (6) - filtered
                "C1CCCC1",  //15 (5) - filtered
                "NCC(=O)O", // 9 (4)
                "C=CC=C",   // 9 (3)
                "CCO"       // 8 (2)
        );
        boolean[] tmpIsFilteredArray = new boolean[]{false, true, true, false, false, false};
        //
        int tmpMaxBondCount = 9;
        boolean tmpConsiderImplicitHydrogens = true;
        IFilter tmpFilter = new MaxBondCountFilter(tmpMaxBondCount, tmpConsiderImplicitHydrogens);
        //
        TestUtils.curationPipeline_processMethodTest_testsBehaviorOfMethodWithSpecificFilter(
                tmpFilter, tmpAtomContainerSet, tmpIsFilteredArray
        );
    }

    /**
     * TODO
     * Tests whether the .process() method of class CurationPipeline behaves as expected when filtering with a
     * MaxBondCountFilter not considering bonds to implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void processMethodTest_withMaxBondCountFilter_notConsiderImplHs_multipleMolecules() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "c1ccccc1", //12 (6) - filtered
                "CC(=O)O",  // 7 (3)
                "NCC(=O)O", // 9 (4) - filtered
                "C1CCCC1",  //15 (5) - filtered
                "C=CC=C",   // 9 (3)
                "CCO"       // 8 (2)
        );
        boolean[] tmpIsFilteredArray = new boolean[]{true, false, true, true, false, false};
        //
        int tmpMaxBondCount = 3;
        boolean tmpConsiderImplicitHydrogens = false;
        IFilter tmpFilter = new MaxBondCountFilter(tmpMaxBondCount, tmpConsiderImplicitHydrogens);
        //
        TestUtils.curationPipeline_processMethodTest_testsBehaviorOfMethodWithSpecificFilter(
                tmpFilter, tmpAtomContainerSet, tmpIsFilteredArray
        );
    }
    //</editor-fold>

    //<editor-fold desc="withMinBondCountFilter method tests" defaultstate="collapsed">
    /**
     * Tests whether the .withMinBondCountFilter() method of the class CurationPipeline returns the CurationPipeline
     * instance itself and adds a MinBondCountFilter to the list of processing steps.
     */
    @Test
    public void withMinBondCountFilterMethodTest_returnsPipelineInstanceItself_addsFilterToList() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        int tmpInitialListSize = tmpCurationPipeline.getListOfPipelineSteps().size();
        int tmpIntegerParameter = 10;
        boolean tmpBooleanParameter = true;
        CurationPipeline tmpReturnedPipeline = tmpCurationPipeline.withMinBondCountFilter(tmpIntegerParameter, tmpBooleanParameter);
        Assertions.assertSame(tmpCurationPipeline, tmpReturnedPipeline);
        Assertions.assertEquals(tmpInitialListSize + 1, tmpReturnedPipeline.getListOfPipelineSteps().size());
        Assertions.assertInstanceOf(MinBondCountFilter.class, tmpReturnedPipeline.getListOfPipelineSteps().getLast());
    }

    /**
     * Tests whether the .withMinBondCountFilter() method of the class CurationPipeline throws an IllegalArgumentException
     * if the given integer parameter is of a negative value.
     */
    @Test
    public void withMinBondCountFilterMethodTest_negativeThresholdValue_throwsIllegalArgumentException() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    int tmpNegativeMinBondCount = -1;
                    new CurationPipeline().withMinBondCountFilter(tmpNegativeMinBondCount, true);
                }
        );
    }

    //TODO: removed test filterMethodTest_withMinBondCountFilter_considerImplHs_multipleMolecules() due to the usage of FilterID

    //TODO: removed test filterMethodTest_withMinBondCountFilter_notConsiderImplHs_multipleMolecules() due to the usage of FilterID
    //</editor-fold>

    //<editor-fold desc="withMaxBondsOfSpecificBondOrderFilter method tests" defaultstate="collapsed">
    /**
     * Tests whether the .withMaxBondsOfSpecificBondOrderFilter() method of the class CurationPipeline returns the
     * CurationPipeline instance itself and adds a MaxBondsOfSpecificBondOrderFilter to the list of processing steps.
     */
    @Test
    public void withMaxBondsOfSpecificBondOrderFilterMethodTest_returnsPipelineInstanceItself_addsFilterToList() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        int tmpInitialListSize = tmpCurationPipeline.getListOfPipelineSteps().size();
        IBond.Order tmpBondOrder = IBond.Order.UNSET;
        int tmpIntegerParameter = 10;
        boolean tmpBooleanParameter = true;
        CurationPipeline tmpReturnedPipeline = tmpCurationPipeline.withMaxBondsOfSpecificBondOrderFilter(
                tmpBondOrder, tmpIntegerParameter, tmpBooleanParameter
        );
        Assertions.assertSame(tmpCurationPipeline, tmpReturnedPipeline);
        Assertions.assertEquals(tmpInitialListSize + 1, tmpReturnedPipeline.getListOfPipelineSteps().size());
        Assertions.assertInstanceOf(
                MaxBondsOfSpecificBondOrderFilter.class,
                tmpReturnedPipeline.getListOfPipelineSteps().getLast()
        );
    }

    /**
     * Tests whether the .withMaxBondsOfSpecificBondOrderFilter() method of the class CurationPipeline throws an
     * IllegalArgumentException if the given integer parameter is of a negative value.
     */
    @Test
    public void withMaxBondsOfSpecificBondOrderFilterMethodTest_negativeThresholdValue_throwsIllegalArgumentException() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    IBond.Order tmpBondOrder = IBond.Order.UNSET;
                    int tmpNegativeMaxBondCount = -1;
                    new CurationPipeline().withMaxBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpNegativeMaxBondCount, true);
                }
        );
    }

    /**
     * TODO
     * Tests whether the .process() method of class CurationPipeline behaves as expected when filtering with a
     * MaxBondsOfSpecificBondOrderFilter with bond order single and considering bonds to implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void processMethodTest_withMaxBondsOfSpecificBondOrderFilter_bondOrderSingle_considerImplHs() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "c1ccccc1", // 9 (3) - filtered
                "CC(=O)O",  // 6 (2)
                "O",        // 2 (0)
                "CCO",      // 8 (2) - filtered
                "C=CC=C"    // 7 (1)
        );
        boolean[] tmpIsFilteredArray = new boolean[]{true, false, false, true, false};
        //
        IBond.Order tmpBondOrder = IBond.Order.SINGLE;
        int tmpMaxSpecificBondCount = 7;
        boolean tmpConsiderImplicitHydrogens = true;
        IFilter tmpFilter = new MaxBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpMaxSpecificBondCount, tmpConsiderImplicitHydrogens);
        //
        TestUtils.curationPipeline_processMethodTest_testsBehaviorOfMethodWithSpecificFilter(
                tmpFilter, tmpAtomContainerSet, tmpIsFilteredArray
        );
    }

    /**
     * TODO
     * Tests whether the .process() method of class CurationPipeline behaves as expected when filtering with a
     * MaxBondsOfSpecificBondOrderFilter with bond order single and not considering bonds to implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void processMethodTest_withMaxBondsOfSpecificBondOrderFilter_notConsiderImplHs_multipleMolecules() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "c1ccccc1", // 9 (3) - filtered
                "CC(=O)O",  // 6 (2) - filtered
                "O",        // 2 (0)
                "CCO",      // 8 (2) - filtered
                "C=CC=C"    // 7 (1)
        );
        boolean[] tmpIsFilteredArray = new boolean[]{true, true, false, true, false};
        //
        IBond.Order tmpBondOrder = IBond.Order.SINGLE;
        int tmpMaxSpecificBondCount = 1;
        boolean tmpConsiderImplicitHydrogens = false;
        IFilter tmpFilter = new MaxBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpMaxSpecificBondCount, tmpConsiderImplicitHydrogens);
        //
        TestUtils.curationPipeline_processMethodTest_testsBehaviorOfMethodWithSpecificFilter(
                tmpFilter, tmpAtomContainerSet, tmpIsFilteredArray
        );
    }

    /**
     * TODO
     * Tests whether the .process() method of class CurationPipeline behaves as expected when filtering with a
     * MaxBondsOfSpecificBondOrderFilter with bond order double; test is exemplary for filtering on other bond orders.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void processMethodTest_withMaxBondsOfSpecificBondOrderFilter_bondOrderDouble_exemplaryForFilteringOnOtherBondOrders() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "CC(=O)O",  // 1
                "c1ccccc1", // 3 - filtered
                "NCC(=O)O", // 1
                "CCO",      // 0
                "C=CC=C"    // 2 - filtered
        );
        boolean[] tmpIsFilteredArray = new boolean[]{false, true, false, false, true};
        //
        IBond.Order tmpBondOrder = IBond.Order.DOUBLE;
        int tmpMaxSpecificBondCount = 1;
        boolean tmpConsiderImplicitHydrogens = true;    //can be ignored
        IFilter tmpFilter = new MaxBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpMaxSpecificBondCount, tmpConsiderImplicitHydrogens);
        //
        TestUtils.curationPipeline_processMethodTest_testsBehaviorOfMethodWithSpecificFilter(
                tmpFilter, tmpAtomContainerSet, tmpIsFilteredArray
        );
    }
    //</editor-fold>

    //<editor-fold desc="withMinBondsOfSpecificBondOrderFilter method tests" defaultstate="collapsed">
    /**
     * Tests whether the .withMinBondsOfSpecificBondOrderFilter() method of the class CurationPipeline returns the
     * CurationPipeline instance itself and adds a MinBondsOfSpecificBondOrderFilter to the list of processing steps.
     */
    @Test
    public void withMinBondsOfSpecificBondOrderFilterMethodTest_returnsPipelineInstanceItself_addsFilterToList() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        int tmpInitialListSize = tmpCurationPipeline.getListOfPipelineSteps().size();
        IBond.Order tmpBondOrder = IBond.Order.UNSET;
        int tmpIntegerParameter = 10;
        boolean tmpBooleanParameter = true;
        CurationPipeline tmpReturnedPipeline = tmpCurationPipeline.withMinBondsOfSpecificBondOrderFilter(
                tmpBondOrder, tmpIntegerParameter, tmpBooleanParameter
        );
        Assertions.assertSame(tmpCurationPipeline, tmpReturnedPipeline);
        Assertions.assertEquals(tmpInitialListSize + 1, tmpReturnedPipeline.getListOfPipelineSteps().size());
        Assertions.assertInstanceOf(
                MinBondsOfSpecificBondOrderFilter.class,
                tmpReturnedPipeline.getListOfPipelineSteps().getLast()
        );
    }

    /**
     * Tests whether the .withMinBondsOfSpecificBondOrderFilter() method of the class CurationPipeline throws an
     * IllegalArgumentException if the given integer parameter is of a negative value.
     */
    @Test
    public void withMinBondsOfSpecificBondOrderFilterMethodTest_negativeThreshouldValue_throwsIllegalArgumentException() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    IBond.Order tmpBondOrder = IBond.Order.UNSET;
                    int tmpNegativeMinBondCount = -1;
                    new CurationPipeline().withMinBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpNegativeMinBondCount, true);
                }
        );
    }

    /**
     * TODO
     * Tests whether the .process() method of class CurationPipeline behaves as expected when filtering with a
     * MinBondsOfSpecificBondOrderFilter with bond order single and considering bonds to implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void processMethodTest_withMinBondsOfSpecificBondOrderFilter_bondOrderSingle_considerImplHs() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "c1ccccc1", // 9 (3)
                "CC(=O)O",  // 6 (2) - filtered
                "O",        // 2 (0) - filtered
                "CCO",      // 8 (2)
                "C=CC=C"    // 7 (1)
        );
        boolean[] tmpIsFilteredArray = new boolean[]{false, true, true, false, false};
        //
        IBond.Order tmpBondOrder = IBond.Order.SINGLE;
        int tmpMinSpecificBondCount = 7;
        boolean tmpConsiderImplicitHydrogens = true;
        IFilter tmpFilter = new MinBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpMinSpecificBondCount, tmpConsiderImplicitHydrogens);
        //
        TestUtils.curationPipeline_processMethodTest_testsBehaviorOfMethodWithSpecificFilter(
                tmpFilter, tmpAtomContainerSet, tmpIsFilteredArray
        );
    }

    /**
     * TODO
     * Tests whether the .process() method of class CurationPipeline behaves as expected when filtering with a
     * MinBondsOfSpecificBondOrderFilter with bond order single and not considering bonds to implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void processMethodTest_withMinBondsOfSpecificBondOrderFilter_notConsiderImplHs_multipleMolecules() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "c1ccccc1", // 9 (3)
                "CC(=O)O",  // 6 (2)
                "O",        // 2 (0) - filtered
                "CCO",      // 8 (2)
                "C=CC=C"    // 7 (1) - filtered
        );
        boolean[] tmpIsFilteredArray = new boolean[]{false, false, true, false, true};
        //
        IBond.Order tmpBondOrder = IBond.Order.SINGLE;
        int tmpMinSpecificBondCount = 2;
        boolean tmpConsiderImplicitHydrogens = false;
        IFilter tmpFilter = new MinBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpMinSpecificBondCount, tmpConsiderImplicitHydrogens);
        //
        TestUtils.curationPipeline_processMethodTest_testsBehaviorOfMethodWithSpecificFilter(
                tmpFilter, tmpAtomContainerSet, tmpIsFilteredArray
        );
    }

    /**
     * TODO
     * Tests whether the .process() method of class CurationPipeline behaves as expected when filtering with a
     * MinBondsOfSpecificBondOrderFilter with bond order double; test is exemplary for filtering on other bond orders.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void processMethodTest_withMinBondsOfSpecificBondOrderFilter_bondOrderDouble_exemplaryForFilteringOnOtherBondOrders() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "CC(=O)O",  // 1 - filtered
                "c1ccccc1", // 3
                "NCC(=O)O", // 1 - filtered
                "CCO",      // 0 - filtered
                "C=CC=C"    // 2
        );
        boolean[] tmpIsFilteredArray = new boolean[]{true, false, true, true, false};
        //
        IBond.Order tmpBondOrder = IBond.Order.DOUBLE;
        int tmpMinSpecificBondCount = 2;
        boolean tmpConsiderImplicitHydrogens = true;    //can be ignored
        IFilter tmpFilter = new MinBondsOfSpecificBondOrderFilter(tmpBondOrder, tmpMinSpecificBondCount, tmpConsiderImplicitHydrogens);
        //
        TestUtils.curationPipeline_processMethodTest_testsBehaviorOfMethodWithSpecificFilter(
                tmpFilter, tmpAtomContainerSet, tmpIsFilteredArray
        );
    }
    //</editor-fold>

    //<editor-fold desc="withMaxHeavyAtomCountFilter method tests" defaultstate="collapsed">
    /**
     * Tests whether the .withMaxHeavyAtomCountFilter() method of the class CurationPipeline returns the
     * CurationPipeline instance itself and adds a MaxHeavyAtomCountFilter to the list of processing steps.
     */
    @Test
    public void withMaxHeavyAtomCountFilterMethodTest_returnsPipelineInstanceItself_addsFilterToList() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        int tmpInitialListSize = tmpCurationPipeline.getListOfPipelineSteps().size();
        int tmpIntegerParameter = 10;
        CurationPipeline tmpReturnedPipeline = tmpCurationPipeline.withMaxHeavyAtomCountFilter(tmpIntegerParameter);
        Assertions.assertSame(tmpCurationPipeline, tmpReturnedPipeline);
        Assertions.assertEquals(tmpInitialListSize + 1, tmpReturnedPipeline.getListOfPipelineSteps().size());
        Assertions.assertInstanceOf(MaxHeavyAtomCountFilter.class, tmpReturnedPipeline.getListOfPipelineSteps().getLast());
    }

    /**
     * Tests whether the .withMaxHeavyAtomCountFilter() method of the class CurationPipeline throws an
     * IllegalArgumentException if the given integer parameter is of a negative value.
     */
    @Test
    public void withMaxHeavyAtomCountFilterMethodTest_negativeThresholdValue_throwsIllegalArgumentException() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    int tmpNegativeMaxHeavyAtomCount = -1;
                    new CurationPipeline().withMaxHeavyAtomCountFilter(tmpNegativeMaxHeavyAtomCount);
                }
        );
    }

    /**
     * TODO
     * Tests whether the .process() method of class CurationPipeline behaves as expected in filtering process with a
     * MaxHeavyAtomCountFilter; test 1.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void processMethodTest_withMaxHeavyAtomCountFilter_multipleMolecules_test1() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "CC(=O)O",  // 4
                "c1ccccc1", // 6 - filtered
                "C1CCCC1",  // 5 - filtered
                "NCC(=O)O", // 5 - filtered
                "O",        // 1
                "CCO"       // 3
        );
        boolean[] tmpIsFilteredArray = new boolean[]{false, true, true, true, false, false};
        //
        int tmpMaxHeavyAtomCount = 4;
        IFilter tmpFilter = new MaxHeavyAtomCountFilter(tmpMaxHeavyAtomCount);
        //
        TestUtils.curationPipeline_processMethodTest_testsBehaviorOfMethodWithSpecificFilter(
                tmpFilter, tmpAtomContainerSet, tmpIsFilteredArray
        );
    }

    /**
     * TODO
     * Tests whether the .process() method of class CurationPipeline behaves as expected in filtering process with a
     * MaxHeavyAtomCountFilter; test 2.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void processMethodTest_withMaxHeavyAtomCountFilter_multipleMolecules_test2() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "CC(=O)O",  // 4 - filtered
                "c1ccccc1", // 6 - filtered
                "C1CCCC1",  // 5 - filtered
                "O",        // 1
                "NCC(=O)O", // 5 - filtered
                "CCO"       // 3
        );
        boolean[] tmpIsFilteredArray = new boolean[]{true, true, true, false, true, false};
        //
        int tmpMaxHeavyAtomCount = 3;
        IFilter tmpFilter = new MaxHeavyAtomCountFilter(tmpMaxHeavyAtomCount);
        //
        TestUtils.curationPipeline_processMethodTest_testsBehaviorOfMethodWithSpecificFilter(
                tmpFilter, tmpAtomContainerSet, tmpIsFilteredArray
        );
    }
    //</editor-fold>

    //<editor-fold desc="withMinHeavyAtomCountFilter method tests" defaultstate="collapsed">
    /**
     * Tests whether the .withMinHeavyAtomCountFilter() method of the class CurationPipeline returns the
     * CurationPipeline instance itself and adds a MinHeavyAtomCountFilter to the list of processing steps.
     */
    @Test
    public void withMinHeavyAtomCountFilterMethodTest_returnsPipelineInstanceItself_addsFilterToList() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        int tmpInitialListSize = tmpCurationPipeline.getListOfPipelineSteps().size();
        int tmpIntegerParameter = 10;
        CurationPipeline tmpReturnedPipeline = tmpCurationPipeline.withMinHeavyAtomCountFilter(tmpIntegerParameter);
        Assertions.assertSame(tmpCurationPipeline, tmpReturnedPipeline);
        Assertions.assertEquals(tmpInitialListSize + 1, tmpReturnedPipeline.getListOfPipelineSteps().size());
        Assertions.assertInstanceOf(MinHeavyAtomCountFilter.class, tmpReturnedPipeline.getListOfPipelineSteps().getLast());
    }

    /**
     * Tests whether the .withMinHeavyAtomCountFilter() method of the class CurationPipeline throws an
     * IllegalArgumentException if the given integer parameter is of a negative value.
     */
    @Test
    public void withMinHeavyAtomCountFilterMethodTest_negativeThresholdValue_throwsIllegalArgumentException() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    int tmpNegativeMinHeavyAtomCount = -1;
                    new CurationPipeline().withMinHeavyAtomCountFilter(tmpNegativeMinHeavyAtomCount);
                }
        );
    }

    /**
     * TODO
     * Tests whether the .process() method of class CurationPipeline behaves as expected in a filtering process with a
     * MinHeavyAtomCountFilter; test 1.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void processMethodTest_withMinHeavyAtomCountFilter_multipleMolecules_test1() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "CC(=O)O",  // 4 - filtered
                "c1ccccc1", // 6
                "C1CCCC1",  // 5
                "NCC(=O)O", // 5
                "O",        // 1 - filtered
                "CCO"       // 3 - filtered
        );
        boolean[] tmpIsFilteredArray = new boolean[]{true, false, false, false, true, true};
        //
        int tmpMinHeavyAtomCount = 5;
        IFilter tmpFilter = new MinHeavyAtomCountFilter(tmpMinHeavyAtomCount);
        //
        TestUtils.curationPipeline_processMethodTest_testsBehaviorOfMethodWithSpecificFilter(
                tmpFilter, tmpAtomContainerSet, tmpIsFilteredArray
        );
    }

    /**
     * TODO
     * Tests whether the .process() method of class CurationPipeline behaves as expected in a filtering process with a
     * MinHeavyAtomCountFilter; test 2.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void processMethodTest_withMinHeavyAtomCountFilter_multipleMolecules_test2() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "CC(=O)O",  // 4
                "c1ccccc1", // 6
                "C1CCCC1",  // 5
                "O",        // 1 - filtered
                "NCC(=O)O", // 5
                "CCO"       // 3 - filtered
        );
        boolean[] tmpIsFilteredArray = new boolean[]{false, false, false, true, false, true};
        //
        int tmpMinHeavyAtomCount = 4;
        IFilter tmpFilter = new MinHeavyAtomCountFilter(tmpMinHeavyAtomCount);
        //
        TestUtils.curationPipeline_processMethodTest_testsBehaviorOfMethodWithSpecificFilter(
                tmpFilter, tmpAtomContainerSet, tmpIsFilteredArray
        );
    }
    //</editor-fold>

    //<editor-fold desc="withHasAllValidAtomicNumbersFilter method tests" defaultstate="collapsed">
    /**
     * Tests whether the .withHasAllValidAtomicNumbersFilter() method of the class CurationPipeline returns the
     * CurationPipeline instance itself and adds a HasAllValidAtomicNumbersFilter to the list of processing steps.
     */
    @Test
    public void withHasAllValidAtomicNumbersFilterMethodTest_returnsCurationPipelineInstanceItself() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        int tmpInitialListSize = tmpCurationPipeline.getListOfPipelineSteps().size();
        boolean tmpWildcardAtomicNumberIsValid = true;
        CurationPipeline tmpReturnedPipeline = tmpCurationPipeline.withHasAllValidAtomicNumbersFilter(tmpWildcardAtomicNumberIsValid);
        Assertions.assertSame(tmpCurationPipeline, tmpReturnedPipeline);
        Assertions.assertEquals(tmpInitialListSize + 1, tmpReturnedPipeline.getListOfPipelineSteps().size());
        Assertions.assertInstanceOf(HasAllValidAtomicNumbersFilter.class, tmpReturnedPipeline.getListOfPipelineSteps().getLast());
    }

    /**
     * TODO
     * Tests whether the .process() method of class CurationPipeline behaves as expected in a filtering process with a
     * HasAllValidAtomicNumbersFilter; test 2; wildcard atomic number is considered as valid.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void processMethodTest_withHasAllValidAtomicNumbersFilter_multipleMolecules_test1_zeroIsValid() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "C=CC=C",
                "C",
                "O=C=O",
                "c1ccccc1",
                "CCO"
        );
        tmpAtomContainerSet.getAtomContainer(1).getAtom(0).setAtomicNumber(-1);
        tmpAtomContainerSet.getAtomContainer(2).getAtom(0).setAtomicNumber(119);
        tmpAtomContainerSet.getAtomContainer(3).getAtom(2).setAtomicNumber(0);
        tmpAtomContainerSet.getAtomContainer(4).getAtom(1).setAtomicNumber(null);
        boolean[] tmpIsFilteredArray = new boolean[]{
                false,  //all valid
                true,   //atomic number < 0
                true,   //atomic number > 118
                false,  //atomic number = 0
                true    //atomic number is null
        };
        //
        boolean tmpWildcardAtomicNumberIsValid = true;
        //
        IFilter tmpFilter = new HasAllValidAtomicNumbersFilter(tmpWildcardAtomicNumberIsValid);
        TestUtils.curationPipeline_processMethodTest_testsBehaviorOfMethodWithSpecificFilter(
                tmpFilter, tmpAtomContainerSet, tmpIsFilteredArray
        );
    }

    /**
     * TODO
     * Tests whether the .process() method of class CurationPipeline behaves as expected in a filtering process with a
     * HasAllValidAtomicNumbersFilter; test 2; wildcard atomic number is considered as invalid.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void processMethodTest_withHasAllValidAtomicNumbersFilter_multipleMolecules_test2_zeroIsInvalid() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "c1ccccc1",
                "C=CC=C",
                "C",
                "CCO",
                "O=C=O"
        );
        tmpAtomContainerSet.getAtomContainer(1).getAtom(0).setAtomicNumber(-1);
        tmpAtomContainerSet.getAtomContainer(2).getAtom(0).setAtomicNumber(119);
        tmpAtomContainerSet.getAtomContainer(3).getAtom(2).setAtomicNumber(0);
        tmpAtomContainerSet.getAtomContainer(4).getAtom(1).setAtomicNumber(null);
        boolean[] tmpIsFilteredArray = new boolean[]{
                false,  //all valid
                true,   //atomic number < 0
                true,   //atomic number > 118
                true,   //atomic number = 0
                true    //atomic number is null
        };
        //
        boolean tmpWildcardAtomicNumberIsValid = false;
        //
        IFilter tmpFilter = new HasAllValidAtomicNumbersFilter(tmpWildcardAtomicNumberIsValid);
        TestUtils.curationPipeline_processMethodTest_testsBehaviorOfMethodWithSpecificFilter(
                tmpFilter, tmpAtomContainerSet, tmpIsFilteredArray
        );
    }
    //</editor-fold>

    //<editor-fold desc="withHasInvalidAtomicNumbersFilter method tests" defaultstate="collapsed">
    /**
     * Tests whether the .withHasInvalidAtomicNumbersFilter() method of the class CurationPipeline returns the
     * CurationPipeline instance itself and adds a HasInvalidAtomicNumbersFilter to the list of processing steps.
     */
    @Test
    public void withHasInvalidAtomicNumbersFilterMethodTest_returnsCurationPipelineInstanceItself() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        int tmpInitialListSize = tmpCurationPipeline.getListOfPipelineSteps().size();
        boolean tmpWildcardAtomicNumberIsValid = true;
        CurationPipeline tmpReturnedPipeline = tmpCurationPipeline.withHasInvalidAtomicNumbersFilter(tmpWildcardAtomicNumberIsValid);
        Assertions.assertSame(tmpCurationPipeline, tmpReturnedPipeline);
        Assertions.assertEquals(tmpInitialListSize + 1, tmpReturnedPipeline.getListOfPipelineSteps().size());
        Assertions.assertInstanceOf(HasInvalidAtomicNumbersFilter.class, tmpReturnedPipeline.getListOfPipelineSteps().getLast());
    }

    /**
     * TODO
     * Tests whether the .process() method of class CurationPipeline behaves as expected in a filtering process with a
     * HasInvalidAtomicNumbersFilter; test 2; wildcard atomic number is considered as valid.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void processMethodTest_withHasInvalidAtomicNumbersFilter_multipleMolecules_test1_zeroIsValid() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "C=CC=C",
                "C",
                "O=C=O",
                "c1ccccc1",
                "CCO"
        );
        tmpAtomContainerSet.getAtomContainer(1).getAtom(0).setAtomicNumber(-1);
        tmpAtomContainerSet.getAtomContainer(2).getAtom(0).setAtomicNumber(119);
        tmpAtomContainerSet.getAtomContainer(3).getAtom(2).setAtomicNumber(0);
        tmpAtomContainerSet.getAtomContainer(4).getAtom(1).setAtomicNumber(null);
        boolean[] tmpIsFilteredArray = new boolean[]{
                true,   //all valid
                false,  //atomic number < 0
                false,  //atomic number > 118
                true,   //atomic number = 0
                false   //atomic number is null
        };
        //
        boolean tmpWildcardAtomicNumberIsValid = true;
        //
        IFilter tmpFilter = new HasInvalidAtomicNumbersFilter(tmpWildcardAtomicNumberIsValid);
        TestUtils.curationPipeline_processMethodTest_testsBehaviorOfMethodWithSpecificFilter(
                tmpFilter, tmpAtomContainerSet, tmpIsFilteredArray
        );
    }

    /**
     * TODO
     * Tests whether the .process() method of class CurationPipeline behaves as expected in a filtering process with a
     * HasInvalidAtomicNumbersFilter; test 2; wildcard atomic number is considered as invalid.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void processMethodTest_withHasInvalidAtomicNumbersFilter_multipleMolecules_test2_zeroIsInvalid() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "c1ccccc1",
                "C=CC=C",
                "C",
                "CCO",
                "O=C=O"
        );
        tmpAtomContainerSet.getAtomContainer(1).getAtom(0).setAtomicNumber(-1);
        tmpAtomContainerSet.getAtomContainer(2).getAtom(0).setAtomicNumber(119);
        tmpAtomContainerSet.getAtomContainer(3).getAtom(2).setAtomicNumber(0);
        tmpAtomContainerSet.getAtomContainer(4).getAtom(1).setAtomicNumber(null);
        boolean[] tmpIsFilteredArray = new boolean[]{
                true,   //all valid
                false,  //atomic number < 0
                false,  //atomic number > 118
                false,  //atomic number = 0
                false   //atomic number is null
        };
        //
        boolean tmpWildcardAtomicNumberIsValid = false;
        //
        IFilter tmpFilter = new HasInvalidAtomicNumbersFilter(tmpWildcardAtomicNumberIsValid);
        TestUtils.curationPipeline_processMethodTest_testsBehaviorOfMethodWithSpecificFilter(
                tmpFilter, tmpAtomContainerSet, tmpIsFilteredArray
        );
    }
    //</editor-fold>

    //<editor-fold desc="withMaxMolecularMassFilter method tests" defaultstate="collapsed">
    /**
     * Tests whether the .withMaxMolecularMassFilter() method of the class CurationPipeline with computation flavour
     * parameter returns the CurationPipeline instance itself and adds a MaxMolecularMassFilter to the list of
     * processing steps.
     */
    @Test
    public void withMaxMolecularMassFilterMethodTest_withFlavour_returnsPipelineInstanceItselfAndAddsFilterToList() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        int tmpInitialListSize = tmpCurationPipeline.getListOfPipelineSteps().size();
        double tmpThresholdValue = 10.0;
        MassComputationFlavours tmpFlavour = MassComputationFlavours.MOL_WEIGHT;
        CurationPipeline tmpReturnedPipeline = tmpCurationPipeline.withMaxMolecularMassFilter(tmpThresholdValue, tmpFlavour);
        Assertions.assertSame(tmpCurationPipeline, tmpReturnedPipeline);
        Assertions.assertEquals(tmpInitialListSize + 1, tmpReturnedPipeline.getListOfPipelineSteps().size());
        Assertions.assertInstanceOf(MaxMolecularMassFilter.class, tmpReturnedPipeline.getListOfPipelineSteps().getLast());
    }

    /**
     * Tests whether the .withMaxMolecularMassFilter() method of the class CurationPipeline without computation flavour
     * parameter returns the CurationPipeline instance itself and adds a MaxMolecularMassFilter to the list of
     * processing steps.
     */
    @Test
    public void withMaxMolecularMassFilterMethodTest_withoutFlavour_returnsPipelineInstanceItselfAndAddsFilterToList() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        int tmpInitialListSize = tmpCurationPipeline.getListOfPipelineSteps().size();
        double tmpThresholdValue = 10.0;
        CurationPipeline tmpReturnedPipeline = tmpCurationPipeline.withMaxMolecularMassFilter(tmpThresholdValue);
        Assertions.assertSame(tmpCurationPipeline, tmpReturnedPipeline);
        Assertions.assertEquals(tmpInitialListSize + 1, tmpReturnedPipeline.getListOfPipelineSteps().size());
        Assertions.assertInstanceOf(MaxMolecularMassFilter.class, tmpReturnedPipeline.getListOfPipelineSteps().getLast());
    }

    /**
     * Tests whether the MaxMolecularMassFilter added to the listOfPipelineSteps by the .withMaxMolecularMassFilter()
     * method of the class CurationPipeline with no MassComputationFlavours parameter has {@link MassComputationFlavours#MOL_WEIGHT}
     * set as mass computation flavour.
     */
    @Test
    public void withMaxMolecularMassFilterMethodTest_noFlavour_newFilterHasMolWeightAsMassComputationFlavour() {
        double tmpThresholdValue = 10.0;
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withMaxMolecularMassFilter(tmpThresholdValue);
        Assertions.assertEquals(
                MassComputationFlavours.MOL_WEIGHT,
                ((MaxMolecularMassFilter) tmpCurationPipeline.getListOfPipelineSteps().getLast()).getMassComputationFlavour()
        );
    }

    /**
     * Tests whether both .withMaxMolecularMassFilter() methods of the class CurationPipeline throw an
     * IllegalArgumentException if the given max molecular mass threshold value is of a negative value.
     */
    @Test
    public void withMaxMolecularMassFilterMethodsTest_negativeThresholdValue_throwIllegalArgumentException() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    double tmpThresholdValue = -0.1;
                    MassComputationFlavours tmpFlavour = MassComputationFlavours.MOL_WEIGHT;
                    new CurationPipeline().withMaxMolecularMassFilter(tmpThresholdValue, tmpFlavour);
                }
        );
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    double tmpThresholdValue = -5.0;
                    new CurationPipeline().withMaxMolecularMassFilter(tmpThresholdValue);
                }
        );
    }

    /**
     * Tests whether the .withMaxMolecularMassFilter() method of the class CurationPipeline with MassComputationFlavours
     * parameter throws a NullPointerException if the given mass computation flavour is null.
     */
    @Test
    public void withMaxMolecularMassFilterMethodTest_flavourIsNull_throwsNullPointerException() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    double tmpThresholdValue = 10.0;
                    MassComputationFlavours tmpFlavour = null;
                    new CurationPipeline().withMaxMolecularMassFilter(tmpThresholdValue, tmpFlavour);
                }
        );
    }
    //</editor-fold>

    //<editor-fold desc="withMinMolecularMassFilter method tests" defaultstate="collapsed">
    /**
     * Tests whether the .withMinMolecularMassFilter() method of the class CurationPipeline with computation flavour
     * parameter returns the CurationPipeline instance itself and adds a MinMolecularMassFilter to the list of
     * processing steps.
     */
    @Test
    public void withMinMolecularMassFilterMethodTest_withFlavour_returnsPipelineInstanceItselfAndAddsFilterToList() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        int tmpInitialListSize = tmpCurationPipeline.getListOfPipelineSteps().size();
        double tmpThresholdValue = 10.0;
        MassComputationFlavours tmpFlavour = MassComputationFlavours.MOL_WEIGHT;
        CurationPipeline tmpReturnedPipeline = tmpCurationPipeline.withMinMolecularMassFilter(tmpThresholdValue, tmpFlavour);
        Assertions.assertSame(tmpCurationPipeline, tmpReturnedPipeline);
        Assertions.assertEquals(tmpInitialListSize + 1, tmpReturnedPipeline.getListOfPipelineSteps().size());
        Assertions.assertInstanceOf(MinMolecularMassFilter.class, tmpReturnedPipeline.getListOfPipelineSteps().getLast());
    }

    /**
     * Tests whether the .withMinMolecularMassFilter() method of the class CurationPipeline without computation flavour
     * parameter returns the CurationPipeline instance itself and adds a MinMolecularMassFilter to the list of
     * processing steps.
     */
    @Test
    public void withMinMolecularMassFilterMethodTest_withoutFlavour_returnsPipelineInstanceItselfAndAddsFilterToList() {
        CurationPipeline tmpCurationPipeline = new CurationPipeline();
        int tmpInitialListSize = tmpCurationPipeline.getListOfPipelineSteps().size();
        double tmpThresholdValue = 10.0;
        CurationPipeline tmpReturnedPipeline = tmpCurationPipeline.withMinMolecularMassFilter(tmpThresholdValue);
        Assertions.assertSame(tmpCurationPipeline, tmpReturnedPipeline);
        Assertions.assertEquals(tmpInitialListSize + 1, tmpReturnedPipeline.getListOfPipelineSteps().size());
        Assertions.assertInstanceOf(MinMolecularMassFilter.class, tmpReturnedPipeline.getListOfPipelineSteps().getLast());
    }

    /**
     * Tests whether the MinMolecularMassFilter added to the listOfPipelineSteps by the .withMinMolecularMassFilter()
     * method of the class CurationPipeline with no computation flavour parameter has {@link MassComputationFlavours#MOL_WEIGHT}
     * set as mass computation flavour.
     */
    @Test
    public void withMinMolecularMassFilterMethodTest_noFlavour_newFilterHasMolWeightAsMassComputationFlavour() {
        double tmpThresholdValue = 10.0;
        CurationPipeline tmpCurationPipeline = new CurationPipeline().withMaxMolecularMassFilter(tmpThresholdValue);
        Assertions.assertEquals(
                MassComputationFlavours.MOL_WEIGHT,
                ((MaxMolecularMassFilter) tmpCurationPipeline.getListOfPipelineSteps().getLast()).getMassComputationFlavour()
        );
    }

    /**
     * Tests whether both .withMinMolecularMassFilter() methods of the class CurationPipeline with throw an
     * IllegalArgumentException if the given min molecular mass threshold value is of a negative value.
     */
    @Test
    public void withMinMolecularMassFilterMethodsTest_negativeThresholdValue_throwIllegalArgumentException() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    double tmpThresholdValue = -0.1;
                    MassComputationFlavours tmpFlavour = MassComputationFlavours.MOL_WEIGHT;
                    new CurationPipeline().withMinMolecularMassFilter(tmpThresholdValue, tmpFlavour);
                }
        );
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    double tmpThresholdValue = -5.0;
                    new CurationPipeline().withMinMolecularMassFilter(tmpThresholdValue);
                }
        );
    }

    /**
     * Tests whether the .withMinMolecularMassFilter() method of the class CurationPipeline with MassComputationFlavours
     * parameter throws a NullPointerException if the given mass computation flavour is null.
     */
    @Test
    public void withMinMolecularMassFilterMethodTest_flavourIsNull_throwsNullPointerException() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    double tmpThresholdValue = 10.0;
                    MassComputationFlavours tmpFlavour = null;
                    new CurationPipeline().withMinMolecularMassFilter(tmpThresholdValue, tmpFlavour);
                }
        );
    }
    //</editor-fold>

}
