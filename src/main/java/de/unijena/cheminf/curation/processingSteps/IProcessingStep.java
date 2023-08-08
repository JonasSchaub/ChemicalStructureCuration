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

import de.unijena.cheminf.curation.reporter.IReporter;
import de.unijena.cheminf.curation.utils.ProcessingStepUtils;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

/**
 * Interface of all processing steps. A processing step takes, processes and returns a set of structures and reports
 * encountered issues to a reporter. Furthermore, processing steps provide everything needed to be used as part of
 * a pipeline of multiple processing steps.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public interface IProcessingStep {

    /*
    TODO (optional):
        - method to deep copy / clone a processing step?
     */

    /**
     * Name string of the atom container property that is used to store the MolID. For the processing every atom
     * container needs to have this ID assigned to track and uniquely identify each single atom container and enhance
     * the generated report. The MolID is meant to be a String-property to give the option of extending it with
     * information on duplicates or parent structures.
     */
    public static final String MOL_ID_PROPERTY_NAME = "Processing_MolID";

    /**
     * String that is used instead of the external ID if an atom container does not possess the property that shall
     * contain this optional, second ID. It is only used, if the processing step has been given a property name for the
     * external ID.
     */
    public static final String EXTERNAL_ID_PLACEHOLDER_STRING = "[No external ID]";

    /**
     * Processes the given atom container set according to the logic of the respective processing step and generates
     * a report containing info on issues encountered with structures; respective structures are excluded from the
     * returned atom container set. A MolID (atom container property of name {@link #MOL_ID_PROPERTY_NAME}) gets
     * assigned to every given atom container; it contains the index the atom container has in the processed atom
     * container set and may be used to trace down issues. During the processing the MolIDs might be extended with info
     * on parent structures or duplicates (if the processing step does so).
     * <p>The reporting and MolID assignment can both be prevented by setting the {@link #isReporterSelfContained()}
     * flag to false. If so, issues with structures are only passed to the reporter; the {@link IReporter#report()}
     * method to generate / finalize the report can later be executed manually or by a supervisory pipeline (see {@link
     * CurationPipeline}). Formally assigned MolIDs do not get overwritten.</p>
     * <p><b>WARNING:</b> The given data might be subject to (irreversible) changes if it is not cloned before
     * processing. See the respective parameter.</p>
     *
     * @param anAtomContainerSet     the atom container set to process
     * @param aCloneBeforeProcessing a flag indicating whether to clone the given data before processing; use this
     *                               option to avoid any changes or modifications to the original data; the {@link
     *                               IAtomContainer#clone()} method is used for the cloning of the atom containers
     * @return the processed atom container set
     * @throws NullPointerException if the given IAtomContainerSet instance is null or an atom container of the set does
     *                              not possess a MolID (this will only cause an exception, if the atom container does
     *                              not pass the processing without causing an issue)
     * @throws Exception            if an unexpected, fatal exception occurs
     * @see #getReporter()
     * @see #setReporter(IReporter)
     * @see #MOL_ID_PROPERTY_NAME
     * @see ProcessingStepUtils#assignMolIdToAtomContainers(IAtomContainerSet)
     * @see ProcessingStepUtils#getAssignedMolID(IAtomContainer)
     * @see ProcessingStepUtils#getArrayOfAssignedMolIDs(IAtomContainerSet)
     */
    public IAtomContainerSet process(
            IAtomContainerSet anAtomContainerSet,
            boolean aCloneBeforeProcessing
    ) throws NullPointerException, Exception;

    /**
     * Returns the name string of the atom container property structures processed by this processing step are expected
     * to store a second, external identifier in. The respective atom container property might contain information such
     * as name or CAS registry number of the structures. The field is null by default.
     *
     * @return String instance with the property name
     * @see #setExternalIDPropertyName(String)
     */
    public String getExternalIDPropertyName();

    /**
     * Sets the field that contains the name string of the atom container property structures processed by this
     * processing step are expected to store a second, external identifier in. This property may store info such as name
     * or CAS registry number of the structures. If no such property exists, this field shall be set to null (default).
     * Otherwise, every atom container processed by this processing step is expected to have a respective property; the
     * external identifier is then used to enrich the reports.
     *
     * @param anExternalIDPropertyName  String instance with the name of the atom container property or null
     * @throws IllegalArgumentException if the given string is empty or blank
     * @see #getExternalIDPropertyName()
     */
    public void setExternalIDPropertyName(String anExternalIDPropertyName) throws IllegalArgumentException;

    /**
     * Returns the reporter of the processing step.
     *
     * @return IReporter instance
     */
    public IReporter getReporter();

    /**
     * Sets the reporter of the processing step.
     *
     * @param aReporter IReporter instance
     * @throws NullPointerException if given reporter is null
     */
    public void setReporter(IReporter aReporter) throws NullPointerException;

    /**
     * Returns whether the reporter instance of this processing step is self-contained; depending on this, the {@link
     * #process} method calls the reporter's {@link IReporter#report()} method. By default, this flag shall be true.
     *
     * @return boolean value
     */
    public boolean isReporterSelfContained();

    /**
     * Sets whether the reporter instance of this processing step is self-contained; depending on this, the {@link
     * #process} method of this instance calls the {@code .report()} method of the reporter.
     *
     * @param anIsSelfContained boolean value
     */
    public void setIsReporterSelfContained(boolean anIsSelfContained);

    /**
     * Gets the identifier of the processing step which should equal the index of the processing step in a superordinate
     * pipeline and may be null if the processing step is not part of a pipeline. It is stored as string to enable
     * subordinate IDs (e.g. "1.1").
     *
     * @return String instance or null, if the processing step is not part of a pipeline
     */
    public String getPipelineProcessingStepID();

    /**
     * Sets the identifier of the processing step which should equal the index of the processing step in a superordinate
     * pipeline and may be null if the processing step is not part of a pipeline. It is stored as string to enable
     * subordinate IDs (e.g. "1.1").
     *
     * @param aProcessingStepID String instance or null, if the processing step is not part of a pipeline
     * @throws IllegalArgumentException if the given string is blank or empty
     */
    public void setPipelineProcessingStepID(String aProcessingStepID) throws IllegalArgumentException;

}
