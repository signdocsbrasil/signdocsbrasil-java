package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Result data from a completed step.
 */
public class StepResult {

    @SerializedName("liveness")
    private LivenessResult liveness;

    @SerializedName("match")
    private MatchResult match;

    @SerializedName("otp")
    private OtpResult otp;

    @SerializedName("click")
    private ClickResult click;

    @SerializedName("purposeDisclosure")
    private PurposeDisclosureResult purposeDisclosure;

    @SerializedName("digitalSignature")
    private DigitalSignatureResult digitalSignature;

    @SerializedName("serproIdentity")
    private SerproIdentityResult serproIdentity;

    @SerializedName("geolocation")
    private GeolocationResult geolocation;

    @SerializedName("documentPhotoMatch")
    private DocumentPhotoMatchResult documentPhotoMatch;

    @SerializedName("quality")
    private QualityResult quality;

    @SerializedName("governmentDbValidation")
    private GovernmentDbValidation governmentDbValidation;

    @SerializedName("providerTimestamp")
    private String providerTimestamp;

    public StepResult() {
    }

    public LivenessResult getLiveness() {
        return liveness;
    }

    public void setLiveness(LivenessResult liveness) {
        this.liveness = liveness;
    }

    public MatchResult getMatch() {
        return match;
    }

    public void setMatch(MatchResult match) {
        this.match = match;
    }

    public OtpResult getOtp() {
        return otp;
    }

    public void setOtp(OtpResult otp) {
        this.otp = otp;
    }

    public ClickResult getClick() {
        return click;
    }

    public void setClick(ClickResult click) {
        this.click = click;
    }

    public DigitalSignatureResult getDigitalSignature() {
        return digitalSignature;
    }

    public void setDigitalSignature(DigitalSignatureResult digitalSignature) {
        this.digitalSignature = digitalSignature;
    }

    public String getProviderTimestamp() {
        return providerTimestamp;
    }

    public void setProviderTimestamp(String providerTimestamp) {
        this.providerTimestamp = providerTimestamp;
    }

    public PurposeDisclosureResult getPurposeDisclosure() {
        return purposeDisclosure;
    }

    public void setPurposeDisclosure(PurposeDisclosureResult purposeDisclosure) {
        this.purposeDisclosure = purposeDisclosure;
    }

    public SerproIdentityResult getSerproIdentity() {
        return serproIdentity;
    }

    public void setSerproIdentity(SerproIdentityResult serproIdentity) {
        this.serproIdentity = serproIdentity;
    }

    public GeolocationResult getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(GeolocationResult geolocation) {
        this.geolocation = geolocation;
    }

    public DocumentPhotoMatchResult getDocumentPhotoMatch() {
        return documentPhotoMatch;
    }

    public void setDocumentPhotoMatch(DocumentPhotoMatchResult documentPhotoMatch) {
        this.documentPhotoMatch = documentPhotoMatch;
    }

    public QualityResult getQuality() {
        return quality;
    }

    public void setQuality(QualityResult quality) {
        this.quality = quality;
    }

    public GovernmentDbValidation getGovernmentDbValidation() {
        return governmentDbValidation;
    }

    public void setGovernmentDbValidation(GovernmentDbValidation governmentDbValidation) {
        this.governmentDbValidation = governmentDbValidation;
    }

    public static class LivenessResult {
        @SerializedName("confidence")
        private double confidence;

        @SerializedName("provider")
        private String provider;

        @SerializedName("captureMode")
        private String captureMode;

        @SerializedName("complianceStandards")
        private List<String> complianceStandards;

        public double getConfidence() {
            return confidence;
        }

        public void setConfidence(double confidence) {
            this.confidence = confidence;
        }

        public String getProvider() {
            return provider;
        }

        public void setProvider(String provider) {
            this.provider = provider;
        }

        public String getCaptureMode() {
            return captureMode;
        }

        public void setCaptureMode(String captureMode) {
            this.captureMode = captureMode;
        }

        public List<String> getComplianceStandards() {
            return complianceStandards;
        }

        public void setComplianceStandards(List<String> complianceStandards) {
            this.complianceStandards = complianceStandards;
        }
    }

    public static class MatchResult {
        @SerializedName("similarity")
        private double similarity;

        @SerializedName("threshold")
        private double threshold;

        public double getSimilarity() {
            return similarity;
        }

        public void setSimilarity(double similarity) {
            this.similarity = similarity;
        }

        public double getThreshold() {
            return threshold;
        }

        public void setThreshold(double threshold) {
            this.threshold = threshold;
        }
    }

    public static class OtpResult {
        @SerializedName("verified")
        private boolean verified;

        @SerializedName("channel")
        private String channel;

        public boolean isVerified() {
            return verified;
        }

        public void setVerified(boolean verified) {
            this.verified = verified;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }
    }

    public static class ClickResult {
        @SerializedName("accepted")
        private boolean accepted;

        @SerializedName("textVersion")
        private String textVersion;

        public boolean isAccepted() {
            return accepted;
        }

        public void setAccepted(boolean accepted) {
            this.accepted = accepted;
        }

        public String getTextVersion() {
            return textVersion;
        }

        public void setTextVersion(String textVersion) {
            this.textVersion = textVersion;
        }
    }

    public static class DigitalSignatureResult {
        @SerializedName("certificateSubject")
        private String certificateSubject;

        @SerializedName("certificateSerial")
        private String certificateSerial;

        @SerializedName("certificateIssuer")
        private String certificateIssuer;

        @SerializedName("algorithm")
        private String algorithm;

        @SerializedName("signedAt")
        private String signedAt;

        @SerializedName("signedPdfHash")
        private String signedPdfHash;

        @SerializedName("signedPdfS3Key")
        private String signedPdfS3Key;

        @SerializedName("signatureFieldName")
        private String signatureFieldName;

        public String getCertificateSubject() {
            return certificateSubject;
        }

        public void setCertificateSubject(String certificateSubject) {
            this.certificateSubject = certificateSubject;
        }

        public String getCertificateSerial() {
            return certificateSerial;
        }

        public void setCertificateSerial(String certificateSerial) {
            this.certificateSerial = certificateSerial;
        }

        public String getCertificateIssuer() {
            return certificateIssuer;
        }

        public void setCertificateIssuer(String certificateIssuer) {
            this.certificateIssuer = certificateIssuer;
        }

        public String getAlgorithm() {
            return algorithm;
        }

        public void setAlgorithm(String algorithm) {
            this.algorithm = algorithm;
        }

        public String getSignedAt() {
            return signedAt;
        }

        public void setSignedAt(String signedAt) {
            this.signedAt = signedAt;
        }

        public String getSignedPdfHash() {
            return signedPdfHash;
        }

        public void setSignedPdfHash(String signedPdfHash) {
            this.signedPdfHash = signedPdfHash;
        }

        public String getSignedPdfS3Key() {
            return signedPdfS3Key;
        }

        public void setSignedPdfS3Key(String signedPdfS3Key) {
            this.signedPdfS3Key = signedPdfS3Key;
        }

        public String getSignatureFieldName() {
            return signatureFieldName;
        }

        public void setSignatureFieldName(String signatureFieldName) {
            this.signatureFieldName = signatureFieldName;
        }
    }

    public static class PurposeDisclosureResult {
        @SerializedName("acknowledged")
        private boolean acknowledged;

        @SerializedName("disclosureTextHash")
        private String disclosureTextHash;

        @SerializedName("disclosureVersion")
        private String disclosureVersion;

        @SerializedName("notificationChannel")
        private String notificationChannel;

        @SerializedName("notificationSentAt")
        private String notificationSentAt;

        public boolean isAcknowledged() {
            return acknowledged;
        }

        public void setAcknowledged(boolean acknowledged) {
            this.acknowledged = acknowledged;
        }

        public String getDisclosureTextHash() {
            return disclosureTextHash;
        }

        public void setDisclosureTextHash(String disclosureTextHash) {
            this.disclosureTextHash = disclosureTextHash;
        }

        public String getDisclosureVersion() {
            return disclosureVersion;
        }

        public void setDisclosureVersion(String disclosureVersion) {
            this.disclosureVersion = disclosureVersion;
        }

        public String getNotificationChannel() {
            return notificationChannel;
        }

        public void setNotificationChannel(String notificationChannel) {
            this.notificationChannel = notificationChannel;
        }

        public String getNotificationSentAt() {
            return notificationSentAt;
        }

        public void setNotificationSentAt(String notificationSentAt) {
            this.notificationSentAt = notificationSentAt;
        }
    }

    public static class SerproIdentityResult {
        @SerializedName("valid")
        private boolean valid;

        @SerializedName("provider")
        private String provider;

        @SerializedName("nameMatch")
        private boolean nameMatch;

        @SerializedName("birthDateMatch")
        private boolean birthDateMatch;

        @SerializedName("biometricMatch")
        private boolean biometricMatch;

        @SerializedName("biometricConfidence")
        private double biometricConfidence;

        @SerializedName("governmentDatabase")
        private GovernmentDatabase governmentDatabase;

        public boolean isValid() {
            return valid;
        }

        public void setValid(boolean valid) {
            this.valid = valid;
        }

        public String getProvider() {
            return provider;
        }

        public void setProvider(String provider) {
            this.provider = provider;
        }

        public boolean isNameMatch() {
            return nameMatch;
        }

        public void setNameMatch(boolean nameMatch) {
            this.nameMatch = nameMatch;
        }

        public boolean isBirthDateMatch() {
            return birthDateMatch;
        }

        public void setBirthDateMatch(boolean birthDateMatch) {
            this.birthDateMatch = birthDateMatch;
        }

        public boolean isBiometricMatch() {
            return biometricMatch;
        }

        public void setBiometricMatch(boolean biometricMatch) {
            this.biometricMatch = biometricMatch;
        }

        public double getBiometricConfidence() {
            return biometricConfidence;
        }

        public void setBiometricConfidence(double biometricConfidence) {
            this.biometricConfidence = biometricConfidence;
        }

        public GovernmentDatabase getGovernmentDatabase() {
            return governmentDatabase;
        }

        public void setGovernmentDatabase(GovernmentDatabase governmentDatabase) {
            this.governmentDatabase = governmentDatabase;
        }
    }

    public static class GeolocationResult {
        @SerializedName("latitude")
        private double latitude;

        @SerializedName("longitude")
        private double longitude;

        @SerializedName("accuracy")
        private Double accuracy;

        @SerializedName("source")
        private String source;

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public Double getAccuracy() {
            return accuracy;
        }

        public void setAccuracy(Double accuracy) {
            this.accuracy = accuracy;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }
    }

    public static class DocumentPhotoMatchResult {
        @SerializedName("documentType")
        private String documentType;

        @SerializedName("extractedFaceHash")
        private String extractedFaceHash;

        @SerializedName("similarity")
        private double similarity;

        @SerializedName("threshold")
        private double threshold;

        @SerializedName("faceExtractionConfidence")
        private double faceExtractionConfidence;

        @SerializedName("biographicValidation")
        private BiographicValidation biographicValidation;

        public String getDocumentType() {
            return documentType;
        }

        public void setDocumentType(String documentType) {
            this.documentType = documentType;
        }

        public String getExtractedFaceHash() {
            return extractedFaceHash;
        }

        public void setExtractedFaceHash(String extractedFaceHash) {
            this.extractedFaceHash = extractedFaceHash;
        }

        public double getSimilarity() {
            return similarity;
        }

        public void setSimilarity(double similarity) {
            this.similarity = similarity;
        }

        public double getThreshold() {
            return threshold;
        }

        public void setThreshold(double threshold) {
            this.threshold = threshold;
        }

        public double getFaceExtractionConfidence() {
            return faceExtractionConfidence;
        }

        public void setFaceExtractionConfidence(double faceExtractionConfidence) {
            this.faceExtractionConfidence = faceExtractionConfidence;
        }

        public BiographicValidation getBiographicValidation() {
            return biographicValidation;
        }

        public void setBiographicValidation(BiographicValidation biographicValidation) {
            this.biographicValidation = biographicValidation;
        }
    }

    public static class BiographicValidation {
        @SerializedName("nameMatch")
        private Boolean nameMatch;

        @SerializedName("cpfMatch")
        private Boolean cpfMatch;

        @SerializedName("birthDateMatch")
        private Boolean birthDateMatch;

        @SerializedName("overallValid")
        private boolean overallValid;

        @SerializedName("matchedFields")
        private List<String> matchedFields;

        @SerializedName("unmatchedFields")
        private List<String> unmatchedFields;

        public Boolean getNameMatch() {
            return nameMatch;
        }

        public void setNameMatch(Boolean nameMatch) {
            this.nameMatch = nameMatch;
        }

        public Boolean getCpfMatch() {
            return cpfMatch;
        }

        public void setCpfMatch(Boolean cpfMatch) {
            this.cpfMatch = cpfMatch;
        }

        public Boolean getBirthDateMatch() {
            return birthDateMatch;
        }

        public void setBirthDateMatch(Boolean birthDateMatch) {
            this.birthDateMatch = birthDateMatch;
        }

        public boolean isOverallValid() {
            return overallValid;
        }

        public void setOverallValid(boolean overallValid) {
            this.overallValid = overallValid;
        }

        public List<String> getMatchedFields() {
            return matchedFields;
        }

        public void setMatchedFields(List<String> matchedFields) {
            this.matchedFields = matchedFields;
        }

        public List<String> getUnmatchedFields() {
            return unmatchedFields;
        }

        public void setUnmatchedFields(List<String> unmatchedFields) {
            this.unmatchedFields = unmatchedFields;
        }
    }

    public static class QualityResult {
        @SerializedName("brightness")
        private double brightness;

        @SerializedName("sharpness")
        private double sharpness;

        @SerializedName("faceAreaRatio")
        private double faceAreaRatio;

        public double getBrightness() {
            return brightness;
        }

        public void setBrightness(double brightness) {
            this.brightness = brightness;
        }

        public double getSharpness() {
            return sharpness;
        }

        public void setSharpness(double sharpness) {
            this.sharpness = sharpness;
        }

        public double getFaceAreaRatio() {
            return faceAreaRatio;
        }

        public void setFaceAreaRatio(double faceAreaRatio) {
            this.faceAreaRatio = faceAreaRatio;
        }
    }

    public enum GovernmentDatabase {
        @SerializedName("SERPRO_DATAVALID")
        SERPRO_DATAVALID,

        @SerializedName("TSE")
        TSE,

        @SerializedName("IDRC")
        IDRC
    }

    public static class GovernmentDbValidation {
        @SerializedName("database")
        private GovernmentDatabase database;

        @SerializedName("validatedAt")
        private String validatedAt;

        @SerializedName("cpfHash")
        private String cpfHash;

        @SerializedName("biometricScore")
        private double biometricScore;

        @SerializedName("cached")
        private boolean cached;

        @SerializedName("cacheVerifySimilarity")
        private Double cacheVerifySimilarity;

        @SerializedName("cacheExpiresAt")
        private String cacheExpiresAt;

        public GovernmentDatabase getDatabase() {
            return database;
        }

        public void setDatabase(GovernmentDatabase database) {
            this.database = database;
        }

        public String getValidatedAt() {
            return validatedAt;
        }

        public void setValidatedAt(String validatedAt) {
            this.validatedAt = validatedAt;
        }

        public String getCpfHash() {
            return cpfHash;
        }

        public void setCpfHash(String cpfHash) {
            this.cpfHash = cpfHash;
        }

        public double getBiometricScore() {
            return biometricScore;
        }

        public void setBiometricScore(double biometricScore) {
            this.biometricScore = biometricScore;
        }

        public boolean isCached() {
            return cached;
        }

        public void setCached(boolean cached) {
            this.cached = cached;
        }

        public Double getCacheVerifySimilarity() {
            return cacheVerifySimilarity;
        }

        public void setCacheVerifySimilarity(Double cacheVerifySimilarity) {
            this.cacheVerifySimilarity = cacheVerifySimilarity;
        }

        public String getCacheExpiresAt() {
            return cacheExpiresAt;
        }

        public void setCacheExpiresAt(String cacheExpiresAt) {
            this.cacheExpiresAt = cacheExpiresAt;
        }
    }
}
