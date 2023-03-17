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

import de.unijena.cheminf.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainerSet;

public class WithFilterConvenienceMethodsTest {

    /*
    TODO: add tests for every new filter class
     */

    /**
     * Tests whether the value returned by the .withMaxBondCountFilter() method of the class FilterPipeline is an
     * FilterPipeline instance.
     */
    @Test
    public void withMaxBondCountFilterMethodTest_returnsFilterPipelineInstance() {
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        int tmpIntegerParameter = 10;
        boolean tmpBooleanParameter = true;
        Assertions.assertSame(tmpFilterPipeline, tmpFilterPipeline.withMaxBondCountFilter(tmpIntegerParameter, tmpBooleanParameter));
    }

    /**
     * Tests whether the instance returned by the .withMaxBondCountFilter() method of the class FilterPipeline is the
     * FilterPipeline instance the method was called of.
     */
    @Test
    public void withMaxBondCountFilterMethodTest_returnsFilterPipelineInstanceItWasCalledOf() {
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        int tmpIntegerParameter = 10;
        boolean tmpBooleanParameter = true;
        Assertions.assertSame(tmpFilterPipeline, tmpFilterPipeline.withMaxBondCountFilter(tmpIntegerParameter, tmpBooleanParameter));
    }

    /**
     * Tests whether the listOfSelectedFilters of the FilterPipeline instance returned by the .withMaxBondCountFilter()
     * method of the class FilterPipeline was extended by an instance of MaxBondCountFilter.
     */
    @Test
    public void withMaxBondCountFilterMethodTest_checksWhetherListOfSelectedFiltersWasExtendedByInstanceOfMaxBondCountFilter() {
        int tmpIntegerParameter = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        FilterPipeline tmpFilterPipeline = new FilterPipeline().withMaxBondCountFilter(tmpIntegerParameter, tmpConsiderImplicitHydrogens);
        Assertions.assertInstanceOf(MaxBondCountFilter.class, tmpFilterPipeline.getListOfSelectedFilters().getLast());
    }

    /**
     * Tests whether the MaxBondCountFilter added to the listOfSelectedFilters by the .withMaxBondCountFilter() method
     * of the class FilterPipeline contains the given max bond count threshold value.
     */
    @Test
    public void withMaxBondCountFilterMethodTest_checksWhetherAddedMaxBondCountFilterHasGivenThresholdSet_twoTests() {
        int tmpThresholdValue = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        FilterPipeline tmpFilterPipeline = new FilterPipeline().withMaxBondCountFilter(tmpThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertEquals(tmpThresholdValue, ((MaxBondCountFilter) tmpFilterPipeline.getListOfSelectedFilters().getLast()).getMaxBondCount());
        tmpThresholdValue = 20;
        tmpFilterPipeline = new FilterPipeline().withMaxBondCountFilter(tmpThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertEquals(tmpThresholdValue, ((MaxBondCountFilter) tmpFilterPipeline.getListOfSelectedFilters().getLast()).getMaxBondCount());
    }

    /**
     * Tests whether the MaxBondCountFilter added to the listOfSelectedFilters by the .withMaxBondCountFilter() method
     * of the class FilterPipeline has the given boolean value set.
     */
    @Test
    public void withMaxBondCountFilterMethodTest_checksWhetherAddedMaxBondCountFilterHasGivenBooleanConsiderImplHsSet_twoTests() {
        int tmpThresholdValue = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        FilterPipeline tmpFilterPipeline = new FilterPipeline().withMaxBondCountFilter(tmpThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertTrue(((MaxBondCountFilter) tmpFilterPipeline.getListOfSelectedFilters().getLast()).isConsiderImplicitHydrogens());
        tmpConsiderImplicitHydrogens = false;
        tmpFilterPipeline = new FilterPipeline().withMaxBondCountFilter(tmpThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertFalse(((MaxBondCountFilter) tmpFilterPipeline.getListOfSelectedFilters().getLast()).isConsiderImplicitHydrogens());
    }

    /**
     * Tests whether the .withMaxBondCountFilter() method of the class FilterPipeline throws an IllegalArgumentException
     * if the given integer parameter is of a negative value.
     */
    @Test
    public void withMaxBondCountFilterMethodTest_throwsIllegalArgumentExceptionIfGivenMaxBondCountIsNegative() {    //TODO: do so?
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    int tmpNegativeMaxBondCount = -1;
                    new FilterPipeline().withMaxBondCountFilter(tmpNegativeMaxBondCount, true);
                }
        );
    }

    /**
     * Tests whether the .filter() method of class FilterPipeline behaves as expected when filtering with a
     * MaxBondCountFilter considering bonds to implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_withMaxBondCountFilter_considerImplHs_multipleMolecules() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "CC(=O)O",  // 7 (3)
                "c1ccccc1", //12 (6) - filtered
                "C1CCCC1",  //15 (5) - filtered
                "NCC(=O)O", // 9 (4)
                "C=CC=C",   // 9 (3)
                "CCO"       // 8 (2)
        );
        boolean[] tmpGotFilteredArray = new boolean[]{false, true, true, false, false, false};
        //
        int tmpMaxBondCount = 9;
        boolean tmpConsiderImplicitHydrogens = true;
        FilterPipeline tmpFilterPipeline = new FilterPipeline().withMaxBondCountFilter(tmpMaxBondCount, tmpConsiderImplicitHydrogens);
        IAtomContainerSet tmpFilteredACSet = tmpFilterPipeline.filter(tmpAtomContainerSet);
        int tmpIndexInFilteredSet = 0;
        int tmpFilterID;
        for (int i = 0; i < tmpGotFilteredArray.length; i++) {
            tmpFilterID = tmpAtomContainerSet.getAtomContainer(i).getProperty(FilterPipeline.FILTER_ID_PROPERTY_NAME);
            if (!tmpGotFilteredArray[i]) {
                Assertions.assertEquals(FilterPipeline.NOT_FILTERED_VALUE, tmpFilterID);
                Assertions.assertSame(tmpAtomContainerSet.getAtomContainer(i), tmpFilteredACSet.getAtomContainer(tmpIndexInFilteredSet));
                tmpIndexInFilteredSet++;
                continue;
            }
            Assertions.assertTrue(tmpFilterID != FilterPipeline.NOT_FILTERED_VALUE);
        }
    }

    /**
     * Tests whether the .filter() method of class FilterPipeline behaves as expected when filtering with a
     * MaxBondCountFilter not considering bonds to implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_withMaxBondCountFilter_notConsiderImplHs_multipleMolecules() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "c1ccccc1", //12 (6) - filtered
                "CC(=O)O",  // 7 (3)
                "NCC(=O)O", // 9 (4) - filtered
                "C1CCCC1",  //15 (5) - filtered
                "C=CC=C",   // 9 (3)
                "CCO"       // 8 (2)
        );
        boolean[] tmpGotFilteredArray = new boolean[]{true, false, true, true, false, false};
        //
        int tmpMaxBondCount = 3;
        boolean tmpConsiderImplicitHydrogens = false;
        FilterPipeline tmpFilterPipeline = new FilterPipeline().withMaxBondCountFilter(tmpMaxBondCount, tmpConsiderImplicitHydrogens);
        IAtomContainerSet tmpFilteredACSet = tmpFilterPipeline.filter(tmpAtomContainerSet);
        int tmpIndexInFilteredSet = 0;
        int tmpFilterID;
        for (int i = 0; i < tmpGotFilteredArray.length; i++) {
            tmpFilterID = tmpAtomContainerSet.getAtomContainer(i).getProperty(FilterPipeline.FILTER_ID_PROPERTY_NAME);
            if (!tmpGotFilteredArray[i]) {
                Assertions.assertEquals(FilterPipeline.NOT_FILTERED_VALUE, tmpFilterID);
                Assertions.assertSame(tmpAtomContainerSet.getAtomContainer(i), tmpFilteredACSet.getAtomContainer(tmpIndexInFilteredSet));
                tmpIndexInFilteredSet++;
                continue;
            }
            Assertions.assertTrue(tmpFilterID != FilterPipeline.NOT_FILTERED_VALUE);
        }
    }

    /**
     * Tests whether the value returned by the .withMinBondCountFilter() method of the class FilterPipeline is an
     * FilterPipeline instance.
     */
    @Test
    public void withMinBondCountFilterMethodTest_returnsFilterPipelineInstance() {
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        int tmpIntegerParameter = 10;
        boolean tmpBooleanParameter = true;
        Assertions.assertSame(tmpFilterPipeline, tmpFilterPipeline.withMinBondCountFilter(tmpIntegerParameter, tmpBooleanParameter));
    }

    /**
     * Tests whether the instance returned by the .withMinBondCountFilter() method of the class FilterPipeline is the
     * FilterPipeline instance the method was called of.
     */
    @Test
    public void withMinBondCountFilterMethodTest_returnsFilterPipelineInstanceItWasCalledOf() {
        FilterPipeline tmpFilterPipeline = new FilterPipeline();
        int tmpIntegerParameter = 10;
        boolean tmpBooleanParameter = true;
        Assertions.assertSame(tmpFilterPipeline, tmpFilterPipeline.withMinBondCountFilter(tmpIntegerParameter, tmpBooleanParameter));
    }

    /**
     * Tests whether the listOfSelectedFilters of the FilterPipeline instance returned by the .withMinBondCountFilter()
     * method of the class FilterPipeline was extended by an instance of MinBondCountFilter.
     */
    @Test
    public void withMinBondCountFilterMethodTest_checksWhetherListOfSelectedFiltersWasExtendedByInstanceOfMinBondCountFilter() {
        int tmpIntegerParameter = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        FilterPipeline tmpFilterPipeline = new FilterPipeline().withMinBondCountFilter(tmpIntegerParameter, tmpConsiderImplicitHydrogens);
        Assertions.assertInstanceOf(MinBondCountFilter.class, tmpFilterPipeline.getListOfSelectedFilters().getLast());
    }

    /**
     * Tests whether the MinBondCountFilter added to the listOfSelectedFilters by the .withMinBondCountFilter() method
     * of the class FilterPipeline contains the given min bond count threshold value.
     */
    @Test
    public void withMinBondCountFilterMethodTest_checksWhetherAddedMinBondCountFilterHasGivenThresholdSet_twoTests() {
        int tmpThresholdValue = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        FilterPipeline tmpFilterPipeline = new FilterPipeline().withMinBondCountFilter(tmpThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertEquals(tmpThresholdValue, ((MinBondCountFilter) tmpFilterPipeline.getListOfSelectedFilters().getLast()).getMinBondCount());
        tmpThresholdValue = 20;
        tmpFilterPipeline = new FilterPipeline().withMinBondCountFilter(tmpThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertEquals(tmpThresholdValue, ((MinBondCountFilter) tmpFilterPipeline.getListOfSelectedFilters().getLast()).getMinBondCount());
    }

    /**
     * Tests whether the MinBondCountFilter added to the listOfSelectedFilters by the .withMinBondCountFilter() method
     * of the class FilterPipeline has the given boolean value set.
     */
    @Test
    public void withMinBondCountFilterMethodTest_checksWhetherAddedMinBondCountFilterHasGivenBooleanConsiderImplHsSet_twoTests() {
        int tmpThresholdValue = 10;
        boolean tmpConsiderImplicitHydrogens = true;
        FilterPipeline tmpFilterPipeline = new FilterPipeline().withMinBondCountFilter(tmpThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertTrue(((MinBondCountFilter) tmpFilterPipeline.getListOfSelectedFilters().getLast()).isConsiderImplicitHydrogens());
        tmpConsiderImplicitHydrogens = false;
        tmpFilterPipeline = new FilterPipeline().withMinBondCountFilter(tmpThresholdValue, tmpConsiderImplicitHydrogens);
        Assertions.assertFalse(((MinBondCountFilter) tmpFilterPipeline.getListOfSelectedFilters().getLast()).isConsiderImplicitHydrogens());
    }

    /**
     * Tests whether the .withMinBondCountFilter() method of the class FilterPipeline throws an IllegalArgumentException
     * if the given integer parameter is of a negative value.
     */
    @Test
    public void withMinBondCountFilterMethodTest_throwsIllegalArgumentExceptionIfGivenMinBondCountIsNegative() {    //TODO: do so?
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    int tmpNegativeMinBondCount = -1;
                    new FilterPipeline().withMinBondCountFilter(tmpNegativeMinBondCount, true);
                }
        );
    }

    /**
     * Tests whether the .filter() method of class FilterPipeline behaves as expected when filtering with a
     * MinBondCountFilter considering bonds to implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_withMinBondCountFilter_considerImplHs_multipleMolecules() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "CC(=O)O",  // 7 (3) - filtered
                "c1ccccc1", //12 (6)
                "C1CCCC1",  //15 (5)
                "NCC(=O)O", // 9 (4)
                "C=CC=C",   // 9 (3)
                "CCO"       // 8 (2) - filtered
        );
        boolean[] tmpGotFilteredArray = new boolean[]{true, false, false, false, false, true};
        //
        int tmpMinBondCount = 9;
        boolean tmpConsiderImplicitHydrogens = true;
        FilterPipeline tmpFilterPipeline = new FilterPipeline().withMinBondCountFilter(tmpMinBondCount, tmpConsiderImplicitHydrogens);
        IAtomContainerSet tmpFilteredACSet = tmpFilterPipeline.filter(tmpAtomContainerSet);
        int tmpIndexInFilteredSet = 0;
        int tmpFilterID;
        for (int i = 0; i < tmpGotFilteredArray.length; i++) {
            tmpFilterID = tmpAtomContainerSet.getAtomContainer(i).getProperty(FilterPipeline.FILTER_ID_PROPERTY_NAME);
            if (!tmpGotFilteredArray[i]) {
                Assertions.assertEquals(FilterPipeline.NOT_FILTERED_VALUE, tmpFilterID);
                Assertions.assertSame(tmpAtomContainerSet.getAtomContainer(i), tmpFilteredACSet.getAtomContainer(tmpIndexInFilteredSet));
                tmpIndexInFilteredSet++;
                continue;
            }
            Assertions.assertTrue(tmpFilterID != FilterPipeline.NOT_FILTERED_VALUE);
        }
    }

    /**
     * Tests whether the .filter() method of class FilterPipeline behaves as expected when filtering with a
     * MinBondCountFilter not considering bonds to implicit hydrogen atoms.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void filterMethodTest_withMinBondCountFilter_notConsiderImplHs_multipleMolecules() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "c1ccccc1", //12 (6)
                "CC(=O)O",  // 7 (3) - filtered
                "NCC(=O)O", // 9 (4)
                "C1CCCC1",  //15 (5)
                "C=CC=C",   // 9 (3) - filtered
                "CCO"       // 8 (2) - filtered
        );
        boolean[] tmpGotFilteredArray = new boolean[]{false, true, false, false, true, true};
        //
        int tmpMinBondCount = 4;
        boolean tmpConsiderImplicitHydrogens = false;
        FilterPipeline tmpFilterPipeline = new FilterPipeline().withMinBondCountFilter(tmpMinBondCount, tmpConsiderImplicitHydrogens);
        IAtomContainerSet tmpFilteredACSet = tmpFilterPipeline.filter(tmpAtomContainerSet);
        int tmpIndexInFilteredSet = 0;
        int tmpFilterID;
        for (int i = 0; i < tmpGotFilteredArray.length; i++) {
            tmpFilterID = tmpAtomContainerSet.getAtomContainer(i).getProperty(FilterPipeline.FILTER_ID_PROPERTY_NAME);
            if (!tmpGotFilteredArray[i]) {
                Assertions.assertEquals(FilterPipeline.NOT_FILTERED_VALUE, tmpFilterID);
                Assertions.assertSame(tmpAtomContainerSet.getAtomContainer(i), tmpFilteredACSet.getAtomContainer(tmpIndexInFilteredSet));
                tmpIndexInFilteredSet++;
                continue;
            }
            Assertions.assertTrue(tmpFilterID != FilterPipeline.NOT_FILTERED_VALUE);
        }
    }

}
