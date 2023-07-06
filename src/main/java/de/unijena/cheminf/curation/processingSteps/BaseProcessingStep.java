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
     * Name string of the atom container property that contains an optional second identifier of type integer. If the
     * String is null (default) or the reported atom containers do not have a respective property, no second identifier
     * is used at the reporting of the processing.
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

    /**
     * Constructor; initializes the fields {@link #reporter} and {@link #optionalIDPropertyName}.
     *
     * @param aReporter reporter to report to when processing; if null is given, the class field is initialized with an
     *                  instance of the default reporter ({@link MarkDownReporter})
     * @param anOptionalIDPropertyName name string of the atom container property containing an optional second
     *                                 identifier (e.g. the name of the structure) for each structure that is to be
     *                                 processed by this processing step; if null is given, no second identifier is used
     */
    public BaseProcessingStep(IReporter aReporter, String anOptionalIDPropertyName) {
        //if given reporter is null, the field is initialized with an instance of MarkDownReporter
        this.reporter = Objects.requireNonNullElseGet(aReporter, MarkDownReporter::new);
        this.optionalIDPropertyName = anOptionalIDPropertyName;
    }

    @Override
    public IAtomContainerSet process(IAtomContainerSet anAtomContainerSet,
                                     boolean aCloneBeforeProcessing,
                                     boolean anAssignIdentifiers) throws NullPointerException, Exception {
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of IAtomContainerSet) is null.");
        //assign MolIDs to the given structures if so desired
        if (anAssignIdentifiers) {
            ProcessingStepUtils.assignMolIdToAtomContainers(anAtomContainerSet);    //TODO: only assign the ID to the clone?
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
     * does not possess a MolID (this will only cause an exception, if the atom container does not pass the processing
     * without causing an issue)
     * @throws Exception if an unexpected, fatal exception occurred
     */
    protected abstract IAtomContainerSet applyLogic(IAtomContainerSet anAtomContainerSet) throws NullPointerException, Exception;

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
                this.appendToReporter(tmpAtomContainer, ErrorCodes.CLONE_ERROR);
            }
        }
        return tmpCloneOfGivenACSet;
    }

    /**
     * Appends a report to the reporter by creating a ReportDataObject instance according to the given data and passing
     * it to the reporter instance. If the given atom container is not null, it is expected to have a property with the
     * name {@link #MOL_ID_PROPERTY_NAME} and with a value not null; otherwise an NullPointerException is thrown.
     *
     * @param anAtomContainer atom container instance the issue refers to
     * @param anErrorCode error code of the reported issue
     * @throws NullPointerException if the given ErrorCodes constant is null; if the value returned by {@link
     * #getMolIDString(IAtomContainer)} is null while anAtomContainer not being null
     */
    protected void appendToReporter(IAtomContainer anAtomContainer, ErrorCodes anErrorCode) throws NullPointerException {
        Objects.requireNonNull(anErrorCode, "anErrorCode (constant of ErrorCodes) is null.");
        ReportDataObject tmpReportDataObject;
        if (anAtomContainer == null) {
            //create report data object for atom container being null
            tmpReportDataObject = new ReportDataObject(
                    this.pipelineProcessingStepID,
                    this.getClass(),
                    anErrorCode
            );
        } else {
            //get the identifiers of the atom container if the atom container is not null
            String tmpMolIDString = this.getMolIDString(anAtomContainer);
            String tmpOptionalIDString = this.getOptionalIDString(anAtomContainer);
            //create report data object for atom container not being null
            tmpReportDataObject = new ReportDataObject(
                    anAtomContainer,
                    tmpMolIDString,     //TODO: what if MolID is null?
                    tmpOptionalIDString,
                    this.pipelineProcessingStepID,
                    this.getClass(),
                    anErrorCode
            );
        }
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
        String tmpMolIDString;
        try {
            Object tmpMolID = anAtomContainer.getProperty(IProcessingStep.MOL_ID_PROPERTY_NAME);
            tmpMolIDString = ((tmpMolID != null) ?
                    anAtomContainer.getProperty(IProcessingStep.MOL_ID_PROPERTY_NAME).toString()
                    : null);
        } catch (NullPointerException aNullPointerException) {
            tmpMolIDString = null;
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
        String tmpOptionalIDString;
        if (this.optionalIDPropertyName == null) {
            tmpOptionalIDString = null;
        } else {
            try {
                Object tmpOptionalID = anAtomContainer.getProperty(IProcessingStep.MOL_ID_PROPERTY_NAME);
                tmpOptionalIDString = ((tmpOptionalID != null) ?
                        anAtomContainer.getProperty(IProcessingStep.MOL_ID_PROPERTY_NAME).toString()
                        : null);
            } catch (NullPointerException aNullPointerException) {
                tmpOptionalIDString = null;
            }
        }
        return tmpOptionalIDString;
    }

    @Override
    public String getOptionalIDPropertyName() {
        return this.optionalIDPropertyName;
    }

    @Override
    public void setOptionalIDPropertyName(String anOptionalIDPropertyName) {
        this.optionalIDPropertyName = anOptionalIDPropertyName;
    }

    @Override
    public IReporter getReporter() {
        return this.reporter;
    }

    /**
     * Sets the reporter of this processing step; uses a default reporter (instance of {@link MarkDownReporter}) if the
     * given IReporter instance is null.
     *
     * @param aReporter IReporter instance
     */
    @Override
    public void setReporter(IReporter aReporter){
        //if the given reporter is null, the reporter is initialized with an instance of MarkDownReporter
        this.reporter = Objects.requireNonNullElseGet(aReporter, MarkDownReporter::new);
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
    public void setPipelineProcessingStepID(String aProcessingStepID) {
        //TODO: accept empty or blank Strings?
        this.pipelineProcessingStepID = aProcessingStepID;
    }

}
