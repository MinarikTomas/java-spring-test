package sk.uteg.springdatatest.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sk.uteg.springdatatest.db.model.Campaign;

import java.math.BigDecimal;
import java.util.UUID;

public interface CampaignRepository extends JpaRepository<Campaign, UUID> {
    @Query("SELECT COUNT(*) FROM  Feedback f WHERE f.campaign.id = :campaign_id")
    long getNumberOfFeedbacks(@Param("campaign_id") UUID uuid);

    @Query("SELECT avg(a.ratingValue) FROM Answer a WHERE a.question.id = :question_id")
    BigDecimal getAverageQuestionRating(@Param("question_id") UUID uuid);

    @Query(value = "SELECT COUNT(*) FROM \"answer_selected_option\" WHERE \"option_id\"=:option_id",
            nativeQuery = true)
    int getOptionOccurrences(@Param("option_id") UUID uuid);
}