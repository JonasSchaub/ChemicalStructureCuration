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

import java.util.Objects;

public abstract class BaseProcessingStep implements IProcessingStep {

    /**
     * Reporter of this processing step.
     */
    private IReporter reporter; //TODO: is "private" here correct?

    /**
     * Name string of the atom container property that contains an optional second identifier of type integer. If the
     * String is not null, no second identifier is used.
     */
    private String optionalIDPropertyName;

    private String indexOfStepInPipeline = null;

    /**
     * Constructor. TODO
     *
     * @param aReporter reporter to report to when processing; if null is given, an instance of the default reporter
     *                  is used
     * @param anOptionalIDPropertyName optional name string of an atom container property that contains an optional
     *                                 second identifier to be used at the reporting of a processing process; if null
     *                                 is given, no second identifier is used
     */
    public BaseProcessingStep(IReporter aReporter, String anOptionalIDPropertyName) {
        if (aReporter == null) {
            //TODO: initialize with default reporter
        } else {
            this.reporter = aReporter;
        }
        this.optionalIDPropertyName = anOptionalIDPropertyName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IAtomContainerSet process(IAtomContainerSet anAtomContainerSet, boolean aCloneBeforeProcessing) throws NullPointerException {
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of IAtomContainerSet) is null.");
        //TODO: assign IDs to the given atom containers?
        if (aCloneBeforeProcessing) {
            IAtomContainerSet tmpClonedACSet = ChemicalStructureCurationUtils.cloneAtomContainerSet(anAtomContainerSet, this.reporter);
            return this.process(tmpClonedACSet);
        }
        return this.process(anAtomContainerSet);
    }

    /**
     * TODO
     * @param anAtomContainerSet
     * @return
     * @throws NullPointerException
     */
    protected abstract IAtomContainerSet process(IAtomContainerSet anAtomContainerSet) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getOptionalIDPropertyName() {
        return this.optionalIDPropertyName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOptionalIDPropertyName(String anOptionalIDPropertyName) {
        this.optionalIDPropertyName = anOptionalIDPropertyName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IReporter getReporter() {
        return this.reporter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setReporter(IReporter aReporter) throws NullPointerException {
        //Objects.requireNonNull(aReporter, "aReporter (instance of IReporter) is null.");  //TODO: use default reporter?
        this.reporter = aReporter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getIndexOfStepInPipeline() {
        return this.indexOfStepInPipeline;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setIndexOfStepInPipeline(String anIndexString) {
        this.indexOfStepInPipeline = anIndexString;
    }

}
