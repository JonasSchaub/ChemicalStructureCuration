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

package de.unijena.cheminf.curation.reporter;

import de.unijena.cheminf.curation.enums.ErrorCodes;
import de.unijena.cheminf.curation.processingSteps.IProcessingStep;
import org.openscience.cdk.interfaces.IAtomContainer;

import java.util.Objects;

/**
 * Object that stores all data necessary for reporting an issue encountered by an {@link IProcessingStep} instance.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public class ReportDataObject {

    //<editor-fold desc="static class variable" defaultstate="collapsed">
    /**
     * Array of {@link ErrorCodes} that are allowed to be reported without an atom container. The array may be modified
     * and provided with additional error codes if necessary.
     */
    public static ErrorCodes[] ERROR_CODES_ALLOWED_WITH_NO_ATOM_CONTAINER = {
            ErrorCodes.ATOM_CONTAINER_NULL_ERROR,
            ErrorCodes.SDF_IMPORT_FAILED_ERROR,
            ErrorCodes.UNEXPECTED_EXCEPTION_ERROR
    };
    //</editor-fold>

    //<editor-fold desc="private variables" defaultstate="collapsed">
    /**
     * Atom container of the structure the report refers to.
     */
    private IAtomContainer atomContainer = null;

    /**
     * Identifier string of the structure the report refers to.
     */
    private String identifier = null;

    /**
     * Second, external identifier string of the structure the report refers to or null if there is none.
     */
    private String externalIdentifier = null;

    /**
     * String with the identifier of the processing step instance the reported problem occurred in, or null, if the
     * processing step has no ID (as it might be the case if it is not part of a pipeline).
     */
    private String processingStepIdentifier = null;
    //</editor-fold>

    //<editor-fold desc="private final variables" defaultstate="collapsed">
    /**
     * Runtime class of the IProcessingStep instance the reported problem occurred in.
     */
    private final Class<? extends IProcessingStep> classOfProcessingStep;

    /**
     * Error code of the reported problem.
     */
    private final ErrorCodes errorCode;
    //</editor-fold>

    //<editor-fold desc="Constructors" defaultstate="collapsed">
    //<editor-fold desc="ReportDataObject(ErrorCodes, Class)" defaultstate="collapsed">
    /**
     * Constructor; initializes the report data object with an error code and a class of processing step. The given
     * error code needs to be listed in the {@link #ERROR_CODES_ALLOWED_WITH_NO_ATOM_CONTAINER} array for this
     * constructor to be used; otherwise, the report data object is expected to be initialized with the atom container
     * instance belonging to the reported issue.
     *
     * @param anErrorCode               error code of the reported issue
     * @param aClassOfProcessingStep    runtime class of the IProcessingStep instance reporting the issue
     * @throws NullPointerException     if any of the given parameters is null
     * @throws IllegalArgumentException if the reported error code is not listed in the {@link
     *                                  #ERROR_CODES_ALLOWED_WITH_NO_ATOM_CONTAINER} array
     */
    public ReportDataObject(
            ErrorCodes anErrorCode,
            Class<? extends IProcessingStep> aClassOfProcessingStep
    ) throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(anErrorCode, "anErrorCode (instance of ErrorCodes) is null.");
        Objects.requireNonNull(aClassOfProcessingStep, "aClassOfProcessingStep (instance of Class<? extends" +
                " IProcessingStep>) is null.");
        //<editor-fold desc="check of the error code" defaultstate="collapsed">
        boolean tmpIsAllowed = false;
        for (ErrorCodes tmpErrorCode : ReportDataObject.ERROR_CODES_ALLOWED_WITH_NO_ATOM_CONTAINER) {
            if (anErrorCode == tmpErrorCode) {
                tmpIsAllowed = true;
                break;
            }
        }
        if (!tmpIsAllowed) {
            throw new IllegalArgumentException("The given error code is not allowed to be reported without the" +
                    " respective atom container.");
        }
        //</editor-fold>
        this.errorCode = anErrorCode;
        this.classOfProcessingStep = aClassOfProcessingStep;
    }
    //</editor-fold>

    //<editor-fold desc="ReportDataObject(ErrorCodes, Class, String)" defaultstate="collapsed">
    /**
     * Constructor; initializes the report data object with an error code, a class of processing step and the
     * identifier of the processing step. The given error code needs to be listed in the {@link
     * #ERROR_CODES_ALLOWED_WITH_NO_ATOM_CONTAINER} array for this constructor to be used; otherwise, the report data
     * object is expected to be initialized with the atom container instance belonging to the reported issue.
     *
     * @param anErrorCode               error code of the reported issue
     * @param aClassOfProcessingStep    runtime class of the IProcessingStep instance reporting the issue
     * @param aProcessingStepIdentifier identifier string of the processing step (probably with the index the step has
     *                                  in a pipeline it is part of)
     * @throws NullPointerException     if any of the given parameters is null
     * @throws IllegalArgumentException if the reported error code is not listed in the {@link
     *                                  #ERROR_CODES_ALLOWED_WITH_NO_ATOM_CONTAINER} array
     */
    public ReportDataObject(
            ErrorCodes anErrorCode,
            Class<? extends IProcessingStep> aClassOfProcessingStep,
            String aProcessingStepIdentifier
    ) throws NullPointerException, IllegalArgumentException {
        this(anErrorCode, aClassOfProcessingStep);
        Objects.requireNonNull(aProcessingStepIdentifier, "aProcessingStepIdentifier (instance of String)" +
                " is null.");
        if (aProcessingStepIdentifier.isBlank()) {
            throw new IllegalArgumentException("aProcessingStepIdentifier (instance of String) is empty or blank.");
        }
        this.processingStepIdentifier = aProcessingStepIdentifier;
    }
    //</editor-fold>

    //<editor-fold desc="ReportDataObject(ErrorCodes, Class, IAtomContainer, String)" defaultstate="collapsed">
    /**
     * Constructor; initializes the report data object with an error code, a class of processing step, atom container
     * and identifier.
     *
     * @param anErrorCode            error code of the reported issue
     * @param aClassOfProcessingStep runtime class of the IProcessingStep instance reporting the issue
     * @param anAtomContainer        atom container of the structure the reported issue refers to
     * @param anIdentifier           identifier string of the structure
     * @throws NullPointerException if any of the given parameters is null
     * @throws IllegalArgumentException if the identifier string is empty or blank
     */
    public ReportDataObject(
            ErrorCodes anErrorCode,
            Class<? extends IProcessingStep> aClassOfProcessingStep,
            IAtomContainer anAtomContainer,
            String anIdentifier
    ) throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(anErrorCode, "anErrorCode (instance of ErrorCodes) is null.");
        Objects.requireNonNull(aClassOfProcessingStep, "aClassOfProcessingStep (instance of Class<? extends" +
                " IProcessingStep>) is null.");
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of IAtomContainer) is null.");
        Objects.requireNonNull(anIdentifier, "anIdentifier (instance of String) is null.");
        if (anIdentifier.isBlank()) {
            throw new IllegalArgumentException("anIdentifier (instance of String) is empty or blank.");
        }
        this.errorCode = anErrorCode;
        this.classOfProcessingStep = aClassOfProcessingStep;
        this.atomContainer = anAtomContainer;
        this.identifier = anIdentifier;
    }
    //</editor-fold>

    //<editor-fold desc="ReportDataObject(ErrorCodes, Class, String, IAtomContainer, String)" defaultstate="collapsed">
    /**
     * Constructor; initializes the report data object with error code, class of processing step, identifier of
     * processing step, atom container and identifier.
     *
     * @param anErrorCode               error code of the reported issue
     * @param aClassOfProcessingStep    runtime class of the IProcessingStep instance reporting the issue
     * @param aProcessingStepIdentifier identifier string of the processing step (probably with the index the step has
     *                                  in a pipeline it is part of)
     * @param anAtomContainer           atom container of the structure the reported issue refers to
     * @param anIdentifier              identifier string of the structure
     * @throws NullPointerException if any of the given parameters is null
     * @throws IllegalArgumentException if an identifier string is empty or blank
     */
    public ReportDataObject(
            ErrorCodes anErrorCode,
            Class<? extends IProcessingStep> aClassOfProcessingStep,
            String aProcessingStepIdentifier,
            IAtomContainer anAtomContainer,
            String anIdentifier
    ) throws NullPointerException, IllegalArgumentException {
        this(anErrorCode, aClassOfProcessingStep, anAtomContainer, anIdentifier);
        Objects.requireNonNull(aProcessingStepIdentifier, "aProcessingStepIdentifier (instance of String)" +
                " is null.");
        if (aProcessingStepIdentifier.isBlank()) {
            throw new IllegalArgumentException("aProcessingStepIdentifier (instance of String) is empty or blank.");
        }
        this.processingStepIdentifier = aProcessingStepIdentifier;
    }
    //</editor-fold>

    //<editor-fold desc="ReportDataObject(ErrorCodes, Class, IAtomContainer, String, String)" defaultstate="collapsed">
    /**
     * Constructor; initializes the report data object with an error code, a class of processing step, atom container,
     * identifier and external identifier.
     *
     * @param anErrorCode               error code of the reported issue
     * @param aClassOfProcessingStep    runtime class of the IProcessingStep instance reporting the issue
     * @param anAtomContainer           atom container of the structure the reported issue refers to
     * @param anIdentifier              identifier string of the structure
     * @param anExternalIdentifier      second, external identifier string of the structure
     * @throws NullPointerException if any of the given parameters is null
     * @throws IllegalArgumentException if an identifier string is empty or blank
     */
    public ReportDataObject(
            ErrorCodes anErrorCode,
            Class<? extends IProcessingStep> aClassOfProcessingStep,
            IAtomContainer anAtomContainer,
            String anIdentifier,
            String anExternalIdentifier
    ) throws NullPointerException, IllegalArgumentException {
        this(anErrorCode, aClassOfProcessingStep, anAtomContainer, anIdentifier);
        Objects.requireNonNull(anExternalIdentifier, "anExternalIdentifier (instance of String) is null.");
        if (anExternalIdentifier.isBlank()) {
            throw new IllegalArgumentException("anExternalIdentifier (instance of String) is empty or blank.");
        }
        this.externalIdentifier = anExternalIdentifier;
    }
    //</editor-fold>

    //<editor-fold desc="ReportDataObject(ErrorCodes, Class, String, IAtomContainer, String, String)" defaultstate="collapsed">
    /**
     * Constructor; initializes the report data object with error code, class of processing step, identifier of
     * processing step, atom container, identifier and external identifier.
     *
     * @param anErrorCode               error code of the reported issue
     * @param aClassOfProcessingStep    runtime class of the IProcessingStep instance reporting the issue
     * @param aProcessingStepIdentifier identifier string of the processing step (probably with the index the step has
     *                                  in a pipeline it is part of)
     * @param anAtomContainer           atom container of the structure the reported issue refers to
     * @param anIdentifier              identifier string of the structure
     * @param anExternalIdentifier      second, external identifier string of the structure
     * @throws NullPointerException if any of the given parameters is null
     * @throws IllegalArgumentException if an identifier string is empty or blank
     */
    public ReportDataObject(
            ErrorCodes anErrorCode,
            Class<? extends IProcessingStep> aClassOfProcessingStep,
            String aProcessingStepIdentifier,
            IAtomContainer anAtomContainer,
            String anIdentifier,
            String anExternalIdentifier
    ) throws NullPointerException, IllegalArgumentException {
        this(anErrorCode, aClassOfProcessingStep, anAtomContainer, anIdentifier, anExternalIdentifier);
        Objects.requireNonNull(aProcessingStepIdentifier, "aProcessingStepIdentifier (instance of String)" +
                " is null.");
        if (aProcessingStepIdentifier.isBlank()) {
            throw new IllegalArgumentException("aProcessingStepIdentifier (instance of String) is empty or blank.");
        }
        this.processingStepIdentifier = aProcessingStepIdentifier;
    }
    //</editor-fold>

    //<editor-fold desc="ReportDataObject(ErrorCodes, Class, String, String)" defaultstate="collapsed">
    /**
     * Constructor to be used when there is no info on the structure (no atom container) but an identifier is known.
     * Initializes the report data object with error code, class of processing step, identifier of processing step and
     * the identifier of the structure the issue refers to. The reported error code needs to be listed in the {@link
     * #ERROR_CODES_ALLOWED_WITH_NO_ATOM_CONTAINER} array for this constructor to be used. The identifier of the
     * processing step is optional.
     *
     * @param anErrorCode               error code of the reported issue
     * @param aClassOfProcessingStep    runtime class of the IProcessingStep instance reporting the issue
     * @param aProcessingStepIdentifier identifier string of the processing step (probably with the index the step has
     *                                  in a pipeline it is part of)
     * @param anIdentifier              identifier string of the structure
     * @throws NullPointerException if any of the given parameters is null (except the processing step identifier)
     * @throws IllegalArgumentException if an identifier string is empty or blank; if the reported error code is not
     *                                  listed in the {@link #ERROR_CODES_ALLOWED_WITH_NO_ATOM_CONTAINER} array
     */
    public ReportDataObject(
            ErrorCodes anErrorCode,
            Class<? extends IProcessingStep> aClassOfProcessingStep,
            String aProcessingStepIdentifier,
            String anIdentifier
    ) throws NullPointerException, IllegalArgumentException {
        // not using the constructor with processing step identifier param since it might be null
        this(anErrorCode, aClassOfProcessingStep);
        if (aProcessingStepIdentifier != null && aProcessingStepIdentifier.isBlank()) {
            throw new IllegalArgumentException("aProcessingStepIdentifier (instance of String) is empty or blank.");
        }
        this.processingStepIdentifier = aProcessingStepIdentifier;
        Objects.requireNonNull(anIdentifier, "anIdentifier (instance of String) is null.");
        if (anIdentifier.isBlank()) {
            throw new IllegalArgumentException("anIdentifier (instance of String) is empty or blank.");
        }
        this.identifier = anIdentifier;
    }
    //</editor-fold>
    //</editor-fold>

    //<editor-fold desc="public properties" defaultstate="collapsed">
    /**
     * Returns the atom container of the structure the report refers to.
     *
     * @return atom container
     */
    public IAtomContainer getAtomContainer() {
        return this.atomContainer;
    }

    /**
     * Returns the identifier string of the structure the report refers to.
     *
     * @return String
     */
    public String getIdentifier() {
        return this.identifier;
    }

    /**
     * Returns the second, external identifier string of the structure the report refers to or null if none was given.
     *
     * @return integer value or null
     */
    public String getExternalIdentifier() {
        return this.externalIdentifier;
    }

    /**
     * Returns the identifier of the processing step instance the reported problem occurred in, or null, if the
     * processing step has no ID (as it might be the case if it is not part of a pipeline).
     *
     * @return String instance or null
     */
    public String getProcessingStepIdentifier() {
        return this.processingStepIdentifier;
    }

    /**
     * Returns the runtime class of the IProcessingStep instance the reported problem occurred in.
     *
     * @return instance of Class&lt;? extends IProcessingStep&gt;
     */
    public Class<? extends IProcessingStep> getClassOfProcessingStep() {
        return this.classOfProcessingStep;
    }

    /**
     * Returns the error code of the reported problem.
     *
     * @return ErrorCodes constant
     */
    public ErrorCodes getErrorCode() {
        return this.errorCode;
    }
    //</editor-fold>

}
