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

import de.unijena.cheminf.MassComputationFlavours;
import de.unijena.cheminf.filter.filters.HasAllValidAtomicNumbersFilter;
import de.unijena.cheminf.filter.filters.HasInvalidAtomicNumbersFilter;
import de.unijena.cheminf.filter.filters.MaxAtomCountFilter;
import de.unijena.cheminf.filter.filters.MaxBondCountFilter;
import de.unijena.cheminf.filter.filters.MaxBondsOfSpecificBondOrderFilter;
import de.unijena.cheminf.filter.filters.MaxHeavyAtomCountFilter;
import de.unijena.cheminf.filter.filters.MaxMolecularMassFilter;
import de.unijena.cheminf.filter.filters.MinAtomCountFilter;
import de.unijena.cheminf.filter.filters.MinBondCountFilter;
import de.unijena.cheminf.filter.filters.MinBondsOfSpecificBondOrderFilter;
import de.unijena.cheminf.filter.filters.MinHeavyAtomCountFilter;
import de.unijena.cheminf.filter.filters.MinMolecularMassFilter;
import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IBond;

import java.util.BitSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    TODO: doc comments and test methods for new class fields
    TODO: getters for new class fields
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
     * Logger of this class.
     */
    private static final Logger LOGGER = Logger.getLogger(FilterPipeline.class.getName());

    /**
     * Linked list of the IFilter instances that were added to this filter pipeline. Filters can be added via the filter
     * specific convenience methods or using the .withFilter() method of this class.
     */
    protected final LinkedList<IFilter> listOfSelectedFilters;

    /**
     * Name string of the atom container property optionally used at the reporting of a filtering process.
     */
    protected final String optionalIDPropertyName;

    /**
     * TODO
     */
    protected int latestFilteringProcess_numberOfGivenACs;

    /**
     * TODO
     */
    protected int latestFilteringProcess_numberOfReturnedACs;

    /**
     * TODO
     */
    protected int[] latestFilteringProcess_numberOfACsFilteredByEachFilter;

    /** TODO: realize what is written in the comment
     * Constructor. At reporting of filtering processes, the MolID (assigned to each atom container during filtering
     * process) is used for a unique identification of each atom container.
     */
    public FilterPipeline() {
        this((String) null);
    }

    /** TODO: realize what is written in the comment
     * Constructor. At reporting of filtering processes, the atom container property with the given name (String
     * parameter) is used for a unique identification of each atom container in addition to the MolID, an identifier
     * assigned to each atom container during filtering process.
     *
     * @param aNameOfAtomContainerProperty Name string of the atom container property to be used at reporting of a
     *                                     filtering process; if null is given,
     * @throws IllegalArgumentException if the given property name string is blank
     */
    public FilterPipeline(String aNameOfAtomContainerProperty) throws IllegalArgumentException {
        if (aNameOfAtomContainerProperty != null && aNameOfAtomContainerProperty.isBlank()) {
            throw new IllegalArgumentException("The given String aNameOfAtomContainerProperty is blank.");
        }
        this.optionalIDPropertyName = aNameOfAtomContainerProperty;
        this.listOfSelectedFilters = new LinkedList<>();
    }

    /** TODO: remove this constructor (?!)
     * Protected Constructor. Generates a copy of the original Filter instance maintaining all its fields.
     *
     * @param anOriginalFilterPipeline FilterPipeline instance to generate the copy of
     */
    protected FilterPipeline(FilterPipeline anOriginalFilterPipeline) {
        this.optionalIDPropertyName = anOriginalFilterPipeline.optionalIDPropertyName;
        this.listOfSelectedFilters = anOriginalFilterPipeline.listOfSelectedFilters;
    }

    /**
     * Applies the filter pipeline on the given set of atom containers and returns the set of those who passed all
     * filters. To uniquely identify the atom containers during the filtering process, each atom container gets a
     * unique MolID ("FilterPipeline.MolID") in form of an integer type property assigned. In order to trace by which
     * filter an atom container got filtered by, every atom container gets the index of the filter in the list of
     * selected filters assigned as FilterID (integer property "FilterPipeline.FilterID"). Atom containers that do
     * not get filtered get a FilterID of negative one.
     * Atom containers do not pass a filter if they cause an exception to be thrown.    TODO
     *
     * @param anAtomContainerSet set of atom containers to be filtered
     * @return atom container set of all atom containers that passed the filter pipeline
     * @throws NullPointerException if the given IAtomContainerSet instance is null
     */
    public IAtomContainerSet filter(IAtomContainerSet anAtomContainerSet) throws NullPointerException {
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of IAtomContainerSet) is null.");
        this.latestFilteringProcess_numberOfGivenACs = anAtomContainerSet.getAtomContainerCount();
        this.latestFilteringProcess_numberOfACsFilteredByEachFilter = new int[this.listOfSelectedFilters.size()];
        this.assignMolIdToAtomContainers(anAtomContainerSet);
        final BitSet tmpIsFilteredBitSet = new BitSet(anAtomContainerSet.getAtomContainerCount());
        boolean tmpIsFilteredFlag;
        IAtomContainer tmpAtomContainer;

        for (int tmpIndexOfFilter = 0; tmpIndexOfFilter < this.listOfSelectedFilters.size(); tmpIndexOfFilter++) {
            for (int i = 0; i < anAtomContainerSet.getAtomContainerCount(); i++) {
                if (!tmpIsFilteredBitSet.get(i)) {
                    tmpAtomContainer = anAtomContainerSet.getAtomContainer(i);
                    try {
                        tmpIsFilteredFlag = this.listOfSelectedFilters.get(tmpIndexOfFilter).isFiltered(tmpAtomContainer);
                    } catch (Exception anException) {
                        FilterPipeline.LOGGER.log(Level.INFO, anException.toString(), anException);   //TODO: which level to log at?
                        tmpIsFilteredFlag = true;
                    }
                    if (tmpIsFilteredFlag) {
                        tmpAtomContainer.setProperty(FilterPipeline.FILTER_ID_PROPERTY_NAME, tmpIndexOfFilter);
                        tmpIsFilteredBitSet.set(i);
                        this.latestFilteringProcess_numberOfACsFilteredByEachFilter[tmpIndexOfFilter]++;
                    }
                }
            }
        }
        final IAtomContainerSet tmpFilteredACSet = new AtomContainerSet();
        for (int i = 0; i < anAtomContainerSet.getAtomContainerCount(); i++) {
            tmpAtomContainer = anAtomContainerSet.getAtomContainer(i);
            if (!tmpIsFilteredBitSet.get(i)) {
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

        this.latestFilteringProcess_numberOfReturnedACs = tmpFilteredACSet.getAtomContainerCount();

        return tmpFilteredACSet;
    }

    /**
     * Adds a max atom count filter with the given parameters to the filter pipeline. Implicit hydrogen atoms may or
     * may not be considered; atom containers that equal the given max atom count do not get filtered.
     *
     * @param aMaxAtomCount integer value of the max atom count to filter by
     * @param aConsiderImplicitHydrogens boolean value whether to consider implicit hydrogen atoms
     * @return the FilterPipeline instance itself
     * @throws IllegalArgumentException if the given max atom count has a negative value
     */
    public FilterPipeline withMaxAtomCountFilter(int aMaxAtomCount, boolean aConsiderImplicitHydrogens) throws IllegalArgumentException {
        if (aMaxAtomCount < 0) {    //TODO: param checks here or log those of the filter's constructor?
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
     * @throws IllegalArgumentException if the given min atom count has a negative value
     */
    public FilterPipeline withMinAtomCountFilter(int aMinAtomCount, boolean aConsiderImplicitHydrogens) throws IllegalArgumentException {
        if (aMinAtomCount < 0) {    //TODO: param checks here or log those of the filter's constructor?
            throw new IllegalArgumentException("aMinAtomCount (integer value) was < than 0.");
        }
        IFilter tmpFilter = new MinAtomCountFilter(aMinAtomCount, aConsiderImplicitHydrogens);
        this.listOfSelectedFilters.add(tmpFilter);
        return this;
    }

    /**
     * Adds a max heavy atom count filter with the given max heavy atom count to the filter pipeline. Atom containers
     * that equal the given max heavy atom count do not get filtered.
     *
     * @param aMaxHeavyAtomCount integer value of the max atom count to filter by
     * @return the FilterPipeline instance itself
     * @throws IllegalArgumentException if the given max heavy atom count has a negative value
     */
    public FilterPipeline withMaxHeavyAtomCountFilter(int aMaxHeavyAtomCount) throws IllegalArgumentException {
        if (aMaxHeavyAtomCount < 0) {    //TODO: param checks here or log those of the filter's constructor?
            throw new IllegalArgumentException("aMaxHeavyAtomCount (integer value) was < than 0.");
        }
        IFilter tmpFilter = new MaxHeavyAtomCountFilter(aMaxHeavyAtomCount);
        this.listOfSelectedFilters.add(tmpFilter);
        return this;
    }

    /**
     * Adds a min heavy atom count filter with the given min heavy atom count to the filter pipeline. Atom containers
     * that equal the given min heavy atom count do not get filtered.
     *
     * @param aMinHeavyAtomCount integer value of the min atom count to filter by
     * @return the FilterPipeline instance itself
     * @throws IllegalArgumentException if the given min heavy atom count has a negative value
     */
    public FilterPipeline withMinHeavyAtomCountFilter(int aMinHeavyAtomCount) throws IllegalArgumentException {
        if (aMinHeavyAtomCount < 0) {    //TODO: param checks here or log those of the filter's constructor?
            throw new IllegalArgumentException("aMinHeavyAtomCount (integer value) was < than 0.");
        }
        IFilter tmpFilter = new MinHeavyAtomCountFilter(aMinHeavyAtomCount);
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
     * @throws IllegalArgumentException if the given max bond count has a negative value
     */
    public FilterPipeline withMaxBondCountFilter(int aMaxBondCount, boolean aConsiderImplicitHydrogens) throws IllegalArgumentException {
        if (aMaxBondCount < 0) {    //TODO: param checks here or log those of the filter's constructor?
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
     * @throws IllegalArgumentException if the given min bond count has a negative value
     */
    public FilterPipeline withMinBondCountFilter(int aMinBondCount, boolean aConsiderImplicitHydrogens) throws IllegalArgumentException {
        if (aMinBondCount < 0) {    //TODO: param checks here or log those of the filter's constructor?
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
     * @throws IllegalArgumentException if the given max specific bond count has a negative value
     */
    public FilterPipeline withMaxBondsOfSpecificBondOrderFilter(
            IBond.Order aBondOrder, int aMaxSpecificBondCount, boolean aConsiderImplicitHydrogens
    ) throws IllegalArgumentException {
        if (aMaxSpecificBondCount < 0) {    //TODO: param checks here or log those of the filter's constructor?
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
     * @throws IllegalArgumentException if the given min specific bond count has a negative value
     */
    public FilterPipeline withMinBondsOfSpecificBondOrderFilter(
            IBond.Order aBondOrder, int aMinSpecificBondCount, boolean aConsiderImplicitHydrogens
    ) throws IllegalArgumentException {
        if (aMinSpecificBondCount < 0) {    //TODO: param checks here or log those of the filter's constructor?
            throw new IllegalArgumentException("aMinSpecificBondCount (integer value) was < than 0.");
        }
        IFilter tmpFilter = new MinBondsOfSpecificBondOrderFilter(aBondOrder, aMinSpecificBondCount, aConsiderImplicitHydrogens);
        this.listOfSelectedFilters.add(tmpFilter);
        return this;
    }

    /**
     * Adds a has all valid atomic numbers filter with the given boolean parameter to the filter pipeline.
     *
     * @param aWildcardAtomicNumberIsValid boolean value whether the wildcard atomic number zero should be considered
     *                                     as a valid atomic number
     * @return the FilterPipeline instance itself
     */
    public FilterPipeline withHasAllValidAtomicNumbersFilter(boolean aWildcardAtomicNumberIsValid) {
        IFilter tmpFilter = new HasAllValidAtomicNumbersFilter(aWildcardAtomicNumberIsValid);
        this.listOfSelectedFilters.add(tmpFilter);
        return this;
    }

    /**
     * Adds a has invalid atomic numbers filter with the given boolean parameter to the filter pipeline.
     *
     * @param aWildcardAtomicNumberIsValid boolean value whether the wildcard atomic number zero should be considered
     *                                     as a valid atomic number
     * @return the FilterPipeline instance itself
     */
    public FilterPipeline withHasInvalidAtomicNumbersFilter(boolean aWildcardAtomicNumberIsValid) {
        IFilter tmpFilter = new HasInvalidAtomicNumbersFilter(aWildcardAtomicNumberIsValid);
        this.listOfSelectedFilters.add(tmpFilter);
        return this;
    }

    /**
     * Adds a max molecular mass filter with the given parameters to the filter pipeline. The given mass computation
     * flavour switches the computation type of the mass calculation; atom containers that equal the given max
     * molecular mass value do not get filtered.
     *
     * @param aMaxMolecularMass double value of the max molecular mass value to filter by
     * @param aMassComputationFlavour MassComputationFlavours constant that switches the computation type of the mass
     *                                calculation
     * @return the FilterPipeline instance itself
     * @throws NullPointerException if the given MassComputationFlavours value is null
     * @throws IllegalArgumentException if the given max molecular mass is of a negative value
     * @see MassComputationFlavours
     * @see org.openscience.cdk.tools.manipulator.AtomContainerManipulator#getMass(IAtomContainer, int)
     */
    public FilterPipeline withMaxMolecularMassFilter(double aMaxMolecularMass, MassComputationFlavours aMassComputationFlavour) throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(aMassComputationFlavour, "aMassComputationFlavour (MassComputationFlavours constant) is null.");
        if (aMaxMolecularMass < 0) {    //TODO: param checks here or log those of the filter's constructor?
            throw new IllegalArgumentException("aMaxMolecularMass (double value) is < than 0.");
        }
        IFilter tmpFilter = new MaxMolecularMassFilter(aMaxMolecularMass, aMassComputationFlavour);
        this.listOfSelectedFilters.add(tmpFilter);
        return this;
    }

    /**
     * Adds a max molecular mass filter with the given max molecular mass value to the filter pipeline.
     * This method takes no mass computation flavour; for the new filter the default 'mass flavour'
     * {@link MassComputationFlavours#MolWeight} is used. Atom containers that equal the given max
     * molecular mass value do not get filtered.
     *
     * @param aMaxMolecularMass double value of the max molecular mass value to filter by
     * @return the FilterPipeline instance itself
     * @throws IllegalArgumentException if the given max molecular mass is of a negative value
     * @see MassComputationFlavours
     * @see org.openscience.cdk.tools.manipulator.AtomContainerManipulator#getMass(IAtomContainer, int)
     */
    public FilterPipeline withMaxMolecularMassFilter(double aMaxMolecularMass) throws IllegalArgumentException {
        if (aMaxMolecularMass < 0) {    //TODO: param checks here or log those of the filter's constructor?
            throw new IllegalArgumentException("aMaxMolecularMass (double value) is < than 0.");
        }
        IFilter tmpFilter = new MaxMolecularMassFilter(aMaxMolecularMass);
        this.listOfSelectedFilters.add(tmpFilter);
        return this;
    }

    /**
     * Adds a min molecular mass filter with the given parameters to the filter pipeline. The given mass computation
     * flavour switches the computation type of the mass calculation; atom containers that equal the given min
     * molecular mass value do not get filtered.
     *
     * @param aMinMolecularMass double value of the min molecular mass value to filter by
     * @param aMassComputationFlavour MassComputationFlavours constant that switches the computation type of the mass
     *                                calculation
     * @return the FilterPipeline instance itself
     * @throws NullPointerException if the given MassComputationFlavours value is null
     * @throws IllegalArgumentException if the given min molecular mass is of a negative value
     * @see MassComputationFlavours
     * @see org.openscience.cdk.tools.manipulator.AtomContainerManipulator#getMass(IAtomContainer, int)
     */
    public FilterPipeline withMinMolecularMassFilter(double aMinMolecularMass, MassComputationFlavours aMassComputationFlavour) throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(aMassComputationFlavour, "aMassComputationFlavour (MassComputationFlavours constant) is null.");
        if (aMinMolecularMass < 0) {    //TODO: param checks here or log those of the filter's constructor?
            throw new IllegalArgumentException("aMinMolecularMass (double value) is < than 0.");
        }
        IFilter tmpFilter = new MinMolecularMassFilter(aMinMolecularMass, aMassComputationFlavour);
        this.listOfSelectedFilters.add(tmpFilter);
        return this;
    }

    /**
     * Adds a min molecular mass filter with the given min molecular mass value to the filter pipeline.
     * This method takes no mass computation flavour; for the new filter the default 'mass flavour'
     * {@link MassComputationFlavours#MolWeight} is used. Atom containers that equal the given min
     * molecular mass value do not get filtered.
     *
     * @param aMinMolecularMass double value of the min molecular mass value to filter by
     * @return the FilterPipeline instance itself
     * @throws IllegalArgumentException if the given min molecular mass is of a negative value
     * @see MassComputationFlavours
     * @see org.openscience.cdk.tools.manipulator.AtomContainerManipulator#getMass(IAtomContainer, int)
     */
    public FilterPipeline withMinMolecularMassFilter(double aMinMolecularMass) throws IllegalArgumentException {
        if (aMinMolecularMass < 0) {    //TODO: param checks here or log those of the filter's constructor?
            throw new IllegalArgumentException("aMinMolecularMass (double value) is < than 0.");
        }
        IFilter tmpFilter = new MinMolecularMassFilter(aMinMolecularMass);
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
     * purpose, each atom container is assigned an integer property of name "FilterPipeline.MolID". The assigned MolID
     * equals the index of the atom container in the given atom container set.
     *
     * @param anAtomContainerSet IAtomContainerSet to whose atom containers MolIDs should be assigned
     * @throws NullPointerException if the given IAtomContainerSet instance is null
     */
    protected void assignMolIdToAtomContainers(IAtomContainerSet anAtomContainerSet) throws NullPointerException {
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of IAtomContainerSet) is null.");
        for (int i = 0; i < anAtomContainerSet.getAtomContainerCount(); i++) {
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

    /**
     * Returns the optional name string of the atom container property containing an identifier. This field is
     * specified in the constructor.
     *
     * @return String optionalIDPropertyName
     */
    public String getOptionalIDPropertyName() {
        return optionalIDPropertyName;
    }

}
