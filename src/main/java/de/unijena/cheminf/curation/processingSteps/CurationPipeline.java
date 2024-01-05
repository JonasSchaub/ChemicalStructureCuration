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
import de.unijena.cheminf.curation.fileReaders.CustomIteratingSDFReader;
import de.unijena.cheminf.curation.processingSteps.filters.ContainsNoPseudoAtomsFilter;
import de.unijena.cheminf.curation.processingSteps.filters.ContainsPseudoAtomsFilter;
import de.unijena.cheminf.curation.processingSteps.filters.HasAllValidAtomicNumbersFilter;
import de.unijena.cheminf.curation.processingSteps.filters.HasAllValidValencesFilter;
import de.unijena.cheminf.curation.processingSteps.filters.HasInvalidAtomicNumbersFilter;
import de.unijena.cheminf.curation.processingSteps.filters.HasInvalidValencesFilter;
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
import de.unijena.cheminf.curation.processingSteps.filters.hasProperty.HasNoExternalIDFilter;
import de.unijena.cheminf.curation.processingSteps.filters.hasProperty.HasPropertyFilter;
import de.unijena.cheminf.curation.processingSteps.filters.hasProperty.HasExternalIDFilter;
import de.unijena.cheminf.curation.processingSteps.filters.hasProperty.NotHasPropertyFilter;
import de.unijena.cheminf.curation.reporter.IReporter;
import de.unijena.cheminf.curation.reporter.MarkDownReporter;
import de.unijena.cheminf.curation.reporter.ReportDataObject;
import de.unijena.cheminf.curation.valenceHandling.valenceModels.IValenceModel;
import de.unijena.cheminf.curation.valenceHandling.valenceModels.PubChemValenceModel;
import de.unijena.cheminf.curation.valenceHandling.valenceModels.ValenceListBasedValenceModel;
import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IPseudoAtom;
import org.openscience.cdk.io.IChemObjectReader;
import org.openscience.cdk.isomorphism.matchers.QueryAtomContainer;
import org.openscience.cdk.silent.SilentChemObjectBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
 * As all processing steps, the curation pipeline gives the option to use a second, external ID such as name or CAS
 * registry number of the structures. It is then used in the report and complements the "MolID", an automatically
 * assigned identifier that matches the index the atom container has in the processed atom container set. To prevent
 * the original data from modifications, an atom container set might be cloned before the processing. This can be
 * addressed by setting the boolean parameter of the {@link #process} method to true.
 * <br>
 * To check the existence of the external ID or of any other data annotated to the structures via atom container
 * properties, the following steps might be added to the pipeline:
 * <pre>{@code
 * String tmpExternalIDPropertyName = CDKConstants.CASRN; // exemplary
 * String tmpNameOfPropertyToCheck = "DataSource";        // exemplary
 * new CurationPipeline(*aReporter*, tmpExternalIDPropertyName)
 *                 .withExternalIDChecker()
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
     * Constructor; initializes the curation pipeline and sets the reporter and the external ID property name;
     * initializes the list of pipeline steps.
     * <br>
     * The option of the second identifier property might be used if information such as name of the structures or their
     * CAS registry numbers exists. By setting this field, every atom container processed by this curation pipeline is
     * expected to have a property with the respective name. The given information is then included in the report. The
     * reports are generated using the given reporter. To initialize the curation pipeline with a default reporter
     * (instance of {@link MarkDownReporter}), see the other constructors.
     *
     * @param aReporter the reporter to generate the reports with when processing sets of structures
     * @param anExternalIDPropertyName name string of an atom container property containing a second, external
     *                                 identifier (e.g. the name of the structures or CAS Registry Numbers); by
     *                                 specifying this field, every atom container processed by this processing step
     *                                 is expected to have this property
     * @throws NullPointerException if the given IReporter or String instance is null
     * @throws IllegalArgumentException if the given external ID property name is blank or empty
     * @see #CurationPipeline(String, String)
     * @see #CurationPipeline(String)
     * @see #CurationPipeline(IReporter)
     */
    public CurationPipeline(IReporter aReporter, String anExternalIDPropertyName)
            throws NullPointerException, IllegalArgumentException {
        super(aReporter, anExternalIDPropertyName);
        Objects.requireNonNull(anExternalIDPropertyName, "The given external ID property name is null.");
        this.listOfPipelineSteps = new LinkedList<>();
    }

    /**
     * Constructor; initializes the curation pipeline by calling the respective super constructor passing the given
     * reporter and no external ID property name.
     *
     * @param aReporter the reporter to generate the reports with when processing sets of structures
     * @throws NullPointerException if the given IReporter instance is null
     * @see #CurationPipeline(IReporter, String)
     */
    public CurationPipeline(IReporter aReporter) throws NullPointerException {
        super(aReporter, null);
        this.listOfPipelineSteps = new LinkedList<>();
    }

    /**
     * Constructor; initializes the curation pipeline with an instance of the default reporter that will generate
     * reports in markdown-format at the given directory path (see {@link MarkDownReporter}).
     * <br>
     * The option of the second identifier property might be used if information such as name of the structures or their
     * CAS registry numbers exists. By setting this field, every atom container processed by this curation pipeline is
     * expected to have a property with the respective name. The given information is then included in the report. To
     * initialize the curation pipeline with a specific reporter, see the respective constructors.
     *
     * @param aReportFilesDirectoryPath the directory path for the MarkDownReporter to create the report files at
     * @param anExternalIDPropertyName name string of the atom container property containing a second, external
     *                                 identifier (e.g. the name of the structures or CAS Registry Numbers); by
     *                                 specifying this field, every atom container processed by this processing step
     *                                 is expected to have this property
     * @throws NullPointerException if one of the given String instances is null
     * @throws IllegalArgumentException if the given file path is no directory path; if the given property name string
     *                                  is blank or empty
     * @see #CurationPipeline(IReporter, String)
     * @see #CurationPipeline(IReporter)
     * @see #CurationPipeline(String)
     */
    public CurationPipeline(String aReportFilesDirectoryPath, String anExternalIDPropertyName)
            throws NullPointerException, IllegalArgumentException {
        super(aReportFilesDirectoryPath, anExternalIDPropertyName);
        Objects.requireNonNull(anExternalIDPropertyName, "The given external ID property name is null.");
        this.listOfPipelineSteps = new LinkedList<>();
    }

    /**
     * Constructor; initializes the curation pipeline by calling the respective super constructor passing the given
     * directory path and no external ID property name. The pipeline is initialized with an instance of the default
     * reporter that will generate reports in markdown-format at the given directory path (see {@link
     * MarkDownReporter}).
     *
     * @param aReportFilesDirectoryPath the directory path for the MarkDownReporter to create the report files at
     * @throws NullPointerException if the given String containing the directory path is null
     * @throws IllegalArgumentException if the given file path is no directory path
     * @see #CurationPipeline(String, String)
     */
    public CurationPipeline(String aReportFilesDirectoryPath) throws NullPointerException, IllegalArgumentException {
        super(aReportFilesDirectoryPath, null);
        this.listOfPipelineSteps = new LinkedList<>();
    }
    //</editor-fold>

    /**
     * {@inheritDoc}
     * <p>
     * <b>Pipeline specific Info:</b> The given atom container set is sequentially processed by all steps of the
     * pipeline. All steps report to the same reporter (as long as there have been no changes to reporters of the
     * processing steps after them being added to the pipeline).
     * </p>
     */
    @Override
    public IAtomContainerSet process(IAtomContainerSet anAtomContainerSet, boolean aCloneBeforeProcessing) throws Exception {
        return super.process(anAtomContainerSet, aCloneBeforeProcessing);
    }

    /**
     * TODO
     *
     * @param aFilePath the path of the file to import a set of structures from (currently only supports SD files)
     * @return the imported and processed atom container set
     * @throws NullPointerException if the given file path is null
     * @throws IllegalArgumentException if the given file path is blank or empty
     * @throws FileNotFoundException if the file does not exist, is a directory rather than a regular file, or for some
     *                               other reason cannot be opened for reading
     * @throws SecurityException if a security manager exists and its checkRead method denies read access to the file
     * @throws IOException if the import process fails due to ... TODO
     * @throws Exception if an unexpected, fatal exception occurs
     */
    public IAtomContainerSet importAndProcess(String aFilePath) throws NullPointerException, IllegalArgumentException,
            IOException, Exception {
        Objects.requireNonNull(aFilePath, "aFilePath (instance of String) is null.");
        if (aFilePath.isBlank()) {
            throw new IllegalArgumentException("aFilePath (instance of String) is empty or blank.");
        }
        File tmpFile = new File(aFilePath);
        return this.importAndProcess(tmpFile);
    }

    /**
     * TODO
     *
     * @param aFileToImport the file to import a set of structures from (currently only supports SD files)
     * @return the imported and processed atom container set
     * @throws NullPointerException if the given file is null
     * @throws FileNotFoundException if the file does not exist, is a directory rather than a regular file, or for some
     *                               other reason cannot be opened for reading
     * @throws SecurityException if a security manager exists and its checkRead method denies read access to the file
     * @throws IOException if the import process fails due to ... TODO
     * @throws Exception if an unexpected, fatal exception occurs
     */
    public IAtomContainerSet importAndProcess(File aFileToImport) throws FileNotFoundException, IOException, Exception {
        Objects.requireNonNull(aFileToImport, "aFileToImport (instance of File) is null.");
        //
        // initialize the report
        this.getReporter().initializeNewReport();
        //
        final IAtomContainerSet tmpImportedMoleculeSet = new AtomContainerSet();
        //<editor-fold desc="import process" defaultstate="collapsed">
        final ImportRoutines tmpImportRoutine = ImportRoutines.SDF_IMPORT;
        CustomIteratingSDFReader tmpSDFReader = new CustomIteratingSDFReader(new FileInputStream(aFileToImport),
                SilentChemObjectBuilder.getInstance(), false);
        tmpSDFReader.setReaderMode(IChemObjectReader.Mode.RELAXED);
        int tmpCounter = 0; //TODO: remove (?!)
        int tmpFailedStructureImportsCounter = 0; //TODO: remove (?!)
        int tmpQueryAtomContainersCount = 0; //TODO: remove
        // continue until the thread is interrupted or the end of the file is reached
        while(!Thread.currentThread().isInterrupted() && tmpSDFReader.hasNext()) {  //TODO: remove listening to thread interruption?
            try {
                // load the structure and give it its position in the imported data set as MolID
                IAtomContainer tmpNextMolecule = tmpSDFReader.next();
                if (tmpNextMolecule == null) {
                    //TODO: log?
                    CurationPipeline.LOGGER.warning(String.format("Structure %d (index), line %dff, failed to be" +
                            " imported.", tmpSDFReader.getMoleculesInFileCounter(),
                            tmpSDFReader.getLineCountAtBeginOfNext()));
                    //TODO: throw exception?
                    throw new Exception(ErrorCodes.SDF_IMPORT_FAILED_ERROR.name());
                }
                if (tmpNextMolecule instanceof QueryAtomContainer) {   //TODO: remove
                    System.out.printf("Structure %d (index), line %dff, was imported as QueryAtomContainer" +
                            " instance.\n", tmpSDFReader.getMoleculesInFileCounter(),
                            tmpSDFReader.getLineCountAtBeginOfNext());
                    System.out.println("\t" + tmpNextMolecule.getProperty("ChEBI ID"));
                    System.out.println("\t" + tmpNextMolecule.getProperty("ChEBI Name"));
                    tmpQueryAtomContainersCount++;
                }
                /* setting the index of the structure in combination with the line number the entry begins at in the
                 * file as MolID */
                String tmpMolID = String.format("%d (line %dff)", (tmpSDFReader.getMoleculesInFileCounter() - 1),
                        tmpSDFReader.getLineCountAtBeginOfNext());
                tmpNextMolecule.setProperty(IProcessingStep.MOL_ID_PROPERTY_NAME, tmpMolID);
                tmpImportedMoleculeSet.addAtomContainer(tmpNextMolecule);
            } catch (Exception anException) {
                // import process of structure failed
                // report the issue to the reporter
                String tmpExceptionMessageString = anException.getMessage();
                ErrorCodes tmpErrorCode = null;
                try {
                    // the message of the exception is expected to match the name of an ErrorCodes enum's constant
                    tmpErrorCode = ErrorCodes.valueOf(tmpExceptionMessageString);
                } catch (Exception aFatalException) {
                    /* the message string of the given exception did not match the name of an ErrorCodes enum's
                     * constant; the exception is considered as fatal and re-thrown */
                    tmpErrorCode = ErrorCodes.UNEXPECTED_EXCEPTION_ERROR;
                    throw anException;
                } finally {
                    this.appendToReport(tmpErrorCode, String.format("%d (line %dff)",
                            (tmpSDFReader.getMoleculesInFileCounter() - 1),
                            tmpSDFReader.getLineCountAtBeginOfNext()), tmpImportRoutine);
                    tmpFailedStructureImportsCounter++;
                }
            }
            tmpCounter++;
        }
        // TODO: probably remove the following lines of code or use the ILoggingTool of the CDK and put this as "debug"
        System.out.println("Check: Structures in file count is correct: " + (tmpCounter == tmpSDFReader.getMoleculesInFileCounter()));
        System.out.println("Check: Structures failing import count is correct: " + (tmpFailedStructureImportsCounter == tmpSDFReader.getNullMoleculesCounter()));
        CurationPipeline.LOGGER.info("Structures in file count: " + tmpSDFReader.getMoleculesInFileCounter());
        CurationPipeline.LOGGER.info("Successfully imported structures: " + (tmpSDFReader.getMoleculesInFileCounter() - tmpFailedStructureImportsCounter));
        if (tmpFailedStructureImportsCounter > 0) {
            CurationPipeline.LOGGER.severe("Structures failing the import process: " + tmpFailedStructureImportsCounter);
        }
        System.out.println("Detected QueryAtomContainer Count: " + tmpQueryAtomContainersCount);
        //</editor-fold>
        //
        // the following code is an adaption of the core part of the .process() method of the BaseProcessingStep class
        IAtomContainerSet tmpProcessedAtomContainerSet;
        //<editor-fold desc="process and handle fatal exceptions" defaultstate="collapsed">
        try {
            // do the processing
            tmpProcessedAtomContainerSet = this.applyLogic(tmpImportedMoleculeSet);
        } catch (Exception aFatalException) {
            // the exception is considered as fatal
            CurationPipeline.LOGGER.severe("The processing was interrupted due to an unexpected, fatal" +
                    " exception.");
            try {
                // try to finish the report via respective method
                this.getReporter().reportAfterFatalException();
            } catch (Exception anException) {
                //TODO: use ILoggingTool debug mode to log the exception? (see BaseProcessingStep)
                CurationPipeline.LOGGER.log(Level.WARNING, anException.toString(), anException);
                CurationPipeline.LOGGER.warning("The report could not be generated / finished.");
            }
            throw aFatalException;
        }
        //</editor-fold>
        //
        // generate / finish the report
        this.getReporter().report();
        //
        return tmpProcessedAtomContainerSet;
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
        if (this.isIsReporterSelfContained()) {
            System.out.printf("Processing started with %d structures.\n", anAtomContainerSet.getAtomContainerCount());  //TODO: remove
        }
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
                CurationPipeline.LOGGER.severe(String.format("The processing step with identifier %s and of class %s" +
                        " was interrupted by an unexpected exception.", tmpProcessingStep.getPipelineProcessingStepID(),
                        tmpProcessingStep.getClass().getName()));
                throw aFatalException;
            }
            System.out.printf("Step %s ended with %d structures remaining.\n",
                    tmpProcessingStep.getPipelineProcessingStepID(), tmpResultingACSet.getAtomContainerCount()); //TODO: remove
        }
        return tmpResultingACSet;
    }

    /**
     * Generates a report data object on the basis of the given error code, MolID and info respective to the processing
     * step and passes it to the reporter. This method may only be used if there is no info on the structure but its
     * MolID as it is the case at failed import attempts; see {@link CurationPipeline#importAndProcess(String)}).
     *
     * @param anErrorCode     error code of the reported issue
     * @param aMolID          the MolID of the structure that failed the import
     * @param anImportRoutine the import routine the structure failed
     * @throws NullPointerException if the given error code, identifier or import routine is null
     * @throws IllegalArgumentException if the identifier string is empty or blank
     */
    protected void appendToReport(ErrorCodes anErrorCode, String aMolID, ImportRoutines anImportRoutine)
            throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(anErrorCode, "anErrorCode (ErrorCodes constant) is null.");
        Objects.requireNonNull(aMolID, "aMolID (instance of String) is null.");
        Objects.requireNonNull(anImportRoutine, "anImportRoutine (ImportRoutines constant) is null.");
        if (aMolID.isBlank()) {
            throw new IllegalArgumentException("aMolID (instance of String) is empty or blank.");
        }
        ReportDataObject tmpReportDataObject = new ReportDataObject(anErrorCode, this.getClass(),
                anImportRoutine.getIdentifier(), aMolID);
        this.getReporter().appendReport(tmpReportDataObject);
    }

    //<editor-fold desc="with...Filter methods" defaultstate="collapsed">
    //<editor-fold desc="withMaxAtomCountFilter" defaultstate="collapsed">
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
     * @see MaxAtomCountFilter
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
    //</editor-fold>

    //<editor-fold desc="withMinAtomCountFilter" defaultstate="collapsed">
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
     * @see MinAtomCountFilter
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
    //</editor-fold>

    //<editor-fold desc="withMaxHeavyAtomCountFilter" defaultstate="collapsed">
    /**
     * Adds a max heavy atom count filter with the given max heavy atom count to the curation pipeline. Atom containers
     * that equal the given max heavy atom count do not get filtered.
     *
     * @param aMaxHeavyAtomCount integer value of the max atom count to filter by
     * @param aConsiderPseudoAtoms boolean value whether to consider pseudo-atoms in the heavy atoms count
     * @return the CurationPipeline instance itself
     * @throws IllegalArgumentException if the given max heavy atom count has a negative value
     * @see MaxHeavyAtomCountFilter
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
    //</editor-fold>

    //<editor-fold desc="withMinHeavyAtomCountFilter" defaultstate="collapsed">
    /**
     * Adds a min heavy atom count filter with the given min heavy atom count to the curation pipeline. Atom containers
     * that equal the given min heavy atom count do not get filtered.
     *
     * @param aMinHeavyAtomCount integer value of the min atom count to filter by
     * @param aConsiderPseudoAtoms boolean value whether to consider pseudo-atoms in the heavy atoms count
     * @return the CurationPipeline instance itself
     * @throws IllegalArgumentException if the given min heavy atom count has a negative value
     * @see MinHeavyAtomCountFilter
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
    //</editor-fold>

    //<editor-fold desc="withMaxBondCountFilter" defaultstate="collapsed">
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
     * @see MaxBondCountFilter
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
    //</editor-fold>

    //<editor-fold desc="withMinBondCountFilter" defaultstate="collapsed">
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
     * @see MinBondCountFilter
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
    //</editor-fold>

    //<editor-fold desc="withMaxBondsOfSpecificBondOrderFilter" defaultstate="collapsed">
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
     * @see MaxBondsOfSpecificBondOrderFilter
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
    //</editor-fold>

    //<editor-fold desc="withMinBondsOfSpecificBondOrderFilter" defaultstate="collapsed">
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
     * @see MinBondsOfSpecificBondOrderFilter
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
    //</editor-fold>

    //<editor-fold desc="withHasAllValidAtomicNumbersFilter" defaultstate="collapsed">
    /**
     * Adds a has all valid atomic numbers filter with the given boolean parameter to the curation pipeline.
     *
     * @param aWildcardAtomicNumberIsValid boolean value whether the wildcard atomic number zero should be considered
     *                                     as a valid atomic number
     * @return the CurationPipeline instance itself
     * @see HasAllValidAtomicNumbersFilter
     */
    public CurationPipeline withHasAllValidAtomicNumbersFilter(boolean aWildcardAtomicNumberIsValid) {
        IFilter tmpFilter = new HasAllValidAtomicNumbersFilter(aWildcardAtomicNumberIsValid, this.getReporter());
        this.addToListOfProcessingSteps(tmpFilter);
        return this;
    }
    //</editor-fold>

    //<editor-fold desc="withHasInvalidAtomicNumbersFilter" defaultstate="collapsed">
    /**
     * Adds a has invalid atomic numbers filter with the given boolean parameter to the curation pipeline.
     *
     * @param aWildcardAtomicNumberIsValid boolean value whether the wildcard atomic number zero should be considered
     *                                     as a valid atomic number
     * @return the CurationPipeline instance itself
     * @see HasInvalidAtomicNumbersFilter
     */
    public CurationPipeline withHasInvalidAtomicNumbersFilter(boolean aWildcardAtomicNumberIsValid) {
        IFilter tmpFilter = new HasInvalidAtomicNumbersFilter(aWildcardAtomicNumberIsValid, this.getReporter());
        this.addToListOfProcessingSteps(tmpFilter);
        return this;
    }
    //</editor-fold>

    //<editor-fold desc="withMaxMolecularMassFilter" defaultstate="collapsed">
    /**
     * Adds a max molecular mass filter with the given parameters to the curation pipeline. The given mass computation
     * flavour switches the computation type of the mass calculation. Filters consider threshold values to be inclusive.
     *
     * @param aMaxMolecularMass double value of the max molecular mass value to filter by
     * @param aMassComputationFlavour MassComputationFlavours constant that switches the computation type of the mass
     *                                calculation
     * @return the CurationPipeline instance itself
     * @throws NullPointerException if the given mass computation flavour is null
     * @throws IllegalArgumentException if the given max molecular mass is of a negative value
     * @see MaxMolecularMassFilter
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
     * #MolWeight} is used. Filters consider threshold values to be inclusive.
     *
     * @param aMaxMolecularMass double value of the max molecular mass value to filter by
     * @return the CurationPipeline instance itself
     * @throws IllegalArgumentException if the given max molecular mass is of a negative value
     * @see MaxMolecularMassFilter
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
    //</editor-fold>

    //<editor-fold desc="withMinMolecularMassFilter" defaultstate="collapsed">
    /**
     * Adds a min molecular mass filter with the given parameters to the curation pipeline. The given mass computation
     * flavour switches the computation type of the mass calculation. Filters consider threshold values to be inclusive.
     *
     * @param aMinMolecularMass double value of the min molecular mass value to filter by
     * @param aMassComputationFlavour MassComputationFlavours constant that switches the computation type of the mass
     *                                calculation
     * @return the CurationPipeline instance itself
     * @throws NullPointerException if the given mass computation flavour is null
     * @throws IllegalArgumentException if the given min molecular mass is of a negative value
     * @see MinMolecularMassFilter
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
     * #MolWeight} is used. Filters consider threshold values to be inclusive.
     *
     * @param aMinMolecularMass double value of the min molecular mass value to filter by
     * @return the CurationPipeline instance itself
     * @throws IllegalArgumentException if the given min molecular mass is of a negative value
     * @see MinMolecularMassFilter
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

    //<editor-fold desc="withContainsPseudoAtomsFilter" defaultstate="collapsed">
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
    //</editor-fold>

    //<editor-fold desc="withContainsNoPseudoAtomsFilter" defaultstate="collapsed">
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

    //<editor-fold desc="withHasAllValidValencesFilter" defaultstate="collapsed">
    /**
     * Adds a {@link HasAllValidValencesFilter} as step to the curation pipeline. The filter is initialized with the
     * given valence model and boolean value whether to generally consider atoms with wildcard atomic number (zero) as
     * having a valid valence.
     *
     * @param aValenceModel                the valence model to check the valences for their validity with
     * @param aWildcardAtomicNumberIsValid boolean value whether to generally consider atoms with wildcard atomic number
     *                                     (zero) as having a valid valence
     * @return the CurationPipeline instance itself
     * @throws NullPointerException if the given valence model is null
     * @see HasAllValidValencesFilter
     * @see PubChemValenceModel
     * @see ValenceListBasedValenceModel
     */
    public CurationPipeline withHasAllValidValencesFilter(IValenceModel aValenceModel,
                                                          boolean aWildcardAtomicNumberIsValid)
            throws NullPointerException {
        IFilter tmpFilter = new HasAllValidValencesFilter(aValenceModel, aWildcardAtomicNumberIsValid, this.getReporter());
        this.addToListOfProcessingSteps(tmpFilter);
        return this;
    }

    /**
     * Adds a {@link HasAllValidValencesFilter} as step to the curation pipeline initializing it with an instance of
     * {@link PubChemValenceModel} as valence model.
     *
     * @param aWildcardAtomicNumberIsValid boolean value whether to generally consider atoms with wildcard atomic number
     *                                     (zero) as having a valid valence
     * @return the CurationPipeline instance itself
     * @see HasAllValidValencesFilter
     * @see PubChemValenceModel
     */
    public CurationPipeline withHasAllValidValencesFilter(boolean aWildcardAtomicNumberIsValid) {
        IFilter tmpFilter = new HasAllValidValencesFilter(aWildcardAtomicNumberIsValid, this.getReporter());
        this.addToListOfProcessingSteps(tmpFilter);
        return this;
    }
    //</editor-fold>

    //<editor-fold desc="withHasInvalidValencesFilter" defaultstate="collapsed">
    /**
     * Adds a {@link HasInvalidValencesFilter} as step to the curation pipeline. The filter is initialized with the
     * given valence model and boolean value whether to generally consider atoms with wildcard atomic number (zero) as
     * having a valid valence.
     *
     * @param aValenceModel                the valence model to check the valences for their validity with
     * @param aWildcardAtomicNumberIsValid boolean value whether to generally consider atoms with wildcard atomic number
     *                                     (zero) as having a valid valence
     * @return the CurationPipeline instance itself
     * @throws NullPointerException if the given valence model is null
     * @see HasInvalidValencesFilter
     * @see PubChemValenceModel
     * @see ValenceListBasedValenceModel
     */
    public CurationPipeline withHasInvalidValencesFilter(IValenceModel aValenceModel,
                                                         boolean aWildcardAtomicNumberIsValid)
            throws NullPointerException {
        IFilter tmpFilter = new HasInvalidValencesFilter(aValenceModel, aWildcardAtomicNumberIsValid, this.getReporter());
        this.addToListOfProcessingSteps(tmpFilter);
        return this;
    }

    /**
     * Adds a {@link HasInvalidValencesFilter} as step to the curation pipeline initializing it with an instance of
     * {@link PubChemValenceModel} as valence model.
     *
     * @param aWildcardAtomicNumberIsValid boolean value whether to generally consider atoms with wildcard atomic number
     *                                     (zero) as having a valid valence
     * @return the CurationPipeline instance itself
     * @see HasInvalidValencesFilter
     * @see PubChemValenceModel
     */
    public CurationPipeline withHasInvalidValencesFilter(boolean aWildcardAtomicNumberIsValid) {
        IFilter tmpFilter = new HasInvalidValencesFilter(aWildcardAtomicNumberIsValid, this.getReporter());
        this.addToListOfProcessingSteps(tmpFilter);
        return this;
    }
    //</editor-fold>

    //<editor-fold desc="withHasPropertyFilter" defaultstate="collapsed">
    /**
     * Adds a {@link HasPropertyFilter} as step to the curation pipeline. The filter is initialized with the given
     * property name and removes all atom containers from a set that do not possess that specific property.
     * <br>
     * <b>Note:</b> This option might be used to ensure a coherent annotation of data sets.
     *
     * @param aNameOfProperty the name of the atom container property to check for
     * @return the CurationPipeline instance itself
     * @throws NullPointerException     if the given property name string is null
     * @throws IllegalArgumentException if the property name string is blank or empty
     * @see HasPropertyFilter
     * @see #withNotHasPropertyFilter(String)
     */
    public CurationPipeline withHasPropertyFilter(String aNameOfProperty) throws NullPointerException,
            IllegalArgumentException {
        HasPropertyFilter tmpHasPropertyFilter = new HasPropertyFilter(aNameOfProperty, this.getReporter());
        this.addToListOfProcessingSteps(tmpHasPropertyFilter);
        return this;
    }
    //</editor-fold>

    //<editor-fold desc="withNotHasPropertyFilter" defaultstate="collapsed">
    /**
     * Adds a {@link NotHasPropertyFilter} as step to the curation pipeline. The filter is initialized with the given
     * property name and removes all atom containers from a set that possess that specific property.
     * <br>
     * <b>Note:</b> This option might be used to ensure a coherent annotation of data sets.
     *
     * @param aNameOfProperty the name of the atom container property to check for
     * @return the CurationPipeline instance itself
     * @throws NullPointerException     if the given property name string is null
     * @throws IllegalArgumentException if the property name string is blank or empty
     * @see NotHasPropertyFilter
     * @see #withHasPropertyFilter(String)
     */
    public CurationPipeline withNotHasPropertyFilter(String aNameOfProperty) throws NullPointerException,
            IllegalArgumentException {
        NotHasPropertyFilter tmpNotHasPropertyFilter = new NotHasPropertyFilter(aNameOfProperty, this.getReporter());
        this.addToListOfProcessingSteps(tmpNotHasPropertyFilter);
        return this;
    }
    //</editor-fold>

    //<editor-fold desc="withHasExternalIDFilter" defaultstate="collapsed">
    /**
     * Adds a {@link HasExternalIDFilter} as step to the curation pipeline. The filter removes all atom containers from
     * a set that do not possess a property with the external ID property name that has been given to this pipeline
     * (see {@link #getExternalIDPropertyName()}).
     * <br>
     * <b>Note:</b> If the pipeline has been given an external ID property name, it is advised to add this processing
     * step as the initial step to the pipeline to remove atom containers with missing external ID from the processed
     * atom container set and ensure a coherent annotation of the curated data.
     *
     * @return the CurationPipeline instance itself
     * @throws NullPointerException if the external ID property name field of the pipeline has not been specified (via
     *                              constructor or respective setter)
     * @see HasExternalIDFilter
     * @see #setExternalIDPropertyName(String)
     * @see #withHasNoExternalIDFilter()
     */
    public CurationPipeline withHasExternalIDFilter() throws NullPointerException {
        HasPropertyFilter tmpHasExternalIDFilter = new HasExternalIDFilter(this.getExternalIDPropertyName(),
                this.getReporter());
        this.addToListOfProcessingSteps(tmpHasExternalIDFilter);
        return this;
    }
    //</editor-fold>

    //<editor-fold desc="withHasNoExternalIDFilter" defaultstate="collapsed">
    /**
     * Adds a {@link HasNoExternalIDFilter} as step to the curation pipeline. The filter removes all atom containers
     * from a set that possess a property with the external ID property name that has been given to this pipeline
     * (see {@link #getExternalIDPropertyName()}).
     * <br>
     * <b>Note:</b> If the pipeline has been given an external ID property name, this processing step might be added to
     * receive those atom containers that do not possess this external ID.
     *
     * @return the CurationPipeline instance itself
     * @throws NullPointerException if the external ID property name field of the pipeline has not been specified (via
     *                              constructor or respective setter)
     * @see HasNoExternalIDFilter
     * @see #setExternalIDPropertyName(String)
     * @see #withHasExternalIDFilter()
     */
    public CurationPipeline withHasNoExternalIDFilter() throws NullPointerException {
        HasNoExternalIDFilter tmpHasNoExternalIDFilter = new HasNoExternalIDFilter(this.getExternalIDPropertyName(),
                this.getReporter());
        this.addToListOfProcessingSteps(tmpHasNoExternalIDFilter);
        return this;
    }
    //</editor-fold>
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
     * Clears the pipeline by emptying the list of pipeline steps. All processing steps added to the pipeline are
     * removed.
     *
     * @see #getListOfPipelineSteps()
     */
    public void clear() {
        this.listOfPipelineSteps.clear();
    }

    /**
     * Adds the given processing step to the list of processing steps and sets its fields externalIDPropertyName and
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
        aProcessingStep.setExternalIDPropertyName(this.getExternalIDPropertyName());
        aProcessingStep.setReporter(this.getReporter());    //TODO: remove this? or just keep it to make sure?
        aProcessingStep.setIsReporterSelfContained(false);
        String tmpStepID = ((this.getPipelineProcessingStepID() == null) ?
                "" : this.getPipelineProcessingStepID() + ".") +
                (this.listOfPipelineSteps.size() - 1);
        aProcessingStep.setPipelineProcessingStepID(
                ((this.getPipelineProcessingStepID() == null) ?
                        "" : this.getPipelineProcessingStepID() + ".") +
                        (this.listOfPipelineSteps.size() - 1)
        );
    }

    //<editor-fold desc="public properties" defaultstate="collapsed">
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
     * This external identifier property name is also set to every processing step that is part of the pipeline.
     */
    @Override
    public void setExternalIDPropertyName(String anExternalIDPropertyName) throws IllegalArgumentException {
        super.setExternalIDPropertyName(anExternalIDPropertyName);
        this.listOfPipelineSteps.forEach(aProcessingStep -> {
            aProcessingStep.setExternalIDPropertyName(anExternalIDPropertyName);
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
    //</editor-fold>

    //<editor-fold desc="ImportRoutines enum" defaultstate="collapsed">
    /** TODO: check if it is necessary
     * Enum containing an entry for every available import routine. Every entry has an associated identifier string.
     *
     * @author Samuel Behr
     * @version 1.0.0.0
     * @see #importAndProcess(String)
     */
    public enum ImportRoutines {

        /**
         * SD file import routine.
         */
        SDF_IMPORT("SDFileImporterRoutine");

        /**
         * The identifier string of the import routine.
         */
        private final String identifier;

        /**
         * Internal constructor.
         *
         * @param anIdentifier the identifier string of the import routine
         * @throws NullPointerException if the identifier is null
         * @throws IllegalArgumentException if the identifier string is empty or blank
         */
        ImportRoutines(String anIdentifier) throws NullPointerException, IllegalArgumentException {
            Objects.requireNonNull(anIdentifier, "anIdentifier (instance of String) is null.");
            if (anIdentifier.isBlank()) {
                throw new IllegalArgumentException("anIdentifier (instance of String) is empty or blank.");
            }
            this.identifier = anIdentifier;
        }

        /**
         * Returns the identifier string of the import routine.
         *
         * @return String instance
         */
        public String getIdentifier() {
            return this.identifier;
        }

    }
    //</editor-fold>

}
