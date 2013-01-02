package org.broadinstitute.sting.gatk.walkers.annotator;

import org.broadinstitute.sting.WalkerTest;
import org.broadinstitute.sting.utils.exceptions.UserException;
import org.testng.annotations.Test;

import java.util.Arrays;

public class VariantAnnotatorIntegrationTest extends WalkerTest {

    public static String baseTestString() {
        return "-T VariantAnnotator -R " + b36KGReference + " --no_cmdline_in_header -o %s";
    }

    @Test
    public void testHasAnnotsNotAsking1() {
        WalkerTestSpec spec = new WalkerTestSpec(
                baseTestString() + " --variant " + privateTestDir + "vcfexample2.vcf -I " + validationDataLocation + "low_coverage_CEU.chr1.10k-11k.bam -L 1:10,020,000-10,021,000", 1,
                Arrays.asList("360610e4990860bb5c45249b8ac31e5b"));
        executeTest("test file has annotations, not asking for annotations, #1", spec);
    }

    @Test
    public void testHasAnnotsNotAsking2() {
        WalkerTestSpec spec = new WalkerTestSpec(
                baseTestString() + " --variant " + privateTestDir + "vcfexample3.vcf -I " + validationDataLocation + "NA12878.1kg.p2.chr1_10mb_11_mb.SLX.bam -L 1:10,000,000-10,050,000", 1,
                Arrays.asList("d69a3c92a0e8f44e09e7377e3eaed4e8"));
        executeTest("test file has annotations, not asking for annotations, #2", spec);
    }

    @Test
    public void testHasAnnotsAsking1() {
        WalkerTestSpec spec = new WalkerTestSpec(
                baseTestString() + " -G Standard --variant " + privateTestDir + "vcfexample2.vcf -I " + validationDataLocation + "low_coverage_CEU.chr1.10k-11k.bam -L 1:10,020,000-10,021,000", 1,
                Arrays.asList("a127623a26bac4c17c9df491e170ed88"));
        executeTest("test file has annotations, asking for annotations, #1", spec);
    }

    @Test
    public void testHasAnnotsAsking2() {
        WalkerTestSpec spec = new WalkerTestSpec(
                baseTestString() + " -G Standard --variant " + privateTestDir + "vcfexample3.vcf -I " + validationDataLocation + "NA12878.1kg.p2.chr1_10mb_11_mb.SLX.bam -L 1:10,000,000-10,050,000", 1,
                Arrays.asList("13e24e6b9dfa241df5baa2c3f53415b9"));
        executeTest("test file has annotations, asking for annotations, #2", spec);
    }

    @Test
    public void testNoAnnotsNotAsking1() {
        WalkerTestSpec spec = new WalkerTestSpec(
                baseTestString() + " --variant " + privateTestDir + "vcfexample2empty.vcf -I " + validationDataLocation + "low_coverage_CEU.chr1.10k-11k.bam -L 1:10,020,000-10,021,000", 1,
                Arrays.asList("540a9be8a8cb85b0f675fea1184bf78c"));
        executeTest("test file doesn't have annotations, not asking for annotations, #1", spec);
    }

    @Test
    public void testNoAnnotsNotAsking2() {
        // the genotype annotations in this file are actually out of order.  If you don't parse the genotypes
        // they don't get reordered.  It's a good test of the genotype ordering system.
        WalkerTestSpec spec = new WalkerTestSpec(
                baseTestString() + " --variant " + privateTestDir + "vcfexample3empty.vcf -I " + validationDataLocation + "NA12878.1kg.p2.chr1_10mb_11_mb.SLX.bam -L 1:10,000,000-10,050,000", 1,
                Arrays.asList("f900e65b65ff0f9d9eb0891ef9b28c73"));
        executeTest("test file doesn't have annotations, not asking for annotations, #2", spec);
    }

    @Test
    public void testNoAnnotsAsking1() {
        WalkerTestSpec spec = new WalkerTestSpec(
                baseTestString() + " -G Standard --variant " + privateTestDir + "vcfexample2empty.vcf -I " + validationDataLocation + "low_coverage_CEU.chr1.10k-11k.bam -L 1:10,020,000-10,021,000", 1,
                Arrays.asList("07cb4d427235878aeec0066d7d298e54"));
        executeTest("test file doesn't have annotations, asking for annotations, #1", spec);
    }

    @Test
    public void testNoAnnotsAsking2() {
        WalkerTestSpec spec = new WalkerTestSpec(
                baseTestString() + " -G Standard --variant " + privateTestDir + "vcfexample3empty.vcf -I " + validationDataLocation + "NA12878.1kg.p2.chr1_10mb_11_mb.SLX.bam -L 1:10,000,000-10,050,000", 1,
                Arrays.asList("e579097677d5e56a5776151251947961"));
        executeTest("test file doesn't have annotations, asking for annotations, #2", spec);
    }

    @Test
    public void testExcludeAnnotations() {
        WalkerTestSpec spec = new WalkerTestSpec(
                baseTestString() + " -G Standard -XA FisherStrand -XA ReadPosRankSumTest --variant " + privateTestDir + "vcfexample2empty.vcf -I " + validationDataLocation + "low_coverage_CEU.chr1.10k-11k.bam -L 1:10,020,000-10,021,000", 1,
                Arrays.asList("348314945436ace71ce6b1a52559d9ee"));
        executeTest("test exclude annotations", spec);
    }

    @Test
    public void testOverwritingHeader() {
        WalkerTestSpec spec = new WalkerTestSpec(
                baseTestString() + " -G Standard --variant " + privateTestDir + "vcfexample4.vcf -I " + validationDataLocation + "NA12878.1kg.p2.chr1_10mb_11_mb.SLX.bam -L 1:10,001,292", 1,
                Arrays.asList("ae7930e37a66c0aa4cfe0232736864fe"));
        executeTest("test overwriting header", spec);
    }

    @Test
    public void testNoReads() {
        WalkerTestSpec spec = new WalkerTestSpec(
                baseTestString() + " -G Standard --variant " + privateTestDir + "vcfexample3empty.vcf -L " + privateTestDir + "vcfexample3empty.vcf", 1,
                Arrays.asList("a0ba056c2625033e5e859fd6bcec1256"));
        executeTest("not passing it any reads", spec);
    }

    @Test
    public void testDBTagWithDbsnp() {
        WalkerTestSpec spec = new WalkerTestSpec(
                baseTestString() + " --dbsnp " + b36dbSNP129 + " -G Standard --variant " + privateTestDir + "vcfexample3empty.vcf -L " + privateTestDir + "vcfexample3empty.vcf", 1,
                Arrays.asList("0be7da17340111a94e8581ee3808c88a"));
        executeTest("getting DB tag with dbSNP", spec);
    }

    @Test
    public void testMultipleIdsWithDbsnp() {
        WalkerTestSpec spec = new WalkerTestSpec(
                baseTestString() + " --alwaysAppendDbsnpId --dbsnp " + b36dbSNP129 + " -G Standard --variant " + privateTestDir + "vcfexample3withIDs.vcf -L " + privateTestDir + "vcfexample3withIDs.vcf", 1,
                Arrays.asList("e40e625302a496ede42eed61c2ce524b"));
        executeTest("adding multiple IDs with dbSNP", spec);
    }

    @Test
    public void testDBTagWithHapMap() {
        WalkerTestSpec spec = new WalkerTestSpec(
                baseTestString() + " --comp:H3 " + privateTestDir + "fakeHM3.vcf -G Standard --variant " + privateTestDir + "vcfexample3empty.vcf -L " + privateTestDir + "vcfexample3empty.vcf", 1,
                Arrays.asList("cb50876477d3e035b6eda5d720d7ba8d"));
        executeTest("getting DB tag with HM3", spec);
    }

    @Test
    public void testNoQuals() {
        WalkerTestSpec spec = new WalkerTestSpec(
                baseTestString() + " --variant " + privateTestDir + "noQual.vcf -I " + validationDataLocation + "NA12878.1kg.p2.chr1_10mb_11_mb.SLX.bam -L " + privateTestDir + "noQual.vcf -A QualByDepth", 1,
                Arrays.asList("458412261d61797d39f802c1e03d63f6"));
        executeTest("test file doesn't have QUALs", spec);
    }

    @Test
    public void testUsingExpression() {
        WalkerTestSpec spec = new WalkerTestSpec(
                baseTestString() + " --resource:foo " + privateTestDir + "targetAnnotations.vcf -G Standard --variant " + privateTestDir + "vcfexample3empty.vcf -E foo.AF -L " + privateTestDir + "vcfexample3empty.vcf", 1,
                Arrays.asList("39defa8108dca9fa3e54b22a7da43f77"));
        executeTest("using expression", spec);
    }

    @Test
    public void testUsingExpressionWithID() {
        WalkerTestSpec spec = new WalkerTestSpec(
                baseTestString() + " --resource:foo " + privateTestDir + "targetAnnotations.vcf -G Standard --variant " + privateTestDir + "vcfexample3empty.vcf -E foo.ID -L " + privateTestDir + "vcfexample3empty.vcf", 1,
                Arrays.asList("a917edd58a0c235e9395bfc2d2020a8c"));
        executeTest("using expression with ID", spec);
    }

    @Test
    public void testTabixAnnotationsAndParallelism() {
        final String MD5 = "99938d1e197b8f10c408cac490a00a62";
        for ( String file : Arrays.asList("CEU.exon.2010_03.sites.vcf", "CEU.exon.2010_03.sites.vcf.gz")) {
            WalkerTestSpec spec = new WalkerTestSpec(
                    baseTestString() + " -A HomopolymerRun --variant:vcf " + validationDataLocation + file + " -L " + validationDataLocation + "CEU.exon.2010_03.sites.vcf --no_cmdline_in_header", 1,
                    Arrays.asList(MD5));
            executeTest("Testing lookup vcf tabix vs. vcf tribble", spec);
        }

        WalkerTestSpec spec = new WalkerTestSpec(
                baseTestString() + " -A HomopolymerRun -nt 2 --variant:vcf " + validationDataLocation + "CEU.exon.2010_03.sites.vcf -L " + validationDataLocation + "CEU.exon.2010_03.sites.vcf --no_cmdline_in_header", 1,
                Arrays.asList(MD5));

        executeTest("Testing lookup vcf tabix vs. vcf tribble plus parallelism", spec);
    }

    @Test
    public void testSnpEffAnnotations() {
        WalkerTestSpec spec = new WalkerTestSpec(
            "-T VariantAnnotator -R " + hg19Reference + " --no_cmdline_in_header -o %s -A SnpEff --variant " +
            validationDataLocation + "1kg_exomes_unfiltered.AFR.unfiltered.vcf --snpEffFile  " + validationDataLocation +
            "snpEff2.0.5.AFR.unfiltered.vcf -L 1:1-1,500,000 -L 2:232,325,429",
            1,
            Arrays.asList("d9291845ce5a8576898d293a829a05b7")
        );
        executeTest("Testing SnpEff annotations", spec);
    }

    @Test
    public void testSnpEffAnnotationsUnsupportedVersion() {
        WalkerTestSpec spec = new WalkerTestSpec(
            "-T VariantAnnotator -R " + hg19Reference + " --no_cmdline_in_header -o %s -A SnpEff --variant " +
            validationDataLocation + "1kg_exomes_unfiltered.AFR.unfiltered.vcf --snpEffFile  " + validationDataLocation +
            "snpEff.AFR.unfiltered.unsupported.version.vcf -L 1:1-1,500,000",
            1,
            UserException.class
        );
        executeTest("Testing SnpEff annotations (unsupported version)", spec);
    }

    @Test
    public void testTDTAnnotation() {
        final String MD5 = "427dfdc665359b67eff210f909ebf8a2";
        WalkerTestSpec spec = new WalkerTestSpec(
                "-T VariantAnnotator -R " + b37KGReference + " -A TransmissionDisequilibriumTest --variant:vcf " + privateTestDir + "ug.random50000.subset300bp.chr1.family.vcf" +
                        " -L " + privateTestDir + "ug.random50000.subset300bp.chr1.family.vcf --no_cmdline_in_header -ped " + privateTestDir + "ug.random50000.family.ped -o %s", 1,
                Arrays.asList(MD5));
        executeTest("Testing TDT annotation ", spec);
    }


    @Test
    public void testChromosomeCountsPed() {
        final String MD5 = "6b5cbedf4a8b3385edf128d81c8a46f2";
        WalkerTestSpec spec = new WalkerTestSpec(
                "-T VariantAnnotator -R " + b37KGReference + " -A ChromosomeCounts --variant:vcf " + privateTestDir + "ug.random50000.subset300bp.chr1.family.vcf" +
                        " -L " + privateTestDir + "ug.random50000.subset300bp.chr1.family.vcf --no_cmdline_in_header -ped " + privateTestDir + "ug.random50000.family.ped -o %s", 1,
                Arrays.asList(MD5));
        executeTest("Testing ChromosomeCounts annotation with PED file", spec);
    }

    @Test
    public void testInbreedingCoeffPed() {
        final String MD5 = "159a771c1deaeffb786097e106943893";
        WalkerTestSpec spec = new WalkerTestSpec(
                "-T VariantAnnotator -R " + b37KGReference + " -A InbreedingCoeff --variant:vcf " + privateTestDir + "ug.random50000.subset300bp.chr1.family.vcf" +
                        " -L " + privateTestDir + "ug.random50000.subset300bp.chr1.family.vcf --no_cmdline_in_header -ped " + privateTestDir + "ug.random50000.family.ped -o %s", 1,
                Arrays.asList(MD5));
        executeTest("Testing InbreedingCoeff annotation with PED file", spec);
    }

}
