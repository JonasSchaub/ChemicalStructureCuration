package de.unijena.cheminf.curation;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.smiles.SmiFlavor;
import org.openscience.cdk.smiles.SmilesGenerator;
import java.util.BitSet;
import java.util.LinkedList;
import java.util.Objects;

public class ChemicalStructureCurationPipeline {

    public static void preprocessAtomContainer(IAtomContainer anAtomContainer) {
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of IAtomContainer) is null");
        //
        try {
            ChemicalStructureCurationUtils.addImplicitHydrogens(anAtomContainer);
        } catch (CDKException anException) {
            //TODO
        }
    }

    public static void preprocessAtomContainerSet(IAtomContainerSet anAtomContainerSet) {
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of IAtomContainerSet) is null");
        //
        for (IAtomContainer tmpAtomContainer :
                anAtomContainerSet.atomContainers()) {
            ChemicalStructureCurationPipeline.preprocessAtomContainer(tmpAtomContainer);
        }
    }

    public static void checkForCorrectValencies(IAtomContainerSet anAtomContainerSet) {
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of IAtomContainerSet) is null");
        //
        ValenceListContainer tmpValencyContainer = new ValenceListContainer();
        BitSet tmpCorrectValenciesBitSet = new BitSet();
        int tmpNumberOfFailedValenceChecks = 0;
        int tmpIntervalFailedValenceChecksCount = 0;
        int tmpIterator = 0;
        for (IAtomContainer tmpAtomContainer :
                anAtomContainerSet.atomContainers()) {
            if (ChemicalStructureCurationUtils.hasCorrectValencies(tmpAtomContainer, tmpValencyContainer)) {
                tmpCorrectValenciesBitSet.set(tmpIterator);
            } else {
                //false is default
                tmpNumberOfFailedValenceChecks++;
                tmpIntervalFailedValenceChecksCount++;
            }
            tmpIterator++;
            /*if (tmpIterator % 10000 == 0) {
                System.out.println(tmpIntervalFailedValenceChecksCount);
                tmpIntervalFailedValenceChecksCount = 0;
            }*/
        }
        //System.out.println(tmpIntervalFailedValenceChecksCount);
        //System.out.println("BitArray of valency check:");
        //System.out.println(Arrays.toString(tmpCorrectValenciesArray));
        System.out.println();
        System.out.println(tmpNumberOfFailedValenceChecks + "/" + anAtomContainerSet.getAtomContainerCount() + " Molecules failed the valency check!");
        //System.out.println(!tmpValencyCheckFailed ? "No molecule failed valency check!" : "A molecule failed the valency check!");
        System.out.println();
        SmilesGenerator tmpSmilesGenerator = new SmilesGenerator(SmiFlavor.Unique);
        int tmpStartPoint = 0;
        String tmpIdentifierString;
        String tmpSmilesString;
        //for (int i = 0; i < 5; i++) {
        for (int i = 0; i < anAtomContainerSet.getAtomContainerCount(); i++) {
            if (i >= tmpNumberOfFailedValenceChecks)
                break;
            int tmpIndex = tmpCorrectValenciesBitSet.nextClearBit(tmpStartPoint);
            tmpIdentifierString = anAtomContainerSet.getAtomContainer(tmpIndex).getProperty("ID");
            tmpSmilesString = anAtomContainerSet.getAtomContainer(tmpIndex).getProperty("SMILES");
            //System.out.printf("%s\t%s\n", tmpIdentifierString, tmpSmilesString);
            //System.out.printf("%d\t%s\t%s\n", i, tmpIdentifierString, tmpSmilesString);
            /*
            try {

                System.out.printf("%s\t%s\n", anIdentifierList.get(tmpIndex), tmpSmilesGenerator.create(anAtomContainerSet.getAtomContainer(tmpIndex)));
            } catch (CDKException e) {
            }
             */
            tmpStartPoint = tmpIndex + 1;
        }
    }

}
