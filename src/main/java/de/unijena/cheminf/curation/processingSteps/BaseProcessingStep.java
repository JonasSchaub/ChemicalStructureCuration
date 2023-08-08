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
import de.unijena.cheminf.curation.reporter.IReporter;
import de.unijena.cheminf.curation.reporter.MarkDownReporter;
import de.unijena.cheminf.curation.reporter.ReportDataObject;
import de.unijena.cheminf.curation.utils.ProcessingStepUtils;
import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Base class of all processing steps. Reduces the number of abstract methods to the protected method {@link
 * #applyLogic( IAtomContainerSet)}. In addition to overwriting this one method, processing steps extending this base
 * class possibly need constructors initializing the fields specific to the processing step and respective getters.
 * <br>
 * Essentially, this abstract class works as a wrapper class for methods that take, process and return a set of atom
 * containers, and thereby may be used to easily create workers that can be used as part of a pipeline (see {@link
 * CurationPipeline}).
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public abstract class BaseProcessingStep implements IProcessingStep {

    /*
    TODO: additional constructors?
        - at all?
        - here in the constructor or only at the level of the classes children?
    //
    TODO:
        - initialize reporter
        - make the destination of the report-file adjustable without the need to set a new reporter?
     */

    /**
     * Logger of this class.
     */
    private static final Logger LOGGER = Logger.getLogger(BaseProcessingStep.class.getName());

    /**
     * Name string of the atom container property structures processed by this processing step are expected to store a
     * second, external identifier in. The respective atom container property might contain information such as name or
     * CAS registry number of the structures. If this field is null, no second identifier is used. Otherwise, every atom
     * container processed by this processing step is expected to have a property with the respective name. A
     * placeholder is used for structures that leak this property.
     */
    private String externalIDPropertyName;

    /**
     * Identifier string of the processing step in the pipeline (if it is part of a pipeline), or null if it is not
     * part of a pipeline (default). It should equal the index of the processing step in the respective pipeline and
     * is stored as string to enable subordinate IDs (e.g. "1.1").
     */
    private String pipelineProcessingStepID = null;

    /**
     * Reporter of this processing step. The field may never be null.
     */
    private IReporter reporter;

    /**
     * Boolean value whether the reporter of this instance is self-contained and does not belong to a superordinate
     * entity like a pipeline ({@link CurationPipeline}); if false, calls of the {@link IReporter#report()} method are
     * suppressed.
     */
    private boolean isReporterSelfContained = true;

    //<editor-fold desc="Constructors" defaultstate="collapsed">
    /**
     * Constructor; initializes the fields {@link #reporter} and {@link #externalIDPropertyName}. Since not every
     * processing step might give the option to specify the external ID property name in the constructor, this parameter
     * is allowed to be null.
     *
     * @param aReporter the reporter that is to be used when processing sets of structures
     * @param anExternalIDPropertyName name string of the atom container property containing a second, external
     *                                 identifier (e.g. the name or CAS registry number) for each structure; if this
     *                                 field gets specified (not null), every atom container processed by this
     *                                 processing step is expected to have a property with the respective name; the info
     *                                 is then included in the report
     * @throws NullPointerException if the given IReporter instance is null
     * @throws IllegalArgumentException if an external ID property name is given, but it is blank or empty
     */
    public BaseProcessingStep(IReporter aReporter, String anExternalIDPropertyName) throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(aReporter, "aReporter (instance of IReporter) is null.");
        if (anExternalIDPropertyName != null && anExternalIDPropertyName.isBlank()) {
            throw new IllegalArgumentException("anExternalIDPropertyName (instance of String) is empty or blank.");
        }
        this.reporter = aReporter;
        this.externalIDPropertyName = anExternalIDPropertyName;
    }

    /**
     * Constructor; calls the main constructor with an instance of {@link MarkDownReporter} - initialized with the given
     * report files directory path - as default reporter. Since not every processing step might give the option to
     * specify the external ID property name in the constructor, this parameter is allowed to be null.
     *
     * @param aReportFilesDirectoryPath the directory path for the MarkDownReporter to create the report files at
     * @param anExternalIDPropertyName  name string of the atom container property containing a second, external
     *                                  identifier (e.g. the name or CAS registry number) for each structure; if this
     *                                  field gets specified (not null), every atom container processed by this
     *                                  processing step is expected to have a property with the respective name; the
     *                                  info is then included in the report
     * @throws NullPointerException if the given String with the directory path is null
     * @throws IllegalArgumentException if the given file path is no directory path; if a property name string is given,
     *                                  but it is blank or empty
     */
    public BaseProcessingStep(String aReportFilesDirectoryPath, String anExternalIDPropertyName)
            throws NullPointerException, IllegalArgumentException {
        this(new MarkDownReporter(aReportFilesDirectoryPath), anExternalIDPropertyName);
    }
    //</editor-fold>

    @Override
    public IAtomContainerSet process(IAtomContainerSet anAtomContainerSet, boolean aCloneBeforeProcessing)
            throws NullPointerException, Exception {
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of IAtomContainerSet) is null.");
        //
        if (this.isReporterSelfContained) {
            // initialize the report and assign MolIDs to the given structures
            this.reporter.initializeNewReport();
            ProcessingStepUtils.assignMolIdToAtomContainers(anAtomContainerSet);
        }
        //
        // clone the given atom container set, if so desired
        IAtomContainerSet tmpACSetToProcess;
        if (aCloneBeforeProcessing) {
            tmpACSetToProcess = this.cloneAtomContainerSet(anAtomContainerSet);
        } else {
            tmpACSetToProcess = anAtomContainerSet;
        }
        //
        // apply the logic of the processing step on the atom container set
        IAtomContainerSet tmpResultingACSet;
        try {
            tmpResultingACSet = this.applyLogic(tmpACSetToProcess);
        } catch (Exception aFatalException) {
            // the exception is considered as fatal and can not be handled
            BaseProcessingStep.LOGGER.severe("The processing was interrupted due to an unexpected, fatal" +
                    " exception.");
            if (this.isReporterSelfContained) {
                try {
                    // try to finish the report via respective method
                    this.reporter.reportAfterFatalException();
                } catch (IOException anIOException) {
                    BaseProcessingStep.LOGGER.warning("The report could not be generated / finished due to an" +
                            " IOException.");
                } catch (Exception anException) {
                    BaseProcessingStep.LOGGER.warning("The report could not be generated / finished.");
                }
            }
            throw aFatalException;
        }
        //
        // generate / finish the report if the reporter is self-contained by this processing step
        if (this.isReporterSelfContained()) {
            this.reporter.report();
        }
        //
        return tmpResultingACSet;
    }

    /**
     * Processes the given atom container set according to the logic of the processing step. Does not clone the given
     * data; expects all atom containers to have a MolID (atom container property with the name {@link
     * #MOL_ID_PROPERTY_NAME}).
     *
     * @param anAtomContainerSet atom container set to process
     * @return the processed atom container set
     * @throws NullPointerException if the given IAtomContainerSet instance is null or an atom container of the set
     * does not possess a MolID (this will only cause an exception, if the processing of the atom container causes an
     * issue)
     * @throws Exception if an unexpected, fatal exception occurred
     */
    protected abstract IAtomContainerSet applyLogic(IAtomContainerSet anAtomContainerSet)
            throws NullPointerException, Exception;

    /**
     * Clones the given atom container set and reports issues with the cloning of individual atom containers to the
     * reporter (therefore it is not part of an utils class so far).
     *
     * @param anAtomContainerSet atom container set to be cloned
     * @return a clone of the given atom container set
     * @throws NullPointerException if the given IAtomContainerSet instance is null
     * @throws Exception if an encountered issue could not be appended to the reporter
     */
    private IAtomContainerSet cloneAtomContainerSet(IAtomContainerSet anAtomContainerSet) throws NullPointerException,
            Exception {
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of IAtomContainerSet) is null.");
        IAtomContainerSet tmpCloneOfGivenACSet = new AtomContainerSet();
        for (IAtomContainer tmpAtomContainer : anAtomContainerSet.atomContainers()) {
            try {
                tmpCloneOfGivenACSet.addAtomContainer(tmpAtomContainer.clone());
            } catch (CloneNotSupportedException aCloneNotSupportedException) {
                this.appendToReport(ErrorCodes.CLONE_ERROR, tmpAtomContainer);
            }
        }
        return tmpCloneOfGivenACSet;
    }

    /**
     * Generates a report data object on the basis of the given error code and atom container and info respective to the
     * processing step and passes it to the reporter. The given atom container may only be null in combination with
     * specific error codes. If it is not null, it is expected to have a MolID (atom container property with name {@link
     * #MOL_ID_PROPERTY_NAME}) and - if the {@link #externalIDPropertyName} field has been specified - also to have an
     * external ID assigned. If the external ID is null, a placeholder is used instead.
     *
     * @param anErrorCode     error code of the reported issue
     * @param anAtomContainer atom container instance the issue refers to
     * @throws NullPointerException     if the given ErrorCodes constant is null; if an atom container is given, but it
     *                                  possesses no MolID; if the report has not been initialized (dependent on the
     *                                  type of reporter)
     * @throws IllegalArgumentException if the atom container is null but the reported issue is neither {@link
     *                                  ErrorCodes#UNEXPECTED_EXCEPTION_ERROR} nor {@link
     *                                  ErrorCodes#ATOM_CONTAINER_NULL_ERROR}; if an identifier of the structure or the
     *                                  identifier of the processing step is not null but empty or blank
     */
    protected void appendToReport(ErrorCodes anErrorCode, IAtomContainer anAtomContainer) throws NullPointerException,
            IllegalArgumentException {
        Objects.requireNonNull(anErrorCode, "anErrorCode (ErrorCodes constant) is null.");
        byte tmpCase;
        if (anAtomContainer == null) {
            if (this.pipelineProcessingStepID == null) {
                tmpCase = 0;    // case: no atom container; no step ID
            } else {
                tmpCase = 1;    // case: no atom container; with step ID
            }
        } else if (this.externalIDPropertyName == null) {
            if (this.pipelineProcessingStepID == null) {
                tmpCase = 2;    // case: with atom container; no external ID; no step ID
            } else {
                tmpCase = 3;    // case: with atom container; no external ID; with step ID
            }
        } else {
            if (this.pipelineProcessingStepID == null) {
                tmpCase = 4;    // case: with atom container; with external ID; no step ID
            } else {
                tmpCase = 5;    // case: with atom container; with external ID; with step ID
            }
        }
        ReportDataObject tmpReportDataObject = switch (tmpCase) {
            // each constructor of ReportDataObject expects all its respective parameters to be non-null
            case 0 -> new ReportDataObject(anErrorCode, this.getClass());
            case 1 -> new ReportDataObject(anErrorCode, this.getClass(), this.pipelineProcessingStepID);
            case 2 -> new ReportDataObject(anErrorCode, this.getClass(), anAtomContainer,
                    this.getMolIDString(anAtomContainer));
            case 3 -> new ReportDataObject(anErrorCode, this.getClass(), this.pipelineProcessingStepID,
                    anAtomContainer, this.getMolIDString(anAtomContainer));
            case 4 -> new ReportDataObject(anErrorCode, this.getClass(), anAtomContainer,
                    this.getMolIDString(anAtomContainer), this.getExternalIDString(anAtomContainer));
            case 5 -> new ReportDataObject(anErrorCode, this.getClass(), this.pipelineProcessingStepID,
                    anAtomContainer, this.getMolIDString(anAtomContainer), this.getExternalIDString(anAtomContainer));
            default -> throw new IllegalStateException("An illegal state has been reached.");
        };
        this.reporter.appendReport(tmpReportDataObject);
    }

    /**
     * Returns the string representation of the MolID of the structure or null, if the structure has no MolID.
     *
     * @param anAtomContainer atom container to get the MolID from
     * @return string representation of the MolID of the given structure or null
     * @throws NullPointerException if the given IAtomContainer instance is null
     */
    protected String getMolIDString(IAtomContainer anAtomContainer) throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of IAtomContainer) is null.");
        String tmpMolIDString = null;
        Object tmpMolID = anAtomContainer.getProperty(IProcessingStep.MOL_ID_PROPERTY_NAME);
        if (tmpMolID != null) {
            tmpMolIDString = tmpMolID.toString();
        } // else: tmpMolIDString remains null
        return tmpMolIDString;
    }

    /**
     * Returns the string representation of the external identifier of the structure or a default string, if the
     * respective atom container property is unset.
     *
     * @param anAtomContainer atom container to get the external second identifier from
     * @return string representation of the external identifier or a placeholder string
     * @throws NullPointerException if the given IAtomContainer instance is null; if {@link #externalIDPropertyName} is
     *                              null
     */
    protected String getExternalIDString(IAtomContainer anAtomContainer) throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of IAtomContainer) is null.");
        Objects.requireNonNull(this.externalIDPropertyName, "The external ID property name of the pipeline is" +
                " unset.");
        String tmpExternalIDString;
        Object tmpExternalID = anAtomContainer.getProperty(IProcessingStep.MOL_ID_PROPERTY_NAME);
        if (tmpExternalID != null) {
            tmpExternalIDString = tmpExternalID.toString();
        } else {
            tmpExternalIDString = IProcessingStep.EXTERNAL_ID_PLACEHOLDER_STRING;
        }
        return tmpExternalIDString;
    }

    @Override
    public String getExternalIDPropertyName() {
        return this.externalIDPropertyName;
    }

    @Override
    public void setExternalIDPropertyName(String anExternalIDPropertyName) throws IllegalArgumentException {
        if (anExternalIDPropertyName != null && anExternalIDPropertyName.isBlank()) {
            throw new IllegalArgumentException("The given property name (anExternalIDPropertyName) is empty or blank.");
        }
        this.externalIDPropertyName = anExternalIDPropertyName;
    }

    /**
     * {@inheritDoc}
     *  If the reporter has not been specified via a respective constructor, the reporter is an instance of
     *  {@link MarkDownReporter}.
     */
    @Override
    public IReporter getReporter() {
        return this.reporter;
    }

    @Override
    public void setReporter(IReporter aReporter){
        Objects.requireNonNull(aReporter, "aReporter (instance of IReporter) is null.");
        this.reporter = aReporter;
    }

    @Override
    public boolean isReporterSelfContained() {
        return this.isReporterSelfContained;
    }

    @Override
    public void setIsReporterSelfContained(boolean anIsSelfContained) {
        this.isReporterSelfContained = anIsSelfContained;
    }

    /**
     * {@inheritDoc}
     * This field needs to be set manually / by the superordinate pipeline.
     */
    @Override
    public String getPipelineProcessingStepID() {
        return this.pipelineProcessingStepID;
    }

    /**
     * {@inheritDoc}
     *  This field needs to be set manually / by the superordinate pipeline.
     */
    @Override
    public void setPipelineProcessingStepID(String aProcessingStepID) throws IllegalArgumentException {
        if (aProcessingStepID != null && aProcessingStepID.isBlank()) {
            throw new IllegalArgumentException("The given processing step identifier may not be empty or blank.");
        }
        this.pipelineProcessingStepID = aProcessingStepID;
    }

}
