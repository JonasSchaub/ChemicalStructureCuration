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

import de.unijena.cheminf.curation.enums.ErrorCodes;
import de.unijena.cheminf.curation.enums.MassComputationFlavours;
import de.unijena.cheminf.curation.processingSteps.filters.ContainsNoPseudoAtomsFilter;
import de.unijena.cheminf.curation.processingSteps.filters.ContainsPseudoAtomsFilter;
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
import de.unijena.cheminf.curation.processingSteps.filters.propertyCheckers.PropertyChecker;
import de.unijena.cheminf.curation.processingSteps.filters.propertyCheckers.OptionalIDChecker;
import de.unijena.cheminf.curation.reporter.IReporter;
import de.unijena.cheminf.curation.reporter.MarkDownReporter;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IPseudoAtom;

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
 * IAtomContainerSet tmpMoleculeSet = new AtomContainerSet();
 * //
 * CurationPipeline tmpCurationPipeline = new CurationPipeline(*aReportFilesDirectoryPathName*)
 *                 .withMaxAtomCountFilter(20, true)
 *                 .withMinAtomCountFilter(5, true)
 *                 .withHasAllValidAtomicNumbersFilter(false);
 * //
 * boolean tmpCloneBeforeProcessing = true;
 * IAtomContainerSet tmpProcessedACSet = tmpCurationPipeline.process(tmpMoleculeSet, tmpCloneBeforeProcessing);
 * }</pre>
 *
 * <b>One Line Quick Use</b>
 * For simplified use, we can create a pipeline and use it once for a single curation process.
 * <pre>{@code
 * boolean tmpCloneBeforeProcessing = true;
 * new CurationPipeline(*aReportFilesDirectoryPathName*)
 *                 .withMaxAtomCountFilter(20, true)
 *                 .withMinBondCountFilter(5, false)
 *                 .process(tmpMoleculeSet, tmpCloneBeforeProcessing);
 * // with exemplary values
 * }</pre>
 *
 * A curation pipeline can be initialized passing the constructor either an IReporter instance
 * <pre>{@code
 * CurationPipeline tmpPipeline = new CurationPipeline(*anIReporterInstance*);
 * }</pre>
 * or a directory path name.
 * <pre>{@code
 * CurationPipeline tmpPipeline = new CurationPipeline(*aDirectoryPathName*);
 * }</pre>
 * The latter will lead to the reporter of the pipeline being initialized with an instance of {@link MarkDownReporter}.
 * The reporter is then used for the reporting of encountered issues with structures.
 *
 * <br><br>
 * <b>Further Info</b>
 * As all processing steps, the curation pipeline gives the option to use a second, optional ID such as name or CAS
 * registry number of the structures. It is then used in the report and complements the "MolID", an automatically
 * assigned identifier that matches the index the atom container has in the processed atom container set. To prevent
 * the original data from modifications, an atom container set might be cloned before the processing. This can be
 * addressed by setting the boolean parameter of the {@link #process} method to true.
 * <br>
 * To check the existence of the optional ID or of any other data annotated to the structures via atom container
 * properties, the following steps might be added to the pipeline:
 * <pre>{@code
 * String tmpOptionalIDPropertyName = CDKConstants.CASRN; // exemplary
 * String tmpNameOfPropertyToCheck = "DataSource";        // exemplary
 * new CurationPipeline(*aReporter*, tmpOptionalIDPropertyName)
 *                 .withOptionalIDChecker()
 *                 .withPropertyChecker(tmpNameOfPropertyToCheck);
 * }</pre>
 *
 * To manually add any instance of {@link IProcessingStep} (including CurationPipeline instances) to the pipeline, the
 * {@code .addProcessingStep()} method may be used. This option might especially be used for processing steps no
 * respective {@code CurationPipeline} method exists for (including instances of CurationPipeline).
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
     adding a step to the pipeline could throw a NullPointerException; do I need to address that?
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
     * <b>Pipeline specific Info</b> The given atom container set is sequentially processed by all steps of the
     * pipeline. All steps report to the same reporter (as long as there have been no changes to reporters of the
     * processing steps after them being added to the pipeline).
     * </p>
     */
    @Override
    public IAtomContainerSet process(IAtomContainerSet anAtomContainerSet, boolean aCloneBeforeProcessing) throws Exception {
        return super.process(anAtomContainerSet, aCloneBeforeProcessing);
    }

    /**
     * {@inheritDoc}
     * <p>
     * The given atom container set is sequentially processed by all steps of the pipeline. Issues with structures
     * encountered by processing steps of the pipeline are reported to the reporter of this pipeline (as long as there
     * have been no changes to reporters of the processing steps after them being added to the pipeline).
     * </p>
     */
    @Override
    protected IAtomContainerSet applyLogic(IAtomContainerSet anAtomContainerSet) throws NullPointerException, Exception {
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of IAtomContainerSet) is null.");
        IAtomContainerSet tmpResultingACSet = anAtomContainerSet;
        //
        for (IProcessingStep tmpProcessingStep : this.listOfPipelineSteps) {
            if (tmpResultingACSet == null || tmpResultingACSet.isEmpty()) {
                break;
            }
            try {
                tmpResultingACSet = tmpProcessingStep.process(tmpResultingACSet, false);
            } catch (Exception aFatalException) {
                // the exception will be re-thrown
                CurationPipeline.LOGGER.severe(String.format("The processing step of class %s with the" +
                                " identifier %s was interrupted by an unexpected exception.",
                        tmpProcessingStep.getClass().getName(), tmpProcessingStep.getPipelineProcessingStepID()));
                throw aFatalException;
            }
        }
        return tmpResultingACSet;
    }

    //<editor-fold desc="withPropertyChecker methods" defaultstate="collapsed">
    /**
     * Adds a step to the pipeline that checks all given atom containers whether they have a specific atom container
     * property. It appends a report to the reporter for every atom container that does not have the respective property
     * and removes the respective atom containers from the returned set.
     * <br>
     * <b>Note:</b> This option might be used to ensure a coherent annotation of data sets.
     *
     * @param aPropertyName the name of the atom container the existence is checked for
     * @param anErrorCode the error code associated with the non-existence of the property
     * @return the CurationPipeline instance itself
     * @see PropertyChecker
     */
    public CurationPipeline withPropertyChecker(String aPropertyName, ErrorCodes anErrorCode) {
        PropertyChecker tmpPropertyChecker = new PropertyChecker(aPropertyName, anErrorCode, this.getReporter());
        this.addToListOfProcessingSteps(tmpPropertyChecker);
        return this;
    }

    /**
     * Calls {@link #withPropertyChecker(String, ErrorCodes)} with {@code ErrorCodes.MISSING_ATOM_CONTAINER_PROPERTY} as
     * default error code.
     *
     * @param aPropertyName the name of the atom container the existence is checked for
     * @return the CurationPipeline instance itself
     * @see #withPropertyChecker(String, ErrorCodes)
     * @see PropertyChecker
     */
    public CurationPipeline withPropertyChecker(String aPropertyName) {
        return this.withPropertyChecker(aPropertyName, ErrorCodes.MISSING_ATOM_CONTAINER_PROPERTY);
    }

    /**
     * Adds a step to the pipeline that checks all given atom containers whether they have a property with the optional
     * ID property name given to this pipeline ({@link #getOptionalIDPropertyName()}. It appends a report to the
     * reporter for every atom container that does not possess such a property and returns only those that have one.
     * <br>
     * <b>Note:</b> If the pipeline has been given an optional ID property name, it is advised to add this processing
     * step as the initial step to the pipeline to remove atom containers with missing optional ID from the given atom
     * container set and manually check them in the generated report.
     *
     * @return the CurationPipeline instance itself
     * @throws NullPointerException if the optional ID property name field of the pipeline has not been specified (via
     *                              constructor or respective setter)
     * @see OptionalIDChecker
     * @see #setOptionalIDPropertyName(String)
     */
    public CurationPipeline withOptionalIDChecker() throws NullPointerException {
        PropertyChecker tmpOptionalIDChecker = new OptionalIDChecker(this.getOptionalIDPropertyName(), this.getReporter());
        this.addToListOfProcessingSteps(tmpOptionalIDChecker);
        return this;
    }
    //</editor-fold>

    //<editor-fold desc="with...Filter methods" defaultstate="collapsed">
    /**
     * Adds a max atom count filter with the given parameters to the curation pipeline. Implicit hydrogen atoms and
     * {@link IPseudoAtom} instances may or may not be considered; atom containers that equal the given max atom count
     * do not get filtered.
     *
     * @param aMaxAtomCount integer value of the max atom count to filter by
     * @param aConsiderImplicitHydrogens boolean value whether to consider implicit hydrogen atoms
     * @param aConsiderPseudoAtoms boolean value whether to consider pseudo-atoms
     * @return the CurationPipeline instance itself
     * @throws IllegalArgumentException if the given max atom count has a negative value
     */
    public CurationPipeline withMaxAtomCountFilter(int aMaxAtomCount, boolean aConsiderImplicitHydrogens,
                                                   boolean aConsiderPseudoAtoms) throws IllegalArgumentException {
        if (aMaxAtomCount < 0) {
            throw new IllegalArgumentException("aMaxAtomCount (integer value) was below zero.");
        }
        IFilter tmpFilter = new MaxAtomCountFilter(aMaxAtomCount, aConsiderImplicitHydrogens,
                aConsiderPseudoAtoms, this.getReporter());
        this.addToListOfProcessingSteps(tmpFilter);
        return this;
    }

    /**
     * Adds a min atom count filter with the given parameters to the curation pipeline. Implicit hydrogen atoms and
     * {@link IPseudoAtom} instances may or may not be considered; atom containers that equal the given min atom count
     * do not get filtered.
     *
     * @param aMinAtomCount integer value of the min atom count to filter by
     * @param aConsiderImplicitHydrogens boolean value whether to consider implicit hydrogen atoms
     * @param aConsiderPseudoAtoms boolean value whether to consider pseudo-atoms
     * @return the CurationPipeline instance itself
     * @throws IllegalArgumentException if the given min atom count has a negative value
     */
    public CurationPipeline withMinAtomCountFilter(int aMinAtomCount, boolean aConsiderImplicitHydrogens,
                                                   boolean aConsiderPseudoAtoms) throws IllegalArgumentException {
        if (aMinAtomCount < 0) {
            throw new IllegalArgumentException("aMinAtomCount (integer value) was below zero.");
        }
        IFilter tmpFilter = new MinAtomCountFilter(aMinAtomCount, aConsiderImplicitHydrogens,
                aConsiderPseudoAtoms, this.getReporter());
        this.addToListOfProcessingSteps(tmpFilter);
        return this;
    }

    /**
     * Adds a max heavy atom count filter with the given max heavy atom count to the curation pipeline. Atom containers
     * that equal the given max heavy atom count do not get filtered.
     *
     * @param aMaxHeavyAtomCount integer value of the max atom count to filter by
     * @param aConsiderPseudoAtoms boolean value whether to consider pseudo-atoms in the heavy atoms count
     * @return the CurationPipeline instance itself
     * @throws IllegalArgumentException if the given max heavy atom count has a negative value
     */
    public CurationPipeline withMaxHeavyAtomCountFilter(int aMaxHeavyAtomCount, boolean aConsiderPseudoAtoms)
            throws IllegalArgumentException {
        if (aMaxHeavyAtomCount < 0) {
            throw new IllegalArgumentException("aMaxHeavyAtomCount (integer value) was below zero.");
        }
        IFilter tmpFilter = new MaxHeavyAtomCountFilter(aMaxHeavyAtomCount, aConsiderPseudoAtoms, this.getReporter());
        this.addToListOfProcessingSteps(tmpFilter);
        return this;
    }

    /**
     * Adds a min heavy atom count filter with the given min heavy atom count to the curation pipeline. Atom containers
     * that equal the given min heavy atom count do not get filtered.
     *
     * @param aMinHeavyAtomCount integer value of the min atom count to filter by
     * @param aConsiderPseudoAtoms boolean value whether to consider pseudo-atoms in the heavy atoms count
     * @return the CurationPipeline instance itself
     * @throws IllegalArgumentException if the given min heavy atom count has a negative value
     */
    public CurationPipeline withMinHeavyAtomCountFilter(int aMinHeavyAtomCount, boolean aConsiderPseudoAtoms)
            throws IllegalArgumentException {
        if (aMinHeavyAtomCount < 0) {
            throw new IllegalArgumentException("aMinHeavyAtomCount (integer value) was below zero.");
        }
        IFilter tmpFilter = new MinHeavyAtomCountFilter(aMinHeavyAtomCount, aConsiderPseudoAtoms, this.getReporter());
        this.addToListOfProcessingSteps(tmpFilter);
        return this;
    }

    /**
     * Adds a max bond count filter with the given parameters to the curation pipeline. Bonds to implicit hydrogen atoms
     * and bonds with participation of instances of {@link IPseudoAtom} may or may not be considered. If bonds of
     * pseudo-atoms are not considered, their bonds to implicit hydrogen atoms are not considered either. Atom
     * containers that equal the given max bond count do not get filtered.
     *
     * @param aMaxBondCount integer value of the max bond count to filter by
     * @param aConsiderImplicitHydrogens boolean value whether to consider bonds to implicit hydrogen atoms
     * @param aConsiderPseudoAtoms boolean value whether to consider bonds to pseudo-atoms and their implicit hydrogens
     * @return the CurationPipeline instance itself
     * @throws IllegalArgumentException if the given max bond count has a negative value
     */
    public CurationPipeline withMaxBondCountFilter(int aMaxBondCount, boolean aConsiderImplicitHydrogens,
                                                   boolean aConsiderPseudoAtoms) throws IllegalArgumentException {
        if (aMaxBondCount < 0) {
            throw new IllegalArgumentException("aMaxBondCount (integer value) was below zero.");
        }
        IFilter tmpFilter = new MaxBondCountFilter(aMaxBondCount, aConsiderImplicitHydrogens,
                aConsiderPseudoAtoms, this.getReporter());
        this.addToListOfProcessingSteps(tmpFilter);
        return this;
    }

    /**
     * Adds a min bond count filter with the given parameters to the curation pipeline. Bonds to implicit hydrogen atoms
     * and bonds with participation of instances of {@link IPseudoAtom} may or may not be considered. If bonds of
     * pseudo-atoms are not considered, their bonds to implicit hydrogen atoms are not considered either. Atom
     * containers that equal the given min bond count do not get filtered.
     *
     * @param aMinBondCount integer value of the min bond count to filter by
     * @param aConsiderImplicitHydrogens boolean value whether to consider bonds to implicit hydrogen atoms
     * @param aConsiderPseudoAtoms boolean value whether to consider bonds to pseudo-atoms and their implicit hydrogens
     * @return the CurationPipeline instance itself
     * @throws IllegalArgumentException if the given min bond count has a negative value
     */
    public CurationPipeline withMinBondCountFilter(int aMinBondCount, boolean aConsiderImplicitHydrogens,
                                                   boolean aConsiderPseudoAtoms) throws IllegalArgumentException {
        if (aMinBondCount < 0) {
            throw new IllegalArgumentException("aMinBondCount (integer value) was below zero.");
        }
        IFilter tmpFilter = new MinBondCountFilter(aMinBondCount, aConsiderImplicitHydrogens,
                aConsiderPseudoAtoms, this.getReporter());
        this.addToListOfProcessingSteps(tmpFilter);
        return this;
    }

    /**
     * Adds a max bonds of specific bond order filter with the given parameters to the curation pipeline. Bonds to
     * implicit hydrogen atoms may or may not be considered when counting bonds of bond order single. If the second
     * boolean parameter is false, instances of {@link IPseudoAtom} and their implicit hydrogen atoms are not taken into
     * account. Atom containers that equal the given max specific bond count do not get filtered.
     *
     * @param aBondOrder bond order of bonds to count and filter on
     * @param aMaxSpecificBondCount  integer value of the max specific bond count to filter by
     * @param aConsiderImplicitHydrogens boolean value whether to consider bonds to implicit hydrogen atoms; this is
     *                                   only relevant when counting bonds of the order one / single
     * @param aConsiderPseudoAtoms boolean value whether to consider bonds to pseudo-atoms and their implicit hydrogens
     * @return the CurationPipeline instance itself
     * @throws IllegalArgumentException if the given max specific bond count has a negative value
     */
    public CurationPipeline withMaxBondsOfSpecificBondOrderFilter(IBond.Order aBondOrder,
                                                                  int aMaxSpecificBondCount,
                                                                  boolean aConsiderImplicitHydrogens,
                                                                  boolean aConsiderPseudoAtoms)
            throws IllegalArgumentException {
        if (aMaxSpecificBondCount < 0) {
            throw new IllegalArgumentException("aMaxSpecificBondCount (integer value) was below zero.");
        }
        IFilter tmpFilter = new MaxBondsOfSpecificBondOrderFilter(aBondOrder, aMaxSpecificBondCount,
                aConsiderImplicitHydrogens, aConsiderPseudoAtoms, this.getReporter());
        this.addToListOfProcessingSteps(tmpFilter);
        return this;
    }

    /**
     * Adds a min bonds of specific bond order filter with the given parameters to the curation pipeline. Bonds to
     * implicit hydrogen atoms may or may not be considered when counting bonds of bond order single. If the second
     * boolean parameter is false, instances of {@link IPseudoAtom} and their implicit hydrogen atoms are not taken into
     * account. Atom containers that equal the given min specific bond count do not get filtered.
     *
     * @param aBondOrder bond order of bonds to count and filter on
     * @param aMinSpecificBondCount  integer value of the min specific bond count to filter by
     * @param aConsiderImplicitHydrogens boolean value whether to consider bonds to implicit hydrogen atoms; this is
     *                                   only relevant when counting bonds of the order one / single
     * @param aConsiderPseudoAtoms boolean value whether to consider bonds to pseudo-atoms and their implicit hydrogens
     * @return the CurationPipeline instance itself
     * @throws IllegalArgumentException if the given min specific bond count has a negative value
     */
    public CurationPipeline withMinBondsOfSpecificBondOrderFilter(IBond.Order aBondOrder,
                                                                  int aMinSpecificBondCount,
                                                                  boolean aConsiderImplicitHydrogens,
                                                                  boolean aConsiderPseudoAtoms)
            throws IllegalArgumentException {
        if (aMinSpecificBondCount < 0) {
            throw new IllegalArgumentException("aMinSpecificBondCount (integer value) was below zero.");
        }
        IFilter tmpFilter = new MinBondsOfSpecificBondOrderFilter(aBondOrder, aMinSpecificBondCount,
                aConsiderImplicitHydrogens, aConsiderPseudoAtoms, this.getReporter());
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

    /**
     * Adds a contains pseudo-atoms filter to the curation pipeline.
     *
     * @return the CurationPipeline instance itself
     * @see ContainsPseudoAtomsFilter
     */
    public CurationPipeline withContainsPseudoAtomsFilter() {
        IFilter tmpFilter = new ContainsPseudoAtomsFilter(this.getReporter());
        this.addToListOfProcessingSteps(tmpFilter);
        return this;
    }

    /**
     * Adds a contains no pseudo-atoms filter to the curation pipeline.
     *
     * @return the CurationPipeline instance itself
     * @see ContainsNoPseudoAtomsFilter
     */
    public CurationPipeline withContainsNoPseudoAtomsFilter() {
        IFilter tmpFilter = new ContainsNoPseudoAtomsFilter(this.getReporter());
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
