package de.unijena.cheminf.filter;

import de.unijena.cheminf.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 * Test class for .getsFiltered() method of class FilterPipeline.
 */
public class GetsFilteredMethodTest {

    //TODO: adopt or remove!!

    /**
     * Tests whether the .getsFiltered() method of the class FilterPipeline returns a boolean value.
     */
    /*@Test
    public void getsFilteredMethodTest_returnsBooleanValue() {
        IAtomContainer tmpAtomContainer = new AtomContainer();
        FilterPipeline.FilterTypes tmpFilterType = FilterPipeline.FilterTypes.NONE;
        int tmpIntegerParameter = 0;
        Assertions.assertInstanceOf(Boolean.class, new FilterPipeline().getsFiltered(tmpAtomContainer, tmpFilterType, tmpIntegerParameter));
    }*/

    /**
     * Tests whether the .getsFiltered() method of the class FilterPipeline throws a NullPointerException if the given
     * atom container instance is null.
     */
    /*@Test
    public void getsFilteredMethodTest_throwsNullPointerExceptionIfAnAtomContainerIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    new FilterPipeline().getsFiltered(null, FilterPipeline.FilterTypes.NONE, 0);
                }
        );
    }*/

    /**
     * Tests whether the .getsFiltered() method of the class FilterPipeline throws a NullPointerException if the given
     * Filter.FilterTypes constant is null.
     */
    /*@Test
    public void getsFilteredMethodTest_throwsNullPointerExceptionIfAFilterTypeIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    new FilterPipeline().getsFiltered(new AtomContainer(), null, 0);
                }
        );
    }*/

    /**
     * Tests whether the .getsFiltered() method of the class FilterPipeline returns false if the given filter type is
     * Filter.FilterTypes.NONE since no filter is applied.
     */
    /*@Test
    public void getsFilteredMethodTest_filterTypeNONE_returnsFalse() {
        IAtomContainer tmpAnyAtomContainer = new AtomContainer();
        int tmpAnyIntegerValue = 0;
        Assertions.assertFalse(new FilterPipeline().getsFiltered(tmpAnyAtomContainer, FilterPipeline.FilterTypes.NONE, tmpAnyIntegerValue));
    }*/

    /**
     * TODO?! there is no way of testing this except implementing a redundant / wasted enum constant
     */
    /*@Test
    public void getsFilteredMethodTest_default_TODO() {
        //default case: throws NotImplementedException (/ UnsupportedOperationException)
    }*/

    /**
     * Tests whether true is returned by the .getsFiltered() method of class FilterPipeline if the max atom count filter
     * considering implicit hydrogen applies. Given AC exceeds the threshold.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    /*@Test
    public void getsFilteredMethodTest_maxAtomCountFilter_considerImplicitHydrogens_max10_12atomsAC() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        FilterPipeline.FilterTypes tmpFilterType = FilterPipeline.FilterTypes.MAX_ATOM_COUNT_FILTER_CONSIDER_IMPLICIT_HYDROGENS;
        int tmpMaxAtomCount = 10;
        //
        Assertions.assertTrue(new FilterPipeline().getsFiltered(tmpAtomContainer, tmpFilterType, tmpMaxAtomCount));
    }*/

    /**
     * Tests whether false is returned by the .getsFiltered() method of class FilterPipeline if the max atom count
     * filter considering implicit hydrogen does not apply. Given AC equals the threshold.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    /*@Test
    public void getsFilteredMethodTest_maxAtomCountFilter_considerImplicitHydrogens_max12_12atomsAC() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        FilterPipeline.FilterTypes tmpFilterType = FilterPipeline.FilterTypes.MAX_ATOM_COUNT_FILTER_CONSIDER_IMPLICIT_HYDROGENS;
        int tmpMaxAtomCount = 12;
        //
        Assertions.assertFalse(new FilterPipeline().getsFiltered(tmpAtomContainer, tmpFilterType, tmpMaxAtomCount));
    }*/

    /**
     * Tests whether true is returned by the .getsFiltered() method of class FilterPipeline if the max atom count filter
     * not considering implicit hydrogen applies. Given AC exceeds the threshold.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    /*@Test
    public void getsFilteredMethodTest_maxAtomCountFilter_notConsiderImplicitHydrogens_max4_6atomsAC() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        FilterPipeline.FilterTypes tmpFilterType = FilterPipeline.FilterTypes.MAX_ATOM_COUNT_FILTER_NOT_CONSIDER_IMPLICIT_HYDROGENS;
        int tmpMaxAtomCount = 4;
        //
        Assertions.assertTrue(new FilterPipeline().getsFiltered(tmpAtomContainer, tmpFilterType, tmpMaxAtomCount));
    }*/

    /**
     * Tests whether false is returned by the .getsFiltered() method of class FilterPipeline if the max atom count
     * filter not considering implicit hydrogen does not apply. Given AC equals the threshold.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    /*@Test
    public void getsFilteredMethodTest_maxAtomCountFilter_notConsiderImplicitHydrogens_max6_6atomsAC() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        FilterPipeline.FilterTypes tmpFilterType = FilterPipeline.FilterTypes.MAX_ATOM_COUNT_FILTER_NOT_CONSIDER_IMPLICIT_HYDROGENS;
        int tmpMaxAtomCount = 6;
        //
        Assertions.assertFalse(new FilterPipeline().getsFiltered(tmpAtomContainer, tmpFilterType, tmpMaxAtomCount));
    }*/

    /**
     * Tests whether true is returned by the .getsFiltered() method of class FilterPipeline if the min atom count filter
     * considering implicit hydrogen applies. Given AC falls short of the threshold.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    /*@Test
    public void getsFilteredMethodTest_minAtomCountFilter_considerImplicitHydrogens_min14_12atomsAC() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        FilterPipeline.FilterTypes tmpFilterType = FilterPipeline.FilterTypes.MIN_ATOM_COUNT_FILTER_CONSIDER_IMPLICIT_HYDROGENS;
        int tmpMinAtomCount = 14;
        //
        Assertions.assertTrue(new FilterPipeline().getsFiltered(tmpAtomContainer, tmpFilterType, tmpMinAtomCount));
    }*/

    /**
     * Tests whether false is returned by the .getsFiltered() method of class FilterPipeline if the min atom count
     * filter considering implicit hydrogen does not apply. Given AC equals the threshold.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    /*@Test
    public void getsFilteredMethodTest_minAtomCountFilter_considerImplicitHydrogens_min12_12atomsAC() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        FilterPipeline.FilterTypes tmpFilterType = FilterPipeline.FilterTypes.MIN_ATOM_COUNT_FILTER_CONSIDER_IMPLICIT_HYDROGENS;
        int tmpMinAtomCount = 12;
        //
        Assertions.assertFalse(new FilterPipeline().getsFiltered(tmpAtomContainer, tmpFilterType, tmpMinAtomCount));
    }*/

    /**
     * Tests whether true is returned by the .getsFiltered() method of class FilterPipeline if the min atom count filter
     * not considering implicit hydrogen applies. Given AC falls short of the threshold.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    /*@Test
    public void getsFilteredMethodTest_minAtomCountFilter_notConsiderImplicitHydrogens_min8_6atomsAC() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        FilterPipeline.FilterTypes tmpFilterType = FilterPipeline.FilterTypes.MIN_ATOM_COUNT_FILTER_NOT_CONSIDER_IMPLICIT_HYDROGENS;
        int tmpMinAtomCount = 8;
        //
        Assertions.assertTrue(new FilterPipeline().getsFiltered(tmpAtomContainer, tmpFilterType, tmpMinAtomCount));
    }*/

    /**
     * Tests whether false is returned by the .getsFiltered() method of class FilterPipeline if the min atom count
     * filter not considering implicit hydrogen does not apply. Given AC equals the threshold.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    /*@Test
    public void getsFilteredMethodTest_minAtomCountFilter_considerImplicitHydrogens_min6_6atomsAC() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        FilterPipeline.FilterTypes tmpFilterType = FilterPipeline.FilterTypes.MIN_ATOM_COUNT_FILTER_NOT_CONSIDER_IMPLICIT_HYDROGENS;
        int tmpMinAtomCount = 6;
        //
        Assertions.assertFalse(new FilterPipeline().getsFiltered(tmpAtomContainer, tmpFilterType, tmpMinAtomCount));
    }*/

    //TODO: add test methods for every new filter algorithm

}
