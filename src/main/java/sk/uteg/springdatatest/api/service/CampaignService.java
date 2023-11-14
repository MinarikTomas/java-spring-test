package sk.uteg.springdatatest.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.uteg.springdatatest.api.exception.CampaignNotFoundException;
import sk.uteg.springdatatest.api.model.CampaignSummary;
import sk.uteg.springdatatest.api.model.OptionSummary;
import sk.uteg.springdatatest.api.model.QuestionSummary;
import sk.uteg.springdatatest.api.repository.CampaignRepository;
import sk.uteg.springdatatest.db.model.Campaign;
import sk.uteg.springdatatest.db.model.Option;
import sk.uteg.springdatatest.db.model.Question;
import sk.uteg.springdatatest.db.model.QuestionType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CampaignService implements ICampaignService{


    @Autowired
    CampaignRepository repository;

    /**
     * Create CampaignSummary of the campaign with the given ID.
     * @throws CampaignNotFoundException - If campaign does not exist
     * @param uuid - Campaign ID
     * @return CampaignSummary of the campaign
     */
    public CampaignSummary getSummary(UUID uuid){
        Optional<Campaign> campaign = this.repository.findById(uuid);
        if(campaign.isEmpty()){
            throw new CampaignNotFoundException();
        }

        CampaignSummary summary = new CampaignSummary();
        summary.setTotalFeedbacks(this.repository.getNumberOfFeedbacks(uuid));
        summary.setQuestionSummaries(this.getQuestionSummaries(campaign.get()));
        return summary;
    }

    /**
     * Iterate through the questions of the given campaign
     * and create QuestionSummary for each question.
     * @param campaign - campaign
     * @return List of question summaries
     */
    private List<QuestionSummary> getQuestionSummaries(Campaign campaign){
        List<QuestionSummary> questionSummaries = new ArrayList<>();

        for(Question question: campaign.getQuestions()){
            QuestionSummary questionSummary = new QuestionSummary();
            questionSummary.setName(question.getText());
            questionSummary.setType(question.getType());

            if(question.getType() == QuestionType.RATING){
                questionSummary.setRatingAverage(this.repository.getAverageQuestionRating(question.getId()));
            }else{
                questionSummary.setRatingAverage(new BigDecimal(0));
            }

            questionSummary.setOptionSummaries(this.getOptionSummaries(question));
            questionSummaries.add(questionSummary);
        }
        return questionSummaries;
    }

    /**
     * Iterate through the options of the given question
     * and create OptionSummary for each option.
     * @param question - question
     * @return List of option summaries
     */
    private List<OptionSummary> getOptionSummaries(Question question){
        List<OptionSummary> optionSummaries = new ArrayList<>();
        if(question.getType() == QuestionType.CHOICE){
            for(Option option: question.getOptions()){
                OptionSummary optionSummary = new OptionSummary();
                optionSummary.setText(option.getText());
                optionSummary.setOccurrences(this.repository.getOptionOccurrences(option.getId()));
                optionSummaries.add(optionSummary);
            }
        }
        return optionSummaries;
    }
}
