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

import de.unijena.cheminf.curation.processingSteps.BaseProcessingStep;
import de.unijena.cheminf.curation.reporter.IReporter;
import de.unijena.cheminf.curation.reporter.MarkDownReporter;
import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

import java.util.Objects;
import java.util.logging.Logger;

/**
 * Abstract class belonging to IFilter, the interface of processing steps that filter individual molecules from a
 * bigger set according to a defined molecular descriptor value. The abstract class reduces the number of abstract
 * methods to one.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 * @see IFilter
 */
public abstract class BaseFilter extends BaseProcessingStep implements IFilter {

    /**
     * Logger of this class.
     */
    private static final Logger LOGGER = Logger.getLogger(BaseFilter.class.getName());

    /**
     * Constructor; calls the super constructor with the given reporter and optional ID property name string. Since not
     * every filter might give the option to specify the optional ID property name in the constructor, this parameter
     * is allowed to be null.
     *
     * @param aReporter the reporter that is to be used when processing sets of structures
     * @param anOptionalIDPropertyName name string of the atom container property containing an optional second
     *                                 identifier (e.g. the name or CAS registry number) for each structure; if this
     *                                 field gets specified (not null), every atom container processed by this filter
     *                                 is expected to have a property with the respective name; the info is then
     *                                 included in the report
     * @throws NullPointerException if the given IReporter instance is null
     * @throws IllegalArgumentException if an optional ID property name is given, but it is blank or empty
     */
    public BaseFilter(IReporter aReporter, String anOptionalIDPropertyName) throws NullPointerException, IllegalArgumentException {
        super(aReporter, anOptionalIDPropertyName);
    }

    /**
     * Constructor; calls the super constructor that initializes the reporter with an instance of {@link
     * MarkDownReporter} - initialized with the given report files directory path. Since not every filter might give
     * the option to specify the optional ID property name in the constructor, this parameter is allowed to be null.
     *
     * @param aReportFilesDirectoryPath the directory path for the MarkDownReporter to create the report files at
     * @param anOptionalIDPropertyName name string of the atom container property containing an optional second
     *                                 identifier (e.g. the name or CAS registry number) for each structure; if this
     *                                 field gets specified (not null), every atom container processed by this filter
     *                                 is expected to have a property with the respective name; the info is then
     *                                 included in the report
     * @throws NullPointerException if the given String with the directory path is null
     * @throws IllegalArgumentException if the given file path is no directory path; if a property name string is given,
     *                                  but it is blank or empty
     */
    public BaseFilter(String aReportFilesDirectoryPath, String anOptionalIDPropertyName) {
        super(aReportFilesDirectoryPath, anOptionalIDPropertyName);
    }

    /**
     * Filters the atom containers of the given atom container set according to the values returned by {@link
     * #isFiltered(IAtomContainer)}. Returns all those atom containers that meet the filter criterion.
     *
     * @return the set of all atom containers that meet the filter criterion
     */
    @Override
    protected IAtomContainerSet applyLogic(IAtomContainerSet anAtomContainerSet) throws NullPointerException, Exception {
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of IAtomContainerSet) is null.");
        final IAtomContainerSet tmpFilteredACSet = new AtomContainerSet();
        for (IAtomContainer tmpAtomContainer : anAtomContainerSet.atomContainers()) {
            try {
                //check whether the atom container meets the filter criterion; the structure passes the filter
                if (!this.isFiltered(tmpAtomContainer)) {
                    tmpFilteredACSet.addAtomContainer(tmpAtomContainer);
                }
            } catch (Exception anException) {
                //appends report to the reporter; the structure does not pass the filter
                this.reportIssue(tmpAtomContainer, anException);
            }
        }
        return tmpFilteredACSet;
    }

    /**
     * Handles the given exception by appending a report to the reporter; re-throws the exception, if it is considered
     * as fatal. Most implementations expect non-fatal exceptions to have the name of an ErrorCodes enum constant as
     * message string.
     *
     * @param anAtomContainer the atom container the issue refers to
     * @param anException the thrown exception
     * @throws NullPointerException if the given atom container (if it is not null) does not possess a MolID
     * @throws Exception if the given exception is considered as fatal
     */
    protected abstract void reportIssue(IAtomContainer anAtomContainer, Exception anException) throws NullPointerException, Exception;

}
