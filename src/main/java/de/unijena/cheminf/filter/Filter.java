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

import org.apache.commons.lang3.NotImplementedException;
import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

import java.util.LinkedList;
import java.util.Objects;

/**
 *
 */
public class Filter {

    public static final String MOL_ID_PROPERTY_NAME = "MolID";

    protected final LinkedList<FilterTypes> listOfSelectedFilters;

    //TODO: possibly change data type to an Interface IFilterParameterStorage so that each filter can have its specific set of parameters (not only one integer)
    //TODO: / use a generic class
    public final LinkedList<Integer> listOfFilterParameters;

    /**
     * Constructor.
     */
    public Filter() {
        this.listOfSelectedFilters = new LinkedList<>();
        this.listOfFilterParameters = new LinkedList<>();
    }

    /**
     * Protected Constructor.
     * For generating a copy of the original Filter instance.
     *
     * @param anOriginalFilter Filter instance to copy
     */
    protected Filter(Filter anOriginalFilter) {
        this.listOfSelectedFilters = anOriginalFilter.listOfSelectedFilters;
        this.listOfFilterParameters = anOriginalFilter.listOfFilterParameters;
    }

    public IAtomContainerSet filter(IAtomContainerSet anAtomContainerSet) {
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of AtomContainerSet) is null.");
        this.assignIdToAtomContainers(anAtomContainerSet);
        final IAtomContainerSet tmpFilteredACSet = new AtomContainerSet();
        for (IAtomContainer tmpAtomContainer :
                anAtomContainerSet.atomContainers()) {
            //apply filters
            //TODO: setProperty FILTERED_BY_FILTER to the index of the filter in the listOfSelectedFilters list
            tmpFilteredACSet.addAtomContainer(tmpAtomContainer);
        }
        //
        return tmpFilteredACSet;
    }

    /**
     * TODO
     * @param aMaxAtomCount
     * @param aConsiderImplicitHydrogens
     * @return
     * @throws IllegalArgumentException
     */
    public Filter withMaxAtomCountFilter(int aMaxAtomCount, boolean aConsiderImplicitHydrogens) throws IllegalArgumentException {
        if (aMaxAtomCount < 0) {    //TODO: would not harm the code but makes no sense
            throw new IllegalArgumentException("aMaxAtomCount (integer value) was < than 0.");
        }
        if (aConsiderImplicitHydrogens) {
            return this.withFilter(FilterTypes.MAX_ATOM_COUNT_FILTER_CONSIDER_IMPLICIT_HYDROGENS, aMaxAtomCount);
        } else {
            return this.withFilter(FilterTypes.MAX_ATOM_COUNT_FILTER_NOT_CONSIDER_IMPLICIT_HYDROGENS, aMaxAtomCount);
        }
        /*FilterTypes tmpFilterType = aConsiderImplicitHydrogens ?
                FilterTypes.MAX_ATOM_COUNT_FILTER_CONSIDER_IMPLICIT_HYDROGENS :
                FilterTypes.MAX_ATOM_COUNT_FILTER_NOT_CONSIDER_IMPLICIT_HYDROGENS;
        return this.withFilter(tmpFilterType, aMaxAtomCount);*/
    }

    /**
     * TODO
     * @param aMinAtomCount
     * @param aConsiderImplicitHydrogens
     * @return
     * @throws IllegalArgumentException
     */
    public Filter withMinAtomCountFilter(int aMinAtomCount, boolean aConsiderImplicitHydrogens) throws IllegalArgumentException {
        if (aMinAtomCount < 0) {    //TODO: would not harm the code but makes no sense
            throw new IllegalArgumentException("aMinAtomCount (integer value) was < than 0.");
        }
        if (aConsiderImplicitHydrogens) {
            return this.withFilter(FilterTypes.MIN_ATOM_COUNT_CONSIDER_IMPLICIT_HYDROGENS, aMinAtomCount);
        } else {
            return this.withFilter(FilterTypes.MIN_ATOM_COUNT_NOT_CONSIDER_IMPLICIT_HYDROGENS, aMinAtomCount);
        }
    }

    protected Filter withFilter(FilterTypes aFilterType, int anIntegerParameter) {
        Objects.requireNonNull(aFilterType, "aFilterType (Filter.FilterTypes constant) is null.");
        final Filter tmpFilterCopy = new Filter(this);
        tmpFilterCopy.listOfSelectedFilters.add(aFilterType);
        tmpFilterCopy.listOfFilterParameters.add(anIntegerParameter);
        return tmpFilterCopy;
    }

    /**
     * Checks if a specific filter applies on a given atom container.
     * Returns true, if the atom container needs to be filtered / sorted out.
     *
     * @param anAtomContainer IAtomContainer instance to be checked
     * @param aFilterType FilterTypes constant which stands for the filter algorithm that is to be applied
     * @param anIntegerParameter Integer value which is a parameter to the given filter algorithm
     * @return true if the given filter applies on the atom container
     */
    protected boolean checkIfFilterApplies(IAtomContainer anAtomContainer, FilterTypes aFilterType, int anIntegerParameter) {
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of IAtomContainer) is null.");
        Objects.requireNonNull(aFilterType, "aFilterType (Filter.FilterTypes constant) is null.");
        switch (aFilterType) {
            case MAX_ATOM_COUNT_FILTER_CONSIDER_IMPLICIT_HYDROGENS:
                return true;
            case MAX_ATOM_COUNT_FILTER_NOT_CONSIDER_IMPLICIT_HYDROGENS:
                return true;
            case MIN_ATOM_COUNT_CONSIDER_IMPLICIT_HYDROGENS:
                return true;
            case MIN_ATOM_COUNT_NOT_CONSIDER_IMPLICIT_HYDROGENS:
                return true;
            case NONE:
                return false;
            default:
                throw new NotImplementedException("There is no filter routine deposited for the given filter type."); //may I do this?
        }
    }

    /**
     * Assigns an identifier to every atom container of the given atom container set.
     *
     * @param anAtomContainerSet IAtomContainerSet
     * @throws NullPointerException
     */
    protected void assignIdToAtomContainers(IAtomContainerSet anAtomContainerSet) {     //TODO: could be static; could be placed in FilterUtils (as protected method)
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of AtomContainerSet) is null");
        for (int i = 0; i < anAtomContainerSet.getAtomContainerCount(); i++) {
            //TODO: check whether the property is already set? they might not be unique
            //TODO: assign them to the original AtomContainer instance or to a copy?
            anAtomContainerSet.getAtomContainer(i).setProperty(Filter.MOL_ID_PROPERTY_NAME, i);
        }
    }

    /** TODO: could / should be placed in a Utils class
     * TODO
     * @param aAtomContainerSet
     * @return
     */
    public int[] getArrayOfAssignedMolIDs(IAtomContainerSet aAtomContainerSet) {    //TODO: deal with AC or MolID being null
        final int[] tmpMolIDArray = new int[aAtomContainerSet.getAtomContainerCount()];
        for (int i = 0; i < tmpMolIDArray.length; i++) {
            tmpMolIDArray[i] = aAtomContainerSet.getAtomContainer(i).getProperty(Filter.MOL_ID_PROPERTY_NAME);
        }
        return tmpMolIDArray;
    }

    /**
     * Returns the list of selected filters.
     *
     * @return LinkedList<FilterTypes>
     */
    public LinkedList<FilterTypes> getListOfSelectedFilters() {
        return this.listOfSelectedFilters;
    }

    /**
     * Returns the list of filter parameters.
     *
     * @return LinkedList<Integer>
     */
    public LinkedList<Integer> getListOfFilterParameters() {
        return this.listOfFilterParameters;
    }

    /**
     * Enum of filter types.
     */
    public enum FilterTypes {
        MAX_ATOM_COUNT_FILTER_CONSIDER_IMPLICIT_HYDROGENS,
        MAX_ATOM_COUNT_FILTER_NOT_CONSIDER_IMPLICIT_HYDROGENS,
        MIN_ATOM_COUNT_CONSIDER_IMPLICIT_HYDROGENS,
        MIN_ATOM_COUNT_NOT_CONSIDER_IMPLICIT_HYDROGENS,
        NONE
    }
}
