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

import de.unijena.cheminf.curation.enums.ErrorCodes;
import de.unijena.cheminf.curation.processingSteps.IProcessingStep;
import de.unijena.cheminf.curation.processingSteps.filters.BaseFilter;
import de.unijena.cheminf.curation.processingSteps.filters.IFilter;
import de.unijena.cheminf.curation.reporter.IReporter;
import de.unijena.cheminf.curation.reporter.MarkDownReporter;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

import java.util.Objects;

/**
 * Processing step to filter atom containers that have a specific atom container property out of a set of atom
 * containers. To receive the atom containers of a set that miss a specific property, see the counterpart of this
 * filter, the {@link NotHasPropertyFilter}.
 * <br>
 * This filter may be used to guarantee uniform annotations for all elements of a dataset.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 * @see NotHasPropertyFilter
 */
public class HasPropertyFilter extends BaseFilter {

    /**
     * Name string of the atom container property to check for.
     */
    private String nameOfProperty;

    /**
     * Constructor; initializes the filter by calling the super constructor with the given reporter instance and
     * sets the name of the property to check for. The external ID property name filed is not specified.
     *
     * @param aNameOfProperty the name of the property to check for
     * @param aReporter       the reporter that is to be used when processing sets of structures
     * @throws NullPointerException     if the given reporter or property name string is null
     * @throws IllegalArgumentException if the property name string is blank or empty
     * @see BaseFilter(IReporter, String)
     */
    public HasPropertyFilter(String aNameOfProperty, IReporter aReporter) throws NullPointerException,
            IllegalArgumentException {
        super(aReporter, null);
        this.setNameOfProperty(aNameOfProperty);
    }

    /**
     * Constructor; initializes the filter with an instance of {@link MarkDownReporter} as reporter by calling the
     * respective super constructor and sets the name of the property to check for. The external ID property name filed
     * is not specified.
     *
     * @param aNameOfProperty           the name of the property to check for
     * @param aReportFilesDirectoryPath the directory path for the MarkDownReporter to create the report files at
     * @throws NullPointerException     if the given directory path or property name string is null
     * @throws IllegalArgumentException if the given file path is no directory path; if the property name string is
     *                                  blank or empty
     * @see BaseFilter(String, String)
     */
    public HasPropertyFilter(String aNameOfProperty, String aReportFilesDirectoryPath)
            throws NullPointerException, IllegalArgumentException {
        super(aReportFilesDirectoryPath, null);
        this.setNameOfProperty(aNameOfProperty);
    }

    /**
     * Returns the set of those atom containers that have the atom container property that is checking for (see {@link
     * #nameOfProperty}). Atom containers that have the property unset are excluded from the returned set.
     * <br>
     * See the method description in the {@link IProcessingStep} and {@link IFilter} interface for further details on
     * the method.
     *
     * @see IProcessingStep#process(IAtomContainerSet, boolean)
     */
    @Override
    public IAtomContainerSet process(IAtomContainerSet anAtomContainerSet, boolean aCloneBeforeProcessing)
            throws Exception {
        return super.process(anAtomContainerSet, aCloneBeforeProcessing);
    }

    /**
     * Returns boolean value whether the given atom container has a property with name {@link #nameOfProperty}.
     *
     * @param anAtomContainer IAtomContainer instance to be checked
     * @return true if the property is unset
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public boolean isFiltered(IAtomContainer anAtomContainer) throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, ErrorCodes.ATOM_CONTAINER_NULL_ERROR.name());
        return (anAtomContainer.getProperty(this.nameOfProperty) == null);
    }

    /**
     * Handles the given exception by appending a report to the reporter; if the message string of the given exception
     * does not match the name of {@link ErrorCodes#ATOM_CONTAINER_NULL_ERROR}, the exception is considered as fatal and
     * re-thrown.
     *
     * @param anAtomContainer the atom container the issue refers to
     * @param anException     the thrown exception
     * @throws Exception if the message string of the given exception does not match the name of {@link
     *                   ErrorCodes#ATOM_CONTAINER_NULL_ERROR}
     */
    @Override
    protected void reportIssue(IAtomContainer anAtomContainer, Exception anException) throws NullPointerException,
            Exception {
        String tmpExceptionMessageString = anException.getMessage();
        ErrorCodes tmpErrorCode;
        try {
            // try to get the respective constant of the ErrorCodes enum
            tmpErrorCode = ErrorCodes.valueOf(tmpExceptionMessageString);
            if (tmpErrorCode == ErrorCodes.ATOM_CONTAINER_NULL_ERROR) {
                // not fatal
                this.appendToReport(tmpErrorCode, anAtomContainer);
                return;
            }
        } catch (Exception aFatalException) {
            tmpErrorCode = ErrorCodes.UNEXPECTED_EXCEPTION_ERROR;
        }
        // exception is considered as fatal; append to the reporter and re-throw the exception
        this.appendToReport(tmpErrorCode, anAtomContainer);
        throw anException;
    }

    /**
     * Returns the name of the atom container property for which existence is checked for.
     *
     * @return String instance
     */
    public String getNameOfProperty() {
        return this.nameOfProperty;
    }

    /**
     * Sets the name of the atom container property the existence of which is to check for to the given string.
     *
     * @param aNameOfProperty the name of the property to check for
     * @throws NullPointerException     if the given property name string is null
     * @throws IllegalArgumentException if the property name string is blank or empty
     */
    protected void setNameOfProperty(String aNameOfProperty) throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(aNameOfProperty, "aNameOfProperty (instance of String) is null.");
        if (aNameOfProperty.isBlank()) {
            throw new IllegalArgumentException("aNameOfProperty (instance of String) is empty or blank.");
        }
        this.nameOfProperty = aNameOfProperty;
    }

}
