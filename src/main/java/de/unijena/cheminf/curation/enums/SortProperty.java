package de.unijena.cheminf.curation.enums;

import de.unijena.cheminf.curation.reporter.ReportDataObject;

public enum SortProperty {
    EXTERNAL_IDENTIFIER(java.util.Comparator.comparing(ReportDataObject::getExternalIdentifier)),
    PROCESSING_STEP_ID(java.util.Comparator.comparing(ReportDataObject::getProcessingStepIdentifier)),
    ERROR_CODE(java.util.Comparator.comparing(ReportDataObject::getErrorCode));

    private final java.util.Comparator<ReportDataObject> comparator;

    SortProperty(java.util.Comparator<ReportDataObject> comparator) {
        this.comparator = comparator;
    }

    public java.util.Comparator<ReportDataObject> getComparator() {
        return comparator;
    }
}
