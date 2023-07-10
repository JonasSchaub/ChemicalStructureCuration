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

package de.unijena.cheminf.curation.processingSteps;

import de.unijena.cheminf.curation.enums.MassComputationFlavours;
import de.unijena.cheminf.curation.processingSteps.filters.HasAllValidAtomicNumbersFilter;
import de.unijena.cheminf.curation.processingSteps.filters.HasInvalidAtomicNumbersFilter;
import de.unijena.cheminf.curation.processingSteps.filters.IFilter;
import de.unijena.cheminf.curation.processingSteps.filters.MaxAtomCountFilter;
import de.unijena.cheminf.curation.processingSteps.filters.MaxBondCountFilter;
import de.unijena.cheminf.curation.processingSteps.filters.MaxBondsOfSpecificBondOrderFilter;
import de.unijena.cheminf.curation.processingSteps.filters.MaxHeavyAtomCountFilter;
import de.unijena.cheminf.curation.processingSteps.filters.MaxMolecularMassFilter;
import de.unijena.cheminf.curation.processingSteps.filters.MinAtomCountFilter;
import de.unijena.cheminf.curation.processingSteps.filters.MinBondCountFilter;
import de.unijena.cheminf.curation.processingSteps.filters.MinBondsOfSpecificBondOrderFilter;
import de.unijena.cheminf.curation.processingSteps.filters.MinHeavyAtomCountFilter;
import de.unijena.cheminf.curation.processingSteps.filters.MinMolecularMassFilter;
import de.unijena.cheminf.curation.reporter.IReporter;
import de.unijena.cheminf.curation.reporter.MarkDownReporter;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IBond;

import java.util.LinkedList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A high-level API for curating, standardizing and filtering sets of molecules in a pipeline of multiple processing
 * steps.
 * TODO (use the DepictionGenerator as guideline)
 * TODO: demonstrate usage of pipeline exemplary
 * Use the {@link #addProcessingStep(IProcessingStep)} method to add processing steps to the pipeline no convenience
 * method exists for.
 * ...
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public class CurationPipeline extends BaseProcessingStep {

    /*
    TODO: adopt test methods to pipeline changes
        - check whether they are still doing their job (throw no exception)
        - check whether they need to be removed / replaced
        - check the doc-comments
        - new test methods might be necessary
    //
    TODO: is one constructor enough or shall there be "convenience constructors"?  @Felix, @Jonas
        at the moment (since there is no reporting) I only use the convenience constructor
    //
    TODO: remove param checks out of .with...Filter() methods?
    //
    TODO (optional):
        - method to deep copy / clone a CurationPipeline?
     */

    /**
     * Logger of this class.
     */
    private static final Logger LOGGER = Logger.getLogger(CurationPipeline.class.getName());

    /**
     * Linked list that contains all processing steps (instances of IProcessingStep) that were added to the pipeline.
     */
    private final LinkedList<IProcessingStep> listOfPipelineSteps;

    /** TODO
     * Constructor. Initializes the curation pipeline.
     * <br>
     * At the reporting of a curation process, the MolID (assigned to each atom container before or during a curation
     * process) is used for a unique identification of each atom container; no optional second identifier is used.
     * The reporting of the curation / processing of a set of atom containers is done using the default reporter.
     */
    public CurationPipeline() {
        this(null, null);
    }

    /** TODO
     * Constructor. At reporting of a curation processes, the atom container property with the given name (String
     * parameter) is used as a second identifier for each atom container in addition to the MolID, an identifier
     * assigned to each atom container during a curation process.
     * <br>
     * The reporting of the curation / processing of a set of atom container is done using the given reporter. If
     * null is given, the default reporter is used.
     *
     * @param aReporter IReporter instance to use for the creation of the report file
     * @param anOptionalIdentifierPropertyName optional string with the name of the atom container property that
     *                                         contains an optional second identifier to be used at reporting of a
     *                                         curation process; if null is given, no second identifier is used
     */
    public CurationPipeline(IReporter aReporter, String anOptionalIdentifierPropertyName) {
        super(aReporter, anOptionalIdentifierPropertyName);
        this.listOfPipelineSteps = new LinkedList<>();
    }

    /**
     * {@inheritDoc}
     * <p>
     * The given atom container set is sequentially processed by all steps of the pipeline. All steps report to the
     * reporter of the pipeline.
     * </p>
     */
    @Override
    public IAtomContainerSet process(IAtomContainerSet anAtomContainerSet, boolean aCloneBeforeProcessing,
                                     boolean anAssignIdentifiers) throws Exception {
        return super.process(anAtomContainerSet, aCloneBeforeProcessing, anAssignIdentifiers);
    }

    /**
     * {@inheritDoc}
     * <p>
     * The given atom container set is sequentially processed by all steps of the pipeline. Issues encountered with
     * structures are reported to the reporter of this instance.
     * </p>
     */
    @Override
    protected IAtomContainerSet applyLogic(IAtomContainerSet anAtomContainerSet) throws NullPointerException {
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of IAtomContainerSet) is null.");
        IAtomContainerSet tmpResultingACSet = anAtomContainerSet;
        //
        try {
            for (IProcessingStep tmpProcessingStep : this.listOfPipelineSteps) {
                if (tmpResultingACSet == null || tmpResultingACSet.isEmpty()) {
                    break;
                }
                tmpResultingACSet = tmpProcessingStep.process(tmpResultingACSet, false, false);
                //TODO: is there a way to set an initial atom container count to fasten the processing?  @Felix, @Jonas
                // so far I have not been able to find one (it is a protected method that does so)
            }
        } catch (Exception anUnexpectedException) {
            CurationPipeline.LOGGER.log(Level.SEVERE, "The curation process was interrupted by an unexpected" +
                    " exception: " + anUnexpectedException.toString(), anUnexpectedException);
            //TODO: create some kind of notification / message to append to the report file - discuss with Max (or Felix, Jonas)
            return null;
        }
        return tmpResultingACSet;
    }

    //<editor-fold desc="with...Filter methods" defaultstate="collapsed">
    /**
     * Adds a max atom count filter with the given parameters to the curation pipeline. Implicit hydrogen atoms may or
     * may not be considered; atom containers that equal the given max atom count do not get filtered.
     *
     * @param aMaxAtomCount integer value of the max atom count to filter by
     * @param aConsiderImplicitHydrogens boolean value whether to consider implicit hydrogen atoms
     * @return the CurationPipeline instance itself
     * @throws IllegalArgumentException if the given max atom count has a negative value
     */
    public CurationPipeline withMaxAtomCountFilter(int aMaxAtomCount, boolean aConsiderImplicitHydrogens) throws IllegalArgumentException {
        if (aMaxAtomCount < 0) {
            throw new IllegalArgumentException("aMaxAtomCount (integer value) was < than 0.");
        }
        IFilter tmpFilter = new MaxAtomCountFilter(aMaxAtomCount, aConsiderImplicitHydrogens);
        this.addToListOfProcessingSteps(tmpFilter);
        return this;
    }

    /**
     * Adds a min atom count filter with the given parameters to the curation pipeline. Implicit hydrogen atoms may or
     * may not be considered; atom containers that equal the given min atom count do not get filtered.
     *
     * @param aMinAtomCount integer value of the min atom count to filter by
     * @param aConsiderImplicitHydrogens boolean value whether to consider implicit hydrogen atoms
     * @return the CurationPipeline instance itself
     * @throws IllegalArgumentException if the given min atom count has a negative value
     */
    public CurationPipeline withMinAtomCountFilter(int aMinAtomCount, boolean aConsiderImplicitHydrogens) throws IllegalArgumentException {
        if (aMinAtomCount < 0) {
            throw new IllegalArgumentException("aMinAtomCount (integer value) was < than 0.");
        }
        IFilter tmpFilter = new MinAtomCountFilter(aMinAtomCount, aConsiderImplicitHydrogens);
        this.addToListOfProcessingSteps(tmpFilter);
        return this;
    }

    /**
     * Adds a max heavy atom count filter with the given max heavy atom count to the curation pipeline. Atom containers
     * that equal the given max heavy atom count do not get filtered.
     *
     * @param aMaxHeavyAtomCount integer value of the max atom count to filter by
     * @return the CurationPipeline instance itself
     * @throws IllegalArgumentException if the given max heavy atom count has a negative value
     */
    public CurationPipeline withMaxHeavyAtomCountFilter(int aMaxHeavyAtomCount) throws IllegalArgumentException {
        if (aMaxHeavyAtomCount < 0) {
            throw new IllegalArgumentException("aMaxHeavyAtomCount (integer value) was < than 0.");
        }
        IFilter tmpFilter = new MaxHeavyAtomCountFilter(aMaxHeavyAtomCount);
        this.addToListOfProcessingSteps(tmpFilter);
        return this;
    }

    /**
     * Adds a min heavy atom count filter with the given min heavy atom count to the curation pipeline. Atom containers
     * that equal the given min heavy atom count do not get filtered.
     *
     * @param aMinHeavyAtomCount integer value of the min atom count to filter by
     * @return the CurationPipeline instance itself
     * @throws IllegalArgumentException if the given min heavy atom count has a negative value
     */
    public CurationPipeline withMinHeavyAtomCountFilter(int aMinHeavyAtomCount) throws IllegalArgumentException {
        if (aMinHeavyAtomCount < 0) {
            throw new IllegalArgumentException("aMinHeavyAtomCount (integer value) was < than 0.");
        }
        IFilter tmpFilter = new MinHeavyAtomCountFilter(aMinHeavyAtomCount);
        this.addToListOfProcessingSteps(tmpFilter);
        return this;
    }

    /**
     * Adds a max bond count filter with the given parameters to the curation pipeline. Bonds to implicit hydrogen atoms
     * may or may not be considered; atom containers that equal the given max bond count do not get filtered.
     *
     * @param aMaxBondCount integer value of the max bond count to filter by
     * @param aConsiderImplicitHydrogens boolean value whether to consider bonds to implicit hydrogen atoms
     * @return the CurationPipeline instance itself
     * @throws IllegalArgumentException if the given max bond count has a negative value
     */
    public CurationPipeline withMaxBondCountFilter(int aMaxBondCount, boolean aConsiderImplicitHydrogens) throws IllegalArgumentException {
        if (aMaxBondCount < 0) {
            throw new IllegalArgumentException("aMaxBondCount (integer value) was < than 0.");
        }
        IFilter tmpFilter = new MaxBondCountFilter(aMaxBondCount, aConsiderImplicitHydrogens);
        this.addToListOfProcessingSteps(tmpFilter);
        return this;
    }

    /**
     * Adds a min bond count filter with the given parameters to the curation pipeline. Bonds to implicit hydrogen atoms
     * may or may not be considered; atom containers that equal the given min bond count do not get filtered.
     *
     * @param aMinBondCount integer value of the min bond count to filter by
     * @param aConsiderImplicitHydrogens boolean value whether to consider bonds to implicit hydrogen atoms
     * @return the CurationPipeline instance itself
     * @throws IllegalArgumentException if the given min bond count has a negative value
     */
    public CurationPipeline withMinBondCountFilter(int aMinBondCount, boolean aConsiderImplicitHydrogens) throws IllegalArgumentException {
        if (aMinBondCount < 0) {
            throw new IllegalArgumentException("aMinBondCount (integer value) was < than 0.");
        }
        IFilter tmpFilter = new MinBondCountFilter(aMinBondCount, aConsiderImplicitHydrogens);
        this.addToListOfProcessingSteps(tmpFilter);
        return this;
    }

    /**
     * Adds a max bonds of specific bond order filter with the given parameters to the curation pipeline. Bonds to
     * implicit hydrogen atoms may or may not be considered when counting bonds of bond order single; atom containers
     * that equal the given max specific bond count do not get filtered.
     *
     * @param aBondOrder bond order of bonds to count and filter on
     * @param aMaxSpecificBondCount  integer value of the max specific bond count to filter by
     * @param aConsiderImplicitHydrogens boolean value whether to consider bonds to implicit hydrogen atoms; this is
     *                                   only relevant when counting bonds of the order one / single
     * @return the CurationPipeline instance itself
     * @throws IllegalArgumentException if the given max specific bond count has a negative value
     */
    public CurationPipeline withMaxBondsOfSpecificBondOrderFilter(
            IBond.Order aBondOrder, int aMaxSpecificBondCount, boolean aConsiderImplicitHydrogens
    ) throws IllegalArgumentException {
        if (aMaxSpecificBondCount < 0) {
            throw new IllegalArgumentException("aMaxSpecificBondCount (integer value) was < than 0.");
        }
        IFilter tmpFilter = new MaxBondsOfSpecificBondOrderFilter(aBondOrder, aMaxSpecificBondCount, aConsiderImplicitHydrogens);
        this.addToListOfProcessingSteps(tmpFilter);
        return this;
    }

    /**
     * Adds a min bonds of specific bond order filter with the given parameters to the curation pipeline. Bonds to
     * implicit hydrogen atoms may or may not be considered when counting bonds of bond order single; atom containers
     * that equal the given min specific bond count do not get filtered.
     *
     * @param aBondOrder bond order of bonds to count and filter on
     * @param aMinSpecificBondCount  integer value of the min specific bond count to filter by
     * @param aConsiderImplicitHydrogens boolean value whether to consider bonds to implicit hydrogen atoms; this is
     *                                   only relevant when counting bonds of the order one / single
     * @return the CurationPipeline instance itself
     * @throws IllegalArgumentException if the given min specific bond count has a negative value
     */
    public CurationPipeline withMinBondsOfSpecificBondOrderFilter(
            IBond.Order aBondOrder, int aMinSpecificBondCount, boolean aConsiderImplicitHydrogens
    ) throws IllegalArgumentException {
        if (aMinSpecificBondCount < 0) {
            throw new IllegalArgumentException("aMinSpecificBondCount (integer value) was < than 0.");
        }
        IFilter tmpFilter = new MinBondsOfSpecificBondOrderFilter(aBondOrder, aMinSpecificBondCount, aConsiderImplicitHydrogens);
        this.addToListOfProcessingSteps(tmpFilter);
        return this;
    }

    /**
     * Adds a has all valid atomic numbers filter with the given boolean parameter to the curation pipeline.
     *
     * @param aWildcardAtomicNumberIsValid boolean value whether the wildcard atomic number zero should be considered
     *                                     as a valid atomic number
     * @return the CurationPipeline instance itself
     */
    public CurationPipeline withHasAllValidAtomicNumbersFilter(boolean aWildcardAtomicNumberIsValid) {
        IFilter tmpFilter = new HasAllValidAtomicNumbersFilter(aWildcardAtomicNumberIsValid);
        this.addToListOfProcessingSteps(tmpFilter);
        return this;
    }

    /**
     * Adds a has invalid atomic numbers filter with the given boolean parameter to the curation pipeline.
     *
     * @param aWildcardAtomicNumberIsValid boolean value whether the wildcard atomic number zero should be considered
     *                                     as a valid atomic number
     * @return the CurationPipeline instance itself
     */
    public CurationPipeline withHasInvalidAtomicNumbersFilter(boolean aWildcardAtomicNumberIsValid) {
        IFilter tmpFilter = new HasInvalidAtomicNumbersFilter(aWildcardAtomicNumberIsValid);
        this.addToListOfProcessingSteps(tmpFilter);
        return this;
    }

    /**
     * Adds a max molecular mass filter with the given parameters to the curation pipeline. The given mass computation
     * flavour switches the computation type of the mass calculation; atom containers that equal the given max
     * molecular mass value do not get filtered.
     *
     * @param aMaxMolecularMass double value of the max molecular mass value to filter by
     * @param aMassComputationFlavour MassComputationFlavours constant that switches the computation type of the mass
     *                                calculation
     * @return the CurationPipeline instance itself
     * @throws NullPointerException if the given mass computation flavour is null
     * @throws IllegalArgumentException if the given max molecular mass is of a negative value
     * @see MassComputationFlavours
     * @see org.openscience.cdk.tools.manipulator.AtomContainerManipulator#getMass(IAtomContainer, int)
     */
    public CurationPipeline withMaxMolecularMassFilter(double aMaxMolecularMass, MassComputationFlavours aMassComputationFlavour)
            throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(aMassComputationFlavour, "aMassComputationFlavour (MassComputationFlavours constant) is null.");
        if (aMaxMolecularMass < 0) {
            throw new IllegalArgumentException("aMaxMolecularMass (double value) is < than 0.");
        }
        IFilter tmpFilter = new MaxMolecularMassFilter(aMaxMolecularMass, aMassComputationFlavour);
        this.addToListOfProcessingSteps(tmpFilter);
        return this;
    }

    /**
     * Adds a max molecular mass filter with the given max molecular mass value to the curation pipeline. This method
     * takes no mass computation flavour; for the new filter the default 'mass flavour' {@link MassComputationFlavours
     * #MolWeight} is used. Atom containers that equal the given max molecular mass value do not get filtered.
     *
     * @param aMaxMolecularMass double value of the max molecular mass value to filter by
     * @return the CurationPipeline instance itself
     * @throws IllegalArgumentException if the given max molecular mass is of a negative value
     * @see MassComputationFlavours
     * @see org.openscience.cdk.tools.manipulator.AtomContainerManipulator#getMass(IAtomContainer, int)
     */
    public CurationPipeline withMaxMolecularMassFilter(double aMaxMolecularMass) throws IllegalArgumentException {
        if (aMaxMolecularMass < 0) {
            throw new IllegalArgumentException("aMaxMolecularMass (double value) is < than 0.");
        }
        IFilter tmpFilter = new MaxMolecularMassFilter(aMaxMolecularMass);
        this.addToListOfProcessingSteps(tmpFilter);
        return this;
    }

    /**
     * Adds a min molecular mass filter with the given parameters to the curation pipeline. The given mass computation
     * flavour switches the computation type of the mass calculation; atom containers that equal the given min
     * molecular mass value do not get filtered.
     *
     * @param aMinMolecularMass double value of the min molecular mass value to filter by
     * @param aMassComputationFlavour MassComputationFlavours constant that switches the computation type of the mass
     *                                calculation
     * @return the CurationPipeline instance itself
     * @throws NullPointerException if the given mass computation flavour is null
     * @throws IllegalArgumentException if the given min molecular mass is of a negative value
     * @see MassComputationFlavours
     * @see org.openscience.cdk.tools.manipulator.AtomContainerManipulator#getMass(IAtomContainer, int)
     */
    public CurationPipeline withMinMolecularMassFilter(double aMinMolecularMass, MassComputationFlavours aMassComputationFlavour)
            throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(aMassComputationFlavour, "aMassComputationFlavour (MassComputationFlavours constant) is null.");
        if (aMinMolecularMass < 0) {
            throw new IllegalArgumentException("aMinMolecularMass (double value) is < than 0.");
        }
        IFilter tmpFilter = new MinMolecularMassFilter(aMinMolecularMass, aMassComputationFlavour);
        this.addToListOfProcessingSteps(tmpFilter);
        return this;
    }

    /**
     * Adds a min molecular mass filter with the given min molecular mass value to the curation pipeline. This method
     * takes no mass computation flavour; for the new filter the default 'mass flavour' {@link MassComputationFlavours
     * #MolWeight} is used. Atom containers that equal the given min molecular mass value do not get filtered.
     *
     * @param aMinMolecularMass double value of the min molecular mass value to filter by
     * @return the CurationPipeline instance itself
     * @throws IllegalArgumentException if the given min molecular mass is of a negative value
     * @see MassComputationFlavours
     * @see org.openscience.cdk.tools.manipulator.AtomContainerManipulator#getMass(IAtomContainer, int)
     */
    public CurationPipeline withMinMolecularMassFilter(double aMinMolecularMass) throws IllegalArgumentException {
        if (aMinMolecularMass < 0) {
            throw new IllegalArgumentException("aMinMolecularMass (double value) is < than 0.");
        }
        IFilter tmpFilter = new MinMolecularMassFilter(aMinMolecularMass);
        this.addToListOfProcessingSteps(tmpFilter);
        return this;
    }
    //</editor-fold>

    /**
     * Adds the given processing step to the curation pipeline. This method may be used to manually add
     * IProcessingStep instances to the pipeline for which no convenience method - in the form of .with...Filter() -
     * exist. This allows the usage of subsequently implemented IProcessingStep implementations.
     *
     * @param aProcessingStep the IProcessingStep instance that is to be added to the pipeline
     * @return the CurationPipeline instance itself
     * @throws NullPointerException if the given IProcessingStep instance is null
     */
    public CurationPipeline addProcessingStep(IProcessingStep aProcessingStep) throws NullPointerException {
        Objects.requireNonNull(aProcessingStep, "aProcessingStep (instance of IProcessingStep) is null.");
        this.addToListOfProcessingSteps(aProcessingStep);
        return this;
    }

    /**
     * Adds the given processing step to the list of processing steps and sets its fields optionalIDPropertyName and
     * reporter to the ones of the pipeline; sets the flag of the processing step whether the reporter is
     * self-contained to false; sets the identifier of the processing step to the respective ID of the pipeline added
     * by the index of the new step in the pipeline.
     *
     * @param aProcessingStep IProcessingStep to add to the list
     * @throws NullPointerException if the given IProcessingStep instance is null
     */
    private void addToListOfProcessingSteps(IProcessingStep aProcessingStep) throws NullPointerException {
        Objects.requireNonNull(aProcessingStep, "aProcessingStep (instance of IProcessingStep) is null.");
        this.listOfPipelineSteps.add(aProcessingStep);
        aProcessingStep.setOptionalIDPropertyName(this.getOptionalIDPropertyName());
        aProcessingStep.setReporter(this.getReporter());
        aProcessingStep.setIsReporterSelfContained(false);
        aProcessingStep.setPipelineProcessingStepID(
                ((this.getPipelineProcessingStepID() == null) ?
                        "" : this.getPipelineProcessingStepID() + ".") +
                        (this.listOfPipelineSteps.size() - 1)
        );
    }

    /**
     * Returns the processing step of the pipeline with the specified index.
     *
     * @param anIndex the index of the processing step
     * @return the processing step with the given index
     * @throws IndexOutOfBoundsException if the index is out of range ({@code index < 0 || index >=
     * listOfPipelineSteps.size()})
     */
    public IProcessingStep getProcessingStepOfIndex(int anIndex) throws IndexOutOfBoundsException {
        return this.listOfPipelineSteps.get(anIndex);
    }

    /**
     * Returns the list that contains all processing steps that were added to the pipeline.
     *
     * @return LinkedList of IProcessingStep instances
     */
    public LinkedList<IProcessingStep> getListOfPipelineSteps() {
        return this.listOfPipelineSteps;
    }

    /**
     * {@inheritDoc}
     * <br>
     * This optional identifier property name is also set to every processing step of the pipeline.
     */
    @Override
    public void setOptionalIDPropertyName(String anOptionalIDPropertyName) {
        super.setOptionalIDPropertyName(anOptionalIDPropertyName);
        this.listOfPipelineSteps.forEach(aProcessingStep -> {
            aProcessingStep.setOptionalIDPropertyName(anOptionalIDPropertyName);
        });
    }

    /**
     * Sets the reporter of the pipeline and of every processing step in the pipeline; if given null, an instance of
     * {@link MarkDownReporter} is used by default.
     *
     * @param aReporter IReporter instance
     */
    @Override
    public void setReporter(IReporter aReporter) {
        super.setReporter(aReporter);
        this.listOfPipelineSteps.forEach(aProcessingStep -> {
            aProcessingStep.setReporter(this.getReporter());
        });
    }

    /**
     * Sets the identifier of the pipeline and - in combination with the respective index of the step in this pipeline
     * - to every processing step that is part of this pipeline. The identifier should equal the index the pipeline has
     * in a superordinate pipeline and may be null if there is no superordinate pipeline.
     *
     * @param aProcessingStepID String with the index or null if it is not part of a pipeline
     */
    @Override
    public void setPipelineProcessingStepID(String aProcessingStepID) {
        super.setPipelineProcessingStepID(aProcessingStepID);
        String tmpSubordinateID;
        if (aProcessingStepID != null) {
            tmpSubordinateID = aProcessingStepID + ".";
        } else {
            tmpSubordinateID = "";
        }
        for (int i = 0; i < this.listOfPipelineSteps.size(); i++) {
            this.listOfPipelineSteps.get(i).setPipelineProcessingStepID(tmpSubordinateID + i);
        }
    }

}
