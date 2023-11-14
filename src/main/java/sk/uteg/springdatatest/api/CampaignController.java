package sk.uteg.springdatatest.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.uteg.springdatatest.api.exception.CampaignNotFoundException;
import sk.uteg.springdatatest.api.model.CampaignSummary;
import sk.uteg.springdatatest.api.service.CampaignService;

import java.util.UUID;

@RestController("campaign")
public class CampaignController {

    @Autowired
    CampaignService service;

    @GetMapping("/summary/{uuid}")
    public ResponseEntity<CampaignSummary> getSummary(@PathVariable UUID uuid) {
        return new ResponseEntity<>(this.service.getSummary(uuid), HttpStatus.OK);
    }

    @ExceptionHandler(value = CampaignNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleCampaignNotFoundException(){}
}
