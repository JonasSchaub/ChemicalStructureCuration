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
     * Processes the given atom container set according to the logic of the respective processing step. By the end of
     * the processing, a report containing info on encountered issues with structures is created. The {@link
     * #isReporterSelfContained()} flag needs to be true for this; if not so, issues are only appended to the reporter
     * and can later on be reported manually or by a supervisory pipeline (see {@link CurationPipeline}). Respective
     * structures are excluded from the returned atom container set.
     * <p>
     * <b>WARNING:</b> The given data might be subject to (irreversible) changes if it is not cloned before
     * processing. See the respective parameter.
     * </p>
     *
     * @param anAtomContainerSet the atom container set to process
     * @param aCloneBeforeProcessing a flag indicating whether to clone the given data before processing; use this
     *                               option to avoid any changes or modifications to the original data
     * @param anAssignIdentifiers a flag indicating whether to assign MolIDs (atom container property with name {@link
     *                            #MOL_ID_PROPERTY_NAME}) to the atom containers of the given set; if true, the assigned
     *                            MolIDs contain the respective index of the atom container in the set before processing
     *                            (formerly assigned MolIDs do get overwritten); in case of false, every given atom
     *                            container is expected to have a MolID assigned and an exception might be thrown if
     *                            this is not the case; the MolIDs might be extended with further information during the
     *                            processing (e.g. with info on parent structures or duplicates of the molecule)
     * @return the processed atom container set
     * @throws NullPointerException if the given IAtomContainerSet instance is null or an atom container of the set
     * does not possess a MolID (this will only cause an exception, if the atom container does not pass the processing
     * without causing an issue)
     * @throws Exception if an unexpected, fatal exception occurred
     * @see #getReporter()
     * @see #setReporter(IReporter)
     * @see #setPipelineProcessingStepID(String)
     * @see #MOL_ID_PROPERTY_NAME
     * @see ProcessingStepUtils#assignMolIdToAtomContainers(IAtomContainerSet)
     * @see ProcessingStepUtils#getAssignedMolID(IAtomContainer)
     * @see ProcessingStepUtils#getArrayOfAssignedMolIDs(IAtomContainerSet)
     */
    public IAtomContainerSet process(
            IAtomContainerSet anAtomContainerSet,
            boolean aCloneBeforeProcessing,
            boolean anAssignIdentifiers
    ) throws NullPointerException, Exception;

    /**
     * Returns the name string of the atom container property that contains an optional second identifier. The
     * respective atom container property might store information such as a name of the structure or the CAS registry
     * number. The field might be null if it has not been specified via constructor or setter.
     *
     * @return String instance with the property name
     * @see #setOptionalIDPropertyName(String)
     */
    public String getOptionalIDPropertyName();

    /**
     * Sets the field that contains the name string of the atom container property that stores an optional second
     * identifier of the structure such as a name or its CAS registry number. If no such property exists, the field
     * shall be set to null (default). Otherwise, every atom container processed by this processing step is expected to
     * have a property with the respective name; the info is then used to enrich the generated reports.
     *
     * @param anOptionalIDPropertyName String instance with the name of the atom container property or null
     * @throws IllegalArgumentException if the given string is empty or blank
     * @see #getOptionalIDPropertyName()
     */
    public void setOptionalIDPropertyName(String anOptionalIDPropertyName) throws IllegalArgumentException;

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
