package org.broadinstitute.sting.gatk.walkers.genotyper;

import org.broadinstitute.sting.utils.*;
import org.broadinstitute.sting.utils.pileup.*;
import org.broadinstitute.sting.gatk.refdata.RefMetaDataTracker;
import org.broadinstitute.sting.gatk.refdata.rodDbSNP;
import org.broadinstitute.sting.gatk.contexts.*;
import org.broadinstitute.sting.gatk.contexts.variantcontext.*;

import java.util.*;

public class SimpleIndelCalculationModel extends GenotypeCalculationModel {

    protected SimpleIndelCalculationModel() {}

    public VariantCallContext callLocus(RefMetaDataTracker tracker, char ref, GenomeLoc loc, Map<String, StratifiedAlignmentContext> contexts, DiploidGenotypePriors priors) {

        return null;

        //VariantContext vc = new MutableVariantContext("UG_indel_call", loc, alleles, genotypes, phredScaledConfidence/10.0, null, attributes);
        //return new VariantCallContext(vc, phredScaledConfidence >= CONFIDENCE_THRESHOLD);
    }
}