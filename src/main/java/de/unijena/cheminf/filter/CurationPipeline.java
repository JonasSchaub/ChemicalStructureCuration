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

import de.unijena.cheminf.BaseProcessingStep;
import de.unijena.cheminf.IProcessingStep;
import de.unijena.cheminf.MassComputationFlavours;
import de.unijena.cheminf.curation.ChemicalStructureCurationUtils;
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
import de.unijena.cheminf.reporter.IReporter;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IBond;

import java.util.LinkedList;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * A high-level API for curating, standardizing or filtering sets of molecules.
 * TODO (use the DepictionGenerator as guideline)
 *
 * Class for creating/building pipelines of multiple filters for filtering sets of atom containers based on molecular descriptors.
 * Only contains filters that are based on molecular descriptors and can be applied for each atom container separately
 * (without knowledge of the other atom containers - as it would be necessary e.g. for filtering duplicates).
 */
public class CurationPipeline extends BaseProcessingStep {

    /*
    TODO: remove parameter tests of filters out of FilterPipeline methods?
    TODO: use the .withFilter() method in the .with...Filter() convenience methods?
    //
    TODO: adopt test methods to pipeline changes
        - check whether they are still doing their job (throw no exception)
        - check whether they need to be removed / replaced
        - check the doc-comments
    TODO:
        - initialize reporter
        - remove reporter as parameter
        - make file destination adjustable without the need to set a new reporter?
    //
    TODO (optional):
    - method to deep copy / clone a FilterPipeline?
    - find a shorter name for the MolID property (?)
    - methods for clearing MolIDs / FilterIDs of atom containers?
     */

    /**
     * String of the name of the atom container property for uniquely identifying an atom container during the filtering
     * process.
     */
    public static final String MOL_ID_PROPERTY_NAME = "FilterPipeline.MolID";

    /**
     * Logger of this class.
     */
    private static final Logger LOGGER = Logger.getLogger(CurationPipeline.class.getName());

    /**
     * Linked list that contains all processing steps (instances of IProcessingStep) that were added to the pipeline.
     */
    protected final LinkedList<IProcessingStep> listOfSelectedPipelineSteps;

    /**
     * Name string of the atom container property that contains an optional second identifier of type integer. If the
     * String is not null, no second identifier is used.
     */
    protected final String optionalIDPropertyName;

    /**
     * Constructor. At reporting of a curation process, the MolID (assigned to each atom container during a curation
     * process) is used for a unique identification of each atom container.
     */
    public CurationPipeline() {
        this(null);
    }

    /**
     * Constructor. At reporting of a curation processes, the atom container property with the given name (String
     * parameter) is used as a second identifier for each atom container in addition to the MolID, an identifier
     * assigned to each atom container during a curation process.
     *
     * @param aNameOfAtomContainerProperty Name string of the atom container property to be used at reporting of a
     *                                     curation process; if null is given, no second identifier is used
     * @throws IllegalArgumentException if the given property name string is blank
     */
    public CurationPipeline(String aNameOfAtomContainerProperty) throws IllegalArgumentException {
        if (aNameOfAtomContainerProperty != null && aNameOfAtomContainerProperty.isBlank()) {
            throw new IllegalArgumentException("The given String aNameOfAtomContainerProperty is blank.");
        }
        this.optionalIDPropertyName = aNameOfAtomContainerProperty;
        this.listOfSelectedPipelineSteps = new LinkedList<>();
    }

    /**
     * Applies the curation pipeline on the given set of atom containers and returns the set of those who passed all
     * curation steps. To uniquely identify the atom containers during the process, each atom container gets a unique
     * MolID ("CurationPipeline.MolID") in form of an integer type property assigned.
     * TODO
     * In order to trace by which step an atom container got filtered by, every atom container gets the index of the filter in the list of
     * selected filters assigned as FilterID (integer property "FilterPipeline.FilterID"). Atom containers that do
     * not get filtered get a FilterID of negative one. TODO: FilterID is removed
     * Atom containers do not pass a curation step if they cause an exception to be thrown.
     *
     * @param anAtomContainerSet set of atom containers to be processed
     * @return atom container set of all atom containers that passed the filter pipeline
     * @throws NullPointerException if the given IAtomContainerSet instance is null
     */
    public IAtomContainerSet curate(IAtomContainerSet anAtomContainerSet) throws NullPointerException {
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of IAtomContainerSet) is null.");
        this.assignMolIdToAtomContainers(anAtomContainerSet);   //TODO: think about this again
        IAtomContainerSet tmpClonedACSet = ChemicalStructureCurationUtils.cloneAtomContainerSet(this.getReporter(), anAtomContainerSet);
        return this.process(tmpClonedACSet, this.getReporter(), null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IAtomContainerSet process(IAtomContainerSet anAtomContainerSet, IReporter aReporter, Integer anIndexInPipeline) throws NullPointerException {
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of IAtomContainerSet) is null.");
        Objects.requireNonNull(aReporter, "aReporter (instance of IReporter) is null.");
        //this.assignMolIdToAtomContainers(anAtomContainerSet); TODO: think about this again
        IAtomContainerSet tmpACSetToProcess;
        IAtomContainerSet tmpResultingACSet = anAtomContainerSet;
        IProcessingStep tmpProcessingStep;

        for (int i = 0; i < this.listOfSelectedPipelineSteps.size(); i++) {
            tmpProcessingStep = this.listOfSelectedPipelineSteps.get(i);
            tmpACSetToProcess = tmpResultingACSet;
            tmpResultingACSet = tmpProcessingStep.process(tmpACSetToProcess, aReporter, i);
            //TODO: is there a way to set an initial atom container count?

        }

        this.getReporter().report();    //TODO: directly report? this would cause problems with the lot of test methods
        return tmpResultingACSet;
    }

    //<editor-fold desc="with...Filter methods" default-state="collapsed">
    /**
     * Adds a max atom count filter with the given parameters to the filter pipeline. Implicit hydrogen atoms may or
     * may not be considered; atom containers that equal the given max atom count do not get filtered.
     *
     * @param aMaxAtomCount integer value of the max atom count to filter by
     * @param aConsiderImplicitHydrogens boolean value whether to consider implicit hydrogen atoms
     * @return the FilterPipeline instance itself
     * @throws IllegalArgumentException if the given max atom count has a negative value
     */
    public CurationPipeline withMaxAtomCountFilter(int aMaxAtomCount, boolean aConsiderImplicitHydrogens) throws IllegalArgumentException {
        if (aMaxAtomCount < 0) {    //TODO: param checks here or log those of the filter's constructor?
            throw new IllegalArgumentException("aMaxAtomCount (integer value) was < than 0.");
        }
        IFilter tmpFilter = new MaxAtomCountFilter(aMaxAtomCount, aConsiderImplicitHydrogens);
        this.listOfSelectedPipelineSteps.add(tmpFilter);
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
    public CurationPipeline withMinAtomCountFilter(int aMinAtomCount, boolean aConsiderImplicitHydrogens) throws IllegalArgumentException {
        if (aMinAtomCount < 0) {    //TODO: param checks here or log those of the filter's constructor?
            throw new IllegalArgumentException("aMinAtomCount (integer value) was < than 0.");
        }
        IFilter tmpFilter = new MinAtomCountFilter(aMinAtomCount, aConsiderImplicitHydrogens);
        this.listOfSelectedPipelineSteps.add(tmpFilter);
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
    public CurationPipeline withMaxHeavyAtomCountFilter(int aMaxHeavyAtomCount) throws IllegalArgumentException {
        if (aMaxHeavyAtomCount < 0) {    //TODO: param checks here or log those of the filter's constructor?
            throw new IllegalArgumentException("aMaxHeavyAtomCount (integer value) was < than 0.");
        }
        IFilter tmpFilter = new MaxHeavyAtomCountFilter(aMaxHeavyAtomCount);
        this.listOfSelectedPipelineSteps.add(tmpFilter);
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
    public CurationPipeline withMinHeavyAtomCountFilter(int aMinHeavyAtomCount) throws IllegalArgumentException {
        if (aMinHeavyAtomCount < 0) {    //TODO: param checks here or log those of the filter's constructor?
            throw new IllegalArgumentException("aMinHeavyAtomCount (integer value) was < than 0.");
        }
        IFilter tmpFilter = new MinHeavyAtomCountFilter(aMinHeavyAtomCount);
        this.listOfSelectedPipelineSteps.add(tmpFilter);
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
    public CurationPipeline withMaxBondCountFilter(int aMaxBondCount, boolean aConsiderImplicitHydrogens) throws IllegalArgumentException {
        if (aMaxBondCount < 0) {    //TODO: param checks here or log those of the filter's constructor?
            throw new IllegalArgumentException("aMaxBondCount (integer value) was < than 0.");
        }
        IFilter tmpFilter = new MaxBondCountFilter(aMaxBondCount, aConsiderImplicitHydrogens);
        this.listOfSelectedPipelineSteps.add(tmpFilter);
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
    public CurationPipeline withMinBondCountFilter(int aMinBondCount, boolean aConsiderImplicitHydrogens) throws IllegalArgumentException {
        if (aMinBondCount < 0) {    //TODO: param checks here or log those of the filter's constructor?
            throw new IllegalArgumentException("aMinBondCount (integer value) was < than 0.");
        }
        IFilter tmpFilter = new MinBondCountFilter(aMinBondCount, aConsiderImplicitHydrogens);
        this.listOfSelectedPipelineSteps.add(tmpFilter);
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
    public CurationPipeline withMaxBondsOfSpecificBondOrderFilter(
            IBond.Order aBondOrder, int aMaxSpecificBondCount, boolean aConsiderImplicitHydrogens
    ) throws IllegalArgumentException {
        if (aMaxSpecificBondCount < 0) {    //TODO: param checks here or log those of the filter's constructor?
            throw new IllegalArgumentException("aMaxSpecificBondCount (integer value) was < than 0.");
        }
        IFilter tmpFilter = new MaxBondsOfSpecificBondOrderFilter(aBondOrder, aMaxSpecificBondCount, aConsiderImplicitHydrogens);
        this.listOfSelectedPipelineSteps.add(tmpFilter);
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
    public CurationPipeline withMinBondsOfSpecificBondOrderFilter(
            IBond.Order aBondOrder, int aMinSpecificBondCount, boolean aConsiderImplicitHydrogens
    ) throws IllegalArgumentException {
        if (aMinSpecificBondCount < 0) {    //TODO: param checks here or log those of the filter's constructor?
            throw new IllegalArgumentException("aMinSpecificBondCount (integer value) was < than 0.");
        }
        IFilter tmpFilter = new MinBondsOfSpecificBondOrderFilter(aBondOrder, aMinSpecificBondCount, aConsiderImplicitHydrogens);
        this.listOfSelectedPipelineSteps.add(tmpFilter);
        return this;
    }

    /**
     * Adds a has all valid atomic numbers filter with the given boolean parameter to the filter pipeline.
     *
     * @param aWildcardAtomicNumberIsValid boolean value whether the wildcard atomic number zero should be considered
     *                                     as a valid atomic number
     * @return the FilterPipeline instance itself
     */
    public CurationPipeline withHasAllValidAtomicNumbersFilter(boolean aWildcardAtomicNumberIsValid) {
        IFilter tmpFilter = new HasAllValidAtomicNumbersFilter(aWildcardAtomicNumberIsValid);
        this.listOfSelectedPipelineSteps.add(tmpFilter);
        return this;
    }

    /**
     * Adds a has invalid atomic numbers filter with the given boolean parameter to the filter pipeline.
     *
     * @param aWildcardAtomicNumberIsValid boolean value whether the wildcard atomic number zero should be considered
     *                                     as a valid atomic number
     * @return the FilterPipeline instance itself
     */
    public CurationPipeline withHasInvalidAtomicNumbersFilter(boolean aWildcardAtomicNumberIsValid) {
        IFilter tmpFilter = new HasInvalidAtomicNumbersFilter(aWildcardAtomicNumberIsValid);
        this.listOfSelectedPipelineSteps.add(tmpFilter);
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
    public CurationPipeline withMaxMolecularMassFilter(double aMaxMolecularMass, MassComputationFlavours aMassComputationFlavour) throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(aMassComputationFlavour, "aMassComputationFlavour (MassComputationFlavours constant) is null.");
        if (aMaxMolecularMass < 0) {    //TODO: param checks here or log those of the filter's constructor?
            throw new IllegalArgumentException("aMaxMolecularMass (double value) is < than 0.");
        }
        IFilter tmpFilter = new MaxMolecularMassFilter(aMaxMolecularMass, aMassComputationFlavour);
        this.listOfSelectedPipelineSteps.add(tmpFilter);
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
    public CurationPipeline withMaxMolecularMassFilter(double aMaxMolecularMass) throws IllegalArgumentException {
        if (aMaxMolecularMass < 0) {    //TODO: param checks here or log those of the filter's constructor?
            throw new IllegalArgumentException("aMaxMolecularMass (double value) is < than 0.");
        }
        IFilter tmpFilter = new MaxMolecularMassFilter(aMaxMolecularMass);
        this.listOfSelectedPipelineSteps.add(tmpFilter);
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
    public CurationPipeline withMinMolecularMassFilter(double aMinMolecularMass, MassComputationFlavours aMassComputationFlavour) throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(aMassComputationFlavour, "aMassComputationFlavour (MassComputationFlavours constant) is null.");
        if (aMinMolecularMass < 0) {    //TODO: param checks here or log those of the filter's constructor?
            throw new IllegalArgumentException("aMinMolecularMass (double value) is < than 0.");
        }
        IFilter tmpFilter = new MinMolecularMassFilter(aMinMolecularMass, aMassComputationFlavour);
        this.listOfSelectedPipelineSteps.add(tmpFilter);
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
    public CurationPipeline withMinMolecularMassFilter(double aMinMolecularMass) throws IllegalArgumentException {
        if (aMinMolecularMass < 0) {    //TODO: param checks here or log those of the filter's constructor?
            throw new IllegalArgumentException("aMinMolecularMass (double value) is < than 0.");
        }
        IFilter tmpFilter = new MinMolecularMassFilter(aMinMolecularMass);
        this.listOfSelectedPipelineSteps.add(tmpFilter);
        return this;
    }
    //</editor-fold>

    /**
     * Adds the given filter to the filter pipeline. This method may be used to manually add a Filter instance to the
     * filter pipeline for which no convenience method - in the form of .with...Filter() - is available. This allows
     * the usage of subsequently implemented filters.
     *
     * @param aProcessingStep the IProcessingStep instance that is to be added to the pipeline
     * @return the FilterPipeline instance itself
     * @throws NullPointerException if the given Filter instance is null
     */
    public CurationPipeline addProcessingStep(IProcessingStep aProcessingStep) throws NullPointerException {
        Objects.requireNonNull(aProcessingStep, "aProcessingStep (instance of Filter) is null.");
        this.listOfSelectedPipelineSteps.add(aProcessingStep);
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
            anAtomContainerSet.getAtomContainer(i).setProperty(CurationPipeline.MOL_ID_PROPERTY_NAME, i);
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
        if (anAtomContainer.getProperty(CurationPipeline.MOL_ID_PROPERTY_NAME) == null) {
            throw new IllegalArgumentException("The given IAtomContainer instance has no MolID assigned.");
        }
        if (anAtomContainer.getProperty(CurationPipeline.MOL_ID_PROPERTY_NAME).getClass() != Integer.class) {
            throw new IllegalArgumentException("The MolID assigned to the given IAtomContainer instance is not of " +
                    "data type Integer.");
        }
        return anAtomContainer.getProperty(CurationPipeline.MOL_ID_PROPERTY_NAME);
    }

    /**
     * Returns the list of selected filters.
     *
     * @return LinkedList of IFilter instances
     */
    public LinkedList<IProcessingStep> getListOfSelectedPipelineSteps() {
        return this.listOfSelectedPipelineSteps;
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
