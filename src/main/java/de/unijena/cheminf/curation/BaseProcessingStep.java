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

import java.util.Objects;

public abstract class BaseProcessingStep implements IProcessingStep {

    /**
     * Reporter of this processing step.
     */
    private IReporter reporter; //TODO: is "private" here correct?

    /**
     * Constructor. TODO
     *
     * @param aDefaultReporter default reporter of the instance
     * @throws NullPointerException if the given IReporter instance is null
     */
    /*public BaseCurationPipelineStep(IReporter aDefaultReporter) throws NullPointerException {
        Objects.requireNonNull(aDefaultReporter, "aDefaultReporter (instance of IReporter) has been null.");
        this.reporter = aDefaultReporter;
    }*/

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
