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

package de.unijena.cheminf.curation.processingSteps.filters.hasProperty;

import de.unijena.cheminf.curation.reporter.IReporter;
import de.unijena.cheminf.curation.reporter.MarkDownReporter;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 * Processing step to filter atom containers that not have a specific atom container property out of a set of atom
 * containers. To receive the atom containers of a set that have a specific property, see the counterpart of this
 * filter, the {@link HasPropertyFilter}.
 * <br>
 * This filter may be used to receive the entries of a dataset that miss a specific annotation.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 * @see HasPropertyFilter
 */
public class NotHasPropertyFilter extends HasPropertyFilter {

    /**
     * Constructor; calls the respective super passing the given reporter and property name string.
     *
     * @param aNameOfProperty the name of the property to check for
     * @param aReporter       the reporter that is to be used when processing sets of structures
     * @throws NullPointerException     if the given reporter or property name string is null
     * @throws IllegalArgumentException if the property name string is blank or empty
     * @see HasPropertyFilter (String, IReporter)
     */
    public NotHasPropertyFilter(String aNameOfProperty, IReporter aReporter) throws NullPointerException,
            IllegalArgumentException {
        super(aNameOfProperty, aReporter);
    }

    /**
     * Constructor; calls the respective super passing the given directory path and property name string. The super
     * initializes the reporter with an instance of {@link MarkDownReporter}.
     *
     * @param aNameOfProperty           the name of the property to check for
     * @param aReportFilesDirectoryPath the directory path for the MarkDownReporter to create the report files at
     * @throws NullPointerException     if the given directory path or property name string is null
     * @throws IllegalArgumentException if the given file path is no directory path; if the property name string is
     *                                  blank or empty
     * @see HasPropertyFilter (String, String)
     */
    public NotHasPropertyFilter(String aNameOfProperty, String aReportFilesDirectoryPath) throws NullPointerException,
            IllegalArgumentException {
        super(aNameOfProperty, aReportFilesDirectoryPath);
    }

    /**
     * Returns boolean value whether the given atom container does not possess a property with name {@link
     * #getNameOfProperty()}.
     *
     * @param anAtomContainer IAtomContainer instance to be checked
     * @return true if the property is existent
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public boolean isFiltered(IAtomContainer anAtomContainer) throws NullPointerException {
        return !super.isFiltered(anAtomContainer);
    }

}
