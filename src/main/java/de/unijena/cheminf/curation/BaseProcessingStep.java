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
import de.unijena.cheminf.curation.reporter.ReportDataObject;
import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class BaseProcessingStep implements IProcessingStep {

    /*
    TODO:
     - find a solution on where and how to assign identifier to each given atom container
        -> general solution for all processing steps?
        -> I do not like the idea that only some of the implementations assign an ID when .process() is called and
           others do not
    //
    TODO:
        - initialize reporter
        - make file destination adjustable without the need to set a new reporter?
     */

    /**
     * Logger of this class.
     */
    private static final Logger LOGGER = Logger.getLogger(BaseProcessingStep.class.getName());

    /**
     * Reporter of this processing step.
     */
    private IReporter reporter;

    /**
     * Name string of the atom container property that contains an optional second identifier of type integer. If the
     * String is not null, no second identifier is used.
     */
    private String optionalIDPropertyName;

    /**
     * String with the index of the processing step in the pipeline (if it is part of a pipeline), or null if it is not
     * part of a pipeline. It is stored as string to enable subordinate IDs (e.g. "1.1").
     */
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
    public IAtomContainerSet process(IAtomContainerSet anAtomContainerSet, boolean aCloneBeforeProcessing, boolean anAssignIdentifiers) throws NullPointerException {
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of IAtomContainerSet) is null.");
        //TODO: assign IDs to the given atom containers dependent on second boolean param
        if (anAssignIdentifiers) {
            ProcessingStepUtils.assignMolIdToAtomContainers(anAtomContainerSet);
        } //TODO: else: check existence of MolID ?
        if (aCloneBeforeProcessing) {
            IAtomContainerSet tmpClonedACSet = this.cloneAtomContainerSet(anAtomContainerSet);
            return this.process(tmpClonedACSet);
        }
        IAtomContainerSet tmpResultingACSet = this.process(anAtomContainerSet);
        if (this.indexOfStepInPipeline == null) {
            //generate the report file only if the processing step is not part of a pipeline
            //this.reporter.report();   //TODO: default reporter necessary
            //this.reporter.clear();
        }
        return tmpResultingACSet;
    }

    /**
     * TODO
     * @param anAtomContainerSet
     * @return
     * @throws NullPointerException
     */
    protected abstract IAtomContainerSet process(IAtomContainerSet anAtomContainerSet) throws NullPointerException;

    /** TODO
     * TODO: place in utils? would mean a lot more of params (reporter, optionalIDPropertyName, this.getClass())
     * Clones the given atom container set. Atom containers that cause a CloneNotSupportedException to be thrown are
     * appended to the given reporter and excluded from the returned atom container set. The total count of atom
     * containers failing the cloning-process is being logged.
     *
     * @param anAtomContainerSet atom container set to be cloned
     * @return a clone of the given atom container set
     * @throws NullPointerException if the given IAtomContainerSet or IReporter instance is null
     */
    private IAtomContainerSet cloneAtomContainerSet(IAtomContainerSet anAtomContainerSet) throws NullPointerException {
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of IAtomContainerSet) is null.");
        IAtomContainerSet tmpCloneOfGivenACSet = new AtomContainerSet();
        int tmpCloneNotSupportedExceptionsCount = 0;
        for (IAtomContainer tmpAtomContainer :
                anAtomContainerSet.atomContainers()) {
            try {
                tmpCloneOfGivenACSet.addAtomContainer(tmpAtomContainer.clone());
            } catch (CloneNotSupportedException aCloneNotSupportedException) {
                tmpCloneNotSupportedExceptionsCount++;
                //TODO: check the data type of both atom container properties?
                this.reporter.appendReport(new ReportDataObject(
                        tmpAtomContainer,
                        tmpAtomContainer.getProperty(IProcessingStep.MOL_ID_PROPERTY_NAME).toString(),
                        tmpAtomContainer.getProperty(this.optionalIDPropertyName).toString(),
                        null,
                        this.getClass(),
                        ErrorCodes.CLONE_ERROR
                ));
            }
        }
        if (tmpCloneNotSupportedExceptionsCount > 0) {
            BaseProcessingStep.LOGGER.log(Level.WARNING, tmpCloneNotSupportedExceptionsCount + " of " + //TODO: logging level?
                    anAtomContainerSet.getAtomContainerCount() + " given atom containers could not be cloned and" +
                    " thereby were excluded from the returned atom container set.");
        }
        return tmpCloneOfGivenACSet;
    }

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
        //TODO: just check for not null? / use the default reporter instead?
        //Objects.requireNonNull(aReporter, "aReporter (instance of IReporter) is null.");  //TODO: would cause problems with too many test methods at the moment
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
        //TODO: accept empty or blank Strings?
        this.indexOfStepInPipeline = anIndexString;
    }

}
