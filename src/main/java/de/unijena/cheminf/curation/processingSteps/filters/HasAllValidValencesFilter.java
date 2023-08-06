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

import de.unijena.cheminf.curation.enums.ErrorCodes;
import de.unijena.cheminf.curation.reporter.IReporter;
import de.unijena.cheminf.curation.reporter.MarkDownReporter;
import de.unijena.cheminf.curation.utils.FilterUtils;
import de.unijena.cheminf.curation.valenceListContainers.IValenceModel;
import de.unijena.cheminf.curation.valenceListContainers.PubChemValenceModel;
import org.openscience.cdk.interfaces.IAtomContainer;

import java.util.Objects;

/**
 * Has all valid valences filter to filter atom containers with all valid valences out of a set of atom containers. The
 * valences of the atoms are checked based on a valence model which is by default the {@link PubChemValenceModel}.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 * @see IValenceModel
 */
public class HasAllValidValencesFilter extends BaseFilter {

    /**
     * The valence model based on which the validity of the valence of atoms is checked on.
     */
    private final IValenceModel valenceModel;

    /**
     * Boolean value whether to generally consider atoms with wildcard atomic number (zero) as having a valid valence.
     */
    private final boolean wildcardAtomicNumberIsValid;

    //<editor-fold desc="Constructors" defaultstate="collapsed">
    /**
     * Constructor; initializes a {@link HasAllValidValencesFilter} with the given valence model and reporter and
     * whether to generally consider atoms with wildcard atomic number as having a valid valence.
     *
     * @param aValenceModel                the valence model to check the valences for their validity with
     * @param aWildcardAtomicNumberIsValid boolean value whether to generally consider atoms with wildcard atomic number
     *                                     (zero) as having a valid valence
     * @param aReporter                    the reporter to do the reporting with
     * @throws NullPointerException if the given valence model or reporter is null
     */
    public HasAllValidValencesFilter(IValenceModel aValenceModel, boolean aWildcardAtomicNumberIsValid,
                                     IReporter aReporter) throws NullPointerException {
        super(aReporter, null);
        this.valenceModel = aValenceModel;
        this.wildcardAtomicNumberIsValid = aWildcardAtomicNumberIsValid;
    }

    /**
     * Constructor; calls {@link #HasAllValidValencesFilter(IValenceModel, boolean, IReporter)} with an instance of
     * {@link PubChemValenceModel} as valence model.
     *
     * @param aWildcardAtomicNumberIsValid boolean value whether to generally consider atoms with wildcard atomic number
     *                                     (zero) as having a valid valence
     * @param aReporter                    the reporter to do the reporting with
     * @throws NullPointerException if the given reporter is null
     */
    public HasAllValidValencesFilter(boolean aWildcardAtomicNumberIsValid, IReporter aReporter)
            throws NullPointerException {
        this(new PubChemValenceModel(), aWildcardAtomicNumberIsValid, aReporter);
    }

    /**
     * Constructor; initializes a {@link HasAllValidValencesFilter} with an instance of {@link MarkDownReporter}
     * (initialized with the given directory path), the given valence model and reporter and whether to generally
     * consider atoms with wildcard atomic number as having a valid valence.
     *
     * @param aValenceModel                the valence model to check the valences for their validity with
     * @param aWildcardAtomicNumberIsValid boolean value whether to generally consider atoms with wildcard atomic number
     *                                     (zero) as having a valid valence
     * @param aReportFilesDirectoryPath    the directory path for the MarkDownReporter to create the report files at
     * @throws NullPointerException if the valence model or the directory path string is null
     * @throws IllegalArgumentException if the given file path is no directory path
     */
    public HasAllValidValencesFilter(IValenceModel aValenceModel, boolean aWildcardAtomicNumberIsValid,
                                     String aReportFilesDirectoryPath)
            throws NullPointerException, IllegalArgumentException {
        super(aReportFilesDirectoryPath, null);
        this.valenceModel = aValenceModel;
        this.wildcardAtomicNumberIsValid = aWildcardAtomicNumberIsValid;
    }

    /**
     * Constructor; calls {@link #HasAllValidValencesFilter(IValenceModel, boolean, String)} with an instance of {@link
     * PubChemValenceModel} as valence model.
     *
     * @param aWildcardAtomicNumberIsValid boolean value whether to generally consider atoms with wildcard atomic number
     *                                     (zero) as having a valid valence
     * @param aReportFilesDirectoryPath    the directory path for the MarkDownReporter to create the report files at
     * @throws NullPointerException if the directory path string is null
     * @throws IllegalArgumentException if the given file path is no directory path
     */
    public HasAllValidValencesFilter(boolean aWildcardAtomicNumberIsValid, String aReportFilesDirectoryPath)
            throws NullPointerException, IllegalArgumentException {
        this(new PubChemValenceModel(), aWildcardAtomicNumberIsValid, aReportFilesDirectoryPath);
    }
    //</editor-fold>

     /**
     * {@inheritDoc}
     * <br>
     * The class field considerWildcardAsValid decides whether atoms with atomic number zero are generally considered as
     * having a valid valence. Otherwise the wildcard atomic number is considered as invalid if not covered by the
     * valence model.
     *
     * @throws NullPointerException if the given atom container or an atom contained by it is null; if atomic number,
     *                              formal charge or the implicit hydrogen count of an atom is null; if the bond order
     *                              of a bond is null
     * @throws IllegalArgumentException if the bond order of a bond is IBond.Order.UNSET
     */
    @Override
    public boolean isFiltered(IAtomContainer anAtomContainer) throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(anAtomContainer, ErrorCodes.ATOM_CONTAINER_NULL_ERROR.name());
        //
        return FilterUtils.hasAllValidValences(anAtomContainer, this.wildcardAtomicNumberIsValid, this.valenceModel);
    }

    @Override
    protected void reportIssue(IAtomContainer anAtomContainer, Exception anException) throws NullPointerException,
            Exception {
        String tmpExceptionMessageString = anException.getMessage();
        ErrorCodes tmpErrorCode;
        try {
            // the message of the exception is expected to match the name of an ErrorCodes enum's constant
            tmpErrorCode = ErrorCodes.valueOf(tmpExceptionMessageString);
        } catch (Exception aFatalException) {
            /*
             * the message string of the given exception did not match the name of an ErrorCodes enum's constant; the
             * exception is considered as fatal and re-thrown
             */
            this.appendToReport(ErrorCodes.UNEXPECTED_EXCEPTION_ERROR, anAtomContainer);
            throw anException;
        }
        this.appendToReport(tmpErrorCode, anAtomContainer);
    }

    /**
     * Returns the valence model based on which the validity of the valence of atoms is checked on.
     *
     * @return IValenceModel instance
     */
    public IValenceModel getValenceModel() {
        return this.valenceModel;
    }

    /**
     * Returns whether atoms with wildcard atomic number (zero) are generally considered as having a valid valence.
     *
     * @return boolean value
     */
    public boolean isWildcardAtomicNumberIsValid() {
        return this.wildcardAtomicNumberIsValid;
    }

}
