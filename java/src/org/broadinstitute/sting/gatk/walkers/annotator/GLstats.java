package org.broadinstitute.sting.gatk.walkers.annotator;

import org.broad.tribble.util.variantcontext.Genotype;
import org.broad.tribble.util.variantcontext.VariantContext;
import org.broad.tribble.vcf.VCFHeaderLineType;
import org.broad.tribble.vcf.VCFInfoHeaderLine;
import org.broadinstitute.sting.gatk.contexts.AlignmentContext;
import org.broadinstitute.sting.gatk.contexts.ReferenceContext;
import org.broadinstitute.sting.gatk.refdata.RefMetaDataTracker;
import org.broadinstitute.sting.gatk.walkers.annotator.interfaces.*;
import org.broadinstitute.sting.utils.MathUtils;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: rpoplin
 * Date: 5/16/11
 */

// A set of annotations calculated directly from the GLs
public class GLstats implements InfoFieldAnnotation {

    private static final int MIN_SAMPLES = 10;

    public Map<String, Object> annotate(RefMetaDataTracker tracker, ReferenceContext ref, Map<String, AlignmentContext> stratifiedContexts, VariantContext vc) {

        final Map<String, Genotype> genotypes = vc.getGenotypes();
        if ( genotypes == null || genotypes.size() < MIN_SAMPLES || !vc.isBiallelic() )
            return null;

        double refCount = 0.0;
        double hetCount = 0.0;
        double homCount = 0.0;
        int N = 0; // number of samples that have likelihoods
        for ( final Map.Entry<String, Genotype> genotypeMap : genotypes.entrySet() ) {
            Genotype g = genotypeMap.getValue();
            if ( g.isNoCall() )
                continue;

            N++;
            final double[] normalizedLikelihoods = MathUtils.normalizeFromLog10( g.getLikelihoods().getAsVector() );
            refCount += normalizedLikelihoods[0];
            hetCount += normalizedLikelihoods[1];
            homCount += normalizedLikelihoods[2];
        }

        final double p = ( 2.0 * refCount + hetCount ) / ( 2.0 * (refCount + hetCount + homCount) ); // expected reference allele frequency
        final double q = 1.0 - p; // expected alternative allele frequency
        final double F = 1.0 - ( hetCount / ( 2.0 * p * q * (double)N ) ); // inbreeding coefficient

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(getKeyNames().get(0), String.format("%.4f", F));
        return map;
    }

    public List<String> getKeyNames() { return Arrays.asList("InbreedingCoeff"); }

    public List<VCFInfoHeaderLine> getDescriptions() { return Arrays.asList(new VCFInfoHeaderLine("InbreedingCoeff", 1, VCFHeaderLineType.Float, "Inbreeding coefficient as estimated from the genotype likelihoods per-sample when compared against the Hardy-Weinberg expectation")); }
}