package de.unijena.cheminf.filter;

import org.openscience.cdk.interfaces.IAtomContainer;

/**
 * IFilter interface.
 */
public interface IFilter {

    /**
     * Checks whether the filter applies on a given IAtomContainer instance.
     * Returns true, if the given atom container gets filtered.
     *
     * @param anAtomContainer IAtomContainer instance to be checked
     * @return true, if the filter applies on the given atom container
     * @throws NullPointerException if the given IAtomContainer instance is null
     */
    boolean isFiltered(IAtomContainer anAtomContainer) throws NullPointerException;

}
