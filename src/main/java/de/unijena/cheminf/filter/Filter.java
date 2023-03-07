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
 * Filter on based on a molecular descriptor. Auf Basis eines einzelnen Moleküls
 */
public class Filter {

    public static final String MOL_ID_PROPERTY_NAME = "Filter.MolID";

    public static final String FILTER_ID_PROPERTY_NAME = "Filter.FilterID";

    public static final int MOL_ID_ERROR_VALUE = -1;

    public static final int NOT_FILTERED = -1;

    protected final LinkedList<FilterTypes> listOfSelectedFilters;

    //TODO: possibly change data type to an Interface IFilterParameterStorage so that each filter can have its specific set of parameters (not only one integer)
    //TODO: / use a generic class
    //so far only filters with one single parameter of type int are possible
    protected final LinkedList<Integer> listOfFilterParameters; //TODO: List of Objects[] ?

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

    /**
     * TODO
     * TODO: name that every atom container gets a MolID assigned (for better traceability)
     * @param anAtomContainerSet
     * @return
     */
    public IAtomContainerSet filter(IAtomContainerSet anAtomContainerSet) {
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of AtomContainerSet) is null.");
        this.assignMolIdToAtomContainers(anAtomContainerSet);
        final IAtomContainerSet tmpFilteredACSet = new AtomContainerSet();
        //boolean tmpAtomContainerGetsFiltered;
        int tmpIndexOfAppliedFilter;
        for (IAtomContainer tmpAtomContainer :
                anAtomContainerSet.atomContainers()) {
            //tmpAtomContainerGetsFiltered = false;
            tmpIndexOfAppliedFilter = Filter.NOT_FILTERED;
            //apply filters
            for (int i = 0; i < this.listOfSelectedFilters.size(); i++) {
                if (this.checkWhetherFilterApplies(tmpAtomContainer, this.listOfSelectedFilters.get(i), this.listOfFilterParameters.get(i))) {
                    //tmpAtomContainerGetsFiltered = true;
                    tmpIndexOfAppliedFilter = i;
                    break;
                }
            }
            //if (!tmpAtomContainerGetsFiltered) {
            if (tmpIndexOfAppliedFilter == Filter.NOT_FILTERED) {
                tmpFilteredACSet.addAtomContainer(tmpAtomContainer);
            }
            //
            //TODO: setProperty FILTERED_BY_FILTER to the index of the filter in the listOfSelectedFilters list
            //tmpAtomContainer.setProperty(Filter.FILTER_ID_PROPERTY_NAME, Filter.NOT_FILTERED);
            tmpAtomContainer.setProperty(Filter.FILTER_ID_PROPERTY_NAME, tmpIndexOfAppliedFilter);
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
            return this.withFilter(FilterTypes.MIN_ATOM_COUNT_FILTER_CONSIDER_IMPLICIT_HYDROGENS, aMinAtomCount);
        } else {
            return this.withFilter(FilterTypes.MIN_ATOM_COUNT_FILTER_NOT_CONSIDER_IMPLICIT_HYDROGENS, aMinAtomCount);
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
     * Checks whether a specific filter applies on a given atom container.
     * Returns true, if the atom container needs to be filtered / sorted out.
     *
     * @param anAtomContainer IAtomContainer instance to be checked
     * @param aFilterType FilterTypes constant which stands for the filter algorithm that is to be applied
     * @param anIntegerParameter Integer value which is a parameter to the given filter algorithm
     * @return true if the given filter applies on the atom container
     */
    protected boolean checkWhetherFilterApplies(IAtomContainer anAtomContainer, FilterTypes aFilterType, int anIntegerParameter) {  //TODO: adopt name; question that is answered
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of IAtomContainer) is null.");
        Objects.requireNonNull(aFilterType, "aFilterType (Filter.FilterTypes constant) is null.");
        return switch (aFilterType) {
            case MAX_ATOM_COUNT_FILTER_CONSIDER_IMPLICIT_HYDROGENS ->
                    FilterUtils.exceedsOrEqualsAtomCount(anAtomContainer, anIntegerParameter + 1, true);
            case MAX_ATOM_COUNT_FILTER_NOT_CONSIDER_IMPLICIT_HYDROGENS ->
                    FilterUtils.exceedsOrEqualsAtomCount(anAtomContainer, anIntegerParameter + 1, false);
            case MIN_ATOM_COUNT_FILTER_CONSIDER_IMPLICIT_HYDROGENS ->
                    !FilterUtils.exceedsOrEqualsAtomCount(anAtomContainer, anIntegerParameter, true);
            case MIN_ATOM_COUNT_FILTER_NOT_CONSIDER_IMPLICIT_HYDROGENS ->
                    !FilterUtils.exceedsOrEqualsAtomCount(anAtomContainer, anIntegerParameter, false);
            case NONE ->
                    false;
            default -> //TODO: NotImplementedException or UnsupportedOperationException?!
                    throw new NotImplementedException("There is no filter routine deposited for the given filter type."); //may I do this?
                    //throw new UnsupportedOperationException("There is no filter routine deposited for the given filter type.");
        };
    }

    /**
     * Assigns an identifier to every atom container of the given atom container set.
     *
     * @param anAtomContainerSet IAtomContainerSet
     * @throws NullPointerException
     */
    protected void assignMolIdToAtomContainers(IAtomContainerSet anAtomContainerSet) throws NullPointerException {     //TODO: could be static; could be placed in FilterUtils (as protected method)
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of AtomContainerSet) is null");
        for (int i = 0; i < anAtomContainerSet.getAtomContainerCount(); i++) {
            //TODO: check whether the property is already set? they might not be unique
            //TODO: assign them to the original AtomContainer instance or to a copy?
            anAtomContainerSet.getAtomContainer(i).setProperty(Filter.MOL_ID_PROPERTY_NAME, i);
        }
    }

    /** TODO: could / should be placed in a Utils class
     * Returns an array of the MolIDs assigned to the atom containers in the given atom container set. If an atom
     * container given to this method has no MolID, the corresponding entry in the returned array is set to -1.
     * TODO
     *
     * @param anAtomContainerSet
     * @return
     * @throws NullPointerException if the given instance of IAtomContainerSet is null
     */
    public int[] getArrayOfAssignedMolIDs(IAtomContainerSet anAtomContainerSet) throws NullPointerException {    //TODO: deal with AC or MolID being null
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of IAtomContainerSet) is null.");
        final int[] tmpMolIDArray = new int[anAtomContainerSet.getAtomContainerCount()];
        IAtomContainer tmpAtomContainer;
        for (int i = 0; i < tmpMolIDArray.length; i++) {
            tmpAtomContainer = anAtomContainerSet.getAtomContainer(i);
            if (tmpAtomContainer == null || tmpAtomContainer.getProperty(Filter.MOL_ID_PROPERTY_NAME) == null) {
                tmpMolIDArray[i] = Filter.MOL_ID_ERROR_VALUE;
            } else {
                tmpMolIDArray[i] = tmpAtomContainer.getProperty(Filter.MOL_ID_PROPERTY_NAME);
            }
        }
        return tmpMolIDArray;
    }

    /**
     * Returns the MolID assigned to the given atom container. A MolID is assigned to every atom container of an atom
     * container set that was given to or returned by the .filter() method of this class. This method may not be used
     * for any atom container that does not meet this criterion.
     *
     * @param anAtomContainer IAtomContainer instance the MolID should be returned of
     * @return MolID (integer value) of the given AtomContainer
     * @throws NullPointerException if the given instance of IAtomContainer is null
     * @throws IllegalArgumentException if the given IAtomContainer instance is null or if the MolID assigned to it is
     * not of data type Integer
     */
    public int getAssignedMolID(IAtomContainer anAtomContainer) throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of IAtomContainer) is null.");
        if (anAtomContainer.getProperty(Filter.MOL_ID_PROPERTY_NAME) == null) {
            throw new IllegalArgumentException("The given AtomContainer instance has no MolID assigned.");
        }
        if (anAtomContainer.getProperty(Filter.MOL_ID_PROPERTY_NAME).getClass() != Integer.class) {
            throw new IllegalArgumentException("The MolID assigned to the given AtomContainer instance is not of data type Integer.");
        }
        return anAtomContainer.getProperty(Filter.MOL_ID_PROPERTY_NAME);
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
        MIN_ATOM_COUNT_FILTER_CONSIDER_IMPLICIT_HYDROGENS,
        MIN_ATOM_COUNT_FILTER_NOT_CONSIDER_IMPLICIT_HYDROGENS,
        NONE
    }
}
