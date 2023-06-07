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

package de.unijena.cheminf.curation;

import de.unijena.cheminf.curation.reporter.IReporter;
import org.openscience.cdk.interfaces.IAtomContainerSet;

/**
 * TODO
 */
public interface IProcessingStep {

    /**
     * Processes the given atom container set according to the processing steps added to the pipeline. To avoid any
     * changes or modifications to your original data, use the option of cloning the given atom container set before
     * processing (by setting the boolean parameter to true).
     * <ul>
     * <b>WARNING:</b> The given data might be subject to (irreversible) changes if it is not cloned before processing.
     * </ul>
     * By the end of the processing, a report file containing info on ... is created.
     * TODO
     *
     * @param anAtomContainerSet atom container set to process
     * @param aCloneBeforeProcessing boolean value, whether to clone the atom containers of the given set before
     *                               processing them
     * @return the processed atom container set
     * @throws NullPointerException if the given IAtomContainerSet instance is null
     */
    public IAtomContainerSet process(IAtomContainerSet anAtomContainerSet, boolean aCloneBeforeProcessing) throws NullPointerException;

    /**
     * Returns the name string of the atom container property containing an optional second identifier (e.g. the name
     * of the structure). This field is specified in the constructor or in the respective setter.
     * If the field is null, no second identifier is used when reporting.
     *
     * @return String containing the property name
     * @see #setOptionalIDPropertyName(String)
     */
    public String getOptionalIDPropertyName();

    /**
     * Sets the name string of the atom container property containing an optional second identifier (e.g. the name
     * of the structure) that is to be used when reporting the processing of a set of structures. If null is given
     * or the processed structures do not have this property, no second identifier will be used at reporting.
     *
     * @param anOptionalIDPropertyName String containing the property name
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
     * TODO: use default reporter if null is given?
     *
     * @param aReporter IReporter instance
     * @throws NullPointerException if the given instance of IReporter is null
     */
    public void setReporter(IReporter aReporter) throws NullPointerException;

    /**
     * Gets which index the processing step has in a superordinate pipeline.
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
