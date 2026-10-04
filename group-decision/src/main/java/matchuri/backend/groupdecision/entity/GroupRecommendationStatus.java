package matchuri.backend.groupdecision.entity;

public enum GroupRecommendationStatus {
    PREPARING,
    OPEN,
    FINALIZED,
    REROLLED_WITH_SKIP,
    REROLLED_WITHOUT_SKIP,
    CANCELED,
    EXPIRED,
    FAILED
}
