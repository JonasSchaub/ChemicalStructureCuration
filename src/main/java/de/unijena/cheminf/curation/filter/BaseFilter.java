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
import de.unijena.cheminf.curation.ErrorCodes;
import de.unijena.cheminf.curation.reporter.IReporter;
import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract class BaseFilter - implements IFilter. Reduces the number of abstract methods to the protected method
 * {@link #isFiltered(IAtomContainer, boolean)}.
 */
public abstract class BaseFilter extends BaseProcessingStep implements IFilter {

    /**
     * Logger of this class.
     */
    private static final Logger LOGGER = Logger.getLogger(BaseFilter.class.getName());

    /**
     * Convenience constructor. Calls the main constructor with both params set to null.
     */
    public BaseFilter() {   //TODO: view all the filters constructors!!
        this(null, null);
    }

    /**
     * Constructor. TODO: what shall I type in a constructor that just calls the super? @Felix, @Jonas
     *
     * @param aReporter reporter to report to when processing; if null is given, an instance of the default reporter
     *                 is used
     * @param anOptionalIDPropertyName null or the name string of an atom container property containing an optional
     *                                 second identifier of structures to be used at the reporting of a processing
     *                                 process; if null is given, no second identifier is used
     */
    public BaseFilter(IReporter aReporter, String anOptionalIDPropertyName) {
        super(aReporter, anOptionalIDPropertyName);
    }

    /**
     * Filters the atom containers of the given atom container set according to the values returned by {@link
     * #isFiltered(IAtomContainer, boolean)}.
     *
     * @throws NullPointerException  if the given IAtomContainerSet instance is null or an atom container of the set
     * does not possess a MolID (this will only cause an exception, if the atom container does not pass the processing
     * without causing an issue)
     */
    @Override
    protected IAtomContainerSet process(IAtomContainerSet anAtomContainerSet) throws NullPointerException {
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of IAtomContainerSet) is null.");
        final IAtomContainerSet tmpFilteredACSet = new AtomContainerSet();
        for (IAtomContainer tmpAtomContainer :
                anAtomContainerSet.atomContainers()) {
            //apply filter
            try {
                if (!this.isFiltered(tmpAtomContainer, true)) {
                    tmpFilteredACSet.addAtomContainer(tmpAtomContainer);
                }
            } catch (Exception anUnexpectedException) {
                //reports and logs any unexpected exception; the structure does not pass the filter
                this.appendReportToReporter(tmpAtomContainer, ErrorCodes.UNEXPECTED_EXCEPTION_ERROR);
                BaseFilter.LOGGER.log(Level.SEVERE, anUnexpectedException.toString(), anUnexpectedException);
            }
        }
        return tmpFilteredACSet;
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException if the given IAtomContainer instance is null
     */
    @Override
    public boolean isFiltered(IAtomContainer anAtomContainer) throws NullPointerException {
        return this.isFiltered(anAtomContainer, false);
    }

    /**
     * Checks whether the filter applies on a given IAtomContainer instance. True is returned, if the given atom
     * container gets filtered; returns false if the atom container passes the filter. This implementation takes a
     * second, boolean parameter that specifies whether to report issues with the given structure to the reporter
     * or to throw an exceptions instead. If an issue gets reported, true is returned.
     *
     * @param anAtomContainer   IAtomContainer instance to be checked
     * @param aReportToReporter boolean value whether to report issues with the given structure to the reporter
     *                          or throw exceptions instead
     * @return true if the structure does not pass the filter or has issues that were reported to the reporter;
     *         false if the structure passes the criteria of the filter
     * @throws NullPointerException if the given IAtomContainer instance is null and issues do not get reported to the
     * reporter
     */
    protected abstract boolean isFiltered(IAtomContainer anAtomContainer, boolean aReportToReporter) throws NullPointerException;

}
