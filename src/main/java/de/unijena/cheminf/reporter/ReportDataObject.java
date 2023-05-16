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

package de.unijena.cheminf.reporter;

import org.openscience.cdk.interfaces.IAtomContainer;

/**
 * Object that stores all data necessary for an entry in a report file.
 */
public class ReportDataObject {

    /*
    TODO: choose a more specific class name?
    TODO: implement an IReport interface?
    //
    TODO: parameter checks?
     */

    /**
     * Atom container of the structure the report refers to.
     */
    private final IAtomContainer atomContainer;

    /**
     * Integer identifier of the structure the report refers to.
     */
    private final int identifier;

    /**
     * Optional second integer identifier of the structure the report refers to or null if there is none.
     */
    private final Integer optionalIdentifier;

    /**
     * String description of the processing step the reported problem occurred in.
     */
    private final String processingStepDescription;

    /**
     * String description of the reported problem.
     */
    private final String errorDescription;

    /**
     * Constructor. Initializes all class variables.
     *
     * @param anAtomContainer atom container of the structure
     * @param anIdentifier integer identifier of the structure
     * @param anOptionalIdentifier optional second integer identifier of the structure or null if there is none
     * @param aProcessingStepDescription string describing the processing step the reported problem occurred in
     * @param anErrorDescription string description of the reported problem
     */
    public ReportDataObject(IAtomContainer anAtomContainer, int anIdentifier, Integer anOptionalIdentifier,
                            String aProcessingStepDescription, String anErrorDescription) {
        this.atomContainer = anAtomContainer;
        this.identifier = anIdentifier;
        this.optionalIdentifier = anOptionalIdentifier;
        this.processingStepDescription = aProcessingStepDescription;
        this.errorDescription = anErrorDescription;
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
     * Returns the integer identifier of the structure the report refers to.
     *
     * @return integer value
     */
    public int getIdentifier() {
        return identifier;
    }

    /**
     * Returns the optional second integer identifier of the structure the report refers to or null if none was given.
     *
     * @return integer value or null
     */
    public Integer getOptionalIdentifier() {
        return optionalIdentifier;
    }

    /**
     * Returns a string description of the processing step the reported problem occurred in.
     *
     * @return instance of String
     */
    public String getProcessingStepDescription() {
        return processingStepDescription;
    }

    /**
     * Returns a string description of the reported problem.
     *
     * @return instance of String
     */
    public String getErrorDescription() {
        return errorDescription;
    }

}
