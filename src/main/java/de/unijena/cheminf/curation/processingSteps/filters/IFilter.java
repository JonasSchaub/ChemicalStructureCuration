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

package de.unijena.cheminf.curation.processingSteps.filters;

import de.unijena.cheminf.curation.processingSteps.IProcessingStep;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

/**
 * Filters are processing steps that filter individual molecules from a bigger set according to a defined molecular
 * descriptor value. The filtering process shall not modify any information held by one of the atom containers. Whether
 * an atom container passes a filter or not should be determinable without knowledge of any other atom containers,
 * hence this interface is not meant for tasks like e.g. filtering duplicates.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public interface IFilter extends IProcessingStep {

    /**
     * {@inheritDoc}
     * <ul>
     * <li><b>Note:</b> Classes implementing {@link IFilter} are not meant to cause any modifications to the provided
     * atom containers.</li>
     * </ul>
     */
    public IAtomContainerSet process(
            IAtomContainerSet anAtomContainerSet,
            boolean aCloneBeforeProcessing,
            boolean anAssignIdentifiers
    ) throws NullPointerException, Exception;

    /**
     * Checks whether the filter applies to a given IAtomContainer instance. Returns true, if the given atom container
     * gets filtered; returns false, if the atom container passes the filter; throws an exception if there is an issue
     * with the structure. In case of min and max threshold values, these values are considered as inclusive and
     * respective atom containers pass the filter.
     *
     * @param anAtomContainer IAtomContainer instance to be checked
     * @return true if the structure does not pass the filter
     * @throws NullPointerException if the given IAtomContainer instance is null
     * @throws Exception any other, filter specific exception
     */
    public boolean isFiltered(IAtomContainer anAtomContainer) throws NullPointerException, Exception;

}
