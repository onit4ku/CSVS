package org.babelomics.pvs.server;


import com.wordnik.swagger.annotations.Api;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/variants")
@Api(value = "variants", description = "Variants")
@Produces(MediaType.APPLICATION_JSON)
public class VariantsWSServer{
//public class VariantsWSServer extends PVSWSServer {

//    private PVSVariantMongoDBAdaptor variantMongoDbAdaptor;
//    private PVSStudyMongoDBAdaptor studyMongoDBAdaptor;
//
//
//    public VariantsWSServer(@DefaultValue("") @PathParam("version") String version, @Context UriInfo uriInfo, @Context HttpServletRequest hsr)
//            throws IOException, IllegalOpenCGACredentialsException {
//        super(version, uriInfo, hsr);
//
//        variantMongoDbAdaptor = new PVSVariantMongoDBAdaptor(credentials);
//        studyMongoDBAdaptor = new PVSStudyMongoDBAdaptor(credentials);
//    }
//
//    @GET
//    @Path("/fetch")
//    @Produces("application/json")
//    @ApiOperation(value = "Get Variants By Region")
//    public Response getVariantsByRegion(@ApiParam(value = "regions") @QueryParam("regions") @DefaultValue("") String regionId,
//                                        @ApiParam(value = "limit") @QueryParam("limit") @DefaultValue("10") int limit,
//                                        @ApiParam(value = "skip") @QueryParam("skip") @DefaultValue("0") int skip,
//                                        @ApiParam(value = "studies") @QueryParam("studies") String studies,
//                                        @ApiParam(value = "diseases") @QueryParam("diseases") String diseases,
//                                        @ApiParam(value = "phenotypes") @QueryParam("phenotypes") String phenotypes,
//                                        @ApiParam(value = "csv") @QueryParam("csv") @DefaultValue("false") boolean csv
//    ) {
//
//        queryOptions.put("merge", true);
//
//        if (!csv) {
//            queryOptions.put("limit", limit);
//            queryOptions.put("skip", skip);
//        }
//
//
//        // Parse the provided regions. The total size of all regions together
//        // can't excede 1 million positions
//        int regionsSize = 0;
//        List<Region> regions = new ArrayList<>();
//
//        if (regionId.length() > 0) {
//            String[] regSplits = regionId.split(",");
//            for (String s : regSplits) {
//                Region r = Region.parseRegion(s);
//                regions.add(r);
//                regionsSize += r.getEnd() - r.getStart();
//            }
//        }
//
//        List<StudyElement> studyElements = new ArrayList<>();
//        List<StudyElement> staticStudyElements = new ArrayList<>();
//        QueryOptions qo = new QueryOptions();
//
//        QueryResult<BasicDBObject> allStudies = studyMongoDBAdaptor.getAllFileId(qo);
//
//        for (BasicDBObject study : allStudies.getResult()) {
//            String fid = study.getString("fid");
//            StudyElement se = new StudyElement(fid);
//
//            se.setStaticStudy(((BasicDBObject) study.get("meta")).getString("sta").equalsIgnoreCase("false"));
//            se.setNumSamples(((BasicDBObject) study.get("st")).getInt("nSamp"));
//
//            if (!se.getStaticStudy()) {
//                staticStudyElements.add(se);
//            } else {
//                studyElements.add(se);
//            }
//        }
//
//        List<StudyElement> finalStudyElements = new ArrayList<>();
//
//        if (studies != null) {
//            String[] studiesList = studies.trim().split(",");
//            for (StudyElement fid : studyElements) {
//                if (checkFileIdStudies(fid, studiesList)) {
//                    finalStudyElements.add(fid);
//                }
//            }
//        }
//
//        if (diseases != null) {
//            String[] diseasesList = diseases.trim().split(",");
//
//            if (finalStudyElements.isEmpty()) {
//                for (StudyElement fid : studyElements) {
//                    if (checkFileIdDiseases(fid, diseasesList)) {
//                        finalStudyElements.add(fid);
//                    }
//                }
//            } else {
//                Iterator<StudyElement> it = finalStudyElements.listIterator();
//                while (it.hasNext()) {
//                    StudyElement fid = it.next();
//                    if (!checkFileIdDiseases(fid, diseasesList)) {
//                        it.remove();
//                    }
//                }
//            }
//        }
//
//        if (phenotypes != null) {
//            String[] phenotypesList = phenotypes.trim().split(",");
//            if (finalStudyElements.isEmpty()) {
//                for (StudyElement fid : studyElements) {
//                    if (checkFileIdPhenotypes(fid, phenotypesList)) {
//                        finalStudyElements.add(fid);
//                    }
//                }
//            } else {
//                Iterator<StudyElement> it = finalStudyElements.listIterator();
//                while (it.hasNext()) {
//                    StudyElement fid = it.next();
//                    if (!checkFileIdPhenotypes(fid, phenotypesList)) {
//                        it.remove();
//                    }
//                }
//            }
//        }
//
//        List<String> aux = new ArrayList<>(finalStudyElements.size());
//        List<String> staticStudies = new ArrayList<>(finalStudyElements.size());
//
//        int totalSamples = 0;
//        for (StudyElement se : finalStudyElements) {
//            aux.add(se.toString());
//            totalSamples += se.getNumSamples();
//        }
//
//        for (StudyElement se : staticStudyElements) {
//            staticStudies.add(se.getStudy() + "_" + se.toString());
//        }
//
//        BasicDBObject sort = new BasicDBObject("chr", 1).append("start", 1);
//        queryOptions.put("sort", sort);
//        if (regionsSize <= 1000000) {
//
//            List<QueryResult> allVariantsByRegionList = variantMongoDbAdaptor.getAllVariantsByRegionListAndFileIds(regions, aux, queryOptions);
//
//            finalStudyElements.addAll(staticStudyElements);
//            removeStudies(allVariantsByRegionList, finalStudyElements);
//
//            transformVariants(allVariantsByRegionList, staticStudies, totalSamples);
//
//            return createOkResponse(allVariantsByRegionList);
//        } else {
//            return createErrorResponse("The total size of all regions provided can't exceed 1 million positions. ");
//        }
//    }
//
//    private void removeStudies(List<QueryResult> allVariantsByRegionList, List<StudyElement> finalStudyElements) {
//
//        List<String> ids = new ArrayList<>(finalStudyElements.size());
//        for (StudyElement se : finalStudyElements) {
//            ids.add((se.getStudy() + "_" + se.toString()).toUpperCase());
//        }
//
//        for (QueryResult qr : allVariantsByRegionList) {
//            List<Variant> variantList = qr.getResult();
//
//            for (Variant v : variantList) {
//                Map<String, VariantSourceEntry> files = v.getSourceEntries();
//
//                for (Iterator<Map.Entry<String, VariantSourceEntry>> it = files.entrySet().iterator(); it.hasNext(); ) {
//                    Map.Entry<String, VariantSourceEntry> entry = it.next();
//                    VariantSourceEntry vse = entry.getValue();
//
//                    if (!ids.contains(entry.getKey().toUpperCase())) {
//                        it.remove();
//                    }
//                }
//            }
//        }
//    }
//
//    private boolean checkFileIdStudies(StudyElement fid, String[] studiesList) {
//        for (String study : studiesList) {
//            if (fid.getStudy().equalsIgnoreCase(study))
//                return true;
//        }
//        return false;
//    }
//
//    private boolean checkFileIdDiseases(StudyElement fid, String[] diseasesList) {
//        for (String disease : diseasesList) {
//            if (fid.getDisease().equalsIgnoreCase(disease))
//                return true;
//        }
//        return false;
//    }
//
//    private boolean checkFileIdPhenotypes(StudyElement fid, String[] phenotypesList) {
//        for (String phenotype : phenotypesList) {
//            if (fid.getPhenotype().equalsIgnoreCase(phenotype))
//                return true;
//        }
//        return false;
//    }
//
//    private void transformVariants(List<QueryResult> allVariantsByRegionList, List<String> staticStudies, int totalSamples) {
//        for (QueryResult qr : allVariantsByRegionList) {
//            List<Variant> variantList = qr.getResult();
//
//            for (Variant v : variantList) {
//                combineFiles(v, staticStudies, totalSamples);
//            }
//        }
//    }
//
//    private void combineFiles(Variant variant, List<String> staticStudies, int totalSamples) {
//        Map<String, VariantSourceEntry> map = new HashMap<>();
//        VariantStats mafStats = new VariantStats();
//        VariantSourceEntry mafVSE = new VariantSourceEntry("MAF", "MAF");
//        mafVSE.setStats(mafStats);
//
//        for (Map.Entry<String, VariantSourceEntry> entry : variant.getSourceEntries().entrySet()) {
//            VariantSourceEntry avf = entry.getValue();
//            if (staticStudies.contains(entry.getKey().toUpperCase())) {
//                map.put(avf.getStudyId(), entry.getValue());
//            } else {
//                for (Map.Entry<Genotype, Integer> o : avf.getStats().getGenotypesCount().entrySet()) {
//                    Genotype g = o.getKey();
//                    mafStats.addGenotype(o.getKey(), o.getValue());
//                }
//            }
//
//        }
//        variant.getSourceEntries().clear();
//
//        calculateMAF(mafStats, totalSamples, variant);
//
//        map.put("MAF", mafVSE);
//        variant.getSourceEntries().putAll(map);
//    }
//
//    private void calculateMAF(VariantStats mafStats, int totalSamples, Variant variant) {
//
//        Map<Genotype, Integer> genotypes = mafStats.getGenotypesCount();
//
//        int refCount = 0;
//        int altCount = 0;
//        int total = 0;
//
//        for (Map.Entry<Genotype, Integer> entry : genotypes.entrySet()) {
//
//            Genotype genotype = entry.getKey();
//            Integer count = entry.getValue();
//
//            refCount += (genotype.getAllele(0) == 0 ? 1 : 0) * count;
//            refCount += (genotype.getAllele(1) == 0 ? 1 : 0) * count;
//
//            altCount += (genotype.getAllele(0) == 1 ? 1 : 0) * count;
//            altCount += (genotype.getAllele(1) == 1 ? 1 : 0) * count;
//
//            total += count;
//        }
//        refCount += ((totalSamples - total) * 2);
//
//        for (Map.Entry<Genotype, Integer> entry : genotypes.entrySet()) {
//            Genotype genotype = entry.getKey();
//
//            if (genotype.getAllele(0) == 0 && genotype.getAllele(1) == 0) {
//                Integer count = entry.getValue();
//                count += (totalSamples - total);
//                entry.setValue(count);
//            }
//
//        }
//
//
//        float refMAF = ((float) refCount) / (altCount + refCount);
//        float altMAF = ((float) altCount) / (altCount + refCount);
//        float maf;
//
//        if (refMAF <= altMAF) {
//            maf = refMAF;
//            mafStats.setMafAllele(variant.getReference());
//        } else {
//            maf = altMAF;
//            mafStats.setMafAllele(variant.getAlternate());
//        }
//
//        mafStats.setMaf(maf);
//
//    }
//
//    private class StudyElement {
//        private String study;
//        private String disease;
//        private String phenotype;
//        private Boolean staticStudy;
//        private int numSamples;
//
//        public StudyElement(String fid) {
//            String[] aux = fid.split(PVSMain.SEPARATOR);
//            study = aux[0];
//            disease = aux[1];
//            phenotype = aux[2];
//            numSamples = 0;
//        }
//
//        public String getStudy() {
//            return study;
//        }
//
//        public String getDisease() {
//            return disease;
//        }
//
//        public String getPhenotype() {
//            return phenotype;
//        }
//
//        public Boolean getStaticStudy() {
//            return staticStudy;
//        }
//
//        public void setStaticStudy(Boolean staticStudy) {
//            this.staticStudy = staticStudy;
//        }
//
//        @Override
//        public String toString() {
//            return this.study + PVSMain.SEPARATOR + this.disease + PVSMain.SEPARATOR + this.phenotype;
//        }
//
//        public int getNumSamples() {
//            return numSamples;
//        }
//
//        public void setNumSamples(int numSamples) {
//            this.numSamples = numSamples;
//        }
//    }
}
