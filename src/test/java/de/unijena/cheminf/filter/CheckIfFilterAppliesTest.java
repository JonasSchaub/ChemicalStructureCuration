package de.unijena.cheminf.filter;

import de.unijena.cheminf.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;

public class CheckIfFilterAppliesTest {

    /**
     * Tests whether the .checkIfFilterApplies() method of the class Filter returns a boolean value.
     */
    @Test
    public void checkIfFilterAppliesMethodTest_returnsBooleanValue() {
        IAtomContainer tmpAtomContainer = new AtomContainer();
        Filter.FilterTypes tmpFilterType = Filter.FilterTypes.NONE;
        int tmpIntegerParameter = 0;
        Assertions.assertInstanceOf(Boolean.class, new Filter().checkWhetherFilterApplies(tmpAtomContainer, tmpFilterType, tmpIntegerParameter));
    }

    /**
     * Tests whether the .checkIfFilterApplies() method of the class Filter throws a NullPointerException if the given
     * atom container instance is null.
     */
    @Test
    public void checkIfFilterAppliesMethodTest_throwsNullPointerExceptionIfAnAtomContainerIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    new Filter().checkWhetherFilterApplies(null, Filter.FilterTypes.NONE, 0);
                }
        );
    }

    /**
     * Tests whether the .checkIfFilterApplies() method of the class Filter throws a NullPointerException if the given
     * Filter.FilterTypes constant is null.
     */
    @Test
    public void checkIfFilterAppliesMethodTest_throwsNullPointerExceptionIfAFilterTypeIsNull() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> {
                    new Filter().checkWhetherFilterApplies(new AtomContainer(), null, 0);
                }
        );
    }

    //TODO: add test methods for every new filter algorithm

    /**
     * Tests whether the .checkIfFilterApplies() method of the class Filter returns false if the given filter type is
     * Filter.FilterTypes.NONE since no filter is applied.
     */
    @Test
    public void checkIfFilterAppliesMethodTest_filterTypeNONE_returnsFalse() {
        IAtomContainer tmpAnyAtomContainer = new AtomContainer();
        int tmpAnyIntegerValue = 0;
        Assertions.assertFalse(new Filter().checkWhetherFilterApplies(tmpAnyAtomContainer, Filter.FilterTypes.NONE, tmpAnyIntegerValue));
    }

    /**
     * TODO?! there is no way of testing this except implementing a redundant / wasted enum constant
     */
    @Test
    public void checkIfFilterAppliesMethodTest_default_TODO() {
        //default case: throws NotImplementedException
    }

    @Test
    public void checkIfFilterAppliesMethodTest_maxAtomCountFilter_considerImplicitHydrogens_max10_12atomsTestAC() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        Filter.FilterTypes tmpFilterType = Filter.FilterTypes.MAX_ATOM_COUNT_FILTER_CONSIDER_IMPLICIT_HYDROGENS;
        int tmpMaxAtomCount = 10;
        //
        Assertions.assertTrue(new Filter().checkWhetherFilterApplies(tmpAtomContainer, tmpFilterType, tmpMaxAtomCount));
    }

    @Test
    public void checkIfFilterAppliesMethodTest_maxAtomCountFilter_considerImplicitHydrogens_max12_12atomsTestAC() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        Filter.FilterTypes tmpFilterType = Filter.FilterTypes.MAX_ATOM_COUNT_FILTER_CONSIDER_IMPLICIT_HYDROGENS;
        int tmpMaxAtomCount = 12;
        //
        Assertions.assertFalse(new Filter().checkWhetherFilterApplies(tmpAtomContainer, tmpFilterType, tmpMaxAtomCount));
    }

    @Test
    public void checkIfFilterAppliesMethodTest_maxAtomCountFilter_notConsiderImplicitHydrogens_max4_6atomsTestAC() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        Filter.FilterTypes tmpFilterType = Filter.FilterTypes.MAX_ATOM_COUNT_FILTER_NOT_CONSIDER_IMPLICIT_HYDROGENS;
        int tmpMaxAtomCount = 4;
        //
        Assertions.assertTrue(new Filter().checkWhetherFilterApplies(tmpAtomContainer, tmpFilterType, tmpMaxAtomCount));
    }

    @Test
    public void checkIfFilterAppliesMethodTest_maxAtomCountFilter_notConsiderImplicitHydrogens_max6_6atomsTestAC() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        Filter.FilterTypes tmpFilterType = Filter.FilterTypes.MAX_ATOM_COUNT_FILTER_NOT_CONSIDER_IMPLICIT_HYDROGENS;
        int tmpMaxAtomCount = 6;
        //
        Assertions.assertFalse(new Filter().checkWhetherFilterApplies(tmpAtomContainer, tmpFilterType, tmpMaxAtomCount));
    }

    @Test
    public void checkIfFilterAppliesMethodTest_minAtomCountFilter_considerImplicitHydrogens_min14_12atomsTestAC() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        Filter.FilterTypes tmpFilterType = Filter.FilterTypes.MIN_ATOM_COUNT_FILTER_CONSIDER_IMPLICIT_HYDROGENS;
        int tmpMinAtomCount = 14;
        //
        Assertions.assertTrue(new Filter().checkWhetherFilterApplies(tmpAtomContainer, tmpFilterType, tmpMinAtomCount));
    }

    @Test
    public void checkIfFilterAppliesMethodTest_minAtomCountFilter_considerImplicitHydrogens_min12_12atomsTestAC() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        Filter.FilterTypes tmpFilterType = Filter.FilterTypes.MIN_ATOM_COUNT_FILTER_CONSIDER_IMPLICIT_HYDROGENS;
        int tmpMinAtomCount = 12;
        //
        Assertions.assertFalse(new Filter().checkWhetherFilterApplies(tmpAtomContainer, tmpFilterType, tmpMinAtomCount));
    }

    @Test
    public void checkIfFilterAppliesMethodTest_minAtomCountFilter_notConsiderImplicitHydrogens_min8_6atomsTestAC() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        Filter.FilterTypes tmpFilterType = Filter.FilterTypes.MIN_ATOM_COUNT_FILTER_NOT_CONSIDER_IMPLICIT_HYDROGENS;
        int tmpMinAtomCount = 8;
        //
        Assertions.assertTrue(new Filter().checkWhetherFilterApplies(tmpAtomContainer, tmpFilterType, tmpMinAtomCount));
    }

    @Test
    public void checkIfFilterAppliesMethodTest_minAtomCountFilter_considerImplicitHydrogens_min6_6atomsTestAC() throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("c1ccccc1");
        Filter.FilterTypes tmpFilterType = Filter.FilterTypes.MIN_ATOM_COUNT_FILTER_NOT_CONSIDER_IMPLICIT_HYDROGENS;
        int tmpMinAtomCount = 6;
        //
        Assertions.assertFalse(new Filter().checkWhetherFilterApplies(tmpAtomContainer, tmpFilterType, tmpMinAtomCount));
    }

}
