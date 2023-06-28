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
 * encountered issues to a reporter. Furthermore, processing steps provide everything needed to use them as part of
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
     * Name string of the atom container property that is used to store the MolID. The MolID is an ID that needs to be
     * assigned to every atom container of a processed atom container set to uniquely identify each single atom
     * container and enhance the entries of the report file. The MolID is meant to be a String-property to give the
     * option to extend info on duplicates or parent structures.
     */
    public static final String MOL_ID_PROPERTY_NAME = "Processing_MolID";

    /**
     * Processes the given atom container set according to the processing step. To avoid any changes or modifications
     * to the original data, use the option of cloning the given atom container set before the processing (by setting
     * the first boolean parameter to true).
     * <ul>
     * <li><b>WARNING:</b> The given data might be subject to (irreversible) changes if it is not cloned before
     * processing.</li>
     * </ul>
     * To make modifications to and possible issues with structures the most traceable, it is advised to set the second
     * boolean parameter to true to automatically assign a MolID, an atom container property with the name
     * {@link #MOL_ID_PROPERTY_NAME}, to every atom container in the given set. The MolID is a String containing the
     * index of the respective atom container in the original data set. Note that in this case formerly assigned values
     * do get overwritten. If the second boolean parameter is set to false, every atom container of the given set is
     * expected to have a MolID assigned; an exception might be thrown if this is not the case.<br>
     * <br>
     * By the end of the processing and if the index of this processing step has not been set (via
     * {@link #setIndexOfStepInPipeline(String)}) to anything else than null (default), a report file containing info
     * on issues with structures is created. If the index differs from null, a supervisory entity is expected to
     * execute the {@link IReporter#report()} method of the reporter. The reporter can be accessed via the respective
     * getter and setter to change the (type of) reporter or modify the location to create the report file at.
     *
     * @param anAtomContainerSet atom container set to process
     * @param aCloneBeforeProcessing boolean value, whether to process the original data or to clone the given atom
     *                               container set before processing
     * @param anAssignIdentifiers boolean value, whether to assign a MolID to the atom containers of the given set; in
     *                            case of false, every atom container in the set is expected to have a MolID assigned
     * @return the processed atom container set
     * @throws NullPointerException if the given IAtomContainerSet instance is null or an atom container of the set
     * does not possess a MolID (this will only cause an exception, if the atom container does not pass the processing
     * without causing an issue)
     * @see #getReporter()
     * @see #setReporter(IReporter)
     * @see #setIndexOfStepInPipeline(String)
     * @see #MOL_ID_PROPERTY_NAME
     * @see ProcessingStepUtils#assignMolIdToAtomContainers(IAtomContainerSet)
     * @see ProcessingStepUtils#getAssignedMolID(IAtomContainer)
     * @see ProcessingStepUtils#getArrayOfAssignedMolIDs(IAtomContainerSet)
     */
    public IAtomContainerSet process(
            IAtomContainerSet anAtomContainerSet,
            boolean aCloneBeforeProcessing,
            boolean anAssignIdentifiers
    ) throws NullPointerException;

    /**
     * Returns the name string of the atom container property containing an optional second identifier (example given
     * the name of the structure) or null, if the field has not been specified via the constructor or the respective
     * setter. The second identifier is used at the reporting of the processing of sets of structures. If the field is
     * null or processed structures do not have this property, no second identifier will be used at reporting.
     *
     * @return String instance with the property name
     * @see #setOptionalIDPropertyName(String)
     */
    public String getOptionalIDPropertyName();

    /**
     * Sets the name string of the atom container property containing an optional second identifier (example given the
     * name of the structure) that is to be used when reporting the processing of a set of structures. If null is given
     * or the processed structures do not have this property, no second identifier will be used at reporting.
     *
     * @param anOptionalIDPropertyName String instance with the property name
     * @see #getOptionalIDPropertyName()
     */
    public void setOptionalIDPropertyName(String anOptionalIDPropertyName);

    /**
     * Returns the reporter of the instance.
     *
     * @return IReporter instance
     */
    public IReporter getReporter();

    /**
     * Sets the reporter of the instance.
     *
     * @param aReporter IReporter instance
     * @throws NullPointerException if the given instance of IReporter is null   TODO: use the default reporter instead?
     */
    public void setReporter(IReporter aReporter) throws NullPointerException;

    /**
     * Gets the index of the processing step in a superordinate pipeline.
     *
     * @return String instance or null, if the processing step is not part of a pipeline
     */
    public String getIndexOfStepInPipeline();

    /**
     * Sets which index the processing step has in a superordinate pipeline.
     *
     * @param anIndexString String instance or null, if the processing step is not part of a pipeline
     */
    public void setIndexOfStepInPipeline(String anIndexString);

}