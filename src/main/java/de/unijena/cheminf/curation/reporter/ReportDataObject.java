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
 * Object that stores all data necessary for an entry in a report file.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public class ReportDataObject {

    /**
     * Atom container of the structure the report refers to.
     */
    private final IAtomContainer atomContainer;

    /**
     * Identifier string of the structure the report refers to.
     */
    private final String identifier;

    /**
     * Optional second identifier string of the structure the report refers to or null if there is none.
     */
    private final String optionalIdentifier;

    /**
     * String with the index of the processing step instance in which the reported problem occurred in the pipeline, or
     * null if the processing step is not part of a pipeline.
     */
    private final String indexOfProcessingStepInPipeline;

    /**
     * Runtime class of the IProcessingStep instance the reported problem occurred in.
     */
    private final Class<? extends IProcessingStep> classOfProcessingStep;

    /**
     * Error code of the reported problem.
     */
    private final ErrorCodes errorCode;

    /**
     * Constructor. Creates a new report data object without taking an atom container as parameter. This constructor
     * is ought to be used if the atom container the reported issue refers to is null.
     *
     * @param anIndexOfProcessingStepInPipeline string of the index of the processing step in the pipeline or null
     *                                          if it is not part of a pipeline
     * @param aClassOfProcessingStep runtime class of the IProcessingStep instance reporting the problem
     * @param anErrorCode error code of the reported problem
     * @throws NullPointerException if the given aClassOfProcessingStep or anErrorCode is null
     * @see #ReportDataObject(IAtomContainer, String, String, String, Class, ErrorCodes)
     */
    public ReportDataObject(
            String anIndexOfProcessingStepInPipeline,
            Class<? extends IProcessingStep> aClassOfProcessingStep,
            ErrorCodes anErrorCode
    ) throws NullPointerException {
        Objects.requireNonNull(aClassOfProcessingStep, "aClassOfProcessingStep (instance of Class<? extends" +
                " IProcessingStep>) is null.");
        Objects.requireNonNull(anErrorCode, "anErrorCode (instance of ErrorCodes) is null.");
        this.atomContainer = null;
        this.identifier = null;
        this.optionalIdentifier = null;
        this.indexOfProcessingStepInPipeline = anIndexOfProcessingStepInPipeline;
        this.classOfProcessingStep = aClassOfProcessingStep;
        this.errorCode = anErrorCode;
    }

    /**
     * Constructor. Creates a new report data object that stores all data needed for an entry in the report file. The
     * reported atom container may not be null.
     *
     * @param anAtomContainer atom container of the structure
     * @param anIdentifier identifier string of the structure
     * @param anOptionalIdentifier optional second identifier string of the structure or null if there is none
     * @param anIndexOfProcessingStepInPipeline string of the index of the processing step in the pipeline or null
     *                                          if it is not part of a pipeline
     * @param aClassOfProcessingStep runtime class of the IProcessingStep instance reporting the problem
     * @param anErrorCode error code of the reported problem
     * @throws NullPointerException if anAtomContainer, anIdentifier, aClassOfProcessingStep or anErrorCode is null
     * @see #ReportDataObject(String, Class, ErrorCodes)
     */
    public ReportDataObject(
            IAtomContainer anAtomContainer,
            String anIdentifier,
            String anOptionalIdentifier,
            String anIndexOfProcessingStepInPipeline,
            Class<? extends IProcessingStep> aClassOfProcessingStep,
            ErrorCodes anErrorCode
    ) throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of IAtomContainer) is null.");
        Objects.requireNonNull(anIdentifier, "anIdentifier (instance of String) is null.");
        Objects.requireNonNull(aClassOfProcessingStep, "aClassOfProcessingStep (instance of Class<? extends" +
                " IProcessingStep>) is null.");
        Objects.requireNonNull(anErrorCode, "anErrorCode (instance of ErrorCodes) is null.");
        this.atomContainer = anAtomContainer;
        this.identifier = anIdentifier;
        this.optionalIdentifier = anOptionalIdentifier;
        this.indexOfProcessingStepInPipeline = anIndexOfProcessingStepInPipeline;
        this.classOfProcessingStep = aClassOfProcessingStep;
        this.errorCode = anErrorCode;
    }

    /**
     * Returns the atom container of the structure the report refers to.
     *
     * @return atom container
     */
    public IAtomContainer getAtomContainer() {
        return atomContainer;
    }

    /**
     * Returns the identifier string of the structure the report refers to.
     *
     * @return String
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Returns the optional second identifier string of the structure the report refers to or null if none was given.
     *
     * @return integer value or null
     */
    public String getOptionalIdentifier() {
        return optionalIdentifier;
    }

    /**
     * Returns the index of the processing step in pipeline. This String may be null to state that the processing step
     * is not part of a pipeline.
     *
     * @return String instance or null
     */
    public String getIndexOfProcessingStepInPipeline() {
        return indexOfProcessingStepInPipeline;
    }

    /**
     * Returns the runtime class of the IProcessingStep instance the reported problem occurred in.
     *
     * @return instance of Class&lt;? extends IProcessingStep&gt;
     */
    public Class<? extends IProcessingStep> getClassOfProcessingStep() {
        return classOfProcessingStep;
    }

    /**
     * Returns the error code of the reported problem.
     *
     * @return ErrorCodes constant
     */
    public ErrorCodes getErrorCode() {
        return errorCode;
    }

}
