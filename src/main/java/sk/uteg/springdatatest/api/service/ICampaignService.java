package sk.uteg.springdatatest.api.service;

import sk.uteg.springdatatest.api.model.CampaignSummary;

import java.util.UUID;

public interface ICampaignService {
    CampaignSummary getSummary(UUID uuid);
}
