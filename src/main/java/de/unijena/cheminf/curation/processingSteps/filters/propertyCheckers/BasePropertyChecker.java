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

package de.unijena.cheminf.curation.processingSteps.filters.propertyCheckers;

import de.unijena.cheminf.curation.enums.ErrorCodes;
import de.unijena.cheminf.curation.processingSteps.filters.BaseFilter;
import de.unijena.cheminf.curation.reporter.IReporter;
import de.unijena.cheminf.curation.reporter.MarkDownReporter;
import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

import java.util.Objects;

/**
 * Base class of all classes that check all atom containers of a given atom container set for the existence of a
 * specific property. If this is not the case, this is reported and respective atom containers removed from the
 * returned atom container set.
 *
 * @implNote Note that the constant {@link #errorCode} needs to be adjusted for every single child of this class.
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public class BasePropertyChecker extends BaseFilter {

    /**
     * Name string of the atom container property to check for.
     */
    private final String nameOfProperty;

    /**
     * The error code that is to be used if the atom container property is not present.
     */
    private final ErrorCodes errorCode;

    /**
     * Main constructor; calls the super constructor with the given reporter and no optional ID property name string.
     *
     * @param aReporter the reporter that is to be used when processing sets of structures
     * @param aNameOfProperty the name of the property to check for
     * @param anErrorCode the error code belonging to the missing of the property
     * @throws NullPointerException if the given reporter, property name or error code is null
     * @throws IllegalArgumentException if the property name string is blank or empty
     */
    public BasePropertyChecker(IReporter aReporter, String aNameOfProperty, ErrorCodes anErrorCode)
            throws NullPointerException, IllegalArgumentException {
        super(aReporter, null);
        Objects.requireNonNull(aNameOfProperty, "aNameOfProperty (instance of String) is null.");
        Objects.requireNonNull(anErrorCode, "anErrorCode (ErrorCodes constant) is null.");
        if (aNameOfProperty.isBlank()) {
            throw new IllegalArgumentException("aNameOfProperty (instance of String) is empty or blank.");
        }
        this.nameOfProperty = aNameOfProperty;
        this.errorCode = anErrorCode;
    }

    /**
     * Constructor; calls the main constructor with an instance of {@link MarkDownReporter} - initialized with the given
     * report files directory path -, the property name and the error code.
     *
     * @param aReportFilesDirectoryPath the directory path for the MarkDownReporter to create the report files at
     * @param aNameOfProperty the name of the property to check for
     * @param anErrorCode the error code belonging to the missing of the property
     * @throws NullPointerException if the given directory path, property name or error code is null
     * @throws IllegalArgumentException if the given file path is no directory path; if the property name string is
     *                                  blank or empty
     */
    public BasePropertyChecker(String aReportFilesDirectoryPath, String aNameOfProperty, ErrorCodes anErrorCode)
            throws NullPointerException, IllegalArgumentException {
        //TODO: the MarkDownReporter needs a constructor that I can pass the file path to; check for not null;
        // check whether it is a directory path
        this(new MarkDownReporter(), aNameOfProperty, anErrorCode);
    }

    /**
     * Returns only those atom containers that have the atom container property the respective class is checking for.
     * Appends a report to the reporter for all the atom containers that do not have this property.
     */
    @Override
    public IAtomContainerSet process(IAtomContainerSet anAtomContainerSet, boolean aCloneBeforeProcessing,
                                     boolean anAssignIdentifiers) throws Exception {
        return super.process(anAtomContainerSet, aCloneBeforeProcessing, anAssignIdentifiers);
    }

    /**
     * Returns boolean value whether the given atom container has a property with name {@link #nameOfProperty}.
     *
     * @param anAtomContainer IAtomContainer instance to be checked
     * @return true if the property is not set
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public boolean isFiltered(IAtomContainer anAtomContainer) throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, ErrorCodes.ATOM_CONTAINER_NULL_ERROR.name());
        return (anAtomContainer.getProperty(this.nameOfProperty) == null);
    }

    /**
     * Returns only those atom containers that have the respective atom container property (property is not null) and
     * reports all the other atom containers with a respective error code to the reporter.
     *
     * @return the set of all atom containers that have the respective atom container property
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
                } else {
                    this.appendToReporter(this.errorCode, tmpAtomContainer);
                }
            } catch (Exception anException) {
                //appends report to the reporter; the structure does not pass the filter
                this.reportIssue(tmpAtomContainer, anException);
            }
        }
        return tmpFilteredACSet;
    }

    /**
     * Handles the given exception by appending a report to the reporter; if the message string of the given exception
     * does not match the name of {@link ErrorCodes#ATOM_CONTAINER_NULL_ERROR}, the exception is considered as fatal and
     * re-thrown.
     *
     * @param anAtomContainer the atom container the issue refers to
     * @param anException the thrown exception
     * @throws Exception if the message string of the given exception does not match the name of {@link
     *                   ErrorCodes#ATOM_CONTAINER_NULL_ERROR}
     */
    @Override
    protected void reportIssue(IAtomContainer anAtomContainer, Exception anException) throws Exception {
        String tmpExceptionMessageString = anException.getMessage();
        ErrorCodes tmpErrorCode;
        try {
            // try to get the respective constant of the ErrorCodes enum
            tmpErrorCode = ErrorCodes.valueOf(tmpExceptionMessageString);
            if (tmpErrorCode == ErrorCodes.ATOM_CONTAINER_NULL_ERROR) {
                // not fatal
                this.appendToReporter(tmpErrorCode, anAtomContainer);
                return;
            }
        } catch (Exception aFatalException) {
            tmpErrorCode = ErrorCodes.UNEXPECTED_EXCEPTION_ERROR;
        }
        // exception is considered as fatal; append to the reporter and re-throw the exception
        this.appendToReporter(tmpErrorCode, anAtomContainer);
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

}
