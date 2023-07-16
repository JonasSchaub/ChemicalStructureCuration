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
import de.unijena.cheminf.curation.processingSteps.filters.propertyCheckers.BasePropertyChecker;
import de.unijena.cheminf.curation.processingSteps.filters.propertyCheckers.MolIDChecker;
import de.unijena.cheminf.curation.processingSteps.filters.propertyCheckers.OptionalIDChecker;
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
 * A high-level API for curating, standardizing and filtering sets of molecules in a pipeline of multiple
 * processing steps.
 *
 * <br>
 * <b>General Usage</b>
 * Create a curation pipeline and configure it for respective use cases using {@code .with...()} and
 * {@code .add...()} methods.
 * <pre>{@code
 * CurationPipeline tmpCurationPipeline = new CurationPipeline(...)
 *                 .withMaxAtomCountFilter(20, true)
 *                 .withMinAtomCountFilter(5, true)
 *                 .withHasAllValidAtomicNumbersFilter(false);
 * //
 * IAtomContainerSet tmpProcessedACSet = tmpCurationPipeline.process(tmpMoleculeSet, true, true);
 * }</pre>
 *
 * <b>One Line Quick Use</b>
 * For simplified use, we can create a pipeline and use it once for a single curation process.
 * <pre>{@code
 * new CurationPipeline(...).withMaxAtomCountFilter(20, true)
 *                 .withMinBondCountFilter(5, false)
 *                 .process(tmpMoleculeSet, false, true);
 * }</pre>
 *
 * A curation pipeline can be initialized passing the constructor either an IReporter instance
 * <pre>{@code
 * CurationPipeline tmpPipeline = new CurationPipeline(anIReporterInstance);
 * }</pre>
 * or a directory path name.
 * <pre>{@code
 * CurationPipeline tmpPipeline = new CurationPipeline(aReportFilesDirectoryPathString);
 * }</pre>
 * The latter will lead to the reporter of the pipeline being initialized with an instance of {@link MarkDownReporter}.
 * <br>
 * <br>
 * <b>Further Info</b>
 * In general, every atom container needs a "MolID" to be processed by a processing step. This can be addressed by
 * setting the second boolean parameter of the {@code .process()} method to true. The first may be used to clone the
 * given atom containers before processing.
 * <br>
 * Since missing identifiers of structures might trouble the report generation or the pipeline itself, the initial
 * pipeline steps might be used to check for their existence.
 * <pre>{@code
 * new CurationPipeline(...).withMolIDChecker()
 *                          .withOptionalIDChecker();
 * }</pre>
 * The existence of the optional IDs only needs to be checked if a respective property name has been given to the
 * pipeline (see optional second constructor parameter and respective setter).
 *
 * <br>
 * For a description of the two boolean parameters of the {@code .process()}-method, see the respective
 * method description.
 * <br>
 * To manually add any instance of {@link IProcessingStep} (including instances of CurationPipeline) to the pipeline,
 * use the {@code .addProcessingStep()} method. This option might especially be used for processing steps no respective
 * convenience method exists for (including CurationPipeline instances).
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
    TODO: remove param checks out of .with...Filter() methods?
    //
    TODO (optional):
        - method to deep copy / clone a CurationPipeline?
    //
    TODO: it may never happen since the reporter of the pipeline shall never be null, but in theory all the methods
     adding a step to the pipeline could throw a NullPointerException; do I need to address this?
     */

    /**
     * Logger of this class.
     */
    private static final Logger LOGGER = Logger.getLogger(CurationPipeline.class.getName());

    /**
     * Linked list that contains all processing steps (instances of IProcessingStep) that were added to the pipeline.
     */
    private final LinkedList<IProcessingStep> listOfPipelineSteps;

    //<editor-fold desc="Constructors" defaultstate="collapsed">
    /**
     * Constructor; initializes the curation pipeline and sets the reporter and the optional ID property name;
     * initializes the list of pipeline steps.
     * <br>
     * The option of the second identifier property might be used if information such as name of the structures or their
     * CAS registry numbers exists. If this field is set to anything else than null, every atom container processed by
     * this curation pipeline is expected to have a property with the respective name. The given information is then
     * included in the report. The reports are generated using the given reporter. To initialize the curation pipeline
     * with a default reporter (instance of {@link MarkDownReporter}), see the other constructors.
     *
     * @param aReporter the reporter to generate the reports with when processing sets of structures
     * @param anOptionalIDPropertyName name string of the atom container property containing an optional second
     *                                 identifier (e.g. the name of the structures or CAS Registry Numbers); if given
     *                                 null, no second identifier is used; otherwise every atom container processed by
     *                                 this processing step is expected to have this property
     * @throws NullPointerException if the given IReporter instance is null
     * @throws IllegalArgumentException if an optional ID property name is given, but it is blank or empty
     * @see #CurationPipeline(String, String)
     * @see #CurationPipeline(String)
     * @see #CurationPipeline(IReporter)
     */
    public CurationPipeline(IReporter aReporter, String anOptionalIDPropertyName)
            throws NullPointerException, IllegalArgumentException {
        super(aReporter, anOptionalIDPropertyName);
        this.listOfPipelineSteps = new LinkedList<>();
    }

    /**
     * Constructor; initializes the curation pipeline by calling {@link #CurationPipeline(IReporter, String)} with the
     * given reporter and the optional ID property name set to null. See the description of the respective constructor
     * for more details.
     *
     * @param aReporter the reporter to generate the reports with when processing sets of structures
     * @throws NullPointerException if the given IReporter instance is null
     * @see #CurationPipeline(IReporter, String)
     */
    public CurationPipeline(IReporter aReporter) throws NullPointerException {
        this(aReporter, null);
    }

    /**
     * Constructor; initializes the curation pipeline with a default reporter (instance of {@link MarkDownReporter})
     * that generates reports in markdown-format at the given directory path.
     * <br>
     * The option of the second identifier property might be used if information such as name of the structures or their
     * CAS registry numbers exists. If this field is set to anything else than null, every atom container processed by
     * this curation pipeline is expected to have a property with the respective name. The given information is then
     * included in the report. To initialize the curation pipeline with a specific reporter, see the respective
     * constructors.
     *
     * @param aReportFilesDirectoryPath the directory path for the MarkDownReporter to create the report files at
     * @param anOptionalIDPropertyName name string of the atom container property containing an optional second
     *                                 identifier (e.g. the name of the structures or CAS Registry Numbers); if given
     *                                 null, no second identifier is used; otherwise every atom container processed by
     *                                 this processing step is expected to have this property
     * @throws NullPointerException if the given String containing the directory path is null
     * @throws IllegalArgumentException if the given file path is no directory path; if a property name string is given,
     *                                  but it is blank or empty
     * @see #CurationPipeline(IReporter, String)
     * @see #CurationPipeline(IReporter)
     * @see #CurationPipeline(String)
     */
    public CurationPipeline(String aReportFilesDirectoryPath, String anOptionalIDPropertyName)
            throws NullPointerException, IllegalArgumentException {
        super(aReportFilesDirectoryPath, anOptionalIDPropertyName);
        this.listOfPipelineSteps = new LinkedList<>();
    }

    /**
     * Constructor; initializes the curation pipeline by calling {@link #CurationPipeline(String, String)} with the
     * given directory path and the optional ID property name set to null. The pipeline is initialized with a default
     * reporter (instance of {@link MarkDownReporter}) that generates reports in markdown-format at the given directory
     * path.
     *
     * @param aReportFilesDirectoryPath the directory path for the MarkDownReporter to create the report files at
     * @throws NullPointerException if the given String containing the directory path is null
     * @throws IllegalArgumentException if the given file path is no directory path
     * @see #CurationPipeline(String, String)
     */
    public CurationPipeline(String aReportFilesDirectoryPath) throws NullPointerException, IllegalArgumentException {
        this(aReportFilesDirectoryPath, null);
    }
    //</editor-fold>

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
            }
        } catch (Exception anUnexpectedException) {
            CurationPipeline.LOGGER.log(Level.SEVERE, "The curation process was interrupted by an unexpected" +
                    " exception: " + anUnexpectedException.toString(), anUnexpectedException);
            //TODO: create some kind of notification / message to append to the report file - discuss with Max (or Felix, Jonas)
            return null;
        }
        return tmpResultingACSet;
    }

    //<editor-fold desc="withPropertyChecker methods" defaultstate="collapsed">
    /**
     * Adds a step to the pipeline that checks all given atom containers whether they have a MolID (atom container
     * property of name {@link #MOL_ID_PROPERTY_NAME}). It appends a report to the reporter for every atom container
     * that has no MolID and returns only those that have one.
     * <br>
     * <b>Note:</b> In cases where it can not be guarantied that every atom container possesses a MolID, it is advised
     * to add this processing step as initial step of the pipeline to avoid missing MolIDs as cause for exceptions in
     * the further course of the pipeline.
     *
     * @return the CurationPipeline instance itself
     * @see MolIDChecker
     */
    public CurationPipeline withMolIDChecker() {
        BasePropertyChecker tmpMolIDChecker = new MolIDChecker(this.getReporter());
        this.addToListOfProcessingSteps(tmpMolIDChecker);
        return this;
    }

    /**
     * Adds a step to the pipeline that checks all given atom containers whether they have a property with the optional
     * ID property name given to this pipeline ({@link #getOptionalIDPropertyName()}. It appends a report to the
     * reporter for every atom container that does not possess such a property and returns only those that have one.
     * <br>
     * <b>Note:</b> If the pipeline has been given an optional ID property name, it is advised to add this processing
     * step as one of the initial steps to the pipeline to remove atom containers with missing optional ID from the
     * given atom container set and manually check them in the generated report.
     *
     * @return the CurationPipeline instance itself
     * @throws NullPointerException if no optional ID property name has been given to the pipeline
     * @see MolIDChecker
     */
    public CurationPipeline withOptionalIDChecker() throws NullPointerException {
        BasePropertyChecker tmpOptionalIDChecker = new OptionalIDChecker(this.getReporter(), this.getOptionalIDPropertyName());
        this.addToListOfProcessingSteps(tmpOptionalIDChecker);
        return this;
    }
    //</editor-fold>

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
            throw new IllegalArgumentException("aMaxAtomCount (integer value) was below zero.");
        }
        IFilter tmpFilter = new MaxAtomCountFilter(aMaxAtomCount, aConsiderImplicitHydrogens, this.getReporter());
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
            throw new IllegalArgumentException("aMinAtomCount (integer value) was below zero.");
        }
        IFilter tmpFilter = new MinAtomCountFilter(aMinAtomCount, aConsiderImplicitHydrogens, this.getReporter());
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
            throw new IllegalArgumentException("aMaxHeavyAtomCount (integer value) was below zero.");
        }
        IFilter tmpFilter = new MaxHeavyAtomCountFilter(aMaxHeavyAtomCount, this.getReporter());
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
            throw new IllegalArgumentException("aMinHeavyAtomCount (integer value) was below zero.");
        }
        IFilter tmpFilter = new MinHeavyAtomCountFilter(aMinHeavyAtomCount, this.getReporter());
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
            throw new IllegalArgumentException("aMaxBondCount (integer value) was below zero.");
        }
        IFilter tmpFilter = new MaxBondCountFilter(aMaxBondCount, aConsiderImplicitHydrogens, this.getReporter());
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
            throw new IllegalArgumentException("aMinBondCount (integer value) was below zero.");
        }
        IFilter tmpFilter = new MinBondCountFilter(aMinBondCount, aConsiderImplicitHydrogens, this.getReporter());
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
            throw new IllegalArgumentException("aMaxSpecificBondCount (integer value) was below zero.");
        }
        IFilter tmpFilter = new MaxBondsOfSpecificBondOrderFilter(aBondOrder, aMaxSpecificBondCount,
                aConsiderImplicitHydrogens, this.getReporter());
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
            throw new IllegalArgumentException("aMinSpecificBondCount (integer value) was below zero.");
        }
        IFilter tmpFilter = new MinBondsOfSpecificBondOrderFilter(aBondOrder, aMinSpecificBondCount,
                aConsiderImplicitHydrogens, this.getReporter());
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
        IFilter tmpFilter = new HasAllValidAtomicNumbersFilter(aWildcardAtomicNumberIsValid, this.getReporter());
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
        IFilter tmpFilter = new HasInvalidAtomicNumbersFilter(aWildcardAtomicNumberIsValid, this.getReporter());
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
            throw new IllegalArgumentException("aMaxMolecularMass (double value) is below zero.");
        }
        IFilter tmpFilter = new MaxMolecularMassFilter(aMaxMolecularMass, aMassComputationFlavour, this.getReporter());
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
            throw new IllegalArgumentException("aMaxMolecularMass (double value) is below zero.");
        }
        IFilter tmpFilter = new MaxMolecularMassFilter(aMaxMolecularMass, this.getReporter());
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
            throw new IllegalArgumentException("aMinMolecularMass (double value) is below zero.");
        }
        IFilter tmpFilter = new MinMolecularMassFilter(aMinMolecularMass, aMassComputationFlavour, this.getReporter());
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
            throw new IllegalArgumentException("aMinMolecularMass (double value) is below zero.");
        }
        IFilter tmpFilter = new MinMolecularMassFilter(aMinMolecularMass, this.getReporter());
        this.addToListOfProcessingSteps(tmpFilter);
        return this;
    }
    //</editor-fold>

    /**
     * Adds the given processing step to the curation pipeline. This method may be used to manually add
     * IProcessingStep instances to the pipeline for which no convenience method - in the form of a {@code .with...()}
     * or {@code .add...()} method - exist. This allows the usage of subsequently implemented IProcessingStep
     * implementations.
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
        aProcessingStep.setReporter(this.getReporter());    //TODO: remove this? or just keep it to make sure?
        aProcessingStep.setIsReporterSelfContained(false);
        aProcessingStep.setPipelineProcessingStepID(
                ((this.getPipelineProcessingStepID() == null) ?
                        "" : this.getPipelineProcessingStepID() + ".") +
                        (this.listOfPipelineSteps.size() - 1)
        );
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
     * This optional identifier property name is also set to every processing step that is part of the pipeline.
     */
    @Override
    public void setOptionalIDPropertyName(String anOptionalIDPropertyName) throws IllegalArgumentException {
        super.setOptionalIDPropertyName(anOptionalIDPropertyName);
        this.listOfPipelineSteps.forEach(aProcessingStep -> {
            aProcessingStep.setOptionalIDPropertyName(anOptionalIDPropertyName);
        });
    }

    /**
     * Sets the reporter of the pipeline and of every processing step in the pipeline. If the reporter has not been
     * specified via a constructor parameter, it is initialized with an instance of {@link MarkDownReporter} by default.
     */
    @Override
    public void setReporter(IReporter aReporter) throws NullPointerException {
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
    public void setPipelineProcessingStepID(String aProcessingStepID) throws IllegalArgumentException {
        super.setPipelineProcessingStepID(aProcessingStepID);
        String tmpSuperordinateID;
        if (aProcessingStepID != null) {
            tmpSuperordinateID = aProcessingStepID + ".";
        } else {
            tmpSuperordinateID = "";
        }
        for (int i = 0; i < this.listOfPipelineSteps.size(); i++) {
            this.listOfPipelineSteps.get(i).setPipelineProcessingStepID(tmpSuperordinateID + i);
        }
    }

}
