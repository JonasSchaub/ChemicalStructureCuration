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

package de.unijena.cheminf.curation.filter;

import de.unijena.cheminf.curation.BaseProcessingStep;
import de.unijena.cheminf.curation.reporter.IReporter;
import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

import java.util.Objects;

/**
 * Abstract class BaseFilter - implements IFilter.  TODO
 */
public abstract class BaseFilter extends BaseProcessingStep implements IFilter {

    /*
    TODO: flag filtered / not filtered ACs?
    TODO: add countOfFilteredACs (?)
    TODO: is there a way to use the loggers of the classes that extend this class for logging?
     */

    /**
     * Convenience constructor. Calls the main constructor with both params set to null.
     */
    public BaseFilter() {   //TODO: view all the filters constructors!!
        this(null, null);
    }

    /**
     * {@inheritDoc}
     */
    public BaseFilter(IReporter aReporter, String anOptionalIDPropertyName) {
        super(aReporter, anOptionalIDPropertyName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IAtomContainerSet process(IAtomContainerSet anAtomContainerSet) throws NullPointerException {
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of IAtomContainerSet) is null.");
        final IAtomContainerSet tmpFilteredACSet = new AtomContainerSet();
        for (IAtomContainer tmpAtomContainer :
                anAtomContainerSet.atomContainers()) {
            //apply filter
            if (!this.isFiltered(tmpAtomContainer, true)) {
                tmpFilteredACSet.addAtomContainer(tmpAtomContainer);
            }
        }
        return tmpFilteredACSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFiltered(IAtomContainer anAtomContainer) throws NullPointerException {
        return this.isFiltered(anAtomContainer, false);
    }

    /**
     * TODO
     *
     * @param anAtomContainer IAtomContainer instance to be checked
     * @param aReportToReporter boolean value whether to report issues with the given structure to the reporter
     *                          or throw exceptions instead
     * @return true if the structure does not pass the filter or has issues that were reported to the reporter;
     *         false if the structure passes the criteria of the filter
     * @throws NullPointerException if the given IAtomContainer instance is null
     */
    protected abstract boolean isFiltered(IAtomContainer anAtomContainer, boolean aReportToReporter) throws NullPointerException;

}
