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

import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IBond;

import java.util.BitSet;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Class for creating/building pipelines of multiple filters for filtering sets of atom containers based on molecular descriptors.  TODO: is this one sentence enough?
 * Only contains filters that are based on molecular descriptors and can be applied for each atom container separately
 * (without knowledge of the other atom containers - as it would be necessary e.g. for filtering duplicates).
 */
public class FilterPipeline {

    /*
    TODO: remove parameter tests of filters out of FilterPipeline methods?
    TODO: use the .withFilter() method in the .with...Filter() convenience methods?
    //
    TODO: implement test methods for .withMaxBondCountFilter() and .withMinBondCountFilter() methods
    TODO: remove / adopt tests for .getsFiltered() method
    TODO: test whether the MolID always equals the index of the atom container in the given AC set
    //
    TODO (optional):
    - method to deep copy / clone a FilterPipeline?
    - find a shorter name for FilterID and MolID property (?)
    - methods for clearing MolIDs / FilterIDs of atom containers?
     */

    /**
     * String of the name of the atom container property for uniquely identifying an atom container during the filtering
     * process.
     */
    public static final String MOL_ID_PROPERTY_NAME = "FilterPipeline.MolID";

    /**
     * String of the name of the atom container property for tracking / tracing the filtering process by saving the
     * index of the filter an atom container got filtered by.
     */
    public static final String FILTER_ID_PROPERTY_NAME = "FilterPipeline.FilterID";

    /**
     * Default value for the filter ID of an atom container that has not been filtered during a filtering process.
     */
    public static final int NOT_FILTERED_VALUE = -1;

    /**
     * Linked list of the IFilter instances that were added to this filter pipeline. Filters can be added via the filter
     * specific convenience methods or using the .withFilter() method of this class.
     */
    protected final LinkedList<IFilter> listOfSelectedFilters;

    /**
     * Constructor.
     */
    public FilterPipeline() {
        this.listOfSelectedFilters = new LinkedList<>();
    }

    /** TODO: remove this constructor (?!)
     * Protected Constructor. Generates a copy of the original Filter instance maintaining all its fields.
     *
     * @param anOriginalFilterPipeline FilterPipeline instance to generate the copy of
     */
    protected FilterPipeline(FilterPipeline anOriginalFilterPipeline) {
        this.listOfSelectedFilters = anOriginalFilterPipeline.listOfSelectedFilters;
    }

    /**
     * Applies the filter pipeline on the given set of atom containers and returns the set of those who passed all
     * filters. To uniquely identify the atom containers during the filtering process, each atom container gets a
     * unique MolID ("FilterPipeline.MolID") in form of an integer type property assigned. In order to trace by which
     * filter an atom container got filtered by, every atom container gets the index of the filter in the list of
     * selected filters assigned as FilterID (integer property "FilterPipeline.FilterID"). Atom containers that do
     * not get filtered get a FilterID of negative one.
     *
     * @param anAtomContainerSet set of atom containers to be filtered
     * @return atom container set of all atom containers that passed the filter pipeline
     * @throws NullPointerException if the given IAtomContainerSet instance is null
     */
    public IAtomContainerSet filter(IAtomContainerSet anAtomContainerSet) throws NullPointerException {
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of IAtomContainerSet) is null.");
        this.assignMolIdToAtomContainers(anAtomContainerSet);
        final BitSet tmpGotFilteredBitSet = new BitSet(anAtomContainerSet.getAtomContainerCount());
        IAtomContainer tmpAtomContainer;

        for (int tmpIndexOfFilter = 0; tmpIndexOfFilter < this.listOfSelectedFilters.size(); tmpIndexOfFilter++) {
            for (int i = 0; i < anAtomContainerSet.getAtomContainerCount(); i++) {
                if (!tmpGotFilteredBitSet.get(i)) {
                    tmpAtomContainer = anAtomContainerSet.getAtomContainer(i);
                    if (this.listOfSelectedFilters.get(tmpIndexOfFilter).getsFiltered(tmpAtomContainer)) {
                        tmpAtomContainer.setProperty(FilterPipeline.FILTER_ID_PROPERTY_NAME, tmpIndexOfFilter);
                        tmpGotFilteredBitSet.set(i);
                    }
                }
            }
        }
        final IAtomContainerSet tmpFilteredACSet = new AtomContainerSet();
        for (int i = 0; i < anAtomContainerSet.getAtomContainerCount(); i++) {
            tmpAtomContainer = anAtomContainerSet.getAtomContainer(i);
            if (!tmpGotFilteredBitSet.get(i)) {
                tmpAtomContainer.setProperty(FilterPipeline.FILTER_ID_PROPERTY_NAME, FilterPipeline.NOT_FILTERED_VALUE);
                tmpFilteredACSet.addAtomContainer(tmpAtomContainer);
            }
        }

        /*final IAtomContainerSet tmpFilteredACSet = new AtomContainerSet();
        int tmpIndexOfAppliedFilter;
        for (IAtomContainer tmpAtomContainer :
                anAtomContainerSet.atomContainers()) {  //TODO: switch order/position of loops
            tmpIndexOfAppliedFilter = FilterPipeline.NOT_FILTERED_VALUE;
            //apply filters
            for (int i = 0; i < this.listOfSelectedFilters.size(); i++) {
                if (this.listOfSelectedFilters.get(i).getsFiltered(tmpAtomContainer)) {
                    tmpIndexOfAppliedFilter = i;
                    break;
                }
            }
            if (tmpIndexOfAppliedFilter == FilterPipeline.NOT_FILTERED_VALUE) {
                tmpFilteredACSet.addAtomContainer(tmpAtomContainer);
            }
            tmpAtomContainer.setProperty(FilterPipeline.FILTER_ID_PROPERTY_NAME, tmpIndexOfAppliedFilter);
        }*/

        return tmpFilteredACSet;
    }

    /**
     * Adds a max atom count filter with the given parameters to the filter pipeline. Implicit hydrogen atoms may or
     * may not be considered; atom containers that equal the given max atom count do not get filtered.
     *
     * @param aMaxAtomCount integer value of the max atom count to filter by
     * @param aConsiderImplicitHydrogens boolean value whether to consider implicit hydrogen atoms
     * @return the FilterPipeline instance itself
     * @throws IllegalArgumentException if the given max atom count is less than zero
     */
    public FilterPipeline withMaxAtomCountFilter(int aMaxAtomCount, boolean aConsiderImplicitHydrogens) throws IllegalArgumentException {
        if (aMaxAtomCount < 0) {    //TODO: would not harm the code but makes no sense
            throw new IllegalArgumentException("aMaxAtomCount (integer value) was < than 0.");
        }
        IFilter tmpFilter = new MaxAtomCountFilter(aMaxAtomCount, aConsiderImplicitHydrogens);
        this.listOfSelectedFilters.add(tmpFilter);
        return this;
    }

    /**
     * Adds a min atom count filter with the given parameters to the filter pipeline. Implicit hydrogen atoms may or
     * may not be considered; atom containers that equal the given min atom count do not get filtered.
     *
     * @param aMinAtomCount integer value of the min atom count to filter by
     * @param aConsiderImplicitHydrogens boolean value whether to consider implicit hydrogen atoms
     * @return the FilterPipeline instance itself
     * @throws IllegalArgumentException if the given min atom count is less than zero
     */
    public FilterPipeline withMinAtomCountFilter(int aMinAtomCount, boolean aConsiderImplicitHydrogens) throws IllegalArgumentException {
        if (aMinAtomCount < 0) {    //TODO: would not harm the code but makes no sense; param. checks here?!
            throw new IllegalArgumentException("aMinAtomCount (integer value) was < than 0.");
        }
        IFilter tmpFilter = new MinAtomCountFilter(aMinAtomCount, aConsiderImplicitHydrogens);
        this.listOfSelectedFilters.add(tmpFilter);
        return this;
    }

    /**
     * Adds a max bond count filter with the given parameters to the filter pipeline. Bonds to implicit hydrogen atoms
     * may or may not be considered; atom containers that equal the given max bond count do not get filtered.
     *
     * @param aMaxBondCount integer value of the max bond count to filter by
     * @param aConsiderImplicitHydrogens boolean value whether to consider bonds to implicit hydrogen atoms
     * @return the FilterPipeline instance itself
     * @throws IllegalArgumentException if the given max bond count is less than zero
     */
    public FilterPipeline withMaxBondCountFilter(int aMaxBondCount, boolean aConsiderImplicitHydrogens) throws IllegalArgumentException {
        if (aMaxBondCount < 0) {    //TODO: would not harm the code but makes no sense
            throw new IllegalArgumentException("aMaxBondCount (integer value) was < than 0.");
        }
        IFilter tmpFilter = new MaxBondCountFilter(aMaxBondCount, aConsiderImplicitHydrogens);
        this.listOfSelectedFilters.add(tmpFilter);
        return this;
    }

    /**
     * Adds a min bond count filter with the given parameters to the filter pipeline. Bonds to implicit hydrogen atoms
     * may or may not be considered; atom containers that equal the given min bond count do not get filtered.
     *
     * @param aMinBondCount integer value of the min bond count to filter by
     * @param aConsiderImplicitHydrogens boolean value whether to consider bonds to implicit hydrogen atoms
     * @return the FilterPipeline instance itself
     * @throws IllegalArgumentException if the given min bond count is less than zero
     */
    public FilterPipeline withMinBondCountFilter(int aMinBondCount, boolean aConsiderImplicitHydrogens) throws IllegalArgumentException {
        if (aMinBondCount < 0) {    //TODO: would not harm the code but makes no sense; param. checks here?!
            throw new IllegalArgumentException("aMinBondCount (integer value) was < than 0.");
        }
        IFilter tmpFilter = new MinBondCountFilter(aMinBondCount, aConsiderImplicitHydrogens);
        this.listOfSelectedFilters.add(tmpFilter);
        return this;
    }

    /**
     * Adds a max bonds of specific bond order filter with the given parameters to the filter pipeline. Bonds to
     * implicit hydrogen atoms may or may not be considered when counting bonds of bond order single; atom containers
     * that equal the given max specific bond count do not get filtered.
     *
     * @param aBondOrder bond order of bonds to count and filter on
     * @param aMaxSpecificBondCount  integer value of the max specific bond count to filter by
     * @param aConsiderImplicitHydrogens boolean value whether to consider bonds to implicit hydrogen atoms; this is
     *                                   only relevant when counting bonds of the order one / single
     * @return the FilterPipeline instance itself
     * @throws IllegalArgumentException if the given max specific bond count is less than zero
     */
    public FilterPipeline withMaxBondsOfSpecificBondOrderFilter(
            IBond.Order aBondOrder, int aMaxSpecificBondCount, boolean aConsiderImplicitHydrogens
    ) throws IllegalArgumentException {
        if (aMaxSpecificBondCount < 0) {
            throw new IllegalArgumentException("aMaxSpecificBondCount (integer value) was < than 0.");
        }
        IFilter tmpFilter = new MaxBondsOfSpecificBondOrderFilter(aBondOrder, aMaxSpecificBondCount, aConsiderImplicitHydrogens);
        this.listOfSelectedFilters.add(tmpFilter);
        return this;
    }

    /**
     * Adds a min bonds of specific bond order filter with the given parameters to the filter pipeline. Bonds to
     * implicit hydrogen atoms may or may not be considered when counting bonds of bond order single; atom containers
     * that equal the given min specific bond count do not get filtered.
     *
     * @param aBondOrder bond order of bonds to count and filter on
     * @param aMinSpecificBondCount  integer value of the min specific bond count to filter by
     * @param aConsiderImplicitHydrogens boolean value whether to consider bonds to implicit hydrogen atoms; this is
     *                                   only relevant when counting bonds of the order one / single
     * @return the FilterPipeline instance itself
     * @throws IllegalArgumentException if the given min specific bond count is less than zero
     */
    public FilterPipeline withMinBondsOfSpecificBondOrderFilter(
            IBond.Order aBondOrder, int aMinSpecificBondCount, boolean aConsiderImplicitHydrogens
    ) throws IllegalArgumentException {
        if (aMinSpecificBondCount < 0) {
            throw new IllegalArgumentException("aMinSpecificBondCount (integer value) was < than 0.");
        }
        IFilter tmpFilter = new MinBondsOfSpecificBondOrderFilter(aBondOrder, aMinSpecificBondCount, aConsiderImplicitHydrogens);
        this.listOfSelectedFilters.add(tmpFilter);
        return this;
    }

    /**
     * Adds the given filter to the filter pipeline. This method may be used to manually add a Filter instance to the
     * filter pipeline for which no convenience method - in the form of .with...Filter() - is available. This allows
     * the usage of subsequently implemented filters.
     *
     * @param aFilterToAdd the IFilter instance that is to be added to the pipeline
     * @return the FilterPipeline instance itself
     * @throws NullPointerException if the given Filter instance is null
     */
    public FilterPipeline withFilter(IFilter aFilterToAdd) throws NullPointerException {
        Objects.requireNonNull(aFilterToAdd, "aFilterToAdd (instance of Filter) is null.");
        this.listOfSelectedFilters.add(aFilterToAdd);
        return this;
    }

    /**
     * Assigns a unique identifier in form of a MolID to every atom container of the given atom container set. For this
     * purpose, each atom container is assigned an integer property of name "FilterPipeline.MolID".
     *
     * @param anAtomContainerSet IAtomContainerSet to whose atom containers MolIDs should be assigned
     * @throws NullPointerException if the given IAtomContainerSet instance is null
     */
    protected void assignMolIdToAtomContainers(IAtomContainerSet anAtomContainerSet) throws NullPointerException {
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of IAtomContainerSet) is null.");
        for (int i = 0; i < anAtomContainerSet.getAtomContainerCount(); i++) {
            //TODO: check whether the property is already set? they might not be unique
            //TODO: assign them to the original AtomContainer instance or to a copy?
            anAtomContainerSet.getAtomContainer(i).setProperty(FilterPipeline.MOL_ID_PROPERTY_NAME, i);
        }
    }

    /**
     * Returns the MolIDs assigned to the atom containers in the given atom container set as integer array. A MolID is
     * assigned to every atom container of an atom container set that was given to or returned by the .filter() method
     * of this class. This method may only be used for atom container sets that meet this criterion. Otherwise, an
     * IllegalArgumentException gets thrown.
     *
     * @param anAtomContainerSet IAtomContainerSet instance the MolID array should be returned of
     * @return Integer array of the atom containers MolIDs
     * @throws NullPointerException if the given instance of IAtomContainerSet or an AtomContainer contained by it
     * is null
     * @throws IllegalArgumentException if an AtomContainer of the given IAtomContainerSet instance has no MolID
     * assigned or the MolID is not of data type Integer
     */
    public int[] getArrayOfAssignedMolIDs(IAtomContainerSet anAtomContainerSet) throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of IAtomContainerSet) is null.");
        final int[] tmpMolIDArray = new int[anAtomContainerSet.getAtomContainerCount()];
        for (int i = 0; i < tmpMolIDArray.length; i++) {
            try {
                tmpMolIDArray[i] = this.getAssignedMolID(anAtomContainerSet.getAtomContainer(i));
            } catch (NullPointerException aNullPointerException) {
                throw new NullPointerException("AtomContainer " + i + " of the given IAtomContainerSet instance is null.");
            } catch (IllegalArgumentException anIllegalArgumentException) {
                throw new IllegalArgumentException("AtomContainer " + i + " of the given AtomContainerSet has no MolID " +
                        "assigned or the MolID is not of data type Integer.");
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
     * @throws IllegalArgumentException if the given IAtomContainer instance has no MolID assigned or the MolID is not
     * of data type Integer
     */
    public int getAssignedMolID(IAtomContainer anAtomContainer) throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of IAtomContainer) is null.");
        if (anAtomContainer.getProperty(FilterPipeline.MOL_ID_PROPERTY_NAME) == null) {
            throw new IllegalArgumentException("The given IAtomContainer instance has no MolID assigned.");
        }
        if (anAtomContainer.getProperty(FilterPipeline.MOL_ID_PROPERTY_NAME).getClass() != Integer.class) {
            throw new IllegalArgumentException("The MolID assigned to the given IAtomContainer instance is not of " +
                    "data type Integer.");
        }
        return anAtomContainer.getProperty(FilterPipeline.MOL_ID_PROPERTY_NAME);
    }

    /**
     * Returns the FilterIDs assigned to the atom containers in the given atom container set as integer array. A
     * FilterID is assigned to every atom container of an atom container set that was given to or returned by the
     * .filter() method of this class. This method may only be used for atom container sets that meet this criterion.
     * For atom containers that have no FilterID set or an FilterID that is not of type integer, the corresponding
     * entry in the returned array is set to an error value (-1).
     *
     * @param anAtomContainerSet IAtomContainerSet instance the FilterID array should be returned of
     * @return Integer array of the atom containers FilterIDs
     * @throws NullPointerException if the given instance of IAtomContainerSet or an AtomContainer contained by it
     * is null
     * @throws IllegalArgumentException if an AtomContainer contained by the given IAtomContainerSet instance has no
     * FilterID assigned or the FilterID is not of data type Integer
     */
    public int[] getArrayOfAssignedFilterIDs(IAtomContainerSet anAtomContainerSet) throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of IAtomContainerSet) is null.");
        final int[] tmpFilterIDArray = new int[anAtomContainerSet.getAtomContainerCount()];
        for (int i = 0; i < tmpFilterIDArray.length; i++) {
            try {
                tmpFilterIDArray[i] = this.getAssignedFilterID(anAtomContainerSet.getAtomContainer(i));
            } catch (NullPointerException aNullPointerException) {
                throw new NullPointerException("AtomContainer " + i + " of the given IAtomContainerSet instance is null.");
            } catch (IllegalArgumentException anIllegalArgumentException) {
                throw new IllegalArgumentException("AtomContainer " + i + " of the given IAtomContainerSet instance " +
                        "has no FilterID assigned or the FilterID is not of data type Integer.");
            }
        }
        return tmpFilterIDArray;
    }

    /**
     * Returns the FilterID assigned to the given atom container. A FilterID is assigned to every atom container of an
     * atom container set that was given to or returned by the .filter() method of this class. This method may not be
     * used for any atom container that does not meet this criterion.
     *
     * @param anAtomContainer IAtomContainer instance the FilterID should be returned of
     * @return FilterID (integer value) of the given AtomContainer
     * @throws NullPointerException if the given instance of IAtomContainer is null
     * @throws IllegalArgumentException if the given IAtomContainer instance has no FilterID assigned or the FilterID
     * is not of data type Integer
     */
    public int getAssignedFilterID(IAtomContainer anAtomContainer) throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of IAtomContainer) is null.");
        if (anAtomContainer.getProperty(FilterPipeline.FILTER_ID_PROPERTY_NAME) == null) {
            throw new IllegalArgumentException("The given IAtomContainer instance has no FilterID assigned.");
        }
        if (anAtomContainer.getProperty(FilterPipeline.FILTER_ID_PROPERTY_NAME).getClass() != Integer.class) {
            throw new IllegalArgumentException("The FilterID assigned to the given IAtomContainer instance is not of " +
                    "data type Integer.");
        }
        return anAtomContainer.getProperty(FilterPipeline.FILTER_ID_PROPERTY_NAME);
    }

    /**
     * Returns the list of selected filters.
     *
     * @return LinkedList of IFilter instances
     */
    public LinkedList<IFilter> getListOfSelectedFilters() {
        return this.listOfSelectedFilters;
    }

}
