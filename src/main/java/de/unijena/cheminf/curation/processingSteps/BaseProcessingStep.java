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

import java.util.Objects;
import java.util.logging.Logger;

/**
 * Base class of all processing steps. Reduces the number of abstract methods to the protected method {@link #applyLogic(
 * IAtomContainerSet)}. In addition to overwriting this one method, processing steps extending this base class possibly
 * need constructors initializing the fields specific to the processing step and respective getters.
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

    /** TODO: might be removed in the end
     * Logger of this class.
     */
    private static final Logger LOGGER = Logger.getLogger(BaseProcessingStep.class.getName());

    /**
     * Name string of the atom container property that contains an optional second identifier. The respective atom
     * container property might store information such as a name of the structure or the CAS registry number. If this
     * field gets set, every atom container processed by this processing step is expected to have a property with the
     * respective name; the info is then included in the report.
     */
    private String optionalIDPropertyName;

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
     * Constructor; initializes the fields {@link #reporter} and {@link #optionalIDPropertyName}. Since not every
     * processing step might give the option to specify the optional ID property name in the constructor, this parameter
     * is allowed to be null.
     *
     * @param aReporter the reporter that is to be used when processing sets of structures
     * @param anOptionalIDPropertyName name string of the atom container property containing an optional second
     *                                 identifier (e.g. the name or CAS registry number) for each structure; if this
     *                                 field gets specified (not null), every atom container processed by this
     *                                 processing step is expected to have a property with the respective name; the info
     *                                 is then included in the report
     * @throws NullPointerException if the given IReporter instance is null
     * @throws IllegalArgumentException if an optional ID property name is given, but it is blank or empty
     */
    public BaseProcessingStep(IReporter aReporter, String anOptionalIDPropertyName) throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(aReporter, "aReporter (instance of IReporter) is null.");
        if (anOptionalIDPropertyName != null && anOptionalIDPropertyName.isBlank()) {
            throw new IllegalArgumentException("anOptionalIDPropertyName (instance of String) is empty or blank.");
        }
        this.reporter = aReporter;
        this.optionalIDPropertyName = anOptionalIDPropertyName;
    }

    /**
     * Constructor; calls the main constructor with an instance of {@link MarkDownReporter} - initialized with the given
     * report files directory path - as default reporter. Since not every processing step might give the option to
     * specify the optional ID property name in the constructor, this parameter is allowed to be null.
     *
     * @param aReportFilesDirectoryPath the directory path for the MarkDownReporter to create the report files at
     * @param anOptionalIDPropertyName name string of the atom container property containing an optional second
     *                                 identifier (e.g. the name or CAS registry number) for each structure; if this
     *                                 field gets specified (not null), every atom container processed by this
     *                                 processing step is expected to have a property with the respective name; the info
     *                                 is then included in the report
     * @throws NullPointerException if the given String with the directory path is null
     * @throws IllegalArgumentException if the given file path is no directory path; if a property name string is given,
     *                                  but it is blank or empty
     */
    public BaseProcessingStep(String aReportFilesDirectoryPath, String anOptionalIDPropertyName)
            throws NullPointerException, IllegalArgumentException {
        //TODO: the MarkDownReporter needs a constructor that I can pass the file path to; check for not null; check whether it is a directory path
        this(new MarkDownReporter(), anOptionalIDPropertyName);
    }
    //</editor-fold>

    @Override
    public IAtomContainerSet process(IAtomContainerSet anAtomContainerSet,
                                     boolean aCloneBeforeProcessing,
                                     boolean anAssignIdentifiers) throws NullPointerException, Exception {
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of IAtomContainerSet) is null.");
        //assign MolIDs to the given structures if so desired
        if (anAssignIdentifiers) {
            ProcessingStepUtils.assignMolIdToAtomContainers(anAtomContainerSet);
            //TODO: assign the MolIDs after the cloning?
            // pro: avoid ANY changes to the original data
            // con: the assigned indices might not be correct if the cloning of even a single atom container fails
        } //TODO: else: check existence of MolIDs ?
        //clone the given atom container set if so desired
        IAtomContainerSet tmpACSetToProcess;
        if (aCloneBeforeProcessing) {
            tmpACSetToProcess = this.cloneAtomContainerSet(anAtomContainerSet);
        } else {
            tmpACSetToProcess = anAtomContainerSet;
        }
        //apply the logic of the processing step on the atom container set
        IAtomContainerSet tmpResultingACSet = this.applyLogic(tmpACSetToProcess);
        //
        //flush all data appended to the reporter, but only if the reporter is self-contained by this processing step
        if (this.isReporterSelfContained()) {
            //this.reporter.report();   //TODO: uncomment after merge with markdown-reporter-new branch
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
     */
    private IAtomContainerSet cloneAtomContainerSet(IAtomContainerSet anAtomContainerSet) throws NullPointerException {
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of IAtomContainerSet) is null.");
        IAtomContainerSet tmpCloneOfGivenACSet = new AtomContainerSet();
        for (IAtomContainer tmpAtomContainer : anAtomContainerSet.atomContainers()) {
            try {
                tmpCloneOfGivenACSet.addAtomContainer(tmpAtomContainer.clone());
            } catch (CloneNotSupportedException aCloneNotSupportedException) {
                this.appendToReporter(ErrorCodes.CLONE_ERROR, tmpAtomContainer);
            }
        }
        return tmpCloneOfGivenACSet;
    }

    /**
     * Appends a report to the reporter by creating a ReportDataObject instance according to the given data and passing
     * it to the reporter instance. If the given atom container is not null, it is expected to have a property with name
     * {@link #MOL_ID_PROPERTY_NAME}; if an optional ID property name has been set, the respective atom container
     * property may not be null; otherwise a NullPointerException is thrown.
     *
     * @param anErrorCode     error code of the reported issue
     * @param anAtomContainer atom container instance the issue refers to
     * @throws NullPointerException if the given ErrorCodes constant is null; if an atom container is given, but it
     *                              possesses no MolID; if an optional ID property name has been set but the respective
     *                              atom container property is null
     */
    protected void appendToReporter(ErrorCodes anErrorCode, IAtomContainer anAtomContainer) throws NullPointerException {
        Objects.requireNonNull(anErrorCode, "anErrorCode (constant of ErrorCodes) is null.");
        byte tmpCase;
        if (anAtomContainer == null) {
            if (this.pipelineProcessingStepID == null) {
                tmpCase = 0;    // case: no atom container; no step ID
            } else {
                tmpCase = 1;    // case: no atom container; with step ID
            }
        } else if (this.optionalIDPropertyName == null) {
            if (this.pipelineProcessingStepID == null) {
                tmpCase = 2;    // case: with atom container; no optional ID; no step ID
            } else {
                tmpCase = 3;    // case: with atom container; no optional ID; with step ID
            }
        } else {
            if (this.pipelineProcessingStepID == null) {
                tmpCase = 4;    // case: with atom container; with optional ID; no step ID
            } else {
                tmpCase = 5;    // case: with atom container; with optional ID; with step ID
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
                    this.getMolIDString(anAtomContainer), this.getOptionalIDString(anAtomContainer));
            case 5 -> new ReportDataObject(anErrorCode, this.getClass(), this.pipelineProcessingStepID,
                    anAtomContainer, this.getMolIDString(anAtomContainer), this.getOptionalIDString(anAtomContainer));
            default -> throw new IllegalStateException("An illegal state has been reached.");
        };
        this.reporter.appendReport(tmpReportDataObject);
    }

    /**
     * Returns the string representation of the structure's MolID or null, if the MolID is null or no property with
     * the name {@link #MOL_ID_PROPERTY_NAME} exists.
     *
     * @param anAtomContainer atom container to get the MolID from
     * @return string representation of the MolID of the given structure or null
     * @throws NullPointerException if the given IAtomContainer instance is null
     */
    protected String getMolIDString(IAtomContainer anAtomContainer) throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of IAtomContainer) is null.");
        String tmpMolIDString = null;
        try {
            Object tmpMolID = anAtomContainer.getProperty(IProcessingStep.MOL_ID_PROPERTY_NAME);
            if (tmpMolID != null) {
                tmpMolIDString = tmpMolID.toString();
            } // else: tmpMolIDString remains null
        } catch (NullPointerException aNullPointerException) {
            // tmpMolIDString remains null
        }
        return tmpMolIDString;
    }

    /**
     * Returns the string representation of the structure's optional second identifier or null, if {@link
     * #getOptionalIDPropertyName()} is null, an atom container property with the respective name does not exist or the
     * optional ID is null.
     *
     * @param anAtomContainer atom container to get the optional second identifier from
     * @return string representation of the optional second identifier of the given structure or null
     * @throws NullPointerException if the given IAtomContainer instance is null
     */
    protected String getOptionalIDString(IAtomContainer anAtomContainer) throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of IAtomContainer) is null.");
        String tmpOptionalIDString = null;
        if (this.optionalIDPropertyName != null) {
            try {
                Object tmpOptionalID = anAtomContainer.getProperty(IProcessingStep.MOL_ID_PROPERTY_NAME);
                if (tmpOptionalID != null) {
                    tmpOptionalIDString = tmpOptionalID.toString();
                } //else: tmpOptionalID remains null
            } catch (NullPointerException aNullPointerException) {
                //tmpOptionalID remains null
            }
        } //else: tmpOptionalID remains null
        return tmpOptionalIDString;
    }

    @Override
    public String getOptionalIDPropertyName() {
        return this.optionalIDPropertyName;
    }

    @Override
    public void setOptionalIDPropertyName(String anOptionalIDPropertyName) throws IllegalArgumentException {
        if (anOptionalIDPropertyName != null && anOptionalIDPropertyName.isBlank()) {
            throw new IllegalArgumentException("The given property name (anOptionalIDPropertyName) is empty or blank.");
        }
        this.optionalIDPropertyName = anOptionalIDPropertyName;
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
