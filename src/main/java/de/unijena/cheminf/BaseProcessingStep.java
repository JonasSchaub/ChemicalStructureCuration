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

package de.unijena.cheminf;

import de.unijena.cheminf.curation.ChemicalStructureCurationUtils;
import de.unijena.cheminf.reporter.IReporter;
import org.openscience.cdk.interfaces.IAtomContainerSet;

import java.util.Objects;

/**
 * TODO
 */
public abstract class BaseProcessingStep implements IProcessingStep {

    /*
    TODO: param checks
     */

    /**
     * Reporter of this processing step.
     */
    private IReporter reporter;

    //TODO: Constructor
    /*public BaseProcessingStep(IReporter aDefaultReporter) throws NullPointerException {
        Objects.requireNonNull(aDefaultReporter, "aDefaultReporter (instance of IReporter) has been null.");
        this.reporter = aDefaultReporter;
    }*/

    /**
     * Clones the given atom container set and processes it by giving it to the method
     * {@link #process(IAtomContainerSet)}.
     *
     * @param anAtomContainerSet atom container set to clone and process
     * @return the set of cloned and processed atom containers
     * @throws NullPointerException if the given IAtomContainerSet instance is null
     */
    @Override
    public IAtomContainerSet cloneAndProcess(IAtomContainerSet anAtomContainerSet) throws NullPointerException {
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of IAtomContainerSet) is null.");
        IAtomContainerSet tmpClonedACSet = ChemicalStructureCurationUtils.cloneAtomContainerSet(this.reporter, anAtomContainerSet);
        return this.process(tmpClonedACSet);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IAtomContainerSet process(IAtomContainerSet anAtomContainerSet) {
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of IAtomContainerSet) is null.");
        IAtomContainerSet tmpResultingACSet = this.process(anAtomContainerSet, this.reporter, null);
        this.reporter.report();
        return tmpResultingACSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IReporter getReporter() {
        return reporter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setReporter(IReporter aReporter) {
        Objects.requireNonNull(aReporter, "aReporter (instance of IReporter) is null.");
        this.reporter = aReporter;
    }

}
